package com.skymicrosystems.buildmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.buildmanagement.model.Processo;

@Component
public interface ProcessoRepository extends JpaRepository<Processo, Long> , JpaSpecificationExecutor<Processo>{

}
