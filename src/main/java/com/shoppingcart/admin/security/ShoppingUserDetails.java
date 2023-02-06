package com.shoppingcart.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;

public class ShoppingUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private User user;
	
	public ShoppingUserDetails(User user){
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authories = new ArrayList<>();
		for (Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getName())); //compare to role at webSecurityCOnfig.config()
		}
		return authories;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { //locked -> unlogin
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true; //true -> can still use
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	} 
	
	public String getFullName() {
	
		return user.getFullName();
	}
	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}
	
	public void setLastName(String LastName) {
		user.setLastName(LastName);
	}
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
}
