package com.cmwa.ec.weixin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 类说明-Workbook工具类
 * 
 * @author ex-chenhq
 * 
 */
public class ParseExcel {

	static Logger logger = LoggerFactory.getLogger(ParseExcel.class.getName());

	public static List<String> getRowValue(String filePath,int sheetIdx){
		Workbook book;
		List<String> rowValue = null;
		try {
			book = getBook(filePath);
			Sheet sheet = book.getSheetAt(sheetIdx);
			if(sheet == null){
				throw new IOException("当前工作表为空");
			}
			rowValue = getRowValue(sheet,sheet.getLastRowNum());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rowValue;
	}

	/**
	 * 获取book对象
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Workbook getBook(String fileName) throws IOException {
		Workbook workbook = null;
		if (null != fileName) {
			InputStream is = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(is);// 创建 Excel 2007 工作簿对象
		}
		return workbook;
	}

	/**
	 * 返回一个以列形式的数据
	 * 
	 * @param sheet
	 * @param rw
	 * @return
	 */
	public static List<String> getRowValue(Sheet sheet, int rw) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < rw + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				Cell cell = row.getCell(0);
				list.add(String.valueOf(getJavaValue(cell)));
			}
		}
		return list;
	}
	
    /**
     * 获取数据类型返回值
     * @param cell
     * @return
     */
    private static Object getValueOfNumericCell(Cell cell) {
        Boolean isDate = DateUtil.isCellDateFormatted(cell);
        Double d = cell.getNumericCellValue();
        Object o = null;
        if (isDate) {
            o = DateUtils.formatDate(cell.getDateCellValue(), DateUtils.yyyy_MM_dd);
        } else {
            o = getRealStringValueOfDouble(d);
        }
        return o;
    }
    
    /**
     * 处理科学计数法与普通计数法的字符串显示，尽最大努力保持精度
     * @param d
     * @return
     */
    private static String getRealStringValueOfDouble(Double d) {
        String doubleStr = d.toString();
        boolean b = doubleStr.contains("E");
        int indexOfPoint = doubleStr.indexOf('.');
        if (b) {
            int indexOfE = doubleStr.indexOf('E');
            // 小数部分
            BigInteger xs = new BigInteger(doubleStr.substring(indexOfPoint
                    + BigInteger.ONE.intValue(), indexOfE));
            // 指数
            int pow = Integer.valueOf(doubleStr.substring(indexOfE
                    + BigInteger.ONE.intValue()));
            int xsLen = xs.toByteArray().length;
            int scale = xsLen - pow > 0 ? xsLen - pow : 0;
            doubleStr = String.format("%." + scale + "f", d);
        } else {
            java.util.regex.Pattern p = Pattern.compile(".0$");
            java.util.regex.Matcher m = p.matcher(doubleStr);
            if (m.find()) {
                doubleStr = doubleStr.replace(".0", "");
            }
        }
        return doubleStr;
    }
	
 	/**
     * 根据不同情况获取Java类型值
     * 
     * @param cell Cell类型单元格
     * @return 返回Object类型值
     */
    public static Object getJavaValue(Cell cell) {
        Object o = null;
        int cellType = -1;
        try{
        	cellType = cell.getCellType();
        }catch(NullPointerException e){
        	// 如果为空指针则默认返回空值
        	cellType = 3;
        }
        switch (cellType) {
        case Cell.CELL_TYPE_BLANK:
            o = "";
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            o = cell.getBooleanCellValue();
            break;
        case Cell.CELL_TYPE_ERROR:
            o = "Bad value!";
            break;
        case Cell.CELL_TYPE_NUMERIC:
        	o = getValueOfNumericCell(cell);
            break;
        case Cell.CELL_TYPE_FORMULA:
            try {
                o = getValueOfNumericCell(cell);
            } catch (IllegalStateException e) {
                try {
                    o = cell.getRichStringCellValue().toString();
                } catch (IllegalStateException e2) {
                    o = cell.getErrorCellValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        default:
            o = cell.getRichStringCellValue().getString();
        }
        return o;
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                             

}
