package com.focusmaster.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class GoalCreateRequest {
    @NotBlank(message = "目标名称不能为空")
    @Size(max = 20, message = "目标名称最长20个字符")
    private String name;

    @Min(value = 60, message = "目标时长最少1小时")
    private Long totalMinutes;
}
