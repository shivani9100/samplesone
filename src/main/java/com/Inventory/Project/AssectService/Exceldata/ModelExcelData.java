package com.Inventory.Project.AssectService.Exceldata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Dao.BrandRepo;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.BrandTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Model.Model;

@Component
public class ModelExcelData {

	@Autowired
	AssetTypeRepositry assetRepository;

	@Autowired
	BrandRepo brandrepo;

	@Autowired
	org.springframework.core.env.Environment environment;

	@SuppressWarnings("resource")
	public ByteArrayInputStream exportingModelDataToExcelFile(List<Model> models) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("Models");

		XSSFCellStyle headercellstyle = workbook.createCellStyle();
		headercellstyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headercellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headercellstyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);

		Row row = sheet.createRow(0);

		String string = environment.getProperty("model.table.columns");
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(headercellstyle);
			cell.setCellValue(split[i]);
		}
		for (int i = 0; i < models.size(); i++) {

			Row datarow = sheet.createRow(i + 1);

			datarow.createCell(0).setCellValue(models.get(i).getModelname());
			datarow.createCell(1).setCellValue(models.get(i).getModelstatus());

		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	public ByteArrayInputStream exportingDummyModelDataToExcelFile() throws IOException {

		Map<String, String[]> map = new HashMap<String, String[]>();

		List<Brand> brandlist = brandrepo.findAll();

		List<AssetTypeMaster> assetTypelist = assetRepository.findAll();

		String[] array = assetTypelist.stream().map(assetType -> assetType.getAssetType()).toArray(String[]::new);
		Integer[] arrayIds = assetTypelist.stream().map(assetType -> assetType.getAssetId()).toArray(Integer[]::new);

		for (int i = 0; i < assetTypelist.size(); i++) {

			AssetTypeMaster assetType = assetRepository.findById(arrayIds[i]).get();

			List<Brand> brandlist1 = assetType.getBrandList();

			String[] brands = brandlist1.stream().map(brand -> brand.getBrandname()).toArray(String[]::new);

			map.put(assetType.getAssetType(), brands);
		}
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("ListSheet");

		Row row;
		Name namedRange;
		String colLetter;
		String reference;

		int c = 0;

		for (String key : map.keySet()) {
			int r = 0;
			row = sheet.getRow(r);
			if (row == null)
				row = sheet.createRow(r);
			r++;
			row.createCell(c).setCellValue(key);
			String[] items = map.get(key);
			if (items.length == 0) {
				row = sheet.getRow(r);
				if (row == null)
					row = sheet.createRow(r);
				r++;
				row.createCell(c).setCellValue("");
			}

			for (String item : items) {
				row = sheet.getRow(r);
				if (row == null)
					row = sheet.createRow(r);
				r++;
				row.createCell(c).setCellValue(item);
			}

			colLetter = CellReference.convertNumToColString(c);
			namedRange = workbook.createName();
			namedRange.setNameName(key);
			reference = "ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + r;
			System.out.println(reference);
			namedRange.setRefersToFormula(reference);
			c++;
		}

		colLetter = CellReference.convertNumToColString((c - 1));
		System.out.println("popr");
		namedRange = workbook.createName();
		namedRange.setNameName("assets");
		reference = "ListSheet!$A$1:$" + colLetter + "$1";
		namedRange.setRefersToFormula(reference);

		sheet.setSelected(false);
		sheet = workbook.createSheet("ModelData");

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);
		String string = environment.getProperty("model.table.columns");
		row = sheet.createRow(0);
		System.out.println(string);
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue(split[i]);

		}
		for (int i = 0; i < split.length; i++) {
			sheet.autoSizeColumn(i);
		}

		CreationHelper creationHelper = workbook.getCreationHelper();
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		System.out.println("popr");
		DataValidationHelper dvHelper = sheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("assets");
		//cell0
		CellRangeAddressList addressList = new CellRangeAddressList(1, 1, 0, 0);
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data","Please Select Data from the DropDown");
		//DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);				
		sheet.addValidationData(validation);		
		dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(ModelData!$A$2)");
		//cell1
		addressList = new CellRangeAddressList(1, 1, 1, 1);

		validation = dvHelper.createValidation(dvConstraint, addressList);
		//validation.setSuppressDropDownArrow(false);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data","Please Select Data from the Drop Down");
		validation = dvHelper.createValidation(dvConstraint, addressList);			
		sheet.addValidationData(validation);		
		workbook.setSheetHidden(0, true);
		workbook.setActiveSheet(1);
		for (int i = 0; i < split.length; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		workbook.write(byteArrayOutputStream);

		workbook.close();

		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	String SHEET = "Model";

	public boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public List<Model> readingModelDataFromExcel(InputStream is) throws AssetTypeNotFoundException,
			BrandTypeNotFoundException, FeildsShouldNotBeEmptyException, RecordNotFoundException {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet("ModelData");
			java.util.Iterator<Row> rows = sheet.iterator();

			ArrayList<Model> models = new ArrayList<Model>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				java.util.Iterator<Cell> cellsInRow = currentRow.iterator();

				Model model = new Model();
				Brand brand = new Brand();
				AssetTypeMaster assetType = new AssetTypeMaster();

				// AssetType
				Cell cell0 = sheet.getRow(rowNumber).getCell(0);
				DataFormatter formatter0 = new DataFormatter();
				String assetypename = formatter0.formatCellValue(cell0);
				assetType = assetRepository.findByassetType(assetypename);
				if (cell0 == null) {
					throw new FeildsShouldNotBeEmptyException("AssetType Shouldn't be empty");
				}

				if (assetType == null) {
					throw new AssetTypeNotFoundException("AssetType Not found");

				}
				
				Set<AssetTypeMaster> assetType1 = new HashSet<>();
				assetType1.add(assetType);
				brand.setAssetTypeMasterEx(assetType);

				// Brand //
				Cell cell1 = sheet.getRow(rowNumber).getCell(1);
				DataFormatter formatter1 = new DataFormatter();
				String brandname = formatter1.formatCellValue(cell1);
				brand = brandrepo.findByBrandnameAndAssetTypeMasterExAssetType(brandname, assetType.getAssetType());

				if (cell1 == null) {
					throw new FeildsShouldNotBeEmptyException("Brand Type Shouldn't be empty");
				}

				if (brand == null) {
					throw new BrandTypeNotFoundException("brand type not found");
				}
				Set<Brand> brand1 = new HashSet<>();
				brand1.add(brand);
				model.setBrand(brand);

				Cell cell2 = sheet.getRow(rowNumber).getCell(2);
				DataFormatter formatter2 = new DataFormatter();
				String model1 = formatter2.formatCellValue(cell2);
				if (cell2 == null) {
					throw new FeildsShouldNotBeEmptyException("Model Should Not Be Empty");
				}
				model.setModelname(model1);
				models.add(model);
			}
			if (!models.isEmpty()) {
				workbook.close();
				return models;
			} else {
				throw new RecordNotFoundException("Model is empty");
			}

		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}

	}
}
