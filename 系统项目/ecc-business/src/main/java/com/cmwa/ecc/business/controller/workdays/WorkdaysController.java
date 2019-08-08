package com.cmwa.ecc.business.controller.workdays;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.fundinfo.FundManagerVo;
import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.service.fundinfo.FundInfoManagerService;
import com.cmwa.ecc.business.service.workdays.WorkdaysService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/service/workdaysManager")
public class WorkdaysController {
		
	@Autowired
	private WorkdaysService workdaysService;
	
	@Autowired
	private FundInfoManagerService fundInfoManagerService;
	
	/**
	 * 跳转至分页数据页面
	 *  
	 * */
	@RequestMapping(value = "/workdaysMgr.do",method=RequestMethod.GET)
	public String goWorkdaysManagerView() {
		return "jsp/workdaysMgr/workdaysInfo";
	}
	
	/**
	 * 打开新增工作日页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/goAddPage.xhtml")
	public ModelAndView goAddPage(ModelAndView model) throws IOException{
		model.addObject("method", "add");
		model.setViewName("jsp/workdaysMgr/workdayAddOrUpdate");
		return model;
	}
	
	/**
	 * 新增工作日
	 * @param request
	 * @param response
	 * @param workdaysVo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/createWorkday.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public JSONObject createWorkday(HttpServletRequest request,HttpServletResponse response,WorkdaysVo workdaysVo) throws IOException{
		String operator = SessionUtils.getEmployee().getID();
		workdaysVo.setWorkdate((workdaysVo.getWorkdate().equals("") || workdaysVo.getWorkdate() == null) ? "" : workdaysVo.getWorkdate().replace("-", ""));
		try {
			workdaysVo = workdaysService.create(operator, workdaysVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", workdaysVo.getResultCode()==null?"":workdaysVo.getResultCode());
		return jsonObject;
	}
	
	/**
	 * 打开修改工作日页面
	 * @param request
	 * @param response
	 * @param workdaysVo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/goUpdatePage.xhtml")
	public ModelAndView goUpdatePage(HttpServletRequest request , ModelAndView model,@Param("fundid")String fundid,@Param("workdate")String workdate) throws IOException{
		WorkdaysVo workdaysVo = new WorkdaysVo();
		try {
			workdaysVo = workdaysService.getWorkDate(fundid, workdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("method", "update");
		model.addObject("workdaysVo", workdaysVo);
		model.setViewName("jsp/workdaysMgr/workdayAddOrUpdate");
		return model;
	}
	
	/**
	 * 修改工作日
	 * @param request
	 * @param response
	 * @param workdaysVo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/updateWorkday.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public JSONObject updateWorkday(HttpServletRequest request,HttpServletResponse response,WorkdaysVo workdaysVo) throws IOException{
		String operator = SessionUtils.getEmployee().getID();
		WorkdaysVo WorkdaysVo = new WorkdaysVo();
		try {
			WorkdaysVo = workdaysService.update(operator, workdaysVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", WorkdaysVo.getResultCode()==null?"":WorkdaysVo.getResultCode());
		return jsonObject;
	}
	
	/**
	 * 删除工作日
	 * @param request
	 * @param response
	 * @param fundid
	 * @param workdate
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/deleteWorkday.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public JSONObject deleteWorkday(HttpServletRequest request,
								HttpServletResponse response,
								@RequestParam("fundid")String fundid,
								@RequestParam("workdate")String workdate) throws IOException{
		String operator = SessionUtils.getEmployee().getID();
		WorkdaysVo WorkdaysVo = new WorkdaysVo();
		try {
			WorkdaysVo = workdaysService.delete(operator, fundid, workdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", WorkdaysVo.getResultCode()==null?"":WorkdaysVo.getResultCode());
		return jsonObject;
	}
	
	/**
	 * 获取单个工作日信息
	 * @param request
	 * @param response
	 * @param fundid
	 * @param workdate
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getWorkDate.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public WorkdaysVo getWorkDate(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("fundid")String fundid,
			@RequestParam("workdate")String workdate) throws IOException{
		WorkdaysVo workdaysVo = new WorkdaysVo();
		try {
			workdaysVo = workdaysService.getWorkDate(fundid, workdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workdaysVo;
	}
	
	/**
	 * 获取所有工作日信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getWorkDateList.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<WorkdaysVo> getWorkDateList(SearchParam sp) throws IOException{
		Page<WorkdaysVo> list = new Page<WorkdaysVo>();
		try {
			list = workdaysService.getWorkDateListPage(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 打开新增工作日页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/goAutoAddPage.xhtml")
	public ModelAndView goAutoAddPage(ModelAndView model) throws IOException{
		model.setViewName("jsp/workdaysMgr/autoAddWorkday");
		return model;
	}
	
	/**
	 * 生成当前年份及之后9个年份
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getYearsList.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public List<String> getYearsList() throws IOException{
		List<String> list = new ArrayList<String>();
		Calendar cld = Calendar.getInstance();
		int curYear = cld.get(Calendar.YEAR);
		for (int i=0; i<10 ; i++){
			String iYear = curYear+i+"";
			list.add(iYear);
		}
		return list;
	}
	
	/**
	 * 获取所有基金信息
	 * @return
	 */
	@RequestMapping(value="/getFundAllInfo.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public List<FundManagerVo> getFundAllInfo(){
		/*FundInfoCached fundInfoCache=new FundInfoCached();*/
		List<FundManagerVo> funds = fundInfoManagerService.getAllFundInfo();
		return funds;
	}
	
