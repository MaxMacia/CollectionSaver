package com.maxencemacia.collectionSaver.repository;

import com.maxencemacia.collectionSaver.entity.authentication.ERole;
import com.maxencemacia.collectionSaver.entity.authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole roleName);
}
