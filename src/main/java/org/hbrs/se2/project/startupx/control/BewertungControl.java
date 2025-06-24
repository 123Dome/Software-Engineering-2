package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.BewertungException;
import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.entities.Bewertung;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.BewertungMapper;
import org.hbrs.se2.project.startupx.repository.BewertungRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BewertungControl {

    private static final Logger logger = LoggerFactory.getLogger(BewertungControl.class);

    @Autowired
    private BewertungRepository bewertungRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Transactional
    public void createBewertung(BewertungDTO bewertungDTO) {
        User user = userRepository.findById(bewertungDTO.getUser()).orElse(null);
        if (user == null) {
            logger.warn("User mit der ID {} nicht gefunden.", bewertungDTO.getUser());
            throw new BewertungException("User mit der ID " + bewertungDTO.getUser() + " nicht gefunden.");
        }

        Startup startUp = startupRepository.findById(bewertungDTO.getStartup()).orElse(null);
        if (startUp == null) {
            logger.warn("Startup mit der ID {} nicht gefunden.", bewertungDTO.getStartup());
            throw new BewertungException("Startup mit der ID " + bewertungDTO.getStartup() + " nicht gefunden.");
        }

        Bewertung bewertung = BewertungMapper.mapToBewertung(bewertungDTO, user, startUp);
        try {
            bewertungRepository.save(bewertung);
            logger.info("Bewertung mit der ID {} gespeichert.", bewertung.getId());
        } catch (Exception e) {
            logger.error("Fehler beim Speichern der Bewertung: {}", e.getMessage());
            throw new BewertungException("Fehler beim Speichern der Bewertung: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteBewertung(Long bewertungId) {
        try{
            bewertungRepository.deleteById(bewertungId);
            logger.info("Bewertung mit der ID {} erfolgreich gelöscht.", bewertungId);
        } catch (Exception e) {
            logger.error("Fehler beim Löschen der Bewertung: {}", e.getMessage());
            throw new BewertungException(e.getMessage());
        }
    }

    public List<BewertungDTO> getAlleBewertungZuStartup(Long startupId) {
        try {
            List<Bewertung> bewertungen = bewertungRepository.findAllByStartupId(startupId);
            List<BewertungDTO> bewertungDTOs = new ArrayList<>();
            for (Bewertung bewertung : bewertungen) {
                bewertungDTOs.add(BewertungMapper.mapToBewertungDTO(bewertung));
            }
            return bewertungDTOs;
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Bewertungen für Startup-ID: {}", startupId, e);
            throw new BewertungException("Bewertungen für dieses Startup konnten nicht geladen werden.", e);
        }
    }

    public List<BewertungDTO> getAlleBewertungZuUser(Long userId) {
        try {
            List<Bewertung> bewertungen = bewertungRepository.findAllByUserId(userId);
            List<BewertungDTO> bewertungDTOs = new ArrayList<>();
            for (Bewertung bewertung : bewertungen) {
                bewertungDTOs.add(BewertungMapper.mapToBewertungDTO(bewertung));
            }
            return bewertungDTOs;
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Bewertungen für User-ID: {}", userId, e);
            throw new BewertungException("Bewertungen für diesen User konnten nicht geladen werden.", e);
        }
    }

    public double getDurchschnittlicheBewertungZuStartup(Long startupId) {
        try{
            List<Bewertung> bewertungen = bewertungRepository.findAllByStartupId(startupId);
            return bewertungen.stream()
                    .mapToDouble(Bewertung::getBewertung)
                    .average()
                    .orElse(0.0);
        } catch (Exception e) {
            logger.error("Fehler beim Berechnen der durchschnittlichen Bewertung für Startup-ID: {}", startupId, e);
            throw new BewertungException("Durchschnittliche Bewertung konnte nicht berechnet werden.", e);
        }
    }

    public String getUserNameById(Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                logger.warn("User mit der ID {} nicht gefunden.", userId);
                return "Unbekannter Nutzer";
            }
            return user.getNutzername();
        } catch (Exception e) {
            logger.error("Fehler beim Laden des Nutzernamens für User-ID: {}", userId, e);
            throw new BewertungException("Fehler beim Laden des Nutzernamens.", e);
        }
    }

}
