package com.cmwa.ec.weixin.manager.activity.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.MagicMap;
import com.cmwa.ec.message.facade.dto.BusinessTypeDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.model.MsgResult;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.controller.CommonController;
import com.cmwa.ec.weixin.dao.ActivityDao;
import com.cmwa.ec.weixin.dao.UserInfoDao;
import com.cmwa.ec.weixin.dao.UserInfoexDao;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dao.activity.ActivityAwardStoreDao;
import com.cmwa.ec.weixin.dao.activity.ActivityUserInfoDao;
import com.cmwa.ec.weixin.dao.activity.AwardInfoDao;
import com.cmwa.ec.weixin.dao.activity.CustomerAwardDao;
import com.cmwa.ec.weixin.dao.activity.ShareInfoDao;
import com.cmwa.ec.weixin.dto.ActivityAwardStoreDto;
import com.cmwa.ec.weixin.dto.ActivityBaseDto;
import com.cmwa.ec.weixin.dto.ActivityUserInfoDto;
import com.cmwa.ec.weixin.dto.AwardInfoDto;
import com.cmwa.ec.weixin.dto.CustomerAwardDto;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.QueryUserAwardDto;
import com.cmwa.ec.weixin.dto.ShareConfigDto;
import com.cmwa.ec.weixin.dto.ShareInfoDto;
import com.cmwa.ec.weixin.dto.SystemNoticeDto;
import com.cmwa.ec.weixin.dto.UserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.dto.UserSignInDto;
import com.cmwa.ec.weixin.manager.activity.CustomerAwardManager;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.manager.business.MailManager;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.LotteryUtil;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.UUIDKeyGenerator;
import com.cmwa.ec.weixin.util.WeixinUtil;

public class CustomerAwardManagerImpl implements CustomerAwardManager {

    @Autowired
    private CustomerAwardDao customerAwardDao;

    @Autowired
    private AwardInfoDao awardInfoDao;

    @Autowired
    private QueryServiceClient queryServiceClient;

    @Autowired
    private ActivityUserInfoDao actUserInfoDao;

    @Autowired
    private ShareInfoDao shareInfoDao;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private ActParameterDao actParameterDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserInfoexDao userInfoExDao;

    @Autowired
    private ActivityAwardStoreDao activityAwardStoreDao;

    @Autowired
    private MessageServiceClient messageServiceClient;

    @Autowired
    private MailManager mailManager;

    private static Logger logger = LoggerFactory.getLogger(CustomerAwardManagerImpl.class.getName());

    /**
     * 前几次碎片特殊获取规则参数
     */
    private final static int TIMES = 6;

    /**
     * 铜箱子配置参数
     */
    private final static String TONG = "TONGBOX";

    /**
     * 查询paramer表参数
     */
    private final static String PMST = "SYSTEM";

    /**
     * 查询paramer表参数
     */
    private final static String YMH_AWARD = "GDBMW_AWARD";

    /**
     * 查询paramer表活动期间获奖次数
     */
    private final static String FDACTLUCKCOUNT = "FDACTLUCKCOUNT";

    /**
     * 查询paramer表活动每日中奖机会参数
     */
    private final static String FDACTWINCOUNT = "FDACTWINCOUNT";

    /**
     * 条数
     */
    private final static int PAGE_SIZE = 10;

    /**
     * 活动奖品数量开关前缀
     */
    private final static String AWARD_NUMBER = "AWARD_NUMBER@";

