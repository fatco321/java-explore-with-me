package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        try {
            return UserMapper.fromUser(userRepository.save(UserMapper.fromDtoToUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("User with email: " + userDto.getEmail() + " already exist");
        }
    }

    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id: " + id + " not found"));
        userRepository.deleteById(id);
    }

    public Collection<UserDto> getUsers(List<Long> usersId, PageRequest pageRequest) {
        return userRepository.findByIdIsIn(usersId, pageRequest)
                .stream().map(UserMapper::fromUser).collect(Collectors.toList());
    }
}
