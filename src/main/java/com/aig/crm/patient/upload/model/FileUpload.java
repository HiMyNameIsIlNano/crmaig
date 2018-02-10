package com.aig.crm.patient.upload.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="FILE_UPLOAD")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_upload_generator")
    @SequenceGenerator(name="file_upload_generator", sequenceName = "file_upload_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "NOME_FILE")
    private String fileName;

    @Column(name = "CATEGORIA")
    @Enumerated(EnumType.STRING)
    private FileCategory category;

    @Column(name = "DATAORA_CARICAMENTO")
    private ZonedDateTime uploadedAt;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="FILE", nullable=false)
    private byte[] fileContent;
}
