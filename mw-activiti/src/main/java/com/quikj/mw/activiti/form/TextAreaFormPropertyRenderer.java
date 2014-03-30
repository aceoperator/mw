/**
 * 
 */
package com.quikj.mw.activiti.form;

import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;

/**
 * @author amit
 * 
 */
public class TextAreaFormPropertyRenderer extends AbstractFormPropertyRenderer {
	private static final long serialVersionUID = 8885889201099121997L;

	public TextAreaFormPropertyRenderer() {
		super(TextAreaFormType.class);
	}
	
	@Override
	public Field getPropertyField(FormProperty formProperty) {
		TextArea textArea = new TextArea(getPropertyLabel(formProperty));
		textArea.setRequired(formProperty.isRequired());
		textArea.setEnabled(formProperty.isWritable());
		textArea.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED,
				getPropertyLabel(formProperty)));
		textArea.setRows(10);
		textArea.setColumns(50);
		
		if (formProperty.getValue() != null) {
			textArea.setValue(formProperty.getValue());
		}
		
		return textArea;
	}
}
