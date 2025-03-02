package com.example.file_backup.controller;

import com.example.file_backup.dto.FileDTO;
import com.example.file_backup.exception.ResourceAlreadyExistException;
import com.example.file_backup.exception.ResourceNotFound;
import com.example.file_backup.model.File;
import com.example.file_backup.service.file.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(path = "/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {
    private final IFileService fileService;

    @GetMapping
    public ResponseEntity<List<FileDTO>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @GetMapping(path = "/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) throws SQLException {
        try {
            File file = fileService.getFileById(fileId);
            ByteArrayResource resource = new ByteArrayResource(
                    file.getFile().getBytes(1, (int)file.getFile().length())
            );
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (ResolutionException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDTO> saveFile(@RequestParam("file") MultipartFile file) {
        try {
            FileDTO savedFile = fileService.saveFile(file);
            return new ResponseEntity<>(
                    savedFile,
                    HttpStatus.CREATED
            );
        } catch (ResourceAlreadyExistException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @DeleteMapping(path = "/{fileId}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable Long fileId) {
        try {
            fileService.deleteFileById(fileId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }
}
