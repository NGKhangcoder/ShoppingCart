package com.shoppingcart.admin.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
