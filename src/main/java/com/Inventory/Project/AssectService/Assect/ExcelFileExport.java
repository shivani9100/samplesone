package com.Inventory.Project.AssectService.Assect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Dao.BrandRepo;
import com.Inventory.Project.AssectService.Dao.HardDiskCapacityDao;
import com.Inventory.Project.AssectService.Dao.HardDiskTypeMasterDao;
import com.Inventory.Project.AssectService.Dao.ModelRepo;
import com.Inventory.Project.AssectService.Dao.RamCapacityDAO;
import com.Inventory.Project.AssectService.Dao.RamTypeDao;
import com.Inventory.Project.AssectService.Dao.VendorRepository;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.BrandTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.HardDiskCapacityNotFoundException;
import com.Inventory.Project.AssectService.Exception.HardDiskNotFoundException;
import com.Inventory.Project.AssectService.Exception.ModelNotFoundException;
import com.Inventory.Project.AssectService.Exception.RamCapacityNotFoundException;
import com.Inventory.Project.AssectService.Exception.RamTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Model.Vendor;

@Component
public class ExcelFileExport {

	@Autowired
	Environment environment;

	@Autowired
	AssectDao assetDao;

	@Autowired
	AssetTypeRepositry assetRepository;

	@Autowired
	BrandRepo brandrepo;

	@Autowired
	VendorRepository vendorRepo;

	@Autowired
	ModelRepo modelRepo;

	@Autowired
	HardDiskTypeMasterDao hardDiskTypeDao;

	@Autowired
	HardDiskCapacityDao hardDiskCapacityDao;

	@Autowired
	RamTypeDao ramTypeDao;

	@Autowired
	RamCapacityDAO ramCapacityDao;

	@SuppressWarnings("resource")

	public static String Type = "application/octet-stream";

	public ByteArrayInputStream exportAssectListToExcelFile(List<AssectModel> assectModels) throws IOException {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("AssetData");

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		headerCellStyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);

		String string = environment.getProperty("asset.table.columns");
		Row row = sheet.createRow(0);
		System.out.println(string);
		String[] split = string.split(",");
		Cell cell = null;
		for (int i = 0; i < split.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue(split[i]);
		}

		for (int i = 0; i < assectModels.size(); i++) {

			Row dataRow = sheet.createRow(i + 1); // plus on to Exclude the Header Row

			dataRow.createCell(0).setCellValue(assectModels.get(i).getSerialNumber());
			dataRow.createCell(1).setCellValue(assectModels.get(i).getAssectType().getAssetType());
			dataRow.createCell(2).setCellValue(assectModels.get(i).getBrand().getBrandname());
			dataRow.createCell(3).setCellValue(assectModels.get(i).getModels().getModelname());

			dataRow.createCell(4)
					.setCellValue("Name : " + assectModels.get(i).getVendor().getVendorName() + ",E-Mail : "
							+ assectModels.get(i).getVendor().getEmail() + ",ContactNumber : "
							+ assectModels.get(i).getVendor().getContactNumber() + ",City : "
							+ assectModels.get(i).getVendor().getCityname() + ",State : "
							+ assectModels.get(i).getVendor().getState() + ",PinCode : "
							+ assectModels.get(i).getVendor().getPincode());
			dataRow.createCell(5).setCellValue(assectModels.get(i).getNoOfHardDisks());
			dataRow.createCell(6).setCellValue(assectModels.get(i).getHardDiskType().getHardDiskType());
			dataRow.createCell(7).setCellValue(assectModels.get(i).getHardDiskCapacity().getHarddiskCapacityType());
			dataRow.createCell(8).setCellValue(assectModels.get(i).getNoOfRams());
			dataRow.createCell(9).setCellValue(assectModels.get(i).getTypeOfRam().getRamtypeName());
			dataRow.createCell(10).setCellValue(assectModels.get(i).getRamCapacity().getRamCapacity());
			dataRow.createCell(11).setCellValue(assectModels.get(i).getWarrentStartDate());
			dataRow.createCell(12).setCellValue(assectModels.get(i).getWarrentEndDate());

			dataRow.createCell(13).setCellValue(assectModels.get(i).getPurchaseDate());
			dataRow.createCell(14).setCellValue(assectModels.get(i).getPurchaseDCNo());
			dataRow.createCell(15).setCellValue(assectModels.get(i).getPurchaseInvoiceNo());
			dataRow.createCell(16).setCellValue(assectModels.get(i).getFloor());
			dataRow.createCell(17).setCellValue(assectModels.get(i).getLocation());

		}
		// Making sure the size of Excel auto resize to fit the data
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

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	String SHEET = "Asset";

