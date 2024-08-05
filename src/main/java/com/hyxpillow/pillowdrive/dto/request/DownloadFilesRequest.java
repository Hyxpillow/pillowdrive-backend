package com.hyxpillow.pillowdrive.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DownloadFilesRequest {
    private List<String> pathList;
}
