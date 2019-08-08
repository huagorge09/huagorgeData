package com.cmwa.ecc.business.entity.fundinfo;

import org.apache.ibatis.type.Alias;

@Alias("fundElementDto")
public class FundElementDto {
	
	private String fundid;				
	private String sortno;				//序号
	private String title;				//标题
	private String content;				//内容
	private String contenttype;			//内容类型    1：文本；2：图片网址
	private String typename;			//要素类型名称
	private String eletype;				//产品基本信息:110-小标题;120-大标题;130-图片;  投资项目信息:210-小标题;220-大标题;230-PC图片  240-WX图片; qa:310-小标题;320-大标题;330-图片
	private String status;				//状态(y-正常)
	private byte[] pic;					//图片存储
	
	private int period;					//产品期数
	private String eletypeText; 		
	public String getFundid() {
		return fundid;
	}
	public void setFundid(String fundid) {
		this.fundid = fundid;
	}
	public String getSortno() {
		return sortno;
	}
	public void setSortno(String sortno) {
		this.sortno = sortno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContenttype() {
		return contenttype;
	}
	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getEletype() {
		return eletype;
	}
	public void setEletype(String eletype) {
		this.eletype = eletype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "FundElementDto [fundid=" + fundid + ", sortno=" + sortno
				+ ", title=" + title + ", content=" + content
				+ ", contenttype=" + contenttype + ", typename=" + typename
				+ ", eletype=" + eletype + ", status=" + status + "]";
	}
	public byte[] getPic() {
		return pic;
	}
	public void setPic(byte[] pic) {
		this.pic = pic;
	}
	public String getEletypeText() {
		return eletypeText;
	}
	public void setEletypeText(String eletypeText) {
		this.eletypeText = eletypeText;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}


}
