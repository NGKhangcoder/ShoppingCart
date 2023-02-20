package com.shoppingcart.admin.category;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.property.Child;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.shoppingcart.admin.category.export.CategoryCsvExporter;
import com.shoppingcart.admin.category.export.CategoryExcelExporter;
import com.shoppingcart.admin.category.export.CategoryPDFExporter;
import com.shoppingcart.admin.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	CategoryService service;

	@GetMapping("/categories")
	public String listAll(Model model) {
		return listByPage(1, model, "id", "asc", null);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {


//		Page<Category> page = service.listPerPage(pageNum, sortField, sortDir, keyword);
//		List<Category> listCategories = page.getContent();
		CategoryPageInfor pageInfo = new CategoryPageInfor();
		List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "categories/categories";
	}

//	@GetMapping("/categories/new")
//	public String createNewCategory(Model model) {
//		
//	
//		model.addAttribute("listCategories", listChildren);
//		model.addAttribute("category", new Category());
//		model.addAttribute("pageTittle", "Create New Category");
//
//		return "categories/categories_form";
//	}
	
	@GetMapping("/categories/new")
	public String createNewCategory(Model model) {
		List<Category> listCategories = service.listCategoriesUsedInForm();

	
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("category", new Category());
		model.addAttribute("pageTittle", "Create New Category");

		return "categories/categories_form";
	}

//	private Set<Category> callChildren(Set<Category> children, Category parent, String underline) {
//
//		Set<Category> getChildren = parent.getChildren();
//		if (parent.getParent() == null) {
//
//			children.add(parent);
//		}
//		for (Category category : getChildren) {
//			category.setName(underline + category.getName());
//			children.add(category);
//
//			if (!category.getChildren().isEmpty()) {
//
//				callChildren(children, category, underline + "--");
//
//			}
//
//		}
//		return children;
//
//	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, RedirectAttributes redirectAttributes,
			@RequestParam("image1") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = service.saveCategory(category);
			String uploadDir = "category-photos/" + savedCategory.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (category.getImage().isEmpty()) {
				category.setImage(null);
			}
			service.saveCategory(category);

		}

		redirectAttributes.addFlashAttribute("message", "Saved Category");

		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		service.deleteCategoryById(id);

		redirectAttributes.addFlashAttribute("message", "Deleted Category Id: " + id);

		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			Model model, Category category) {
		category = service.findById(id);

		model.addAttribute("category", category);
		model.addAttribute("pageTittle", "Edit User");

		redirectAttributes.addFlashAttribute("message", "Editted Id: " + id);

		return "/categories/categories_form";
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled,
			RedirectAttributes redirectAttributes) {

		service.updateStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "Id: " + id + " " + status;
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/categories";
	}

	@GetMapping("/categories/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Category> listCategoris = service.listAll();
		CategoryCsvExporter categoryCsvExporter = new CategoryCsvExporter();
		categoryCsvExporter.export(listCategoris, response);
	}

	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Category> listCategoris = service.listAll();
		CategoryExcelExporter categoryExcelExporter = new CategoryExcelExporter();
		categoryExcelExporter.export(response, listCategoris);
	}

	@GetMapping("/categories/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<Category> listCategoris = service.listAll();
		CategoryPDFExporter categoryPdfExporter = new CategoryPDFExporter();
		categoryPdfExporter.export(response, listCategoris);
	}

}
