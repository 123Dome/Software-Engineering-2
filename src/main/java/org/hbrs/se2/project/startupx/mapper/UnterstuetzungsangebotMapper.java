package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.UnterstuetzungsangebotDTO;
import org.hbrs.se2.project.startupx.entities.Investor;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Unterstuetzungsangebot;

public class UnterstuetzungsangebotMapper {

    public static UnterstuetzungsangebotDTO mapToUnterstuetzungsangebotDTO(Unterstuetzungsangebot unterstuetzungsangebot) {
        if(unterstuetzungsangebot == null){
            return null;
        }

        return UnterstuetzungsangebotDTO.builder()
                .id(unterstuetzungsangebot.getId())
                .investor(unterstuetzungsangebot.getInvestor().getId())
                .startup(unterstuetzungsangebot.getStartup().getId())
                .betrag(unterstuetzungsangebot.getBetrag())
                .build();
    }

    public static Unterstuetzungsangebot mapToUnterstuetzungsangebot(UnterstuetzungsangebotDTO unterstuetzungsangebotDTO, Investor investor, Startup startup) {
        if(unterstuetzungsangebotDTO == null){
            return null;
        }

        return Unterstuetzungsangebot.builder()
                .investor(investor)
                .startup(startup)
                .betrag(unterstuetzungsangebotDTO.getBetrag())
                .build();
    }
}