	public boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public ByteArrayInputStream exportDummyAssectListToExcelFile() throws IOException {

		Map<String, String[]> map = new HashMap<String, String[]>();

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("ListSheet");

		Row row;
		Name namedRange;
		String colLetter;
		String reference;

		List<AssetTypeMaster> assetTypelist = assetRepository.findAll();

		Integer[] arrayids = assetTypelist.stream().map(assetType -> assetType.getAssetId()).toArray(Integer[]::new);

		for (int i = 0; i < assetTypelist.size(); i++) {

			AssetTypeMaster assetType = assetRepository.findById(arrayids[i]).get();

			List<Brand> brandlist1 = assetType.getBrandList();

			String[] brands = brandlist1.stream().map(brand -> brand.getBrandname()).toArray(String[]::new);

			map.put(assetType.getAssetType(), brands);
		}

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

			namedRange.setRefersToFormula(reference);
			c++;
		}

		colLetter = CellReference.convertNumToColString((c - 1));

		namedRange = workbook.createName();
		namedRange.setNameName("assets");
		reference = "ListSheet!$A$1:$" + colLetter + "$1";
		namedRange.setRefersToFormula(reference);

		sheet.setSelected(false);

		sheet = workbook.createSheet("Assets");

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		headerCellStyle.setAlignment(HorizontalAlignment.GENERAL.CENTER);

		String string = environment.getProperty("asset.table.columns");
		row = sheet.createRow(0);

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

		DataValidationHelper dvHelper = sheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("assets");
		CellRangeAddressList addressList = new CellRangeAddressList(1, 1, 1, 1);
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");
		sheet.addValidationData(validation);
		dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(Assets!$B$2)");

		// dvConstraint =
		// dvHelper.createFormulaListConstraint("INDIRECT(Assets!('$B$2'):B7)");

		addressList = new CellRangeAddressList(1, 1, 2, 2);

		validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");
		sheet.addValidationData(validation);
		workbook.setSheetHidden(0, true);

		workbook.setActiveSheet(1);

		// MODEL

		Map<String, String[]> modelbrandmap = new HashMap<String, String[]>();

		List<AssetTypeMaster> assetTypelist1 = assetRepository.findAll();

		Integer[] arrayids1 = assetTypelist.stream().map(assetType -> assetType.getAssetId()).toArray(Integer[]::new);

		String[] array1 = assetTypelist.stream().map(assetType -> assetType.getAssetType()).toArray(String[]::new);

		for (int i = 0; i < assetTypelist1.size(); i++) {

			AssetTypeMaster asset = assetRepository.findById(arrayids1[i]).get();

			List<Brand> brandList2 = asset.getBrandList();
			for (int j = 0; j < brandList2.size(); j++) {

				List<Model> modelLists = brandList2.get(j).getModelList();
				String[] arrayofmodels = modelLists.stream().map(model -> model.getModelname()).toArray(String[]::new);

				modelbrandmap.put(asset.getAssetType() + "" + brandList2.get(j).getBrandname(), arrayofmodels);
			}

		}
		Sheet sheet2 = workbook.createSheet("modelsheet");

		Row rowmodel;
		Name namedRangemodel;
		String colLettermodel;
		String referencemodel;
		int c2 = 0;

