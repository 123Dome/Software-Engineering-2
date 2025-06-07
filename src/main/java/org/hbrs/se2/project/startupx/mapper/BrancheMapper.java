package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.springframework.stereotype.Component;

@Component
public class BrancheMapper {

    public static BrancheDTO mapToBrancheDTO(Branche branche) {
        if (branche == null) {
            return null;
        }

        BrancheDTO brancheDTO = BrancheDTO.builder()
                .id(branche.getId())
                .bezeichnung(branche.getBezeichnung())
                .build();

        return brancheDTO;
    }

    public static Branche mapToBranche(BrancheDTO brancheDTO) {

        return Branche.builder()
                .bezeichnung(brancheDTO.getBezeichnung())
                .build();
    }

}
