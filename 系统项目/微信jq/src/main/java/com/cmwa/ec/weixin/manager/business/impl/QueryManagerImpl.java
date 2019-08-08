package com.cmwa.ec.weixin.manager.business.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.query.facade.dto.account.AgentFundDto;
import com.cmwa.ec.query.facade.dto.account.BalanceBillDto;
import com.cmwa.ec.query.facade.dto.account.TotalBillInfoDto;
import com.cmwa.ec.query.facade.dto.account.TradeAcctDto;
import com.cmwa.ec.query.facade.dto.account.TradeAcctInfoDto;
import com.cmwa.ec.query.facade.dto.channel.ChannelInfoDto;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.FeeRateDto;
import com.cmwa.ec.query.facade.dto.fund.FundContractDto;
import com.cmwa.ec.query.facade.dto.fund.FundElementDto;
import com.cmwa.ec.query.facade.dto.fund.FundInfoDtoV2;
import com.cmwa.ec.query.facade.dto.fund.FundReportsDto;
import com.cmwa.ec.query.facade.dto.fund.ProductEstimate;
import com.cmwa.ec.query.facade.dto.fund.ProductProfit;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.query.facade.dto.trade.RedemmBalanceDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoStatusDto;
import com.cmwa.ec.query.facade.dto.user.QualifiedUserInfoDto;
import com.cmwa.ec.query.facade.dto.user.RiskTermsDto;
import com.cmwa.ec.query.facade.dto.user.UserTermsDto;
import com.cmwa.ec.query.facade.dto.web.AdvertDto;
import com.cmwa.ec.query.facade.dto.web.QuestionDto;
import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.TradeServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.CustServiceInfoDao;
import com.cmwa.ec.weixin.dao.UserInfoDao;
import com.cmwa.ec.weixin.dto.CustomerServiceDto;
import com.cmwa.ec.weixin.dto.CustomerServiceRespDto;
import com.cmwa.ec.weixin.manager.business.QueryManager;
import com.cmwa.ec.weixin.util.FileUtil;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.WeixinUtil;

/**
 * 查询模块
 * 
 * @author liury
 * @Time 2014-12-2
 */

public class QueryManagerImpl implements QueryManager {

    private static Logger logger = Logger.getLogger(QueryManagerImpl.class.getName());

    @Autowired
    private QueryServiceClient queryServiceClient;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private TradeServiceClient tradeServiceClient;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private CustServiceInfoDao custServiceInfoDao;

    public UserServiceMessage queryUserInfoByCmfUserId(Context context, String cmfUserId) {
        UserServiceMessage userServiceMessage = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
        return userServiceMessage;
    }

