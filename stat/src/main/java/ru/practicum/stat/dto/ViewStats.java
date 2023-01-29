package ru.practicum.stat.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;
}
