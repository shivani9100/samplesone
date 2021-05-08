package com.Inventory.Project.AssectService.Assect;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.Inventory.Project.AssectService.AssectEmployee.AssectEmployeeModel;

@Component
public class CsvFileExporter {

	@Autowired
	Environment environment;

	@SuppressWarnings("resource")
	public void getCsvData(PrintWriter writer, List<AssectModel> assetModels) throws IOException {

		CSVPrinter csvPrinter = new CSVPrinter(writer,
				CSVFormat.DEFAULT.withHeader(environment.getProperty("asset.table.columns")));

		for (AssectModel assectModel : assetModels) {

			List<Object> data = Arrays.asList(assectModel.getSerialNumber() + ","
					+ assectModel.getAssectType().getAssetType() + "," + assectModel.getBrand().getBrandname() + ","
					+ assectModel.getFloor() + "," + assectModel.getLocation() + "," + assectModel.getNoOfHardDisks()
					+ assectModel.getModels().getModelname() + ",{" + assectModel.getVendor().getVendorName() + ","
					+ assectModel.getVendor().getEmail() + "," + assectModel.getVendor().getContactNumber() + ","
					+ assectModel.getVendor().getCityname() + ',' + assectModel.getVendor().getState() + ","
					+ assectModel.getVendor().getPincode() + "}," + assectModel.getNoOfHardDisks() + ","
					+ assectModel.getHardDiskType().getHardDiskType() + "," + assectModel.getNoOfRams() + ","
					+ assectModel.getTypeOfRam().getRamtypeName() + assectModel.getRamCapacity().getRamCapacity() + ","
					+ assectModel.getWarrentStartDate() + "," + assectModel.getWarrentEndDate() + ","
					+ assectModel.getPurchaseDate() + "," + assectModel.getPurchaseDCNo() + ","
					+ assectModel.getPurchaseInvoiceNo() + "," + assectModel.getLocation() + ","
					+ assectModel.getFloor());

			csvPrinter.printRecords(data);

		}
		csvPrinter.flush();

	}

}
