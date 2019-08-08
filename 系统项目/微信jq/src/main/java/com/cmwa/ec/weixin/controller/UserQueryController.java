package com.cmwa.ec.weixin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.query.facade.dto.user.UserCommonDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoexDto;
import com.cmwa.ec.query.facade.dto.user.UserOrderDto;
import com.cmwa.ec.weixin.manager.business.SynchronousDataManager;

@Controller("UserQueryController")
@RequestMapping(value = "/WeixinService")
public class UserQueryController {

    @Autowired
    private SynchronousDataManager synchronousDataManager;

    /**
     * 同步微信用户扩展信息表
     * 
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/sysnUserInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String sysnUserInfo(HttpServletResponse response, HttpServletRequest request) {
        String cmfUserId = request.getParameter("cmfUserId");
        List<UserInfoexDto> exList = synchronousDataManager.queryInfoex(cmfUserId);
        int count = 0;
        for (int i = 0; exList != null && i < exList.size(); i++) {
    		UserInfoexDto exDto = exList.get(i);
    		UserCommonDto commDto = new UserCommonDto();
    		commDto.setOpenId(exDto.getOpenid());
    		commDto.setCmfUserId(exDto.getCmfuserid());
    		count += synchronousDataManager.synchUserInfoExtend(commDto, "synchronous");
    	}
        JSONObject object = new JSONObject();
        object.put("size", count);
        return object.toString();
    }

    /**
     * 同步订单表信息
     * 
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/synchUserOrderInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String synchUserOrderInfo(HttpServletResponse response, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        UserOrderDto orderDto = new UserOrderDto();
        orderDto.setCustNo(request.getParameter("custNo"));
        int count = synchronousDataManager.synchUserOrderInfo(orderDto);
        json.put("size", count);
        return json.toString();
    }

    /**
     * 同步微信用户信息表
     * 
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/synchOldUseInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String listAll(HttpServletResponse response, HttpServletRequest request) {
        UserInfoDto userDto = new UserInfoDto();
        userDto.setOpenid(request.getParameter("openId"));
        JSONObject json = new JSONObject();
        int count = synchronousDataManager.synchOldUseInfo(userDto);
        json.put("size", count);
        return json.toString();
    }

}
