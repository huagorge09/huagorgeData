package com.cmwa.ec.webapp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Dto转换工具类<br>
 * 将两个类似同构的dto相互转换工具类
 * @author wanggang
 *
 */
public class DtoConvertUtils{
//	private static Log log = LogFactory.getLog(DtoConvertUtils.class);
	private static Logger log = Logger.getLogger(DtoConvertUtils.class.getName());
	
	/**
	 * 将两个类似同构的dto相互转换
	 * @param dto
	 * @param bearerDto
	 */
	public static void convertDto(Object dto,Object bearerDto){
		if(dto == null || bearerDto == null)
			return;
		
		Class clazz = bearerDto.getClass();
		
		Field[] fields = clazz.getDeclaredFields();
		Class superClass = clazz.getSuperclass();
		log.info(superClass.getName());
		
		List<Field> tmpList = new ArrayList<Field>(); 
		for(Field f : fields)
		{
			tmpList.add(f);
		}
		if(superClass.getName().contains("BaseResult"))
		{
			Field[] fieldTs = superClass.getDeclaredFields();
			for(Field f : fieldTs)
			{
				tmpList.add(f);
			}
		}
		
		
		Object value = null;
		Object valueT = null;
		Field field = null;
		String methodName = null;
		Method method = null;
		//Method methodT = null;
		for(int i = 0; i < tmpList.size(); i++){
			field = tmpList.get(i);
			methodName = field.getName();
//			methodName = getGetMethod(methodName);
			try{
//				method = dto.getClass().getMethod(methodName, null);
				method = getGetMethod(dto.getClass().getMethods(), methodName);
				if(method == null)
				{
					log.info(clazz+"没有"+methodName+"方法");
					continue;
				}
				
				value = method.invoke(dto, null);
//				log.info("[DEBUG]" + field.getType().getName());
				if(field.getType().getName().contains("com.cmwa"))
				{
					//methodT = bearerDto.getClass().getMethod(methodName, null);
					//valueT = methodT.invoke(bearerDto, null);
					valueT = convertDto(value, field.getType());
					
					field.setAccessible(true);
					field.set(bearerDto, valueT);
				}
				else
				{
//					log.info("[DEBUG]" + dto.getClass() + "\t" + field.getName() + "\t"+value);
					field.setAccessible(true);
					field.set(bearerDto, value);
				}
				
			} catch (Exception e){
				log.info(e);
				log.info(dto.getClass()+"没有"+methodName+"方法");
			} 
		}
	}
	
	
	
	public static Object convertDto(Object dto,Class tarClass){
		Object obj = null;
		try{
			obj = tarClass.newInstance();
		} catch (Exception e){
			e.printStackTrace();
		} 
		convertDto(dto,obj);
		return obj;
	}
	
	
	
	/**
	 * 得到属性的get方法
	 * @param field
	 * @return
	 */
	public static String getGetMethod(String field){
		return "get"+field.substring(0, 1).toUpperCase()+field.substring(1);
	}
	
	
	/**
	 * 得到属性的get方法
	 * @param field
	 * @return
	 */
	public static Method getGetMethod(Method[] methods, String fieldName){
		for(int i=0; null != methods && i<methods.length; i++)
		{
			if(null != methods[i])
			{
				String methodName = methods[i].getName();
//				log.debug("[TEST]" + methodName + "\t" + fieldName);
				if(methodName.startsWith("get") && methodName.toUpperCase().contains(fieldName.toUpperCase()))
				{					
					return methods[i];
				}
			}
		}
		return null;
	}
	
	public static List getConvertList(List list,Class clazz){
		if(list == null || list.isEmpty() || clazz == null)
			return new ArrayList();
		
		List retrunList = new ArrayList();
		for(int i=0;i<list.size();i++){
			try{
				Object obj = clazz.newInstance();
				convertDto(list.get(i),obj);
				retrunList.add(obj);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return retrunList;
	}
}