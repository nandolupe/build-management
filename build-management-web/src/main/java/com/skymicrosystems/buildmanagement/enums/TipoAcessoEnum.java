package com.skymicrosystems.buildmanagement.enums;

public enum TipoAcessoEnum {
	
	INTERNO("INTERNO"), EMPRESA("EMPRESA");
	
	private TipoAcessoEnum(String name) {
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String[] toList() {
		return new String[]{INTERNO.getName(), EMPRESA.getName()};
	}
}
