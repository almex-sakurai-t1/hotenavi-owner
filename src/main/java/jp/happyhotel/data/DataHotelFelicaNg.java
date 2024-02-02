package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Thu Nov 14 16:36:58 JST 2013
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ハピホテタッチNGデータ
 * 
 * @author tashiro-s1
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2013/11/14 tashiro-s1 Generated.
 */
public class DataHotelFelicaNg implements Serializable
{

    private static final long serialVersionUID = 1568757283385831471L;
    private int               id;
    private int               seq;
    private int               kind;
    private String            idm;
    private String            memo;
    private int               registDate;
    private int               registTime;

    public DataHotelFelicaNg()
    {
        id = 0;
        seq = 0;
        kind = 0;
        idm = "";
        memo = "";
        registDate = 0;
        registTime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getKind()
    {
        return kind;
    }

    public String getIdm()
    {
        return idm;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setIdm(String idm)
    {
        this.idm = idm;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /**
     * ホテルフェリカNGデータ
     * 
     * @param licenceKey ライセンスキー
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String licenceKey, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_felica_ng WHERE licence_key = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, licenceKey );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.kind = result.getInt( "kind" );
                    this.idm = result.getString( "idm" );
                    this.memo = result.getString( "memo" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelFelicaNg.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルフェリカNGデータ
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.kind = result.getInt( "kind" );
                this.idm = result.getString( "idm" );
                this.memo = result.getString( "memo" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelFelicaNg.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ホテル認証データ追加
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

        query = "INSERT hh_hotel_felica_ng SET ";
        query += " id = ?,";
        query += " seq = 0,";
        query += " kind = ?,";
        query += " idm = ?,";
        query += " memo = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.kind );
            prestate.setString( 3, this.idm );
            prestate.setString( 4, this.memo );
            prestate.setInt( 5, this.registDate );
            prestate.setInt( 6, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelFelicaNg.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /****
     * ホテル認証データ追加
     * 
     * @param id ホテルID
     * @param kind タッチ区分
     * @param idm フェリカID
     * @param memo メモ
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(int id, int kind, String idm, String memo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_hotel_felica_ng SET ";
        query += " id = ?,";
        query += " seq = 0,";
        query += " kind = ?,";
        query += " idm = ?,";
        query += " memo = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, id );
            prestate.setInt( 2, kind );
            prestate.setString( 3, idm );
            prestate.setString( 4, memo );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 6, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelFelicaNg.insertData] Exception=" + e.toString() );
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
     * ホテル認証データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param licencekey ライセンスキー
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String licencekey, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_felica_ng SET ";
        query += " kind = ?,";
        query += " idm = ?,";
        query += " memo = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?";
        query += " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.kind );
            prestate.setString( 2, this.idm );
            prestate.setString( 3, this.memo );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setInt( 6, id );
            prestate.setInt( 7, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelFelicaNg.updateData] Exception=" + e.toString() );
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
