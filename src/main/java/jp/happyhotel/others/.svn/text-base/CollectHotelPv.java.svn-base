/*
 * @(#)HotelCount.java 1.00 2008/01/24 Copyright (C) ALMEX Inc. 2018 ホテル件数取得用ファイルクラス
 */
package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 検索対象ホテル抽出用クラス
 * 
 * @author paku-k1
 */
public class CollectHotelPv implements Serializable
{
    /** 端末種別：DoCoMo **/
    public static final int USERAGENT_DOCOMO          = 1;
    /** 端末種別：au **/
    public static final int USERAGENT_AU              = 2;
    /** 端末種別：SoftBank **/
    public static final int USERAGENT_SOFTBANK        = 3;
    /** 端末種別：pc **/
    public static final int USERAGENT_PC              = 4;
    /** 端末種別：iPhone WEB **/
    public static final int USERAGENT_IPHONE_WEB      = 5;
    /** 端末種別：Android WEB **/
    public static final int USERAGENT_ANDROID_WEB     = 6;
    /** 端末種別：iPhone アプリ **/
    public static final int USERAGENT_IPHONE_APP      = 7;
    /** 端末種別：Android アプリ **/
    public static final int USERAGENT_ANDROID_APP     = 8;
    /** 端末種別：iPhone旧予約アプリ **/
    public static final int USERAGENT_IPHONE_RSV_APP  = 9;
    /** 端末種別：Android旧予約アプリ **/
    public static final int USERAGENT_ANDROID_RSV_APP = 10;

    private static int      totalPvPlace              = 0;
    private static int      pcPvPlace                 = 1;
    private static int      docomoPvPlace             = 2;
    private static int      auPvPlace                 = 3;
    private static int      softbankPvPlace           = 4;
    private static int      prevDayRatioPlace         = 5;
    private static int      totalUUPvPlace            = 6;
    private static int      pcUUPvPlace               = 7;
    private static int      docomoUUPvPlace           = 8;
    private static int      auUUPvPlace               = 9;
    private static int      softbankUUPvPlace         = 10;
    private static int      smartPvPlace              = 11;
    private static int      smartUUPvPlace            = 12;
    private static int      iphoneWebPVPlace          = 13;
    private static int      iphoneWebUUPVPlace        = 14;
    private static int      androidWebPVPlace         = 15;
    private static int      androidWebUUPVPlace       = 16;
    private static int      iphoneAppliPVPlace        = 17;
    private static int      iphoneAppliUUPVPlace      = 18;
    private static int      androidAppliPVPlace       = 19;
    private static int      androidAppliUUPVPlace     = 20;
    private static int      iphoneApplirsvUUPVPlace   = 21;
    private static int      androidApplirsvUUPVPlace  = 22;

    public static boolean updateHotelPv(Connection connection, int yesterday)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";
        String agent = "";
        int hotelId = 0;
        int count = 0;
        boolean ret = false;
        HashMap<Integer, ArrayList<Integer>> hotelPvTotalList = new HashMap<Integer, ArrayList<Integer>>();
        List<Integer> hotelIdList = new ArrayList<Integer>();
        ArrayList<Integer> hotelPvList = new ArrayList<Integer>( 23 );

        query = "SELECT id,count(seq) AS count ,disp_useragent FROM hh_user_history WHERE disp_date = ? ";
        query += " GROUP BY id,disp_ip,disp_useragent ";
        query += " ORDER BY id";

        try
        {
            Logging.info( "START CollectHotelPv" );
            List<Integer> recordedHotelId = getRecordedHotelId( connection );
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, yesterday );
            result = prestate.executeQuery();

