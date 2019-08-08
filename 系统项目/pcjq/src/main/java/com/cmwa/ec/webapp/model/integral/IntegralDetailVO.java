package com.cmwa.ec.webapp.model.integral;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 积分记录View Object
 *
 * @author ex-chent@cmfchina.com
 */
public class IntegralDetailVO implements Comparable<IntegralDetailVO> {

    /** 内容 */
    private String content;
    /** 展示状态 */
    private Integer displayState;
    /** 此条流水用户的操作类型 */
    private Integer operationid;
    /** 此条流水产生的积分变化 */
    private Long integralChange;
    /** 创建时间 */
    private String createTime;


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param   o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    @Override
    public int compareTo(IntegralDetailVO o) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date thisTime = dateFormat.parse(o.createTime);
            Date otherTime = dateFormat.parse(this.createTime);
            return thisTime.compareTo(otherTime);
        } catch (ParseException e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "IntegralDetailVO{" +
            "content='" + content + '\'' +
            ", displayState=" + displayState +
            ", operationid=" + operationid +
            ", integralChange=" + integralChange +
            ", createTime='" + createTime + '\'' +
            '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDisplayState() {
        return displayState;
    }

    public void setDisplayState(Integer displayState) {
        this.displayState = displayState;
    }

    public Integer getOperationid() {
        return operationid;
    }

    public void setOperationid(Integer operationid) {
        this.operationid = operationid;
    }

    public Long getIntegralChange() {
        return integralChange;
    }

    public void setIntegralChange(Long integralChange) {
        this.integralChange = integralChange;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
