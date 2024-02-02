/*
 * @(#)SearchResultDao_M2.java 2008/08/11 Copyright (C) HCL Technologies Ltd. 2008
 */

package jp.happyhotel.search;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataSearchHotel_M2;

/**
 * 
 * �T�[�`�z�e��DAO�N���X
 * 
 * @author HCL Technologies Ltd.
 * @see ���̃N���X���A�b�v����ꍇ��DataSearchHotel_M2���ꏏ�ɃA�b�v����
 * 
 */
public class SearchHotelDao_M2
{
    private final int            NON_DISP      = 0;
    private int                  m_hotelCount;
    private int                  m_hotelAllCount;
    private DataSearchHotel_M2[] m_hotelInfo;

    private String               imgPath;
    private String               imgUrl;
    private String               noimgUrl;
    int                          reserveCount  = 0;
    int[]                        reservePlanId = null;
    String[]                     reservePlan   = null;
    int                          roomCount     = 0;
    int[]                        roomSeq       = null;

    /**
     * �f�[�^�����������܂��B
     */
    public SearchHotelDao_M2()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;

        FileInputStream propfile = null;
        Properties propConfig = new Properties();

        try
        {
            propfile = new FileInputStream( "/etc/happyhotel/hotelimage.conf" );
            propConfig = new Properties();

            propConfig = new Properties();
            propConfig.load( propfile );

            imgPath = propConfig.getProperty( "image.path" );
            imgUrl = propConfig.getProperty( "image.url" );
            noimgUrl = propConfig.getProperty( "noimage.url" );
        }
        catch ( Exception e )
        {
            imgPath = "";
            imgUrl = "";
            noimgUrl = "";
        }
        finally
        {
            if ( propfile != null )
            {
                try
                {
                    propfile.close();
                }
                catch ( Exception e )
                {
                }
            }
        }

    }

    /** �z�e����{��񌏐��擾 **/
    public int getCount()
    {
        return(m_hotelCount);
    }

    /** �z�e����{��񌏐��擾 **/
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    /** �z�e����{���擾 **/
    public DataSearchHotel_M2[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    /**
     * �z�e���f�[�^�擾
     * 
     * @param arrhotelId(array of HotelIds
     * @return array of DataSearchHotel Objects
     * @throws Exception
     */
    public DataSearchHotel_M2[] getDataFromDataBase2(int[] arrHotelId) throws Exception
    {
        boolean ret = false;
        String query;
        File fileImg;
        int hotelcount = arrHotelId.length;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        DataSearchHotel_M2 dataSearchHotel[] = new DataSearchHotel_M2[hotelcount];
        query = " SELECT HB.id, HB.rank, HB.name, HB.pref_name,HB.name_mobile, " +
                " HB.address1, HB.address_all, HB.tel1, HB.hotel_lat, HB.hotel_lon, HB.url, HB.url_official1, " +
                " HB.url_official2, HB.pr, HB.reserve, HB.reserve_tel, HB.reserve_mail, " +
                " HB.reserve_web, HS.empty_status, HB.over18_flag, HB.company_type, HS.last_update, HS.last_uptime ,HM.disp_message , COUNT(HC.id) as coupon_count , " +
                " HMR.bbs_config, COUNT(HBBS.id) as bbsAllCount, SUM(HBBS.point*100) as points, HMR.empty_disp_kind, HH.name AS happie_name" +
                " FROM hh_hotel_basic HB LEFT JOIN hh_hotel_status HS ON (HB.id = HS.id)" +
                " LEFT JOIN hh_hotel_message HM ON (HB.id = HM.id" +
                " AND HM.del_flag = 0 AND (HM.start_date <= " + DateEdit.getDate( 2 ) + " AND HM.end_date >= " + DateEdit.getDate( 2 ) + "))" +
                " LEFT JOIN hh_hotel_coupon HC ON (HB.id = HC.id " +
                " AND HC.del_flag<> 1 AND (HC.start_date <= " + DateEdit.getDate( 2 ) + " AND HC.end_date >= " + DateEdit.getDate( 2 ) + "))" +
                " LEFT JOIN hh_hotel_sort HP ON ( HB.id = HP.id AND HP.collect_date = 0 ) " +
                " LEFT JOIN hh_hotel_master HMR ON (HB.id = HMR.id )" +
                " LEFT JOIN hh_hotel_bbs HBBS ON (HB.id = HBBS.id and (thread_status=1 OR thread_status=2) AND kind_flag=0 )" +
                " LEFT JOIN hh_hotel_happie HH ON (HB.id = HH.id and (HH.start_date <=" + DateEdit.getDate( 2 ) + " AND HH.end_date >=" + DateEdit.getDate( 2 ) + "))" +
                " WHERE HB.id in (";
        for( int i = 0 ; i < hotelcount ; i++ )
        {
            if ( i == 0 )
                query = query + "?";
            else
                query = query + ", " + "?";
        }
        query = query + " ) group by HB.id order by HB.rank desc ,HP.all_point DESC,HB.name_kana";

        try
        {
            connection = DBConnection.getConnectionRO();

            prestate = connection.prepareStatement( query );

            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {

                for( int i = 0 ; result.next() ; i++ )
                {
                    dataSearchHotel[i] = new DataSearchHotel_M2();
                    // Sets Hotel Data
                    dataSearchHotel[i].setId( result.getInt( "id" ) );
                    dataSearchHotel[i].setRank( result.getInt( "rank" ) );

                    dataSearchHotel[i].setName( CheckString.checkStringForNull( result.getString( "name" ) ) );
                    dataSearchHotel[i].setNameMobile( CheckString.checkStringForNull( result.getString( "name_mobile" ) ) );

                    dataSearchHotel[i].setPrefName( CheckString.checkStringForNull( result.getString( "pref_name" ) ) );
                    dataSearchHotel[i].setAddress1( CheckString.checkStringForNull( result.getString( "address1" ) ) );

                    dataSearchHotel[i].setAddressAll( CheckString.checkStringForNull( result.getString( "address_all" ) ) );
                    dataSearchHotel[i].setTel1( CheckString.checkStringForNull( result.getString( "tel1" ) ) );
                    dataSearchHotel[i].setLat( CheckString.checkStringForNull( result.getString( "hotel_lat" ) ) );
                    dataSearchHotel[i].setLon( CheckString.checkStringForNull( result.getString( "hotel_lon" ) ) );
                    dataSearchHotel[i].setUrl( CheckString.checkStringForNull( result.getString( "url" ) ) );

                    dataSearchHotel[i].setUrlOfficial1( CheckString.checkStringForNull( result.getString( "url_official1" ) ) );
                    dataSearchHotel[i].setUrlOfficial2( CheckString.checkStringForNull( result.getString( "url_official2" ) ) );
                    dataSearchHotel[i].setPr( CheckString.checkStringForNull( result.getString( "pr" ) ) );

                    dataSearchHotel[i].setReserve( result.getInt( "reserve" ) );
                    dataSearchHotel[i].setReserveTel( result.getInt( "reserve_tel" ) );

                    dataSearchHotel[i].setReserveMail( result.getInt( "reserve_mail" ) );
                    dataSearchHotel[i].setReserveWeb( result.getInt( "reserve_web" ) );

                    // hh_hotel_master.empty_disp_kind == 1�̏ꍇ�̂݋󖞏����擾����
                    if ( result.getInt( "empty_disp_kind" ) == 1 )
                    {
                        dataSearchHotel[i].setEmptyStatus( result.getInt( "empty_status" ) );
                    }
                    else
                    {
                        dataSearchHotel[i].setEmptyStatus( NON_DISP );
                    }
                    dataSearchHotel[i].setOver18Flag( result.getInt( "over18_flag" ) );
                    dataSearchHotel[i].setCompanyType( result.getInt( "company_type" ) );

                    dataSearchHotel[i].setLastUpDate( result.getInt( "last_update" ) );
                    dataSearchHotel[i].setLastUpTime( result.getInt( "last_uptime" ) );

                    dataSearchHotel[i].setHotelMessage( CheckString.checkStringForNull( result.getString( "disp_message" ) ) );
                    dataSearchHotel[i].setCouponCount( result.getInt( "coupon_count" ) );

                    dataSearchHotel[i].setBbsConfig( result.getInt( "bbs_config" ) );
                    dataSearchHotel[i].setBbsAllCount( result.getInt( "bbsAllCount" ) );
                    dataSearchHotel[i].setPoints( result.getInt( "points" ) );

                    // �X�^���_�[�h�z�e���̂݉摜���擾����
                    if ( result.getInt( "rank" ) >= 2 )
                    {
                        fileImg = new File( imgPath + result.getInt( "id" ) + "jpg.jpg" );
                        if ( fileImg.exists() == false )
                        {
                            dataSearchHotel[i].setImagePath( noimgUrl );
                        }
                        else
                        {
                            dataSearchHotel[i].setImagePath( imgUrl + result.getInt( "id" ) + "jpg.jpg" );
                        }
                    }
                    else
                    {
                        dataSearchHotel[i].setImagePath( "GetByServlet" );
                    }
                    // �n�s�[�����X�֘A
                    if ( result.getInt( "rank" ) >= 3 )
                    {
                        dataSearchHotel[i].setHappieName( result.getString( "happie_name" ) );

                        // �\��v�����̃f�[�^���擾
                        ret = this.getReserveData( result.getInt( "id" ) );
                        dataSearchHotel[i].setIsReserveFlag( ret );
                        if ( ret != false )
                        {
                            dataSearchHotel[i].setReservePlanCount( this.reserveCount );
                            dataSearchHotel[i].setReservePlanId( this.reservePlanId );
                            dataSearchHotel[i].setReservePlan( this.reservePlan );
                        }
                        else
                        {
                            dataSearchHotel[i].setReservePlanCount( this.reserveCount );
                            dataSearchHotel[i].setReservePlanId( this.reservePlanId );
                            dataSearchHotel[i].setReservePlan( this.reservePlan );
                        }

                        // 3�����摜�̎擾
                        ret = this.getRoomSeq( result.getInt( "id" ) );
                        if ( ret != false )
                        {
                            dataSearchHotel[i].setRoomCount( this.roomCount );
                            dataSearchHotel[i].setRoomSeq( this.roomSeq );
                        }
                        else
                        {
                            dataSearchHotel[i].setRoomCount( 0 );
                            dataSearchHotel[i].setRoomSeq( null );
                        }
                    }
                    else
                    {
                        dataSearchHotel[i].setHappieName( "" );
                        dataSearchHotel[i].setIsReserveFlag( false );
                        dataSearchHotel[i].setReservePlanCount( 0 );
                        dataSearchHotel[i].setReservePlan( null );
                        dataSearchHotel[i].setRoomCount( 0 );
                        dataSearchHotel[i].setRoomSeq( null );
                    }
                }
            }
            return dataSearchHotel;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelDao_M2.getDataFromDataBase(int[] arrHotelId = " + arrHotelId + ") ] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            dataSearchHotel = null;
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �z�e�����擾
     * 
     * @param arrhotelId(array of HotelIds
     * @return array of DataSearchHotel Objects
     * @throws Exception
     */
    public DataSearchHotel_M2[] getDataFromDataBase(int[] arrHotelId) throws Exception
    {
        boolean ret = false;
        String query1;
        String query2;
        String query3;
        String query4;
        String query5;

        String strIdlist = "";
        File fileImg;
        int count = 0;
        int hotelId;
        int prevHoteId;
        int hotelcount = arrHotelId.length;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        DataSearchHotel_M2 dataSearchHotelUO[] = new DataSearchHotel_M2[hotelcount];
        DataSearchHotel_M2 dataSearchHotel[] = new DataSearchHotel_M2[hotelcount];

        for( int i = 0 ; i < hotelcount ; i++ )
        {
            if ( i == 0 )
                strIdlist = strIdlist + "?";
            else
                strIdlist = strIdlist + ", " + "?";
        }

        query1 = "SELECT HB.id, HB.rank, HB.name, HB.pref_name,HB.name_mobile, HB.address1, HB.address_all, HB.touch_equip_flag, " +
                " HB.tel1, HB.hotel_lat, HB.hotel_lon, HB.url, HB.url_official1,  HB.url_official2, HB.pr, HB.reserve, HB.reserve_tel," +
                " HB.reserve_mail,  HB.reserve_web, HS.empty_status,HB.over18_flag, HB.company_type," +
                " HS.last_update, HS.last_uptime ,HMR.bbs_config, HMR.empty_disp_kind, HH.name AS plan_name" +
                " FROM hh_hotel_basic HB" +
                " LEFT JOIN hh_hotel_status HS ON (HB.id = HS.id)" +
                " LEFT JOIN hh_hotel_master HMR ON (HB.id = HMR.id )" +
                " LEFT JOIN hh_hotel_happie HH ON (HB.id = HH.id and (HH.start_date <=" + DateEdit.getDate( 2 ) + " AND HH.end_date >=" + DateEdit.getDate( 2 ) + "))" +
                " WHERE HB.id in (" + strIdlist + ") group by HB.id";

        query2 = "SELECT HB.id, COUNT(HC.id) as coupon_count" +
                " FROM hh_hotel_basic HB ,hh_hotel_coupon HC" +
                " WHERE HB.id in (" + strIdlist + ")" +
                " and HB.id = HC.id  AND HC.del_flag <> 1 AND (HC.start_date <= " + DateEdit.getDate( 2 ) + " AND HC.end_date >= " + DateEdit.getDate( 2 ) + ")" +
                " group by HB.id";

        query3 = "SELECT HB.id,COUNT(HBBS.id) as bbsAllCount ,SUM(HBBS.point*100)as points" +
                " FROM hh_hotel_basic HB , hh_hotel_bbs HBBS" +
                " WHERE HB.id in (" + strIdlist + ")" +
                " and HB.id = HBBS.id and (thread_status=1 OR thread_status=2) AND kind_flag=0" +
                " group by HB.id";

        query4 = "SELECT HMM.id, HMM.disp_message, HMM.seq " +
                " FROM hh_hotel_message HMM " +
                " WHERE HMM.id IN (" + strIdlist + ") " +
                " AND HMM.del_flag = 0 " +
                " AND ( HMM.start_date < ? OR (HMM.start_date = ? AND HMM.start_time <= ? ))" +
                " AND ( HMM.end_date > ? OR ( HMM.end_date = ? AND HMM.end_time >= ? ))" +
                " ORDER BY HMM.id," +
                " HMM.start_date DESC, HMM.start_time DESC, HMM.last_update DESC, HMM.last_uptime DESC,HMM.seq DESC";

        query5 = "SELECT id,reserve_pr,sales_flag FROM newRsvDB.hh_rsv_reserve_basic WHERE id in (" + strIdlist + ") ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query1 );

            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                for( int j = 0 ; result.next() ; j++ )
                {
                    ret = false;
                    dataSearchHotelUO[j] = new DataSearchHotel_M2();
                    // Sets Hotel Data
                    dataSearchHotelUO[j].setId( result.getInt( "id" ) );
                    dataSearchHotelUO[j].setRank( result.getInt( "rank" ) );
                    dataSearchHotelUO[j].setName( CheckString.checkStringForNull( result.getString( "name" ) ) );
                    dataSearchHotelUO[j].setNameMobile( CheckString.checkStringForNull( result.getString( "name_mobile" ) ) );
                    dataSearchHotelUO[j].setPrefName( CheckString.checkStringForNull( result.getString( "pref_name" ) ) );
                    dataSearchHotelUO[j].setAddress1( CheckString.checkStringForNull( result.getString( "address1" ) ) );
                    dataSearchHotelUO[j].setAddressAll( CheckString.checkStringForNull( result.getString( "address_all" ) ) );
                    dataSearchHotelUO[j].setTel1( CheckString.checkStringForNull( result.getString( "tel1" ) ) );
                    dataSearchHotelUO[j].setLat( CheckString.checkStringForNull( result.getString( "hotel_lat" ) ) );
                    dataSearchHotelUO[j].setLon( CheckString.checkStringForNull( result.getString( "hotel_lon" ) ) );
                    dataSearchHotelUO[j].setUrl( CheckString.checkStringForNull( result.getString( "url" ) ) );
                    dataSearchHotelUO[j].setUrlOfficial1( CheckString.checkStringForNull( result.getString( "url_official1" ) ) );
                    dataSearchHotelUO[j].setUrlOfficial2( CheckString.checkStringForNull( result.getString( "url_official2" ) ) );
                    dataSearchHotelUO[j].setPr( CheckString.checkStringForNull( result.getString( "pr" ) ) );
                    dataSearchHotelUO[j].setReserve( result.getInt( "reserve" ) );
                    dataSearchHotelUO[j].setReserveTel( result.getInt( "reserve_tel" ) );
                    dataSearchHotelUO[j].setReserveMail( result.getInt( "reserve_mail" ) );
                    dataSearchHotelUO[j].setReserveWeb( result.getInt( "reserve_web" ) );
                    dataSearchHotelUO[j].setTouchEquipFlag( result.getInt( "touch_equip_flag" ) );
                    // hh_hotel_master.empty_disp_kind == 1�̏ꍇ�̂݋󖞏����擾����
                    if ( result.getInt( "empty_disp_kind" ) == 1 )
                    {
                        dataSearchHotelUO[j].setEmptyStatus( result.getInt( "empty_status" ) );
                    }
                    else
                    {
                        dataSearchHotelUO[j].setEmptyStatus( NON_DISP );
                    }
                    dataSearchHotelUO[j].setOver18Flag( result.getInt( "over18_flag" ) );
                    dataSearchHotelUO[j].setCompanyType( result.getInt( "company_type" ) );
                    dataSearchHotelUO[j].setLastUpDate( result.getInt( "last_update" ) );
                    dataSearchHotelUO[j].setLastUpTime( result.getInt( "last_uptime" ) );
                    dataSearchHotelUO[j].setBbsConfig( result.getInt( "bbs_config" ) );
                    if ( result.getInt( "rank" ) >= 2 )
                    {
                        fileImg = new File( imgPath + result.getInt( "id" ) + "jpg.jpg" );
                        if ( fileImg.exists() == false )
                        {
                            dataSearchHotelUO[j].setImagePath( noimgUrl );
                        }
                        else
                        {
                            dataSearchHotelUO[j].setImagePath( imgUrl + result.getInt( "id" ) + "jpg.jpg" );
                        }
                    }
                    else
                    {
                        dataSearchHotelUO[j].setImagePath( "GetByServlet" );
                    }
                    // �n�s�[�����X�֘A
                    if ( result.getInt( "rank" ) >= 3 )
                    {
                        dataSearchHotelUO[j].setHappieName( result.getString( "plan_name" ) );

                        ret = this.getReserveData( result.getInt( "id" ) );
                        dataSearchHotelUO[j].setIsReserveFlag( ret );
                        // �f�[�^����������f�[�^���Z�b�g
                        if ( ret != false )
                        {
                            dataSearchHotelUO[j].setReservePlanCount( this.reserveCount );
                            dataSearchHotelUO[j].setReservePlanId( this.reservePlanId );
                            dataSearchHotelUO[j].setReservePlan( this.reservePlan );

                        }
                        else
                        {
                            dataSearchHotelUO[j].setReservePlanCount( 0 );
                            dataSearchHotelUO[j].setReservePlanId( null );
                            dataSearchHotelUO[j].setReservePlan( null );
                        }
                        // �����̃f�[�^���擾
                        ret = this.getRoomSeq( result.getInt( "id" ) );
                        if ( ret != false )
                        {
                            dataSearchHotelUO[j].setRoomCount( this.roomCount );
                            dataSearchHotelUO[j].setRoomSeq( this.roomSeq );
                        }
                        else
                        {
                            dataSearchHotelUO[j].setRoomCount( 0 );
                            dataSearchHotelUO[j].setRoomSeq( null );
                        }
                    }
                    else
                    {
                        dataSearchHotelUO[j].setHappieName( "" );
                        dataSearchHotelUO[j].setIsReserveFlag( false );
                        dataSearchHotelUO[j].setReservePlanCount( 0 );
                        dataSearchHotelUO[j].setReservePlan( null );
                        dataSearchHotelUO[j].setRoomCount( 0 );
                        dataSearchHotelUO[j].setRoomSeq( null );
                    }
                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < hotelcount ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i] = dataSearchHotelUO[j];
                            break;
                        }
                    }
                }
            }

            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;
            // //////////////////////////////////////////////////////////////////////////////////////
            prestate = connection.prepareStatement( query2 );
            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                for( int j = 0 ; result.next() ; j++ )
                {
                    dataSearchHotelUO[j] = new DataSearchHotel_M2();
                    dataSearchHotelUO[j].setId( result.getInt( "id" ) );
                    dataSearchHotelUO[j].setCouponCount( result.getInt( "coupon_count" ) );
                    count++;
                }
                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setCouponCount( dataSearchHotelUO[j].getCouponCount() );
                            break;
                        }
                    }
                }
            }

            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;
            // //////////////////////////////////////////////////////////////////////////////////////

            prestate = connection.prepareStatement( query3 );
            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                for( int j = 0 ; result.next() ; j++ )
                {
                    dataSearchHotelUO[j] = new DataSearchHotel_M2();
                    dataSearchHotelUO[j].setId( result.getInt( "id" ) );
                    dataSearchHotelUO[j].setBbsAllCount( result.getInt( "bbsAllCount" ) );
                    dataSearchHotelUO[j].setPoints( result.getInt( "points" ) );
                    count++;
                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setBbsAllCount( dataSearchHotelUO[j].getBbsAllCount() );
                            dataSearchHotel[i].setPoints( dataSearchHotelUO[j].getPoints() );
                            break;
                        }
                    }
                }

            }
            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;
            // //////////////////////////////////////////////////////////////////////////////////////

            int today_date = Integer.parseInt( DateEdit.getDate( 2 ) );
            int today_time = Integer.parseInt( DateEdit.getTime( 1 ) );
            prestate = connection.prepareStatement( query4 );
            int k = 0;
            for( k = 0 ; k < hotelcount ; k++ )
            {
                prestate.setInt( (k + 1), arrHotelId[k] );
            }
            k = k + 1;
            prestate.setInt( k++, today_date );
            prestate.setInt( k++, today_date );
            prestate.setInt( k++, today_time );
            prestate.setInt( k++, today_date );
            prestate.setInt( k++, today_date );
            prestate.setInt( k++, today_time );

            result = prestate.executeQuery();

            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                prevHoteId = 0;
                for( int j = 0 ; result.next() ; j++ )
                {

                    hotelId = result.getInt( "id" );
                    if ( hotelId != prevHoteId )
                    {
                        prevHoteId = hotelId;
                        dataSearchHotelUO[count] = new DataSearchHotel_M2();
                        dataSearchHotelUO[count].setId( hotelId );
                        dataSearchHotelUO[count].setHotelMessage( CheckString.checkStringForNull( result.getString( "disp_message" ) ) );
                        count++;
                    }

                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setHotelMessage( dataSearchHotelUO[j].getHotelMessage() );
                            break;
                        }
                    }
                }

            }
            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;

            // //////////////////////////////////////////////////////////////////////////////////////

            prestate = connection.prepareStatement( query5 );

            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    for( int i = 0 ; i < dataSearchHotel.length ; i++ )
                    {
                        if ( dataSearchHotel[i].getId() == result.getInt( "id" ) )
                        {
                            dataSearchHotel[i].setReserveSalesFlag( result.getInt( "sales_flag" ) );
                            dataSearchHotel[i].setReservePlanPr( ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "reserve_pr" ) )).trim() ) );
                            break;
                        }
                    }
                }
            }
            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;

            return dataSearchHotel;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelDao_M2.getDataFromDataBase(int[] arrHotelId = " + arrHotelId + ") ] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            dataSearchHotelUO = null;
            dataSearchHotel = null;
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �\��n�s�[�����X���擾
     * 
     * @param arrhotelId �z�e��ID���X�g
     * @return ��������
     */
    public DataSearchHotel_M2[] getRsvHappieDataFromDataBase(int[] arrHotelId) throws Exception
    {
        boolean ret = false;
        String query1;
        String query2;
        String query3;
        String query4;
        String query5;

        String strIdlist = "";
        File fileImg;
        int count = 0;
        int hotelId;
        int prevHoteId;
        int hotelcount = arrHotelId.length;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        DataSearchHotel_M2 dataSearchHotelUO[] = new DataSearchHotel_M2[hotelcount];
        DataSearchHotel_M2 dataSearchHotel[] = new DataSearchHotel_M2[hotelcount];

        for( int i = 0 ; i < hotelcount ; i++ )
        {
            if ( i == 0 )
                strIdlist = strIdlist + "?";
            else
                strIdlist = strIdlist + ", " + "?";
        }

        query1 = "SELECT HB.id, HB.rank, HB.name, HB.pref_name,HB.name_mobile, HB.address1, HB.address_all," +
                " HB.tel1, HB.hotel_lat, HB.hotel_lon, HB.url, HB.url_official1,  HB.url_official2, HB.pr, HB.reserve, HB.reserve_tel," +
                " HB.reserve_mail,  HB.reserve_web, HS.empty_status,HB.over18_flag, HB.company_type," +
                " HS.last_update, HS.last_uptime, HMR.bbs_config, HMR.empty_disp_kind, HH.name AS plan_name" +
                " FROM hh_hotel_basic HB" +
                " LEFT JOIN hh_hotel_status HS ON (HB.id = HS.id)" +
                " LEFT JOIN hh_hotel_master HMR ON (HB.id = HMR.id )" +
                " LEFT JOIN hh_hotel_happie HH ON (HB.id = HH.id and (HH.start_date <=" + DateEdit.getDate( 2 ) + " AND HH.end_date >=" + DateEdit.getDate( 2 ) + "))" +
                " WHERE HB.id in (" + strIdlist + ") group by HB.id";

        query2 = "SELECT HB.id, COUNT(HC.id) as coupon_count" +
                " FROM hh_hotel_basic HB ,hh_hotel_coupon HC" +
                " WHERE HB.id in (" + strIdlist + ")" +
                " and HB.id = HC.id  AND HC.del_flag <> 1 AND (HC.start_date <= " + DateEdit.getDate( 2 ) + " AND HC.end_date >= " + DateEdit.getDate( 2 ) + ")" +
                " group by HB.id";

        query3 = "SELECT HB.id,COUNT(HBBS.id) as bbsAllCount ,SUM(HBBS.point*100)as points" +
                " FROM hh_hotel_basic HB , hh_hotel_bbs HBBS" +
                " WHERE HB.id in (" + strIdlist + ")" +
                " and HB.id = HBBS.id and (thread_status=1 OR thread_status=2) AND kind_flag=0" +
                " group by HB.id";

        query4 = "SELECT HMM.id, HMM.disp_message, HMM.seq " +
                " FROM hh_hotel_message HMM " +
                " WHERE HMM.id IN (" + strIdlist + ") " +
                " AND HMM.del_flag = 0 " +
                " AND (HMM.start_date <= " + DateEdit.getDate( 2 ) + " AND HMM.end_date >= " + DateEdit.getDate( 2 ) + " )  " +
                " ORDER BY HMM.id, HMM.seq DESC";

        query5 = "SELECT HRP.id, HRRB.reserve_pr plan_pr, HRP.plan_id, HRP.image_pc, HRP.image_gif, HRP.image_png" +
                " FROM hh_rsv_plan HRP, newRsvDB.hh_rsv_reserve_basic HRRB" +
                " WHERE HRP.id = HRRB.id" +
                " AND HRP.id IN (" + strIdlist + ") " +
                " AND HRRB.sales_flag = 1 " +
                " AND HRP.sales_flag = 1 " +
                " AND (HRP.disp_start_date <= " + DateEdit.getDate( 2 ) + " AND HRP.disp_end_date >= " + DateEdit.getDate( 2 ) + " )  " +
                " ORDER BY HRP.id, HRP.disp_index, HRP.plan_id DESC" +
                " LIMIT 0,3 ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query1 );

            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                for( int j = 0 ; result.next() ; j++ )
                {
                    ret = false;
                    dataSearchHotelUO[j] = new DataSearchHotel_M2();
                    // Sets Hotel Data
                    dataSearchHotelUO[j].setId( result.getInt( "id" ) );
                    dataSearchHotelUO[j].setRank( result.getInt( "rank" ) );
                    dataSearchHotelUO[j].setName( CheckString.checkStringForNull( result.getString( "name" ) ) );
                    dataSearchHotelUO[j].setNameMobile( CheckString.checkStringForNull( result.getString( "name_mobile" ) ) );
                    dataSearchHotelUO[j].setPrefName( CheckString.checkStringForNull( result.getString( "pref_name" ) ) );
                    dataSearchHotelUO[j].setAddress1( CheckString.checkStringForNull( result.getString( "address1" ) ) );
                    dataSearchHotelUO[j].setAddressAll( CheckString.checkStringForNull( result.getString( "address_all" ) ) );
                    dataSearchHotelUO[j].setTel1( CheckString.checkStringForNull( result.getString( "tel1" ) ) );
                    dataSearchHotelUO[j].setLat( CheckString.checkStringForNull( result.getString( "hotel_lat" ) ) );
                    dataSearchHotelUO[j].setLon( CheckString.checkStringForNull( result.getString( "hotel_lon" ) ) );
                    dataSearchHotelUO[j].setUrl( CheckString.checkStringForNull( result.getString( "url" ) ) );
                    dataSearchHotelUO[j].setUrlOfficial1( CheckString.checkStringForNull( result.getString( "url_official1" ) ) );
                    dataSearchHotelUO[j].setUrlOfficial2( CheckString.checkStringForNull( result.getString( "url_official2" ) ) );
                    dataSearchHotelUO[j].setPr( CheckString.checkStringForNull( result.getString( "pr" ) ) );
                    dataSearchHotelUO[j].setReserve( result.getInt( "reserve" ) );
                    dataSearchHotelUO[j].setReserveTel( result.getInt( "reserve_tel" ) );
                    dataSearchHotelUO[j].setReserveMail( result.getInt( "reserve_mail" ) );
                    dataSearchHotelUO[j].setReserveWeb( result.getInt( "reserve_web" ) );
                    // hh_hotel_master.empty_disp_kind == 1�̏ꍇ�̂݋󖞏����擾����
                    if ( result.getInt( "empty_disp_kind" ) == 1 )
                    {
                        dataSearchHotelUO[j].setEmptyStatus( result.getInt( "empty_status" ) );
                    }
                    else
                    {
                        dataSearchHotelUO[j].setEmptyStatus( NON_DISP );
                    }
                    dataSearchHotelUO[j].setOver18Flag( result.getInt( "over18_flag" ) );
                    dataSearchHotelUO[j].setCompanyType( result.getInt( "company_type" ) );
                    dataSearchHotelUO[j].setLastUpDate( result.getInt( "last_update" ) );
                    dataSearchHotelUO[j].setLastUpTime( result.getInt( "last_uptime" ) );
                    dataSearchHotelUO[j].setBbsConfig( result.getInt( "bbs_config" ) );
                    if ( result.getInt( "rank" ) >= 2 )
                    {
                        fileImg = new File( imgPath + result.getInt( "id" ) + "jpg.jpg" );
                        if ( fileImg.exists() == false )
                        {
                            dataSearchHotelUO[j].setImagePath( noimgUrl );
                        }
                        else
                        {
                            dataSearchHotelUO[j].setImagePath( imgUrl + result.getInt( "id" ) + "jpg.jpg" );
                        }
                    }
                    else
                    {
                        dataSearchHotelUO[j].setImagePath( "GetByServlet" );
                    }

                    /* ���n�s�[�����X�� */
                    if ( result.getInt( "rank" ) >= 3 )
                    {
                        dataSearchHotelUO[j].setHappieName( result.getString( "plan_name" ) );
                        // �����̃f�[�^���擾
                        ret = this.getRoomSeq( result.getInt( "id" ) );
                        if ( ret != false )
                        {
                            dataSearchHotelUO[j].setRoomCount( this.roomCount );
                            dataSearchHotelUO[j].setRoomSeq( this.roomSeq );
                        }
                        else
                        {
                            dataSearchHotelUO[j].setRoomCount( 0 );
                            dataSearchHotelUO[j].setRoomSeq( null );
                        }
                    }
                    else
                    {
                        dataSearchHotelUO[j].setHappieName( "" );
                        dataSearchHotelUO[j].setRoomCount( 0 );
                        dataSearchHotelUO[j].setRoomSeq( null );
                    }
                    /* ���n�s�[�����X�� */

                    /* ���\��n�s�[�����X�� */
                    if ( result.getInt( "rank" ) >= 3 )
                    {
                        ret = this.getReserveData( result.getInt( "id" ) );
                        dataSearchHotelUO[j].setIsReserveFlag( ret );
                        // �f�[�^����������f�[�^���Z�b�g
                        if ( ret != false )
                        {
                            dataSearchHotelUO[j].setReservePlanCount( this.reserveCount );
                            dataSearchHotelUO[j].setReservePlanId( this.reservePlanId );
                            dataSearchHotelUO[j].setReservePlan( this.reservePlan );
                        }
                        else
                        {
                            dataSearchHotelUO[j].setReservePlanCount( 0 );
                            dataSearchHotelUO[j].setReservePlanId( null );
                            dataSearchHotelUO[j].setReservePlan( null );
                        }
                    }
                    else
                    {
                        dataSearchHotelUO[j].setIsReserveFlag( false );
                        dataSearchHotelUO[j].setReservePlanCount( 0 );
                        dataSearchHotelUO[j].setReservePlan( null );
                    }
                    /* ���\��n�s�[�����X�� */
                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < hotelcount ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i] = dataSearchHotelUO[j];
                            break;
                        }
                    }
                }
            }

            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;
            // //////////////////////////////////////////////////////////////////////////////////////
            prestate = connection.prepareStatement( query2 );
            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                for( int j = 0 ; result.next() ; j++ )
                {
                    dataSearchHotelUO[j] = new DataSearchHotel_M2();
                    dataSearchHotelUO[j].setId( result.getInt( "id" ) );
                    dataSearchHotelUO[j].setCouponCount( result.getInt( "coupon_count" ) );
                    count++;
                }
                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setCouponCount( dataSearchHotelUO[j].getCouponCount() );
                            break;
                        }
                    }
                }
            }

            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;
            // //////////////////////////////////////////////////////////////////////////////////////

            prestate = connection.prepareStatement( query3 );
            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                for( int j = 0 ; result.next() ; j++ )
                {
                    dataSearchHotelUO[j] = new DataSearchHotel_M2();
                    dataSearchHotelUO[j].setId( result.getInt( "id" ) );
                    dataSearchHotelUO[j].setBbsAllCount( result.getInt( "bbsAllCount" ) );
                    dataSearchHotelUO[j].setPoints( result.getInt( "points" ) );
                    count++;
                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setBbsAllCount( dataSearchHotelUO[j].getBbsAllCount() );
                            dataSearchHotel[i].setPoints( dataSearchHotelUO[j].getPoints() );
                            break;
                        }
                    }
                }

            }
            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;
            // //////////////////////////////////////////////////////////////////////////////////////

            prestate = connection.prepareStatement( query4 );

            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                prevHoteId = 0;
                for( int j = 0 ; result.next() ; j++ )
                {

                    hotelId = result.getInt( "id" );
                    if ( hotelId != prevHoteId )
                    {
                        prevHoteId = hotelId;
                        dataSearchHotelUO[count] = new DataSearchHotel_M2();
                        dataSearchHotelUO[count].setId( hotelId );
                        dataSearchHotelUO[count].setHotelMessage( CheckString.checkStringForNull( result.getString( "disp_message" ) ) );
                        count++;
                    }

                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setHotelMessage( dataSearchHotelUO[j].getHotelMessage() );
                            break;
                        }
                    }
                }

            }
            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;

            // //////////////////////////////////////////////////////////////////////////////////////

            prestate = connection.prepareStatement( query5 );

            for( int i = 0 ; i < hotelcount ; i++ )
            {
                prestate.setInt( (i + 1), arrHotelId[i] );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                dataSearchHotelUO = new DataSearchHotel_M2[hotelcount];
                count = 0;
                prevHoteId = 0;
                for( int j = 0 ; result.next() ; j++ )
                {

                    hotelId = result.getInt( "id" );
                    if ( hotelId != prevHoteId )
                    {
                        prevHoteId = hotelId;
                        dataSearchHotelUO[count] = new DataSearchHotel_M2();
                        dataSearchHotelUO[count].setId( hotelId );
                        dataSearchHotelUO[count].setReservePlanPr( CheckString.checkStringForNull( result.getString( "plan_pr" ) ) );
                        dataSearchHotelUO[count].setReservePlanImagePc( CheckString.checkStringForNull( result.getString( "image_pc" ) ) );
                        dataSearchHotelUO[count].setReservePlanImageGif( CheckString.checkStringForNull( result.getString( "image_gif" ) ) );
                        dataSearchHotelUO[count].setReservePlanImagePng( CheckString.checkStringForNull( result.getString( "image_png" ) ) );
                        count++;
                    }

                }

                for( int i = 0 ; i < hotelcount ; i++ )
                { // ORDERED ID LIST
                    for( int j = 0 ; j < count ; j++ )
                    { // UNORDERED DATA LIST
                        if ( dataSearchHotelUO[j].getId() == arrHotelId[i] )
                        {
                            dataSearchHotel[i].setReservePlanPr( dataSearchHotelUO[j].getReservePlanPr() );
                            dataSearchHotel[i].setReservePlanImagePc( dataSearchHotelUO[j].getReservePlanImagePc() );
                            dataSearchHotel[i].setReservePlanImageGif( dataSearchHotelUO[j].getReservePlanImageGif() );
                            dataSearchHotel[i].setReservePlanImagePng( dataSearchHotelUO[j].getReservePlanImagePng() );
                            // PR��ReservePlanPr�ŏ㏑��������
                            dataSearchHotel[i].setPr( dataSearchHotelUO[j].getReservePlanPr() );

                            // �v�����̉摜���󔒈ȊO�ł���΁AImagePath��ReservePlanImagePc���Z�b�g����
                            if ( dataSearchHotelUO[j].getReservePlanImagePc().compareTo( "" ) != 0 )
                            {
                                dataSearchHotel[i].setImagePath( dataSearchHotelUO[j].getReservePlanImagePc() );
                            }
                            break;
                        }
                    }
                }

            }
            dataSearchHotelUO = null;
            DBConnection.releaseResources( result );
            result = null;
            DBConnection.releaseResources( prestate );
            prestate = null;

            return dataSearchHotel;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelDao_M2.getRsvHappieDataFromDataBase(int[] arrHotelId = " + arrHotelId + ") ] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            dataSearchHotelUO = null;
            dataSearchHotel = null;
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 
     * This method finds the details of the hotels that are searched corresponding to the hotel ids
     * 
     * @param hotel ids
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     */
    public void getHotelList(int[] hotelBasicList, int countNum, int pageNum) throws Exception
    {
        int loop;
        int count;

        if ( hotelBasicList != null )
        {
            m_hotelAllCount = hotelBasicList.length;
            if ( m_hotelAllCount > 0 )
            {
                count = 0;
                for( loop = countNum * pageNum ; loop < m_hotelAllCount ; loop++ )
                { // no of hotel to be fetch
                    count++;
                    if ( count >= countNum && countNum != 0 )
                        break;

                }

                int hotelIds[] = new int[count];
                int start = countNum * pageNum;
                for( int k = 0 ; k < count ; k++ )
                {
                    hotelIds[k] = hotelBasicList[start];
                    start++;
                }

                m_hotelInfo = this.getDataFromDataBase( hotelIds );
                for( loop = 0 ; loop < count ; loop++ )
                {
                    if ( m_hotelInfo[loop].getRank() >= 2 )
                    {
                        m_hotelInfo[loop].setStars( PagingDetails.getStarsCount( m_hotelInfo[loop].getPoints(), m_hotelInfo[loop].getBbsAllCount() ) );
                    }
                }
                hotelIds = null;
                m_hotelCount = count;
            }
        }
    }

    /**
     * �\��n�s�[�����X�̃z�e��ID���X�g���擾
     * 
     * @param hotelBasicList �z�e��ID���X�g
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     */
    public void getRsvHappieList(int[] hotelBasicList, int countNum, int pageNum) throws Exception
    {
        int loop;
        int count;

        if ( hotelBasicList != null )
        {
            m_hotelAllCount = hotelBasicList.length;
            if ( m_hotelAllCount > 0 )
            {
                count = 0;
                for( loop = countNum * pageNum ; loop < m_hotelAllCount ; loop++ )
                { // no of hotel to be fetch
                    count++;
                    if ( count >= countNum && countNum != 0 )
                        break;

                }

                int hotelIds[] = new int[count];
                int start = countNum * pageNum;
                for( int k = 0 ; k < count ; k++ )
                {
                    hotelIds[k] = hotelBasicList[start];
                    start++;
                }

                // ���̌������ʂł��\��PR��\������悤�ɂȂ������߂ɃR�����g�A�E�g
                // m_hotelInfo = this.getRsvHappieDataFromDataBase( hotelIds );

                // �\��n�s�[�����X�f�[�^�擾
                m_hotelInfo = this.getDataFromDataBase( hotelIds );
                for( loop = 0 ; loop < count ; loop++ )
                {
                    if ( m_hotelInfo[loop].getRank() >= 2 )
                    {
                        m_hotelInfo[loop].setStars( PagingDetails.getStarsCount( m_hotelInfo[loop].getPoints(), m_hotelInfo[loop].getBbsAllCount() ) );
                    }
                }
                hotelIds = null;
                m_hotelCount = count;
            }
        }
    }

    /****
     * �\��̃f�[�^���擾����
     * 
     * @param id
     * @return ��������(true�Afalse)
     */
    private boolean getReserveData(int id)
    {
        final int RESERVE_AVAILABLE = 1;
        boolean ret;
        int count = 0;
        int dataCount = 0;
        int today = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        // �\���{�f�[�^
        DataRsvReserveBasic drrb;

        drrb = new DataRsvReserveBasic();
        ret = drrb.getData( id );
        if ( ret != false )
        {
            if ( drrb.getSalesFlag() == RESERVE_AVAILABLE )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }

        // �O���[�o���ϐ��̏�����
        this.reserveCount = 0;
        this.reservePlan = null;

        // ���t�w�肪�Ȃ��ꍇ����t���J�����_�[�ɑ��݂��Ȃ��ꍇ�́A�����`�P����������ԂƂ���
        today = Integer.parseInt( DateEdit.getDate( 2 ) );

        // �\��n�s�[�����X��������\��̃f�[�^���擾����
        if ( ret != false )
        {

            // �I�����ꂽ���t�̗������[�h�Ōv�Z�A�I�����ꂽ�����̋󂫂�������̃v�������擾����
            query = " SELECT DISTINCT HRP.plan_id, HRP.plan_name FROM hh_rsv_plan HRP" +
                    "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND HRP.sales_flag = HRRB.sales_flag AND HRRB.sales_flag = 1" +
                    "   INNER JOIN hh_rsv_plan_charge HRPC ON HRP.id = HRPC.id AND HRP.plan_id = HRPC.plan_id" +
                    " WHERE HRP.id = ?" +
                    " AND HRP.sales_flag = 1" +
                    " AND HRP.publishing_flag = 1" +
                    " AND HRP.disp_start_date <=" + today +
                    " AND HRP.disp_end_date >=" + today +
                    " ORDER BY HRP.disp_index, HRP.plan_id DESC" +
                    " limit 0, 3";

            try
            {

                connection = DBConnection.getConnection();

                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, id );

                dataCount = 0;
                this.reserveCount = 0;
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.last() != false )
                    {
                        this.reserveCount = result.getRow();
                    }
                    this.reservePlanId = new int[reserveCount];
                    this.reservePlan = new String[reserveCount];

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ������
                        this.reservePlan[dataCount] = new String();

                        // �f�[�^���Z�b�g
                        this.reservePlan[dataCount] = ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "plan_name" ) )).trim() );
                        this.reservePlanId[dataCount] = result.getInt( "plan_id" );

                        dataCount++;
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[SearchHotelDao_M2.getReserveData()] Exception:" + e.toString() );

            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        return(ret);
    }

    /***
     * �z�e�������摜�f�[�^�Ǘ��ԍ��擾
     * 
     * @param id
     * @return
     */
    private boolean getRoomSeq(int id)
    {
        boolean ret;
        int count = 0;
        int dataCount = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT id, seq FROM hh_hotel_room";
        query += " WHERE id = ? AND disp_flag = 1";
        query += " ORDER BY seq";

        try
        {

            connection = DBConnection.getConnection();

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            dataCount = 0;
            this.roomCount = 0;
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.roomCount = result.getRow();
                }

                this.roomSeq = new int[this.roomCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �f�[�^���Z�b�g
                    this.roomSeq[dataCount] = result.getInt( "seq" );
                    dataCount++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelDao_M2.getRoomImage()] Exception:" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            if ( dataCount > 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }
        return(ret);
    }
}
