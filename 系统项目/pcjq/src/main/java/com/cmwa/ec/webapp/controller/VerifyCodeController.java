package com.cmwa.ec.webapp.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.SessionValue;

/**
 * 验证码controller
 * 
 * @author jouislu
 * 
 */
@Controller("verifyController")
@RequestMapping(value = "/AppService")
public class VerifyCodeController {
	private static Logger logger = Logger.getLogger(VerifyCodeController.class.getName());

	@RequestMapping(value = "/setUp/buildimageservlet.xhtml",  method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void verify(HttpServletResponse response,HttpServletRequest request) throws Exception {
	
		  //beginTime,endTime 为测试用
        long beginTime;
        long endTime;


        //设置页面不缓存
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //测试图像生成时间
        beginTime = System.currentTimeMillis();

        // 在内存中创建图象
        //int width = 60, height = 40;
        int width = 100, height = 37;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics g = image.getGraphics();

        //生成随机类
        Random random = new Random();

        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        //设定字体
        g.setFont(new Font("宋体", Font.PLAIN, 13));

        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(Color.WHITE);

        // 取随机产生的认证码(4位数字)
        String sRand = "";
        
        if(SpringUtil.getProperty("verificationCode") != null && SpringUtil.getProperty("verificationCode").equals("8888")){
        	for (int i = 0; i < 4; i++) {
                String rand = "8";
                g.setFont(new Font(rand, Font.LAYOUT_LEFT_TO_RIGHT, height / 2));
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                g.drawString(String.valueOf(rand), 20 * i + random.nextInt(6) + 3, height - random.nextInt(25));
                sRand += rand;
            }
        }else{
        	for (int i = 0; i < 4; i++) {
                String rand = "";
                if (i % 2 == 0) {
                    //rand = String.valueOf(random.nextInt(10));
                    rand = getValidRand("N", 10, random);
                    g.setFont(new Font(rand, Font.HANGING_BASELINE, height / 2));
                } else {
                    //char c = 65;
                    g.setFont(new Font(rand, Font.LAYOUT_LEFT_TO_RIGHT, height / 2));
                    //c = (char) (c + random.nextInt(26));
                    //rand = String.valueOf(c);
                    rand = getValidRand("C", 10, random);
                }

                //g.setFont(new Font(rand, Font.ITALIC, height/2));

                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                g.drawString(String.valueOf(rand), 20 * i + random.nextInt(6) + 3, height - random.nextInt(25));

                sRand += rand;
            }
            
        }
        
        // 随机产生88个干扰点，使图象中的认证码不易被其它程序探测到
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 将认证码存入SESSION
        HttpSession session = request.getSession();
        session.setAttribute(SessionValue.SESSION_MNU, sRand);

        // 图象生效
        g.dispose();

        endTime = System.currentTimeMillis();
        //System.out.println("图像 " + sRand + " 生成时间：" + (endTime - beginTime));

        /**
         * 将验证码通过ImageIO.write方式输出到页面
         * 该方法已经停用，原因：耗时太长 2007-01-18 by zhanxb
         */
        //测试图像输出时间
        beginTime = System.currentTimeMillis();
        // 输出图象到页面
        //ImageIO.write(image, "JPEG", response.getOutputStream());
        endTime = System.currentTimeMillis();
        //System.out.println("图像 " + sRand + " 生成文件时间：" + (endTime - beginTime));

        /**
         * 使用JPEGImageEncoder将验证码输出到页面
         * added by zhanxb 2007-01-18
         */
        //测试图像输出-new文件时间
        beginTime = System.currentTimeMillis();

        ServletOutputStream out = response.getOutputStream();
        
	   	 ImageWriter imageWriter  =   ImageIO.getImageWritersBySuffix("jpg").next();  
	     ImageOutputStream ios  =  ImageIO.createImageOutputStream(out);  
	     imageWriter.setOutput(ios);  
	     IIOMetadata imageMetaData  =  imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);  
	     imageWriter.write(imageMetaData, new IIOImage(image, null, null), null);  
	     ios.close();  
	     imageWriter.dispose();  
       



	}
	 private Random generator = new Random();
	    private Color getRandColor(int fc, int bc) {
	        //给定范围获得随机颜色
	        Random random = new Random();
	        if (fc > 255) {
	            fc = 255;
	        }
	        if (bc > 255) {
	            bc = 255;
	        }
	        int r = fc + random.nextInt(bc - fc);
	        int g = fc + random.nextInt(bc - fc);
	        int b = fc + random.nextInt(bc - fc);
	        return new Color(r, g, b);
	    }
    public void shearX(Graphics g, int w1, int h1, Color color) {

        int period = generator.nextInt(2);

        boolean borderGap = true;
        int frames = 1;
        int phase = generator.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                       * Math.sin((double) i / (double) period
                                  + (6.2831853071795862D * (double) phase)
                                  / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }

    }

