package com.hyxpillow.pillowdrive.dto.response;

import com.hyxpillow.pillowdrive.entity.FileEntity;
import lombok.Data;

import java.util.List;

@Data
public class FileListResponse {
    private List<FileEntity> allFiles;
}
