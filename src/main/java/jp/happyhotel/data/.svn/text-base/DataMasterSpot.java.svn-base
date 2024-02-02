package jp.happyhotel.data;

/*
 * @(#)DataUserPoint.java 1.00 2010/04/28
 * Copyright (C) ALMEX Inc. 2007
 * スポットマスタ情報取得クラス
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * スポットマスタ情報取得
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/28
 */
public class DataMasterSpot implements Serializable
{
    private static final long serialVersionUID = 9008558795968322474L;
    private int               spotId;
    private String            spotName;
    private String            header;
    private String            footer;
    private String            detail;
    private int               startDate;
    private int               endDate;
    private int               dispIndex;
    private String            memo;

    public DataMasterSpot()
    {
        spotId = 0;
        spotName = "";
        header = "";
        footer = "";
        detail = "";
        detail = "";
        startDate = 0;
        endDate = 0;
        dispIndex = 0;
        memo = "";
    }

    /* getter */
    public String getDetail()
    {
        return detail;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getFooter()
    {
        return footer;
    }

    public String getHeader()
    {
        return header;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getSpotId()
    {
        return spotId;
    }

    public String getSpotName()
    {
        return spotName;
    }

    public int getStartDate()
    {
        return startDate;
    }

    /* getter */

    /* setter */
    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setFooter(String footer)
    {
        this.footer = footer;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setSpotId(int spotId)
    {
        this.spotId = spotId;
    }

    public void setSpotName(String spotName)
    {
        this.spotName = spotName;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    /* setter */

    /**
     * スポットマスタ取得
     * 
     * @param spotId スポットID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int spotId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_master_spot WHERE spot_id = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.spotId = result.getInt( "spot_id" );
                    this.spotName = result.getString( "spot_name" );
                    this.header = result.getString( "header" );
                    this.footer = result.getString( "footer" );
                    this.detail = result.getString( "detail" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.memo = result.getString( "memo" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSpot.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * スポットマスタ設定
     * 
     * @param result スポットマスタレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.spotId = result.getInt( "spot_id" );
                this.spotName = result.getString( "spot_name" );
                this.header = result.getString( "header" );
                this.footer = result.getString( "footer" );
                this.detail = result.getString( "detail" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.dispIndex = result.getInt( "disp_index" );
                this.memo = result.getString( "memo" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSpot.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * スポットマスタ情報データ追加
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

        query = "INSERT hh_master_spot SET ";
        query = query + " spot_id = ?,";
        query = query + " spot_name = ?,";
        query = query + " header = ?,";
        query = query + " footer = ?,";
        query = query + " detail = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " disp_index = ?";
        query = query + " memo = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.spotId );
            prestate.setString( 2, this.spotName );
            prestate.setString( 3, this.header );
            prestate.setString( 4, this.footer );
            prestate.setString( 5, this.detail );
            prestate.setInt( 6, this.startDate );
            prestate.setInt( 7, this.endDate );
            prestate.setInt( 8, this.dispIndex );
            prestate.setString( 9, this.memo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSpot.insertData] Exception=" + e.toString() );
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
     * スポットマスタ情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param spotId スポットID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int spotId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_master_spot SET ";
        query = query + " spot_name = ?,";
        query = query + " header = ?,";
        query = query + " footer = ?,";
        query = query + " detail = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " disp_index = ?";
        query = query + " memo = ?,";
        query = query + " WHERE spot_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.spotName );
            prestate.setString( 2, this.header );
            prestate.setString( 3, this.footer );
            prestate.setString( 4, this.detail );
            prestate.setInt( 5, this.startDate );
            prestate.setInt( 6, this.endDate );
            prestate.setInt( 7, this.dispIndex );
            prestate.setString( 8, this.memo );
            prestate.setInt( 9, spotId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSpot.updateData] Exception=" + e.toString() );
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
     * スポットマスタ取得(期限内のデータを取得)
     * 
     * @param spotId スポットID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByLimit(int spotId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_master_spot WHERE spot_id = ?";
        query += " AND start_date <= " + DateEdit.getDate( 2 );
        query += " AND end_date >= " + DateEdit.getDate( 2 );

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.spotId = result.getInt( "spot_id" );
                    this.spotName = result.getString( "spot_name" );
                    this.header = result.getString( "header" );
                    this.footer = result.getString( "footer" );
                    this.detail = result.getString( "detail" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.memo = result.getString( "memo" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSpot.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
