package com.cmwa.ec.webapp.common.category;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.cmwa.ec.webapp.common.category.CategoryDao;

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
public class CategoryXmlDao implements CategoryDao {
    private String xmlPath;
    private SAXBuilder builder;
    private Document doc;
    private Element rootElem;

    public CategoryXmlDao(String xmlPath) throws Exception {
        this.xmlPath = xmlPath;
        try {
            builder = new SAXBuilder(false);
            doc = builder.build(xmlPath);
            rootElem = doc.getRootElement();
        }
        catch (JDOMException e) {
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public CategoryDto getRootCat() {
        CategoryDto dto = null;
        List rootList = rootElem.getChildren("root-category");
        if (rootList != null && rootList.size() > 0) {
            Element rootCatElem = (Element) rootList.get(0);
            String id = rootCatElem.getAttributeValue("id");
            String name = rootCatElem.getAttributeValue("name");
            String level = rootCatElem.getAttributeValue("level");
            String defaultId = rootCatElem.getAttributeValue("default-id");
            String url = rootCatElem.getChildTextTrim("url");
            String param = rootCatElem.getChildTextTrim("param");
            dto = new CategoryDto();
            dto.setId(id);
            dto.setName(name);
            dto.setLevel(level);
            dto.setDefaultId(defaultId);
            dto.setUrl(url);
            dto.setParam(param);
        }
        return dto;
    }

    public CategoryDto[] getSubCats() {
        CategoryDto[] dtos = null;
        List dtosList = null;

        List subList = rootElem.getChildren("sub-category");
        if (subList != null && subList.size() > 0) {
            dtosList = new ArrayList();
            Iterator iter = subList.iterator();
            while (iter.hasNext()) {
                Element subCatElem = (Element) iter.next();
                String id = subCatElem.getAttributeValue("id");
                String name = subCatElem.getAttributeValue("name");
                String level = subCatElem.getAttributeValue("level");
                String defaultId = subCatElem.getAttributeValue("default-id");
                String parentId = subCatElem.getChildTextTrim("parent-id");
                String url = subCatElem.getChildTextTrim("url");
                String param = subCatElem.getChildTextTrim("param");
                CategoryDto dtoTmp = new CategoryDto();
                dtoTmp.setId(id);
                dtoTmp.setName(name);
                dtoTmp.setLevel(level);
                dtoTmp.setDefaultId(defaultId);
                dtoTmp.setParentId(parentId);
                dtoTmp.setUrl(url);
                dtoTmp.setParam(param);
                dtosList.add(dtoTmp);
                dtoTmp = null;
            }
        }

        if (dtosList != null && dtosList.size() > 0) {
            dtos = new CategoryDto[dtosList.size()];
            dtos = (CategoryDto[])dtosList.toArray(dtos);
        }
        return dtos;
    }
}
