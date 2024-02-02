package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;

/**
 * 有料ユーザーポイント一時データ
 * 
 * @author S.Tashiro
 * @version 1.00 2011/01/07
 */
public class DataUserPointPayTemp implements Serializable
{

    private static final long serialVersionUID = -3046368837141771246L;
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
    private int               reflectDate;
    private int               addFlag;
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
    private int               ciDate;
    private int               ciTime;

    public DataUserPointPayTemp()
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
        reflectDate = 0;
        addFlag = 0;
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
        ciDate = 0;
        ciTime = 0;
    }

    /** getter **/
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

    public int getReflectDate()
    {
        return reflectDate;
    }

    public int getAddFlag()
    {
        return addFlag;
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
        return userType;
    }

    public int getCiDate()
    {
        return ciDate;
    }

    public int getCiTime()
    {
        return ciTime;
    }

    /** setter **/
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

    public void setReflectDate(int reflectDate)
    {
        this.reflectDate = reflectDate;
    }

    public void setAddFlag(int addFlag)
    {
        this.addFlag = addFlag;
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

    public void setCiDate(int ciDate)
    {
        this.ciDate = ciDate;
    }

    public void setCiTime(int ciTime)
    {
        this.ciTime = ciTime;
    }

    /**
     * 有料ユーザポイント一時データ取得
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

        query = "SELECT * FROM hh_user_point_pay_temp WHERE user_id = ? AND seq = ?";

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
                    this.reflectDate = result.getInt( "reflect_date" );
                    this.addFlag = result.getInt( "add_flag" );
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
                    this.ciDate = result.getInt( "ci_date" );
                    this.ciTime = result.getInt( "ci_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.getData] Exception=" + e.toString() );
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
                this.reflectDate = result.getInt( "reflect_date" );
                this.addFlag = result.getInt( "add_flag" );
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
                this.ciDate = result.getInt( "ci_date" );
                this.ciTime = result.getInt( "ci_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_user_point_pay_temp SET ";
        query += " user_id = ?,";
        query += " seq = 0,";
        query += " get_date = ?,";
        query += " get_time = ?,";
        query += " code = ?,";
        query += " point = ?,";
        query += " point_kind = ?,";
        query += " ext_code = ?,";
        query += " ext_string = ?,";
        query += " person_code = ?,";
        query += " append_reason = ?,";
        query += " memo = ?,";
        query += " reflect_date = ?,";
        query += " add_flag = ?,";
        query += " idm = ?,";
        query += " user_seq = ?,";
        query += " visit_seq = ?,";
        query += " slip_no = ?,";
        query += " room_no = ?,";
        query += " amount = ?,";
        query += " then_point = ?,";
        query += " hotenavi_id = ?,";
        query += " employee_code = ?,";
        query += " used_point = ?,";
        query += " user_type = ?,";
        query += " ci_date = ?,";
        query += " ci_time = ?";

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
            prestate.setInt( 12, this.reflectDate );
            prestate.setInt( 13, this.addFlag );
            prestate.setString( 14, this.idm );
            prestate.setInt( 15, this.userSeq );
            prestate.setInt( 16, this.visitSeq );
            prestate.setInt( 17, this.slipNo );
            prestate.setString( 18, this.roomNo );
            prestate.setInt( 19, this.amount );
            prestate.setInt( 20, this.thenPoint );
            prestate.setString( 21, this.hotenaviId );
            prestate.setInt( 22, this.employeeCode );
            prestate.setInt( 23, this.usedPoint );
            prestate.setInt( 24, this.userType );
            prestate.setInt( 25, this.ciDate );
            prestate.setInt( 26, this.ciTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.insertData] Exception=" + e.toString() );
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

        query = "INSERT hh_user_point_pay_temp SET ";
        query += " user_id = ?,";
        query += " seq = ?,";
        query += " get_date = ?,";
        query += " get_time = ?,";
        query += " code = ?,";
        query += " point = ?,";
        query += " point_kind = ?,";
        query += " ext_code = ?,";
        query += " ext_string = ?,";
        query += " person_code = ?,";
        query += " append_reason = ?,";
        query += " memo = ?,";
        query += " reflect_date = ?,";
        query += " add_flag = ?,";
        query += " idm = ?,";
        query += " user_seq = ?,";
        query += " visit_seq = ?,";
        query += " slip_no = ?,";
        query += " room_no = ?,";
        query += " amount = ?,";
        query += " then_point = ?,";
        query += " hotenavi_id = ?,";
        query += " employee_code = ?,";
        query += " used_point = ?,";
        query += " user_type = ?,";
        query += " ci_date = ?,";
        query += " ci_time = ?";

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
            prestate.setInt( i++, this.reflectDate );
            prestate.setInt( i++, this.addFlag );
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
            prestate.setInt( i++, this.ciDate );
            prestate.setInt( i++, this.ciTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.insertDataBySeq()] Exception=" + e.toString() );
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

        query = "UPDATE hh_user_point_pay_temp SET ";
        query += " get_date = ?,";
        query += " get_time = ?,";
        query += " code = ?,";
        query += " point = ?,";
        query += " point_kind = ?,";
        query += " ext_code = ?,";
        query += " ext_string = ?,";
        query += " person_code = ?,";
        query += " append_reason = ?,";
        query += " memo = ?,";
        query += " reflect_date = ?,";
        query += " add_flag = ?,";
        query += " idm = ?,";
        query += " user_seq = ?,";
        query += " visit_seq = ?,";
        query += " slip_no = ?,";
        query += " room_no = ?,";
        query += " amount = ?,";
        query += " then_point = ?,";
        query += " hotenavi_id = ?,";
        query += " employee_code = ?,";
        query += " used_point = ?,";
        query += " user_type = ?,";
        query += " ci_date =?,";
        query += " ci_time =?";
        query += " WHERE user_id = ? AND seq = ?";

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
            prestate.setInt( 11, this.reflectDate );
            prestate.setInt( 12, this.addFlag );
            prestate.setString( 13, this.idm );
            prestate.setInt( 14, this.userSeq );
            prestate.setInt( 15, this.visitSeq );
            prestate.setInt( 16, this.slipNo );
            prestate.setString( 17, this.roomNo );
            prestate.setInt( 18, this.amount );
            prestate.setInt( 19, this.thenPoint );
            prestate.setString( 20, this.hotenaviId );
            prestate.setInt( 21, this.employeeCode );
            prestate.setInt( 22, this.usedPoint );
            prestate.setInt( 23, this.userType );
            prestate.setInt( 24, this.ciDate );
            prestate.setInt( 25, this.ciTime );
            prestate.setString( 26, userId );
            prestate.setInt( 27, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.updateData] Exception=" + e.toString() );
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
     * ユーザマイエリア情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(Connection connection, String userId, int seq)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_point_pay_temp SET ";
        query += " get_date = ?,";
        query += " get_time = ?,";
        query += " code = ?,";
        query += " point = ?,";
        query += " point_kind = ?,";
        query += " ext_code = ?,";
        query += " ext_string = ?,";
        query += " person_code = ?,";
        query += " append_reason = ?,";
        query += " memo = ?,";
        query += " reflect_date = ?,";
        query += " add_flag = ?,";
        query += " idm = ?,";
        query += " user_seq = ?,";
        query += " visit_seq = ?,";
        query += " slip_no = ?,";
        query += " room_no = ?,";
        query += " amount = ?,";
        query += " then_point = ?,";
        query += " hotenavi_id = ?,";
        query += " employee_code = ?,";
        query += " used_point = ?,";
        query += " ci_date = ?,";
        query += " ci_time = ?";

        query += " WHERE user_id = ? AND seq = ?";

        try
        {
            // connection = DBConnection.getConnection();
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
            prestate.setInt( 11, this.reflectDate );
            prestate.setInt( 12, this.addFlag );
            prestate.setString( 13, this.idm );
            prestate.setInt( 14, this.userSeq );
            prestate.setInt( 15, this.visitSeq );
            prestate.setInt( 16, this.slipNo );
            prestate.setString( 17, this.roomNo );
            prestate.setInt( 18, this.amount );
            prestate.setInt( 19, this.thenPoint );
            prestate.setString( 20, this.hotenaviId );
            prestate.setInt( 21, this.employeeCode );
            prestate.setInt( 22, this.usedPoint );
            prestate.setInt( 23, this.ciDate );
            prestate.setInt( 24, this.ciTime );
            prestate.setString( 25, userId );
            prestate.setInt( 26, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 有料ユーザポイント一時データ取得
     * 
     * @param userId ユーザID
     * @param seq 管理番号
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

        query = "SELECT * FROM hh_user_point_pay_temp WHERE user_id = ? AND point_kind = ? AND ext_code = ?";
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
                    this.reflectDate = result.getInt( "reflect_date" );
                    this.addFlag = result.getInt( "add_flag" );
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
                    this.ciDate = result.getInt( "ci_date" );
                    this.ciTime = result.getInt( "ci_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.getData1] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ボーナスマイル一時データ取得
     * 
     * @param userId ユーザID
     * @param rsvNo 予約番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, String rsvNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_pay_temp WHERE user_id = ? AND ext_string = ?";
        query += " AND code = ? AND point_kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setString( 2, rsvNo );
            prestate.setInt( 3, OwnerRsvCommon.RSV_BONUS_CODE );
            prestate.setInt( 4, OwnerRsvCommon.HAPYPOINT_29 );
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
                    this.reflectDate = result.getInt( "reflect_date" );
                    this.addFlag = result.getInt( "add_flag" );
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
                    this.ciDate = result.getInt( "ci_date" );
                    this.ciTime = result.getInt( "ci_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.getData1] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザポイントデータ存在確認
     * 
     * @param pointKind
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean isData(int pointKind, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_point_pay_temp WHERE user_id = ? AND point_kind = ? LIMIT 0,1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, pointKind );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPointPayTemp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
