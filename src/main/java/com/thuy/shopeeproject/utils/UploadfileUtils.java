package com.thuy.shopeeproject.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
public class UploadfileUtils {
    @Value("${application.cloudinary.server-name}")
    private String cloudinaryServerName;

    @Value("${application.cloudinary.cloud-name}")
    private String cloudinaryName;

    @Value("${application.cloudinary.default-folder}/product")
    private String cloudinaryDefaultFolderProduct;

    @Value("${application.cloudinary.default-folder}/user")
    private String cloudinaryDefaultFolderUser;

    @Value("${application.cloudinary.default-file-name}")
    private String cloudinaryDefaultFileName;

    @Value("${application.cloudinary.api-key}")
    private String cloudinaryApiKey;

    @Value("${application.cloudinary.api-secret}")
    private String cloudinaryApiSecret;

    public Map uploadToProductFolderCloudinary(MultipartFile file) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinaryApiSecret));

        try {
            // Chuyển đổi MultipartFile thành mảng byte[]
            byte[] fileBytes = file.getBytes();

            // Thiết lập options cho việc tải lên
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "folder", cloudinaryDefaultFolderProduct,
                    "resource_type", "auto" // Kiểu tài nguyên tự động phát hiện
            );

            // Thực hiện tải lên file lên Cloudinary
            Map<?, ?> result = cloudinary.uploader().upload(fileBytes, uploadOptions);

            // Trả về URL của file đã tải lên trên Cloudinary
            return result;
        } catch (IOException e) {
            // Xử lý exception khi đọc dữ liệu từ MultipartFile
            e.printStackTrace(); // Hoặc log thông báo lỗi
        } catch (Exception e) {
            // Xử lý các exception khác khi tải lên lên Cloudinary
            e.printStackTrace(); // Hoặc log thông báo lỗi
        }
        return null; // Trả về null hoặc một giá trị mặc định nếu có lỗi xảy ra

    }

    public Map uploadToUserFolderCloudinary(MultipartFile file) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinaryApiSecret));

        try {
            // Chuyển đổi MultipartFile thành mảng byte[]
            byte[] fileBytes = file.getBytes();

            // Thiết lập options cho việc tải lên
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "folder", cloudinaryDefaultFolderUser,
                    "resource_type", "auto" // Kiểu tài nguyên tự động phát hiện
            );

            // Thực hiện tải lên file lên Cloudinary
            Map<?, ?> result = cloudinary.uploader().upload(fileBytes, uploadOptions);

            // Trả về URL của file đã tải lên trên Cloudinary
            return result;
        } catch (IOException e) {
            // Xử lý exception khi đọc dữ liệu từ MultipartFile
            e.printStackTrace(); // Hoặc log thông báo lỗi
        } catch (Exception e) {
            // Xử lý các exception khác khi tải lên lên Cloudinary
            e.printStackTrace(); // Hoặc log thông báo lỗi
        }
        return null; // Trả về null hoặc một giá trị mặc định nếu có lỗi xảy ra

    }
}
