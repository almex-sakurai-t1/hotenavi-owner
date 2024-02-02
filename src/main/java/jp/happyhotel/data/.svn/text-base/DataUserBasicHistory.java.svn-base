/*
 * @(#)DataUserBasicHistory.java 1.00 2007/07/17
 * Copyright (C) ALMEX Inc. 2007
 * ユーザ基本情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザ基本履歴情報取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2007/08/31
 * @version 1.1 2007/11/28
 */
public class DataUserBasicHistory implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1217672538952462082L;

    private String            userId;
    private int               seq;
    private String            passwd;
    private String            mobileTermNo;
    private int               registStatus;
    private int               delFlag;
    private String            handleName;
    private int               point;
    private int               birthdayYear;
    private int               birthdayMonth;
    private int               birthdayDay;
    private int               sex;
    private int               prefCode;
    private int               jisCode;
    private String            mailAddr;
    private String            mailAddrMd5;
    private int               mailAddrUnknown;
    private String            mailAddrMobile;
    private String            mailAddrMobileMd5;
    private int               mailAddrMobileUnknown;
    private int               mailmagOfficial;
    private int               mailmagTarget;
    private String            bloodType;
    private String            occupation;
    private String            nameLast;
    private String            nameFirst;
    private String            nameLastKana;
    private String            nameFirstKana;
    private String            zipCode;
    private String            address1;
    private String            address2;
    private String            tel1;
    private String            tel2;
    private String            family;
    private String            delReason;
    private int               tempDatePc;
    private int               tempTimePc;
    private int               tempDateMobile;
    private int               tempTimeMobile;
    private int               registDatePc;
    private int               registTimePc;
    private int               registDateMobile;
    private int               registTimeMobile;
    private int               loginDatePc;
    private int               loginTimePc;
    private int               loginDateMobile;
    private int               loginTimeMobile;
    private int               delDatePc;
    private int               delTimePc;
    private int               delDateMobile;
    private int               delTimeMobile;
    private int               registDate;
    private int               registTime;
    private String            memo;
    private int               docomoFlag;
    private int               loginFlag;
    private int               mailStartTime;
    private int               mailEndTime;
    private int               campaignFlag;
    private int               pointUpdate;
    private int               buzzPointPlus;
    private int               buzzPointMinus;
    private int               registStatusPay;
    private int               registStatusOld;
    private int               pointPay;
    private String            delReasonPay;
    private int               registDatePay;
    private int               registTimePay;
    private int               delDatePay;
    private int               delTimePay;
    private String            accessTicket;
    private int               pointPayUpdate;
    private int               birthdayChangeFlag;
    private int               constellation;
    private int               loveConstellation;
    private int               flag1;
    private int               flag2;
    private int               flag3;
    private int               smartPhoneFlag;
    private String            address3;

    /**
     * データを初期化します。
     */
    public DataUserBasicHistory()
    {
        userId = "";
        seq = 0;
        passwd = "";
        mobileTermNo = "";
        registStatus = 0;
        delFlag = 0;
        handleName = "";
        point = 0;
        birthdayYear = 0;
        birthdayMonth = 0;
        birthdayDay = 0;
        sex = 0;
        prefCode = 0;
        jisCode = 0;
        mailAddr = "";
        mailAddrMd5 = "";
        mailAddrUnknown = 0;
        mailAddrMobile = "";
        mailAddrMobileMd5 = "";
        mailAddrMobileUnknown = 0;
        mailmagOfficial = 0;
        mailmagTarget = 0;
        bloodType = "";
        occupation = "";
        nameLast = "";
        nameFirst = "";
        nameLastKana = "";
        nameFirstKana = "";
        zipCode = "";
        address1 = "";
        address2 = "";
        tel1 = "";
        tel2 = "";
        family = "";
        delReason = "";
        tempDatePc = 0;
        tempTimePc = 0;
        tempDateMobile = 0;
        tempTimeMobile = 0;
        registDatePc = 0;
        registTimePc = 0;
        registDateMobile = 0;
        registTimeMobile = 0;
        loginDatePc = 0;
        loginTimePc = 0;
        loginDateMobile = 0;
        loginTimeMobile = 0;
        delDatePc = 0;
        delTimePc = 0;
        delDateMobile = 0;
        delTimeMobile = 0;
        registDate = 0;
        registTime = 0;
        memo = "";
        docomoFlag = 0;
        loginFlag = 0;
        mailStartTime = 0;
        mailEndTime = 0;
        campaignFlag = 0;
        pointUpdate = 0;
        buzzPointPlus = 0;
        buzzPointMinus = 0;
        registStatusPay = 0;
        registStatusOld = 0;
        pointPay = 0;
        delReasonPay = "";
        registDatePay = 0;
        registTimePay = 0;
        delDatePay = 0;
        delTimePay = 0;
        accessTicket = "";
        pointPayUpdate = 0;
        birthdayChangeFlag = 0;
        constellation = 0;
        loveConstellation = 0;
        flag1 = 0;
        flag2 = 0;
        flag3 = 0;
        smartPhoneFlag = 0;
        address3 = "";
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public int getSmartPhoneFlag()
    {
        return smartPhoneFlag;
    }

    public void setSmartPhoneFlag(int smartPhoneFlag)
    {
        this.smartPhoneFlag = smartPhoneFlag;
    }

    public int getFlag3()
    {
        return this.flag3;
    }

    public void setFlag3(int flag3)
    {
        this.flag3 = flag3;
    }

    public int getFlag2()
    {
        return this.flag2;
    }

    public void setFlag2(int flag2)
    {
        this.flag2 = flag2;
    }

    public int getFlag1()
    {
        return this.flag1;
    }

    public void setFlag1(int flag1)
    {
        this.flag1 = flag1;
    }

    public int getLoveConstellation()
    {
        return constellation;
    }

    public void setLoveConstellation(int loveConstellation)
    {
        this.loveConstellation = loveConstellation;
    }

    public int getConstellation()
    {
        return constellation;
    }

    public void setConstellation(int constellation)
    {
        this.constellation = constellation;
    }

    public int getPointPayUpdate()
    {
        return pointPayUpdate;
    }

    public void setPointPayUpdate(int pointPayUpdate)
    {
        this.pointPayUpdate = pointPayUpdate;
    }

    public int getBirthdayChangeFlag()
    {
        return birthdayChangeFlag;
    }

    public void setBirthdayChangeFlag(int birthdayChangeFlag)
    {
        this.birthdayChangeFlag = birthdayChangeFlag;
    }

    public int getRegistStatusPay()
    {
        return registStatusPay;
    }

    public void setRegistStatusPay(int registStatusPay)
    {
        this.registStatusPay = registStatusPay;
    }

    public int getRegistStatusOld()
    {
        return registStatusOld;
    }

    public void setRegistStatusOld(int registStatusOld)
    {
        this.registStatusOld = registStatusOld;
    }

    public int getPointPay()
    {
        return pointPay;
    }

    public void setPointPay(int pointPay)
    {
        this.pointPay = pointPay;
    }

    public String getDelReasonPay()
    {
        return delReasonPay;
    }

    public void setDelReasonPay(String delReasonPay)
    {
        this.delReasonPay = delReasonPay;
    }

    public int getRegistDatePay()
    {
        return registDatePay;
    }

    public void setRegistDatePay(int registDatePay)
    {
        this.registDatePay = registDatePay;
    }

    public int getRegistTimePay()
    {
        return registTimePay;
    }

    public void setRegistTimePay(int registTimePay)
    {
        this.registTimePay = registTimePay;
    }

    public int getDelDatePay()
    {
        return delDatePay;
    }

    public void setDelDatePay(int delDatePay)
    {
        this.delDatePay = delDatePay;
    }

    public int getDelTimePay()
    {
        return delTimePay;
    }

    public void setDelTimePay(int delTimePay)
    {
        this.delTimePay = delTimePay;
    }

    public String getAccessTicket()
    {
        return accessTicket;
    }

    public void setAccessTicket(String accessTicket)
    {
        this.accessTicket = accessTicket;
    }

    public int getBuzzPointMinus()
    {
        return buzzPointMinus;
    }

    public int getBuzzPointPlus()
    {
        return buzzPointPlus;
    }

    public int getCampaignFlag()
    {
        return campaignFlag;
    }

    public int getPointUpdate()
    {
        return pointUpdate;
    }

    public void setBuzzPointMinus(int buzzPointMinus)
    {
        this.buzzPointMinus = buzzPointMinus;
    }

    public void setBuzzPointPlus(int buzzPointPlus)
    {
        this.buzzPointPlus = buzzPointPlus;
    }

    public void setPointUpdate(int pointUpdate)
    {
        this.pointUpdate = pointUpdate;
    }

    public int getDocomoFlag()
    {
        return docomoFlag;
    }

    public int getLoginFlag()
    {
        return loginFlag;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public int getBirthdayDay()
    {
        return birthdayDay;
    }

    public int getBirthdayMonth()
    {
        return birthdayMonth;
    }

    public int getBirthdayYear()
    {
        return birthdayYear;
    }

    public String getBloodType()
    {
        return bloodType;
    }

    public int getCanmpaingFlag()
    {
        return campaignFlag;
    }

    public int getDelDateMobile()
    {
        return delDateMobile;
    }

    public int getDelDatePc()
    {
        return delDatePc;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public String getDelReason()
    {
        return delReason;
    }

    public int getDelTimeMobile()
    {
        return delTimeMobile;
    }

    public int getDelTimePc()
    {
        return delTimePc;
    }

    public String getFamily()
    {
        return family;
    }

    public String getHandleName()
    {
        return handleName;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public int getLoginDateMobile()
    {
        return loginDateMobile;
    }

    public int getLoginDatePc()
    {
        return loginDatePc;
    }

    public int getLoginTimeMobile()
    {
        return loginTimeMobile;
    }

    public int getLoginTimePc()
    {
        return loginTimePc;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getMailAddrMd5()
    {
        return mailAddrMd5;
    }

    public String getMailAddrMobile()
    {
        return mailAddrMobile;
    }

    public String getMailAddrMobileMd5()
    {
        return mailAddrMobileMd5;
    }

    public int getMailAddrMobileUnknown()
    {
        return mailAddrMobileUnknown;
    }

    public int getMailAddrUnknown()
    {
        return mailAddrUnknown;
    }

    public int getMailEndTime()
    {
        return mailEndTime;
    }

    public int getMailStartTime()
    {
        return mailStartTime;
    }

    public int getMailmagOfficial()
    {
        return mailmagOfficial;
    }

    public int getMailmagTarget()
    {
        return mailmagTarget;
    }

    public String getMemo()
    {
        return memo;
    }

    public String getMobileTermNo()
    {
        return mobileTermNo;
    }

    public String getNameFirst()
    {
        return nameFirst;
    }

    public String getNameFirstKana()
    {
        return nameFirstKana;
    }

    public String getNameLast()
    {
        return nameLast;
    }

    public String getNameLastKana()
    {
        return nameLastKana;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPrefCode()
    {
        return prefCode;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getRegistDateMobile()
    {
        return registDateMobile;
    }

    public int getRegistDatePc()
    {
        return registDatePc;
    }

    public int getRegistStatus()
    {
        return registStatus;
    }

    public int getRegistTimeMobile()
    {
        return registTimeMobile;
    }

    public int getRegistTimePc()
    {
        return registTimePc;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getSex()
    {
        return sex;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public int getTempDateMobile()
    {
        return tempDateMobile;
    }

    public int getTempDatePc()
    {
        return tempDatePc;
    }

    public int getTempTimeMobile()
    {
        return tempTimeMobile;
    }

    public int getTempTimePc()
    {
        return tempTimePc;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setBirthdayDay(int birthdayDay)
    {
        this.birthdayDay = birthdayDay;
    }

    public void setBirthdayMonth(int birthdayMonth)
    {
        this.birthdayMonth = birthdayMonth;
    }

    public void setBirthdayYear(int birthdayYear)
    {
        this.birthdayYear = birthdayYear;
    }

    public void setBloodType(String bloodType)
    {
        this.bloodType = bloodType;
    }

    public void setCampaignFlag(int campaignFlag)
    {
        this.campaignFlag = campaignFlag;
    }

    public void setDelDateMobile(int delDateMobile)
    {
        this.delDateMobile = delDateMobile;
    }

    public void setDelDatePc(int delDatePc)
    {
        this.delDatePc = delDatePc;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setDelReason(String delReason)
    {
        this.delReason = delReason;
    }

    public void setDelTimeMobile(int delTimeMobile)
    {
        this.delTimeMobile = delTimeMobile;
    }

    public void setDelTimePc(int delTimePc)
    {
        this.delTimePc = delTimePc;
    }

    public void setFamily(String family)
    {
        this.family = family;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setLoginDateMobile(int loginDateMobile)
    {
        this.loginDateMobile = loginDateMobile;
    }

    public void setLoginDatePc(int loginDatePc)
    {
        this.loginDatePc = loginDatePc;
    }

    public void setLoginTimeMobile(int loginTimeMobile)
    {
        this.loginTimeMobile = loginTimeMobile;
    }

    public void setLoginTimePc(int loginTimePc)
    {
        this.loginTimePc = loginTimePc;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMailAddrMd5(String mailAddrMd5)
    {
        this.mailAddrMd5 = mailAddrMd5;
    }

    public void setMailAddrMobile(String mailAddrMobile)
    {
        this.mailAddrMobile = mailAddrMobile;
    }

    public void setMailAddrMobileMd5(String mailAddrMobileMd5)
    {
        this.mailAddrMobileMd5 = mailAddrMobileMd5;
    }

    public void setMailAddrMobileUnknown(int mailAddrMobileUnknown)
    {
        this.mailAddrMobileUnknown = mailAddrMobileUnknown;
    }

    public void setMailAddrUnknown(int mailAddrUnknown)
    {
        this.mailAddrUnknown = mailAddrUnknown;
    }

    public void setMailEndTime(int mailEndTime)
    {
        this.mailEndTime = mailEndTime;
    }

    public void setMailStartTime(int mailStartTime)
    {
        this.mailStartTime = mailStartTime;
    }

    public void setMailmagOfficial(int mailmagOfficial)
    {
        this.mailmagOfficial = mailmagOfficial;
    }

    public void setMailmagTarget(int mailmagTarget)
    {
        this.mailmagTarget = mailmagTarget;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setMobileTermNo(String mobileTermNo)
    {
        this.mobileTermNo = mobileTermNo;
    }

    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }

    public void setNameFirstKana(String nameFirstKana)
    {
        this.nameFirstKana = nameFirstKana;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

    public void setNameLastKana(String nameLastKana)
    {
        this.nameLastKana = nameLastKana;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setRegistDateMobile(int registDateMobile)
    {
        this.registDateMobile = registDateMobile;
    }

    public void setRegistDatePc(int registDatePc)
    {
        this.registDatePc = registDatePc;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public void setRegistTimeMobile(int registTimeMobile)
    {
        this.registTimeMobile = registTimeMobile;
    }

    public void setRegistTimePc(int registTimePc)
    {
        this.registTimePc = registTimePc;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setTempDateMobile(int tempDateMobile)
    {
        this.tempDateMobile = tempDateMobile;
    }

    public void setTempDatePc(int tempDatePc)
    {
        this.tempDatePc = tempDatePc;
    }

    public void setTempTimeMobile(int tempTimeMobile)
    {
        this.tempTimeMobile = tempTimeMobile;
    }

    public void setTempTimePc(int tempTimePc)
    {
        this.tempTimePc = tempTimePc;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setDocomoFlag(int docomoFlag)
    {
        this.docomoFlag = docomoFlag;
    }

    public void setLoginFlag(int loginFlag)
    {
        this.loginFlag = loginFlag;
    }

    /**
     * ユーザ基本履歴データ取得
     * 
     * @param userId ユーザ基本データ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_basic_history WHERE user_id = ? AND seq = ? AND del_flag != 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.passwd = result.getString( "passwd" );
                    this.mobileTermNo = result.getString( "mobile_termno" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.handleName = result.getString( "handle_name" );
                    this.point = result.getInt( "point" );
                    this.birthdayYear = result.getInt( "birthday_year" );
                    this.birthdayMonth = result.getInt( "birthday_month" );
                    this.birthdayDay = result.getInt( "birthday_day" );
                    this.sex = result.getInt( "sex" );
                    this.prefCode = result.getInt( "pref_code" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.mailAddrMd5 = result.getString( "mail_addr_md5" );
                    this.mailAddrUnknown = result.getInt( "mail_addr_unknown" );
                    this.mailAddrMobile = result.getString( "mail_addr_mobile" );
                    this.mailAddrMobileMd5 = result.getString( "mail_addr_mobile_md5" );
                    this.mailAddrMobileUnknown = result.getInt( "mail_addr_mobile_unknown" );
                    this.mailmagOfficial = result.getInt( "mailmag_official" );
                    this.mailmagTarget = result.getInt( "mailmag_target" );
                    this.bloodType = result.getString( "blood_type" );
                    this.occupation = result.getString( "occupation" );
                    this.nameLast = result.getString( "name_last" );
                    this.nameFirst = result.getString( "name_first" );
                    this.nameLastKana = result.getString( "name_last_kana" );
                    this.nameFirstKana = result.getString( "name_first_kana" );
                    this.zipCode = result.getString( "zip_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.family = result.getString( "family" );
                    this.delReason = result.getString( "del_reason" );
                    this.tempDatePc = result.getInt( "temp_date_pc" );
                    this.tempTimePc = result.getInt( "temp_time_pc" );
                    this.tempDateMobile = result.getInt( "temp_date_mobile" );
                    this.tempTimeMobile = result.getInt( "temp_time_mobile" );
                    this.registDatePc = result.getInt( "regist_date_pc" );
                    this.registTimePc = result.getInt( "regist_time_pc" );
                    this.registDateMobile = result.getInt( "regist_date_mobile" );
                    this.registTimeMobile = result.getInt( "regist_time_mobile" );
                    this.loginDatePc = result.getInt( "login_date_pc" );
                    this.loginTimePc = result.getInt( "login_time_pc" );
                    this.loginDateMobile = result.getInt( "login_date_mobile" );
                    this.loginTimeMobile = result.getInt( "login_time_mobile" );
                    this.delDatePc = result.getInt( "del_date_pc" );
                    this.delTimePc = result.getInt( "del_time_pc" );
                    this.delDateMobile = result.getInt( "del_date_mobile" );
                    this.delTimeMobile = result.getInt( "del_time_mobile" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.memo = result.getString( "memo" );
                    this.docomoFlag = result.getInt( "docomo_flag" );
                    this.loginFlag = result.getInt( "login_flag" );
                    this.mailStartTime = result.getInt( "mail_starttime" );
                    this.mailEndTime = result.getInt( "mail_endtime" );
                    this.campaignFlag = result.getInt( "campaign_flag" );
                    this.pointUpdate = result.getInt( "point_update" );
                    this.buzzPointPlus = result.getInt( "buzz_point_plus" );
                    this.buzzPointMinus = result.getInt( "buzz_point_minus" );
                    this.registStatusPay = result.getInt( "regist_status_pay" );
                    this.registStatusOld = result.getInt( "regist_status_old" );
                    this.pointPay = result.getInt( "point_pay" );
                    this.delReasonPay = result.getString( "del_reason_pay" );
                    this.registDatePay = result.getInt( "regist_date_pay" );
                    this.registTimePay = result.getInt( "regist_time_pay" );
                    this.delDatePay = result.getInt( "del_date_pay" );
                    this.delTimePay = result.getInt( "del_time_pay" );
                    this.accessTicket = result.getString( "access_ticket" );
                    this.pointPayUpdate = result.getInt( "point_pay_update" );
                    this.birthdayChangeFlag = result.getInt( "birthday_change_flag" );
                    this.constellation = result.getInt( "constellation" );
                    this.loveConstellation = result.getInt( "love_constellation" );
                    this.flag1 = result.getInt( "flag1" );
                    this.flag2 = result.getInt( "flag2" );
                    this.flag3 = result.getInt( "flag3" );
                    this.smartPhoneFlag = result.getInt( "smartphone_flag" );
                    this.address3 = result.getString( "address3" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicHistory.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザ基本履歴データ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.seq = result.getInt( "seq" );
                this.passwd = result.getString( "passwd" );
                this.mobileTermNo = result.getString( "mobile_termno" );
                this.registStatus = result.getInt( "regist_status" );
                this.delFlag = result.getInt( "del_flag" );
                this.handleName = result.getString( "handle_name" );
                this.point = result.getInt( "point" );
                this.birthdayYear = result.getInt( "birthday_year" );
                this.birthdayMonth = result.getInt( "birthday_month" );
                this.birthdayDay = result.getInt( "birthday_day" );
                this.sex = result.getInt( "sex" );
                this.prefCode = result.getInt( "pref_code" );
                this.jisCode = result.getInt( "jis_code" );
                this.mailAddr = result.getString( "mail_addr" );
                this.mailAddrMd5 = result.getString( "mail_addr_md5" );
                this.mailAddrUnknown = result.getInt( "mail_addr_unknown" );
                this.mailAddrMobile = result.getString( "mail_addr_mobile" );
                this.mailAddrMobileMd5 = result.getString( "mail_addr_mobile_md5" );
                this.mailAddrMobileUnknown = result.getInt( "mail_addr_mobile_unknown" );
                this.mailmagOfficial = result.getInt( "mailmag_official" );
                this.mailmagTarget = result.getInt( "mailmag_target" );
                this.bloodType = result.getString( "blood_type" );
                this.occupation = result.getString( "occupation" );
                this.nameLast = result.getString( "name_last" );
                this.nameFirst = result.getString( "name_first" );
                this.nameLastKana = result.getString( "name_last_kana" );
                this.nameFirstKana = result.getString( "name_first_kana" );
                this.zipCode = result.getString( "zip_code" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.family = result.getString( "family" );
                this.delReason = result.getString( "del_reason" );
                this.tempDatePc = result.getInt( "temp_date_pc" );
                this.tempTimePc = result.getInt( "temp_time_pc" );
                this.tempDateMobile = result.getInt( "temp_date_mobile" );
                this.tempTimeMobile = result.getInt( "temp_time_mobile" );
                this.registDatePc = result.getInt( "regist_date_pc" );
                this.registTimePc = result.getInt( "regist_time_pc" );
                this.registDateMobile = result.getInt( "regist_date_mobile" );
                this.registTimeMobile = result.getInt( "regist_time_mobile" );
                this.loginDatePc = result.getInt( "login_date_pc" );
                this.loginTimePc = result.getInt( "login_time_pc" );
                this.loginDateMobile = result.getInt( "login_date_mobile" );
                this.loginTimeMobile = result.getInt( "login_time_mobile" );
                this.delDatePc = result.getInt( "del_date_pc" );
                this.delTimePc = result.getInt( "del_time_pc" );
                this.delDateMobile = result.getInt( "del_date_mobile" );
                this.delTimeMobile = result.getInt( "del_time_mobile" );
                this.registDate = result.getInt( "result_date" );
                this.registTime = result.getInt( "result_time" );
                this.memo = result.getString( "memo" );
                this.docomoFlag = result.getInt( "docomo_flag" );
                this.loginFlag = result.getInt( "login_flag" );
                this.mailStartTime = result.getInt( "mail_starttime" );
                this.mailEndTime = result.getInt( "mail_endtime" );
                this.campaignFlag = result.getInt( "campaign_flag" );
                this.pointUpdate = result.getInt( "point_update" );
                this.buzzPointPlus = result.getInt( "buzz_point_plus" );
                this.buzzPointMinus = result.getInt( "buzz_point_minus" );
                this.registStatusPay = result.getInt( "regist_status_pay" );
                this.registStatusOld = result.getInt( "regist_status_old" );
                this.pointPay = result.getInt( "point_pay" );
                this.delReasonPay = result.getString( "del_reason_pay" );
                this.registDatePay = result.getInt( "regist_date_pay" );
                this.registTimePay = result.getInt( "regist_time_pay" );
                this.delDatePay = result.getInt( "del_date_pay" );
                this.delTimePay = result.getInt( "del_time_pay" );
                this.accessTicket = result.getString( "access_ticket" );
                this.pointPayUpdate = result.getInt( "point_pay_update" );
                this.birthdayChangeFlag = result.getInt( "birthday_change_flag" );
                this.constellation = result.getInt( "constellation" );
                this.loveConstellation = result.getInt( "love_constellation" );
                this.flag1 = result.getInt( "flag1" );
                this.flag2 = result.getInt( "flag2" );
                this.flag3 = result.getInt( "flag3" );
                this.smartPhoneFlag = result.getInt( "smartphone_flag" );
                this.address3 = result.getString( "address3" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicHistory.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ基本履歴情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserve.insertData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * ユーザ基本履歴情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection connection)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_basic_history SET ";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " passwd = ?,";
        query = query + " mobile_termno = ?,";
        query = query + " regist_status = ?,";
        query = query + " del_flag = ?,";
        query = query + " handle_name = ?,";
        query = query + " point = ?,";
        query = query + " birthday_year = ?,";
        query = query + " birthday_month = ?,";
        query = query + " birthday_day = ?,";
        query = query + " sex = ?,";
        query = query + " pref_code = ?,";
        query = query + " jis_code = ?,";
        query = query + " mail_addr = ?,";
        query = query + " mail_addr_md5 = ?,";
        query = query + " mail_addr_unknown = ?,";
        query = query + " mail_addr_mobile = ?,";
        query = query + " mail_addr_mobile_md5 = ?,";
        query = query + " mail_addr_mobile_unknown = ?,";
        query = query + " mailmag_official = ?,";
        query = query + " mailmag_target = ?,";
        query = query + " blood_type = ?,";
        query = query + " occupation = ?,";
        query = query + " name_last = ?,";
        query = query + " name_first = ?,";
        query = query + " name_last_kana = ?,";
        query = query + " name_first_kana = ?,";
        query = query + " zip_code = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " family = ?,";
        query = query + " del_reason= ?,";
        query = query + " temp_date_pc = ?,";
        query = query + " temp_time_pc = ?,";
        query = query + " temp_date_mobile = ?,";
        query = query + " temp_time_mobile = ?,";
        query = query + " regist_date_pc = ?,";
        query = query + " regist_time_pc = ?,";
        query = query + " regist_date_mobile = ?,";
        query = query + " regist_time_mobile = ?,";
        query = query + " login_date_pc = ?,";
        query = query + " login_time_pc = ?,";
        query = query + " login_date_mobile = ?,";
        query = query + " login_time_mobile = ?,";
        query = query + " del_date_pc = ?,";
        query = query + " del_time_pc = ?,";
        query = query + " del_date_mobile = ?,";
        query = query + " del_time_mobile = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " memo = ?,";
        query = query + " docomo_flag = ?,";
        query = query + " login_flag = ?,";
        query = query + " mail_starttime = ?,";
        query = query + " mail_endtime = ?,";
        query = query + " campaign_flag = ?,";
        query = query + " point_update = ?,";
        query = query + " buzz_point_plus = ?,";
        query = query + " buzz_point_minus = ?,";
        query = query + " regist_status_pay = ?,";
        query = query + " regist_status_old = ?,";
        query = query + " point_pay = ?,";
        query = query + " del_reason_pay = ?,";
        query = query + " regist_date_pay = ?,";
        query = query + " regist_time_pay = ?,";
        query = query + " del_date_pay = ?,";
        query = query + " del_time_pay = ?,";
        query = query + " access_ticket = ?,";
        query = query + " point_pay_update = ?,";
        query = query + " birthday_change_flag = ?,";
        query = query + " constellation = ?,";
        query = query + " love_constellation = ?,";
        query = query + " flag1 = ?,";
        query = query + " flag2 = ?,";
        query = query + " flag3 = ?,";
        query = query + " smartphone_flag = ?,";
        query = query + " address3 = ?";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.passwd );
            prestate.setString( 3, this.mobileTermNo );
            prestate.setInt( 4, this.registStatus );
            prestate.setInt( 5, this.delFlag );
            prestate.setString( 6, this.handleName );
            prestate.setInt( 7, this.point );
            prestate.setInt( 8, this.birthdayYear );
            prestate.setInt( 9, this.birthdayMonth );
            prestate.setInt( 10, this.birthdayDay );
            prestate.setInt( 11, this.sex );
            prestate.setInt( 12, this.prefCode );
            prestate.setInt( 13, this.jisCode );
            prestate.setString( 14, this.mailAddr );
            prestate.setString( 15, this.mailAddrMd5 );
            prestate.setInt( 16, this.mailAddrUnknown );
            prestate.setString( 17, this.mailAddrMobile );
            prestate.setString( 18, this.mailAddrMobileMd5 );
            prestate.setInt( 19, this.mailAddrMobileUnknown );
            prestate.setInt( 20, this.mailmagOfficial );
            prestate.setInt( 21, this.mailmagTarget );
            prestate.setString( 22, this.bloodType );
            prestate.setString( 23, this.occupation );
            prestate.setString( 24, this.nameLast );
            prestate.setString( 25, this.nameFirst );
            prestate.setString( 26, this.nameLastKana );
            prestate.setString( 27, this.nameFirstKana );
            prestate.setString( 28, this.zipCode );
            prestate.setString( 29, this.address1 );
            prestate.setString( 30, this.address2 );
            prestate.setString( 31, this.tel1 );
            prestate.setString( 32, this.tel2 );
            prestate.setString( 33, this.family );
            prestate.setString( 34, this.delReason );
            prestate.setInt( 35, this.tempDatePc );
            prestate.setInt( 36, this.tempTimePc );
            prestate.setInt( 37, this.tempDateMobile );
            prestate.setInt( 38, this.tempTimeMobile );
            prestate.setInt( 39, this.registDatePc );
            prestate.setInt( 40, this.registTimePc );
            prestate.setInt( 41, this.registDateMobile );
            prestate.setInt( 42, this.registTimeMobile );
            prestate.setInt( 43, this.loginDatePc );
            prestate.setInt( 44, this.loginTimePc );
            prestate.setInt( 45, this.loginDateMobile );
            prestate.setInt( 46, this.loginTimeMobile );
            prestate.setInt( 47, this.delDatePc );
            prestate.setInt( 48, this.delTimePc );
            prestate.setInt( 49, this.delDateMobile );
            prestate.setInt( 50, this.delTimeMobile );
            prestate.setInt( 51, this.registDate );
            prestate.setInt( 52, this.registTime );
            prestate.setString( 53, this.memo );
            prestate.setInt( 54, this.docomoFlag );
            prestate.setInt( 55, this.loginFlag );
            prestate.setInt( 56, this.mailStartTime );
            prestate.setInt( 57, this.mailEndTime );
            prestate.setInt( 58, this.campaignFlag );
            prestate.setInt( 59, this.pointUpdate );
            prestate.setInt( 60, this.buzzPointPlus );
            prestate.setInt( 61, this.buzzPointMinus );
            prestate.setInt( 62, this.registStatusPay );
            prestate.setInt( 63, this.registStatusOld );
            prestate.setInt( 64, this.pointPay );
            prestate.setString( 65, this.delReasonPay );
            prestate.setInt( 66, this.registDatePay );
            prestate.setInt( 67, this.registTimePay );
            prestate.setInt( 68, this.delDatePay );
            prestate.setInt( 69, this.delTimePay );
            prestate.setString( 70, this.accessTicket );
            prestate.setInt( 71, this.pointPayUpdate );
            prestate.setInt( 72, this.birthdayChangeFlag );
            prestate.setInt( 73, this.constellation );
            prestate.setInt( 74, this.loveConstellation );
            prestate.setInt( 75, this.flag1 );
            prestate.setInt( 76, this.flag2 );
            prestate.setInt( 77, this.flag3 );
            prestate.setInt( 78, this.smartPhoneFlag );
            prestate.setString( 79, this.address3 );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicHistory.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * ユーザ基本履歴情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_basic_history SET ";
        query = query + " passwd = ?,";
        query = query + " mobile_termno = ?,";
        query = query + " regist_status = ?,";
        query = query + " del_flag = ?,";
        query = query + " handle_name = ?,";
        query = query + " point = ?,";
        query = query + " birthday_year = ?,";
        query = query + " birthday_month = ?,";
        query = query + " birthday_day = ?,";
        query = query + " sex = ?,";
        query = query + " pref_code = ?,";
        query = query + " jis_code = ?,";
        query = query + " mail_addr = ?,";
        query = query + " mail_addr_md5 = ?,";
        query = query + " mail_addr_unknown = ?,";
        query = query + " mail_addr_mobile = ?,";
        query = query + " mail_addr_mobile_md5 = ?,";
        query = query + " mail_addr_mobile_unknown = ?,";
        query = query + " mailmag_official = ?,";
        query = query + " mailmag_target = ?,";
        query = query + " blood_type = ?,";
        query = query + " occupation = ?,";
        query = query + " name_last = ?,";
        query = query + " name_first = ?,";
        query = query + " name_last_kana = ?,";
        query = query + " name_first_kana = ?,";
        query = query + " zip_code = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " family = ?,";
        query = query + " del_reason= ?,";
        query = query + " temp_date_pc = ?,";
        query = query + " temp_time_pc = ?,";
        query = query + " temp_date_mobile = ?,";
        query = query + " temp_time_mobile = ?,";
        query = query + " regist_date_pc = ?,";
        query = query + " regist_time_pc = ?,";
        query = query + " regist_date_mobile = ?,";
        query = query + " regist_time_mobile = ?,";
        query = query + " login_date_pc = ?,";
        query = query + " login_time_pc = ?,";
        query = query + " login_date_mobile = ?,";
        query = query + " login_time_mobile = ?,";
        query = query + " del_date_pc = ?,";
        query = query + " del_time_pc = ?,";
        query = query + " del_date_mobile = ?,";
        query = query + " del_time_mobile = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " memo = ?,";
        query = query + " docomo_flag = ?,";
        query = query + " login_flag = ?,";
        query = query + " mail_starttime = ?,";
        query = query + " mail_endtime = ?,";
        query = query + " campaign_flag = ?,";
        query = query + " point_update = ?,";
        query = query + " buzz_point_plus = ?,";
        query = query + " buzz_point_minus = ?,";
        query = query + " regist_status_pay = ?,";
        query = query + " regist_status_old = ?,";
        query = query + " point_pay = ?,";
        query = query + " del_reason_pay = ?,";
        query = query + " regist_date_pay = ?,";
        query = query + " regist_time_pay = ?,";
        query = query + " del_date_pay = ?,";
        query = query + " del_time_pay = ?,";
        query = query + " access_ticket = ?,";
        query = query + " point_pay_update = ?,";
        query = query + " birthday_change_flag = ?,";
        query = query + " constellation = ?,";
        query = query + " love_constellation = ?,";
        query = query + " flag1 = ?,";
        query = query + " flag2 = ?,";
        query = query + " flag3 = ?,";
        query = query + " smartphone_flag = ?,";
        query = query + " address3 = ?";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.passwd );
            prestate.setString( 2, this.mobileTermNo );
            prestate.setInt( 3, this.registStatus );
            prestate.setInt( 4, this.delFlag );
            prestate.setString( 5, this.handleName );
            prestate.setInt( 6, this.point );
            prestate.setInt( 7, this.birthdayYear );
            prestate.setInt( 8, this.birthdayMonth );
            prestate.setInt( 9, this.birthdayDay );
            prestate.setInt( 10, this.sex );
            prestate.setInt( 11, this.prefCode );
            prestate.setInt( 12, this.jisCode );
            prestate.setString( 13, this.mailAddr );
            prestate.setString( 14, this.mailAddrMd5 );
            prestate.setInt( 15, this.mailAddrUnknown );
            prestate.setString( 16, this.mailAddrMobile );
            prestate.setString( 17, this.mailAddrMobileMd5 );
            prestate.setInt( 18, this.mailAddrMobileUnknown );
            prestate.setInt( 19, this.mailmagOfficial );
            prestate.setInt( 20, this.mailmagTarget );
            prestate.setString( 21, this.bloodType );
            prestate.setString( 22, this.occupation );
            prestate.setString( 23, this.nameLast );
            prestate.setString( 24, this.nameFirst );
            prestate.setString( 25, this.nameLastKana );
            prestate.setString( 26, this.nameFirstKana );
            prestate.setString( 27, this.zipCode );
            prestate.setString( 28, this.address1 );
            prestate.setString( 29, this.address2 );
            prestate.setString( 30, this.tel1 );
            prestate.setString( 31, this.tel2 );
            prestate.setString( 32, this.family );
            prestate.setString( 33, this.delReason );
            prestate.setInt( 34, this.tempDatePc );
            prestate.setInt( 35, this.tempTimePc );
            prestate.setInt( 36, this.tempDateMobile );
            prestate.setInt( 37, this.tempTimeMobile );
            prestate.setInt( 38, this.registDatePc );
            prestate.setInt( 39, this.registTimePc );
            prestate.setInt( 40, this.registDateMobile );
            prestate.setInt( 41, this.registTimeMobile );
            prestate.setInt( 42, this.loginDatePc );
            prestate.setInt( 43, this.loginTimePc );
            prestate.setInt( 44, this.loginDateMobile );
            prestate.setInt( 45, this.loginTimeMobile );
            prestate.setInt( 46, this.delDatePc );
            prestate.setInt( 47, this.delTimePc );
            prestate.setInt( 48, this.delDateMobile );
            prestate.setInt( 49, this.delTimeMobile );
            prestate.setInt( 50, this.registDate );
            prestate.setInt( 51, this.registTime );
            prestate.setString( 52, this.memo );
            prestate.setInt( 53, this.docomoFlag );
            prestate.setInt( 54, this.loginFlag );
            prestate.setInt( 55, this.mailStartTime );
            prestate.setInt( 56, this.mailEndTime );
            prestate.setInt( 57, this.campaignFlag );
            prestate.setInt( 58, this.pointUpdate );
            prestate.setInt( 59, this.buzzPointPlus );
            prestate.setInt( 60, this.buzzPointMinus );
            prestate.setInt( 61, this.registStatusPay );
            prestate.setInt( 62, this.registStatusOld );
            prestate.setInt( 63, this.pointPay );
            prestate.setString( 64, this.delReasonPay );
            prestate.setInt( 65, this.registDatePay );
            prestate.setInt( 66, this.registTimePay );
            prestate.setInt( 67, this.delDatePay );
            prestate.setInt( 68, this.delTimePay );
            prestate.setString( 69, this.accessTicket );
            prestate.setInt( 70, this.pointPayUpdate );
            prestate.setInt( 71, this.birthdayChangeFlag );
            prestate.setInt( 72, this.constellation );
            prestate.setInt( 73, this.loveConstellation );
            prestate.setInt( 74, this.flag1 );
            prestate.setInt( 75, this.flag2 );
            prestate.setInt( 76, this.flag3 );
            prestate.setInt( 77, this.smartPhoneFlag );
            prestate.setString( 78, this.address3 );
            prestate.setString( 79, userId );
            prestate.setInt( 80, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserBasicHistory.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
