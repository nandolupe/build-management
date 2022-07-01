package com.skymicrosystems.buildmanagement.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Usuario;

@Repository
@Transactional(readOnly = true)
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
	public Optional<Usuario> findByUsername(String username);
	
	@Query("SELECT u FROM Usuario u, Empresa e WHERE (:username is not null and u.username LIKE %:username%) and (:name is not null and u.name LIKE %:name% ) and (:empresa is null or e = u.empresa and u.empresa = :empresa)" )
	public Page<Usuario> defaultSearch(
			@Param("username") String username, 
			@Param("name") String name, 
			@Param("empresa") Empresa empresa, Pageable pageable);

}
