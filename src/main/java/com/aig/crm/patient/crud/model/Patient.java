package com.aig.crm.patient.crud.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAZIENTE")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_generator")
    @SequenceGenerator(name="patient_generator", sequenceName = "patient_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "DATA_INSERIMENTO")
    private Date insertionDate;

    @Column(name = "COGNOME")
    private String surname;

    @Column(name = "NOME")
    private String name;

    @Column(name = "GLICOGENOSI")
    @Enumerated(EnumType.STRING)
    private GlycogenStorageDiseaseType glycogenSDType;

    @Column(name = "DATA_NASCITA")
    private Date dateOfBirth;

    @Column(name = "ETA")
    private Integer age;

    @Column(name = "OSPEDALE")
    private String hospital;

    @Column(name = "OSPEDALE_INFUZIONE")
    private String adoptingHospital;

    @Column(name = "GENITORI")
    private String parents;

    @Column(name = "FIGLI")
    private String sons;

    @Column(name = "DATI_CORRETTI")
    private String correctData;

    @Column(name = "PRESSO")
    private String careGiver;

    @Column(name = "QA_2016")
    private String qa2016;

    @Column(name = "QA_2017")
    private String qa2017;

    @Column(name = "QA_2018")
    private String qa2018;

    @Column(name = "NOTE")
    @Lob
    @Type(type = "text")
    private String note;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "REF_INDIRIZZO")
    private Address address;

}
