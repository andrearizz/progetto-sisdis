package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.Groups;
import it.unical.progettosisdis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupRepository extends JpaRepository<Groups, Long> {


    Optional<Groups> findGroupsByJoinCode(String joinCode);
}
