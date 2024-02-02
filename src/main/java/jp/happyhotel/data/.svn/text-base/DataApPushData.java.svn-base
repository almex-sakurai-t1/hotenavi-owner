package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * プッシュ配信データ(ap_push_data)取得クラス
 *
 * @author Takeshi.Sakurai
 * @version 1.00 2014/9/11
 */
public class DataApPushData implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 1393028357603346918L;
    public static final String TABLE            = "ap_push_data";
    private int                pushSeq;                                // プッシュ配信連番
    private int                campaignId;                             // キャンペーンID
    private int                status;                                 // 0:仮登録，1:配信希望, 2:配信承認, 3:配信 10:配信却下
    private int                delFlag;                                // 1:削除
    private int                desiredDate;                            // PUSH希望日(YYYYMMDD)
    private int                desiredTime;                            // PUSH希望時刻(HHMMSS)
    private int                desiredCount;                           // PUSH希望件数
    private int                pushDate;                               // PUSH配信日(YYYYMMDD)
    private int                pushTime;                               // PUSH配信時刻(HHMMSS)
    private int                pushCount;                              // PUSH件数
    private int                conditionSex;                           //性別条件(9:なし,1:男,2:女）
    private int                conditionAgeFrom;                       //年齢以上条件（0:なし）
    private int                conditionAgeTo;                         //年齢以下条件（99:なし）
    private int                conditionBirthday;                      //YYYYMMDD（それぞれで0は条件なし）
    private String             conditionArea;                          //住まい条件（,区切り）
    private String             conditionMyarea;                        //マイエリア条件（,区切り）
    private String             conditionMyhotel;                       //マイホテル条件（,区切り）
    private String             conditionUserId;                        //ユーザーID指定（,区切り）
    private String             ownerHotelId;                           // 更新契約ホテルID
    private int                ownerUserId;                            // 更新オーナーユーザーID
    private int                registDate;                             // 登録日付(YYYYMMDD)
    private int                registTime;                             // 登録時刻(HHMMSS)
    private int                companyId;                              // 承認会社ID:2
    private String             staffId;                                // 承認社員ID
    private int                approvalDate;                           // 承認日付(YYYYMMDD)
    private int                approvalTime;                           // 承認時刻(HHMMSS)
    private int                apliKind;                              // 1:ハピホテアプリ,10:予約アプリ

    /**
     * データを初期化します。
     */
    public DataApPushData()
    {
        this.pushSeq = 0;
        this.campaignId = 0;
        this.status = 0;
        this.delFlag = 0;
        this.desiredDate = 0;
        this.desiredTime = 0;
        this.desiredCount = 0;
        this.pushDate = 0;
        this.pushTime = 0;
        this.pushCount = 0;
        this.conditionSex = 0;
        this.conditionAgeFrom = 0;
        this.conditionAgeTo = 0;
        this.conditionBirthday = 0;
        this.conditionArea = "";
        this.conditionMyarea = "";
        this.conditionMyhotel = "";
        this.conditionUserId = "";
        this.ownerHotelId = "";
        this.ownerUserId = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.companyId = 0;
        this.staffId = "";
        this.approvalDate = 0;
        this.approvalTime = 0;
        this.apliKind = 0;
    }

    public int getPushSeq()
    {
        return pushSeq;
    }

    public int getCampaignId()
    {
        return campaignId;
    }

    public int getStatus()
    {
        return status;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getDesiredDate()
    {
        return desiredDate;
    }

    public int getDesiredTime()
    {
        return desiredTime;
    }

    public int getDesiredCount()
    {
        return desiredCount;
    }

    public int getPushDate()
    {
        return pushDate;
    }

    public int getPushTime()
    {
        return pushTime;
    }

    public int getPushCount()
    {
        return pushCount;
    }

    public int getConditionSex()
    {
        return conditionSex;
    }

    public int geConditionAgeFrom()
    {
        return conditionAgeFrom;
    }

    public int getConditionAgeTo()
    {
        return conditionAgeTo;
    }

    public int getConditionBirthday()
    {
        return conditionBirthday;
    }

    public String geConditionArea()
    {
        return conditionArea;
    }

    public String getConditionMyarea()
    {
        return conditionMyarea;
    }

    public String getConditionMyhotel()
    {
        return conditionMyhotel;
    }

    public String getConditionUserId()
    {
        return conditionUserId;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public String getStaffId()
    {
        return staffId;
    }

    public int getApprovalDate()
    {
        return approvalDate;
    }

    public int getApprovalTime()
    {
        return approvalTime;
    }

    public int getApliKind()
    {
        return apliKind;
    }

    public void setPushSeq(int pushSeq)
    {
        this.pushSeq = pushSeq;
    }

    public void setCampaignId(int campaignId)
    {
        this.campaignId = campaignId;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setDesiredDate(int desiredDate)
    {
        this.desiredDate = desiredDate;
    }

    public void setDesiredTime(int desiredTime)
    {
        this.desiredTime = desiredTime;
    }

    public void setDesiredCount(int desiredCount)
    {
        this.desiredCount = desiredCount;
    }

    public void setPushDate(int pushDate)
    {
        this.pushDate = pushDate;
    }

    public void setPushTime(int pushTime)
    {
        this.pushTime = pushTime;
    }

    public void setPushCount(int pushCount)
    {
        this.pushCount = pushCount;
    }

    public void setConditionSex(int conditionSex)
    {
        this.conditionSex = conditionSex;
    }

    public void setConditionAgeFrom(int conditionAgeFrom)
    {
        this.conditionAgeFrom = conditionAgeFrom;
    }

    public void setConditionAgeTo(int conditionAgeTo)
    {
        this.conditionAgeTo = conditionAgeTo;
    }

    public void setConditionBirthday(int conditionBirthday)
    {
        this.conditionBirthday = conditionBirthday;
    }

    public void setConditionArea(String conditionArea)
    {
        this.conditionArea = conditionArea;
    }

    public void setConditionMyareat(String conditionMyarea)
    {
        this.conditionMyarea = conditionMyarea;
    }

    public void setConditionMyhotel(String conditionMyhotel)
    {
        this.conditionMyhotel = conditionMyhotel;
    }

    public void setConditionUserId(String conditionUserId)
    {
        this.conditionUserId = conditionUserId;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public void setStaffId(String staffId)
    {
        this.staffId = staffId;
    }

    public void setApprovalDate(int approvalDate)
    {
        this.approvalDate = approvalDate;
    }

    public void setApprovalTime(int approvalTime)
    {
        this.approvalTime = approvalTime;
    }

    public void setApliKind(int apliKind)
    {
        this.apliKind = apliKind;
    }

    /****
     * プッシュ配信データ(ap_push_data)取得
     *
     * @param pushSeq プッシュ配信連番
     * @return
     */
    public boolean getData(int pushSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_data WHERE push_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, pushSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.pushSeq = result.getInt( "push_seq" );
                    this.campaignId = result.getInt( "campaign_id" );
                    this.status = result.getInt( "status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.desiredDate = result.getInt( "desired_date" );
                    this.desiredTime = result.getInt( "desired_time" );
                    this.desiredCount = result.getInt( "desired_count" );
                    this.pushDate = result.getInt( "push_date" );
                    this.pushTime = result.getInt( "push_time" );
                    this.pushCount = result.getInt( "push_count" );
                    this.conditionSex  = result.getInt( "condition_sex" );
                    this.conditionAgeFrom  = result.getInt( "condition_age_from" );
                    this.conditionAgeTo  = result.getInt( "condition_age_to" );
                    this.conditionBirthday  = result.getInt( "condition_birthday" );
                    this.conditionArea = result.getString( "condition_area" );
                    this.conditionMyarea = result.getString( "condition_myarea" );
                    this.conditionMyhotel = result.getString( "condition_myhotel" );
                    this.conditionUserId = result.getString( "condition_user_id" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.companyId = result.getInt( "company_id" );
                    this.staffId = result.getString( "staff_id" );
                    this.approvalDate = result.getInt( "approval_date" );
                    this.approvalTime = result.getInt( "approval_time" );
                    this.apliKind = result.getInt( "apli_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.getData] Exception=" + e.toString() ,"DataApPushData.getData");
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * プッシュ配信データ(ap_push_data)取得
     *
     * @param connection コネクション
     * @param pushSeq プッシュ配信連番
     * @return
     */
    public boolean getData(Connection connection, int pushSeq )
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_data WHERE push_seq = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, pushSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.pushSeq = result.getInt( "push_seq" );
                    this.campaignId = result.getInt( "campaign_id" );
                    this.status = result.getInt( "status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.desiredDate = result.getInt( "desired_date" );
                    this.desiredTime = result.getInt( "desired_time" );
                    this.desiredCount = result.getInt( "desired_count" );
                    this.pushDate = result.getInt( "push_date" );
                    this.pushTime = result.getInt( "push_time" );
                    this.pushCount = result.getInt( "push_count" );
                    this.conditionSex  = result.getInt( "condition_sex" );
                    this.conditionAgeFrom  = result.getInt( "condition_age_from" );
                    this.conditionAgeTo  = result.getInt( "condition_age_to" );
                    this.conditionBirthday  = result.getInt( "condition_birthday" );
                    this.conditionArea = result.getString( "condition_area" );
                    this.conditionMyarea = result.getString( "condition_myarea" );
                    this.conditionMyhotel = result.getString( "condition_myhotel" );
                    this.conditionUserId = result.getString( "condition_user_id" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.companyId = result.getInt( "company_id" );
                    this.staffId = result.getString( "staff_id" );
                    this.approvalDate = result.getInt( "approval_date" );
                    this.approvalTime = result.getInt( "approval_time" );
                    this.apliKind = result.getInt( "apli_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.getData] Exception=" + e.toString() ,"DataApPushData.getData");
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * プッシュ配信データ(ap_push_data)設定
     *
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.pushSeq = result.getInt( "push_seq" );
                this.campaignId = result.getInt( "campaign_id" );
                this.status = result.getInt( "status" );
                this.delFlag = result.getInt( "del_flag" );
                this.desiredDate = result.getInt( "desired_date" );
                this.desiredTime = result.getInt( "desired_time" );
                this.desiredCount = result.getInt( "desired_count" );
                this.pushDate = result.getInt( "push_date" );
                this.pushTime = result.getInt( "push_time" );
                this.pushCount = result.getInt( "push_count" );
                this.conditionSex  = result.getInt( "condition_sex" );
                this.conditionAgeFrom  = result.getInt( "condition_age_from" );
                this.conditionAgeTo  = result.getInt( "condition_age_to" );
                this.conditionBirthday  = result.getInt( "condition_birthday" );
                this.conditionArea = result.getString( "condition_area" );
                this.conditionMyarea = result.getString( "condition_myarea" );
                this.conditionMyhotel = result.getString( "condition_myhotel" );
                this.conditionUserId = result.getString( "condition_user_id" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.companyId = result.getInt( "company_id" );
                this.staffId = result.getString( "staff_id" );
                this.approvalDate = result.getInt( "approval_date" );
                this.approvalTime = result.getInt( "approval_time" );
                this.apliKind = result.getInt( "apli_kind" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * プッシュ配信データ(ap_push_data)挿入
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_push_data SET ";
        query += " push_seq=?";
        query += ", campaign_id=?";
        query += ", status=?";
        query += ", del_flag=?";
        query += ", desired_date=?";
        query += ", desired_time=?";
        query += ", push_date=?";
        query += ", push_time=?";

        query += ", push_count=?";
        query += ", condition_sex=?";
        query += ", condition_age_from=?";
        query += ", condition_age_to=?";
        query += ", condition_birthday=?";
        query += ", condition_area=?";
        query += ", condition_myarea=?";
        query += ", condition_myhotel=?";
        query += ", condition_user_id=?";


        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += ", apli_kind=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.pushSeq );
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.desiredDate );
            prestate.setInt( i++, this.desiredTime );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );

            prestate.setInt( i++, this.conditionSex );
            prestate.setInt( i++, this.conditionAgeFrom );
            prestate.setInt( i++, this.conditionAgeTo );
            prestate.setInt( i++, this.conditionBirthday );
            prestate.setString( i++, this.conditionArea );
            prestate.setString( i++, this.conditionMyarea );
            prestate.setString( i++, this.conditionMyhotel );
            prestate.setString( i++, this.conditionUserId );

            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, this.apliKind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * プッシュ配信データ(ap_push_data)更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param pushSeq プッシュ配信連番
     * @return
     */
    public boolean updateData(int pushSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_data SET ";
        query += " campaign_id=?";
        query += ", status=?";
        query += ", del_flag=?";
        query += ", desired_date=?";
        query += ", desired_time=?";
        query += ", push_date=?";
        query += ", push_time=?";
        query += ", push_count=?";
        query += ", condition_sex=?";
        query += ", condition_age_from=?";
        query += ", condition_age_to=?";
        query += ", condition_birthday=?";
        query += ", condition_area=?";
        query += ", condition_myarea=?";
        query += ", condition_myhotel=?";
        query += ", condition_user_id=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += ", apli_kind=?";
        query += " WHERE push_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.desiredDate );
            prestate.setInt( i++, this.desiredTime );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.pushCount );
            prestate.setInt( i++, this.conditionSex );
            prestate.setInt( i++, this.conditionAgeFrom );
            prestate.setInt( i++, this.conditionAgeTo );
            prestate.setInt( i++, this.conditionBirthday );
            prestate.setString( i++, this.conditionArea );
            prestate.setString( i++, this.conditionMyarea );
            prestate.setString( i++, this.conditionMyhotel );
            prestate.setString( i++, this.conditionUserId );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, this.apliKind );
            prestate.setInt( i++, this.pushSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.updateData] Exception=" + e.toString(),"DataApPushData" );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * プッシュ配信データ(ap_push_data)更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param connection コネクション
     * @param pushSeq プッシュ配信連番
     * @return
     */
    public boolean updateData(Connection connection, int pushSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_data SET ";
        query += " campaign_id=?";
        query += ", status=?";
        query += ", del_flag=?";
        query += ", desired_date=?";
        query += ", desired_time=?";
        query += ", push_date=?";
        query += ", push_time=?";
        query += ", push_count=?";
        query += ", condition_sex=?";
        query += ", condition_age_from=?";
        query += ", condition_age_to=?";
        query += ", condition_birthday=?";
        query += ", condition_area=?";
        query += ", condition_myarea=?";
        query += ", condition_myhotel=?";
        query += ", condition_user_id=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += ", apli_kind=?";
        query += " WHERE push_seq=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.desiredDate );
            prestate.setInt( i++, this.desiredTime );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.pushCount );
            prestate.setInt( i++, this.conditionSex );
            prestate.setInt( i++, this.conditionAgeFrom );
            prestate.setInt( i++, this.conditionAgeTo );
            prestate.setInt( i++, this.conditionBirthday );
            prestate.setString( i++, this.conditionArea );
            prestate.setString( i++, this.conditionMyarea );
            prestate.setString( i++, this.conditionMyhotel );
            prestate.setString( i++, this.conditionUserId );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, this.apliKind );
            prestate.setInt( i++, this.pushSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.updateData] Exception=" + e.toString(),"DataApPushData" );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

}
