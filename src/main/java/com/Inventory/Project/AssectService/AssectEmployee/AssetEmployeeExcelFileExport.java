package com.Inventory.Project.AssectService.AssectEmployee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AssetEmployeeExcelFileExport {

	@Autowired
	private Environment environment;

	public ByteArrayInputStream exportAssetEmployeeListToExcelFile(List<AssectEmployeeModel> assectEmployeeModels)
			throws IOException {

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("AssetEmployeeSheet");

		CellStyle headerstyles = workbook.createCellStyle();
		headerstyles.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerstyles.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Creating a Cell
		Row row = sheet.createRow(0);

		String string = environment.getProperty("assetEmployee.table.columns");
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headerstyles);
			cell.setCellValue(split[i]);

		}

		/*
		 * Cell cell = row.createCell(0); cell.setCellValue("AssetEmployeeid");
		 * 
		 * cell = row.createCell(1); cell.setCellValue("Employeeid");
		 * 
		 * cell = row.createCell(2); cell.setCellValue("Serialno");
		 * 
		 * cell = row.createCell(3); cell.setCellValue("AssetType");
		 * 
		 * cell = row.createCell(4); cell.setCellValue("Brand");
		 * 
		 * cell = row.createCell(5); cell.setCellValue("Model");
		 * 
		 * cell = row.createCell(6); cell.setCellValue("Vendor");
		 * 
		 * cell = row.createCell(7); cell.setCellValue("NoOfHardDisks");
		 * 
		 * cell = row.createCell(8); cell.setCellValue("HardDiskType");
		 * 
		 * cell = row.createCell(9); cell.setCellValue("HardDiskCapacity");
		 * 
		 * cell = row.createCell(10); cell.setCellValue("NoOfRams");
		 * 
		 * cell = row.createCell(11); cell.setCellValue("RamType");
		 * 
		 * cell = row.createCell(12); cell.setCellValue("RamCapacity");
		 * 
		 * cell = row.createCell(13); cell.setCellValue("WarrentyStartDate");
		 * 
		 * cell = row.createCell(14); cell.setCellValue("WarrentyEndDate");
		 * 
		 * cell = row.createCell(15); cell.setCellValue("PurchaseDate");
		 * 
		 * cell = row.createCell(16); cell.setCellValue("PurchaseDocNo");
		 * 
		 * cell = row.createCell(17); cell.setCellValue("PurchaseInvoice");
		 * 
		 * cell = row.createCell(18); cell.setCellValue("DateOfAssignment");
		 * 
		 * cell = row.createCell(19); cell.setCellValue("DeskNumber");
		 * 
		 * cell = row.createCell(20); cell.setCellValue("Floor");
		 * 
		 * cell = row.createCell(21); cell.setCellValue("Location");
		 * 
		 * cell = row.createCell(22); cell.setCellValue("Status");
		 * 
		 * 
		 * cell = row.createCell(23); cell.setCellValue("RamType");
		 * 
		 * cell = row.createCell(24); cell.setCellValue("VendorName");
		 * 
		 * cell = row.createCell(25); cell.setCellValue("Status");
		 */

		for (int i = 0; i < assectEmployeeModels.size(); i++) {

			Row datarow = sheet.createRow(i + 1);

			
//			 datarow.createCell(0).setCellValue(assectEmployeeModels.get(i).
//			 getAssectemployeeid().toString());
			 
			datarow.createCell(0).setCellValue(assectEmployeeModels.get(i).getEmployee().getEmployeeid().toString());
			datarow.createCell(1).setCellValue(assectEmployeeModels.get(i).getAssectModel().getSerialNumber());
			datarow.createCell(2)
					.setCellValue(assectEmployeeModels.get(i).getAssectModel().getAssectType().getAssetType());
			datarow.createCell(3).setCellValue(assectEmployeeModels.get(i).getAssectModel().getBrand().getBrandname());
			datarow.createCell(4).setCellValue(assectEmployeeModels.get(i).getAssectModel().getModels().getModelname());
			datarow.createCell(5)
					.setCellValue(assectEmployeeModels.get(i).getAssectModel().getVendor().getVendorName());
			datarow.createCell(6).setCellValue(assectEmployeeModels.get(i).getAssectModel().getNoOfHardDisks());
			datarow.createCell(7)
					.setCellValue(assectEmployeeModels.get(i).getAssectModel().getHardDiskType().getHardDiskType());
			datarow.createCell(8).setCellValue(
					assectEmployeeModels.get(i).getAssectModel().getHardDiskCapacity().getHarddiskCapacityType());
			datarow.createCell(9).setCellValue(assectEmployeeModels.get(i).getAssectModel().getNoOfRams());
			datarow.createCell(10)
					.setCellValue(assectEmployeeModels.get(i).getAssectModel().getTypeOfRam().getRamtypeName());
			datarow.createCell(11)
					.setCellValue(assectEmployeeModels.get(i).getAssectModel().getRamCapacity().getRamCapacity());
			datarow.createCell(12).setCellValue(assectEmployeeModels.get(i).getAssectModel().getWarrentStartDate());
			datarow.createCell(13).setCellValue(assectEmployeeModels.get(i).getAssectModel().getWarrentEndDate());
			datarow.createCell(14).setCellValue(assectEmployeeModels.get(i).getAssectModel().getPurchaseDate());
			datarow.createCell(15).setCellValue(assectEmployeeModels.get(i).getAssectModel().getPurchaseDCNo());
			datarow.createCell(16).setCellValue(assectEmployeeModels.get(i).getAssectModel().getPurchaseInvoiceNo());
			datarow.createCell(17).setCellValue(assectEmployeeModels.get(i).getDateOfAssignment());
			datarow.createCell(18).setCellValue(assectEmployeeModels.get(i).getDeskNumber());
			datarow.createCell(19).setCellValue(assectEmployeeModels.get(i).getFloor());
			datarow.createCell(20).setCellValue(assectEmployeeModels.get(i).getLocation());
			datarow.createCell(21).setCellValue(assectEmployeeModels.get(i).getStatus());
			/*
			 * datarow.createCell(23)
			 * .setCellValue(assectEmployeeModels.get(i).getAssectModel().getTypeOfRam().
			 * getRamtypeName()); datarow.createCell(24)
			 * .setCellValue(assectEmployeeModels.get(i).getAssectModel().getVendor().
			 * getVendorName());
			 * datarow.createCell(25).setCellValue(assectEmployeeModels.get(i).getStatus());
			 */
		}

		// Making sure that Size of Excel Auto Resize To Fit the Data

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
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
		sheet.autoSizeColumn(13);
		sheet.autoSizeColumn(14);
		sheet.autoSizeColumn(15);
		sheet.autoSizeColumn(16);
		sheet.autoSizeColumn(17);
		sheet.autoSizeColumn(18);
		sheet.autoSizeColumn(19);
		sheet.autoSizeColumn(20);
		sheet.autoSizeColumn(21);
		sheet.autoSizeColumn(22);
		/*
		 * sheet.autoSizeColumn(23); sheet.autoSizeColumn(24); sheet.autoSizeColumn(25);
		 */

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new ByteArrayInputStream(outputStream.toByteArray());

	}
}