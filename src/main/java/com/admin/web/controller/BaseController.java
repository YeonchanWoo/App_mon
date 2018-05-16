package com.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.admin.web.dto.Apps;
import com.admin.web.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.admin.web.dto.Menu;
import com.admin.web.service.impl.UserSession;


@Controller
public class BaseController {

	@Autowired
	private SettingsService settingsService;

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

	@ModelAttribute("appList")
	private List<Apps> appsList(){
        UserSession userSession = (UserSession)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Apps> appList = new ArrayList<Apps>();

        if(userSession.getGrpAlias().equals("admin") || userSession.getGrpAlias().toUpperCase().equals("ADMIN")){
            appList = settingsService.selectAppsList(null);
        }else{
            String grpAlias = userSession.getGrpAlias();

            Apps apps = new Apps();
            apps.setGrpAlias(grpAlias);

            appList = settingsService.selectAppsList(apps);
        }
        return  appList;
	}
	
}
