package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BrancheDto;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrancheMapper {

    BrancheMapper INSTANCE = Mappers.getMapper(BrancheMapper.class);

    Branche mapToBranche(BrancheDto brancheDto);

    BrancheDto mapToBrancheDto(Branche branche);
}
