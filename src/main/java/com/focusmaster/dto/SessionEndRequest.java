package com.focusmaster.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SessionEndRequest {
    @NotNull(message = "实际时长不能为空")
    @Min(value = 0, message = "实际时长不能为负数")
    private Integer actualMinutes;
}
