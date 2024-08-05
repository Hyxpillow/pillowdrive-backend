package com.hyxpillow.pillowdrive.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String password;
    private String code;
}
