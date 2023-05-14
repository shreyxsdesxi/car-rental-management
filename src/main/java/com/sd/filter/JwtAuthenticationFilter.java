package com.sd.filter;

import com.sd.exception.AuthenticationException;
import com.sd.exception.JwtException;
import com.sd.service.security.CustomUserDetailsService;
import com.sd.util.Constants;
import com.sd.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        log.info("[doFilterInternal] JWT Filter");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(Objects.isNull(authorizationHeader)){
            log.error("[doFilterInternal] AUth Header not present");
            filterChain.doFilter(request, response);
            throw new AuthenticationException(Constants.Errors.AUTHORIZATION_HEADER_NOT_FOUND.getErrorMessage());
        }
        if (!authorizationHeader.startsWith(Constants.JWT_TOKEN_PREFIX)){
            log.error("[doFilterInternal] Auth header malformed");
            filterChain.doFilter(request, response);
            throw new AuthenticationException(Constants.Errors.BEARER_TOKEN_NOT_PRESENT.getErrorMessage());
        }
        String jwtToken = authorizationHeader.substring(7);
        String extractedUserName = tokenUtils.extractUserName(jwtToken);
        if(!StringUtils.isBlank(jwtToken) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(extractedUserName);
            if(tokenUtils.isTokenValid(jwtToken)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //TODO: what this does
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //move the filter to the next filter
        filterChain.doFilter(request, response);
    }
}
