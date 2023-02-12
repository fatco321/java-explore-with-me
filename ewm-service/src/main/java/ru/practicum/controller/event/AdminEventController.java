package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.etc.State;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.etc.util.TimePattern.TIME_PATTERN;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService service;

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("admin update event with id: {} to {}", eventId, updateEventAdminRequest);
        return service.updateAdmin(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public Collection<EventFullDto> getAll(@RequestParam(defaultValue = "") Set<Long> users,
                                           @RequestParam(defaultValue = "") Set<String> states,
                                           @RequestParam(defaultValue = "") Set<Long> categories,
                                           @RequestParam(defaultValue = "") @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(defaultValue = "") @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("admin get all events with param: users: {}, states: {}, categories:{}, range start: {}, range end: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getAllByAdmin(new ArrayList<>(users), states.stream().map(State::valueOf).collect(Collectors.toList()),
                new ArrayList<>(categories), rangeStart, rangeEnd, PageRequest.of(from, size));
    }
}
