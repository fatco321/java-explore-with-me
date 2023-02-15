package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.EndpointHitInputDto;
import ru.practicum.stat.dto.ViewStatsEndpointDto;
import ru.practicum.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.stat.util.TimePattern.TIME_FORMAT;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndpointHit(@Validated @RequestBody EndpointHitInputDto endpointHitInputDto) {
        log.info("create endpoint hit: {}", endpointHitInputDto);
        service.createEndpointHit(endpointHitInputDto);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsEndpointDto> getStat(@RequestParam @DateTimeFormat(pattern = TIME_FORMAT) LocalDateTime start,
                                                    @RequestParam @DateTimeFormat(pattern = TIME_FORMAT) LocalDateTime end,
                                                    @RequestParam List<String> uris,
                                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Get statistic with parameters: start{}, end{}, uris{}, unique{}", start, end, uris, unique);
        return service.getStat(start, end, uris, unique);
    }
}