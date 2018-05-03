package com.admin.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.web.dto.Menu;
import com.admin.web.dto.User;
import com.admin.web.mapper.UserMapper;
import com.admin.web.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
@Autowired private UserMapper userMapper;
	
	public List<User> selectMbrList(String loginId) {
		List<Menu> setMenuList = null;
		List<Integer> menuListSeq = null;
		List<User> list = new ArrayList<User>();
		
		list = userMapper.selectMbrList(loginId);
		
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getSetMenuNo() == null || 
					list.get(i).getSetMenuNo().isEmpty()){
				setMenuList = null;
				
			}else if(list.get(i).getSetMenuNo().equals("-1")){
				setMenuList = userMapper.selectMainMenuList(menuListSeq);
			
			}else{
				menuListSeq = new ArrayList<Integer>();

				String[] splitMenu = list.get(i).getSetMenuNo().split(",");
				
				for(int k=0; k<splitMenu.length; k++){
					menuListSeq.add(Integer.parseInt(splitMenu[k]));
				}
				setMenuList =  userMapper.selectMainMenuList(menuListSeq);
			}
			
			if(setMenuList != null){
				String menuName = "";
				for(int j=0; j<setMenuList.size(); j++){
					menuName += setMenuList.get(j).getMenuName() + ", ";
				}

				if(menuName.lastIndexOf(",") > 0){
					list.get(i).setSetMenuNm(menuName.substring(0, menuName.lastIndexOf(",")));
				}else{
					list.get(i).setSetMenuNm(menuName.substring(0));
				}
			}
		}
		
		return list;
	}

	@Override
	public List<Menu> selectMainMenuList(List<Integer> menuId) {
		return userMapper.selectMainMenuList(menuId);
	}

	@Override
	public List<Menu> selectMainMenuGrpList(List<Integer> menuId) {
		return userMapper.selectMainMenuGrpList(menuId);
	}


}
