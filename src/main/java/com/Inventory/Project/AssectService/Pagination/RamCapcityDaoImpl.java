package com.Inventory.Project.AssectService.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;

@Repository
public class RamCapcityDaoImpl {
	@PersistenceContext
	EntityManager entityManager;

	public PagenationResponse getAllRamCapacitys(Integer pagenumber, Integer pageSize, String search) throws RecordNotFoundException {

		long totalRecords = 0;
		
		StringBuilder queryString = new StringBuilder(
				"select rt.ramtype_id,rt.ramtype_name,rc.* from `ram_type_capacities` rtc join `ram_master` rc on rc.ram_id = rtc.ram_capacity_id join `ram_type_master` rt on rt.ramtype_id = rtc.ram_type_id");

		if (search != null) {
			queryString.append(" WHERE rt.ramtype_name LIKE '%" + search + "%' or rc.ram_capacity Like '%"+search+"%'");
		}
		queryString.append(" order by rc.lastmodefied_date DESC ");

		int startingRow = 0;
		if (pagenumber == 1) {
			startingRow = 0;
		} else {
			startingRow = ((pagenumber - 1) * pageSize);
		}

		List<Object[]> resultList = entityManager
				.createNativeQuery(queryString + " limit " + startingRow + "," + pageSize).getResultList();
		List<RamCapacityPagination> list = new ArrayList<>();

		List<Object[]> resultObj = entityManager.createNativeQuery(queryString.toString()).getResultList();

		if (!resultObj.isEmpty()) {
			totalRecords = Long.valueOf(resultObj.size());
		}
		for (Object[] obj : resultList) {
			RamCapacityPagination ramcapacitypagination = new RamCapacityPagination();
			ramcapacitypagination.setRamtypeId(Integer.parseInt(String.valueOf(obj[0])));
			ramcapacitypagination.setRamtypeName(String.valueOf(obj[1]));
			ramcapacitypagination.setRamId(Integer.parseInt(String.valueOf(obj[2])));
			ramcapacitypagination.setRamCapacity(String.valueOf(obj[6]));
			ramcapacitypagination.setCreatedBy(String.valueOf(obj[3]));
			ramcapacitypagination.setUpdatedBy(String.valueOf(obj[8]));
			ramcapacitypagination.setCreatedOn(String.valueOf(obj[4]));
			ramcapacitypagination.setUpdatedOn(String.valueOf(obj[9]));
			ramcapacitypagination.setRamtypeStatus(Boolean.parseBoolean(String.valueOf(obj[7])));
			list.add(ramcapacitypagination);
		}
		if(!list.isEmpty()) {

		PagenationResponse remcapacityresp = new PagenationResponse();
		long totalPages = 1;

		if (totalRecords == 0) {
			remcapacityresp.setTotalNumberOfPages(Long.valueOf(0));
		} else {
			totalPages = totalRecords / pageSize;
			totalPages = ((totalRecords % pageSize > 0) ? (totalPages + 1) : totalPages);
		}
		remcapacityresp.setTotalNumberOfPages(totalPages);
		remcapacityresp.setTotalNumberOfRecords(totalRecords);
		remcapacityresp.setList(list);
		return remcapacityresp;
	}else {
		
		throw new RecordNotFoundException("No Ram Type Data Found");
	}
	}
}
