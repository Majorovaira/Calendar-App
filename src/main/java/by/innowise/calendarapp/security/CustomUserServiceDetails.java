package by.innowise.calendarapp.security;

import by.innowise.calendarapp.entities.User;
import by.innowise.calendarapp.services.UserService;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserServiceDetails implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.ofNullable(userService.getUserByName(s));
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            log.info(String.valueOf(user));

            return org.springframework.security.core.userdetails.User.withUsername(user.getName())
                    .password(user.getPassword())
                    .authorities(Authorities.AUTHORITY)
                    .passwordEncoder(new BCryptPasswordEncoder(12)::encode)
                    .build();
        }
        else {
            throw new UsernameNotFoundException("USER WITH NAME " + s + " NOT FOUND");
        }
    }
}
