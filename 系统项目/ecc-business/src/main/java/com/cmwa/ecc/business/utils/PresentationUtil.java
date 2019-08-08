package com.cmwa.ecc.business.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
*
* <p>Title: 展示层工具类</p>
*
* <p>Description: 工具类，供展示层调用</p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: Legion Technology</p>
*
* @author xuhw
* @version 1.0
*/

public class PresentationUtil {
   /**
    * yyyyMMdd日期格式化样式
    */
   public static final String yyyyMMdd = "yyyyMMdd";

   /**
    * yyyy-MM-dd日期格式化样式
    */
   public static final String yyyy_MM_dd = "yyyy-MM-dd";

   /**
    * HHmmss日期格式化样式
    */
   public static final String HHmmss = "HHmmss";

   /**
    * HH:mm:ss日期格式化样式
    */
   public static final String HH_mm_ss = "HH:mm:ss";

   /**
    * yyyyMMddHHmmss日期格式化样式
    */
   public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
   /**
    * yyyy年M月d日H时m分日期格式化样式
    */
   public static final String yyyy$M$d$H$m$ = "yyyy年M月d日H时m分";

   /**
    * yyyy年M月d日H时m分日期格式化样式
    */
   public static final String yyyy$MM$dd$ = "yyyy年MM月dd日";

   /**
    * yyyyMMdd日期格式化工具
    */
   public static final SimpleDateFormat DF_yyyyMMdd = new SimpleDateFormat(yyyyMMdd);

   /**
    * yyyy-MM-dd日期格式化工具
    */
   public static final SimpleDateFormat DF_yyyy_MM_dd = new SimpleDateFormat(yyyy_MM_dd);

   /**
    * HHmmss日期格式化工具
    */
   public static final SimpleDateFormat DF_HHmmss = new SimpleDateFormat(HHmmss);

   /**
    * HH:mm:ss日期格式化工具
    */
   public static final SimpleDateFormat DF_HH_mm_ss = new SimpleDateFormat(HH_mm_ss);

   /**
    * yyyyMMddHHmmss日期格式化工具
    */
   public static final SimpleDateFormat DF_yyyyMMddHHmmss = new SimpleDateFormat(yyyyMMddHHmmss);

   /**
    *  yyyy年M月d日H时m分日期格式化工具
    */
   public static final SimpleDateFormat DF_yyyy$M$d$H$m$ = new SimpleDateFormat(yyyy$M$d$H$m$);

   /**
    *  yyyy年M月d日H时m分日期格式化工具
    */
   public static final SimpleDateFormat DF_yyyy$MM$dd$ = new SimpleDateFormat(yyyy$MM$dd$);


   /**
    * 2位小数格式化工具
    */
   public static final NumberFormat NF_2FRACTION = NumberFormat.getNumberInstance();
   static {
       NF_2FRACTION.setMinimumFractionDigits(2);
       NF_2FRACTION.setMaximumFractionDigits(2);
   }

   /**
    * 4位小数格式化工具
    */
   public static final NumberFormat NF_4FRACTION = NumberFormat.getNumberInstance();
   static {
       NF_4FRACTION.setMinimumFractionDigits(4);
       NF_4FRACTION.setMaximumFractionDigits(4);
   }

   /**
    * 资金转换工具，转换后100,000
    */
   public static final DecimalFormat DEF_CASH = new DecimalFormat(",##0");

   /**
    * 资金转换工具，转换后100 000
    */
   public static final DecimalFormat DEF_CASH_NO_COMMA = new DecimalFormat("##0");
   
   /**
    * 资金转换工具，转换后100000.00 2010-05-19 zhangq
    */   
   public static final DecimalFormat DEF_CASH_COMMON = new DecimalFormat("##0.00"); 


   /**
    * 隐藏字符串开始位置
    */
   public static final int HIDDEN_STRING_START = 4;
   /**
    * 隐藏字符串结束位置
    */
   public static final int HIDDEN_STRING_END = 6;
   /**
    * 显示最后几位
    */
   public static final int HIDDEN_STRING_LAST = 4;
   /**
    * 隐藏字符串
    */
   private static final String HIDDEN_STRING = "******";

   public static final int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
   public static final int ROUND_STRIP = 99999999;

   public static final String ALERT_PRESENT_FULL = "FULL";
   public static final String ALERT_PRESENT_MINI = "MINI";
   public static final String ALERT_PRESENT_POP = "POP";

