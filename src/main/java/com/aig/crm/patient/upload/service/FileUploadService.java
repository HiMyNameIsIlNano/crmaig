package com.aig.crm.patient.upload.service;

import com.aig.crm.patient.crud.model.Address;
import com.aig.crm.patient.crud.model.GlycogenStorageDiseaseType;
import com.aig.crm.patient.crud.model.Patient;
import com.aig.crm.patient.crud.repository.PatientRepository;
import com.aig.crm.patient.upload.exception.DifferentFileRecordAmount;
import com.aig.crm.patient.upload.exception.StorageFileNotReadableException;
import com.aig.crm.patient.upload.model.FileCategory;
import com.aig.crm.patient.upload.model.FileUpload;
import com.aig.crm.patient.upload.repository.FileUploadRepository;
import com.aig.crm.shared.DateUtils;
import com.aig.crm.shared.service.PatientNaturalKeyCalculatorClient;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.aig.crm.patient.upload.service.PatientCsvEnum.*;

@Service
public class FileUploadService {

    private static final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private FileUploadRepository fileUploadRepository;

    @Inject
    private PatientNaturalKeyCalculatorClient patientNaturalKeyCalculatorClient;

    public Page<FileUpload> findAll(Pageable pageable) {
        return fileUploadRepository.findAll(pageable);
    }

    public void importData(MultipartFile patients, MultipartFile addresses) {
        try (
                InputStream patientsStream = patients.getInputStream();
                BufferedReader patientsReader = new BufferedReader(new InputStreamReader(patientsStream));
                CSVReader patientsCsvReader = new CSVReaderBuilder(patientsReader).withSkipLines(1).build();

                InputStream addressesStream = addresses.getInputStream();
                BufferedReader addressesReader = new BufferedReader(new InputStreamReader(addressesStream));
                CSVReader addressesCsvReader = new CSVReaderBuilder(addressesReader).withSkipLines(1).build()
        ) {
            List<String[]> addressRecords = addressesCsvReader.readAll();
            List<String[]> patientRecords = patientsCsvReader.readAll();
            if (addressRecords.size() != patientRecords.size()) {
                throw new DifferentFileRecordAmount("The two files have a different number of records");
            }

            int i = 0;
            for (String[] patient : patientRecords) {
                String[] address = addressRecords.get(i);
                if (patient.length == PatientCsvEnum.getRecordLength()
                        && address.length == AddressCsvEnum.getRecordLength()) {
                    savePatient(patient, address);
                    i++;
                } else {
                    if (patient.length != PatientCsvEnum.getRecordLength()) {
                        log.info("A record in the patients file differs from its record length");
                    }
                    if (address.length != AddressCsvEnum.getRecordLength()) {
                        log.info("A record in the addresses file differs from its record length");
                    }

                    throw new StorageFileNotReadableException("An error occurred while saving the data");
                }
            }
        } catch (IOException e) {
            throw new StorageFileNotReadableException("An error occurred while saving data");
        }
    }

