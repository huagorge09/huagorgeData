package com.cmwa.ecc.business.entity.fundinfo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@Alias("productEstimateDto")
public class ProductEstimateDto {
    private Date dDate;

    private Date dCdate;

    private String cFundcode;

    private String cTodaystatus;

    private String cStatus;

    private BigDecimal fNetvalue;

    private BigDecimal fLastshares;

    private BigDecimal fLastasset;

    private BigDecimal fAsucceed;

    private BigDecimal fRsucceed;

    private String cVastflag;

    private BigDecimal fEncashratio;

    private BigDecimal fChangeratio;

    private String cExcessflag;

    private BigDecimal fSubscriberatio;

    private String cInputpersonnel;

    private String cCheckpersonnel;

    private BigDecimal fIncome;

    private BigDecimal fIncomeunit;

    private BigDecimal fIncomeratio;

    private BigDecimal fUnassign;

    private BigDecimal fTotalnetvalue;

    private BigDecimal fServicefare;

    private String cTano;

    private String cPublished;

    private BigDecimal fDynamicline;

    private BigDecimal fDayinc;

    private BigDecimal fWeekinc;

    private BigDecimal fDayincSms;

    private BigDecimal fMonthinc;

    private BigDecimal fNetvaluedayinc;

    private BigDecimal fIncinthreedays;

    private BigDecimal fIncomeinc;

    private BigDecimal fTotalincomeinc;

    private BigDecimal fReservedasset;

    private BigDecimal fRealnetvalue;

    private String cTainput;

    private String cFainput;

    private BigDecimal fSeasoninc;

    private BigDecimal fHalfinc;

    private BigDecimal fYearinc;

    private String cTodaychgstatus;

    private String cTodaypcsstatus;

    private String cTodaysplitstatus;

    private BigDecimal fFanetvalue;

    private String cPublishedTemp;

    private String cManualinput;

    private String cPcfinput;

    private String cTodaytrfstatus;

    private String cCheckflag;

    private String cIsvaluationday;

    private BigDecimal fIncomeratio30days;

    private BigDecimal fRmbnetvalue;

    private BigDecimal fRmbtotalnetvalue;

    private BigDecimal fThisYearinc;

    private BigDecimal f1Yearinc;

    private BigDecimal f2Yearinc;

    private BigDecimal f3Yearinc;

    private BigDecimal f5Yearinc;

    private BigDecimal fSetupinc;

    private BigDecimal fIncomeratio90days;

    private BigDecimal fIncomeratio45days;

    private Date dCreatedate;

    private Date dAuditdate;

    private BigDecimal fLasttotalasset;

    public Date getdDate() {
        return dDate;
    }

    public void setdDate(Date dDate) {
        this.dDate = dDate;
    }

    public Date getdCdate() {
        return dCdate;
    }

    public void setdCdate(Date dCdate) {
        this.dCdate = dCdate;
    }

    public String getcFundcode() {
        return cFundcode;
    }

    public void setcFundcode(String cFundcode) {
        this.cFundcode = cFundcode == null ? null : cFundcode.trim();
    }

    public String getcTodaystatus() {
        return cTodaystatus;
    }

    public void setcTodaystatus(String cTodaystatus) {
        this.cTodaystatus = cTodaystatus == null ? null : cTodaystatus.trim();
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus == null ? null : cStatus.trim();
    }

    public BigDecimal getfNetvalue() {
        return fNetvalue;
    }

    public void setfNetvalue(BigDecimal fNetvalue) {
        this.fNetvalue = fNetvalue;
    }

    public BigDecimal getfLastshares() {
        return fLastshares;
    }

    public void setfLastshares(BigDecimal fLastshares) {
        this.fLastshares = fLastshares;
    }

    public BigDecimal getfLastasset() {
        return fLastasset;
    }

    public void setfLastasset(BigDecimal fLastasset) {
        this.fLastasset = fLastasset;
    }

    public BigDecimal getfAsucceed() {
        return fAsucceed;
    }

