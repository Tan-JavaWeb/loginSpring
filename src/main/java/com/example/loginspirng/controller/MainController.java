package com.example.loginspirng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping({"/", "/welcome"})
	public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }
	
	@GetMapping("/loginpage")
	public String loginPage(Model model) {
		return "loginPage";
	}
	
	@GetMapping("/userinfo")
	public String userInfo(Model model) {
		return "userInfo";
	}

}