    private void savePatient(String[] patientRecord, String[] addressRecord) {
        String insertionDate = patientRecord[PatientCsvEnum.fromName(INSERTION_DATE)];
        String surname = patientRecord[PatientCsvEnum.fromName(SURNAME)];
        String name = patientRecord[PatientCsvEnum.fromName(NAME)];
        String glycogenSDType = patientRecord[PatientCsvEnum.fromName(GLYCOGEN_SD_TYPE)];
        String dateOfBirth = patientRecord[PatientCsvEnum.fromName(DATE_OF_BIRTH)];
        String age = patientRecord[PatientCsvEnum.fromName(AGE)];
        String hospital = patientRecord[PatientCsvEnum.fromName(HOSPITAL)];
        String adoptingHospital = patientRecord[PatientCsvEnum.fromName(ADOPTING_HOSPITAL)];
        String parents = patientRecord[PatientCsvEnum.fromName(PARENTS)];
        String sons = patientRecord[PatientCsvEnum.fromName(SONS)];
        String correctData = patientRecord[PatientCsvEnum.fromName(CORRECT_DATA)];
        String careGiver = patientRecord[PatientCsvEnum.fromName(CARE_GIVER)];
        String qa2016 = patientRecord[PatientCsvEnum.fromName(QA_2016)];
        String qa2017 = patientRecord[PatientCsvEnum.fromName(QA_2017)];
        String qa2018 = patientRecord[PatientCsvEnum.fromName(QA_2018)];
        byte[] note = patientRecord[PatientCsvEnum.fromName(NOTE)].getBytes();

        Address address;
        String street = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.STREET)];
        String poBox = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.POBOX)];
        String city = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.CITY)];
        String prov = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.PROV)];
        String deliveryAddress = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.DELIVERY_ADDRESS)];
        String region = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.REGION)];
        String nation = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.NATION)];
        String email = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.EMAIL)];
        String phone = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.PHONE)];
        String mobilePhone = addressRecord[AddressCsvEnum.fromName(AddressCsvEnum.MOBILE_PHONE)];

        Optional<String> ssn = patientNaturalKeyCalculatorClient.getPatientNaturalKey(name,
                surname,
                city,
                dateOfBirth,
                "M");

        if (ssn.isPresent()) {
            Patient aPatient = patientRepository.findBySsn(ssn.get());

            // Check if a patient with the same SSN already exists. If so, skip the patient then.
            if (aPatient == null) {
                address = Address.builder()
                        .poBox(poBox)
                        .mobilePhone(mobilePhone)
                        .street(street)
                        .city(city)
                        .prov(prov)
                        .region(region)
                        .nation(nation)
                        .deliveryAddress(deliveryAddress)
                        .email(email)
                        .phone(phone)
                        .build();

                Patient patient = Patient.builder()
                        .insertionDate(isValidDate(insertionDate) ? DateUtils.fromString(insertionDate) : null)
                        .ssn(ssn.get())
                        .name(name)
                        .surname(surname)
                        .glycogenSDType(GlycogenStorageDiseaseType.fromString(glycogenSDType))
                        .dateOfBirth(isValidDate(dateOfBirth) ? DateUtils.fromString(dateOfBirth) : null)
                        .age(isValidNumber(age) ? Integer.parseInt(age) : null)
                        .hospital(hospital)
                        .adoptingHospital(adoptingHospital)
                        .parents(parents)
                        .sons(sons)
                        .correctData(correctData)
                        .careGiver(careGiver)
                        .qa2016(qa2016)
                        .qa2017(qa2017)
                        .qa2018(qa2018)
                        .note(note)
                        .address(address)
                        .build();

                patientRepository.save(patient);
            } else {
                // TODO: update instead of skipping
                log.info("Skipped patient with ssn " + ssn + "as it already exists");
            }
        }
    }

    private boolean isValidDate(String data) {
        return data != null && !"".equals(data);
    }

    private boolean isValidNumber(String number) {
        return number != null && !"".equals(number);
    }

    public void saveCsv(MultipartFile patients, MultipartFile addresses) {
        try {
            FileUpload patientsFile = FileUpload.builder()
                    .category(FileCategory.PATIENTS)
                    .fileName(patients.getOriginalFilename())
                    .fileContent(patients.getBytes())
                    .uploadedAt(ZonedDateTime.now())
                    .build();
            fileUploadRepository.save(patientsFile);

            FileUpload addressesFile = FileUpload.builder()
                    .category(FileCategory.ADDRESSES)
                    .fileName(addresses.getOriginalFilename())
                    .fileContent(addresses.getBytes())
                    .uploadedAt(ZonedDateTime.now())
                    .build();
            fileUploadRepository.save(addressesFile);
        } catch (IOException e) {
            throw new StorageFileNotReadableException("An error occurred while saving data");
        }
    }

}
