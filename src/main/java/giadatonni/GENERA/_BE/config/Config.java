package giadatonni.GENERA._BE.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    @Bean
    public Cloudinary getImageUploader(@Value("${cloudinary.name}") String cloudName,
                                       @Value("${cloudinary.apiKey}") String apiKey,
                                       @Value("${cloudinary.secret}") String apiSecret
    ) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}