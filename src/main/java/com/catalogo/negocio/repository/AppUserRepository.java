package com.catalogo.negocio.repository;

import com.catalogo.negocio.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRolesContaining(com.catalogo.negocio.model.Role role);
}
