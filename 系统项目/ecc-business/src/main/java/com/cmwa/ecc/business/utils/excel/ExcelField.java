package com.cmwa.ecc.business.utils.excel;

import java.lang.annotation.*;
 
/**
 * Package：com.gomsws.util
 * Author：tanyong
 * Date： 2016/12/2
 * Desc：Excel字段名
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {
 
    public String fieldName();

}