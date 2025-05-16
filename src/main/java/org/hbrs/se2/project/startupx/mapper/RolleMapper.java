package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.RolleDTO;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class RolleMapper {

    public static RolleDTO mapToDto(Rolle rolle) {
        if (rolle == null) {
            return null;
        }

        RolleDTO dto = RolleDTO.builder()
                .id(rolle.getId())
                .bezeichnung(rolle.getBezeichnung())
                .build();

        return dto;
    }

    public static Rolle mapToRolle(RolleDTO dto) {
        if (dto == null) {
            return null;
        }

        Rolle rolle = Rolle.builder()
                .id(dto.getId())
                .bezeichnung(dto.getBezeichnung())
                .build();

        return rolle;
    }
}
