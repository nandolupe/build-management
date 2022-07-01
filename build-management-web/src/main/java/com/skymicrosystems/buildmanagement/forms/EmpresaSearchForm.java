package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Empresa;

public class EmpresaSearchForm extends DefaultSearchForm<Empresa> {

	public EmpresaSearchForm(Empresa data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
