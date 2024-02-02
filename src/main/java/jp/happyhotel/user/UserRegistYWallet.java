/*
 * @(#)UserRegist.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ���[�U�}�C���j���[�o�^�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserSp;

/**
 * �L�����[�U�}�C���j���[�o�^�E�X�V�N���X ���[�U�̃}�C���j���[�o�^����@�\��񋟂���
 * 
 * @author S.Tashiro
 * @version 1.00 2013/03/05
 */
public class UserRegistYWallet implements Serializable
{
    private static final long serialVersionUID = 5952932526512866499L;

    /**
     * �f�[�^�����������܂��B
     */
    public UserRegistYWallet()
    {
    }

    /**
     * �L�������
     * 
     * @param request HTTP���N�G�X�g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean registPay(HttpServletRequest request)
    {
        final int REGIST_POINT = 1000001; // �L������|�C���g�̃|�C���g�R�[�h
        boolean ret;
        boolean retSp;
        boolean retFirst = false;
        int nowDate = 0;
        int nowTime = 0;
        String suid = "";
        String sStkn = "";
        String date = "";
        String time = "";
        UserBasicInfo ubi;
        UserPointPay upp;
        UserPoint up;

        DataUserSp dus;
        Cookie cookieSp;
        String orderNo = "";

        suid = (String)request.getAttribute( "OPEN_ID" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        date = (String)request.getAttribute( "DATE" );
        time = (String)request.getAttribute( "TIME" );
        sStkn = (String)request.getAttribute( "TOKEN" );
        dus = new DataUserSp();

        ret = false;
        retSp = false;
        retFirst = false;

        if ( orderNo == null || orderNo.equals( "" ) != false || CheckString.numCheck( orderNo ) == false )
        {
            orderNo = "0";
        }
        if ( suid == null )
        {
            suid = "";
        }
        if ( date == null || date.equals( "" ) != false || CheckString.numCheck( date ) == false )
        {
            date = "0";
        }
        if ( time == null || time.equals( "" ) != false || CheckString.numCheck( time ) == false )
        {
            time = "0";
        }
        if ( sStkn == null )
        {
            sStkn = "";
        }
        Logging.info( "[UserRegistYWallet.registPay()] sStkn:" + sStkn );

        retSp = dus.getDataBySuid( suid );
        // �}�C���j���[�o�^����
        if ( retSp != false )
        {
            // �}�C���j���[�o�^���ͽð����1�ɂ���hh_user_basic�ɏ�������
            try
            {
                ubi = new UserBasicInfo();
                // ���[�U�[���ԍ����̈�����f�[�^�`�F�b�N
                ret = ubi.getUserBasic( dus.getUserId() );
                if ( ret != false )
                {
                    ubi.getUserInfo().setRegistStatusOld( ubi.getUserInfo().getRegistStatus() );
                    // ��������o�^�Ń��[�UID�A�p�X���[�h���o�^����Ă���΁Aregist_status_pay=9�Aregist_status=9�ɂ���
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
                    // �o�^�ɐ��������ꍇ�A
                    if ( ret != false )
                    {
                        upp = new UserPointPay();
                        retFirst = upp.setRegistPoint( ubi.getUserInfo().getUserId(), REGIST_POINT, 0, "" );
                        // ��������o�^���I���Ă��Ȃ����[�U�ɖ����|�C���g��t�^����
                        if ( ubi.getUserInfo().getRegistStatusOld() == 2 || ubi.getUserInfo().getRegistStatusOld() == 3 )
                        {
                            up = new UserPoint();
                            up.setPointJoin( ubi.getUserInfo().getUserId() );
                        }
                        nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                        nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                        // DataUserSp�Ƀf�[�^��o�^
                        retSp = dus.getData( ubi.getUserInfo().getUserId() );
                        dus.setOpenId( suid );
                        dus.setCarrierKind( DataUserSp.YAHOO_WALLET );
                        dus.setRegistDatePay( Integer.parseInt( date ) );
                        dus.setRegistTimePay( Integer.parseInt( time ) );
                        dus.setToken( sStkn );
                        dus.setOrderNo( orderNo );
                        dus.setChargeFlag( DataUserSp.CHARGEFLAG_PAY );
                        Logging.info( "retSp:" + retSp );
                        // �f�[�^�̎擾�󋵂ɉ����ăC���T�[�g�A�A�b�v�f�[�g���s���B
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
                Logging.info( "[UserRegistYWallet.registPay] Exception=" + e.toString() );
            }
            finally
            {
            }
        }

        return(ret);
    }

    /**
     * �މ��
     * 
     * @param request HTTP���N�G�X�g
     * @param kind (0:�o�^�A1:����������)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean secession(HttpServletRequest request)
    {
        boolean ret;
        boolean retSp;
        int nowDate = 0;
        int nowTime = 0;
        String suid = "";
        String sStkn = "";
        String date = "";
        String time = "";
        UserBasicInfo ubi;
        DataUserSp dus;
        Cookie cookieSp;
        String orderNo = "";

        ret = false;
        retSp = false;
        dus = new DataUserSp();
        suid = (String)request.getAttribute( "OPEN_ID" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        date = (String)request.getAttribute( "DATE" );
        time = (String)request.getAttribute( "TIME" );
        sStkn = (String)request.getAttribute( "TOKEN" );

        if ( orderNo == null || orderNo.equals( "" ) != false || CheckString.numCheck( orderNo ) == false )
        {
            orderNo = "0";
        }
        if ( suid == null )
        {
            suid = "";
        }
        if ( date == null || date.equals( "" ) != false || CheckString.numCheck( date ) == false )
        {
            date = "0";
        }
        if ( time == null || time.equals( "" ) != false || CheckString.numCheck( time ) == false )
        {
            time = "0";
        }

        if ( sStkn == null )
        {
            sStkn = "";
        }

        retSp = dus.getDataBySuid( suid );
        // �}�C���j���[�폜����
        if ( retSp != false )
        {
            try
            {
                ubi = new UserBasicInfo();
                // ���[�U�[���ԍ������f�[�^�`�F�b�N
                ret = ubi.getUserBasic( dus.getUserId() );

                if ( ret != false )
                {
                    nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

                    // �L���̑މ�ŁA�L������ł���΃f�[�^���폜
                    if ( ubi.getUserInfo().getRegistStatusPay() > 0 )
                    {
                        // �L������r�����ǂ���
                        if ( (ubi.getUserInfo().getRegistStatusPay() == 1) && (ubi.getUserInfo().getRegistStatusOld() == 8) )
                        {
                            ret = ubi.getUserInfo().deleteData( ubi.getUserInfo().getUserId() );
                        }
                        else
                        {
                            // ��������L���ɂȂ��Ă�����폜�t���O�𗧂Ă�
                            if ( ubi.getUserInfo().getRegistStatusOld() == 8 )
                            {
                                // ���[�U��{���̍폜�t���O��1�𗧂Ă�
                                ubi.getUserInfo().setDelFlag( 1 );
                            }
                            // ���o�^��������i�}�C���[�h�o�^�j
                            else if ( ubi.getUserInfo().getRegistStatusOld() == 1 )
                            {
                                // ���[�U�[ID�ƒ[���ԍ��[���ԍ��������Ńp�X���[�h�����͂���Ă��Ȃ������炻�̂܂�
                                if ( ubi.getUserInfo().getUserId().compareTo( ubi.getUserInfo().getMobileTermNo() ) == 0 &&
                                        ubi.getUserInfo().getPasswd().compareTo( "" ) == 0 )
                                {
                                    ubi.getUserInfo().setRegistStatus( 1 );
                                }
                                // ����ȊO��regist_status=2�ɕύX����
                                else
                                {
                                    ubi.getUserInfo().setRegistStatus( 2 );
                                }
                            }
                            else
                            {
                                // regist_status_old��0�̃��[�U�[��regist_status��ύX���Ȃ�
                                if ( ubi.getUserInfo().getRegistStatusOld() == 2 || ubi.getUserInfo().getRegistStatusOld() == 3 ||
                                        ubi.getUserInfo().getRegistStatusOld() == 9 )
                                {
                                    ubi.getUserInfo().setRegistStatus( ubi.getUserInfo().getRegistStatusOld() );
                                }
                            }
                            ubi.getUserInfo().setRegistStatusPay( 0 );
                            ubi.getUserInfo().setDelDatePay( Integer.parseInt( date ) );
                            ubi.getUserInfo().setDelTimePay( Integer.parseInt( time ) );
                            // �X�V
                            ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                        }
                    }

                    // �މ���܂���������SP�����ύX
                    if ( ret != false )
                    {
                        // DataUserSp�Ƀf�[�^��o�^
                        retSp = dus.getData( ubi.getUserInfo().getUserId() );

                        // �L���މ�̏ꍇ
                        // �����}�C���j���[�o�^���Ă��Ȃ��A�܂��͂����Ȃ�L�������������މ����
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
                        dus.setDelDatePay( Integer.parseInt( date ) );
                        dus.setDelTimePay( Integer.parseInt( time ) );
                        dus.setToken( sStkn );
                        dus.setOrderNo( orderNo );

                        ret = dus.updateData( ubi.getUserInfo().getUserId() );
                    }

                    if ( ret == false )
                    {
                        Logging.info( "�}�C���j���[�폜���s" +
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
                    Logging.info( "[UserRegistYWallet.secession]:" + "userBasic noData" );

                    // �L���މ�̏ꍇ
                    // �����}�C���j���[�o�^���Ă��Ȃ��A�܂��͂����Ȃ�L�������������މ����
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
                    dus.setDelDatePay( Integer.parseInt( date ) );
                    dus.setDelTimePay( Integer.parseInt( time ) );
                    dus.setToken( sStkn );
                    dus.setOrderNo( orderNo );
                    ret = dus.updateData( dus.getUserId() );

                }
                // �폜�̏ꍇ�͏��OK�Ƃ���
                ret = true;
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegistYWallet.registPay] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }
}
