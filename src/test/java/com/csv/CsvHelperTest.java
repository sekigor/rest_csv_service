package com.csv;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.elements.Content;
import com.elements.Header;
import com.elements.Row;

public class CsvHelperTest {

	@Test
	public void getHeaderTest() {
		String csvHeader = "name 1,name 2,name 3\n";
		String csvString = csvHeader;
		InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
		Header header = CsvHelper.getHeader(stream);

		Assert.assertNotNull(header);
		Assert.assertEquals(3, header.getHeaderNames().size());
		csvHeader = csvHeader.trim();
		List<String> expectedList = Arrays.asList(csvHeader.split(","));

		Assert.assertEquals(expectedList, header.getHeaderNames());
	}

	@Test
	public void getContentTest() {
		String csvHeader = "name 1,name 2,name 3";
		String csvRows = "value 11,value 12,value 13" + System.lineSeparator() + "value 21,value 22,value 23";
		String csvString = csvHeader + System.lineSeparator() + csvRows;
		InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
		Content content = CsvHelper.getContent(stream, 0, 2);

		Assert.assertNotNull(content);

		Header header = content.getHeader();
		Assert.assertNotNull(header);
		Assert.assertEquals(3, header.getHeaderNames().size());

		csvHeader = csvHeader.trim();
		List<String> expectedList = Arrays.asList(csvHeader.split(","));
		Assert.assertEquals(expectedList, header.getHeaderNames());

		List<Row> rows = content.getRows();
		Assert.assertEquals(2, rows.size());
		Assert.assertNotNull(rows);
		int i = 0;
		for (String rowElem : csvRows.split(System.lineSeparator())) {

			List<String> expectedRowList = Arrays.asList(rowElem.split(","));

			Assert.assertEquals(expectedRowList, rows.get(i).getValues());
			Assert.assertEquals(rowElem, rows.get(i).getRawValues());
			i++;
		}
	}

	@Test
	public void getEmptyContentTest() {
		String csvHeader = "name 1,name 2,name 3";
		String csvRows = "value 11,value 12,value 13" + System.lineSeparator() + "value 21,value 22,value 23";
		String csvString = csvHeader + System.lineSeparator() + csvRows;
		InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
		Content content = CsvHelper.getContent(stream, 3, 1);

		Assert.assertNotNull(content);
		Assert.assertNull(content.getHeader());

		List<Row> rows = content.getRows();
		Assert.assertNotNull(rows);
		Assert.assertEquals(0, rows.size());
	}

	@Test
	public void getHeaderlessContentTest() {
		String csvHeader = "name 1,name 2,name 3";
		String firstRow = "value 11,value 12,value 13";
		String csvRows = firstRow + System.lineSeparator() + "value 21,value 22,value 23";
		String csvString = csvHeader + System.lineSeparator() + csvRows;
		InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
		Content content = CsvHelper.getContent(stream, 1, 10);//10 covers also out of bounds 

		Assert.assertNotNull(content);
		Assert.assertNull(content.getHeader());

		List<Row> rows = content.getRows();
		Assert.assertEquals(2, rows.size());
		Assert.assertNotNull(rows);
		int i = 0;
		for (String rowElem : csvRows.split(System.lineSeparator())) {

			List<String> expectedRowList = Arrays.asList(rowElem.split(","));

			Assert.assertEquals(expectedRowList, rows.get(i).getValues());
			Assert.assertEquals(rowElem, rows.get(i).getRawValues());
			i++;
		}
	}
	
	@Test
	public void getHalfContentContentTest() {
		String csvHeader = "name 1,name 2,name 3";
		String firstRow = "value 11,value 12,value 13";
		String csvRows = firstRow + System.lineSeparator() + "value 21,value 22,value 23";
		String csvString = csvHeader + System.lineSeparator() + csvRows;
		InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
		Content content = CsvHelper.getContent(stream, 0, 1);

		Assert.assertNotNull(content);

		Header header = content.getHeader();
		Assert.assertNotNull(header);
		Assert.assertEquals(3, header.getHeaderNames().size());

		csvHeader = csvHeader.trim();
		List<String> expectedList = Arrays.asList(csvHeader.split(","));
		Assert.assertEquals(expectedList, header.getHeaderNames());

		List<Row> rows = content.getRows();
		Assert.assertEquals(1, rows.size());
		Assert.assertNotNull(rows);
		int i = 0;
		for (String rowElem : firstRow.split(System.lineSeparator())) {

			List<String> expectedRowList = Arrays.asList(rowElem.split(","));

			Assert.assertEquals(expectedRowList, rows.get(i).getValues());
			Assert.assertEquals(rowElem, rows.get(i).getRawValues());
			i++;
		}
	}

}
