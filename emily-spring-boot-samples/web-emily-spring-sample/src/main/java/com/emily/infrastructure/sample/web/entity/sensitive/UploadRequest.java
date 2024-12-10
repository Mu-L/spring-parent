package com.emily.infrastructure.sample.web.entity.sensitive;

import com.emily.infrastructure.sensitize.annotation.DesensitizeNullProperty;
import com.emily.infrastructure.sensitize.annotation.DesensitizeModel;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author Emily
 * @since Created in 2023/5/14 4:44 PM
 */
@DesensitizeModel
public class UploadRequest {
    @DesensitizeNullProperty
    private MultipartFile file;
    @DesensitizeNullProperty
    private String fileName;
    @DesensitizeNullProperty
    private int age;
    @DesensitizeNullProperty
    private Integer ageW;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getAgeW() {
        return ageW;
    }

    public void setAgeW(Integer ageW) {
        this.ageW = ageW;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
