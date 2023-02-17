package ru.practicum.controller.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.LikeService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/")
@RequiredArgsConstructor
@Slf4j
public class PrivateLikeController {
    private final LikeService likeService;

    @PatchMapping("/like/{eventId}")
    public EventShortDto addLike(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("user with id {} to like event {}", userId, eventId);
        return likeService.addLike(userId, eventId);
    }

    @DeleteMapping("/like/{eventId}")
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long eventId) {
        log.info("user {} delete like event {}", userId, eventId);
        likeService.deleteLike(userId, eventId);
    }

    @GetMapping("/like")
    public Collection<EventShortDto> getUserLikesEvents(@PathVariable Long userId) {
        log.info("user with id {} get liked events", userId);
        return likeService.getUserLikesEvents(userId);
    }

    @GetMapping("/dislike")
    public Collection<EventShortDto> getUserDislikesEvents(@PathVariable Long userId) {
        log.info("user with id {} get disliked event", userId);
        return likeService.getUserDislikesEvents(userId);
    }

    @PatchMapping("/dislike/{eventId}")
    public EventShortDto addDislike(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("user {} dislike event {}", userId, eventId);
        return likeService.addDislike(userId, eventId);
    }

    @DeleteMapping("/dislike/{eventId}")
    public void deleteDisLike(@PathVariable Long userId,
                              @PathVariable Long eventId) {
        log.info("user {} delete dislike event {}", userId, eventId);
        likeService.deleteDislike(userId, eventId);
    }
}
