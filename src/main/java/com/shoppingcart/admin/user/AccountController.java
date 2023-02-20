package com.shoppingcart.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.FileUploadUtil;
import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.security.ShoppingUserDetails;


@Controller
public class AccountController {

	@Autowired
	private UserService service;

	@GetMapping("/account")
	public String showInfor(Model model, @AuthenticationPrincipal ShoppingUserDetails logger) {
		User user = service.getUserEmail(logger.getUsername());
		System.out.println(user.getRoles());
		model.addAttribute("listRoles", user.getRoles());
		model.addAttribute("user", user);
		return "users/account_form";
	}

	@PostMapping("/account/save")
	public String saveAccount(User user, @RequestParam("image") MultipartFile multipartFile,
			@AuthenticationPrincipal ShoppingUserDetails logger,RedirectAttributes redirectAttributes) throws IOException{
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			
			String uploadDir = "user-photo/" + savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) {
				user.setPhotos(null);
		
			}
			service.updateAccount(user);
		}
		

	
		logger.setFirstName(user.getFirstName());
		logger.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("message","Your account details have been updated");
		return "redirect:/account";
	}

}
