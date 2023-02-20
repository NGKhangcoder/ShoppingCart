package com.shoppingcart.admin.product;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
import com.shoppingcart.admin.brand.BrandService;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.Product;

@Controller
public class ProductController {

	@Autowired
	private CategoryService catService;

	@Autowired
	private ProductService service;

	@Autowired
	private BrandService brandService;

	private String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc";
	
	@GetMapping("/products")
	public String listAll(Model model) {
		return listPerPage(1, model, "name", "asc", null);
	}

	@GetMapping("/products/new")
	public String createNewProduct(Model model) {

		List<Category> listCategories = catService.listCategoriesUsedInForm();
		List<Brand> listBrands = brandService.findAllBrand();
	
		model.addAttribute("product", new Product());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTittle", "Create New");
		return "products/products_form";
	}
	
	@GetMapping("/products/page/{pageNumb}")
	public String listPerPage(@PathVariable(name = "pageNumb") int pageNumb, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Product> page = service.listPerPage(pageNumb, sortField, sortDir, keyword);
		List<Product> listProducts = page.getContent();

		long startCount = (pageNumb - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reservedSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("listProducts", listProducts);
		model.addAttribute("currentPage", pageNumb);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reservedSortDir);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);

		return "products/products";

	}


	@PostMapping("products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttribute,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			product.setMainImage(fileName);
			product.setCreateTime(new Date());
			Product savedProduct =	service.save(product);
			String uploadDir = "product-photos/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}else {
			if(product.getMainImage().isEmpty()) {
				product.setMainImage(null);
				service.save(product);
			}
		}
		redirectAttribute.addFlashAttribute("message","Product had saved successfully");
		
		return getRedirectURLtoAffectedBrand(product);
	}
	private String getRedirectURLtoAffectedBrand(Product product) {
		String firstPartName = product.getName();
		return "redirect:/products/page/1?sortField=id&sortDir=asc&keyword=" + firstPartName;
	}
	
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes,
			Model model) {
		service.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "the product ID " + id + " hase been " + status;
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id")Integer id,Model model,RedirectAttributes redirectAttribute) {
		try {
			service.deleteById(id);
			String productlogo = "product-photos/" + id;
			FileUploadUtil.removeDir(productlogo);
			redirectAttribute.addFlashAttribute("message","The product ID: " + id +" has been deleted");
		}catch(ProductNotFoundException ex) {
			redirectAttribute.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/products/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, Product product,
			RedirectAttributes redirectAttributes) {
		try {
			product = service.findById(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();
			List<Brand> listBrands = brandService.findAllBrand();
	
			service.updateDate(id, new Date());
			model.addAttribute("product", product);
		

			model.addAttribute("product",product);
			model.addAttribute("listBrands", listBrands);

			model.addAttribute("pageTittle", "Edit Product");
			redirectAttributes.addFlashAttribute("message", "Edit ID: " + id);

			return "products/products_form";
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}

	}

}
