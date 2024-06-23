package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;


public interface FileRepository extends CrudRepository<FileEntity, Integer> {
    FileEntity findByFileId(int fileId);
}
