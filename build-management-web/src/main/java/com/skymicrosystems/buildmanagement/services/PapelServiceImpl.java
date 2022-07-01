package com.skymicrosystems.buildmanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skymicrosystems.buildmanagement.model.Papel;
import com.skymicrosystems.buildmanagement.repositories.PapelRepository;

@Service
public class PapelServiceImpl {
	
	@Autowired
    private PapelRepository papelRepository;
    
    public Page<Papel> searchPaginate(Papel papel, Pageable pageable) {
    	
		return papelRepository.findAll(pageable);
	}
    
    public Optional<Papel> findById(Long id) {
    	return papelRepository.findById(id);
	}
    
    public List<Papel> findByNome(String nome) {
    	return papelRepository.findByNome(nome);
	}
    
    public Papel save(Papel papel) {
        return papelRepository.save(papel);
	}
    
    public Papel update(Papel papel) {
        return papelRepository.saveAndFlush(papel);
	}
    
    public void delete(Long id) {
    	papelRepository.findById(id).ifPresent(u -> papelRepository.delete(u));
	}
	
}
