package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.ERole;
import com.example.dindonback.DTOs.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
