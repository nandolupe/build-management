package com.skymicrosystems.buildmanagement.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Orcamento;
import com.skymicrosystems.buildmanagement.repositories.OrcamentoRepository;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

import javassist.NotFoundException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class OrcamentoServiceImpl {
	
	private final static Logger logger = LoggerFactory.getLogger(OrcamentoServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
    private OrcamentoRepository orcamentoRepository;
	
	@Autowired
	private EmailService emailService;
    
    public Page<Orcamento> searchPaginate(Orcamento orcamento, Pageable pageable) {
		
    	return orcamentoRepository.findAll(
				this.defaultSearchSpec(orcamento, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Orcamento> findById(Long id) {
    	return orcamentoRepository.findById(id);
	}
    
    public Orcamento save(Orcamento orcamento) {
    	this.calculatingValues(orcamento);
        return orcamentoRepository.save(orcamento);
	}
    
    public Orcamento update(Orcamento orcamento) {
    	this.calculatingValues(orcamento);
        return orcamentoRepository.saveAndFlush(orcamento);
	}
    
    public void delete(Long id) {
    	orcamentoRepository.findById(id).ifPresent(u -> orcamentoRepository.delete(u));
	}
    
    private void calculatingValues(Orcamento orcamento) {
    	BigDecimal valorTotalCusto = 
    			orcamento
    			.getProdutos()
    			.stream()
    			.map(x -> x != null ? x.getProduto().getValorCusto() : BigDecimal.ZERO)
    			.reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	BigDecimal valorTotalVenda = 
    			orcamento
    			.getProdutos()
    			.stream()
    			.map(x -> x != null ? x.getProduto().getValorVenda() : BigDecimal.ZERO)
    			.reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	orcamento.setValorTotalVenda(valorTotalVenda);
    	orcamento.setValorTotalCusto(valorTotalCusto);
    }

	private Specification<Orcamento> defaultSearchSpec(Orcamento orcamento, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			
			if (orcamento != null) {
			
				if (StringUtils.isNotBlank(orcamento.getCodigo())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("codigo")), 
											"%" + orcamento.getCodigo().toUpperCase().trim() + "%"), 
									predicate);
				}
				
				
				if (StringUtils.isNotBlank(orcamento.getCliente().getNomeCliente())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("cliente.nomeCliente")), 
											"%" + orcamento.getCliente().getNomeCliente().toUpperCase().trim() + "%"), 
									predicate);
				}
			}
			
			return predicate;
		};
	}
	
	/**
	 * @param idOrcamento
	 * @param email
	 * @throws NotFoundException
	 * @throws JRException
	 * @throws IOException
	 * @throws MessagingException
	 */
	public void enviarOrcamentoPorEmail(Long idOrcamento, String email) throws NotFoundException, JRException, IOException, MessagingException {
		
		Optional<Orcamento> orcamento = orcamentoRepository.findById(idOrcamento);
		if(orcamento.isPresent()) {
			
			File file = this.generateReport(orcamento.get());
			
			if (StringUtils.isBlank(email)) {
				email = orcamento.get().getCliente().getContato().getEmail();
			}
			
			emailService.send(
					env.getProperty("default.mail.from"), 
					email, 
					"envio-orcamento-to-client", 
					new Object[] {orcamento.get().getCliente().getNomeCliente(), orcamento.get().getCodigo()}, 
					new File[] {file});
			
		} else {
			throw new NotFoundException("Nao foi possível encontrar o orcamento para o id desejado!");
		}
		
	}
	
	/**
	 * @param orcamento
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public File generateReport(Orcamento orcamento) throws JRException, IOException {
		
		logger.info("gerando relatorio");
		
		Map<String, Object> empParams = new HashMap<String, Object>();

		JasperPrint empReport =
				JasperFillManager.fillReport (
						JasperCompileManager.compileReport(
						ResourceUtils.getFile("classpath:reports/orcamento/orcamento-to-cliente.jrxml")
								.getAbsolutePath()) 
						, empParams
						, new JRBeanCollectionDataSource(Collections.singleton(orcamento))
				);
		
		//JasperExportManager.exportReportToPdf(empReport);
		
		File file = File.createTempFile("orcamento-to-client", ".pdf");
		
		FileOutputStream output = new FileOutputStream(file);
		JasperExportManager.exportReportToPdfStream(empReport, output);
		
		logger.info("relatorio gerado com sucesso!");
		
		return file;
		
	}
}
