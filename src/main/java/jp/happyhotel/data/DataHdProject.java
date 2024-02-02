/*
 * @(#)DataHdProject.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ハピホテデベロッパーズのプロジェクト管理情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ハピホテデベロッパーズのプロジェクト管理データ取得クラス
 * 
 * @author Mitsuhashi-k1
 * @version 1.00 2019/07/13
 */
public class DataHdProject implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    /**
     * プロジェクトのID（DBの設計上の都合により作成されたものなので、外部のアプリケーションでは直接使用しないでください）。
     */
    private int               projectId;

    /**
     * プロジェクトの名前（プロジェクトIDとして使用され、外部のアプリケーションから見たときの、実質的なプライマリキーとなります）。
     */
    private String            name;

    /**
     * プロジェクトの表示名。
     */
    private String            display;

    /**
     * プロジェクトを識別するためのAPIキー（秘密鍵なので慎重に管理してください）。
     */
    private String            secretKey;

    /**
     * データを初期化します。
     */
    public DataHdProject()
    {
        projectId = 0;
        name = "";
        display = "";
        secretKey = "";
    }

    public int getProjectId()
    {
        return projectId;
    }

    public void setMemberId(int projectId)
    {
        this.projectId = projectId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDisplay()
    {
        return display;
    }

    public void setDisplay(String display)
    {
        this.display = display;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    /**
     * ハピホテデベロッパーズのプロジェクト管理情報データ取得
     * 
     * @param projectId プロジェクトID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int projectId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM happyhotel_developers.project WHERE project_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, projectId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.projectId = result.getInt( "project_id" );
                    this.name = result.getString( "name" );
                    this.display = result.getString( "display" );
                    this.secretKey = result.getString( "secret_key" );

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHdProject.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ハピホテデベロッパーズのプロジェクト管理情報データ設定
     * 
     * @param result ハピホテデベロッパーズのプロジェクト管理情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.projectId = result.getInt( "project_id" );
                this.name = result.getString( "name" );
                this.display = result.getString( "display" );
                this.secretKey = result.getString( "secret_key" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHdProject.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ハピホテデベロッパーズのプロジェクト管理情報データ追加
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

        query = "INSERT happyhotel_developers.project SET ";
        query = query + "project_id        =   ?,";
        query = query + "name              =   ?,";
        query = query + "display           =   ?,";
        query = query + "secret_key        =   ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, this.projectId );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.display );
            prestate.setString( i++, this.secretKey );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHdProject.insertData] Exception=" + e.toString() );
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
     * ハピホテデベロッパーズのプロジェクト管理情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param projectId プロジェクトID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int projectId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE happyhotel_developers.project SET ";
        query = query + "name             =   ?,";
        query = query + "display          =   ?,";
        query = query + "secret_key       =   ?";
        query = query + " WHERE project_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int i = 1;

            prestate.setString( i++, this.name );
            prestate.setString( i++, this.display );
            prestate.setString( i++, this.secretKey );
            prestate.setInt( i++, this.projectId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHdProject.updateData] Exception=" + e.toString() );
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
     * ハピホテデベロッパーズのプロジェクト管理データ存在チェック
     * (削除フラグは考慮する)
     * 
     * @param projectId プロジェクトID
     * @return 存在するとき：true 存在しないとき:false
     */
    public boolean existsData(int projectId)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT count(*) as cnt FROM happyhotel_developers.project WHERE project_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, projectId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "cnt" ) > 0 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHdProject.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
