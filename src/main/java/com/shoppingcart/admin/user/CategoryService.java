package com.shoppingcart.admin.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Category;

@Service
@Transactional
public class CategoryService {
	 
	@Autowired
	CategoryRepository categoryRepo;
	
	public List<Category> listAll(){
		return (List<Category>) categoryRepo.findAll();
	}
	
	public Category saveCategory(Category category) {
	
		return categoryRepo.save(category);
	}
	
	public void deleteCategoryById(Integer id) {
		categoryRepo.deleteById(id);
	}
	
	public Category findById(Integer id) {
		return categoryRepo.findById(id).get();
	}
	
	public void updateStatus(Integer id,boolean enabled) {
		categoryRepo.updateStatus(id,enabled);
	}

}
