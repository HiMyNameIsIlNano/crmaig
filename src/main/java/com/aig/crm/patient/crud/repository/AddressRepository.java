package com.aig.crm.patient.crud.repository;

import com.aig.crm.patient.crud.model.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
}
