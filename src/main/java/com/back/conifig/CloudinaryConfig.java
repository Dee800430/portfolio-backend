package com.back.conifig;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class CloudinaryConfig {

    @Value("${CLOUDINARY_CLOUD_NAME}")
    private String cloudName;

    @Value("${CLOUDINARY_API_KEY}")
    private String apiKey;

    @Value("${CLOUDINARY_API_SECRET}")
    private String apiSecret;
    @Bean
    public Cloudinary cloudinary() {
        Map<String,String> map = new HashMap<>();
        map.put("cloud_name",cloudName);
        map.put("api_key",apiKey);
        map.put("api_secret",apiSecret);
        return new Cloudinary(map);
    }
}
