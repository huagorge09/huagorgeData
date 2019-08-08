package com.cmwa.ecc.business.entity.visit;


import java.util.List;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;
import com.cmwa.ecc.business.entity.todolist.TodoListVo;
import com.cmwa.ecc.business.utils.Page;

@Alias("visitHistoryVo")
public class VisitHistoryVo extends BaseVo {
	private String visId;
	private String instId;
	private String visEmpId;
	private String visEmpName;
	private String visMeetId;//主送人
	private String visMeetName;
	private String visBeginDate;//开始日期
	private String visEndDate;//结束日期
	private String visContacts;//联系人Id
	private String visContactsName;//联系人Name
	private String visAddress;//拜访地点
	private String visMeetExplain;//会谈背景说明 
	private String visMeetingFeedback;//会谈中对方反馈 
	private String visCopyToPeople;//抄送人Id
	private String visCopyToPeopleName;//抄送人Name
	private String visConTel;//抄送人联系方式
	private List<TodoListVo> todoListVos;//获取所有的待办人信息
	
	/**
	 * 机构全称 查询用
	 */
	private String instLName;
	/**
	 * 拜访时间 (开始 - 结束)
	 */
	private String visDate;
	
	/**
	 * 抄送人 
	 */
	private String visCopyId;
	
	public String getVisCopyId() {
		return visCopyId;
	}

	public void setVisCopyId(String visCopyId) {
		this.visCopyId = visCopyId;
	}

	public VisitHistoryVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getVisMeetId() {
		return visMeetId;
	}

	public void setVisMeetId(String visMeetId) {
		this.visMeetId = visMeetId;
	}

	public String getVisMeetName() {
		return visMeetName;
	}

	public void setVisMeetName(String visMeetName) {
		this.visMeetName = visMeetName;
	}

	public String getVisEmpId() {
		return visEmpId;
	}


	public void setVisEmpId(String visEmpId) {
		this.visEmpId = visEmpId;
	}


	public String getVisEmpName() {
		return visEmpName;
	}

	public void setVisEmpName(String visEmpName) {
		this.visEmpName = visEmpName;
	}

	public String getVisMeetExplain() {
		return visMeetExplain;
	}

	public void setVisMeetExplain(String visMeetExplain) {
		this.visMeetExplain = visMeetExplain;
	}

	public String getVisMeetingFeedback() {
		return visMeetingFeedback;
	}

	public void setVisMeetingFeedback(String visMeetingFeedback) {
		this.visMeetingFeedback = visMeetingFeedback;
	}

	public String getVisDate() {
		return visDate;
	}

	public void setVisDate(String visDate) {
		this.visDate = visDate;
	}

	public String getInstLName() {
		return instLName;
	}

	public void setInstLName(String instLName) {
		this.instLName = instLName;
	}

	public String getVisContactsName() {
		return visContactsName;
	}

	public void setVisContactsName(String visContactsName) {
		this.visContactsName = visContactsName;
	}

	public String getVisId() {
		return visId;
	}
	public void setVisId(String visId) {
		this.visId = visId;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getVisBeginDate() {
		return visBeginDate;
	}
	public void setVisBeginDate(String visBeginDate) {
		this.visBeginDate = visBeginDate;
	}
	public String getVisEndDate() {
		return visEndDate;
	}
	public void setVisEndDate(String visEndDate) {
		this.visEndDate = visEndDate;
	}
	public String getVisContacts() {
		return visContacts;
	}
	public void setVisContacts(String visContacts) {
		this.visContacts = visContacts;
	}
	public String getVisAddress() {
		return visAddress;
	}
	public void setVisAddress(String visAddress) {
		this.visAddress = visAddress;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getVisCopyToPeople() {
		return visCopyToPeople;
	}

	public void setVisCopyToPeople(String visCopyToPeople) {
		this.visCopyToPeople = visCopyToPeople;
	}

	public String getVisCopyToPeopleName() {
		return visCopyToPeopleName;
	}

	public void setVisCopyToPeopleName(String visCopyToPeopleName) {
		this.visCopyToPeopleName = visCopyToPeopleName;
	}

	public String getVisConTel() {
		return visConTel;
	}

	public void setVisConTel(String visConTel) {
		this.visConTel = visConTel;
	}

	public List<TodoListVo> getTodoListVos() {
		return todoListVos;
	}

	public void setTodoListVos(List<TodoListVo> todoListVos) {
		this.todoListVos = todoListVos;
	}

}
