package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.BrancheException;
import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.mapper.BrancheMapper;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BrancheControl {
    private static final Logger logger = LoggerFactory.getLogger(BrancheControl.class);

    @Autowired
    private BrancheRepository brancheRepository;

    public List<BrancheDTO> getAll() {
        List<BrancheDTO> branchenDTOs = new ArrayList<>();
        for (Branche branche : brancheRepository.findAll()) {
            branchenDTOs.add(BrancheMapper.mapToBrancheDTO(branche));
        }
        return branchenDTOs;
    }

    public BrancheDTO getById(Long id) {
        Branche branche = brancheRepository.findById(id).
                orElseThrow(() -> {
                    logger.error("Branche with id {} not found", id);
                return new BrancheException("Branche with id " + id + " not found");
                });
        return BrancheMapper.mapToBrancheDTO(branche);
    }
}
