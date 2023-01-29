package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.ViewStats;
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

    public void createEndpointHit(EndpointHit endpointHit) {
        repository.save(endpointHit);
    }

    public List<ViewStats> getStat(LocalDateTime startTime, LocalDateTime endTime, List<String> urls, Boolean unique) {
        List<ViewStats> result = new ArrayList<>();
        if (unique) {
            for (String uri : urls) {
                ViewStats viewStats = new ViewStats(repository.findAppByUri(uri), uri,
                        repository.getHitCountUnique(startTime, endTime, uri));
                result.add(viewStats);
            }
        } else {
            for (String uri : urls) {
                ViewStats viewStats = new ViewStats(repository.findAppByUri(uri), uri,
                        repository.getHitCountAll(startTime, endTime, uri));
                result.add(viewStats);
            }
        }
        result.sort(Comparator.comparing(ViewStats::getHits).reversed());
        return result;
    }
}
