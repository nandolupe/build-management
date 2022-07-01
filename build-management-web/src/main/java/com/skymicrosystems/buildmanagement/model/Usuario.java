package com.skymicrosystems.buildmanagement.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.skymicrosystems.buildmanagement.enums.TipoAcessoEnum;
import com.skymicrosystems.buildmanagement.repositories.IgnoreUppercaseTransform;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "USUARIO")
public class Usuario extends AuditModel implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * FIELDS
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoAcessoEnum tipoAcesso; // EMPRESA, INTERNO
	
	private Integer dddCelular;
	private Integer numeroCelular;
	private String codigoEsqueciSenha;
	private String name;
	
	@IgnoreUppercaseTransform
	@Column(nullable = false, updatable = false)
    private String username;
    private String email;
    
    @IgnoreUppercaseTransform
    private String password;
    private Boolean locked;
    private Boolean enabled;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "UsuarioRole",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "idPapel")}
    )
    private List<Papel> papeis;
    
    @OneToOne(cascade = CascadeType.DETACH, optional = true)
    @JoinColumn(name = "idEmpresa")
    private Empresa empresa;
    
    /*
	 * CONSTRUCTORS
	 */
    
    public Usuario() {}
    
	public Usuario(Boolean locked, Boolean enabled) {
		super();
		this.locked = locked;
		this.enabled = enabled;
	}
	
	public Usuario(Long id) {
		super();
		this.id = id;
	}

	public Usuario(TipoAcessoEnum tipoAcesso, Integer dddCelular,
			Integer numeroCelular, String codigoEsqueciSenha, String name, String username, String email,
			String password, Boolean locked, Boolean enabled, List<Papel> papeis, Empresa empresa) {
		super();
		this.tipoAcesso = tipoAcesso;
		this.dddCelular = dddCelular;
		this.numeroCelular = numeroCelular;
		this.codigoEsqueciSenha = codigoEsqueciSenha;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.locked = locked;
		this.enabled = enabled;
		this.papeis = papeis;
		this.empresa = empresa;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public List<Papel> getPapeis() {
		return papeis;
	}
	public void setPapeis(List<Papel> papeis) {
		this.papeis = papeis;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public TipoAcessoEnum getTipoAcesso() {
		return tipoAcesso;
	}
	public void setTipoAcesso(TipoAcessoEnum tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getDddCelular() {
		return dddCelular;
	}
	public void setDddCelular(Integer dddCelular) {
		this.dddCelular = dddCelular;
	}
	public Integer getNumeroCelular() {
		return numeroCelular;
	}
	public void setNumeroCelular(Integer numeroCelular) {
		this.numeroCelular = numeroCelular;
	}
	public String getCodigoEsqueciSenha() {
		return codigoEsqueciSenha;
	}
	public void setCodigoEsqueciSenha(String codigoEsqueciSenha) {
		this.codigoEsqueciSenha = codigoEsqueciSenha;
	}
	
	@Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<SimpleGrantedAuthority> listAuthorities = new ArrayList<>();
		
		if (this.papeis != null && this.papeis.size() > 0) {
			for (Papel papel : this.papeis) {
				listAuthorities.add(new SimpleGrantedAuthority(papel.getNome()));
			}
		}
        
        return listAuthorities;
    }
	
	/*
	 * PROCESSORS
	 */
	
	@PrePersist
	@Override
	protected void prePersist() {
		super.prePersist();
		
		this.setPassword(BuildManagementUtils.defaultEncoder(this.getPassword()));	
		
		if (getEmpresa() == null) {
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
			this.setTipoAcesso(TipoAcessoEnum.EMPRESA);
		}
	}
	
	@PreUpdate
	@Override
	protected void preUpdate() {
		super.preUpdate();
		
		this.setPassword(BuildManagementUtils.defaultEncoder(this.getPassword()));	
		
		if (getEmpresa() == null) {
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
			this.setTipoAcesso(TipoAcessoEnum.EMPRESA);
		}
	}
	
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", tipoAcesso=" + tipoAcesso + ", dddCelular=" + dddCelular + ", numeroCelular="
				+ numeroCelular + ", codigoEsqueciSenha=" + codigoEsqueciSenha + ", name=" + name + ", username="
				+ username + ", email=" + email + ", password=" + password + ", locked=" + locked + ", enabled="
				+ enabled + ", papeis=" + papeis + ", empresa=" + empresa + "]";
	}
	
	public Boolean isAdministrador () {
		return tipoAcesso.equals(TipoAcessoEnum.INTERNO);
	}
}
