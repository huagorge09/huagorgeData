package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("seatDto")
public class SeatDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String seatno;
    private String seatnm;

    public String getSeatno() {
		return seatno;
	}

	public void setSeatno(String seatno) {
		this.seatno = seatno;
	}

	public String getSeatnm() {
		return seatnm;
	}

	public void setSeatnm(String seatnm) {
		this.seatnm = seatnm;
	}

	@Override
	public String toString() {
		return "SeatDto [seatno=" + seatno + ", seatnm=" + seatnm + "]";
	}
	
}
