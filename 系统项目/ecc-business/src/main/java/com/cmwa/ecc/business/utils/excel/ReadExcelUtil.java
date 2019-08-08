package com.cmwa.ecc.business.utils.excel;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmwa.ec.base.util.StringUtil;
 
/**
 * 
 * @author ex-chenbq
 * Desc：excel解析工具类
 */
public class ReadExcelUtil {
	
    private static Logger logger = LoggerFactory.getLogger(ReadExcelUtil.class);
 
    /**
     * 设置字段值
     * @param object
     * @param excelFiledName
     * @param value
     */
    public static void setFiledValue(Object object,String excelFiledName,String value){
    	Class<? extends Object> classz = object.getClass();
        Field[] fields = classz.getDeclaredFields();
 
        for(Field field : fields){
            String filedName = field.getName();
             ExcelField excelField = field.getAnnotation(ExcelField.class);
             if(excelField != null){
                 filedName = excelField.fieldName();
            }
            
            String orgFiledName = field.getType().getName();
            String filedTypeName = orgFiledName.toUpperCase();
 
            if(filedName.indexOf(excelFiledName) != -1 || excelFiledName.indexOf(filedName) != -1){
                field.setAccessible(true);
                try {
                    if(isNumeric(value)){
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        Number number = numberFormat.parse(value);
                        if(filedTypeName.contains("INT")){
                            field.set(object, number.intValue());
                        }else if(filedTypeName.contains("DOUBLE")){
                            field.set(object, number.doubleValue());
                        }else if(filedTypeName.contains("FLOAT")){
                            field.set(object, number.floatValue());
                        }else if(filedTypeName.contains("LONG")){
                            field.set(object, number.longValue());
                        }else if(filedTypeName.contains("SHORT")){
                            field.set(object, number.shortValue());
                        }else if(filedTypeName.contains("STRING")){
                            field.set(object, number.toString());
                        }
                    }else {
                        if(filedTypeName.contains("BOOLEAN")){
                            field.set(object,Boolean.valueOf(value));
                        }else{
                            field.set(object,value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("暂不支持的数据类型["+orgFiledName+"]");
                }
                break;
            }
        }
    }
 
    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
 
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC: // 数字
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                SimpleDateFormat sdf = null;
                // 验证short值
                if (cell.getCellStyle().getDataFormat() == 14) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (cell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                } else if (cell.getCellStyle().getDataFormat() == 22) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    throw new RuntimeException("日期格式错误!!!");
                }
                Date date = cell.getDateCellValue();
                cellValue = sdf.format(date);
            } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
            	double value = cell.getNumericCellValue();
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                if (String.valueOf(value).length() < cellValue.length()) {
                	cellValue = String.valueOf(value);
				}
            } else if (cell.getCellStyle().getDataFormatString().indexOf("%") != -1) {
            	cell.setCellValue(cell.getNumericCellValue()*100);
            	cell.setCellType(Cell.CELL_TYPE_STRING);
                cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                if(!StringUtil.isEmpty(cellValue) && cellValue.lastIndexOf(".0") != -1) {
                	cellValue = cellValue.substring(0, cellValue.length()-2);
                }
            } else {
            	BigDecimal b = new BigDecimal(cell.getNumericCellValue());
        		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            	cellValue = String.valueOf(f1);
            }
            break;
        case Cell.CELL_TYPE_STRING: // 字符串
            cellValue = String.valueOf(cell.getStringCellValue());
            break;
        case Cell.CELL_TYPE_BOOLEAN: // Boolean
            cellValue = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_FORMULA: // 公式
        	//cellValue = String.valueOf(cell.getCellFormula());
            try {
            	cellValue = String.valueOf(cell.getDateCellValue());
            	SimpleDateFormat sdf = null;
                // 验证short值
                if (cell.getCellStyle().getDataFormat() == 14) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (cell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                } else if (cell.getCellStyle().getDataFormat() == 22) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    throw new RuntimeException("日期格式错误!!!");
                }
                Date date = cell.getDateCellValue();
                cellValue = sdf.format(date);
            } catch (IllegalStateException e) {
            	cellValue = String.valueOf(cell.getCellFormula());
            }
            break;
        case Cell.CELL_TYPE_BLANK: // 空值
            cellValue = null;
            break;
        case Cell.CELL_TYPE_ERROR: // 故障
            cellValue = "非法字符";
            break;
        default:
            cellValue = "未知类型";
            break;
        }
        return cellValue;
    }
    
    /**
     * 设置字段值
     * @param object
     * @param excelFiledName
     * @param value
     */
    public static void setFiledValue(int index, Object object,String excelFiledName,String value){
    	Class<? extends Object> classz = object.getClass();
        Field[] fields = classz.getDeclaredFields();
        
        for(Field field : fields){
            String filedName = field.getName();
             ExcelField excelField = field.getAnnotation(ExcelField.class);
             if(excelField != null){
                 filedName = excelField.fieldName();
            }
            
            String orgFiledName = field.getType().getName();
            String filedTypeName = orgFiledName.toUpperCase();
 
            if(excelFiledName.equals(filedName)){
                field.setAccessible(true);
                try {
                    if(ReadExcelUtil.isNumeric(value)){
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        Number number = numberFormat.parse(value);
                        if(filedTypeName.contains("INT")){
                            field.set(object, number.intValue());
                        }else if(filedTypeName.contains("DOUBLE")){
                            field.set(object, number.doubleValue());
                        }else if(filedTypeName.contains("FLOAT")){
                            field.set(object, number.floatValue());
                        }else if(filedTypeName.contains("LONG")){
                            field.set(object, number.longValue());
                        }else if(filedTypeName.contains("SHORT")){
                            field.set(object, number.shortValue());
                        }else if(filedTypeName.contains("STRING")){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
                            field.set(object, value);
                        }
                    }else {
                        if(filedTypeName.contains("BOOLEAN")){
                            field.set(object,Boolean.valueOf(value));
                        }else{
                            field.set(object,value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
