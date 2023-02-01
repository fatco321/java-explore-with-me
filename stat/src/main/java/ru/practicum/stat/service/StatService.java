package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitInputDto;
import ru.practicum.stat.dto.ViewStatsEndpointDto;
import ru.practicum.stat.entity.EndpointHit;
import ru.practicum.stat.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatService {
    private final StatRepository repository;

    public void createEndpointHit(EndpointHitInputDto endpointHitInputDto) {
        repository.save(new EndpointHit(endpointHitInputDto.getApp(),
                endpointHitInputDto.getUri(), endpointHitInputDto.getIp(), endpointHitInputDto.getTimestamp()));
    }

    public List<ViewStatsEndpointDto> getStat(LocalDateTime startTime, LocalDateTime endTime, List<String> urls, Boolean unique) {
        List<ViewStatsEndpointDto> result = new ArrayList<>();
        if (unique) {
            for (String uri : urls) {
                ViewStatsEndpointDto viewStatsEndpointDto = new ViewStatsEndpointDto(repository.findAppByUri(uri), uri,
                        repository.getHitCountUnique(startTime, endTime, uri));
                result.add(viewStatsEndpointDto);
            }
        } else {
            for (String uri : urls) {
                ViewStatsEndpointDto viewStatsEndpointDto = new ViewStatsEndpointDto(repository.findAppByUri(uri), uri,
                        repository.getHitCountAll(startTime, endTime, uri));
                result.add(viewStatsEndpointDto);
            }
        }
        result.sort(Comparator.comparing(ViewStatsEndpointDto::getHits).reversed());
        return result;
    }
}
