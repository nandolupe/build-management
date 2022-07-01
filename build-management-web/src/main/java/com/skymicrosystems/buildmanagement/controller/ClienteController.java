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

import com.skymicrosystems.buildmanagement.forms.ClienteSearchForm;
import com.skymicrosystems.buildmanagement.model.Cliente;
import com.skymicrosystems.buildmanagement.services.ClienteServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class ClienteController {
	
	private static final String FORWARD_FORM = "manutencao-cliente/form";
	private static final String FORWARD_LIST = "manutencao-cliente/list";
	private static final String BASE_DOMAIN = "/cliente";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private ClienteServiceImpl clienteServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new ClienteSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, ClienteSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new ClienteSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, ClienteSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		clienteServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Cliente cliente) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(cliente, model);
            return FORWARD_FORM;
        }
        
        Cliente clienteSave = null;
        		
        try {
        
        	clienteSave = clienteServiceImpl.save(cliente);
	        redirAttrs.addFlashAttribute("success", "manutencao.cliente.label.save.success");
	        this.initForm(clienteSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(cliente, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Cliente(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") long id, Model model) {
		
		Cliente cliente = clienteServiceImpl.findById(id).get();
		cliente.setIdCliente(null);
		cliente.setCnpjCpf(null);
		
		this.initForm(cliente, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	cliente.setIdCliente(id);
        	this.initForm(cliente, model);
            return FORWARD_FORM;
        }
        
        Cliente clienteSave = null;
        
        try {
            
        	clienteSave = clienteServiceImpl.update(cliente);
	        redirAttrs.addFlashAttribute("success", "manutencao.cliente.label.update.success");
	        this.initForm(clienteSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(cliente, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			clienteServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.cliente.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Cliente cliente, Model model) {
		
		if (cliente == null) {
			cliente = new Cliente();
			
		} else if (cliente.getIdCliente() != null) {
			cliente = clienteServiceImpl.findById(cliente.getIdCliente()).get();
		}
		
		model.addAttribute("cliente", cliente);
	}
}
