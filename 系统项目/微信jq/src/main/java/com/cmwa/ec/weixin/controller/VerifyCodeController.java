package com.cmwa.ec.weixin.controller;

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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.util.SessionValue;

import net.sf.json.JSONObject;


/**
 * 验证码controller
 * 
 * @author jouislu
 * 
 */
@Controller("verifyController")
@RequestMapping(value = "/WeixinService")
public class VerifyCodeController {
	
	/**
	 * 图片验证码验证   后端调用
	 * @param request
	 * @param randomCode
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public static Map<String, String> verifyRandom(HttpServletRequest request,String randomCode){

		Map<String, String> map = new HashMap<String, String>();
		String returnCode = "";
		String returnMsg = "";
		// 保存在session中的图片验证码
		Object tempCode = request.getSession().getAttribute(WXConstants.SESSION_MNU);
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
				returnCode = "0000";
				returnMsg = "成功";
				
				//当前验证成功的秒数+300秒  验证手机验证码的时候，对此timeStamp进行验证，为空或超时则返回第一个页面
				request.getSession(true).setAttribute(SessionValue.SESSION_TIMESTAMP, System.currentTimeMillis()/1000+300);
			}else{
				returnCode = "9301";
				returnMsg = "验证码错误";
			}
		}
		map.put("returnCode", returnCode);
		map.put("returnMsg", returnMsg);
		
		System.out.println("验证图片验证码结束："+map);
		
		return map;
	}
	
	/**
	 * 验证码校验  前端调用
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/verifyRandomCode.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String verifyRandomCode(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String inputCode = request.getParameter("inputCode")!=null?request.getParameter("inputCode"):"";
		System.out.println(request.getSession().getMaxInactiveInterval()+"<<<<<session有效期　");
		Object tempCode = request.getSession().getAttribute(WXConstants.SESSION_MNU);
		
		String returnCode = "";
		String randomCode = "";
		JSONObject returnJsonObject = new JSONObject();
		System.out.println("session中存放验证码："+tempCode);
		if(null==tempCode){
			System.out.println("session中存放验证码为空");
			returnCode = "timeOut";
		}else{
			System.out.println("session中存放验证码："+tempCode.toString());
			randomCode = tempCode.toString().toLowerCase();
			inputCode = inputCode.toLowerCase();
			
			if(randomCode.equals(inputCode)){
				returnCode = "success";
				request.getSession(true).setAttribute(SessionValue.SESSION_RESETLPWVERFLAG, WXConstants.COMMON_SUCCESS);
				String randomChannel = request.getParameter("randomChannel");
				if(randomChannel != null && !"".equals(randomChannel)){
					if(randomChannel.equals("regRandom")){// 注册页面验证图片验证码
						request.getSession(true).setAttribute(SessionValue.SESSION_RANDOMCHANNEL, "regRandom");
					}else if(randomChannel.equals("resetRandom")){// 注册页面验证图片验证码
						request.getSession(true).setAttribute(SessionValue.SESSION_RANDOMCHANNEL, "resetRandom");
					}
				}
				//当前验证成功的秒数+300秒  验证手机验证码的时候，对此timeStamp进行验证，为空或超时则返回第一个页面
				request.getSession(true).setAttribute(SessionValue.SESSION_TIMESTAMP, System.currentTimeMillis()/1000+300);
			}else{
				request.getSession(true).setAttribute(SessionValue.SESSION_RESETLPWVERFLAG, WXConstants.COMMON_ERROR_SYSERRCODE);
				returnCode = "failed";
			}
		}
		returnJsonObject.put("returnCode", returnCode);
		
		return returnJsonObject.toString();
	}
	
	
	
	@RequestMapping(value = "/buildimageservlet.xhtml",  method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void verify(HttpServletResponse response,HttpServletRequest request) throws Exception {
        //设置页面不缓存
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 在内存中创建图象
        //int width = 60, height = 40;
        int width = 100, height = 40;
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
        session.setAttribute(WXConstants.SESSION_MNU, sRand);
        // 图象生效
        g.dispose();
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
}
