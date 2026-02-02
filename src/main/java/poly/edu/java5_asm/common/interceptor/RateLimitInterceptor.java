package poly.edu.java5_asm.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import poly.edu.java5_asm.common.service.RateLimitService;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String clientIp = getClientIP(request);
        
        // Define rate limits for different endpoints
        int requestsPerMinute = getRateLimitForPath(path);
        
        if (requestsPerMinute > 0) {
            String key = clientIp + ":" + path;
            
            if (!rateLimitService.isAllowed(key, requestsPerMinute)) {
                log.warn("Rate limit exceeded for IP {} on path {}", clientIp, path);
                response.setStatus(429); // Too Many Requests
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
                return false;
            }
        }
        
        return true;
    }

    private int getRateLimitForPath(String path) {
        // Login/Register: 5 requests per minute
        if (path.contains("/api/auth/login") || path.contains("/api/auth/register")) {
            return 5;
        }
        
        // Checkout: 10 requests per minute
        if (path.contains("/api/orders/checkout")) {
            return 10;
        }
        
        // Cart operations: 30 requests per minute
        if (path.contains("/api/cart")) {
            return 30;
        }
        
        // Admin operations: 60 requests per minute
        if (path.contains("/api/admin")) {
            return 60;
        }
        
        // No rate limit for other paths
        return 0;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
