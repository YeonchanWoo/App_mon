package com.admin.web.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("SystemCode")
public class SystemCode {
	private int id;
	private String type;
	private String code;
	private String data1;
	private String data2;
	private String data3;
	private String regDate;
	private String uptDate;
}
