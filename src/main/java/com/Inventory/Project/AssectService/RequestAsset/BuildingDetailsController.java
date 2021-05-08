package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("BuildingDetails")
public class BuildingDetailsController {
	
	private static final Logger logger = LogManager.getLogger(BuildingDetailsController.class);
	@Autowired
	private BuildingDetailsDao buildingDetailsDao;
	
	@GetMapping("/getall")
	public List<BuildingDetailsModel> getAll(){
		logger.debug("entered into getall method");
		
		return buildingDetailsDao.findAll();
		
	}

}
