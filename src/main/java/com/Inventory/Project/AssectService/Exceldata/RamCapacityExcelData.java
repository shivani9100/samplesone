package com.Inventory.Project.AssectService.Exceldata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.RamTypeDao;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RamTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;

@Component
public class RamCapacityExcelData {

	@Autowired
	org.springframework.core.env.Environment environment;

	@Autowired
	RamTypeDao ramTypeDao;

	public ByteArrayInputStream exportDummyRamTypeListToExcelFile() throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("RamCapacity");

		XSSFCellStyle headercellstyle = workbook.createCellStyle();
		headercellstyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headercellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headercellstyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);
		CreationHelper creationHelper = workbook.getCreationHelper();
		Row row = sheet.createRow(0);

		String string = environment.getProperty("ramcapacity.table.columns");
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(headercellstyle);
			cell.setCellValue(split[i]);
		}
		DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;

		List<RamTypeMaster> ramTypelist = ramTypeDao.findAll();

		String[] array = ramTypelist.stream().map(ramType -> ramType.getRamtypeName()).toArray(String[]::new);
		validationHelper = new XSSFDataValidationHelper(sheet);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 0, 0);
		constraint = validationHelper.createExplicitListConstraint(array);
		dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);
		dataValidation.setShowErrorBox(true);
		dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		dataValidation.createErrorBox("Invalid Data","Please Select Data from the Drop Down");
		sheet.addValidationData(dataValidation);
		XSSFRow row1 = sheet.createRow(1);

		for (int i = 0; i < split.length; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	String SHEET = "RamCapacity";

	public boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public List<RamCapacityMaster> readingRamCapacityDataFromExcel(InputStream is)
			throws RamTypeNotFoundException, FeildsShouldNotBeEmptyException, RecordNotFoundException {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet("RamCapacity");
			java.util.Iterator<Row> rows = sheet.iterator();

			ArrayList<RamCapacityMaster> ramCapacities = new ArrayList<RamCapacityMaster>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				RamCapacityMaster ramCapacity = new RamCapacityMaster();
				Cell cell = sheet.getRow(rowNumber).getCell(0);
				DataFormatter dataFormatter2 = new DataFormatter();

				String formatCellValue = dataFormatter2.formatCellValue(cell);
				if (formatCellValue == null) {
					throw new FeildsShouldNotBeEmptyException("Ram type Feild shouldn't be empty");
				}

				RamTypeMaster ramType = ramTypeDao.findByRamtypeName(formatCellValue);

				if (ramType == null) {
					throw new RamTypeNotFoundException("Ram type not found");
				}

				List<RamTypeMaster> ramTypeMaster = new ArrayList();
				ramTypeMaster.add(ramType);
				ramCapacity.setRamTypeMasters(ramTypeMaster);

				Cell cell2 = sheet.getRow(rowNumber).getCell(1);
				DataFormatter dataFormatter = new DataFormatter();
				String ramcapa = dataFormatter.formatCellValue(cell2);
				if (cell2 == null) {
					throw new FeildsShouldNotBeEmptyException("Ram Capacity Field Should Not Be Empty");
				}
				ramCapacity.setRamCapacity(ramcapa);

				ramCapacities.add(ramCapacity);
				rowNumber++;
			}

			if (!ramCapacities.isEmpty()) {
				workbook.close();
				return ramCapacities;
			} else {

				throw new RecordNotFoundException("RamCapacity File is Empty");
			}
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}

}
