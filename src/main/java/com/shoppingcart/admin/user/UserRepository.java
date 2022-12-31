package com.shoppingcart.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shoppingcart.admin.entity.User;

//ORM object relational mapping: query through Entity's name and properties's name
public interface UserRepository extends CrudRepository<User, Integer> {


	
	public Long countById(Integer Id);
	
	@Query("UPDATE User u SET u.enabled = ?2 where u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	//check Duplicated Email
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
}
