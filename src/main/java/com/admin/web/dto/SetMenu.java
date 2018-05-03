package com.admin.web.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("SetMenu")
public class SetMenu {
	private int id;  
	private String loginId; 
    protected String setMenuNo;
    protected String setMenuNm;
    private String regDate;  
    private String uptDate; 
    
}