package com.shoppingcart.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false) // negative return default data
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole(){
		Role roleAdmin = new Role("Admin","manage everything");
		Role savedRole = repo.save(roleAdmin); 
		
		assertThat(savedRole.getId()).isGreaterThan(0);//return true or false,
	}
	
	@Test
	public void testCreateRoles() {
		Role rolesalesSaleperson = new Role("salesperson" + "mamage product price","customers, shipping, oders and sales report");
		
		Role roleEditor = new Role("Editor","manage categories,brands" + "product, acticles and menus");
		
		Role roleShipper = new Role("Shipper", "view products, view orders" + " and update order status");
		
		Role roleAssistant = new Role("Assistant", " manage questions and reviews");
		
		repo.saveAll(List.of(rolesalesSaleperson,roleEditor,roleShipper,roleAssistant));
		
	}
}
