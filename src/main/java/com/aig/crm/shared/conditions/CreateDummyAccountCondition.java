package com.aig.crm.shared.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class CreateDummyAccountCondition implements Condition {

    private static final String CREATE_DUMMY_ACCOUNTS = "crm.db.init.create-dummy-accounts";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment()
                .getProperty(CREATE_DUMMY_ACCOUNTS)
                .equals(Boolean.TRUE.toString());
    }

}