/*
 * @(#)SearchHotelMessage.java 1.00 2007/07/18
 * Copyright (C) ALMEX Inc. 2007
 * ホテル最新情報検索クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelMessage;

/**
 * ホテル最新情報検索クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/20
 */
public class SearchHotelMessage implements Serializable
{
    private static final long  serialVersionUID = -4699924906958739993L;
    private static final int   LAST_MONTH       = -2;                   // 3ヶ月前の日付までを取得する
    private int                m_hotelCount;
    private int                m_hotelAllCount;
    private int[]              m_hotelIdList;
    private DataHotelBasic[]   m_hotelInfo;
    private String[]           m_hotelMessage;
    private int[]              m_hotelMessageDate;
    private DataHotelMessage[] m_hotelMsgInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelMessage()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_hotelIdList = new int[0];
        m_hotelMessage = new String[0];
        m_hotelInfo = new DataHotelBasic[0];
    }

    public int getCount()
    {
        return(m_hotelCount);
    }

    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    public String[] getHotelMessage()
    {
        return(m_hotelMessage);
    }

    public int[] getHotelMessageDate()
    {
        return(m_hotelMessageDate);
    }

    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    /**
     * ホテル最新情報検索結果取得(ホテルランク順)
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(int[] hotelIdList, int countNum, int pageNum)
    {
        int i = 0;
        int count;
        int countData;
        boolean ret;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        countData = 0;
        ret = false;

        if ( hotelIdList != null )
        {
            if ( hotelIdList.length == 0 )
            {
                return(false);
            }
        }
        query = "SELECT hh_hotel_message.* FROM hh_hotel_message,hh_hotel_basic";
        query = query + " WHERE ( hh_hotel_message.start_date < ? OR ( hh_hotel_message.start_date = ? AND hh_hotel_message.start_time <= ? ))";
        query = query + " AND ( hh_hotel_message.start_date >= ? )";
        query = query + " AND ( hh_hotel_message.end_date > ?  OR ( hh_hotel_message.end_date = ? AND hh_hotel_message.end_time >= ? ))";
        query = query + " AND hh_hotel_message.del_flag = 0";

        if ( hotelIdList != null )
        {
            if ( hotelIdList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < hotelIdList.length ; i++ )
                {
                    query = query + hotelIdList[i];
                    if ( i < hotelIdList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }
        query = query + " AND hh_hotel_basic.id = hh_hotel_message.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_message.disp_message <> ''";
        query = query + " ORDER BY hh_hotel_message.start_date DESC, hh_hotel_message.start_time DESC, hh_hotel_message.last_update DESC, hh_hotel_message.last_uptime DESC,";
        query = query + " hh_hotel_message.seq DESC, hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            ret = this.getHotelMessageDataSub( prestate );
            if ( ret != false )
            {
                countData = this.deleteDuplicateData( this.m_hotelMsgInfo );
                this.m_hotelAllCount = countData;
                // 日付ごとにhh_hotel_adjustment_hisotryで並べ替え
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMessage.getHotelList()] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            prestate = null;
            result = null;
        }

        count = 0;
        try
        {
            if ( countData > 0 )
            {
                // 表示件数指定されていたら必要な部分だけセット
                if ( countNum > 0 )
                {
                    m_hotelInfo = new DataHotelBasic[countNum];
                    m_hotelMessage = new String[countNum];
                    m_hotelMessageDate = new int[countNum];
                    for( i = 0 ; i < countNum ; i++ )
                    {
                        // 配列の要素数を超えるか、配列がnullだったらbreak
                        if ( ((pageNum * countNum) + i == this.m_hotelAllCount) || (m_hotelMsgInfo[(pageNum * countNum) + i] == null) )
                        {
                            break;
                        }
                        m_hotelInfo[i] = new DataHotelBasic();
                        m_hotelInfo[i].getData( m_hotelMsgInfo[(pageNum * countNum) + i].getId() );
                        m_hotelMessage[i] = m_hotelMsgInfo[(pageNum * countNum) + i].getDispMessage();
                        m_hotelMessageDate[i] = m_hotelMsgInfo[(pageNum * countNum) + i].getStartDate();
                        count++;
                    }
                    if ( count < countNum )
                    {
                        m_hotelCount = count;
                    }
                    else
                    {
                        m_hotelCount = countNum;
                    }
                    ret = true;
                }
                // 表示件数指定されていなかったら全件をセット
                else
                {
                    m_hotelInfo = new DataHotelBasic[this.m_hotelAllCount];
                    m_hotelMessage = new String[this.m_hotelAllCount];
                    m_hotelMessageDate = new int[this.m_hotelAllCount];
                    for( i = 0 ; i < this.m_hotelAllCount ; i++ )
                    {
                        m_hotelInfo[i] = new DataHotelBasic();
                        m_hotelInfo[i].getData( m_hotelMsgInfo[i].getId() );
                        m_hotelMessage[i] = m_hotelMsgInfo[i].getDispMessage();
                        m_hotelMessageDate[i] = m_hotelMsgInfo[i].getStartDate();
                        count++;
                    }
                    m_hotelCount = count;
                    ret = true;
                }
            }
            else
            {
                ret = false;
                m_hotelCount = 0;
                m_hotelAllCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ホテル最新情報検索結果取得(ホテルランク順)
     * 
     * @param hotelIdList ホテルIDリスト(null:全件検索)
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelListByPref(int[] hotelIdList, int countNum, int pageNum, int prefId)
    {
        int i = 0;
        int count;
        int countData;
        boolean ret;
        String query;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        countData = 0;
        ret = false;

        if ( hotelIdList != null )
        {
            if ( hotelIdList.length == 0 )
            {
                return(false);
            }
        }

        query = "SELECT hh_hotel_message.* FROM hh_hotel_message,hh_hotel_basic";
        query = query + " WHERE ( hh_hotel_message.start_date < ? OR ( hh_hotel_message.start_date = ? AND hh_hotel_message.start_time <= ? ))";
        query = query + " AND ( hh_hotel_message.start_date >= ? )";
        query = query + " AND ( hh_hotel_message.end_date > ?  OR ( hh_hotel_message.end_date = ? AND hh_hotel_message.end_time >= ? ))";
        query = query + " AND hh_hotel_message.del_flag = 0";

        if ( hotelIdList != null )
        {
            if ( hotelIdList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < hotelIdList.length ; i++ )
                {
                    query = query + hotelIdList[i];
                    if ( i < hotelIdList.length - 1 )
                    {
                        query = query + ",";
                    }
                }
                query = query + ")";
            }
        }
        query = query + " AND hh_hotel_basic.id = hh_hotel_message.id";
        query = query + " AND hh_hotel_basic.kind <= 7";
        query = query + " AND hh_hotel_message.disp_message <> ''";
        if ( prefId != 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = " + prefId;
        }
        query = query + " ORDER BY hh_hotel_message.start_date DESC, hh_hotel_message.start_time DESC, hh_hotel_message.last_update DESC, hh_hotel_message.last_uptime DESC,";
        query = query + " hh_hotel_message.seq DESC, hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            ret = this.getHotelMessageDataSub( prestate );
            if ( ret != false )
            {
                countData = this.deleteDuplicateData( this.m_hotelMsgInfo );
                this.m_hotelAllCount = countData;
                // 日付ごとにhh_hotel_adjustment_hisotryで並べ替え
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMessage.getHotelList()] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            prestate = null;
            result = null;
        }

        count = 0;
        try
        {
            if ( countData > 0 )
            {
                // 表示件数指定されていたら必要な部分だけセット
                if ( countNum > 0 )
                {
                    m_hotelInfo = new DataHotelBasic[countNum];
                    m_hotelMessage = new String[countNum];
                    m_hotelMessageDate = new int[countNum];
                    for( i = 0 ; i < countNum ; i++ )
                    {
                        // 配列の要素数を超えるか、配列がnullだったらbreak
                        if ( ((pageNum * countNum) + i == this.m_hotelAllCount) || (m_hotelMsgInfo[(pageNum * countNum) + i] == null) )
                        {
                            break;
                        }
                        m_hotelInfo[i] = new DataHotelBasic();
                        m_hotelInfo[i].getData( m_hotelMsgInfo[(pageNum * countNum) + i].getId() );
                        m_hotelMessage[i] = m_hotelMsgInfo[(pageNum * countNum) + i].getDispMessage();
                        m_hotelMessageDate[i] = m_hotelMsgInfo[(pageNum * countNum) + i].getStartDate();
                        count++;
                    }
                    if ( count < countNum )
                    {
                        m_hotelCount = count;
                    }
                    else
                    {
                        m_hotelCount = countNum;
                    }
                    ret = true;
                }
                // 表示件数指定されていなかったら全件をセット
                else
                {
                    m_hotelInfo = new DataHotelBasic[this.m_hotelAllCount];
                    m_hotelMessage = new String[this.m_hotelAllCount];
                    m_hotelMessageDate = new int[this.m_hotelAllCount];
                    for( i = 0 ; i < this.m_hotelAllCount ; i++ )
                    {
                        m_hotelInfo[i] = new DataHotelBasic();
                        m_hotelInfo[i].getData( m_hotelMsgInfo[i].getId() );
                        m_hotelMessage[i] = m_hotelMsgInfo[i].getDispMessage();
                        m_hotelMessageDate[i] = m_hotelMsgInfo[i].getStartDate();
                        count++;
                    }
                    m_hotelCount = count;
                    ret = true;
                }
            }
            else
            {
                ret = false;
                m_hotelCount = 0;
                m_hotelAllCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ホテルメッセージ情報をセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    private boolean getHotelMessageDataSub(PreparedStatement prestate)
    {
        int i;
        int count = 0;
        boolean ret = false;
        ResultSet result = null;
        int today_date = Integer.parseInt( DateEdit.getDate( 2 ) );
        int today_time = Integer.parseInt( DateEdit.getTime( 1 ) );
        int last_month = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), LAST_MONTH );

        this.m_hotelAllCount = 0;
        try
        {
            prestate.setInt( 1, today_date );
            prestate.setInt( 2, today_date );
            prestate.setInt( 3, today_time );
            prestate.setInt( 4, last_month );
            prestate.setInt( 5, today_date );
            prestate.setInt( 6, today_date );
            prestate.setInt( 7, today_time );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.m_hotelAllCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelMsgInfo = new DataHotelMessage[this.m_hotelAllCount];
                for( i = 0 ; i < m_hotelAllCount ; i++ )
                {
                    this.m_hotelMsgInfo[i] = new DataHotelMessage();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // スポンサー情報の設定
                    this.m_hotelMsgInfo[count].setData( result );

                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelMessage.getHotelMessageDataSub(prestate)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        if ( this.m_hotelAllCount > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * 重複ID削除処理
     * 
     * @param prestate プリペアドステートメント
     * @param ret
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    private int deleteDuplicateData(DataHotelMessage[] hotelMessage)
    {
        int l;
        int m;
        int count;
        boolean boolAdd;
        DataHotelMessage[] dhm;

        boolAdd = false;
        dhm = new DataHotelMessage[hotelMessage.length];
        count = 0;
        try
        {
            // 最新情報の降順で取得した配列のID重複をなくす
            for( l = 0 ; l < hotelMessage.length ; l++ )
            {
                boolAdd = true;
                for( m = 0 ; m < count ; m++ )
                {
                    if ( dhm[m].getId() == hotelMessage[l].getId() )
                    {
                        boolAdd = false;
                        break;
                    }
                }
                if ( boolAdd != false )
                {
                    dhm[m] = hotelMessage[l];
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            count = 0;
            Logging.error( "[SearchHotelMessage.deleteDuplicateData()] Exception=" + e.toString() );
        }

        // 1件でもうまく登録されていたら、メッセージデータをコピーする
        if ( count > 0 )
        {
            // すでにクラスが割り当てられていたらnullをセットする
            if ( this.m_hotelMsgInfo != null )
            {
                this.m_hotelMsgInfo = null;
            }
            // 要素数をきっちりする
            this.m_hotelMsgInfo = new DataHotelMessage[count];
            for( l = 0 ; l < count ; l++ )
            {
                this.m_hotelMsgInfo[l] = new DataHotelMessage();
                this.m_hotelMsgInfo[l] = dhm[l];
            }
            dhm = null;
            hotelMessage = null;
        }
        return(count);
    }
}