    public void setfAsucceed(BigDecimal fAsucceed) {
        this.fAsucceed = fAsucceed;
    }

    public BigDecimal getfRsucceed() {
        return fRsucceed;
    }

    public void setfRsucceed(BigDecimal fRsucceed) {
        this.fRsucceed = fRsucceed;
    }

    public String getcVastflag() {
        return cVastflag;
    }

    public void setcVastflag(String cVastflag) {
        this.cVastflag = cVastflag == null ? null : cVastflag.trim();
    }

    public BigDecimal getfEncashratio() {
        return fEncashratio;
    }

    public void setfEncashratio(BigDecimal fEncashratio) {
        this.fEncashratio = fEncashratio;
    }

    public BigDecimal getfChangeratio() {
        return fChangeratio;
    }

    public void setfChangeratio(BigDecimal fChangeratio) {
        this.fChangeratio = fChangeratio;
    }

    public String getcExcessflag() {
        return cExcessflag;
    }

    public void setcExcessflag(String cExcessflag) {
        this.cExcessflag = cExcessflag == null ? null : cExcessflag.trim();
    }

    public BigDecimal getfSubscriberatio() {
        return fSubscriberatio;
    }

    public void setfSubscriberatio(BigDecimal fSubscriberatio) {
        this.fSubscriberatio = fSubscriberatio;
    }

    public String getcInputpersonnel() {
        return cInputpersonnel;
    }

    public void setcInputpersonnel(String cInputpersonnel) {
        this.cInputpersonnel = cInputpersonnel == null ? null : cInputpersonnel.trim();
    }

    public String getcCheckpersonnel() {
        return cCheckpersonnel;
    }

    public void setcCheckpersonnel(String cCheckpersonnel) {
        this.cCheckpersonnel = cCheckpersonnel == null ? null : cCheckpersonnel.trim();
    }

    public BigDecimal getfIncome() {
        return fIncome;
    }

    public void setfIncome(BigDecimal fIncome) {
        this.fIncome = fIncome;
    }

    public BigDecimal getfIncomeunit() {
        return fIncomeunit;
    }

    public void setfIncomeunit(BigDecimal fIncomeunit) {
        this.fIncomeunit = fIncomeunit;
    }

    public BigDecimal getfIncomeratio() {
        return fIncomeratio;
    }

    public void setfIncomeratio(BigDecimal fIncomeratio) {
        this.fIncomeratio = fIncomeratio;
    }

    public BigDecimal getfUnassign() {
        return fUnassign;
    }

    public void setfUnassign(BigDecimal fUnassign) {
        this.fUnassign = fUnassign;
    }

    public BigDecimal getfTotalnetvalue() {
        return fTotalnetvalue;
    }

    public void setfTotalnetvalue(BigDecimal fTotalnetvalue) {
        this.fTotalnetvalue = fTotalnetvalue;
    }

    public BigDecimal getfServicefare() {
        return fServicefare;
    }

    public void setfServicefare(BigDecimal fServicefare) {
        this.fServicefare = fServicefare;
    }

    public String getcTano() {
        return cTano;
    }

    public void setcTano(String cTano) {
        this.cTano = cTano == null ? null : cTano.trim();
    }

    public String getcPublished() {
        return cPublished;
    }

    public void setcPublished(String cPublished) {
        this.cPublished = cPublished == null ? null : cPublished.trim();
    }

    public BigDecimal getfDynamicline() {
        return fDynamicline;
    }

    public void setfDynamicline(BigDecimal fDynamicline) {
        this.fDynamicline = fDynamicline;
    }

    public BigDecimal getfDayinc() {
        return fDayinc;
    }

    public void setfDayinc(BigDecimal fDayinc) {
        this.fDayinc = fDayinc;
    }

    public BigDecimal getfWeekinc() {
        return fWeekinc;
    }

    public void setfWeekinc(BigDecimal fWeekinc) {
        this.fWeekinc = fWeekinc;
    }

    public BigDecimal getfDayincSms() {
        return fDayincSms;
    }

