package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.AuditModel;

public class DefaultSearchForm<T extends AuditModel>   {
	
	private T data;
	private	Integer pageNumber; 
	private Integer size;
	
	public DefaultSearchForm() {}
	
	
	public <E extends AuditModel> DefaultSearchForm(E element, Integer pageNumber, Integer size) {
		super();
		this.data = (T) element;
		this.pageNumber = pageNumber;
		this.size = size;
	}
	
	public DefaultSearchForm(Integer pageNumber, Integer size) {
		super();
		this.pageNumber = pageNumber;
		this.size = size;
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	
}
