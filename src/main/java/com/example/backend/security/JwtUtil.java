package com.example.backend.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// for generating the JWT key
import java.security.Key;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET = "7b7a5d5916bc874648185ceed32dddb53d6e7848d773285c3f9221aac5bee0d2dd5cea840d66d2e513a98e17489170c7366a02d681e3ef654d9a7c4c42be5f33ec4a31d065fd5e612d691cda69579e42571f9cce6590f8ba84dcc5b358f1160b6b60fefa206250167daa8081df4956530678195504a6a082c4afaef60ded40560158c1272b9bf7e48d28e42b546fd8889eed51f8d154806d6ff20900de9a303782d428f44bb025ffa92b91bad0380ea7817f3d6c18dd30e14a53012d252c0eab32fa922b183e4091dc2a72c1a4e1811ec14c288a204919537de163d9edff1f116a597ac1631be12d1b9cab59a23b85ac56d31e292475f358d148477c0a2b1dac";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username) 
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // set token to expire in one hour
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .before(new Date());
    }
}
