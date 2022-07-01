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

import com.skymicrosystems.buildmanagement.forms.EmpresaSearchForm;
import com.skymicrosystems.buildmanagement.model.Empresa;
import com.skymicrosystems.buildmanagement.model.Usuario;
import com.skymicrosystems.buildmanagement.services.EmpresaServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class EmpresaController {
	
	
	private final static Logger logger = LoggerFactory.getLogger(EmpresaController.class);
	
	private static final String FORWARD_FORM = "manutencao-empresa/form";
	private static final String FORWARD_LIST = "manutencao-empresa/list";
	private static final String BASE_DOMAIN = "/empresa";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private EmpresaServiceImpl empresaServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		Empresa empresa = new Empresa();
		this.search(model, new EmpresaSearchForm(empresa, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, EmpresaSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new EmpresaSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, EmpresaSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		empresaServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Empresa empresa) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Empresa empresa, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(empresa, model);
            return FORWARD_FORM;
        }
        
        Empresa empresaSave = null;
        		
        try {
        
        	empresaSave = empresaServiceImpl.save(empresa);
	        redirAttrs.addFlashAttribute("success", "manutencao.empresa.label.save.success");
	        this.initForm(empresaSave, model);
	        
        } catch (Exception e) {
        	logger.info(e.getLocalizedMessage());
        	
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(empresa, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Empresa(id), model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Empresa empresa, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	empresa.setIdEmpresa(id);
        	this.initForm(empresa, model);
            return FORWARD_FORM;
        }
        
        Empresa empresaSave = null;
        
        try {
            
        	empresaSave = empresaServiceImpl.update(empresa);
	        redirAttrs.addFlashAttribute("success", "manutencao.empresa.label.update.success");
	        this.initForm(empresaSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(empresa, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			empresaServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.empresa.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Empresa empresa, Model model) {
		
		if (empresa == null) {
			empresa = new Empresa();
			empresa.setUsuario(new Usuario(Boolean.FALSE, Boolean.TRUE));
			
		} else if (empresa.getIdEmpresa() != null) {
			empresa = empresaServiceImpl.findById(empresa.getIdEmpresa()).get();
			if (empresa.getUsuario() == null) {
				empresa.setUsuario(new Usuario(Boolean.FALSE, Boolean.TRUE));
			} else {
				empresa.getUsuario().setPassword(null);
			}
		}
		
		model.addAttribute("empresa", empresa);
	}
}