		for (String key : modelbrandmap.keySet()) {
			int r1 = 0;
			rowmodel = sheet2.getRow(r1);
			if (rowmodel == null)
				rowmodel = sheet2.createRow(r1);
			r1++;
			rowmodel.createCell(c2).setCellValue(key);
			String[] items = modelbrandmap.get(key);
			if (items.length == 0) {
				row = sheet2.getRow(r1);
				if (row == null)
					row = sheet2.createRow(r1);
				r1++;
				row.createCell(c2).setCellValue("");
			}

			for (String item : items) {
				rowmodel = sheet2.getRow(r1);
				if (rowmodel == null)
					rowmodel = sheet2.createRow(r1);
				r1++;
				rowmodel.createCell(c2).setCellValue(item);
			}

			colLettermodel = CellReference.convertNumToColString(c2);
			namedRangemodel = workbook.createName();

			namedRangemodel.setNameName(key);
			referencemodel = "modelsheet!$" + colLettermodel + "$2:$" + colLettermodel + "$" + r1;

			namedRangemodel.setRefersToFormula(referencemodel);
			c2++;
		}

		colLettermodel = CellReference.convertNumToColString((c2 - 1));

		namedRangemodel = workbook.createName();
		namedRangemodel.setNameName("brandmodel");
		referencemodel = "modelsheet!$A$1:$" + colLettermodel + "$1";
		namedRangemodel.setRefersToFormula(referencemodel);

		// sheet2.setSelected(false);

		dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(CONCATENATE(Assets!$B$2&Assets!$C$2))");

		addressList = new CellRangeAddressList(1, 1, 3, 3);
		validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setSuppressDropDownArrow(true);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");
		sheet.addValidationData(validation);
		workbook.setSheetHidden(2, true);

		List<Vendor> vendorlist = vendorRepo.findAll();
		// vendor
		if (vendorlist.size() != 0) {

			XSSFDataValidationHelper validationHelper2 = new XSSFDataValidationHelper((XSSFSheet) sheet);

			String[] arrayvendor = vendorlist.stream().map(vendor -> vendor.getVendorName()).toArray(String[]::new);

			DataValidationConstraint constraint2 = validationHelper2.createExplicitListConstraint(arrayvendor);

			CellRangeAddressList addressList2 = new CellRangeAddressList(1, 100, 4, 4);

			DataValidation dataValidation2 = validationHelper2.createValidation(constraint2, addressList2);

			dataValidation2.setErrorStyle(DataValidation.ErrorStyle.STOP);

			dataValidation2.setSuppressDropDownArrow(true);

			validation.setShowErrorBox(true);
			validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");

			sheet.addValidationData(dataValidation2);
		}

		// harddisk
		Map<String, String[]> harddisk = new HashMap<String, String[]>();

		List<HardDiskTypeMaster> hardDiskType = hardDiskTypeDao.findAll();

		Integer[] arrayharddiskids = hardDiskType.stream().map(hardDisk -> hardDisk.getHardDiskTypeId())
				.toArray(Integer[]::new);

		for (int i = 0; i < hardDiskType.size(); i++) {

			HardDiskTypeMaster hardDiskType1 = hardDiskTypeDao.findById(arrayharddiskids[i]).get();

			List<HardDiskCapacity> capacList = hardDiskType1.getCapac();

			String[] capacitys = capacList.stream().map(harddiskcapacity -> harddiskcapacity.getHarddiskCapacityType())
					.toArray(String[]::new);

			harddisk.put(hardDiskType1.getHardDiskType(), capacitys);
		}
		Sheet sheet3 = workbook.createSheet("harddiskdata");

		Row rowHarddisk;
		Name namedRangeHarddisk;
		String colLetterHarddisk;
		String referenceHarddisk;
		int c3 = 0;

		for (String key : harddisk.keySet()) {
			int r1 = 0;
			rowHarddisk = sheet3.getRow(r1);
			if (rowHarddisk == null)
				rowHarddisk = sheet3.createRow(r1);
			r1++;
			rowHarddisk.createCell(c3).setCellValue(key);
			String[] items = harddisk.get(key);

			if (items.length == 0) {
				row = sheet3.getRow(r1);
				if (row == null)
					row = sheet3.createRow(r1);
				r1++;
				row.createCell(c3).setCellValue("");
			}

			for (String item : items) {
				rowHarddisk = sheet3.getRow(r1);
				if (rowHarddisk == null)
					rowHarddisk = sheet3.createRow(r1);
				r1++;
				rowHarddisk.createCell(c3).setCellValue(item);
			}

			colLetterHarddisk = CellReference.convertNumToColString(c3);
			namedRangeHarddisk = workbook.createName();
			namedRangeHarddisk.setNameName(key);
			referenceHarddisk = "harddiskdata!$" + colLetterHarddisk + "$2:$" + colLetterHarddisk + "$" + r1;

			namedRangeHarddisk.setRefersToFormula(referenceHarddisk);
			c3++;
		}

