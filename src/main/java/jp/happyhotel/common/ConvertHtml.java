/*
 * @(#)ConvertString.java 1.00
 * 2009/07/27 Copyright (C) ALMEX Inc. 2009
 * 文字列変換クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

/**
 * html文字列変換クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2017/04/25
 */
public class ConvertHtml implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -7356965057448803600L;

    /**
     * <font>を<span style=に変換する
     * 
     * @param input 変換対象文字列
     * @return 処理結果("":失敗)
     */

    public static String convertFontToCss(String input)
    {
        String convertString = input;
        try
        {
            convertString = convertString.replace( "<font color=\"", "<span style=\"color:" );
            convertString = convertString.replace( "<font color='", "<span style='color:" );
            convertString = convertString.replace( "<font size=\"1\"", "<span style=\"font-size:60%\"" );
            convertString = convertString.replace( "<font size='1'", "<span style=\"font-size:60%\"" );
            convertString = convertString.replace( "<font size=\"2\"", "<span style=\"font-size:80%\"" );
            convertString = convertString.replace( "<font size='2'", "<span style=\"font-size:80%\"" );
            convertString = convertString.replace( "<font size=\"3\"", "<span style=\"font-size:105%\"" );
            convertString = convertString.replace( "<font size='3'", "<span style=\"font-size:105%\"" );
            convertString = convertString.replace( "<font size=\"4\"", "<span style=\"font-size:120%\"" );
            convertString = convertString.replace( "<font size='4'", "<span style=\"font-size:120%\"" );
            convertString = convertString.replace( "<font size=\"5\"", "<span style=\"font-size:150%\"" );
            convertString = convertString.replace( "<font size='5'", "<span style=\"font-size:150%\"" );
            convertString = convertString.replace( "<u>", "<span style=\"text-decoration: underline;\">" );
            convertString = convertString.replace( "</font>", "</span>" );
            convertString = convertString.replace( "</u>", "</span>" );
            convertString = convertString.replace( "http://owner.hotenavi.com", "https://owner.hotenavi.com" );
        }
        catch ( Exception e )
        {
            Logging.error( "ConvertHtml.convertFontToCss Exception=" + e.toString() );
            return("");
        }
        return(convertString);
    }

    public static boolean convertFontToCss(Connection connection, String hotelId, int dataType, int id, String loginHotelId, int loginUserId)
    {
        boolean ret = false;
        String query;
        String updateQuery;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ret = false;
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) );
        int trialDate = HotelElement.getTrialDate( hotelId );
        boolean flag_5to1 = false;

        int dispIdx = 0;
        String startDate = "";
        String endDate = "";

        query = "SELECT * FROM edit_event_info";
        query += " WHERE hotelid = ?";
        query += " AND   data_type = ?";
        query += " AND   id = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, dataType );
            prestate.setInt( 3, id );
            result = prestate.executeQuery();
            updateQuery = "";
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    updateQuery = "UPDATE edit_event_info SET ";
                    updateQuery += " title = '" + ReplaceString.SQLEscape( convertFontToCss( result.getString( "title" ) ) ) + "'";
                    for( int i = 1 ; i <= 8 ; i++ )
                    {
                        updateQuery += ",msg" + i + "_title = '" + ReplaceString.SQLEscape( convertFontToCss( result.getString( "msg" + i + "_title" ) ) ) + "'";
                        updateQuery += ",msg" + i + " = '" + ReplaceString.SQLEscape( convertFontToCss( result.getString( "msg" + i ) ) ) + "'";
                    }

                    if ( dataType == 5 )
                    {
                        startDate = result.getString( "start_date" );
                        endDate = result.getString( "end_date" );
                        dispIdx = result.getInt( "disp_idx" );
                        int start_date = Integer.parseInt( result.getString( "start_date" ).replace( "-", "" ) );
                        int end_date = Integer.parseInt( result.getString( "end_date" ).replace( "-", "" ) );
                        if ( end_date < now_date )
                        {
                            updateQuery += ",disp_flg=9";
                        }
                        else if ( start_date < trialDate )
                        {
                            // updateQuery += ",end_date='" + DateEdit.getDate( 0, DateEdit.addDay( trialDate, -1 ) ) + "'";
                            if ( result.getInt( "disp_idx" ) != -9999 )
                            {
                                flag_5to1 = true;
                            }
                        }
                    }
                    updateQuery += ",last_update=" + DateEdit.getDate( 2 );
                    updateQuery += ",last_uptime=" + DateEdit.getTime( 1 );
                    updateQuery += ",upd_hotelid='" + loginHotelId + "'";
                    updateQuery += ",upd_userid='" + loginUserId + "'";
                    updateQuery += " WHERE hotelid = ?";
                    updateQuery += " AND   data_type = ?";
                    updateQuery += " AND   id = ?";
                }
            }
            DBConnection.releaseResources( result );

            if ( !updateQuery.equals( "" ) )
            {
                DBConnection.releaseResources( prestate );
                prestate = connection.prepareStatement( updateQuery );
                prestate.setString( 1, hotelId );
                prestate.setInt( 2, dataType );
                prestate.setInt( 3, id );
                if ( prestate.executeUpdate() > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "ConvertHtml.convertFontToCss Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        if ( flag_5to1 )
        {
            convert5to1( connection, hotelId, id, startDate, endDate, dispIdx, loginHotelId, loginUserId );
        }

        return(ret);
    }

    public static void convert5to1(Connection connection, String hotelId, int id, String startDate, String endDate, int dispIdx, String loginHotelId, int loginUserId)
    {
        String query;
        String updateQuery = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean flag_5to1 = true;

        // What's New に登録がないかを調べる

        query = "SELECT * FROM edit_event_info";
        query += " WHERE hotelid = ?";
        query += " AND   data_type = 1";
        query += " AND   disp_idx = ?";
        query += " AND   start_date= ?";
        query += " AND   end_date = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, dispIdx );
            prestate.setString( 3, startDate );
            prestate.setString( 4, endDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    flag_5to1 = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "ConvertHtml.convertFontToCss Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        if ( flag_5to1 )
        {
            try
            {
                try
                {
                    query = "SELECT * FROM edit_event_info";
                    query += " WHERE hotelid = ?";
                    query += " AND   data_type = 5";
                    query += " AND   id = ?";
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, hotelId );
                    prestate.setInt( 2, id );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            updateQuery = "INSERT edit_event_info SET";
                            updateQuery += " hotelid = ?";
                            updateQuery += ",data_type = 1";
                            updateQuery += ",disp_flg = " + result.getInt( "disp_flg" );
                            updateQuery += ",disp_idx =" + dispIdx;
                            updateQuery += ",start_date ='" + startDate + "'";
                            updateQuery += ",end_date ='" + endDate + "'";
                            updateQuery += ",title ='" + ReplaceString.SQLEscape( result.getString( "title" ) ) + "'";
                            updateQuery += ",title_color ='" + ReplaceString.SQLEscape( result.getString( "title_color" ) ) + "'";
                            for( int i = 1 ; i <= 8 ; i++ )
                            {
                                updateQuery += ",msg" + i + "_title ='" + ReplaceString.SQLEscape( result.getString( "msg" + i + "_title" ) ) + "'";
                                updateQuery += ",msg" + i + "_title_color ='" + ReplaceString.SQLEscape( result.getString( "msg" + i + "_title_color" ) ) + "'";
                                updateQuery += ",msg" + i + " ='" + ReplaceString.SQLEscape( result.getString( "msg" + i ) ) + "'";
                            }
                            updateQuery += ",member_only =" + result.getInt( "member_only" );
                            updateQuery += ",add_date=" + DateEdit.getDate( 2 );
                            updateQuery += ",add_time=" + DateEdit.getTime( 1 );
                            updateQuery += ",add_hotelid='" + loginHotelId + "'";
                            updateQuery += ",add_userid='" + loginUserId + "'";
                            updateQuery += ",last_update=" + DateEdit.getDate( 2 );
                            updateQuery += ",last_uptime=" + DateEdit.getTime( 1 );
                            updateQuery += ",upd_hotelid='" + loginHotelId + "'";
                            updateQuery += ",upd_userid='" + loginUserId + "'";
                            updateQuery += ",start_time =" + result.getInt( "start_time" );
                            updateQuery += ",end_time =" + result.getInt( "end_time" );
                            updateQuery += ",smart_flg =" + result.getInt( "smart_flg" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "ConvertHtml.convertFontToCss Exception=" + e.toString() );
                }
                finally
                {
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }

                prestate = connection.prepareStatement( updateQuery );
                prestate.setString( 1, hotelId );
                prestate.executeUpdate();
            }
            catch ( Exception e )
            {
                Logging.error( "ConvertHtml.convertFontToCss Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( prestate );
            }
        }
        Logging.info( "ConvertHtml.convertFontToCss query=" + updateQuery + "," + flag_5to1 );
    }

    public static String convertUrl(String hotelId, HttpServletRequest request)
    {
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        url = url.replace( uri, "" );
        url = url.replace( "http://", "https://" );

        uri = uri.replace( "/i/", "/" );
        uri = uri.replace( "/j/", "/" );
        uri = uri.replace( "/ez/", "/" );
        uri = uri.replace( "/smart/", "/" );
        uri = uri.replace( "/" + hotelId + "/", "" );
        String contents = uri;
        if ( contents.length() > 0 )
        {
            // 拡張子削除
            int l = contents.lastIndexOf( "." );
            if ( l > 0 )
            {
                contents = contents.substring( 0, l );
            }
            int m = contents.indexOf( "member" );
            if ( m == 0 && !contents.equals( "memberonly" ) )
            {
                contents = contents.substring( 0, 6 ) + "/" + contents.substring( 6 );
            }
            // roominfo は room に変換
            if ( contents.equals( "roominfo" ) )
            {
                contents = "room";
            }
            // roomdetail は room に変換
            if ( contents.equals( "roomdetail" ) )
            {
                contents = "room";
            }
            // priceinfo は room に変換
            if ( contents.equals( "priceinfo" ) )
            {
                contents = "room";
            }

            // search は トップページへ
            if ( contents.equals( "search" ) )
            {
                contents = "";
            }
            if ( contents.equals( "index" ) )
            {
                contents = "";
            }
            if ( contents.equals( "mailmagazineregist" ) )
            {
                contents = "mailmagazine/registration";
            }
            if ( contents.equals( "membermagazineregist" ) )
            {
                contents = "mailmagazine/registration";
            }
            if ( uri.indexOf( "www.dev" ) != -1 )
            {
                contents = contents.replace( "original", "" );
            }
            if ( contents.equals( "memqr" ) )
            {
                contents = "memberqr";
            }
            if ( contents.equals( "memqrregist" ) )
            {
                contents = "memberqr/register";
            }
            if ( contents.indexOf( "memqrmailmagazine" ) != -1 )
            {
                contents = "memberqr/sendmail";
            }
        }

        url = url + "/" + hotelId + "/" + contents;
        if ( queryString != null )
        {
            url = url + "?" + queryString.replace( "&amp;", "&" );
        }
        return(url);
    }

}
