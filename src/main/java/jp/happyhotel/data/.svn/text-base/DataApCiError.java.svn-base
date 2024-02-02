package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * チェックインエラーデータ(ap_ci_error)取得クラス
 * 
 * @author Keion.Park
 */
public class DataApCiError implements Serializable
{
    public static final String TABLE = "ap_ci_error";
    private int                id;                   // ハピホテホテルID
    private int                seq;                  // チェックインコード
    private String             frontIp;              // ホテナビフロントIP
    private int                ciDate;               // チェックイン日付(YYYYMMDD)
    private int                ciTime;               // チェックイン時刻(HHMMSS)
    private String             roomNo;               // 部屋名称
    private String             userId;               // ユーザーID
    private String             rsvNo;                // 予約NO　X99999999-999999
    private int                kind;                 // エラー種類(0:不明,1:ci無効,2:ci未送信,3:ci未送信(タッチPC設定違い),4:金額未受信,5:金額0受信,6:金額マイナス受信
    private String             reason;               // エラー理由
    private int                amount;               // 利用金額
    private int                usePoint;             // マイル使用
    private int                errorCode;            // エラーコード
    private int                reflectFlag;          // 0:未対応, 1:対象データなし,2.その他エラー,9:反映済,-1:反映対象外

    /**
     * データを初期化します。
     */
    public DataApCiError()
    {
        this.id = 0;
        this.seq = 0;
        this.frontIp = "";
        this.ciDate = 0;
        this.ciTime = 0;
        this.roomNo = "";
        this.userId = "";
        this.rsvNo = "";
        this.kind = 0;
        this.reason = "";
        this.amount = 0;
        this.usePoint = 0;
        this.reflectFlag = 0;
        this.errorCode = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getFrontIp()
    {
        return frontIp;
    }

    public int getCiDate()
    {
        return ciDate;
    }

    public int getCiTime()
    {
        return ciTime;
    }

    public String getRoomNo()
    {
        return roomNo;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getRsvNo()
    {
        return rsvNo;
    }

    public int getKind()
    {
        return kind;
    }

    public String getReason()
    {
        return reason;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getUsePoint()
    {
        return usePoint;
    }

    public int getReflectFlag()
    {
        return reflectFlag;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setFrontIp(String frontIp)
    {
        this.frontIp = frontIp;
    }

    public void setCiDate(int ciDate)
    {
        this.ciDate = ciDate;
    }

    public void setCiTime(int ciTime)
    {
        this.ciTime = ciTime;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = rsvNo;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setUsePoint(int usePoint)
    {
        this.usePoint = usePoint;
    }

    public void setReflectFlag(int reflectFlag)
    {
        this.reflectFlag = reflectFlag;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    /****
     * チェックインエラーデータ(ap_ci_error)取得
     * 
     * @param id ハピホテホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int seq)
    {
        String query;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement prestate = null;
        boolean rtn = false;

        query = "SELECT * FROM ap_ci_error WHERE id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                    rtn = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCiError.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(rtn);
    }

    /**
     * ユーザ基本データ設定
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
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.frontIp = result.getString( "front_ip" );
                this.ciDate = result.getInt( "ci_date" );
                this.ciTime = result.getInt( "ci_time" );
                this.roomNo = result.getString( "room_no" );
                this.userId = result.getString( "user_id" );
                this.rsvNo = result.getString( "rsv_no" );
                this.kind = result.getInt( "kind" );
                this.reason = result.getString( "reason" );
                this.amount = result.getInt( "amount" );
                this.usePoint = result.getInt( "use_point" );
                this.reflectFlag = result.getInt( "reflect_flag" );
                this.errorCode = result.getInt( "error_code" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCiError.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 予約ユーザ基本情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws SQLException
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT ap_ci_error SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " front_ip = ?,";
        query = query + " ci_date = ?,";
        query = query + " ci_time = ?,";
        query = query + " room_no = ?,";
        query = query + " user_id = ?,";
        query = query + " rsv_no = ?,";
        query = query + " kind = ?,";
        query = query + " reason = ?,";
        query = query + " amount = ?,";
        query = query + " use_point = ?,";
        query = query + " reflect_flag = ?,";
        query = query + " error_code = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.frontIp );
            prestate.setInt( 4, this.ciDate );
            prestate.setInt( 5, this.ciTime );
            prestate.setString( 6, this.roomNo );
            prestate.setString( 7, this.userId );
            prestate.setString( 8, this.rsvNo );
            prestate.setInt( 9, this.kind );
            prestate.setString( 10, this.reason );
            prestate.setInt( 11, this.amount );
            prestate.setInt( 12, this.usePoint );
            prestate.setInt( 13, this.reflectFlag );
            prestate.setInt( 14, this.errorCode );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCiError.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public boolean updateData(int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE ap_ci_error SET ";
        query = query + " front_ip = ?,";
        query = query + " ci_date = ?,";
        query = query + " ci_time = ?,";
        query = query + " room_no = ?,";
        query = query + " user_id = ?,";
        query = query + " rsv_no = ?,";
        query = query + " kind = ?,";
        query = query + " reason = ?,";
        query = query + " amount = ?,";
        query = query + " use_point = ?,";
        query = query + " reflect_flag = ?,";
        query = query + " error_code = ?";
        query = query + " WHERE id = ? ";
        query = query + " AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.frontIp );
            prestate.setInt( 2, this.ciDate );
            prestate.setInt( 3, this.ciTime );
            prestate.setString( 4, this.roomNo );
            prestate.setString( 5, this.userId );
            prestate.setString( 6, this.rsvNo );
            prestate.setInt( 7, this.kind );
            prestate.setString( 8, this.reason );
            prestate.setInt( 9, this.amount );
            prestate.setInt( 10, this.usePoint );
            prestate.setInt( 11, this.reflectFlag );
            prestate.setInt( 12, this.errorCode );
            prestate.setInt( 13, this.id );
            prestate.setInt( 14, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCiError.updateData] Exception=" + e.toString() );
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
