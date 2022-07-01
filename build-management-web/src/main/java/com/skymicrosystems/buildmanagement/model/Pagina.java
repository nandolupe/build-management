package com.skymicrosystems.buildmanagement.model;

public class Pagina {

	private String nome;
	private String url;
	private String permissions;
	
	public Pagina() {}
	
	public Pagina(String nome, String url, String permissions) {
		super();
		this.nome = nome;
		this.url = url;
		this.permissions = permissions;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
}
