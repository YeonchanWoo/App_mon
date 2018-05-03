package com.admin.web.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.admin.web.dto.Menu;

public class UserSession extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserSession(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}

	private List<Menu> mainMenuList;
	private String loginId;
	private String grpAlias;
	private String encData;

	public List<Menu> getMainMenuGrpList() {
		return mainMenuGrpList;
	}

	public void setMainMenuGrpList(List<Menu> mainMenuGrpList) {
		this.mainMenuGrpList = mainMenuGrpList;
	}

	private List<Menu> mainMenuGrpList;
	
	public String getEncData() {
		return encData;
	}
	public void setEncData(String encData) {
		this.encData = encData;
	}

	public String getGrpAlias() {
		return grpAlias;
	}
	public void setGrpAlias(String grpAlias) {
		this.grpAlias = grpAlias;
	}

	public List<Menu> getMainMenuList() {
		return mainMenuList;
	}
	public void setMainMenuList(List<Menu> mainMenuList) {
		this.mainMenuList = mainMenuList;
	}

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
}
