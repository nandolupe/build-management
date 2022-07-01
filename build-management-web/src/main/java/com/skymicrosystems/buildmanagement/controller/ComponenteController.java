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

import com.skymicrosystems.buildmanagement.forms.ComponenteSearchForm;
import com.skymicrosystems.buildmanagement.model.Componente;
import com.skymicrosystems.buildmanagement.services.ComponenteServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class ComponenteController {
	
	private static final String FORWARD_FORM = "manutencao-componente/form";
	private static final String FORWARD_LIST = "manutencao-componente/list";
	private static final String BASE_DOMAIN = "/componente";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	private final static Logger logger = LoggerFactory.getLogger(ComponenteController.class);
	
	@Autowired
	private ComponenteServiceImpl componenteServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new ComponenteSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, ComponenteSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new ComponenteSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, ComponenteSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		componenteServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(
	        						searchForm.getPageNumber() - 1, 
	        						searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Componente componente, RedirectAttributes redirAttrs) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Componente componente, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(componente, model);
            return FORWARD_FORM;
        }
        
        Componente componenteSave = null;
        		
        try {
        
        	componenteSave = componenteServiceImpl.save(componente);
	        redirAttrs.addFlashAttribute("success", "manutencao.componente.label.save.success");
	        this.initForm(componenteSave, model);
	        
        } catch (Exception e) {
        	
        	logger.error(e.getLocalizedMessage());
        	
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	
        	this.initForm(componente, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		this.initForm(new Componente(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		Componente componente = componenteServiceImpl.findById(id).get();
		componente.setIdComponente(null);
		componente.setCodigo(null);
		
		this.initForm(componente, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Componente componente, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	componente.setIdComponente(id);
        	this.initForm(componente, model);
            return FORWARD_FORM;
        }
        
        Componente componenteSave = null;
        
        try {
            
        	componenteSave = componenteServiceImpl.update(componente);
	        redirAttrs.addFlashAttribute("success", "manutencao.componente.label.update.success");
	        this.initForm(componenteSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(componente, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			componenteServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.componente.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Componente componente, Model model) {
		
		if (componente == null) {
			componente = new Componente();
			
		} else if (componente.getIdComponente() != null) {
			componente = componenteServiceImpl.findById(componente.getIdComponente()).get();
		}
		
		model.addAttribute("componente", componente);
	}
}
