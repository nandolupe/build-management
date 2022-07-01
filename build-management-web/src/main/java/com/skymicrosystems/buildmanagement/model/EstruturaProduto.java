package com.skymicrosystems.buildmanagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "ESTRUTURA_PRODUTO")
public class EstruturaProduto extends AuditModel {
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEstruturaProduto;
	
	private Integer numeroStep; // (Step 1, Step 2, Step 3 etc...)
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estruturaProduto")
	private Produto produto;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "EstruturaProdutoProcesso",
            joinColumns = {@JoinColumn(name = "idEstruturaProduto")},
            inverseJoinColumns = {@JoinColumn(name = "idProcesso")}
    )
	private List<Processo> processos;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "EstruturaProdutoComponente",
            joinColumns = {@JoinColumn(name = "idEstruturaProduto")},
            inverseJoinColumns = {@JoinColumn(name = "idComponente")}
    )
	private List<Componente> componentes;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@ManyToMany(mappedBy = "produtos", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Orcamento> orcamentos;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public EstruturaProduto() {}
	
	public EstruturaProduto(Long idEstruturaProduto) {
		super();
		this.idEstruturaProduto = idEstruturaProduto;
	}
	
	public EstruturaProduto(Produto produto, List<Processo> processos,
			List<Componente> componentes, Empresa empresa, Integer numeroStep) {
		super();
		this.produto = produto;
		this.processos = processos;
		this.componentes = componentes;
		this.empresa = empresa;
		this.numeroStep = numeroStep;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */

	public Long getIdEstruturaProduto() {
		return idEstruturaProduto;
	}
	public void setIdEstruturaProduto(Long idEstruturaProduto) {
		this.idEstruturaProduto = idEstruturaProduto;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public List<Processo> getProcessos() {
		return processos;
	}
	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}
	public List<Componente> getComponentes() {
		return componentes;
	}
	public void setComponentes(List<Componente> componentes) {
		this.componentes = componentes;
	}
	public Integer getNumeroStep() {
		return numeroStep;
	}
	public void setNumeroStep(Integer numeroStep) {
		this.numeroStep = numeroStep;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
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
		return "EstruturaProduto [idEstruturaProduto=" + idEstruturaProduto + ", produto=" + produto + ", processos="
				+ processos + ", componentes=" + componentes + ", numeroStep=" + numeroStep + "]";
	}
}
