package com.aig.crm.shared.service;

import codicefiscale.wsdl.CalcolaCodiceFiscale;
import codicefiscale.wsdl.CalcolaCodiceFiscaleResponse;
import com.aig.crm.shared.validator.SsnValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.Optional;

public class PatientNaturalKeyCalculatorClient extends WebServiceGatewaySupport {

    private static final String URI = "http://webservices.dotnethell.it/codicefiscale.asmx";
    private static final String SOAP_ACTION = "http://webservices.dotnethell.it/CodiceFiscale/CalcolaCodiceFiscale";
    private final static String WSDL = "codicefiscale.wsdl";

    private static final Logger log = LoggerFactory.getLogger(PatientNaturalKeyCalculatorClient.class);

    public static String getURI() {
        return URI;
    }

    public static String getWSDL() {
        return WSDL;
    }

    public Optional<String> getPatientNaturalKey(String name, String surname, String cityOfBirth, String dateOfBirth, String gender) {
        CalcolaCodiceFiscale request = new CalcolaCodiceFiscale();
        request.setNome(name);
        request.setCognome(surname);
        request.setComuneNascita(cityOfBirth);
        request.setDataNascita(dateOfBirth);
        request.setSesso(gender); // M or F

        // TODO
        log.info("Requesting Codice Fiscale for ...");

        CalcolaCodiceFiscaleResponse response = (CalcolaCodiceFiscaleResponse) getWebServiceTemplate()
                .marshalSendAndReceive(URI,
                        request,
                        new SoapActionCallback(SOAP_ACTION));

        String ssn = response.getCalcolaCodiceFiscaleResult();
        return SsnValidator.validate(ssn);
    }

}