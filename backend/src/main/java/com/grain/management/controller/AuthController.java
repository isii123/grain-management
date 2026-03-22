package com.grain.management.controller;

import com.grain.management.dto.LoginRequest;
import com.grain.management.dto.LoginResponse;
import com.grain.management.entity.User;
import com.grain.management.mapper.UserMapper;
import com.grain.management.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenUtil.generateToken(loginRequest.getUsername());

            // 获取用户信息
            User user = userMapper.findByUsername(loginRequest.getUsername());
            if(user==null) {
                throw new RuntimeException("用户不存在");
            };

            LoginResponse response = new LoginResponse(
                    token,
                    user.getUsername(),
                    user.getFullName(),
                    user.getRole()
            );

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            return ResponseEntity.badRequest().body("用户已被禁用");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userMapper.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }

        // 设置默认角色为operator
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("operator");
        }

        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);

        userMapper.insert(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "注册成功");
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(401).body("未登录");
        }

        String username = authentication.getName();
        User user = userMapper.findByUsername(username);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("fullName", user.getFullName());
        userInfo.put("role", user.getRole());

        return ResponseEntity.ok(userInfo);
    }
}