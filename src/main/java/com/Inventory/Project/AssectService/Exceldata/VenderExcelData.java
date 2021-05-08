package com.Inventory.Project.AssectService.Exceldata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.Vendor;

@Component
public class VenderExcelData<E> {

	@Autowired
	Environment environment;

	@SuppressWarnings("resource")
	public ByteArrayInputStream exportingVendorDataToExcelFile(java.util.List<Vendor> vendorlist) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("VendorData");
		CellStyle cellStyle = workbook.createCellStyle();

		cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		XSSFRow row = sheet.createRow(0);

		String property = environment.getProperty("vendor.table.columns");
		String[] split = property.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(split[i]);

		}

		for (int i = 0; i < vendorlist.size(); i++) {

			XSSFRow datarow = sheet.createRow(i + 1);

			datarow.createCell(0).setCellValue(vendorlist.get(i).getVendorName());
			datarow.createCell(1).setCellValue(vendorlist.get(i).getEmail());
			datarow.createCell(2).setCellValue(vendorlist.get(i).getGstNumber());
			datarow.createCell(3).setCellValue(vendorlist.get(i).getContactNumber());
			datarow.createCell(4).setCellValue(vendorlist.get(i).getStreetLine1());
			datarow.createCell(5).setCellValue(vendorlist.get(i).getStreetLine2());
			datarow.createCell(6).setCellValue(vendorlist.get(i).getCityname());
			datarow.createCell(7).setCellValue(vendorlist.get(i).getState());
			datarow.createCell(8).setCellValue(vendorlist.get(i).getPincode());
			datarow.createCell(9).setCellValue(vendorlist.get(i).getVendorStatus());
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);

		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	String SHEET = "Vendor";

	public boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("resource")
	public ByteArrayInputStream dummyVendorExcelFile() throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("VendorDummyExcelFile");

		CellStyle cellStyle = workbook.createCellStyle();

		XSSFCreationHelper creationHelper = workbook.getCreationHelper();

		cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		String property = environment.getProperty("vendor.table.columns");

		XSSFRow row = sheet.createRow(0);

		String[] split = property.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(split[i]);
			sheet.autoSizeColumn(i);

		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	public ArrayList<Vendor> readingVendorDataFromExcelFile(InputStream file)
			throws FeildsShouldNotBeEmptyException, RecordNotFoundException {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			System.out.println(sheet);
			java.util.Iterator<Row> rows = sheet.iterator();

			ArrayList<Vendor> vendorlist = new ArrayList<Vendor>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip Headers
				Vendor vendor = new Vendor();
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				// Vendor Name //
				Cell cell0 = sheet.getRow(rowNumber).getCell(0);
				DataFormatter formatter0 = new DataFormatter();
				String vendorName = formatter0.formatCellValue(cell0);
				if (cell0 == null) {
					throw new FeildsShouldNotBeEmptyException("Vendor Name Should Not Be Empty");
				}
				vendor.setVendorName(vendorName);

				// Vendor Email //
				Cell cell1 = sheet.getRow(rowNumber).getCell(1);
				DataFormatter formatter1 = new DataFormatter();
				String email = formatter1.formatCellValue(cell1);
				if (cell1 == null) {
					throw new FeildsShouldNotBeEmptyException("Vendor Email Should Not Be Empty");
				}
				vendor.setEmail(email);

				// Vendor GST Number //
				Cell cell2 = sheet.getRow(rowNumber).getCell(2);
				DataFormatter formatter2 = new DataFormatter();
				String gstNumber = formatter2.formatCellValue(cell2);
				if (cell2 == null) {
					throw new FeildsShouldNotBeEmptyException("Vendor of GST Number Should Not Be Empty");
				}
				vendor.setGstNumber(gstNumber);

				// Vendor Contact Number //
				Cell cell3 = sheet.getRow(rowNumber).getCell(3);
				DataFormatter formatter3 = new DataFormatter();
				String contact = formatter3.formatCellValue(cell3);
				if (cell3 == null) {
					throw new FeildsShouldNotBeEmptyException("Vendor Contact Number Should Not Be Empty");
				}
				vendor.setContactNumber(contact);

				// Vendor StreetLine -- 1 //
				Cell cell4 = sheet.getRow(rowNumber).getCell(4);
				DataFormatter formatter4 = new DataFormatter();
				String streetLine1 = formatter4.formatCellValue(cell4);
				if (cell4 == null) {
					throw new FeildsShouldNotBeEmptyException("StreetLine--1 Should Not Be Empty");
				}
				vendor.setStreetLine1(streetLine1.toString());

				// Vendor StreetLine -- 2 //
				Cell cell5 = sheet.getRow(rowNumber).getCell(5);
				DataFormatter formatter5 = new DataFormatter();
				String streetLine2 = formatter5.formatCellValue(cell5);
				if (cell5 == null) {
					vendor.setStreetLine2("Null");
				}
				vendor.setStreetLine2(streetLine2.toString());

				// Vendor City Details //
				Cell cell6 = sheet.getRow(rowNumber).getCell(6);
				DataFormatter formatter6 = new DataFormatter();
				String city = formatter6.formatCellValue(cell6);
				if (cell6 == null) {
					throw new FeildsShouldNotBeEmptyException("City Details Should Not Be Empty");
				}
				vendor.setCityname(city);

				// Vendor State Details //
				Cell cell7 = sheet.getRow(rowNumber).getCell(7);
				DataFormatter formatter7 = new DataFormatter();
				String state = formatter7.formatCellValue(cell7);
				if (cell7 == null) {
					throw new FeildsShouldNotBeEmptyException("State Details Should Not Be Empty");
				}
				vendor.setState(state);

				// Vendor Pincode Details //
				Cell cell8 = sheet.getRow(rowNumber).getCell(8);
				DataFormatter formatter8 = new DataFormatter();
				String pincode = formatter8.formatCellValue(cell8);
				if (cell8 == null) {
					throw new FeildsShouldNotBeEmptyException("Pincode Details Should Not Be Empty");
				}
				vendor.setPincode(pincode);

				vendorlist.add(vendor);
				rowNumber++;
			}
			if (!vendorlist.isEmpty()) {

				workbook.close();

				return vendorlist;

			} else {
				throw new RecordNotFoundException("File is Empty");
			}

		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
