package org.hbrs.se2.project.startupx.dtos;

import lombok.*;
import org.hbrs.se2.project.startupx.util.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StellenausschreibungDTO {
    private Long id;
    private Long startup;
    private String titel;
    private String beschreibung;
    private Set<Long> skills;
    private List<Long> bewerbungen;
    private Status status;
}
