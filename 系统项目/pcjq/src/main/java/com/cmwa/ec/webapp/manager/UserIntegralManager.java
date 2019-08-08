package com.cmwa.ec.webapp.manager;

import com.cmwa.ec.webapp.model.integral.IntegralDetailVO;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户积分业务处理类
 *
 * @author ex-chent@cmfchina.com
 */
public interface UserIntegralManager {

    /**
     * 获取用户积分和手机号
     *
     * @param request
     * @return
     */
    JSONObject getUserIntegralInfo(HttpServletRequest request, String cmfUserId);

    /**
     * 积分流水
     *
     * @param request
     * @return
     */
    List<IntegralDetailVO> listUserIntegralDetail(String cmfUserId);

    /**
     * 获取可用积分兑换的商品列表
     *
     * @return
     */
    List listIntegralGoods();

    /**
     * 判断是否新用户并且该用户未绑定邀请人
     *
     * @return 0:是老用户或已绑定邀请人的新用户, 1:是未绑定邀请人的新用户
     */
    boolean isNewUserNotInvited(String cmfUserId);

    /**
     * 查询积分开关, 控制是否启用积分功能
     *
     * @return <tt>true</tt> 启用
     */
    boolean enableIntegral();

    /**
     *
     * @return
     */
    boolean bindingReferrer(String cmfUserId, String invitationCode);

    /**
     *
     * @return
     */
    boolean bindingReferrerByPhoneNumber(String cmfUserId, String phoneNumber);


    /**
     * 推荐顾问
     * <P>进行预绑定推荐人</P>
     *
     * @param customerName
     * @param customerMobile
     * @param cmfUserId
     * @return
     */
    int shareConsultant(String customerName, String customerMobile, String cmfUserId, String referrer,
                            String consultant, String consultantMobile);

    /**
     * 物品兑换
     *
     * @param goodsId
     * @param cmfUserId
     * @return
     */
    boolean goodsExchange(String cmfUserId, int goodsId, long integralBeforeChange, long integralChange);

    /**
     * 积分规则配置展示的热门产品
     *
     * @return
     */
    List listExhibitionFund();

}
