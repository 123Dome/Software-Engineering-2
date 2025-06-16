package org.hbrs.se2.project.startupx.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BewerbungsStatusConverter implements AttributeConverter<BewerbungsStatus, String> {

    @Override
    public String convertToDatabaseColumn(BewerbungsStatus status) {
        return status == null ? null : status.name();
    }

    @Override
    public BewerbungsStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : BewerbungsStatus.valueOf(dbData);
    }
}
