package com.cmwa.ecc.business.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 综合业务平台附件TAG
 * @author fangdb
 *
 */
public class AttachTag extends TagSupport {	

	private String attachKey;
	
	public String getAttachKey() {
		return attachKey;
	}

	public void setAttachKey(String attachKey) {
		this.attachKey = attachKey;
	}

	/**
	 * 生成附件的相关HTML要素
	 * eg: attachKey = 'Attach'
	 * <span id="span_Attach" name="span_Attach"></span>
	 * <input type="hidden" id="hid_Attach" name="Attach" value="">
	 * <div id="div_Attach" class="filesdiv">
	 * <img id="loading_Attach" src="/CMFKMProject/WA_SpecialServer/WaWeb/WaWeb_Images/FileUpload/loading.gif" style="display:none;">
	 * <a href="javascript:void(0);" class="files" id="a_Attach">
	 * <input id="uploadFile_Attach" type="file" onchange="uploadFile('Attach')" name="uploadFile_Attach">
	 **/
	public int doStartTag() throws JspException {
		try{
			this.attachKey = this.attachKey + System.currentTimeMillis();
			
            pageContext.getOut().println("<div id=\"div_" + this.attachKey + "\" class=\"filesdiv\">");
            pageContext.getOut().println("<span id=\"span_" + this.attachKey + "\" name=\"span_Attach\"></span>");
            pageContext.getOut().println("<input type=\"hidden\" id=\"hid_" + this.attachKey + "\" name=\"hid_Attach\" value=''>");
            pageContext.getOut().println("<img id=\"loading_"+ this.attachKey + "\" ");
            pageContext.getOut().println(" src=\"/cbp/WA_SpecialServer/WaWeb/WaWeb_Images/FileUpload/loading.gif\"");
            pageContext.getOut().println(" style=\"display:none;\">");
            pageContext.getOut().println("<a href=\"javascript:void(0);\" class=\"files\" id=\"a_" + this.attachKey + "\">");
            pageContext.getOut().println("<input id=\"uploadFile_" + this.attachKey + "\" type=\"file\" onchange=\"uploadAttach('"+ this.attachKey + "')\" name=\"uploadFile_" + this.attachKey + "\">");
            pageContext.getOut().println("</a></div>");
        }
        catch(IOException e)
        {
            throw new JspException("Create Attach Tag Error:" + e.getMessage());
        }
		
		
        return SKIP_BODY;
	}
	
	public int doEndTag()	 
	{	 
		return EVAL_PAGE;
	}

}
