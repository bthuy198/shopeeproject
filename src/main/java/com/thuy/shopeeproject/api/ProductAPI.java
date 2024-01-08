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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import com.thuy.shopeeproject.domain.dto.CartItemReqDTO;
import com.thuy.shopeeproject.domain.dto.CartItemUpdateReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductAvatarDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductCreateResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductDetailResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductAvatar;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.domain.entity.Size;
import com.thuy.shopeeproject.domain.entity.UserAvatar;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.ICartItemService;
import com.thuy.shopeeproject.service.ICartService;
import com.thuy.shopeeproject.service.ICategoryService;
import com.thuy.shopeeproject.service.IProductAvatarService;
import com.thuy.shopeeproject.service.IProductDetailService;
import com.thuy.shopeeproject.service.IProductService;
import com.thuy.shopeeproject.service.ISizeService;
import com.thuy.shopeeproject.utils.AppUtils;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
// @SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private IProductService productService;

    @Autowired
    private IProductAvatarService productAvatarService;

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

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
        int currentPageNumber = 0;
        if (productFilterReqDTO.getCurrentPageNumber() != null) {
            currentPageNumber = productFilterReqDTO.getCurrentPageNumber();
        }

        pageable = PageRequest.of(currentPageNumber, size, Sort.by("productName").ascending());
        Page<ProductResDTO> productResDTOS = productService.findAllProduct(productFilterReqDTO, pageable);

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
    @PreAuthorize("hasAnyAuthority('admin')")
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
    @PreAuthorize("hasAnyAuthority('admin')")
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
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        productService.deleteProduct(optionalProduct.get());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{productId}/avatars")
    public ResponseEntity<?> getListAvatarByProductId(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }
        List<ProductAvatar> productAvatars = optionalProduct.get().getProductAvatars();
        List<ProductAvatarDTO> productAvatarDTOs = new ArrayList<>();
        for (ProductAvatar item : productAvatars) {
            ProductAvatarDTO productAvatarDTO = item.toProductAvatarDTO();
            productAvatarDTOs.add(productAvatarDTO);
        }
        return new ResponseEntity<>(productAvatarDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/avatars/{avatarId}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> deleteProductAvatar(@PathVariable Long productId,
            @PathVariable String avatarId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        Optional<ProductAvatar> optionalProductAvatar = productAvatarService.findById(avatarId);
        if (!optionalProductAvatar.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this avatar");
        }

        ProductAvatar productAvatar = optionalProductAvatar.get();
        productAvatarService.deleteProductAvatar(optionalProduct.get(), productAvatar);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/avatars")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> deleteAllProductAvatar(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productService.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product");
        }

        List<ProductAvatar> productAvatars = optionalProduct.get().getProductAvatars();
        if (productAvatars == null) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product avatar");
        }
        if (productAvatars.size() == 1) {
            String fileName = productAvatars.get(0).getFileName();
            String nullFileName = "null_" + optionalProduct.get().getId();
            if (fileName.contains(nullFileName)) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "This product don't have image to delete");
            }
        }
        productAvatarService.deleteAllProductAvatar(optionalProduct.get(), productAvatars);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{productId}/add-to-cart")
    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    public ResponseEntity<?> addToCart(@PathVariable Long productId,
            @RequestBody CartItemReqDTO cartItemReqDTO,
            HttpServletRequest request) {

        ProductDetail productDetail = productDetailService.findByProductIdAndSize(productId,
                cartItemReqDTO.getSizeId());
        if (productDetail == null) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this product detail");
        }

        Long cartId = (Long) request.getSession().getAttribute("cartId");
        Optional<Cart> optionalCart = cartService.findById(cartId);
        if (!optionalCart.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found your cart");
        }

        CartItem cartItem = cartItemService.addToCart(optionalCart.get(), productDetail, cartItemReqDTO);

        return new ResponseEntity<>(cartItem.toCartItemResDTO(), HttpStatus.OK);
    }

}
