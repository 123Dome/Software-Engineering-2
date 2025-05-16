package org.hbrs.se2.project.startupx.dtos;

import lombok.*;
import org.hbrs.se2.project.startupx.entities.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolleDTO {
    private Long id;
    private String bezeichnung;
}
