package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer>{
    @NonNull
    Optional<Bank> findById(@NonNull Integer id);

    @NonNull
    List<Bank> findAll();

    @NonNull
    Optional<Bank> findByName(@NonNull String name);

    void deleteById(@NonNull Integer id);
}
