package com.admin.web.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("HitSource")
public class HitSource implements Serializable {
    private static final long serialVersionUID = 1;

    private String device_model;
    private String app_gubun;
    private String os_version;
    private String ip;
    private String uuid;
    private String err_message;
    private String err_time;
    private String refer;
    private String err_name;
    private String app_ver;
    private String device_gubun;
    private String network_type;
    private String customer_id;


}
