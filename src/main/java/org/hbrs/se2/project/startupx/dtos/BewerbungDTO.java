package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BewerbungDTO {
    private Long id;
    private Long student;
    private Long stellenausschreibungen;
    private String bewerbungsschreiben;
}
