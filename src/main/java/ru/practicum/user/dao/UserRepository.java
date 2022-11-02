package ru.practicum.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);
}