package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
*
* <p>Title: 客户通讯信息DTO</p>
*
* <p>Description: 为InvestorInfoDto的关联类，一个InvestorInfoDto关联一个AddressDto</p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: Legion Technology</p>
*
* @author xuhw
* @version 1.0
* @Modify history:liwx modified 20121231
*/
@Alias("addressDto")
public class AddressDto implements Serializable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String name; //姓名
   private String province; //省份
   private String city; //城市（这里是指名称）
   private String addr; //用户详细地址
   private String postcode; //邮政编码
   private String mobile; //手机号码
   private String tel; //固定电话
   private String fax; //传真号码
   private String email; //电子邮件
   private String billsendtype; //账单寄送方式
   private String errcode; //业务返回代码
   private String errmsg; //业务返回信息

   public AddressDto() {
   }

   /**
    * 设置联系人
    * @param name String
    */
   public void setName(String name) {
       this.name = name;
   }

   /**
    * 设置省份
    * @param province String
    */
   public void setProvince(String province) {
       this.province = province;
   }

   /**
    * 设置城市
    * @param city String
    */
   public void setCity(String city) {
       this.city = city;
   }

   /**
    * 设置地址
    * @param addr String
    */
   public void setAddr(String addr) {
       this.addr = addr;
   }

   /**
    * 设置邮政编码
    * @param postcode String
    */
   public void setPostcode(String postcode) {
       this.postcode = postcode;
   }

   /**
    * 设置手机
    * @param mobile String
    */
   public void setMobile(String mobile) {
       this.mobile = mobile;
   }

   /**
    * 设置电话
    * @param tel String
    */
   public void setTel(String tel) {
       this.tel = tel;
   }

   /**
    * 设置传真
    * @param fax String
    */
   public void setFax(String fax) {
       this.fax = fax;
   }

   /**
    * 设置电子邮件
    * @param email String
    */
   public void setEmail(String email) {
       this.email = email;
   }
   
   public void setBillsendtype(String billsendtype) {
   	this.billsendtype = billsendtype;
   }
   /**
    * 设置业务返回代码
    * @param errcode String
    */
   public void setErrcode(String errcode) {
       this.errcode = errcode;
   }

   /**
    * 设置业务返回信息
    * @param errmsg String
    */
   public void setErrmsg(String errmsg) {
       this.errmsg = errmsg;
   }

   public String getName() {
       return name;
   }

   public String getProvince() {
       return province;
   }

   public String getCity() {
       return city;
   }

   public String getAddr() {
       return addr;
   }

   public String getPostcode() {
       return postcode;
   }

   public String getMobile() {
       return mobile;
   }

   public String getTel() {
       return tel;
   }

   public String getFax() {
       return fax;
   }

   public String getEmail() {
       return email;
   }
   
   public String getBillsendtype() {
       return billsendtype;
   }
   
   public String getErrcode() {
       return errcode;
   }

   public String getErrmsg() {
       return errmsg;
   }

   public String toString() {
       return "";
   }
}
