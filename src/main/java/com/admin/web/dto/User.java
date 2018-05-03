package com.admin.web.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Alias("User")
@EqualsAndHashCode(callSuper=false)
public class User extends SetMenu{
	private int id;
	private String loginId;
	private String password;
	private String activeYn;
	private String grpAlias;
	private int authId;
	private String regDate;
	private String uptDate;
	
}
