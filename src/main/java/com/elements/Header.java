package com.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class Header {

	@XmlElementWrapper(name="headerNames")
	@XmlElement(name = "headerName")
	private List<String> headerNames;
	@XmlElement
	private String rawHeader;

	public Header() {
		headerNames = new ArrayList<String>();
	}

	public List<String> getHeaderNames() {
		return headerNames;
	}

	public void setHeaderNames(List<String> headerNames) {
		this.headerNames = headerNames;
	}

	public String getRawHeader() {
		return rawHeader;
	}

	public void setRawHeader(String rawHeader) {
		this.rawHeader = rawHeader;
	}

	@Override
	public String toString() {
		return "Header [headerNames=" + headerNames + ", rawHeader=" + rawHeader + "]";
	}

}
