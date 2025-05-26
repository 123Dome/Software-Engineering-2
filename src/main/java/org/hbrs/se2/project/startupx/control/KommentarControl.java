package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.KommentarException;
import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(KommentarControl.class);

    @Autowired
    private KommentarRepository kommentarRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Transactional
    public KommentarDTO createKommentar(KommentarDTO kommentarDTO) {
        User user = userRepository.findById(kommentarDTO.getUser()).orElse(null);
        if (user == null) {
            logger.error("Kommentar konnte nicht erstellt werden: Nutzer mit ID {} nicht gefunden", kommentarDTO.getUser());
            throw new KommentarException("Nutzer nicht gefunden.");
        }

        Startup startup = startupRepository.findById(kommentarDTO.getStartup()).orElse(null);
        if (startup == null) {
            logger.error("Kommentar konnte nicht erstellt werden: Startup mit ID {} nicht gefunden", kommentarDTO.getStartup());
            throw new KommentarException("Startup nicht gefunden.");
        }

        Kommentar neuesKommentar = KommentarMapper.mapToKommentar(kommentarDTO, user, startup);
        Kommentar savedKommentar = kommentarRepository.save(neuesKommentar);
        logger.info("Kommentar {} erstellt", savedKommentar.getId());
        return KommentarMapper.mapToKommentarDto(savedKommentar);
    }

    public List<KommentarDTO> getAllKommentarZuStartup(StartupDTO startupDTO) {
        List<Kommentar> kommentarList = kommentarRepository.findByStartup_id(startupDTO.getId());
        List<KommentarDTO> kommentarDTOList = new ArrayList<>();

        for(Kommentar kommentar : kommentarList) {
            kommentarDTOList.add(KommentarMapper.mapToKommentarDto(kommentar));
        }

        logger.info("Kommentare für Startup '{}' geladen", startupDTO.getName());
        return kommentarDTOList;
    }

    public KommentarDTO updateKommentar(KommentarDTO kommentarDTO) {
        Kommentar altesKommentar = kommentarRepository.findById(kommentarDTO.getId()).orElse(null);
        if (altesKommentar == null) {
            logger.error("Update fehlgeschlagen: Kommentar mit ID {} nicht gefunden", kommentarDTO.getId());
            throw new KommentarException("Kommentar nicht gefunden.");
        }

        altesKommentar.setKommentar(kommentarDTO.getKommentar());
        altesKommentar.setUpdated(kommentarDTO.getUpdated());
        altesKommentar.setBewertung(kommentarDTO.getBewertung());

        kommentarRepository.save(altesKommentar);
        logger.info("Kommentar {} aktualisiert", altesKommentar.getId());
        return KommentarMapper.mapToKommentarDto(altesKommentar);
    }

    public boolean deleteKommentar(KommentarDTO kommentarDTO) {
        try {
            kommentarRepository.deleteById(kommentarDTO.getId());
            logger.info("Kommentar {} gelöscht", kommentarDTO.getId());
            return true;
        } catch (Exception e) {
            logger.error("Löschen fehlgeschlagen für Kommentar {}: {}", kommentarDTO.getId(), e.getMessage());
            return false;
        }
    }
}
