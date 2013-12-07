/*
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project UniVote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */

package ch.hsr.univote.unigen.helper;

import java.io.PrintWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Provides support for the conversion of JAXB objects to XML and vice versa.
 *
 * @author Eric Dubuis &lt;eric.dubuis@bfh.ch&gt;
 */
public class JAXBHelper {

   // public static void fromJAXBtoXML(Certificates certificateList, PrintWriter printWriter)
   //     throws JAXBException
   // {
   //     JAXBContext context = JAXBContext.newInstance(Certificates.class);
   //     Marshaller m = context.createMarshaller();
   //     m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
   //     m.marshal(certificateList, printWriter);
   // }
}
