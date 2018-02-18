package com.aig.crm.patient.upload.controller;

import com.aig.crm.breadcrumb.annotations.Link;
import com.aig.crm.patient.upload.service.FileUploadService;
import com.aig.crm.patient.upload.exception.StorageFileNotReadableException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

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
                                   Model model) {

        fileUploadService.saveCsv(patients, addresses);
        fileUploadService.importData(patients, addresses);

        /*redirectAttributes.addFlashAttribute("pazienti_ok",
                "File " + patients.getOriginalFilename() + "caricato con successo!");
        redirectAttributes.addFlashAttribute("indirizzi_ok",
                "File " + addresses.getOriginalFilename() + "caricato con successo!");*/

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotReadableException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotReadableException exc) {
        return ResponseEntity.notFound().build();
    }

}
