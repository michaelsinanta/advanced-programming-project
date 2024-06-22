package id.ac.ui.cs.advprog.warnetservice.repository;

import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionPricingRepository extends JpaRepository<SessionPricing, Integer> {
    @NonNull
    Optional<SessionPricing> findById(@NonNull Integer id);
    List<SessionPricing> findBySession(Session session);
}
