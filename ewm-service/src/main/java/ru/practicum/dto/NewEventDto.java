package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.entity.Location;
import ru.practicum.etc.util.Create;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class NewEventDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 2000, min = 20)
    private String annotation;
    @NotNull(groups = {Create.class})
    private Long category;
    @NotBlank(groups = {Create.class})
    @Size(max = 7000, min = 20)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(groups = {Create.class})
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration;
    @NotBlank(groups = {Create.class})
    @Size(max = 120, min = 3)
    private String title;
}
