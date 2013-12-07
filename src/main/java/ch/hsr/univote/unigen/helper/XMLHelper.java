/*
 * Copyright (c) 2012 Berner Fachhochschule, Switzerland.
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

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

/**
 * XML helper class.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class XMLHelper {

	public static String serialize(Object object) {
		String name = object.getClass().getSimpleName();
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, ConfigHelper.getCharEncoding());
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement element = new JAXBElement(new QName(name), object.getClass(), object);
			StringWriter message = new StringWriter();
			marshaller.marshal(element, message);
			return message.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}

