package com.focusmaster.repository;

import com.focusmaster.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByDeviceUuid(String deviceUuid);
}
