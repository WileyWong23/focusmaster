package com.focusmaster.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NicknameUpdateRequest {
    @NotBlank(message = "昵称不能为空")
    @Size(min = 1, max = 50, message = "昵称长度1~50个字符")
    private String nickname;
}
