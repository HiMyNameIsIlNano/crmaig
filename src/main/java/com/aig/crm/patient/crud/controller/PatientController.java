package com.aig.crm.patient.crud.controller;

import com.aig.crm.breadcrumb.annotations.Link;
import com.aig.crm.patient.crud.repository.PatientRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
@RequestMapping("/patients")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class PatientController {

    @Inject
    private PatientRepository patientRepository;

    @ModelAttribute("module")
    public String module() {
        return "patients";
    }

    @Link(label="patients.breadcrum.list", family="PatientController", parent = "patients.breadcrum.root" )
    @GetMapping("/list")
    public String getPatientsList(Model model, @SortDefault("surname") Pageable pageable){
        model.addAttribute("patients", patientRepository.findAll(pageable));
        return "/patients/data/patientsList";
    }

    @Link(label="patients.breadcrum.list", family="PatientController", parent = "patients.breadcrum.root" )
    @GetMapping("/find-by-surname/{surname}")
    public String getPatientsBySurname(@PathVariable(value="surname", required = false) String surname,
                                       Model model,
                                       @SortDefault("surname") Pageable pageable) {
        model.addAttribute("patients", patientRepository.findBySurname(pageable, surname));
        // return "/patients/patientsList";
        return "/patients/data/patientsList :: results-list";
    }

    @Link(label="patients.breadcrum.details", family="PatientController", parent = "patients.breadcrum.list" )
    @GetMapping("/find-by-id/{id}")
    public String getPatientsDetails(@PathVariable(value="id") Long id, Model model) {
        model.addAttribute("patient", patientRepository.findOne(id));
        return "/patients/data/patientDetails";
    }
}
