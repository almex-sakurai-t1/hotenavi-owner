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

/**
 * 応募登録フォームマスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/05/12
 */
public class DataMasterRegistForm implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4280837088303986630L;
    private int               formId;
    private String            title;
    private String            titleDetail;
    private int               dispFlag;
    private int               memberFlag;
    private int               ownerFlag;
    private int               id;
    private int               availableCount;
    private int               startDate;
    private int               endDate;
    private int               userIdFlag;
    private int               userNameFlag;
    private int               userNameKanaFlag;
    private int               handleNameFlag;
    private int               birthdayFlag;
    private int               sexFlag;
    private int               ageFlag;
    private int               zipCodeFlag;
    private int               prefNameFlag;
    private int               address1Flag;
    private int               address2Flag;
    private int               tel1Flag;
    private int               tel2Flag;
    private int               mailAddrFlag;
    private int               bloodTypeFlag;
    private int               occupationFlag;
    private int               memoFlag;
    private int               familyFlag;
    private int               pointCode;
    private int               titleFlag;
    private int               contentFlag;
    private int               urlFlag;
    private int               addDate;
    private int               addTime;
    private String            userId;
    private String            ownerHotelId;
    private int               ownerUserId;

    /**
     * データを初期化します。
     */
    public DataMasterRegistForm()
    {
        formId = 0;
        title = "";
        titleDetail = "";
        dispFlag = 0;
        memberFlag = 0;
        ownerFlag = 0;
        id = 0;
        availableCount = 0;
        startDate = 0;
        endDate = 0;
        userIdFlag = 0;
        userNameFlag = 0;
        userNameKanaFlag = 0;
        handleNameFlag = 0;
        birthdayFlag = 0;
        sexFlag = 0;
        ageFlag = 0;
        zipCodeFlag = 0;
        prefNameFlag = 0;
        address1Flag = 0;
        address2Flag = 0;
        tel1Flag = 0;
        tel2Flag = 0;
        mailAddrFlag = 0;
        bloodTypeFlag = 0;
        occupationFlag = 0;
        memoFlag = 0;
        familyFlag = 0;
        pointCode = 0;
        titleFlag = 0;
        contentFlag = 0;
        urlFlag = 0;
        addDate = 0;
        addTime = 0;
        userId = "";
        ownerHotelId = "";
        ownerUserId = 0;
    }

    // ゲッター
    public int getAddDate()
    {
        return addDate;
    }

    public int getAddress1Flag()
    {
        return address1Flag;
    }

    public int getAddress2Flag()
    {
        return address2Flag;
    }

    public int getAddTime()
    {
        return addTime;
    }

    public int getAgeFlag()
    {
        return ageFlag;
    }

    public int getAvailableCount()
    {
        return availableCount;
    }

    public int getBirthDayFlag()
    {
        return birthdayFlag;
    }

    public int getBloodTypeFlag()
    {
        return bloodTypeFlag;
    }

    public int getContentFlag()
    {
        return contentFlag;
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getFamilyFlag()
    {
        return familyFlag;
    }

    public int getFormId()
    {
        return formId;
    }

    public int getHandleNameFlag()
    {
        return handleNameFlag;
    }

    public int getId()
    {
        return id;
    }

    public int getMailAddrFlag()
    {
        return mailAddrFlag;
    }

    public int getMemberFlag()
    {
        return memberFlag;
    }

    public int getMemoFlag()
    {
        return memoFlag;
    }

    public int getOccupationFlag()
    {
        return occupationFlag;
    }

    public int getOwnerFlag()
    {
        return ownerFlag;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getPointCode()
    {
        return pointCode;
    }

    public int getPrefNameFlag()
    {
        return prefNameFlag;
    }

    public int getSexFlag()
    {
        return sexFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getTel1Flag()
    {
        return tel1Flag;
    }

    public int getTel2Flag()
    {
        return tel2Flag;
    }

    public String getTitle()
    {
        return title;
    }

    public int getTitleFlag()
    {
        return titleFlag;
    }

    public String getTitleDetail()
    {
        return titleDetail;
    }

    public int getUrlFlag()
    {
        return urlFlag;
    }

    public String getUserid()
    {
        return userId;
    }

    public int getUserIdFlag()
    {
        return userIdFlag;
    }

    public int getUserNameFlag()
    {
        return userNameFlag;
    }

    public int getUserNameKanaFlag()
    {
        return userNameKanaFlag;
    }

    public int getZipCodeFlag()
    {
        return zipCodeFlag;
    }

    // セッター
    public void setAddDate(int addDate)
    {
        this.addDate = addDate;
    }

    public void setAddress1Flag(int address1Flag)
    {
        this.address1Flag = address1Flag;
    }

    public void setAddress2Flag(int address2Flag)
    {
        this.address2Flag = address2Flag;
    }

    public void setAddTime(int addTime)
    {
        this.addTime = addTime;
    }

    public void setAgeFlag(int ageFlag)
    {
        this.ageFlag = ageFlag;
    }

    public void setAvailableCount(int availableCount)
    {
        this.availableCount = availableCount;
    }

    public void setBirthdayFlag(int birthdayFlag)
    {
        this.birthdayFlag = birthdayFlag;
    }

    public void setBloodTypeFlag(int bloodTypeFlag)
    {
        this.bloodTypeFlag = bloodTypeFlag;
    }

    public void setContentFlag(int contentFlag)
    {
        this.contentFlag = contentFlag;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setFamilyFlag(int familyFlag)
    {
        this.familyFlag = familyFlag;
    }

    public void setFormId(int formId)
    {
        this.formId = formId;
    }

    public void setHandleNameFlag(int handleNameFlag)
    {
        this.handleNameFlag = handleNameFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMailAddrFlag(int mailAddrFlag)
    {
        this.mailAddrFlag = mailAddrFlag;
    }

    public void setMemberFlag(int memberFlag)
    {
        this.memberFlag = memberFlag;
    }

    public void setMemoFlag(int memoFlag)
    {
        this.memoFlag = memoFlag;
    }

    public void setOccupationFlag(int occupationFlag)
    {
        this.occupationFlag = occupationFlag;
    }

    public void setOwnerFlag(int ownerFlag)
    {
        this.ownerFlag = ownerFlag;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerHotelUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setPointCode(int pointCode)
    {
        this.pointCode = pointCode;
    }

    public void setPrefNameFlag(int prefNameFlag)
    {
        this.prefNameFlag = prefNameFlag;
    }

    public void setSexFlag(int sexFlag)
    {
        this.sexFlag = sexFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTel1Flag(int tel1Flag)
    {
        this.tel1Flag = tel1Flag;
    }

    public void setTel2Flag(int tel2Flag)
    {
        this.tel2Flag = tel2Flag;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTitleFlag(int titleFlag)
    {
        this.titleFlag = titleFlag;
    }

    public void setTitleDetail(String titleDetail)
    {
        this.titleDetail = titleDetail;
    }

    public void setUrlFlag(int urlFlag)
    {
        this.urlFlag = urlFlag;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserIdFlag(int userIdFlag)
    {
        this.userIdFlag = userIdFlag;
    }

    public void setUserNameFlag(int userNameFlag)
    {
        this.userNameFlag = userNameFlag;
    }

    public void setUserNameKanaFlag(int userNameKanaFlag)
    {
        this.userNameKanaFlag = userNameKanaFlag;
    }

    public void setZipCodeFlag(int zipCodeFlag)
    {
        this.zipCodeFlag = zipCodeFlag;
    }

    /**
     * 応募登録フォームマスタデータ取得
     * 
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int formId)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_regist_form WHERE form_id= ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            result = prestate.executeQuery();
            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.title = result.getString( "title" );
                    this.titleDetail = result.getString( "title_detail" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.ownerFlag = result.getInt( "owner_flag" );
                    this.id = result.getInt( "id" );
                    this.availableCount = result.getInt( "available_count" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.userIdFlag = result.getInt( "user_id_flag" );
                    this.userNameFlag = result.getInt( "user_name_flag" );
                    this.userNameKanaFlag = result.getInt( "user_name_kana_flag" );
                    this.handleNameFlag = result.getInt( "handle_name_flag" );
                    this.birthdayFlag = result.getInt( "birthday_flag" );
                    this.sexFlag = result.getInt( "sex_flag" );
                    this.ageFlag = result.getInt( "age_flag" );
                    this.zipCodeFlag = result.getInt( "zip_code_flag" );
                    this.prefNameFlag = result.getInt( "pref_name_flag" );
                    this.address1Flag = result.getInt( "address1_flag" );
                    this.address2Flag = result.getInt( "address2_flag" );
                    this.tel1Flag = result.getInt( "tel1_flag" );
                    this.tel2Flag = result.getInt( "tel2_flag" );
                    this.mailAddrFlag = result.getInt( "mail_addr_flag" );
                    this.bloodTypeFlag = result.getInt( "blood_type_flag" );
                    this.occupationFlag = result.getInt( "occupation_flag" );
                    this.memoFlag = result.getInt( "memo_flag" );
                    this.familyFlag = result.getInt( "family_flag" );
                    this.pointCode = result.getInt( "point_code" );
                    this.titleFlag = result.getInt( "title_flag" );
                    this.contentFlag = result.getInt( "content_flag" );
                    this.urlFlag = result.getInt( "url_flag" );
                    this.addDate = result.getInt( "add_date" );
                    this.addTime = result.getInt( "add_time" );
                    this.userId = result.getString( "user_id" );
                    this.ownerHotelId = result.getString( "owner_hotelid" );
                    this.ownerUserId = result.getInt( "owner_userid" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistForm.getData] Exception=" + e.toString() );
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
     * 応募登録フォームマスタデータ取得
     * 
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMaxData()
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT MAX(form_id), hh_master_regist_form.* FROM hh_master_regist_form";
        query = query + " GROUP BY(form_id) ORDER BY form_id DESC ";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.title = result.getString( "title" );
                    this.titleDetail = result.getString( "title_detail" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.memberFlag = result.getInt( "member_flag" );
                    this.ownerFlag = result.getInt( "owner_flag" );
                    this.id = result.getInt( "id" );
                    this.availableCount = result.getInt( "available_count" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.userIdFlag = result.getInt( "user_id_flag" );
                    this.userNameFlag = result.getInt( "user_name_flag" );
                    this.userNameKanaFlag = result.getInt( "user_name_kana_flag" );
                    this.handleNameFlag = result.getInt( "handle_name_flag" );
                    this.birthdayFlag = result.getInt( "birthday_flag" );
                    this.sexFlag = result.getInt( "sex_flag" );
                    this.ageFlag = result.getInt( "age_flag" );
                    this.zipCodeFlag = result.getInt( "zip_code_flag" );
                    this.prefNameFlag = result.getInt( "pref_name_flag" );
                    this.address1Flag = result.getInt( "address1_flag" );
                    this.address2Flag = result.getInt( "address2_flag" );
                    this.tel1Flag = result.getInt( "tel1_flag" );
                    this.tel2Flag = result.getInt( "tel2_flag" );
                    this.mailAddrFlag = result.getInt( "mail_addr_flag" );
                    this.bloodTypeFlag = result.getInt( "blood_type_flag" );
                    this.occupationFlag = result.getInt( "occupation_flag" );
                    this.memoFlag = result.getInt( "memo_flag" );
                    this.familyFlag = result.getInt( "family_flag" );
                    this.pointCode = result.getInt( "point_code" );
                    this.titleFlag = result.getInt( "title_flag" );
                    this.contentFlag = result.getInt( "content_flag" );
                    this.urlFlag = result.getInt( "url_flag" );
                    this.addDate = result.getInt( "add_date" );
                    this.addTime = result.getInt( "add_time" );
                    this.userId = result.getString( "user_id" );
                    this.ownerHotelId = result.getString( "owner_hotelid" );
                    this.ownerUserId = result.getInt( "owner_userid" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistForm.getData] Exception=" + e.toString() );
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
     * 応募登録フォームマスタデータ設定
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
                this.title = result.getString( "title" );
                this.titleDetail = result.getString( "title_detail" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.memberFlag = result.getInt( "member_flag" );
                this.ownerFlag = result.getInt( "owner_flag" );
                this.id = result.getInt( "id" );
                this.availableCount = result.getInt( "available_count" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.userIdFlag = result.getInt( "user_id_flag" );
                this.userNameFlag = result.getInt( "user_name_flag" );
                this.userNameKanaFlag = result.getInt( "user_name_kana_flag" );
                this.handleNameFlag = result.getInt( "handle_name_flag" );
                this.birthdayFlag = result.getInt( "birthday_flag" );
                this.sexFlag = result.getInt( "sex_flag" );
                this.ageFlag = result.getInt( "age_flag" );
                this.zipCodeFlag = result.getInt( "zip_code_flag" );
                this.prefNameFlag = result.getInt( "pref_name_flag" );
                this.address1Flag = result.getInt( "address1_flag" );
                this.address2Flag = result.getInt( "address2_flag" );
                this.tel1Flag = result.getInt( "tel1_flag" );
                this.tel2Flag = result.getInt( "tel2_flag" );
                this.mailAddrFlag = result.getInt( "mail_addr_flag" );
                this.bloodTypeFlag = result.getInt( "blood_type_flag" );
                this.occupationFlag = result.getInt( "occupation_flag" );
                this.memoFlag = result.getInt( "memo_flag" );
                this.familyFlag = result.getInt( "family_flag" );
                this.pointCode = result.getInt( "point_code" );
                this.titleFlag = result.getInt( "title_flag" );
                this.contentFlag = result.getInt( "content_flag" );
                this.urlFlag = result.getInt( "url_flag" );
                this.addDate = result.getInt( "add_date" );
                this.addTime = result.getInt( "add_time" );
                this.userId = result.getString( "user_id" );
                this.ownerHotelId = result.getString( "owner_hotelid" );
                this.ownerUserId = result.getInt( "owner_userid" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistForm.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 応募登録フォームマスタデータ設定
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

        query = "INSERT hh_master_regist_form SET";
        query = query + " form_id = ?,";
        query = query + " title = ?,";
        query = query + " title_detail = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " owner_flag = ?,";
        query = query + " id = ?,";
        query = query + " available_count = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " user_id_flag = ?,";
        query = query + " user_name_flag = ?,";
        query = query + " user_name_kana_flag = ?,";
        query = query + " handle_name_flag = ?,";
        query = query + " birthday_flag = ?,";
        query = query + " sex_flag = ?,";
        query = query + " age_flag = ?,";
        query = query + " zip_code_flag = ?,";
        query = query + " pref_name_flag = ?,";
        query = query + " address1_flag = ?,";
        query = query + " address2_flag = ?,";
        query = query + " tel1_flag = ?,";
        query = query + " tel2_flag = ?,";
        query = query + " mail_addr_flag = ?,";
        query = query + " blood_type_flag = ?,";
        query = query + " occupation_flag = ?,";
        query = query + " memo_flag = ?,";
        query = query + " family_flag= ?,";
        query = query + " point_code = ?,";
        query = query + " title_flag = ?,";
        query = query + " content_flag = ?,";
        query = query + " url_flag = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?,";
        query = query + " user_id = ?,";
        query = query + " owner_hotelid = ?,";
        query = query + " owner_userid = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.formId );
            prestate.setString( 2, this.title );
            prestate.setString( 3, this.titleDetail );
            prestate.setInt( 4, this.dispFlag );
            prestate.setInt( 5, this.memberFlag );
            prestate.setInt( 6, this.ownerFlag );
            prestate.setInt( 7, this.id );
            prestate.setInt( 8, this.availableCount );
            prestate.setInt( 9, this.startDate );
            prestate.setInt( 10, this.endDate );
            prestate.setInt( 11, this.userIdFlag );
            prestate.setInt( 12, this.userNameFlag );
            prestate.setInt( 13, this.userNameKanaFlag );
            prestate.setInt( 14, this.handleNameFlag );
            prestate.setInt( 15, this.birthdayFlag );
            prestate.setInt( 16, this.sexFlag );
            prestate.setInt( 17, this.ageFlag );
            prestate.setInt( 18, this.zipCodeFlag );
            prestate.setInt( 19, this.prefNameFlag );
            prestate.setInt( 20, this.address1Flag );
            prestate.setInt( 21, this.address2Flag );
            prestate.setInt( 22, this.tel1Flag );
            prestate.setInt( 23, this.tel2Flag );
            prestate.setInt( 24, this.mailAddrFlag );
            prestate.setInt( 25, this.bloodTypeFlag );
            prestate.setInt( 26, this.occupationFlag );
            prestate.setInt( 27, this.memoFlag );
            prestate.setInt( 28, this.familyFlag );
            prestate.setInt( 29, this.pointCode );
            prestate.setInt( 30, this.titleFlag );
            prestate.setInt( 31, this.contentFlag );
            prestate.setInt( 32, this.urlFlag );
            prestate.setInt( 33, this.addDate );
            prestate.setInt( 34, this.addTime );
            prestate.setString( 35, this.userId );
            prestate.setString( 36, this.ownerHotelId );
            prestate.setInt( 37, this.ownerUserId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistForm.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 応募登録フォームマスタデータ設定
     * 
     * @param formId フォームID
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int formId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_regist_form SET";
        query = query + " title = ?,";
        query = query + " title_detail = ?,";
        query = query + " disp_flag = ?,";
        query = query + " member_flag = ?,";
        query = query + " owner_flag = ?,";
        query = query + " id = ?,";
        query = query + " available_count = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " user_id_flag = ?,";
        query = query + " user_name_flag = ?,";
        query = query + " user_name_kana_flag = ?,";
        query = query + " handle_name_flag = ?,";
        query = query + " birthday_flag = ?,";
        query = query + " sex_flag = ?,";
        query = query + " age_flag = ?,";
        query = query + " zip_code_flag = ?,";
        query = query + " pref_name_flag = ?,";
        query = query + " address1_flag = ?,";
        query = query + " address2_flag = ?,";
        query = query + " tel1_flag = ?,";
        query = query + " tel2_flag = ?,";
        query = query + " mail_addr_flag = ?,";
        query = query + " blood_type_flag = ?,";
        query = query + " occupation_flag = ?,";
        query = query + " memo_flag = ?,";
        query = query + " family_flag= ?,";
        query = query + " point_code = ?,";
        query = query + " title_flag = ?,";
        query = query + " content_flag = ?,";
        query = query + " url_flag = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?,";
        query = query + " user_id = ?,";
        query = query + " owner_hotelid = ?,";
        query = query + " owner_userid = ?";
        query = query + " WHERE form_id = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.title );
            prestate.setString( 2, this.titleDetail );
            prestate.setInt( 3, this.dispFlag );
            prestate.setInt( 4, this.memberFlag );
            prestate.setInt( 5, this.ownerFlag );
            prestate.setInt( 6, this.id );
            prestate.setInt( 7, this.availableCount );
            prestate.setInt( 8, this.startDate );
            prestate.setInt( 9, this.endDate );
            prestate.setInt( 10, this.userIdFlag );
            prestate.setInt( 11, this.userNameFlag );
            prestate.setInt( 12, this.userNameKanaFlag );
            prestate.setInt( 13, this.handleNameFlag );
            prestate.setInt( 14, this.birthdayFlag );
            prestate.setInt( 15, this.sexFlag );
            prestate.setInt( 16, this.ageFlag );
            prestate.setInt( 17, this.zipCodeFlag );
            prestate.setInt( 18, this.prefNameFlag );
            prestate.setInt( 19, this.address1Flag );
            prestate.setInt( 20, this.address2Flag );
            prestate.setInt( 21, this.tel1Flag );
            prestate.setInt( 22, this.tel2Flag );
            prestate.setInt( 23, this.mailAddrFlag );
            prestate.setInt( 24, this.bloodTypeFlag );
            prestate.setInt( 25, this.occupationFlag );
            prestate.setInt( 26, this.memoFlag );
            prestate.setInt( 27, this.familyFlag );
            prestate.setInt( 28, this.pointCode );
            prestate.setInt( 29, this.titleFlag );
            prestate.setInt( 30, this.contentFlag );
            prestate.setInt( 31, this.urlFlag );
            prestate.setInt( 32, this.addDate );
            prestate.setInt( 33, this.addTime );
            prestate.setString( 34, this.userId );
            prestate.setString( 35, this.ownerHotelId );
            prestate.setInt( 36, this.ownerUserId );
            prestate.setInt( 37, formId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistForm.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 応募登録フォームマスタデータ削除
     * 
     * @param formId フォームID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(int formId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_master_regist_form ";
        query = query + " WHERE form_id = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, formId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterRegistForm.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
