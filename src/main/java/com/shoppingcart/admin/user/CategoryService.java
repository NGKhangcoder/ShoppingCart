package com.shoppingcart.admin.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;

@Service
@Transactional
public class CategoryService {
	 
	@Autowired
	CategoryRepository categoryRepo;
	
	public static final int CATEGORIES_PER_PAGE = 4;
	
	public List<Category> listAll(){
		return (List<Category>) categoryRepo.findAll(Sort.by("id").ascending());
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

	public Page<Category> listPerPage(int pageNum,String sortField,String sortDir,String keyword) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending()  : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE,sort);
		
		if(keyword != null) {
			return categoryRepo.findAll(keyword,pageable);
		}
		
		return categoryRepo.findAll(pageable);
	}

}
