package org.hbrs.se2.project.startupx.control;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
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

import java.util.ArrayList;
import java.util.List;
/**
 * KommentarControl ist für jegliche Interaktion zwischen Nutzer/Views und der Datenbank (durch versch.
 * Repositories) zuständig.
 * Durch die Klasse werden die CRUD-Methoden für die Kommentare ausgeführt.
 * @author Korbinian Gauglitz
 * @version 1.0
 */
@Component
public class KommentarControl {

    private static Logger logger = LoggerFactory.getLogger(KommentarControl.class);

    @Autowired
    private KommentarRepository kommentarRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Transactional
    public KommentarDTO createKommentar(KommentarDTO kommentarDTO) {

        User user = userRepository.findById(kommentarDTO.getUser())
                .orElseThrow(() -> new EntityNotFoundException("Branche " + kommentarDTO.getUser() + " not found"));

        Startup startup = startupRepository.findById(kommentarDTO.getStartup())
                .orElseThrow(() -> new EntityNotFoundException("Startup " + kommentarDTO.getStartup() + " not found"));


        Kommentar neuesKommentar = Kommentar.builder()
                .kommentar(kommentarDTO.getKommentar())
                .bewertung(kommentarDTO.getBewertung())
                .updated(kommentarDTO.getUpdated())
                .erstellungsdatum(kommentarDTO.getErstellungsdatum())
                .user(user)
                .startup(startup)
                .build();

        Kommentar savedKommentar = kommentarRepository.save(neuesKommentar);

        logger.info("Kommentar " + neuesKommentar.getId() + " erstellt");
        return KommentarMapper.mapToKommentarDto(savedKommentar);
    }

    public List<KommentarDTO> getAllKommentarZuStartup(StartupDTO startupDTO) {

        List<Kommentar> kommentarList = kommentarRepository.findByStartup_id(startupDTO.getId());

        List<KommentarDTO> kommentarDTOList = new ArrayList<>();

        for(Kommentar kommentar : kommentarList) {
            kommentarDTOList.add(KommentarMapper.mapToKommentarDto(kommentar));
        }

        logger.info("Kommentare zu Startup: " + startupDTO.getName() + " erstellt");
        return kommentarDTOList;
    }

    public KommentarDTO updateKommentar(KommentarDTO kommentarDTO) {

        Kommentar altesKommentar = kommentarRepository.findById(kommentarDTO.getId()).get();

        altesKommentar.setKommentar(kommentarDTO.getKommentar());
        altesKommentar.setUpdated(kommentarDTO.getUpdated());
        altesKommentar.setBewertung(kommentarDTO.getBewertung());

        kommentarRepository.save(altesKommentar);

        logger.info("Kommentar " + altesKommentar.getId() + " aktualisiert");
        return KommentarMapper.mapToKommentarDto(altesKommentar);
    }

    public boolean deleteKommentar(KommentarDTO kommentarDTO) {

        try {
            kommentarRepository.deleteById(kommentarDTO.getId());
            logger.info("Kommentar " + kommentarDTO.getId() + " gelöscht");
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
