/*
 * @(#)DataMasterSponsorArea.java 1.00 2007/09/01 Copyright (C) ALMEX Inc. 2007 スポンサーエリア管理マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * スポンサーエリア管理マスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/01
 * @version 1.1 2007/11/29
 */
public class DataMasterSponsorArea implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -1385742648576254070L;

    private int               areaCode;
    private int               maxCount;
    private String            name;
    private String            nameKana;

    /**
     * データを初期化します。
     */
    public DataMasterSponsorArea()
    {
        areaCode = 0;
        maxCount = 0;
        name = "";
        nameKana = "";
    }

    public int getAreaCode()
    {
        return areaCode;
    }

    public int getMaxCount()
    {
        return maxCount;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public void setAreaCode(int areaCode)
    {
        this.areaCode = areaCode;
    }

    public void setMaxCount(int maxCount)
    {
        this.maxCount = maxCount;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    /**
     * スポンサーエリア管理マスタ取得
     * 
     * @param areaCode スポンサーエリアコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int areaCode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_master_sponsor_area WHERE area_code = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, areaCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.areaCode = result.getInt( "area_code" );
                    this.maxCount = result.getInt( "max_count" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSponsorArea.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * スポンサーエリア管理マスタ設定
     * 
     * @param result スポンサーエリア管理マスタレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.areaCode = result.getInt( "area_code" );
                this.maxCount = result.getInt( "max_count" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSponsorArea.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
