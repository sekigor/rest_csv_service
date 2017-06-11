package com.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {

	@XmlElement
	private Header header;

	@XmlElementWrapper(name="rows")
	@XmlElement(name = "row")
	private List<Row> rows;

	public Content() {
		rows = new ArrayList<Row>();
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

}
