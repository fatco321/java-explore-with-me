package ru.practicum.controller.like;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.service.LikeService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/raiting")
@RequiredArgsConstructor
public class PublicLikeController {
    private final LikeService service;

    @GetMapping("/users")
    public Collection<UserShortDto> getPopularUsers(
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        return service.getMostPopularEventsInitiator(PageRequest.of(from, size));
    }

    @GetMapping("/events")
    public Collection<EventShortDto> getPopularEvents(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                      @Positive @RequestParam(defaultValue = "10") int size) {
        return service.getMostPopularEvents(from, size);
    }
}
