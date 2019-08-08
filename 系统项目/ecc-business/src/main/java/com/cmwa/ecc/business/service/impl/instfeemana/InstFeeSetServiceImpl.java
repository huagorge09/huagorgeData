package com.cmwa.ecc.business.service.impl.instfeemana;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.instfeemana.InstFeeSetDao;
import com.cmwa.ecc.business.entity.ratediscmgr.InstDiscounVo;
import com.cmwa.ecc.business.service.instfeemana.InstFeeSetService;
import com.cmwa.ecc.business.utils.MagicMap;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
/**
 * 财富柜台费率设置后端接口
 * @author ex-liuy
 *
 */
@Service
public class InstFeeSetServiceImpl implements InstFeeSetService {

	private Logger logger = LoggerFactory.getLogger(InstFeeSetServiceImpl.class);
	@Autowired
	private InstFeeSetDao instFeeSetDao;
	
	@Override
	public Page<InstDiscounVo> queryInstDiscountListPage(SearchParam sp) {
		List<InstDiscounVo> instDiscounVos = new ArrayList<InstDiscounVo>();
		try{
			instDiscounVos = instFeeSetDao.queryInstDiscountListPage(sp);
		}catch(Exception e){
			logger.error("----InstFeeSetServiceImpl--queryInstDiscountListPage-Exception:"+sp.toString(),e);
		}
		return Page.create(instDiscounVos, sp.getStart(), sp.getLimit(), sp.getTotal());
	}

	@Override
	public List<InstDiscounVo> queryInstDiscountAllList(SearchParam sp) {
		List<InstDiscounVo> instDiscounVos = new ArrayList<InstDiscounVo>();
		try{
			instDiscounVos = instFeeSetDao.queryInstDiscountAllList(sp);
		}catch(Exception e){
			logger.error("----InstFeeSetServiceImpl--queryInstDiscountAllList-Exception:"+sp.toString(),e);
		}
		return instDiscounVos;
	}

