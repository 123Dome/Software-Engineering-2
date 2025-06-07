package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static User mapToUser(UserDTO userDTO, Set<Rolle> rollen, Set<Kommentar> kommentare, Student student, Investor investor) {

        return User.builder()
                .vorname(userDTO.getVorname())
                .nachname(userDTO.getNachname())
                .geburtsdatum(userDTO.getGeburtsdatum())
                .nutzername(userDTO.getNutzername())
                .passwort(userDTO.getPasswort())
                .email(userDTO.getEmail())
                .rollen(rollen)
                .kommentare(kommentare)
                .student(student)
                .investor(investor)
                .build();
    }

    public static UserDTO mapToUserDto(User user) {
        if (user == null) {
            return null;
        }

        Set<Long> rollenIds = new LinkedHashSet<>();

        if (user.getRollen() != null) {
            rollenIds = user.getRollen().stream()
                    .map(Rolle::getId)
                    .collect(Collectors.toSet());
        }

        Long studentId = null;
        if (user.getStudent() != null) {
            studentId = user.getStudent().getId();
        }

        Long investorId = null;
        if (user.getInvestor() != null) {
            investorId = user.getInvestor().getId();
        }


        Set<Long> kommentare = new LinkedHashSet<>();
        if (user.getKommentare() != null) {
            kommentare = user.getKommentare().stream()
                    .map(Kommentar::getId)
                    .collect(Collectors.toSet());
        }

        return  UserDTO.builder()
                .id(user.getId())
                .vorname(user.getVorname())
                .nachname(user.getNachname())
                .passwort(user.getPasswort())
                .nutzername(user.getNutzername())
                .email(user.getEmail())
                .geburtsdatum(user.getGeburtsdatum())
                .rollen(rollenIds)
                .kommentare(kommentare)
                .student(studentId)
                .investor(investorId)
                .build();
    }
}
