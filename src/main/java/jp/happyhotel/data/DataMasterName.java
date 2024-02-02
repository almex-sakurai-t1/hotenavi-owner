/*
 * @(#)DataMasterName.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 名称マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 名称マスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterName implements Serializable
{
    private static final long serialVersionUID = -6185468558334380773L;

    /** 名称区分 **/
    private int               classId;
    /** 名称コード **/
    private int               code;
    /** 名称（漢字） **/
    private String            name;
    /** 名称（カナ） **/
    private String            nameKana;
    /** 名称（略称） **/
    private String            nameShort;

    /**
     * データを初期化します。
     */
    public DataMasterName()
    {
        classId = 0;
        code = 0;
        name = "";
        nameKana = "";
        nameShort = "";
    }

    public int getClassId()
    {
        return classId;
    }

    public int getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getNameShort()
    {
        return nameShort;
    }

    public void setClassId(int classId)
    {
        this.classId = classId;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setNameShort(String nameShort)
    {
        this.nameShort = nameShort;
    }

    /**
     * 名称データ取得
     * 
     * @param classId 名称区分
     * @param classCode 名称コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int classId, int classCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_name WHERE class = ? AND code = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, classId );
            prestate.setInt( 2, classCode );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.classId = result.getInt( "class" );
                    this.code = result.getInt( "code" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.nameShort = result.getString( "name_short" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterName.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 名称データ設定
     * 
     * @param result 都道府県データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.classId = result.getInt( "class" );
                this.code = result.getInt( "code" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.nameShort = result.getString( "name_short" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterName.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
