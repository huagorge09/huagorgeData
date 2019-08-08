package com.cmwa.ec.webapp.common.category;

import java.io.Serializable;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class CategoryDto implements Serializable {
    private String id;
    private String name;
    private String level;
    private String defaultId;
    private String parentId;
    private String url;
    private String param;
    private CategoryDto[] subCats;

    public CategoryDto() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }

    public String getDefaultId() {
        return defaultId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setSubCats(CategoryDto[] subCats) {
        this.subCats = subCats;
    }

    public CategoryDto[] getSubCats() {
        return subCats;
    }

}
