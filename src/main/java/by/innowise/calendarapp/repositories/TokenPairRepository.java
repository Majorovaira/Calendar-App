package by.innowise.calendarapp.repositories;

import by.innowise.calendarapp.entities.TokenPair;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenPairRepository extends CrudRepository<TokenPair, Long> {

    @Query("SELECT t FROM TokenPair t JOIN t.user u ON u.name=?1")
    TokenPair getTokenPairByUserName(String userName);

    @Query("SELECT t.refreshToken FROM TokenPair t JOIN t.user u ON u.name=?1")
    String getRefreshTokenByUserName(String userName);

    @Query("select t.accessToken from TokenPair t join t.user u on u.name=?1")
    String getAccessTokenByUserName(String userName);
}
