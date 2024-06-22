package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentLog, Integer> {
    @NonNull
    Optional<PaymentLog> findBySessionId(@NonNull UUID uuid);
}
