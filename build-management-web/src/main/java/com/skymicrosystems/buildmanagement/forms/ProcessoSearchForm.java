package com.skymicrosystems.buildmanagement.forms;

import com.skymicrosystems.buildmanagement.model.Processo;

public class ProcessoSearchForm extends DefaultSearchForm<Processo> {

	public ProcessoSearchForm(Processo data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
