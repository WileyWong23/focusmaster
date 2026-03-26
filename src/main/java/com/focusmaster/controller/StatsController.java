package com.focusmaster.controller;

import com.focusmaster.dto.ApiResponse;
import com.focusmaster.service.StatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/overview")
    public ApiResponse<?> getOverview(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(statsService.getOverview(userId));
    }
}
