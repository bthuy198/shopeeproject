package com.thuy.shopeeproject.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thuy.shopeeproject.domain.entity.ProductAvatar;
import com.thuy.shopeeproject.domain.dto.product.ProductCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.domain.entity.Size;
import com.thuy.shopeeproject.repository.ProductRepository;
import com.thuy.shopeeproject.utils.UploadfileUtils;

@ConfigurationProperties(prefix = "application.cloudinary")
@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private ISizeService sizeService;

    @Autowired
    private IProductAvatarService productAvatarService;

    @Autowired
    private UploadfileUtils uploadfileUtils;

    @Value("${application.cloudinary.server-name}")
    private String cloudinaryServerName;

    @Value("${application.cloudinary.default-folder}/product")
    private String cloudinaryDefaultFolderProduct;

    @Value("${application.cloudinary.default-file-name}")
    private String cloudinaryDefaultFileName;

    @Value("${application.cloudinary.default-image-url}")
    private String cloudinaryDefaultImage;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<ProductResDTO> findAll(ProductFilterReqDTO productFilterReqDTO, Pageable pageable) {
        return productRepository.findAll(productFilterReqDTO, pageable).map(Product::toProductResDTO);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product e) {
        return productRepository.save(e);
    }

    @Override
    public void delete(Product e) {
        e.setDeleted(true);
        productRepository.save(e);
    }

    @Override
    public void deleteById(Long id) {
        // productRepository.deleteById(id);
    }

    @Override
    public Product createProduct(ProductCreateReqDTO productCreateReqDTO, Category category) {
        Product product = productCreateReqDTO.toProduct(category);
        productRepository.save(product);
        BigDecimal price = BigDecimal.valueOf(Long.parseLong(productCreateReqDTO.getPrice()));
        List<Size> sizeList = sizeService.findAll();
        List<ProductDetail> productDetails = new ArrayList<>();
        for (int i = 0; i < sizeList.size(); i++) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setPrice(price);
            productDetail.setQuantity(0L);
            productDetail.setSize(sizeList.get(i));
            productDetail.setProduct(product);
            productDetailService.save(productDetail);
            productDetails.add(productDetail);
        }

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar.setFileFolder(cloudinaryDefaultFolderProduct);
        productAvatar.setFileUrl(cloudinaryDefaultImage);
        productAvatar.setProduct(product);
        productAvatar.setCloudId("shopee_project/product/" + productAvatar.getCloudId());
        productAvatar.setFileName(productAvatar.getId() + ".jpg");
        productAvatarService.save(productAvatar);

        product.setProductDetails(productDetails);
        return productRepository.save(product);
    }

    @Override
    public Product createProductWithAvatars(ProductCreateReqDTO productCreateReqDTO, Category category) {
        Product product = productCreateReqDTO.toProduct(category);
        productRepository.save(product);
        MultipartFile[] files = productCreateReqDTO.getFiles();

        BigDecimal price = BigDecimal.valueOf(Long.parseLong(productCreateReqDTO.getPrice()));
        List<Size> sizeList = sizeService.findAll();
        List<ProductDetail> productDetails = new ArrayList<>();
        for (int i = 0; i < sizeList.size(); i++) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setPrice(price);
            productDetail.setQuantity(0L);
            productDetail.setSize(sizeList.get(i));
            productDetail.setProduct(product);
            productDetailService.save(productDetail);
            productDetails.add(productDetail);
        }
        product.setProductDetails(productDetails);

        List<ProductAvatar> avatars = uploadProductImageFiles(files, product);
        return product;
    }

    private List<ProductAvatar> uploadProductImageFiles(MultipartFile[] files, Product product) {
        // List<AvatarDTO> avatarDTOList = new ArrayList<>();
        List<ProductAvatar> avatars = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {

            // tạo mới ảnh
            ProductAvatar avatar = new ProductAvatar();
            if (i == 0) {
                avatar.setIsDefault(true);
            }
            productAvatarService.save(avatar);
            avatars.add(avatar);

            // upload ảnh
            Map<?, ?> uploadResult = uploadfileUtils.uploadToProductFolderCloudinary(files[i]);
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");
            Integer width = (Integer) uploadResult.get("width");
            Integer height = (Integer) uploadResult.get("height");

            // set các giá trị theo giá trị sinh ra trên cloudinary
            avatar.setFileName(avatar.getId() + "." + fileFormat);
            avatar.setFileType(fileFormat);
            avatar.setFileUrl(fileUrl);
            avatar.setFileFolder(cloudinaryDefaultFolderProduct);
            avatar.setCloudId(avatar.getFileFolder() + "/" + avatar.getId());
            avatar.setWidth(width);
            avatar.setHeight(height);
            avatar.setProduct(product);
            productAvatarService.save(avatar);

        }
        product.setProductAvatars(avatars);
        return avatars;
    }

    @Override
    public Product updateProductWithAvatar(ProductUpdateReqDTO productUpdateReqDTO, Product product) {
        MultipartFile[] files = productUpdateReqDTO.getFiles();

        List<ProductAvatar> productAvatars = product.getProductAvatars();
        for (ProductAvatar avatar : productAvatars) {
            productAvatarService.deleteById(avatar.getId());
        }

        productAvatars = uploadProductImageFiles(files, product);
        product.setProductAvatars(productAvatars);
        product.setProductDetails(product.getProductDetails());
        return productRepository.save(product);
    }

    @Override
    public Product updateProductNoAvatar(ProductUpdateReqDTO productUpdateReqDTO, Product product,
            Category category) {
        Product newProduct = productUpdateReqDTO.toProduct(product, category);
        newProduct.setId(product.getId());
        return productRepository.save(newProduct);
    }

}
