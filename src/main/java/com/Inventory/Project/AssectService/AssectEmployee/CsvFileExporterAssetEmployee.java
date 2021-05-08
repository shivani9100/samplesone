package com.Inventory.Project.AssectService.AssectEmployee;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CsvFileExporterAssetEmployee {

	@Autowired
	private Environment environment;

	@SuppressWarnings("resource")
	public void getCsvData(PrintWriter writer, List<AssectEmployeeModel> list) throws IOException {

		CSVPrinter csvPrinter = new CSVPrinter(writer,
				CSVFormat.DEFAULT.withHeader(environment.getProperty("assetEmployee.table.columns")));

		for (AssectEmployeeModel assectEmployeeModel : list) {

			List<String> data = Arrays.asList(assectEmployeeModel.getEmployee().getEmployeeid() + ","
					+ assectEmployeeModel.getAssectModel().getSerialNumber() + ","
					+ assectEmployeeModel.getAssectModel().getAssectType().getAssetType() + ","
					+ assectEmployeeModel.getAssectModel().getBrand().getBrandname() + ","
					+ assectEmployeeModel.getAssectModel().getModels().getModelname() + ","
					+ assectEmployeeModel.getAssectModel().getVendor().getVendorName() + ","
					+ assectEmployeeModel.getAssectModel().getNoOfHardDisks() + ","
					+ assectEmployeeModel.getAssectModel().getHardDiskType().getHardDiskType() + ","
					+ assectEmployeeModel.getAssectModel().getHardDiskCapacity().getHarddiskCapacityType() + ","
					+ assectEmployeeModel.getAssectModel().getNoOfRams() + ","
					+ assectEmployeeModel.getAssectModel().getTypeOfRam().getRamtypeName() + ","
					+ assectEmployeeModel.getAssectModel().getRamCapacity().getRamCapacity() + ","
					+ assectEmployeeModel.getAssectModel().getWarrentStartDate() + ","
					+ assectEmployeeModel.getAssectModel().getWarrentEndDate() + ","
					+ assectEmployeeModel.getAssectModel().getPurchaseDate() + ","
					+ assectEmployeeModel.getAssectModel().getPurchaseDCNo() + ","
					+ assectEmployeeModel.getAssectModel().getPurchaseInvoiceNo() + ","
					+ assectEmployeeModel.getDateOfAssignment() + "," + assectEmployeeModel.getDeskNumber() + ","
					+ assectEmployeeModel.getFloor() + "," + assectEmployeeModel.getLocation() + ","
					+ assectEmployeeModel.getStatus());
			/*
			 * + "," + assectEmployeeModel.getAssectModel().getVendor().getVendorName() +
			 * "," + assectEmployeeModel.getStatus());
			 */

			csvPrinter.printRecord(data);

		}
		csvPrinter.flush();
	}

	
}
