package com.skymicrosystems.buildmanagement.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Usuario;
import com.skymicrosystems.buildmanagement.repositories.EmpresaRepository;

@Service
public class EmpresaServiceImpl {
	
	@Autowired
    private EmpresaRepository empresaRepository;
	
	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl;
    
    public Page<Empresa> searchPaginate(Empresa empresa, Pageable pageable) {
    	
		return empresaRepository.findAll(pageable);
	}
    
    public Optional<Empresa> findById(Long id) {
    	return empresaRepository.findById(id);
	}
    
    public Empresa save(Empresa empresa) {
    	
    	Usuario usuario = empresa.getUsuario();
    	empresa.setUsuario(null);

    	Empresa empresaSaved = empresaRepository.save(empresa);
    	
    	usuarioServiceImpl.saveEmpresaPrincipal(usuario, empresaSaved);
    	
        return empresaSaved;
	}
    
    public Empresa update(Empresa empresa) {
        return empresaRepository.saveAndFlush(empresa);
	}
    
    public void delete(Long id) {
    	empresaRepository.findById(id).ifPresent(u -> empresaRepository.delete(u));
	}
	
}
