package com.thuy.shopeeproject.domain.entity;

import org.hibernate.annotations.GenericGenerator;

import com.thuy.shopeeproject.domain.dto.UserAvatarDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_avatars")
public class UserAvatar extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_folder")
    private String fileFolder;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "cloud_id")
    private String cloudId;

    private Integer width;
    private Integer height;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserAvatarDTO toUserAvatarDTO() {
        return new UserAvatarDTO()
                .setId(id)
                .setCloudId(cloudId)
                .setFileFolder(fileFolder)
                .setFileName(fileName)
                .setFileType(fileType)
                .setFileUrl(fileUrl);
    }
}
