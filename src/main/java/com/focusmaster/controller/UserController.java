package com.focusmaster.controller;

import com.focusmaster.dto.ApiResponse;
import com.focusmaster.dto.NicknameUpdateRequest;
import com.focusmaster.entity.AppUser;
import com.focusmaster.exception.BusinessException;
import com.focusmaster.service.AppUserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/init")
    public ApiResponse<?> initUser(@RequestBody Map<String, String> request) {
        String deviceUuid = request.get("deviceUuid");
        if (deviceUuid == null || deviceUuid.trim().isEmpty()) {
            throw new BusinessException("deviceUuid不能为空");
        }
        AppUser user = appUserService.initUser(deviceUuid);
        return ApiResponse.success(appUserService.toProfileMap(user));
    }

    @GetMapping("/profile")
    public ApiResponse<?> getProfile(@RequestHeader("X-User-Id") Long userId) {
        AppUser user = appUserService.getUserById(userId);
        return ApiResponse.success(appUserService.toProfileMap(user));
    }

    @PutMapping("/profile")
    public ApiResponse<?> updateProfile(@RequestHeader("X-User-Id") Long userId,
                                        @Valid @RequestBody NicknameUpdateRequest request) {
        String nickname = request.getNickname().trim();
        if (nickname.isEmpty()) {
            throw new BusinessException("昵称不能为空");
        }
        AppUser user = appUserService.updateNickname(userId, nickname);
        return ApiResponse.success("修改成功");
    }
}