    /**
     * 活动奖品概率开关前缀
     */
    private final static String AWARD_CHANCE = "AWARD_CHANCE@";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public JSONObject obtainFragmentLogic(String json) {
        logger.info("调用用户获取碎片逻辑方法>>>>ObtainFragmentLogic>>>>start>>>>入参：" + json);
        JSONObject jsonObject = new JSONObject();
        boolean ret = false;
        JSONObject param = JSONObject.fromObject(json);
        try {
            if (param.has("openid") && param.has("activityId") && param.has("awardFrom")) {
                String openId = param.getString("openid");
                String activityId = param.getString("activityId");
                String awardFrom = param.getString("awardFrom");
                if (StringUtils.isEmpty(openId) || "null".equals(openId) || StringUtils.isEmpty(activityId) || StringUtils.isEmpty(awardFrom)) {
                    jsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                    jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("openId", openId);
                    map.put("activityId", activityId);
                    map.put("awardType", "2");
                    map.put("getState", "0");
                    List<CustomerAwardDto> customerAwardList = customerAwardDao.queryCustomerAwardList(map);
                    List<AwardInfoDto> awardInfoList = null;
                    if (customerAwardList.size() < TIMES) {
                        map.put("awardsize", TIMES);
                        awardInfoList = awardInfoDao.queryAwardList(map);
                        retFragmentSet(awardInfoList, customerAwardList);
                        int sum = 0;
                        if (customerAwardList.size() != 5) {
                            for (CustomerAwardDto dto : customerAwardList) {
                                String awardId = awardInfoDao.getAwardDto(dto.getAwardId()).getAwardPid();
                                AwardInfoDto award = awardInfoDao.getAwardDto(awardId);
                                if (award.getAwardName().equals(TONG)) {
                                    sum++;
                                }
                            }
                            if (sum >= TIMES / 2) {
                                for (int i = 0; i < awardInfoList.size(); i++) {
                                    String awardId = awardInfoDao.getAwardDto(awardInfoList.get(i).getAwardId()).getAwardPid();
                                    AwardInfoDto award = awardInfoDao.getAwardDto(awardId);
                                    if (TONG.equals(award.getAwardName())) {
                                        awardInfoList.remove(i);
                                    }
                                }
                            }
                        }
                    } else {
                        awardInfoList = awardInfoDao.queryAwardList(map);
                        retFragmentSet(awardInfoList, customerAwardList);
                    }
                    if (awardInfoList.size() == 0) {
                        awardInfoList = awardInfoDao.queryAwardList(map);
                    }
                    int x = (int) (Math.random() * awardInfoList.size());
                    ret = insertArawd(activityId, awardFrom, openId, awardInfoList.get(x).getAwardId()) > 0;
                    if (ret) {
                        jsonObject.put("returnCode", "0");
                        jsonObject.put("returnMsg", "SUCCESS");
                    }
                }
            } else {
                jsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("调用用户获取碎片逻辑方法>>>>发生异常,异常信息:", e);
            jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用用户获取碎片逻辑方法>>>>ObtainFragmentLogic>>>>end>>>>出参：" + jsonObject.toString());
        return jsonObject;
    }

    @Override
    @Transactional(value = "waecUserTransactionManager")
    public JSONObject luckDrawLogic(String json) {
        logger.info("调用用户开盒子抽奖逻辑方法>>>>LuckDrawLogic>>>>start>>>>入参：" + json);
        JSONObject jsonObject = new JSONObject();
        JSONObject param = JSONObject.fromObject(json);
        try {
            if (param.has("openid") && param.has("activityId") && param.has("boxState")) {
                String openId = param.getString("openid");
                String activityId = param.getString("activityId");
                String boxState = param.getString("boxState");
                if (StringUtils.isEmpty(openId) || "null".equals(openId) || StringUtils.isEmpty(activityId)) {
                    jsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                    jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
                } else {
                    // 判断用户是否关注
                    if (!getSubscribeState(openId)) {
                        jsonObject.put("returnCode", "1");
                        jsonObject.put("returnMsg", "用户未关注");
                        return jsonObject;
                    }
                    JSONObject ret = consume(activityId, openId, boxState);
                    if (ret.has("errorCode")) {
                        jsonObject.put("returnCode", "2222222");
                        jsonObject.put("returnMsg", "用户不符合开宝箱条件");
                        return jsonObject;
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("openId", openId);
                    map.put("activityId", activityId);
                    map.put("awardType", "0");
                    // 查询该用户已获得代金券次数
                    List<CustomerAwardDto> luckList = customerAwardDao.queryCustomerAwardList(map);
                    map.put("awardType", "-1");
                    map.put("sameDay", 10);
                    // 查询该用户当日开宝箱次数
                    List<CustomerAwardDto> sameDayList = customerAwardDao.queryCustomerAwardList(map);
                    map.put("awardType", "0");
                    map.put("boxState", boxState);
                    // 查询宝箱对应的奖品列表
                    List<AwardInfoDto> awardInfoList = awardInfoDao.queryAwardList(map);
                    ParameterDto parameter = new ParameterDto();
                    parameter.setPmst(PMST);
                    parameter.setPmky(FDACTLUCKCOUNT);
                    parameter.setPmco("num");
                    // 查询parameter参数开关值(活动期间中奖次数限制)
                    ParameterDto luckCount = actParameterDao.getParameterDto(parameter);
                    parameter.setPmky(FDACTWINCOUNT);
                    // 查询parameter参数开关值(单日抽奖次数限制)
                    ParameterDto twinCount = actParameterDao.getParameterDto(parameter);
                    if (luckList.size() >= Integer.parseInt(luckCount.getPmnm()) || sameDayList.size() >= Integer.parseInt(twinCount.getPmnm())) {
                        for (AwardInfoDto dto : awardInfoList) {
                            insertArawd(activityId, "2", openId, dto.getAwardPid());
                            break;
                        }
                        jsonObject.put("state", "0");
                        jsonObject.put("num", openBoxGeFragments(openId, activityId, boxState).size());
                        jsonObject.put("returnCode", "0");
                        jsonObject.put("returnMsg", "SUCCESS");
                        return jsonObject;
                    }
                    AwardInfoDto dto = lottery(awardInfoList, boxState);
                    if (null != dto) {
                        if ("0".equals(dto.getAwardPrice().toString())) {
                            for (AwardInfoDto d : awardInfoList) {
                                insertArawd(activityId, "2", openId, d.getAwardPid());
                                break;
                            }
                            jsonObject.put("state", "0");
                            jsonObject.put("num", openBoxGeFragments(openId, activityId, boxState).size());
                            jsonObject.put("returnCode", "0");
                            jsonObject.put("returnMsg", "SUCCESS");
                            return jsonObject;
                        } else {
                            String awardNumber = "AWARD_NUMBER";
                            if (null != dto.getAwardSort()) {
                                awardNumber = awardNumber + "@" + dto.getAwardSort();
                            }
                            parameter.setPmky(boxState);
                            parameter.setPmco(awardNumber);
                            ParameterDto object = actParameterDao.getParameterDto(parameter);
                            if (null != object) {
                                if (Integer.parseInt(object.getPmnm()) > 0) {
                                    // 修改奖品库存数量
                                    int returnState = actParameterDao.updateParameter(parameter);
                                    if (returnState > 0) {
                                        // 保存用户中奖信息
                                        insertArawd(dto.getActivityId(), "2", openId, dto.getAwardId());
                                        jsonObject.put("state", "1");
                                        jsonObject.put("data", dto);

                                    } else {
                                        for (AwardInfoDto d : awardInfoList) {
                                            insertArawd(activityId, "2", openId, d.getAwardPid());
                                            break;
                                        }
                                        jsonObject.put("state", "0");
                                        jsonObject.put("num", openBoxGeFragments(openId, activityId, boxState).size());
                                    }
                                } else {
                                    for (AwardInfoDto d : awardInfoList) {
                                        insertArawd(activityId, "2", openId, d.getAwardPid());
                                        break;
                                    }
                                    jsonObject.put("state", "0");
                                    jsonObject.put("num", openBoxGeFragments(openId, activityId, boxState).size());
                                }
                                jsonObject.put("returnCode", "0");
                                jsonObject.put("returnMsg", "SUCCESS");
                            } else {
                                jsonObject.put("returnCode", "4");
                                jsonObject.put("returnMsg", "查询奖品参数值有异常");
                            }
                        }
                    } else {
                        jsonObject.put("returnCode", "3");
                        jsonObject.put("returnMsg", "抽奖异常");
                    }
                }
            } else {
                jsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("调用用户开盒子抽奖逻辑方法>>>>LuckDrawLogic>>>>>发生异常,异常信息:", e);
            jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用用户开盒子抽奖逻辑方法>>>>LuckDrawLogic>>>>end>>>>出参：" + jsonObject);
        return jsonObject;
    }

    @Override
    public JSONObject shardListLogic(String json) {
        logger.info("查询用户碎片列表方法>>>>shardListLogic>>>>start>>>>入参：" + json);
        JSONObject jsonObject = new JSONObject();
        JSONObject param = JSONObject.fromObject(json);
        try {
            if (param.has("openid") && param.has("activityId")) {
                String openId = param.getString("openid");
                String activityId = param.getString("activityId");
                if (StringUtils.isEmpty(openId) || "null".equals(openId) || StringUtils.isEmpty(activityId)) {
                    jsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                    jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("openId", openId);
                    map.put("activityId", activityId);
                    map.put("awardType", "2");
                    map.put("getState", "0");
                    List<CustomerAwardDto> list = customerAwardDao.queryCustomerAwardList(map);
                    List<AwardInfoDto> awardInfoList = awardInfoDao.queryAwardList(map);
                    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("total", list.size());
                    for (int i = 0; i < awardInfoList.size(); i++) {
                        AwardInfoDto awardInfo = awardInfoList.get(i);
                        Map<String, Object> m = new HashMap<String, Object>();
                        int number = 0;
                        for (int j = 0; j < list.size(); j++) {
                            CustomerAwardDto dto = list.get(j);
                            if (dto.getAwardId().equals(awardInfo.getAwardId())) {
                                number++;
                                list.remove(j);
                                j--;
                            }
                        }
                        m.put("sort", awardInfo.getAwardName().substring(awardInfo.getAwardName().length() - 1, awardInfo.getAwardName().length()));
                        m.put("num", number);
                        if (-1 != awardInfo.getAwardName().indexOf("A")) {
                            m.put("type", "0");
                        } else if (-1 != awardInfo.getAwardName().indexOf("B")) {
                            m.put("type", "1");
                        } else if (-1 != awardInfo.getAwardName().indexOf("C")) {
                            m.put("type", "2");
                        }
                        dataList.add(m);
                    }
                    dataJson.put("list", dataList);
                    // 查询用户是否开过箱子
                    map = new HashMap<String, Object>();
                    map.put("openId", openId);
                    map.put("activityId", activityId);
                    map.put("awardFrom", "2");
                    map.put("awardType", -1);
                    list = customerAwardDao.queryCustomerAwardList(map);
                    String isFristOpen = "false";
                    if (list.size() > 0) {
                        isFristOpen = "true";
                    }
                    dataJson.put("isFristOpen", isFristOpen);
                    jsonObject.put("data", dataJson);
                    jsonObject.put("returnCode", "0");
                    jsonObject.put("returnMsg", "SUCCESS");
                }
            } else {
                jsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("查询用户碎片列表方法>>>>shardListLogic>>>>>发生异常,异常信息:", e);
            jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("查询用户碎片列表方法>>>>shardListLogic>>>>end>>>>入参：" + jsonObject.toString());
        return jsonObject;
    }

    /**
     * 开宝箱获取碎片逻辑处理方法
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @Transactional(value = "transactionManager")
    private List<AwardInfoDto> openBoxGeFragments(String openid, String activityId, String boxState) throws Exception {
        List<AwardInfoDto> list = new ArrayList<AwardInfoDto>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", activityId);
        map.put("awardType", "2");
        List<AwardInfoDto> awardInfoList = awardInfoDao.queryAwardList(map);
        ParameterDto parameter = new ParameterDto();
        parameter.setPmst(PMST);
        parameter.setPmky(boxState);
        parameter.setPmco("FDACTCB_COUNT");
        ParameterDto fdactcb = actParameterDao.getParameterDto(parameter);
        int number = Integer.parseInt(fdactcb.getPmnm());
        for (int i = 0; i < number; i++) {
            int x = (int) (Math.random() * awardInfoList.size());
            insertArawd(activityId, "2", openid, awardInfoList.get(x).getAwardId());
            list.add(awardInfoList.get(x));
            awardInfoList.remove(x);
        }
        return list;

    }

    /**
     * 过滤用户已经获取的碎片信息
     * 
     * @param param1
     * @param param2
     */
    private static void retFragmentSet(List<AwardInfoDto> param1, List<CustomerAwardDto> param2) {
        for (int i = 0; i < param2.size(); i++) {
            CustomerAwardDto cAward = param2.get(i);
            for (int j = 0; j < param1.size(); j++) {
                if (param1.get(j).getAwardId().equals(cAward.getAwardId()) || param1.get(j).getAwardId() == cAward.getAwardId()) {
                    param1.remove(j);
                    break;
                }
            }
        }
    }

    private AwardInfoDto lottery(List<AwardInfoDto> awardInfoList, String boxState) throws Exception {
        List<Map<String, Object>> list = queryParametertoMap(PMST, boxState);
        if (null != awardInfoList && awardInfoList.size() != 0) {
            if (awardInfoList.size() > 0) {
                List<Double> orignalRates = new ArrayList<Double>(awardInfoList.size());
                logger.info(">>>>>>>" + this.getClass().getName() + ">>>Lottery()>>>构建各个奖品元素概率>>>start>>>入参 giftList：" + JSONArray.fromObject(awardInfoList).toString());
                for (int i = 0; i < awardInfoList.size(); i++) {
                    AwardInfoDto dto = awardInfoList.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        Map<String, Object> map = list.get(j);
                        if (map.get("type").toString().equals(dto.getRemark())) {
                            if (list.size() > 1) {
                                dto.setProbability(Double.valueOf(map.get("AWARD_CHANCE" + "@" + i).toString()));
                                dto.setFactnum(Integer.valueOf(map.get("AWARD_NUMBER" + "@" + i).toString()));
                                break;
                            } else {
                                dto.setProbability(Double.valueOf(map.get("AWARD_CHANCE").toString()));
                                dto.setFactnum(Integer.valueOf(map.get("AWARD_NUMBER").toString()));
                                break;
                            }
                        }
                    }
                    // 将大于0的奖品保存到概率列表中
                    if (dto.getFactnum().intValue() > 0) {
                        Double probability = Double.parseDouble(dto.getProbability().toString());
                        if (probability < 0) {
                            probability = 0d;
                        }
                        orignalRates.add(probability);
                    }
                }
                logger.info(">>>>>>>" + this.getClass().getName() + ">>>Lottery()>>>构建各个奖品元素概率 >>>end>>>出参 orignalRates：" + JSONArray.fromObject(orignalRates).toString());
                logger.info(">>>>>>>" + this.getClass().getName() + ">>>Lottery()>>>执行随机抽取奖品元素>>>start>>>入参 orignalRates：" + JSONArray.fromObject(orignalRates).toString());
                AwardInfoDto dto = new AwardInfoDto();
                if (orignalRates.isEmpty() || null == orignalRates) {
                    dto.setAwardPrice(0);
                    return dto;
                }
                int orignalIndex = LotteryUtil.lottery(orignalRates);
                if (orignalRates.size() == orignalIndex || orignalIndex == -1) {
                    dto.setAwardPrice(0);
                } else {
                    dto = awardInfoList.get(orignalIndex);
                    if (list.size() > 1) {
                        dto.setAwardSort(orignalIndex);
                    } else {
                        dto.setAwardSort(null);
                    }
                }
                return dto;
            }
        }
        return null;
    }

    /**
     * 开箱子消耗碎片逻辑
     * 
     * @param activityId
     * @param openId
     * @param boxState
     */
    @Transactional(value = "transactionManager")
    private JSONObject consume(String activityId, String openId, String boxState) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", openId);
        map.put("activityId", activityId);
        map.put("awardType", "2");
        map.put("getState", "0");
        map.put("boxState", boxState);
        List<CustomerAwardDto> customerAwardList = customerAwardDao.queryCustomerAwardList(map);
        for (int i = 0; i < customerAwardList.size(); i++) {
            CustomerAwardDto dto = customerAwardList.get(i);
            json.put(dto.getAwardId(), dto.getCusAwardId());
        }
        if (json.size() >= 4) {
            Iterator keys = json.keys();
            while (keys.hasNext()) {
                customerAwardDao.updateCustomerAward(json.getString(keys.next().toString()), "1");
            }
            json.put("returnCode", "0");
        } else {
            json.put("errorCode", "1");
        }
        return json;
    }

    private int insertArawd(String activityId, String awardFrom, String openid, String awardId) {
        return insertArawd(activityId, awardFrom, openid, awardId, null, null, null);
    }

    @Transactional(value = "transactionManager")
    private int insertArawd(String activityId, String awardFrom, String openid, String awardId, String storeId, String telePhone, String name) {
        CustomerAwardDto customerAwardDto = new CustomerAwardDto();
        UUIDKeyGenerator uuidGen = new UUIDKeyGenerator();
        customerAwardDto.setCusAwardId(uuidGen.generateUUIDKey());
        customerAwardDto.setActivityId(activityId);
        customerAwardDto.setAwardFrom(awardFrom);
        customerAwardDto.setOpenId(openid);
        customerAwardDto.setAwardId(awardId);
        customerAwardDto.setGetState("0");
        customerAwardDto.setStoreId(storeId);
        customerAwardDto.setUserName(name);
        customerAwardDto.setTelePhone(telePhone);
        return customerAwardDao.createCustomerAward(customerAwardDto);
    }

    public List<Map<String, Object>> queryParametertoMap(String pmst, String pmky) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        ParameterDto parameter = new ParameterDto();
        parameter.setPmst(pmst);
        parameter.setPmky(pmky);
        List<ParameterDto> list = actParameterDao.getParameter(parameter);
        for (int i = 0; i < list.size(); i++) {
            ParameterDto d = list.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(d.getPmv1())) {
                map.put(d.getPmco(), d.getPmnm());
                map.put("type", d.getPmv1());
                for (int j = 1; j < list.size(); j++) {
                    ParameterDto d1 = list.get(j);
                    if (d.getPmv1().equals(d1.getPmv1())) {
                        map.put(d1.getPmco(), d1.getPmnm());
                        list.remove(j);
                        j--;
                    }
                }
                i = 0;
                mapList.add(map);
            }
        }
        return mapList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.business.ActivityManager#checkUserCanSignIn
     * (java.lang.String)
     */
    @Override
    public JSONObject checkUserCanSignIn(String openId) {
        JSONObject obj = new JSONObject();
        Calendar calendar = null;
        try {
            logger.info("查询用户当前是否可以签到开始   openId为：" + openId);
            // 查询用户的签到记录
            List<CustomerAwardDto> result = customerAwardDao.checkUserCanSignIn(openId);
            UserSignInDto param = new UserSignInDto();
            // 如果查询为空 则认为用户今日未签到
            if (result == null || result.size() == 0) {
                logger.info("查询出" + openId + "用户今日的签到次数为0");
                param.setCanSignIn(true);
                param.setSignTime(0);
                obj.put("data", param);
            } else {
                logger.info("查询出" + openId + "用户今日的签到次数为" + result.size());
                // 如果查询出来的记录大于等于4 下一次签到时间为明天凌晨0点
                if (result.size() >= 4) {
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                    param.setCanSignIn(false);
                    param.setSignTime(4);
                    param.setNextSignTime(DateUtils.formatDate(calendar.getTime(), DateUtils.yyyy_MM_dd));
                    obj.put("data", param);
                } else {
                    int signInIntervalTime = 6;
                    // 查询签到间隔时间
                    QueryMessageDto queryMessageDto = queryServiceClient.queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "FDACTSIGNINTIME", null, null);
                    if (queryMessageDto == null) {
                        logger.error("调用远程query服务查询参数失败");
                        obj.put("returnCode", "1");
                        obj.put("returnMsg", "内部服务器错误");
                        return obj;
                    }
                    @SuppressWarnings("unchecked")
                    List<com.cmwa.ec.query.facade.dto.system.ParameterDto> data = (List<com.cmwa.ec.query.facade.dto.system.ParameterDto>) queryMessageDto.getData();
                    for (com.cmwa.ec.query.facade.dto.system.ParameterDto parameterDto : data) {
                        if ("******".equals(parameterDto.getPmco())) {
                            continue;
                        } else {
                            signInIntervalTime = Integer.parseInt(parameterDto.getPmco());
                        }
                    }
                    // 如果查询出来的记录小于4则做判断
                    calendar = Calendar.getInstance();
                    CustomerAwardDto customerAwardDto = result.get(0);
                    calendar.setTime(customerAwardDto.getCreatedDate());
                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + signInIntervalTime);
                    int differentDays = DateUtils.differentDays(new Date(), calendar.getTime());
                    // 如果下一次签到时间已经到了明天
                    calendar = Calendar.getInstance();
                    if (differentDays == 1) {
                        // 初始化时间 将下一次签到时间设置为明天凌晨0点
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        param.setCanSignIn(false);
                        param.setNextSignTime(DateUtils.formatDate(calendar.getTime(), DateUtils.yyyy_MM_dd));
                        param.setSignTime(result.size());
                        obj.put("data", param);
                    } else {
                        // 如果下一次签到时间还在今天则判断当前时间是否已经超过了签到时间
                        long now = calendar.getTime().getTime();
                        calendar.setTime(customerAwardDto.getCreatedDate());
                        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + signInIntervalTime);
                        long nextSignInTime = calendar.getTime().getTime();
                        param.setCanSignIn(now > nextSignInTime);
                        param.setNextSignTime(DateUtils.formatDate(calendar.getTime(), DateUtils.yyyy_MM_dd_HH_mm_ss));
                        param.setSignTime(result.size());
                        obj.put("data", param);
                    }
                    logger.info("返回参数为" + param);
                }
            }
            obj.put("returnCode", "0");
            obj.put("returnMsg", "");
        } catch (Exception e) {
            logger.error("判断用户是否可签到捕获异常:", e);
            obj.put("returnCode", "1");
            obj.put("returnMsg", "内部服务器错误");
        }
        return obj;
    }

    /**
     * 用户签到（获取碎片）
     */
    public String userSignIn(String openId, String activityId, String toOpenId) {
        logger.info("ActivityManagerImpl[userSignIn]用户签到>>>>>openId:" + openId + ">>>>>>toOpenId:" + toOpenId + ">>>>>>activityId:" + activityId);
        JSONObject obj = new JSONObject();
        String awardFrom = StringUtils.isBlank(toOpenId) ? "0" : "1";
        int shareGetPiece = 0;
        try {// 判断用户是否有资格获取碎片，防止刷接口
            JSONObject signIn = checkUserCanSignIn(openId);
            if (signIn == null) {
                logger.error("签到失败>>>>openId:" + openId);
                obj.put("returnCode", "1");
                obj.put("returnMsg", "签到权限获取失败");
            } else {
                Boolean ref = (Boolean) JSONObject.fromObject(signIn.getJSONObject("data")).get("canSignIn");
                if (ref) {// 可以获取碎片
                    if (awardFrom == "1") {// 此为分享获取碎片入口。该用户获取一枚碎片，分享者获取N枚，N为开关设置
                        return SynchroPiece(openId, activityId, toOpenId, awardFrom, shareGetPiece);// 同步更新分享者、用户获得碎片及分享记录
                    }
                    // 用户自己签到的情况
                    MagicMap magicMap = new MagicMap(new Object[][] { { "openid", openId }, { "activityId", activityId }, { "awardFrom", awardFrom } });// 此处渠道为"0"
                    String json = JSONObject.fromObject(magicMap).toString();
                    JSONObject obtainFragmentLogic = obtainFragmentLogic(json);
                    String obt = (String) (obtainFragmentLogic != null ? obtainFragmentLogic.get("returnCode") : "");
                    if (!StringUtils.isEmpty(obt) && obt.equals("0")) {
                        obj.put("returnCode", "0");
                        obj.put("returnMsg", "签到成功");
                    } else {
                        obj.put("returnCode", "1");
                        obj.put("returnMsg", "签到失败");
                    }
                } else {
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "签到权限获取失败");
                }
            }
        } catch (Exception e) {
            logger.error("用户签到捕获异常:", e);
            obj.put("returnCode", 1);
            obj.put("returnMsg", "内部服务器错误");
        }
        return obj.toString();
    }

