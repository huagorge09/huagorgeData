package com.cmwa.ecc.business.entity.user;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("user")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userid; //操作员代码
    private String usernm; //用户名称
    private String password; //密码
    private List roleList; //用户所有角色
    private String netpoint; //网点
    private String machineAddr; //网卡地址  //add by xgb
    private String pwdflg; //是否已经设置密码
    private String status; //状态
    private String custid; //用户ID
    private String winad_yorn; //是否使用域验证:Y 域验证；N 数据库验证
    
    //2010-07-28 wdw add
    private String loginId;		//用户ID，把原来的custid切换到loginId
    private String operatorty;//用户类型:A 域用户；N 普通用户
    private String errCode; //返回码
    private String errMsg;	//返回信息
    
    public User(){
    	
    }
    /**
     * 构造函数
     * @param userid String 用户ID
     */
    public User(String userid) {
        this.userid = userid;
    }

    /**
     * 设置用户名称
     * @param usernm String
     */
    public void setUsernm(String usernm) {
        this.usernm = usernm;
    }

    /**
     * 设置密码
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }

   

    /**
     * 设置网点
     * @param netpoint String
     */
    public void setNetpoint(String netpoint) {
        this.netpoint = netpoint;
    }

    /**
     * 设置是否已经设置密码标志Y，N
     * @param pwdflg String
     */
    public void setPwdflg(String pwdflg) {
        this.pwdflg = pwdflg;
    }

    /**
     * 设置状态
     * @param status String
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsernm() {
        return usernm;
    }

    public String getPassword() {
        return password;
    }

    public List getRoles() {
        return roleList;
    }

    public String getNetpoint() {
        return netpoint;
    }

    public String getPwdflg() {
        return pwdflg;
    }

    public String getStatus() {
        return status;
    }

    /**
     * 获取网卡地址
     * @return String
     */
    public String getMachineAddr() {
        return machineAddr;
    }

    /**
     * 设置网卡地址
     * @param machineAddr
     */
    public void setMachineAddr(String machineAddr) {
        this.machineAddr = machineAddr;
    }

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getWinad_yorn() {
		return winad_yorn;
	}

	public void setWinad_yorn(String winad_yorn) {
		this.winad_yorn = winad_yorn;
	}

	public String getOperatorty() {
		return operatorty;
	}

	public void setOperatorty(String operatorty) {
		this.operatorty = operatorty;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public List getRoleList() {
		return roleList;
	}
	public void setRoleList(List roleList) {
		this.roleList = roleList;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + ", usernm=" + usernm + ", password="
				+ password + ", roleList=" + roleList + ", netpoint="
				+ netpoint + ", machineAddr=" + machineAddr + ", pwdflg="
				+ pwdflg + ", status=" + status + ", custid=" + custid
				+ ", winad_yorn=" + winad_yorn + ", loginId=" + loginId
				+ ", operatorty=" + operatorty + ", errCode=" + errCode
				+ ", errMsg=" + errMsg + "]";
	}
    
}

