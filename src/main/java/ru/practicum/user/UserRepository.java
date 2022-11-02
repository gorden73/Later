package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);
}