    /**
     * 同步碎片时：用户的碎片记录、分享者的碎片记录、添加的分享记录三者同时更新
     * 
     * @param openId
     * @param activityId
     * @param toUserId
     * @param awardFrom
     * @param shareGetPiece
     * @return
     */
    private String SynchroPiece(String openId, String activityId, String toOpenId, String awardFrom, int shareGetPiece) {
        // 查询分享者每次获得的碎片数
        JSONObject obj1 = new JSONObject();
        if (!openId.equals(toOpenId)) {// 点击自己的链接时，助力失效
            List<ParameterDto> parameter = queryServiceClient.queryParameter("SYSTEM", "FDACTSHARECOUNT", null, null);
            if (parameter == null) {
                logger.error("调用远程query服务查询开关FDACTSHARECOUNT参数失败");
                obj1.put("returnCode", "1");
                obj1.put("returnMsg", "内部服务器错误");
                return obj1.toString();
            }
            for (ParameterDto dto : parameter) {
                if (dto.getPmky().equals("FDACTSHARECOUNT")) {
                    shareGetPiece = Integer.parseInt(dto.getPmco());
                }
            }
            MagicMap magicMap = new MagicMap(new Object[][] { { "openid", toOpenId }, { "activityId", activityId }, { "awardFrom", awardFrom } });
            for (int i = 1; i <= shareGetPiece; i++) {
                JSONObject obtainFragmentLogic = obtainFragmentLogic(JSONObject.fromObject(magicMap).toString());
                String obt = (String) (obtainFragmentLogic != null ? obtainFragmentLogic.get("returnCode") : "");
                if (!obt.equals("0")) {
                    logger.error("分享方获取碎片失败，toOpenId：" + toOpenId);
                    obj1.put("returnCode", "1");
                    obj1.put("returnMsg", "签到失败");
                    return obj1.toString();
                }
            }
        }
        // 用户添加碎片
        MagicMap magicMap1 = new MagicMap(new Object[][] { { "openid", openId }, { "activityId", activityId }, { "awardFrom", "0" } });// 消耗自己的碎片（等同于自己签到），所以渠道用"0"
        JSONObject obtainFragmentLogic = obtainFragmentLogic(JSONObject.fromObject(magicMap1).toString());
        String obt = (String) (obtainFragmentLogic != null ? obtainFragmentLogic.get("returnCode") : "");
        if (!obt.equals("0")) {
            logger.error("分享方获取碎片失败，openId：" + toOpenId);
            obj1.put("returnCode", "1");
            obj1.put("returnMsg", "签到失败");
            return obj1.toString();
        }
        // 创建分享记录
        if (!openId.equals(toOpenId)) {
            ShareInfoDto shareInfoDto = new ShareInfoDto();
            shareInfoDto.setActivityId(activityId);
            shareInfoDto.setFromOpenid(toOpenId);
            shareInfoDto.setOpenId(openId);
            shareInfoDto.setShareId(UUID.randomUUID().toString().replace("-", ""));
            Boolean ref1 = customerAwardDao.createShareRecored(shareInfoDto) > 0;
            if (!ref1) {
                logger.error("创建分享记录失败");
                obj1.put("returnCode", "1");
                obj1.put("returnMsg", "创建分享记录失败");
                return obj1.toString();
            }
        }
        obj1.put("returnCode", "0");
        obj1.put("returnMsg", "签到成功");
        return obj1.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#saveUserAwardMsg
     * (java.lang.String)
     */
    @Override
    @Transactional(value = "transactionManager")
    public JSONObject saveUserAwardInfo(String jsonParam) {
        JSONObject obj = new JSONObject(); // 返回参数
        JSONObject param = JSONObject.fromObject(jsonParam);
        String openId = (String) param.get("openid");
        String cusAwardId = (String) param.get("cusAwardId");
        String activityId = (String) param.get("activityId");
        logger.info("用户兑奖开始 接收到入参：" + jsonParam);
        // 参数判断
        if (StringUtils.isBlank(openId) || StringUtils.isBlank(cusAwardId) || StringUtils.isBlank(activityId)) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数为空");
            return obj;
        }
        // 判断用户是否关注
        if (!getSubscribeState(openId)) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "用户未关注");
            return obj;
        }

        // 根据用户中奖记录id查询中奖信息
        CustomerAwardDto result = customerAwardDao.queryUserAwardByCusAwardId(cusAwardId, openId);
        logger.info("根据用户中奖记录id查询到中奖信息为  " + result);
        if (result == null) {
            logger.info("未查询到奖品信息，返回错误信息");
            obj.put("returnCode", "1");
            obj.put("returnMsg", "未查询到奖品信息");
            return obj;
        }
        // 如果奖品是未领取状态
        if ("0".equals(result.getGetState())) {
            logger.info("修改奖品状态，并保存用户信息");
            int updateCustomerAward = customerAwardDao.updateCustomerAward(cusAwardId, "1");
            if (updateCustomerAward >= 1) {
                ActivityUserInfoDto dto = new ActivityUserInfoDto();
                try {
                    BeanUtils.copyProperties(dto, param);
                } catch (Exception e) {
                    logger.error("保存用户兑奖信息捕获异常 ： " + e);
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "内部服务器错误");
                    return obj;
                }
                dto.setUserinfoId(UUID.randomUUID().toString().replace("-", ""));
                prepareNewActDto(dto);
                int saveUserInfo = actUserInfoDao.saveUserInfo(dto);
                if (saveUserInfo >= 1) {
                    obj.put("returnCode", "0");
                    obj.put("returnMsg", "success");
                } else {
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "保存兑奖信息失败");
                }
            } else {
                obj.put("returnCode", "1");
                obj.put("returnMsg", "领取奖品失败");
            }
        } else {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "奖品已被领取");
        }
        return obj;
    }

    public void prepareNewActDto(ActivityBaseDto dto) {
        dto.setCreatedBy("CustomerAwardManagerImpl");
        dto.setCreatedDate(new Date());
        dto.setUpdatedDate(new Date());
        dto.setUpdatedBy("CustomerAwardManagerImpl");
    }

    public void prepareExistsActDto(ActivityBaseDto dto) {
        dto.setUpdatedDate(new Date());
        dto.setUpdatedBy("CustomerAwardManagerImpl");
    }

    /**
     * 解密userId查询对应的openId
     * 
     * @param userId
     * @param request
     */
    private String queryOpenIdByDecryptUserId(String userId) {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("encrypted", userId));
        String entity = HttpPostUtil.sendPost("decrypt", param, "");
        if (StringUtils.isBlank(entity)) {
            logger.info("调用远程api解密userId失败");
            return "";
        }
        JSONObject fromObject = JSONObject.fromObject(entity);
        boolean flag = (Boolean) fromObject.get("success");
        if (flag) {
            String decryptUserId = String.valueOf(fromObject.get("resp"));
            InvectorUserInfoexDto resultDto = activityManager.queryUserInfoByUerId(decryptUserId);
            if (resultDto != null) {
                return resultDto.getOpenId();
            } else {
                logger.info("通过解密userId查询用户信息失败");
            }
        } else {
            logger.info("调用远程api解密userId失败");
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#queryUserAwardMsg
     * (java.lang.String)
     */
    @Override
    public JSONObject queryUserAwardInfo(String jsonParam) {
        JSONObject obj = JSONObject.fromObject(jsonParam);
        String openId = (String) obj.get("openid");
        String activityId = (String) obj.get("activityId");
        if (StringUtils.isBlank(openId) || StringUtils.isBlank(activityId)) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数为空");
        } else {
            List<ActivityUserInfoDto> result = actUserInfoDao.queryUserInfo(openId, activityId);
            obj.put("returnCode", "0");
            obj.put("returnMsg", "");
            obj.put("data", result);
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#updateUserAwardInfo
     * (java.lang.String)
     */
    @Override
    public JSONObject updateUserAwardInfo(String jsonParam) {
        JSONObject obj = new JSONObject(); // 返回参数
        JSONObject param = JSONObject.fromObject(jsonParam);
        String userinfoId = (String) param.get("userinfoId");
        // 关键参数判断
        if (StringUtils.isBlank(userinfoId)) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数为空");
            return obj;
        }
        String userFullName = (String) param.get("userFullName");
        String mobile = (String) param.get("mobile");
        String address = (String) param.get("address");
        if (!StringUtils.isBlank(userFullName) || !StringUtils.isBlank(mobile) || !StringUtils.isBlank(address)) {
            ActivityUserInfoDto dto = new ActivityUserInfoDto();
            try {
                BeanUtils.copyProperties(dto, param);
                int updateUserInfo = actUserInfoDao.updateUserInfo(dto);
                if (updateUserInfo >= 1) {
                    obj.put("returnCode", "0");
                    obj.put("returnMsg", "修改成功");
                } else {
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "修改失败");
                }
            } catch (Exception e) {
                logger.error("更新用户兑奖信息捕获异常 ：" + e);
                obj.put("returnCode", "1");
                obj.put("returnMsg", "内部服务器错误");
            }
        } else {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "请添加信息后再提交");
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#querySystemNotice
     * ()
     */
    @Override
    public JSONObject querySystemNotice() {
        JSONObject obj = new JSONObject();
        long startStamp = System.currentTimeMillis();
        logger.info("查询系统通知开始..");
        try {
            List<SystemNoticeDto> systemNotice = customerAwardDao.querySystemNotice();
            logger.info("查询到系统通知信息：" + systemNotice);
            long endStamp = System.currentTimeMillis();
            long queryTime = (endStamp - startStamp) / 1000;
            logger.info("查询花费时间：" + queryTime);
            obj.put("returnCode", "0");
            obj.put("returnMsg", "");
            obj.put("data", systemNotice);
        } catch (Exception e) {
            logger.error("查询系统信息捕获异常：", e);
            obj.put("returnCode", "1");
            obj.put("returnMsg", "内部服务器错误");
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#queryUserNotice
     * (java.lang.String)ovWiouL1VymzbRsGwP5ZWqL4Tujg
     */
    @Override
    public JSONObject queryUserNotice(String openId, String activityId) {
        JSONObject obj = new JSONObject();
        if (StringUtils.isBlank(openId) || StringUtils.isBlank(activityId)) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数为空");
            return obj;
        }
        // 需求： 查询出有其他用户为当前用户助力时的信息 用户中奖时的信息 用户好友的信息
        List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();
        try {
            logger.info("查询用户相关通知接收到参数 openid :" + openId + ",activityId:" + activityId);
            // 此处查询出来的信息为所有与用户有关的openId type等于0时是用户为其他用户助力 等于1时是其他用户为用户助力
            List<Map<String, Object>> queryUserRelation = shareInfoDao.queryUserRelation(openId);
            if (queryUserRelation == null || queryUserRelation.size() == 0) {
                obj.put("returnCode", "0");
                obj.put("returnMsg", "");
                obj.put("data", noticeList);
                return obj;
            }
            logger.info("查询到与用户相关的openid返回值为:" + queryUserRelation);
            Set<String> set = new HashSet<String>();
            // 根据分享记录的创建时间排序
            Collections.sort(queryUserRelation, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Date dateOne = DateUtils.parseString(String.valueOf(o1.get("created_date")));
                    Date dateTwo = DateUtils.parseString(String.valueOf(o2.get("created_date")));
                    long stampOne = dateOne.getTime();
                    long stampTwo = dateTwo.getTime();
                    long result = stampOne - stampTwo;
                    if (result < 0) {
                        return 1;
                    } else if (result > 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            List<String> shareOpenIdList = new ArrayList<String>();
            List<String> fromShareOpenIdList = new ArrayList<String>();
            for (Map<String, Object> map : queryUserRelation) {
                set.add((String) map.get("from_openid"));
                set.add((String) map.get("openid"));
                // 如果是为当前用户助力
                if (openId.equals(map.get("from_openid"))) {
                    shareOpenIdList.add((String) map.get("openid"));
                } else {
                    fromShareOpenIdList.add((String) map.get("from_openid"));
                }
            }
            set.remove(openId);
            int endIdx = shareOpenIdList.size() < 10 ? shareOpenIdList.size() : 10;
            logger.info("为用户助力的openid有" + shareOpenIdList);
            shareOpenIdList = shareOpenIdList.subList(0, endIdx);
            logger.info("截取最新的为用户助力的openid有" + shareOpenIdList);
            ArrayList<String> distinctList = new ArrayList<String>(set);
            List<Map<String, Object>> userinfoList = new ArrayList<Map<String, Object>>();
            // 此处查询的是所有与用户有关的openid和昵称
            if (distinctList.size() != 0) {
                userinfoList = actUserInfoDao.queryUserNameOnCmwaWxUserInfo(distinctList);
            }
            // 再去查询与用户有关联的openid是否有中奖信息
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("activityId", activityId);
            param.put("awardType", "0");
            param.put("list", distinctList);
            List<Map<String, Object>> userAwardRecords = customerAwardDao.queryUserAwardRecordsOnFatherDay(param);
            logger.info("查询到与当前用户有关的其他用户的中奖信息：" + userAwardRecords);

            Map<String, Object> tempMsgMap = null;

            // 查询当前用户的中奖信息
            param.put("openid", openId);
            List<QueryUserAwardDto> userAwardList = customerAwardDao.queryUserAwardRecordOnFatherDay(param);
            for (int i = 0; i < userAwardList.size(); i++) {
                QueryUserAwardDto queryUserAwardDto = userAwardList.get(i);
                tempMsgMap = new HashMap<String, Object>();
                tempMsgMap.put("awardName", queryUserAwardDto.getAwardName());
                tempMsgMap.put("type", "0");
                AwardInfoDto dto = awardInfoDao.getAwardDto(queryUserAwardDto.getAwardId());
                dto = awardInfoDao.getAwardDto(dto.getAwardPid());
                String boxState = "铜宝箱";
                if ("YINBOX".equals(dto.getAwardName())) {
                    boxState = "银宝箱";
                } else if ("JINBOX".equals(dto.getAwardName())) {
                    boxState = "金宝箱";
                }
                tempMsgMap.put("awardSource", boxState);
                noticeList.add(tempMsgMap);
            }

            // 用户的好友的中奖信息
            if (userAwardRecords != null && userAwardRecords.size() > 0) {
                for (int i = 0; i < userAwardRecords.size(); i++) {
                    Map<String, Object> userAward = userAwardRecords.get(i);
                    for (int j = 0; j < userinfoList.size(); j++) {
                        Map<String, Object> info = userinfoList.get(j);
                        if (info.get("openid").equals(userAward.get("openid"))) {
                            tempMsgMap = new HashMap<String, Object>();
                            tempMsgMap.put("nickname", info.get("nickname"));
                            tempMsgMap.put("awardName", userAward.get("awardName"));
                            tempMsgMap.put("type", "2");
                            AwardInfoDto dto = awardInfoDao.getAwardDto(userAward.get("awardId").toString());
                            dto = awardInfoDao.getAwardDto(dto.getAwardPid());
                            String boxState = "铜宝箱";
                            if ("YINBOX".equals(dto.getAwardName())) {
                                boxState = "银宝箱";
                            } else if ("JINBOX".equals(dto.getAwardName())) {
                                boxState = "金宝箱";
                            }
                            tempMsgMap.put("awardSource", boxState);
                            noticeList.add(tempMsgMap);
                            break;
                        }
                    }
                }
            }

            // 用户为其他用户助力的信息
            if (fromShareOpenIdList.size() > 0) {
                for (int i = 0; i < fromShareOpenIdList.size(); i++) {
                    String tempOpenId = fromShareOpenIdList.get(i);
                    for (int j = 0; j < userinfoList.size(); j++) {
                        Map<String, Object> info = userinfoList.get(j);
                        if (info.get("openid").equals(tempOpenId)) {
                            tempMsgMap = new HashMap<String, Object>();
                            tempMsgMap.put("nickname", info.get("nickname"));
                            tempMsgMap.put("type", "3");
                            noticeList.add(tempMsgMap);
                            break;
                        }
                    }
                }
            }

            // 为用户助力的信息
            if (shareOpenIdList.size() > 0) {
                for (int i = 0; i < shareOpenIdList.size(); i++) {
                    String shareOpenId = shareOpenIdList.get(i);
                    for (int j = 0; j < userinfoList.size(); j++) {
                        Map<String, Object> info = userinfoList.get(j);
                        if (info.get("openid").equals(shareOpenId)) {
                            tempMsgMap = new HashMap<String, Object>();
                            tempMsgMap.put("nickname", info.get("nickname"));
                            tempMsgMap.put("type", "1");
                            noticeList.add(tempMsgMap);
                            break;
                        }
                    }
                }
            }

            param.remove("list");
            param.put("openid", openId);
            logger.info("返回的用户通知信息：" + noticeList);
            obj.put("returnCode", "0");
            obj.put("returnMsg", "");
            obj.put("data", noticeList);
        } catch (Exception e) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "内部服务器错误");
            logger.error("获取用户通知捕获异常 ", e);
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#queryUserAward
     * (java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String queryUserAward(String openId, String activityId, String awardName, String awardType) {
        logger.info("用户查询奖品记录接收到参数,openId:" + openId + ",activityId:" + activityId + ",awardName:" + awardName + ",awardType:" + awardType);
        JSONObject obj = new JSONObject();
        // 关键参数判断
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(activityId)) {
            logger.info("关键参数为空，返回错误信息");
            obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            return obj.toString();
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("openid", openId);
        param.put("activityId", activityId);
        param.put("awardName", awardName);
        param.put("awardType", awardType);
        try {
            List<QueryUserAwardDto> list = customerAwardDao.queryUserAwardRecordOnFatherDay(param);
            List<QueryUserAwardDto> result = new ArrayList<QueryUserAwardDto>();
            for (QueryUserAwardDto dto : list) {
                if (df.parse(dto.getInvalidDate()).before(new Date())) {
                    dto.setAwardState("2");
                }
                result.add(dto);
            }
            logger.info("根据参数查询出数据为：" + result);
            obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            obj.put("data", result);
        } catch (Exception e) {
            logger.error("查询用户中奖记录捕获异常 ", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#queryShareInfo
     * (java.lang.String, java.lang.String)
     */
    @Override
    public JSONObject queryShareInfo(String activityId, String channel) {
        JSONObject obj = new JSONObject();
        try {
            ShareConfigDto queryShareInfo = customerAwardDao.queryShareInfo(activityId, channel);
            if (queryShareInfo == null) {
                logger.info("未查询到分享信息");
                obj.put("returnCode", "1");
                obj.put("returnMsg", "未查询到分享信息");
                return obj;
            }
            obj.put("data", queryShareInfo);
            obj.put("returnCode", "0");
            obj.put("returnMsg", "查询成功");
        } catch (Exception e) {
            logger.error("查询分享记录发生异常，", e);
            obj.put("returnCode", "1");
            obj.put("returnMsg", "系统内部错误");
        }
        return obj;
    }

    @Override
    public JSONObject getShareInfo(String activityId, String openId) {
        JSONObject obj = new JSONObject();
        try {
            ShareInfoDto ShareInfo = customerAwardDao.getShareInfo(activityId, openId);
            if (ShareInfo == null) {
                logger.info("未查询到分享信息");
                obj.put("returnCode", "1");
                obj.put("returnMsg", "未查询到分享信息");
                return obj;
            }
            obj.put("data", ShareInfo);
            obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error("查询分享记录发生异常，", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj;
    }

    @Override
    public JSONObject getSystemMessList(String json) {
        logger.info("调用查询用户的助力信息方法>>>>getSystemMessList>>>>start>>>>入参：" + json);
        JSONObject jsonObject = new JSONObject();
        JSONObject param = JSONObject.fromObject(json);
        int pageIndex = 1;
        try {
            if (param.has("openid") && param.has("activityId") && param.has("type")) {
                String openId = param.getString("openid");
                String activityId = param.getString("activityId");
                String type = param.getString("type");
                if (param.has("pageIndex")) {
                    pageIndex = Integer.parseInt(param.getString("pageIndex"));
                }
                if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(activityId) || StringUtils.isEmpty(type)) {
                    jsonObject.put("returnCode", "1");
                    jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
                }
                int startNumbern = (pageIndex - 1) * PAGE_SIZE + 1;
                int endNumber = startNumbern + PAGE_SIZE;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("openId", openId);
                map.put("activityId", activityId);
                map.put("type", type);
                map.put("startNumbern", startNumbern);
                map.put("endNumber", endNumber);
                List<ShareInfoDto> shareList = shareInfoDao.queryShareInfoList(map);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < shareList.size(); i++) {
                    ShareInfoDto dto = shareList.get(i);
                    Map<String, Object> ret = new HashMap<String, Object>();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if ("0".equals(type)) {
                        ret.put("nickName", dto.getNickName());
                    } else {
                        ret.put("nickName", dto.getFromNickName());
                    }
                    ret.put("type", type);
                    ret.put("date", df.format(dto.getCreatedDate()));
                    list.add(ret);
                }
                jsonObject.put("returnCode", "0");
                jsonObject.put("returnMsg", "SUCCESS");
                jsonObject.put("data", list);
            }
        } catch (Exception e) {
            logger.error("调用查询用户的助力信息方法>>>>getSystemMessList>>>>>发生异常,异常信息:", e);
            jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用查询用户的助力信息方法>>>>getSystemMessList>>>>end>>>>入参：" + jsonObject.toString());
        return jsonObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#
     * queryAllUserAwardInfo(java.lang.String, java.lang.String, int, int)
     */
    @Override
    public JSONObject queryAllUserAwardInfo(String activityId, int beginIdx, int endIdx) {
        JSONObject obj = new JSONObject();
        if (StringUtils.isBlank(activityId)) {
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数为空");
            return obj;
        }
        // 查询与用户有关的其他用户
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("activityId", activityId);
            param.put("awardType", "0");
            param.put("beginIdx", beginIdx + 1);
            param.put("endIdx", endIdx);
            List<Map<String, Object>> queryUserFriendAwardInfo = customerAwardDao.queryAllUserAwardInfo(param);
            int count = customerAwardDao.queryAllUserAwardInfoCount(param);
            obj.put("returnCode", "0");
            obj.put("returnMsg", "");
            obj.put("data", queryUserFriendAwardInfo);
            obj.put("total", count);
        } catch (Exception e) {
            logger.error("查询与当前用户有关的其他用户获奖信息捕获异常，", e);
            obj.put("returnCode", "1");
            obj.put("returnMsg", "系统内部错误");
        }
        return obj;
    }

    @Override
    public JSONObject queryAwardNum(String str1, String str2, String str3) {
        logger.info("调用查询奖品库存数量方法》》》》》》》》queryAwardNum》》start》》入参[str1:" + str1 + ",str2:" + str2 + ",str3:" + str3 + "]");
        JSONObject obj = new JSONObject();
        int num = 0;
        try {
            if (StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)) {
                logger.info("关键参数为空");
                obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            } else {
                StringBuilder sb = new StringBuilder();
                if (!StringUtils.isEmpty(str3)) {
                    sb.append(AWARD_NUMBER);
                    sb.append(str3);
                }
                ParameterDto dto = new ParameterDto();
                dto.setPmst(str1);
                dto.setPmky(str2);
                dto.setPmco(sb.toString());
                List<ParameterDto> list = actParameterDao.getParameter(dto);
                for (ParameterDto p : list) {
                    if (p.getPmco().contains(AWARD_NUMBER)) {
                        num += Integer.valueOf(p.getPmnm());
                    }
                }
                obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
                obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                obj.put("num", num);
            }
        } catch (Exception e) {
            logger.error("调用查询奖品库存数量方法》》》》》》》》queryAwardNum》》》发生异常，异常信息:", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj;
    }

    @Override
    public JSONObject lotteryLogic(String openId, String activityId) {
        logger.info("调用抽奖逻辑方法》》》》》》》》lotteryLogic》》start》》入参[openId:" + openId + ",activityId:" + activityId + "]");
        JSONObject obj = new JSONObject();
        try {
            if (!StringUtils.isEmpty(openId) && !StringUtils.isEmpty(activityId)) {
                // 1、判断用户是否关注
                if (!getSubscribeState(openId)) {
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "用户未关注");
                    return obj;
                }
                // 2、判断用户是否已经抽奖
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("openid", openId);
                param.put("activityId", activityId);
                List<QueryUserAwardDto> list = customerAwardDao.queryUserAwardRecordOnFatherDay(param);
                if (null != list && list.size() > 0) {
                    obj.put("returnCode", "2");
                    obj.put("returnMsg", "用户已经抽过奖");
                    return obj;
                }
                // 3、查询奖品列表
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("activityId", activityId);
                map.put("awardType", "0");
                List<AwardInfoDto> awardInfoList = awardInfoDao.queryAwardList(map);
                AwardInfoDto awardInfo = newlottery(awardInfoList);
                if (null == awardInfo) {
                    obj.put("returnCode", "3");
                    obj.put("returnMsg", "活动太火爆，奖品已抽完！");
                    return obj;
                }
                // 4、修改奖品数量
                ParameterDto parameter = new ParameterDto();
                parameter.setPmst(PMST);
                parameter.setPmky(YMH_AWARD);
                parameter.setPmco(AWARD_NUMBER + awardInfo.getAwardId());
                // 修改奖品库存数量
                int returnState = actParameterDao.updateParameter(parameter);
                if (returnState > 0) {
                    // 保存用户中奖信息
                    insertArawd(activityId, "2", openId, awardInfo.getAwardId());
                    obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
                    obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("award", awardInfo);
                    obj.put("data", jsonObject);
                } else {
                    obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
                    obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
                }
            } else {
                logger.info("关键参数为空");
                obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            }
        } catch (Exception e) {
            logger.error("调用抽奖逻辑方法》》》》》》》》lotteryLogic》》》》》发生异常，异常信息:", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj;
    }

    private AwardInfoDto newlottery(List<AwardInfoDto> awardInfoList) throws Exception {
        List<Double> orignalRates = new ArrayList<Double>(awardInfoList.size());
        // 查询奖品开关
        ParameterDto dto = new ParameterDto();
        dto.setPmst(PMST);
        dto.setPmky(YMH_AWARD);
        List<ParameterDto> list = actParameterDao.getParameter(dto);
        for (int i = 0; i < awardInfoList.size(); i++) {
            AwardInfoDto d = awardInfoList.get(i);
            for (ParameterDto p : list) {
                if (p.getPmco().contains(d.getAwardId())) {
                    if (p.getPmco().contains(AWARD_NUMBER))
                        d.setFactnum(Integer.valueOf(p.getPmnm()));
                    if (p.getPmco().contains(AWARD_CHANCE))
                        d.setProbability(Double.valueOf(p.getPmnm()));
                }
            }
            if (d.getFactnum().intValue() > 0) {
                Double probability = Double.parseDouble(d.getProbability().toString());
                if (probability < 0) {
                    probability = 0d;
                }
                orignalRates.add(probability);
            } else {
                awardInfoList.remove(i);
                i--;
            }

        }
        logger.info(">>>>>>>" + this.getClass().getName() + ">>>newlottery()>>>执行随机抽取奖品元素>>>start>>>入参 orignalRates：" + JSONArray.fromObject(orignalRates).toString());
        if (orignalRates.isEmpty() || null == orignalRates) {
            return null;
        }
        int orignalIndex = LotteryUtil.lottery(orignalRates);
        return awardInfoList.get(orignalIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.activity.CustomerAwardManager#gdBmwGetCard
     * (java.util.Map)
     */
    @Override
    public JSONObject gdBmwGetCard(Map<String, Object> paramMap) {
        try {
            String userFullName = (String) paramMap.get("userFullName");
            String userMobile = (String) paramMap.get("userMobile");
            String openid = (String) paramMap.get("openid");
            String activityId = (String) paramMap.get("activityId");
            String awardId = (String) paramMap.get("awardId");
            JSONObject returnObj = new JSONObject();
            logger.info("粤马会领取券接口接受到参数,userFullName:{},userMobile:{},,openid:{},activityId:{}", userFullName, userMobile, openid, activityId);
            List<ActivityUserInfoDto> existsUserInfoList = actUserInfoDao.queryUserInfo(openid, activityId);
            if (existsUserInfoList == null || existsUserInfoList.size() == 0) {
                logger.info("查询当前用户未在数据库内登记信息 执行信息入库操作");
                ActivityUserInfoDto insertDto = new ActivityUserInfoDto();
                insertDto.setActivityId(activityId);
                insertDto.setAddress(null);
                insertDto.setMobile(userMobile);
                insertDto.setOpenid(openid);
                insertDto.setUserFullName(userFullName);
                insertDto.setUserinfoId(UUID.randomUUID().toString().replaceAll("-", ""));
                prepareNewActDto(insertDto);
                logger.info("添加数据为：{}", insertDto);
                int executeResult = actUserInfoDao.saveUserInfo(insertDto);
                logger.info("添加影响列为：{}", executeResult);
                if (executeResult != 1) {
                    returnObj.put("returnCode", "9999");
                    returnObj.put("returnMsg", "系统异常");
                    return returnObj;
                }
            }
            logger.info("开始查询配置的奖品领取上限");
            List<ParameterDto> queryResult = queryServiceClient.queryParameter("SYSTEM", "GDBMWACTCONF", "LIMITCOUNT", "");
            if (queryResult == null || queryResult.size() == 0) {
                logger.info("未配置奖品上限，不进行控制");
            } else {
                int countUsedAward = activityAwardStoreDao.countUsedAward(activityId, awardId);
                int limitCount = Integer.parseInt(queryResult.get(0).getPmnm());
                logger.info("已经发放的奖品数量为:{},奖品上限为:{}", countUsedAward, limitCount);
                if (countUsedAward >= limitCount) {
                    logger.info("已经放发的奖品数量达到上限，不发放奖品");
                    returnObj.put("returnCode", "9999");
                    returnObj.put("returnMsg", "高端洗牙套餐领取火爆，已经全部领完，请关注招商财富公众号更多活动。");
                    return returnObj;
                }
            }
            logger.info("开始随机选取数据库内剩余卡券");
            ActivityAwardStoreDto param = new ActivityAwardStoreDto();
            param.setActivityId(activityId);
            param.setAwardId(awardId);
            param.setAwardState("0");
            param.setVersion("1");
            ActivityAwardStoreDto doService = doService(param, 1);
            logger.info("经过选取，返回数据为:{}", doService);
            if (doService == null) {
                returnObj.put("returnCode", "9999");
                returnObj.put("returnMsg", "高端洗牙套餐领取火爆，已经全部领完，请关注招商财富公众号更多活动。");
                return returnObj;
            } else {
                int insertArawd = insertArawd(activityId, "0", openid, awardId, doService.getStoreId(), null, null);
                if (insertArawd != 1) {
                    logger.warn("用户已经成功领取卡片，但记录至中奖纪录表内失败！影响列为:{},领取的卡券信息为：{}", insertArawd, doService);
                }
                List<ParameterDto> msgConf = queryServiceClient.queryParameter("SYSTEM", "GDBMWACTCONF", "", "");
                if (msgConf == null || msgConf.size() == 0) {
                    logger.info("未查询到模板消息相关配置 不发送模板消息");
                } else {
                    logger.info("开始发送模板消息");
                    String url = "" + SpringContextUtil.getProperty("FRONTSERVICE_URL") + "/sendTemplateMsg.html";
                    List<NameValuePair> sendTemplateMessage = new ArrayList<NameValuePair>();
                    sendTemplateMessage.add(new BasicNameValuePair("toUserId", (String) paramMap.get("openid")));
                    for (ParameterDto temp : msgConf) {
                        if (temp.getPmco().contains("TEMPLATEID")) {
                            sendTemplateMessage.add(new BasicNameValuePair("templateId", temp.getPmnm()));
                        } else if (temp.getPmco().contains("TEMPLATEURL")) {
                            sendTemplateMessage.add(new BasicNameValuePair("url", temp.getPmnm()));
                        } else if (temp.getPmco().contains("TEMPLATEDATA")) {
                            String replace = temp.getPmnm().replace("{cardNum}", doService.getAwardNum());
                            sendTemplateMessage.add(new BasicNameValuePair("content", CommonController.cn2unicode(WeixinUtil.genTempLateUtil(replace))));
                        }
                    }
                    sendTemplateMessage.add(new BasicNameValuePair("filter", "5"));
                    JSONObject response = JSONObject.fromObject(HttpPostUtil.sendPostToFront(url, sendTemplateMessage, null));
                    logger.info("推送模板消息返回值为：{}", response);
                }

                returnObj.put("returnCode", "0000");
                returnObj.put("returnMsg", "success");
                returnObj.put("data", doService);
                return returnObj;
            }
        } catch (Exception e) {
            logger.error("获取卡券时捕获异常", e);
            JSONObject returnObj = new JSONObject();
            returnObj.put("returnCode", "9999");
            returnObj.put("returnMsg", "系统异常");
            return returnObj;
        }
    }

    private ActivityAwardStoreDto doService(ActivityAwardStoreDto param, int count) {
        try {
            if (count >= 4) {
                logger.info("已经执行{}次且未成功获取到卡券，中断执行", count - 1);
                return null;
            }
            logger.info("递归方法执行第{}次，参数为：{}", count, param);
            List<ActivityAwardStoreDto> listAwardOnStore = activityAwardStoreDao.listAwardOnStore(param, true);
            logger.info("根据参数查询返回结果集为：{}", listAwardOnStore);
            if (listAwardOnStore != null && listAwardOnStore.size() != 0) {
                ActivityAwardStoreDto activityAwardStoreDto = listAwardOnStore.get(0);
                int version = Integer.parseInt(activityAwardStoreDto.getVersion());
                activityAwardStoreDto.setVersion(String.valueOf(version + 1));
                activityAwardStoreDto.setAwardState("1");
                logger.info("经过处理之后需求进行修改的参数为：{}", activityAwardStoreDto);
                int executeResult = activityAwardStoreDao.updateAwardOnStore(activityAwardStoreDto, true);
                logger.info("修改影响列为：{}", executeResult);
                if (executeResult < 1) {
                    logger.info("修改失败,再次执行递归");
                    count++;
                    return doService(param, count);
                } else {
                    logger.info("回写成功！返回值为：{}", activityAwardStoreDto);
                    return activityAwardStoreDto;
                }
            } else {
                logger.info("返回结果集为空");
                return null;
            }
        } catch (Exception e) {
            logger.error("执行递归方法获取卡券时捕获异常：", e);
            return null;
        }
    }

    @Override
    public JSONObject queryAllWinningCount(String activityId, String awardFrom) {
        logger.info("查询参加活动总人数>>>queryAllWinningCount>>>start,入参>>>[activityId:" + activityId + ",awardFrom:" + awardFrom + "]");
        JSONObject obj = new JSONObject();
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(awardFrom)) {
            obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            return obj;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("activityId", activityId);
            map.put("awardFrom", awardFrom);
            int result = customerAwardDao.queryAllWinningCount(map);// 实际参加活动人数
            // 查询基础人数
            ParameterDto dto = new ParameterDto();
            dto.setPmst("SYSTEM");
            dto.setPmky("BASICS_NUMBER");
            List<ParameterDto> list = actParameterDao.getParameter(dto);
            if (null != list && !list.isEmpty()) {
                ParameterDto param = list.get(0);
                if (!StringUtils.isEmpty(param.getPmco())) {
                    result += Integer.parseInt(param.getPmco());
                }
            }
            obj.put("returnCode", WXConstants.COMMON_SUCCESS);
            obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            obj.put("data", result);
        } catch (Exception e) {
            logger.error("查询参加活动总人数时捕获异常：", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj;
    }

    @Override
    public JSONObject queryAwardList(String activityId) {
        logger.info("查询活动奖品列表>>>queryAwardList>>>start,入参>>>[activityId:" + activityId + "]");
        JSONObject obj = new JSONObject();
        if (StringUtils.isEmpty(activityId)) {
            obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            return obj;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("activityId", activityId);
            map.put("awardType", "0");
            List<AwardInfoDto> list = awardInfoDao.queryAwardList(map);
            obj.put("returnCode", WXConstants.COMMON_SUCCESS);
            obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            obj.put("data", list);
        } catch (Exception e) {
            logger.error("查询活动奖品列表时捕获异常：{}", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj;
    }

    @Override
    public JSONObject turnDraw(String activityId, String openId, String telePhone, String name) {
        logger.info("调用大转盘抽奖方法>>>turnDraw>>>start>>>入参{activityId:" + activityId + ",openId" + openId + ",telePhone:" + telePhone + ",name:" + name + "}");
        JSONObject obj = new JSONObject();
        try {
            if (!StringUtils.isEmpty(openId) && !StringUtils.isEmpty(activityId) && !StringUtils.isEmpty(telePhone) && !StringUtils.isEmpty(name)) {
                // 1、判断用户是否关注
                if (!getSubscribeState(openId)) {
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "用户未关注");
                    return obj;
                }
                // 2、判断手机是否已经参加抽奖
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("telephone", telePhone);
                param.put("activityId", activityId);
                List<CustomerAwardDto> list = customerAwardDao.queryUserAwardByTelephone(param);
                if (null != list && !list.isEmpty()) {
                    obj.put("returnCode", "2");
                    obj.put("returnMsg", "该号码已被使用");
                    return obj;
                }
                // 3、查询奖品列表
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("activityId", activityId);
                map.put("awardType", "0");
                List<AwardInfoDto> awardInfoList = awardInfoDao.queryAwardList(map);
                AwardInfoDto awardInfo = newlottery(awardInfoList);
                if (null == awardInfo) {
                    obj.put("returnCode", "3");
                    obj.put("returnMsg", "活动太火爆，奖品已抽完！");
                    return obj;
                }
                // 4、修改奖品数量
                ParameterDto parameter = new ParameterDto();
                parameter.setPmst(PMST);
                parameter.setPmky(YMH_AWARD);
                parameter.setPmco(AWARD_NUMBER + awardInfo.getAwardId());
                // 修改奖品库存数量
                int returnState = actParameterDao.updateParameter(parameter);
                if (returnState > 0) {
                    // 保存用户中奖信息
                    insertArawd(activityId, "2", openId, awardInfo.getAwardId(), null, telePhone, name);
                    obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
                    obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("award", awardInfo);
                    obj.put("data", jsonObject);
                } else {
                    obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
                    obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
                }
            } else {
                obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("调用大转盘抽奖方法>>>turnDraw>>>时捕获异常{}:", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return obj;
    }

    @Override
    public boolean getSubscribeState(String openId) {
        logger.info("------------getSubscribeState---------openId:" + openId);
        try {
            InvectorUserInfoexDto resultDto = activityDao.queryUserInfoByOpenId(openId);
            logger.info(">>>>>queryUserInfoByOpenId>>> decoder openId获取用户信息,回参:{resultDto :}" + resultDto);
            if (null != resultDto) {
                if (resultDto.getSubscribe() == null || "".equals(resultDto.getSubscribe())) {// 新表为空，则去旧表里面查询关注状态
                    // 判断旧表数据：cmf_weixin_user_info中status=1,并且cmf_weixin_user_infoex表BINDSTAT字段不为C
                    UserInfoDto userInfoDto = userInfoDao.queryByOpenId(resultDto.getOpenId());
                    UserInfoexDto userInfoexDto = userInfoExDao.queryByOpenId(resultDto.getOpenId(), null);
                    if (userInfoDto != null) {// 非空
                        if ("1".equals(userInfoDto.getStatus())) {
                            if (userInfoexDto == null) {
                                return true;
                            } else {
                                if (!"C".equals(userInfoexDto.getBindstat()))
                                    return true;
                            }
                        }
                    }
                } else {
                    String subscribe = resultDto.getSubscribe();
                    if ("1".equals(subscribe)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("查询用户关注状态发生异常", e);
            return false;
        }
        return false;
    }

    @Override
    public JSONObject saveEnrolmentUser(String activityId, String openId, String mobile, String name, String state) {
        logger.info("调用用户预约信息方法>>>saveEnrolmentUser>>>start>>>入参{activityId:" + activityId + ",openId" + openId + ",mobile:" + mobile + ",name:" + name + ",state" + state + "}");
        JSONObject obj = new JSONObject();
        try {
            if (!StringUtils.isEmpty(openId) && !StringUtils.isEmpty(activityId) && !StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(state)) {
                // 查询手机号码是否已经预约
                int count = actUserInfoDao.queryUserInfoByMobileCount(activityId, mobile);
                if (count > 0) {
                    obj.put("returnCode", "2");
                    obj.put("returnMsg", "该号码已被使用");
                    return obj;
                }
                ActivityUserInfoDto dto = new ActivityUserInfoDto();
                dto.setActivityId(activityId);
                dto.setOpenid(openId);
                dto.setMobile(mobile);
                dto.setUserFullName(name);
                dto.setItemValue(state);
                dto.setItemKey("1");
                dto.setUserinfoId(UUID.randomUUID().toString().replace("-", ""));
                prepareNewActDto(dto);
                actUserInfoDao.saveUserInfo(dto);
                obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
                obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            } else {
                obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("调用用户预约信息方法>>>saveEnrolmentUser>>>时捕获异常{}:", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用用户预约信息方法>>>saveEnrolmentUser>>>end>>>出参{" + obj + "}");
        return obj;
    }

    @Override
    public JSONObject sendMsg(String mobile, String msgType) {
        logger.info("调用给客户推送短信方法>>>sendMsg>>>start>>>入参{mobile:" + mobile + ",msgType" + msgType + "}");
        JSONObject obj = new JSONObject();
        try {
            if (!StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(msgType)) {
                BusinessTypeDto businessType = messageServiceClient.queryBusinessType(msgType);
                if (null != businessType) {
                    MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
                    msgRecord.setMobile(mobile);
                    msgRecord.setContent(businessType.getMsgTemplate());
                    msgRecord.setMsgType(msgType);
                    msgRecord.setMsgTypeDesc(businessType.getBnsTypeDesc());
                    msgRecord.setMethod("A");
                    msgRecord.setSendUser("system");
                    MsgResult<?> msgResult = messageServiceClient.sendMarketMsg(msgRecord);
                    if (null != msgResult) {
                        obj.put("returnCode", msgResult);
                    } else {
                        obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
                        obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
                    }
                } else {
                    obj.put("returnCode", "763");
                    obj.put("returnMsg", "查不到相对应的消息模板");
                }
            } else {
                obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("调用给客户推送短信方法>>>saveEnrolmentUser>>>时捕获异常{}:", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用给客户推送短信方法>>>sendMsg>>>end>>>出参{mobile:" + mobile + ",msgType" + msgType + "}");
        return obj;
    }

    @Override
    public JSONObject saveUserLotteryNum(String activityId, String openId, String mobile, String name) {
        logger.info("调用用户抽奖信息方法>>>saveUserLotteryNum>>>start>>>入参{activityId:" + activityId + ",openId" + openId + ",mobile:" + mobile + ",name:" + name);
        JSONObject obj = new JSONObject();
        try {
            if (StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(activityId) && StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(name)) {
                // 查询手机号码是否已经抽奖了
                int count = actUserInfoDao.queryUserInfoByMobileAndItemKeyCount(activityId, mobile, 2);
                if (count > 0) {
                    obj.put("returnCode", "2");
                    obj.put("returnMsg", "该号码已获取抽奖号码了");
                    return obj;
                }
                // 查询ItemKey值为2的所有数据的条数
                int totalCount = actUserInfoDao.queryUserInfoByMobileAndItemKeyCount(activityId, "", 2);
                ActivityUserInfoDto dto = new ActivityUserInfoDto();
                dto.setActivityId(activityId);
                dto.setOpenid(openId);
                dto.setMobile(mobile);
                dto.setUserFullName(name);
                dto.setItemValue(String.valueOf(totalCount + 1));
                dto.setItemKey("2");
                dto.setUserinfoId(UUID.randomUUID().toString().replace("-", ""));
                prepareNewActDto(dto);
                actUserInfoDao.saveUserInfo(dto);

                // 设置模板消息内容
                ParameterDto paramDto = new ParameterDto();
                paramDto.setPmst("SYSTEM");
                paramDto.setPmky("TEMPLATE_04");
                List<ParameterDto> paramList = actParameterDao.getParameter(paramDto);
                String data = "";
                for (ParameterDto d : paramList) {
                    if (d.getPmco().contains("TEMPDATA")) {
                        data = d.getPmnm();
                        data = data.replace("{#1}", mobile).replace("{#2}", String.valueOf(totalCount + 1));
                        logger.info("读取配置对应的模板消息data: " + data);
                    }
                }
                // 发送模板消息
                logger.info("发送蔚来汽车抽奖号的模板消息=============开始");
                messageManager.sendTemplateMsg(openId, paramList, data);
                logger.info("发送蔚来汽车抽奖号的模板消息=============结束");
                // 发送邮件
                String[] titles = { "用户名称", "用户openid", "手机号", "用户抽奖码", "获取抽奖码时间" };
                String[] targetFields = { "userFullName", "openid", "mobile", "itemValue", "createdTime" };
                logger.info("发送蔚来汽车用户抽奖信息邮件=============开始");
                // 查询数据
                List<ActivityUserInfoDto> activityUserInfoList = actUserInfoDao.queryUserInfoByCondition(activityId, "", 2);
                mailManager.sendMail(activityUserInfoList, "29", titles, targetFields);
                logger.info("发送蔚来汽车用户抽奖信息邮件=============结束");
                obj.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
                obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            } else {
                obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
                obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            }
        } catch (Exception e) {
            logger.error("调用用户抽奖信息方法>>>saveUserLotteryNum>>>时捕获异常{}:", e);
            obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用用户抽奖信息方法>>>saveUserLotteryNum>>>end>>>出参{" + obj + "}");
        return obj;
    }
}