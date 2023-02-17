package ru.practicum.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdIsIn(List<Long> id, Pageable pageable);

    List<User> findAllSortByOrderByRaitingDesc(Pageable pageable);
}
