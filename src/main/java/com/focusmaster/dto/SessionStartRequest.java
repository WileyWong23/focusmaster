package com.focusmaster.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SessionStartRequest {
    @NotNull(message = "目标ID不能为空")
    private Long goalId;

    @Min(value = 5, message = "专注时长最少5分钟")
    @Max(value = 180, message = "专注时长最多180分钟")
    private Integer plannedMinutes;
}
