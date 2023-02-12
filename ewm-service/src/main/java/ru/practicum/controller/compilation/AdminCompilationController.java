package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.etc.util.Create;
import ru.practicum.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Validated(Create.class) NewCompilationDto compilationDto) {
        log.info("create new compilation: {}", compilationDto);
        return service.create(compilationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id) {
        log.info("delete compilation with id: {}", id);
        service.delete(id);
    }

    @PatchMapping("/{id}")
    private CompilationDto update(@PathVariable Long id,
                                  @RequestBody NewCompilationDto compilationDto) {
        log.info("update compilation with id: {} to {}", id, compilationDto);
        return service.update(id, compilationDto);
    }
}
