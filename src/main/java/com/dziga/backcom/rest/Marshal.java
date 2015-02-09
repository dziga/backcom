package com.dziga.backcom.rest;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

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
    public static Object toObject (String input, Object modelObject) throws JAXBException, XMLStreamException, JSONException {
        if (input == null || input.isEmpty()) {
            return null;
        }
        if(!input.startsWith("<")) {
        	JSONObject json = new JSONObject(input);
        	input = XML.toString(json);
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(((JAXBElement<Object>) modelObject).getDeclaredType().getPackage().getName());
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Object> jb = (JAXBElement<Object>) unmarshaller.unmarshal(new StreamSource(new StringReader(input)));

        return (Object) jb.getValue();
    }
	
	public static String toJson (Object modelObject) throws JAXBException, JSONException {
		String xml = toXml(modelObject);
		JSONObject json = XML.toJSONObject(xml);
        return json.toString();
	}

}
