package com.Inventory.Project.AssectService.Exceldata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;

@Component
public class BrandExcelData {
	@Autowired
	private Environment environment;

	@Autowired
	private AssetTypeRepositry assetTypeRepositry;

	public ByteArrayInputStream exportingBrandDataToExcelFile(List<Brand> brandlist) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("Brand");

		XSSFCellStyle headercellstyle = workbook.createCellStyle();
		headercellstyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headercellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headercellstyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);

		Row row = sheet.createRow(0);

		String string = environment.getProperty("brand.table.columns");
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(headercellstyle);
			cell.setCellValue(split[i]);
		}
		for (int i = 0; i < brandlist.size(); i++) {

			Row datarow = sheet.createRow(i + 1);

			datarow.createCell(0).setCellValue(brandlist.get(i).getBrandname());
			datarow.createCell(1).setCellValue(brandlist.get(i).getBrandstatus());

		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	String SHEET = "Brand";

	public boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public List<Brand> readingBrandDataFromExcel(InputStream is)
			throws AssetTypeNotFoundException, FeildsShouldNotBeEmptyException, RecordNotFoundException {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(SHEET);
			java.util.Iterator<Row> rows = sheet.iterator();

			ArrayList<Brand> brands = new ArrayList<Brand>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Brand brand = new Brand();

				// Asset Type Name //
				Cell cell0 = sheet.getRow(rowNumber).getCell(0);
				DataFormatter formatter0 = new DataFormatter();
				String assetTypeName = formatter0.formatCellValue(cell0);
				AssetTypeMaster assetType = assetTypeRepositry.findByassetType(assetTypeName);
				if (cell0 == null) {
					throw new FeildsShouldNotBeEmptyException("AssetType Shouldn't be empty or not found");
				}

				if (assetType == null) {
					throw new AssetTypeNotFoundException("AssetType Not found");
				}
				Set<AssetTypeMaster> asset = new HashSet<>();
				asset.add(assetType);
				brand.setAssetTypeMasterEx(assetType);

				// Brand details //
				Cell cell1 = sheet.getRow(rowNumber).getCell(1);
				DataFormatter formatter1 = new DataFormatter();
				String brandName = formatter1.formatCellValue(cell1);
				if (cell1 == null) {
					throw new FeildsShouldNotBeEmptyException("Brand Should Not Be Empty");
				}

				brand.setBrandname(brandName);

				brands.add(brand);
				rowNumber++;
			}
			if (!brands.isEmpty()) {
				workbook.close();
				return brands;
			} else {
				throw new RecordNotFoundException("File is Empty");
			}
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}

	}

	public ByteArrayInputStream dummyBrandExcelFile() throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("Brand");

		XSSFCellStyle headercellstyle = workbook.createCellStyle();
		headercellstyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headercellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headercellstyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);
		CreationHelper creationHelper = workbook.getCreationHelper();
		Row row = sheet.createRow(0);

		String string = environment.getProperty("brand.table.columns");
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

		List<AssetTypeMaster> list = assetTypeRepositry.findAll();

		String[] array = list.stream().map(asset -> asset.getAssetType()).toArray(String[]::new);
		validationHelper = new XSSFDataValidationHelper(sheet);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 1, 0, 0);
		constraint = validationHelper.createExplicitListConstraint(array);
		dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);
		dataValidation.setShowErrorBox(true);
		dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		dataValidation.createErrorBox("Invalid Data","Please Select Data from the Drop Down");
		

		dataValidation.setShowErrorBox(true);
		sheet.addValidationData(dataValidation);
		for (int i = 0; i < split.length; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return new ByteArrayInputStream(outputStream.toByteArray());

	}

}
