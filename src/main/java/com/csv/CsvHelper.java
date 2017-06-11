package com.csv;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.elements.Content;
import com.elements.Header;
import com.elements.Row;

public class CsvHelper {

	public static Header getHeader(InputStream csvFile) {
		Scanner scanner = new Scanner(csvFile);

		if (scanner.hasNext()) {
			String rawHeader = scanner.nextLine();
			String[] splitHeaderNames = rawHeader.split(",");

			List<String> headerNames = new ArrayList<String>();
			for (String headerName : splitHeaderNames) {
				headerNames.add(headerName);
			}
			Header header = new Header();
			header.setHeaderNames(headerNames);
			header.setRawHeader(rawHeader);
			scanner.close();

			return header;
		}
		scanner.close();

		return null;
	}

	public static Content getContent(InputStream csvFile, int from, int to) {

		Content content = new Content();
		if (to < from) {
			return content;
		}

		int rowNum = 0;
		Scanner scanner = new Scanner(csvFile);
		while (scanner.hasNextLine()) {
			String rawRowValues = scanner.nextLine();

			if (from <= rowNum && rowNum <= to) {
				if (rowNum == 0) {

					String[] splitHeaderNames = rawRowValues.split(",");

					List<String> headerNames = new ArrayList<String>();
					for (String headerName : splitHeaderNames) {
						headerNames.add(headerName);
					}
					Header header = new Header();
					header.setHeaderNames(headerNames);
					header.setRawHeader(rawRowValues);
					content.setHeader(header);
				} else if (rowNum >= from) {

					String[] splitValues = rawRowValues.split(",");

					List<String> values = new ArrayList<String>();
					for (String value : splitValues) {
						values.add(value);
					}
					Row row = new Row();
					row.setValues(values);
					row.setRawValues(rawRowValues);
					content.getRows().add(row);
				}
			} else if (rowNum > to) {
				break;
			}
			rowNum++;
		}

		scanner.close();
		return content;

	}
}
