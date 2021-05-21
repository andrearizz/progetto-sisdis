package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.CredentialsGroup;
import it.unical.progettosisdis.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsGroupRepository extends JpaRepository<CredentialsGroup, Long> {

    Boolean existsByGroupsAndUrlAndLogin(Groups groups, String url, String login);

    Optional<CredentialsGroup> findCredentialsGroupByGroups(Groups groups);
}
