package com.admin.web.mapper;

import com.admin.web.dto.Apps;
import com.admin.web.dto.SetMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.admin.web.dto.Menu;

import java.util.List;

@Mapper
public interface SettingsMapper {
	public int updateSetMenu(@Param("setNewMenuNo") String setNewMenuNo,
                             @Param("loginId") String loginId);
	public int insertMenu(@Param("menu") Menu menu);
	public int deleteMenu(@Param("menu") Menu menu);

	public int deleteSetUser(@Param("setMenu") SetMenu setMenu);
	public int deleteUser(@Param("setMenu") SetMenu setMenu);

    public int updateApps(@Param("apps") Apps apps);
    public List<Apps> selectAppsList(@Param("apps") Apps apps);
    public int deleteApps(@Param("apps") Apps apps);
}
