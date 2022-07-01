package com.skymicrosystems.buildmanagement.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.skymicrosystems.buildmanagement.enums.TipoClienteEnum;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "CLIENTE")
public class Cliente extends AuditModel {
	
	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCliente;
	
	private String nomeCliente;
	private String cnpjCpf;
	
	@Enumerated(EnumType.STRING)
	private TipoClienteEnum tipoCliente;
	
	private String status;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idEndereco")
	private Endereco endereco;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idContato")
	private Contato contato;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Cliente() {}
	
	public Cliente(Long idCliente) {
		super();
		this.idCliente = idCliente;
	}
	
	public Cliente(String nomeCliente, String cnpjCpf, 
			TipoClienteEnum tipoCliente, String status,
			Empresa empresa, Endereco endereco, Contato contato) {
		super();
		this.nomeCliente = nomeCliente;
		this.cnpjCpf = cnpjCpf;
		this.tipoCliente = tipoCliente;
		this.status = status;
		this.empresa = empresa;
		this.endereco = endereco;
		this.contato = contato;
	}
	
	public Cliente(Long idCliente, String nomeCliente, String cnpjCpf, 
			TipoClienteEnum tipoCliente, String status,
			Empresa empresa, Endereco endereco, Contato contato) {
		super();
		this.idCliente = idCliente;
		this.nomeCliente = nomeCliente;
		this.cnpjCpf = cnpjCpf;
		this.tipoCliente = tipoCliente;
		this.status = status;
		this.empresa = empresa;
		this.endereco = endereco;
		this.contato = contato;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */

	public Long getIdCliente() {
		return idCliente;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCnpjCpf() {
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}

	public TipoClienteEnum getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoClienteEnum tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
	
	/*
	 * PROCESSORS
	 */

	@PrePersist
	@Override
	protected void prePersist() {
		super.prePersist();
		if (getEmpresa() == null)
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}
	
	@PreUpdate
	@Override
	protected void preUpdate() {
		super.preUpdate();
		
		if (getEmpresa() == null) 
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}
}
