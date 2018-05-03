package com.admin.web.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("Apps")
public class Apps {
    private int id;
    private String thumbnailName;
    private String appName;
    private String appDivision;
    private Date regDate;
    private Date uptDate;
}
