package com.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csv.CsvHelper;
import com.elements.Content;
import com.elements.Header;

/**
 * Main REST controller
 * 
 * @author igors
 *
 */
@RestController
public class CsvController {
	private final static Logger log = Logger.getLogger(CsvController.class.getName());

	private static String UPLOADS_DIR = "uploads";

	/**
	 * This method consumes the csv file as multipart/form-data and stores it
	 * 
	 * @param file
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam("csvFile") MultipartFile file, HttpServletResponse response) {
		String realPathtoUploads = "C:" + File.separatorChar + UPLOADS_DIR + File.separatorChar;

		if (!checkCsvFile(file.getOriginalFilename(), response)) {
			return;
		}

		if (!new File(realPathtoUploads).exists()) {
			log.log(Level.INFO, "Creating new upload directory: " + realPathtoUploads);
			new File(realPathtoUploads).mkdir();
		}

		File dest = new File(realPathtoUploads + file.getOriginalFilename());
		try {
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			try {
				response.getWriter().append("Error while processing the csv file.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		log.log(Level.INFO, "Csv file uploaded successfully " + dest.getName());
	}

	/**
	 * This method consumes the csv file as multipart/form-data, processes it
	 * and returns the Header of the csv
	 * 
	 * @param file
	 * @return Header
	 */
	@RequestMapping(value = "/getHeader", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/xml")
	public Header getHeader(@RequestParam("csvFile") MultipartFile file, HttpServletResponse response) {

		if (!checkCsvFile(file.getOriginalFilename(), response)) {
			return null;
		}

		Header header = null;
		try {
			header = CsvHelper.getHeader(file.getInputStream());
		} catch (IOException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			try {
				response.getWriter().append("Error while processing the csv file.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * * This method consumes the csv file as multipart/form-data, processes it
	 * and returns the Content of the csv. If the range is from 0 then a header
	 * is returned in the Content object
	 * 
	 * @param file
	 * @param fromRange
	 * @param toRange
	 * @return
	 */
	@RequestMapping(value = "/getContent", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/xml")
	public Content getContent(@RequestParam("csvFile") MultipartFile file, @RequestParam("from") int fromRange,
			@RequestParam("to") int toRange, HttpServletResponse response) {

		if (!checkCsvFile(file.getOriginalFilename(), response)) {
			return null;
		}

		Content content = null;
		try {
			content = CsvHelper.getContent(file.getInputStream(), fromRange, toRange);
		} catch (IOException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			try {
				response.getWriter().append("Error while processing the csv file.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return content;
	}

	/**
	 * This method returns the Header from a specified uploaded csv file
	 * 
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/getHeaderFromSavedFile", method = RequestMethod.GET, produces = "application/xml")
	public Header getHeaderFromSavedFile(@RequestParam("filename") String filename) {

		String realPathtoUploads = "C:" + File.separatorChar + UPLOADS_DIR + File.separatorChar;

		if (!new File(realPathtoUploads).exists()) {
			log.log(Level.INFO, "Creating new upload directory: " + realPathtoUploads);
			new File(realPathtoUploads).mkdir();
		}

		File requestCsvFile = new File(realPathtoUploads + filename);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(requestCsvFile);

		} catch (FileNotFoundException e) {
			log.log(Level.INFO, "File not found:" + filename);
			Header header = new Header();
			header.setRawHeader("The csv file " + filename + " doesn't exist on the server.");
			return header;
		}
		Header header = CsvHelper.getHeader(fis);

		return header;
	}

	/**
	 * This method returns the Content of the specified uploaded csv file.
	 * If the range is from 0 then a header is returned in the Content object
	 * 
	 * @param filename
	 * @param fromRange
	 * @param toRange
	 * @return
	 */
	@RequestMapping(value = "/getContentFromSavedFile", method = RequestMethod.GET, produces = "application/xml")
	public Content getContentFromSavedFile(@RequestParam("filename") String filename,
			@RequestParam("from") int fromRange, @RequestParam("to") int toRange) {

		String realPathtoUploads = "C:" + File.separatorChar + UPLOADS_DIR + File.separatorChar;

		if (!new File(realPathtoUploads).exists()) {
			log.log(Level.INFO, "Creating new upload directory: " + realPathtoUploads);
			new File(realPathtoUploads).mkdir();
		}

		File requestCsvFile = new File(realPathtoUploads + filename);
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(requestCsvFile);
		} catch (FileNotFoundException e) {
			log.log(Level.INFO, "File not found:" + filename);
			return new Content();
		}

		Content content = CsvHelper.getContent(fis, fromRange, toRange);

		return content;
	}

	/**
	 * Method that checks if the file is csv
	 * Sets the response to return code 415 as unsupported media type
	 * @param filename
	 * @param response
	 * @return
	 */
	private Boolean checkCsvFile(String filename, HttpServletResponse response) {
		if (!filename.toLowerCase().contains(".csv")) {
			response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
			try {
				response.getWriter().append("This call consumes only CSV files.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

}
