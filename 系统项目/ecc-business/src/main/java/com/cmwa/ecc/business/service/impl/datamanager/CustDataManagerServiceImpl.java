package com.cmwa.ecc.business.service.impl.datamanager;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.datamanager.CustDataManagerDao;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.service.datamanager.CustDataManagerService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.ExcelUtil;
import com.cmwa.ecc.business.utils.MagicMap;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 客户资料管理
 * @author ex-liuy
 *
 */
@Service
public class CustDataManagerServiceImpl implements CustDataManagerService {
	private final Logger logger = LoggerFactory.getLogger(CustDataManagerServiceImpl.class);
	
	@Autowired
	private CustDataManagerDao custDataManagerDao;
	
	@Override
	public Page<OpenAccountDto> queryCustDataInfoListPage(SearchParam sp) {
		List<OpenAccountDto> custDataList = new ArrayList<OpenAccountDto>();
		try{
			String strDate = (String)sp.getSp().get("strDate");
			String endDate = (String)sp.getSp().get("endDate");
			if(!StringUtils.isEmpty(strDate)){
				sp.getSp().put("strDate", strDate.replaceAll("-", ""));
			}
			if(!StringUtils.isEmpty(endDate)){
				sp.getSp().put("endDate", endDate.replaceAll("-", ""));
			}
			custDataList = custDataManagerDao.queryCustDataInfoListPage(sp);
		}catch(Exception e){
			logger.error("-----CustDataManagerServiceImpl-queryCustDataInfoListPage-Exception:",e);
		}
		return Page.create(custDataList, sp.getStart(), sp.getLimit(), sp.getTotal());
	}

	@Override
	public JSONObject queryExportCustDataInfo(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
		result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		SearchParam sp = new SearchParam();
		try{
			String data = request.getParameter("data");
			data = StringEscapeUtils.unescapeHtml4(data);
			if(!StringUtils.isEmpty(data)){
				try{
					JSONObject param = JSONObject.fromObject(data);
					sp.getSp().putAll(param.getJSONObject("sp"));
				}catch(Exception e){
					logger.error("-----queryExportCustDataInfo-JSONObject.fromObject(data)-Exception-"+data.toString(),e);
				}
			}
			List<OpenAccountDto> custDataList = new ArrayList<OpenAccountDto>();
			String strDate = (String)sp.getSp().get("strDate");
			String endDate = (String)sp.getSp().get("endDate");
			if(!StringUtils.isEmpty(strDate)){
				sp.getSp().put("strDate", strDate.replaceAll("-", ""));
			}
			if(!StringUtils.isEmpty(endDate)){
				sp.getSp().put("endDate", endDate.replaceAll("-", ""));
			}
			custDataList = custDataManagerDao.queryCustDataInfoAllList(sp);
			/**
			 * 转换数据-显示名
			 */
			exportConvertDtoDispName(custDataList);
			LinkedHashMap<String, String> headLineAlias = new LinkedHashMap<String, String>();
			headLineAlias.put("apdt","申请时间");
			headLineAlias.put("fileno","文件编号");
			headLineAlias.put("fundacct","基金账号");
			headLineAlias.put("invnm","客户名称");
			headLineAlias.put("apkindName","业务类型");
			headLineAlias.put("invprtp","投资者类型");
			headLineAlias.put("ifOriginal","是否原件");
			headLineAlias.put("ifalldocument","是否齐全");
			headLineAlias.put("isscan","是否扫描");
			headLineAlias.put("ifsaved","是否归档");
			headLineAlias.put("keepaddress","归档位置");
			headLineAlias.put("isupload","是否上传");
			headLineAlias.put("salesaccmanager","所属客户经理");
			headLineAlias.put("remarkinfo","备注");
			HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(custDataList, headLineAlias);
			OutputStream output = response.getOutputStream();  
			try{
				//返回给前端信息  
				response.addHeader("Content-Disposition","inline;filename="+URLEncoder.encode("客户资料", "utf-8")+".xls");
				response.setContentType("application/msexcel");
				wb.write(output);
			}catch(Exception e){
				logger.error("----CustDataManagerServiceImpl-queryExportCustDataInfo-Exception:",e);
			}finally{
				output.flush();
				output.close();
			}
		}catch(Exception e){
			logger.error("----CustDataManagerServiceImpl-queryExportCustDataInfo-Exception:",e);
		}
		return result;
	}
	
