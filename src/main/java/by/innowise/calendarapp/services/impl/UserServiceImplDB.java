package by.innowise.calendarapp.services.impl;

import by.innowise.calendarapp.entities.User;
import by.innowise.calendarapp.repositories.UserRepository;
import by.innowise.calendarapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImplDB implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImplDB(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public Iterable<User> getAllUsers() {
        return  userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
