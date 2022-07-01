package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Papel;

public class PapelSearchForm extends DefaultSearchForm<Papel> {

	public PapelSearchForm(Papel data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
