package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.Credentials;
import it.unical.progettosisdis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findByUrl(String url);

    Optional<Credentials> findCredentialsByUrlAndUser(String url, User user);

    Optional<Credentials> findCredentialsByUrlAndUserAndLogin(String url, User user, String login);

    Optional<List<Credentials>> findAllByUser(User user);

    Boolean existsByUserAndUrlAndLogin(User user, String url, String login);

    Optional<Credentials> findCredentialsById(Long id);

    Optional<Credentials> findCredentialsByUserAndId(User user, Long id);
}
