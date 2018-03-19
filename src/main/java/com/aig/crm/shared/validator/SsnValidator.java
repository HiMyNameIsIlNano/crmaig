package com.aig.crm.shared.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SsnValidator {

    private static final Logger log = LoggerFactory.getLogger(SsnValidator.class);

    private static final String REGEXP = "^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})|([0-9]{11})$";

    public static Optional<String> validate(String value) {
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(value);
        boolean matches = matcher.matches();
        if (matches) {
            return Optional.of(value);
        } else {
            log.info("The following ssn " + value + "is malformed and therefore it will be skipped");
            return Optional.empty();
        }
    }
}
