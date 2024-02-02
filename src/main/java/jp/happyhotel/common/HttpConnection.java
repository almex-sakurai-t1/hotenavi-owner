/*
 * @(#)HttpConnection.java 1.00 2009/06/22 Copyright (C) ALMEX Inc. 2009 HTTP�ʐM�N���X
 */

package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataMasterUseragent;

/**
 * HTTP�ʐM�N���X �w�肵��URL��HTTP�ʐM���s���N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/22
 */
public class HttpConnection implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -6827518152165230677L;

    private String            contentType;
    private int               imageSize;
    private byte[]            image;
    private String            coordinate;
    private String            coordinateUL;
    private String            coordinateUR;
    private String            coordinateDL;
    private String            coordinateDR;
    private String            sclale;

    /**
     * �f�[�^�����������܂��B
     */
    public HttpConnection()
    {
        this.contentType = "";
        imageSize = 0;
        coordinate = "";
        coordinateUL = "";
        coordinateUR = "";
        coordinateDL = "";
        coordinateDR = "";
        sclale = "";
    }

    /**
     * �R���e���g�^�C�v���擾
     * 
     * @return �o�͂���R���e���g�^�C�v
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * ���S�n���W���擾
     * 
     * @return ���S�n�̍��W
     */
    public String getCoordinate()
    {
        return coordinate;
    }

    /**
     * �n�}�����̍��W���擾
     * 
     * @return �n�}�̍����̍��W���擾
     */
    public String getCoordinateDL()
    {
        return coordinateDL;
    }

    /**
     * �n�}�E���̍��W���擾
     * 
     * @return �n�}�̉E���̍��W���擾
     */
    public String getCoordinateDR()
    {
        return coordinateDR;
    }

    /**
     * �n�}����̍��W���擾
     * 
     * @return �n�}�̍���̍��W���擾
     */
    public String getCoordinateUL()
    {
        return coordinateUL;
    }

    /**
     * �n�}�E��̍��W���擾
     * 
     * @return �n�}�̉E��̍��W���擾
     */
    public String getCoordinateUR()
    {
        return coordinateUR;
    }

    /**
     * �摜�C���[�W�擾
     * 
     * @return image
     */
    public byte[] getImage()
    {
        return image;
    }

    /**
     * �摜�T�C�Y�擾
     * 
     * @return imageSize
     */
    public int getImageSize()
    {
        return imageSize;
    }

    /**
     * �k�ڂ��擾
     * 
     * @return �\�����Ă���n�}�̏k��
     */
    public String getSclale()
    {
        return sclale;
    }

    /**
     * �R���e���g�^�C�v���Z�b�g
     * 
     * @param contentType �ݒ肷��R���e���g�^�C�v���Z�b�g
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * ���S���W���Z�b�g
     * 
     * @param coordinate �n�}�̒��S�n�̍��W���Z�b�g
     */
    public void setCoordinate(String coordinate)
    {
        this.coordinate = coordinate;
    }

    /**
     * �n�}�����̍��W���Z�b�g
     * 
     * @param coordinateDL �n�}�̍����̍��W���Z�b�g
     */
    public void setCoordinateDL(String coordinateDL)
    {
        this.coordinateDL = coordinateDL;
    }

    /**
     * �n�}�E���̍��W���Z�b�g
     * 
     * @param coordinateDR �n�}�̉E���̍��W���Z�b�g
     */
    public void setCoordinateDR(String coordinateDR)
    {
        this.coordinateDR = coordinateDR;
    }

    /**
     * �n�}����̍��W���Z�b�g
     * 
     * @param coordinateUL �n�}�̍���̍��W���Z�b�g
     */
    public void setCoordinateUL(String coordinateUL)
    {
        this.coordinateUL = coordinateUL;
    }

    /**
     * �n�}�E��̍��W���Z�b�g
     * 
     * @param coordinateUR �n�}�̉E��̍��W���Z�b�g
     */
    public void setCoordinateUR(String coordinateUR)
    {
        this.coordinateUR = coordinateUR;
    }

    /**
     * �摜�C���[�W���Z�b�g
     * 
     * @param image �ݒ肷��image
     */
    public void setImage(byte[] image)
    {
        this.image = image;
    }

    /**
     * �摜�T�C�Y���Z�b�g
     * 
     * @param imageSize �ݒ肷��imageSize
     */
    public void setImageSize(int imageSize)
    {
        this.imageSize = imageSize;
    }

    /**
     * �k�ڂ��Z�b�g
     * 
     * @param sclale �ݒ肷��n�}�̏k��
     */
    public void setSclale(String sclale)
    {
        this.sclale = sclale;
    }

    /**
     * HTTP�ʐM�N���X
     * 
     * @param request HTTP���N�G�X�g
     * @param response HTTP���X�|���X
     * @param connectUrl �ڑ�����URL
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean urlConnection(HttpServletRequest request, HttpServletResponse response, String connectUrl)
    {
        int i;
        int count;
        int readCount;
        int carrierFlag;
        byte[] readBuff;
        // byte[] readData;
        String headerName;
        String headerValue;
        URL url;
        HttpURLConnection urlConn;
        Enumeration headerNamesEnum;
        Enumeration headerValueEnum;
        ArrayList<Byte> readData;

        carrierFlag = UserAgent.getUserAgentType( request );
        readCount = 0;
        readData = new ArrayList<Byte>();
        readBuff = new byte[1024];
        headerNamesEnum = null;
        headerValueEnum = null;

        try
        {
            // �ڑ�����URL�̍쐬
            url = new URL( connectUrl );
            // �ڑ�����I�u�W�F�N�g�̍쐬
            urlConn = (HttpURLConnection)url.openConnection();
            // GET���\�b�h���Z�b�g
            urlConn.setRequestMethod( "POST" );

            // �L�����A�ʂɃw�b�_��ǉ�����B
            switch( carrierFlag )
            {
                case DataMasterUseragent.CARRIER_DOCOMO:
                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );
                    break;

                case DataMasterUseragent.CARRIER_AU:
                    // �w�b�_����񋓌^�z��ɃZ�b�g
                    headerNamesEnum = request.getHeaderNames();
                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

                    // ���̗v�f���Ȃ��Ȃ�܂ŌJ��Ԃ�
                    while( headerNamesEnum.hasMoreElements() != false )
                    {
                        // ���̃w�b�_�����擾���A�w�b�_������l��񋓌^�f�[�^�փZ�b�g
                        headerName = (String)headerNamesEnum.nextElement();
                        headerValueEnum = request.getHeaders( headerName );

                        // ���̗v�f���Ȃ��Ȃ�܂ŌJ��Ԃ�
                        while( headerValueEnum.hasMoreElements() != false )
                        {
                            headerValue = (String)headerValueEnum.nextElement();
                            // x-up-subno(�[���ԍ��̃w�b�_)�ȊO�̃w�b�_�[���Z�b�g���Ă���
                            if ( headerName.compareTo( "x-up-subno" ) != 0 )
                            {
                                // x-up�Ŏn�܂�w�b�_�[���Z�b�g����
                                if ( headerName.indexOf( "x-up-" ) >= 0 )
                                {
                                    urlConn.setRequestProperty( headerName, headerValue );
                                }
                            }
                        }
                    }
                    break;

                case DataMasterUseragent.CARRIER_SOFTBANK:
                    headerNamesEnum = request.getHeaderNames();

                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

                    // ���̗v�f���Ȃ��Ȃ�܂ŌJ��Ԃ�
                    while( headerNamesEnum.hasMoreElements() != false )
                    {
                        // ���̃w�b�_�[�����擾���A�w�b�_�[������l��񋓌^�f�[�^�փZ�b�g
                        headerName = (String)headerNamesEnum.nextElement();
                        headerValueEnum = request.getHeaders( headerName );
                        // ���̗v�f���Ȃ��Ȃ�܂ŌJ��Ԃ�
                        while( headerValueEnum.hasMoreElements() != false )
                        {
                            headerValue = (String)headerValueEnum.nextElement();
                            // x-jphone-uid(�[���ԍ��̃w�b�_)�ȊO�̃w�b�_�[���Z�b�g���Ă���
                            if ( headerName.compareTo( "x-jphone-uid" ) != 0 )
                            {
                                // x-jphone�Ŏn�܂�w�b�_�[���Z�b�g����
                                if ( headerName.indexOf( "x-jphone-" ) >= 0 )
                                {
                                    urlConn.setRequestProperty( headerName, headerValue );
                                }
                            }
                        }
                    }
                    break;
            }

            // �ڑ�
            urlConn.connect();

            // URL����擾����XML������W�����擾
            connectUrl += "&output=xml";
            ReadXml readXml = new ReadXml( connectUrl );
            readXml.getElementCoordinate();

            // ���S�n�̍��W���Z�b�g
            this.coordinate = readXml.getCoordinate();
            // �E��̍��W���Z�b�g
            this.coordinateUR = readXml.getUr();
            // ����̍��W���Z�b�g
            this.coordinateUL = readXml.getUl();
            // �E���̍��W���Z�b�g
            this.coordinateDR = readXml.getDr();
            // �����̍��W���Z�b�g
            this.coordinateDL = readXml.getDl();
            // �k�ڂ��Z�b�g
            this.sclale = readXml.getScale();

            // // �擾�����R���e���g�^�C�v���Z�b�g
            // this.contentType = urlConn.getContentType();
            //
            // // ���S�n�̍��W���Z�b�g
            // this.coordinate = urlConn.getHeaderField( "X-Coordinate" );
            // // ����̍��W���Z�b�g
            // this.coordinateUL = urlConn.getHeaderField( "X-Coordinate-UL" );
            // // �E��̍��W���Z�b�g
            // this.coordinateUR = urlConn.getHeaderField( "X-Coordinate-UR" );
            // // �����̍��W���Z�b�g
            // this.coordinateDL = urlConn.getHeaderField( "X-Coordinate-DL" );
            // // �E���̍��W���Z�b�g
            // this.coordinateDR = urlConn.getHeaderField( "X-Coordinate-DR" );
            //
            // // �k�ڂ��Z�b�g
            // this.sclale = urlConn.getHeaderField( "X-Scale" );

            // �߂��Ă����C���[�W��Byte�z��Ɋi�[
            while( true )
            {
                count = urlConn.getInputStream().read( readBuff );
                if ( count == -1 )
                {
                    break;
                }

                for( i = 0 ; i < count ; i++ )
                {
                    readData.add( new Byte( readBuff[i] ) );
                    readCount++;
                }

            }

            if ( readCount > 0 )
            {
                readData.toArray();
                // �摜�̏o��
                this.imageSize = readCount;
                // �摜�̃T�C�Y�Ŕz��̗v�f�������߂�
                this.image = new byte[readCount];
                //
                for( i = 0 ; i < readCount ; i++ )
                {
                    this.image[i] = readData.get( i );
                }
                return(true);
            }
            else
            {
                return(false);
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HttpConnetion.urlConnection] Exception=" + e.toString() );
            return(false);
        }
    }

    public static void sendPost(String url, String param, HttpServletRequest request)
    {
        try
        {
            URL obj = new URL( url );
            HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();

            // add reuqest header
            con.setRequestMethod( "POST" );
            con.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

            // Send post request
            con.setDoOutput( true );
            DataOutputStream wr = new DataOutputStream( con.getOutputStream() );
            wr.writeBytes( param );
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            Logging.info( "\nSending 'POST' request to URL : " + url );
            Logging.info( "Post parameters : " + param );
            Logging.info( "Response Code : " + responseCode );

            BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while( (inputLine = in.readLine()) != null )
            {
                response.append( inputLine );
            }
            in.close();
            con.disconnect();
        }
        catch ( Exception e )
        {
            Logging.error( "[HttpConnetion.sendPost] Exception=" + e.toString() );
        }
        finally
        {
        }
    }

}