package com.admin.web.dto;

import org.apache.http.annotation.Contract;
import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("Menu")
public class Menu {
	private int id;
	private int sort;
	private boolean activeYn;
	private String activeYnStr;
	private String menuName;
	private String menuComment;
	private String mainUrl;
	private String mainMenuGrp;
	
	private String subMenuName;
	private String subMenuComment;
	private String subMenuUrl;
	private String subMenuGrp;

}

