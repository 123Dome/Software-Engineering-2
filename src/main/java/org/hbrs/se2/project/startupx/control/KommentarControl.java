package org.hbrs.se2.project.startupx.control;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.KommentarMapper;
import org.hbrs.se2.project.startupx.repository.KommentarRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KommentarControl {

    private static Logger logger = LoggerFactory.getLogger(KommentarControl.class);

    @Autowired
    private KommentarRepository kommentarRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;
//
//    @Transactional
//    public KommentarDTO createKommentar(KommentarDTO kommentarDTO, UserDTO userDTO, StartupDTO startupDTO) {
//
//        User user = userRepository.findById(userDTO.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Branche " + userDTO.getId() + " not found"));
//
//        Startup startup = startupRepository.findById(startupDTO.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Startup " + startupDTO.getId() + " not found"));
//
//        kommentarDTO.setStartup(startupDTO);
//        kommentarDTO.setUser(userDTO);
//
//        kommentarRepository.save(KommentarMapper.INSTANCE.mapToKommentar(kommentarDTO));
//
//        return kommentarDTO;
//    }
}
