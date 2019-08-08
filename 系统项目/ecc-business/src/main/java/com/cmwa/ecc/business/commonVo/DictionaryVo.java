package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.utils.Cached;
import com.cmwa.ecc.business.utils.cached.DictionaryCached;

@Alias("dictionaryVo")
public class DictionaryVo {
	private String dctId;          //字典ID
	private String dctRootType;    //字典根类型
	private String dctFathType;    //字典父类型	
	private String dctLeftType;    //字典子类型	
	private String dctName;        //字典名		
	private String dctValue;       //字典值	
	private String dctSortNo;      //排序号	
	private String dctIsLeaf;      //是否叶子
	private String dctStat;        //是否生效
	private String createId;       //经办人ID	
	private String createNm;       //经办人名称
	private String createTime;     //经办时间
	private String checkId;        //复核人ID	
	private String checkNm;        //复核人名称
	private String checkTime;      //复核时间
	private String stat;           //状态

	@Cached
	private String dctIsLeafNM;     //是否子叶对应值  by huangch 2016.5.5
	@Cached
	private String dctStatNM;       //是否生效对应值  by huangch 2016.5.5
	@Cached
	private String statNM;          //状态对应值  by huangch 2016.5.5
	private String oldDctValue;     //查询出来的原有字典值  by huangch 2016.5.5
	
	
	public String getOldDctValue() {
		return oldDctValue;
	}
	public void setOldDctValue(String oldDctValue) {
		this.oldDctValue = oldDctValue;
	}
	public String getDctIsLeafNM() {
		if (dctIsLeaf != null) {
			return DictionaryCached.getDictName("DAT_YES_NOX",dctIsLeaf);
		}
		return dctIsLeafNM;
	}
	public void setDctIsLeafNM(String dctIsLeafNM) {
		this.dctIsLeafNM = dctIsLeafNM;
	}
	public String getStatNM() {
		if (stat != null) {
			return DictionaryCached.getDictName("DAT_CHK_SAT",stat);
		}
		return statNM;
	}
	public void setStatNM(String statNM) {
		this.statNM = statNM;
	}
	public String getDctStatNM() {
		if (dctStat != null) {
			return DictionaryCached.getDictName("DAT_YES_NOX",dctStat);
		}
		return dctStatNM;
	}
	public void setDctStatNM(String dctStatNM) {
		this.dctStatNM = dctStatNM;
	}
	public String getDctId() {
		return dctId;
	}
	public void setDctId(String dctId) {
		this.dctId = dctId;
	}
	public String getDctRootType() {
		return dctRootType;
	}
	public void setDctRootType(String dctRootType) {
		this.dctRootType = dctRootType;
	}
	public String getDctFathType() {
		return dctFathType;
	}
	public void setDctFathType(String dctFathType) {
		this.dctFathType = dctFathType;
	}
	public String getDctLeftType() {
		return dctLeftType;
	}
	public void setDctLeftType(String dctLeftType) {
		this.dctLeftType = dctLeftType;
	}
	public String getDctName() {
		return dctName;
	}
	public void setDctName(String dctName) {
		this.dctName = dctName;
	}
	public String getDctValue() {
		return dctValue;
	}
	public void setDctValue(String dctValue) {
		this.dctValue = dctValue;
	}
	public String getDctSortNo() {
		return dctSortNo;
	}
	public void setDctSortNo(String dctSortNo) {
		this.dctSortNo = dctSortNo;
	}
	public String getDctIsLeaf() {
		return dctIsLeaf;
	}
	public void setDctIsLeaf(String dctIsLeaf) {
		this.dctIsLeaf = dctIsLeaf;
	}
	public String getDctStat() {
		return dctStat;
	}
	public void setDctStat(String dctStat) {
		this.dctStat = dctStat;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateNm() {
		return createNm;
	}
	public void setCreateNm(String createNm) {
		this.createNm = createNm;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getCheckNm() {
		return checkNm;
	}
	public void setCheckNm(String checkNm) {
		this.checkNm = checkNm;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("dctId="+this.getDctId());
		sb.append(", dctRootType="+this.getDctRootType());
		sb.append(", dctFathType="+this.getDctFathType());
		sb.append(", dctLeftType="+this.getDctLeftType());
		sb.append(", dctName="+this.getDctName());
		sb.append(", dctValue="+this.getDctValue());
		sb.append(", dctSortNo="+this.getDctSortNo());
		sb.append(", dctIsLeaf="+this.dctIsLeaf);
		sb.append(", dctStat="+this.getDctStat());
		sb.append(", createId="+this.getCreateId());
		sb.append(", createNm="+this.getCreateNm());
		sb.append(", createTime="+this.getCreateTime());
		sb.append(", checkId="+this.getCheckId());
		sb.append(", checkNm="+this.getCheckNm());
		sb.append(", checkTime="+this.getCheckTime());
		sb.append(", stat="+this.getStat());
		return sb.toString();
	}
	
	public DictionaryVo() {
	 
	}

}
