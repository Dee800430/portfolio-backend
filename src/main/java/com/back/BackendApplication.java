package com.back;

import com.back.entity.Contact;
import com.back.entity.Profile;
import com.back.entity.User;
import com.back.repository.ContactRepository;
import com.back.repository.ProfileRepository;
import com.back.repository.ProjectRepository;
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
            PasswordEncoder passwordEncoder,
            ProfileRepository profileRepository,
            ContactRepository contactRepository,
            ProjectRepository projectRepository
    ) {

        return args -> {

            String username = "admin";

            Optional<User> existingUser = userRepo.findByUsername(username);

            if (existingUser.isEmpty()) {

                User user = new User();
                Profile profile = new Profile();
               Contact  contact = new Contact();

                user.setUsername("admin");
                user.setPassword(
                        passwordEncoder.encode("admin123")
                );
                user.setRole("ROLE_ADMIN");

               User u1 =  userRepo.save(user);
               profile.setUserId(u1.getUserId());
               profileRepository.save(profile);
               contact.setUserId(u1.getUserId());
               contactRepository.save(contact);

                System.out.println("Default Admin Created");
            } else {
                System.out.println("Admin Already Exists");
            }
        };
    }
}