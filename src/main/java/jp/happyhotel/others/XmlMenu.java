/*
 * @(#)FelicaMatching.java 1.00 2009/08/02
 * Copyright (C) ALMEX Inc. 2012
 * フェリカ紐付けクラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSystemMenu;

/**
 * XMLメニューデータ
 * メニューを取得する
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/12
 */
public class XmlMenu implements Serializable
{
    private int              menuCount;
    private DataSystemMenu[] menuData;

    /**
     * データを初期化します。
     */
    public XmlMenu()
    {
        menuCount = 0;
    }

    /** ユーザ基本情報件数取得 **/
    public int getCount()
    {
        return(menuCount);
    }

    /** ユーザ基本情報取得 **/
    public DataSystemMenu[] getMenuDataInfo()
    {
        return(menuData);
    }

    /***
     * 
     * @param kind
     * @param dispCount
     * @return
     */
    public boolean getMenu(int kind, int dispCount)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int today = Integer.parseInt( DateEdit.getDate( 2 ) );

        if ( kind < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_system_menu";
        query += " WHERE kind = ?";
        query += " AND start_date <= ? ";
        query += " AND end_date >= ? ";
        query += " AND del_flag = 0 ";
        query += " ORDER BY disp_no % 100";
        query += " Limit 0, ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, today );
            prestate.setInt( 3, today );
            prestate.setInt( 4, dispCount );
            ret = getMenuSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[XmlMenu.getMenu()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);

    }

    /**
     * メニューのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMenuSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    menuCount = result.getRow();
                }
                this.menuData = new DataSystemMenu[this.menuCount];

                for( i = 0 ; i < menuCount ; i++ )
                {
                    menuData[i] = new DataSystemMenu();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // アンケート回答情報の設定
                    this.menuData[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[XmlMenu.getMenuSub()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( menuCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

}
