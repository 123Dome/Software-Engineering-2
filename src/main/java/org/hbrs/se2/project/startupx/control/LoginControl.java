package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.LoginException;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginControl {

    private static final Logger logger = LoggerFactory.getLogger(LoginControl.class);

    @Autowired
    private UserRepository repository;

    private UserDTO userDTO = null;

    public boolean authenticate(String username, String password) {
        logger.info("Authentifizierungsversuch für Nutzer: {}", username);

        UserDTO tmpUser;
        try {
            tmpUser = this.getUserWithJPA(username, password);
        } catch (LoginException e) {
            logger.error("Fehler beim Abrufen des Nutzers: {}", e.getMessage());
            return false;
        }

        if (tmpUser == null) {
            logger.warn("Authentifizierung fehlgeschlagen: Benutzername oder Passwort ungültig für Nutzer: {}", username);
            return false;
        }

        this.userDTO = tmpUser;
        logger.info("Authentifizierung erfolgreich für Nutzer: {}", username);
        return true;
    }

    public UserDTO getCurrentUser() {
        return this.userDTO;
    }

    private UserDTO getUserWithJPA(String username, String password) {
        try {
            return UserMapper.mapToUserDto(repository.findUserByNutzernameAndPasswort(username, password));
        } catch (Exception e) {
            throw new LoginException("Fehler beim Zugriff auf die Benutzerdatenbank: " + e.getMessage());
        }
    }
}
