package ru.practicum.dto;

import lombok.*;
import ru.practicum.etc.util.Create;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank(groups = {Create.class})
    private String title;
    private Boolean pinned = false;
    private List<Long> events;
}
