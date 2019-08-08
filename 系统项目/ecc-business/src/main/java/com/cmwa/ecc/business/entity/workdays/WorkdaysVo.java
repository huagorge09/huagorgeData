package com.cmwa.ecc.business.entity.workdays;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("workdaysVo")
public class WorkdaysVo  implements Serializable {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String workdayId;
	private String fundid;
    private String workdate;
    private String workdateFmt;
    private String workflag;
    private String workflagnm;
    private String operator;
    private String status;
    private String resultCode; //返回代码
    private String resultMessage; //返回信息
    
    public String getWorkdayId() {
		return workdayId;
	}

	public void setWorkdayId(String workdayId) {
		this.workdayId = workdayId;
	}

	public String getFundid() {
        return fundid;
    }

    public void setFundid(String fundid) {
        this.fundid = fundid;
    }

    public String getWorkdate() {
        return workdate;
    }

    public void setWorkdate(String workdate) {
        this.workdate = workdate;
    }

    public String getWorkflag() {
        return workflag;
    }

    public void setWorkflag(String workflag) {
        this.workflag = workflag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkflagnm() {
		return workflagnm;
	}

	public void setWorkflagnm(String workflagnm) {
		this.workflagnm = workflagnm;
	}

	public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getWorkdateFmt() {
		return workdateFmt;
	}

	public void setWorkdateFmt(String workdateFmt) {
		this.workdateFmt = workdateFmt;
	}

	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WorkdaysVo)) {
            return false;
        }
        WorkdaysVo that = (WorkdaysVo) obj;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid))) {
            return false;
        }
        if (!(that.workdate == null ? this.workdate == null :
              that.workdate.equals(this.workdate))) {
            return false;
        }
        if (!(that.workflag == null ? this.workflag == null :
              that.workflag.equals(this.workflag))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.workdate.hashCode();
        result = 37 * result + this.workflag.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(96);
        returnStringBuffer.append("[");
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("workdate:").append(workdate);
        returnStringBuffer.append("workflag:").append(workflag);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}

