package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.RolleDTO;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class RolleMapper {

    public static RolleDTO mapToDto(Rolle rolle) {
        if (rolle == null) return null;

        RolleDTO dto = new RolleDTO();
        dto.setId(rolle.getId());
        dto.setBezeichnung(rolle.getBezeichnung());
        return dto;
    }

    public Rolle mapToRolle(RolleDTO dto) {
        if (dto == null) return null;

        Rolle rolle = new Rolle();
        rolle.setId(dto.getId());
        rolle.setBezeichnung(dto.getBezeichnung());
        return rolle;
    }
}
