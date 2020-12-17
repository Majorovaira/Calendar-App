package by.innowise.calendarapp.security;

import by.innowise.calendarapp.entities.User;
import by.innowise.calendarapp.security.utils.Authorities;
import by.innowise.calendarapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserServiceDetails implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String s) {
        log.info("load user");
        User user = userService.getUserByName(s);


            return org.springframework.security.core.userdetails.User.withUsername(user.getName())
                    .password(user.getPassword())
                    .authorities(Authorities.AUTHORITY)
                    .passwordEncoder(new BCryptPasswordEncoder(12)::encode)
                    .build();
        }


}
