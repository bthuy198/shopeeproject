package com.thuy.shopeeproject.api;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.UserCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.UserLoginReqDTO;
import com.thuy.shopeeproject.domain.dto.UserLoginResDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.enums.ERole;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.exceptions.MessageResponse;
import com.thuy.shopeeproject.security.jwt.JwtUtils;
import com.thuy.shopeeproject.service.IUserService;
import com.thuy.shopeeproject.service.UserDetailsImpl;
import com.thuy.shopeeproject.utils.AppUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthAPI {
        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        private IUserService userService;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        private AppUtils appUtils;

        @PostMapping("/signin")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginReqDTO userLoginReqDTO) {

                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(userLoginReqDTO.getUsername(),
                                                userLoginReqDTO.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

                String jwt = jwtCookie.getValue();

                // List<String> roles = userDetails.getAuthorities().stream()
                // .map(item -> item.getAuthority())
                // .collect(Collectors.toList());

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new UserLoginResDTO(userDetails.getId(),
                                                userDetails.getUsername(),
                                                userDetails.getEmail()));
        }

        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateReqDTO userCreateReqDTO,
                        BindingResult bindingResult) {
                if (userService.existsByUsername(userCreateReqDTO.getUsername())) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Username is already taken!"));
                }

                if (userService.existsByEmail(userCreateReqDTO.getEmail())) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Email is already in use!"));
                }

                if (bindingResult.hasErrors()) {
                        return appUtils.mapErrorToResponse(bindingResult);
                }

                // Create new user's account
                User user = new User();

                String role = userCreateReqDTO.getRole();

                ERole userRole;

                if (role == null) {
                        userRole = ERole.USER;
                } else {
                        if (!isRoleValid(role)) {
                                throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this user's role");
                        } else {
                                userRole = ERole.getByValue(role);
                        }
                }

                user = userCreateReqDTO.toUser(userRole);

                user = userService.save(user);

                user = userService.createNoAvatar(user);

                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }

        public boolean isRoleValid(String role) {
                for (ERole eRole : ERole.values()) {
                        if (eRole.getValue().equals(role)) {
                                return true;
                        }
                }
                return false;
        }

        @PostMapping("/signout")
        public ResponseEntity<?> logoutUser() {
                ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(HttpStatus.OK);

        }
}
