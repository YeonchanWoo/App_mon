package com.admin.web.controller.settings;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.admin.web.controller.BaseController;
import com.admin.web.dto.SetMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.admin.web.dto.Menu;
import com.admin.web.dto.User;
import com.admin.web.service.SettingsService;
import com.admin.web.service.UserService;


@Controller
public class SettingsUserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SettingsUserController.class);

	@Autowired private SettingsService settingsService;
	@Autowired private UserService userService;

	@GetMapping(value={"/settings/user"})
	public String userMenuSettings(Locale locale, Model model){
		logger.debug("{}","settings page");
		
		List<Integer> menuList = null;
		List<Menu> activeMenuList = userService.selectMainMenuList(menuList);
		List<User> setUserList = userService.selectMbrList(null);
		
		model.addAttribute("activeMenu", activeMenuList);
		model.addAttribute("setUser", setUserList);
		
		return "settings/settings_user";
	}
	
	@PostMapping(value={"/settings/user/updateUserSetMenu"})
	public ModelAndView updateSetMenu(Locale locale, Model model, @RequestParam Map<String, String> params) throws Exception{
		String loginId = params.get("loginId");
		String setNewMenuNo = params.get("setNewMenuNo");
		
		settingsService.updateSetMenu(setNewMenuNo, loginId);

        List<Integer> menuList = null;
        List<Menu> activeMenuList = userService.selectMainMenuList(menuList);
        List<User> setUserList = userService.selectMbrList(null);

        ModelAndView mav = new ModelAndView("settings/component/user/user_set_menu_list");
        mav.addObject("activeMenu", activeMenuList);
        mav.addObject("setUser", setUserList);

		return mav;
	}

	@PostMapping(value={"/settings/user/deleteSetUserMenu"})
	@ResponseBody
	public String deleteSetUserMenu(Locale locale, Model model, @RequestParam Map<String, String> params) throws Exception{
		String loginId = params.get("loginId");

		SetMenu setMenu = new SetMenu();
		setMenu.setLoginId(loginId);

		int insertResult = settingsService.deleteSetUserMenu(setMenu);

		JSONObject result = new JSONObject();

		if(insertResult > 0){
			result.put("code", "success");
		}else{
			result.put("code", "fail");
		}

		return result.toString();
	}

    @PostMapping(value={"/settings/user/selectSetUserMenu"})
    public ModelAndView selectSetUserMenu(Locale locale, Model model, @RequestParam Map<String, String> params){
        List<Integer> menuList = null;
        List<Menu> activeMenuList = userService.selectMainMenuList(menuList);
        List<User> setUserList = userService.selectMbrList(null);

        ModelAndView mav = new ModelAndView("settings/component/user/user_set_menu_list");
        mav.addObject("activeMenu", activeMenuList);
        mav.addObject("setUser", setUserList);

        return mav;
    }
}
