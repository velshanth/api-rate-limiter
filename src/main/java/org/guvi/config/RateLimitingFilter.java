package org.guvi.config;

import org.guvi.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimitingFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp =
                request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.isBlank()) {

            clientIp = request.getRemoteAddr();
        }

        boolean allowed =
                rateLimiterService.allowRequest(clientIp);

        if (!allowed) {

            response.setStatus(429);

            response.getWriter()
                    .write("Rate Limit Exceeded");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