	@Override
	public JSONObject addInstFeeSetInfo(InstDiscounVo paramInstDiscounVo) {
		JSONObject result = new JSONObject();
		try{
			if(StringUtils.isEmpty(paramInstDiscounVo.getInstType()) && StringUtils.isEmpty(paramInstDiscounVo.getFundId()) 
					&& StringUtils.isEmpty(paramInstDiscounVo.getApkind())){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "操作失败：参数不能为空");
				return result;
			}
			
			List<InstDiscounVo> instDiscounVos =  multiInstFeeSetToList(paramInstDiscounVo);
			StringBuffer strBuff = new StringBuffer();
			for (InstDiscounVo instDiscounVo : instDiscounVos) {
				
				MagicMap paramMap = new MagicMap(new Object[][]{
						{"PI_SERIALNO",""},{"PI_INSTTYPE",instDiscounVo.getInstType()},{"PI_FUNDID",instDiscounVo.getFundId()},
						{"PI_APKIND",instDiscounVo.getApkind()},{"PI_STRAMT",instDiscounVo.getStrAmt()},{"PI_ENDAMT",instDiscounVo.getEndAmt()},
						{"PI_DISCOUNT",instDiscounVo.getDiscount()},{"PI_CMAN",instDiscounVo.getCreateId()},{"PI_EMAN",instDiscounVo.getModifyId()},
						{"PI_CONFMAN",instDiscounVo.getCheckId()},{"PI_STATUS",instDiscounVo.getStatus()},{"PI_CKSTATUS","N"},
						{"PI_OPERATYPE","ADD"},{"PO_RETCOD",""},{"PO_RETMSG",""}
				});
				instFeeSetDao.operateInstFeeSetInfo(paramMap);
				if(paramMap.containsKey("PO_RETCOD")){
					String PO_RETCOD = (String)paramMap.get("PO_RETCOD");
					String PO_RETMSG = (String)paramMap.get("PO_RETMSG");
					if(!ResultConstant.C_RESULT_SUCCESS.equalsIgnoreCase(PO_RETCOD)){
						strBuff.append("</br>"+PO_RETMSG);
					}
				}
			}
			if(instDiscounVos.size() <= 0 ){
				strBuff.append("操作失败：解析参数错误！");
			}
			if(strBuff.length() > 0){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, strBuff.toString());
			}else{
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
				result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
			}
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----InstFeeSetServiceImpl-addInstFeeSetInfo-Exception:"+paramInstDiscounVo.toString(),e);
		}
		return result;
	}

	private List<InstDiscounVo> multiInstFeeSetToList(InstDiscounVo instDiscounVo) {
		List<InstDiscounVo> instDiscounVos  = new ArrayList<InstDiscounVo>();
		try{
			String[] instTypes = instDiscounVo.getInstType().split(",");
			String[] fundIds = instDiscounVo.getFundId().split(",");
			for (int i = 0; i < instTypes.length; i++) {
				String instType = instTypes[i];
				for (int j = 0; j < fundIds.length; j++) {
					String fundId = fundIds[j];
					InstDiscounVo saveDiscounVo = (InstDiscounVo)BeanUtils.cloneBean(instDiscounVo);
					saveDiscounVo.setInstType(instType);
					saveDiscounVo.setFundId(fundId);
					instDiscounVos.add(saveDiscounVo);
					
				}
			}
		}catch(Exception e){
			logger.error("----InstFeeSetServiceImpl-multiInstFeeSetToList-Exception:"+instDiscounVo.toString(),e);
		}
		return instDiscounVos;
	}

	@Override
	public JSONObject checkInstFeeSetInfo(InstDiscounVo instDiscounVo) {
		JSONObject result = new JSONObject();
		try{
			if(StringUtils.isEmpty(instDiscounVo.getSerialNo())){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "操作失败：参数不能为空");
				return result;
			}
			
			MagicMap paramMap = new MagicMap(new Object[][]{
					{"PI_SERIALNO",instDiscounVo.getSerialNo()},{"PI_INSTTYPE",""},{"PI_FUNDID",""},
					{"PI_APKIND",""},{"PI_STRAMT",""},{"PI_ENDAMT",""},
					{"PI_DISCOUNT",""},{"PI_CMAN",""},{"PI_EMAN",""},
					{"PI_CONFMAN",instDiscounVo.getCheckId()},{"PI_STATUS",""},{"PI_CKSTATUS",instDiscounVo.getCheckStatus()},
					{"PI_OPERATYPE","CHECK"},{"PO_RETCOD",""},{"PO_RETMSG",""}
			});
			instFeeSetDao.operateInstFeeSetInfo(paramMap);
			String PO_RETCOD = "0000";
			String PO_RETMSG = "";
			
			if(paramMap.containsKey("PO_RETCOD")){
				PO_RETCOD = (String)paramMap.get("PO_RETCOD");
				PO_RETMSG = (String)paramMap.get("PO_RETMSG");
			}
			result.put(ResultConstant.C_RESULT_CODE, PO_RETCOD);
			result.put(ResultConstant.C_RESULT_MSG, PO_RETMSG);
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----InstFeeSetServiceImpl-checkInstFeeSetInfo-Exception:"+instDiscounVo.toString(),e);
		}
		return result;
	}

	@Override
	public JSONObject updateInstFeeSetInfo(InstDiscounVo instDiscounVo) {
		JSONObject result = new JSONObject();
		try{
			if(StringUtils.isEmpty(instDiscounVo.getSerialNo())){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "操作失败：参数不能为空");
				return result;
			}
			
			MagicMap paramMap = new MagicMap(new Object[][]{
					{"PI_SERIALNO",instDiscounVo.getSerialNo()},{"PI_INSTTYPE",instDiscounVo.getInstType()},{"PI_FUNDID",instDiscounVo.getFundId()},
					{"PI_APKIND",instDiscounVo.getApkind()},{"PI_STRAMT",instDiscounVo.getStrAmt()},{"PI_ENDAMT",instDiscounVo.getEndAmt()},
					{"PI_DISCOUNT",instDiscounVo.getDiscount()},{"PI_CMAN",instDiscounVo.getCreateId()},{"PI_EMAN",instDiscounVo.getModifyId()},
					{"PI_CONFMAN",instDiscounVo.getCheckId()},{"PI_STATUS",instDiscounVo.getStatus()},{"PI_CKSTATUS","N"},
					{"PI_OPERATYPE","UPDATE"},{"PO_RETCOD",""},{"PO_RETMSG",""}
			});
			instFeeSetDao.operateInstFeeSetInfo(paramMap);
			String PO_RETCOD = "0000";
			String PO_RETMSG = "";
			
			if(paramMap.containsKey("PO_RETCOD")){
				PO_RETCOD = (String)paramMap.get("PO_RETCOD");
				PO_RETMSG = (String)paramMap.get("PO_RETMSG");
			}
			result.put(ResultConstant.C_RESULT_CODE, PO_RETCOD);
			result.put(ResultConstant.C_RESULT_MSG, PO_RETMSG);
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----InstFeeSetServiceImpl-checkInstFeeSetInfo-Exception:"+instDiscounVo.toString(),e);
		}
		return result;
	}

	@Override
	public JSONObject delInstFeeSetInfo(InstDiscounVo instDiscounVo) {
		JSONObject result = new JSONObject();
		try{
			if(StringUtils.isEmpty(instDiscounVo.getSerialNo())){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "操作失败：参数不能为空");
				return result;
			}
			
			MagicMap paramMap = new MagicMap(new Object[][]{
					{"PI_SERIALNO",instDiscounVo.getSerialNo()},{"PI_INSTTYPE",instDiscounVo.getInstType()},{"PI_FUNDID",instDiscounVo.getFundId()},
					{"PI_APKIND",instDiscounVo.getApkind()},{"PI_STRAMT",instDiscounVo.getStrAmt()},{"PI_ENDAMT",instDiscounVo.getEndAmt()},
					{"PI_DISCOUNT",instDiscounVo.getDiscount()},{"PI_CMAN",instDiscounVo.getCreateId()},{"PI_EMAN",instDiscounVo.getModifyId()},
					{"PI_CONFMAN",instDiscounVo.getCheckId()},{"PI_STATUS",instDiscounVo.getStatus()},{"PI_CKSTATUS","N"},
					{"PI_OPERATYPE","DEL"},{"PO_RETCOD",""},{"PO_RETMSG",""}
			});
			instFeeSetDao.operateInstFeeSetInfo(paramMap);
			String PO_RETCOD = "0000";
			String PO_RETMSG = "";
			
			if(paramMap.containsKey("PO_RETCOD")){
				PO_RETCOD = (String)paramMap.get("PO_RETCOD");
				PO_RETMSG = (String)paramMap.get("PO_RETMSG");
			}
			result.put(ResultConstant.C_RESULT_CODE, PO_RETCOD);
			result.put(ResultConstant.C_RESULT_MSG, PO_RETMSG);
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----InstFeeSetServiceImpl-checkInstFeeSetInfo-Exception:"+instDiscounVo.toString(),e);
		}
		return result;
	}

}
