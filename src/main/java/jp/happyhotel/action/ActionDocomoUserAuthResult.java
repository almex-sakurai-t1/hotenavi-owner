package jp.happyhotel.action;

import java.util.Calendar;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.user.UserRegistDocomoSp;

/**
 * 
 * ���[�U�F�،���IF
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionDocomoUserAuthResult extends BaseAction
{

    /**
     * ���[�U�F�،���IF
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final String USER = "USER";
        final String USER1 = "USER1";
        final String USER12 = "USER12";
        final String USER3 = "USER3";
        final String USER10 = "USER10";
        final int REGIST_NEW = 0;
        final int REGIST_RE = 1;

        boolean ret = true;
        int nContentLength = 0;
        ServletOutputStream stream;
        String result = "";
        String sSpnm = "";
        String sSpsppr = "";
        String sDate = "";
        String sType = "";
        String sPric = "315";
        String sStkn = "";
        String sSlcd = "";
        String sDspstr1 = "";
        String sDspstr2 = "";
        String sTrset = "1";
        String sSuid = "";

        String date = "";
        String time = "";
        DataLoginInfo_M2 dataLoginInfo;
        UserRegistDocomoSp ursd;

        sSpnm = request.getParameter( "sSpnm" );
        sSpsppr = request.getParameter( "sSpsppr" );
        sSlcd = request.getParameter( "sSlcd" );
        sDate = request.getParameter( "sDate" );
        sType = request.getParameter( "sType" );
        sPric = request.getParameter( "sPric" );
        sStkn = request.getParameter( "sStkn" );
        sSuid = request.getParameter( "sSuid" );

        dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

        Logging.info( "sSpnm:" + sSpnm );
        Logging.info( "sSpsppr:" + sSpsppr );
        Logging.info( "sSlcd:" + sSlcd );
        Logging.info( "sDate:" + sDate );
        Logging.info( "sType:" + sType );
        Logging.info( "sPric:" + sPric );
        Logging.info( "sStkn:" + sStkn );
        Logging.info( "sSuid:" + sSuid );

        try
        {
            stream = response.getOutputStream();
            if ( sSpnm == null )
            {
                sSpnm = "";
            }
            if ( sSpsppr == null )
            {
                sSpsppr = "";
            }
            if ( sSlcd == null )
            {
                sSlcd = "";
            }
            if ( sDate == null )
            {
                sDate = "";
            }
            if ( sType == null )
            {
                sType = "";
            }
            if ( sPric == null )
            {
                sPric = "";
            }
            if ( sStkn == null )
            {
                sStkn = "";
            }
            if ( sSuid == null )
            {
                sSuid = "";
            }

            while( true )
            {
                // ���[�U���ʎq���h�R���w��̎��ʎq
                if ( sType.equals( USER1 ) == false && sType.equals( USER12 ) == false &&
                        sType.equals( USER3 ) == false && sType.equals( USER10 ) == false )
                {
                    Logging.error( "sType:error" );
                    ret = false;
                    break;
                }
                if ( sSlcd.equals( "" ) != false
                        || sDate.equals( "" ) != false || sType.equals( "" ) != false || sPric.equals( "" ) != false
                        || sStkn.equals( "" ) != false || sSuid.equals( "" ) != false )
                {
                    Logging.error( "parameter:error" );
                    ret = false;
                    break;
                }
                if ( sSpnm.equals( "" ) != false && sType.equals( USER3 ) == false )
                {
                    Logging.error( "parameter2:error" );
                    ret = false;
                    break;
                }

                // �h�R�����ʒm����sPric�i���ϋ��z�j���������擾IF�Őݒ肵���l�Ɠ����ł��邱��
                if ( sType.equals( USER1 ) != false )
                {
                    if ( sPric.equals( "315" ) == false )
                    {
                        Logging.error( "sPric_315:error" );
                        ret = false;
                    }
                }
                else if ( sType.equals( USER12 ) != false )
                {
                    if ( sPric.equals( "0" ) == false )
                    {
                        Logging.error( "sPric_0:error" );
                        ret = false;
                        break;
                    }
                }

                // ���t�̃`�F�b�N
                // �h�R�����ʒm����sDate�i���[�U�F�ؓ����j�ƌ��ݎ����̍���5���ȓ��ł��邱��
                date = sDate.substring( 0, 10 );
                date = date.replaceAll( "-", "" );

                time = sDate.substring( 11 );
                time = time.replaceAll( ":", "" );

                Logging.info( "date:" + date );
                Logging.info( "time:" + time );
                if ( this.compareDate( Integer.parseInt( date ), Integer.parseInt( time ) ) == false )
                {
                    Logging.error( "date_compare:error" );

                    ret = false;
                    break;
                }

                // �ڑ������h�R����IP�A�h���X�ł��邱��

                break;
            }
            request.setAttribute( "SUID", sSuid );
            request.setAttribute( "ORDER_NO", sSlcd );

            if ( ret != false )
            {
                ursd = new UserRegistDocomoSp();

                // �L���o�^
                if ( sType.equals( USER1 ) )
                {
                    ret = ursd.registPay( request, REGIST_NEW );
                    Logging.info( "registPay1:" + ret );
                }
                // ����
                if ( sType.equals( USER12 ) )
                {
                    ret = ursd.registFree( request );
                    Logging.info( "registFree1:" + ret );
                }
                // �}�C���j���[����
                if ( sType.equals( USER3 ) )
                {
                    ret = ursd.secession( request );
                    Logging.info( "secession:" + ret );
                    ret = true;
                }
                // �������ēo�^
                if ( sType.equals( USER10 ) )
                {
                    if ( sPric.equals( "315" ) != false )
                    {
                        ret = ursd.registPay( request, REGIST_RE );
                        Logging.info( "registPay2:" + ret );
                    }
                    else
                    {
                        ret = ursd.registFree( request );
                        Logging.info( "registFree2:" + ret );
                    }
                }
            }

            // �o�^����
            response.setContentType( "text/html" );
            response.setCharacterEncoding( "Shift_JIS" );
            response.setStatus( 200 );

            // OK<LF>�𑗐M
            if ( ret == false )
            {
                result = "NG";
                stream.print( "Result=" + result + "\r" );
            }
            else
            {
                result = "OK";
                stream.print( "Result=" + result + "\r" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionDocomoUserAuthResult.excute()] Exception:" + e.toString() );
        }
        finally
        {
        }
    }

    /***
     * ���t�A��������T�[�o�Ƃ̎��ԍ��m�F����
     * 
     * @param strDate ���t
     * @param strTime ����
     * @return �������ʁitrue�Afalse�j
     * @see ����5���ȓ���������true�A����ȊO�Ȃ�false
     */
    public boolean compareDate(int strDate, int strTime)
    {
        boolean ret = false;
        boolean retBefore = false;
        boolean retAfter = false;
        int nResult = 0;
        int nYear = 0;
        int nMonth = 0;
        int nDate = 0;
        int nHour = 0;
        int nMinute = 0;
        int nSecond = 0;
        Calendar cal1 = Calendar.getInstance();// ��r����
        Calendar cal2 = Calendar.getInstance(); // ���݂̎���

        // ���t�̒l�Z�b�g
        nYear = strDate / 10000;
        nMonth = strDate % 10000 / 100;
        nDate = strDate % 100;

        // �����̃Z�b�g
        nHour = strTime / 10000;
        nMinute = strTime % 10000 / 100;
        nSecond = strTime % 100;

        // ���t���r����
        cal1.clear();
        cal1.set( nYear, nMonth - 1, nDate, nHour, nMinute, nSecond );
        // 5����̃J�����_�[���擾
        cal2.add( Calendar.MINUTE, 5 );

        // ��r�@�J�����_�[2�i5����̃J�����_�[�j > �J�����_�[1�i���N�G�X�g���ꂽ�J�����_�[�j
        nResult = cal2.compareTo( cal1 );

        // cal2�̕����傫��������true
        if ( nResult >= 0 )
        {
            retAfter = true;
        }

        // 5���O�̃J�����_�[���擾(5���i�߂����ߔ{�̐��l�ň���)
        cal2.add( Calendar.MINUTE, -10 );

        // ��r�@�J�����_�[2�i5���O�̃J�����_�[�j < �J�����_�[1�i���N�G�X�g���ꂽ�J�����_�[�j
        nResult = cal2.compareTo( cal1 );
        if ( nResult <= 0 )
        {
            retBefore = true;
        }

        // �����Ƃ����������true
        if ( (retBefore != false) && (retAfter != false) )
        {
            ret = true;
        }

        return(ret);
    }

}
