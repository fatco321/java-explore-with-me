package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.entity.Event;
import ru.practicum.entity.Request;
import ru.practicum.entity.User;
import ru.practicum.etc.State;
import ru.practicum.etc.Status;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.RequestRepository;
import ru.practicum.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Collection<ParticipationRequestDto> getUserRequests(Long userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::fromRequest).collect(Collectors.toList());
    }

    public ParticipationRequestDto create(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("event with id:" + eventId + " not found");
        });
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("user with id:" + userId + " not found");
                });
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("user was initiator");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("event not publisher");
        }
        List<Request> requests = requestRepository.findAllByEvent_IdIs(eventId);
        if (requests.stream().anyMatch(r -> r.getRequester().getId().equals(userId))) {
            throw new ConflictException("user already sent request");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requests.size()) {
            throw new ConflictException("event was max limit");
        }
        Request request = requestMapper.createRequest(requester, event);
        return requestMapper.fromRequest(requestRepository.save(request));
    }

    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() ->
                new NotFoundException("request not found"));
        request.setStatus(Status.CANCELED);
        return requestMapper.fromRequest(requestRepository.save(request));
    }
}
