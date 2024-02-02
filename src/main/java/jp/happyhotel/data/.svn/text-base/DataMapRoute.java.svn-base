/*
 * @(#)DataMasterCity.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 地図路線マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;
import jp.happyhotel.common.*;

/**
 * 地図路線マスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMapRoute implements Serializable
{
    private static final long serialVersionUID = -7317009974893855435L;

    /** 路線ID **/
    private String            routeId;
    /** 属性分類コード **/
    private String            classCode;
    /** 路線名称 **/
    private String            name;
    /** 路線名称カナ **/
    private String            nameKana;
    /** 路線名通称 **/
    private String            routeName;
    /** 会社名通称 **/
    private String            companyName;

    /**
     * データを初期化します。
     */
    public DataMapRoute()
    {
        routeId = "";
        classCode = "";
        name = "";
        nameKana = "";
        routeName = "";
        companyName = "";
    }

    public String getClassCode()
    {
        return classCode;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getRouteId()
    {
        return routeId;
    }

    public String getRouteName()
    {
        return routeName;
    }

    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setRouteId(String routeId)
    {
        this.routeId = routeId;
    }

    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }

    /**
     * 地図路線マスタデータ取得
     * 
     * @param routeId 路線ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String routeId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT * FROM hh_map_route WHERE route_id = ?";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, routeId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.routeId = result.getString( "route_id" );
                    this.classCode = result.getString( "class_code" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.routeName = result.getString( "route_name" );
                    this.companyName = result.getString( "company_name" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapRoute.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 地図路線マスタデータ設定
     * 
     * @param result 地図路線データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.routeId = result.getString( "route_id" );
                this.classCode = result.getString( "class_code" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.routeName = result.getString( "route_name" );
                this.companyName = result.getString( "company_name" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapRoute.setData] Exception=" + e.toString() );
        }
        return(true);
    }
}
