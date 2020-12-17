package by.innowise.calendarapp.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @CreatedDate
    @Column(name = "created")
    private LocalDate created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDate updated;

    @Column
    private String status;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_tasks",
//    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")
//    }, inverseJoinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")})
//    private List<Task> tasks;

}
