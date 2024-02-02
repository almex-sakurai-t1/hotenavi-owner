/*
 * @(#)UserRegist.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ���[�U�}�C���j���[�o�^�E�X�V�N���X
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
 * �L�����[�U�}�C���j���[�o�^�E�X�V�N���X ���[�U�̃}�C���j���[�o�^����@�\��񋟂���
 * 
 * @author S.Tashiro
 * @version 1.00 2013/01/16
 */
public class UserRegistAUSp implements Serializable
{
    private static final long serialVersionUID = 5843097734679781045L;

    /**
     * �f�[�^�����������܂��B
     */
    public UserRegistAUSp()
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
        UserBasicInfo ubi;
        UserPointPay upp;
        UserPoint up;

        DataUserSp dus;
        Cookie cookieSp;
        String orderNo = "";

        cookieSp = SelectCookie.getCookie( request, "hhauhappy" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        sStkn = (String)request.getAttribute( "TRAN_ID" );
        dus = new DataUserSp();

        ret = false;
        retSp = false;
        retFirst = false;

        if ( orderNo == null )
        {
            orderNo = "";
        }
        if ( sStkn == null )
        {
            sStkn = "";
        }
        if ( cookieSp != null )
        {
            suid = cookieSp.getValue();
        }

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
                        dus.setCarrierKind( DataUserSp.AU );
                        dus.setRegistDate( nowDate );
                        dus.setRegistTime( nowTime );
                        dus.setRegistDatePay( nowDate );
                        dus.setRegistTimePay( nowTime );
                        if ( orderNo.equals( "" ) == false )
                        {
                            dus.setToken( sStkn );
                        }
                        if ( orderNo.equals( "" ) == false )
                        {
                            dus.setOrderNo( orderNo );
                        }

                        dus.setChargeFlag( DataUserSp.CHARGEFLAG_PAY );
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
                Logging.info( "[UserRegistAUSp.registPay] Exception=" + e.toString() );
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
        boolean retPay = true;
        int nowDate = 0;
        int nowTime = 0;
        String suid = "";
        String sStkn = "";
        UserBasicInfo ubi;
        DataUserSp dus;
        Cookie cookieSp;
        String orderNo = "";

        dus = new DataUserSp();
        sStkn = request.getParameter( "sStkn" );
        cookieSp = SelectCookie.getCookie( request, "hhauhappy" );
        orderNo = (String)request.getAttribute( "ORDER_NO" );
        ret = false;
        retSp = false;

        if ( orderNo == null )
        {
            orderNo = "";
        }
        if ( cookieSp != null )
        {
            suid = cookieSp.getValue();
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
                    if ( retPay != false && ubi.getUserInfo().getRegistStatusPay() > 0 )
                    {
                        retPay = true;
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
                            ubi.getUserInfo().setDelDatePay( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            ubi.getUserInfo().setDelTimePay( Integer.parseInt( DateEdit.getTime( 1 ) ) );
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
                        if ( retPay != false )
                        {
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
                            dus.setDelDatePay( nowDate );
                            dus.setDelTimePay( nowTime );
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
                    Logging.info( "[UserRegistAUSp.secession]:" + "userBasic noData" );

                    // �L���މ�̏ꍇ
                    if ( retPay != false )
                    {
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
                        dus.setDelDatePay( nowDate );
                        dus.setDelTimePay( nowTime );
                    }
                    dus.setToken( sStkn );
                    if ( orderNo.equals( "" ) == false )
                    {
                        dus.setOrderNo( orderNo );
                    }
                    ret = dus.updateData( dus.getUserId() );
                }
                // �폜�̏ꍇ�͏��OK�Ƃ���
                ret = true;
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegistAUSp.registPay] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }
}
