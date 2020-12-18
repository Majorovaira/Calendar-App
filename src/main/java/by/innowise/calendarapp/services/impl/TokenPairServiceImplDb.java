package by.innowise.calendarapp.services.impl;

import by.innowise.calendarapp.repositories.TokenPairRepository;
import by.innowise.calendarapp.services.TokenPairService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class TokenPairServiceImplDb implements TokenPairService {

    private TokenPairRepository tokenPairRepository;

    @Autowired
    public TokenPairServiceImplDb(TokenPairRepository tokenPairRepository) {
        this.tokenPairRepository = tokenPairRepository;
    }

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
}
