package com.hyxpillow.pillowdrive.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DeleteFilesRequest {
    private List<String> pathList;
}