    private void shearY(Graphics g, int w1, int h1, Color color) {

        int period = generator.nextInt(40) + 10; // 50;

        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                       * Math.sin((double) i / (double) period
                                  + (6.2831853071795862D * (double) phase)
                                  / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }

        }

    }

    /**
     * 取得合法字符：不能出现如下易混淆的字符: 0（数字）、1（数字）、o（字母）、O（字母）、i、I、l（L）
     * @param charType
     * @param count
     * @return String
     * @throws Exception
     */
    private String getValidRand(String charType, int count, Random random) {
        //System.out.println("getValidRand: " + charType);
        String invalidCString = "01oOiIl";
        //Random random = new Random();
        String rand = "";
        if ("N".equalsIgnoreCase(charType)) {
            for (int i=0; i<count; i++) {
                rand = String.valueOf(random.nextInt(10));
                if ((rand != null) && (invalidCString.indexOf(rand) == -1)) {
                    return rand;
                }
            }
            return "9";//默认数字为9
        } else {
            for (int i = 0; i<count; i++) {
                char c = 65;
                c = (char) (c + random.nextInt(26));
                rand = String.valueOf(c);
                if ((rand != null) && (invalidCString.indexOf(rand) == -1)) {
                    return rand;
                }
            }
            return "Q";//默认字母为Q
        }
    }
    
    /**
	 * 图片验证码校验 前端调用
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/setUp/verifyRandomCode.xhtml" , produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String verifyRandomCode(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String inputCode = request.getParameter("inputCode");
		
		String mobile = request.getParameter("mobile");
		
		logger.info("【获取前台传递的数据：》》》inputCode="+inputCode+"》》》mobile="+mobile+"】");
		
		String randomCode = (String) request.getSession(true).getAttribute(SessionValue.SESSION_MNU);
		
		if(inputCode != null && !inputCode.equals("")){
			inputCode = inputCode.trim().toLowerCase();
		}else{
			inputCode = ""; 
		}
		logger.info("VerifyCodeController.class的【verifyRandomCode】开始>>>inputCode="+inputCode+"，randomCode="+randomCode);
		
		if(randomCode != null && !randomCode.equals("")){
			randomCode = randomCode.toLowerCase();
		}else{
			randomCode = "";
		}
		
		logger.info("VerifyCodeController.class的【verifyRandomCode】经过toLowerCase()的值>>>inputCode="+inputCode+"，randomCode="+randomCode+",randomCode.equals(inputCode)="+randomCode.equals(inputCode));
		
		JSONObject returnJsonObject = new JSONObject();
		
		String returnCode = "";
		String returnMsg = "";
		if(randomCode == null || randomCode.equals("")){
			returnCode = "0011";
			returnMsg = "returnCode is null";
		}else if(randomCode.equals(inputCode)){
			returnCode = "0000";
			returnMsg = "success";
			//当前验证成功的秒数+300秒  验证手机验证码的时候，对此timeStamp进行验证，为空或超时则返回第一个页面
			request.getSession(true).setAttribute("timeStamp", System.currentTimeMillis()/1000+300);
			//当图片验证码验证通过，则将用户的手机号码存放到session中，发送短信验证码的时候直接从session中获取
			if(mobile != null && !mobile.equals("")){
				logger.info("验证图片验证码：手机号码不为空，则将手机号码存放到session中》》》mobile："+mobile);
				request.getSession(true).setAttribute("msgMobile", mobile.trim());
			}
			logger.info("VerifyCodeController.class的【verifyRandomCode】方法中>>>图片验证码输入正确");
		}else{
			returnCode = "0022";
			returnMsg = "failed";
			logger.info("VerifyCodeController.class的【verifyRandomCode】方法中>>>图片验证码输入错误");
		}
		logger.info("VerifyCodeController.class的【verifyRandomCode】方法中>>>inputCode="+inputCode+"，randomCode:"+randomCode+"，returnCode:"+returnCode);
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		
		logger.info("VerifyCodeController.class的【verifyRandomCode】结束");
		return returnJsonObject.toString();
	}
	
	/**
	 * 图片验证码验证   后端调用
	 * @param request
	 * @param randomCode
	 * @return
	 * 			Map<String,String>
	 * @author maj
	 */
	public static Map<String, String> verifyRandom(HttpServletRequest request,String randomCode){

		Map<String, String> map = new HashMap<String, String>();
		String returnCode = "";
		String returnMsg = "";
		
		// 保存在session中的图片验证码
		Object tempCode = request.getSession().getAttribute(SessionValue.SESSION_MNU);
		String sessionCode = "";
		if(null == tempCode){
			System.out.println("session中存放验证码为空");
			returnCode = "9000";
			returnMsg = "请获取验证码";
		}else{
			sessionCode = tempCode.toString().toLowerCase();
			randomCode = randomCode.toLowerCase();
			
			System.out.println("session中存放验证码："+sessionCode+"----sessionCode:"+randomCode);
			
			if(sessionCode.equals(randomCode)){
				returnCode = ECConstants.RETURN_CODE_0000;
				returnMsg = ECConstants.RETURN_MSG_0000;
				
				//当前验证成功的秒数+300秒  验证手机验证码的时候，对此timeStamp进行验证，为空或超时则返回第一个页面
				request.getSession(true).setAttribute(SessionValue.SESSION_TIMESTAMP, System.currentTimeMillis()/1000+300);
			}else{
				returnCode = ECConstants.RETURN_CODE_9301;
				returnMsg = "验证码错误";
			}
		}
		map.put("returnCode", returnCode);
		map.put("returnMsg", returnMsg);
		
		System.out.println("验证图片验证码结束："+map);
		
		return map;
	}
}
