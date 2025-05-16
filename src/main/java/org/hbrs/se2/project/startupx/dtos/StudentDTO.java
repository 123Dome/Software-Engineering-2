package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private Long user;
    private Integer matrikelnr;
    private Long studiengang;
    private String steckbrief;
    private List<Long> skills;
    private Set<Long> startups;
}
