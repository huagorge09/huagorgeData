package com.cmwa.ecc.business.dao.common;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.common.AttachVo;
import com.cmwa.ecc.business.entity.fundinfo.ProductEstimateDto;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@MybatisDao
public interface AttachDao {
	/**
	 * 上传电子合同附件
	 * @param attachVo
	 */
	public void saveEleContract(AttachVo attachVo);
	
	/**
	 * 获取序列
	 * @return
	 */
	public int getEleContract();
	
	/**
	 * 删除附件文件
	 * @param attId
	 */
	public void delEleContractAttach(@Param("attId")String attId);

	/**
	 * 获取附件列表
	 * @param attachId
	 * @return
	 */
	public List<AttachVo> queryEleContractAttachList(String[] attachId);

	public AttachVo queryEleContractAttach(@Param("attId")String attId);
	
	
	public List<ProductEstimateDto> queryEstimateFromCrmBp();

	/**
	 * 获取产品净值
	 * @param productIdList
	 * @param workDate
	 * @return
	 */
	public List<ProductEstimateDto> queryEstimateFromCrmBp(
			@Param("list")List<ProductInfoDto> productIdList,@Param("workDate") String workDate);
}
