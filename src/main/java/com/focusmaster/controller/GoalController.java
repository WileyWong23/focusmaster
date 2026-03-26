package com.focusmaster.controller;

import com.focusmaster.dto.ApiResponse;
import com.focusmaster.dto.GoalCreateRequest;
import com.focusmaster.entity.Goal;
import com.focusmaster.service.GoalService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ApiResponse<?> getAllGoals(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(goalService.getAllGoals(userId));
    }

    @PostMapping
    public ApiResponse<?> createGoal(@RequestHeader("X-User-Id") Long userId,
                                     @Valid @RequestBody GoalCreateRequest request) {
        Goal goal = goalService.createGoal(userId, request.getName(), request.getTotalMinutes());
        return ApiResponse.success(goalService.getAllGoals(userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getGoalDetail(@RequestHeader("X-User-Id") Long userId,
                                        @PathVariable Long id) {
        return ApiResponse.success(goalService.getGoalDetail(userId, id));
    }

    @PostMapping("/{id}/end")
    public ApiResponse<?> endGoal(@RequestHeader("X-User-Id") Long userId,
                                  @PathVariable Long id) {
        goalService.endGoal(userId, id);
        return ApiResponse.success(goalService.getAllGoals(userId));
    }

    @PostMapping("/{id}/restart")
    public ApiResponse<?> restartGoal(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long id) {
        goalService.restartGoal(userId, id);
        return ApiResponse.success(goalService.getAllGoals(userId));
    }
}
