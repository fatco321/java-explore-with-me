package ru.practicum.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.Event;
import ru.practicum.etc.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findAllByIdIn(List<Long> events);

    List<Event> findAll(Specification<Event> specification, Pageable pageable);

    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndState_InAndCategory_IdInAndEventDateBetween(Collection<Long> initiatorId, Collection<State> state, Collection<Long> categoryId,
                                                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.id = ?2 and e.initiator.id = ?1")
    Optional<Event> findAllByInitiatorIdAndId(Long userId, Long eventId);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
}
