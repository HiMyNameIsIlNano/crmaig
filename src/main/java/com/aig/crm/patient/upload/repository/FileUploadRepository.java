package com.aig.crm.patient.upload.repository;

import com.aig.crm.patient.upload.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
