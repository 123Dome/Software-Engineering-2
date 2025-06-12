package org.hbrs.se2.project.startupx.dtos;

import lombok.*;
import org.hbrs.se2.project.startupx.util.BewerbungsStatus;

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
    private BewerbungsStatus status;
}
