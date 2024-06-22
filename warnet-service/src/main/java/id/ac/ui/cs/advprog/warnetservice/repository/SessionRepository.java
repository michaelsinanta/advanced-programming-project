package id.ac.ui.cs.advprog.warnetservice.repository;

import id.ac.ui.cs.advprog.warnetservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    @NonNull
    Optional<Session> findById(@NonNull UUID id);
    @Query(value = "SELECT * FROM session WHERE pc_id = :id ORDER BY datetime_start DESC LIMIT 1", nativeQuery = true)
    List<Session> getLatestSessionByPcId(Integer id);
    @Query(value = "SELECT * FROM session WHERE (:pcId IS NULL OR pc_id = :pcId) AND (:date IS NULL OR DATE(datetime_start) = CAST(:date AS DATE)) ORDER BY datetime_start DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
List<Session> listSession(
    @Param("offset") Integer offset,
    @Param("limit") Integer limit,
    @Param("pcId") Integer pcId,
    @Param("date") String date
);
}
