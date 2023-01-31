package ru.practicum.stat.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ViewStatsEndpointDto {
    private String app;
    private String uri;
    private Integer hits;
}
