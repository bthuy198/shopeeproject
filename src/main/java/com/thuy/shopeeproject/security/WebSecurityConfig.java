package com.thuy.shopeeproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserPrincipal;
import com.thuy.shopeeproject.security.jwt.AuthEntryPointJwt;
import com.thuy.shopeeproject.security.jwt.AuthTokenFilter;
import com.thuy.shopeeproject.service.IUserService;

@Configuration
// @EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    // @Autowired
    // private UserPrincipal userPrincipal;

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
                // .formLogin((form) -> form
                // .loginPage("/login")
                // .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                // .exceptionHandling(exception ->
                // exception.authenticationEntryPoint(unauthorizedHandler))
                // .sessionManagement(session ->
                // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        // Allow access to Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/auth/**",
                                "/api/products/**",
                                "/api/details/**")
                        .permitAll()
                        .requestMatchers("/**")
                        .permitAll()
                        // Authenticate all other requests
                        .anyRequest()
                        .authenticated());
        ;
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

        // fix H2 database console: Refused to display ' in a frame because it set
        // 'X-Frame-Options' to 'deny'
        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    // UserDetails admin = User.withUsername("hach")
    // .password(encoder.encode("hacheery"))
    // .roles("ADMIN")
    // .build();
    // UserDetails user = User.withUsername("user")
    // .password(encoder.encode("pwd1"))
    // .roles("USER")
    // .build();
    // return new InMemoryUserDetailsManager(admin, user);
    // }

}
