package com.admin.web.service;

import java.util.List;

import com.admin.web.dto.Menu;
import com.admin.web.dto.User;

public interface UserService {
	
	public List<User> selectMbrList(String loginId);
	public List<Menu> selectMainMenuList(List<Integer> loginId);
	public List<Menu> selectMainMenuGrpList(List<Integer> loginId);

}
