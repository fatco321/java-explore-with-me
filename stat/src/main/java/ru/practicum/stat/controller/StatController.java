package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.ViewStats;
import ru.practicum.stat.entity.EndpointHit;
import ru.practicum.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    public void createEndpointHit(@Validated @RequestBody EndpointHit endpointHit) {
        log.info("create endpoint hit: {}", endpointHit);
        service.createEndpointHit(endpointHit);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam List<String> uris,
                                         @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Get statistic with parameters: start{}, end{}, uris{}, unique{}", start, end, uris, unique);
        return service.getStat(start, end, uris, unique);
    }
}