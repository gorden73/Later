package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.UserState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@TestPropertySource("/test.properties")
class UserServiceImplTest {

    private final UserService service;

    @Test
    void getAllUsers() {
        List<UserDto> sourceUsers = new ArrayList<>();
        UserDto user1 = new UserDto(2L,
                "Pablo1",
                "Picasso1",
                "picasso1@mail.ru",
                LocalDate.of(1991, 12, 12),
                UserState.ACTIVE);
        service.saveUser(user1);
        UserDto user2 = new UserDto(3L,
                "Pablo2",
                "Picasso2",
                "picasso2@mail.ru",
                LocalDate.of(1992, 12, 12),
                UserState.ACTIVE);
        service.saveUser(user2);
        UserDto user3 = new UserDto(4L,
                "Pablo3",
                "Picasso3",
                "picasso3@mail.ru",
                LocalDate.of(1993, 12, 12),
                UserState.ACTIVE);
        service.saveUser(user3);
        sourceUsers.add(user1);
        sourceUsers.add(user2);
        sourceUsers.add(user3);
        List<UserDto> userList = service.getAllUsers();
        assertThat(userList, hasSize(3));
        for (UserDto sourceUser : sourceUsers) {
            assertThat(userList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("firstName", equalTo(sourceUser.getFirstName())),
                    hasProperty("lastName", equalTo(sourceUser.getLastName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void saveUser() {
        UserDto userDto = new UserDto(1L,
                "PabloDto",
                "PicassoDto",
                "picassoDto@mail.ru",
                LocalDate.of(1990, 12, 12),
                null);
        service.saveUser(userDto);

        UserDto userDtoByEmail = service.getUserByEmail(userDto.getEmail());

        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getFirstName(), equalTo(userDtoByEmail.getFirstName()));
        assertThat(userDto.getLastName(), equalTo(userDtoByEmail.getLastName()));
        assertThat(userDto.getEmail(), equalTo(userDtoByEmail.getEmail()));
        assertThat(UserState.ACTIVE, equalTo(userDtoByEmail.getState()));
        assertThat(userDto.getRegistrationDate(), notNullValue());
    }
}
