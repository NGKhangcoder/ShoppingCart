package com.shoppingcart.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.shoppingcart.admin.security.ShoppingUserDetails;
import com.shoppingcart.admin.security.ShoppingUserDetailsService;
import com.shoppingcart.admin.user.UserNotFoundException;
import com.shoppingcart.admin.user.UserService;
import com.shoppingcart.admin.user.export.UserCsvExporter;
import com.shoppingcart.admin.user.export.UserExcelExporter;
import com.shoppingcart.admin.user.export.UserPDFExporter;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	private ShoppingUserDetailsService detailService = new ShoppingUserDetailsService();
	
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc";

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}

	@GetMapping("/users/new")
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
			@RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShoppingUserDetails logger)
			throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.save(user);
		}
		logger.setFirstName(user.getFirstName());
		logger.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("message", " The user has been saved successfully.");
		return getRedirectURLtoAffectedUser(user);
	}
	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, User user,
			RedirectAttributes redirectAttributes) {
		try {
			user = service.findById(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("pageTittle", "Edit User");
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("user", user);
			redirectAttributes.addFlashAttribute("message", "Edit ID: " + id);
			

			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}

	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.deleteById(id);
			String userPhotosDir = "user-photos/" + id;
			FileUploadUtil.removeDir(userPhotosDir);
			redirectAttributes.addFlashAttribute("message", "the user ID: " + id + " has been successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());

		}
		return defaultRedirectURL;
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes, User user,Model model) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "the user ID " + id + "hase been " + status;
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addFlashAttribute("message", message);
		return defaultRedirectURL;
	}

	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserPDFExporter exporter = new UserPDFExporter();
		exporter.export(listUsers, response);

	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {


		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();

		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "users/users";
	}

	@GetMapping("/account")
	public String showInfor( Model model,
			@AuthenticationPrincipal ShoppingUserDetails logger) {

		User user = service.getUserEmail(logger.getUsername());

		List<Role> listRoles = service.listRoles();
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("user", user);
		model.addAttribute("pageTittle", "User's Information");

		return "users/user_form";
	}

}
