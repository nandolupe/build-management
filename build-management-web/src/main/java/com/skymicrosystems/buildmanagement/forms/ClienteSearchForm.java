package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Cliente;

public class ClienteSearchForm extends DefaultSearchForm<Cliente> {

	public ClienteSearchForm(Cliente data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
