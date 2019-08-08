package com.cmwa.ec.webapp.common.category;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

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
public class CategoryServiceImpl implements CategoryService {
    private CategoryDto rootCat;
    private CategoryDto[] subCats;
    private static Hashtable instances = new Hashtable();
    private static Logger logger = Logger.getLogger(CategoryServiceImpl.class.getName());

    private CategoryServiceImpl(String xmlPath) {
        try {
            CategoryDao dao = new CategoryXmlDao(xmlPath);
            CategoryDto[] subCatsTmp = null;
            logger.info("CategoryServiceImpl类开始初始化栏目<<===========");
            //初始化根栏目
            rootCat = dao.getRootCat();
            //初始化子栏目
            subCats = dao.getSubCats();

            //初始化根栏目的子栏目
            subCatsTmp = getSubCats(rootCat.getId());
            rootCat.setSubCats(subCatsTmp);

            //初始化子栏目的子栏目
            if (subCats != null) {
                for (int i = 0; i < subCats.length; i++) {
                    if (subCats[i] != null) {
                        subCatsTmp = getSubCats(subCats[i].getId());
                        subCats[i].setSubCats(subCatsTmp);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CategoryService getInstance(String xmlPath)  {
        if (xmlPath == null) {
            return null;
        }
        if (instances.containsKey(xmlPath)) {
            return (CategoryService)instances.get(xmlPath);
        }
        else {
            CategoryService instance = new CategoryServiceImpl(xmlPath);
            instances.put(xmlPath, instance);
            return instance;
        }
    }

    public CategoryDto getRootCat() {
        return rootCat;
    }

    public CategoryDto getCat(String catId) {
        if (catId.equals(rootCat.getId())) {
            return rootCat;
        }
        if (subCats != null) {
            for (int i = 0; i < subCats.length; i++) {
                CategoryDto dtoTmp = subCats[i];
                if (dtoTmp != null) {
                    if (catId.equals(dtoTmp.getId())) {
                        return dtoTmp;
                    }
                }
            }
        }
        return null;
    }

    public CategoryDto[] getSubCats() {
        return subCats;
    }

    public CategoryDto[] getSubCats(String catId) {
        if (catId == null) {
            return null;
        }
        CategoryDto[] subDtos = null;
        List subDtosList = null;
        if (subCats != null) {
            subDtosList = new ArrayList();
            for (int i = 0; i < subCats.length; i++) {
                CategoryDto dtoTmp = subCats[i];
                if (dtoTmp != null) {
                    if (catId.equals(dtoTmp.getParentId())) {
                        subDtosList.add(dtoTmp);
                    }
                }
                dtoTmp = null;
            }
        }
        if (subDtosList != null && subDtosList.size() > 0) {
            subDtos = new CategoryDto[subDtosList.size()];
            subDtos = (CategoryDto[])subDtosList.toArray(subDtos);
        }
        return subDtos;
    }

    /**
     * 清除实例
     * @return boolean
     */
    public boolean clearInstances() {
        instances.clear();
        return true;
    }
}
