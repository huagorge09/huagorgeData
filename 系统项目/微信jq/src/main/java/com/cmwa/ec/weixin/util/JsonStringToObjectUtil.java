package com.cmwa.ec.weixin.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * @
 * 传入一个json字符串，根据Java反射，获取入参类中所有属性和set方法，
 * 将json字符串中有的属性 ，都设置到入参类的属性中
 * （循环获取类的属性，若json字符串中有此属性，则将对应值设置给该属性，若json字符串中无此属性，则不为该属性赋值）
 * @author liury
 *
 */
public class JsonStringToObjectUtil {

	private static Logger logger = Logger.getLogger(JsonStringToObjectUtil.class.getName());
	
	public static Object jsonStringToObject(String str,String className){
		//String str = "{\"subscribe\":1,\"openid\":\"ovWiouDsjV4q-Ucxnh1SCCkc7PWs\",\"bool\":false,\"date\":2015-05-12,\"nickname\":\"后知厚觉。\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"襄阳\",\"province\":\"湖北\",\"country\":\"中国\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/HBqkWia1E928Ws7zfb7OIwic57kw22nTNibK7H2h2IicR7icMRZQ3ibevCJibmkwx6ml8GxrPd10PDu8alVndFqsgmhYtxz5Yib6HRgD/0\",\"subscribe_time\":1431413998,\"remark\":\"\",\"groupid\":0}";
		
		if(str==null || "".equals(str) || className==null || "".equals(className)){
			logger.info("JsonStringToObjectUtil--json字符串转换为Java类：入参为空！className="+className+" ,  str="+str);
			return null;
		}
		JSONObject json = JSONObject.fromObject(str);
		
		Object model = null;
		
        try {
        	
        	model = (Object)Class.forName(className.trim()).newInstance();
        	
        	Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        	
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                   // String value = (String) m.invoke(model); // 调用getter方法获取属性值
                   //if (value == null) {
                        m = model.getClass().getMethod("set"+name,String.class);
                        if(json.get(field[j].getName())!=null&& !"".equals(json.get(field[j].getName()))){
                        	m.invoke(model, ""+json.get(field[j].getName()));
                        }else{
                        	m.invoke(model, "");
                        }
                   // }
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = model.getClass().getMethod("get" + name);
                    m = model.getClass().getMethod("set"+name,Integer.class);
                    if(json.get(field[j].getName())!=null&& !"".equals(json.get(field[j].getName()))){
                    	m.invoke(model, json.get(field[j].getName()));
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = model.getClass().getMethod("get" + name);
                    m = model.getClass().getMethod("set"+name,Boolean.class);
                    if(json.get(field[j].getName())!=null&& !"".equals(json.get(field[j].getName()))){
                    	m.invoke(model, json.get(field[j].getName()));
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = model.getClass().getMethod("get" + name);
                    m = model.getClass().getMethod("set"+name,Date.class);
                    if(json.get(field[j].getName())!=null&& !"".equals(json.get(field[j].getName()))){
                    	Date date = null;
                    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    	try {
                    		date = sdf.parse(""+json.get(field[j].getName()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
                    	m.invoke(model, date);
                    }
                }
                // 如果有需要,可以依照上面继续进行扩充,再增加对其它类型的判断
            }
            logger.info("JsonStringToObjectUtil--json字符串："+str);
            logger.info("JsonStringToObjectUtil--Java类全名:"+className);
            logger.info("JsonStringToObjectUtil--转换结果："+model.toString());
        } catch (Exception e) {
        	logger.error("json to object 异常",e);
		}
        return model;
	}
	
}
