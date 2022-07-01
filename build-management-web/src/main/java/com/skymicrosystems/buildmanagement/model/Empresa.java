package com.skymicrosystems.buildmanagement.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "EMPRESA")
public class Empresa extends AuditModel {
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEmpresa;
	
	private String cnpjCpf;
	private String razaoSocial;
	private String nomeFantasia;
	
	@Column(columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataInicioContrato;
	
	@Column(columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataFimContrato;
	
	private String status;
	
	@OneToMany(mappedBy="empresa")
	private List<Processo> processos;
	
	@OneToMany(mappedBy="empresa")
	private List<Componente> componentes;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idEndereco")
	private Endereco endereco;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idContato")
	private Contato contato;
	
	@OneToOne(mappedBy = "empresa", optional = true)
	@JoinColumn(name = "id")
	private Usuario usuario;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Empresa() {	}
	
	public Empresa(Long idEmpresa) {
		super();
		this.idEmpresa = idEmpresa;
	}

	public Empresa(String cnpjCpf, String razaoSocial, String nomeFantasia,
			LocalDate dataInicioContrato, LocalDate dataFimContrato, String status, Endereco endereco, Contato contato,
			Usuario usuario) {
		super();
		this.cnpjCpf = cnpjCpf;
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.dataInicioContrato = dataInicioContrato;
		this.dataFimContrato = dataFimContrato;
		this.status = status;
		this.endereco = endereco;
		this.contato = contato;
		this.usuario = usuario;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getCnpjCpf() {
		return cnpjCpf;
	}
	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public LocalDate getDataInicioContrato() {
		return dataInicioContrato;
	}
	public void setDataInicioContrato(LocalDate dataInicioContrato) {
		this.dataInicioContrato = dataInicioContrato;
	}
	public LocalDate getDataFimContrato() {
		return dataFimContrato;
	}
	public void setDataFimContrato(LocalDate dataFimContrato) {
		this.dataFimContrato = dataFimContrato;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
