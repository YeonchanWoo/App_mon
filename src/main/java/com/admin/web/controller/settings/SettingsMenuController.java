package com.admin.web.controller.settings;

import com.admin.web.controller.BaseController;
import com.admin.web.dto.Menu;
import com.admin.web.dto.User;
import com.admin.web.service.SettingsService;
import com.admin.web.service.UserService;
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

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class SettingsMenuController  extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsMenuController.class);

    @Autowired private SettingsService settingsService;
    @Autowired private UserService userService;

    @GetMapping(value={"/settings/menu"})
    public String menuSettings(Locale locale, Model model){
        logger.debug("{}","settings page");

        List<Integer> menuList = null;
        List<Menu> activeMenuList = userService.selectMainMenuList(menuList);
        List<User> setUserList = userService.selectMbrList(null);

        model.addAttribute("activeMenu", activeMenuList);
        model.addAttribute("setUser", setUserList);

        return "settings/settings_menu";
    }

    @PostMapping(value={"/settings/menu/updateMenu"})
    public ModelAndView insertMenu(Locale locale, Model model, @RequestParam Map<String, String> params) throws Exception{
        String menuName = params.get("menuNm");
        String menuComment = params.get("menuComment");
        String menuGrp = params.get("menuGrp");
        String menuUrl = params.get("menuUrl");
        String activeYn = params.get("activeYn");
        String sort = params.get("sort");
        String id = params.get("id");

        if(id.equals("") || id==null ){
            id = "0";
        }

        boolean isActive = false;
        if(activeYn.equals("Y")){
            isActive = true;
        }

        Menu menu = new Menu();
        menu.setMenuName(menuName);
        menu.setMenuComment(menuComment);
        menu.setMainMenuGrp(menuGrp);
        menu.setMainUrl(menuUrl);
        menu.setActiveYn(isActive);
        menu.setSort(Integer.parseInt(sort));
        menu.setId(Integer.parseInt(id));

        int insertResult = settingsService.insertMenu(menu);

        List<Integer> menuList = null;
        List<Menu> activeMenuList = userService.selectMainMenuList(menuList);
        List<User> setUserList = userService.selectMbrList(null);

        ModelAndView mav = new ModelAndView("settings/component/menu/menu_list");
        mav.addObject("activeMenu", activeMenuList);
        mav.addObject("setUser", setUserList);

        return mav;
    }

    @PostMapping(value={"/settings/menu/deleteMenu"})
    @ResponseBody
    public String deleteMenu(Locale locale, Model model, @RequestParam Map<String, String> params) throws Exception{
        String id = params.get("id");

        Menu menu = new Menu();
        menu.setId(Integer.parseInt(id));

        int insertResult = settingsService.deleteMenu(menu);

        JSONObject result = new JSONObject();

        if(insertResult > 0){
            result.put("code", "success");
        }else{
            result.put("code", "fail");
        }

        return result.toString();
    }

    @PostMapping(value={"/settings/menu/selectMenu"})
    public ModelAndView selectMenu(Locale locale, Model model, @RequestParam Map<String, String> params){
        List<Integer> menuList = null;
        List<Menu> activeMenuList = userService.selectMainMenuList(menuList);
        List<User> setUserList = userService.selectMbrList(null);

        ModelAndView mav = new ModelAndView("settings/component/menu/menu_list");
        mav.addObject("activeMenu", activeMenuList);
        mav.addObject("setUser", setUserList);

        return mav;
    }
}
