package com.hyxpillow.pillowdrive.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileEntity {
    private String filename;
    private String size;
    private String lastModified;
    private boolean isDirectory;
}
