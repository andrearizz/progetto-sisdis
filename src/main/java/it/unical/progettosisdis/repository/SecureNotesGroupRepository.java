package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.Groups;
import it.unical.progettosisdis.entity.SecureNotesGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecureNotesGroupRepository extends JpaRepository<SecureNotesGroup, Long> {


    Boolean existsSecureNotesGroupByTitleAndGroups(String title, Groups groups);
}