    public void setfDayincSms(BigDecimal fDayincSms) {
        this.fDayincSms = fDayincSms;
    }

    public BigDecimal getfMonthinc() {
        return fMonthinc;
    }

    public void setfMonthinc(BigDecimal fMonthinc) {
        this.fMonthinc = fMonthinc;
    }

    public BigDecimal getfNetvaluedayinc() {
        return fNetvaluedayinc;
    }

    public void setfNetvaluedayinc(BigDecimal fNetvaluedayinc) {
        this.fNetvaluedayinc = fNetvaluedayinc;
    }

    public BigDecimal getfIncinthreedays() {
        return fIncinthreedays;
    }

    public void setfIncinthreedays(BigDecimal fIncinthreedays) {
        this.fIncinthreedays = fIncinthreedays;
    }

    public BigDecimal getfIncomeinc() {
        return fIncomeinc;
    }

    public void setfIncomeinc(BigDecimal fIncomeinc) {
        this.fIncomeinc = fIncomeinc;
    }

    public BigDecimal getfTotalincomeinc() {
        return fTotalincomeinc;
    }

    public void setfTotalincomeinc(BigDecimal fTotalincomeinc) {
        this.fTotalincomeinc = fTotalincomeinc;
    }

    public BigDecimal getfReservedasset() {
        return fReservedasset;
    }

    public void setfReservedasset(BigDecimal fReservedasset) {
        this.fReservedasset = fReservedasset;
    }

    public BigDecimal getfRealnetvalue() {
        return fRealnetvalue;
    }

    public void setfRealnetvalue(BigDecimal fRealnetvalue) {
        this.fRealnetvalue = fRealnetvalue;
    }

    public String getcTainput() {
        return cTainput;
    }

    public void setcTainput(String cTainput) {
        this.cTainput = cTainput == null ? null : cTainput.trim();
    }

    public String getcFainput() {
        return cFainput;
    }

    public void setcFainput(String cFainput) {
        this.cFainput = cFainput == null ? null : cFainput.trim();
    }

    public BigDecimal getfSeasoninc() {
        return fSeasoninc;
    }

    public void setfSeasoninc(BigDecimal fSeasoninc) {
        this.fSeasoninc = fSeasoninc;
    }

    public BigDecimal getfHalfinc() {
        return fHalfinc;
    }

    public void setfHalfinc(BigDecimal fHalfinc) {
        this.fHalfinc = fHalfinc;
    }

    public BigDecimal getfYearinc() {
        return fYearinc;
    }

    public void setfYearinc(BigDecimal fYearinc) {
        this.fYearinc = fYearinc;
    }

    public String getcTodaychgstatus() {
        return cTodaychgstatus;
    }

    public void setcTodaychgstatus(String cTodaychgstatus) {
        this.cTodaychgstatus = cTodaychgstatus == null ? null : cTodaychgstatus.trim();
    }

    public String getcTodaypcsstatus() {
        return cTodaypcsstatus;
    }

    public void setcTodaypcsstatus(String cTodaypcsstatus) {
        this.cTodaypcsstatus = cTodaypcsstatus == null ? null : cTodaypcsstatus.trim();
    }

    public String getcTodaysplitstatus() {
        return cTodaysplitstatus;
    }

    public void setcTodaysplitstatus(String cTodaysplitstatus) {
        this.cTodaysplitstatus = cTodaysplitstatus == null ? null : cTodaysplitstatus.trim();
    }

    public BigDecimal getfFanetvalue() {
        return fFanetvalue;
    }

    public void setfFanetvalue(BigDecimal fFanetvalue) {
        this.fFanetvalue = fFanetvalue;
    }

    public String getcPublishedTemp() {
        return cPublishedTemp;
    }

    public void setcPublishedTemp(String cPublishedTemp) {
        this.cPublishedTemp = cPublishedTemp == null ? null : cPublishedTemp.trim();
    }

    public String getcManualinput() {
        return cManualinput;
    }

    public void setcManualinput(String cManualinput) {
        this.cManualinput = cManualinput == null ? null : cManualinput.trim();
    }

