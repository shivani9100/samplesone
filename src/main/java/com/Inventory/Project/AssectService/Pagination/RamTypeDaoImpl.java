package com.Inventory.Project.AssectService.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;

@Repository
public class RamTypeDaoImpl {

	@PersistenceContext
	EntityManager entityManager;

	public PagenationResponse getAllRamTypes(Integer pagenumber, Integer pagesize, String searchBy) throws RecordNotFoundException {

		long totalRecords = 0;

		StringBuilder queryString = new StringBuilder("select * from `ram_type_master` ramType");

		if (searchBy != null) {

			queryString.append(" where ramType.ramtype_name Like '%" + searchBy + "%'");
		}

		queryString.append(" order BY ramType.lastmodefied_date DESC");

		int startingRow = 0;
		if (pagenumber == 1) {
			startingRow = 0;
		} else {
			startingRow = ((pagenumber - 1) * pagesize);
		}
		List<Object[]> resultList = entityManager
				.createNativeQuery(queryString + " limit " + startingRow + "," + pagesize).getResultList();

		List<RamTypePagenation> list = new ArrayList<>();

		List<Object[]> resultObj = entityManager.createNativeQuery(queryString.toString()).getResultList();

		if (!resultObj.isEmpty()) {
			totalRecords = Long.valueOf(resultObj.size());
		}
		for (Object[] obj : resultList) {
			RamTypePagenation ramTypePagenation = new RamTypePagenation();
			ramTypePagenation.setRamTypeid(Integer.parseInt(String.valueOf(obj[0])));
			ramTypePagenation.setCreatedBy(String.valueOf(obj[1]));
			ramTypePagenation.setCreatedOn(String.valueOf(obj[2]));
			ramTypePagenation.setLastmodefiedDate(String.valueOf(obj[3]));
			ramTypePagenation.setRamTypeName(String.valueOf(obj[4]));
			ramTypePagenation.setRamTypeStatus(Boolean.parseBoolean(String.valueOf(obj[5])));
			ramTypePagenation.setUpdatedBy(String.valueOf(obj[6]));
			ramTypePagenation.setUpdatedOn(String.valueOf(obj[7]));
			list.add(ramTypePagenation);
		}
		if (!list.isEmpty()) {

			PagenationResponse ramTyperes = new PagenationResponse();
			long totalPages = 1;

			if (totalRecords == 0) {
				ramTyperes.setTotalNumberOfPages(Long.valueOf(0));
			} else {
				totalPages = totalRecords / pagesize;
				totalPages = ((totalRecords % pagesize > 0) ? (totalPages + 1) : totalPages);
			}
			ramTyperes.setTotalNumberOfPages(totalPages);
			ramTyperes.setTotalNumberOfRecords(totalRecords);
			ramTyperes.setList(list);
			return ramTyperes;
		} else {
			throw new RecordNotFoundException("No Ram Type Data Found");
		}

	}
}
