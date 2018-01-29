package com.aig.crm.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@SuppressWarnings("serial")
@Entity
@Table(name = "ACCOUNT")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name="account_generator", sequenceName = "account_id_seq")
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "NOME_UTENTE", unique = true)
    private String userName;

    @JsonIgnore
    private String password;

    @Column(name = "RUOLO")
    @Enumerated(value = EnumType.STRING)
    private AccountRole role;

    @Column(name = "CREATO_IL")
    // https://stackoverflow.com/questions/30717640/lombok-exclude-property-from-builder
    private ZonedDateTime createdAt;

    @PrePersist
    private void beforeSave() {
        this.createdAt = ZonedDateTime.now();
    }
}
