package ru.shopocon.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shopocon.ecommerce.common.util.EncryptionService;
import ru.shopocon.ecommerce.common.util.EncryptionServiceImpl;

@Configuration
public class Config {

    @Bean
    public EncryptionService encryptionService() {
        return new EncryptionServiceImpl();
    }
}
