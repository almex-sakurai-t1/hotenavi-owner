/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.others;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataUserDataIndex;
import jp.happyhotel.touch.MemberList;

/**
 * ハピホテタッチ カードレスメンバー
 * 
 * @author T.Sakurai
 * @version 1.00 2018/04/25
 */
public class Member
{
    private static final int    KIND_CARD     = 0;
    private static final int    KIND_BOTH     = 1;
    private static final int    KIND_CARDLESS = 2;
    private static final String RESULT_OK     = "OK";
    private static final String RESULT_NG     = "NG";
    private static final String CONTENT_TYPE  = "text/xml; charset=UTF-8";
    private static final String ENCODE        = "UTF-8";
    /* 本番環境 */
    private int                 errorCode     = 0;

    /**
     * ハピホテカードレスメンバー情報取得
     * 
     * @param memberId 顧客ID
     * @param response レスポンス
     * 
     */
    public void getMember(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {

        DataApHotelCustom dahc = new DataApHotelCustom();
        DataUserDataIndex dudi = new DataUserDataIndex();
        boolean ret = false;
        String memberId = request.getParameter( "memberId" );
        GenerateXmlHapiTouchGetMember gxTouch;
        ServletOutputStream stream = null;
        gxTouch = new GenerateXmlHapiTouchGetMember();

        try
        {

            stream = response.getOutputStream();
            if ( memberId != null )
            {
                if ( CheckString.numCheck( memberId ) )
                {
                    ret = true;
                    if ( dahc.getDataCustom( hotelId, memberId ) != false )
                    {
                        gxTouch.setRegistDate( dahc.getRegistDate() );
                        if ( dudi.getData( dahc.getUserId(), hotelId ) != false )
                        {
                            gxTouch.setUserId( dudi.getUserSeq() );
                        }
                        if ( dahc.getAutoFlag() == 1 )
                        {
                            gxTouch.setKind( KIND_CARDLESS );
                        }
                        else
                        {
                            gxTouch.setKind( KIND_BOTH );
                        }
                    }
                    else
                    {
                        gxTouch.setKind( KIND_CARD );
                    }
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_30801; // 顧客コード不正
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_30802; // 顧客コードパラメータなし
            }

            // xml出力クラスにノードをセット
            if ( ret != false )
            {
                // xml出力クラスに値をセット
                gxTouch.setResult( RESULT_OK );
            }
            else
            {
                // xml出力クラスに値をセット
                gxTouch.setResult( RESULT_NG );
                gxTouch.setErrorCode( errorCode );
            }

            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getMember]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getMember]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * 顧客の一覧を取得する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void getMemberList(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = false;
        MemberList ml = new MemberList();
        int memberCount = 0;
        int errorCount = 0;
        String paramMax = "0";
        paramMax = request.getParameter( "max" );
        if ( (paramMax == null) || (paramMax.equals( "" ) != false) || (CheckString.numCheck( paramMax ) == false) )
        {
            paramMax = "0";
        }
        int max = Integer.parseInt( paramMax );
        GenerateXmlHapiTouchGetMemberList gxMemberList;
        ServletOutputStream stream = null;

        gxMemberList = new GenerateXmlHapiTouchGetMemberList();

        try
        {
            if ( ml.getData( hotelId, request, response ) != false )
            {
                ret = true;
                if ( ml.getMemberCount() != 0 )
                {
                    memberCount = ml.getMemberListData().length;
                    errorCount = memberCount - ml.getMemberCount();
                    if ( max != 0 )
                    {
                        if ( memberCount > max )
                        {
                            gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30817 ); // 検索最大値を超えた
                            ml.setErrorCode( HapiTouchErrorMessage.ERR_30817 );
                            errorCount = memberCount - max;
                            memberCount = max;
                            ret = true;
                        }
                    }
                }
                if ( ret && memberCount != 0 )
                {
                    ret = ml.sendToHost( hotelId ); // 送信処理
                }
            }
            else
            {
                gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30811 );// メンバーリスト取得エラー
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getMemberList]Exception:" + e.toString() );
            e.printStackTrace();
            if ( ml.getErrorCode() != 0 )
            {
                gxMemberList.setErrorCode( ml.getErrorCode() );
            }
            else
            {
                gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30815 );// メンバーリスト取得その他エラー
            }
        }
        if ( ret )
        {
            // レスポンスをセット
            try
            {
                stream = response.getOutputStream();
                if ( errorCount == 0 )
                {
                    gxMemberList.setResult( RESULT_OK );
                    gxMemberList.setIdentifyNo( ml.getIdentifyNo() );
                    gxMemberList.setMemberCount( memberCount );
                    gxMemberList.setErrorCount( errorCount );
                }
                else
                {
                    if ( ml.getErrorCode() != 0 )
                    {
                        gxMemberList.setResult( RESULT_NG );
                        gxMemberList.setErrorCode( ml.getErrorCode() );
                    }
                    else
                    {
                        gxMemberList.setResult( RESULT_OK );
                        gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30819 );// 一部送信エラー
                    }
                    gxMemberList.setIdentifyNo( ml.getIdentifyNo() );
                    gxMemberList.setMemberCount( memberCount );
                    gxMemberList.setErrorCount( errorCount );
                }

                // XMLの出力
                String xmlOut = gxMemberList.createXml();
                ServletOutputStream out = null;

                Logging.info( xmlOut );
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch getMemberList]Exception:" + e.toString() );
            }
            finally
            {
                if ( stream != null )
                {
                    try
                    {
                        stream.close();
                    }
                    catch ( IOException e )
                    {
                        Logging.error( "[ActionHapiTouch getMemberList]Exception:" + e.toString() );
                    }
                }
            }
        }
        else
        // 送信エラー
        // 送信がなかった
        {
            // レスポンスをセット
            try
            {
                stream = response.getOutputStream();
                gxMemberList.setResult( RESULT_NG );
                if ( request.getParameter( "IdentifyNo" ) == null )
                {
                    gxMemberList.setIdentifyNo( 0 );
                }
                else
                {
                    gxMemberList.setIdentifyNo( Integer.parseInt( request.getParameter( "IdentifyNo" ) ) );
                }
                gxMemberList.setMemberCount( 0 );
                gxMemberList.setErrorCount( memberCount );
                if ( ml.getErrorCode() != 0 )
                {
                    gxMemberList.setErrorCode( ml.getErrorCode() );
                }
                else
                {
                    gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30816 );
                }

                // XMLの出力
                String xmlOut = gxMemberList.createXml();
                ServletOutputStream out = null;

                Logging.info( xmlOut );
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch getMemberList]Exception:" + e.toString() );
            }
            finally
            {
                if ( stream != null )
                {
                    try
                    {
                        stream.close();
                    }
                    catch ( IOException e )
                    {
                        Logging.error( "[ActionHapiTouch getMemberList]Exception:" + e.toString() );
                    }
                }
            }
        }
    }

    /**
     * ハピホテカードレスメンバー情報取得
     * 
     * @param customId 顧客番号
     * @param response レスポンス
     * 
     */
    public void memberCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        DataApHotelCustom dahc = new DataApHotelCustom();
        boolean ret = false;
        String memberId = request.getParameter( "memberId" );
        GenerateXmlHapiTouchMemberCancel gxTouch;
        ServletOutputStream stream = null;
        gxTouch = new GenerateXmlHapiTouchMemberCancel();

        try
        {
            stream = response.getOutputStream();
            if ( memberId != null )
            {
                if ( CheckString.numCheck( memberId ) )
                {
                    if ( dahc.getDataCustom( hotelId, memberId ) != false )
                    {
                        if ( dahc.deleteCustom( hotelId, memberId ) )
                        {
                            ret = true;
                        }
                        else
                        {
                            errorCode = HapiTouchErrorMessage.ERR_30824;// 削除エラー
                        }
                    }
                    else
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30823;// 該当顧客なし・削除済み
                    }
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_30821;// 顧客コード不正
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_30822;// 顧客コードパラメータなし
            }

            // xml出力クラスにノードをセット
            if ( ret != false )
            {
                // xml出力クラスに値をセット
                gxTouch.setResult( RESULT_OK );
            }
            else
            {
                // xml出力クラスに値をセット
                gxTouch.setResult( RESULT_NG );
                gxTouch.setErrorCode( errorCode );
            }

            // XMLの出力
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            Logging.info( xmlOut );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch memberCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch memberCancel]Exception:" + e.toString() );
                }
            }
        }
    }
}
