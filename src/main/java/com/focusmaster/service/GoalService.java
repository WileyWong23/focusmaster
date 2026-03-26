package com.focusmaster.service;

import com.focusmaster.entity.AppUser;
import com.focusmaster.entity.Goal;
import com.focusmaster.enums.GoalStatus;
import com.focusmaster.enums.Rank;
import com.focusmaster.exception.BusinessException;
import com.focusmaster.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final RankService rankService;
    private final AppUserService appUserService;

    public GoalService(GoalRepository goalRepository, RankService rankService, AppUserService appUserService) {
        this.goalRepository = goalRepository;
        this.rankService = rankService;
        this.appUserService = appUserService;
    }

    public Map<String, Object> getAllGoals(Long userId) {
        List<Goal> allGoals = goalRepository.findByUserIdOrderByCreatedAtDesc(userId);

        Goal activeGoal = allGoals.stream()
                .filter(g -> g.getStatus() == GoalStatus.ACTIVE)
                .findFirst().orElse(null);

        List<Map<String, Object>> completedGoals = allGoals.stream()
                .filter(g -> g.getStatus() == GoalStatus.COMPLETED)
                .map(this::toGoalMap)
                .collect(Collectors.toList());

        List<Map<String, Object>> abandonedGoals = allGoals.stream()
                .filter(g -> g.getStatus() == GoalStatus.ABANDONED)
                .map(this::toGoalMap)
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activeGoal", activeGoal != null ? toGoalMap(activeGoal) : null);
        result.put("completedGoals", completedGoals);
        result.put("abandonedGoals", abandonedGoals);
        return result;
    }

    @Transactional
    public Goal createGoal(Long userId, String name, Long totalMinutes) {
        goalRepository.findByUserIdAndStatus(userId, GoalStatus.ACTIVE).ifPresent(g -> {
            throw new BusinessException("请先结束当前进行中的目标");
        });

        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setName(name);
        goal.setTotalMinutes(totalMinutes);
        goal.setAccumulatedMinutes(0L);
        goal.setStatus(GoalStatus.ACTIVE);
        goal.setCurrentRank(Rank.BRONZE);
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal endGoal(Long userId, Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new BusinessException(404, "目标不存在"));

        if (!goal.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此目标");
        }
        if (goal.getStatus() != GoalStatus.ACTIVE) {
            throw new BusinessException("该目标已不在进行中");
        }

        if (goal.getAccumulatedMinutes() >= goal.getTotalMinutes()) {
            goal.setStatus(GoalStatus.COMPLETED);
        } else {
            goal.setStatus(GoalStatus.ABANDONED);
        }
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal restartGoal(Long userId, Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new BusinessException(404, "目标不存在"));

        if (!goal.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此目标");
        }
        if (goal.getStatus() == GoalStatus.ACTIVE) {
            throw new BusinessException("该目标已在进行中");
        }

        goalRepository.findByUserIdAndStatus(userId, GoalStatus.ACTIVE).ifPresent(g -> {
            throw new BusinessException("请先结束当前进行中的目标");
        });

        goal.setStatus(GoalStatus.ACTIVE);
        return goalRepository.save(goal);
    }

    public Map<String, Object> getGoalDetail(Long userId, Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new BusinessException(404, "目标不存在"));

        if (!goal.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权查看此目标");
        }

        Map<String, Object> result = toGoalMap(goal);
        result.put("rankThresholds", rankService.getRankThresholds(goal.getTotalMinutes()));
        return result;
    }

    public Goal getActiveGoal(Long userId) {
        return goalRepository.findByUserIdAndStatus(userId, GoalStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("没有进行中的目标"));
    }

    private Map<String, Object> toGoalMap(Goal goal) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", goal.getId());
        map.put("name", goal.getName());
        map.put("totalMinutes", goal.getTotalMinutes());
        map.put("accumulatedMinutes", goal.getAccumulatedMinutes());
        map.put("status", goal.getStatus().name());
        map.put("currentRank", goal.getCurrentRank().name());
        map.put("currentRankName", goal.getCurrentRank().getName());
        map.put("currentRankColor", goal.getCurrentRank().getColor());
        map.put("progressPercent", rankService.getProgressPercent(goal.getAccumulatedMinutes(), goal.getTotalMinutes()));
        map.put("createdAt", goal.getCreatedAt().toString().replace("T", " "));
        return map;
    }
}
