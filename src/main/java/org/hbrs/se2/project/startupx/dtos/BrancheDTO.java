package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hbrs.se2.project.startupx.entities.Startup;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrancheDTO {

    private Integer id;
    private String bezeichnung;
    private List<Startup> startups;
}
