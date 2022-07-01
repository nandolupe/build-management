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
import com.skymicrosystems.buildmanagement.model.Cliente;
import com.skymicrosystems.buildmanagement.repositories.ClienteRepository;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Service
public class ClienteServiceImpl {
	
	@Autowired
    private ClienteRepository clienteRepository;
    
    public Page<Cliente> searchPaginate(Cliente cliente, Pageable pageable) {
		
    	return clienteRepository.findAll(
				this.defaultSearchSpec(cliente, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Cliente> findById(Long id) {
    	return clienteRepository.findById(id);
	}
    
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
	}
    
    public Cliente update(Cliente cliente) {
        return clienteRepository.saveAndFlush(cliente);
	}
    
    public void delete(Long id) {
    	clienteRepository.findById(id).ifPresent(u -> clienteRepository.delete(u));
	}

	private Specification<Cliente> defaultSearchSpec(Cliente cliente, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (cliente != null) {
			
				if (StringUtils.isNotBlank(cliente.getCnpjCpf())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("cnpjCpf")), 
											"%" + cliente.getCnpjCpf().toUpperCase().trim() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(cliente.getNomeCliente())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeCliente")), 
											"%" + cliente.getNomeCliente().toUpperCase().trim() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
}
