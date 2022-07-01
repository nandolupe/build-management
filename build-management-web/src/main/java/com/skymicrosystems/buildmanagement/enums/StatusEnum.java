package com.skymicrosystems.buildmanagement.enums;

public enum StatusEnum {
	
	ATIVO("ATIVO"), INATIVO("INATIVO");
	
	private StatusEnum(String name) {
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
		return new String[]{ATIVO.getName(), INATIVO.getName()};
	}
}
