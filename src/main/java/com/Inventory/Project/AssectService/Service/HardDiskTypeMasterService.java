package com.Inventory.Project.AssectService.Service;

import java.util.List;
import java.util.Map;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Response.ResponseList;

public interface HardDiskTypeMasterService {

	public Boolean insertHardDisktTpe(HardDiskTypeMaster hardDiskTypeMaster);

	public List<HardDiskTypeMaster> getListOfHardDiskTypes();

	public ResponseList getListOfHardDiskTypes(int pageNo, int sizePerPage);

	public ResponseList getListOfHardDiskTypes(String search, int pageNo, int sizePerPage);

	public HardDiskTypeMaster getHardDiskTypeById(Integer hardDiskId);

	public HardDiskTypeMaster getHardDiskTypeByType(String hardDiskType);

	public List<HardDiskTypeMaster> getHardDiskTypeByStatus(Boolean hardDiskStatus);

	public Map<String, Boolean> deleteHardDiskTypeById(Integer hardDiskId) throws RecordNotFoundException;

	public Boolean updateHardDiskType(HardDiskTypeMaster hardDiskTypeMaster) throws RecordNotFoundException;

    ResponseList getListOfHardDiskTypes(int pageNo, int sizePerPage, String sortBy);


}