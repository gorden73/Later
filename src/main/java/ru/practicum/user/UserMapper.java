package ru.practicum.user;

import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class UserMapper {

    public static UserDto toDTO(User user) {
        if (user.getRegistrationDate() != null) {
            return new UserDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRegistrationDate(),
                    user.getDateOfBirthday(),
                    user.getState());
        } else {
            return new UserDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getDateOfBirthday(),
                    user.getState());
        }
    }

    public static User toUser(UserDto dto) {
        return new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getRegistrationDate(),
                dto.getDateOfBirthday(),
                dto.getState());
    }
}
