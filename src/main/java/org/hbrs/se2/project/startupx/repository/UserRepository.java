package org.hbrs.se2.project.startupx.repository;

import jakarta.validation.constraints.NotBlank;
import org.hbrs.se2.project.startupx.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByNutzernameAndPasswort(String nutzername, String passwort);

    User findByEmail(String email);

    User findByNutzername(String nutzername);

    User findUserByNutzername(@NotBlank(message = "Nutzername darf nicht leer sein") String nutzername);
}
