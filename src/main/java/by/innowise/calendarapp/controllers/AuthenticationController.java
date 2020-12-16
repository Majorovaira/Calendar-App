package by.innowise.calendarapp.controllers;


import by.innowise.calendarapp.model.User;
import by.innowise.calendarapp.security.JwtTokenProvider;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
        String token = jwtTokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
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
}
