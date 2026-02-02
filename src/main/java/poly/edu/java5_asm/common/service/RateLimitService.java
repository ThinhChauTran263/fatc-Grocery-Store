package poly.edu.java5_asm.common.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Check if request is allowed for given key
     * @param key Unique identifier (IP, userId, etc.)
     * @param requestsPerMinute Number of requests allowed per minute
     * @return true if allowed, false if rate limit exceeded
     */
    public boolean isAllowed(String key, int requestsPerMinute) {
        Bucket bucket = cache.computeIfAbsent(key, k -> createBucket(requestsPerMinute));
        boolean allowed = bucket.tryConsume(1);
        
        if (!allowed) {
            log.warn("Rate limit exceeded for key: {}", key);
        }
        
        return allowed;
    }

    /**
     * Create bucket with specified rate limit
     */
    private Bucket createBucket(int requestsPerMinute) {
        Bandwidth limit = Bandwidth.classic(
            requestsPerMinute,
            Refill.intervally(requestsPerMinute, Duration.ofMinutes(1))
        );
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    /**
     * Clear rate limit for specific key (useful for testing or manual reset)
     */
    public void clearLimit(String key) {
        cache.remove(key);
        log.info("Rate limit cleared for key: {}", key);
    }
}
