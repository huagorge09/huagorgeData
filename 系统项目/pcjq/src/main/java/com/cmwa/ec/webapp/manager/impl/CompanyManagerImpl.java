package com.cmwa.ec.webapp.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.FundReportsDto;
import com.cmwa.ec.webapp.client.CompanyClient;
import com.cmwa.ec.webapp.manager.CompanyManager;
import com.cmwa.ec.webapp.util.ECConstants;

public class CompanyManagerImpl implements CompanyManager{
	
	@Autowired
	private CompanyClient companyClient;

	@Override
	public QueryMessageDto branchUserLogin(Context context, String branchName, String fundAcco, String branchLicense) {
		QueryMessageDto queryMessageDto = companyClient.branchUserLogin(context,branchName,fundAcco,branchLicense);
		return queryMessageDto;
	}
	
	@Override
	public QueryMessageDto absUserLogin(Context context, String branchName,
			String type, String branchLicense) {
		QueryMessageDto queryMessageDto = companyClient.absUserLogin(context,branchName,type,branchLicense);
		return queryMessageDto;
	}

	@Override
	public JSONObject queryUserMessageListForCompany(Context context, HttpServletRequest request) {
		JSONObject returnJsonObject=new JSONObject();
		int beginIdx = 0;// 每页展示的开始的条数
        int amount = Integer.parseInt(ECConstants.PAGE_LIST_AMOUNT);// 每页展示的条数
        int totalAmount = -1;// 总条数
        int maxPages = 1;// 总页数
        int page = 1;// 当前页
        String pageStr = request.getParameter("page");
        String totalAmountStr = "";
        String resultCode = "";
		String resultMsg = "";
		String fundAcco = (String) request.getSession(true).getAttribute("fundAcco");
        Object obj = request.getSession(true).getAttribute("totalAmount");
        String type = (String) request.getSession(true).getAttribute("type");
        if(ECConstants.SESSION_USERTYPE.equals(type)){
        	fundAcco = (String) request.getSession(true).getAttribute("branchLicense");
        }
        if (obj != null) {
            totalAmountStr = obj.toString();
        }
        if (pageStr != null && !pageStr.equals("")) {
            page = Integer.parseInt(pageStr);
            if (page <= 0) {// 当前页数 <= 0 , 则当前页 page = 1
                page = 1;
            }
        }
        if (totalAmountStr != null && !totalAmountStr.equals("")) {
            totalAmount = Integer.parseInt(totalAmountStr);
            maxPages = (totalAmount % amount == 0 ? totalAmount / amount : (totalAmount / amount + 1));
        }

        if (page >= maxPages) {
            beginIdx = (maxPages - 1) * amount;
        } else {
            beginIdx = (page - 1) * amount;
        }
        QueryMessageDto queryMessageDto = companyClient.queryUserMessageListForCompany(context, fundAcco, beginIdx, amount, totalAmount,type);
        List<FundReportsDto> fundReportsDtoList = new ArrayList<FundReportsDto>();
        if (queryMessageDto != null) {
            fundReportsDtoList = (List<FundReportsDto>) queryMessageDto.getData();

            String total = (String) queryMessageDto.getOtherData();

            if (total != null && !total.equals("")) {
                totalAmount = Integer.parseInt(total);// 总记录数
            }

            resultCode = queryMessageDto.getResultCode();
            resultMsg = queryMessageDto.getResultMsg();

            if (resultCode != null && resultCode.equals("0000")) {
                if (fundReportsDtoList != null && fundReportsDtoList.size() > 0) {
                    // 计算总页数
                    maxPages = (totalAmount % amount == 0 ? totalAmount / amount : (totalAmount / amount + 1));
                    // 将总记录数保存在session中--totalAmount必须转换为字符串
                    request.getSession(true).setAttribute("totalAmount", totalAmount + "");
                }
                returnJsonObject.put("fundReportsDtoList", fundReportsDtoList);
                returnJsonObject.put("totalAmount", totalAmount + "");
                returnJsonObject.put("maxPage", maxPages);
                returnJsonObject.put("page", page);
            }
            returnJsonObject.put("returnCode", resultCode);
            returnJsonObject.put("returnMsg", resultMsg);
        }
		return returnJsonObject;
	}

	@Override
	public QueryMessageDto queryUserMessageForCompany(Context context, String fundAcco, String msgId,String msgType) {
		QueryMessageDto queryMessageDto = companyClient.queryUserMessageForCompany(context, fundAcco, msgId,msgType);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryUserMessageListByNewTimeForCompany(Context context, String fundAcco, int beginIdx, int amount) {
		QueryMessageDto queryMessageDto = companyClient.queryUserMessageListByNewTimeForCompany(context, fundAcco, beginIdx, amount);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryUserMessageListByFundIdForCompany(Context context, String fundAcco, String fundId, int beginIdx, int amount) {
		QueryMessageDto queryMessageDto = companyClient.queryUserMessageListByFundIdForCompany(context, fundAcco, fundId, beginIdx, amount);
		return queryMessageDto;
	}
}
