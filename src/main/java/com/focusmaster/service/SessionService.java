package com.focusmaster.service;

import com.focusmaster.entity.FocusSession;
import com.focusmaster.entity.Goal;
import com.focusmaster.enums.Rank;
import com.focusmaster.exception.BusinessException;
import com.focusmaster.repository.GoalRepository;
import com.focusmaster.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final GoalRepository goalRepository;
    private final GoalService goalService;
    private final RankService rankService;
    private final AppUserService appUserService;

    public SessionService(SessionRepository sessionRepository, GoalRepository goalRepository,
                          GoalService goalService, RankService rankService, AppUserService appUserService) {
        this.sessionRepository = sessionRepository;
        this.goalRepository = goalRepository;
        this.goalService = goalService;
        this.rankService = rankService;
        this.appUserService = appUserService;
    }

    @Transactional
    public Map<String, Object> startSession(Long userId, Long goalId, Integer plannedMinutes) {
        Goal goal = goalService.getActiveGoal(userId);

        if (!goal.getId().equals(goalId)) {
            throw new BusinessException("当前进行中的目标不匹配");
        }

        FocusSession session = new FocusSession();
        session.setUserId(userId);
        session.setGoalId(goalId);
        session.setPlannedMinutes(plannedMinutes);
        session.setStartedAt(LocalDateTime.now());
        FocusSession saved = sessionRepository.save(session);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", saved.getId());
        result.put("startedAt", saved.getStartedAt().toString());
        result.put("plannedMinutes", plannedMinutes);
        return result;
    }

    @Transactional
    public Map<String, Object> endSession(Long userId, Long sessionId, Integer actualMinutes) {
        FocusSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(404, "专注记录不存在"));

        if (!session.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此记录");
        }
        if (session.getEndedAt() != null) {
            throw new BusinessException("该专注已结束");
        }

        boolean isValid = actualMinutes >= 5;
        Rank previousRank = null;
        boolean rankChanged = false;
        long newAccumulatedMinutes = 0;

        session.setActualMinutes(actualMinutes);
        session.setIsValid(isValid);
        session.setEndedAt(LocalDateTime.now());

        if (isValid) {
            Goal goal = goalService.getActiveGoal(userId);
            previousRank = goal.getCurrentRank();
            newAccumulatedMinutes = goal.getAccumulatedMinutes() + actualMinutes;
            goal.setAccumulatedMinutes(newAccumulatedMinutes);

            Rank newRank = rankService.calculateRank(newAccumulatedMinutes, goal.getTotalMinutes());
            rankChanged = !newRank.equals(previousRank);
            goal.setCurrentRank(newRank);

            // Check completion
            if (newAccumulatedMinutes >= goal.getTotalMinutes()) {
                goal.setStatus(com.focusmaster.enums.GoalStatus.COMPLETED);
                appUserService.incrementCompletedGoalCount(userId);
            }

            sessionRepository.save(session);
            goalRepository.save(goal);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("sessionId", session.getId());
            result.put("isValid", true);
            result.put("actualMinutes", actualMinutes);
            result.put("accumulatedMinutes", newAccumulatedMinutes);
            result.put("currentRank", goal.getCurrentRank().name());
            result.put("currentRankName", goal.getCurrentRank().getName());
            result.put("currentRankColor", goal.getCurrentRank().getColor());
            result.put("rankChanged", rankChanged);
            result.put("previousRank", previousRank != null ? previousRank.name() : null);
            result.put("previousRankName", previousRank != null ? previousRank.getName() : null);
            result.put("previousRankColor", previousRank != null ? previousRank.getColor() : null);
            result.put("goalCompleted", goal.getStatus() == com.focusmaster.enums.GoalStatus.COMPLETED);
            return result;
        }

        sessionRepository.save(session);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", session.getId());
        result.put("isValid", false);
        result.put("actualMinutes", actualMinutes);
        result.put("accumulatedMinutes", 0);
        result.put("rankChanged", false);
        return result;
    }
}
