package com.dziga.backcom.service;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;

public class Marshal {

	@SuppressWarnings("unchecked")
    public static <T> String toXml (Object modelObject) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(((JAXBElement<Object>) modelObject).getDeclaredType().getPackage().getName());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter writer = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        jaxbMarshaller.marshal(modelObject, writer);
        return writer.toString();
    }

	@SuppressWarnings("unchecked")
    public static Object toObject (String xml, Object modelObject) throws JAXBException, XMLStreamException {
        if (xml == null || xml.isEmpty()) {
            return null;
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(((JAXBElement<Object>) modelObject).getDeclaredType().getPackage().getName());
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Object> jb = (JAXBElement<Object>) unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
 
        return (Object) jb.getValue();
    }

}
