package com.skymicrosystems.buildmanagement.model;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;

import com.skymicrosystems.buildmanagement.repositories.IgnoreUppercaseTransform;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@MappedSuperclass
public class AuditModel {
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime dtCriacao;
	
	@Column(nullable = false, updatable = false)
	private String criadoPor;
	
	private LocalDateTime dtAlteracao;
	private String alteradoPor;
	
	public LocalDateTime getDtCriacao() {
		return dtCriacao;
	}
	public void setDtCriacao(LocalDateTime dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	public LocalDateTime getDtAlteracao() {
		return dtAlteracao;
	}
	public void setDtAlteracao(LocalDateTime dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	public String getCriadoPor() {
		return criadoPor;
	}
	public void setCriadoPor(String criadoPor) {
		this.criadoPor = criadoPor;
	}
	public String getAlteradoPor() {
		return alteradoPor;
	}
	public void setAlteradoPor(String alteradoPor) {
		this.alteradoPor = alteradoPor;
	}
	
	@PrePersist
    protected void prePersist(){
		if (StringUtils.isBlank(this.getCriadoPor())) {
			this.setCriadoPor(BuildManagementUtils.getUsername());
			this.setDtCriacao(LocalDateTime.now());
		}
		
		this.uppercaseFields();
    }
	
	@PreUpdate
	protected void preUpdate(){
		if (StringUtils.isBlank(this.getCriadoPor())) {
			this.setAlteradoPor(BuildManagementUtils.getUsername());
			this.setDtAlteracao(LocalDateTime.now());
		}
		
		this.uppercaseFields();
    }
	
	private void uppercaseFields() {
		  Class<? extends AuditModel> clazz = this.getClass();
		  List<Field> fields = new ArrayList<>();
		  Class<?> superClazz = clazz;
		  while (!superClazz.equals(AuditModel.class)) {
		    fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));
		    superClazz = superClazz.getSuperclass();
		  }
		  fields.forEach(this::transformFieldToUppercase);
		}
		private void transformFieldToUppercase(Field field) {
		  if (!field.getType().equals(String.class)) {
		    return;
		  }
		  // Pay attention to this part!
		  if (field.getAnnotation(IgnoreUppercaseTransform.class) != null) {
		    return;
		  }
		  try {
		    Boolean unsetAccessible = false;
		    if (!field.isAccessible()) {
		      unsetAccessible = true;
		      field.setAccessible(true);
		    }
		  String entityFieldValue = (String) field.get(this);
		    if (entityFieldValue != null) {
		      field.set(this, entityFieldValue.toUpperCase());
		    }
		  if (unsetAccessible) {
		      field.setAccessible(false);
		    }
		  } catch (IllegalAccessException ex) {
		    // We set the field to accessible so field.get() should never throw this
		  }
		}
}
