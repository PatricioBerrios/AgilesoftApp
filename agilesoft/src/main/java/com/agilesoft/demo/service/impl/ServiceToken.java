package com.agilesoft.demo.service.impl;

import com.agilesoft.demo.entity.User;
import com.agilesoft.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Service
public class ServiceToken {

    @Value("${spring.secretkey}")
    private String secretKey;

    @Autowired
    UserRepository userRepository;

    public String validateSession(String jwt){

        try{
            String username = extractUsername(jwt);
            User user = userRepository.findByUsername(username);

            if (user == null ) {
                throw new IllegalArgumentException("Las credenciales no son válidas.");
            }

            return username;

        } catch (SignatureException e) {
            throw new IllegalArgumentException("Token JWT no válido.");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al validar el token JWT.", e);
        }

    }

    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public String extractUsername(String jwt) throws SignatureException {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new ExpiredJwtException(null, claims, "El token ha expirado");
            }

            return claims.getSubject();

        } catch (ExpiredJwtException e) {
            throw new SignatureException("El token JWT ha expirado");
        }
    }

    public String getJwtToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }
}
