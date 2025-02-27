package com.example.file_backup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private Long fileId;
    private String fileName;
    private String fileType;
    private String downloadUrl;
}
