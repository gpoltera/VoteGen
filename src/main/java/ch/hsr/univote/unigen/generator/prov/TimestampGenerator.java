/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator.prov;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Gian
 */
public class TimestampGenerator {

    public static final XMLGregorianCalendar generateTimestamp() throws DatatypeConfigurationException {
        Date date = new Date();
        date.getDay();

        GregorianCalendar gcal = new GregorianCalendar();

        XMLGregorianCalendar xgcal;
        xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal).normalize();

        xgcal.setTime(date.getHours(), date.getMinutes(), date.getSeconds());
        xgcal.setDay(date.getDay());
        xgcal.setMonth(date.getMonth());
        xgcal.setYear(date.getYear());
        xgcal.setFractionalSecond(BigDecimal.ZERO);
        xgcal.setMillisecond(0);
        xgcal.setTimezone(0);

        return xgcal;
    }
}
