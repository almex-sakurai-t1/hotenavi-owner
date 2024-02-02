package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Sun Feb 01 19:39:51 JST 2015
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * Ap_push_hotelVo.
 *
 * @author tashiro-s1
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2015/02/01 tashiro-s1 Generated.
 */
public class DataApPushHotel implements Serializable
{
    /**
     * 2:checkout:int(10) <Primary Key>
     */
    private int    type;

    /**
     * 0:標準　それ以外ホテルID独自:int(10) <Primary Key>
     */
    private int    id;

    /**
     * プッシュ配信連番:int(10) <Primary Key>
     */
    private int    seq;

    /**
     * キャンペーンタイトル:varchar(255)
     */
    private String title;

    /**
     * キャンペーン詳細:varchar(255)
     */
    private String detail;

    /**
     * コンテンツ（html）:text(65535)
     */
    private String contents;

    /**
     * 1:削除:int(10)
     */
    private int    delFlag;

    /**
     * 表示開始日(YYYYMMDD):int(10)
     */
    private int    dispFrom;

    /**
     * 表示開始時刻(HHMMSS):int(10)
     */
    private int    dispFromTime;

    /**
     * 表示終了日(YYYYMMDD):int(10)
     */
    private int    dispTo;

    /**
     * 表示終了時刻(HHMMSS):int(10)
     */
    private int    dispToTime;

    /**
     * 更新契約ホテルID:varchar(10)
     */
    private String ownerHotelId;

    /**
     * 更新オーナーユーザーID:int(10)
     */
    private int    ownerUserId;

    /**
     * 登録日付(YYYYMMDD):int(10)
     */
    private int    registDate;

    /**
     * 登録時刻(HHMMSS):int(10)
     */
    private int    registTime;

    /**
     * 承認会社ID:2:int(10)
     */
    private int    companyId;

    /**
     * 承認社員ID:varchar(6)
     */
    private String staffId;

    /**
     * 承認日付(YYYYMMDD):int(10)
     */
    private int    approvalDate;

    /**
     * 承認時刻(HHMMSS):int(10)
     */
    private int    approvalTime;

    /**
     * Constractor
     */
    public DataApPushHotel()
    {
        this.type = 0;
        this.id = 0;
        this.seq = 0;
        this.title = "";
        this.detail = "";
        this.contents = "";
        this.delFlag = 0;
        this.dispFrom = 0;
        this.dispFromTime = 0;
        this.dispTo = 0;
        this.dispToTime = 0;
        this.ownerHotelId = "";
        this.ownerUserId = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.companyId = 0;
        this.staffId = "";
        this.approvalDate = 0;
        this.approvalTime = 0;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDetail()
    {
        return this.detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getContents()
    {
        return this.contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public int getDelFlag()
    {
        return this.delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public int getDispFrom()
    {
        return this.dispFrom;
    }

    public void setDispFrom(int dispFrom)
    {
        this.dispFrom = dispFrom;
    }

    public int getDispFromTime()
    {
        return this.dispFromTime;
    }

    public void setDispFromTime(int dispFromTime)
    {
        this.dispFromTime = dispFromTime;
    }

    public int getDispTo()
    {
        return this.dispTo;
    }

    public void setDispTo(int dispTo)
    {
        this.dispTo = dispTo;
    }

    public int getDispToTime()
    {
        return this.dispToTime;
    }

    public void setDispToTime(int dispToTime)
    {
        this.dispToTime = dispToTime;
    }

    public String getOwnerHotelId()
    {
        return this.ownerHotelId;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return this.ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getCompanyId()
    {
        return this.companyId;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public String getStaffId()
    {
        return this.staffId;
    }

    public void setStaffId(String staffId)
    {
        this.staffId = staffId;
    }

    public int getApprovalDate()
    {
        return this.approvalDate;
    }

    public void setApprovalDate(int approvalDate)
    {
        this.approvalDate = approvalDate;
    }

    public int getApprovalTime()
    {
        return this.approvalTime;
    }

    public void setApprovalTime(int approvalTime)
    {
        this.approvalTime = approvalTime;
    }

    /****
     * プッシュホテルデータ(ap_push_data_list)取得
     *
     * @param pushSeq プッシュ配信連番
     * @param userId ユーザーID
     * @param token iOS:device token,Android:registration id
     * @return
     */
    public boolean getData(int type, int id, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_hotel WHERE type = ? AND id = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, id );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.type = result.getInt( "type" );
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.title = result.getString( "title" );
                    this.detail = result.getString( "detail" );
                    this.contents = result.getString( "contents" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.dispFrom = result.getInt( "disp_from" );
                    this.dispFromTime = result.getInt( "disp_from_time" );
                    this.dispTo = result.getInt( "disp_to" );
                    this.dispToTime = result.getInt( "disp_to_time" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.companyId = result.getInt( "company_id" );
                    this.staffId = result.getString( "staff_id" );
                    this.approvalDate = result.getInt( "approval_date" );
                    this.approvalTime = result.getInt( "approval_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushHotel.getData] Exception=" + e.toString(),"[DataApPushHotel.getData]" );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * プッシュホテルデータ(ap_push_data_list)設定
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
                this.type = result.getInt( "type" );
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.title = result.getString( "title" );
                this.detail = result.getString( "detail" );
                this.contents = result.getString( "contents" );
                this.delFlag = result.getInt( "del_flag" );
                this.dispFrom = result.getInt( "disp_from" );
                this.dispFromTime = result.getInt( "disp_from_time" );
                this.dispTo = result.getInt( "disp_to" );
                this.dispToTime = result.getInt( "disp_to_time" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.companyId = result.getInt( "company_id" );
                this.staffId = result.getString( "staff_id" );
                this.approvalDate = result.getInt( "approval_date" );
                this.approvalTime = result.getInt( "approval_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushHotel.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * プッシュホテルデータ(ap_push_data_list)挿入
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

        query = "INSERT ap_push_hotel SET ";
        query += " type=?";
        query += ", id=?";
        query += ", seq=?";
        query += ", title=?";
        query += ", detail=?";
        query += ", contents=?";
        query += ", del_flag=?";
        query += ", disp_from=?";
        query += ", disp_from_time=?";
        query += ", disp_to=?";
        query += ", disp_to_time=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.type );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.seq );
            prestate.setString( i++, this.title );
            prestate.setString( i++, this.detail );
            prestate.setString( i++, this.contents );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.dispFrom );
            prestate.setInt( i++, this.dispFromTime );
            prestate.setInt( i++, this.dispTo );
            prestate.setInt( i++, this.dispToTime );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushHotel.insertData] Exception=" + e.toString() );
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
     * プッシュホテルデータ(ap_push_data_list)更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param pushSeq プッシュ配信連番
     * @param userId ユーザーID
     * @param token iOS:device token,Android:registration id
     * @return
     */
    public boolean updateData(int type, int id, int seq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_hotel SET ";
        query += ", title=?";
        query += ", detail=?";
        query += ", contents=?";
        query += ", del_flag=?";
        query += ", disp_from=?";
        query += ", disp_from_time=?";
        query += ", disp_to=?";
        query += ", disp_to_time=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += " WHERE type=? AND id=? AND seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.title );
            prestate.setString( i++, this.detail );
            prestate.setString( i++, this.contents );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.dispFrom );
            prestate.setInt( i++, this.dispFromTime );
            prestate.setInt( i++, this.dispTo );
            prestate.setInt( i++, this.dispToTime );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, type );
            prestate.setInt( i++, id );
            prestate.setInt( i++, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushHotel.updateData] Exception=" + e.toString() );
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
