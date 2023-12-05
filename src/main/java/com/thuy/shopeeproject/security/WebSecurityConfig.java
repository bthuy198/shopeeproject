package com.thuy.shopeeproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.thuy.shopeeproject.service.IUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

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
                // .authorizeHttpRequests((requests) -> requests
                // .requestMatchers("/", "/home", "/api/**", "/**").permitAll()
                // .anyRequest())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                // .logout((logout) -> logout.permitAll());
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // This second filter chain will secure the static resources without reading the
    // SecurityContext from the session.
    // @Bean
    // @Order(0)
    // SecurityFilterChain resources(HttpSecurity http) throws Exception {
    // http
    // .requestMatchers((matchers) -> matchers.antMatchers("/resources/**",
    // "/static/**", "/css/**", "/js/**",
    // "/images/**"))
    // .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
    // .requestCache().disable()
    // .securityContext().disable()
    // .sessionManagement().disable();

    // return http.build();
    // }
}
