package com.shoppingcart.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}
			
}
