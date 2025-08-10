package com.selena.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    private String secretKey;

    public JwtService() {
        secretKey = generateSecretKey();
    }
    // TẠO KHÓA BÍ MẬT (dùng HmacSHA256) và trả về dạng Base64 string
    public String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }
//    generateToken(username) → tạo claims rỗng → setSubject(username) → setIssuedAt(now) → setExpiration(now+3 phút) → signWith(HS256, getKey()) → trả về chuỗi JWT.
    // Sinh JWT cho username truyền vào
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // chỗ để nhét thêm claim custom (role, userId...)

        return Jwts.builder()
                .setClaims(claims)   // payload tùy biến
                .setSubject(username)  // "sub" = đối tượng chính (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // iat = thời điểm phát hành
    //  ngày hết hạn, đang cập nhật là mã token hết hạn sau 1000*60*3 ms = 3 phút
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*3))
    //  Ký bằng HS256 với khóa bí mật
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    //  getKey() → BASE64 decode secretKey → Keys.hmacShaKeyFor(bytes) → trả Key để ký/parse.
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // giải Base64
        return Keys.hmacShaKeyFor(keyBytes); // tạo key hợp lệ cho HS256 (đủ độ dài)
    }

    //  extractUserName(token) → extractClaim(token, Claims::getSubject) → trả username (subject).
    public String extractUserName(String token) {
        //subject = username
        return extractClaim(token, Claims::getSubject);
    }

    //  HÀM CHUNG: trích bất kỳ claim nào theo "hàm lấy claim" truyền vào
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // PARSE TOKEN → VERIFY CHỮ KÝ → LẤY TOÀN BỘ CLAIMS
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()   // key để verify chữ ký
                .setSigningKey(getKey())  // parse & verify (ném exception nếu sai/hết hạn)
                .build().parseClaimsJws(token).getBody(); // lấy phần payload (claims)
    }

    // KIỂM TRA TOKEN: đúng người & chưa hết hạn
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        //    kiểm tra người dùng dc gửi trong mã thông báo có khớp với ng dùng trong cơ sở dữ liệu không ?
        //     và kiểm tra xem mã thông báo hết hạn chưa
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //KIỂM TRA HẾT HẠN
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //TRÍCH THỜI ĐIỂM HẾT HẠN TỪ CLAIMS
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
