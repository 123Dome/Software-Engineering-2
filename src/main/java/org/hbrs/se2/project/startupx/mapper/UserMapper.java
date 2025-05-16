package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.hbrs.se2.project.startupx.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
//    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//
//    User mapToUser(UserDTO userDTO);


    public UserDTO mapToUserDto(User user) {
        if (user == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setVorname(user.getVorname());
        userDTO.setNachname(user.getNachname());
        userDTO.setPasswort(user.getPasswort());
        userDTO.setNutzername(user.getNutzername());
        userDTO.setEmail(user.getEmail());
        userDTO.setGeburtsdatum(user.getGeburtsdatum());

        userDTO.setRollen(user.getRollen() != null
                ? user.getRollen().stream()
                .map(Rolle::getId)
                .collect(Collectors.toSet())
                : Set.of());

        return userDTO;
    }
}
