package com.hyxpillow.pillowdrive.dto.request;

import lombok.Data;

@Data
public class RenameFileRequest {
    private String src;
    private String dst;
}
