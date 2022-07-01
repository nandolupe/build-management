package com.skymicrosystems.buildmanagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.skymicrosystems.buildmanagement.enums.StatusEnum;
import com.skymicrosystems.buildmanagement.enums.TipoAcessoEnum;

@Entity
@Table(name = "PAPEL")
public class Papel extends AuditModel {
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPapel;
	
	private String nome;
	
	@NotBlank(message = "A Descrição Resumida deve ser informada!")
	private String descricaoResumida;
	
	@NotBlank(message = "A Descrição Completa deve ser informada!")
	private String descricaoCompleta;
	
	@Enumerated(EnumType.STRING)
	private TipoAcessoEnum tipoAcesso; // EMPRESA, INTERNO
	
	@Enumerated(EnumType.STRING)
	private StatusEnum status;
	
	@ManyToMany(mappedBy = "papeis", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Usuario> usuarios;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Papel() {}
	
	public Papel(Long idPapel) {
		super();
		this.idPapel = idPapel;
	}

	public Papel(String nome, String descricaoResumida, String descricaoCompleta, TipoAcessoEnum tipoAcesso, StatusEnum status) {
		this.nome = nome;
		this.descricaoResumida = descricaoResumida;
		this.descricaoCompleta = descricaoCompleta;
		this.tipoAcesso = tipoAcesso;
		this.status = status;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public Long getIdPapel() {
		return idPapel;
	}
	public void setIdPapel(Long idPapel) {
		this.idPapel = idPapel;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public TipoAcessoEnum getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(TipoAcessoEnum tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	public String getDescricaoResumida() {
		return descricaoResumida;
	}

	public void setDescricaoResumida(String descricaoResumida) {
		this.descricaoResumida = descricaoResumida;
	}

	public String getDescricaoCompleta() {
		return descricaoCompleta;
	}

	public void setDescricaoCompleta(String descricaoCompleta) {
		this.descricaoCompleta = descricaoCompleta;
	}
}