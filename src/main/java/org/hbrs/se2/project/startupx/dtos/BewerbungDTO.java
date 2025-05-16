package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BewerbungDTO {
    private Long id;
    private Long student;
    private Long stellenausschreibungen;
    private String bewerbungsschreiben;
}
