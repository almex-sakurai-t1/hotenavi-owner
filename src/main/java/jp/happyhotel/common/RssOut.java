package jp.happyhotel.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class RssOut
{
    private static final int    LAST_MONTH        = -2;                                                                                                                                                                                   // 3ヶ月前の日付までを取得する
    private static StringBuffer strRss            = new StringBuffer();
    private static String       strLimitRecord    = null;

    private static final String hotelBasicCols    =
                                                          "   hh_hotel_basic.id" +
                                                                  " , hh_hotel_basic.rank" +
                                                                  " , hh_hotel_basic.kind" +
                                                                  " , hh_hotel_basic.hotenavi_id" +
                                                                  " , hh_hotel_basic.name" +
                                                                  " , hh_hotel_basic.name_kana" +
                                                                  " , hh_hotel_basic.name_mobile" +
                                                                  " , hh_hotel_basic.zip_code" +
                                                                  " , hh_hotel_basic.jis_code" +
                                                                  " , hh_hotel_basic.pref_id" +
                                                                  " , hh_hotel_basic.pref_name" +
                                                                  " , hh_hotel_basic.pref_kana" +
                                                                  " , hh_hotel_basic.address1" +
                                                                  " , hh_hotel_basic.address2" +
                                                                  " , hh_hotel_basic.address3" +
                                                                  " , hh_hotel_basic.address_all" +
                                                                  " , hh_hotel_basic.tel1" +
                                                                  " , hh_hotel_basic.tel2" +
                                                                  " , hh_hotel_basic.fax" +
                                                                  " , hh_hotel_basic.charge_last" +
                                                                  " , hh_hotel_basic.charge_first" +
                                                                  " , hh_hotel_basic.charge_kana_last" +
                                                                  " , hh_hotel_basic.charge_kana_first" +
                                                                  " , hh_hotel_basic.charge_tel" +
                                                                  " , hh_hotel_basic.charge_mail" +
                                                                  " , hh_hotel_basic.open_date" +
                                                                  " , hh_hotel_basic.renewal_date" +
                                                                  " , hh_hotel_basic.url" +
                                                                  " , hh_hotel_basic.url_official1" +
                                                                  " , hh_hotel_basic.url_official2" +
                                                                  " , hh_hotel_basic.url_official_mobile" +
                                                                  " , hh_hotel_basic.pr" +
                                                                  " , hh_hotel_basic.pr_detail" +
                                                                  " , hh_hotel_basic.pr_event" +
                                                                  " , hh_hotel_basic.pr_member" +
                                                                  " , hh_hotel_basic.access" +
                                                                  " , hh_hotel_basic.access_station" +
                                                                  " , hh_hotel_basic.access_ic" +
                                                                  " , hh_hotel_basic.room_count" +
                                                                  " , hh_hotel_basic.parking" +
                                                                  " , hh_hotel_basic.parking_count" +
                                                                  " , hh_hotel_basic.type_building" +
                                                                  " , hh_hotel_basic.type_kodate" +
                                                                  " , hh_hotel_basic.type_rentou" +
                                                                  " , hh_hotel_basic.type_etc" +
                                                                  " , hh_hotel_basic.location_station" +
                                                                  " , hh_hotel_basic.location_ic" +
                                                                  " , hh_hotel_basic.location_kougai" +
                                                                  " , hh_hotel_basic.benefit" +
                                                                  " , hh_hotel_basic.roomservice" +
                                                                  " , hh_hotel_basic.pay_front" +
                                                                  " , hh_hotel_basic.pay_auto" +
                                                                  " , hh_hotel_basic.credit" +
                                                                  " , hh_hotel_basic.credit_visa" +
                                                                  " , hh_hotel_basic.credit_master" +
                                                                  " , hh_hotel_basic.credit_jcb" +
                                                                  " , hh_hotel_basic.credit_dc" +
                                                                  " , hh_hotel_basic.credit_nicos" +
                                                                  " , hh_hotel_basic.credit_amex" +
                                                                  " , hh_hotel_basic.credit_etc" +
                                                                  " , hh_hotel_basic.halfway" +
                                                                  " , hh_hotel_basic.coupon" +
                                                                  " , hh_hotel_basic.possible_one" +
                                                                  " , hh_hotel_basic.possible_three" +
                                                                  " , hh_hotel_basic.reserve" +
                                                                  " , hh_hotel_basic.reserve_tel" +
                                                                  " , hh_hotel_basic.reserve_mail" +
                                                                  " , hh_hotel_basic.reserve_web" +
                                                                  " , hh_hotel_basic.empty_method" +
                                                                  " , hh_hotel_basic.hotel_lat" +
                                                                  " , hh_hotel_basic.hotel_lon" +
                                                                  " , hh_hotel_basic.hotel_lat_num" +
                                                                  " , hh_hotel_basic.hotel_lon_num" +
                                                                  " , hh_hotel_basic.disp_lat" +
                                                                  " , hh_hotel_basic.disp_lon" +
                                                                  " , hh_hotel_basic.zoom" +
                                                                  " , hh_hotel_basic.over18_flag" +
                                                                  " , hh_hotel_basic.company_type" +
                                                                  " , hh_hotel_basic.last_update" +
                                                                  " , hh_hotel_basic.last_uptime" +
                                                                  " , IFNULL(hh_hotel_status.`mode`, 0) AS empty_kind" +
                                                                  " , IFNULL(hh_hotel_status.empty_status, 0) AS empty_status" +
                                                                  " , hh_hotel_basic.group_code" +
                                                                  " , hh_hotel_basic.hotel_picture_pc" +
                                                                  " , hh_hotel_basic.hotel_picture_gif" +
                                                                  " , hh_hotel_basic.hotel_picture_png" +
                                                                  " , hh_hotel_basic.pr_room" +
                                                                  " , hh_hotel_basic.pr_etc" +
                                                                  " , hh_hotel_basic.renewal_flag" +
                                                                  " , hh_hotel_basic.url_special" +
                                                                  " , hh_hotel_basic.hotel_lat_jp" +
                                                                  " , hh_hotel_basic.hotel_lon_jp" +
                                                                  " , hh_hotel_basic.map_code" +
                                                                  " , hh_hotel_basic.high_roof" +
                                                                  " , hh_hotel_basic.high_roof_count" +
                                                                  " , hh_hotel_basic.empty_hotenavi_id" +
                                                                  " , hh_hotel_basic.attention_flag" +
                                                                  " , hh_hotel_basic.ad_pref_id" +
                                                                  " , hh_hotel_basic.renewal_date_text" +
                                                                  " , hh_hotel_basic.new_open_search_flag" +
                                                                  " , hh_hotel_basic.url_yahoo" +
                                                                  " , hh_hotel_basic.touch_equip_flag" +
                                                                  " , hh_hotel_basic.noshow_hotelid";

    // Query to fetch new hotel arrival records
    private static String       strNewArrival     = " SELECT hh_hotel_adjustment_history.*," + hotelBasicCols +
                                                          " FROM hh_hotel_adjustment_history,hh_hotel_basic" +
                                                          " LEFT JOIN hh_hotel_status" +
                                                          " ON hh_hotel_basic.id = hh_hotel_status.id" +
                                                          " WHERE ((edit_id = 101 AND edit_sub = 0) OR (edit_id = 102 AND edit_sub = 0))" +
                                                          " AND hh_hotel_basic.id = hh_hotel_adjustment_history.id" +
                                                          " AND hh_hotel_basic.kind <= 7" +
                                                          " AND (hh_hotel_basic.rank = 1 OR hh_hotel_basic.rank = 2)" +
                                                          " GROUP BY hh_hotel_basic.id, hh_hotel_adjustment_history.edit_id" +
                                                          " ORDER BY hh_hotel_adjustment_history.input_date DESC, hh_hotel_adjustment_history.input_time DESC ";

    // Query to fetch new open hotel records
    private static String       strNewOpen        = "SELECT " + hotelBasicCols +
                                                          " FROM hh_hotel_basic" +
                                                          " LEFT JOIN hh_hotel_status" +
                                                          " ON hh_hotel_basic.id = hh_hotel_status.id" +
                                                          " WHERE renewal_date >= ? AND renewal_date <= ?" +
                                                          " ORDER BY renewal_date DESC, hh_hotel_basic.name_kana";

    // Query to fetch kuchikomi records
    private static String       strKuchiKomi      = "SELECT hh_hotel_bbs.*," + hotelBasicCols +
                                                          " FROM hh_hotel_bbs,hh_hotel_basic" +
                                                          " LEFT JOIN hh_hotel_status" +
                                                          " ON hh_hotel_basic.id = hh_hotel_status.id" +
                                                          " WHERE hh_hotel_basic.id = hh_hotel_bbs.id" +
                                                          " AND kind_flag = 0 AND (thread_status = 1 OR thread_status = 2)" +
                                                          " ORDER BY contribute_date DESC, contribute_time DESC";

    // Query to fetch latest information records
    private static String       strNewMessage     = "SELECT distinct hh_hotel_message.*," + hotelBasicCols +
                                                          " FROM hh_hotel_message,hh_hotel_basic" +
                                                          " LEFT JOIN hh_hotel_status" +
                                                          " ON hh_hotel_basic.id = hh_hotel_status.id" +
                                                          " ,hh_hotel_adjustment_history WHERE" +
                                                          " (hh_hotel_message.start_date < ? OR ( hh_hotel_message.start_date = ? AND hh_hotel_message.start_time <= ? ))" +
                                                          " AND hh_hotel_message.start_date >= ? " +
                                                          " AND ( hh_hotel_message.end_date > ? OR ( hh_hotel_message.end_date = ? AND hh_hotel_message.end_time >= ? ))" +
                                                          " AND hh_hotel_message.del_flag = 0" +
                                                          " AND hh_hotel_basic.id = hh_hotel_message.id AND hh_hotel_basic.kind <= 7" +
                                                          " AND hh_hotel_adjustment_history.edit_id = 50 AND hh_hotel_basic.id = hh_hotel_adjustment_history.id" +
                                                          " AND hh_hotel_message.disp_message <> ''" +
                                                          " ORDER BY hh_hotel_message.start_date DESC, hh_hotel_message.start_time DESC, hh_hotel_adjustment_history.input_date DESC, hh_hotel_basic.rank DESC, hh_hotel_basic.name_kana";

    // Query to fetch topics records
    private static String       strTopics         = "SELECT * FROM hh_system_info where" +
                                                          " hh_system_info.disp_flag = 1 " +
                                                          " and hh_system_info.data_type = 1 " +
                                                          " and hh_system_info.start_date <= ? " +
                                                          " and hh_system_info.end_date >= ? " +
                                                          " order by hh_system_info.disp_idx desc, hh_system_info.last_update desc, hh_system_info.last_uptime desc";

    // this constant is for defining the Rss configuration file path for windows system
    // private static final String rssConfigFilePath=File.separator+"C:\\kapil_docs\\RssOut\\rss.conf";

    // This constant is for defining the Rss config file path for Linux system
    private static final String rssConfigFilePath = File.separator + "/etc/happyhotel/rss.conf";

    public static void main(String[] args)
    {
        try
        {
            generateRss();
            System.out.println( "Rss file generated successfully" );
        }
        catch ( Exception e )
        {
            System.out.println( "Exception catched in main()!" + e.getMessage() );
            e.printStackTrace();
        }
    }

    /**
     * This method append Strings in buffer
     * 
     * @return String buffer
     * @param StringBuffer,title,link,description,category
     */

    private static StringBuffer generateRssXML(StringBuffer sbr, String title, String link, String description, String category)
    {
        sbr.append( "<channel>\n" );
        sbr.append( "<title>" + title + "</title>\n" );
        sbr.append( "<link>" + link + "</link>\n" );
        sbr.append( "<description > " + description + " </description>\n" );
        sbr.append( "<language>ja</language>\n" );
        sbr.append( "<copyright>Copyright (C) 2007-2013 ALMEX inc. All Right Reserved.</copyright>\n" );
        sbr.append( "<lastBuildDate>" + getDate() + "</lastBuildDate>\n" );
        sbr.append( "<category>" + category + "</category>\n" );

        return sbr;

    }

    /**
     * This method will generate XML file
     * 
     * @param FileName
     * @param sbr StringBuffer which is to be written in file
     * @throws Exception
     */
    private static void generateXMLFile(String FileName, StringBuffer sbr) throws Exception
    {
        try
        {
            OutputStream fout = new FileOutputStream( FileName );
            OutputStream bout = new BufferedOutputStream( fout );
            OutputStreamWriter out = new OutputStreamWriter( bout, "UTF-8" );
            out.write( sbr.toString() );
            out.flush();
            out.close();
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in generateXMLFile()=" + e.getMessage() );
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method return current date in a format eg. Fri, 01 Feb 2008 14:41:08 +0900
     * 
     * @return String
     */
    private static String getDate()
    {
        String todayStr = null;

        try
        {
            Date date = new Date();

            Locale strlocale = Locale.ENGLISH;

            SimpleDateFormat fmt = new SimpleDateFormat( "E, dd MMM yyyy HH:mm:ss Z", strlocale );

            todayStr = fmt.format( date );

        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in getDate()=" + e.getMessage() );
        }
        return todayStr;
    }

    /**
     * This method return current date in a YYYYMMDD format
     * 
     * @return int
     */
    public static int getStrDate()
    {
        Calendar c = Calendar.getInstance();
        int m = c.get( Calendar.MONTH ) + 1;
        int d = c.get( Calendar.DATE );
        String mm = Integer.toString( m );
        String dd = Integer.toString( d );
        String str = c.get( Calendar.YEAR ) + (m < 10 ? "0" + mm : mm) + (d < 10 ? "0" + dd : dd);
        int r = Integer.parseInt( str );
        return r;
    }

    /**
     * This method return current time in a HHMMDD format
     * 
     * @return int
     */
    public static int getStrTime()
    {
        Calendar c = Calendar.getInstance();

        int h = c.get( Calendar.HOUR_OF_DAY );
        int m = c.get( Calendar.MINUTE );
        int s = c.get( Calendar.SECOND );
        String hh = Integer.toString( h );
        String mm = Integer.toString( m );
        String ss = Integer.toString( s );
        String str = (h < 10 ? "0" + hh : hh) + (m < 10 ? "0" + mm : mm) + (s < 10 ? "0" + ss : ss);
        int r = Integer.parseInt( str );
        return r;
    }

    /**
     * This method make the connection with database and return connection object
     * 
     * @param FileName which is a configuration file
     * @return connection object
     * @throws Exception
     */

    private static Connection getConnection(String FileName) throws Exception
    {
        String strMYSQLDriver = "com.mysql.jdbc.Driver";
        String strDatabaseURL = null;
        String strUserName = null;
        String strPassword = null;
        Connection conn = null;
        BufferedReader bis = null;
        String[] test = new String[2];
        String record = null;

        try
        {
            // load the MYSQL JDBC Driver
            Class.forName( strMYSQLDriver );
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( FileName );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            strDatabaseURL = prop.getProperty( "database.url" );
            strUserName = prop.getProperty( "database.username" );
            strPassword = prop.getProperty( "database.password" );
            strLimitRecord = prop.getProperty( "common.output_count" );

            conn = DriverManager.getConnection( strDatabaseURL, strUserName, strPassword );
        }
        catch ( SQLException se )
        {
            System.out.println( "SQLException Occured in getNewArrivalRecord()=" + se.getMessage() );
            throw se;
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in getNewArrivalRecord()=" + e.getMessage() );
            throw e;
        }
        finally
        {
            try
            {
                if ( bis != null )
                {
                    bis.close();
                }
            }
            catch ( Exception e )
            {
            }
        }
        return conn;

    }

    /**
     * This method return the number of records based on the query
     * 
     * @param FileName which is a configuration file
     * @param strOperation indicate operation to be performed
     * @return ArrayList
     * @throws Exception
     */
    private static ArrayList<Object> getRecord(String FileName, String strOperation) throws Exception
    {

        // System.out.println("entered getRecord=");
        ArrayList<Object> arrRecord = new ArrayList<Object>();
        Connection conn = null;
        Statement stmt = null;// connection object
        PreparedStatement pstmt = null; // statement object
        ResultSet rs = null; // result set object
        HotelInformation hotelObj = null;
        String strHotelName = null;
        try
        {
            conn = getConnection( FileName );
            // getting data for New Open Hotel
            if ( strOperation.equalsIgnoreCase( "open" ) )
            {
                int oldDate = getStrDate() - 10000;
                strNewOpen = strNewOpen + " LIMIT " + strLimitRecord;
                pstmt = conn.prepareStatement( strNewOpen );
                pstmt.setInt( 1, oldDate );
                pstmt.setInt( 2, getStrDate() );
                rs = pstmt.executeQuery();
                while( rs.next() )
                {
                    hotelObj = new HotelInformation();
                    hotelObj.setOpen_date( rs.getInt( "open_date" ) );
                    hotelObj.setRenewal_date( rs.getInt( "renewal_date" ) );
                    hotelObj.setId( rs.getInt( "id" ) );
                    strHotelName = rs.getString( "name" );
                    if ( strHotelName.contains( "&" ) )
                    {
                        strHotelName = strHotelName.replaceAll( "&", "&amp;" );
                    }
                    hotelObj.setName( strHotelName );
                    hotelObj.setPref_name( rs.getString( "pref_name" ) );
                    hotelObj.setAddress1( rs.getString( "address1" ) );
                    arrRecord.add( hotelObj );
                }

            }
            else if ( strOperation.equalsIgnoreCase( "arrival" ) )
            {
                stmt = conn.createStatement();
                strNewArrival = strNewArrival + " LIMIT " + strLimitRecord;
                rs = stmt.executeQuery( strNewArrival );
                while( rs.next() )
                {
                    hotelObj = new HotelInformation();
                    hotelObj.setEdit_id( rs.getInt( "edit_id" ) );
                    hotelObj.setId( rs.getInt( "id" ) );
                    strHotelName = rs.getString( "name" );
                    if ( strHotelName.contains( "&" ) )
                    {
                        strHotelName = strHotelName.replaceAll( "&", "&amp;" );
                    }
                    hotelObj.setName( strHotelName );
                    hotelObj.setPref_name( rs.getString( "pref_name" ) );
                    hotelObj.setAddress1( rs.getString( "address1" ) );
                    hotelObj.setInput_date( rs.getInt( "input_date" ) );
                    arrRecord.add( hotelObj );

                }

            }
            else if ( strOperation.equalsIgnoreCase( "kuchikomi" ) )
            {
                strKuchiKomi = strKuchiKomi + " LIMIT " + strLimitRecord;
                stmt = conn.createStatement();
                rs = stmt.executeQuery( strKuchiKomi );
                while( rs.next() )
                {
                    hotelObj = new HotelInformation();
                    hotelObj.setContribution_date( rs.getInt( "contribute_date" ) );
                    hotelObj.setUser_name( rs.getString( "user_name" ) );
                    hotelObj.setMessage( rs.getString( "message" ) );
                    hotelObj.setId( rs.getInt( "id" ) );
                    strHotelName = rs.getString( "name" );
                    if ( strHotelName.contains( "&" ) )
                    {

                        strHotelName = strHotelName.replaceAll( "&", "&amp;" );

                    }
                    hotelObj.setName( strHotelName );
                    hotelObj.setPref_name( rs.getString( "pref_name" ) );
                    hotelObj.setAddress1( rs.getString( "address1" ) );
                    arrRecord.add( hotelObj );
                }
            }
            else if ( strOperation.equalsIgnoreCase( "newmessage" ) )
            {
                strNewMessage = strNewMessage + " LIMIT " + strLimitRecord;
                pstmt = conn.prepareStatement( strNewMessage );
                pstmt.setInt( 1, getStrDate() );
                pstmt.setInt( 2, getStrDate() );
                pstmt.setInt( 3, getStrTime() );
                pstmt.setInt( 4, DateEdit.addMonth( getStrDate(), LAST_MONTH ) );
                pstmt.setInt( 5, getStrDate() );
                pstmt.setInt( 6, getStrDate() );
                pstmt.setInt( 7, getStrTime() );
                rs = pstmt.executeQuery();
                while( rs.next() )
                {
                    hotelObj = new HotelInformation();
                    hotelObj.setStart_date( rs.getInt( "start_date" ) );
                    hotelObj.setNewMessage( rs.getString( "disp_message" ) );
                    hotelObj.setId( rs.getInt( "id" ) );
                    strHotelName = rs.getString( "name" );
                    if ( strHotelName.contains( "&" ) )
                    {
                        strHotelName = strHotelName.replaceAll( "&", "&amp;" );
                    }
                    hotelObj.setName( strHotelName );
                    hotelObj.setPref_name( rs.getString( "pref_name" ) );
                    hotelObj.setAddress1( rs.getString( "address1" ) );
                    arrRecord.add( hotelObj );
                }
            }

            else if ( strOperation.equalsIgnoreCase( "topics" ) )
            {
                strTopics = strTopics + " LIMIT " + strLimitRecord;
                pstmt = conn.prepareStatement( strTopics );
                pstmt.setInt( 1, getStrDate() );
                pstmt.setInt( 2, getStrDate() );
                rs = pstmt.executeQuery();
                while( rs.next() )
                {
                    hotelObj = new HotelInformation();
                    hotelObj.setStart_date( rs.getInt( "start_date" ) );
                    hotelObj.setTitle( rs.getString( "title" ) );
                    hotelObj.setLink( rs.getString( "link_url_in" ) );
                    arrRecord.add( hotelObj );
                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in getRecord()=" + e.getMessage() );
            throw e;
        }
        finally
        {
            try
            {

                if ( pstmt != null )
                {
                    pstmt.close();
                }
                if ( rs != null )
                {
                    rs.close();
                }
                if ( stmt != null )
                {
                    stmt.close();
                }
                if ( conn != null )
                {
                    conn.close();
                }
            }
            catch ( Exception e )
            {
            }
        }
        return arrRecord;

    }

    /**
     * This method will read the configuration file
     * 
     * @param strFileName
     * @return BufferedReader
     * @throws Exception
     */
    private static BufferedReader readConfigFile(String strFileName) throws Exception
    {
        BufferedReader bis = null;
        try
        {
            File f = new File( strFileName );
            FileReader fis = new FileReader( f );
            bis = new BufferedReader( fis );
        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in readConfigFile()=" + e.getMessage() );
            throw e;
        }
        return bis;
    }

    /**
     * This method will return the key value pair
     * 
     * @param strRecord
     * @return String []
     */
    private static String[] getKeyValue(String strRecord)
    {
        String[] strKeyValue = new String[2];
        strKeyValue[0] = strRecord.substring( 0, strRecord.indexOf( '=' ) );
        strKeyValue[1] = strRecord.substring( strRecord.indexOf( '=' ) + 1 );
        return strKeyValue;

    }

    /**
     * This method return the division string concatenated with input_date
     * 
     * @param edit_id
     * @param input_date
     * @return String
     */
    private static String getDivisionArrival(int edit_id, int input_date)
    {
        String div1 = "ラブホ 新着 - ";
        String div2 = "ラブホ 情報追加 - ";
        int yrInput = (input_date / 10000);
        int mnInput = (input_date / 100) % 100;
        String yrI = Integer.toString( yrInput );
        String mnI = Integer.toString( mnInput );
        if ( edit_id == 101 )
            return div1 + yrI + "." + (mnInput < 10 ? "0" + mnI : mnI);
        else
            return div2 + yrI + "." + (mnInput < 10 ? "0" + mnI : mnI);
    }

    /**
     * 
     * @param open_date
     * @param renewal_date
     * @return String
     */
    private static String getDivisionOpen(int open_date, int renewal_date)
    {
        String div1 = "新築オープン - ";
        String div2 = "リニューアル - ";
        int yrOpen = (open_date / 10000);
        int mnOpen = (open_date / 100) % 100;
        String yrO = Integer.toString( yrOpen );
        String mnO = Integer.toString( mnOpen );
        int yrRenewal = (renewal_date / 10000);
        int mnRenewal = (renewal_date / 100) % 100;
        String yrN = Integer.toString( yrRenewal );
        String mnN = Integer.toString( mnRenewal );

        if ( open_date == renewal_date )
            return div1 + yrO + "." + (mnOpen < 10 ? "0" + mnO : mnO);

        else
            return div2 + yrN + "." + (mnRenewal < 10 ? "0" + mnN : mnN);
    }

    /**
     * This method generates the xml file for each operation
     * 
     * @throws Exception
     */
    private static void generateRss() throws Exception
    {
        String str1 = "<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8" + "\"?>";
        String str2 = "<rss version=" + "\"2.0\"" + ">";
        String record = null;
        String NewOpenFileName = null;
        String NewlyArrivalFileName = null;
        String KuchiKomiFileName = null;
        String NewMessageFileName = null;
        String TopicsFileName = null;
        BufferedReader bis = null;
        String[] test = new String[2];
        try
        {
            bis = readConfigFile( rssConfigFilePath );
            while( (record = bis.readLine()) != null )
            {
                test = getKeyValue( record );

                if ( test[0].toString().contains( "newopen_rss" ) )
                {
                    StringBuffer sbrOpen = new StringBuffer();
                    NewOpenFileName = File.separator + test[1].toString();
                    sbrOpen.append( str1 + "\n" );
                    sbrOpen.append( str2 + "\n" );
                    String titleO = "新規オープンホテル | ハッピー・ホテル";
                    String linkO = Url.getUrl() + "/others/bn_newopen.jsp";
                    String descriptionO = "新規オープンホテル（新規オープン・リニューアルが行われたホテル）の一覧";
                    String categoryO = "新規オープンホテル";
                    String LinkO1 = Url.getUrl() + "/detail/detail_top.jsp?id=";

                    StringBuffer sbr1 = generateRssXML( sbrOpen, titleO, linkO, descriptionO, categoryO );

                    ArrayList<Object> arrHotelObj = getRecord( rssConfigFilePath, "open" );
                    if ( arrHotelObj != null && arrHotelObj.size() > 0 )
                    {
                        for( int i = 0 ; i < arrHotelObj.size() ; i++ )
                        {
                            HotelInformation hotelObj = (HotelInformation)arrHotelObj.get( i );
                            strRss.append( "<item>\n" );
                            strRss.append( "<title>" + getDivisionOpen( hotelObj.getOpen_date(), hotelObj.getRenewal_date() ) + " - " + hotelObj.getName() + "(" + hotelObj.getPref_name() + hotelObj.getAddress1() + ")</title>\n" );
                            strRss.append( "<link>" + LinkO1 + hotelObj.getId() + "</link>\n" );
                            strRss.append( "<pubDate>" + getDate() + "</pubDate>\n" );
                            strRss.append( "</item>\n" );
                        }
                        sbr1.append( strRss.toString() );
                        sbr1.append( " </channel>\n" );
                        sbr1.append( "</rss>\n" );
                        generateXMLFile( NewOpenFileName, sbr1 );
                        sbr1 = null;
                    }
                }

                else if ( test[0].toString().contains( "newarrival_rss" ) )
                {
                    strRss.delete( 0, strRss.length() );
                    StringBuffer sbrArrival = new StringBuffer();
                    NewlyArrivalFileName = File.separator + test[1].toString();
                    sbrArrival.append( str1 + "\n" );
                    sbrArrival.append( str2 + "\n" );
                    String title = "新着ホテル | ハッピー・ホテル";
                    String link = Url.getUrl() + "/others/bn_newarrival.jsp";
                    String description = "新着ホテル（情報の更新が行われたホテル）の一覧";
                    String category = "新着ホテル";
                    String Link1 = Url.getUrl() + "/detail/detail_top.jsp?id=";
                    StringBuffer sbr2 = generateRssXML( sbrArrival, title, link, description, category );

                    ArrayList<Object> arrHotelObj = getRecord( rssConfigFilePath, "arrival" );
                    if ( arrHotelObj != null && arrHotelObj.size() > 0 )
                    {
                        for( int i = 0 ; i < arrHotelObj.size() ; i++ )
                        {
                            HotelInformation hotelObj1 = (HotelInformation)arrHotelObj.get( i );
                            strRss.append( "<item>\n" );
                            strRss.append( "<title>" + getDivisionArrival( hotelObj1.getEdit_id(), hotelObj1.getInput_date() ) + " - " + hotelObj1.getName() + "(" + hotelObj1.getPref_name() + hotelObj1.getAddress1() + ")</title>\n" );
                            strRss.append( "<link>" + Link1 + hotelObj1.getId() + "</link>\n" );
                            strRss.append( "<pubDate>" + getDate() + "</pubDate>\n" );
                            strRss.append( "</item>\n" );
                        }
                        sbr2.append( strRss.toString() );
                        sbr2.append( " </channel>\n" );
                        sbr2.append( "</rss>\n" );
                        generateXMLFile( NewlyArrivalFileName, sbr2 );
                    }
                }
                else if ( test[0].toString().contains( "kuchikomi_rss" ) )
                {
                    strRss.delete( 0, strRss.length() );
                    StringBuffer sbrKuchiKomi = new StringBuffer();
                    KuchiKomiFileName = File.separator + test[1].toString();
                    sbrKuchiKomi.append( str1 + "\n" );
                    sbrKuchiKomi.append( str2 + "\n" );
                    String title = "新着クチコミ | ハッピー・ホテル";
                    String link = Url.getUrl() + "/others/newbuzz.jsp";
                    String description = "ラブホテル・ラブホの最新クチコミ一覧";
                    String category = "最新クチコミ";
                    String Link1 = Url.getUrl() + "/detail/detail_buzz.jsp?id=";
                    StringBuffer sbr3 = generateRssXML( sbrKuchiKomi, title, link, description, category );

                    ArrayList<Object> arrHotelObj = getRecord( rssConfigFilePath, "kuchikomi" );
                    if ( arrHotelObj != null && arrHotelObj.size() > 0 )
                    {
                        for( int i = 0 ; i < arrHotelObj.size() ; i++ )
                        {
                            HotelInformation hotelObj2 = (HotelInformation)arrHotelObj.get( i );
                            strRss.append( "<item>\n" );
                            strRss.append( "<title>" + hotelObj2.getContribution_date() + " " + hotelObj2.getName() + "(" + hotelObj2.getPref_name() + hotelObj2.getAddress1() + ") " + hotelObj2.getUser_name() + "</title>\n" );
                            strRss.append( "<link>" + Link1 + hotelObj2.getId() + "</link>\n" );
                            strRss.append( "<description>" + hotelObj2.getMessage() + "</description>\n" );
                            strRss.append( "<pubDate>" + getDate() + "</pubDate>\n" );
                            strRss.append( "</item>\n" );
                        }
                        sbr3.append( strRss.toString() );
                        sbr3.append( " </channel>\n" );
                        sbr3.append( "</rss>\n" );
                        generateXMLFile( KuchiKomiFileName, sbr3 );
                    }
                }

                else if ( test[0].toString().contains( "newmessage_rss" ) )
                {
                    strRss.delete( 0, strRss.length() );
                    StringBuffer sbrNewmessage = new StringBuffer();
                    NewMessageFileName = File.separator + test[1].toString();
                    sbrNewmessage.append( str1 + "\n" );
                    sbrNewmessage.append( str2 + "\n" );
                    String title = "ホテル最新情報 | ハッピー・ホテル";
                    String link = Url.getUrl() + "/others/newmessage.jsp";
                    String description = "ラブホテル・ラブホの最新情報一覧";
                    String category = "ホテル最新情報";
                    String Link1 = Url.getUrl() + "/detail/detail_top.jsp?id=";
                    StringBuffer sbr4 = generateRssXML( sbrNewmessage, title, link, description, category );

                    ArrayList<Object> arrHotelObj = getRecord( rssConfigFilePath, "newmessage" );
                    if ( arrHotelObj != null && arrHotelObj.size() > 0 )
                    {
                        for( int i = 0 ; i < arrHotelObj.size() ; i++ )
                        {
                            HotelInformation hotelObj2 = (HotelInformation)arrHotelObj.get( i );
                            strRss.append( "<item>\n" );
                            strRss.append( "<title> ラブホ最新情報 " + hotelObj2.getStart_date() + " " + hotelObj2.getName() + "(" + hotelObj2.getPref_name() + hotelObj2.getAddress1() + ") </title>\n" );
                            strRss.append( "<link>" + Link1 + hotelObj2.getId() + "</link>\n" );
                            strRss.append( "<description>" + hotelObj2.getNewMessage() + "</description>\n" );
                            strRss.append( "<pubDate>" + getDate() + "</pubDate>\n" );
                            strRss.append( "</item>\n" );
                        }
                        sbr4.append( strRss.toString() );
                        sbr4.append( " </channel>\n" );
                        sbr4.append( "</rss>\n" );
                        generateXMLFile( NewMessageFileName, sbr4 );
                    }
                }
                else if ( test[0].toString().contains( "topics_rss" ) )
                {
                    strRss.delete( 0, strRss.length() );
                    StringBuffer sbrTopics = new StringBuffer();
                    TopicsFileName = File.separator + test[1].toString();
                    sbrTopics.append( str1 + "\n" );
                    sbrTopics.append( str2 + "\n" );
                    String title = "トピックス | ハッピー・ホテル";
                    String link = Url.getUrl() + "/others/topics.jsp";
                    String description = "ハッピーホテルのトピックス一覧";
                    String category = "トピックス";
                    // String Link1="http://happyhotel.jp/othes/info_mapcode.jsp";
                    StringBuffer sbr2 = generateRssXML( sbrTopics, title, link, description, category );

                    ArrayList<Object> arrHotelObj = getRecord( rssConfigFilePath, "topics" );
                    if ( arrHotelObj != null && arrHotelObj.size() > 0 )
                    {
                        for( int i = 0 ; i < arrHotelObj.size() ; i++ )
                        {
                            HotelInformation hotelObj1 = (HotelInformation)arrHotelObj.get( i );
                            strRss.append( "<item>\n" );
                            strRss.append( "<title> トピックス" + hotelObj1.getStart_date() + hotelObj1.getTitle() + "</title>\n" );
                            strRss.append( "<link>" + hotelObj1.getLink() + "</link>\n" );
                            strRss.append( "<description>" + hotelObj1.getTitle() + "</description>\n" );
                            strRss.append( "<pubDate>" + getDate() + "</pubDate>\n" );
                            strRss.append( "</item>\n" );
                        }
                        sbr2.append( strRss.toString() );
                        sbr2.append( " </channel>\n" );
                        sbr2.append( "</rss>\n" );
                        generateXMLFile( TopicsFileName, sbr2 );
                    }
                }

            }

        }
        catch ( Exception e )
        {
            System.out.println( "Exception Occured in genRssFile():" + e.getMessage() );
            throw e;
        }
        finally
        {
            // if the file is opened , make sure we close it
            if ( bis != null )
            {
                try
                {
                    bis.close();
                }
                catch ( IOException ioe )
                {
                }
            }
        }
    }

}
