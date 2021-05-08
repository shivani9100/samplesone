package com.Inventory.Project.AssectService.Exceldata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;

@Component
public class AssetTypeExcel {

	@Autowired
	private Environment environment;

	@SuppressWarnings("resource")
	public ByteArrayInputStream exportingAssetDataToExcelFile(List<AssetTypeMaster> assetlsit) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("AssetType");

		XSSFCellStyle headercellstyle = workbook.createCellStyle();
		headercellstyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headercellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headercellstyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);

		Row row = sheet.createRow(0);

		String string = environment.getProperty("assettype.table.columns");
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(headercellstyle);
			cell.setCellValue(split[i]);
		}
		for (int i = 0; i < assetlsit.size(); i++) {

			Row datarow = sheet.createRow(i + 1);

			datarow.createCell(0).setCellValue(assetlsit.get(i).isAssetStatus());
			datarow.createCell(1).setCellValue(assetlsit.get(i).getAssetType());

		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	String SHEET = "AssetType";

	public boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public List<AssetTypeMaster> readingAssetTypeDataFromExcel(InputStream is) throws RecordNotFoundException {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			java.util.Iterator<Row> rows = sheet.iterator();

			ArrayList<AssetTypeMaster> assetTypes = new ArrayList<AssetTypeMaster>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				java.util.Iterator<Cell> cellsInRow = currentRow.iterator();

				AssetTypeMaster assetTypemaster = new AssetTypeMaster();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						assetTypemaster.setAssetType(currentCell.getStringCellValue());
						break;

					default:
						break;
					}
					cellIdx++;
				}
				assetTypes.add(assetTypemaster);
			}

			if (!assetTypes.isEmpty()) {
                workbook.close();
                return assetTypes;
            } else {
                throw new RecordNotFoundException("File is Empty");
            }

		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}

	}

	public ByteArrayInputStream dummyAssetTypeExcelFile() throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("AssetTypeFormatExcel");

		XSSFCellStyle headercellstyle = workbook.createCellStyle();
		headercellstyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headercellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headercellstyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);

		CreationHelper creationHelper = workbook.getCreationHelper();

		Row row = sheet.createRow(0);

		String string = environment.getProperty("assettype.table.columns");
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {

			cell = row.createCell(i);
			cell.setCellStyle(headercellstyle);
			cell.setCellValue(split[i]);
		}

		

		for (int i = 0; i < split.length; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new ByteArrayInputStream(outputStream.toByteArray());

	}
}
