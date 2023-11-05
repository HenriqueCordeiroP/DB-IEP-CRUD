package com.db_project.iep.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/components")
public class ComponentsController {
	
	@GetMapping("/navbar")
	public String loadNavbar() {
		return "components/navbar";
	}
}
