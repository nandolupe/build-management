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

import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "COMPONENTE")
public class Componente extends AuditModel {
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idComponente;
	
	private String codigo;
	private String nomeComponente; 
	private BigDecimal largura; 
	private BigDecimal altura; 
	private BigDecimal peso; 
	private BigDecimal diametro; 
	
	private BigDecimal valorVenda; 
	
	private BigDecimal valorCusto; 
	
	private Integer quantidadeEstoque;
	private String status;
	
	@ManyToMany(mappedBy = "componentes", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<EstruturaProduto> componentesEstruturaProdutoList;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Componente() {}
	
	public Componente(Long idComponente) {
		super();
		this.idComponente = idComponente;
	}

	public Componente(String codigo, String nomeComponente, BigDecimal largura, BigDecimal altura, BigDecimal peso, BigDecimal diametro,
			BigDecimal valorVenda, BigDecimal valorCusto, Integer quantidadeEstoque, String status, Empresa empresa) {
		super();
		this.codigo = codigo;
		this.nomeComponente = nomeComponente;
		this.largura = largura;
		this.altura = altura;
		this.peso = peso;
		this.diametro = diametro;
		this.valorVenda = valorVenda;
		this.valorCusto = valorCusto;
		this.quantidadeEstoque = quantidadeEstoque;
		this.status = status;
		this.empresa = empresa;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Long getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Long idComponente) {
		this.idComponente = idComponente;
	}
	public String getNomeComponente() {
		return nomeComponente;
	}
	public void setNomeComponente(String nomeComponente) {
		this.nomeComponente = nomeComponente;
	}
	public BigDecimal getLargura() {
		return largura;
	}
	public void setLargura(BigDecimal largura) {
		this.largura = largura;
	}
	public BigDecimal getAltura() {
		return altura;
	}
	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}
	public BigDecimal getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public BigDecimal getDiametro() {
		return diametro;
	}
	public void setDiametro(BigDecimal diametro) {
		this.diametro = diametro;
	}
	public BigDecimal getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}
	public BigDecimal getValorCusto() {
		return valorCusto;
	}
	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}
	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}
	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<EstruturaProduto> getComponentesEstruturaProdutoList() {
		return componentesEstruturaProdutoList;
	}

	public void setComponentesEstruturaProdutoList(List<EstruturaProduto> componentesEstruturaProdutoList) {
		this.componentesEstruturaProdutoList = componentesEstruturaProdutoList;
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
