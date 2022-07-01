package com.skymicrosystems.buildmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenericController {
	
	@GetMapping("/index")
    public String showIndex(Model model) {
		
        return "index";
    }
	
	@GetMapping("/home")
    public String showHome(Model model) {
		
        return "home";
    }
	
	@GetMapping("/index2")
    public String indexTest(Model model) {
		
        return "index-teste";
    }
}
