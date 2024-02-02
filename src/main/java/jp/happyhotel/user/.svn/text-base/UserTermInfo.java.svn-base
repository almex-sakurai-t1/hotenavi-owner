/*
 * @(#)UserTermInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ端末情報取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;
import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * ユーザ端末情報取得・更新クラス。 ユーザの端末情報を取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserTermInfo implements Serializable
{
    /**
	 *
	 */
    private static final long   serialVersionUID = 442143587628568787L;

    private DataMasterUseragent userTerm;

    /**
     * データを初期化します。
     */
    public UserTermInfo()
    {

    }

    /** ユーザ端末情報取得 **/
    public DataMasterUseragent getTerm()
    {
        return(userTerm);
    }

    /**
     * 端末情報を取得する
     * 
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getTermInfo(HttpServletRequest request)
    {
        boolean ret;
        String query;
        String userAgent;
        String cutAgent;
        Connection connection = null;
        PreparedStatement prestate = null;

        userAgent = request.getHeader( "user-agent" );
        if ( userAgent == null )
        {
            return(false);
        }

        // ユーザエージェントをカットする
        cutAgent = getCutUserAgent( userAgent );

        query = "SELECT * FROM hh_master_useragent";

        if ( userAgent.compareTo( "" ) != 0 )
        {
            query = query + " WHERE useragent LIKE ?";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userAgent.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, cutAgent + "%" );
            }

            ret = getTermInfoSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserTermInfo.getMyHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 端末情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getTermInfoSub(PreparedStatement prestate)
    {
        boolean ret;
        ResultSet result = null;

        ret = false;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                userTerm = new DataMasterUseragent();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // ユーザ情報の取得
                    this.userTerm.setData( result );
                }

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserTermInfo.getTermInfoSub] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(ret);
    }

    /**
     * ユーザエージェント切出処理
     * 
     * @param userAgent ユーザエージェント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private String getCutUserAgent(String userAgent)
    {
        int i;
        int count;
        int cutStart;
        StringBuffer cutOrgData;
        StringBuffer cutData;

        count = 0;
        cutOrgData = new StringBuffer( userAgent );
        cutData = new StringBuffer();

        // DoCoMo用
        if ( userAgent.indexOf( "DoCoMo" ) >= 0 )
        {
            // バージョンチェック
            if ( userAgent.indexOf( "1.0" ) >= 0 )
            {
                // ユーザエージェントの切出（"/"を３個検出で終了）
                for( i = 0 ; i < cutOrgData.length() ; i++ )
                {
                    if ( cutOrgData.charAt( i ) == '/' )
                    {
                        count++;
                        if ( count >= 3 )
                        {
                            break;
                        }
                    }
                    cutData.append( cutOrgData.charAt( i ) );
                }
            }
            else if ( userAgent.indexOf( "2.0" ) >= 0 )
            {
                // ユーザエージェントの切出（"("を検出で終了）
                for( i = 0 ; i < cutOrgData.length() ; i++ )
                {
                    if ( cutOrgData.charAt( i ) == '(' )
                    {
                        break;
                    }
                    cutData.append( cutOrgData.charAt( i ) );
                }
            }
        }
        // SoftBank用
        else if ( userAgent.indexOf( "SoftBank" ) >= 0 || userAgent.indexOf( "Vodafone" ) >= 0 || userAgent.indexOf( "J-PHONE" ) >= 0 )
        {
            // ユーザエージェントの切出（"/"を３個検出で終了）
            for( i = 0 ; i < cutOrgData.length() ; i++ )
            {
                if ( cutOrgData.charAt( i ) == '/' || cutOrgData.charAt( i ) == ' ' )
                {
                    count++;
                    if ( count >= 3 && userAgent.indexOf( "J-PHONE" ) >= 0 )
                    {
                        // J-PHONEに関しては3つまで
                        break;
                    }
                    if ( count >= 4 )
                    {
                        // その他は4つまで
                        break;
                    }
                }
                cutData.append( cutOrgData.charAt( i ) );
            }
        }
        // au用
        else if ( userAgent.indexOf( "KDDI" ) >= 0 || userAgent.indexOf( "UP.Browser" ) >= 0 )
        {
            // ユーザエージェントの切出（" "を検出で終了）
            for( i = 0 ; i < cutOrgData.length() ; i++ )
            {
                if ( cutOrgData.charAt( i ) == ' ' )
                {
                    break;
                }
                cutData.append( cutOrgData.charAt( i ) );
            }
            // '-'を検出しデバイスIDを取得する
            cutStart = cutData.indexOf( "-" );
            if ( cutStart >= 0 )
            {
                cutData = new StringBuffer( cutData.substring( cutStart + 1 ) );
            }
        }
        else
        {
            cutData = cutOrgData;
        }

        return(cutData.toString());
    }

}
