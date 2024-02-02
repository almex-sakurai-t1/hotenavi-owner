/*
 * @(#)SearchSystemInfo.java 1.00 2008/04/09 Copyright (C) ALMEX Inc. 2008 更新情報履歴データ取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSystemInfo;

/**
 * 更新情報履歴データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2008/04/09
 */
public class SearchSystemInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID       = -6027036515228791561L;

    private static final int  DATATYPE_MAGAZINE_LIST = 13;

    private int               dataCount;
    // private int allCount;
    private DataSystemInfo[]  systemInfo;
    private int[]             dataTypeList;

    /**
     * データを初期化します。
     */
    public SearchSystemInfo()
    {
        dataCount = 0;
        // allCount = 0;
        systemInfo = new DataSystemInfo[0];
    }

    public int getAllCount()
    {
        return 0;
    }

    public int getDataCount()
    {
        return dataCount;
    }

    public DataSystemInfo[] getSystemInfo()
    {
        return systemInfo;
    }

    public int[] getDataTypeList()
    {
        return dataTypeList;
    }

    /**
     * 更新情報履歴データ取得(disp_idx降順)
     * 
     * @param dataType 表示タイプ
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataList(int dataType, int countNum, int pageNum)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( dataType == DATATYPE_MAGAZINE_LIST )
        {
            // 雑誌掲載リストは開始日付無関係で表示
            query = "SELECT * FROM hh_system_info WHERE";
            query = query + " hh_system_info.disp_flag = 1";
            query = query + " AND hh_system_info.data_type = ?";
            query = query + " AND hh_system_info.end_date >= ?";
            query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";
        }
        else
        {
            query = "SELECT * FROM hh_system_info WHERE";
            query = query + " hh_system_info.disp_flag = 1";
            query = query + " AND hh_system_info.data_type = ?";
            query = query + " AND hh_system_info.start_date <= ?";
            query = query + " AND hh_system_info.end_date >= ?";
            query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            if ( dataType == DATATYPE_MAGAZINE_LIST )
            {
                prestate.setInt( 1, dataType );
                prestate.setInt( 2, nowDate );
            }
            else
            {
                prestate.setInt( 1, dataType );
                prestate.setInt( 2, nowDate );
                prestate.setInt( 3, nowDate );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 更新情報履歴データ取得(dataType,id指定)
     * 
     * @param dataType 表示タイプ
     * @param id データID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataById(int dataType, int id)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( dataType == DATATYPE_MAGAZINE_LIST )
        {
            // 雑誌掲載リストは開始日付無関係で表示
            query = "SELECT * FROM hh_system_info WHERE";
            query = query + " hh_system_info.disp_flag = 1";
            query = query + " AND hh_system_info.data_type = ?";
            query = query + " AND hh_system_info.end_date >= ?";
            query = query + " AND hh_system_info.id = ?";
            query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";
        }
        else
        {
            query = "SELECT * FROM hh_system_info WHERE";
            query = query + " hh_system_info.disp_flag = 1";
            query = query + " AND hh_system_info.data_type = ?";
            query = query + " AND hh_system_info.start_date <= ?";
            query = query + " AND hh_system_info.end_date >= ?";
            query = query + " AND hh_system_info.id = ?";
            query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            if ( dataType == DATATYPE_MAGAZINE_LIST )
            {
                prestate.setInt( 1, dataType );
                prestate.setInt( 2, nowDate );
                prestate.setInt( 3, id );
            }
            else
            {
                prestate.setInt( 1, dataType );
                prestate.setInt( 2, nowDate );
                prestate.setInt( 3, nowDate );
                prestate.setInt( 4, id );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * データ取得(dataType,dispFlag指定・start_date,last_update,last_uptime順)
     * 
     * @param dataType 表示タイプ
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param dispFlag 表示フラグ(0:非表示以外 1:PC, 2:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataListByDispFlag(int dataType, int countNum, int pageNum, int dispFlag)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        query = query + " AND hh_system_info.data_type = ?";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id > 0";
        query = query + " ORDER BY hh_system_info.start_date DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataType );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * データ取得(dataType範囲,dispFlag指定・start_date,last_update,last_uptime順)
     * 
     * @param dataType 表示タイプ(開始)
     * @param dataType 表示タイプ(終了)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param dispFlag 表示フラグ(0:非表示以外 1:PC, 2:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataListByDispFlag(int dataTypeStart, int dataTypeEnd, int countNum, int pageNum, int dispFlag)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        query = query + " AND hh_system_info.data_type >= ?";
        query = query + " AND hh_system_info.data_type <= ?";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id > 0";
        query = query + " ORDER BY hh_system_info.start_date DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataTypeStart );
            prestate.setInt( 2, dataTypeEnd );
            prestate.setInt( 3, nowDate );
            prestate.setInt( 4, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * データ取得(dataType,dispFlag,memberOnly指定・start_date,last_update,last_uptime順)
     * 
     * @param dataType 表示タイプ
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param dispFlag 表示フラグ(0:非表示以外 1:PC, 2:携帯)
     * @param memberOnly メンバー限定フラグ(1:メンバー 2:ビジター)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataListByMemberOnly(int dataType, int countNum, int pageNum, int dispFlag, int memberOnly)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        if ( memberOnly == 1 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 1)";
        }
        else if ( memberOnly == 2 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 2)";
        }
        query = query + " AND hh_system_info.data_type = ?";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id > 0";
        query = query + " ORDER BY hh_system_info.start_date DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataType );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * Topicsデータ取得(dataType,dispFlag,memberOnly指定・disp_idx降順)
     * 
     * @param dataType 表示タイプ
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param dispFlag 表示フラグ(0:非表示以外 1:PC, 2:携帯, 3:スマートフォン )
     * @param memberOnly メンバー限定フラグ(0:両方表示 1:メンバー 2:ビジター)
     * @param carrierFlag キャリアフラグ(0:PC)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getTopicsDataList(int dataType, int countNum, int pageNum, int dispFlag, int memberOnly, int carrierFlag)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 3 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 4)";
        }

        if ( memberOnly == 1 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 1)";
        }
        else if ( memberOnly == 2 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 2)";
        }

        if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            query = query + " AND hh_system_info.url_docomo <> ''";
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
        {
            query = query + " AND hh_system_info.url_au <> ''";
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
        {
            query = query + " AND hh_system_info.url_softbank <> ''";
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
        {
            query = query + " AND hh_system_info.url_smart <> ''";
        }

        query = query + " AND hh_system_info.data_type = ?";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id > 0";
        query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataType );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * お客様の声新着データ取得(dataType範囲,dispFlag指定・start_date,last_update,last_uptime順) (id=0のレコードが表示の場合のみ、それに属するレコードを取得する)
     * 
     * @param dataType 表示タイプ(開始)
     * @param dataType 表示タイプ(終了)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param dispFlag 表示フラグ(1:PC, 2:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUsersVoiceListNew(int dataTypeStart, int dataTypeEnd, int countNum, int pageNum, int dispFlag)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // id=0のレコード(タイトル)のdisp_flagが該当するdata_typeを取得
        query = "SELECT data_type FROM hh_system_info WHERE";
        query = query + " data_type >= ?";
        query = query + " AND data_type <= ?";
        query = query + " AND id = 0";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        if ( dispFlag == 1 )
        {
            query = query + " AND (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " AND (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " AND hh_system_info.disp_flag != 0";
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataTypeStart );
            prestate.setInt( 2, dataTypeEnd );
            prestate.setInt( 3, nowDate );
            prestate.setInt( 4, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // 配列を初期化する。
                this.dataTypeList = new int[this.dataCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.dataTypeList[count] = result.getInt( "data_type" );
                    Logging.info( "[SearchSystemInfo.getDataList] dataTypelistNew=" + this.dataTypeList[count] );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        if ( dataTypeList != null )
        {
            if ( dataTypeList.length > 0 )
            {
                query = query + " AND hh_system_info.data_type IN(";
                for( i = 0 ; i < dataTypeList.length ; i++ )
                {
                    query = query + dataTypeList[i];
                    if ( i < dataTypeList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id > 0";
        query = query + " ORDER BY hh_system_info.start_date DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, nowDate );
            prestate.setInt( 2, nowDate );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * データ取得(dataType,id,dispFlag指定)
     * 
     * @param dataType 表示タイプ
     * @param id データID
     * @param dispFlag 表示フラグ(1:PC, 2:携帯)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByDispFlag(int dataType, int id, int dispFlag)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        query = query + " AND hh_system_info.data_type = ?";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id = ?";
        query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataType );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );
            prestate.setInt( 4, id );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * データ取得(dataType,id,dispFlag,memberOnly指定)
     * 
     * @param dataType 表示タイプ
     * @param id データID
     * @param dispFlag 表示フラグ(1:PC, 2:携帯)
     * @param memberOnly メンバー限定フラグ(1:メンバー 2:ビジター)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByMemberOnly(int dataType, int id, int dispFlag, int memberOnly)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        if ( memberOnly == 1 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 1)";
        }
        else if ( memberOnly == 2 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 2)";
        }
        query = query + " AND hh_system_info.data_type = ?";
        query = query + " AND hh_system_info.start_date <= ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id = ?";
        query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataType );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );
            prestate.setInt( 4, id );

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * データ取得(dataType,id,dispFlag,memberOnly指定 ※開始日付以前でもアドレス指定で表示する)
     * 
     * @param dataType 表示タイプ
     * @param id データID
     * @param dispFlag 表示フラグ(1:PC, 2:携帯)
     * @param memberOnly メンバー限定フラグ(1:メンバー 2:ビジター)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByMemberOnlyPre(int dataType, int id, int dispFlag, int memberOnly)
    {
        int i;
        int count;
        int nowDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_info WHERE";
        if ( dispFlag == 1 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 2)";
        }
        else if ( dispFlag == 2 )
        {
            query = query + " (hh_system_info.disp_flag = 1 OR hh_system_info.disp_flag = 3)";
        }
        else if ( dispFlag == 0 )
        {
            query = query + " hh_system_info.disp_flag != 0";
        }
        if ( memberOnly == 1 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 1)";
        }
        else if ( memberOnly == 2 )
        {
            query = query + " AND (hh_system_info.member_only = 0 OR hh_system_info.member_only = 2)";
        }
        query = query + " AND hh_system_info.data_type = ?";
        query = query + " AND hh_system_info.end_date >= ?";
        query = query + " AND hh_system_info.id = ?";
        query = query + " ORDER BY hh_system_info.disp_idx DESC, hh_system_info.last_update DESC, hh_system_info.last_uptime DESC";

        count = 0;
        dataCount = 0;
        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, dataType );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, id );

            result = prestate.executeQuery();
            Logging.info( query );
            Logging.info( "" + dataType );
            Logging.info( "" + nowDate );
            Logging.info( "" + id );

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    dataCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.systemInfo = new DataSystemInfo[this.dataCount];
                for( i = 0 ; i < dataCount ; i++ )
                {
                    systemInfo[i] = new DataSystemInfo();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.systemInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemInfo.getDataByMemberOnlyPre] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }
}
