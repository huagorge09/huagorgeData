package com.cmwa.ec.webapp.common.category;


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
public interface CategoryService {
    /**
     * 获取根栏目
     * @return CategoryDto
     */
    CategoryDto getRootCat();

    /**
     * 获取单个栏目
     * @param catId String
     * @return CategoryDto
     */
    CategoryDto getCat(String catId);

    /**
     * 获取所有子栏目
     * @return CategoryDto[]
     */
    CategoryDto[] getSubCats();

    /**
     * 获取指定栏目的子栏目
     * @param catId String
     * @return CategoryDto[]
     */
    CategoryDto[] getSubCats(String catId);

    /**
     * 清除实例
     * @return boolean
     */
    boolean clearInstances();
}
