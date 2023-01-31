package ru.practicum.stat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

import static ru.practicum.stat.util.TimePattern.TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitInputDto {
    @NotNull
    private String app;
    @NotNull
    @Column(name = "uri", length = 512)
    private String uri;
    @NotNull
    private String ip;
    @NotNull
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime timestamp;
}
