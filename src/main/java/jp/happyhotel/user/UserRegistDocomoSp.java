/*
 * @(#)UserRegist.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ユーザマイメニュー登録・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SelectCookie;
import jp.happyhotel.data.DataUserSp;

/**
 * 有料ユーザマイメニュー登録・更新クラス ユーザのマイメニュー登録する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/24
 */
public class UserRegistDocomoSp implements Serializable
{
    private static final long serialVersionUID = 9145975676134243559L;

    /**
     * データを初期化します。
     */
    public UserRegistDocomoSp()
    {

    }

    /**
     * 無料入会処理
     * 
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean registFree(HttpServletRequest request)
    {
        final int REGIST_POINT = 1000001; // 有料入会ポイントのポイントコード
        int loop = 0;
        boolean ret;
        boolean retSp;
        boolean retFirst;
        int nowDate = 0;
        int nowTime = 0;
        String suid = "";
        String sStkn = "";
        UserBasicInfo ubi;
        UserPointPay upp;
        UserPoint up;
        String orderNo = "";

        DataUserSp dus;
        Cookie cookieSp;

        cookieSp = SelectCookie.getCookie( request, "hhihappy" );
        sStkn = request.getParameter( "sStkn" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        dus = new DataUserSp();
        ret = false;
        retSp = false;
        retFirst = false;

        if ( orderNo == null )
        {
            orderNo = "";
        }

        if ( cookieSp != null )
        {
            suid = cookieSp.getValue();
        }
        else
        {
            if ( request.getAttribute( "SUID" ) != null )
            {
                suid = (String)request.getAttribute( "SUID" );
                Logging.info( "Session.getAttribute('SUID'):" + suid );
            }
        }
        if ( sStkn == null )
        {
            sStkn = "";
        }

        // マイメニュー登録処理(SUIDが取得できたら)
        if ( suid.equals( "" ) == false )
        {
            try
            {
                ret = dus.getDataBySuid( suid );
                dus.setOpenId( suid );
                dus.setCarrierKind( DataUserSp.DOCOMO );
                dus.setFreeMymenu( 1 );
                dus.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dus.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dus.setToken( sStkn );
                if ( orderNo.equals( "" ) == false )
                {
                    dus.setOrderNo( orderNo );
                }

                // insert、updateの判断を行う
                if ( ret == false )
                {
                    dus.setUserId( suid );
                    // SUIDでユーザIDを仮登録
                    ret = dus.insertData();
                    if ( ret == false )
                    {
                        dus.setDelFlag( 0 );
                        ret = dus.updateData( suid );
                    }
                }
                else
                {
                    ret = dus.updateData( dus.getUserId() );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegistSp.registFree] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        else
        {
            Logging.error( "[UserRegist.registFree]:SUID = " );
        }
        return(ret);
    }

    /**
     * 有料入会処理
     * 
     * @param request HTTPリクエスト
     * @param kind (0:登録、1:同月内入会)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean registPay(HttpServletRequest request, int kind)
    {
        final int REGIST_POINT = 1000001; // 有料入会ポイントのポイントコード
        boolean ret;
        boolean retSp;
        boolean retFirst;
        int nowDate = 0;
        int nowTime = 0;
        String suid = "";
        String sStkn = "";
        UserBasicInfo ubi;
        UserPointPay upp;
        UserPoint up;

        DataUserSp dus;
        Cookie cookieSp;
        String orderNo = "";

        cookieSp = SelectCookie.getCookie( request, "hhihappy" );
        sStkn = request.getParameter( "sStkn" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        dus = new DataUserSp();

        ret = false;
        retSp = false;
        retFirst = false;

        if ( orderNo == null )
        {
            orderNo = "";
        }
        if ( cookieSp != null )
        {
            suid = cookieSp.getValue();
        }
        else
        {
            if ( request.getAttribute( "SUID" ) != null )
            {
                suid = (String)request.getAttribute( "SUID" );
                Logging.info( "Session.getAttribute('SUID'):" + suid );
            }
        }

        retSp = dus.getDataBySuid( suid );
        // マイメニュー登録処理
        if ( retSp != false )
        {
            // マイメニュー登録時はｽﾃｰﾀｽを1にしてhh_user_basicに書き込み
            try
            {
                ubi = new UserBasicInfo();
                // ユーザ端末番号仮領域既存データチェック
                ret = ubi.getUserBasic( dus.getUserId() );
                if ( ret != false )
                {
                    ubi.getUserInfo().setRegistStatusOld( ubi.getUserInfo().getRegistStatus() );
                    // 無料会員登録でユーザID、パスワードが登録されていれば、regist_status_pay=9、regist_status=9にする
                    if ( ubi.getUserInfo().getRegistStatus() >= 2 )
                    {
                        ubi.getUserInfo().setRegistStatus( 9 );
                        ubi.getUserInfo().setRegistStatusPay( 9 );
                    }
                    else
                    {
                        ubi.getUserInfo().setRegistStatusPay( 1 );
                    }
                    ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                    // 登録に成功した場合、
                    if ( ret != false )
                    {
                        upp = new UserPointPay();
                        retFirst = upp.setRegistPoint( ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );
                        // 無料会員登録を終えていないユーザに無料ポイントを付与する
                        if ( ubi.getUserInfo().getRegistStatusOld() == 2 || ubi.getUserInfo().getRegistStatusOld() == 3 )
                        {
                            up = new UserPoint();
                            up.setPointJoin( ubi.getUserInfo().getUserId() );
                        }
                        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                        // DataUserSpにデータを登録
                        retSp = dus.getData( ubi.getUserInfo().getUserId() );
                        dus.setOpenId( suid );
                        dus.setCarrierKind( DataUserSp.DOCOMO );
                        dus.setRegistDate( nowDate );
                        dus.setRegistTime( nowTime );
                        dus.setRegistDatePay( nowDate );
                        dus.setRegistTimePay( nowTime );
                        dus.setToken( sStkn );
                        if ( orderNo.equals( "" ) == false )
                        {
                            dus.setOrderNo( orderNo );
                        }

                        // マイルが付与されたら初回登録
                        if ( kind == 0 )
                        {
                            dus.setChargeFlag( DataUserSp.CHARGEFLAG_PAY );
                        }
                        else
                        {
                            dus.setChargeFlag( DataUserSp.CHARGEFLAG_PAY );
                        }
                        // データの取得状況に応じてインサート、アップデートを行う。
                        if ( retSp != false )
                        {
                            ret = dus.updateData( ubi.getUserInfo().getUserId() );
                        }
                        else
                        {
                            dus.setUserId( ubi.getUserInfo().getUserId() );
                            ret = dus.insertData();
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegistSp.registPay] Exception=" + e.toString() );
            }
            finally
            {
            }
        }

        return(ret);
    }

    /**
     * 退会処理
     * 
     * @param request HTTPリクエスト
     * @param kind (0:登録、1:同月内入会)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean secession(HttpServletRequest request)
    {
        final int REGIST_POINT = 1000001; // 有料入会ポイントのポイントコード
        boolean ret;
        boolean retSp;
        boolean retPay = false;
        int nowDate = 0;
        int nowTime = 0;
        String suid = "";
        String sStkn = "";
        UserBasicInfo ubi;
        DataUserSp dus;
        Cookie cookieSp;
        String orderNo = "";
        String sPric = "";

        dus = new DataUserSp();
        sStkn = request.getParameter( "sStkn" );
        cookieSp = SelectCookie.getCookie( request, "hhihappy" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        ret = false;
        retSp = false;
        sPric = request.getParameter( "sPric" );

        if ( orderNo == null )
        {
            orderNo = "";
        }
        if ( cookieSp != null )
        {
            suid = cookieSp.getValue();
        }
        else
        {
            if ( request.getAttribute( "SUID" ) != null )
            {
                suid = (String)request.getAttribute( "SUID" );
                Logging.info( "Session.getAttribute('SUID'):" + suid );
            }
        }

        if ( sStkn == null )
        {
            sStkn = "";
        }

        retSp = dus.getDataBySuid( suid );
        // マイメニュー削除処理
        if ( retSp != false )
        {
            try
            {
                ubi = new UserBasicInfo();
                // ユーザ端末番号既存データチェック
                ret = ubi.getUserBasic( dus.getUserId() );

                Logging.info( "sPric:" + sPric );
                if ( sPric.equals( "315" ) != false )
                {
                    retPay = true;
                }
                else
                {
                    retPay = false;
                }

                if ( ret != false )
                {
                    nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

                    // 有料の退会で、有料会員であればデータを削除
                    if ( retPay != false && ubi.getUserInfo().getRegistStatusPay() > 0 )
                    {
                        retPay = true;
                        // 有料入会途中かどうか
                        if ( (ubi.getUserInfo().getRegistStatusPay() == 1) && (ubi.getUserInfo().getRegistStatusOld() == 8) )
                        {
                            ret = ubi.getUserInfo().deleteData( ubi.getUserInfo().getUserId() );
                        }
                        else
                        {
                            // 非会員から有料になっていたら削除フラグを立てる
                            if ( ubi.getUserInfo().getRegistStatusOld() == 8 )
                            {
                                // ユーザ基本情報の削除フラグに1を立てる
                                ubi.getUserInfo().setDelFlag( 1 );
                            }
                            // 仮登録だったら（マイモード登録）
                            else if ( ubi.getUserInfo().getRegistStatusOld() == 1 )
                            {
                                // ユーザーIDと端末番号端末番号が同じでパスワードが入力されていなかったらそのまま
                                if ( ubi.getUserInfo().getUserId().compareTo( ubi.getUserInfo().getMobileTermNo() ) == 0 &&
                                        ubi.getUserInfo().getPasswd().compareTo( "" ) == 0 )
                                {
                                    ubi.getUserInfo().setRegistStatus( 1 );
                                }
                                // それ以外はregist_status=2に変更する
                                else
                                {
                                    ubi.getUserInfo().setRegistStatus( 2 );
                                }
                            }
                            else
                            {
                                // regist_status_oldが0のユーザーはregist_statusを変更しない
                                if ( ubi.getUserInfo().getRegistStatusOld() == 2 || ubi.getUserInfo().getRegistStatusOld() == 3 ||
                                        ubi.getUserInfo().getRegistStatusOld() == 9 )
                                {
                                    ubi.getUserInfo().setRegistStatus( ubi.getUserInfo().getRegistStatusOld() );
                                }
                            }
                            ubi.getUserInfo().setRegistStatusPay( 0 );
                            ubi.getUserInfo().setDelDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            ubi.getUserInfo().setDelTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            // 更新
                            ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                        }
                    }
                    // 無料退会で無料会員であればデータを削除
                    else if ( retPay == false && ubi.getUserInfo().getRegistStatusPay() == 0 )
                    {
                        retPay = false;
                        ubi.getUserInfo().setDelFlag( 1 );
                        ubi.getUserInfo().setDelDatePc( nowDate );
                        ubi.getUserInfo().setDelTimePc( nowTime );
                        // 更新
                        ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                    }

                    // 退会がうまくいったらSP情報も変更
                    if ( ret != false )
                    {
                        // DataUserSpにデータを登録
                        retSp = dus.getData( ubi.getUserInfo().getUserId() );

                        // 有料退会の場合
                        if ( retPay != false )
                        {
                            // 無料マイメニュー登録していない、またはいきなり有料会員だったら退会させる
                            if ( dus.getFreeMymenu() == 0 || ubi.getUserInfo().getRegistStatusOld() == 8 )
                            {
                                dus.setChargeFlag( 0 );
                                dus.setDelFlag( 1 );
                            }
                            else if ( dus.getFreeMymenu() == 1 )
                            {
                                dus.setChargeFlag( 0 );
                                dus.setDelFlag( 0 );
                            }
                            dus.setDelDatePay( nowDate );
                            dus.setDelTimePay( nowTime );
                        }
                        // 無料退会の場合
                        else
                        {
                            dus.setFreeMymenu( 0 );
                            if ( dus.getChargeFlag() == 0 )
                            {
                                dus.setDelFlag( 1 );
                            }
                            dus.setDelDate( nowDate );
                            dus.setDelTime( nowTime );
                        }
                        dus.setToken( sStkn );
                        if ( orderNo.equals( "" ) == false )
                        {
                            dus.setOrderNo( orderNo );
                        }

                        ret = dus.updateData( ubi.getUserInfo().getUserId() );
                    }

                    if ( ret == false )
                    {
                        Logging.info( "マイメニュー削除失敗" +
                                "user_id=" + ubi.getUserInfo().getUserId() +
                                "regist_status=" + ubi.getUserInfo().getRegistStatus() +
                                "term_no=" + ubi.getUserInfo().getMobileTermNo() +
                                "temp_date_mobile=" + ubi.getUserInfo().getTempDateMobile() +
                                "temp_time_mobile=" + ubi.getUserInfo().getTempTimeMobile() +
                                "regist_date_mobile=" + ubi.getUserInfo().getRegistDateMobile() +
                                "regist_time_mobile=" + ubi.getUserInfo().getRegistTimeMobile() +
                                "del_reason=" + ubi.getUserInfo().getDelReason() );
                    }
                }
                else
                {
                    Logging.info( "[UserRegistDocomoSp.secession]:" + "userBasic noData" );

                    // 有料退会の場合
                    if ( retPay != false )
                    {
                        // 無料マイメニュー登録していない、またはいきなり有料会員だったら退会させる
                        if ( dus.getFreeMymenu() == 0 || ubi.getUserInfo().getRegistStatusOld() == 8 )
                        {
                            dus.setChargeFlag( 0 );
                            dus.setDelFlag( 1 );
                        }
                        else if ( dus.getFreeMymenu() == 1 )
                        {
                            dus.setChargeFlag( 0 );
                            dus.setDelFlag( 0 );
                        }
                        dus.setDelDatePay( nowDate );
                        dus.setDelTimePay( nowTime );
                    }
                    // 無料退会の場合
                    else
                    {

                        dus.setFreeMymenu( 0 );
                        // 無料退会で、無料データの場合だけ、削除するようにする
                        if ( dus.getChargeFlag() == 0 )
                        {
                            dus.setDelFlag( 1 );
                        }
                        dus.setDelDate( nowDate );
                        dus.setDelTime( nowTime );
                    }
                    dus.setToken( sStkn );
                    if ( orderNo.equals( "" ) == false )
                    {
                        dus.setOrderNo( orderNo );
                    }
                    ret = dus.updateData( dus.getUserId() );
                }
                // 削除の場合は常にOKとする
                ret = true;
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegistSp.registPay] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }
}
