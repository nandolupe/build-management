package com.skymicrosystems.buildmanagement.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "PROCESSO")
public class Processo extends AuditModel {
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProcesso;
	
	@NotBlank
	private String codigo;
	
	@NotBlank
	private String nomeProcesso;
	
	private Long tempoExecucao; 
	private BigDecimal valorCusto;
	private BigDecimal valorVenda;
	private String status;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@ManyToMany(mappedBy = "processos", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<EstruturaProduto> processosEstruturaProdutoList;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Processo() {}
	
	public Processo(Long idProcesso) {
		super();
		this.idProcesso = idProcesso;
	}

	public Processo(String codigo, String nomeProcesso, Long tempoExecucao, BigDecimal valorCusto, BigDecimal valorVenda, String status, Empresa empresa) {
		super();
		this.codigo = codigo;
		this.nomeProcesso = nomeProcesso;
		this.tempoExecucao = tempoExecucao;
		this.valorCusto = valorCusto;
		this.valorVenda = valorVenda;
		this.status = status;
		this.empresa = empresa;
	}

	/*
	 * GETTERS AND SETTERS
	 */
	
	public Long getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public String getNomeProcesso() {
		return nomeProcesso;
	}
	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}
	public Long getTempoExecucao() {
		return tempoExecucao;
	}
	public void setTempoExecucao(Long tempoExecucao) {
		this.tempoExecucao = tempoExecucao;
	}
	public BigDecimal getValorCusto() {
		return valorCusto;
	}
	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}
	public BigDecimal getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<EstruturaProduto> getProcessosEstruturaProdutoList() {
		return processosEstruturaProdutoList;
	}

	public void setProcessosEstruturaProdutoList(List<EstruturaProduto> processosEstruturaProdutoList) {
		this.processosEstruturaProdutoList = processosEstruturaProdutoList;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
	
	@Override
	public String toString() {
		return "Processo [idProcesso=" + idProcesso + ", nomeProcesso=" + nomeProcesso + ", tempoExecucao="
				+ tempoExecucao + ", valorCusto=" + valorCusto + ", status=" + status + "]";
	}
}
