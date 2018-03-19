package com.aig.crm.patient.crud.repository;

import com.aig.crm.patient.crud.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findBySurname(Pageable pageable, String surname);

    Patient findBySsn(String ssn);

}
