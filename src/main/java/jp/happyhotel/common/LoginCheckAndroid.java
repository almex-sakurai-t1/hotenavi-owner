/**
 *
 */
package jp.happyhotel.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataUserBasic;

/**
 * @author miura-s2
 * 
 */
public class LoginCheckAndroid extends LoginCheck
{
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            Logging.info( "LoginCheckAndroid.execute start" );

            messageUrlPremium = Url.getUrl() + "/phone/others/info_premium_app.jsp";

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
            Logging.error( "[LoginCheckAndroid.execute() ] Exception:", exception );
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
                Logging.error( "[LoginCheckAndroid response]Exception:" + e.toString() );
            }
        }
        finally
        {
            Logging.info( "LoginCheckAndroid.execute end" );
        }
    }

    private void UserInfoUpdate()
    {
        Logging.info( "LoginCheckAndroid.UserInfoUpdate start" );

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

        Logging.info( "LoginCheckAndroid.UserInfoUpdate end" );
    }

    private void UserCheck() throws Exception
    {
        Logging.info( "LoginCheckAndroid.UserCheck start" );

        // 無料会員
        if ( uli.isPaymemberFlag() == false )
        {
            SetFreeMember();
        }

        // 有料会員
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

        Logging.info( "LoginCheckAndroid.UserCheck end" );
    }
}
