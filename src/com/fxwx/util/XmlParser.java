package com.fxwx.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XmlParser {
	
	//对象转为xml
	public static String objectToXml(Object object) {
		XStream stream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		//stream.alias("xml", object.getClass());
		stream.processAnnotations(object.getClass());
		String xml = stream.toXML(object);
		return xml;
	}
	
	//xml转为对象
	@SuppressWarnings("unchecked")
	public static <T> T xmlTransferObject(String xml,Class<T> classzs){
		 XStream stream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		 stream.processAnnotations(classzs);
		 return (T)stream.fromXML(xml);
	}

}
