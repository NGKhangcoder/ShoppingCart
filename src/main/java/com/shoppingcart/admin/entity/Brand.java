package com.shoppingcart.admin.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity()
@Table(name = "brands")

public class Brand extends IdBaseEntity {
	@Column(nullable = false, length = 45, unique = true)
	private String name;

	@Column(nullable = false, length = 128)
	private String logo;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> category = new HashSet<>();

	@OneToMany(mappedBy = "brand")
	private Set<Product> listProduct = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return category;
	}

	public void setCategories(Set<Category> category) {
		this.category = category;
	}

	public String getLogosImagePath() {
		if (id == null || logo == null)
			return "";
		return "/logo-photos/" + this.id + "/" + this.logo;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	

}
