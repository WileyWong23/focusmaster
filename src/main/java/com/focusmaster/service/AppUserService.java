package com.focusmaster.service;

import com.focusmaster.entity.AppUser;
import com.focusmaster.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser initUser(String deviceUuid) {
        return appUserRepository.findByDeviceUuid(deviceUuid)
                .orElseGet(() -> {
                    AppUser user = new AppUser();
                    user.setDeviceUuid(deviceUuid);
                    return appUserRepository.save(user);
                });
    }

    public AppUser getUserById(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new com.focusmaster.exception.BusinessException(404, "用户不存在"));
    }

    public AppUser updateNickname(Long userId, String nickname) {
        AppUser user = getUserById(userId);
        user.setNickname(nickname);
        return appUserRepository.save(user);
    }

    public void incrementCompletedGoalCount(Long userId) {
        AppUser user = getUserById(userId);
        user.setCompletedGoalCount(user.getCompletedGoalCount() + 1);
        appUserRepository.save(user);
    }

    public Map<String, Object> toProfileMap(AppUser user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("nickname", user.getNickname());
        map.put("avatar", user.getAvatar());
        map.put("totalFocusMinutes", user.getTotalFocusMinutes());
        map.put("completedGoalCount", user.getCompletedGoalCount());
        map.put("createdAt", user.getCreatedAt().toString().replace("T", " "));
        return map;
    }
}
