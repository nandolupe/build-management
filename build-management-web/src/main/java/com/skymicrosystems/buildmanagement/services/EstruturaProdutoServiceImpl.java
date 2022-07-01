package com.skymicrosystems.buildmanagement.services;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.EstruturaProduto;
import com.skymicrosystems.buildmanagement.repositories.EstruturaProdutoRepository;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Service
public class EstruturaProdutoServiceImpl {
	
	private final static Logger logger = LoggerFactory.getLogger(EstruturaProdutoServiceImpl.class);
	
	@Autowired
    private EstruturaProdutoRepository estruturaProdutoRepository;
    
    public Page<EstruturaProduto> searchPaginate(EstruturaProduto estruturaProduto, Pageable pageable) {
		
    	return estruturaProdutoRepository.findAll(
				this.defaultSearchSpec(estruturaProduto, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<EstruturaProduto> findById(Long id) {
    	return estruturaProdutoRepository.findById(id);
	}
    
    public EstruturaProduto save(EstruturaProduto estruturaProduto) {
    	this.calculatingValues(estruturaProduto);
        return estruturaProdutoRepository.save(estruturaProduto);
	}
    
    public EstruturaProduto update(EstruturaProduto estruturaProduto) {
    	this.calculatingValues(estruturaProduto);
        return estruturaProdutoRepository.saveAndFlush(estruturaProduto);
	}
    
    public void delete(Long id) {
    	estruturaProdutoRepository.findById(id).ifPresent(u -> estruturaProdutoRepository.delete(u));
	}
    
    private void calculatingValues(EstruturaProduto estruturaProduto) {
    	BigDecimal valorTotalCustoProcessos = 
    			estruturaProduto
    			.getProcessos()
    			.stream()
    			.map(x -> x != null ? x.getValorCusto() : BigDecimal.ZERO)
    			.reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	BigDecimal valorTotalVendaProcessos = 
    			estruturaProduto
    			.getProcessos()
    			.stream()
    			.map(x -> x != null ? x.getValorVenda() : BigDecimal.ZERO)
    			.reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	BigDecimal valorTotalVendaComponentes = 
    			estruturaProduto
    			.getComponentes()
    			.stream() 
    			.map(x -> x != null ? x.getValorVenda() : BigDecimal.ZERO)
    			.reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	BigDecimal valorTotalCustoComponentes = 
    			estruturaProduto
    			.getComponentes()
    			.stream()
    			.map(x -> x != null ? x.getValorCusto() : BigDecimal.ZERO)
    			.reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	estruturaProduto.getProduto().setValorCusto(valorTotalCustoProcessos.add(valorTotalCustoComponentes));
    	estruturaProduto.getProduto().setValorVenda(valorTotalVendaProcessos.add(valorTotalVendaComponentes));
    }

	private Specification<EstruturaProduto> defaultSearchSpec(EstruturaProduto estruturaProduto, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			/*
			if (estruturaProduto != null && estruturaProduto.getProduto() != null) {
			
				if (StringUtils.isNotBlank(estruturaProduto.getProduto().getCodigo())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("produto.codigo")), 
											"%" + estruturaProduto.getProduto().getCodigo().toUpperCase().trim() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(estruturaProduto.getProduto().getNomeProduto())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("produto.nomeProduto")), 
											"%" + estruturaProduto.getProduto().getNomeProduto().toUpperCase().trim() + "%"), 
									predicate);
				}
			}*/
			return predicate;
		};
	}
}
