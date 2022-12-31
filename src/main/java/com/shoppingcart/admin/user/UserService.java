package com.shoppingcart.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	public List<User> listAll() {
		return (List<User>) userRepo.findAll();

	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();

	}

	public User save(User user) {
		return userRepo.save(user);
	}

	public User findById(Integer id) throws UserNotFoundException {
		try {

			return userRepo.findById(id).get();

		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID: " + id);

		}

	}

	public void deleteById(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID: " + id);
		}

		userRepo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id,boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if(userByEmail != null) return false;
			
		}else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

}
