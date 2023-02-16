package ru.practicum.controller.like;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.LikeService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/")
@RequiredArgsConstructor
public class PrivateLikeController {
    private final LikeService likeService;

    @PatchMapping("/like/{eventId}")
    public EventShortDto toLike(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return likeService.toLike(userId, eventId);
    }

    @DeleteMapping("/like/{eventId}")
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long eventId) {
        likeService.deleteLike(userId, eventId);
    }

    @GetMapping("/like")
    public Collection<EventShortDto> getUserLikesEvents(@PathVariable Long userId) {
        return likeService.getUserLikesEvents(userId);
    }

    @GetMapping("/dislike")
    private Collection<EventShortDto> getUserDislikesEvents(@PathVariable Long userId) {
        return likeService.getUserDislikesEvents(userId);
    }

    @PatchMapping("/dislike/{eventId}")
    public EventShortDto toDislike(@PathVariable Long userId,
                                   @PathVariable Long eventId) {
        return likeService.toDislike(userId, eventId);
    }

    @DeleteMapping("/dislike/{eventId}")
    public void deleteDisLike(@PathVariable Long userId,
                              @PathVariable Long eventId) {
        likeService.deleteDislike(userId, eventId);
    }
}
