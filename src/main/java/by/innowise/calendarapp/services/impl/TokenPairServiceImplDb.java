package by.innowise.calendarapp.services.impl;

import by.innowise.calendarapp.entities.TokenPair;
import by.innowise.calendarapp.entities.User;
import by.innowise.calendarapp.repositories.TokenPairRepository;
import by.innowise.calendarapp.repositories.UserRepository;
import by.innowise.calendarapp.security.utils.JwtTokenProvider;
import by.innowise.calendarapp.services.TokenPairService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.oauth2.resourceserver.OAuth2ResourceServerSecurityMarker;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class TokenPairServiceImplDb implements TokenPairService {


    @Autowired
    private TokenPairRepository tokenPairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean validateRefreshToken(String userName, String refreshToken) {
        return tokenPairRepository.getRefreshTokenByUserName(userName).equals(refreshToken);
    }

    @Override
    public boolean validateAccessToken(String userName, String accessToken) {
        return tokenPairRepository.getAccessTokenByUserName(userName).equals(accessToken);
    }

    @Override
    public String getAccessTokenByUserName(String userName) {
        return tokenPairRepository.getAccessTokenByUserName(userName);
    }

    @Override
    public String getRefreshTokenByUserName(String userName) {
        return tokenPairRepository.getRefreshTokenByUserName(userName);
    }

    @Override
    public void updateAccessAndRefreshToken(String userName, String accessToken, String refreshToken) {
        tokenPairRepository.updateAccessTokenAndRefreshToken(accessToken, refreshToken, userName);
    }

    @Override
    public boolean validateRefreshTokenAndUpdateIfValid(String oldRefreshToken) {
        if(jwtTokenProvider.validateToken(oldRefreshToken)) {
            String userName = jwtTokenProvider.getUsernameFromToken(oldRefreshToken);
            String accessToken = jwtTokenProvider.generateAccessToken(userName);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userName);
           updateAccessAndRefreshToken(userName, accessToken, newRefreshToken);
           return true;
        }
        return false;
    }

    @Override
    public void saveTokenPair(String userName, String accessToken, String refreshToken) {
        final TokenPair tokenPair = new TokenPair();
        final User user = userRepository.getUserByName(userName);
        tokenPair.setUser(user);
        tokenPair.setAccessToken(accessToken);
        tokenPair.setRefreshToken(refreshToken);
        tokenPairRepository.save(tokenPair);
    }


}
