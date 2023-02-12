package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.entity.Event;
import ru.practicum.entity.Request;
import ru.practicum.entity.User;
import ru.practicum.etc.Status;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    public Request createRequest(User user, Event event) {
        return new Request(null, LocalDateTime.now(), event, user, event.getRequestModeration() ? Status.PENDING : Status.CONFIRMED);
    }

    public ParticipationRequestDto fromRequest(Request request) {
        return new ParticipationRequestDto(request.getId(), request.getCreated(), request.getEvent().getId(),
                request.getRequester().getId(), request.getStatus());
    }
}
