/**
 * 
 */
package com.quikj.mw.service.framework;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.Marshaller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author amit
 * 
 */
public class ServiceExceptionView extends AbstractView {

	private static final String ACCEPT_HEADER = "Accept";
	private Marshaller xmlMarshaller;
	private ObjectMapper jsonMapper;

	public ServiceExceptionView() {
	}

	public ObjectMapper getJsonMapper() {
		return jsonMapper;
	}

	public void setJsonMapper(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public Marshaller getXmlMarshaller() {
		return xmlMarshaller;
	}

	public void setXmlMarshaller(Marshaller xmlMarshaller) {
		this.xmlMarshaller = xmlMarshaller;
	}

	@Override
	protected void renderMergedOutputModel(java.util.Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Object toBeMarshalled = locateToBeMarshalled(model);
		if (toBeMarshalled == null) {
			throw new ServletException(
					"Unable to locate object to be marshalled in model: "
							+ model);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);

		if (request.getHeader(ACCEPT_HEADER) != null
				&& request.getHeader(ACCEPT_HEADER).contains("xml")) {
			xmlMarshaller.marshal(toBeMarshalled, new StreamResult(bos));
			response.setContentType("application/xml");
		} else {
			jsonMapper.writeValue(bos, toBeMarshalled);
			response.setContentType("application/json");
		}

		response.setContentLength(bos.size());
		response.setStatus(HttpStatus.METHOD_FAILURE.value());

		FileCopyUtils.copy(bos.toByteArray(), response.getOutputStream());
	}

	protected Object locateToBeMarshalled(Map<?, ?> model)
			throws ServletException {

		if (model.size() != 1) {
			throw new ServletException("There can only be one model object");
		}

		for (Object o : model.values()) {
			if (xmlMarshaller.supports(o.getClass())) {
				return o;
			}
		}
		return null;
	}
}
