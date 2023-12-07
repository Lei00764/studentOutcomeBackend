package com.example.studentoutcomebackend.adapter.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    /**
     * 将图片传到service上
     * @param multipartImg 图片
     * @return 图片的文件名
     */
    String saveImage(MultipartFile multipartImg);

    /**
     * 删除imageName指定的图片
     * @param imageName 图片的路径
     */
    void removeImage(String imageName);
}
