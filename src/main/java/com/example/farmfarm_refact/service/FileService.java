package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.entity.FileEntity;
import com.example.farmfarm_refact.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    public FileEntity save(FileEntity files) {
        fileRepository.save(files);
        return files;
    }
    public FileEntity findByFileId(int id) {
        return fileRepository.findByFileId(id);
    }

    public void deleteByFileId(int id) {
        fileRepository.deleteByFileId(id);
    }
}

