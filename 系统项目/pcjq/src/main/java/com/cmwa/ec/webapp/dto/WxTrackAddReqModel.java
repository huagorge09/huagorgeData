/**
 * 
 */
package com.cmwa.ec.webapp.dto;

import java.util.Date;

public class WxTrackAddReqModel {
	
	private static final long serialVersionUID = 1L;

    private String openid;

    private String unionid;

    private String trackDate;

    private Long trackMillis;
    
    private String group;

    private String page;

    private String event;
    
    private String data;

    private String pageSource;
    
    private String userId;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPageSource() {
        return pageSource;
    }

    public void setPageSource(String pageSource) {
        this.pageSource = pageSource;
    }

    public WxTrackAddReqModel() {
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getTrackDate() {
        return trackDate;
    }

    public void setTrackDate(String trackDate) {
        this.trackDate = trackDate;
    }

    public Long getTrackMillis() {
        return trackMillis;
    }

    public void setTrackMillis(Long trackMillis) {
        this.trackMillis = trackMillis;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(String data) {
        this.data = data;
    }
    
    public WxTrackAddReqModel(String openid, String unionid, String trackDate,
			Long trackMillis, String group, String page, String event,
			String data, String pageSource, String userId) {
		super();
		this.openid = openid;
		this.unionid = unionid;
		this.trackDate = trackDate;
		this.trackMillis = trackMillis;
		this.group = group;
		this.page = page;
		this.event = event;
		this.data = data;
		this.pageSource = pageSource;
		this.userId = userId;
	}

    @Override
    public String toString() {
        return "WxTrackAddReqModel [openid=" + openid + ", unionid=" + unionid + ", trackDate=" + trackDate
                + ", trackMillis=" + trackMillis + ", group=" + group + ", page=" + page + ", event=" + event
                + ", data=" + data + ", pageSource=" + pageSource + ", userId=" + userId + "]";
    }


    
}
