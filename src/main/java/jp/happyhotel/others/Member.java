/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * �n�s�^�b�`����N���X
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
 * �n�s�z�e�^�b�` �J�[�h���X�����o�[
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
    /* �{�Ԋ� */
    private int                 errorCode     = 0;

    /**
     * �n�s�z�e�J�[�h���X�����o�[���擾
     * 
     * @param memberId �ڋqID
     * @param response ���X�|���X
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
                    errorCode = HapiTouchErrorMessage.ERR_30801; // �ڋq�R�[�h�s��
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_30802; // �ڋq�R�[�h�p�����[�^�Ȃ�
            }

            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            if ( ret != false )
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxTouch.setResult( RESULT_OK );
            }
            else
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxTouch.setResult( RESULT_NG );
                gxTouch.setErrorCode( errorCode );
            }

            // XML�̏o��
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
     * �ڋq�̈ꗗ���擾����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
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
                            gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30817 ); // �����ő�l�𒴂���
                            ml.setErrorCode( HapiTouchErrorMessage.ERR_30817 );
                            errorCount = memberCount - max;
                            memberCount = max;
                            ret = true;
                        }
                    }
                }
                if ( ret && memberCount != 0 )
                {
                    ret = ml.sendToHost( hotelId ); // ���M����
                }
            }
            else
            {
                gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30811 );// �����o�[���X�g�擾�G���[
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
                gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30815 );// �����o�[���X�g�擾���̑��G���[
            }
        }
        if ( ret )
        {
            // ���X�|���X���Z�b�g
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
                        gxMemberList.setErrorCode( HapiTouchErrorMessage.ERR_30819 );// �ꕔ���M�G���[
                    }
                    gxMemberList.setIdentifyNo( ml.getIdentifyNo() );
                    gxMemberList.setMemberCount( memberCount );
                    gxMemberList.setErrorCount( errorCount );
                }

                // XML�̏o��
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
        // ���M�G���[
        // ���M���Ȃ�����
        {
            // ���X�|���X���Z�b�g
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

                // XML�̏o��
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
     * �n�s�z�e�J�[�h���X�����o�[���擾
     * 
     * @param customId �ڋq�ԍ�
     * @param response ���X�|���X
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
                            errorCode = HapiTouchErrorMessage.ERR_30824;// �폜�G���[
                        }
                    }
                    else
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30823;// �Y���ڋq�Ȃ��E�폜�ς�
                    }
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_30821;// �ڋq�R�[�h�s��
                }
            }
            else
            {
                errorCode = HapiTouchErrorMessage.ERR_30822;// �ڋq�R�[�h�p�����[�^�Ȃ�
            }

            // xml�o�̓N���X�Ƀm�[�h���Z�b�g
            if ( ret != false )
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxTouch.setResult( RESULT_OK );
            }
            else
            {
                // xml�o�̓N���X�ɒl���Z�b�g
                gxTouch.setResult( RESULT_NG );
                gxTouch.setErrorCode( errorCode );
            }

            // XML�̏o��
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
