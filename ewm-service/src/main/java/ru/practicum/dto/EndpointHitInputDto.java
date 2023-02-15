package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static ru.practicum.etc.util.TimePattern.TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitInputDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = TIME_PATTERN)
    private LocalDateTime timestamp;
}
