package com.example.marketing.util;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.function.Function;


@Component
@Slf4j
public class JWTUtil {
    private static String SECRET = "gland84";
    // thời gian hiệu lực
    private final long EXPIRATION_TIME = 604800000L;
    // prefix token
    //private final String TOKEN_PREFIX = "Bearer ";
    // header option
    //private final String HEADER_STRING = "Authorization";

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    // tạo jwt
    public String generateToken(UserDetail userDetail) {

        //Map<String, Object> claims = new HashMap<>();

        Date date = new Date();

        Date expiryDate = new Date(date.getTime()+ EXPIRATION_TIME);

        // tạo jwt từ id user
        String jwt = "";
        try {
            jwt = Jwts.builder()
                    .setSubject(Long.toString(userDetail.getUser().getId()))
                    .claim("username", userDetail.getUser().getUsername())
                    .claim("admin", userDetail.getUser().isAdmin())
                    .setIssuedAt(date)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jwt;
    }

    // Lấy thông tin user từ jwt
    public Long getUserByIdfromJWT(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                 | IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Long.parseLong(claims.getSubject());
    }

    public String getUsernameFromJwt(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes("UTF-8")) // Sử dụng cùng secret key để giải mã JWT
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Trích xuất giá trị của trường "username" từ payload
            String username = claims.get("username", String.class);
            return username;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Xử lý lỗi nếu có
        }
    }

    public UserDTO getUserFromJwt(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes("UTF-8")) // Sử dụng cùng secret key để giải mã JWT
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Trích xuất giá trị của trường "username" từ payload
            String username = claims.get("username", String.class);
            boolean isAdmin = claims.get("admin", Boolean.class);
            long id =  Long.parseLong(claims.getSubject());
            return new UserDTO(id, username, isAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Xử lý lỗi nếu có
        }
    }

    // validate jwt
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes("UTF-8")).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        }catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        } catch (SignatureException e) {
            System.out.println("Signature Exception");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception");
        }
        return false;
    }

    public UserDTO validateTokenAndGetUsername(String bearerToken){
        if(validateToken(bearerToken.replace("Bearer ",""))){
            /*String username = getUsernameFromJwt(bearerToken.replace("Bearer ",""));
            return username;*/
            UserDTO userDTO = getUserFromJwt(bearerToken.replace("Bearer ",""));
            return userDTO;
        }
        return null;
    }
}
