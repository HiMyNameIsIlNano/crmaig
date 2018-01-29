package com.aig.crm.patient.crud.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Table(name = "INDIRIZZO")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
    @SequenceGenerator(name="address_generator", sequenceName = "address_id_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "VIA")
    private String street;

    @Column(name = "CITTA")
    private String city;

    @Column(name = "CAP")
    private String poBox;

    @Column(name = "PROV")
    private String prov;

    @Column(name = "INDIRIZZO_SPEDIZIONE")
    private String deliveryAddress;

    @Column(name = "NOME_REGIONE")
    private String region;

    @Column(name = "NAZIONE")
    private String nation;

    @Column(name = "EMAIL")
    @Valid
    private String email;

    @Column(name = "TELEFONO")
    private String phone;

    @Column(name = "CELLULARE")
    private String mobilePhone;

}

