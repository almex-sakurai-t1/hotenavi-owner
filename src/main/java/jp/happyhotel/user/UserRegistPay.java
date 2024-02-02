/*
 * @(#)UserRegist.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ユーザマイメニュー登録・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.ConvertString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * 有料ユーザマイメニュー登録・更新クラス ユーザのマイメニュー登録する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/24
 */
public class UserRegistPay implements Serializable
{
    private static final long serialVersionUID = 9145975676134243559L;

    /**
     * データを初期化します。
     */
    public UserRegistPay()
    {

    }

    /**
     * ユーザ端末番号を設定する(DoCoMo用)
     * 
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setTermInfoDoCoMo(HttpServletRequest request)
    {
        final int REGIST_POINT = 1000001; // 有料入会ポイントのポイントコード
        boolean ret;
        String paramUid;
        String paramAct;
        UserBasicInfo ubi;
        UserPointPay upp;
        UserPoint up;

        // 端末番号の取得
        paramUid = request.getParameter( "uid" );
        if ( paramUid == null )
        {
            return(false);
        }
        if ( paramUid.compareTo( "" ) == 0 )
        {
            return(false);
        }

        // アクションの取得
        paramAct = request.getParameter( "act" );
        if ( paramAct == null )
        {
            return(false);
        }
        if ( paramAct.compareTo( "" ) == 0 )
        {
            return(false);
        }

        ret = false;

        // マイメニュー登録処理
        if ( paramAct.compareTo( "reg" ) == 0 )
        {
            // マイメニュー登録時はｽﾃｰﾀｽを1にしてhh_user_basicに書き込み
            try
            {
                ubi = new UserBasicInfo();

                // ユーザ端末番号仮領域既存データチェック
                ret = ubi.getUserBasicByTermno( paramUid );
                if ( ret == false )
                {
                    ubi.getUserInfo().setUserId( paramUid );
                    ubi.getUserInfo().setMobileTermNo( paramUid );
                    ubi.getUserInfo().setRegistStatusPay( 1 );
                    ubi.getUserInfo().setRegistStatusOld( 8 );
                    ubi.getUserInfo().setRegistDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    ubi.getUserInfo().setRegistTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ubi.getUserInfo().setDocomoFlag( 1 );
                    // mail_addr_mobile_Md5が登録されていない場合、端末番号のハッシュ値を登録する
                    if ( ubi.getUserInfo().getMailAddrMobileMd5() == null || ubi.getUserInfo().getMailAddrMobileMd5().compareTo( "" ) == 0 )
                    {
                        ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( paramUid ) );
                        // Logging.info( "[ActionPaymemberRegist.execute] ハッシュ値：" + ConvertString.convert2md5( termNo ) ); // test log
                    }
                    ret = ubi.getUserInfo().insertData();
                    System.out.println( "有料insert結果:" + ret );
                }
                else
                {
                    ubi.getUserInfo().setRegistStatusOld( ubi.getUserInfo().getRegistStatus() );
                    // 無料会員登録でユーザID、パスワードが登録されていれば、regist_status_pay=9、regist_status=9にする
                    if ( ubi.getUserInfo().getRegistStatusOld() >= 2 )
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
                    ubi.getUserInfo().setDocomoFlag( 1 );
                    // キャリア変更時に前の端末でのログインを防ぐため、端末番号でMD5を更新しておく(docomo対応)
                    ubi.getUserInfo().setMailAddrMobileMd5( ConvertString.convert2md5( paramUid ) );
                    ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                    // 登録に成功した場合、
                    if ( ret != false && ubi.getUserInfo().getRegistStatusOld() >= 2 )
                    {
                        upp = new UserPointPay();
                        upp.setRegistPoint( ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );
                        // 無料会員登録を終えていないユーザに無料ポイントを付与する
                        if ( ubi.getUserInfo().getRegistStatusOld() == 2 || ubi.getUserInfo().getRegistStatusOld() == 3 )
                        {
                            up = new UserPoint();
                            up.setPointJoin( ubi.getUserInfo().getUserId() );
                        }
                    }
                    System.out.println( "有料update結果:" + ret );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegistPay.setTermInfoDoCoMo] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        // マイメニュー削除処理
        else if ( paramAct.compareTo( "rel" ) == 0 )
        {
            try
            {
                ubi = new UserBasicInfo();
                // ユーザ端末番号既存データチェック
                ret = ubi.getUserBasicByTermnoNoCheck( paramUid );

                Logging.info( "[UserRegistPay.setTermInfoDoCoMo] ret:" + ret );
                if ( ret != false )
                {
                    // 有料入会途中かどうか
                    if ( (ubi.getUserInfo().getRegistStatusPay() == 1) && (ubi.getUserInfo().getRegistStatusOld() == 8) )
                    {
                        ret = ubi.getUserInfo().deleteData( ubi.getUserInfo().getUserId() );
                        Logging.info( "[UserRegistPay.setTermInfoDoCoMo] RegistStatusPay=1, RegistStatusOld = 8" );
                    }
                    else
                    {
                        // 非会員から有料になっていたら削除フラグを立てる
                        if ( ubi.getUserInfo().getRegistStatusOld() == 8 )
                        {
                            // ユーザ基本情報の削除フラグに1を立てる
                            ubi.getUserInfo().setDelFlag( 1 );
                        }
                        //
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
                        // ubi.getUserInfo().setPointPayUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                        Logging.info( "[UserRegistPay.setTermInfoDoCoMo] RegistStatusPay = 9, RegistStatusOld = " + ubi.getUserInfo().getRegistStatusOld() );
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
                    else
                    {
                        // 登録未完了の半端なデータの削除フラグを立てる
                        // ubi.deleteOddDataByTermno( paramUid, memo );

                    }
                }
                // 削除の場合は常にOKとする
                ret = true;
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegist.setTermInfoDoCoMo] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }
}
