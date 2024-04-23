package com.cntt.rentalmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
// lớp FileStorageProperties này được sử dụng để cấu hình các
// đường dẫn lưu trữ tệp tin trong ứng dụng của bạn và sử dụng các giá trị được
// cấu hình này thông qua Spring Boot để thực hiện các thao tác liên quan đến lưu trữ và xử lý tệp tin
@ConfigurationProperties(
        prefix = "file",
        ignoreUnknownFields = true,
        ignoreInvalidFields = true
)
public class FileStorageProperties {
    private String uploadDir;
    private String tempExportExcel;
    private String libreOfficePath;

    public FileStorageProperties() {
        // TODO document why this constructor is empty
    }

    public String getUploadDir() {
        return this.uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getTempExportExcel() {
        return this.tempExportExcel;
    }

    public void setTempExportExcel(String tempExportExcel) {
        this.tempExportExcel = tempExportExcel;
    }

    public String getLibreOfficePath() {
        return this.libreOfficePath;
    }

    public void setLibreOfficePath(String libreOfficePath) {
        this.libreOfficePath = libreOfficePath;
    }
}

