package com.skymicrosystems.buildmanagement.enums;

public enum TipoClienteEnum {
	
	PF("PF"), PJ("PJ");
	
	private TipoClienteEnum(String name) {
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
		return new String[]{PF.getName(), PJ.getName()};
	}
}
