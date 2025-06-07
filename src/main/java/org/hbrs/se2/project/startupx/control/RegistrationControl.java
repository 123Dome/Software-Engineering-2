package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.RegistrationException;
import org.hbrs.se2.project.startupx.dtos.InvestorDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.hbrs.se2.project.startupx.mapper.InvestorMapper;
import org.hbrs.se2.project.startupx.mapper.StudentMapper;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RegistrationControl {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationControl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private StudiengangRepository studiengangRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private BrancheRepository brancheRepository;

    public User registerUser(UserDTO userDTO) {
        List<String> errors = new ArrayList<>();

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            logger.warn("Registrierung fehlgeschlagen: E-Mail existiert bereits ({})", userDTO.getEmail());
            errors.add("E-Mail existiert bereits.");
        }

        if (userRepository.findByNutzername(userDTO.getNutzername()) != null) {
            logger.warn("Registrierung fehlgeschlagen: Nutzername existiert bereits ({})", userDTO.getNutzername());
            errors.add("Nutzername existiert bereits.");
        }

        Rolle defaultRolle = rolleRepository.findByBezeichnung("user");
        if (defaultRolle == null) {
            logger.error("Registrierung fehlgeschlagen: Standardrolle 'user' nicht gefunden.");
            errors.add("Rolle 'user' nicht gefunden.");
        }

        if (!errors.isEmpty()) {
            logger.error("Registrierung abgebrochen wegen: {}", errors);
            throw new RegistrationException(errors);
        }

        Set<Rolle> defaultRollen = new LinkedHashSet<>();
        defaultRollen.add(defaultRolle);

        Set<Kommentar> kommentarList = new LinkedHashSet<>();
        Student student = null;
        Investor investor = null;

        User newUser = UserMapper.mapToUser(userDTO, defaultRollen, kommentarList, student, investor);
        newUser.getRollen().add(defaultRolle);

        return userRepository.save(newUser);
    }

    @Transactional
    public void registerStudent(UserDTO userDTO, StudentDTO studentDTO) {
        User newUser = registerUser(userDTO);
        List<String> errors = new ArrayList<>();

        if (studentRepository.findByMatrikelnr(studentDTO.getMatrikelnr()) != null) {
            logger.warn("Studentenregistrierung fehlgeschlagen: Matrikelnummer existiert bereits ({})", studentDTO.getMatrikelnr());
            errors.add("Matrikelnr existiert bereits.");
        }

        Set<Skill> skillSet = new LinkedHashSet<>();
        for (Long skillId : studentDTO.getSkills()) {
            skillRepository.findById(skillId)
                    .ifPresentOrElse(
                            skillSet::add,
                            () -> {
                                logger.warn("Studentenregistrierung fehlgeschlagen: Skill mit ID {} nicht gefunden.", skillId);
                                errors.add("Skill mit ID " + skillId + " nicht gefunden.");
                            }
                    );
        }

        Studiengang studiengang = null;
        try {
            studiengang = studiengangRepository.getReferenceById(studentDTO.getStudiengang());
        } catch (Exception e) {
            logger.error("Studentenregistrierung fehlgeschlagen: Studiengang mit ID {} nicht gefunden.", studentDTO.getStudiengang());
            errors.add("Studiengang mit ID " + studentDTO.getStudiengang() + " nicht gefunden.");
        }

        if (!errors.isEmpty()) {
            logger.error("Studentenregistrierung abgebrochen wegen: {}", errors);
            throw new RegistrationException(errors);
        }

        Set<Bewerbung> bewerbungSet = new LinkedHashSet<>();
        Set<Startup> startupSet = new LinkedHashSet<>();

        Student newStudent = StudentMapper.mapToStudent(studentDTO, bewerbungSet, skillSet, startupSet, newUser, studiengang);

        logger.info("Registrierung erfolgreich: {}", newStudent);
        studentRepository.save(newStudent);
    }

    @Transactional
    public void registerInvestor(UserDTO userDTO, InvestorDTO investorDTO) {
        User newUser = registerUser(userDTO);
        List<String> errors = new ArrayList<>();

        Branche branche = null;
        try {
            branche = brancheRepository.getReferenceById(investorDTO.getBrancheId());
        } catch (Exception e){
            logger.error("Branche mit ID {} nicht gefunden.", investorDTO.getBrancheId());
            errors.add("Branche mit ID " + investorDTO.getBrancheId() + " nicht gefunden.");
        }

        if (!errors.isEmpty()) {
            logger.error("Investorenregistrierung abgebrochen wegen: {}", errors);
            throw new RegistrationException(errors);
        }

        Investor newInvestor = InvestorMapper.mapToInvestor(investorDTO, newUser, branche);

        logger.info("Registrierung erfolgreich: {}", newInvestor);
        investorRepository.save(newInvestor);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
