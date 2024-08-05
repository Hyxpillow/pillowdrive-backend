package com.hyxpillow.pillowdrive.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
