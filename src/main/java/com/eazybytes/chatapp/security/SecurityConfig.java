package com.eazybytes.chatapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Static and public resources
                .requestMatchers("/login.html", "/register.html", "/chat.html").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                // Public auth endpoints
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                // WebSocket public endpoints
                .requestMatchers("/chat-websocket/**").permitAll()
                .requestMatchers("/chat-websocket/info/**").permitAll()

                // ✅ FIX: Allow room chat history endpoint
                .requestMatchers(HttpMethod.GET, "/api/chat-messages/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/chat-messages/**").permitAll()
                .requestMatchers("/","/error").permitAll()

                // Any other requests need authentication
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider());

      

        return http.build();
    }
}
