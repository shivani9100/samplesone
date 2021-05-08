package com.Inventory.Project.AssectService.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Repository
public class BrandDaoImpl {

	@PersistenceContext
	EntityManager entityManager;

	public PagenationResponse getAllBrands(Integer pagenumber, Integer pageSize, String search) throws RecordNotFoundException {
		long totalRecords = 0;
		if (search.equalsIgnoreCase("null") || search.equalsIgnoreCase("undefined")) {
			search = null;
			
		}

		// select as1.assetid,as1.assettype,br.* from `asset_type_brand` bd join `brand`
		// br on br.brandid=bd.brand_bid join `asset_type1` as1 on
		// as1.assetid=bd.asset_type_id WHERE as1.assettype="Laptop" order by
		// br.lastmodefied_date DESC limit 0,5

		StringBuilder queryString = new StringBuilder(

				"select as1.assetid,as1.assettype,br.* from  `asset_type_brand` bd join  `brand` br on br.brandid=bd.brand_bid join `asset_type1` as1 on as1.assetid=bd.asset_type_id ");

		if (search != null) {

			queryString.append("WHERE as1.assettype LIKE '" + search + "%' or br.brandname LIKE '" + search + "%'");

		}

		queryString.append(" order by br.lastmodefied_date DESC ");

		int startingRow = 0;
		if (pagenumber == 1) {
			startingRow = 0;
		} else {
			startingRow = ((pagenumber - 1) * pageSize);
		}

		List<Object[]> resultList = entityManager
				.createNativeQuery(queryString + " limit " + startingRow + "," + pageSize).getResultList();
		List<BrandPagination> list = new ArrayList<>();

		List<Object[]> resultObj = entityManager.createNativeQuery(queryString.toString()).getResultList();
		if (!resultObj.isEmpty()) {
			totalRecords = Long.valueOf(resultObj.size());
		}
		for (Object[] obj : resultList) {
			BrandPagination brandresponse1 = new BrandPagination();
			brandresponse1.setAssetId(Integer.parseInt((String.valueOf(obj[0]))));
			brandresponse1.setAssetType(String.valueOf(obj[1]));
			brandresponse1.setBrandId(Integer.parseInt((String.valueOf(obj[2]))));
			brandresponse1.setBrandName(String.valueOf(obj[3]));
			brandresponse1.setBrandStatus(Boolean.parseBoolean((String.valueOf(obj[4]))));
			brandresponse1.setCreatedBy(String.valueOf(obj[5]));
			brandresponse1.setCreatedOn(String.valueOf(obj[6]));
			brandresponse1.setUpdatedBy(String.valueOf(obj[8]));
			brandresponse1.setUpdatedOn(String.valueOf(obj[9]));
			list.add(brandresponse1);
		}

		if (!list.isEmpty()) {

			PagenationResponse brandResponse = new PagenationResponse();

			long totalPages = 1;
			if (totalRecords == 0) {
				brandResponse.setTotalNumberOfPages(Long.valueOf(0));
			} else {
				totalPages = totalRecords / pageSize;
				totalPages = ((totalRecords % pageSize > 0) ? (totalPages + 1) : totalPages);
			}
			brandResponse.setTotalNumberOfPages(totalPages);
			brandResponse.setTotalNumberOfRecords(totalRecords);
			brandResponse.setList(list);

			return brandResponse;
		}else {
			throw new RecordNotFoundException("No Data Present in the Brand");
		}
	}

}
