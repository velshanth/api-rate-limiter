package org.guvi.config;

import org.guvi.model.ApiLog;
import org.guvi.repo.ApiLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private final ApiLogRepository apiLogRepository;

    public LoggingFilter(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long responseTime =
                System.currentTimeMillis() - startTime;

        String clientIp =
                request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.isBlank()) {

            clientIp = request.getRemoteAddr();
        }

        ApiLog log = new ApiLog(
                null,
                request.getRequestURI(),
                request.getMethod(),
                clientIp,
                response.getStatus(),
                responseTime,
                Instant.now(),
                response.getStatus() == 429
        );

        apiLogRepository.save(log);
    }
}