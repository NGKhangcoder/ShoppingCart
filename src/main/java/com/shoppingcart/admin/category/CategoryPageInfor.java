package com.shoppingcart.admin.category;

public class CategoryPageInfor {
	private int totalPages;
	private long totalElement;
	
	public int getTotalPages() {
		return totalPages;
		
	}
	
	public void setTotalPages(int totaPages) {
		this.totalPages = totaPages;
	}
	public long getTotalElements() {
		return this.totalPages;
	}
	public void setTotalElements(long totalElements) {
		this.totalElement = totalElements;
	}
}
