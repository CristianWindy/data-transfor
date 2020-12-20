package com.haizhi.datatransfor.enums;

/**
 * @Author windycristian
 * @Date: 2020/12/18 15:10
 **/
public enum ContentTypeEnum {

    TYPE_JSON("application/json", 0),
    TYPE_WWW_FORM("x-www-form-urlencoded", 1),
    TYPE_FORM_DATA("multipart/form-data", 2);

    private Integer type;
    private String description;

    ContentTypeEnum(String description, Integer type) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
