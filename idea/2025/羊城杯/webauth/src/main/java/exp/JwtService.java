package exp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtService {

    public String secret = "25d55ad283aa400af464c76d713c07add57f21e6a273781dbf8b7657940f3b03";

    // 生成 Token
    public String generateToken(String userId, String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + 360000000);  // 1小时后过期

        // 生成 JWT
        return Jwts.builder()
                .setSubject(userId)  // 设置 Subject (一般用作用户标识)
//                .claim("username", username)  // 设置自定义 claim
                .setIssuedAt(now)  // 设置生成时间
                .setExpiration(exp)  // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())  // 使用 HS256 算法和密钥签名
                .compact();  // 生成 JWT
    }

    // 验证 Token
    public boolean validateToken(String token) {
        try {
            // 验证 Token
            Jwts.parserBuilder()
                    .setSigningKey(this.secret.getBytes()) // 使用同样的密钥
                    .build()
                    .parseClaimsJws(token);  // 解析 token，验证签名
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secret.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public static void main(String[] args) {
        JwtService jwtService = new JwtService();
        String token = jwtService.generateToken("user1", "exampleUser");
        System.out.println("Authorization: Bearer " + token);

        // 验证 token
        boolean isValid = jwtService.validateToken(token);
        System.out.println("Is token valid? " + isValid);
        String usernameFromToken = jwtService.getUsernameFromToken(token);
        System.out.println("Username: " + usernameFromToken);


    }
}

