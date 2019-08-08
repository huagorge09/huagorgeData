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
public interface CategoryDao {

    /**
     * 获取根栏目
     * @return CategoryDto
     * @throws Exception
     */
    CategoryDto getRootCat();

    /**
     * 获取子栏目
     * @return CategoryDto[]
     * @throws Exception
     */
    CategoryDto[] getSubCats();

}
