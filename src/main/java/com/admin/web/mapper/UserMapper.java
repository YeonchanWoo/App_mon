package com.admin.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.admin.web.dto.Menu;
import com.admin.web.dto.User;

@Mapper
public interface UserMapper {
	public List<User> selectMbrList(@Param("loginId") String loginId);
	
	public List<Menu> selectMainMenuList(@Param("menuNo") List<Integer> loginId);

	public List<Menu> selectMainMenuGrpList(@Param("menuNo") List<Integer> loginId);
}
