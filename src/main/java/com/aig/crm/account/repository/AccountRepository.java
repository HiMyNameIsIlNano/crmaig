package com.aig.crm.account.repository;

import com.aig.crm.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findOneByUserName(String userName);
}
