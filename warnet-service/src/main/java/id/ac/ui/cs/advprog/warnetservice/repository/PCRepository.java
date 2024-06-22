package id.ac.ui.cs.advprog.warnetservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Session;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRepository extends JpaRepository<PC, Integer> {
    @NonNull
    List<PC> findAll();

    @NonNull
    Optional<PC> findById(@NonNull Integer id);

    void deleteById(@NonNull Integer id);
    @Query(value = "SELECT * FROM session LEFT JOIN pc ON pc_id = pc.id WHERE pc_id = ?1 ORDER BY datetime_start DESC LIMIT 1", nativeQuery = true)
    List<Session> getLatestSessionByPcId(int id);
}
