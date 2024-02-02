/*
 * @(#)MasterQuestionData.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 アンケート質問取得クラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterSpot;

/**
 * スポットマスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/28
 */
public class MasterSpot1 implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -2851199667940699294L;
    int                       spotCount;
    int                       spotAllCount;
    DataMasterSpot[]          masterSpot;

    /**
     * データを初期化します。
     */
    public MasterSpot1()
    {
        spotCount = 0;
        spotAllCount = 0;
    }

    /** スポット件数取得 **/
    public int getCount()
    {
        return(spotCount);
    }

    /** スポット件数取得 **/
    public int getAllCount()
    {
        return(spotAllCount);
    }

    public DataMasterSpot[] getMasterSpotInfo()
    {
        return masterSpot;
    }

    /**
     * スポットマスタを取得する
     * 
     * @param countNum 取得件数
     * @param pageNum ページ番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMasterSpot(int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        /* 任意のページ、任意の件数を取得するSQL */
        query = "SELECT * FROM hh_master_spot";
        query = query + " ORDER BY hh_master_spot.disp_index";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            ret = getMasterSpotSub( prestate );
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[MasterSpot.getMasterSpot()] Exception=" + e.toString() );
        }
        finally
        {
            prestate = null;
        }

        if ( ret != false )
        {

            /* 全件数を取得するSQL */
            query = "SELECT * FROM hh_master_spot";

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                // 全件数を取得
                ret = getMasterSpotCountSub( prestate );
            }
            catch ( Exception e )
            {
                Logging.error( "[MasterSpot.getMapSpot()] Exception=" + e.toString() );
                return(false);
            }
            finally
            {
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( connection );
            }
        }
        return(ret);
    }

    /**
     * スポットマスタのデータをセット
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterSpotSub(PreparedStatement prestate)
    {
        int count;
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();
            count = 0;

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.spotCount = result.getRow();
                }
                this.masterSpot = new DataMasterSpot[this.spotCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.masterSpot[count] = new DataMasterSpot();
                    this.masterSpot[count].setData( result );
                    count++;
                }
            }
            else
            {
                this.spotCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MasterSpot.getMasterSpotSub()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * スポットマスタの全件数をセット
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMasterSpotCountSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.spotAllCount = result.getRow();
                }
            }
            else
            {
                this.spotAllCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MasterSpot.getMasterSpotCountSub()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

}
