package com.shoppingcart.admin.product;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoppingcart.admin.entity.Product;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	@Query("select p from Product p WHERE p.name like %?1%")
	Page<Product> findAll(String keyword, Pageable pageable);

	@Query("UPDATE Product p SET p.enabled = ?2 where p.id = ?1")
	@Modifying
	void updateEnabledStatus(Integer id, boolean enabled);

	Long countById(Integer id);
	
	@Query("UPDATE Product p SET p.updateTime = ?2 where p.id = ?1")
	@Modifying
	void updateTime(Integer id, Date updateDate);

}
