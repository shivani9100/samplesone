package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("LocationDetails")
public class LocationDetailsController {
	private static final Logger logger = LogManager.getLogger(BlockDetailsController.class);
	
	@Autowired
	private LocationDetailsDao locationDetailsDao;
	
	@GetMapping("/getall")
	public List<LocationDetailsModel> getAll(){
		
		return locationDetailsDao.findAll();
		
	}
	
	

}
