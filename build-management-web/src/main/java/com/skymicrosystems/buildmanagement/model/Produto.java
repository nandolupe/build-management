package com.skymicrosystems.buildmanagement.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUTO")
public class Produto extends AuditModel {
	
	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProduto;

	private String codigo;
	private String nomeProduto;
	
	@Column(scale = 2)
	private BigDecimal valorCusto;
	
	@Column(scale = 2)
	private BigDecimal valorVenda;
	
	private String status;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idEstruturaProduto")
	private EstruturaProduto estruturaProduto;
	
	/*
	 * CONSTRUCTORS
	 */
	public Produto() {}
	
	public Produto(Long idProduto) {
		super();
		this.idProduto = idProduto;
	}

	public Produto(String codigo, String nomeProduto, BigDecimal valorCusto, BigDecimal valorVenda, String status,
			EstruturaProduto estruturaProduto) {
		super();
		this.codigo = codigo;
		this.nomeProduto = nomeProduto;
		this.valorCusto = valorCusto;
		this.valorVenda = valorVenda;
		this.status = status;
		this.estruturaProduto = estruturaProduto;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	public Long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
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
	public EstruturaProduto getEstruturaProduto() {
		return estruturaProduto;
	}
	public void setEstruturaProduto(EstruturaProduto estruturaProduto) {
		this.estruturaProduto = estruturaProduto;
	}
	
}
