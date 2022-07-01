package com.skymicrosystems.buildmanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.buildmanagement.forms.ProcessoSearchForm;
import com.skymicrosystems.buildmanagement.model.Processo;
import com.skymicrosystems.buildmanagement.services.ProcessoServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class ProcessoController {
	
	private static final String FORWARD_FORM = "manutencao-processo/form";
	private static final String FORWARD_LIST = "manutencao-processo/list";
	private static final String BASE_DOMAIN = "/processo";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private ProcessoServiceImpl processoServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new ProcessoSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, ProcessoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new ProcessoSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, ProcessoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		processoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Processo processo) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Processo processo, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(processo, model);
            return FORWARD_FORM;
        }
        
        Processo processoSave = null;
        		
        try {
        
        	processoSave = processoServiceImpl.save(processo);
	        redirAttrs.addFlashAttribute("success", "manutencao.processo.label.save.success");
	        this.initForm(processoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(processo, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Processo(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") long id, Model model) {
		
		Processo processo = processoServiceImpl.findById(id).get();
		processo.setIdProcesso(null);
		processo.setCodigo(null);
		
		this.initForm(processo, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Processo processo, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	processo.setIdProcesso(id);
        	this.initForm(processo, model);
            return FORWARD_FORM;
        }
        
        Processo processoSave = null;
        
        try {
            
        	processoSave = processoServiceImpl.update(processo);
	        redirAttrs.addFlashAttribute("success", "manutencao.processo.label.update.success");
	        this.initForm(processoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(processo, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			processoServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.processo.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Processo processo, Model model) {
		
		if (processo == null) {
			processo = new Processo();
			
		} else if (processo.getIdProcesso() != null) {
			processo = processoServiceImpl.findById(processo.getIdProcesso()).get();
		}
		
		model.addAttribute("processo", processo);
	}
}
