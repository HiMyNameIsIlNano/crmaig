package com.aig.crm.patient.upload.controller;

import com.aig.crm.breadcrumb.annotations.Link;
import com.aig.crm.patient.upload.exception.DifferentFileRecordAmount;
import com.aig.crm.patient.upload.service.FileUploadService;
import com.aig.crm.patient.upload.exception.StorageFileNotReadableException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import java.nio.file.FileAlreadyExistsException;

@Controller
@RequestMapping("/csv")
@Secured({"ROLE_ADMIN"})
public class FileUploadController {

    @Inject
    private FileUploadService fileUploadService;

    @ModelAttribute("module")
    public String module() {
        return "files";
    }

    @Link(label="upload.breadcrum.upload", family="FileUploadController", parent = "" )
    @GetMapping("/list")
    public String listUploadedFiles(Model model, Pageable pageable) {
        model.addAttribute("files", fileUploadService.findAll(pageable));
        return "/patients/csvUpload/fileUpload";
    }

    /*@GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        *//*Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);*//*

        return null;
    }*/

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("patients") MultipartFile patients,
                                   @RequestParam("addresses") MultipartFile addresses,
                                   RedirectAttributes redirectAttributes) {

        fileUploadService.importData(patients, addresses);
        fileUploadService.saveCsv(patients, addresses);

        // We do not have to bother about exceptions and the respective messages. Exception are handled globally.
        redirectAttributes.addFlashAttribute("uploadResult","upload.file.ok");
        redirectAttributes.addFlashAttribute("fileNames", patients.getOriginalFilename() + ", " + addresses.getOriginalFilename());

        return "redirect:/";
    }

}
