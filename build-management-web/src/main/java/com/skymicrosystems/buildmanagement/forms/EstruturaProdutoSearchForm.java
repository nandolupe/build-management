package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.EstruturaProduto;

public class EstruturaProdutoSearchForm extends DefaultSearchForm<EstruturaProduto> {

	public EstruturaProdutoSearchForm(EstruturaProduto data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
