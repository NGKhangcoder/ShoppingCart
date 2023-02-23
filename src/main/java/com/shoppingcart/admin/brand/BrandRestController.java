package com.shoppingcart.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@RestController
public class BrandRestController {
	
	@Autowired
	BrandService brandService;


	@GetMapping("products/{id}/categories")
	public List<CategoryDTO> listCategories(@PathVariable(name ="id")Integer brandId)throws BrandNotFoundRestException{
		List<CategoryDTO> listCategoies = new ArrayList<>();
		try {
			Brand brand = brandService.findById(brandId);
			
			Set<Category> categories = brand.getCategories();
			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(),category.getName());
				listCategoies.add(dto);
			}
			return listCategoies;
		}catch(BrandNotFoundException e){
			throw new BrandNotFoundRestException();
		}
	}
	
}
