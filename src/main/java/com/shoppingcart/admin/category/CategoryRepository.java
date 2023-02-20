package com.shoppingcart.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shoppingcart.admin.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>{

	@Query("Update Category c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateStatus(Integer id, boolean enabled);
	
	@Query("Select c FROM Category c where c.name LIKE %?1% OR c.alias LIKE %?1%")
	public  Page<Category> findAll(String keyword,Pageable pageable);

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories(Sort sort);
	
	@Query("Select c FROM Category c WHERE c.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageable);
	
	@Query("select c FROM Category c WHERE c.name LIKE %?1%")
	public Page<Category> search(String keyword,Pageable pageable);


	public Category findByName(String name);
	

	public Category findByAlias(String alias);
	
 	
	
}
