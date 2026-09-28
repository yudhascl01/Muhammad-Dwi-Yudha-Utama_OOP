package com.muhammad_dwi_yudha_utama.backend.repository;

import com.muhammad_dwi_yudha_utama.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    @Query("SELECT s FROM Score s WHERE s.point > :minValue")
    List<Score> findPointGreaterThan(@Param("minValue") Integer minValue);

    List<Score> findAllByOrderByCreatedAtDesc();

    @Query(
        value = "SELECT * FROM scores ORDER BY points DESC LIMIT :limit",
        nativeQuery = true
    )
    List<Score> findTopScores(@Param("limit") Integer limit);

    List<Score> findTopScores();

    List<Score> findTopScores();

    List<Score> findTopScores();

    List<Score> findTopScores();

}
