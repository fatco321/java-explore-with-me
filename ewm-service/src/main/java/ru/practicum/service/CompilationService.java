package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.storage.CompilationRepository;
import ru.practicum.storage.EventRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    public CompilationDto create(NewCompilationDto newCompilationDto) {
        if (compilationRepository.existsByTitle(newCompilationDto.getTitle())) {
            throw new ConflictException("Compilation with title: " + newCompilationDto.getTitle() + " already exist");
        }
        Set<Event> events = new HashSet<>();
        for (Long eventId : newCompilationDto.getEvents()) {
            events.add(eventRepository.findById(eventId).orElseThrow(() ->
                    new NotFoundException("event with id: " + eventId + " not found")));
        }
        return mapper.toCompilationDto(compilationRepository
                .save(mapper.fromNewDto(newCompilationDto, events)));
    }

    public CompilationDto getById(Long id) {
        return mapper.toCompilationDto(compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation with id:" + id + " not found")));
    }

    public CompilationDto update(Long id, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation with id:" + id + " not found"));
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }

        if (newCompilationDto.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        return mapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public void delete(Long id) {
        compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation with id:" + id + " not found"));
        compilationRepository.deleteById(id);
    }

    public Collection<CompilationDto> getAll(PageRequest pageRequest, Boolean pinned) {
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageRequest).stream()
                    .map(mapper::toCompilationDto).collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(pageRequest).stream()
                    .map(mapper::toCompilationDto).collect(Collectors.toList());
        }
    }
}
