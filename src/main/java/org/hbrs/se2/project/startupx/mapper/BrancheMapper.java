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
//    BrancheMapper INSTANCE = Mappers.getMapper(BrancheMapper.class);
//
//    Branche mapToBranche(BrancheDTO brancheDTO);

    public static BrancheDTO toDTO(Branche branche) {
        if (branche == null) return null;

        BrancheDTO brancheDTO = new BrancheDTO();
        brancheDTO.setId(branche.getId());
        brancheDTO.setBezeichnung(branche.getBezeichnung());
        return brancheDTO;
    }
}
