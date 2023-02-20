package com.shoppingcart.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.category.CategoryRepository;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@Service
@Transactional
public class BrandService {
	
	@Autowired
	private BrandRepository brandRepo;
	
	@Autowired
	private CategoryRepository cateRepo;
	
	public static final int BRANDS_PER_PAGE = 10;
	
	public Page<Brand> listPerPage(int pageNum,String sortField,String sortDir,String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of( pageNum - 1,BRANDS_PER_PAGE ,sort);
		if(keyword != null) {
			return brandRepo.findAll(keyword,pageable);
		}
		return brandRepo.findAll(pageable);
		
	}

	public Brand save(Brand brand) {
		boolean isUpdatingBrand = (brand.getId() != null);
		if(isUpdatingBrand) {
			Brand existingBrand = brandRepo.findById(brand.getId()).get();
			
		}
		return brandRepo.save(brand);
	}

	public Brand findById(Integer id) throws BrandNotFoundException {
		try {
			return brandRepo.findById(id).get();
			
		}catch(NoSuchElementException ex) {
			
			throw new BrandNotFoundException("Could not find any brand");
		}
	
	}

	public List<Category> listCategory() {
		// TODO Auto-generated method stub
		return (List<Category>) cateRepo.findAll() ;
	}

	public void deleteById(Integer id) throws BrandNotFoundException{
		Long countById = brandRepo.countById(id);
		if(countById == 0 || countById == null) {
			throw new BrandNotFoundException("Could not found");
		}
		brandRepo.deleteById(id);
		
	}
	
	public List<Brand> findAllBrand() {
		return (List<Brand>) brandRepo.findAll();
	}
	
	
//	public String checkUnique(Integer id,String name) {
//		boolean isCreatingNew  = (id==null || id == 0);
//		
//	}
}
