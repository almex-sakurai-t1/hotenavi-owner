/*
 * @(#)HotelRoomPicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 部屋画像出力サーブレット
 */
package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.AuAuthCheck;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.user.UserPointPay;

/**
 * レジャーホテルデモサーブレット<br>
 *
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class ActionLhfDemo extends BaseAction
{
    /**
     *
     */
    private static final long   serialVersionUID = -5186785324124494508L;
    private static final String REGIST           = "regist";
    private static final String VISIT            = "visit_bufferin";
    private static final String FIND             = "find";
    private static final String AMOUNT           = "AMOUNT";
    private RequestDispatcher   requestDispatcher;
    private DataLoginInfo_M2    dataLoginInfo_M2 = null;
    private int                 COME_POINT       = 9999999;
    // private static String BASE_URL = "http://121.101.88.177/";

    private static String       BASE_URL         =  Url.getUrl() +"/";

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // 定義
        boolean ret;
        int nPremiumPoint;
        String paramHotelId;
        String paramIdm;
        String paramMethod;
        DataUserFelica duf;
        DataMasterPoint dmp;
        UserPointPay upp;
        ServletOutputStream stream;

        upp = new UserPointPay();
        duf = new DataUserFelica();

        nPremiumPoint = 0;
        paramIdm = request.getParameter( "idm" );
        paramMethod = request.getParameter( "method" );
        paramHotelId = request.getParameter( "hotel_id" );

        if ( (paramIdm == null) || (paramIdm.compareTo( "" ) == 0) )
        {
            paramIdm = "";
        }
        if ( (paramHotelId == null) || (paramHotelId.compareTo( "" ) == 0) || CheckString.numCheck( paramHotelId ) == false )
        {
            paramHotelId = "0";
        }
        if ( paramMethod == null )
        {
            paramMethod = "";
        }

        // IDm情報取得
        if ( paramMethod.compareTo( "" ) == 0 || paramMethod.compareTo( FIND ) == 0 )
        {
            try
            {
                stream = response.getOutputStream();
                ret = duf.getUserData( paramIdm );
                if ( ret != false )
                {
                    nPremiumPoint = upp.getNowPoint( duf.getUserId(), false );
                    stream.print( "OK" );
                    stream.print( "," );
                    stream.print( Integer.toString( nPremiumPoint ) );
                }
                else
                {
                    stream.print( "NO" );
                    stream.print( "," );
                    stream.print( BASE_URL + "lhf_demo.jsp?method=regist&idm=" + paramIdm );
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionoLhfDemo FIND]Exception;" + e.toString() );
            }

        }
        // 来店時
        else if ( paramMethod.compareTo( VISIT ) == 0 )
        {
            // レスポンスをセット
            try
            {
                stream = response.getOutputStream();
                ret = duf.getUserData( paramIdm );
                if ( ret != false )
                {
                    dmp = new DataMasterPoint();
                    dmp.getData( COME_POINT );
                    // 来店ポイント加算
                    ret = upp.setPoint( duf.getUserId(), COME_POINT, Integer.parseInt( paramHotelId ), "" );
                    nPremiumPoint = upp.getNowPoint( duf.getUserId(), false );
                    if ( ret != false )
                    {
                        stream.print( "OK" );
                        stream.print( "," );
                        stream.print( Integer.toString( nPremiumPoint ) );
                    }
                    else
                    {
                        stream.print( "NG" );
                        stream.print( "," );
                        stream.print( Integer.toString( nPremiumPoint ) );
                    }
                }
                else
                {
                    stream.print( "NO" );
                    stream.print( "," );
                    stream.print( BASE_URL + "lhf_demo.jsp?method=regist&idm=" + paramIdm );
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionoLhfDemo VISIT]Exception;" + e.toString() );
            }
        }
        // methodがREGISTだったら紐付け処理
        else if ( paramMethod.compareTo( REGIST ) == 0 )
        {
            this.registUserFelica( request, response );
        }
        else if ( paramMethod.compareTo( AMOUNT ) == 0 )
        {
            try
            {
                stream = response.getOutputStream();
                stream.print( "ERROR" );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionoLhfDemo AMOUNT]Exception;" + e.toString() );
            }
        }
        else
        {
            try
            {
                stream = response.getOutputStream();
                stream.print( "ERROR" );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionoLhfDemo ELSE]Exception;" + e.toString() );
            }
        }
    }

    /* フェリカIDとユーザIDの紐付けを行う
     *
     */
    public void registUserFelica(HttpServletRequest request, HttpServletResponse response)
    {
        // 定義
        boolean ret;
        int carrierFlag;
        String paramUidLink;
        String paramAcRead;
        String paramHotelId;
        String paramIdm;
        String sendUrl;
        DataUserFelica duf;

        AuAuthCheck auCheck;
        duf = new DataUserFelica();

        ret = false;
        sendUrl = "";
        carrierFlag = UserAgent.getUserAgentType( request );
        dataLoginInfo_M2 = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );
        paramUidLink = (String)request.getAttribute( "UID-LINK" );
        paramAcRead = request.getParameter( "acread" );
        paramIdm = request.getParameter( "idm" );
        paramHotelId = request.getParameter( "hotel_id" );

        if ( (paramIdm == null) || (paramIdm.compareTo( "" ) == 0) )
        {
            paramIdm = "";
        }
        if ( (paramHotelId == null) || (paramHotelId.compareTo( "" ) == 0) || CheckString.numCheck( paramHotelId ) == false )
        {
            paramHotelId = "0";
        }

        // auだったらアクセスチケットをチェックする
        if ( (paramAcRead == null) && (carrierFlag == DataMasterUseragent.CARRIER_AU) )
        {
            try
            {
                auCheck = new AuAuthCheck();
                ret = auCheck.authCheckForClass( request, "free/mymenu/paymemberRegist.act?" + paramUidLink );
                // アクセスチケット確認の結果 falseだったらリダイレクト
                if ( ret == false )
                {
                    response.sendRedirect( auCheck.getResultData() );
                    return;
                }
                // アクセスチケット確認の結果 trueだったら情報を取得
                else
                {
                    // DataLoginInfo_M2を取得する
                    if ( auCheck.getDataLoginInfo() != null )
                    {
                        dataLoginInfo_M2 = auCheck.getDataLoginInfo();
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[ActionLhfDemo AuAuthCheck] Exception:" + e.toString() );
            }
        }

        try
        {
            // ユーザー情報の取得
            if ( dataLoginInfo_M2 != null )
            {
                // 有料会員登録途中のユーザーは有料会員登録ページへ
                if ( dataLoginInfo_M2.isPaymemberFlag() == false && dataLoginInfo_M2.isPaymemberTempFlag() != false )
                {
                    response.sendRedirect( "free/mymenu/paymemberRegist.act?" + paramUidLink );
                    return;
                }
                // 有料会員登録を行っていないユーザーは有料会員登録ページへ
                else if ( dataLoginInfo_M2.isPaymemberFlag() == false && dataLoginInfo_M2.isPaymemberTempFlag() == false )
                {
                    response.sendRedirect( "free/mymenu/paymemberRegist.act?" + paramUidLink );
                    return;
                }

            }
            else
            {
                if ( carrierFlag == DataMasterUseragent.CARRIER_SMARTPHONE )
                {
                    response.sendRedirect( "/mypage/mypage_login.jsp?idm=" + paramIdm );
                }
                else
                {
                    response.sendRedirect( "free/mymenu/paymemberRegist.act?" + paramUidLink );
                }
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionLhfDemo ] Exception:" + e.toString() );
        }

        if ( (dataLoginInfo_M2.isPaymemberFlag() != false) && (paramIdm.compareTo( "" ) != 0) && (paramIdm.length() == 16) )
        {
            ret = duf.getData( dataLoginInfo_M2.getUserId() );
            duf.setIdm( paramIdm );
            duf.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            duf.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            duf.setId( Integer.parseInt( paramHotelId ) );
            duf.setDelFlag( 0 );
            if ( ret != false )
            {
                ret = duf.updateData( dataLoginInfo_M2.getUserId() );
            }
            else
            {
                duf.setUserId( dataLoginInfo_M2.getUserId() );
                ret = duf.insertData();
            }
        }

        if ( ret != false )
        {
            sendUrl = "lhf_demo.jsp";
            // ドコモ又はソフトバンクの場合はuidLinkを追加する
            if ( (carrierFlag == DataMasterUseragent.CARRIER_DOCOMO) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
            {
                sendUrl += "?" + paramUidLink;
            }

            try
            {
                response.sendRedirect( sendUrl );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLhfDemo registUserFelica] Exception:" + e.toString() );
            }
        }
        else
        {
            sendUrl = "lhf_demo.jsp";
            try
            {
                response.sendRedirect( sendUrl );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionLhfDemo registUserFelica] Exception:" + e.toString() );
            }
        }
    }
}
