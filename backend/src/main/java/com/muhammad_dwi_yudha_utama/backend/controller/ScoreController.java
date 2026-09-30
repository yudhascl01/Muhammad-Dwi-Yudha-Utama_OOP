package com.muhammad_dwi_yudha_utama.backend.controller;

import com.muhammad_dwi_yudha_utama.backend.model.Score;
import com.muhammad_dwi_yudha_utama.backend.service.ScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {
        Optional<Score> score = scoreService.getScoreByID(scoreId);

        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "error",
                "Score not found with id: " + scoreId
            ));
    }

    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {
        try {
            Score createdScore = scoreService.createScore(score);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdScore);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "error",
                    e.getMessage() != null
                        ? e.getMessage()
                        : "Failed to create score"
                ));
        }
    }

    @GetMapping
    public ResponseEntity<List<Score>> getAllScores() {
        List<Score> scores = scoreService.getAllScores();

        return ResponseEntity.ok(scores);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboardByPoint(
        @RequestParam(defaultValue = "10") Integer limit) {

        List<Score> scores = scoreService.getLeaderboard(limit);

        return ResponseEntity.ok(scores);
    }

    @GetMapping("/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(
        @PathVariable Integer minValue) {

        List<Score> scores = scoreService.getScoreAboveValue(minValue);

        return ResponseEntity.ok(scores);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Score>> getRecentScores() {
        List<Score> scores = scoreService.getRecentScores();

        return ResponseEntity.ok(scores);
    }

    @DeleteMapping("/{scoreId}")
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {
        try {
            scoreService.deleteScore(scoreId);

            return ResponseEntity.ok(
                Map.of("message", "Score berhasil dihapus")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "error",
                    e.getMessage() != null
                        ? e.getMessage()
                        : "Score tidak ditemukan"
                ));
        }
    }
}
