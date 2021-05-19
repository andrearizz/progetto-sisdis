package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.permissions.ERole;
import it.unical.progettosisdis.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
