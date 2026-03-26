package com.focusmaster.service;

import com.focusmaster.entity.AppUser;
import com.focusmaster.entity.FocusSession;
import com.focusmaster.enums.GoalStatus;
import com.focusmaster.repository.GoalRepository;
import com.focusmaster.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final SessionRepository sessionRepository;
    private final GoalRepository goalRepository;

    public StatsService(SessionRepository sessionRepository, GoalRepository goalRepository) {
        this.sessionRepository = sessionRepository;
        this.goalRepository = goalRepository;
    }

    public Map<String, Object> getOverview(Long userId) {
        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(6).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        // Total focus minutes from ALL valid sessions (not just recent)
        long totalFocusMinutes = sessionRepository.sumActualMinutesByUserIdAndIsValidTrue(userId);

        List<FocusSession> recentSessions = sessionRepository
                .findByUserIdAndEndedAtBetweenAndIsValidTrue(userId, sevenDaysAgo, now);

        // Completed goals count
        long completedGoalCount = goalRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(g -> g.getStatus() == GoalStatus.COMPLETED)
                .count();

        // Daily stats for last 7 days
        List<Map<String, Object>> dailyStats = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

            long dayMinutes = recentSessions.stream()
                    .filter(s -> !s.getEndedAt().isBefore(dayStart) && !s.getEndedAt().isAfter(dayEnd))
                    .mapToLong(FocusSession::getActualMinutes)
                    .sum();

            Map<String, Object> dayMap = new LinkedHashMap<>();
            dayMap.put("date", date.toString());
            dayMap.put("label", date.getMonthValue() + "/" + date.getDayOfMonth());
            dayMap.put("weekday", getWeekday(date));
            dayMap.put("totalMinutes", dayMinutes);
            dailyStats.add(dayMap);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalFocusMinutes", totalFocusMinutes);
        result.put("completedGoalCount", completedGoalCount);
        result.put("dailyStats", dailyStats);
        return result;
    }

    private String getWeekday(LocalDate date) {
        String[] weekdays = {"日", "一", "二", "三", "四", "五", "六"};
        return "周" + weekdays[date.getDayOfWeek().getValue() % 7];
    }
}
