package com.cmwa.ec.webapp.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;

 


/**
*  json 转换帮助类
 *niedc  2014-12-24
 */

public class JsonUtils {

	/***
	 * 将传过来的实体转换成JSON格式
	 * @param o
	 * @return
	 */
	public static String toJson(Object o){
		  try{
			  	StdSerializerProvider sp = new StdSerializerProvider();
			  	sp.setNullValueSerializer(new NullSerializer());
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setSerializerProvider(sp);
				
				return objectMapper.writeValueAsString(o);
			}
			catch(Exception ex){
				ex.printStackTrace();
				return "";
			}
	  }
	  
	  public static class NullSerializer extends JsonSerializer
	  {
	     public void serialize(Object value, JsonGenerator jgen,
	    		 	SerializerProvider provider)
	     throws IOException, JsonProcessingException
	     {
	         jgen.writeString("");
	     }
	  }

}
