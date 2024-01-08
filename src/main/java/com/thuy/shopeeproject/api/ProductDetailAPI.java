package com.thuy.shopeeproject.api;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.product.ProductDetailUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.IProductDetailService;
import com.thuy.shopeeproject.utils.AppUtils;

@RestController
@RequestMapping("api/details")
public class ProductDetailAPI {
    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private AppUtils appUtils;

    @PatchMapping("/{detailId}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> updateProductDetail(@PathVariable Long detailId,
            @Validated ProductDetailUpdateReqDTO productDetailUpdateReqDTO, BindingResult bindingResult) {
        Optional<ProductDetail> optionalProductDetail = productDetailService.findById(detailId);

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(productDetailUpdateReqDTO, optionalProductDetail.get());

        productDetailService.save(optionalProductDetail.get());

        Optional<ProductDetail> result = productDetailService.findById(detailId);
        return new ResponseEntity<>(result.get().toProductDetailResDTO(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{detailId}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Long detailId) {
        Optional<ProductDetail> optionalProductDetail = productDetailService.findById(detailId);
        if (!optionalProductDetail.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product detail");
        }
        return new ResponseEntity<>(optionalProductDetail.get().toProductDetailResDTO(), HttpStatus.OK);
    }

}
