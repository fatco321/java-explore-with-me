package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
