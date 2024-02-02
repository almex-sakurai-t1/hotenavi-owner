/*
 * メール送信依頼データクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvMailRequest implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7035242476703552868L;
    private int               id;
    private String            reserveNo;
    private int               reserveSubNo;
    private int               requestMailKind;
    private int               requestFlag;
    private int               registTermKind;
    private int               registDate;
    private int               registTime;
    private String            language;

    /**
     * データの初期化
     */
    public DataRsvMailRequest()
    {
        id = 0;
        reserveNo = "";
        reserveSubNo = 0;
        requestMailKind = 0;
        requestFlag = 0;
        registTermKind = 0;
        registDate = 0;
        registTime = 0;
        language = "";
    }

    // getter
    public int getID()
    {
        return this.id;
    }

    public String getReserveNo()
    {
        return this.reserveNo;
    }

    public int getReserveSubNo()
    {
        return this.reserveSubNo;
    }

    public int getRequestMailKind()
    {
        return this.requestMailKind;
    }

    public int getRequestFlag()
    {
        return this.requestFlag;
    }

    public int getRegistTermKind()
    {
        return this.registTermKind;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public String getLanguage()
    {
        return this.language;
    }

    /**
     * 
     * setter
     * 
     */
    public void setId(int id)
    {
        this.id = id;
    }

    public void setReserveNo(String reserveno)
    {
        this.reserveNo = reserveno;
    }

    public void setReserveSubNo(int reservesubno)
    {
        this.reserveSubNo = reservesubno;
    }

    public void setRequestMailKind(int requestmailkind)
    {
        this.requestMailKind = requestmailkind;
    }

    public void setRequestFlag(int requestflag)
    {
        this.requestFlag = requestflag;
    }

    public void setRegistTermKind(int registtermkind)
    {
        this.registTermKind = registtermkind;
    }

    public void setRegistDate(int registdate)
    {
        this.registDate = registdate;
    }

    public void setRegistTime(int registtime)
    {
        this.registTime = registtime;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * メール送信依頼データ情報取得
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @param reserveSubNo 予約番号枝番
     * @return 処理結果(True:正常,False:異常)
     */
    public boolean getData(int id, String reserveNo, int reserveSubNo)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, reserve_no, reserve_sub_no, request_mail_kind," +
                " request_flag, regist_term_kind, regist_date, regist_time " +
                " FROM hh_rsv_mail_request WHERE id = ? AND reserve_no = ?" +
                " AND reserve_sub_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, reserveSubNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.reserveSubNo = result.getInt( "reserve_sub_no" );
                    this.requestMailKind = result.getInt( "request_mail_kind" );
                    this.requestFlag = result.getInt( "request_flag" );
                    this.registTermKind = result.getInt( "regist_term_kind" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMailRequest.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * メール送信依頼データ情報取得
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @param requestMailKind メール種類
     * @param requestMailKind メール種類
     * @return 処理結果(True:正常,False:異常)
     */
    public boolean getData(Connection connection, int id, String reserveNo, int requestMailKind)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, reserve_no, reserve_sub_no, request_mail_kind," +
                " request_flag, regist_term_kind, regist_date, regist_time ,language" +
                " FROM newRsvDB.hh_rsv_mail_request WHERE id = ? AND reserve_no = ?" +
                " AND request_mail_kind = ? " +
                " AND request_flag = 1 " +
                " ORDER BY reserve_sub_no DESC LIMIT 0,1";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, requestMailKind );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.reserveSubNo = result.getInt( "reserve_sub_no" );
                    this.requestMailKind = result.getInt( "request_mail_kind" );
                    this.requestFlag = result.getInt( "request_flag" );
                    this.registTermKind = result.getInt( "regist_term_kind" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.language = result.getString( "language" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMailRequest.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予約データ登録
     * 
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

        query = "INSERT INTO hh_rsv_mail_request SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", reserve_sub_no = ? " +
                ", request_mail_kind = ? " +
                ", request_flag = ? " +
                ", regist_term_kind = ? " +
                ", regist_date = ? " +
                ", regist_time = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.reserveNo );
            prestate.setInt( 3, this.reserveSubNo );
            prestate.setInt( 4, this.requestMailKind );
            prestate.setInt( 5, this.requestFlag );
            prestate.setInt( 6, this.registTermKind );
            prestate.setInt( 7, this.registDate );
            prestate.setInt( 8, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMailRequest.insertData] Exception=" + e.toString() );
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
     * 予約データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn, String Schema)
    {
        boolean ret;
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            int result;
            String query;
            PreparedStatement prestate = null;

            ret = false;

            query = "INSERT INTO " + ReserveCommon.SCHEMA_NEWRSV + ".hh_rsv_mail_request SET " +
                    "  id = ?" +
                    ", reserve_no = ?" +
                    ", reserve_sub_no = ? " +
                    ", request_mail_kind = ? " +
                    ", request_flag = ? " +
                    ", regist_term_kind = ? " +
                    ", regist_date = ? " +
                    ", regist_time = ? " +
                    ", language = ? ";

            try
            {
                prestate = conn.prepareStatement( query );
                // 更新対象の値をセットする
                prestate.setInt( 1, this.id );
                prestate.setString( 2, this.reserveNo );
                prestate.setInt( 3, this.reserveSubNo );
                prestate.setInt( 4, this.requestMailKind );
                prestate.setInt( 5, this.requestFlag );
                prestate.setInt( 6, this.registTermKind );
                prestate.setInt( 7, this.registDate );
                prestate.setInt( 8, this.registTime );
                prestate.setString( 9, this.language );

                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataRsvMailRequest.insertData] Exception=" + e.toString() );
                ret = false;
            }
        }
        else
        {
            ret = insertData( conn );
        }
        return(ret);

    }

    /**
     * 予約データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_mail_request SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", reserve_sub_no = ? " +
                ", request_mail_kind = ? " +
                ", request_flag = ? " +
                ", regist_term_kind = ? " +
                ", regist_date = ? " +
                ", regist_time = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.reserveNo );
            prestate.setInt( 3, this.reserveSubNo );
            prestate.setInt( 4, this.requestMailKind );
            prestate.setInt( 5, this.requestFlag );
            prestate.setInt( 6, this.registTermKind );
            prestate.setInt( 7, this.registDate );
            prestate.setInt( 8, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMailRequest.insertData] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);

    }
}
