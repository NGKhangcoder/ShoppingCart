package com.shoppingcart.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "products")
public class Product extends IdBaseEntity {

	@Column(length = 255, nullable = false, unique = true)
	private String name;

	@Column(length = 255, nullable = false, unique = true)
	private String alias;

	@Column(length = 512, nullable = false,name="short_description")
	private String shortDescription;

	@Column(length = 4096, nullable = false,name="full_description")
	private String fullDescription;

	@Column(nullable = false)
	private String mainImage;

	@Column(nullable = false, name="create_time",updatable = false)
	private Date createTime;


	@Column(name="update_time")
	private Date updateTime;

	

	@Column()
	private boolean enabled;

	@Column()
	private boolean intStock;

	@Column()
	private float cost;

	@Column()
	private float price;

	@ManyToOne()
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@ManyToOne()
	@JoinColumn(name = "category_id")
	private Category category;

	
	public String getImagePath() {
		return "/product-photos/" + this.id + "/" + this.mainImage;
	}



	@Column()
	private float discountPercent;

	@Column()
	private float length;

	@Column()
	private float width;

	@Column()
	private float height;

	@Column()
	private float weight;
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public boolean isIntStock() {
		return intStock;
	}

	public void setIntStock(boolean intStock) {
		this.intStock = intStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

}
