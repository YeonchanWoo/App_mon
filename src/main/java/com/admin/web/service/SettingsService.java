package com.admin.web.service;

import com.admin.web.dto.Apps;
import com.admin.web.dto.Menu;
import com.admin.web.dto.SetMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SettingsService {
	public int updateSetMenu(String setNewMenuNo, String loginId);
    public int deleteSetUserMenu(SetMenu setMenu);

    public int insertMenu(Menu menu);
	public int deleteMenu(Menu menu);

    public int updateApps(MultipartFile sourceFile, String imageFileName, String appName, String appDivision);
    public int deleteApps(Apps apps);
    public List<Apps> selectAppsList(@Param("apps") Apps apps);
}