	/**
	 * 导出
	 * 转换-查询中代码转为名称
	 * @param custDataList
	 */
	private void exportConvertDtoDispName(List<OpenAccountDto> custDataList){
		try{
			for (OpenAccountDto openAccountDto : custDataList) {
				String apdt = openAccountDto.getApdt().trim();
				try{
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
					DateUtils.formatDate(sf.parse(apdt), "yyyy-MM-dd");
				}catch(Exception e){
					logger.error("----exportConvertDtoDispName-DateUtils.formatDate-Exception-"+custDataList.toString(),e);
				}
				openAccountDto.setInvprtp("0".equals(openAccountDto.getInvprtp().trim()) ? "专业投资者":"普通投资者");
				openAccountDto.setIfOriginal("1".equals(openAccountDto.getIfOriginal().trim()) ?"是":"否");
				openAccountDto.setIfalldocument("1".equals(openAccountDto.getIfalldocument().trim()) ?"是":"否");
				openAccountDto.setIsscan("1".equals(openAccountDto.getIsscan().trim()) ?"是":"否");
				openAccountDto.setIfsaved("1".equals(openAccountDto.getIfsaved().trim()) ?"是":"否");
				openAccountDto.setIsupload("1".equals(openAccountDto.getIsupload().trim()) ?"是":"否");
			}
		}catch(Exception e){
			logger.error("----exportConvertDtoDispName-Exception-"+custDataList.toString(),e);	
		}
	}

	@Override
	public OpenAccountDto queryCustDataInfoByCondtion(SearchParam sp) {
		OpenAccountDto dto = new OpenAccountDto();
		try{
			dto = custDataManagerDao.queryCustDataInfoByCondtion(sp);
		}catch(Exception e){
			logger.error("----queryCustDataInfoByCondtion-Exception:"+sp.toString(),e);
		}
		return dto;
	}

	@Override
	public JSONObject updateCustDataManagerInfo(OpenAccountDto dto) {
		JSONObject result = new JSONObject();
		result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
		result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		try {
			String[] docArr = dto.getCustinfoid().split(",");
			Object[] docinfo = new Object[docArr.length];
			for (int i = 0; i < docArr.length; i++) {
				String[] docObject = new String[2];
				if (docArr[i] == null || "$".equals(docArr[i])) {
					docObject[0] = "";
					docObject[1] = "";
				} else {
					String[] s2 = docArr[i].split("-");
					if (s2.length == 2) {
						docObject[0] = s2[0];
						docObject[1] = s2[1];
					}
				}
				docinfo[i] = docObject;
			}
			//资料字符串*********************************************************
			String newDocStr = getStringByArrays(docinfo,"||;","||,");
			
			MagicMap paramMap = new MagicMap(new Object[][]{
					{"PI_TRANSCD","8007"},{"PI_OPID",dto.getModifylist()},{"PI_SERIALNO",dto.getAppserialno()},{"PI_APTYPE",dto.getAptype()},
					{"PI_CUSTINFOSTAT",newDocStr},{"PI_ISFULL",dto.getIfalldocument()},{"PI_ORIGINAL",dto.getIfOriginal()},{"PI_FILED",dto.getIfsaved()},
					{"PI_ATTACH",dto.getIsupload()},{"PI_REMARK",dto.getRemarkinfo()},{"PI_ISSCAN",dto.getIsscan()},{"PI_KEEPADDRESS",dto.getKeepaddress()},
					{"PI_SALESACCMANAGER",dto.getSalesaccmanager()},{"PI_FILENO",dto.getFileno()},{"PI_CUSTNO",dto.getCustno()},{"PO_ERRCOD",""},{"PO_ERRMSG",""}
			});
			custDataManagerDao.updateCustDataManagerInfo(paramMap);
			if(paramMap.containsKey("PO_ERRCOD")){
				String PO_RETCOD = (String)paramMap.get("PO_ERRCOD");
				String PO_RETMSG = (String)paramMap.get("PO_ERRMSG");
				result.put(ResultConstant.C_RESULT_CODE, PO_RETCOD);
				result.put(ResultConstant.C_RESULT_MSG, PO_RETMSG);
			}
		} catch (Exception e) {
			logger.error("----updateCustDataManagerInfo-Exception-"+dto.toString(),e);
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
		}
		
		return result;
	}
	
	public String getStringByArrays(Object[] arrays,String maxDivision,String minDivision){
		String retStr = "";
		try{
			for(int i=0;i<arrays.length;i++){
				String[] s2 = (String[])arrays[i];
				if(s2 != null && s2.length>0){
					for(int j=0;j<s2.length;j++){
						if(j==s2.length-1){
							retStr += s2[j];
						}else{
							retStr += s2[j] + minDivision;
						}
					}
				}
				if(i<arrays.length-1){
					retStr += maxDivision;
				}
			}
			if(retStr.equals("null||,null")){
				retStr = "";
			}
			logger.info("::getStringByArrays：："+retStr);
		}catch(Exception e){
			logger.info("----getStringByArrays-"+arrays.toString(),e);
		}
		return retStr;
	}
}
