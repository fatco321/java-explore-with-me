package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService service;

    @GetMapping
    public Collection<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("get all compilation with param: pinned: {}, from: {}, size: {}", pinned, from, size);
        return service.getAll(PageRequest.of(from, size), pinned);
    }

    @GetMapping("/{id}")
    private CompilationDto getById(@PathVariable Long id) {
        log.info("get compilation with id: {}", id);
        return service.getById(id);
    }
}
