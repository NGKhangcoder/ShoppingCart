package com.shoppingcart.admin.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoppingcart.admin.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{

	@Query("select b from Brand b WHERE b.name like %?1%")
	Page<Brand> findAll(String keyword, Pageable pageable);

	Long countById(Integer id);

}
