package com.example.farmfarm_refact.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Data
@Entity
@Table(name="files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long fileId;

    @Column(name="filename")
    private String filename;


    @Column(name="fileurl")
    private String fileurl;

    private FileType fileType;

    @ManyToOne
    @JoinColumn(name="product")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name="farm")
    private FarmEntity farm;

    public FileEntity(String filename, String fileurl) {
        this.filename = filename;
        this.fileurl = fileurl;
    }
}
