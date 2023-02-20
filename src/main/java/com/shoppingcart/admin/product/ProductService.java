package com.shoppingcart.admin.product;

import java.util.Date;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.brand.BrandNotFoundException;
import com.shoppingcart.admin.entity.Product;

@Service
@Transactional
public class ProductService {

	public static final int PRODUCTS_PER_PAGE = 4;
	@Autowired
	ProductRepository productRepo;
	
	public Product save(Product product) {
		boolean isUpdatingProduct = (product.getId() != null);
		if(isUpdatingProduct) {
			Product exsitingProduct = productRepo.findById(product.getId()).get();
		}
		return productRepo.save(product);
	}

	public Page<Product> listPerPage(int pageNumb, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of( pageNumb - 1,PRODUCTS_PER_PAGE ,sort);
		if(keyword != null) {
			return productRepo.findAll(keyword,pageable);
		}
		return productRepo.findAll(pageable);
	}

	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		productRepo.updateEnabledStatus(id, enabled);
		
	}

	public void deleteById(Integer id) throws ProductNotFoundException {
		Long countById =	productRepo.countById(id);
		if(countById == 0 || countById == null) {
			throw new ProductNotFoundException("Could not found");
		}
		productRepo.deleteById(id);
		
	}

	public Product findById(Integer id) throws ProductNotFoundException{
		try {
			return productRepo.findById(id).get();
			
		}catch(NoSuchElementException ex) {
			
			throw new ProductNotFoundException("Could not find any product");
		}
	
	}
	public void updateDate(Integer id, Date updateTime) {
		productRepo.updateTime(id, updateTime);
	}

}
