package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation fromNewDto(NewCompilationDto newCompilationDto, Set<Event> eventSet) {
        return Compilation.builder()
                .id(null)
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(eventSet)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.getPinned(),
                compilation.getEvents().stream().map(eventMapper::toShortEvent).collect(Collectors.toSet()));
    }
}
