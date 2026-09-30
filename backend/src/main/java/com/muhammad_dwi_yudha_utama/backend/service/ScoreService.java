package com.muhammad_dwi_yudha_utama.backend.service;

import com.muhammad_dwi_yudha_utama.backend.model.Score;
import com.muhammad_dwi_yudha_utama.backend.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    public Optional<Score> getScoreByID(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public List<Score> getRecentScores() {
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue) {
        return scoreRepository.findPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        return scoreRepository.findTopScores(limit);
    }

    public void deleteScore(UUID scoreId) {
        Score score = scoreRepository.findById(scoreId)
            .orElseThrow(() -> new RuntimeException(
                "Score dengan ID " + scoreId + " tidak ditemukan"
            ));

        scoreRepository.delete(score);
    }
}
