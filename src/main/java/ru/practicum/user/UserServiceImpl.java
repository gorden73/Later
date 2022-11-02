package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Запрошен список всех пользователей.");
        return repository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDTO) {
        User user = UserMapper.toUser(userDTO);
        user.setRegistrationDate(LocalDateTime.now());
        log.info("Сохранен пользователь email {}.", user.getEmail());
        return UserMapper.toDTO(repository.save(user));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return UserMapper.toDTO(repository.getUserByEmail(email));
    }
}