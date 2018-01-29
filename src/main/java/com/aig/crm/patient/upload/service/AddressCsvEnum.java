package com.aig.crm.patient.upload.service;

public enum AddressCsvEnum {

    STREET(0),
    CITY(1),
    POBOX(2),
    PROV(3),
    DELIVERY_ADDRESS(4),
    REGION(5),
    NATION(6),
    EMAIL(7),
    PHONE(8),
    MOBILE_PHONE(9);

    private int index;

    AddressCsvEnum(int index) {
        this.index = index;
    }

    private int getIndex() {
        return index;
    }

    public static int fromName(AddressCsvEnum column) {
        return column.getIndex();
    }
}