   public static final String ERROR_PRESENT_FULL = "FULL";
   public static final String ERROR_PRESENT_MINI = "MINI";
   public static final String ERROR_PRESENT_POP = "POP";

   private PresentationUtil() {
   }

   /**
    * 将特殊字符转为html编码
    * @param orgStr String
    * @return String
    */
   public static String encodeHTML(String orgStr) {
       if (orgStr == null) {
           return "&nbsp;";
       }
       int orgStrLen = orgStr.length();

       StringBuffer result = new StringBuffer(6 * orgStrLen);

       for (int i = 0; i < orgStrLen; i++) {
           char c = orgStr.charAt(i);
           switch (c) {
           case '<':
               result.append("&lt;");
               break;

           case '>':
               result.append("&gt;");
               break;

           case '&':
               if (!(orgStr.startsWith("&lt;", i) ||
                     orgStr.startsWith("&gt;", i) ||
                     orgStr.startsWith("&amp;", i) ||
                     orgStr.startsWith("&quot;", i) ||
                     orgStr.startsWith("&#39;", i) ||
                     orgStr.startsWith("&#9;", i) ||
                     orgStr.startsWith("&#10;", i) ||
                     orgStr.startsWith("&nbsp;", i) ||
                     orgStr.startsWith("&#13;", i))) {
                   result.append("&amp;");
               }
               else {
                   result.append(c);
               }
               break;

           case '\"':
               result.append("&quot;");
               break;

           case '\'':
               result.append("&#39;");
               break;

           case '\t':
               result.append("&#9;");
               break;

           case '\n':
               result.append("&#10;");
               break;

           case '\r':
               result.append("&#13;");
               break;
           /*
           case ' ':
                   result.append("&nbsp;");
                   break;
           */

           default:
               result.append(c);
               break;
           }
       }
       return result.toString();
   }

   /**
    * 将特殊字符转为html编码
    * @param orgStr String
    * @return String
    */
   public static String encodeUrl(String orgStr) {
       if (orgStr == null) {
           return null;
       }
       int orgStrLen = orgStr.length();

       StringBuffer result = new StringBuffer(6 * orgStrLen);

       for (int i = 0; i < orgStrLen; i++) {
           char c = orgStr.charAt(i);
           switch (c) {
           case '?':
               result.append("%3F");
               break;

           case '&':
               result.append("%26");
               break;

           case '=':
               result.append("%3D");
               break;

           default:
               result.append(c);
               break;
           }
       }
       return result.toString();
   }

   /**
    * 将html编码解码为正常编码
    * @param orgStr String
    * @return String
    */
   public static String decodeHTML(String orgStr) {

       if (orgStr == null) {
           return "";
       }
       StringBuffer orgStrBuffer = new StringBuffer(orgStr);

       int idx1 = 0;

       while ((idx1 = orgStr.indexOf("&quot;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&quot;".length(), "\"");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&lt;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&lt;".length(), "<");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&gt;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&gt;".length(), ">");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&amp;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&amp;".length(), "&");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&#39;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&#39;".length(), "\'");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&#9;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&#9;".length(), "\t");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&#10;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&#10;".length(), "\n");
           orgStr = orgStrBuffer.toString();
       }

       while ((idx1 = orgStr.indexOf("&#13;")) != -1) {
           orgStrBuffer.replace(idx1, idx1 + "&#13;".length(), "\r");
           orgStr = orgStrBuffer.toString();
       }

