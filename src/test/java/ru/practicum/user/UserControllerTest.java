package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.UserState;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;
    private UserDto user1;

    private UserDto user2;
    private UserDto user3;

    private List<UserDto> users = new ArrayList<>();

    UserControllerTest() {
    }

    @BeforeEach
    void setUp() {
        user1 = new UserDto(
                1L,
                "John1",
                "Doe1",
                "john.doe1@mail.com",
                LocalDate.of(1991, 12, 12),
                UserState.ACTIVE);
        user2 = new UserDto(
                2L,
                "John2",
                "Doe2",
                "john.doe2@mail.com",
                LocalDate.of(1992, 12, 12),
                UserState.ACTIVE);
        user3 = new UserDto(
                3L,
                "John3",
                "Doe3",
                "john.doe3@mail.com",
                LocalDate.of(1993, 12, 12),
                UserState.ACTIVE);
    }

    @Test
    void getAllUsers() throws Exception {
        users.add(user1);
        users.add(user2);
        when(userService.getAllUsers())
                .thenReturn(users);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(users.size())));
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(user3);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user3.getId()), Long.class))
                .andExpect(jsonPath("$.firstName", is(user3.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user3.getLastName())))
                .andExpect(jsonPath("$.email", is(user3.getEmail())));
    }

    @Test
    void saveNewUserWithException() throws Exception {
        when(userService.saveUser(any()))
                .thenThrow(IllegalArgumentException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }
}