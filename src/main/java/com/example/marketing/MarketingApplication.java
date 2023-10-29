package com.example.marketing;

import com.example.marketing.model.entities.Store;
import com.example.marketing.model.entities.User;
import com.example.marketing.repository.StoreRepository;
import com.example.marketing.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MarketingApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MarketingApplication.class, args);
    }

    @Autowired
    private AccountService accountService;
    @Autowired
    private StoreRepository storeRepository;
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        String apiUrl = "https://smshub.org/stubs/handler_api.php?api_key=192336U741fdf0fafd82d9b5754040a3d7268d3&action=getStatus&id=503430797";
        String response = new RestTemplate().getForObject(apiUrl, String.class);
        System.out.println(response);
    }
}
