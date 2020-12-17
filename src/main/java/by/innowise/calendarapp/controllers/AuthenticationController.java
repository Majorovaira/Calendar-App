package by.innowise.calendarapp.controllers;


import by.innowise.calendarapp.repositories.UserRepository;
import by.innowise.calendarapp.security.Authorities;
import by.innowise.calendarapp.security.CustomUserServiceDetails;
import by.innowise.calendarapp.security.JwtTokenProvider;
import by.innowise.calendarapp.security.UserResponse;
import by.innowise.calendarapp.services.UserService;
import io.jsonwebtoken.impl.DefaultClaims;
import liquibase.license.LicenseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private CustomUserServiceDetails userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestHeader("Authorization") String userPassword) throws Exception {
        String[] userAndPasswordEncode = getUsernameAndPasswordFromHeaders(userPassword).split(":");
        String userName = userAndPasswordEncode[0];
        String password = userAndPasswordEncode[1];
       try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password, List.of(Authorities.AUTHORITY)));
       }
       catch (DisabledException e) {
           throw new Exception("USER_DISABLED", e);
       }
       catch (BadCredentialsException e) {
           throw new Exception("INVALID_CREDENTIALS", e);
       }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = jwtTokenProvider.getRefreshToken(userDetails.getUsername());


        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .body(refreshToken);
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> controllerRefreshToken(HttpServletRequest request) {
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtTokenProvider.getRefreshToken(expectedMap.get("sub").toString());
        return ResponseEntity.ok(token);

    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    private String getUsernameAndPasswordFromHeaders(String header) throws Exception {
        if(StringUtils.hasText(header) && header.contains("Basic ")) {
            String userNameBase64 = header.split(" ")[1];
            return new String(Base64.getDecoder().decode(userNameBase64.getBytes()));
        }
        throw new Exception("NO VALID CREDITS IN HEADERS");
    }

}
