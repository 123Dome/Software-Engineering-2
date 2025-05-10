package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrancheMapper {

    BrancheMapper INSTANCE = Mappers.getMapper(BrancheMapper.class);

    Branche mapToBranche(BrancheDTO brancheDto);

    BrancheDTO mapToBrancheDto(Branche branche);
}
