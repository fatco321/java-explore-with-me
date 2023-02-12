package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.etc.util.Create;
import ru.practicum.service.EventService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody @Validated(Create.class) NewEventDto newEventDto) {
        log.info("user with id: {} create event: {}", userId, newEventDto);
        return service.create(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        log.info("user with id: {}, get event with id: {}", userId, eventId);
        return service.getByUserAndEvent(userId, eventId);
    }

    @GetMapping
    private Collection<EventFullDto> getUserEvents(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("user gets all his events with param: from: {}, size: {}", from, size);
        return service.getByUser(userId, PageRequest.of(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @RequestBody @Validated UpdateUserEvent updateUserEvent) {
        log.info("user with id: {} update event with id: {} to {}", userId, eventId, updateUserEvent);
        return service.updateByUser(userId, eventId, updateUserEvent);
    }

    @GetMapping("/{eventId}/requests")
    private Collection<ParticipationRequestDto> getUserEventRequest(@PathVariable Long userId,
                                                                    @PathVariable Long eventId) {
        log.info("user with id: {} get all his requests to event with id: {}", userId, eventId);
        return service.getParticipationRequestsUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequests(@PathVariable() Long userId,
                                                                  @PathVariable() Long eventId,
                                                                  @RequestBody() EventRequestStatusUpdateRequest request) {
        log.info("user with id: {} update requests to event with id: {} to {}", userId, eventId, request);
        return service.updateRequests(userId, eventId, request);
    }

}
