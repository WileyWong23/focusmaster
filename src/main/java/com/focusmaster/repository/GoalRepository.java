package com.focusmaster.repository;

import com.focusmaster.entity.Goal;
import com.focusmaster.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findByUserIdAndStatus(Long userId, GoalStatus status);
    List<Goal> findByUserIdOrderByCreatedAtDesc(Long userId);
}
