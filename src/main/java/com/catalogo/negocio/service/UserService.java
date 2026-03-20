package com.catalogo.negocio.service;

import com.catalogo.negocio.model.AppUser;
import com.catalogo.negocio.model.Role;
import com.catalogo.negocio.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerAdmin(String username, String rawPassword) {
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(user);
    }

    public boolean hasAdminUsers() {
        return appUserRepository.existsByRolesContaining(Role.ROLE_ADMIN);
    }
}
