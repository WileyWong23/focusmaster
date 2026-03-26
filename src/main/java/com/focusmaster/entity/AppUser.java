package com.focusmaster.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_uuid", nullable = false, unique = true, length = 64)
    private String deviceUuid;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname = "专注大师";

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "wechat_openid", unique = true, length = 64)
    private String wechatOpenid;

    @Column(name = "wechat_unionid", length = 64)
    private String wechatUnionid;

    @Column(name = "total_focus_minutes", nullable = false)
    private Long totalFocusMinutes = 0L;

    @Column(name = "completed_goal_count", nullable = false)
    private Integer completedGoalCount = 0;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.lastActiveAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.lastActiveAt = LocalDateTime.now();
    }
}
