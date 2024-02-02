package jp.happyhotel.lvj;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHogeHotelBasic;
import jp.happyhotel.data.DataHogeHotelEquip;
import jp.happyhotel.data.DataHogeHotelMap;
import jp.happyhotel.data.DataHogeHotelPrice;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataLvjHotelBasic;

/**
 * データ更新クラス
 *
 * @author mitsuhashi-t1
 * @version 1.00 2016/3/1
 */
public class LogicLvjHotelBasic implements Serializable
{
    /**
     * 更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param hotelId ホテルID (hh_hotel_basic.id)
     * @param seq 管理番号
     * @return
     */
    public boolean updateData(int hotelId)
    {
        boolean ret = false;

        try
        {

            if ( hotelId == 0 )
            {
                throw new Exception( "ホテルIDが不正(0)です。 [hotelId=" + hotelId + "]" );
            }

            // 最新のラブインジャパン連携情報を取得
            DataLvjHotelBasic dhb = fetchDataLvjHotel( hotelId );
            int hotel_id = dhb.getHotelId();
            int seq = 0;

            // hh_hotel_basicを読み込む
            DataHogeHotelBasic dataHogeHotelBasic = fetchDataHotelBasic( hotelId );
            // hh_hotel_priceを読み込む
            DataHogeHotelPrice dataHogeHotelPrice = fetchDataHotelPrice( hotelId );
            // hh_hotel_mapを読み込む
            DataHogeHotelMap dataHogeHotelMap = fetchDataHotelMap( hotelId );
            // hh_hotel_equipを読み込む
            DataHogeHotelEquip dataHogeHotelEquip = fetchDataHotelEquip( hotelId );
            // hh_hotel_remarksを読み込む(途中外出詳細 (hh_hotel_remarks disp_no=9))
            String disp_message = fetchDataHotelRemarks( hotelId );

            if ( hotel_id == 0 )
            {
                // データが存在しない時、insertのみ行う
                if ( insertData( hotelId, dataHogeHotelBasic, dataHogeHotelPrice, dataHogeHotelMap, dataHogeHotelEquip, disp_message ) == false )
                {
                    throw new Exception( "データ挿入に失敗しました hoteiId=" + hotelId );
                }

                ret = true;
            }
            else
            {
                // データが存在する時、変更箇所チェック＆更新＆挿入
                seq = dhb.getSeq();
                DataLvjHotelBasic dhb_tmp = new DataLvjHotelBasic();

                if ( dhb_tmp.getData( hotel_id, seq ) == false )
                {
                    Logging.error( "[ActionApiLvjHotel]ラブインジャパン連携情報の取得に失敗しました。 [hotelId=" + hotel_id + ":seq=]" + seq );
                    throw new Exception();
                }
                else
                {
                    // 1箇所でも違っていたらhogeの内容を新レコードに書き込み
                    if ( chkData( dhb_tmp, dataHogeHotelBasic, dataHogeHotelPrice, dataHogeHotelMap, dataHogeHotelEquip, disp_message ) == false )
                    {
                        // reflect_flagが0の場合は1に更新（最終更新日は更新しない）
                        if ( dhb_tmp.getReflectFlag() == 0 )
                        {
                            dhb_tmp.setReflectFlag( 1 );
                            if ( dhb_tmp.updateData( hotelId, seq ) == false )
                            {
                                throw new Exception( "反映フラグの更新に失敗しました hoteiId=" + hotelId );
                            }
                        }

                        // データ挿入
                        if ( insertData( hotel_id, dataHogeHotelBasic, dataHogeHotelPrice, dataHogeHotelMap, dataHogeHotelEquip, disp_message ) == false )
                        {
                            throw new Exception( "データ挿入に失敗しました hoteiId=" + hotel_id );
                        }
                    }
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataLvjHotelBasic.updateData] Exception=" + e.toString() );
            ret = false;
        }

        return(ret);
    }

    /**
     * ラブインジャパン連携情報取得
     *
     * @param hotelId ホテルID)
     * @return 処理結果(成功:結果オブジェクト、失敗:空オブジェクト)
     * @throws Exception
     **/
    public static DataLvjHotelBasic fetchDataLvjHotel(int hotelId) throws Exception
    {

        DataLvjHotelBasic dhb = new DataLvjHotelBasic();

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            connection = DBConnection.getConnection();

            // 最新ラブインジャパン連携情報取得
            query = "SELECT * FROM lvj_hotel_basic " +
                    "  WHERE hotel_id = ?  ORDER BY seq DESC";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    dhb.setHotelId( result.getInt( "hotel_id" ) );
                    dhb.setSeq( result.getInt( "seq" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicLvjHotelBasisc.fetchDataLvjHotel()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dhb;
    }

    /**
     * ホテルマスタの情報取得
     *
     * @param hotelId ホテルID)
     * @return 処理結果(成功:結果オブジェクト、失敗:空オブジェクト)
     * @throws Exception
     **/
    public static DataHogeHotelBasic fetchDataHotelBasic(int hotelId) throws Exception
    {
        DataHotelBasic dhb = new DataHotelBasic();
        DataHogeHotelBasic dataHogeHotelBasic = new DataHogeHotelBasic();
        try
        {
            if ( dhb.getData( hotelId ) )
            {
                dataHogeHotelBasic.setId( dhb.getId() );
                dataHogeHotelBasic.setName( dhb.getName() );
                dataHogeHotelBasic.setNameKana( dhb.getNameKana() );
                dataHogeHotelBasic.setRank( dhb.getRank() );
                dataHogeHotelBasic.setZipCode( dhb.getZipCode() );
                dataHogeHotelBasic.setJisCode( dhb.getJisCode() );
                dataHogeHotelBasic.setPrefId( dhb.getPrefId() );
                dataHogeHotelBasic.setPrefName( dhb.getPrefName() );
                dataHogeHotelBasic.setAddress1( dhb.getAddress1() );
                dataHogeHotelBasic.setAddress2( dhb.getAddress2() );
                dataHogeHotelBasic.setAddress3( dhb.getAddress3() );
                dataHogeHotelBasic.setTel( dhb.getTel1() );
                dataHogeHotelBasic.setFax( dhb.getFax() );
                dataHogeHotelBasic.setRoomCount( dhb.getRoomCount() );
                dataHogeHotelBasic.setHalfway( dhb.getHalfway() );
                dataHogeHotelBasic.setPossibleOne( dhb.getPossibleOne() );
                dataHogeHotelBasic.setPossibleThree( dhb.getPossibleThree() );
                dataHogeHotelBasic.setHotelLat( dhb.getHotelLat() );
                dataHogeHotelBasic.setHotelLon( dhb.getHotelLon() );
                dataHogeHotelBasic.setOver18Flag( dhb.getOver18Flag() );

                dataHogeHotelBasic.setIsRoomService( dhb.getRoomService() );
                dataHogeHotelBasic.setIsCasher( dhb.getPayAuto() );
                return dataHogeHotelBasic;

            }
            else
            {
                throw new Exception( "ホテル基本情報の取得に失敗しました。 [hotelId=" + hotelId + "]" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicLvjHotelBasisc.fetchDataHotelBasic()] Exception=" + e.toString() );
            throw e;
        }
    }

    /**
     * ホテル料金マスタの情報取得
     *
     * @param hotelId ホテルID)
     * @return 処理結果(成功:結果オブジェクト、失敗:空オブジェクト)
     * @throws Exception
     **/
    public static DataHogeHotelPrice fetchDataHotelPrice(int hotelId) throws Exception
    {

        DataHogeHotelPrice dataHogeHotelPrice = new DataHogeHotelPrice();

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();

            // 休憩料金取得
            query = "SELECT MIN(price_from) AS price_from, MIN(price_to) AS price_to FROM hh_hotel_price " +
                    "  WHERE id = ? AND start_date <= ? AND end_date >= ? AND data_flag = 1 AND (data_type in( 1, 2))";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 1：休憩（平日）、2：休憩（休日）
                    dataHogeHotelPrice.setRestPriceFrom( result.getInt( "price_from" ) );
                    dataHogeHotelPrice.setRestPriceTo( result.getInt( "price_to" ) );
                }
            }
            // 休憩料金取得
            query = "SELECT MIN(price_from) AS price_from, MIN(price_to) AS price_to FROM hh_hotel_price " +
                    "  WHERE id = ? AND start_date <= ? AND end_date >= ? AND data_flag = 1 AND (data_type in( 5, 6))";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, nowDate );
            prestate.setInt( 3, nowDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 5：宿泊（平日）、6：宿泊（休日）
                    dataHogeHotelPrice.setStayPriceFrom( result.getInt( "price_from" ) );
                    dataHogeHotelPrice.setStayPriceTo( result.getInt( "price_to" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicLvjHotelBasisc.fetchDataHotelPrice()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dataHogeHotelPrice;
    }

    /**
     * ホテル地図マスタの情報取得
     *
     * @param hotelId ホテルID)
     * @return 処理結果(成功:結果オブジェクト、失敗:空オブジェクト)
     * @throws Exception
     **/
    public static DataHogeHotelMap fetchDataHotelMap(int hotelId) throws Exception
    {

        DataHogeHotelMap dataHogeHotelMap = new DataHogeHotelMap();

        String query;
        String tmp_mapId = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            query = "SELECT MP.option_4  AS map_id " +
                    " FROM hh_map_point MP" +
                    " INNER JOIN hh_hotel_map HM ON HM.map_id = MP.option_4 " +
                    " WHERE HM.id = ? AND MP.class_code IN( '521@', '522@', '523@') AND HM.disp_flag = 1" +
                    " GROUP BY MP.option_4";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    tmp_mapId = tmp_mapId + result.getString( "map_id" ) + ",";
                }

                // 最後に余分になる", "の削除
                if ( !(tmp_mapId == null || tmp_mapId.length() == 0) )
                {
                    tmp_mapId = tmp_mapId.substring( 0, tmp_mapId.length() - 1 );
                }
            }

            dataHogeHotelMap.setMapId( tmp_mapId );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicLvjHotelBasisc.fetchDataHotelMap()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dataHogeHotelMap;
    }

    /**
     * ホテル設備の情報取得
     *
     * @param hotelId ホテルID)
     * @return 処理結果(成功:結果オブジェクト、失敗:空オブジェクト)
     * @throws Exception
     **/
    public static DataHogeHotelEquip fetchDataHotelEquip(int hotelId) throws Exception
    {

        DataHogeHotelEquip dataHogeHotelEquip = new DataHogeHotelEquip();

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "SELECT *  FROM hh_hotel_equip WHERE id = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    int equipId = result.getInt( "equip_id" );
                    int equipType = result.getInt( "equip_type" );
                    switch( equipId )
                    {
                        case 73:// 同性利用可(女性)
                            dataHogeHotelEquip.setIsWomensUse( equipType );
                            break;
                        case 72:// 同性利用可(男性)
                            dataHogeHotelEquip.setIsMensUse( equipType );
                            break;
                        case 54:// 無料Wi-Fi(無線LAN)
                            dataHogeHotelEquip.setIsFreeWifi( equipType );
                            break;
                        case 53:// 有線LAN
                            dataHogeHotelEquip.setIsFreeLan( equipType );
                            break;
                        case 2:// 禁煙ルーム
                            dataHogeHotelEquip.setIsNoSmokingRoom( equipType );
                            break;
                        case 62:// パーティールーム
                            dataHogeHotelEquip.setIsPartyRoom( equipType );
                            break;
                        case 3:// ジェットバス/ブロアバス
                            dataHogeHotelEquip.setIsJetbath( equipType );
                            break;
                        case 11:// 露天風呂
                            dataHogeHotelEquip.setIsOpenAirBath( equipType );
                            break;
                        case 13:// 岩盤浴
                            dataHogeHotelEquip.setIsBedrockBath( equipType );
                            break;
                        case 5:// サウナ
                            dataHogeHotelEquip.setIsSauna( equipType );
                            break;
                        case 47:// 温泉
                            dataHogeHotelEquip.setIsHotSpring( equipType );
                            break;
                        case 48:// プール
                            dataHogeHotelEquip.setIsPool( equipType );
                            break;
                        case 9:
                            dataHogeHotelEquip.setIsBathroomTv( equipType );
                            break;
                        case 59:
                            dataHogeHotelEquip.setIsHugeScreenTv( equipType );
                            break;
                        case 23:
                            dataHogeHotelEquip.setIsSurroundSystem( equipType );
                            break;
                        case 25:
                            dataHogeHotelEquip.setIsVod( equipType );
                            break;
                        case 35:
                            dataHogeHotelEquip.setIsOnlineKaraoke( equipType );
                            break;
                        case 37:
                            dataHogeHotelEquip.setIsVideoGame( equipType );
                            break;
                        case 51:
                            dataHogeHotelEquip.setIsPc( equipType );
                            break;
                        case 52:
                            dataHogeHotelEquip.setIsTabletPc( equipType );
                            break;
                        case 41:
                            dataHogeHotelEquip.setIsInternet( equipType );
                            break;
                        case 45:
                            dataHogeHotelEquip.setIsJapaneseRoom( equipType );
                            break;
                        case 16:
                            dataHogeHotelEquip.setIsLaundry( equipType );
                            break;
                        case 18:
                            dataHogeHotelEquip.setIsDryingMachine( equipType );
                            break;
                        case 34:
                            dataHogeHotelEquip.setIsShowerToilet( equipType );
                            break;
                        case 46:
                            dataHogeHotelEquip.setIsProjecter( equipType );
                            break;
                        case 38:
                            dataHogeHotelEquip.setIsBathrobe( equipType );
                            break;
                        case 64:
                            dataHogeHotelEquip.setIsRoomWear( equipType );
                            break;
                        case 68:
                            dataHogeHotelEquip.setIsWomenCosmetics( equipType );
                            break;
                        case 42:
                            dataHogeHotelEquip.setIsVariousShampoo( equipType );
                            break;
                        case 44:
                            dataHogeHotelEquip.setIsCostume( equipType );
                            break;
                        case 55:
                            dataHogeHotelEquip.setIsMobilePhoneCharger( equipType );
                            break;
                        case 56:
                            dataHogeHotelEquip.setIsAndroidPhoneCharger( equipType );
                            break;
                        case 57:
                            dataHogeHotelEquip.setIsIphonePhoneCharger( equipType );
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicLvjHotelBasisc.fetchDataHotelEquip()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return dataHogeHotelEquip;
    }

    /**
     * ホテル備考データの情報取得
     *
     * @param hotelId ホテルID
     * @return 処理結果(成功:結果オブジェクト、失敗:空オブジェクト)
     * @throws Exception
     **/
    public static String fetchDataHotelRemarks(int hotelId) throws Exception
    {

        int disp_no = 9;

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        String disp_message = "";

        try
        {
            connection = DBConnection.getConnection();

            // id=hotel_id
            // disp_no=9
            // seqが一番大きいもの
            // disp_flag = 1
            // 当日日付がstart_dateとstart_endの範囲内

            query = "SELECT disp_message FROM hh_hotel_remarks " +
                    "  WHERE id = ? AND disp_no = ? AND start_date <= ? AND end_date >= ? AND disp_flag = 1 ORDER BY seq DESC ";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, disp_no );
            prestate.setInt( 3, nowDate );
            prestate.setInt( 4, nowDate );
            result = prestate.executeQuery();


            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // メッセージ
                    disp_message = result.getString( "disp_message" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicLvjHotelBasisc.fetchDataHotelRemarks()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return disp_message;
    }

    /**
     * チェック
     *
     * @see "照合チェック"
     * @param
     * @param
     * @return
     */
    public boolean chkData(DataLvjHotelBasic dlhb, DataHogeHotelBasic dataHogeHotelBasic, DataHogeHotelPrice dataHogeHotelPrice,
            DataHogeHotelMap dataHogeHotelMap, DataHogeHotelEquip dataHogeHotelEquip, String halfwayMessage)
    {

        boolean ret = false;

        // lvj_hotel_basicデータ更新
        if ( dlhb.getName().equals( dataHogeHotelBasic.getName() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 1" );
            return(ret);
        }
        if ( dlhb.getNameKana().equals( dataHogeHotelBasic.getNameKana() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 2" );
            return(ret);
        }
        if ( dlhb.getRank() != dataHogeHotelBasic.getRank() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 3" );
            return(ret);
        }
        if ( dlhb.getMapId().equals( dataHogeHotelMap.getMapId() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 4:" + dlhb.getMapId() + ":" + dataHogeHotelMap.getMapId() );
            return(ret);
        }
        if ( dlhb.getZipCode().equals( dataHogeHotelBasic.getZipCode() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 5" );
            return(ret);
        }
        if ( dlhb.getJisCode() != dataHogeHotelBasic.getJisCode() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 6" );
            return(ret);
        }
        if ( dlhb.getPrefId() != dataHogeHotelBasic.getPrefId() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 7" );
            return(ret);
        }
        if ( dlhb.getPrefName().equals( dataHogeHotelBasic.getPrefName() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 8" );
            return(ret);
        }
        if ( dlhb.getAddress1().equals( dataHogeHotelBasic.getAddress1() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 9" );
            return(ret);
        }
        if ( dlhb.getAddress2().equals( dataHogeHotelBasic.getAddress2() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 10" );
            return(ret);
        }
        if ( dlhb.getAddress3().equals( dataHogeHotelBasic.getAddress3() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 11" );
            return(ret);
        }
        if ( dlhb.getTel().equals( dataHogeHotelBasic.getTel() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 12" );
            return(ret);
        }
        if ( dlhb.getFax().equals( dataHogeHotelBasic.getFax() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 13" );
            return(ret);
        }
        if ( dlhb.getRoomCount() != dataHogeHotelBasic.getRoomCount() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 14" );
            return(ret);
        }
        if ( dlhb.getHalfway() != dataHogeHotelBasic.getHalfway() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 15" );
            return(ret);
        }
        if ( dlhb.getHalfwayMessage().equals( halfwayMessage ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 16" );
            return(ret);
        }
        if ( dlhb.getPossibleOne() != dataHogeHotelBasic.getPossibleOne() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 17" );
            return(ret);
        }
        if ( dlhb.getPossibleThree() != dataHogeHotelBasic.getPossibleThree() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 18" );
            return(ret);
        }
        if ( dlhb.getHotelLat().equals( dataHogeHotelBasic.getHotelLat() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 19" );
            return(ret);
        }
        if ( dlhb.getHotelLon().equals( dataHogeHotelBasic.getHotelLon() ) == false )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 20" );
            return(ret);
        }
        if ( dlhb.getOver18Flag() != dataHogeHotelBasic.getOver18Flag() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 21" );
            return(ret);
        }
        if ( dlhb.getRestPriceFrom() != dataHogeHotelPrice.getRestPriceFrom() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 22" );
            return(ret);
        }
        if ( dlhb.getRestPriceTo() != dataHogeHotelPrice.getRestPriceTo() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 23" );
            return(ret);
        }
        if ( dlhb.getStayPriceFrom() != dataHogeHotelPrice.getStayPriceFrom() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 24" );
            return(ret);
        }
        if ( dlhb.getStayPriceTo() != dataHogeHotelPrice.getStayPriceTo() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 25" );
            return(ret);
        }
        if ( dlhb.getIsWomensUse() != dataHogeHotelEquip.getIsWomensUse() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 26" );
            return(ret);
        }
        if ( dlhb.getIsMensUse() != dataHogeHotelEquip.getIsMensUse() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 27" );
            return(ret);
        }
        if ( dlhb.getIsFreeWifi() != dataHogeHotelEquip.getIsFreeWifi() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 28" );
            return(ret);
        }
        if ( dlhb.getIsFreeLan() != dataHogeHotelEquip.getIsFreeLan() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 29" );
            return(ret);
        }
        if ( dlhb.getIsNoSmokingRoom() != dataHogeHotelEquip.getIsNoSmokingRoom() )
        {
            Logging.info( "[LogicLvjHotelBasisc.chkData()] 30" );
            return(ret);
        }
        if ( dlhb.getIsPartyRoom() != dataHogeHotelEquip.getIsPartyRoom() )
        {
            return(ret);
        }
        if ( dlhb.getIsRoomService() != dataHogeHotelBasic.getIsRoomService() )
        {
            return(ret);
        }
        if ( dlhb.getIsCasher() != dataHogeHotelBasic.getIsCasher() )
        {
            return(ret);
        }
        if ( dlhb.getIsJetbath() != dataHogeHotelEquip.getIsJetbath() )
        {
            return(ret);
        }
        if ( dlhb.getIsOpenAirBath() != dataHogeHotelEquip.getIsOpenAirBath() )
        {
            return(ret);
        }
        if ( dlhb.getIsBedrockBath() != dataHogeHotelEquip.getIsBedrockBath() )
        {
            return(ret);
        }
        if ( dlhb.getIsSauna() != dataHogeHotelEquip.getIsSauna() )
        {
            return(ret);
        }
        if ( dlhb.getIsHotSpring() != dataHogeHotelEquip.getIsHotSpring() )
        {
            return(ret);
        }
        if ( dlhb.getIsPool() != dataHogeHotelEquip.getIsPool() )
        {
            return(ret);
        }
        if ( dlhb.getIsBathroomTv() != dataHogeHotelEquip.getIsBathroomTv() )
        {
            return(ret);
        }
        if ( dlhb.getIsHugeScreenTv() != dataHogeHotelEquip.getIsHugeScreenTv() )
        {
            return(ret);
        }
        if ( dlhb.getIsSurroundSystem() != dataHogeHotelEquip.getIsSurroundSystem() )
        {
            return(ret);
        }
        if ( dlhb.getIsVod() != dataHogeHotelEquip.getIsVod() )
        {
            return(ret);
        }
        if ( dlhb.getIsOnlineKaraoke() != dataHogeHotelEquip.getIsOnlineKaraoke() )
        {
            return(ret);
        }
        if ( dlhb.getIsVideoGame() != dataHogeHotelEquip.getIsVideoGame() )
        {
            return(ret);
        }
        if ( dlhb.getIsPc() != dataHogeHotelEquip.getIsPc() )
        {
            return(ret);
        }
        if ( dlhb.getIsTabletPc() != dataHogeHotelEquip.getIsTabletPc() )
        {
            return(ret);
        }
        if ( dlhb.getIsInternet() != dataHogeHotelEquip.getIsInternet() )
        {
            return(ret);
        }
        if ( dlhb.getIsJapaneseRoom() != dataHogeHotelEquip.getIsJapaneseRoom() )
        {
            return(ret);
        }
        if ( dlhb.getIsLaundry() != dataHogeHotelEquip.getIsLaundry() )
        {
            return(ret);
        }
        if ( dlhb.getIsDryningMachine() != dataHogeHotelEquip.getIsDryingMachine() )
        {
            return(ret);
        }
        if ( dlhb.getIsShowerToilet() != dataHogeHotelEquip.getIsShowerToilet() )
        {
            return(ret);
        }
        if ( dlhb.getIsProjecter() != dataHogeHotelEquip.getIsProjecter() )
        {
            return(ret);
        }
        if ( dlhb.getIsBathrobe() != dataHogeHotelEquip.getIsBathrobe() )
        {
            return(ret);
        }
        if ( dlhb.getIsRoomWear() != dataHogeHotelEquip.getIsRoomWear() )
        {
            return(ret);
        }
        if ( dlhb.getIsWomenCosmetics() != dataHogeHotelEquip.getIsWomenCosmetics() )
        {
            return(ret);
        }
        if ( dlhb.getIsVariousShampoo() != dataHogeHotelEquip.getIsVariousShampoo() )
        {
            return(ret);
        }
        if ( dlhb.getIsCostume() != dataHogeHotelEquip.getIsCostume() )
        {
            return(ret);
        }
        if ( dlhb.getIsMobilePhoneCharger() != dataHogeHotelEquip.getIsMobilePhoneCharger() )
        {
            return(ret);
        }
        if ( dlhb.getIsAndroidPhoneCharger() != dataHogeHotelEquip.getIsAndroidPhoneCharger() )
        {
            return(ret);
        }
        if ( dlhb.getIsIphonePhoneCharger() != dataHogeHotelEquip.getIsIphonePhoneCharger() )
        {
            return(ret);
        }
        Logging.info( "[LogicLvjHotelBasic.chkData()] true" );
        ret = true;
        return(ret);
    }

    /**
     * 新規作成
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param hotelId ホテルID (hh_hotel_basic.id)
     * @param seq 管理番号
     * @return
     */
    public boolean insertData(int hotelId, DataHogeHotelBasic dataHogeHotelBasic, DataHogeHotelPrice dataHogeHotelPrice,
            DataHogeHotelMap dataHogeHotelMap, DataHogeHotelEquip dataHogeHotelEquip, String halfwayMessage)
    {

        boolean ret;
        ret = false;

        DataLvjHotelBasic dlhb = new DataLvjHotelBasic();

        // lvj_hotel_basicデータ更新
        dlhb.setHotelId( hotelId );
        dlhb.setName( dataHogeHotelBasic.getName() );
        dlhb.setNameKana( dataHogeHotelBasic.getNameKana() );
        dlhb.setRank( dataHogeHotelBasic.getRank() );
        dlhb.setMapId( dataHogeHotelMap.getMapId() );
        dlhb.setZipCode( dataHogeHotelBasic.getZipCode() );
        dlhb.setJisCode( dataHogeHotelBasic.getJisCode() );
        dlhb.setPrefId( dataHogeHotelBasic.getPrefId() );
        dlhb.setPrefName( dataHogeHotelBasic.getPrefName() );
        dlhb.setAddress1( dataHogeHotelBasic.getAddress1() );
        dlhb.setAddress2( dataHogeHotelBasic.getAddress2() );
        dlhb.setAddress3( dataHogeHotelBasic.getAddress3() );
        dlhb.setTel( dataHogeHotelBasic.getTel() );
        dlhb.setFax( dataHogeHotelBasic.getFax() );
        dlhb.setRoomCount( dataHogeHotelBasic.getRoomCount() );
        dlhb.setHalfway( dataHogeHotelBasic.getHalfway() );
        dlhb.setHalfwayMessage( halfwayMessage );
        dlhb.setPossibleThree( dataHogeHotelBasic.getPossibleThree() );
        dlhb.setPossibleOne( dataHogeHotelBasic.getPossibleOne() );
        dlhb.setPossibleThree( dataHogeHotelBasic.getPossibleThree() );
        dlhb.setHotelLat( dataHogeHotelBasic.getHotelLat() );
        dlhb.setHotelLon( dataHogeHotelBasic.getHotelLon() );
        dlhb.setOver18Flag( dataHogeHotelBasic.getOver18Flag() );
        dlhb.setRestPriceFrom( dataHogeHotelPrice.getRestPriceFrom() );
        dlhb.setRestPriceTo( dataHogeHotelPrice.getRestPriceTo() );
        dlhb.setStayPriceFrom( dataHogeHotelPrice.getStayPriceFrom() );
        dlhb.setStayPriceTo( dataHogeHotelPrice.getStayPriceTo() );
        dlhb.setIsWomensUse( dataHogeHotelEquip.getIsWomensUse() );
        dlhb.setIsMensUse( dataHogeHotelEquip.getIsMensUse() );
        dlhb.setIsFreeWifi( dataHogeHotelEquip.getIsFreeWifi() );
        dlhb.setIsFreeLan( dataHogeHotelEquip.getIsFreeLan() );
        dlhb.setIsNoSmokingRoom( dataHogeHotelEquip.getIsNoSmokingRoom() );
        dlhb.setIsPartyRoom( dataHogeHotelEquip.getIsPartyRoom() );
        dlhb.setIsRoomService( dataHogeHotelBasic.getIsRoomService() );
        dlhb.setIsCasher( dataHogeHotelBasic.getIsCasher() );
        dlhb.setIsJetbath( dataHogeHotelEquip.getIsJetbath() );
        dlhb.setIsOpenAirBath( dataHogeHotelEquip.getIsOpenAirBath() );
        dlhb.setIsBedrockBath( dataHogeHotelEquip.getIsBedrockBath() );
        dlhb.setIsSauna( dataHogeHotelEquip.getIsSauna() );
        dlhb.setIsHotSpring( dataHogeHotelEquip.getIsHotSpring() );
        dlhb.setIsPool( dataHogeHotelEquip.getIsPool() );
        dlhb.setIsBathroomTv( dataHogeHotelEquip.getIsBathroomTv() );
        dlhb.setIsHugeScreenTv( dataHogeHotelEquip.getIsHugeScreenTv() );
        dlhb.setIsSurroundSystem( dataHogeHotelEquip.getIsSurroundSystem() );
        dlhb.setIsVod( dataHogeHotelEquip.getIsVod() );
        dlhb.setIsOnlineKaraoke( dataHogeHotelEquip.getIsOnlineKaraoke() );
        dlhb.setIsVideoGame( dataHogeHotelEquip.getIsVideoGame() );
        dlhb.setIsPc( dataHogeHotelEquip.getIsPc() );
        dlhb.setIsTabletPc( dataHogeHotelEquip.getIsTabletPc() );
        dlhb.setIsInternet( dataHogeHotelEquip.getIsInternet() );
        dlhb.setIsJapaneseRoom( dataHogeHotelEquip.getIsJapaneseRoom() );
        dlhb.setIsLaundry( dataHogeHotelEquip.getIsLaundry() );
        dlhb.setIsDryningMachine( dataHogeHotelEquip.getIsDryingMachine() );
        dlhb.setIsShowerToilet( dataHogeHotelEquip.getIsShowerToilet() );
        dlhb.setIsProjecter( dataHogeHotelEquip.getIsProjecter() );
        dlhb.setIsBathrobe( dataHogeHotelEquip.getIsBathrobe() );
        dlhb.setIsRoomWear( dataHogeHotelEquip.getIsRoomWear() );
        dlhb.setIsWomenCosmetics( dataHogeHotelEquip.getIsWomenCosmetics() );
        dlhb.setIsVariousShampoo( dataHogeHotelEquip.getIsVariousShampoo() );
        dlhb.setIsCostume( dataHogeHotelEquip.getIsCostume() );
        dlhb.setIsMobilePhoneCharger( dataHogeHotelEquip.getIsMobilePhoneCharger() );
        dlhb.setIsAndroidPhoneCharger( dataHogeHotelEquip.getIsAndroidPhoneCharger() );
        dlhb.setIsIphonePhoneCharger( dataHogeHotelEquip.getIsIphonePhoneCharger() );

        dlhb.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dlhb.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dlhb.setReflectFlag( 0 );

        ret = dlhb.insertData();

        return(ret);

    }
}
