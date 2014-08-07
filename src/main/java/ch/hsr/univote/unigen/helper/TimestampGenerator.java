/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.helper;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Gian
 */
public class TimestampGenerator {

    public static final XMLGregorianCalendar generateTimestamp() {
        XMLGregorianCalendar xgcal = null;
        try {
            Date date = new Date();
            GregorianCalendar gcal = new GregorianCalendar();
                        
            xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal).normalize();
            
            xgcal.setTime(date.getHours(), date.getMinutes(), date.getSeconds());
            xgcal.setDay(gcal.get(Calendar.DAY_OF_MONTH));
            xgcal.setMonth(gcal.get(Calendar.JANUARY));
            xgcal.setYear(gcal.get(Calendar.YEAR));
            xgcal.setFractionalSecond(BigDecimal.ZERO);
            xgcal.setMillisecond(0);
            xgcal.setTimezone(0);      
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(TimestampGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xgcal;
    }
}
