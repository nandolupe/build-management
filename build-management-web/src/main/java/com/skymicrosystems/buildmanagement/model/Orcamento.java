package com.skymicrosystems.buildmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "ORCAMENTO")
public class Orcamento extends AuditModel {
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrcamento;
	
	private String codigo;
	
	private LocalDate dataEmissao;
	
	private LocalDate dataExpiracao;
	
	private BigDecimal percentualDesconto;
	
	private BigDecimal valorDesconto;
	
	private BigDecimal valorTotalVenda;
	
	private BigDecimal valorTotalCusto;
	
	private BigDecimal valorTaxaEntrega;
	
	private String status;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idEndereco")
	private Endereco enderecoEntrega;
	
	@ManyToOne
    @JoinColumn(name="idCliente", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "OrcamentoEstruturaProduto",
            joinColumns = {@JoinColumn(name = "idOrcamento")},
            inverseJoinColumns = {@JoinColumn(name = "idEstruturaProduto")}
    )
	private List<EstruturaProduto> produtos;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Orcamento() {}
	
	public Orcamento(Long idOrcamento) {
		super();
		this.idOrcamento = idOrcamento;
	}

	public Orcamento(String codigo, LocalDate dataEmissao, LocalDate dataExpiracao, BigDecimal percentualDesconto,
			BigDecimal valorDesconto, BigDecimal valorTotalVenda, BigDecimal valorTotalCusto,
			BigDecimal valorTaxaEntrega, String status, Endereco enderecoEntrega, Cliente cliente, Empresa empresa,
			List<EstruturaProduto> produtos) {
		super();
		this.codigo = codigo;
		this.dataEmissao = dataEmissao;
		this.dataExpiracao = dataExpiracao;
		this.percentualDesconto = percentualDesconto;
		this.valorDesconto = valorDesconto;
		this.valorTotalVenda = valorTotalVenda;
		this.valorTotalCusto = valorTotalCusto;
		this.valorTaxaEntrega = valorTaxaEntrega;
		this.status = status;
		this.enderecoEntrega = enderecoEntrega;
		this.cliente = cliente;
		this.empresa = empresa;
		this.produtos = produtos;
	}

	/*
	 * GETTERS AND SETTERS
	 */
	
	public Long getIdOrcamento() {
		return idOrcamento;
	}

	public BigDecimal getValorTotalCusto() {
		return valorTotalCusto;
	}

	public void setValorTotalCusto(BigDecimal valorTotalCusto) {
		this.valorTotalCusto = valorTotalCusto;
	}

	public void setIdOrcamento(Long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public LocalDate getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(LocalDate dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorTotalVenda() {
		return valorTotalVenda;
	}

	public void setValorTotalVenda(BigDecimal valorTotalVenda) {
		this.valorTotalVenda = valorTotalVenda;
	}

	public BigDecimal getValorTaxaEntrega() {
		return valorTaxaEntrega;
	}

	public void setValorTaxaEntrega(BigDecimal valorTaxaEntrega) {
		this.valorTaxaEntrega = valorTaxaEntrega;
	}

	public Endereco getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(Endereco enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<EstruturaProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<EstruturaProduto> produtos) {
		this.produtos = produtos;
	}
	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
