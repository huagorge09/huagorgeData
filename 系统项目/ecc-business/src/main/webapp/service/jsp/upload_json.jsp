<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="net.sf.json.JSONObject"%>
<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="javax.imageio.ImageIO"%>
<%@ page import="com.cmwa.ecc.business.utils.SysConf"%>
<%

/**
 * KindEditor JSP
 * 
 * 本JSP程序是演示程序，建议不要直接在实际项目中使用。
 * 如果您确定直接使用本程序，使用之前请仔细确认相关安全设置。
 * 
 */

//文件保存目录路径
//String savePath = pageContext.getServletContext().getRealPath("/") + "/3rd/edit4/attached/";
String savePath = SysConf.get("tmpFileBasePath") + "/edit4/attached/";

//文件保存目录URL
String saveUrl  = SysConf.get("tmpFileBasePath") + "/edit4/attached/";

//定义允许上传的文件扩展名
HashMap extMap = new HashMap();
extMap.put("image", "gif,jpg,jpeg,png,bmp");
extMap.put("flash", "swf,flv");
extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

//最大文件大小
//long maxSize = 1000000; //编辑器原始值
//update by xgb 2014-05-20,根据客户要求调整为10M
long maxSize = 10 * 1024 * 1024;//10M

response.setContentType("text/html; charset=UTF-8");

if(!ServletFileUpload.isMultipartContent(request)){
	out.println(getError("请选择文件。"));
	return;
}
//检查目录
System.out.println("savePath="+savePath);
File uploadDir = new File(savePath);
if(!uploadDir.isDirectory()){
	uploadDir.mkdirs();
}
//检查目录写权限
if(!uploadDir.canWrite()){
	out.println(getError("上传目录没有写权限。"));
	return;
}

String dirName = request.getParameter("dir");
if (dirName == null) {
	dirName = "image";
}
if(!extMap.containsKey(dirName)){
	out.println(getError("目录名不正确。"));
	return;
}
//创建文件夹
savePath += dirName + "/";
saveUrl += dirName + "/";
File saveDirFile = new File(savePath);
if (!saveDirFile.exists()) {
	saveDirFile.mkdirs();
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
String ymd = sdf.format(new Date());
savePath += ymd + "/";
saveUrl += ymd + "/";
File dirFile = new File(savePath);
if (!dirFile.exists()) {
	dirFile.mkdirs();
}

FileItemFactory factory = new DiskFileItemFactory();
ServletFileUpload upload = new ServletFileUpload(factory);
upload.setHeaderEncoding("UTF-8");
List items = upload.parseRequest(request);
Iterator itr = items.iterator();
while (itr.hasNext()) {
	FileItem item = (FileItem) itr.next();
	String fileName = item.getName();
	long fileSize = item.getSize();
	if (!item.isFormField()) {
		//检查文件大小
		if(item.getSize() > maxSize){
			out.println(getError("上传文件大小超过限制(10M)。"));
			return;
		}
		//检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		//if(!Arrays.asList(extMap.get(dirName).split(",")).contains(fileExt)){
		if (false) {//不对文件后缀做校验
			out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
			return;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		//将上传的图片写到服务器上
		try{
			File uploadedFile = new File(savePath, newFileName);
			item.write(uploadedFile);
		}catch(Exception e){
			out.println(getError("上传文件失败。"));
			System.out.println("编辑器上传文件有异常（将上传的图片写到服务器上时发生异常）：" + e);
			return;
		}

		
		//获取图片大小
		int width = 0;
		int height = 0;
		
		FileInputStream fileInputStream = null;
		try {//update by xgb 2014-05-20, 处理 javax.imageio.IIOException: Unsupported Image Type 异常
			File picture = new File(savePath + newFileName);
			fileInputStream = new FileInputStream(picture);
			BufferedImage sourceImg = ImageIO.read(fileInputStream);
			width = sourceImg.getWidth();
			height = sourceImg.getHeight();
		} catch (Exception exx) {
			//add by xgb 2014-05-21,原始图片有问题的情况下，直接返回原始图片有问题，系统无法识别。(通过浏览器访问原始图片都会有问题)
			out.println(getError("原始图片丢失了信息，系统无法识别，请重新处理该图片后上传。"));
			exx.printStackTrace();
			return;
			/*
			try {
				ThumbnailConvert tc = new ThumbnailConvert();
				tc.setCMYK_COMMAND(file.getPath());
				Image image = null;
				image = Toolkit.getDefaultToolkit().getImage(file.getPath());
				MediaTracker mediaTracker = new MediaTracker(new Container());
				mediaTracker.addImage(image, 0);
				mediaTracker.waitForID(0);
				image.getWidth(null);
				image.getHeight(null);
			}catch (Exception e1){
				e1.printStackTrace();
			}
			*/
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		
		boolean isLimit = false;//是否超过宽度最大像素限制
		String whString = "";
		System.out.println("宽度=" + width);  
		System.out.println("高度=" + height);  
		/******************************
		if(width>500){
			width =500;
			isLimit = true;
		}
		if(height>400){
			height =400;
			isLimit = true;
		}
		*******************************/
		/*********************************************************
		update by xgb 2014-05-19;
		这里改完之后，本还需要改kindeditor.js的内容；
		但是kindeditor.js改成接收本页面的高度和宽度，不做处理了。
		图片压缩公式改为：
		假设图片长度为L，高度为H：
		if(L<=600){不压缩}
		else(L>600){图片高度压缩为H*600/L，且长度压缩为600}
		**********************************************************/
		if (width > 600) {
			height = height * 600 / width;
			width = 600;
			isLimit = true;
		}
		if (isLimit) {
			whString = "#LEGION#" + width + "," + height;
		}
		
		System.out.println("调整后的宽度="+width);  
		System.out.println("调整后的高度="+height); 

    //以下部分是将上传到服务器的图片在编辑器控件上展示出来。
		JSONObject obj = new JSONObject();
		obj.put("error", new Integer(0));
		obj.put("url", "/cbp/upload/downloadForRichTextEditor.do?url=" + saveUrl + newFileName + whString);
		out.println(obj.toString());
		//System.out.println("报异常？3");
	}
}
%>

<%!
private String getError(String message) {
	JSONObject obj = new JSONObject();
	obj.put("error", new Integer(1));
	obj.put("message", message);
	return obj.toString();
}
%>