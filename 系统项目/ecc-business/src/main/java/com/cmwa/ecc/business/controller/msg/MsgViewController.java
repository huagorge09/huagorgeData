package com.cmwa.ecc.business.controller.msg;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.MsgViewVo;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.service.msg.IMsgViewService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Controller("msgViewController")
@RequestMapping("/service/msgView")
public class MsgViewController extends BaseController{
	private static final Log log = LogFactory.getLog(MsgViewController.class);
	
	@Resource
	private IMsgViewService msgViewService;
	
	@RequestMapping("/list")
	public String list(ModelMap model){
		return "jsp/msg/msgList";
	}
	
	@RequestMapping("/listPage")
	@ResponseBody
	public Page<MsgViewVo> listPage(SearchParam sp){
		Page<MsgViewVo> msgViewVos = msgViewService.msgViewListPage(sp);
		return msgViewVos;
	}
	
	@RequestMapping("/newest")
	@ResponseBody
	public Page<MsgViewVo> newest(SearchParam sp){
		Page<MsgViewVo> msgViewVos = msgViewService.newestListPage(sp);
		return msgViewVos;
	}
	
	@RequestMapping("/unreadMsgCount")
	@ResponseBody
	public int unreadMsgCount(){
		int msgNumber = msgViewService.unreadMsgCount();
		return msgNumber;
	}
	
	@RequestMapping("/msgFlag")
	@ResponseBody
	public Page<MsgViewVo> msgFlag(SearchParam sp){
		Page<MsgViewVo> msgViewVos = msgViewService.flagListPage(sp);
		return msgViewVos;
	}
	
	@RequestMapping("/view")
	@ResponseBody
	public String view(@RequestParam("msgId") String msgId){
		msgViewService.updateForView(msgId);
		return msgId;
	}
	
	@RequestMapping("/flag")
	@ResponseBody
	public String flag(@RequestParam("msgId") String msgId, @RequestParam("flag") String flag){
		msgViewService.updateForFlag(msgId, flag);
		return flag;
	}
	
}