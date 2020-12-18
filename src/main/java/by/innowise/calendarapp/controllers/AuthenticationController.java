package by.innowise.calendarapp.controllers;


import by.innowise.calendarapp.security.utils.Authorities;
import by.innowise.calendarapp.security.CustomUserServiceDetails;
import by.innowise.calendarapp.security.utils.JwtTokenProvider;
import by.innowise.calendarapp.services.TokenPairService;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenPairService tokenPairService;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestHeader("Authorization") String userPassword) throws Exception {
           log.info("start authent");
           String[] userAndPasswordEncode = getUsernameAndPasswordFromHeaders(userPassword).split(":");
           String userName = userAndPasswordEncode[0];
           String password = userAndPasswordEncode[1];
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password, List.of(Authorities.AUTHORITY)));

           log.info("start generate tokens");
           String accessToken = jwtTokenProvider.generateAccessToken(userName);
           String refreshToken = jwtTokenProvider.generateRefreshToken(userName);
         tokenPairService.saveTokenPair(userName, accessToken, refreshToken);
           return ResponseEntity.ok()
                   .header(HttpHeaders.AUTHORIZATION, accessToken)
                   .body(refreshToken);




    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> controllerRefreshToken(@RequestBody String refreshToken) throws AuthenticationException {
        log.info("start refresh");
        String userName = jwtTokenProvider.getUsernameFromToken(refreshToken);
        if(tokenPairService.validateRefreshTokenAndUpdateIfValid(refreshToken)) {
           return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, tokenPairService.getAccessTokenByUserName(userName))
                    .body(tokenPairService.getRefreshTokenByUserName(userName));
        }
        throw new AuthenticationException("your refresh token gavno");

    }


    private String getUsernameAndPasswordFromHeaders(String header) throws Exception {
        if(StringUtils.hasText(header) && header.contains("Basic ")) {
            String userNameBase64 = header.split(" ")[1];
            return new String(Base64.getDecoder().decode(userNameBase64.getBytes()));
        }
        throw new Exception("NO VALID CREDITS IN HEADERS");
    }

}
