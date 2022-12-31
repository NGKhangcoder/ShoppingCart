package com.shoppingcart.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.FileUploadUtil;
import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.user.UserNotFoundException;
import com.shoppingcart.admin.user.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);

		return "users/users";
	}

	@GetMapping("/user/new")
	public String createNew(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		model.addAttribute("pageTittle", "Create New User");
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("user", user);
		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
//		System.out.println(user);
//		service.save(user);
//
//		redirectAttributes.addFlashAttribute("message", " The user has been saved successfully.");
//
//		return "redirect:/users";
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		redirectAttributes.addFlashAttribute("message"," The user has been saved successfully.");
		return "redirect:/users";
	}

	@GetMapping("/user/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, User user,RedirectAttributes redirectAttributes) {
		try {
			user = service.findById(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("pageTittle", "Edit User");
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("user", user);
			redirectAttributes.addFlashAttribute("message","Edit ID: " + id);

			return "users/user_form";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	
	}
	
	@GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable(name = "id")Integer id,Model model,RedirectAttributes redirectAttributes) {
		try {
			service.deleteById(id);
			String userPhotosDir = "user-photos/" + id;
			FileUploadUtil.removeDir(userPhotosDir);
			redirectAttributes.addFlashAttribute("message", "the user ID: "  + id + " has been successfully" );
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
			
		}
		return "redirect:/users";
	}
	
//	@GetMapping("/users/{id}/enabled")
//	public String statusUser(@PathVariable(name = "id")Integer id,Model model,RedirectAttributes redirectAttributes,User user) {
//		
//		try {
//			user = service.findById(id);
//			if(user.isEnabled() == true) {
//				user.setEnabled(false);
// 			}
//			else {
//				user.setEnabled(true);
//			}
//			List<User> listUsers = service.listAll();
//			model.addAttribute("listUsers",listUsers);
//			redirectAttributes.addFlashAttribute("message","Enabled/Disabled ID: " + id);
//		}catch(UserNotFoundException ex){
//			redirectAttributes.addFlashAttribute("message",ex.getMessage());
//		}
//		
//		
//		
//		return "redirect:/users";
//	}
	
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name = "id")Integer id,@PathVariable(name = "status")boolean enabled,RedirectAttributes redirectAttributes,User user) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "the user ID" + id + "hase been " + status;
		redirectAttributes.addAttribute("message",message);
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/users";
	}

}

