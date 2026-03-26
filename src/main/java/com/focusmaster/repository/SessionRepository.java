package com.focusmaster.repository;

import com.focusmaster.entity.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<FocusSession, Long> {
    List<FocusSession> findByUserIdAndEndedAtBetweenAndIsValidTrue(
            Long userId, LocalDateTime start, LocalDateTime end);

    long countByUserIdAndIsValidTrue(Long userId);

    @Query("SELECT COALESCE(SUM(s.actualMinutes), 0) FROM FocusSession s WHERE s.userId = :userId AND s.isValid = true")
    long sumActualMinutesByUserIdAndIsValidTrue(@Param("userId") Long userId);
}
