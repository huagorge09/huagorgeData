package com.cmwa.ecc.business.entity.todolist;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;

@Alias("todoListVo")
public class TodoListVo extends BaseVo{
	/**
	 * id
	 */
	private String todoId;
	/**
	 * 拜访id
	 */
	private String visId;
	/**
	 * 接收人员
	 */
	private String todoReceiveId;
	/**
	 * 接收人员名称
	 */
	private String todoReceiveName;
	
	/**
	 * 待办内容
	 */
	private String todoContent;
	/**
	 * 截止日期
	 */
	private String todoEndDate;
	/**
	 * 是否完成
	 */
	private String isComplete;
	
	/**
	 * 机构 查询用
	 */
	private String instId;
	/**
	 * 机构全称 查询用
	 */
	private String instLName;
	
	public TodoListVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getInstLName() {
		return instLName;
	}

	public void setInstLName(String instLName) {
		this.instLName = instLName;
	}

	public String getVisId() {
		return visId;
	}

	public void setVisId(String visId) {
		this.visId = visId;
	}

	public String getTodoReceiveName() {
		return todoReceiveName;
	}

	public void setTodoReceiveName(String todoReceiveName) {
		this.todoReceiveName = todoReceiveName;
	}

	public String getTodoId() {
		return todoId;
	}

	public void setTodoId(String todoId) {
		this.todoId = todoId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getTodoReceiveId() {
		return todoReceiveId;
	}

	public void setTodoReceiveId(String todoReceiveId) {
		this.todoReceiveId = todoReceiveId;
	}

	public String getTodoContent() {
		return todoContent;
	}

	public void setTodoContent(String todoContent) {
		this.todoContent = todoContent;
	}

	public String getTodoEndDate() {
		return todoEndDate;
	}

	public void setTodoEndDate(String todoEndDate) {
		this.todoEndDate = todoEndDate;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}
	
	
}
