package com.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class Row {
	
	@XmlElementWrapper(name="values")
	@XmlElement(name = "value")
	private List<String> values;
	
	@XmlElement
	private String rawValues;

	public Row() {
		values = new ArrayList<String>();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getRawValues() {
		return rawValues;
	}

	public void setRawValues(String rawValues) {
		this.rawValues = rawValues;
	}

}
