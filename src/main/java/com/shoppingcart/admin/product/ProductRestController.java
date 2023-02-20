package com.shoppingcart.admin.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.brand.BrandNotFoundException;
import com.shoppingcart.admin.brand.BrandService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@RestController
public class ProductRestController {
	
	@Autowired
	BrandService brandService;

	@GetMapping("products/{id}/categories")
	public List<Category> getCategory(@PathVariable(name ="id")Integer id) throws BrandNotFoundException{
		
				try {
					List<Category> newListCategoies = new ArrayList<>();
					
					Brand brand = brandService.findById(id);
					
					Set<Category> listCategories =  brand.getCategories();
					
					for (Category category : listCategories) {
						newListCategoies.add(Category.copyIdAndName(category.getId(), category.getName()));
					}
					return newListCategoies;
					
				} catch (Exception e) {
					throw new BrandNotFoundException("Brand Not Found");
				}
	
	
	}
}
