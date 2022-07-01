package com.skymicrosystems.buildmanagement.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Processo;
import com.skymicrosystems.buildmanagement.repositories.ProcessoRepository;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Service
public class ProcessoServiceImpl {
	
	@Autowired
    private ProcessoRepository processoRepository;
    
    public Page<Processo> searchPaginate(Processo processo, Pageable pageable) {
		
    	return processoRepository.findAll(
				this.defaultSearchSpec(processo, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Processo> findById(Long id) {
    	return processoRepository.findById(id);
	}
    
    public Processo save(Processo processo) {
        return processoRepository.save(processo);
	}
    
    public Processo update(Processo processo) {
        return processoRepository.saveAndFlush(processo);
	}
    
    public void delete(Long id) {
    	processoRepository.findById(id).ifPresent(u -> processoRepository.delete(u));
	}

	private Specification<Processo> defaultSearchSpec(Processo processo, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (processo != null) {
			
				if (StringUtils.isNotBlank(processo.getCodigo())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("codigo")), 
											"%" + processo.getCodigo().toUpperCase() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(processo.getNomeProcesso())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeProcesso")), 
											"%" + processo.getNomeProcesso().toUpperCase() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
}
