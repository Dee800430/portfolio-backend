package com.back.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        try {

            String contentType = file.getContentType();

            Map uploadResult;

            // Resume Files
            if (contentType != null &&
                    (contentType.equals("application/pdf")
                            || contentType.equals("application/msword")
                            || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

                uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "raw",
                                "folder", "resumes"
                        )
                );

            } else {

                // Images
                uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "image",
                                "folder", "profiles"
                        )
                );
            }

            return uploadResult.get("secure_url").toString();

        } catch (Exception ex) {

            ex.printStackTrace();

            throw new RuntimeException(
                    "File upload failed: " + ex.getMessage(),
                    ex
            );
        }
    }
}