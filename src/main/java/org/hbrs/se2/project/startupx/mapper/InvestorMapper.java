package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.InvestorDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.entities.Investor;
import org.hbrs.se2.project.startupx.entities.Unterstuetzungsangebot;
import org.hbrs.se2.project.startupx.entities.User;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InvestorMapper {

    public static InvestorDTO mapToInvestorDto(Investor investor) {
        if (investor == null) {
            return null;
        }

        Set<Long> unterstuetzungsangebotIds = new LinkedHashSet<>();
        if(investor.getUnterstuetzungsangebote() != null) {
            unterstuetzungsangebotIds = investor.getUnterstuetzungsangebote()
                    .stream()
                    .map(Unterstuetzungsangebot::getId)
                    .collect(Collectors.toSet());
        }

        return InvestorDTO.builder()
                .id(investor.getId())
                .userId(investor.getUser().getId())
                .brancheId(investor.getBranche().getId())
                .steckbrief(investor.getSteckbrief())
                .budget(investor.getBudget())
                .unterstuetzungsangebote(unterstuetzungsangebotIds)
                .build();
    }

    public static Investor mapToInvestor(InvestorDTO investorDTO, User user, Branche branche, Set<Unterstuetzungsangebot> unterstuetzungsangebote) {
        if (investorDTO == null) {
            return null;
        }

        return Investor.builder()
                .user(user)
                .branche(branche)
                .steckbrief(investorDTO.getSteckbrief())
                .budget(investorDTO.getBudget())
                .unterstuetzungsangebote(unterstuetzungsangebote)
                .build();
    }
}
