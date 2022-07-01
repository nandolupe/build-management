package com.skymicrosystems.buildmanagement.utils;

import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.skymicrosystems.buildmanagement.enums.TipoAcessoEnum;
import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Usuario;

@Component
public class BuildManagementUtils {
	
	@Autowired
	private Environment env;
	
	public static Usuario getUserAuthenticated() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static Optional<Usuario> getUserAuthenticatedOptional() {
		return Optional.of(getUserAuthenticated());
	}
	
	public static Empresa getEmpresaAuthenticated() {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context != null && context.getAuthentication() != null) { 
		
			Usuario usuario = (Usuario) context.getAuthentication().getPrincipal();
			
			if (usuario != null && usuario.getTipoAcesso().equals(TipoAcessoEnum.EMPRESA)) {
				return usuario.getEmpresa();
			}
		}
		return null;
	}
	
	public static Boolean isAdministrador() {
		
		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return isAdministrador(usuario);
	}
	
	public static Boolean isAdministrador(Usuario usuario) {
		return usuario != null ? usuario.getTipoAcesso().equals(TipoAcessoEnum.INTERNO) : Boolean.FALSE;
	}
	
	public static String getUsername() {
		return getUserAuthenticated().getUsername();
	}
	
	public static String defaultEncoder(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}
	
	public static String getStaticMessage(String key) {
		return ResourceBundle.getBundle("messages").getString(key);
	}
	
	public Integer getPropertyInteger(String key) {
		return Integer.parseInt(env.getProperty(key));
		
	}
}
