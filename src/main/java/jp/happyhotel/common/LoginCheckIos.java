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

            // ����
            if ( uli.isMemberFlag() == false )
            {
                SetNonMember();
            }

            // ���
            else
            {
                // DB���X�V
                UserInfoUpdate();

                // ����X�e�[�^�X�`�F�b�N
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
            // 2015.03.11 �G���[�̏ꍇ�A�����Ŏ擾���Ă����񂩂������ʂ�����悤�ɕύX

            // ����
            if ( uli.isMemberFlag() == false )
            {
                SetNonMember();
            }
            else
            {
                payKind = UserPayKind.getPayKind( paramUser );

                if ( payKind == "" )
                {
                    // �������
                    SetFreeMember();
                }
                else
                {
                    // �L�����(�L�����A�ۋ�)
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

        // UUID�ƃ��[�UID�ŏ��擾
        // �f�[�^�������update�A�Ȃ����insert
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

        // �������
        if ( uli.isPaymemberFlag() == false )
        {
            SetFreeMember();
        }

        // �L�����
        else
        {
            // UUID�擾
            if ( dau.getData( paramUuid ) )
            {
                // �ۋ��t���O�m�F
                int status = dau.getRegistStatusPay();

                // �L�����
                if ( status == KIND_PREMIUMMEMBER )
                {
                    // �X�}�z�L�����
                    SetPremiumMember( "�v���~�A�����(iApp)" );
                }
                else
                {
                    DataUserBasic userbasic = uli.getUserInfo();

                    int pay = userbasic.getRegistStatusPay();

                    // �L�����A�ۋ��̏ꍇ
                    if ( pay == 9 )
                    {
                        payKind = UserPayKind.getPayKind( paramUser );
                        SetPremiumMember( payKind );
                    }
                    else
                    {
                        // �������
                        SetFreeMember();
                    }
                }
            }
            else
            {
                DataUserBasic userbasic = uli.getUserInfo();

                int pay = userbasic.getRegistStatusPay();

                // �L�����A�ۋ��̏ꍇ
                if ( pay == 9 )
                {
                    payKind = UserPayKind.getPayKind( paramUser );
                    SetPremiumMember( payKind );
                }
                else
                {
                    // �������
                    SetFreeMember();
                }
            }
        }

        Logging.info( "LoginCheckIos.UserCheck end" );
    }
}
