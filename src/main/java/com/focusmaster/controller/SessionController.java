package com.focusmaster.controller;

import com.focusmaster.dto.ApiResponse;
import com.focusmaster.dto.SessionEndRequest;
import com.focusmaster.dto.SessionStartRequest;
import com.focusmaster.service.SessionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/start")
    public ApiResponse<?> startSession(@RequestHeader("X-User-Id") Long userId,
                                       @Valid @RequestBody SessionStartRequest request) {
        Map<String, Object> result = sessionService.startSession(userId, request.getGoalId(), request.getPlannedMinutes());
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/end")
    public ApiResponse<?> endSession(@RequestHeader("X-User-Id") Long userId,
                                     @PathVariable Long id,
                                     @Valid @RequestBody SessionEndRequest request) {
        Map<String, Object> result = sessionService.endSession(userId, id, request.getActualMinutes());
        return ApiResponse.success(result);
    }
}
