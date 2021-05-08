package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("BlockDetails")
public class BlockDetailsController {
	private static final Logger logger = LogManager.getLogger(BlockDetailsController.class);
	
	@Autowired
	private BlockDetailsDao blockDetailsDao;
	
	@GetMapping("/getall")
	public List<BlockDetailsModel> getAll(){
		logger.debug("entered into getAll method");
		
		List<BlockDetailsModel>Blockdetailslist= blockDetailsDao.findAll();
		
		return Blockdetailslist;
	}

}
