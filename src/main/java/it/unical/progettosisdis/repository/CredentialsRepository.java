package it.unical.progettosisdis.repository;

import it.unical.progettosisdis.entity.Credentials;
import it.unical.progettosisdis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findByUrl(String url);

    Optional<Credentials> findCredentialsByUrlAndUser(String url, User user);
}
