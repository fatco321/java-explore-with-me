package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.entity.*;
import ru.practicum.etc.State;
import ru.practicum.etc.StateActionAdmin;
import ru.practicum.etc.StateActionUser;
import ru.practicum.etc.Status;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.storage.*;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.etc.Status.CONFIRMED;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventMapper mapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("invalid event date");
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id: " + userId + " not found"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new NotFoundException("Category with id: " + newEventDto.getCategory() + " not found"));
        Location location = locationRepository.save(newEventDto.getLocation());
        Event event = eventRepository.save(mapper.toEvent(newEventDto, category, user, location));
        return mapper.toEventFull(event);
    }

    public EventFullDto getById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Event with id: " + id + " not found"));
        return mapper.toEventFull(event);
    }

    @Transactional
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateUserEvent eventRequestDto) {
        Event event = eventRepository.findAllByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException("event with id: " + eventId + " not found"));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("event is published");
        }
        if (eventRequestDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventRequestDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("category with id:" + eventRequestDto.getCategory() + " not found"));
            event.setCategory(category);
        }
        if (eventRequestDto.getEventDate() != null) {
            if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("invalid event date");
            }
        }
        if (eventRequestDto.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
            event.setState(State.PENDING);
        }

        if (eventRequestDto.getStateAction() == StateActionUser.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        }
        if (eventRequestDto.getAnnotation() != null) {
            event.setAnnotation(eventRequestDto.getAnnotation());
        }
        if (eventRequestDto.getDescription() != null) {
            event.setDescription(eventRequestDto.getDescription());
        }
        if (eventRequestDto.getTitle() != null) {
            event.setTitle(eventRequestDto.getTitle());
        }

        if (eventRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(eventRequestDto.getRequestModeration());
        }
        if (eventRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        }

        if (eventRequestDto.getPaid() != null) {
            event.setPaid(eventRequestDto.getPaid());
        }
        if (eventRequestDto.getLocation() != null) {
            event.setLocation(eventRequestDto.getLocation());
        }
        return mapper.toEventFull(event);
    }

    @Transactional
    public EventFullDto updateAdmin(Long eventId, UpdateEventAdminRequest eventRequestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("event with id: " + eventId + " not found"));
        if (eventRequestDto.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
            if (event.getState() != State.PENDING) {
                throw new ConflictException("Event with id=" + eventId + " cannot be published");
            }
            LocalDateTime published = LocalDateTime.now();
            event.setPublishedOn(published);
            event.setState(State.PUBLISHED);
        }

        if (eventRequestDto.getStateAction() == StateActionAdmin.REJECT_EVENT) {
            if (event.getState() == State.PUBLISHED && event.getPublishedOn().isBefore(LocalDateTime.now())) {
                throw new ConflictException("Event with id=" + eventId + " cannot be rejected");
            }
            event.setState(State.CANCELED);
        }
        if (eventRequestDto.getAnnotation() != null) {
            event.setAnnotation(eventRequestDto.getAnnotation());
        }

        if (eventRequestDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventRequestDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("category not found")));
        }

        if (eventRequestDto.getDescription() != null) {
            event.setDescription(eventRequestDto.getDescription());
        }

        if (eventRequestDto.getEventDate() != null) {
            if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("event date is not correct");
            }
            event.setEventDate(eventRequestDto.getEventDate());
        }

        if (eventRequestDto.getLocation() != null) {
            locationRepository.save(eventRequestDto.getLocation());
            event.setLocation(eventRequestDto.getLocation());
        }

        if (eventRequestDto.getPaid() != null) {
            event.setPaid(eventRequestDto.getPaid());
        }

        if (eventRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        }

        if (eventRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(eventRequestDto.getRequestModeration());
        }

        if (eventRequestDto.getTitle() != null) {
            event.setTitle(eventRequestDto.getTitle());
        }

        event = eventRepository.save(event);
        return mapper.toEventFull(event);
    }

    public List<EventShortDto> getAll(String text, List<Long> categories,
                                      Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Boolean onlyAvailable, PageRequest pageRequest) {
        LocalDateTime start = Objects.requireNonNullElseGet(rangeStart, () -> LocalDateTime.of(1970, 12, 2, 0, 0));
        LocalDateTime end = Objects.requireNonNullElseGet(rangeEnd, () -> LocalDateTime.of(3000, 12, 2, 0, 0));
        Specification<Event> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<Request> requestRoot = subQuery.from(Request.class);
            Join<Request, Event> eventsRequests = requestRoot.join("event");

            predicates.add(builder.equal(root.get("state"), State.PUBLISHED));
            if (text != null && !text.isEmpty()) {
                predicates.add(builder.or(builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
            }
            if (categories != null) {
                predicates.add(builder.and(root.get("category").in(categories)));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }
            predicates.add(builder.greaterThan(root.get("eventDate"), start));
            predicates.add(builder.lessThan(root.get("eventDate"), end));
            if (onlyAvailable != null && onlyAvailable) {
                predicates.add(builder.or(builder.equal(root.get("participantLimit"), 0),
                        builder.and(builder.notEqual(root.get("participantLimit"), 0),
                                builder.greaterThan(root.get("participantLimit"), subQuery.select(builder.count(requestRoot.get("id")))
                                        .where(builder.equal(eventsRequests.get("id"), requestRoot.get("event").get("id")))
                                        .where(builder.equal(requestRoot.get("status"), CONFIRMED))))));

            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return eventRepository.findAll(specification, pageRequest).stream().map(mapper::toShortEvent).collect(Collectors.toList());
    }

    public List<EventFullDto> getAllByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            PageRequest pageRequest) {
        return eventRepository.findAllByInitiator_IdInAndState_InAndCategory_IdInAndEventDateBetween(users,
                states, categories, rangeStart, rangeEnd, pageRequest).stream().map(mapper::toEventFull).collect(Collectors.toList());
    }

    public Collection<EventFullDto> getByUser(Long userId, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorId(userId, pageRequest).stream()
                .map(mapper::toEventFull).collect(Collectors.toList());
    }

    public EventFullDto getByUserAndEvent(Long userId, Long eventId) {
        return mapper.toEventFull(eventRepository.findAllByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException("event with id: " + eventId + " not found")));
    }

    public Collection<ParticipationRequestDto> getParticipationRequestsUser(Long userId, Long eventId) {
        return requestRepository.findAllByRequesterIdAndEventId(userId, eventId).stream()
                .map(requestMapper::fromRequest).collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id : " + eventId + " not found"));
        List<Request> requests = requestRepository.findRequestsForUpdate(eventId, eventRequestStatusUpdateRequest.getRequestIds());
        return getResult(requests, Status.valueOf(eventRequestStatusUpdateRequest.getStatus()), event);

    }

    private EventRequestStatusUpdateResult getResult(List<Request> requests, Status status, Event event) {
        switch (status) {
            case CONFIRMED:
                return confirmed(requests, event);
            case REJECTED:
                return reject(requests);
            default:
                return new EventRequestStatusUpdateResult();
        }

    }

    private EventRequestStatusUpdateResult confirmed(List<Request> requests, Event event) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        for (Request request : requests) {
            try {
                validRequestStatus(request);
            } catch (ConflictException e) {
                continue;
            }
            if (event.getParticipantLimit() - requestRepository.findConfirmedRequests(event.getId(), CONFIRMED) <= 0) {
                throw new ConflictException("event was max limit");
            } else {
                request.setStatus(CONFIRMED);
                request = requestRepository.save(request);
                result.getConfirmedRequests().add(requestMapper.fromRequest(request));
            }
        }
        return result;
    }

    private EventRequestStatusUpdateResult reject(List<Request> requests) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        for (Request request : requests) {
            validRequestStatus(request);
            request.setStatus(Status.REJECTED);
            request = requestRepository.save(request);
            result.getRejectedRequests().add(requestMapper.fromRequest(request));
        }
        return result;
    }

    private void validRequestStatus(Request request) throws ConflictException {
        if (!request.getStatus().equals(Status.PENDING)) {
            throw new ConflictException("request status not pending");
        }
    }
}