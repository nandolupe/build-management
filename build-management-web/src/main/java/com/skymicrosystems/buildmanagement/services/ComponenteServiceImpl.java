package com.skymicrosystems.buildmanagement.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.buildmanagement.exceptions.CodigoDuplicadoException;
import com.skymicrosystems.buildmanagement.model.Componente;
import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.repositories.ComponenteRepository;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Service
public class ComponenteServiceImpl {
	
	@Autowired
    private ComponenteRepository componenteRepository;
    
    public Page<Componente> searchPaginate(Componente componente, Pageable pageable) {
		
    	return componenteRepository.findAll(
				this.defaultSearchSpec(componente, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Componente> findById(Long id) {
    	return componenteRepository.findById(id);
	}
    
    public Componente save(Componente componente) throws CodigoDuplicadoException {
    	
    	this.validationDuplicated(componente);
    	
   		return componenteRepository.save(componente);
	}
    
    public Componente update(Componente componente) throws CodigoDuplicadoException {

    	this.validationDuplicated(componente);
    	
        return componenteRepository.saveAndFlush(componente);
	}
    
    public void delete(Long id) {
    	componenteRepository.findById(id).ifPresent(u -> componenteRepository.delete(u));
	}
    
    private Boolean validationDuplicated(Componente componente) throws CodigoDuplicadoException {
    	
    	Optional<Componente> findByCodigoAndEmpresa = componenteRepository.findByCodigoAndEmpresa(componente.getCodigo(), BuildManagementUtils.getEmpresaAuthenticated());
    	
    	if (findByCodigoAndEmpresa.isPresent()) {
	    	if (findByCodigoAndEmpresa.get() != null) {
	    		if(!findByCodigoAndEmpresa.get().getIdComponente().equals(componente.getIdComponente())) {
	    			throw new CodigoDuplicadoException("O código do Componente nao pode ser duplicado!");
	    		}
	    	}
    	}
    	return Boolean.FALSE;
    }
    
	private Specification<Componente> defaultSearchSpec(Componente componente, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (componente != null) {
			
				if (StringUtils.isNotBlank(componente.getCodigo())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("codigo")), 
											"%" + componente.getCodigo().toUpperCase() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(componente.getNomeComponente())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeComponente")), 
											"%" + componente.getNomeComponente().toUpperCase() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
}