    public String getcPcfinput() {
        return cPcfinput;
    }

    public void setcPcfinput(String cPcfinput) {
        this.cPcfinput = cPcfinput == null ? null : cPcfinput.trim();
    }

    public String getcTodaytrfstatus() {
        return cTodaytrfstatus;
    }

    public void setcTodaytrfstatus(String cTodaytrfstatus) {
        this.cTodaytrfstatus = cTodaytrfstatus == null ? null : cTodaytrfstatus.trim();
    }

    public String getcCheckflag() {
        return cCheckflag;
    }

    public void setcCheckflag(String cCheckflag) {
        this.cCheckflag = cCheckflag == null ? null : cCheckflag.trim();
    }

    public String getcIsvaluationday() {
        return cIsvaluationday;
    }

    public void setcIsvaluationday(String cIsvaluationday) {
        this.cIsvaluationday = cIsvaluationday == null ? null : cIsvaluationday.trim();
    }

    public BigDecimal getfIncomeratio30days() {
        return fIncomeratio30days;
    }

    public void setfIncomeratio30days(BigDecimal fIncomeratio30days) {
        this.fIncomeratio30days = fIncomeratio30days;
    }

    public BigDecimal getfRmbnetvalue() {
        return fRmbnetvalue;
    }

    public void setfRmbnetvalue(BigDecimal fRmbnetvalue) {
        this.fRmbnetvalue = fRmbnetvalue;
    }

    public BigDecimal getfRmbtotalnetvalue() {
        return fRmbtotalnetvalue;
    }

    public void setfRmbtotalnetvalue(BigDecimal fRmbtotalnetvalue) {
        this.fRmbtotalnetvalue = fRmbtotalnetvalue;
    }

    public BigDecimal getfThisYearinc() {
        return fThisYearinc;
    }

    public void setfThisYearinc(BigDecimal fThisYearinc) {
        this.fThisYearinc = fThisYearinc;
    }

    public BigDecimal getF1Yearinc() {
        return f1Yearinc;
    }

    public void setF1Yearinc(BigDecimal f1Yearinc) {
        this.f1Yearinc = f1Yearinc;
    }

    public BigDecimal getF2Yearinc() {
        return f2Yearinc;
    }

    public void setF2Yearinc(BigDecimal f2Yearinc) {
        this.f2Yearinc = f2Yearinc;
    }

    public BigDecimal getF3Yearinc() {
        return f3Yearinc;
    }

    public void setF3Yearinc(BigDecimal f3Yearinc) {
        this.f3Yearinc = f3Yearinc;
    }

    public BigDecimal getF5Yearinc() {
        return f5Yearinc;
    }

    public void setF5Yearinc(BigDecimal f5Yearinc) {
        this.f5Yearinc = f5Yearinc;
    }

    public BigDecimal getfSetupinc() {
        return fSetupinc;
    }

    public void setfSetupinc(BigDecimal fSetupinc) {
        this.fSetupinc = fSetupinc;
    }

    public BigDecimal getfIncomeratio90days() {
        return fIncomeratio90days;
    }

    public void setfIncomeratio90days(BigDecimal fIncomeratio90days) {
        this.fIncomeratio90days = fIncomeratio90days;
    }

    public BigDecimal getfIncomeratio45days() {
        return fIncomeratio45days;
    }

    public void setfIncomeratio45days(BigDecimal fIncomeratio45days) {
        this.fIncomeratio45days = fIncomeratio45days;
    }

    public Date getdCreatedate() {
        return dCreatedate;
    }

    public void setdCreatedate(Date dCreatedate) {
        this.dCreatedate = dCreatedate;
    }

    public Date getdAuditdate() {
        return dAuditdate;
    }

    public void setdAuditdate(Date dAuditdate) {
        this.dAuditdate = dAuditdate;
    }

    public BigDecimal getfLasttotalasset() {
        return fLasttotalasset;
    }

    public void setfLasttotalasset(BigDecimal fLasttotalasset) {
        this.fLasttotalasset = fLasttotalasset;
    }
}