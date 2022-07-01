package com.skymicrosystems.buildmanagement.controller;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public abstract class SmartWebController<T extends Serializable> {
	
	public String listPage = "/list";
	public String formPage = "/form";
		
	@GetMapping("/list")
    public String show(Model model) {
		
		/*
		 * model.addAttribute("principalList", restTemplate .getForEntity(
		 * StringUtils.join( env.getProperty("locacao.veiculos.api.endpoint"),
		 * "/locacao-veiculos-api/locacao"), Locacao[].class) .getBody());
		 */
        
        return listPage;
    }
	
	@GetMapping("/adicionar")
    public String showForm(Model model, T object) {
		
		this.initForm(null, model);
		
		return formPage;
    }
 /*	
	@PostMapping("/adicionar")
    public String add(@Valid T object, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	this.initForm(null, model);
            return "cadastro-locacao/form";
        }
        
        ResponseEntity<Serializable[]> postForEntity = restTemplate
		.postForEntity(
				StringUtils.join(env.getProperty("locacao.veiculos.api.endpoint"),
						"/locacao-veiculos-api/locacao"), 
				object, 
				Serializable[].class);
        
		if(postForEntity.getStatusCode().equals(HttpStatus.OK)) {
        	return "redirect:/locacao/list";
        } else {
        	return "cadastro-locacao/form";
        }
    }
	
	@GetMapping("/locacao/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(id, model);
        
        return "cadastro-locacao/form";
    }
	
	@PostMapping("/locacao/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Locacao locacao, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	locacao.setId(id);
        	this.initForm(id, model);
            return "cadastro-locacao/form";
        }
        
        locacao.setStatus("AGUARDANDO APROVACAO");
        ResponseEntity<Locacao> postForEntity = locacaoGateway.updateLocacao(id, locacao);
        
        if(postForEntity.getStatusCode().equals(HttpStatus.OK)) {
        	return "redirect:/locacao/list";
        } else {
        	return "cadastro-locacao/form";
        }
    }
	
	@GetMapping("/locacao/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
		locacaoGateway.delete(id);
        return "redirect:/locacao/list";
    }
	*/
	private void initForm(Long id, Model model) {
		
	}
}

