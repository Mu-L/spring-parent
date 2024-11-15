package com.emily.infrastructure.test.entity.response;

import com.emily.infrastructure.sensitive.annotation.JsonNullField;
import com.emily.infrastructure.sensitive.annotation.JsonSensitive;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传参数
 *
 * @author Emily
 * @since Created in 2023/7/2 1:21 PM
 */
@JsonSensitive
public class FileUploadRequest {
    @JsonNullField
    private MultipartFile file;
    @JsonNullField
    private MultipartFile imageFile;
    private String accountCode;
    private String address;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
