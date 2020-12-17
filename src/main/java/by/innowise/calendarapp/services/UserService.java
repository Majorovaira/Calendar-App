package by.innowise.calendarapp.services;


import by.innowise.calendarapp.entities.User;

public interface UserService {

   User getUserById(long id);
   User getUserByName(String name);
   Iterable<User> getAllUsers();
   void delete(Long id);

}
