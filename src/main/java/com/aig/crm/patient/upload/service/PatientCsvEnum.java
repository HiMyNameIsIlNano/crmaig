package com.aig.crm.patient.upload.service;

public enum PatientCsvEnum {

    INSERTION_DATE(0),
    SURNAME(1),
    NAME(2),
    GLYCOGEN_SD_TYPE(3),
    DATE_OF_BIRTH(4),
    AGE(5),
    HOSPITAL(6),
    ADOPTING_HOSPITAL(7),
    PARENTS(8),
    SONS(9),
    CORRECT_DATA(10),
    CARE_GIVER(11),
    QA_2016(12),
    QA_2017(13),
    QA_2018(14),
    NOTE(15);

    private int index;

    PatientCsvEnum(int index) {
        this.index = index;
    }

    private int getIndex() {
        return index;
    }

    public static int getRecordLength() { return NOTE.index; }

    public static int fromName(PatientCsvEnum column) {
        return column.getIndex();
    }
}
