package com.admin.web.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("Bucket")
public class Bucket implements Serializable {

    private static final long serialVersionUID = 1;

    private String key;
    private long docCount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getDocCount() {
        return docCount;
    }

    public void setDocCount(long docCount) {
        this.docCount = docCount;
    }

    public Bucket() {
    }

    public Bucket(String key, long docCount) {
        this.key = key;
        this.docCount = docCount;
    }
}
