package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.EndpointHitInputDto;
import ru.practicum.dto.ViewStatsEndpointDto;

import java.util.List;

@FeignClient(value = "stat", url = "http://localhost:9090")
public interface StatClient {

    @GetMapping("/stats?start={start}&end={end}&uris={uris}&unique={unique}")
    List<ViewStatsEndpointDto> getStats(@PathVariable String start, @PathVariable String end,
                                        @PathVariable String[] uris, @PathVariable boolean unique);

    @PostMapping("/hit")
    void addToStat(@RequestBody EndpointHitInputDto endpointHitInputDto);
}
