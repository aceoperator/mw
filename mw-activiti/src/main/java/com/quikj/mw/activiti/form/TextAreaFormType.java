/**
 * 
 */
package com.quikj.mw.activiti.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * @author amit
 *
 */
public class TextAreaFormType extends AbstractFormType {

	public static final String TYPE_NAME = "textarea";
	
	public TextAreaFormType() {
		super();
	}
	
	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return (String)modelValue;
	}
}



