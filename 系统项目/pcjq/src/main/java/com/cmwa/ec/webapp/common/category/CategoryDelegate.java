package com.cmwa.ec.webapp.common.category;

import org.apache.log4j.Logger;

import com.cmwa.ec.webapp.util.LoadProperties;

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
public class CategoryDelegate {
    CategoryService service;
    private static Logger logger = Logger.getLogger(CategoryDelegate.class.getName());

    public CategoryDelegate() {
        String catXmlPath = null;
        try {
            LoadProperties props = new LoadProperties();
            catXmlPath = props.getProperties("CATEGORY_XML_PATH");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        service = CategoryServiceImpl.getInstance(catXmlPath);
    }

    public CategoryDelegate(String categoryName) {
        String catXmlPath = null;
        /*try {
            LoadProperties props = new LoadProperties();
            catXmlPath = props.getProperties(categoryName);
            System.out.println("catXmlPath======" + catXmlPath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        catXmlPath = categoryName;//直接获取xml
        logger.info("CategoryDelegate类获取xml配置地址============>>" + catXmlPath);
        service = CategoryServiceImpl.getInstance(catXmlPath);
    }
    
    /**
     * 获取根栏目
     * @return CategoryDto
     */
    public CategoryDto getRootCat() {
        if (service != null) {
            return service.getRootCat();
        }
        return null;
    }

    /**
     * 获取单个栏目
     * @param catId String
     * @return CategoryDto
     */
    public CategoryDto getCat(String catId) {
        if (service != null) {
            return service.getCat(catId);
        }
        return null;
    }

    /**
     * 获取所有子栏目
     * @return CategoryDto[]
     */
    public CategoryDto[] getSubCats() {
        if (service != null) {
            return service.getSubCats();
        }
        return null;
    }

    /**
     * 获取指定栏目的子栏目
     * @param catId String
     * @return CategoryDto[]
     */
    public CategoryDto[] getSubCats(String catId) {
        if (service != null) {
            return service.getSubCats(catId);
        }
        return null;
    }

    public boolean clearInstances() {
        if (service != null) {
            return service.clearInstances();
        }
        return false;
    }
}
