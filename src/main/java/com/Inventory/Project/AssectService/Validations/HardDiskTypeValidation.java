package com.Inventory.Project.AssectService.Validations;

public class HardDiskTypeValidation {

	public static boolean isHardDiskTypeValid(String str) {
		
		return (str.matches("^[a-zA-Z]+$"));
	}
}
