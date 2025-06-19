package org.hbrs.se2.project.startupx.control;

import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.control.exception.LoginException;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.hbrs.se2.project.startupx.util.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationControl {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationControl.class);

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        logger.info("Authentifizierungsversuch für Nutzer: {}", username);

        UserDTO userDTO;
        try {
            userDTO = this.getUserWithJPA(username, password);
        } catch (LoginException e) {
            logger.error("Fehler beim Abrufen des Nutzers: {}", e.getMessage());
            return false;
        }

        if (userDTO == null) {
            logger.warn("Authentifizierung fehlgeschlagen: Benutzername oder Passwort ungültig für Nutzer: {}", username);
            return false;
        }

        VaadinSession.getCurrent().setAttribute(Globals.CURRENT_USER, userDTO);
        logger.info("Authentifizierung erfolgreich für Nutzer: {}", username);
        return true;
    }

    public UserDTO getCurrentUser() {
        return (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
    }

    private UserDTO getUserWithJPA(String username, String password) {
        try {
            return UserMapper.mapToUserDto(userRepository.findUserByNutzernameAndPasswort(username, password));
        } catch (Exception e) {
            throw new LoginException("Fehler beim Zugriff auf die Benutzerdatenbank: " + e.getMessage());
        }
    }
}