       return orgStr;
   }

   public static String convertNull(String orgStr) {
       if (orgStr == null) {
           return "";
       }
       else {
           return orgStr;
       }
   }

   /**
    * 隐藏部分字串
    * @param orgStr
    * @param start 开始隐藏位
    * @param end 结束隐藏位
    * @return
    * String
    */
   public static String hiddenStr(String orgStr, int start, int end) {
       if (orgStr == null) {
           return HIDDEN_STRING;
       }
       StringBuffer strbTmp = new StringBuffer();
       if (start > end) {
           return HIDDEN_STRING;
       }
       try {
           strbTmp.append(orgStr.substring(0, start - 1));
           int hiddenCount = end - start;
           int i = 1;
           while (hiddenCount >= 0 && i <= HIDDEN_STRING.length()) {
               strbTmp.append("*");
               hiddenCount = hiddenCount - 1;
               i++;
           }
           strbTmp.append(orgStr.substring(end, orgStr.length()));
       }
       catch (Exception e) {
           return HIDDEN_STRING;
       }
       return strbTmp.toString();
   }

   /**
    * 显示 ****** + 后几位
    * @param orgStr String 原字符串
    * @param lastCount int 显示后几位
    * @return String
    */
   public static String hiddenStr(String orgStr, int lastCount) {
       if (orgStr == null) {
           return HIDDEN_STRING;
       }
       else if (orgStr.length() <= lastCount) {
           return orgStr;
       }
       else {
           String strTmp = orgStr.substring(orgStr.length() - lastCount);
           strTmp = HIDDEN_STRING + strTmp;
           return strTmp;
       }
   }

   /**
   * 显示 hidStr + 后几位
   * @param hidStr String 隐藏符
   * @param orgStr String 原字符串
   * @param lastCount int 显示后几位
   * @return String
   */
   public static String hiddenStr(String hidStr, String orgStr, int lastCount) {
       if (orgStr == null) {
           return hidStr;
       }
       else if (orgStr.length() <= lastCount) {
           return orgStr;
       }
       else {
           String strTmp = orgStr.substring(orgStr.length() - lastCount);
           strTmp = hidStr + strTmp;
           return strTmp;
       }
   }

   /**
    * 只显示后几位
    * @param orgStr String 原字符串
    * @param lastCount int 显示后几位
    * @return String
    */
   public static String displayStr(String orgStr, int lastCount) {
       if (orgStr == null) {
           return HIDDEN_STRING;
       }
       else if (orgStr.length() <= lastCount) {
           return orgStr;
       }
       else {
           String strTmp = orgStr.substring(orgStr.length() - lastCount);
           strTmp = strTmp;
           return strTmp;
       }
   }

   /**
    * 创建下拉框表单
    * @param selectName String 表单名称
    * @param className String 表单class名称
    * @param jsName String 表单onchange时触发的js名称
    * @param codeName String[][] 下拉框元素，1位为代码，2位为名称
    * @param selectedCode String 被选中元素代码
    * @param firstOption 第一元素名称，比如：请选择，全部，""，null
    * @return String 直接可以输出到页面，为一个下拉框表单
    */
   public static String buildSelect(String selectName, String className,
                                  String jsName, String[][] codeName,
                                  String selectedCode, String firstOption) {
       StringBuffer html_code = new StringBuffer();
       html_code.append("<select name=\"" + selectName + "\"");
       if (className != null && !className.equals("")) {
           html_code.append(" class=\"" + className + "\"");
       }
       if (jsName != null && !jsName.equals("")) {
           html_code.append(" onChange=\"" + jsName + "\"");
       }
       html_code.append(">\r\n");
       if (firstOption != null) {
           html_code.append("<option value=\"\"> " + firstOption + " </option>\r\n");
       }
       html_code.append(buildOption(codeName, selectedCode));
       html_code.append(" </select>");

       return html_code.toString();
   }
   
   /**
    * 创建下拉框表单
    * @param selectName String 表单名称
    * @param className String 表单class名称
    * @param jsName String 表单onchange时触发的js名称
    * @param codeName String[][] 下拉框元素，1位为代码，2位为名称
    * @param selectedCode String 被选中元素代码
    * @param firstOption 第一元素名称，比如：请选择，全部，""，null
    * @param selectWidth 表单样式
    * @return String 直接可以输出到页面，为一个下拉框表单
    */
   public static String buildSelect(String selectName, String className,
                                  String jsName, String[][] codeName,
                                  String selectedCode, String firstOption,
                                  String selectSytle) {
       StringBuffer html_code = new StringBuffer();
       html_code.append("<select style=\"" + selectSytle + "\" name=\"" + selectName + "\"");
       if (className != null && !className.equals("")) {
           html_code.append(" class=\"" + className + "\"");
       }
       if (jsName != null && !jsName.equals("")) {
           html_code.append(" onChange=\"" + jsName + "\"");
       }
       html_code.append(">\r\n");
       if (firstOption != null) {
           html_code.append("<option value=\"\"> " + firstOption + " </option>\r\n");
       }
       html_code.append(buildOption(codeName, selectedCode));
       html_code.append(" </select>");

       return html_code.toString();
   }

   /**
    * 创建下拉框表单
    * @param selectName String 表单名称
    * @param className String 表单class名称
    * @param jsName String 表单onchange时触发的js名称
    * @param codeName String[][] 下拉框元素，1位为代码，2位为名称
    * @param selectedCode String 被选中元素代码
    * @param isFirstBlank boolean 第一元素是否为空白
    * @param disabled boolean 表单是否被禁止
    * @return String 直接可以输出到页面，为一个下拉框表单
    */
   public static String buildSelect(String selectName, String className,
                                  String jsName, String[][] codeName,
                                  String selectedCode, boolean isFirstBlank,
                                  boolean disabled) {
       StringBuffer html_code = new StringBuffer();
       //html_code.append("<select name=\"" + selectName + "\"");
       html_code.append("<select style=\"width:180px\" name=\"" + selectName + "\"");
       if (className != null && !className.equals("")) {
           html_code.append(" class=\"" + className + "\"");
       }
       if (jsName != null && !jsName.equals("")) {
           html_code.append(" onChange=\"" + jsName + "\"");
       }
       if (disabled) {
           html_code.append(" disabled");
       }
       html_code.append(">\r\n");
       if (isFirstBlank) {
           html_code.append("<option value=\"\"> 全部 </option>\r\n");
       }
       html_code.append(buildOption(codeName, selectedCode));
       html_code.append(" </select>");

       return html_code.toString();
   }

   /**
    * 创建下拉框表单
    * @param selectName String 表单名称
    * @param className String 表单class名称
    * @param jsName String 表单onchange时触发的js名称
    * @param codeName String[][] 下拉框元素，1位为代码，2位为名称
    * @param selectedCode String 被选中元素代码
    * @return String 直接可以输出到页面，为一个下拉框表单
    */
   public static String buildSelect(String selectName, String className,
                                  String jsName, String[][] codeName,
                                  String selectedCode) {
       return buildSelect(selectName, className, jsName, codeName, selectedCode, false, false);
   }

   /**
    * 创建下拉框表单
    * @param selectName String 表单名称
    * @param className String 表单class名称
    * @param jsName String 表单onchange时触发的js名称
    * @param codeName List 元素为String[2]，1位为代码，2位为名称
    * @param selectedCode String 被选中元素代码
    * @param isFirstBlank boolean 第一元素是否为空白
    * @param disabled boolean 表单是否被禁止
    * @return String 直接可以输出到页面，为一个下拉框表单
    */
   public static String buildSelect(String selectName, String className,
                                  String jsName, List codeName,
                                  String selectedCode, boolean isFirstBlank,
                                  boolean disabled) {
       String[][] codeNameArry = null;
       if (codeName != null && codeName.size() > 0) {
           codeNameArry = new String[codeName.size()][2];
       }
       for (int i = 0; i < codeName.size(); i++) {
           codeNameArry[i][0] = ((String[])codeName.get(i))[0];
           codeNameArry[i][1] = ((String[])codeName.get(i))[1];
       }
       return buildSelect(selectName, className, jsName, codeNameArry,
                          selectedCode, isFirstBlank, disabled);
   }

   /**
    * 创建下拉框表单
    * @param selectName String 表单名称
    * @param className String 表单class名称
    * @param jsName String 表单onchange时触发的js名称
    * @param codeName List 元素为String[2]，1位为代码，2位为名称
    * @param selectedCode String 被选中元素代码
    * @return String 直接可以输出到页面，为一个下拉框表单
    */
   public static String buildSelect(String selectName, String className,
                                  String jsName, List codeName,
                                  String selectedCode) {
       String[][] codeNameArry = null;
       if (codeName != null && codeName.size() > 0) {
           codeNameArry = new String[codeName.size()][2];
       }
       for (int i = 0; i < codeName.size(); i++) {
           codeNameArry[i][0] = ((String[])codeName.get(i))[0];
           codeNameArry[i][1] = ((String[])codeName.get(i))[1];
       }
       return buildSelect(selectName, className, jsName, codeNameArry,
                          selectedCode, false, false);
   }

   /**
    * 创建下拉框的option元素
    * @param codeName String[][2] 1位为代码，2位为名称
    * @param selectedCode String
    * @return String
    */
   private static String buildOption(String[][] codeName, String selectedCode) {
       String html_code = "";

       if (selectedCode == null) {
           selectedCode = "";
       }
       String code = null;
       String name = null;
       int rowTotal = 0;
       if (codeName != null) {
           rowTotal = codeName.length;
       }
       for (int i = 0; i < rowTotal; i++) {
           code = codeName[i][0];
           name = codeName[i][1];
           if (code.equals(selectedCode)) {
               html_code = html_code + "<option selected value=\"" + code +
                           "\">" + name + "</option> \r\n";
           }
           else {
               html_code = html_code + "<option value=\"" + code + "\">" +
                           name + "</option> \r\n";
           }
       }

       return html_code;
   }


   /**
    * 获取代码对应的名称
    * @param codeName String[][2]，1位为代码，2位为名称
    * @param selectedCode String
    * @return String
    */
   public static String getName(String[][] codeName, String code) {
       String name = "";

       if (code == null) {
           code = "";
       }
       String codeTmp = null;
       String nameTmp = null;
       int rowTotal = 0;
       if (codeName != null) {
           rowTotal = codeName.length;
       }
       for (int i = 0; i < rowTotal; i++) {
           codeTmp = codeName[i][0];
           nameTmp = codeName[i][1];
           if (codeTmp.equals(code)) {
               name = nameTmp;
               break;
           }
       }
       return name;
   }


   /**
    * 获取代码对应的名称
    * @param codeName List，元素为String[2]，1位为代码，2位为名称
    * @param selectedCode String
    * @return String
    */
   public static String getName(List codeName, String code) {
       String name = "";

       if (code == null) {
           code = "";
       }
       String codeTmp = null;
       String nameTmp = null;
       int rowTotal = 0;
       if (codeName != null) {
           rowTotal = codeName.size();
       }
       for (int i = 0; i < rowTotal; i++) {
           codeTmp = ((String[])codeName.get(i))[0];
           nameTmp = ((String[])codeName.get(i))[1];
           if (codeTmp.equals(code)) {
               name = nameTmp;
               break;
           }
       }
       return name;
   }

   /**
    * 转换日期格式
    * @param date String 日期字符串
    * @param format String 日期字符串格式，PresentationUtil.yyyyMMdd / PresentationUtil.yyyy_MM_dd
    * @param convertFormat String 转换后的日期字符串格式，PresentationUtil.yyyyMMdd / PresentationUtil.yyyy_MM_dd
    * @return String
    */
   public static String convertDateFormat(String date, String format, String convertFormat) {
       //按照日期格式将日期字符串转为日期对象
       Date dDate = null;
       try {
           if (yyyyMMdd.equals(format)) {
               dDate = DF_yyyyMMdd.parse(date);
           }
           else if (yyyy_MM_dd.equals(format)) {
               dDate = DF_yyyy_MM_dd.parse(date);
           }
           else if (HHmmss.equals(format)) {
               dDate = DF_HHmmss.parse(date);
           }
           else if (HH_mm_ss.equals(format)) {
               dDate = DF_HH_mm_ss.parse(date);
           }
           else if (yyyyMMddHHmmss.equals(format)) {
               dDate = DF_yyyyMMddHHmmss.parse(date);
           }
           else if (yyyy$M$d$H$m$.equals(format)) {
               dDate = DF_yyyy$M$d$H$m$.parse(date);
           }
           else if (yyyy$MM$dd$.equals(format)) {
               dDate = DF_yyyy$MM$dd$.parse(date);
           }
           else {
               return date;
           }
       }
       catch (Exception e) {
           return date;
       }

       //将日期对象格式化指定格式的字符串
       try {
           if (yyyyMMdd.equals(convertFormat)) {
               return DF_yyyyMMdd.format(dDate);
           }
           else if (yyyy_MM_dd.equals(convertFormat)) {
               return DF_yyyy_MM_dd.format(dDate);
           }
           else if (HHmmss.equals(convertFormat)) {
               return DF_HHmmss.format(dDate);
           }
           else if (HH_mm_ss.equals(convertFormat)) {
               return DF_HH_mm_ss.format(dDate);
           }
           else if (yyyyMMddHHmmss.equals(convertFormat)) {
               return DF_yyyyMMddHHmmss.format(dDate);
           }
           else if (yyyy$M$d$H$m$.equals(convertFormat)) {
               return DF_yyyy$M$d$H$m$.format(dDate);
           }
           else if (yyyy$MM$dd$.equals(convertFormat)) {
               return DF_yyyy$MM$dd$.format(dDate);
           }
           else {
               return date;
           }
       }
       catch (Exception e) {
           return date;
       }
   }

   /**
    * 将double值转换为百分比
    * @param d double
    * @return String
    */
   public static String getRate(double d) {
       d = d * 100;
       String str = NF_2FRACTION.format(d);
       while (str.length() > 1 && ('0' == (str.charAt(str.length()-1))
                                   || '.' == (str.charAt(str.length()-1)))) {
           str = str.substring(0, str.length()-1);
       }
       return str + "%";
   }

   /**
    * 将字符串转换为百分比
    * @param d double
    * @return String
    */
   public static String getRate(String str) {
       double d;
       try {
           d = Double.parseDouble(str);
       }
       catch (Exception e) {
           return "";
       }
       return getRate(d);
   }

   public static String urlAppendParam(String url, String paraNm, String paraV) {
       if (url == null) {
           return url;
       }
       if (paraNm == null) {
           return url;
       }
       if ("".equals(paraNm)) {
           return url;
       }
       if (paraV == null) {
           return url;
       }
       if (url.indexOf("?") == -1) {
           url = url + "?" + paraNm + "=" + paraV;
       }
       else {
           url = url + "&" + paraNm + "=" + paraV;
       }
       return url;
   }

   public static String urlAppendParam(String url, String param) {
       if (url == null) {
           return url;
       }
       if (param == null) {
           return url;
       }
       if ("".equals(param)) {
           return url;
       }
       if (url.indexOf("?") == -1) {
           url = url + "?" + param;
       }
       else {
           url = url + "&" + param;
       }
       return url;
   }

   /**
    *
    * @param decimal double 原始数
    * @param scale int 保留精度
    * @param model int 舍位模式
    *                  ROUND_HALF_UP，四舍五入
    *                  ROUND_STRIP，舍弃多余位
    * @return String
    */
   public static String formatDecimal(double decimal, int scale, int model) {
       NumberFormat nft = NumberFormat.getNumberInstance();
       nft.setMinimumFractionDigits(scale);
       nft.setMaximumFractionDigits(scale);
       if (model == ROUND_STRIP) {
           String str = new BigDecimal(Double.toString(decimal)) + "";
           int flgIdx = str.lastIndexOf(".");
           //System.out.println(flgIdx);
           if (flgIdx != -1) {
               int fractionCount = str.length() - flgIdx -1;
               if (fractionCount >= scale) {
                   if (scale > 0) {
                       str = str.substring(0, flgIdx + scale + 1);
                   }
                   else {
                       str = str.substring(0, flgIdx);
                   }
               }
               else {
                   int fillCount = scale - fractionCount;
                   while ((fillCount--) > 0) {
                       str = str + "0";
                   }
               }
           }
           else {
           	str = str + ".";
               while ((scale--) > 0) {
                   str = str + "0";
               }
           }
           return str;
       }
       else if (model == ROUND_HALF_UP) {
           BigDecimal bigDecimal = new BigDecimal(decimal);
           decimal = bigDecimal.setScale(scale, model).doubleValue();
           return nft.format(decimal);
       }
       else {
           return decimal + "";
       }
   }
   
	/**
	 * 在传入字符串str右边补n个空格
	 * 
	 * @param int n 需右补空格个数
	 * @param String
	 *            str 原字符串
	 * @return String
	 */
	public static String addspace(int n, String str) {
		StringBuffer returnStr = new StringBuffer();
		returnStr.append(str);
		for (int i = 0; i < n; i++) {
			returnStr.append(" ");
		}
		return returnStr.toString();
	}
	
	/**
	 * 根据传入的日期格式(pstrDateFormat),将字符串转化为日期
	 * 
	 * @param pstrValue
	 * @param pstrDateFormat
	 * @return
	 */
	public static java.util.Date strToDate(String pstrValue,
			String pstrDateFormat)
	{
		if ((pstrValue == null) || (pstrValue.equals("")))
		{
			return null;
		}
		java.util.Date dttDate = null;
		try
		{
			SimpleDateFormat oFormatter = new SimpleDateFormat(pstrDateFormat);
			dttDate = oFormatter.parse(pstrValue);
			oFormatter = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return dttDate;
	}
	

   public static void main(String[] args) {
       String d = PresentationUtil.formatDecimal(500000000.123232321312,
               2,
               PresentationUtil.ROUND_STRIP);
       System.out.println(d);

       //DecimalFormat 采用的是四舍五入
       //DecimalFormat dft = new DecimalFormat("##.##");
       //System.out.println(roundDouble(9090099991.006, 2));

       //也是四舍五入
       //NumberFormat nf = new DecimalFormat();
       //nf.setMaximumFractionDigits(2);
       //nf.setMinimumFractionDigits(2);
       //System.out.println(nf.format(9999999991.075));
   }
}
