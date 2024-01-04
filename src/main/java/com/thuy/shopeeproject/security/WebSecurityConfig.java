package com.thuy.shopeeproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.thuy.shopeeproject.security.jwt.AuthEntryPointJwt;
import com.thuy.shopeeproject.security.jwt.AuthTokenFilter;
import com.thuy.shopeeproject.service.IUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private IUserService userService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        // Allow access to Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        // Authenticate all other requests
                        .anyRequest().permitAll());
        // http
        // .formLogin((form) -> form
        // .loginPage("/login")
        // .permitAll())
        // // .logout((logout) -> logout.permitAll());
        // .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**")
        // .permitAll()
        // .anyRequest()
        // .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
        // .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // @Override
    // public void configure(WebSecurity web) throws Exception {
    // web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**",
    // "/swagger-ui.html");
    // }

}
