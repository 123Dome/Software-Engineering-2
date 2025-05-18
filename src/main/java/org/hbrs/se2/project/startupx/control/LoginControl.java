package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.DatabaseUserException;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LoginControl {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    private UserDTO userDTO = null;

    public boolean authentificate(String username, String password ) throws DatabaseUserException {
        // Standard: User wird mit Spring JPA ausgelesen (Was sind die Vorteile?)
        UserDTO tmpUser = this.getUserWithJPA( username , password );

        // Alternative: Auslesen des Users mit JDBC (Was sind die Vorteile bzw. Nachteile?)
        // UserDTO tmpUser = this.getUserWithJDBC( username , password );

        if ( tmpUser == null ) {
            // ggf. hier ein Loggin einfügen
            return false;
        }
        this.userDTO = tmpUser;
        return true;
    }

    public UserDTO getCurrentUser(){
        return this.userDTO;

    }

    private UserDTO getUserWithJPA( String username , String password ) throws DatabaseUserException {
        UserDTO userTmp;
        try {
            userTmp = userMapper.mapToUserDto(repository.findUserByNutzernameAndPasswort(username, password));
        } catch ( org.springframework.dao.DataAccessResourceFailureException e ) {
            // Analyse und Umwandlung der technischen Errors in 'lesbaren' Darstellungen (ToDo!)
           throw new DatabaseUserException("A failure occured while trying to connect to database with JPA");
        }
        return userTmp;
    }

}
