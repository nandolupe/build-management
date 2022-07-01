package com.skymicrosystems.buildmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.skymicrosystems.buildmanagement.model.Empresa;

@Component
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
