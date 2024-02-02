/*
 * @(#)DataMasterFortune.java 1.00 2008/12/02 Copyright (C) ALMEX Inc. 2008 占いマスタ取得クラス
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
 * 占いマスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/12/02
 */
public class DataMasterFortune implements Serializable
{
    private static final long serialVersionUID = 8762475592669784462L;

    private int               kind;
    private int               seq;
    private String            name;
    private String            url;
    private String            urlAu;
    private String            urlDocomo;
    private String            urlSoftBank;
    private String            color;
    private int               nonDispFlag;

    /**
     * データを初期化します。
     */
    public DataMasterFortune()
    {
        kind = 0;
        seq = 0;
        name = "";
        url = "";
        urlDocomo = "";
        urlAu = "";
        urlSoftBank = "";
        color = "";
        nonDispFlag = 0;
    }

    public String getColor()
    {
        return color;
    }

    public int getKind()
    {
        return kind;
    }

    public String getName()
    {
        return name;
    }

    public int getNonDispFlag()
    {
        return nonDispFlag;
    }

    public String getUrl()
    {
        return Url.convertUrl( url );
    }

    public String getUrlAu()
    {
        return Url.convertUrl( urlAu );
    }

    public String getUrlDocomo()
    {
        return Url.convertUrl( urlDocomo );
    }

    public String getUrlSoftBank()
    {
        return Url.convertUrl( urlSoftBank );
    }

    public int getSeq()
    {
        return seq;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNonDispFlag(int nonDispFlag)
    {
        this.nonDispFlag = nonDispFlag;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUrlAu(String urlAu)
    {
        this.urlAu = urlAu;
    }

    public void setUrlDocomo(String urlDocomo)
    {
        this.urlDocomo = urlDocomo;
    }

    public void getUrlSoftBank(String urlSoftBank)
    {
        this.urlSoftBank = urlSoftBank;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    /**
     * 占いデータ取得
     * 
     * @param kind 種類
     * @param seq 名前
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int kind, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_fortune WHERE kind = ? AND seq = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.kind = result.getInt( "kind" );
                    this.seq = result.getInt( "seq" );
                    this.name = result.getString( "name" );
                    this.url = result.getString( "url" );
                    this.urlDocomo = result.getString( "url_docomo" );
                    this.urlAu = result.getString( "url_au" );
                    this.urlSoftBank = result.getString( "url_softbank" );
                    this.color = result.getString( "color" );
                    this.nonDispFlag = result.getInt( "nondisp_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterFortune.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 占いデータ設定
     * 
     * @param result 占いデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.kind = result.getInt( "kind" );
                this.seq = result.getInt( "seq" );
                this.name = result.getString( "name" );
                this.url = result.getString( "url" );
                this.urlDocomo = result.getString( "url_docomo" );
                this.urlAu = result.getString( "url_au" );
                this.urlSoftBank = result.getString( "url_softbank" );
                this.color = result.getString( "color" );
                this.nonDispFlag = result.getInt( "nondisp_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterFortune.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 占いデータ設定
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

        query = "INSERT hh_master_fortune SET";
        query = query + " kind = ?,";
        query = query + " seq = ?,";
        query = query + " name = ?,";
        query = query + " url = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " color = ?,";
        query = query + " nondisp_flag = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.kind );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.name );
            prestate.setString( 4, this.url );
            prestate.setString( 5, this.urlDocomo );
            prestate.setString( 6, this.urlAu );
            prestate.setString( 7, this.urlSoftBank );
            prestate.setString( 8, this.color );
            prestate.setInt( 9, this.nonDispFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterFortune.insertData] Exception=" + e.toString() );
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
     * @param kind 種類
     * @param seq 管理番号
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int kind, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_fortune SET";
        query = query + " name = ?,";
        query = query + " url = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " color = ?,";
        query = query + " nondisp_flag = ?";
        query = query + " WHERE kind = ?";
        query = query + " AND seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.name );
            prestate.setString( 2, this.url );
            prestate.setString( 3, this.urlDocomo );
            prestate.setString( 4, this.urlAu );
            prestate.setString( 5, this.urlSoftBank );
            prestate.setString( 6, this.color );
            prestate.setInt( 7, this.nonDispFlag );
            prestate.setInt( 8, kind );
            prestate.setInt( 9, seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterFortune.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
