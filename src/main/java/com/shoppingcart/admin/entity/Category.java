package com.shoppingcart.admin.entity;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category extends IdBaseEntity  {
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	@Column(length = 64, nullable = false)
	private String alias;
	
	@Column(length = 128)
	private String image;
	
	private boolean enabled;
	
	public Category() {
		
	}
	
	public Category(String name,String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	@Transient
	public String getImagePath() {
		if(id == null || image == null) return"/images/default-user.png";
		return "/category-photos/" + this.id + "/" + this.image;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}
	

	
	
}
