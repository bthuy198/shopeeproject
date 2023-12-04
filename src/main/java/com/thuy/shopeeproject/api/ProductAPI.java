package com.thuy.shopeeproject.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import com.thuy.shopeeproject.domain.dto.ProductCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.ProductCreateResDTO;
import com.thuy.shopeeproject.domain.dto.ProductDetailResDTO;
import com.thuy.shopeeproject.domain.dto.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.ProductResDTO;
import com.thuy.shopeeproject.domain.dto.ProductUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.ICategoryService;
import com.thuy.shopeeproject.service.IProductService;
import com.thuy.shopeeproject.utils.AppUtils;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllProduct(@Validated ProductFilterReqDTO productFilterReqDTO,
            BindingResult bindingResult,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        new ProductFilterReqDTO().validate(productFilterReqDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        int size = 10;
        int currentPageNumber = productFilterReqDTO.getCurrentPageNumber();

        pageable = PageRequest.of(currentPageNumber, size, Sort.by("productName").ascending());
        Page<ProductResDTO> productResDTOS = productService.findAll(productFilterReqDTO, pageable);

        return new ResponseEntity<>(productResDTOS, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        Product product = optionalProduct.get();

        return new ResponseEntity<>(product.toProductResDTO(), HttpStatus.OK);
    }

    @GetMapping("/{productId}/details")
    public ResponseEntity<?> getProductDetailByProductId(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        Product product = optionalProduct.get();
        List<ProductDetail> productDetails = product.getProductDetails();
        List<ProductDetailResDTO> productDetailResDTOs = new ArrayList<>();
        for (ProductDetail productDetail : productDetails) {
            ProductDetailResDTO productDetailResDTO = productDetail.toProductDetailResDTO();
            productDetailResDTOs.add(productDetailResDTO);
        }

        return new ResponseEntity<>(productDetailResDTOs, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Validated ProductCreateReqDTO productCreateReqDTO,
            BindingResult bindingResult) {

        Optional<Category> categoryOptional = categoryService.findById(productCreateReqDTO.getCategoryId());
        if (!categoryOptional.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Category not found");
        }

        Category category = categoryOptional.get();

        MultipartFile[] files = productCreateReqDTO.getFiles();

        if (files != null && files.length > 0 && !files[0].getOriginalFilename().isEmpty()) {
            new ProductCreateReqDTO().validate(productCreateReqDTO.getFiles(), bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Product product;
        if (files != null && files.length > 0 && !files[0].getOriginalFilename().isEmpty()) {
            product = productService.createProductWithAvatars(productCreateReqDTO, category);
        } else {
            product = productService.createProduct(productCreateReqDTO, category);
        }

        ProductCreateResDTO productCreateResDTO = product.toProductCreateResDTO();
        return new ResponseEntity<>(productCreateResDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
            @ModelAttribute @Validated ProductUpdateReqDTO productUpdateReqDTO,
            BindingResult bindingResult) {

        Optional<Product> optionalProduct = productService.findById(productId);
        Product product = optionalProduct.get();

        if (!optionalProduct.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        Category category = product.getCategory();

        if (productUpdateReqDTO.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryService.findById(productUpdateReqDTO.getCategoryId());
            category = categoryOptional.get();
            if (!optionalProduct.isPresent()) {
                throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this category");
            }
        }

        product.setCategory(category);

        MultipartFile[] files = productUpdateReqDTO.getFiles();

        if (files != null && files.length > 0 && !files[0].getOriginalFilename().isEmpty()) {
            new ProductCreateReqDTO().validate(files, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(productUpdateReqDTO, product);

        if (files != null && files.length > 0 && !files[0].getOriginalFilename().isEmpty()) {
            product = productService.updateProductWithAvatar(productUpdateReqDTO, optionalProduct.get());
        } else {
            product.setId(productId);
            product.setProductAvatars(product.getProductAvatars());
            product.setProductDetails(product.getProductDetails());
            productService.save(product);
        }

        Optional<Product> optionalProductRes = productService.findById(productId);
        Product productRes = optionalProductRes.get();

        return new ResponseEntity<>(productRes.toProductResDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        productService.delete(optionalProduct.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
