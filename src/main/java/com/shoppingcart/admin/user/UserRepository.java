package com.shoppingcart.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shoppingcart.admin.entity.User;

//ORM object relational mapping: query through Entity's name and properties's name
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	public Long countById(Integer Id);

	@Query("UPDATE User u SET u.enabled = ?2 where u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	// check Duplicated Email
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);

	@Query("Select u from User u where u.firstName Like %?1% OR u.lastName Like %?1% Or u.email Like %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);

}
