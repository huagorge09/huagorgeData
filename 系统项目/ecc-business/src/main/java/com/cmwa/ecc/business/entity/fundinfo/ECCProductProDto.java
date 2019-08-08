package com.cmwa.ecc.business.entity.fundinfo;

import org.apache.ibatis.type.Alias;


/**
 * 产品系列
 * @author ex-huangbl
 *
 */
@Alias("eccProductProDto")
public class ECCProductProDto {
	private String id;
	private String dName;//系列名字
	private String creatDate;
	private String updateDate;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	public String getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "ECCProductProDto [id=" + id + ", dName=" + dName
				+ ", creatDate=" + creatDate + ", updateDate=" + updateDate
				+ ", reserve1=" + reserve1 + ", reserve2=" + reserve2
				+ ", reserve3=" + reserve3 + ", description=" + description
				+ "]";
	}
}
