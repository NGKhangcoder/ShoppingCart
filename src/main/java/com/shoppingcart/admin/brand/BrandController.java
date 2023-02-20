package com.shoppingcart.admin.brand;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;

@Controller
public class BrandController {
	@Autowired
	BrandService service;
	
	@Autowired
	CategoryService catService;
	
	private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";

	@GetMapping("/brands")
	public String listAll(Model model) {
		return listPerPage(1,model, "name","asc", null);
	}

	@GetMapping("/brands/page/{pageNumb}")
	public String listPerPage(@PathVariable(name = "pageNumb") int pageNumb, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Brand> page = service.listPerPage(pageNumb, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();

		long startCount = (pageNumb - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reservedSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("currentPage", pageNumb);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reservedSortDir);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);

		return "Brands/Brands";

	}

	@GetMapping("/brands/new")
	public String createBrand(Model model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTittle", "Create New Brand");
		return "brands/brands_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, RedirectAttributes redirectAttributes,
			@RequestParam("logo1") MultipartFile multipartFile) throws IOException{
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			Brand savedBrand = service.save(brand);
			
			String uploadDir = "logo-photos/" + savedBrand.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			System.out.println(brand.getCategories());
		}else {
			if(brand.getLogo().isEmpty())
				brand.setLogo(null);
			service.save(brand);
		}
		redirectAttributes.addFlashAttribute("message","Brands had saved successfully");
		return getRedirectURLtoAffectedBrand(brand);
	}
	
	private String getRedirectURLtoAffectedBrand(Brand brand) {
		String firstPartName = brand.getName();
		return "redirect:/brands/page/1?sortField=id&sortDir=asc&keyword=" + firstPartName;
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name ="id")Integer id, Model model,Brand brand, RedirectAttributes redirectAttributes) {
		try {
			brand = service.findById(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();
			model.addAttribute("listCategories",listCategories);
			model.addAttribute("brand",brand);
			model.addAttribute("pageTittle","Edit Brand");
			return "brands/brands_form";
		}catch(BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return defaultRedirectURL;
		}
	
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id")Integer id,Model model,RedirectAttributes redirectAttribute) {
		try {
			service.deleteById(id);
			String brandLogo = "logo-photos/" + id;
			FileUploadUtil.removeDir(brandLogo);
			redirectAttribute.addFlashAttribute("message","The user ID: " + id +" has been deleted");
		}catch(BrandNotFoundException ex) {
			redirectAttribute.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}
}
