package com.cmwa.ec.weixin.dto;


/**
 * 用户兑奖信息dto类
 * @author ex-hezk
 *
 */
/**
 * @author ex-hezk
 * 
 */
public class ActivityUserInfoDto extends ActivityBaseDto {

    /**
     * 用户兑奖信息id
     * 
     */
    private String userinfoId;
    /**
     * 用户openid
     */
    private String openid;
    /**
     * 所属活动id
     */
    private String activityId;
    /**
     * 用户名称
     */
    private String userFullName;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * added at 2019/01/29 活动关联的外部系统用户id
     */
    private String relationId;

    private String itemKey;

    private String itemValue;

    private String createdTime;

    /**
     * @return the relationId
     */
    public String getRelationId() {
        return relationId;
    }

    /**
     * @param relationId
     *            the relationId to set
     */
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    /**
     * @return
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * @param activityId
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /**
     * @return
     */
    public String getUserFullName() {
        return userFullName;
    }

    /**
     * @param userFullName
     */
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    /**
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return
     */
    public String getUserinfoId() {
        return userinfoId;
    }

    /**
     * @param userinfoId
     */
    public void setUserinfoId(String userinfoId) {
        this.userinfoId = userinfoId;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "ActivityUserInfoDto [userinfoId=" + userinfoId + ", openid=" + openid + ", activityId=" + activityId + ", userFullName=" + userFullName + ", address=" + address
                + ", mobile=" + mobile + ", relationId=" + relationId + ", itemKey=" + itemKey + ", itemValue=" + itemValue + ", createdTime=" + createdTime + "]";
    }
}
