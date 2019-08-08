package com.cmwa.ecc.business.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WaStringUtil {
	public static String toString (Object obj)
	{
	    if (null == obj)
	    {
	    	return "";
	    }
	    
	    return obj.toString();
	}
	
	public static boolean IsNotNullAndEmpty(Object obj){
		if(obj == null )			
			return false;
		if(obj.toString().trim().length() == 0)
			return false;
		return true;
	}	
	
	public static String ArrayToString(String[] str){
		StringBuffer sb = new StringBuffer();
	    if (null != str)
	    {
	    	for(int i=0; i<str.length; i++)
	    	{
	    		if(i == str.length -1 )
	    		{
	    			sb.append(str[i]);	
	    		}
	    		else
	    		{
	    			sb.append(str[i]+",");	
	    		}
	    	}
	    }
	    else
	    {
	    	return "";
	    }
	    return sb.toString();
	}	
	
	
	public static String getFormatCurrency(String amt)
	{
		if(WaStringUtil.IsNotNullAndEmpty(amt))
		{
			return DecimalFormat.getCurrencyInstance().format(Float.valueOf(amt));			
		}
		return "0.00";
	}
	
    /**
     * 根据操作系统类型获取分割符
     * @return
     */
	public static String getSepBySysType()
	{
		String operSystem = System.getProperty("os.name");
		String separative = "";
		if(operSystem.toLowerCase().startsWith("windows"))
		{
			separative = "\\";
		}
		else if(operSystem.toLowerCase().startsWith("linux"))
		{
			separative = "/";
		}
		
		return separative;
	}
	
	/**
	 * 根据操作系统 获取文件绝对路径 的开头
	 * @return
	 */
	public static String getFileHeadPath()
	{
		String operSystem = System.getProperty("os.name");
		String filePath = "";
		if(operSystem.toLowerCase().startsWith("windows"))
		{
			filePath = "";
		}
		else if(operSystem.toLowerCase().startsWith("linux"))
		{
			filePath = "file://";
		}
		
		return filePath;
	}
	
	/**
	 * 根据操作系统 获取文件路径
	 * 
	 * @return
	 */
	public static String getFilePath(String path) {
		String operSystem = System.getProperty("os.name");
		String filePath = new File(path).getAbsolutePath();
		if (operSystem.toLowerCase().startsWith("windows")) {
			return filePath;
		} else if (operSystem.toLowerCase().startsWith("linux")) {
			filePath = "file://" + filePath;
		}

		return filePath;
	}
	
	
	/**
	 * 银行卡号格式化
	 * @param oldBankNumb
	 * @return
	 */
	public static String getFmtBankNumb(String oldBankNumb)
	{
		if(!IsNotNullAndEmpty(oldBankNumb))
		{
			return "";
		}
		
		String newstr = "";
		int size = ((oldBankNumb.length())%4 == 0) ? ((oldBankNumb.length())/4):((oldBankNumb.length())/4 + 1);
		for(int i=0;i<size ;i++)
		{    
			int endIndex = (i+1)*4;    
			if((i+1)==size)
			{         endIndex = oldBankNumb.length();    
			}    
			if(i==0)
			{       
				newstr += oldBankNumb.substring(i,endIndex);    
			}
			else
			{
				newstr += " "+oldBankNumb.substring(i*4, endIndex);    
			}
		}
		
		return newstr;
	}
	
	/*public static String getFlowContentHtml(String empid, String prjCode, String prdCode)
	{
		StringBuffer sb = new StringBuffer();
		ProjectDelegate prjDelegate = new ProjectDelegate();
		SQLResult prjResult = prjDelegate.findById(empid, prjCode);
		
		ProductDelegate prdDelegate = new ProductDelegate();
		SQLResult prdResult = prdDelegate.findById(empid, prdCode);
		
		ProductExtDelegate prdExtDelegate = new ProductExtDelegate();
		SQLResult prdExtResult = prdExtDelegate.findById(empid, prdCode);
		
		DecimalFormat format = new DecimalFormat("###,##0.00");
		String preRaiseAmt = prdResult.getRowResult(0, "PRERAISEAMT");
		if(WaStringUtil.IsNotNullAndEmpty(preRaiseAmt))
		{
			preRaiseAmt = format.format(Double.parseDouble(preRaiseAmt));
		}
		else
		{
			preRaiseAmt = "0.00";
		}
		
		
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<link href=\"/CMFKMProject/WA_SpecialServer/WaWeb/WaWeb_Css/form.css\" rel=\"stylesheet\" type=\"text/css\" />");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<table class=\"form-table\">");
		sb.append("<tr>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("产品名称&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prdResult.getRowResult(0, "FUNDSNAME"));
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("基金代码&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prdResult.getRowResult(0, "FUNDCODE"));
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("募集起始日&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(WaDateUtil.toDate10(prdExtResult.getRowResult(0, "RAISEBEGDATE")));
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("募集结束日&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(WaDateUtil.toDate10(prdExtResult.getRowResult(0, "RAISEENDDATE")));
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("产品起息日&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(WaDateUtil.toDate10(prdExtResult.getRowResult(0, "INTBEGDATE")));
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("产品到期日&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(WaDateUtil.toDate10(prdExtResult.getRowResult(0, "PRDENDDATE")));
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("托管银行&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prjResult.getRowResult(0, "CUSTODIANID_NM"));
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("募集金额&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(preRaiseAmt+"&nbsp;元");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("客户收益率&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prdResult.getRowResult(0, "CLIENTYIELD_NM") + "%");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("管理费率&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prdExtResult.getRowResult(0, "MANAGERATE_NM") + "%");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("销售服务费率&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prdExtResult.getRowResult(0, "SELLSERVICERATE_NM") + "%");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-title\">");
		sb.append("托管费率&nbsp;");
		sb.append("</td>");
		sb.append("<td class=\"form-table-td-content\">");
		sb.append(prdExtResult.getRowResult(0, "CUSTODIANRATE_NM") + "%");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}*/
	
	/**
	 * <pre>
	 * 一维数组转换成二维数组 
	 * 应用场景就是为每一个复选框增加一个文本框的补充信息，
	 * 需要用到这个方法把前台传过来的一位数组转换成数据库可以接受的二维数组
	 * 目前用到的地方有     新建产品--资金来源
	 * </pre>
	 * @param checkBoxs  复选框一维数组
	 * @param checkBoxExts  复选框所对应的文本框的补充信息一维数组
	 * @return List  list.get(0)复选框二维数组      list.get(1)复选框所对应的文本框的补充信息二维数组
	 */
	public static List<String[][]> oneDimensionalArrayToTwo(String[] checkBoxs,String[] checkBoxExts){
		int arrayLength = 0;
		if(null != checkBoxs){
			arrayLength = checkBoxs.length;
		}
		String[][] checkBoxTD = null;
		String[][] checkBoxExtTD = null;
		if(arrayLength>1){
			checkBoxTD = new String[arrayLength][1];
			checkBoxExtTD = new String[arrayLength][1];
			for(int i=0; i<arrayLength; i++){
				checkBoxTD[i][0] = checkBoxs[i];
				checkBoxExtTD[i][0] = checkBoxExts[i];
			}
		}
		List<String[][]> list = new ArrayList<String[][]>();
		list.add(checkBoxTD);
		list.add(checkBoxExtTD);
		return list;
	}

}
