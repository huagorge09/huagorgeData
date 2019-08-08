package com.cmwa.ec.webapp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmwa.ec.query.facade.dto.fund.FeeRateDto;

public class QueryFeeRateUtils {
	
	public static Map<String, Double> queryFeeRate(Map<String, List<FeeRateDto>> map,String fundId,List<String> channelNoList,String money){
		Map<String , Double> returnMap = new HashMap<String, Double>();
		double rate = 0.0;
		double commro = 1.0;
		double feeMode = 0;
		double d = Double.parseDouble(money);
		String channelNo = "";
		if(channelNoList == null || channelNoList.size() == 0){
			channelNo = "*";
		}else{
			channelNo = channelNoList.get(0);
		}
		List<FeeRateDto> list = map.get(channelNo);
		for (FeeRateDto dto : list) {
			if(fundId.equals(dto.getFundId()) && channelNo.equals(dto.getChannelNo()) && d >= dto.getStrAmt() && d < dto.getEndAmt()){
				commro = dto.getCommro();
				try{
					feeMode =  Double.parseDouble(String.valueOf(dto.getFeeMode()));
				}catch(Exception e){
					e.printStackTrace();
				}
				if(dto.getFeeMode() == '0'){//费率类型  0：按rate比例收费；
					rate = d * dto.getBaseRate() * dto.getCommro();
				}else if(dto.getFeeMode() == '1'){//费率类型   1 按次收费，每次收rate元；  
					rate = dto.getSingleFee() * dto.getCommro();// TODO 暂时  按次收费   没有折扣
				}
			}
		}
		returnMap.put("rate", rate);
		returnMap.put("commro", commro);
		returnMap.put("feeMode", feeMode);
		return returnMap;
	}
	
}
