package com.shoppingcart.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.user.UserService;

@RestController //return data instead of views
public class UserRestController {
 @Autowired
 private UserService service;
 
 @PostMapping("users/check_email")
 public String checkDuplicatedEmail(Integer id, String email) {
	 
	 return service.isEmailUnique(id,email) ? "OK" : "Duplicated";
 }
 
}
