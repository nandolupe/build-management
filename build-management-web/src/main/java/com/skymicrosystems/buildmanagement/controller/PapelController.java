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

import com.skymicrosystems.buildmanagement.forms.PapelSearchForm;
import com.skymicrosystems.buildmanagement.model.Papel;
import com.skymicrosystems.buildmanagement.services.PapelServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class PapelController {
	
	private static final String FORWARD_FORM = "manutencao-papel/form";
	private static final String FORWARD_LIST = "manutencao-papel/list";
	private static final String BASE_DOMAIN = "/papel";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private PapelServiceImpl papelServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		Papel papel = new Papel();
		this.search(model, new PapelSearchForm(papel, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, PapelSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new PapelSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, PapelSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		papelServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Papel papel) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Papel papel, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(papel, model);
            return FORWARD_FORM;
        }
        
        Papel papelSave = null;
        		
        try {
        
        	papelSave = papelServiceImpl.save(papel);
	        redirAttrs.addFlashAttribute("success", "manutencao.papel.label.save.success");
	        this.initForm(papelSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(papel, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Papel(id), model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Papel papel, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	papel.setIdPapel(id);
        	this.initForm(papel, model);
            return FORWARD_FORM;
        }
        
        Papel papelSave = null;
        
        try {
            
        	papelSave = papelServiceImpl.update(papel);
	        redirAttrs.addFlashAttribute("success", "manutencao.papel.label.update.success");
	        this.initForm(papelSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(papel, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			papelServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.papel.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Papel papel, Model model) {
		
		if (papel == null) {
			papel = new Papel();
			
		} else if (papel.getIdPapel() != null) {
			papel = papelServiceImpl.findById(papel.getIdPapel()).get();
		}
		
		model.addAttribute("papel", papel);
	}
}
