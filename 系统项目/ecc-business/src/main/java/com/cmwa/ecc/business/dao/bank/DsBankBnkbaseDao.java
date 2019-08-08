package com.cmwa.ecc.business.dao.bank;

import java.util.List;

import com.cmwa.ecc.business.entity.bank.DsBankBnkbaseVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;
@MybatisDao
public interface DsBankBnkbaseDao {

    /**
     * 新增银行基本信息
     * @param bankBnkbaseVo 银行基本信息
     * @return bankBnkbaseVo
     * @throws Exception
     */
    public void addDsBankBaseInfo(DsBankBnkbaseVo dsBankBnkbaseVo) throws Exception;

    /**
     * 修改银行基本信息
     * @param bankBnkbaseVo 银行基本信息
     * @return bankBnkbaseVo
     * @throws Exception
     */
    public void updateDsBankBaseInfo(DsBankBnkbaseVo dsBankBnkbaseVo) throws Exception;

    /**
     * 删除银行基本信息
     * @param bnkno 银行代码
     * @return boolean
     * @throws Exception
     */
    public void deleteDsBankBaseInfo( String bnkno) throws Exception;

    /**
     * 获取单个银行基本信息
     * @param bnkno 银行代码
     * @return DsBankBnkbaseVo 银行基本信息
     * @throws Exception
     */
    public DsBankBnkbaseVo getDsBankBaseInfo(String bnkno) throws Exception;
    
    
    /** 
     * 
     * 查询分页总数
     * 
     * 
     * */
    public int queryDsBankBnkbaseInfoListTotal(SearchParam sp);
    /**
     * 获取所有银行基本信息
     * @return List
     * @throws Exception
     */
    public List<DsBankBnkbaseVo> queryBankBnkbaseListPage(SearchParam sp) throws Exception;
}