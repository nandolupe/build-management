package com.skymicrosystems.buildmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.buildmanagement.model.EstruturaProduto;

@Component
public interface EstruturaProdutoRepository extends JpaRepository<EstruturaProduto, Long>, JpaSpecificationExecutor<EstruturaProduto> {

}
