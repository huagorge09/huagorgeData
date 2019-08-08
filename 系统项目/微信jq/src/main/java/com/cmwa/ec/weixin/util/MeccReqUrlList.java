package com.cmwa.ec.weixin.util;

import java.util.ArrayList;
import java.util.List;

/**
 * mecc不可访问的url列表
 * @author liury
 *
 */
public class MeccReqUrlList {

	private static List<String> list = new ArrayList<String>();
	
	static{
		
		//注册页
		list.add("/WeixinService/otherIERegister.shtml");
		list.add("/WeixinService/register.shtml");
		list.add("/WeixinService/setUp/webRegister.xhtml");
		list.add("/WeixinService/user/weixinRegister.xhtml");
		list.add("/WeixinService/register.shtml");
		list.add("/WeixinService/register.shtml");
		list.add("/WeixinService/register.shtml");
		list.add("/WeixinService/register.shtml");
		
		
		
		//银行卡操作
		list.add("/WeixinService/business/bank/addBank.shtml");
		list.add("/WeixinService/business/bank/bankAuth.shtml");
		list.add("/WeixinService/business/addBank.xhtml");
		list.add("/WeixinService/business/openAccount.xhtml");
		list.add("/WeixinService/business/canalBindBankCard.xhtml");
		
		//交易操作
		list.add("/WeixinService/business/pay/buyMoney.shtml");
		list.add("/WeixinService/business/pay/confirmBuy.shtml");
		list.add("/WeixinService/business/query/modifyBankcard.shtml");
		list.add("/WeixinService/business/query/modifyMoney.shtml");
		
		list.add("/WeixinService/business/fundAppoint.xhtml");
		list.add("/WeixinService/business/fundTrade.xhtml");
		list.add("/WeixinService/business/fundTradeLineUp.xhtml");
		list.add("/WeixinService/business/cancelAppointRequest.xhtml");
		list.add("/WeixinService/business/modifyAppointRequest.xhtml");
		
		//风险测评
		list.add("/WeixinService/business/riskRating.xhtml");
		list.add("/WeixinService/business/addReportReadRecord.xhtml");
		
		//账户操作
		list.add("/WeixinService/business/user/modifyRegMobile.shtml");
		list.add("/WeixinService/business/user/modifyTPassword.shtml");
		list.add("/WeixinService/business/user/resetTPassword.shtml");
		list.add("/WeixinService/business/user/setTPassword.shtml");
		list.add("/WeixinService/resetPassword.shtml");
		list.add("/WeixinService/setUp/resetLPassWord.xhtml");
		
		list.add("/WeixinService/business/setTpassword.xhtml");
		list.add("/WeixinService/business/modifyTPassWord.xhtml");
		list.add("/WeixinService/business/checkTpassword.xhtml");
		list.add("/WeixinService/business/resetTpassword.xhtml");
		list.add("/WeixinService/business/checkTPswByModifyMobile.xhtml");
		list.add("/WeixinService/business/user/modifyRegMobile.shtml");
		list.add("/WeixinService/business/modifyUserMobile.xhtml");
		list.add("/WeixinService/business/modifyMobile.xhtml");
		list.add("/WeixinService/business/setTpassword.xhtml");
		list.add("/WeixinService/business/bankAuthenticationByResetTPass.xhtml");
		
		list.add("/WeixinService/setUp/sendSmsMsg.xhtml");
	}

	public static List<String> getList() {
		return list;
	}
	
	
	

	/*public static void main(String[] args) {
		String url = "/WeixinService/otherIERegister.shtml?aaa=bbb";
		if(MeccReqUrlList.list.contains(url)){
			System.out.println("aaa");
		}else{
			for (String str : MeccReqUrlList.list) {
				if(url.contains(str)){
					System.out.println("aaa");
				}else{
					System.out.println("bbb");
					return;
				}
			}
			
		}
	}*/
	
}
