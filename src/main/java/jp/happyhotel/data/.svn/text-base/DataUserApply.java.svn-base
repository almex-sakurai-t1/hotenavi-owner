/*
 * @(#)DataMasterRegistForm.java 1.00 2008/05/12 Copyright (C) ALMEX Inc. 2007 アンケートマスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

/**
 * 応募状況データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataUserApply implements Serializable
{

    /**
	 *
	 */
    private static final long serialVersionUID = -6556936781237079672L;
    private int               formId;
    private int               seq;
    private String            userId;
    private String            termNo;
    private String            userName;
    private String            userNameKana;
    private String            handleName;
    private int               birthdayYear;
    private int               birthdayMonth;
    private int               birthdayDay;
    private int               sex;
    private int               age;
    private String            zipCode;
    private String            prefName;
    private String            address1;
    private String            address2;
    private String            tel1;
    private String            tel2;
    private String            mailAddr;
    private String            bloodType;
    private String            occupation;
    private String            memo;
    private String            family;
    private int               inputDate;
    private int               inputTime;
    private int               statusFlag;
    private String            title;
    private String            content;
    private String            url;

    /**
     * データを初期化します。
     */
    public DataUserApply()
    {
        formId = 0;
        seq = 0;
        userId = "";
        termNo = "";
        userName = "";
        userNameKana = "";
        handleName = "";
        birthdayYear = 0;
        birthdayMonth = 0;
        birthdayDay = 0;
        sex = 0;
        age = 0;
        zipCode = "";
        prefName = "";
        address1 = "";
        address2 = "";
        tel1 = "";
        tel2 = "";
        mailAddr = "";
        bloodType = "";
        occupation = "";
        memo = "";
        family = "";
        inputDate = 0;
        inputTime = 0;
        statusFlag = 0;
        title = "";
        content = "";
        url = "";
    }

    // ゲッター
    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public int getAge()
    {
        return age;
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

    public String getContent()
    {
        return content;
    }

    public String getFamily()
    {
        return family;
    }

    public int getFormId()
    {
        return formId;
    }

    public String getHandleName()
    {
        return handleName;
    }

    public int getInputDate()
    {
        return inputDate;
    }

    public int getInputTime()
    {
        return inputTime;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getMemo()
    {
        return memo;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public String getUrl()
    {
        return Url.convertUrl( url );
    }

    public int getSeq()
    {
        return seq;
    }

    public int getSex()
    {
        return sex;
    }

    public int getStatusFlag()
    {
        return statusFlag;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public String getTermNo()
    {
        return termNo;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserNameKana()
    {
        return userNameKana;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    // セッター
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setAge(int age)
    {
        this.age = age;
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

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setFamily(String family)
    {
        this.family = family;
    }

    public void setFormId(int formId)
    {
        this.formId = formId;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setInputDate(int inputDate)
    {
        this.inputDate = inputDate;
    }

    public void setInputTime(int inputTime)
    {
        this.inputTime = inputTime;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setSeq(int seq)
    {
        this.sex = seq;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setStatusFlag(int statusFlag)
    {
        this.statusFlag = statusFlag;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setTermNo(String termNo)
    {
        this.termNo = termNo;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setUserNameKana(String userNameKana)
    {
        this.userNameKana = userNameKana;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * 応募状況データデータ取得
     * 
     * @param userId ユーザーID
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int formId)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_apply WHERE user_id = ?";
        query = query + " AND form_id = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, formId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.seq = result.getInt( "seq" );
                    this.userId = result.getString( "user_id" );
                    this.termNo = result.getString( "term_no" );
                    this.userName = result.getString( "user_name" );
                    this.userNameKana = result.getString( "user_name_kana" );
                    this.handleName = result.getString( "handle_name" );
                    this.birthdayYear = result.getInt( "birthday_year" );
                    this.birthdayMonth = result.getInt( "birthday_month" );
                    this.birthdayDay = result.getInt( "birthday_day" );
                    this.sex = result.getInt( "sex" );
                    this.age = result.getInt( "age" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefName = result.getString( "pref_name" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.bloodType = result.getString( "blood_type" );
                    this.occupation = result.getString( "occupation" );
                    this.memo = result.getString( "memo" );
                    this.family = result.getString( "family" );
                    this.inputDate = result.getInt( "input_date" );
                    this.inputTime = result.getInt( "input_time" );
                    this.statusFlag = result.getInt( "status_flag" );
                    this.title = result.getString( "title" );
                    this.content = result.getString( "cointent" );
                    this.url = result.getString( "url" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.error( "[DataUserApply.getData] count=" + count );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserApply.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
            return(true);
        else
            return(false);
    }

    /**
     * 応募状況データデータ設定
     * 
     * @param result 賞品管理マスタデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.formId = result.getInt( "form_id" );
                this.seq = result.getInt( "seq" );
                this.userId = result.getString( "user_id" );
                this.termNo = result.getString( "term_no" );
                this.userName = result.getString( "user_name" );
                this.userNameKana = result.getString( "user_name_kana" );
                this.handleName = result.getString( "handle_name" );
                this.birthdayYear = result.getInt( "birthday_year" );
                this.birthdayMonth = result.getInt( "birthday_month" );
                this.birthdayDay = result.getInt( "birthday_day" );
                this.sex = result.getInt( "sex" );
                this.age = result.getInt( "age" );
                this.zipCode = result.getString( "zip_code" );
                this.prefName = result.getString( "pref_name" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.mailAddr = result.getString( "mail_addr" );
                this.bloodType = result.getString( "blood_type" );
                this.occupation = result.getString( "occupation" );
                this.memo = result.getString( "memo" );
                this.family = result.getString( "family" );
                this.inputDate = result.getInt( "input_date" );
                this.inputTime = result.getInt( "input_time" );
                this.statusFlag = result.getInt( "status_flag" );
                this.title = result.getString( "title" );
                this.content = result.getString( "content" );
                this.url = result.getString( "url" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserApply.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 応募状況データデータ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "INSERT hh_user_apply SET";
        query = query + " form_id = ?,";
        query = query + " seq = 0,";
        query = query + " user_id = ?,";
        query = query + " term_no = ?,";
        query = query + " user_name = ?,";
        query = query + " user_name_kana = ?,";
        query = query + " handle_name = ?,";
        query = query + " birthday_year = ?,";
        query = query + " birthday_month = ?,";
        query = query + " birthday_day = ?,";
        query = query + " sex = ?,";
        query = query + " age = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " mail_addr = ?,";
        query = query + " blood_type = ?,";
        query = query + " occupation = ?,";
        query = query + " memo = ?,";
        query = query + " family= ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " status_flag = ?,";
        query = query + " title = ?,";
        query = query + " content = ?,";
        query = query + " url = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.formId );
            prestate.setString( 2, this.userId );
            prestate.setString( 3, this.termNo );
            prestate.setString( 4, this.userName );
            prestate.setString( 5, this.userNameKana );
            prestate.setString( 6, this.handleName );
            prestate.setInt( 7, this.birthdayYear );
            prestate.setInt( 8, this.birthdayMonth );
            prestate.setInt( 9, this.birthdayDay );
            prestate.setInt( 10, this.sex );
            prestate.setInt( 11, this.age );
            prestate.setString( 12, this.zipCode );
            prestate.setString( 13, this.prefName );
            prestate.setString( 14, this.address1 );
            prestate.setString( 15, this.address2 );
            prestate.setString( 16, this.tel1 );
            prestate.setString( 17, this.tel2 );
            prestate.setString( 18, this.mailAddr );
            prestate.setString( 19, this.bloodType );
            prestate.setString( 20, this.occupation );
            prestate.setString( 21, this.memo );
            prestate.setString( 22, this.family );
            prestate.setInt( 23, this.inputDate );
            prestate.setInt( 24, this.inputTime );
            prestate.setInt( 25, this.statusFlag );
            prestate.setString( 26, this.title );
            prestate.setString( 27, this.content );
            prestate.setString( 28, this.url );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterDecome.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 応募状況データデータ設定
     * 
     * @param formId フォームID
     * @param seq 管理番号
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int formId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_user_apply SET";
        query = query + " user_id = ?,";
        query = query + " term_no = ?,";
        query = query + " user_name = ?,";
        query = query + " user_name_kana = ?,";
        query = query + " handle_name = ?,";
        query = query + " birthday_year = ?,";
        query = query + " birthday_month = ?,";
        query = query + " birthday_day = ?,";
        query = query + " sex = ?,";
        query = query + " age = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " mail_addr = ?,";
        query = query + " blood_type = ?,";
        query = query + " occupation = ?,";
        query = query + " memo = ?,";
        query = query + " family = ?,";
        query = query + " input_date = ?,";
        query = query + " input_time = ?,";
        query = query + " status_flag = ?,";
        query = query + " title = ?,";
        query = query + " content = ?,";
        query = query + " url = ?";
        query = query + " WHERE form_id = ?";
        query = query + " AND seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.termNo );
            prestate.setString( 3, this.userName );
            prestate.setString( 4, this.userNameKana );
            prestate.setString( 5, this.handleName );
            prestate.setInt( 6, this.birthdayYear );
            prestate.setInt( 7, this.birthdayMonth );
            prestate.setInt( 8, this.birthdayDay );
            prestate.setInt( 9, this.sex );
            prestate.setInt( 10, this.age );
            prestate.setString( 11, this.zipCode );
            prestate.setString( 12, this.prefName );
            prestate.setString( 13, this.address1 );
            prestate.setString( 14, this.address2 );
            prestate.setString( 15, this.tel1 );
            prestate.setString( 16, this.tel2 );
            prestate.setString( 17, this.mailAddr );
            prestate.setString( 18, this.bloodType );
            prestate.setString( 19, this.occupation );
            prestate.setString( 20, this.memo );
            prestate.setString( 21, this.family );
            prestate.setInt( 22, this.inputDate );
            prestate.setInt( 23, this.inputTime );
            prestate.setInt( 24, this.statusFlag );
            prestate.setString( 25, this.title );
            prestate.setString( 26, this.content );
            prestate.setString( 27, this.url );
            prestate.setInt( 28, formId );
            prestate.setInt( 29, seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserApply.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
