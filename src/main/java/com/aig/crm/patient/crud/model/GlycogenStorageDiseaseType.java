package com.aig.crm.patient.crud.model;

public enum GlycogenStorageDiseaseType {

    NON_DISPONIBILE(""),
    ZERO("0"),
    UNOA("Ia"),
    UNOB("Ib"),
    DUE("II"),
    TRE("III"),
    QUATTRO("IV"),
    CINQUE("V"),
    SEI("VI"),
    SETTE("VII"),
    OTTO("VIII"),
    NOVE("IX"),
    UNDICI("XI");

    private String value;

    GlycogenStorageDiseaseType(String value) {
        this.value = value;
    }

    public static String getValue(GlycogenStorageDiseaseType type) {
        return type.value;
    }

    public static GlycogenStorageDiseaseType fromString(String value) {
        for (GlycogenStorageDiseaseType glycogenStorageDiseaseType : GlycogenStorageDiseaseType.values()) {
            if(glycogenStorageDiseaseType.value.equalsIgnoreCase(value)) {
                return glycogenStorageDiseaseType;
            }
        }
        return null;
    }
}
