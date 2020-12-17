package by.innowise.calendarapp.security;

import by.innowise.calendarapp.security.utils.JwtTokenProvider;
import by.innowise.calendarapp.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {


    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserServiceDetails userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomJwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("start filter");
            String jwtToken = extractJwtFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
                String userName = jwtTokenProvider.getUsernameFromToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        catch(ExpiredJwtException e) {
            String isRefreshToken = httpServletRequest.getHeader("isRefreshToken");
            String requestUrl = httpServletRequest.getRequestURL().toString();
            if(isRefreshToken != null && isRefreshToken.equals(true) && requestUrl.contains("refreshtoken")) {
                allowForRefreshToken(e, httpServletRequest);
            }
            else {
                httpServletRequest.setAttribute("exception ", e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }

    private void allowForRefreshToken(ExpiredJwtException e, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        httpServletRequest.setAttribute("claims", e.getClaims());

    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return "";
    }
}
