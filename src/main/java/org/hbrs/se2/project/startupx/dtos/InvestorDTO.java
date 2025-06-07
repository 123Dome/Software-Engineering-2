package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestorDTO {
    private Long id;
    private Long userId;
    private Long brancheId;
    private String steckbrief;
    private Long budget;
}
