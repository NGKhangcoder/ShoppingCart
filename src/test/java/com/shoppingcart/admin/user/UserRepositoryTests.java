package com.shoppingcart.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.user.UserRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)

public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class,1);
		User user = new User("nhbtuyen2702@gmail.com","123456","Nguyen Hoang Bao","Nguyen");
		user.setEnabled(true);
		user.setPhotos("TuyenNHB.png");
		user.addRole(roleAdmin);
		User savedUser = repo.save(user);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
		
		
	}
	@Test 
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("ravikumar@gmail.com","123456","Ravi","Kumar");
		 
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);
		
		User savedUser = repo.save(userRavi);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test 
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();	
		for(User user : listUsers) {
			System.out.println(user);
		}
		//listUsers.forEach user -> sysout(user);
	}
}
