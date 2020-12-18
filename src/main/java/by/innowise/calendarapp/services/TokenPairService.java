package by.innowise.calendarapp.services;

public interface TokenPairService {

    boolean validateRefreshToken(String userName, String refreshToken);
    boolean validateAccessToken(String userName, String accessToken);
    String getAccessTokenByUserName(String userName);
    String getRefreshTokenByUserName(String userName);
    void updateAccessAndRefreshToken(String userName, String accessToken, String refreshToken);
    boolean validateRefreshTokenAndUpdateIfValid(String oldRefreshToken);

    void saveTokenPair(String userName, String accessToken, String refreshToken);
}