		colLetterHarddisk = CellReference.convertNumToColString((c3 - 1));

		namedRangeHarddisk = workbook.createName();

		namedRangeHarddisk.setNameName("hd");

		referenceHarddisk = "harddiskdata!$A$1:$" + colLetterHarddisk + "$1";
		namedRangeHarddisk.setRefersToFormula(referenceHarddisk);

		sheet3.setSelected(false);

		DataValidationConstraint dvConstraintHarddisk = dvHelper.createFormulaListConstraint("hd");
		CellRangeAddressList addressListHarddisk = new CellRangeAddressList(1, 1, 6, 6);
		DataValidation validationHarddisk = dvHelper.createValidation(dvConstraintHarddisk, addressListHarddisk);
		
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data","Please Select Data from the Drop Down");
		sheet.addValidationData(validationHarddisk);
		DataValidationHelper dvHelperHarddisk = sheet3.getDataValidationHelper();

		dvConstraintHarddisk = dvHelperHarddisk.createFormulaListConstraint("INDIRECT(Assets!$G$2)");

		addressListHarddisk = new CellRangeAddressList(1, 1, 7, 7);
		validationHarddisk = dvHelperHarddisk.createValidation(dvConstraintHarddisk, addressListHarddisk);
		validationHarddisk.setSuppressDropDownArrow(true);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");
		sheet.addValidationData(validationHarddisk);
		workbook.setSheetHidden(3, true);

		// Ram

		Map<String, String[]> ramMap = new HashMap<String, String[]>();

		List<RamTypeMaster> ramList = ramTypeDao.findAll();

		Integer[] arrayramids = ramList.stream().map(ram -> ram.getRamtypeId()).toArray(Integer[]::new);

		for (int i = 0; i < ramList.size(); i++) {

			RamTypeMaster ramTypeMaster = ramTypeDao.findById(arrayramids[i]).get();

			List<RamCapacityMaster> ramCapacityMaster = ramTypeMaster.getRamMaster();

			String[] capacitys = ramCapacityMaster.stream().map(ramCapacity -> ramCapacity.getRamCapacity())
					.toArray(String[]::new);

			ramMap.put(ramTypeMaster.getRamtypeName(), capacitys);
		}

		Sheet sheet10 = workbook.createSheet("Rams");

		Row rowRam;
		Name namedRangeRam;
		String colLetterRam;
		String referenceRam;
		int c10 = 0;

		for (String key : ramMap.keySet()) {
			int r1 = 0;
			rowRam = sheet10.getRow(r1);
			if (rowRam == null)
				rowRam = sheet10.createRow(r1);
			r1++;
			rowRam.createCell(c10).setCellValue(key);
			String[] items = ramMap.get(key);

			if (items.length == 0) {
				
				rowRam = sheet10.getRow(r1);
				if (rowRam == null)
					rowRam = sheet10.createRow(r1);
				r1++;
				rowRam.createCell(c10).setCellValue("");
			}

			for (String item : items) {
				rowRam = sheet10.getRow(r1);
				if (rowRam == null)
					rowRam = sheet10.createRow(r1);
				r1++;
				rowRam.createCell(c10).setCellValue(item);
			}

			colLetterRam = CellReference.convertNumToColString(c10);
			namedRangeRam = workbook.createName();
			namedRangeRam.setNameName(key);
			referenceRam = "Rams!$" + colLetterRam + "$2:$" + colLetterRam + "$" + r1;

			namedRangeRam.setRefersToFormula(referenceRam);
			c10++;
		}

		colLetterRam = CellReference.convertNumToColString((c10 - 1));

		namedRangeRam = workbook.createName();

		namedRangeRam.setNameName("ramcapacity");

		referenceRam = "Rams!$A$1:$" + colLetterRam + "$1";
		namedRangeRam.setRefersToFormula(referenceRam);

		sheet10.setSelected(false);

