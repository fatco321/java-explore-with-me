package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatClient;
import ru.practicum.dto.EndpointHitInputDto;
import ru.practicum.dto.ViewStatsEndpointDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.etc.util.TimePattern.TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class StatsService {
    private static final String APP_NAME = "ewm-service";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private final StatClient client;

    public Long getViews(String uri) {
        String start = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).format(formatter);
        String end = LocalDateTime.now().plusYears(1000).format(formatter);
        List<ViewStatsEndpointDto> listStats = client.getStats(start, end, new String[]{uri}, false);
        if (listStats != null && listStats.size() > 0) {
            listStats = listStats.stream()
                    .filter(x -> APP_NAME.equals(x.getApp()))
                    .collect(Collectors.toList());
            return listStats.size() > 0 ? listStats.get(0).getHits() : 0L;
        } else {
            return 0L;
        }
    }

    public void setHits(String uri, String ip) {
        EndpointHitInputDto endpointHit = new EndpointHitInputDto(APP_NAME, uri, ip, LocalDateTime.now());
        client.addToStat(endpointHit);
    }

}