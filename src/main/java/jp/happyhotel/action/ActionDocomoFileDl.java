package jp.happyhotel.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.FileUploader;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserSpTemp;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * �������擾IF
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionDocomoFileDl extends BaseAction
{

    /**
     * �������擾IF
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final String FILE_TYPE = "SMTR";
        final String MOBILE_FREE_SPCD = "00003397701";
        final String MOBILE_PAY_SPCD = "00003397702";

        final String FREE_SPCD = "00073361101";
        final String PAY_SPCD = "00073361102";

        String file = "";
        String freeFile = "";
        String payFile = "";
        boolean ret = true;
        boolean multicontentFlag = false;
        int i = 0;
        int nContentLength = 0;

        ServletOutputStream stream;
        FileUploader fupl = new FileUploader();
        // ���X�|���X�p
        String sDate = "";
        String sType = "";
        String sFile = "sFile";
        String fileDir = "";

        Logging.info( "request:" + request.toString() );

        try
        {
            Properties prop = new Properties();
            FileInputStream propfile;

            // �t�H���_�̔z�u�ꏊ�̐ݒ�����擾
            prop = new Properties();
            propfile = new FileInputStream( "/etc/happyhotel/docomoFileDl.conf" );
            // �v���p�e�B�t�@�C����ǂݍ���
            prop.load( propfile );

            // "fileDir"�ɐݒ肳��Ă���l���擾���܂�
            fileDir = prop.getProperty( "fileDir" );

            prop = null;
            propfile.close();

            stream = response.getOutputStream();
            Logging.info( "[ActionDocomoFileDl.excute()] loaded" );
            // multipart�̃t�H�[���f�[�^���ǂ������`�F�b�N
            if ( ServletFileUpload.isMultipartContent( request ) == true )
            {
                multicontentFlag = true;
                fupl.setData( request );
            }

            // multipart�̃f�[�^�ł���΁A�p�����[�^���擾����
            if ( multicontentFlag != false )
            {
                if ( fupl.getParameter( "sDate" ) != null )
                {
                    sDate = fupl.getParameter( "sDate" );
                }

                if ( fupl.getParameter( "sType" ) != null )
                {
                    sType = fupl.getParameter( "sType" );
                }
            }
            Logging.info( "[ActionDocomoFileDl.excute()] sDate:" + sDate + ", sType:" + sType );

            if ( DateEdit.checkDate( sDate ) != false && sType.equals( FILE_TYPE ) )
            {
                file = fupl.getFileName( sFile );
                // freeFile = "Smtr_" + FREE_SPCD + sDate + "01.csv";
                // payFile = "Smtr_" + PAY_SPCD + sDate + "01.csv";

                Logging.info( "[ActionDocomoFileDl.excute()] " + fileDir + file );
                FileOutputStream fos = new FileOutputStream( fileDir + file );
                fos.write( fupl.getInputData( sFile ) );
                fos.close();
                Logging.info( "[ActionDocomoFileDl.excute()] downloadfinish:" + fileDir + file );
                readCSV( fileDir + file );
            }

            response.setContentType( "text/plain" );
            response.setCharacterEncoding( "Shift_JIS" );
            response.setStatus( 200 );

            // OK<LF>�𑗐M
            stream.print( "OK" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionDocomoFileDl.excute()] Exception:" + e.toString() );

            try
            {
                stream = response.getOutputStream();

                response.setContentType( "text/plain" );
                response.setCharacterEncoding( "Shift_JIS" );
                response.setStatus( 200 );

                // OK<LF>�𑗐M
                stream.print( "OK" );
            }
            catch ( Exception e1 )
            {
                Logging.error( "[ActionDocomoFileDl.excute()] Exception:" + e1.toString() );
            }
        }
        finally
        {
        }
    }

    public void readCSV(String fileName)
    {
        int count = 0;
        int tabCount = 0;
        String SUID = "";
        String uid = "";
        String orderNo = "";
        String siteCode = "";
        DataUserSpTemp dust = new DataUserSpTemp();
        String str = "";

        try
        {
            File csv = new File( fileName ); // CSV�f�[�^�t�@�C��
            BufferedReader br = new BufferedReader( new FileReader( csv ) );

            Logging.info( "[ActionDocomoFileDl.readCSV()] fileinput:" + fileName );

            // �ŏI�s�܂œǂݍ���
            String line = "";
            while( (line = br.readLine()) != null )
            {
                // 1�s���f�[�^�̗v�f�ɕ���
                StringTokenizer st = new StringTokenizer( line, "," );
                if ( count > 0 )
                {
                    // ������
                    if ( dust == null )
                    {
                        dust = new DataUserSpTemp();
                    }
                    tabCount = 0;
                    SUID = "";
                    uid = "";
                    orderNo = "";
                    siteCode = "";

                    while( st.hasMoreTokens() )
                    {

                        if ( tabCount == 0 )
                        {
                            SUID = st.nextToken();
                        }
                        else if ( tabCount == 1 )
                        {
                            uid = st.nextToken();
                        }
                        else if ( tabCount == 2 )
                        {
                            orderNo = st.nextToken();
                        }
                        else if ( tabCount == 3 )
                        {
                            siteCode = st.nextToken();
                        }
                        else
                        {
                            str = st.nextToken();
                        }
                        tabCount++;
                    }

                    if ( SUID.equals( "" ) == false && uid.equals( "" ) == false && orderNo.equals( "" ) == false && siteCode.equals( "" ) == false )
                    {
                        dust.setOpenId( SUID );
                        dust.setMobileTermno( uid );
                        dust.setOrderNo( orderNo );
                        dust.setSiteCode( siteCode );
                        dust.insertData();
                        dust = null;
                    }

                }
                count++;
            }
            br.close();
            Logging.info( "[ActionDocomoFileDl.readCSV()] fileinput finish:" + fileName );

        }
        catch ( FileNotFoundException e )
        {
            Logging.error( "[ActionDocomoFileDl.readCsv] File not Found:" + fileName );
        }
        catch ( IOException e )
        {
            Logging.error( "[ActionDocomoFileDl.readCsv] Exception:" + e.toString() );
        }
    }
}
