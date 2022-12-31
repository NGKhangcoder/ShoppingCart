package com.shoppingcart.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.user.CategoryService;

@Controller
public class CategoryController {
	@Autowired
	CategoryService service;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = service.listAll();
		model.addAttribute("listCategories",listCategories);
		
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String createNewCategory(Model model) {
		Category category = new Category();
		
		model.addAttribute("category", category);
		model.addAttribute("pageTittle","Create New");

		return "categories/categories_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,RedirectAttributes redirectAttributes) {
		
		Category savedCategory = service.saveCategory(category);
		redirectAttributes.addFlashAttribute("message","Saved Category");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id")Integer id,RedirectAttributes redirectAttributes) {
		 service.deleteCategoryById(id);
		
		redirectAttributes.addFlashAttribute("message","Deleted Category Id: " + id);
	
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id")Integer id, RedirectAttributes redirectAttributes,Model model,Category category){
		category = service.findById(id); 
		
		model.addAttribute("category", category);
		model.addAttribute("pageTittle","Edit User");
		
		redirectAttributes.addFlashAttribute("message","Editted Id: " + id);
		
		return "/categories/categories_form";
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateStatus(@PathVariable(name = "id")Integer id,@PathVariable(name = "status")boolean enabled, RedirectAttributes redirectAttributes) {
		
		service.updateStatus(id,enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "Id: " + id + " "+ status;
		redirectAttributes.addAttribute("message",message);
		redirectAttributes.addFlashAttribute("message",message);
		
		return "redirect:/categories";
	}
	
}
