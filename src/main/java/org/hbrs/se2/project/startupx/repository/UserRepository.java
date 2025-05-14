package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByNutzernameAndPasswort(String nutzername, String passwort);

    User findUserById(Long id);

    User findUserByNutzername(String nutzername);

    boolean existsByEmail(String email);

    boolean existsByNutzername(String nutzername);

}
