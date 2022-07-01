package com.skymicrosystems.buildmanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTATO")
public class Contato extends AuditModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContato;
	
	private String nomeContato;
	private String celular;
	private String telefone;
	private String email;
	
	@OneToOne(mappedBy = "contato")
	private Empresa empresa;
	
	@OneToOne(mappedBy = "contato")
	private Cliente cliente;
	
	public Contato() {}
	
	public Contato(Long idContato) {
		super();
		this.idContato = idContato;
	}

	public Contato(String nomeContato, String celular, String telefone, String email) {
		super();
		this.nomeContato = nomeContato;
		this.celular = celular;
		this.telefone = telefone;
		this.email = email;
	}

	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Long getIdContato() {
		return idContato;
	}
	public void setIdContato(Long idContato) {
		this.idContato = idContato;
	}
	public String getNomeContato() {
		return nomeContato;
	}
	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}
