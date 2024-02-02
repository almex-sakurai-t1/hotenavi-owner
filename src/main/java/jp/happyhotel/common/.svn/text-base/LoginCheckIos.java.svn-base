/**
 *
 */
package jp.happyhotel.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataApUuid;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataUserBasic;

/**
 * @author miura-s2
 * 
 */
public class LoginCheckIos extends LoginCheck
{
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            Logging.info( "LoginCheckIos.execute start" );

            messageUrlPremium = "premium";

            ParamCheck( request );

            // 非会員
            if ( uli.isMemberFlag() == false )
            {
                SetNonMember();
            }

            // 会員
            else
            {
                // DB情報更新
                UserInfoUpdate();

                // 会員ステータスチェック
                UserCheck();
            }

            String xmlOut = CreateResponse();
            Logging.info( xmlOut );
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );
        }
        catch ( Exception exception )
        {
            Logging.error( "[LoginCheckIos.execute() ] Exception:", exception );
            // 2015.03.11 エラーの場合、既存で取得している情報から会員判別をするように変更

            // 非会員
            if ( uli.isMemberFlag() == false )
            {
                SetNonMember();
            }
            else
            {
                payKind = UserPayKind.getPayKind( paramUser );

                if ( payKind == "" )
                {
                    // 無料会員
                    SetFreeMember();
                }
                else
                {
                    // 有料会員(キャリア課金)
                    SetPremiumMember( payKind );
                }
            }

            String xmlOut = CreateResponse();
            Logging.info( xmlOut );
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[LoginCheckIos response]Exception:" + e.toString() );
            }
        }
        finally
        {
            Logging.info( "LoginCheckIos.execute end" );
        }
    }

    private void UserInfoUpdate()
    {
        Logging.info( "LoginCheckIos.UserInfoUpdate start" );

        DataApUuidUser dauu = new DataApUuidUser();

        // UUIDとユーザIDで情報取得
        // データがあればupdate、なければinsert
        if ( dauu.getData( paramUuid ) == false )
        {
            dauu.setUuid( paramUuid );
            dauu.setUserId( paramUser );
            dauu.setAppStatus( paramLoginStatus );
            dauu.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dauu.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dauu.insertData();

            Logging.info( "ap_uuid_user insert" );
        }
        else
        {
            dauu.setUserId( paramUser );
            dauu.setAppStatus( paramLoginStatus );
            dauu.setUpdateDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dauu.setUpdateTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dauu.updateData( paramUuid, uli.getUserInfo().getUserId() );

            Logging.info( "ap_uuid_user update" );
        }

        Logging.info( "LoginCheckIos.UserInfoUpdate end" );
    }

    private void UserCheck() throws Exception
    {
        Logging.info( "LoginCheckIos.UserCheck start" );

        DataApUuid dau = new DataApUuid();

        // 無料会員
        if ( uli.isPaymemberFlag() == false )
        {
            SetFreeMember();
        }

        // 有料会員
        else
        {
            // UUID取得
            if ( dau.getData( paramUuid ) )
            {
                // 課金フラグ確認
                int status = dau.getRegistStatusPay();

                // 有料会員
                if ( status == KIND_PREMIUMMEMBER )
                {
                    // スマホ有料会員
                    SetPremiumMember( "プレミアム会員(iApp)" );
                }
                else
                {
                    DataUserBasic userbasic = uli.getUserInfo();

                    int pay = userbasic.getRegistStatusPay();

                    // キャリア課金の場合
                    if ( pay == 9 )
                    {
                        payKind = UserPayKind.getPayKind( paramUser );
                        SetPremiumMember( payKind );
                    }
                    else
                    {
                        // 無料会員
                        SetFreeMember();
                    }
                }
            }
            else
            {
                DataUserBasic userbasic = uli.getUserInfo();

                int pay = userbasic.getRegistStatusPay();

                // キャリア課金の場合
                if ( pay == 9 )
                {
                    payKind = UserPayKind.getPayKind( paramUser );
                    SetPremiumMember( payKind );
                }
                else
                {
                    // 無料会員
                    SetFreeMember();
                }
            }
        }

        Logging.info( "LoginCheckIos.UserCheck end" );
    }
}
