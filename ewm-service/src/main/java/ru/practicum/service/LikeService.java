package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.entity.Event;
import ru.practicum.entity.User;
import ru.practicum.etc.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.UserRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;


    public EventShortDto addLike(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            if (event.getLikes().contains(user)) {
                throw new ConflictException("User already like this event");
            }
            user.getDislikedEvents().remove(event);
            user.getLikedEvents().add(event);
            event.getDislikes().remove(user);
            event.getLikes().add(user);
            incrementUserRaiting(event.getInitiator());
            userRepository.save(user);
            return eventMapper.toShortEvent(event);
        } else {
            throw (new ConflictException("event not published"));
        }
    }

    public void deleteLike(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        user.getLikedEvents().remove(event);
        decrementUserRaiting(event.getInitiator());
        userRepository.save(user);
    }


    public EventShortDto addDislike(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            if (event.getDislikes().contains(user)) {
                throw new ConflictException("user already dislike this event");
            }
            event.getLikes().remove(user);
            event.getDislikes().add(user);
            user.getLikedEvents().remove(event);
            user.getDislikedEvents().add(event);
            decrementUserRaiting(event.getInitiator());
            userRepository.save(user);
            return eventMapper.toShortEvent(event);
        } else {
            throw (new ConflictException("event not published"));
        }
    }

    public void deleteDislike(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        user.getDislikedEvents().remove(event);
        incrementUserRaiting(event.getInitiator());
        userRepository.save(user);
    }

    public Collection<EventShortDto> getUserLikesEvents(Long userId) {
        User user = getUser(userId);
        return user.getLikedEvents().stream().map(eventMapper::toShortEvent).collect(Collectors.toList());
    }

    public Collection<EventShortDto> getUserDislikesEvents(Long userId) {
        User user = getUser(userId);
        return user.getDislikedEvents().stream().map(eventMapper::toShortEvent).collect(Collectors.toList());
    }

    public List<EventShortDto> getMostPopularEvents(int from, int size) {
        return eventRepository.findAllByState(State.PUBLISHED, PageRequest.of(from, size)).stream()
                .map(eventMapper::toShortEvent).sorted(Comparator.comparingLong(EventShortDto::getRaiting).reversed())
                .collect(Collectors.toList());
    }

    public List<UserShortDto> getMostPopularEventsInitiator(PageRequest pageRequest) {
        return userRepository.findAllSortByOrderByRaitingDesc(pageRequest).stream()
                .map(UserMapper::fromUserToShortUser).collect(Collectors.toList());
    }

    private void incrementUserRaiting(User initiator) {
        initiator.setRaiting(initiator.getRaiting() + 1);
        userRepository.save(initiator);
    }

    private void decrementUserRaiting(User initiator) {
        initiator.setRaiting(initiator.getRaiting() - 1);
        userRepository.save(initiator);
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("event with id: " + eventId + " not found"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("user with id: " + userId + " not found"));
    }
}
