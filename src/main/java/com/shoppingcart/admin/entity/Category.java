package com.shoppingcart.admin.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category extends IdBaseEntity {
	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Column(length = 64, nullable = false)
	private String alias;

	@Column(length = 128)
	private String image;

	private boolean enabled;


	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();
	
	@OneToMany(mappedBy = "category")
	Set<Product> listProducts = new HashSet<>();
	
	
	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
		
	}
	public Category(String name,Category parent) {
		this(name);
		this.parent = parent;

	}
	
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	

	public Category() {

	}

	public Category(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}


	@Transient
	public String getImagePath() {
		if (id == null || image == null)
			return "/images/default-user.png";
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

	public static Category copyIdAndName(Integer id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);
		
		return copyCategory;
	}
	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		
		return copyCategory;
	}
	public static Category copyFull(Category category, String name) {
		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(name);
		
		return copyCategory;
	
	}
	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setImage(category.getImage());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setHasChildren(category.getChildren().size() > 0);
		
		return copyCategory;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	
	
	@Transient
	private boolean hasChildren;

}
