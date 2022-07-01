package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Usuario;

public class UsuarioSearchForm {
	
	private Usuario usuario;
	private	Integer pageNumber; 
	private Integer size;
	
	public UsuarioSearchForm() {}
	
	public UsuarioSearchForm(Usuario usuario, Integer pageNumber, Integer size) {
		super();
		this.usuario = usuario;
		this.pageNumber = pageNumber;
		this.size = size;
	}

	public UsuarioSearchForm(Integer pageNumber, Integer size) {
		super();
		this.pageNumber = pageNumber;
		this.size = size;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
