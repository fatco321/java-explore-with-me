package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.entity.Category;
import ru.practicum.entity.Event;
import ru.practicum.entity.Location;
import ru.practicum.entity.User;
import ru.practicum.etc.State;
import ru.practicum.etc.Status;
import ru.practicum.service.StatsService;
import ru.practicum.storage.RequestRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final RequestRepository requestRepository;
    private final StatsService statsService;

    public EventFullDto toEventFull(Event event) {
        Long confirmed = (long) requestRepository.findAllByEventIdAndStatus(event.getId(), Status.CONFIRMED).size();
        Long views = statsService.getViews("/events/" + event.getId());
        return new EventFullDto(
                event.getId(), event.getAnnotation(), CategoryMapper.fromCategory(event.getCategory()),
                confirmed, event.getCreatedOn(), event.getDescription(), event.getEventDate(),
                UserMapper.fromUserToShortUser(event.getInitiator()), event.getLocation(),
                event.getPaid(), event.getParticipantLimit(), event.getPublishedOn(), event.getRequestModeration(),
                event.getState(), event.getTitle(), views);
    }

    public Event toEvent(NewEventDto newEventDto, Category category, User user, Location location) {
        return new Event(newEventDto.getId(), newEventDto.getAnnotation(), category, LocalDateTime.now(),
                user, newEventDto.getDescription(), newEventDto.getTitle(), newEventDto.getEventDate(),
                location, newEventDto.isPaid(), newEventDto.getParticipantLimit(), LocalDateTime.now(), newEventDto.isRequestModeration(),
                State.PENDING, new HashSet<>(), new HashSet<>());
    }

    public EventShortDto toShortEvent(Event event) {
        Long confirmed = (long) requestRepository.findAllByEventIdAndStatus(event.getId(), Status.CONFIRMED).size();
        Long views = statsService.getViews("/events/" + event.getId());
        return new EventShortDto(event.getId(), (long) (event.getLikes().size() - event.getDislikes().size()), event.getTitle(), event.getAnnotation(),
                CategoryMapper.fromCategory(event.getCategory()), confirmed, event.getEventDate(),
                UserMapper.fromUserToShortUser(event.getInitiator()), event.getPaid(),
                views, event.getParticipantLimit(), event.getLikes().stream().map(UserMapper::fromUserToShortUser).collect(Collectors.toSet()),
                event.getDislikes().stream().map(UserMapper::fromUserToShortUser).collect(Collectors.toSet()));
    }
}
