package com.selena.conf;

import com.selena.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// JwtFilter đọc Authorization → tách token → parse lấy username → nếu chưa có Auth thì nạp user từ DB → validate token (đúng user & chưa hết hạn) → tạo Authentication & set vào SecurityContext → cho request đi tiếp.

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        Authorization: Bearer eyJhbGc... phải đọc Authorization
//        Nếu authHeader không null và bắt đầu bằng "Bearer " → tiếp tục.
        String authHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        String username = null;

//        trích xuất mã thông báo, bắt đầu bằng Bearer
        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            Tách token & lấy username
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }
        // kiểm tra xem username có null không và kiểm tra xem đối tượng xác thực đã tồn tại hay chưa
//        Chỉ xử lý khi chưa có Authentication trong context
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            lấy thông tin userdetauils từ DB qua service
//            Nạp người dùng từ DB
            UserDetails userDetails = applicationContext.getBean(UserDetailsService.class).loadUserByUsername(username);

//            xác thực mã thông báo Validate token xem có đúng user và chưa hết hạn không ?
//            Xác thực token khớp user & còn hạn
            if(jwtService.validateToken(token,userDetails)){
//                Tạo Authentication và gắn vào SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,                 // principal: ai đang đăng nhập
                                null,                        // credentials: bỏ null vì đã xác thực
                                userDetails.getAuthorities() // quyền của user
                        );
// Gắn thêm thông tin từ request (IP, sessionId...) vào details
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                );
//                SecurityContextHolder là một “hộp” mà Spring Security mang theo cho từng request.
//Nếu “hộp” rỗng → user chưa login → trả 401 khi vào API cần xác thực.
//Nếu “hộp” chứa Authentication → user đã login → controller được phép chạy, và bạn có thể lấy thông tin user từ đó.
// Đưa Authentication vào SecurityContext → Spring Security biết user này đã login
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
//            chuyển sang bộ lọc tiếp theo
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }
}
