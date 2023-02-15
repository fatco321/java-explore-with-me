package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.EventService;
import ru.practicum.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.etc.util.TimePattern.TIME_PATTERN;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService service;
    private final StatsService statsService;

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable Long id, HttpServletRequest request) {
        log.info("public get event with id:{}", id);
        statsService.setHits(request.getRequestURI(), request.getRemoteAddr());
        return service.getById(id);
    }

    @GetMapping
    public Collection<EventShortDto> getAll(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @FutureOrPresent @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        statsService.setHits(request.getRequestURI(), request.getRemoteAddr());
        PageRequest pageRequest = PageRequest.of(from, size);
        if (sort != null) {
            Sort sorting;
            switch (sort) {
                case "EVENT_DATE":
                    sorting = Sort.by(Sort.Direction.DESC, "eventDate");
                    break;
                case "VIEWS":
                    sorting = Sort.by(Sort.Direction.DESC, "views");
                    break;
                default:
                    sorting = Sort.by(Sort.Direction.DESC, "id");
            }
            pageRequest = PageRequest.of(from, size, sorting);

        }
        log.info("public get all events with param: text: {}, categories:{}, paid: {}, range start: {}, range end: {}, only available: {}," +
                "sort: {}, from: {}, size: {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return service.getAll(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, pageRequest);
    }
}
