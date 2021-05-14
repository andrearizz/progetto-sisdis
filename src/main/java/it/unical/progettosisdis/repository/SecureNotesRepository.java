package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.SecureNotes;
import it.unical.progettosisdis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecureNotesRepository extends JpaRepository<SecureNotes, Long> {

    Boolean existsByTitleAndUser(String title, User user);

    Optional<List<SecureNotes>> getAllNotesByUser(User user);
}
