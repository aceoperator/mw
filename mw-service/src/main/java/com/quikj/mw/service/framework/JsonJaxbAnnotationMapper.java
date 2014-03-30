/**
 * 
 */
package com.quikj.mw.service.framework;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

/**
 * @author amit
 *
 */
public class JsonJaxbAnnotationMapper extends ObjectMapper {

	public JsonJaxbAnnotationMapper() {
	}
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
	    getDeserializationConfig().setAnnotationIntrospector(introspector);
	    getSerializationConfig().setAnnotationIntrospector(introspector);
	}	
}
