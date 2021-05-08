package com.Inventory.Project.AssectService.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class AssetDaoImpl {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * @param pageno
	 * @param pagesize
	 * @param search
	 * @return
	 */
	public PagenationResponse getAllAssetData(Integer pageno, Integer pagesize, String search) {

		long totalRecords = 0;

		if (search.equalsIgnoreCase("null") || search.equalsIgnoreCase("undefined")) {
			search = null;
		}

		StringBuilder query = new StringBuilder(
				"select assetType.assettype,brand.brandname,model.modelname,vendor.vendor_name,hardDiskType.harddisk_type,hardDiskCapacity.harddisk_capacity_type,ramType.ramtype_name,ramCapacity.ram_capacity,assect.* from `assect_10` assect join `asset_type1` assetType on assetType.assetid =  assect.asset_id join `brand` brand on brand.brandid = assect.brandid join  `model` model on model.modelid = assect.modelid join `vendor` vendor on vendor.vendor_id = assect.vendor_id join  `hard_disk_type_master` hardDiskType on assect.hard_disk_type_id = hardDiskType.hard_disk_type_id join `hard_disk_capacity` hardDiskCapacity on assect.harddisk_capacity_id = hardDiskCapacity.harddisk_capacity_id join `ram_type_master` ramType on ramType.ramtype_id =  assect.ramtype_id join `ram_master` ramCapacity on ramCapacity.ram_id =  assect.ramcapacity_id ");

		boolean flag = false;
		if (search != null) {

			query.append("where assect.serial_no Like '" + search + "%' or assetType.assettype Like '" + search
					+ "%' or brand.brandname Like '" + search + "%' or model.modelname Like '" + search
					+ "%' or hardDiskType.harddisk_type Like '" + search
					+ "%' or hardDiskCapacity.harddisk_capacity_type Like '" + search
					+ "%' or vendor.vendor_name Like '" + search + "%' or ramType.ramtype_name Like '" + search
					+ "%' or ramCapacity.ram_capacity Like '" + search + "%'");

			// query.append("WHERE harddisktype.harddisk_type LIKE '" + search + "%' or
			// hcapacity.harddisk_capacity_type Like '"+search+"%'");

		}
		query.append(" order by assect.lastmodified_date desc");

		int startingRow = 0;
		if (pageno == 1) {
			startingRow = 0;
		} else {
			startingRow = ((pageno - 1) * pagesize);
		}

		List<Object[]> resultList = entityManager.createNativeQuery(query + " limit " + startingRow + "," + pagesize)
				.getResultList();

		List<AssetPageNation> list = new ArrayList<>();

		List<Object[]> resultObj = entityManager.createNativeQuery(query.toString()).getResultList();
		if (!resultObj.isEmpty()) {
			totalRecords = Long.valueOf(resultObj.size());
//	            System.out.println(totalRecords);
		}

		for (Object[] obj : resultList) {
			AssetPageNation assetPageNation = new AssetPageNation();

			assetPageNation.setAssetType(String.valueOf(obj[0]));
			assetPageNation.setBrandName(String.valueOf(obj[1]));
			assetPageNation.setModelName(String.valueOf(obj[2]));
			assetPageNation.setVendorName(String.valueOf(obj[3]));
			assetPageNation.setHardDiskType(String.valueOf(obj[4]));
			assetPageNation.setHardDiskCapacity(String.valueOf(obj[5]));
			assetPageNation.setRamType(String.valueOf(obj[6]));
			assetPageNation.setRamCapacity(String.valueOf(obj[7]));
			assetPageNation.setAssectid(Long.parseLong(String.valueOf(obj[8])));
			assetPageNation.setCost(Double.parseDouble(String.valueOf(obj[9])));
			assetPageNation.setCreatedBy(String.valueOf(obj[10]));
			assetPageNation.setCreatedon(String.valueOf(obj[11]));
			assetPageNation.setFloor(String.valueOf(obj[12]));
			assetPageNation.setInvoiceDoc(String.valueOf(obj[13]));
			assetPageNation.setLastModefiedDate(String.valueOf(obj[14]));
			assetPageNation.setLocation(String.valueOf(obj[15]));
			assetPageNation.setNoOfHardDisks(Integer.parseInt(String.valueOf(obj[16])));
			assetPageNation.setNoOfRams(Integer.parseInt(String.valueOf(obj[17])));
			assetPageNation.setPurchaseDocNo(String.valueOf(obj[18]));
			assetPageNation.setPurchaseDate(String.valueOf(obj[19]));
			assetPageNation.setPurchaseInvoiceNo(String.valueOf(obj[20]));
			assetPageNation.setSerialNo(String.valueOf(obj[21]));
			assetPageNation.setUpdatedBy(String.valueOf(obj[22]));
			assetPageNation.setUpdatedOn(String.valueOf(obj[23]));
			assetPageNation.setWarrentDoc(String.valueOf(obj[24]));
			assetPageNation.setWarrentEndDate(String.valueOf(obj[25]));
			assetPageNation.setWarrentStartDate(String.valueOf(obj[26]));
			assetPageNation.setAssetId(Integer.parseInt(String.valueOf(obj[27])));
			assetPageNation.setBrandId(Integer.parseInt(String.valueOf(obj[28])));
			assetPageNation.setHardDiskCapacityId(Integer.parseInt(String.valueOf(obj[29])));
			assetPageNation.setHardDiskTypeID(Integer.parseInt(String.valueOf(obj[30])));
			assetPageNation.setModelId(Integer.parseInt(String.valueOf(obj[31])));
			assetPageNation.setRamCapacityId(Integer.parseInt(String.valueOf(obj[32])));
			assetPageNation.setRamTypeId(Integer.parseInt(String.valueOf(obj[33])));
			assetPageNation.setVendorId(Integer.parseInt(String.valueOf(obj[34])));

			list.add(assetPageNation);
		}

		PagenationResponse pageNationResponse = new PagenationResponse();

		long totalPages = 1;

		if (totalRecords == 0) {

			pageNationResponse.setTotalNumberOfPages(Long.valueOf(0));
		} else {
			totalPages = totalRecords / pagesize;
//	            System.out.println("Hello " + totalPages);
			totalPages = ((totalRecords % pagesize > 0) ? (totalPages + 1) : totalPages);
//	            System.out.println("Hello1 " + totalPages);

		}
		pageNationResponse.setTotalNumberOfPages(totalPages);
		pageNationResponse.setTotalNumberOfRecords(totalRecords);
		pageNationResponse.setList(list);

		return pageNationResponse;

	}
}
