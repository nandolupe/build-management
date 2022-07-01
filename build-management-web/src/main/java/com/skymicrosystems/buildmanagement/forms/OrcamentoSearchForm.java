package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Orcamento;

public class OrcamentoSearchForm extends DefaultSearchForm<Orcamento> {

	public OrcamentoSearchForm(Orcamento data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
