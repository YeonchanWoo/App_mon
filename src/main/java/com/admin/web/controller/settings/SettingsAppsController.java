package com.admin.web.controller.settings;

import com.admin.web.controller.BaseController;
import com.admin.web.dto.Apps;

import com.admin.web.dto.Menu;
import com.admin.web.dto.User;
import com.admin.web.service.SettingsService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class SettingsAppsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsAppsController.class);

    @Autowired private SettingsService settingsService;

    @GetMapping(value={"/settings/apps"})
    public String appsIndex(Locale locale, Model model){
        logger.debug("{}","apps page");

        List<Apps> appList = settingsService.selectAppsList(null);
        model.addAttribute("appList", appList);

        return "settings/settings_apps";
    }

    @PostMapping(value={"/settings/apps/updateApps"})
    public ModelAndView updateApps(Locale locale, Model model,
             @RequestParam("imageFile") MultipartFile sourceFile,
             @RequestParam("imageFileName") String imageFileName,
             @RequestParam("appName") String appName,
             @RequestParam("appDivision") String appDivision){

        int resultUpdate = settingsService.updateApps(sourceFile, imageFileName, appName, appDivision);
        List<Apps> appList = settingsService.selectAppsList(null);

        ModelAndView mav = new ModelAndView("settings/settings_apps");
        mav.addObject("appList", appList);

        return mav;
    }

    @PostMapping(value={"/settings/apps/deleteApps"})
    @ResponseBody
    public String deleteApps(Locale locale, Model model, @RequestParam Map<String, String> params) throws Exception{
        String id = params.get("id");

        Apps apps = new Apps();
        apps.setId(Integer.parseInt(id));

        int deleteResult = settingsService.deleteApps(apps);

        JSONObject result = new JSONObject();

        if(deleteResult > 0){
            result.put("code", "success");
        }else{
            result.put("code", "fail");
        }

        return result.toString();
    }

    @PostMapping(value={"/settings/apps/selectApps"})
    public ModelAndView selectApps(Locale locale, Model model, @RequestParam Map<String, String> params){
        List<Apps> appList = settingsService.selectAppsList(null);

        ModelAndView mav = new ModelAndView("settings/component/apps/app_list");
        mav.addObject("appList", appList);

        return mav;
    }

}