            while( result.next() )
            {
                agent = result.getString( "disp_useragent" );
                // 前データと同じhotelIdの場合
                if ( hotelId == result.getInt( "id" ) )
                {
                    if ( !recordedHotelId.contains( hotelId ) )
                    {
                        continue;
                    }
                    hotelPvList = AccessAnalyzeExt( hotelPvList, agent, count );
                    hotelPvTotalList.put( hotelId, hotelPvList );
                    continue;
                }
                hotelId = result.getInt( "id" );
                hotelPvList = new ArrayList<Integer>( 23 );
                // 全部の要素を0にする
                for( int i = 0 ; i < 23 ; i++ )
                {
                    hotelPvList.add( 0 );
                }
                if ( recordedHotelId.contains( hotelId ) )
                {
                    count = result.getInt( "count" );
                    hotelPvList = AccessAnalyzeExt( hotelPvList, agent, count );
                }
                hotelIdList.add( hotelId );
                hotelPvTotalList.put( hotelId, hotelPvList );
            }
            System.out.println( "hotelPvTotalList length:" + hotelPvTotalList.size() );
            ret = insertDataHotelPv( hotelPvTotalList, hotelIdList, yesterday, connection );
            System.out.println( "ret:" + ret );
        }
        catch ( Exception e )
        {
            Logging.error( "[CollectHotelPv.updateHotelPv] Exception=" + e.toString() );
            e.printStackTrace();
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        Logging.info( "END CollectHotelPv" );
        return ret;
    }

    /**
     * アクセス解析処理（拡張）
     * 
     * @param convdate 対象日付
     * @param acccal アクセス日時
     * @param url URL
     * @param status HTTPステータス
     * @param refrere リファラー
     * @param agent ユーザエージェント
     */
    private static ArrayList<Integer> AccessAnalyzeExt(ArrayList<Integer> hotelPvList, String agent, int count)
    {
        int userAgentType = getUserAgent( agent );

        switch( userAgentType )
        {
            case USERAGENT_DOCOMO:
                hotelPvList.set( docomoPvPlace, hotelPvList.get( docomoPvPlace ) + count );
                hotelPvList.set( docomoUUPvPlace, hotelPvList.get( docomoUUPvPlace ) + 1 );
                break;
            case USERAGENT_AU:
                hotelPvList.set( auPvPlace, hotelPvList.get( auPvPlace ) + count );
                hotelPvList.set( auUUPvPlace, hotelPvList.get( auUUPvPlace ) + 1 );
                break;
            case USERAGENT_SOFTBANK:
                hotelPvList.set( softbankPvPlace, hotelPvList.get( softbankPvPlace ) + count );
                hotelPvList.set( softbankUUPvPlace, hotelPvList.get( softbankUUPvPlace ) + 1 );
                break;
            case USERAGENT_PC:
                hotelPvList.set( pcPvPlace, hotelPvList.get( pcPvPlace ) + count );
                hotelPvList.set( pcUUPvPlace, hotelPvList.get( pcUUPvPlace ) + 1 );
                break;
            case USERAGENT_IPHONE_WEB:
                hotelPvList.set( iphoneWebPVPlace, hotelPvList.get( iphoneWebPVPlace ) + count );
                hotelPvList.set( iphoneWebUUPVPlace, hotelPvList.get( iphoneWebUUPVPlace ) + 1 );
                break;
            case USERAGENT_ANDROID_WEB:
                hotelPvList.set( androidWebPVPlace, hotelPvList.get( androidWebPVPlace ) + count );
                hotelPvList.set( androidWebUUPVPlace, hotelPvList.get( androidWebUUPVPlace ) + 1 );
                break;
            case USERAGENT_IPHONE_APP:
                hotelPvList.set( iphoneAppliPVPlace, hotelPvList.get( iphoneAppliPVPlace ) + count );
                hotelPvList.set( iphoneAppliUUPVPlace, hotelPvList.get( iphoneAppliUUPVPlace ) + 1 );
                break;
            case USERAGENT_ANDROID_APP:
                hotelPvList.set( androidAppliPVPlace, hotelPvList.get( androidAppliPVPlace ) + count );
                hotelPvList.set( androidAppliUUPVPlace, hotelPvList.get( androidAppliUUPVPlace ) + 1 );
                break;
            case USERAGENT_IPHONE_RSV_APP:
                hotelPvList.set( iphoneApplirsvUUPVPlace, hotelPvList.get( iphoneApplirsvUUPVPlace ) + 1 );
                break;
            case USERAGENT_ANDROID_RSV_APP:
                hotelPvList.set( androidApplirsvUUPVPlace, hotelPvList.get( androidApplirsvUUPVPlace ) + 1 );
                break;
        }
        if ( userAgentType != USERAGENT_IPHONE_RSV_APP && userAgentType != USERAGENT_ANDROID_RSV_APP )
        {
            hotelPvList.set( totalPvPlace, hotelPvList.get( totalPvPlace ) + count );
        }
        hotelPvList.set( totalUUPvPlace, hotelPvList.get( totalUUPvPlace ) + 1 );

        return hotelPvList;
    }

    /**
     * ユーザエージェントタイプ取得処理
     * 
     * @param agent ユーザエージェント
     * @return ユーザエージェントタイプ
     */
    private static int getUserAgent(String agent)
    {
        if ( agent.indexOf( "DoCoMo" ) >= 0 && agent.indexOf( "i" ) >= 0 )
        {
            return USERAGENT_DOCOMO;
        }
        else if ( agent.indexOf( "J-PHONE" ) >= 0 || agent.indexOf( "Vodafone" ) >= 0 || agent.indexOf( "SoftBank" ) >= 0 && agent.indexOf( "y" ) >= 0 )
        {
            return USERAGENT_SOFTBANK;
        }
        else if ( agent.indexOf( "UP.Browser" ) >= 0 || agent.indexOf( "KDDI-" ) >= 0 && agent.indexOf( "au" ) >= 0 )
        {
            return USERAGENT_AU;
        }
        else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
        {
            return USERAGENT_IPHONE_RSV_APP;
        }
        else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotelRsv" ) >= 0 )
        {
            return USERAGENT_ANDROID_RSV_APP;
        }
        else if ( agent.indexOf( "iPhone" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
        {
            return USERAGENT_IPHONE_APP;
        }
        else if ( agent.indexOf( "Android" ) >= 0 && agent.indexOf( "HappyHotel" ) >= 0 )
        {
            return USERAGENT_ANDROID_APP;
        }
        else if ( agent.indexOf( "Apache-HttpClient/UNAVAILABLE" ) >= 0 || agent.indexOf( "ada" ) >= 0 )
        {
            return USERAGENT_ANDROID_APP;
        }
        else if ( agent.indexOf( "HappyHotel" ) >= 0 || agent.indexOf( "ipa" ) >= 0 )
        {
            return USERAGENT_IPHONE_APP;
        }
        else if ( agent.indexOf( "iPhone" ) >= 0 )
        {
            return USERAGENT_IPHONE_WEB;
        }
        else if ( agent.indexOf( "Android" ) >= 0 )
        {
            return USERAGENT_ANDROID_WEB;
        }
        else
        {
            return USERAGENT_PC;
        }
    }

    private static ArrayList<Integer> getRecordedHotelId(Connection connection)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";

        ArrayList<Integer> recordedHotelIdList = new ArrayList<Integer>();

        query = "SELECT id FROM hotenavi.hh_hotel_basic ";
        query += " WHERE rank >= 1 ";
        query += " AND pref_id <> 0";
        query += " AND kind <=7";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                recordedHotelIdList.add( result.getInt( "id" ) );
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelPv.getRecordedHotelId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return recordedHotelIdList;
    }

    private static boolean insertDataHotelPv(HashMap<Integer, ArrayList<Integer>> hotelPvTotalList, List<Integer> hotelIdList, int yesterday, Connection connection)
    {
        PreparedStatement prestate = null;
        boolean ret = false;
        StringBuilder sb = new StringBuilder();
        sb.append( "INSERT INTO hotenavi.hh_hotel_pv SET" );
        sb.append( " id =?," );
        sb.append( " collect_date =?," );
        sb.append( " total_pv=?," );
        sb.append( " pc_pv=?," );
        sb.append( " docomo_pv=?," );
        sb.append( " au_pv=?," );
        sb.append( " softbank_pv=?," );
        sb.append( " prev_day_ratio=?," );
        sb.append( " total_uu_pv=?," );
        sb.append( " pc_uu_pv=?," );
        sb.append( " docomo_uu_pv=?," );
        sb.append( " au_uu_pv=?," );
        sb.append( " softbank_uu_pv=?," );
        sb.append( " smart_pv=?," );
        sb.append( " smart_uu_pv=?," );
        sb.append( " iphone_web_pv=?," );
        sb.append( " iphone_web_uu_pv=?," );
        sb.append( " android_web_pv=?," );
        sb.append( " android_web_uu_pv=?," );
        sb.append( " iphone_appli_pv=?," );
        sb.append( " iphone_appli_uu_pv=?," );
        sb.append( " android_appli_pv=?," );
        sb.append( " android_appli_uu_pv=?," );
        sb.append( " iphone_appli_reserve_uu_pv=?," );
        sb.append( " android_appli_reserve_uu_pv=?" );

        try
        {
            connection.setAutoCommit( false );
            prestate = connection.prepareStatement( sb.toString() );
            for( int id : hotelIdList )
            {
                ArrayList<Integer> hotelPvList = hotelPvTotalList.get( id );
                for( int i = 1 ; i < hotelPvList.size() + 3 ; i++ )
                {
                    if ( i == 1 )
                    {
                        prestate.setInt( i, id );
                    }
                    else if ( i == 2 )
                    {
                        prestate.setInt( i, yesterday );
                    }
                    else
                    {
                        prestate.setInt( i, hotelPvList.get( i - 3 ) );
                    }
                }
                prestate.addBatch();
            }
            prestate.executeBatch();
            connection.commit();
            connection.setAutoCommit( true );
            ret = true;
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelPv.insertDataHotelPv] Exception=" + e.toString() );
            e.printStackTrace();
            try
            {
                connection.rollback();
            }
            catch ( SQLException e1 )
            {
                e1.printStackTrace();
            }
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }
}
