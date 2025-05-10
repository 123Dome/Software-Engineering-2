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
    private Integer id;
    private StudentDTO student;
    private StellenausschreibungDTO stellenausschreibungen;
    private String bewerbungsschreiben;
}
