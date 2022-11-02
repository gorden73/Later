package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.user.model.UserState;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime registrationDate;

    private UserState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirthday;

    public UserDto(Long id, String firstName, String lastName, String email, LocalDate dateOfBirthday,
                   UserState state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
        this.dateOfBirthday = dateOfBirthday;
        this.state = state;
    }

    public UserDto(Long id, String firstName, String lastName, String email, LocalDateTime registrationDate,
                   LocalDate dateOfBirthday, UserState state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.dateOfBirthday = dateOfBirthday;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        return id != null && id.equals(((UserDto) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
