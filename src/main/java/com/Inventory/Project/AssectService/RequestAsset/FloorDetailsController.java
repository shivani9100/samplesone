package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("FloorDetails")
public class FloorDetailsController {
	
	private static final Logger logger = LogManager.getLogger(FloorDetailsController.class);
	
	@Autowired
	private FloorDetailsDao floorDetailsDao;
	
	@GetMapping("/getall")
	public List<FloorDetailsModel> getAll(){
		logger.debug("entered into getall method");
		
		return floorDetailsDao.findAll();
		
	}

}
