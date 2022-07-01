package com.skymicrosystems.buildmanagement.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.buildmanagement.forms.OrcamentoSearchForm;
import com.skymicrosystems.buildmanagement.model.Orcamento;
import com.skymicrosystems.buildmanagement.services.ClienteServiceImpl;
import com.skymicrosystems.buildmanagement.services.EstruturaProdutoServiceImpl;
import com.skymicrosystems.buildmanagement.services.OrcamentoServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class OrcamentoController {
	
	private final static Logger logger = LoggerFactory.getLogger(OrcamentoController.class);
	
	private static final String FORWARD_FORM = "manutencao-orcamento/form";
	private static final String FORWARD_LIST = "manutencao-orcamento/list";
	private static final String BASE_DOMAIN = "/orcamento";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private OrcamentoServiceImpl orcamentoServiceImpl;
	
	@Autowired
	private EstruturaProdutoServiceImpl estruturaProdutoServiceImpl;
	
	@Autowired
	private ClienteServiceImpl clienteServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new OrcamentoSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, OrcamentoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new OrcamentoSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, OrcamentoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		orcamentoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(
	        						searchForm.getPageNumber() - 1, 
	        						searchForm.getSize())));
        
		 } catch (Exception e) {
			 	logger.error(e.getLocalizedMessage());
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Orcamento orcamento) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Orcamento orcamento, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(orcamento, model);
            return FORWARD_FORM;
        }
        
        Orcamento orcamentoSave = null;
        		
        try {

        	orcamentoSave = orcamentoServiceImpl.save(orcamento);
	        redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.save.success");
	        this.initForm(orcamentoSave, model);
	        
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(orcamento, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Orcamento(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/report-by-email/{id}")
    public String sendOrcamentoByEmail(@PathVariable("id") long id, @RequestParam("email") String email, Model model, RedirectAttributes redirAttrs) {
		
		try {
			
			logger.info("id: " + id);
			logger.info("email: " + email);
			
			orcamentoServiceImpl.enviarOrcamentoPorEmail(id, email);
			
			redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.send.email.success");
		
		} catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
        
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") long id, Model model) {
		
		Orcamento orcamento = orcamentoServiceImpl.findById(id).get();
		orcamento.setIdOrcamento(null);
		orcamento.setCodigo(null);
		orcamento.setCliente(null);
		
		this.initForm(orcamento, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Orcamento orcamento, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	orcamento.setIdOrcamento(id);
        	this.initForm(orcamento, model);
            return FORWARD_FORM;
        }
        
        Orcamento orcamentoSave = null;
        
        try {
            
        	orcamentoSave = orcamentoServiceImpl.update(orcamento);
	        redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.update.success");
	        this.initForm(orcamentoSave, model);
	        
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(orcamento, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			orcamentoServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Orcamento orcamento, Model model) {
		
		if (orcamento == null) {
			orcamento = new Orcamento();
			
		} else if (orcamento.getIdOrcamento() != null) {
			orcamento = orcamentoServiceImpl.findById(orcamento.getIdOrcamento()).get();
		}
		
		model.addAttribute("orcamento", orcamento);
		
		model.addAttribute("availableProdutos", estruturaProdutoServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
		model.addAttribute("availableClientes", clienteServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
		
		
	}
}
