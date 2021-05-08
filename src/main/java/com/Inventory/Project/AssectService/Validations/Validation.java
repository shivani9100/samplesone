package com.Inventory.Project.AssectService.Validations;

//@Component
public class Validation {
	public static boolean isTypeValid(String str) {
		return (str.matches("^[a-zA-Z0-9]+$"));
		

			
	}

}
