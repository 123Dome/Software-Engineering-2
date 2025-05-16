package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrancheDTO {
    private Long id;
    private String bezeichnung;
    private List<Long> startups = new ArrayList<>();
}
