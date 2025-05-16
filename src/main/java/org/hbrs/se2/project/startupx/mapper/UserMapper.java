package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.hbrs.se2.project.startupx.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
//    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//
//    User mapToUser(UserDTO userDTO);


    public UserDTO mapToUserDto(User user) {
        if (user == null) {
            return null;
        }

        Set<Long> rollenIds = new LinkedHashSet<>();

        if (user.getRollen() != null) {
            rollenIds = user.getRollen().stream()
                    .map(Rolle::getId)
                    .collect(Collectors.toSet());
        }

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .vorname(user.getVorname())
                .nachname(user.getNachname())
                .passwort(user.getPasswort())
                .nutzername(user.getNutzername())
                .email(user.getEmail())
                .geburtsdatum(user.getGeburtsdatum())
                .rollen(rollenIds)
                .build();

        return userDTO;
    }
}