    /**
     * 查询产品列表
     */
    @Override
    public JSONObject queryFundList(Context context) {
        JSONObject resultJson = new JSONObject();
        List<FundInfoDtoV2> list = null;
        String returnCode = null;
        String returnMsg = null;
        QueryMessageDto messageDto = queryServiceClient.queryFundList(context);
        if (messageDto != null) {
            returnCode = messageDto.getResultCode();
            returnMsg = messageDto.getResultMsg();
            logger.info("查询产品列表，returnCode=" + returnCode);
            if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode)) {
                list = (List<FundInfoDtoV2>) messageDto.getData();
                String currentWorkdate = "";
                if (list != null && list.size() > 0) {// 获取当前工作日
                    currentWorkdate = list.get(0).getCurrentWorkdate();
                } else {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    currentWorkdate = df.format(new Date());
                }
                List<FundInfoDtoV2> tempList = new ArrayList<FundInfoDtoV2>();
                List<FundInfoDtoV2> tempListNo = new ArrayList<FundInfoDtoV2>();
                for (int i = 0; i < list.size(); i++) {// 取出所有的在售期的产品
                    if (StringUtils.isEmpty(list.get(i).getDisplayLimit()) || "0".equals(list.get(i).getDisplayLimit())) {// 过滤掉线上展示额度为0的产品
                        continue;
                    }
                    if (WXConstants.FUND_TYPE_0500.equals(list.get(i).getTypeId())) {// 活期理财产品不走传统方法
                        tempList.add(list.get(i));
                        continue;
                    }

                    if (Integer.parseInt(list.get(i).getSubdeadLine().replaceAll("-", "")) >= Integer.parseInt(currentWorkdate.replaceAll("-", ""))
                            && Long.parseLong(list.get(i).getDisplayLimit()) > 0) {
                        tempList.add(list.get(i));
                    } else {
                        tempListNo.add(list.get(i));
                    }
                }
                Map<String, JSONArray> map = new LinkedHashMap<String, JSONArray>();
                if (tempList != null && tempList.size() > 0) {
                    logger.info("查询产品列表，tempList.size()=" + tempList.size());
                    for (FundInfoDtoV2 fundInfoDto : tempList) {
                        String gruopId = "gruopId" + fundInfoDto.getGroupId();
                        if (!map.containsKey(gruopId)) {
                            JSONArray jsonArray1 = new JSONArray();
                            jsonArray1.add(JSONObject.fromObject(fundInfoDto));
                            map.put(gruopId, jsonArray1);
                        } else {
                            JSONArray jsonArray2 = map.get(gruopId);
                            if (jsonArray2.size() < 5) {
                                jsonArray2.add(JSONObject.fromObject(fundInfoDto));
                            }
                        }
                    }
                }
                if (tempListNo != null && tempListNo.size() > 0) {
                    logger.info("查询产品列表，tempListNo.size()=" + tempListNo.size());
                    for (FundInfoDtoV2 fundInfoDto : tempListNo) {
                        String gruopId = "gruopId" + fundInfoDto.getGroupId();
                        if (!map.containsKey(gruopId)) {
                            JSONArray jsonArray1 = new JSONArray();
                            jsonArray1.add(JSONObject.fromObject(fundInfoDto));
                            map.put(gruopId, jsonArray1);
                        } else {
                            JSONArray jsonArray2 = map.get(gruopId);
                            if (jsonArray2.size() < 5) {
                                jsonArray2.add(JSONObject.fromObject(fundInfoDto));
                            }
                        }
                    }
                }
                resultJson.put("list", map);
            }
        } else {
            returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
            returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
        }

        resultJson.put("returnCode", returnCode);
        resultJson.put("returnMsg", returnMsg);

        return resultJson;
    }

    /**
     * 查询热销产品列表
     */
    @Override
    public JSONObject queryHotFundList(Context context) {
        JSONObject resultJson = new JSONObject();
        List<FundInfoDtoV2> list = null;
        String returnCode = null;
        String returnMsg = null;
        QueryMessageDto messageDto = queryServiceClient.queryHotFundList(context);

        if (messageDto != null) {
            returnCode = messageDto.getResultCode();
            returnMsg = messageDto.getResultMsg();
            logger.info("查询产品列表，returnCode=" + returnCode);
            if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode)) {
                list = (List<FundInfoDtoV2>) messageDto.getData();
                resultJson.put("list", list);
            }
        } else {
            returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
            returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
        }
        resultJson.put("returnCode", returnCode);
        resultJson.put("returnMsg", returnMsg);
        return resultJson;
    }

    /**
     * 查询产品详情
     */
    @Override
    public JSONObject queryFundInfo(Context context, String fundId, String period) {

        JSONObject resultJson = new JSONObject();
        List<QuestionDto> quesList = null;
        String returnCode = null;
        String returnMsg = null;
        if (period == null || "".equals(period)) {
            period = "1";
        }
        QueryMessageDto messageDto = queryServiceClient.queryFund(context, fundId, Integer.parseInt(period));
        FundInfoDtoV2 fundInfoDto = null;
        byte[] pic = null;
        String content = null;
        if (messageDto != null) {
            fundInfoDto = (FundInfoDtoV2) messageDto.getData();
            returnCode = messageDto.getResultCode();
            returnMsg = messageDto.getResultMsg();
            logger.info("查询产品详情，returnCode=" + returnCode);
            if (fundInfoDto != null) {
                List<FundElementDto> elementList = fundInfoDto.getElementList();
                if (elementList != null && elementList.size() > 0) {
                    for (FundElementDto fundElementDto : elementList) {
                        if (fundElementDto.getType() == 240) {
                            pic = fundElementDto.getPic();// 文件二进制
                            content = fundElementDto.getContent();// 文件名称

                            String preUrl = SpringUtil.getProperty("wxFundElePicPre");
                            String url = SpringUtil.getProperty("wxFundElePic");
                            if (!FileUtil.checkHasFile(preUrl + url, content)) {// 将二进制数据写入到指定路径中
                                FileUtil.writeFile(preUrl + url, content, pic);
                                fundElementDto.setPicUrl(url + content);
                                fundElementDto.setPic(null);
                            } else {
                                fundElementDto.setPicUrl(url + content);
                                fundElementDto.setPic(null);
                            }

                            continue;
                        }
                    }
                }
            }
            if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode)) {
                quesList = (List<QuestionDto>) messageDto.getOtherData();
                if (fundInfoDto != null) {
                    logger.info("查询产品详情，fundInfo：" + fundInfoDto.toString());
                    resultJson.put("fundInfo", fundInfoDto);
                }
                if (quesList != null && quesList.size() > 0) {
                    logger.info("查询产品详情，questionDto.size()：" + quesList.size());
                    resultJson.put("quesList", quesList);
                }
            }
        } else {
            returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
            returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
        }

        resultJson.put("returnCode", returnCode);
        resultJson.put("returnMsg", returnMsg);

        return resultJson;
    }

    /**
     * 查询费率
     * 
     * @param context
     * @param fundId
     *            产品ID，不可为空
     * @param channelNo
     *            用户购买的渠道号，如果还没有获取，可以传空串
     * @param custLevel
     *            客户等级，目前全部传空串
     * @return QueryMessageDto.data = Map<ChannelNo, List<FeeRateDto>>
     */
    @Override
    public JSONObject queryFeeRateList(HttpServletRequest request, String money, Context context, String fundId, List<String> channelNoList, String custLevel) {

        JSONObject resultJson = new JSONObject();
        String returnCode = null;
        String returnMsg = null;
        if (fundId == null || "".equals(fundId)) { // 关键值为空
            returnCode = WXConstants.COMMON_ERROR_PARAMISNULLCODE;
            returnMsg = WXConstants.COMMON_ERROR_PARAMISNULLMSG;
            resultJson.put("returnCode", returnCode);
            resultJson.put("returnCode", returnMsg);
            return resultJson;
        }

        Object sessionQueryFeeRateMap = request.getSession(true).getAttribute(SessionValue.SESSION_QUERYFEERATEMAP);
        Object sessionfundId = request.getSession(true).getAttribute(SessionValue.SESSION_SESSIONFUNDID);
        String fundIdSession = "";
        Map<String, List<FeeRateDto>> map = new HashMap<String, List<FeeRateDto>>();
        // session中存在值
        if (sessionQueryFeeRateMap != null && sessionfundId != null) {

            returnCode = "0000";
            returnMsg = "成功";

            fundIdSession = (String) sessionfundId;
            if (fundIdSession.equals(fundId)) {// 同一个fundId则不去查询数据库
                map = (Map<String, List<FeeRateDto>>) sessionQueryFeeRateMap;
            } else {
                // 调用查询费率
                QueryMessageDto messageDto = queryServiceClient.queryFeeRateList(context, fundId, channelNoList, custLevel);
                returnCode = messageDto.getResultCode();
                returnMsg = messageDto.getResultMsg();
                if (messageDto != null) {
                    map = (Map<String, List<FeeRateDto>>) messageDto.getData();
                    request.getSession(true).setAttribute(SessionValue.SESSION_QUERYFEERATEMAP, map);
                    request.getSession(true).setAttribute(SessionValue.SESSION_SESSIONFUNDID, fundId);
                }
            }
        } else {// session中不存在值
                // 调用查询费率
            QueryMessageDto messageDto = queryServiceClient.queryFeeRateList(context, fundId, channelNoList, custLevel);
            if (messageDto != null) {
                returnCode = messageDto.getResultCode();
                returnMsg = messageDto.getResultMsg();
                if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode)) {
                    map = (Map<String, List<FeeRateDto>>) messageDto.getData();
                    request.getSession(true).setAttribute(SessionValue.SESSION_QUERYFEERATEMAP, map);
                    request.getSession(true).setAttribute(SessionValue.SESSION_SESSIONFUNDID, fundId);
                }
            } else {
                returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
                returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
            }
        }

        double rateValue = 0.0;
        double commro = 1.0;
        double feeMode = 0.0;
        if (map != null) {
            // rateValue = queryFeeRate(map, fundId, channelNoList, money);
            Map<String, Double> mapData = queryFeeRate(map, fundId, channelNoList, money);
            if (mapData != null) {
                rateValue = mapData.get("rateData");
                commro = mapData.get("commroData");
                feeMode = mapData.get("feeModeData");
            }
        }
        resultJson.put("returnCode", returnCode);
        resultJson.put("returnMsg", returnMsg);
        resultJson.put("rate", rateValue);
        resultJson.put("commro", commro);
        resultJson.put("feeMode", feeMode);
        logger.info("QueryController类【queryFeeRateList】结束：>>>resultJson=" + "returnCode>>>" + returnCode + ",returnMsg>>>" + returnMsg + ",rate>>>" + rateValue + ",commro>>>"
                + commro);
        return resultJson;
    }

    /**
     * 计算费率
     * 
     * @param map
     * @param fundId
     * @param channelNoList
     * @param money
     * @return
     */
    public Map<String, Double> queryFeeRate(Map<String, List<FeeRateDto>> map, String fundId, List<String> channelNoList, String money) {
        double rate = 0.0;
        Map<String, Double> returnData = null;
        double dMoney = Double.parseDouble(money);
        String channelNo = "";
        if (channelNoList != null && channelNoList.size() > 0) {
            channelNo = channelNoList.get(0);
        } else {
            channelNo = "*";
        }

        List<FeeRateDto> list = map.get(channelNo);// 获取一种渠道号
        FeeRateDto dto = null;
        for (int i = 0; i < list.size(); i++) {
            dto = list.get(i);
            if (fundId.equals(dto.getFundId()) && channelNo.equals(dto.getChannelNo()) && dMoney >= dto.getStrAmt() && dMoney < dto.getEndAmt()) {
                returnData = new HashMap<String, Double>();
                if (dto.getFeeMode() == '0') {// 费率类型 0：按rate比例收费；
                    rate = dMoney * dto.getBaseRate() * dto.getCommro(); // 折扣，
                                                                         // 产品认购费
                                                                         // =
                                                                         // 认购金额
                                                                         // *认购费用*折扣
                                                                         // 或
                                                                         // 单次费用*折扣
                } else if (dto.getFeeMode() == '1') {// 费率类型 1 按次收费，每次收rate元；
                    rate = dto.getSingleFee() * dto.getCommro();// TODO 暂时 按次收费
                                                                // 没有折扣
                }
                returnData.put("rateData", rate);
                returnData.put("commroData", dto.getCommro());// 折扣
                returnData.put("feeModeData", Double.parseDouble("" + dto.getFeeMode()));// 折扣类型
                break; // 一个金额只有一种区间,匹配了就没有必要循环了
            }
        }

        return returnData;
    }

    /**
     * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同
     * 
     * @param context
     * @param fundId
     * @return returnCode:0000---成功, 9000---关键参数为空,9999---系统错误 <br>
     *         date: 2016-3-16
     */
    public JSONObject queryFundContractById(Context context, String fundId, String type, String period) {

        String returnCode = "";
        String returnMsg = "";
        FundContractDto fundContractDto = null;
        JSONObject resultJson = new JSONObject();
        if (fundId == null || "".equals(fundId)) { // 关键值为空
            returnCode = WXConstants.COMMON_ERROR_PARAMISNULLCODE;
            returnMsg = WXConstants.COMMON_ERROR_PARAMISNULLMSG;
            resultJson.put("returnCode", returnCode);
            resultJson.put("returnMsg", returnMsg);
            return resultJson;
        }
        QueryMessageDto queryMessageDto = queryServiceClient.queryFundContractByIdWithPeirod(context, fundId, period);
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode)) {
                fundContractDto = (FundContractDto) queryMessageDto.getData();
            }
        } else {
            returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
            returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
        }
        if (null != fundContractDto && type.equals("L")) {// 由于内容过大,支付页面可以不用展示详情,则不需要传到页面
            fundContractDto.setTemplateCont1("");
            fundContractDto.setTemplateCont2("");
            fundContractDto.setTemplateCont3("");
            fundContractDto.setTemplateCont4("");
            fundContractDto.setTemplateTitle1("");
            fundContractDto.setTemplateTitle2("");
            fundContractDto.setTemplateTitle3("");
            fundContractDto.setTemplateTitle4("");
        }
        resultJson.put("returnCode", returnCode);
        resultJson.put("returnMsg", returnMsg);
        resultJson.put("fundContractDto", fundContractDto);
        logger.info("QueryController类【queryFundContractById】结束>>>resultCode=" + returnCode + ">>>resultMsg=" + returnMsg + ",fundContractDto=" + fundContractDto);
        return resultJson;
    }

    /**
     * 根据ecCustNo查询银行卡列表
     */
    @Override
    public JSONObject getTradeAcctInfoByCustno(Context context, String ecCustNo, boolean bl, String fundid) {

        JSONObject resultJson = new JSONObject();
        List<TradeAcctInfoDto> tradeAcctDtoList = null;
        String returnCode = null;
        String returnMsg = null;
        QueryMessageDto messageDto = queryServiceClient.getTradeAcctInfoByCustno(context, ecCustNo, fundid);
        if (messageDto != null) {
            returnCode = messageDto.getResultCode();
            returnMsg = messageDto.getResultMsg();
            logger.info("查询银行卡号列表，returnCode=" + returnCode);
            if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode)) {
                tradeAcctDtoList = (List<TradeAcctInfoDto>) messageDto.getData();
                if (bl) {
                    resultJson.put("tradeAcctlist", tradeAcctDtoList);
                } else {
                    List<TradeAcctInfoDto> encryptList = new ArrayList<TradeAcctInfoDto>();
                    for (TradeAcctInfoDto tradeAcctInfoDto : tradeAcctDtoList) {
                        tradeAcctInfoDto.setBankAccoDisplay(WeixinUtil.getEncryptBankCard(tradeAcctInfoDto.getBankAccoDisplay()));
                        tradeAcctInfoDto.setMobile(WeixinUtil.getEncryptMobile(tradeAcctInfoDto.getMobile()));
                        encryptList.add(tradeAcctInfoDto);
                    }
                    resultJson.put("tradeAcctlist", encryptList);
                }
            }
        } else {
            returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
            returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
        }
        resultJson.put("returnCode", returnCode);
        resultJson.put("returnMsg", returnMsg);
        return resultJson;
    }

    /**
     * /** 根据银行卡号查找该卡号的具体银行信息 即卡号校验
     * 
     * @param context
     * @param bankCardNo
     * @return
     */
    @Override
    public JSONObject queryBankInfoByBankNumber(Context context, String bankCardNo) {

        JSONObject returnJson = new JSONObject();
        String returnCode = null;
        String returnMsg = null;
        String listIsNull = "yes";
        String status = "N";
        ChannelInfoDto channelInfoDto = null;
        QueryMessageDto queryMessageDto = queryServiceClient.queryBankInfoByBankNumber(context, bankCardNo);

        if (queryMessageDto != null) {
            logger.info("银行卡号校验，查询卡号具体信息--returnCode：" + queryMessageDto.getResultCode() + ", returnMsg:" + queryMessageDto.getResultMsg());
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            if ("0000".equals(queryMessageDto.getResultCode())) {
                Object obj = queryMessageDto.getData();
                if (obj != null) {
                    List<ChannelInfoDto> list = (List<ChannelInfoDto>) obj;
                    // list为空，则说明银行卡号输入不正确
                    if (list.size() > 0) {
                        logger.info("list.size():" + list.size());
                        listIsNull = "no";
                        boolean bool = true;
                        ChannelInfoDto temp = null;
                        // 如果列表不为空，则判断list中dto的status，
                        // 如果有状态为Y的，则取第一个sataus为Y的dto，表示验证成功
                        // 如果没有状态为Y的，则表示是不支持的银行卡号
                        for (int i = 0; i < list.size() && bool; i++) {
                            temp = list.get(i);
                            if (temp != null && temp.getStatus().equals("Y")) {
                                status = "Y";
                                channelInfoDto = temp;
                                logger.info(channelInfoDto.toString());
                                bool = false;
                            }
                        }
                        if (status.equals("N")) {
                            returnMsg = "抱歉，暂不支持此银行绑卡业务";
                        }
                    } else {
                        returnMsg = "请您输入正确的银行卡号";
                    }
                } else {
                    returnMsg = "请您输入正确的银行卡号";
                }
            }
        } else {
            returnCode = "-1";
        }
        returnJson.put("status", status);
        returnJson.put("listIsNull", listIsNull);
        returnJson.put("returnCode", returnCode);
        returnJson.put("returnMsg", returnMsg);
        returnJson.put("bankInfoDto", channelInfoDto);
        logger.info("银行卡号校验，查询卡号具体信息--结果：" + returnJson.toString());
        return returnJson;
    }

    @Override
    public JSONObject queryCustTradeInfo(Context context, String cmfUserId, String fundCode, String tradeAcco) {
        logger.info("QueryManagerImpl类【queryCustTradeInfo】开始>>>cmfUserId:" + cmfUserId + ",fundCode:" + fundCode);
        QueryMessageDto queryMessageDto = queryServiceClient.queryCustTradeInfo(context, cmfUserId, fundCode, tradeAcco);
        JSONObject returnJsonObject = new JSONObject();
        BalanceBillDto dto = null;
        String returnCode = "";
        String returnMsg = "";
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            dto = (BalanceBillDto) queryMessageDto.getData();
        }
        if (null != dto && !"0".equals(dto.getBalance())) {
            returnJsonObject.put("buySatte", "Y");
        } else {
            returnJsonObject.put("buySatte", "N");
        }
        returnJsonObject.put("data", dto);
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);
        return returnJsonObject;
    }

    @Override
    public JSONObject queryNewCustomer(Context context, String cmfUserId, String fundCode) {
        logger.info("queryNewCustomer类【queryNewCustomer】开始>>>cmfUserId:" + cmfUserId + ",fundCode:" + fundCode);
        QueryMessageDto queryMessageDto = queryServiceClient.queryNewCustomer(context, cmfUserId, fundCode);
        JSONObject returnJsonObject = new JSONObject();
        Integer number = (Integer) queryMessageDto.getData();
        returnJsonObject.put("number", number);
        returnJsonObject.put("returnCode", queryMessageDto.getResultCode());
        returnJsonObject.put("returnMsg", queryMessageDto.getResultMsg());
        return returnJsonObject;
    }

    /**
     * 查询用户的总资产、总收益
     * 
     * @param context
     * @param cmfUserId
     * @param source
     *            报文来源 (weixin)
     * @return QueryMessageDto 返回码\返回信息 返回码如下 成功 0000 返回的实体为 TotalBillInfoDto
     */
    @Override
    public JSONObject queryTotalFundBalance(Context context, String cmfUserId, UserBaseInfoDto userBaseInfo, String sessionId) {

        QueryMessageDto totalFundBalanceMessage = null;
        TotalBillInfoDto totalBillInfoDto = null;
        JSONObject returnJsonObject = null;
        JSONObject jsonAsstes_InCome = null;
        String totalFundBalanceCode = null;
        String totalFundBalanceMsg = null;

        returnJsonObject = new JSONObject();
        jsonAsstes_InCome = new JSONObject();

        String userName = null;
        String mobile = null;
        String idNo = null;
        String userType = null;
        if (null != userBaseInfo) {
            userName = userBaseInfo.getCustName();
            String temp = "";
            if (userName != null && !userName.equals("")) {
                for (int i = 0; i < userName.length() - 1; i++) {
                    temp += "*";
                }
            }
            userName = userName == null || userName.equals("") ? "" : userName.charAt(0) + temp;
            mobile = userBaseInfo.getMobile();
            idNo = userBaseInfo.getIdNo();
            userType = userBaseInfo.getUserType();
        }
        logger.info("我的账单页面>>>>用户名userName：" + userName + " ,  手机号mobile:" + mobile + "  ,  证件号码idNo:" + idNo + "  ,  用户类型userType:" + userType);
        if (userName == null || userName.equals("") || idNo == null || idNo.equals("")) {
            userName = "您好！";
        } else {
            if (Integer.parseInt(idNo.substring(16, 17)) % 2 == 0) {
                userName = "您好！" + userName + "女士";
            } else {
                userName = "您好！" + userName + "先生";
            }
        }
        StringBuffer sbfu = new StringBuffer();
        if (null != mobile && !("").equals(mobile)) {
            sbfu.append(mobile.substring(0, 3));
            sbfu.append("****");
            sbfu.append(mobile.substring(mobile.length() - 4, mobile.length()));
        } else {
            sbfu.append("***********");
        }

        // 调用 --查询用户的总资产、总收益-- 接口
        logger.info("调用 ---查询用户的总资产、总收益--start ---  接口");
        totalFundBalanceMessage = queryServiceClient.queryTotalFundBalance(context, cmfUserId);
        logger.info("调用 ---查询用户的总资产、总收益--end ---  接口");
        logger.info("queryTotalFundBalance()方法：resultCode:" + totalFundBalanceMessage == null ? "totalFundBalanceMessage is null" : totalFundBalanceMessage.getResultCode());

        totalFundBalanceCode = "0000";
        totalFundBalanceMsg = "success";

        totalBillInfoDto = (TotalBillInfoDto) totalFundBalanceMessage.getData();
        // 0000 接口调用成功
        if ("0000".equals(totalFundBalanceMessage.getResultCode()) && totalBillInfoDto != null) {
            logger.info("queryTotalFundBalance()方法：总资产:" + totalBillInfoDto.getTotalbill());
            logger.info("queryTotalFundBalance()方法：总收益:" + totalBillInfoDto.getTotalprofit());
            String asstes = totalBillInfoDto.getTotalbill();
            String inCome = totalBillInfoDto.getTotalprofit();
            String totalValue = totalBillInfoDto.getTotalValue();
            if (null == asstes || "".equals(asstes) || null == inCome || "".equals(inCome)) {
                jsonAsstes_InCome.put("asstes", "0.00");
                jsonAsstes_InCome.put("inCome", "0.00");
                jsonAsstes_InCome.put("totalValue", "0.00");
            } else {
                NumberFormat numberFormat = new DecimalFormat(",###.##");
                jsonAsstes_InCome.put("asstes", numberFormat.format(new BigDecimal(asstes)));
                jsonAsstes_InCome.put("inCome", numberFormat.format(new BigDecimal(inCome)));
                jsonAsstes_InCome.put("totalValue", numberFormat.format(new BigDecimal(totalValue)));
            }
        } else {
            jsonAsstes_InCome.put("asstes", "0.00");
            jsonAsstes_InCome.put("inCome", "0.00");
            jsonAsstes_InCome.put("totalValue", "0.00");
            totalFundBalanceCode = totalFundBalanceMessage.getResultCode();
            totalFundBalanceMsg = totalFundBalanceMessage.getResultMsg();
        }

        jsonAsstes_InCome.put("userName", userName);
        jsonAsstes_InCome.put("mobile", sbfu.toString());
        jsonAsstes_InCome.put("idNo", WeixinUtil.getEncryptIdNo(idNo));
        jsonAsstes_InCome.put("userType", userType);
        // 总资产和总收益 结果
        returnJsonObject.put("totalFundBalanceCode", totalFundBalanceCode);
        returnJsonObject.put("totalFundBalanceMsg", totalFundBalanceMsg);
        returnJsonObject.put("asstesInCome", jsonAsstes_InCome);

        return returnJsonObject;
    }

    /**
     * 查询支持的银行卡列表
     */
    @Override
    public JSONObject getSupportBankDesc(Context context) {
        String returnCode = null;
        String returnMsg = null;
        JSONObject returnObject = new JSONObject();

        QueryMessageDto message = queryServiceClient.getSupportBankDesc(context);

        logger.info("调用 ---查询支持的银行卡列表--end ---  接口");
        String bankListStr = null;
        if (message != null) {
            returnCode = message.getResultCode();
            returnMsg = message.getResultMsg();
            logger.info("returnCode:" + returnCode + ",  returnMsg:" + returnMsg);

            if ("0000".equals(returnCode)) {
                bankListStr = (String) message.getData();
            }
        } else {
            returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
            returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
        }

        returnObject.put("returnCode", returnCode);
        returnObject.put("returnMsg", returnMsg);
        returnObject.put("bankListStr", bankListStr);

        logger.info("--查询支持的银行卡列表,结果：" + returnObject.toString());
        return returnObject;
    }

    @Override
    public QueryMessageDto queryTradeInfoList(Context context, String custno, String fundid, String applyst) {

        QueryMessageDto queryMessageDto = queryServiceClient.queryTradeInfoList(context, custno, fundid, applyst);
        return queryMessageDto;
    }

    @Override
    public JSONObject queryUnReadFundReportsCount(Context context, String cmfUserId) {
        QueryMessageDto message = queryServiceClient.queryUnReadFundReportsCount(context, cmfUserId);

        Integer count = 0;
        String returnCode = "";
        String returnMsg = "";
        JSONObject countJson = new JSONObject();

        if (message != null) {
            returnCode = message.getResultCode();
            returnMsg = message.getResultMsg();
            count = (Integer) message.getData();
        }
        countJson.put("returnCode", returnCode);
        countJson.put("returnMsg", returnMsg);
        countJson.put("count", count);
        return countJson;
    }

    @Override
    public QueryMessageDto queryUserMessageList(Context context, String cmfUserId, int beginIdx, int amount, int totalAmount) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessageList(context, cmfUserId, beginIdx, amount, totalAmount);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryUserMessage(Context context, String cmfUserId, String msgId, String msgType) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessage(context, cmfUserId, msgId, msgType);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryUserMessageByPDF(Context context, String cmfUserId, String msgId, String msgType) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessageByPDF(context, cmfUserId, msgId, msgType);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryFundTradeInfo(Context context, String cmfUserId, String startDate, String endDate) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryFundTradeInfo(context, cmfUserId, startDate, endDate);
        return queryMessageDto;
    }

    @Override
    public JSONObject queryTradeInfoByTradeNo(Context context, HttpServletRequest request) {
        String serialno = request.getParameter("serialno");
        String period = request.getParameter("period");
        logger.debug("【QueryController】queryTradeInfoByTradeNo()开始>>>serialno=" + serialno + ">>>timestamp=" + System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        String returnCode = "";
        String returnMsg = "";
        if (serialno == null || serialno.equals("")) {
            logger.error("【QueryController】queryTradeInfoByTradeNo()开始>>>serialno=" + serialno + ">>>timestamp=" + System.currentTimeMillis());
            jsonObject.put("returnCode", "9000");
            return jsonObject;
        }

        UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
        String custno = "";

        if (userAccoRlaDto != null) {
            custno = userAccoRlaDto.getEcCustNo();
        }
        QueryMessageDto queryMessageDto = null;
        if (period != null && !"".equals(period)) {

            queryMessageDto = queryServiceClient.queryTradeInfoByTradeNo(context, custno, serialno, Integer.parseInt(period) + 1);
        } else {
            queryMessageDto = queryServiceClient.queryTradeInfoByTradeNo(context, custno, serialno, 1);
        }
        AppointRequestDto appointRequestDto = new AppointRequestDto();

        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            // 返回的data信息如果为[]空信息的话，把对象置空
            appointRequestDto = (null == queryMessageDto.getData()) ? null : (AppointRequestDto) queryMessageDto.getData();
        }
        QueryMessageDto tradeMessage = null;
        if (appointRequestDto != null) {
            // 根据tradeAcco获取交易账号
            jsonObject.put("dto", appointRequestDto);
            tradeMessage = getTradeAcctInfoByTradeAcco(context, appointRequestDto.getTradeacco());
        }
        List<TradeAcctDto> tradeList = null;
        List<TradeAcctDto> encryptTradeList = new ArrayList<TradeAcctDto>();
        if (tradeMessage != null) {
            tradeList = (List<TradeAcctDto>) tradeMessage.getData();
            for (TradeAcctDto tradeAcctDto : tradeList) {
                tradeAcctDto.setBankacco(WeixinUtil.getEncryptBankCard(tradeAcctDto.getBankacco()));
                tradeAcctDto.setBankaccodisplay(WeixinUtil.getEncryptBankCard(tradeAcctDto.getBankaccodisplay()));
                tradeAcctDto.setIdno(WeixinUtil.getEncryptIdNo(tradeAcctDto.getIdno()));
                tradeAcctDto.setMobile(WeixinUtil.getEncryptMobile(tradeAcctDto.getIdno()));
                tradeAcctDto.setBankacnm(WeixinUtil.getEncryptUserName(tradeAcctDto.getBankacnm()));
                encryptTradeList.add(tradeAcctDto);
            }
        }

        if (tradeList != null && tradeList.size() > 0) {
            jsonObject.put("tradeList", encryptTradeList);
        }

        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);

        logger.debug("【QueryController】queryTradeInfoByTradeNo()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>appointRequestDto=" + appointRequestDto
                + ">>>timestamp=" + System.currentTimeMillis());
        return jsonObject;
    }

    @Override
    public QueryMessageDto getTradeAcctInfoByTradeAcco(Context context, String tradeacco) {
        QueryMessageDto queryMessageDto = queryServiceClient.getTradeAcctInfoByTradeAcco(context, tradeacco);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryAgentFundInfo(Context context, String cmfUserId) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryAgentFundInfo(context, cmfUserId);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryCompanyArticle(Context context, String cmfUserId, int beginrow, int endrow) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryCompanyArticle(context, cmfUserId, beginrow, endrow);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryCompanyArticleCount(Context context, String cmfUserId) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryCompanyArticleCount(context, cmfUserId);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryArticleContent(Context context, String articleId) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryArticleContent(context, articleId);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryNewsArticle(Context context, String cmfUserId, int beginrow, int endrow) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryNewsArticle(context, cmfUserId, beginrow, endrow);
        return queryMessageDto;
    }

    @Override
    public QueryMessageDto queryNewsArticleCount(Context context, String cmfUserId) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryNewsArticleCount(context, cmfUserId);
        return queryMessageDto;
    }

    @Override
    public JSONObject queryOtherDetailOrder(Context context, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String returnCode = "";
        String returnMsg = "";
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String serialno = request.getParameter("serialno");
        String fundId = request.getParameter("fundId");
        String channleNo = request.getParameter("channleNo");
        if (serialno == null) {
            serialno = "";
        }
        if (fundId == null) {
            fundId = "";
        }
        if (channleNo == null) {
            channleNo = "";
        }
        logger.info("QueryManagerImpl类【queryOtherDetailOrder】开始>>>serialno:" + serialno + ">>>cmfUserId:" + cmfUserId + ">>>fundId:" + fundId + ">>>timestamp:"
                + System.currentTimeMillis());
        QueryMessageDto queryMessageDto = queryServiceClient.queryOtherDetailOrder(context, serialno, cmfUserId, fundId, channleNo);
        AgentFundDto agentFundDto = null;
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getReturnCode();
            returnMsg = queryMessageDto.getReturnMsg();
            agentFundDto = (AgentFundDto) queryMessageDto.getData();
        }
        if (agentFundDto != null) {
            jsonObject.put("agentFundDto", agentFundDto);
            QueryMessageDto fundDetail = queryFund(context, agentFundDto.getFundid());
            FundInfoDtoV2 fundInfoDtoV2 = null;
            List<QuestionDto> questionDtoList = null;
            if (fundDetail != null) {
                returnCode = queryMessageDto.getReturnCode();
                returnMsg = queryMessageDto.getReturnMsg();
                fundInfoDtoV2 = (FundInfoDtoV2) fundDetail.getData();

                questionDtoList = (List<QuestionDto>) fundDetail.getOtherData();// 产品相关问题
            }
            if (fundInfoDtoV2 != null) {
                jsonObject.put("fundInfoDtoV2", fundInfoDtoV2);
            }
            if (questionDtoList != null && questionDtoList.size() > 0) {
                jsonObject.put("questionDtoList", questionDtoList);
            }
        }
        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);
        return jsonObject;
    }

    @Override
    public QueryMessageDto queryFund(Context context, String fundId) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryFund(context, fundId);
        return queryMessageDto;
    }

    @Override
    public JSONObject queryBanner(Context context, String type) {
        JSONObject jsonObject = new JSONObject();
        QueryMessageDto queryMessageDto = queryServiceClient.queryBanner(context, type);
        List<AdvertDto> list = (List<AdvertDto>) queryMessageDto.getData();
        jsonObject.put("advertDtoList", list);
        jsonObject.put("returnCode", WXConstants.COMMON_SUCCESS);
        jsonObject.put("returnMsg", "查询成功");
        return jsonObject;
    }

    @Override
    public JSONObject queryEstimateByFundId(Context context, HttpServletRequest request) {
        JSONObject returnJsonObject = new JSONObject();
        String fundId = request.getParameter("fundId");
        String dateTimeStr = request.getParameter("dateTime");
        logger.info("QueryManagerImpl类【queryEstimateByFundId 】开始>>>fundId:" + fundId + ">>>timestamp:" + System.currentTimeMillis());
        Integer dateTime = 0;
        if (dateTimeStr != null && !dateTimeStr.equals("")) {
            dateTime = Integer.parseInt(dateTimeStr);
        }
        logger.info("QueryManagerImpl类【queryEstimateByFundId 】获取前台参数>>>fundId:" + fundId + ">>>dateTime=" + dateTime);
        String resultCode = "0000";
        String resultMsg = "成功";
        QueryMessageDto queryMessageDto = queryServiceClient.queryEstimateByFundId(context, fundId, dateTime);
        List<ProductEstimate> list = null;
        if (queryMessageDto != null) {
            list = (List<ProductEstimate>) queryMessageDto.getData();
            /*
             * for (int i = 0; i < list.size(); i++) {
             * list.get(i).setFluctuate(WeixinUtil.addAndDiv(i, list)); }
             */
        }
        queryMessageDto.setData(list);
        List<ProductEstimate> productEstimateList = new ArrayList<ProductEstimate>();
        resultCode = queryMessageDto.getResultCode();
        resultMsg = queryMessageDto.getResultMsg();
        if (queryMessageDto != null) {
            productEstimateList = (List<ProductEstimate>) queryMessageDto.getData();
        }
        returnJsonObject.put("resultCode", resultCode);
        returnJsonObject.put("resultMsg", resultMsg);
        returnJsonObject.put("productEstimateList", productEstimateList);
        logger.info("QueryManagerImpl类【queryEstimateByFundId 】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
        return returnJsonObject;
    }

    @Override
    public JSONObject queryEstimateByFundIdByPage(Context context, HttpServletRequest request) {
        String fundId = request.getParameter("fundId");
        String pages = request.getParameter("page");
        logger.info("QueryManagerImpl类【queryEstimateByFundIdByPage 】开始>>>fundId:" + fundId + ">>>timestamp:" + System.currentTimeMillis());
        Integer dateTime = 0;
        // int beginIdx = 0;// 每页展示的开始的条数
        int rowCount = Integer.parseInt(WXConstants.PAGE_LIST_NETVAL);// 每页展示的条数
        // int count = -1;// 总条数
        int maxPages = 1;// 总页数
        int page = 1;// 当前页
        if (pages != null && !pages.equals("")) {
            page = Integer.parseInt(pages);
        }
        logger.info("QueryManagerImpl类【queryEstimateByFundIdByPage 】获取前台参数>>>fundId:" + fundId + ">>>page=" + page);
        JSONObject returnJsonObject = new JSONObject();
        String resultCode = "0000";
        String resultMsg = "成功";
        QueryMessageDto queryMessageDto = queryServiceClient.queryEstimateByFundId(context, fundId, dateTime);
        List<ProductEstimate> list = null;
        if (queryMessageDto != null) {
            list = (List<ProductEstimate>) queryMessageDto.getData();
            /*
             * for (int i = 0; i < list.size(); i++) {
             * list.get(i).setFluctuate(WeixinUtil.addAndDiv(i, list)); }
             */
        }
        queryMessageDto.setData(list);
        List<ProductEstimate> newList = new ArrayList<ProductEstimate>();

        resultCode = queryMessageDto.getResultCode();
        resultMsg = queryMessageDto.getResultMsg();

        if (queryMessageDto != null) {
            list = (List<ProductEstimate>) queryMessageDto.getData();
        }
        if (list != null) {
            int i = 1;
            for (ProductEstimate dto : list) {
                if (dto == null)
                    break;
                if (rowCount * (page - 1) + 1 <= i && i <= rowCount * page) {// 取指定页数的数据
                    newList.add(dto);
                } else if (i > rowCount * page) {
                    break;
                }
                i++;
            }
            // 计算总页数
            maxPages = list.size() % rowCount == 0 ? list.size() / rowCount : list.size() / rowCount + 1;
            // count = productEstimateList.size();
        }
        returnJsonObject.put("resultCode", resultCode);
        returnJsonObject.put("resultMsg", resultMsg);
        returnJsonObject.put("page", page);
        returnJsonObject.put("maxPages", maxPages);
        returnJsonObject.put("productEstimates", newList);
        logger.info("QueryManagerImpl类【queryEstimateByFundIdByPage 】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
        return returnJsonObject;
    }

    @Override
    public QueryMessageDto queryUserTradeAcctInfo(Context context, String ecCustNo, String tradeNo, String fundid) {
        return queryServiceClient.queryUserTradeAcctInfo(context, ecCustNo, tradeNo, fundid);
    }

    @Override
    public QueryMessageDto queryParamList(Context context, String pmst, String pmky, String pmco, String pmv1) {
        QueryMessageDto queryMessageDto = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, pmst, pmky, pmco, pmv1);
        return queryMessageDto;
    }

    @Override
    public UserServiceMessage queryUserRiskEvalDateByCmfUserId(String cmfUserId) {
        return userServiceClient.queryUserRiskEvalDateByCmfUserId(cmfUserId);
    }

    /**
     * 根据消息标题模糊查询
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject queryUserMsgLikeTitle(Context context, String cmfUserId, String title) {
        logger.info("QueryManagerImpl类【queryUserMessageListLikeTitle】开始>>cmfUserId:" + cmfUserId + ">>>>timestamp:" + System.currentTimeMillis());
        QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessageList(context, cmfUserId, 0, 999, 999);
        List<FundReportsDto> list = new ArrayList<FundReportsDto>();
        String returnCode = "";
        String returnMsg = "";
        JSONObject jsonObject = new JSONObject();
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            list = (List<FundReportsDto>) queryMessageDto.getData();
        }
        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);
        String isShowTips = "N";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                FundReportsDto dto = list.get(i);
                if (dto.getFundLName().indexOf(title) != -1) {
                    isShowTips = "Y";
                    break;
                }
            }
        }
        jsonObject.put("isShowTips", isShowTips);
        logger.info("QueryManagerImpl类【queryUserMessageListLikeTitle】结束>>>timestamp:" + System.currentTimeMillis());
        return jsonObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.business.QueryManager#queryUserCustService
     * (java.lang.String)
     */
    @Override
    public JSONObject queryUserCustService(String openid) {
        JSONObject obj = new JSONObject();
        try {
            logger.info("通过openid : " + openid + "查询顾问id");
            String custserId = userInfoDao.queryCustserIdByOpenid(openid);
            logger.info("顾问id为:" + custserId);
            CustomerServiceRespDto response = new CustomerServiceRespDto();
            CustomerServiceDto result = !StringUtils.isBlank(custserId) ? custServiceInfoDao.queryServiceInfoById(custserId) : custServiceInfoDao.queryDefaultCustServiceInfo();
            if (result == null) {
                logger.info("根据用户绑定的id为空，查询默认顾问信息返回前台");
                result = custServiceInfoDao.queryDefaultCustServiceInfo();
            }
            String[] arr = { "custserPhoto", "custserPublicAccQrcode", "custserPersonalQrCode" };
            logger.info("最终查询结果顾问id为：" + result.getCustserId() + ",顾问名称为：" + result.getCustserName());
            BeanUtils.copyProperties(result, response, arr);
            response.setCustserPhoto(new String(result.getCustserPhoto()));
            response.setCustserPublicAccQrcode(new String(result.getCustserPublicAccQrcode()));
            response.setCustserPersonalQrCode(new String(result.getCustserPersonalQrCode()));
            obj.put("returnCode", "0000");
            obj.put("returnMsg", "查询成功");
            obj.put("data", response);
        } catch (Exception e) {
            logger.error("查询微信用户绑定的顾问信息时，捕获异常:" + e);
            obj.put("returnCode", "9999");
            obj.put("returnMsg", "系统内部错误");
        }
        return obj;
    }

    @Override
    public JSONObject queryProfitByFundCode(Context context, String cmfUserId, String fundCode) {
        logger.info("QueryManagerImpl类【queryProfitByFundCode】开始>>>cmfUserId:" + cmfUserId + ",fundCode:" + fundCode);
        QueryMessageDto queryMessageDto = queryServiceClient.queryProfitByFundCode(context, cmfUserId, fundCode);
        JSONObject returnJsonObject = new JSONObject();
        ProductProfit dto = null;
        String returnCode = "";
        String returnMsg = "";
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            dto = (ProductProfit) queryMessageDto.getData();
        }
        returnJsonObject.put("data", dto);
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);
        return returnJsonObject;
    }

    @Override
    public JSONObject queryTradeInfoByCustNo(Context context, String custno, String fundid, String subquty) {
        JSONObject json = new JSONObject();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            logger.info("QueryManagerImpl类【queryTradeInfoByCustNo】开始>>>>>>timestamp:" + System.currentTimeMillis());
            if (StringUtils.isEmpty(subquty) || StringUtils.isEmpty(custno) || StringUtils.isEmpty(fundid)) {
                json.put("resultCode", "9999");
                json.put("resultMsg", "关键参数为空");
            }
            QueryMessageDto queryMessageDto = queryServiceClient.queryTradeInfoByCustNo(context, custno, fundid);
            if ("0000".equals(queryMessageDto.getResultCode())) {
                List<AppointRequestDto> resultDto = (List<AppointRequestDto>) queryMessageDto.getData();
                double sum = 0;
                for (int i = 0; i < resultDto.size(); i++) {
                    sum += Double.valueOf(resultDto.get(i).getSubquty());
                    stringBuilder.append(resultDto.get(i).getSerialno());
                    if (sum >= Double.valueOf(subquty.replace(",", "")).doubleValue()) {
                        break;
                    }
                    stringBuilder.append(",");
                }
                AppointRequestDto dto = resultDto.get(0);
                dto.setSerialno(stringBuilder.toString());
                json.put("data", dto);
            }
            json.put("resultCode", "0000");
            json.put("resultMsg", "成功！");
        } catch (Exception e) {
            logger.error("QueryManagerImpl类【queryTradeInfoByCustNo】异常>>>>>>异常信息:" + e);
            json.put("resultCode", "500");
            json.put("resultMsg", "系统异常");
        }
        return json;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmwa.ec.weixin.manager.business.QueryManager#
     * queryCanRedeemBankInfoByFundCode(java.lang.String, java.lang.String)
     */
    @Override
    public JSONObject queryCanRedeemBankInfoByFundCode(String fundid, String custno) {
        JSONObject json = new JSONObject();
        try {
            QueryMessageDto dto = queryServiceClient.queryCanRedeemBankInfoByFundCode(fundid, custno);
            logger.info("queryCanRedeemBankInfoByFundCode -> query服务返回值为：" + dto);
            if (dto != null && "0000".equals(dto.getErrCode())) {
                List<RedemmBalanceDto> data = (List<RedemmBalanceDto>) dto.getData();
                // 隐藏卡号
                if (data != null) {
                    for (RedemmBalanceDto content : data) {
                        content.setBankaccodisplay(WeixinUtil.getEncryptBankCard(content.getBankaccodisplay()));
                    }
                }
                json.put("data", dto.getData());
                json.put("resultCode", "0000");
                json.put("resultMsg", "成功！");
            } else {
                json.put("returnCode", "9999");
                json.put("returnMsg", "查询失败");
            }
        } catch (Exception e) {
            logger.error("queryCanRedeemBankInfoByFundCode -> 捕获到异常:" + e);
            json.put("resultCode", "9999");
            json.put("resultMsg", "系统异常");
        }
        return json;
    }

    @Override
    public List<ParameterDto> queryParameter(Context context, String pmst, String pmky, String pmco, String pmv1) {
        try {
            QueryMessageDto queryResult = queryServiceClient.queryParameter(context, pmst, pmky, pmco, pmv1);
            if (WXConstants.COMMON_SUCCESS.equals(queryResult.getReturnCode())) {
                // noinspection unchecked
                return (List<ParameterDto>) queryResult.getData();
            } else {
                logger.error("queryParameter_开关查询失败_errCode: " + queryResult.getReturnCode() + ", errMsg: " + queryResult.getReturnMsg());

            }

        } catch (Exception omg) {
            logger.error("queryParameter_开关查询异常_", omg);
        }
        return null;
    }

    @Override
    public CustserviceInfoDTO getConsultantInfo(String cmfUserId) {
        CustserviceInfoDTO custserviceInfoDTO = null;
        try {
            QueryMessageDto queryResult = queryServiceClient.getConsultantInfo(cmfUserId);
            if (WXConstants.COMMON_SUCCESS.equals(queryResult.getResultCode())) {
                custserviceInfoDTO = (CustserviceInfoDTO) queryResult.getData();
            } else {
                logger.error("getConsultantInfo_获取用户的专属顾问信息失败_errCode: " + queryResult.getReturnCode() + ", errMsg: " + queryResult.getReturnMsg());
            }
        } catch (Exception omg) {
            logger.error("getConsultantInfo_获取用户的专属顾问信息时异常_", omg);
        }

        return custserviceInfoDTO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.business.QueryManager#queryQualifiedUserInfoByIdno
     * (java.lang.String)
     */
    @Override
    public JSONObject queryQualifiedUserInfoByIdno(String idno) {
        JSONObject obj = new JSONObject();
        if (StringUtils.isBlank(idno)) {
            obj.put("returnCode", "9999");
            obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
        } else {
            try {
                logger.info("根据身份证号查询合格投资者信息开始. idno:" + idno);
                QueryMessageDto queryMessageDto = queryServiceClient.queryQualifiedUserInfoByIdno(idno);
                obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
                if (queryMessageDto != null) {
                    logger.info("调用query服务查询合格投资者信息成功，resultData:" + queryMessageDto);
                    if (queryMessageDto == null || !WXConstants.COMMON_SUCCESS.equals(queryMessageDto.getResultCode())) {
                        logger.info("query服务返回结果异常，不返回数据");
                        obj.put("returnMsg", "服务调用异常");
                    } else {
                        List<QualifiedUserInfoDto> result = (List<QualifiedUserInfoDto>) queryMessageDto.getData();
                        logger.info("根据idcard：" + idno + "查询到合格投资者信息为:" + result);
                        obj.put("returnCode", WXConstants.COMMON_SUCCESS);
                        obj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                        obj.put("data", result);
                    }
                } else {
                    logger.info("调用query服务查询合格投资者信息失败，返回值为空!");
                    obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
                }
            } catch (Exception e) {
                logger.error("查询合格投资者信息时捕获异常：", e);
                obj.put("returnMsg", "服务器内部异常");
            }
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmwa.ec.weixin.manager.business.QueryManager#queryFileUploadRecord
     * (java.lang.String)
     */
    @Override
    public JSONObject queryFileUploadRecord(String cmfUserId) {
        try {
            // 声明参数
            CmwaFileUploadRecordDto parameter = new CmwaFileUploadRecordDto();
            parameter.setCmfuserid(cmfUserId);
            List<CmwaFileUploadRecordDto> queryFileUploadRecord = tradeServiceClient.queryFileUploadRecord(parameter);
            JSONObject returnObj = new JSONObject();
            returnObj.put("returnCode", WXConstants.COMMON_SUCCESS);
            returnObj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            returnObj.put("data", queryFileUploadRecord);
            return returnObj;
        } catch (Exception e) {
            logger.error("查询文件上传记录时捕获异常:", e);
            JSONObject returnObj = new JSONObject();
            returnObj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
            return returnObj;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmwa.ec.weixin.manager.business.QueryManager#
     * queryAccreditedInvestorConditions(java.lang.String)
     */
    @Override
    public JSONObject queryAccreditedInvestorConditions(String cmfuserid, String custno) {
        JSONObject returnObj = new JSONObject();
        try {
            QueryMessageDto result = queryServiceClient.queryAccreditedInvestorConditions(custno);
            if (result == null || WXConstants.COMMON_ERROR_SYSERRCODE.equals(result.getResultCode())) {
                logger.info("调用query服务查询合格投资者条件失败，custno:" + custno);
                returnObj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
                returnObj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
            } else {
                logger.info("调用query服务查询合格投资者条件成功，custno:" + custno);
                returnObj.put("returnCode", WXConstants.COMMON_SUCCESS);
                returnObj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                returnObj.put("data", result.getOtherData());
                autoHandleData((Map<String, Object>) result.getOtherData(), custno, cmfuserid);
            }
        } catch (Exception e) {
            logger.error("调用query服务查询合格投资者条件捕获异常：", e);
            returnObj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return returnObj;
    }

    private void autoHandleData(Map<String, Object> otherData, String custno, String cmfuserid) {
        try {
            boolean financialCertificate = Boolean.valueOf(String.valueOf(otherData.get("financialCertificate")));
            boolean investCertificate = Boolean.valueOf(String.valueOf(otherData.get("investCertificate")));
            if (investCertificate && financialCertificate) {
                QueryMessageDto serviceResult = this.queryServiceClient.queryQualifiedUserInfoByCustno(custno);
                if (serviceResult != null && WXConstants.COMMON_SUCCESS.equals(serviceResult.getResultCode())) {
                    if (serviceResult.getData() == null) {
                        logger.info("判断到用户已经满足合格投资者条件，为用户添加一条成功的合格投资者申请， custno:" + custno);
                        CmwaQualifiedUserInfoDto innerRecord = new CmwaQualifiedUserInfoDto();
                        innerRecord.setInfoId(UUID.randomUUID().toString());
                        innerRecord.setCmfuserid(cmfuserid);
                        innerRecord.setCustno(custno);
                        innerRecord.setCreatedUser(this.getClass().getSimpleName());
                        innerRecord.setUpdatedUser(this.getClass().getSimpleName());
                        logger.info("为custno:" + custno + "的用户生成的添加参数为:" + innerRecord);
                        int addQualifiedUserInfo = this.tradeServiceClient.insertAccreditedInvestorInfo(getTradeQualifiedDto(innerRecord), "S");
                        logger.info("custno:" + custno + "用户的合格投资者申请记录返回结果为：" + addQualifiedUserInfo);
                    } else {
                        logger.info("判断到用户已经满足合格投资者条件，将用户当前合格投资者申请修改为成功， custno:" + custno);
                        CmwaQualifiedUserInfoStatusDto status = new CmwaQualifiedUserInfoStatusDto();
                        QualifiedUserInfoDto qualifiedUserInfoDto = (QualifiedUserInfoDto) serviceResult.getData();
                        status.setInfoId(qualifiedUserInfoDto.getInfoId());
                        int updateQualifiedUserInfoStatus = this.tradeServiceClient.updateQualifiedUserInfoStatus(getTradeQualifiedStatusDto(status), "S", this.getClass()
                                .getSimpleName());
                        logger.info("custno:" + custno + "用户的合格投资者申请记录返回结果为：" + updateQualifiedUserInfoStatus);
                    }
                } else {
                    logger.error("调用query服务失败，返回数据为：" + serviceResult);
                }
            }
        } catch (Exception e) {
            logger.error("自动添加或修改用户合格投资者申请状态时捕获异常：", e);
        }
    }

    private com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto getTradeQualifiedStatusDto(CmwaQualifiedUserInfoStatusDto source) throws IllegalAccessException,
            InvocationTargetException {
        com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto target = new com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto();
        BeanUtils.copyProperties(target, source);
        return target;
    }

    private com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto getTradeQualifiedDto(CmwaQualifiedUserInfoDto source) throws IllegalAccessException, InvocationTargetException {
        com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto target = new com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto();
        BeanUtils.copyProperties(target, source);
        return target;
    }

    @Override
    public JSONObject queryRiskTermList(Context context, String cmfUserId, String fundId, String period) {
        JSONObject returnObj = new JSONObject();
        try {
            QueryMessageDto result = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, WXConstants.PMST, WXConstants.RISK_TERMS, null, null);
            if (null != result && WXConstants.COMMON_SUCCESS.equals(result.getResultCode())) {
                logger.info("调用query服务查询风险揭示函成功");
                List<ParameterDto> list = (List<ParameterDto>) result.getData();
                if (null != list && !list.isEmpty()) {
                    List<RiskTermsDto> termsList = new ArrayList<RiskTermsDto>();
                    for (ParameterDto dto : list) {
                        RiskTermsDto term = new RiskTermsDto();
                        term.setId(dto.getPmnm());
                        term.setContent(dto.getPmco());
                        for (UserTermsDto d : queryUserTermList(cmfUserId, fundId, period)) {
                            if (dto.getPmnm().equals(d.getTermsId())) {
                                term.setState(d.getState());
                                break;
                            }
                        }
                        termsList.add(term);
                    }
                    Collections.sort(termsList, new Comparator<RiskTermsDto>() {
                        @Override
                        public int compare(RiskTermsDto arg0, RiskTermsDto arg1) {
                            return Integer.valueOf(arg0.getId()) - Integer.valueOf(arg1.getId());
                        }
                    });
                    returnObj.put("returnCode", WXConstants.COMMON_SUCCESS);
                    returnObj.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                    returnObj.put("data", termsList);
                } else {
                    logger.info("调用query服务查询结果异常，未查询到可用数据");
                    returnObj.put("returnCode", WXConstants.COMMON_ERROR_REDATAISNULLCODE);
                    returnObj.put("returnMsg", WXConstants.COMMON_ERROR_REDATAISNULLMSG);
                }
            } else {
                logger.info("调用query服务查询风险揭示函失败");
                returnObj.put("returnCode", result.getErrCode());
                returnObj.put("returnMsg", result.getErrMsg());
            }
        } catch (Exception e) {
            logger.error("查询风险揭示函异常：", e);
            returnObj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return returnObj;
    }

    public List<UserTermsDto> queryUserTermList(String cmfUserId, String fundId, String period) {
        try {
            QueryMessageDto result = queryServiceClient.queryUserTermList(null, cmfUserId, fundId, StringUtils.isEmpty(period) ? 1 : Integer.valueOf(period), "1");
            if (null != result && WXConstants.COMMON_SUCCESS.equals(result.getResultCode())) {
                logger.info("调用query服务查询用户风险揭示函勾选列表成功");
                return (List<UserTermsDto>) result.getData();
            } else {
                logger.info("调用query服务查询用户风险揭示函勾选列表失败");
                return null;
            }
        } catch (Exception e) {
            logger.error("调用query服务查询用户风险揭示函勾选列表失败异常：", e);
            return null;
        }
    }

    @Override
    public UserServiceMessage queryUserInfoByMobile(String mobile) {
        return userServiceClient.queryUserInfoByMobile(mobile);
    }
}
