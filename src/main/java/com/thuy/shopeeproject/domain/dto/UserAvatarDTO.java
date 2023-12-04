package com.thuy.shopeeproject.domain.dto;

import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserAvatar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAvatarDTO {
    private String id;
    private String fileName;
    private String fileFolder;
    private String fileUrl;
    private String fileType;
    private String cloudId;
    private Integer width;
    private Integer height;

    public UserAvatar toAvatar(User user) {
        return new UserAvatar()
                .setId(id)
                .setFileName(fileName)
                .setFileFolder(fileFolder)
                .setFileUrl(fileUrl)
                .setFileType(fileType)
                .setCloudId(cloudId)
                .setHeight(height)
                .setWidth(width)
                .setUser(user);
    }
}
