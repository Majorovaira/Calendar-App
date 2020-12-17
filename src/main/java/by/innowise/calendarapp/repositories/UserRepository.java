package by.innowise.calendarapp.repositories;

import by.innowise.calendarapp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User getUserByName(String name);
    User getUserById(long id);
}
