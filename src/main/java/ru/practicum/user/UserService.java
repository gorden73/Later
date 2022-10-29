package ru.practicum.user;

import java.util.List;

interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserByEmail(String email);

    UserDto saveUser(UserDto user);
}