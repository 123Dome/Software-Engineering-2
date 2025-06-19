package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.InvestorDTO;
import org.hbrs.se2.project.startupx.control.exception.RegistrationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegistrationControlTest {

    @Autowired
    RegistrationControl registrationControl;

    // Test für erfolgreiche User-Registrierung
    @Test
    void registerUser_success() {
        UserDTO userDTO = UserDTO.builder()
                .vorname("Max")
                .nachname("Mustermann")
                .nutzername("testuser123")
                .passwort("geheim123")
                .email("testuser123@email.de")
                .geburtsdatum(LocalDate.of(1990, 1, 1))
                .build();

        assertDoesNotThrow(() -> {
            registrationControl.registerUser(userDTO);
        });
    }

    // Test für doppelte E-Mail bzw. doppelten Nutzernamen (negativer Testfall)
    @Test
    void registerUser_duplicateEmailOrUsername_throwsException() {
        UserDTO userDTO = UserDTO.builder()
                .vorname("Max")
                .nachname("Mustermann")
                .nutzername("dupuser")
                .passwort("geheim123")
                .email("dupuser@email.de")
                .geburtsdatum(LocalDate.of(1990, 1, 1))
                .build();

        // erster Versuch: sollte klappen
        registrationControl.registerUser(userDTO);

        // zweiter Versuch: gleiche Daten => Exception!
        RegistrationException ex = assertThrows(RegistrationException.class, () -> {
            registrationControl.registerUser(userDTO);
        });
        assertTrue(ex.getMessage().contains("existiert bereits"));
    }

    // Test für erfolgreiche Studentenregistrierung
    @Test
    void registerStudent_success() {

        UserDTO userDTO = UserDTO.builder()
                .vorname("Anna")
                .nachname("Testerin")
                .nutzername("studentuser1")
                .passwort("pw123")
                .email("student1@email.de")
                .geburtsdatum(LocalDate.of(1995, 5, 15))
                .build();

        StudentDTO studentDTO = StudentDTO.builder()
                .matrikelnr(12345678)
                .studiengang(1L) // Muss in DB existieren!
                .skills(new HashSet<>())
                .bewerbungen(new HashSet<>())
                .startups(new HashSet<>())
                .build();

        assertDoesNotThrow(() -> {
            registrationControl.registerStudent(userDTO, studentDTO);
        });
    }

    // Test für doppelte Matrikelnummer
    @Test
    void registerStudent_duplicateMatrikelnummer_throwsException() {
        UserDTO userDTO1 = UserDTO.builder()
                .vorname("Test")
                .nachname("Doppel")
                .nutzername("studdup1")
                .passwort("pw1")
                .email("studdup1@email.de")
                .geburtsdatum(LocalDate.of(1997, 3, 7))
                .build();

        UserDTO userDTO2 = UserDTO.builder()
                .vorname("Test")
                .nachname("Doppel2")
                .nutzername("studdup2")
                .passwort("pw2")
                .email("studdup2@email.de")
                .geburtsdatum(LocalDate.of(1998, 4, 8))
                .build();

        StudentDTO studentDTO = StudentDTO.builder()
                .matrikelnr(87654321)
                .studiengang(1L)
                .skills(new HashSet<>())
                .bewerbungen(new HashSet<>())
                .startups(new HashSet<>())
                .build();

        // erster Versuch: sollte klappen
        registrationControl.registerStudent(userDTO1, studentDTO);

        // zweiter Versuch: gleiche Matrikelnummer > Exception!
        RegistrationException ex = assertThrows(RegistrationException.class, () -> {
            registrationControl.registerStudent(userDTO2, studentDTO);
        });
        assertTrue(ex.getMessage().contains("Matrikelnr existiert bereits"));
    }

    // Test für erfolgreiche Investorenregistrierung
    @Test
    void registerInvestor_success() {
        UserDTO userDTO = UserDTO.builder()
                .vorname("Inga")
                .nachname("Investor")
                .nutzername("investoruser1")
                .passwort("pwInvest")
                .email("investor1@email.de")
                .geburtsdatum(LocalDate.of(1980, 8, 8))
                .build();

        InvestorDTO investorDTO = InvestorDTO.builder()
                .brancheId(1L)
                .steckbrief("Kapitalgeber")
                .budget(100000L)
                .build();

        assertDoesNotThrow(() -> {
            registrationControl.registerInvestor(userDTO, investorDTO);
        });
    }
}
