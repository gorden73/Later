package ru.practicum.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.UserState;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                "John",
                "Doe",
                "john.doe@mail.com",
                LocalDate.of(1990, 12, 12),
                UserState.ACTIVE);

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
        assertThat(result).extractingJsonPathStringValue("$.dateOfBirthday").isEqualTo("12-12-1990");
    }
}
