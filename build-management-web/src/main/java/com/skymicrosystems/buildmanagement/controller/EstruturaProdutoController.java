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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.buildmanagement.forms.EstruturaProdutoSearchForm;
import com.skymicrosystems.buildmanagement.model.EstruturaProduto;
import com.skymicrosystems.buildmanagement.services.ComponenteServiceImpl;
import com.skymicrosystems.buildmanagement.services.EstruturaProdutoServiceImpl;
import com.skymicrosystems.buildmanagement.services.ProcessoServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class EstruturaProdutoController {
	
	private final static Logger logger = LoggerFactory.getLogger(EstruturaProdutoController.class);
	
	private static final String FORWARD_FORM = "manutencao-estrutura-produto/form";
	private static final String FORWARD_LIST = "manutencao-estrutura-produto/list";
	private static final String BASE_DOMAIN = "/estrutura-produto";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private EstruturaProdutoServiceImpl estruturaProdutoServiceImpl;
	
	@Autowired
	private ProcessoServiceImpl processoServiceImpl;
	
	@Autowired
	private ComponenteServiceImpl componenteServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new EstruturaProdutoSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, EstruturaProdutoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new EstruturaProdutoSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, EstruturaProdutoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		estruturaProdutoServiceImpl.searchPaginate(
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
    public String showForm(Model model, EstruturaProduto estruturaProduto) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid EstruturaProduto estruturaProduto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(estruturaProduto, model);
            return FORWARD_FORM;
        }
        
        EstruturaProduto estruturaProdutoSave = null;
        		
        try {

        	estruturaProdutoSave = estruturaProdutoServiceImpl.save(estruturaProduto);
	        redirAttrs.addFlashAttribute("success", "manutencao.produto.label.save.success");
	        this.initForm(estruturaProdutoSave, model);
	        
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(estruturaProduto, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new EstruturaProduto(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") long id, Model model) {
		
		EstruturaProduto estruturaProduto = estruturaProdutoServiceImpl.findById(id).get();
		estruturaProduto.setIdEstruturaProduto(null);
		estruturaProduto.getProduto().setCodigo(null);
		
		this.initForm(estruturaProduto, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid EstruturaProduto estruturaProduto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	estruturaProduto.setIdEstruturaProduto(id);
        	this.initForm(estruturaProduto, model);
            return FORWARD_FORM;
        }
        
        EstruturaProduto estruturaProdutoSave = null;
        
        try {
            
        	estruturaProdutoSave = estruturaProdutoServiceImpl.update(estruturaProduto);
	        redirAttrs.addFlashAttribute("success", "manutencao.produto.label.update.success");
	        this.initForm(estruturaProdutoSave, model);
	        
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(estruturaProduto, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			estruturaProdutoServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.produto.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(EstruturaProduto estruturaProduto, Model model) {
		
		if (estruturaProduto == null) {
			estruturaProduto = new EstruturaProduto();
			
		} else if (estruturaProduto.getIdEstruturaProduto() != null) {
			estruturaProduto = estruturaProdutoServiceImpl.findById(estruturaProduto.getIdEstruturaProduto()).get();
		}
		
		model.addAttribute("estruturaProduto", estruturaProduto);
		
		model.addAttribute("availableProcessos", processoServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
		model.addAttribute("availableComponentes", componenteServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
	}
}
