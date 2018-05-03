package com.admin.web.service.impl;

import com.admin.web.dto.Apps;
import com.admin.web.dto.SetMenu;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.admin.web.dto.Menu;
import com.admin.web.mapper.SettingsMapper;
import com.admin.web.service.SettingsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService{

	@Autowired private SettingsMapper settingsMapper;
    @Value("${imageFileUpload.path}") private String filePath ;
	
	public int updateSetMenu(String setNewMenuNo, String loginId){
		return settingsMapper.updateSetMenu(setNewMenuNo, loginId);
	};
	
	public int insertMenu(Menu menu){
		return settingsMapper.insertMenu(menu);
	}
	
	public int deleteMenu(Menu menu){
		return settingsMapper.deleteMenu(menu);
	}

	public int deleteSetUserMenu(SetMenu setMenu){
		int deleteSetUser = settingsMapper.deleteSetUser(setMenu);

		int deleteUser = settingsMapper.deleteUser(setMenu);

		return deleteSetUser * deleteUser;
	};

    public int updateApps(MultipartFile sourceFile, String imageFileName, String appName, String appDivision) {
        String fileName = "";
        String fileExtension = "";
        String fileThumbnail = "";

        if(!sourceFile.isEmpty()){
            fileName = sourceFile.getOriginalFilename().substring(0, sourceFile.getOriginalFilename().lastIndexOf("."));
            fileExtension = FilenameUtils.getExtension(sourceFile.getOriginalFilename());

            fileName = "/" + fileName +"_"+ System.currentTimeMillis() +".";
            try {
                byte[] bytes = sourceFile.getBytes();
                Path uploadPath = Paths.get(filePath + fileName + fileExtension);

                Files.write(uploadPath, bytes);

            }catch (IOException e){
                e.printStackTrace();
            }

            fileThumbnail = filePath.substring(filePath.lastIndexOf("/")) + fileName + fileExtension;
        }else{
            File searchFile = new File(filePath + imageFileName);

            if(!searchFile.exists()){
                fileThumbnail = imageFileName;
            }
        }

        Apps apps = new Apps();
        apps.setAppName(appName);
        apps.setAppDivision(appDivision);
        apps.setThumbnailName(fileThumbnail);

        int resultUpdate =  settingsMapper.updateApps(apps);

        return resultUpdate;
    }

    @Override
    public int deleteApps(Apps apps) {
        return settingsMapper.deleteApps(apps);
    }

    @Override
    public List<Apps> selectAppsList(Apps apps) {
        return settingsMapper.selectAppsList(apps);
    }
}
