package com.skymicrosystems.buildmanagement.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.skymicrosystems.buildmanagement.enums.StatusEnum;
import com.skymicrosystems.buildmanagement.enums.TipoAcessoEnum;
import com.skymicrosystems.buildmanagement.enums.TipoClienteEnum;
import com.skymicrosystems.buildmanagement.model.Cliente;
import com.skymicrosystems.buildmanagement.model.Componente;
import com.skymicrosystems.buildmanagement.model.Contato;
import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Endereco;
import com.skymicrosystems.buildmanagement.model.Papel;
import com.skymicrosystems.buildmanagement.model.Processo;
import com.skymicrosystems.buildmanagement.model.Usuario;
import com.skymicrosystems.buildmanagement.repositories.ClienteRepository;
import com.skymicrosystems.buildmanagement.repositories.ComponenteRepository;
import com.skymicrosystems.buildmanagement.repositories.EmpresaRepository;
import com.skymicrosystems.buildmanagement.repositories.PapelRepository;
import com.skymicrosystems.buildmanagement.repositories.ProcessoRepository;
import com.skymicrosystems.buildmanagement.repositories.UsuarioRepository;

@Service
public class DatabaseInitializingServiceImpl {
	
	private final static Logger logger = LoggerFactory.getLogger(DatabaseInitializingServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ComponenteRepository componenteRepository;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public void initialEssentialDataBase() {
		
		logger.info("######################################################################");
		logger.info("################## INITIALINZING ESSENTIAL DATABASE ##################");
		logger.info("######################################################################");
		
		logger.info("Creating initial datas for Papel...");
		
		Papel papel = new Papel("ROLE_INTERNO_ADMIN", "ADMINISTRATIVO", "PERMISSAO COM TODOS OS ACESSOS LIBERADOS PARA TODO SISTEMA", TipoAcessoEnum.INTERNO, StatusEnum.ATIVO);
		papel.setCriadoPor(env.getProperty("default.system.username"));
		papel.setDtCriacao(LocalDateTime.now());
		Papel INTERNO_ADMIN_SAVED = papelRepository.save(papel);
		
		papel = new Papel("ROLE_EMPRESA_ADMIN", "ADMINISTRATIVO EMPRESA", "PERMISSAO COM TODOS OS ACESSOS LIBERADOS PARA EMPRESA" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
		papel.setCriadoPor(env.getProperty("default.system.username"));
		papel.setDtCriacao(LocalDateTime.now());
		
		papelRepository.save(papel);
		
		logger.info("Creating initial datas for Usuario...");
		
		Usuario usuario = new Usuario(
				TipoAcessoEnum.INTERNO, 
				null, 
				null, 
				null, 
				env.getProperty("default.system.user.administrator.name"), 
				env.getProperty("default.system.user.administrator.login"), 
				env.getProperty("default.system.user.administrator.email"), 
				env.getProperty("default.system.user.administrator.password"), 
				Boolean.parseBoolean(env.getProperty("default.system.user.administrator.locked")), 
				Boolean.parseBoolean(env.getProperty("default.system.user.administrator.enabled")), 
				Collections.singletonList(INTERNO_ADMIN_SAVED), 
				null);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		usuarioRepository.save(usuario);
		
		usuario = new Usuario(
				TipoAcessoEnum.INTERNO, 
				null, 
				null, 
				null, 
				"User 1", 
				"user1", 
				"administrador@test.com", 
				"1234", 
				Boolean.FALSE, 
				Boolean.TRUE, 
				Collections.singletonList(INTERNO_ADMIN_SAVED), 
				null);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		usuarioRepository.save(usuario);
		
		logger.info("######################################################################");
		logger.info("############# FINISH INITIALINZING ESSENTIAL DATABASE ################");
		logger.info("######################################################################");
		
	}
	
	public void initialMockDataBase() {
		
		logger.info("######################################################################");
		logger.info("################## INITIALINZING MOCK DATABASE #######################");
		logger.info("######################################################################");
		
		logger.info("Creating mock datas for Empresa e Acessos...");
		
		List<Papel> papeisEmpresa = papelRepository.findByNome("ROLE_EMPRESA_ADMIN");
		
		this.criarEmpresa("EMPRESA TESTE 1 LTDA", "21.111.111/0001-80", "empresa1", "1234", papeisEmpresa);
		
		this.criarEmpresa("EMPRESA TESTE 2 LTDA", "20.000.000/0001-90", "empresa2", "1234", papeisEmpresa);
		
		logger.info("######################################################################");
		logger.info("############### FINISH INITIALINZING MOCK DATABASE ###################");
		logger.info("######################################################################");
	}
	
	private void criarEmpresa(String nome, String cnpj, String login, String senha,  List<Papel> papel) {
		
		logger.info("Creating mock datas for Empresa e Acessos...");
		
		Endereco endereco = new Endereco("Av. Paulista", "125", "Bela Vista", "São Paulo", "SP", "00000-555", null);
		endereco.setDtCriacao(LocalDateTime.now());
		endereco.setCriadoPor(env.getProperty("default.system.username"));
		
		Contato contato = new Contato("Teste", "(11) 87980-7879", "(11) 8798-7879", "teste@teste.com");
		contato.setDtCriacao(LocalDateTime.now());
		contato.setCriadoPor(env.getProperty("default.system.username"));
		
		Empresa empresaSave = this.criarEmpresa(nome, cnpj, endereco, contato);
		
		this.criarUsuario(nome, login, senha, papel, empresaSave);
		
		logger.info("Creating mock datas for Componentes...");
		
		this.criarComponente("C01", 
				"Componente Teste 1", 
				new BigDecimal("1"),
				new BigDecimal("1"),
				new BigDecimal("1"),
				new BigDecimal("1"), 
				new BigDecimal("10.00"), 
				new BigDecimal("15.00"), 
				1, 
				"ATIVO",
				empresaSave);
		
		this.criarComponente(
				"C02", 
				"Componente Teste 2", 
				new BigDecimal("2"),
				new BigDecimal("2"),
				new BigDecimal("2"),
				new BigDecimal("2"), 
				new BigDecimal("20.00"), 
				new BigDecimal("40.00"), 
				2, 
				"ATIVO", 
				empresaSave);
		
		logger.info("Creating mock datas for Processo...");
		
		this.criarProcesso("P01", "Processo 1 Teste", 80l, new BigDecimal("100"), new BigDecimal("150"), "ATIVO", empresaSave);
		this.criarProcesso("P02", "Processo 2 Teste", 160l, new BigDecimal("200"), new BigDecimal("200"), "ATIVO", empresaSave);
		
		logger.info("Creating mock datas for CLiente...");
		
		this.criarCliente("CLIENTE TESTE 1", "11.555.879/0001/75", TipoClienteEnum.PJ, "ATIVO", empresaSave);
		
		this.criarCliente("CLIENTE TESTE 2", "22.222.666/0001/75", TipoClienteEnum.PJ, "ATIVO", empresaSave);
		
	}
	
	private Empresa criarEmpresa(String nome, String cnpj, Endereco endereco, Contato contato) {
		Empresa empresa1 = new Empresa(
				cnpj, 
				nome, 
				nome, 
				LocalDate.now(), 
				LocalDate.now(), 
				"ATIVO", 
				endereco, 
				contato, 
				null);
		
		empresa1.setCriadoPor(env.getProperty("default.system.username"));
		empresa1.setDtCriacao(LocalDateTime.now());
		return empresaRepository.save(empresa1);
	}
	
	private Usuario criarUsuario(String nome, String login, String senha, List<Papel> papel, Empresa empresa1Save) {
		Usuario usuario = new Usuario(
				TipoAcessoEnum.EMPRESA, 
				null, 
				null, 
				null, 
				nome, 
				login, 
				login + "@test.com", 
				senha, 
				Boolean.FALSE, 
				Boolean.TRUE, 
				papel, 
				empresa1Save);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		return usuarioRepository.save(usuario);
	}
	
	private Componente criarComponente(String codigo, String nomeComponente, BigDecimal largura, BigDecimal altura, BigDecimal peso, BigDecimal diametro,
			BigDecimal valorVenda, BigDecimal valorCusto, Integer quantidadeEstoque, String status, Empresa empresa1Save) {
		Componente componente = new Componente(
				codigo, 
				nomeComponente, 
				largura,
				altura,
				peso,
				diametro,
				valorVenda, 
				valorCusto, 
				quantidadeEstoque, 
				status, 
				empresa1Save);
		componente.setDtCriacao(LocalDateTime.now());
		componente.setCriadoPor(env.getProperty("default.system.username"));
		return componenteRepository.save(componente);
	}

	private Processo criarProcesso(String codigo, String nomeProcesso, Long tempoExecucao, BigDecimal valorCusto, BigDecimal valorVenda, String status, Empresa empresa) {
		
		Processo processo = new Processo(codigo, nomeProcesso, tempoExecucao, valorCusto, valorVenda, status, empresa);
		processo.setDtCriacao(LocalDateTime.now());
		processo.setCriadoPor(env.getProperty("default.system.username"));
		
		return processoRepository.save(processo);
	}
	
	private Cliente criarCliente(String nomeCliente, String cnpjCpf, TipoClienteEnum tipoCliente, String status, Empresa empresa) {
		
		Endereco endereco = new Endereco("Rua. Benjanmin Constant", "125", "Centro", "Suzano", "SP", "00000-555", null);
		endereco.setDtCriacao(LocalDateTime.now());
		endereco.setCriadoPor(env.getProperty("default.system.username"));
		
		Contato contato = new Contato("Teste", "(11) 00000-0000", "(11) 1111-1111", "teste@teste.com");
		contato.setDtCriacao(LocalDateTime.now());
		contato.setCriadoPor(env.getProperty("default.system.username"));
		
		Cliente cliente = new Cliente(null, nomeCliente, cnpjCpf, tipoCliente, status, empresa, endereco, contato);
		cliente.setDtCriacao(LocalDateTime.now());
		cliente.setCriadoPor(env.getProperty("default.system.username"));
		
		return clienteRepository.save(cliente);
	}
}
