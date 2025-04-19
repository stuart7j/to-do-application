package com.jsserverspac.todoapplication.service;

import com.jsserverspac.todoapplication.dto.AuthRequest;
import com.jsserverspac.todoapplication.model.User;
import com.jsserverspac.todoapplication.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Value("${spring.security.jwt.secret}")
    private String secret;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public String register(AuthRequest req) {
        try {
            if (userRepo.findByUsername(req.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(encoder.encode(req.getPassword()));
            userRepo.save(user);
            return generateToken(user.getUsername());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Username already exists");
        }
    }

    public String login(AuthRequest req) {

        try {
            User user = userRepo.findByUsername(req.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!encoder.matches(req.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            return generateToken(user.getUsername());
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
                .compact();
    }
}
