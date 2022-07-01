package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Componente;

public class ComponenteSearchForm extends DefaultSearchForm<Componente> {

	public ComponenteSearchForm(Componente data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
