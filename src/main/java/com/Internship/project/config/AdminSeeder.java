package com.Internship.project.config;

import com.Internship.project.entity.User;
import com.Internship.project.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "admin@ecosync.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {

                User admin = new User();

                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setMobile("0000000000");
                admin.setAddress("System Admin");
                admin.setEmailVerified(true);
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("=================================");
                System.out.println("ADMIN ACCOUNT CREATED");
                System.out.println("Email: admin@ecosync.com");
                System.out.println("Password: admin123");
                System.out.println("=================================");
            }
        };
    }
}