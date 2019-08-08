package com.cmwa.ecc.business.service.bank;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.bank.DsBankBnkbaseVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface DsBankBnkBaseService {
	 /**
     * 新增银行基本信息
     * @param bankBnkbaseVo 银行基本信息
     * @return bankBnkbaseVo
     * @throws Exception
     */
    public JSONObject addDsBankBaseInfo(DsBankBnkbaseVo bankBnkbaseVo) throws Exception;

    /**
     * 修改银行基本信息
     * @param bankBnkbaseVo 银行基本信息
     * @return bankBnkbaseVo
     * @throws Exception
     */
    public JSONObject updateDsBankBaseInfo( DsBankBnkbaseVo DsBankBnkbaseVo) throws Exception;

    /**
     * 删除银行基本信息
     * @param bnkno 银行代码
     * @return boolean
     * @throws Exception
     */
    public JSONObject deleteDsBankBaseInfo(HttpServletRequest request) throws Exception;

    /**
     * 
     * 模态窗调取编辑和更新窗口
     * 
     */
	public ModelAndView openDialog(HttpServletRequest request)  throws Exception ;

    /**
     * 获取单个银行基本信息
     * @param bnkno 银行代码
     * @return DsBankBnkbaseVo 银行基本信息
     * @throws Exception
     */
    public DsBankBnkbaseVo getDsBankBaseInfo(String bnkno) throws Exception;

    /**
     * 获取所有银行基本信息
     * @return List
     * @throws Exception
     */
    public Page<DsBankBnkbaseVo> queryBankBnkbaseListPage(SearchParam sp) throws Exception;
}
