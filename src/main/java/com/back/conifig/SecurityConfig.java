package com.back.conifig;

import com.back.service.CustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomService customUserService;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                // ✅ Enable CORS
                .cors(Customizer.withDefaults())

                // ❌ No sessions (JWT based auth)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC AUTH APIs
                        // =========================
                        .requestMatchers("/auth/**").permitAll()

                        // =========================
                        // PROJECT APIs
                        // =========================
                        .requestMatchers(HttpMethod.GET, "/api/project/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/project/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/project/**").authenticated()

                        // =========================
                        // CONTACT APIs
                        // =========================
                        .requestMatchers(HttpMethod.GET, "/api/contact/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contact/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/contact/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/contact/**").authenticated()

                        // =========================
                        // PROFILE APIs
                        // =========================
                        .requestMatchers("/api/profile/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profile/**").permitAll()

                        .requestMatchers("/api/profile/**").authenticated()

                        // =========================
                        // ADMIN APIs
                        // =========================
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // =========================
                        // DEFAULT
                        // =========================
                        .anyRequest().authenticated()
                )

                // User details service
                .userDetailsService(customUserService)

                // JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =========================
    // PASSWORD ENCODER
    // =========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================
    // AUTH MANAGER
    // =========================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    // =========================
    // CORS CONFIG
    // =========================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://portfolio-frontend-xi-khaki.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}