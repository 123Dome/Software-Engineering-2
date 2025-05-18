package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.hbrs.se2.project.startupx.repository.KommentarRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class BrancheMapper {

    public static BrancheDTO mapToBrancheDto(Branche branche) {
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