	/**
	 * 新增工作日
	 * @param request
	 * @param response
	 * @param workdaysVo
	 * @return
	 * @throws IOException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/autoCreateWorkday.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public JSONObject autoCreateWorkday(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		JSONObject jsonObject = new JSONObject();
		String operator = SessionUtils.getEmployee().getID();
		String year = request.getParameter("years");
	    String offday = request.getParameter("offday");
	    String[] startMonths = request.getParameterValues("startMonth");
		String[] startDays = request.getParameterValues("startDay");
		String[] endMonths = request.getParameterValues("endMonth");
		String[] endDays = request.getParameterValues("endDay");
		String[] descs = request.getParameterValues("desc");
		String[] applys = request.getParameterValues("apply");

		@SuppressWarnings("rawtypes")
		Hashtable holiday = new Hashtable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < applys.length; i++) {
			if (applys[i].equals("N")) {
				continue;
			}
			if (startMonths[i].equals("") || endMonths[i].equals("")) {
				continue;
			}

			//不处理无效的开始日期
			Date startDate = null;
			try {
				startDate = sdf.parse(year + "-" + startMonths[i] + "-" + startDays[i]);
			}
			catch (ParseException ex) {
				ex.printStackTrace();
				continue;
			}

			Date endDate = null;
			try {
				endDate = sdf.parse(year + "-" + endMonths[i] + "-" + endDays[i]);
			}
			catch (ParseException ex) {
				ex.printStackTrace();
				continue;
			}

			if (startDate.after(endDate)) {
				continue;
			}

			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			
			//System.out.println("-------:"  + sdf.format(startCal.getTime()));

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			
			/*int a = 0;
			Calendar cal = Calendar.getInstance();

			for (int o = 0 ; o <= 4 ; o ++ ){
				cal.set(Integer.parseInt(year),Calendar.DECEMBER,a ++);
				//cal.setTime(sdf.parse(year + "-" + "12" + "-" + "30"));
				cal.add(Calendar.DAY_OF_MONTH,1);
				o ++;
				String tdate = sdf.format(cal.getTime());

				if (!holiday.containsKey(tdate)) {
					holiday.put(tdate, descs[i]);
				}
			}*/
			
			while (startCal.before(endCal)) {
				String tdate = sdf.format(startCal.getTime());
				if (!holiday.containsKey(tdate)) {
					holiday.put(tdate, descs[i]);
				}
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
			String tdate = sdf.format(endCal.getTime());
			if (!holiday.containsKey(tdate)) {
				holiday.put(tdate, descs[i]);
			}
		}

		//处理双休日
		boolean isOffday = false;
		if (offday.equals("Y")) {
			isOffday = true;
		}
		
		if (isOffday) {
			Calendar offdayStartCal = Calendar.getInstance();
			offdayStartCal.setTime(sdf.parse(year + "-" + "01-01"));
			Calendar offdayEndCal = Calendar.getInstance();
			offdayEndCal.setTime(sdf.parse(year + "-" + "01-01"));
			offdayEndCal.add(Calendar.YEAR, 1);
			while (offdayStartCal.before(offdayEndCal)) {
				int weekidx = offdayStartCal.get(Calendar.DAY_OF_WEEK);
				String tmpDate = sdf.format(offdayStartCal.getTime());
				if (weekidx == 1 || weekidx == 7) {
					if (!holiday.containsKey(tmpDate)) {
						holiday.put(tmpDate, "双休日");
					}
				}
				offdayStartCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		
		String key = "";
		String value = "";
		String fundid = "******";
		String workFlag = "Y";
		String workdate = "";

		int acount = 0;
		Date everydate;
		Calendar calendar = Calendar.getInstance();
		
		//得到一年所有的天数
		for (int j = 1 ; j <= 731 ; j ++ ){
			calendar.set(Integer.parseInt(year),Calendar.JANUARY,acount++);
			calendar.add(Calendar.DAY_OF_MONTH,1);
			j ++;
			everydate = calendar.getTime();
			//String weekday = calendar.get(Calendar.DAY_OF_WEEK);
			String Workdate = sdf.format(everydate);
			workdate = Workdate.substring(0,4) + Workdate.substring(5,7) + Workdate.substring(8,10);

			Enumeration em = holiday.keys();
			while (em.hasMoreElements()) {
				String Key = (String) em.nextElement();
				key = Key.substring(0,4) + Key.substring(5,7) + Key.substring(8,10);
				System.out.println("节假日" +key);
				value = (String) holiday.get(key);
				if (workdate.trim().equals(key.trim())){
					if("20191202".equals(workdate)) {
					}
						workFlag = "N";
						break;
					}else{
						workFlag = "Y";
					}
			}

			WorkdaysVo workdaysVo = new WorkdaysVo();
			workdaysVo.setFundid(fundid);
			workdaysVo.setWorkflag(workFlag);
			workdaysVo.setWorkdate(workdate);
			
			try {
				workdaysVo = workdaysService.create(operator, workdaysVo);
				jsonObject.put("resultCode", workdaysVo.getResultCode()==null?"":workdaysVo.getResultCode());
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		return jsonObject;
	}
}

