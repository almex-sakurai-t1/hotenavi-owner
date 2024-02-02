package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Aug 07 16:27:10 JST 2009
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 有料ユーザーポイントデータ
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/07
 */
public class DataUserPointPay implements Serializable
{

    private static final long serialVersionUID = 4455955852292489729L;

    private String            userId;
    private int               seq;
    private int               getDate;
    private int               getTime;
    private int               code;
    private int               point;
    private int               pointKind;
    private int               extCode;
    private String            extString;
    private String            personCode;
    private String            appendReason;
    private String            memo;
    private String            idm;
    private int               userSeq;
    private int               visitSeq;
    private int               slipNo;
    private String            roomNo;
    private int               amount;
    private int               thenPoint;
    private String            hotenaviId;
    private int               employeeCode;
    private int               usedPoint;
    private int               userType;
    private int               expiredPoint;

    public DataUserPointPay()
    {
        userId = "";
        seq = 0;
        getDate = 0;
        getTime = 0;
        code = 0;
        point = 0;
        pointKind = 0;
        extCode = 0;
        extString = "";
        personCode = "";
        appendReason = "";
        memo = "";
        idm = "";
        userSeq = 0;
        visitSeq = 0;
        slipNo = 0;
        roomNo = "";
        amount = 0;
        thenPoint = 0;
        hotenaviId = "";
        employeeCode = 0;
        usedPoint = 0;
        userType = 0;
        expiredPoint = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getGetDate()
    {
        return getDate;
    }

    public int getGetTime()
    {
        return getTime;
    }

    public int getCode()
    {
        return code;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPointKind()
    {
        return pointKind;
    }

    public int getExtCode()
    {
        return extCode;
    }

    public String getExtString()
    {
        return extString;
    }

    public String getPersonCode()
    {
        return personCode;
    }

    public String getAppendReason()
    {
        return appendReason;
    }

    public String getMemo()
    {
        return memo;
    }

    public String getIdm()
    {
        return idm;
    }

    public int getUserSeq()
    {
        return userSeq;
    }

    public int getVisitSeq()
    {
        return visitSeq;
    }

    public int getSlipNo()
    {
        return slipNo;
    }

    public String getRoomNo()
    {
        return roomNo;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getThenPoint()
    {
        return thenPoint;
    }

    public String getHotenaviId()
    {
        return hotenaviId;
    }

    public int getEmployeeCode()
    {
        return employeeCode;
    }

    public int getUsedPoint()
    {
        return usedPoint;
    }

    public int getUserType()
    {
        return userSeq;
    }

    public int getExpiredPoint()
    {
        return expiredPoint;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setGetDate(int getDate)
    {
        this.getDate = getDate;
    }

    public void setGetTime(int getTime)
    {
        this.getTime = getTime;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPointKind(int pointKind)
    {
        this.pointKind = pointKind;
    }

    public void setExtCode(int extCode)
    {
        this.extCode = extCode;
    }

    public void setExtString(String extString)
    {
        this.extString = extString;
    }

    public void setPersonCode(String personCode)
    {
        this.personCode = personCode;
    }

    public void setAppendReason(String appendReason)
    {
        this.appendReason = appendReason;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setIdm(String idm)
    {
        this.idm = idm;
    }

    public void setUserSeq(int userSeq)
    {
        this.userSeq = userSeq;
    }

    public void setVisitSeq(int visitSeq)
    {
        this.visitSeq = visitSeq;
    }

    public void setSlipNo(int slipNo)
    {
        this.slipNo = slipNo;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setThenPoint(int thenPoint)
    {
        this.thenPoint = thenPoint;
    }

    public void setHotenaviId(String hotenaviId)
    {
        this.hotenaviId = hotenaviId;
    }

    public void setEmployeeCode(int employeeCode)
    {
        this.employeeCode = employeeCode;
    }

    public void setUsedPoint(int usedPoint)
    {
        this.usedPoint = usedPoint;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public void setExpiredPoint(int expiredPoint)
    {
        this.expiredPoint = expiredPoint;
    }

    /**
     * ユーザポイントデータ取得
     * 
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_pay WHERE user_id = ? AND seq = ?";

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
                    this.getDate = result.getInt( "get_date" );
                    this.getTime = result.getInt( "get_time" );
                    this.code = result.getInt( "code" );
                    this.point = result.getInt( "point" );
                    this.pointKind = result.getInt( "point_kind" );
                    this.extCode = result.getInt( "ext_code" );
                    this.extString = result.getString( "ext_string" );
                    this.personCode = result.getString( "person_code" );
                    this.appendReason = result.getString( "append_reason" );
                    this.memo = result.getString( "memo" );
                    this.idm = result.getString( "idm" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.visitSeq = result.getInt( "visit_seq" );
                    this.slipNo = result.getInt( "slip_no" );
                    this.roomNo = result.getString( "room_no" );
                    this.amount = result.getInt( "amount" );
                    this.thenPoint = result.getInt( "then_point" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.employeeCode = result.getInt( "employee_code" );
                    this.usedPoint = result.getInt( "used_point" );
                    this.userType = result.getInt( "user_type" );
                    this.expiredPoint = result.getInt( "expired_point" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザポイントデータ設定
     * 
     * @param result ユーザポイントデータレコード
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
                this.getDate = result.getInt( "get_date" );
                this.getTime = result.getInt( "get_time" );
                this.code = result.getInt( "code" );
                this.point = result.getInt( "point" );
                this.pointKind = result.getInt( "point_kind" );
                this.extCode = result.getInt( "ext_code" );
                this.extString = result.getString( "ext_string" );
                this.personCode = result.getString( "person_code" );
                this.appendReason = result.getString( "append_reason" );
                this.memo = result.getString( "memo" );
                this.idm = result.getString( "idm" );
                this.userSeq = result.getInt( "user_seq" );
                this.visitSeq = result.getInt( "visit_seq" );
                this.slipNo = result.getInt( "slip_no" );
                this.roomNo = result.getString( "room_no" );
                this.amount = result.getInt( "amount" );
                this.thenPoint = result.getInt( "then_point" );
                this.hotenaviId = result.getString( "hotenavi_id" );
                this.employeeCode = result.getInt( "employee_code" );
                this.usedPoint = result.getInt( "used_point" );
                this.userType = result.getInt( "user_type" );
                this.expiredPoint = result.getInt( "expired_point" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザポイントデータ情報データ追加
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

        ret = false;

        query = "INSERT hh_user_point_pay SET ";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " get_date = ?,";
        query = query + " get_time = ?,";
        query = query + " code = ?,";
        query = query + " point = ?,";
        query = query + " point_kind = ?,";
        query = query + " ext_code = ?,";
        query = query + " ext_string = ?,";
        query = query + " person_code = ?,";
        query = query + " append_reason = ?,";
        query = query + " memo = ?,";
        query = query + " idm = ?,";
        query = query + " user_seq = ?,";
        query = query + " visit_seq = ?,";
        query = query + " slip_no = ?,";
        query = query + " room_no = ?,";
        query = query + " amount = ?,";
        query = query + " then_point = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " employee_code = ?,";
        query = query + " used_point = ?,";
        query = query + " user_type = ?,";
        query = query + " expired_point = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.getDate );
            prestate.setInt( 3, this.getTime );
            prestate.setInt( 4, this.code );
            prestate.setInt( 5, this.point );
            prestate.setInt( 6, this.pointKind );
            prestate.setInt( 7, this.extCode );
            prestate.setString( 8, this.extString );
            prestate.setString( 9, this.personCode );
            prestate.setString( 10, this.appendReason );
            prestate.setString( 11, this.memo );
            prestate.setString( 12, this.idm );
            prestate.setInt( 13, this.userSeq );
            prestate.setInt( 14, this.visitSeq );
            prestate.setInt( 15, this.slipNo );
            prestate.setString( 16, this.roomNo );
            prestate.setInt( 17, this.amount );
            prestate.setInt( 18, this.thenPoint );
            prestate.setString( 19, this.hotenaviId );
            prestate.setInt( 20, this.employeeCode );
            prestate.setInt( 21, this.usedPoint );
            prestate.setInt( 22, this.userType );
            prestate.setInt( 23, this.expiredPoint );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.insertData] Exception=" + e.toString() );
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
     * ユーザポイントデータ情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertDataBySeq()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_point_pay SET ";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " get_date = ?,";
        query = query + " get_time = ?,";
        query = query + " code = ?,";
        query = query + " point = ?,";
        query = query + " point_kind = ?,";
        query = query + " ext_code = ?,";
        query = query + " ext_string = ?,";
        query = query + " person_code = ?,";
        query = query + " append_reason = ?,";
        query = query + " memo = ?,";
        query = query + " idm = ?,";
        query = query + " user_seq = ?,";
        query = query + " visit_seq = ?,";
        query = query + " slip_no = ?,";
        query = query + " room_no = ?,";
        query = query + " amount = ?,";
        query = query + " then_point = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " employee_code = ?,";
        query = query + " used_point = ?,";
        query = query + " user_type = ?,";
        query = query + " expired_point = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.getDate );
            prestate.setInt( i++, this.getTime );
            prestate.setInt( i++, this.code );
            prestate.setInt( i++, this.point );
            prestate.setInt( i++, this.pointKind );
            prestate.setInt( i++, this.extCode );
            prestate.setString( i++, this.extString );
            prestate.setString( i++, this.personCode );
            prestate.setString( i++, this.appendReason );
            prestate.setString( i++, this.memo );
            prestate.setString( i++, this.idm );
            prestate.setInt( i++, this.userSeq );
            prestate.setInt( i++, this.visitSeq );
            prestate.setInt( i++, this.slipNo );
            prestate.setString( i++, this.roomNo );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.thenPoint );
            prestate.setString( i++, this.hotenaviId );
            prestate.setInt( i++, this.employeeCode );
            prestate.setInt( i++, this.usedPoint );
            prestate.setInt( i++, this.userType );
            prestate.setInt( i++, this.expiredPoint );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.insertDataBySeq()] Exception=" + e.toString() );
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
     * ユーザポイントデータ情報データ追加
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

        query = "INSERT hh_user_point_pay SET ";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " get_date = ?,";
        query = query + " get_time = ?,";
        query = query + " code = ?,";
        query = query + " point = ?,";
        query = query + " point_kind = ?,";
        query = query + " ext_code = ?,";
        query = query + " ext_string = ?,";
        query = query + " person_code = ?,";
        query = query + " append_reason = ?,";
        query = query + " memo = ?,";
        query = query + " idm = ?,";
        query = query + " user_seq = ?,";
        query = query + " visit_seq = ?,";
        query = query + " slip_no = ?,";
        query = query + " room_no = ?,";
        query = query + " amount = ?,";
        query = query + " then_point = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " employee_code = ?,";
        query = query + " used_point = ?,";
        query = query + " user_type = ?,";
        query = query + " expired_point = ?";

        try
        {
            // connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.getDate );
            prestate.setInt( 3, this.getTime );
            prestate.setInt( 4, this.code );
            prestate.setInt( 5, this.point );
            prestate.setInt( 6, this.pointKind );
            prestate.setInt( 7, this.extCode );
            prestate.setString( 8, this.extString );
            prestate.setString( 9, this.personCode );
            prestate.setString( 10, this.appendReason );
            prestate.setString( 11, this.memo );
            prestate.setString( 12, this.idm );
            prestate.setInt( 13, this.userSeq );
            prestate.setInt( 14, this.visitSeq );
            prestate.setInt( 15, this.slipNo );
            prestate.setString( 16, this.roomNo );
            prestate.setInt( 17, this.amount );
            prestate.setInt( 18, this.thenPoint );
            prestate.setString( 19, this.hotenaviId );
            prestate.setInt( 20, this.employeeCode );
            prestate.setInt( 21, this.usedPoint );
            prestate.setInt( 22, this.userType );
            prestate.setInt( 23, this.expiredPoint );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * ユーザマイエリア情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_point_pay SET ";
        query = query + " get_date = ?,";
        query = query + " get_time = ?,";
        query = query + " code = ?,";
        query = query + " point = ?,";
        query = query + " point_kind = ?,";
        query = query + " ext_code = ?,";
        query = query + " ext_string = ?,";
        query = query + " person_code = ?,";
        query = query + " append_reason = ?,";
        query = query + " memo = ?,";
        query = query + " idm = ?,";
        query = query + " user_seq = ?,";
        query = query + " visit_seq = ?,";
        query = query + " slip_no = ?,";
        query = query + " room_no = ?,";
        query = query + " amount = ?,";
        query = query + " then_point = ?,";
        query = query + " hotenavi_id = ?,";
        query = query + " employee_code = ?,";
        query = query + " used_point = ?,";
        query = query + " user_type = ?,";
        query = query + " expired_point = ?";
        query = query + " WHERE user_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.getDate );
            prestate.setInt( 2, this.getTime );
            prestate.setInt( 3, this.code );
            prestate.setInt( 4, this.point );
            prestate.setInt( 5, this.pointKind );
            prestate.setInt( 6, this.extCode );
            prestate.setString( 7, this.extString );
            prestate.setString( 8, this.personCode );
            prestate.setString( 9, this.appendReason );
            prestate.setString( 10, this.memo );
            prestate.setString( 11, this.idm );
            prestate.setInt( 12, this.userSeq );
            prestate.setInt( 13, this.visitSeq );
            prestate.setInt( 14, this.slipNo );
            prestate.setString( 15, this.roomNo );
            prestate.setInt( 16, this.amount );
            prestate.setInt( 17, this.thenPoint );
            prestate.setString( 18, this.hotenaviId );
            prestate.setInt( 19, this.employeeCode );
            prestate.setInt( 20, this.usedPoint );
            prestate.setInt( 21, this.userType );
            prestate.setInt( 22, this.expiredPoint );
            prestate.setString( 23, userId );
            prestate.setInt( 24, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.updateData] Exception=" + e.toString() );
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
     * 有料ユーザポイント一時データ取得
     * 
     * @param userId ユーザID
     * @param pointKind マイル種類D
     * @param extCode ホテルID
     * @param userSeq ホテルごとのユーザSeq
     * @param visitSeq ホテルごとのVisitSeq
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int pointKind, int extCode, int userSeq, int visitSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_pay WHERE user_id = ? AND point_kind = ? AND ext_code = ?";
        query += " AND user_seq =? AND visit_seq = ?";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, pointKind );
            prestate.setInt( 3, extCode );
            prestate.setInt( 4, userSeq );
            prestate.setInt( 5, visitSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.getDate = result.getInt( "get_date" );
                    this.getTime = result.getInt( "get_time" );
                    this.code = result.getInt( "code" );
                    this.point = result.getInt( "point" );
                    this.pointKind = result.getInt( "point_kind" );
                    this.extCode = result.getInt( "ext_code" );
                    this.extString = result.getString( "ext_string" );
                    this.personCode = result.getString( "person_code" );
                    this.appendReason = result.getString( "append_reason" );
                    this.memo = result.getString( "memo" );
                    this.idm = result.getString( "idm" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.visitSeq = result.getInt( "visit_seq" );
                    this.slipNo = result.getInt( "slip_no" );
                    this.roomNo = result.getString( "room_no" );
                    this.amount = result.getInt( "amount" );
                    this.thenPoint = result.getInt( "then_point" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.employeeCode = result.getInt( "employee_code" );
                    this.usedPoint = result.getInt( "used_point" );
                    this.userType = result.getInt( "user_type" );
                    this.expiredPoint = result.getInt( "expired_point" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.getData1] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 有料ユーザポイントデータ取得（予約用）
     * 
     * @param userId ユーザID
     * @param seq 管理番号
     * @param extCode ホテルID
     * @param extString 予約No
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int pointKind, int extCode, String extString)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_pay WHERE user_id = ? AND point_kind = ? AND ext_code = ?";
        query += " AND ext_string =? ";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, pointKind );
            prestate.setInt( 3, extCode );
            prestate.setString( 4, extString );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.getDate = result.getInt( "get_date" );
                    this.getTime = result.getInt( "get_time" );
                    this.code = result.getInt( "code" );
                    this.point = result.getInt( "point" );
                    this.pointKind = result.getInt( "point_kind" );
                    this.extCode = result.getInt( "ext_code" );
                    this.extString = result.getString( "ext_string" );
                    this.personCode = result.getString( "person_code" );
                    this.appendReason = result.getString( "append_reason" );
                    this.memo = result.getString( "memo" );
                    this.idm = result.getString( "idm" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.visitSeq = result.getInt( "visit_seq" );
                    this.slipNo = result.getInt( "slip_no" );
                    this.roomNo = result.getString( "room_no" );
                    this.amount = result.getInt( "amount" );
                    this.thenPoint = result.getInt( "then_point" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.employeeCode = result.getInt( "employee_code" );
                    this.usedPoint = result.getInt( "used_point" );
                    this.userType = result.getInt( "user_type" );
                    this.expiredPoint = result.getInt( "expired_point" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.getData (reserveNo)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 有料ユーザポイントデータ取得（予約・使用マイル用）
     * 
     * @param userId ユーザID
     * @param extCode ホテルID
     * @param extString 予約No
     * @param flag -1 :使用マイルデータ取得時 ,1 :使用マイル取消しデータ取得時
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int extCode, String extString, int flag)
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            ret = getData( connection, userId, extCode, extString, flag );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.getData (reserveNo)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 有料ユーザポイントデータ取得（予約・使用マイル用）
     * 
     * @param userId ユーザID
     * @param extCode ホテルID
     * @param extString 予約No
     * @param flag -1 :使用マイルデータ取得時 ,1 :使用マイル取消しデータ取得時
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(Connection connection, String userId, int extCode, String extString, int flag)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_pay WHERE user_id = ? AND point_kind = ? AND ext_code = ?";
        query += " AND ext_string =? ";
        query += " AND point * ? > 0";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, 23 );
            prestate.setInt( 3, extCode );
            prestate.setString( 4, extString );
            prestate.setInt( 5, flag );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.getDate = result.getInt( "get_date" );
                    this.getTime = result.getInt( "get_time" );
                    this.code = result.getInt( "code" );
                    this.point = result.getInt( "point" );
                    this.pointKind = result.getInt( "point_kind" );
                    this.extCode = result.getInt( "ext_code" );
                    this.extString = result.getString( "ext_string" );
                    this.personCode = result.getString( "person_code" );
                    this.appendReason = result.getString( "append_reason" );
                    this.memo = result.getString( "memo" );
                    this.idm = result.getString( "idm" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.visitSeq = result.getInt( "visit_seq" );
                    this.slipNo = result.getInt( "slip_no" );
                    this.roomNo = result.getString( "room_no" );
                    this.amount = result.getInt( "amount" );
                    this.thenPoint = result.getInt( "then_point" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.employeeCode = result.getInt( "employee_code" );
                    this.usedPoint = result.getInt( "used_point" );
                    this.userType = result.getInt( "user_type" );
                    this.expiredPoint = result.getInt( "expired_point" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPay.getData (reserveNo)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
}
