package com.skymicrosystems.buildmanagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.buildmanagement.model.Componente;
import com.skymicrosystems.buildmanagement.model.Empresa;

@Component
public interface ComponenteRepository extends JpaRepository<Componente, Long>, JpaSpecificationExecutor<Componente> {
	public Optional<Componente> findByCodigoAndEmpresa(String codigo, Empresa empresa);
}
