package com.example.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
/* loaded from: webauth.jar:BOOT-INF/classes/com/example/demo/JwtTokenProvider.class */
public class JwtTokenProvider {
    private String secret = "25d55ad283aa400af464c76d713c07add57f21e6a273781dbf8b7657940f3b03";

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.secret.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secret.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
