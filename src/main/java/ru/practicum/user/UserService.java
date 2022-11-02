package ru.practicum.user;

import ru.practicum.user.dto.UserDto;

import java.util.List;

interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserByEmail(String email);

    UserDto saveUser(UserDto user);
}