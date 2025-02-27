package com.example.file_backup.service.file;

import com.example.file_backup.dto.FileDTO;
import com.example.file_backup.exception.ResourceAlreadyExistException;
import com.example.file_backup.exception.ResourceNotFound;
import com.example.file_backup.model.File;
import com.example.file_backup.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
    private final FileRepository fileRepository;

    @Override
    public List<FileDTO> getAllFiles() {
        return fileRepository.findAll()
                .stream()
                .map(file -> new FileDTO(
                        file.getFileId(),
                        file.getFileName(),
                        file.getFileType(),
                        file.getDownloadUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public File getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        String.format("File with id '%d' not found!", id)
                ));
    }

    @Override
    public void deleteFileById(Long id) {
        fileRepository.findById(id)
                .ifPresentOrElse(
                        fileRepository::delete,
                        () -> {
                            throw new ResourceNotFound(String.format("File with id '%d' not found!", id));
                        }
                );
    }

    @Override
    public FileDTO saveFile(MultipartFile file) {
        try {
            Optional<File> fileExistCheck = fileRepository.findByFileName(file.getOriginalFilename());

            if (fileExistCheck.isEmpty()) {
                File newFile = new File();
                newFile.setFileName(file.getOriginalFilename());
                newFile.setFileType(file.getContentType());
                newFile.setFile(new SerialBlob(file.getBytes()));
                File savedFile = fileRepository.save(newFile);
                savedFile.setDownloadUrl("/file/" + savedFile.getFileId());
                savedFile = fileRepository.save(savedFile);
                return new FileDTO(
                        savedFile.getFileId(),
                        savedFile.getFileName(),
                        savedFile.getFileType(),
                        savedFile.getDownloadUrl()
                );
            } else {
                throw new ResourceAlreadyExistException(
                       String.format("File with name '%s' already exists", file.getOriginalFilename())
                );
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
