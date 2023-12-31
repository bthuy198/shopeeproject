package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thuy.shopeeproject.domain.dto.UserCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.user.UserFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.user.UserResDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserAvatar;
import com.thuy.shopeeproject.domain.entity.UserPrincipal;
import com.thuy.shopeeproject.repository.UserRepository;
import com.thuy.shopeeproject.utils.UploadfileUtils;

@ConfigurationProperties(prefix = "application.cloudinary")
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserAvatarService userAvatarService;

    @Autowired
    private UploadfileUtils uploadfileUtils;

    @Value("${application.cloudinary.server-name}")
    private String cloudinaryServerName;

    @Value("${application.cloudinary.default-folder}/product")
    private String cloudinaryDefaultFolderProduct;

    @Value("${application.cloudinary.default-file-name}")
    private String cloudinaryDefaultFileName;

    @Value("${application.cloudinary.default-avatar-url}")
    private String cloudinaryDefaultImage;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User e) {
        return userRepository.save(e);

    }

    @Override
    public void delete(User e) {
        e.setDeleted(true);
        e = userRepository.save(e);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User createNoAvatar(User user) {

        UserAvatar userAvatar = new UserAvatar();
        userAvatarService.save(userAvatar);
        userAvatar.setFileFolder(cloudinaryDefaultFolderProduct);
        userAvatar.setFileUrl(cloudinaryDefaultImage);
        userAvatar.setUser(user);
        userAvatar.setCloudId("shopee_project/user/" + userAvatar.getCloudId());
        userAvatar.setFileName(userAvatar.getId() + ".jpg");
        userAvatarService.save(userAvatar);

        user.setAvatar(userAvatar);
        return user;
    }

    @Override
    public User createWithAvatar(UserCreateReqDTO userCreateReqDTO, User user) {

        MultipartFile file = userCreateReqDTO.getFile();

        UserAvatar userAvatar = uploadAvatar(file, user);
        user.setAvatar(userAvatar);
        userRepository.save(user);

        return user;
    }

    private UserAvatar uploadAvatar(MultipartFile file, User user) {

        UserAvatar userAvatar = new UserAvatar();
        userAvatarService.save(userAvatar);
        // upload ảnh
        Map<?, ?> uploadResult = uploadfileUtils.uploadToUserFolderCloudinary(file);
        String fileUrl = (String) uploadResult.get("secure_url");
        String fileFormat = (String) uploadResult.get("format");
        Integer width = (Integer) uploadResult.get("width");
        Integer height = (Integer) uploadResult.get("height");

        // set các giá trị theo giá trị sinh ra trên cloudinary
        userAvatar.setFileName(userAvatar.getId() + "." + fileFormat);
        userAvatar.setFileType(fileFormat);
        userAvatar.setFileUrl(fileUrl);
        userAvatar.setFileFolder(cloudinaryDefaultFolderProduct);
        userAvatar.setCloudId(userAvatar.getFileFolder() + "/" + userAvatar.getId());
        userAvatar.setWidth(width);
        userAvatar.setHeight(height);
        userAvatar.setUser(user);
        userAvatarService.save(userAvatar);
        return userAvatar;
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserPrincipal.build(user);
    }

    @Override
    public Page<UserResDTO> findAllUser(UserFilterReqDTO userFilterReqDTO, Pageable pageable) {
        return userRepository.findAllUser(userFilterReqDTO, pageable).map(User::toUserResDTO);
    }

}
