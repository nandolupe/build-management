package com.skymicrosystems.buildmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.buildmanagement.model.Orcamento;

@Component
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long>, JpaSpecificationExecutor<Orcamento> {

}
