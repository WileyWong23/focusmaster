package com.focusmaster.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "focus_session")
public class FocusSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "goal_id", nullable = false)
    private Long goalId;

    @Column(name = "planned_minutes", nullable = false)
    private Integer plannedMinutes;

    @Column(name = "actual_minutes")
    private Integer actualMinutes;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;
}
