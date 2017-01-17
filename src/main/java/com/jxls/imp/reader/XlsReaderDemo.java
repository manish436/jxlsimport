package com.jxls.imp.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.jxls.imp.model.Department;
import com.jxls.imp.model.Employee;

/**
 * Created by Leonid Vysochyn on 6/24/2015.
 */
public class XlsReaderDemo {
	static Logger logger = LoggerFactory.getLogger(XlsReaderDemo.class);
	private static String dataFile = "department_data.xls";
	public static final String xmlConfig = "departments.xml";

	public static void main(String[] args) {
		logger.info("Running XLS Reader demo");

		try {
			execute();
		} catch (InvalidFormatException e) {
			logger.info("InvalidFormatException");
		} catch (IOException e) {
			logger.info("IOException");
		} catch (SAXException e) {
			logger.info("SAXException....");
		} catch (Exception e) {
			logger.info("Exception.... " + e.getMessage());
		}
	}

	public static void execute() throws IOException, SAXException, InvalidFormatException {
		logger.info("Reading xml config file and constructing XLSReader");
		try (InputStream xmlInputStream = XlsReaderDemo.class.getResourceAsStream(xmlConfig)) {
			XLSReader reader = ReaderBuilder.buildFromXML(xmlInputStream);
			try (InputStream xlsInputStream = XlsReaderDemo.class.getResourceAsStream(dataFile)) {
				Department department = new Department();
				Department hrDepartment = new Department();
				List<Department> departments = new ArrayList<>();
				Map<String, Object> beans = new HashMap<>();
				beans.put("department", department);
				beans.put("hrDepartment", hrDepartment);
				beans.put("departments", departments);
				logger.info("Reading the data...");
				reader.read(xlsInputStream, beans);
				logger.info("Read " + departments.size() + " departments into departments list");
				logger.info("Read " + department.getName() + " department into department variable");
				logger.info("Read " + hrDepartment.getHeadcount() + " employees	 in hrDepartment");
				logger.info("Printing IT department employees and birthdays:");
				for (Employee employee : department.getStaff()) {
					logger.info(employee.getName() + ": " + employee.getBirthDate());
				}
			}
		}
	}
}