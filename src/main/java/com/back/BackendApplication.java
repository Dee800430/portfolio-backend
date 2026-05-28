package com.back;

import com.back.entity.User;
import com.back.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner createDefaultUser(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            String username = "admin";

            Optional<User> existingUser = userRepo.findByUsername(username);

            if (existingUser == null) {

                User user = new User();

                user.setUsername("admin");
                user.setPassword(
                        passwordEncoder.encode("admin123")
                );
                user.setRole("ADMIN");

                userRepo.save(user);

                System.out.println("Default Admin Created");
            } else {
                System.out.println("Admin Already Exists");
            }
        };
    }
}