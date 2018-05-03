package com.admin.web.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.admin.web.dto.Menu;
import com.admin.web.service.impl.UserSession;


@Controller
public class BaseController {

	@ModelAttribute("mainMenu")
	private List<Menu> mainMenu(){
		UserSession userSession = (UserSession)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userSession.getMainMenuList();
	}

	@ModelAttribute("mainMenuGrp")
	private List<Menu> mainMenuGrp(){
		UserSession userSession = (UserSession)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userSession.getMainMenuGrpList();
	}
	
	@ModelAttribute("loginId")
	private String loginId(){
		UserSession userSession = (UserSession)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userSession.getLoginId();
	}

	@ModelAttribute("grpAlias")
	private String grpAlias(){
		UserSession userSession = (UserSession)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userSession.getGrpAlias();
	}
	
}
