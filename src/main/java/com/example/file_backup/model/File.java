package com.example.file_backup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Entity
@Table(name = "Files")
@Getter
@Setter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileId;
    @Column
    private String fileName;
    @Column
    private String fileType;
    @Lob
    private Blob file;
    private String downloadUrl;


}
