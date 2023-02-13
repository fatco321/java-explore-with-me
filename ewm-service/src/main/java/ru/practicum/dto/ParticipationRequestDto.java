package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.etc.Status;

import java.time.LocalDateTime;

import static ru.practicum.etc.util.TimePattern.TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
}
