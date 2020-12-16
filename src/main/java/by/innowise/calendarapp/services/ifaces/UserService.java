package by.innowise.calendarapp.services.ifaces;


import by.innowise.calendarapp.model.User;

import java.util.List;

public interface UserService {

   User getUserById(long id);
   User getUserByName(String name);
   User register(User user);
   Iterable<User> getAllUsers();
   void delete(Long id);

}
