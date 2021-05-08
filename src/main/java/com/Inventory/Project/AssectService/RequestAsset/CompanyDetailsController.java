package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("CompanyDetails")
public class CompanyDetailsController {
	private static final Logger logger = LogManager.getLogger(CompanyDetailsController.class);
	
	@Autowired
	private CompanyDetailsDao companyDetailsDao;
	
	@GetMapping("/getall")
	public List<CompanyDetailsModel> getAll(){
		logger.debug("entered into getall method");
		
		return companyDetailsDao.findAll();
		
	}

}
