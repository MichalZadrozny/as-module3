package pl.michalzadrozny.asmodule3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.michalzadrozny.asmodule3.entity.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByValue(String value);
}