		DataValidationConstraint dvConstraintRam = dvHelper.createFormulaListConstraint("ramcapacity");
		CellRangeAddressList addressListRam = new CellRangeAddressList(1, 1, 9, 9);
		DataValidation validationRam = dvHelper.createValidation(dvConstraintRam, addressListRam);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");
		sheet.addValidationData(validationRam);
		DataValidationHelper dvHelperRam = sheet10.getDataValidationHelper();

		dvConstraintRam = dvHelperRam.createFormulaListConstraint("INDIRECT(Assets!$J$2)");

		addressListRam = new CellRangeAddressList(1, 1, 10, 10);
		validationRam = dvHelperRam.createValidation(dvConstraintRam, addressListRam);
		validationRam.setSuppressDropDownArrow(true);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Invalid Data", "Please Select Data from the Drop Down");
		sheet.addValidationData(validationRam);

		workbook.setSheetHidden(4, true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		LocalDate now = LocalDate.now();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		java.util.Date d = java.util.Date.from(now.atStartOfDay(defaultZoneId).toInstant());
		Row row2 = sheet.createRow(1);

		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setDataFormat(creationHelper.createDataFormat().getFormat(new String()));
		Cell cell5 = row2.createCell(14);
		cell5.setCellValue("Ad45628");

		cell5.setCellStyle(cellStyle1);

		Cell cell6 = row2.createCell(15);
		cell6.setCellValue("2nd");

		cell6.setCellStyle(cellStyle1);

		Cell cell7 = row2.createCell(16);
		cell7.setCellValue("Hyderabad");

		cell7.setCellStyle(cellStyle1);

		Cell cell2 = row2.createCell(11);
		cell2.setCellValue(d);
		cell2.setCellStyle(cellStyle);

		Cell cell3 = row2.createCell(12);
		cell3.setCellValue(d);
		cell3.setCellStyle(cellStyle);

		Cell cell4 = row2.createCell(13);
		cell4.setCellValue(d);
		cell4.setCellStyle(cellStyle);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		workbook.write(byteArrayOutputStream);

		workbook.close();

		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	public List<AssectModel> uploadingExcel(java.io.InputStream inputStream) throws FeildsShouldNotBeEmptyException,
			RamCapacityNotFoundException, HardDiskNotFoundException, HardDiskCapacityNotFoundException,
			RamTypeNotFoundException, RecordNotFoundException, AssetTypeNotFoundException, BrandTypeNotFoundException, ModelNotFoundException {
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

			Sheet sheet = workbook.getSheet("Assets");

			Iterator<Row> rows = sheet.iterator();

			ArrayList<AssectModel> assectmodelList = new ArrayList<AssectModel>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row next = rows.next();
				AssectModel assectModel = new AssectModel();
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Cell cell0 = sheet.getRow(rowNumber).getCell(0);
				DataFormatter formatter0 = new DataFormatter();
				String serial = formatter0.formatCellValue(cell0);
				if (cell0 == null) {
					throw new FeildsShouldNotBeEmptyException("Serial Number  Should Not be Empty");
				}
				// String serialNumber = cell0.getStringCellValue();
				assectModel.setSerialNumber(serial);

				Cell cell1 = sheet.getRow(rowNumber).getCell(1);
				if (cell1 == null) {
					throw new FeildsShouldNotBeEmptyException("Asset Type Should Not be Empty");
				}

				DataFormatter formatter1 = new DataFormatter();
				String assetType = formatter1.formatCellValue(cell1);

				AssetTypeMaster assetTypeObj = assetRepository.findByAssetType(assetType);
//				AssetTypeMasterEx assetTypeObj = assetRepository.findByAssetType(assetType);

				if (assetTypeObj == null) {
					throw new AssetTypeNotFoundException("AssetType Not found");

				}
				assectModel.setAssectType(assetTypeObj);

				Cell cell2 = sheet.getRow(rowNumber).getCell(2);
				if (cell2 == null) {
					throw new FeildsShouldNotBeEmptyException("Brand Feild Should Not be Empty");
				}

				DataFormatter formatter2 = new DataFormatter();
				String BrandName = formatter2.formatCellValue(cell2);
				Brand brand = brandrepo.findByBrandnameAndAssetTypeMasterExAssetType(BrandName,
						assetTypeObj.getAssetType());
				if (brand == null) {
					throw new BrandTypeNotFoundException("brand type not found");
				}
				assectModel.setBrand(brand);

				Cell cell3 = sheet.getRow(rowNumber).getCell(3);
				if (cell3 == null) {
					throw new FeildsShouldNotBeEmptyException("Model Feild Should Not be Empty");
				}
				DataFormatter formatter3 = new DataFormatter();
				String ModelName = formatter3.formatCellValue(cell3);
				Model model1 = modelRepo.findByModelnameAndBrandBrandid(ModelName, brand.getBrandid());
				if (model1 == null) {
					throw new ModelNotFoundException("model type not found");
				}
				assectModel.setModels(model1);

				Cell cell4 = sheet.getRow(rowNumber).getCell(4);
				if (cell4 == null) {
					throw new FeildsShouldNotBeEmptyException("Vendor Should Not be Empty");
				}
				DataFormatter formatter4 = new DataFormatter();
				String str4 = formatter4.formatCellValue(cell4);
				Vendor vendorName = vendorRepo.findByVendorName(str4);
				assectModel.setVendor(vendorName);

				Cell cell5 = sheet.getRow(rowNumber).getCell(5);
				if (cell5 == null) {
					throw new FeildsShouldNotBeEmptyException("Number of hard disks Should Not be Empty");
				}
				DataFormatter formatter5 = new DataFormatter();

				String str5 = formatter5.formatCellValue(cell5);
				int parseInt = Integer.parseInt(str5);
				assectModel.setNoOfHardDisks(parseInt);

				Cell cell6 = sheet.getRow(rowNumber).getCell(6);

				if (cell6 == null) {
					throw new FeildsShouldNotBeEmptyException("HardDisk Type Should Not be Empty");
				}
				

				DataFormatter formatter6 = new DataFormatter();
				String str6 = formatter6.formatCellValue(cell6);

				HardDiskTypeMaster hardDiskType = null;
				try {
					hardDiskType = hardDiskTypeDao.findByHardDiskType(str6);
					assectModel.setHardDiskType(hardDiskType);

				} catch (Exception e) {
					throw new HardDiskNotFoundException("HardDisk type Not found");
				}

				assectModel.setHardDiskType(hardDiskType);

				Cell cell7 = sheet.getRow(rowNumber).getCell(7);
				if (cell7 == null) {
					throw new FeildsShouldNotBeEmptyException("HardDisk Capacity Should Not be Empty");
				}

				DataFormatter formatter7 = new DataFormatter();
				String str7 = formatter7.formatCellValue(cell7);
				try {
					HardDiskCapacity harddiskCapacityType = hardDiskCapacityDao
							.findByHarddiskCapacityTypeAndCapacitiesHardDiskTypeId(str7,
									hardDiskType.getHardDiskTypeId());

					Optional<HardDiskCapacity> findByHarddiskCapacityIdAndCapacitiesHardDiskTypeId = hardDiskCapacityDao
							.findByHarddiskCapacityIdAndCapacitiesHardDiskTypeId(
									harddiskCapacityType.getHarddiskCapacityId(), hardDiskType.getHardDiskTypeId());
					assectModel.setHardDiskCapacity(findByHarddiskCapacityIdAndCapacitiesHardDiskTypeId.get());
				} catch (Exception e) {
					throw new HardDiskCapacityNotFoundException("HardDisk Capacity Not found");
				}

				Cell cell8 = sheet.getRow(rowNumber).getCell(8);
				if (cell8 == null) {
					throw new FeildsShouldNotBeEmptyException("Number of Rams Should Not be Empty");
				}

				DataFormatter formatter8 = new DataFormatter();
				String str8 = formatter8.formatCellValue(cell8);

				int parseInt1 = Integer.parseInt(str8);
				assectModel.setNoOfRams(parseInt1);

				Cell cell9 = sheet.getRow(rowNumber).getCell(9);
				if (cell9 == null) {
					throw new FeildsShouldNotBeEmptyException("Ram Type Should Not be Empty");
				}
				DataFormatter formatter9 = new DataFormatter();
				String str9 = formatter9.formatCellValue(cell9);
				RamTypeMaster ramtypeName = null;
				try {
					ramtypeName = ramTypeDao.findByRamtypeName(str9);
					assectModel.setTypeOfRam(ramtypeName);
				} catch (Exception e) {
					throw new RamTypeNotFoundException("Ram Type Not found");
				}

				Cell cell10 = sheet.getRow(rowNumber).getCell(10);
				if (cell10 == null) {
					throw new FeildsShouldNotBeEmptyException("RamCapacity Should Not be Empty");
				}
				DataFormatter formatter10 = new DataFormatter();
				String str10 = formatter10.formatCellValue(cell10);
				try {

					RamCapacityMaster ramCapacity = ramCapacityDao.findByRamCapacityAndRamTypeMastersRamtypeId(str10,
							ramtypeName.getRamtypeId());

					Optional<RamCapacityMaster> byRamIdAndRamTypeMastersRamtypeId = ramCapacityDao
							.findByRamIdAndRamTypeMastersRamtypeId(ramCapacity.getRamId(), ramtypeName.getRamtypeId());
					assectModel.setRamCapacity(byRamIdAndRamTypeMastersRamtypeId.get());
				} catch (Exception e) {

					throw new RamCapacityNotFoundException("RamCApacity not Found");
				}

				Cell cell11 = sheet.getRow(rowNumber).getCell(11);
				if (cell11 == null) {
					throw new FeildsShouldNotBeEmptyException("Warrenty Start Date Should Not be Empty");
				}

				assectModel.setWarrentStartDate(cell11.getDateCellValue());

				Cell cell12 = sheet.getRow(rowNumber).getCell(12);
				if (cell12 == null) {
					throw new FeildsShouldNotBeEmptyException("Warrenty End Date Should Not be Empty");
				}
				assectModel.setWarrentEndDate(cell12.getDateCellValue());

				Cell cell13 = sheet.getRow(rowNumber).getCell(13);
				if (cell13 == null) {
					throw new FeildsShouldNotBeEmptyException("Purchase Date Should Not be Empty");
				}

				assectModel.setPurchaseDate(cell13.getDateCellValue());

				Cell cell14 = sheet.getRow(rowNumber).getCell(14);
				DataFormatter formatter14 = new DataFormatter();
				String purchaseDc = formatter14.formatCellValue(cell14);
				if (cell14 == null) {
					throw new FeildsShouldNotBeEmptyException("Purchase DC Number Should Not be Empty");
				}
				assectModel.setPurchaseDCNo(purchaseDc);

				Cell cell15 = sheet.getRow(rowNumber).getCell(15);
				if (cell15 == null) {
					throw new FeildsShouldNotBeEmptyException("Floor Details Should Not be Empty");
				}
				assectModel.setFloor(cell15.getStringCellValue());

				Cell cell16 = sheet.getRow(rowNumber).getCell(16);
				if (cell16 == null) {
					throw new FeildsShouldNotBeEmptyException("location Details Should Not be Empty");
				}

				assectModel.setLocation(cell16.getStringCellValue());

				Cell cell17 = sheet.getRow(rowNumber).getCell(17);
				if (cell17 == null) {
					throw new FeildsShouldNotBeEmptyException("cost Details Should Not be Empty");
				}

				double numericCellValue = cell17.getNumericCellValue();

				assectModel.setCost(numericCellValue);

				Cell cell18 = sheet.getRow(rowNumber).getCell(18);
				if (cell18 == null) {
					throw new FeildsShouldNotBeEmptyException("purchase invoicenumber Details Should Not be Empty");
				}

				DataFormatter formatter18 = new DataFormatter();

				String purchaseInvoice = formatter18.formatCellValue(cell18);

				assectModel.setPurchaseInvoiceNo(purchaseInvoice);

				assectmodelList.add(assectModel);

				rowNumber++;
			}
			if (!assectmodelList.isEmpty()) {
				workbook.close();

			workbook.close();

			return assectmodelList;
				
			} else {
				throw new RecordNotFoundException("File shouldn't be empty");
			}


		} catch (IOException e) {

			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());

		}
	}
}
