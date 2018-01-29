package com.aig.crm.shared.service;

import com.aig.crm.account.model.Account;
import com.aig.crm.account.model.AccountRole;
import com.aig.crm.account.service.AccountService;
import com.aig.crm.shared.conditions.CreateDummyAccountCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Service
@Conditional(value = CreateDummyAccountCondition.class)
public class AccountPopulatorServiceImpl {

    @Inject
    private AccountService accountService;

    @Inject
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    protected void initialize() {
        accountService.save(Account.builder()
                .userName("admin")
                .password(passwordEncoder.encode("password"))
                .role(AccountRole.ROLE_ADMIN)
                .build());

        accountService.save(Account.builder()
                .userName("user")
                .password(passwordEncoder.encode("password"))
                .role(AccountRole.ROLE_USER)
                .build());
    }

}
