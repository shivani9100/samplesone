package com.Inventory.Project.AssectService.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;

@Repository
public class HardDiskCapacityDaoImpl {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * @param pageno
	 * @param pagesize
	 * @param search
	 * @return
	 * @throws RecordNotFoundException
	 */
	public PagenationResponse getAllHardDiskCapacities(Integer pageno, Integer pagesize, String search)
			throws RecordNotFoundException {

		long totalRecords = 0;

		if (search.equalsIgnoreCase("null") || search.equalsIgnoreCase("undefined")) {
			search = null;
		}

		StringBuilder query = new StringBuilder(
				"select harddisktype.hard_disk_type_id,harddisktype.harddisk_type,hcapacity.* from `hard_disk_type_capacities` hdc join `hard_disk_capacity` hcapacity on hcapacity.harddisk_capacity_id = hdc.harddisk_capacity_id join `hard_disk_type_master` harddisktype on harddisktype.hard_disk_type_id = hdc.hard_disk_type_id ");

		boolean flag = false;
		if (search != null) {

			query.append("WHERE harddisktype.harddisk_type LIKE '" + search
					+ "%' or hcapacity.harddisk_capacity_type LIKE '" + search + "%'");

		}
		query.append(" order by hcapacity.lastmodefied_date DESC");

		int startingRow = 0;
		if (pageno == 1) {
			startingRow = 0;
		} else {
			startingRow = ((pageno - 1) * pagesize);
		}

		List<Object[]> resultList = entityManager.createNativeQuery(query + " limit " + startingRow + "," + pagesize)
				.getResultList();

		List<HardDiskCapacityPagination> list = new ArrayList<>();

		List<Object[]> resultObj = entityManager.createNativeQuery(query.toString()).getResultList();
		if (!resultObj.isEmpty()) {
			totalRecords = Long.valueOf(resultObj.size());
			System.out.println(totalRecords);
		}

		for (Object[] obj : resultList) {
			HardDiskCapacityPagination hardDiskCapacityPagination = new HardDiskCapacityPagination();
			hardDiskCapacityPagination.setHarddiskTypeId(Integer.parseInt(String.valueOf(obj[0])));
			hardDiskCapacityPagination.setHarddiskType(String.valueOf(obj[1]));
			hardDiskCapacityPagination.setHarddiskCapacityId(Integer.parseInt(String.valueOf(obj[2])));
			hardDiskCapacityPagination.setCreatedBy(String.valueOf(obj[3]));
			hardDiskCapacityPagination.setCreatedOn(String.valueOf(obj[4]));
			hardDiskCapacityPagination.setHarddiskCapacityStatus(Boolean.parseBoolean(String.valueOf(obj[5])));
			hardDiskCapacityPagination.setHarddiskCapacityType(String.valueOf(obj[6]));
			hardDiskCapacityPagination.setLastmodefieddate(String.valueOf(obj[7]));
			hardDiskCapacityPagination.setUpdatedBy(String.valueOf(obj[8]));
			hardDiskCapacityPagination.setUpdatedOn(String.valueOf(obj[9]));

			list.add(hardDiskCapacityPagination);
		}
		if(!list.isEmpty()) {

		PagenationResponse pageNationResponse = new PagenationResponse();

		long totalPages = 1;

		if (totalRecords == 0) {

			pageNationResponse.setTotalNumberOfPages(Long.valueOf(0));
		} else {
			totalPages = totalRecords / pagesize;
			System.out.println("Hello " + totalPages);
			totalPages = ((totalRecords % pagesize > 0) ? (totalPages + 1) : totalPages);
			System.out.println("Hello1 " + totalPages);

		}
		pageNationResponse.setTotalNumberOfPages(totalPages);
		pageNationResponse.setTotalNumberOfRecords(totalRecords);
		pageNationResponse.setList(list);

		return pageNationResponse;
		}else {
			throw new RecordNotFoundException("No HardDisk Data Found");
		}

	}
}
