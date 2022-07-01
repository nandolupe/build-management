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

import com.skymicrosystems.buildmanagement.enums.StatusEnum;
import com.skymicrosystems.buildmanagement.forms.UsuarioSearchForm;
import com.skymicrosystems.buildmanagement.model.Usuario;
import com.skymicrosystems.buildmanagement.repositories.PapelRepository;
import com.skymicrosystems.buildmanagement.services.UsuarioServiceImpl;
import com.skymicrosystems.buildmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class UsuarioController {
	
	private final static Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	private static final String FORWARD_FORM = "manutencao-usuario/form";
	private static final String FORWARD_LIST = "manutencao-usuario/list";
	private static final String BASE_DOMAIN = "/usuario";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";
	
	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl; 
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new UsuarioSearchForm(
				null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, UsuarioSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new UsuarioSearchForm(
        				searchForm.getUsuario(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, UsuarioSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
			
			model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		usuarioServiceImpl.searchPaginate(
	        				searchForm.getUsuario(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }

        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Usuario usuario) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(null, model);
            return FORWARD_FORM;
        }
        
        Usuario usuarioSave = null;
        
        try {
	        usuarioSave = usuarioServiceImpl.save(usuario);
	        redirAttrs.addFlashAttribute("success", "manutencao.usuario.label.save.success");
	        this.initForm(usuarioSave, model);
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(usuario, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		logger.info("Editing.. " + id);
		
		this.initForm(new Usuario(id), model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	usuario.setId(id);
        	this.initForm(new Usuario(id), model);
            return FORWARD_FORM;
        }
        
        Usuario usuarioSave = null;
        
        try {
	        
        	usuarioSave = usuarioServiceImpl.update(usuario);
	        redirAttrs.addFlashAttribute("success", "manutencao.usuario.label.update.success");
	        this.initForm(usuarioSave, model);
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(usuario, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			usuarioServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.usuario.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }		
		
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Usuario usuario, Model model) {
		
		Usuario userAuthenticated = BuildManagementUtils.getUserAuthenticated();
		
		// TODO ARRUMAR PARA CHAMAR O SERVICE DO PAPEL
		if (usuario == null) {
			usuario = new Usuario(Boolean.FALSE, Boolean.TRUE);
			
		} else if (usuario.getId() != null) {
			usuario = usuarioServiceImpl.findById(usuario.getId()).get();
			usuario.setPassword(null);
		}
		
		if (userAuthenticated.isAdministrador()) {
			model.addAttribute("papeis", papelRepository.findByStatus(StatusEnum.ATIVO));
		} else {
			model.addAttribute("papeis", papelRepository.findByStatusAndTipoAcesso(StatusEnum.ATIVO, userAuthenticated.getTipoAcesso()));
		}
		
		model.addAttribute("usuario", usuario);
	}
}
