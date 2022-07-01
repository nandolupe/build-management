package com.skymicrosystems.buildmanagement.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
	
	@Override
    public Date convertToDatabaseColumn(LocalDate locDate) {
		
        return locDate == null ? null : Date.from(locDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
 
    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return sqlDate == null ? null : Instant.ofEpochMilli(sqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
	
}
