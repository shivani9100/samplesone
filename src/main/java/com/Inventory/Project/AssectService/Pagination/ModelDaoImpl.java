package com.Inventory.Project.AssectService.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;

@Repository
public class ModelDaoImpl {

	@PersistenceContext
	EntityManager entityManager;

	public PagenationResponse getAllModels(Integer pagenumber, Integer pageSize, String search)
			throws RecordNotFoundException {
		long totalRecords = 0;

		StringBuilder queryString = new StringBuilder(
				"select a1.assetid,a1.assettype,b1.brandid,b1.brandname,m.* from `asset_type_brand` atb join `asset_type1` a1 on a1.assetid = atb.asset_type_id join `brand` b1 on b1.brandid = atb.brand_bid join `brand_model` bm on atb.brand_bid = bm.brand_bid join `model` m on m.modelid = bm.model_modelid");
		if (search != null) {

			queryString.append(" WHERE a1.assettype or b1.brandname or m.modelname LIKE '%" + search + "%'");

		}
		queryString.append(" order by m.lastmodefied_date DESC");

		int startingRow = 0;
		if (pagenumber == 1) {
			startingRow = 0;
		} else {
			startingRow = ((pagenumber - 1) * pageSize);
		}

		List<Object[]> resultList = entityManager
				.createNativeQuery(queryString + " limit " + startingRow + "," + pageSize).getResultList();
		List<ModelPagination> list = new ArrayList<>();

		List<Object[]> resultobj = entityManager.createNativeQuery(queryString.toString()).getResultList();

		if (!resultobj.isEmpty()) {
			totalRecords = Long.valueOf(resultobj.size());
		}

		for (Object[] obj : resultList) {
			ModelPagination modelPagination = new ModelPagination();
			modelPagination.setAssetId(Integer.parseInt(String.valueOf(obj[0])));
			modelPagination.setAssetType(String.valueOf(obj[1]));
			modelPagination.setBrandid(Integer.parseInt(String.valueOf(obj[2])));
			modelPagination.setBrandname(String.valueOf(obj[3]));
			modelPagination.setModelid(Integer.parseInt(String.valueOf(obj[4])));
			modelPagination.setCreatedBy(String.valueOf(obj[5]));
			modelPagination.setCreatedOn(String.valueOf(obj[6]));
			modelPagination.setLastmodefiedDate(String.valueOf(obj[7]));
			modelPagination.setModelname(String.valueOf(obj[8]));
			modelPagination.setModelstatus(Boolean.parseBoolean(String.valueOf(obj[9])));
			modelPagination.setUpdatedBy(String.valueOf(obj[10]));
			modelPagination.setUpdatedOn(String.valueOf(obj[11]));
			list.add(modelPagination);
		}

		if (!list.isEmpty()) {

			PagenationResponse response = new PagenationResponse();
			long totalPages = 1;

			if (totalRecords == 0) {
				response.setTotalNumberOfPages(Long.valueOf(0));
			} else {
				totalPages = totalRecords / pageSize;
				totalPages = ((totalRecords % pageSize > 0) ? (totalPages + 1) : totalPages);
			}
			response.setTotalNumberOfPages(totalPages);
			response.setTotalNumberOfRecords(totalRecords);
			response.setList(list);

			return response;
		} else {
			throw new RecordNotFoundException("No Data Found in the Model");
		}
	}

	public List<ModelPagination> getAllModelWithAssettypeBrandIds(Integer assetid,Integer brandid,Integer modelid) {
		StringBuilder queryString = new StringBuilder(
				"select a1.assetid,a1.assettype,b1.brandid,b1.brandname,m.* from `asset_type_brand` atb join `asset_type1` a1 on a1.assetid = atb.asset_type_id join `brand` b1 on b1.brandid = atb.brand_bid join `brand_model` bm on atb.brand_bid = bm.brand_bid join `model` m on m.modelid = bm.model_modelid");
		
			queryString.append(" where a1.assetid="+ assetid +" and b1.brandid="+brandid+" and m.modelid="+modelid);
	
		List<ModelPagination> list = new ArrayList<>();

		List<Object[]> resultobj = entityManager.createNativeQuery(queryString.toString()).getResultList();

		for (Object[] obj : resultobj) {
			ModelPagination modelPagination = new ModelPagination();
			modelPagination.setAssetId(Integer.parseInt(String.valueOf(obj[0])));
			modelPagination.setAssetType(String.valueOf(obj[1]));
			modelPagination.setBrandid(Integer.parseInt(String.valueOf(obj[2])));
			modelPagination.setBrandname(String.valueOf(obj[3]));
			modelPagination.setModelid(Integer.parseInt(String.valueOf(obj[4])));
			modelPagination.setCreatedBy(String.valueOf(obj[5]));
			modelPagination.setCreatedOn(String.valueOf(obj[6]));
			modelPagination.setLastmodefiedDate(String.valueOf(obj[7]));
			modelPagination.setModelname(String.valueOf(obj[8]));
			modelPagination.setModelstatus(Boolean.parseBoolean(String.valueOf(obj[9])));
			modelPagination.setUpdatedBy(String.valueOf(obj[10]));
			modelPagination.setUpdatedOn(String.valueOf(obj[11]));
			list.add(modelPagination);

		}
		return list;

	}
}
