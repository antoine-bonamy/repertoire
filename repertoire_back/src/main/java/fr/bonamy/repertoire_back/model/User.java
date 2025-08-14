package fr.bonamy.repertoire_back.model;

import fr.bonamy.repertoire_back.dto.User.UserDetailDto;
import fr.bonamy.repertoire_back.dto.User.UserFormDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    public static User of(UserFormDto dto) {
        User user = new User();
        if (dto instanceof UserDetailDto) {
             user.setId(((UserDetailDto) dto).getId());
        }
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

}
