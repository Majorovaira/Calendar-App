package by.innowise.calendarapp.services;
import by.innowise.calendarapp.model.User;
import by.innowise.calendarapp.repositories.UserRepository;
import by.innowise.calendarapp.security.JwtTokenProvider;
import by.innowise.calendarapp.services.ifaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider provider;
    private final AuthenticationManager manager;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider provider, AuthenticationManager manager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.provider = provider;
        this.manager = manager;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    @Override
    public User register(User user) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));

    //    user.setPassword(passwordEncoder.encode(user.getPassword()));
        User regUser = userRepository.save(user);
        return regUser;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return  userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
