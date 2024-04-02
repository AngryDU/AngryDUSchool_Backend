package com.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;
import java.util.UUID;

import static com.school.service.impl.UserServiceImpl.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;

    @NotBlank(message = EMAIL_NOT_CORRECT)
    @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
    @Pattern(message = "Bad formed person name: ${validatedValue}",
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+$")
    @Column(name = "email")
    private String email;

    @NotBlank(message = PASSWORD_NOT_CORRECT)
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @NotBlank(message = NAME_NOT_CORRECT)
    @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = NAME_NOT_CORRECT)
    @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "subject")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "goal")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "about_yourself")
    private String aboutYourself;

    @Column(name = "is_active")
    private boolean isActive;

    public enum Status {
        STUDENT, TUTOR, ADMINISTRATOR
    }

    public enum Subject {
        RUSSIAN, ENGLISH, PSYCHOLOGY, IT, FRENCH, GERMAN, MATHS, BIOLOGY, HISTORY, PROGRAMMING, SPANISH, ITALIAN, CHINESE
    }

    public enum Level {
        ALL_LEVELS, BEGINNER, ELEMENTARY, INTERMEDIATE, UPPER_INTERMEDIATE, ADVANCED, PROFICIENT, NATIVE
    }

    public enum Goal {
        ALL_GOALS, SCHOOL, TOURISM, COLLEGE, INTERNATIONAL_EXAMS, WORK, UNIVERSITY
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && status == user.status && subject == user.subject && level == user.level && goal == user.goal && Objects.equals(phone, user.phone) && Objects.equals(address, user.address) && Objects.equals(aboutYourself, user.aboutYourself);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, status, subject, level, goal, phone, address, aboutYourself, isActive);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", subject=" + subject +
                ", level=" + level +
                ", goal=" + goal +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", aboutYourself='" + aboutYourself + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
