package com.shoppingcart.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shoppingcart.admin.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>{

	@Query("Update Category c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateStatus(Integer id, boolean enabled);
}
