package jp.happyhotel.action;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Logging;

public class ActionYahooPremium extends BaseAction
{
    private RequestDispatcher requestDispatcher = null;

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String code = "";
        HttpURLConnection urlConn = null;
        BufferedReader reader;
        String line = "";
        StringBuilder bu = new StringBuilder();
        String param = "";
        String accessToken = "";
        String ypValue = "";
        URL url;
        OutputStreamWriter out;
        InputStreamReader input = null;
        String value;
        String err = "";

        try
        {
            code = request.getParameter( "code" );
            if ( code != null )
            {
                Properties prop = new Properties();
                prop.load( new FileInputStream( "/etc/happyhotel/yahooPremium.conf" ) );
                String actionUrl = prop.getProperty( "actionUrl" );

                param = "grant_type=authorization_code&code=" + URLEncoder.encode( code, "utf-8" ) + "&redirect_uri=" + URLEncoder.encode( actionUrl, "utf-8" );

                url = new URL( "https://auth.login.yahoo.co.jp/yconnect/v1/token" );
                urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setInstanceFollowRedirects( false );
                // �w�b�_�[���Z�b�g
                urlConn.setRequestMethod( "POST" );
                urlConn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded;charset=UTF-8" );
                urlConn.setRequestProperty( "Content-Length", param.length() + "" );
                urlConn.setRequestProperty( "Authorization", "Basic ZGowemFpWnBQWEZZTVZFNFpEWlZPREpIUnlaelBXTnZibk4xYldWeWMyVmpjbVYwSm5nOU1HVS06ZjdiNzRlYmU0NzFjMDJiZTZkNjYzZmY5MmZjYzI4NmQ4NDY0YzJkYQ==" );
                urlConn.setDoOutput( true );

                // POST�̈����Z�b�g
                out = new OutputStreamWriter( urlConn.getOutputStream() );
                out.write( param );
                out.flush();
                out.close();

                input = new InputStreamReader( urlConn.getInputStream(), "UTF-8" );
                reader = new BufferedReader( input );

                while( (line = reader.readLine()) != null )
                {
                    bu.append( line );
                }

                value = bu.toString();
                reader.close();
                input.close();

                // �A�N�Z�X�g�[�N��
                int tokenPos = value.indexOf( "access_token" );
                if ( tokenPos > 0 )
                {
                    bu = new StringBuilder();
                    // �X�^�[�g�ʒu�� {"access_token":" ����X�^�[�g����̂�15���炵�ĊJ�n����
                    for( int i = tokenPos + 15 ; i < value.length() ; i++ )
                    {
                        if ( value.charAt( i ) == '\"' )
                        {
                            break;
                        }
                        else
                        {
                            bu.append( value.charAt( i ) );
                        }
                    }
                    accessToken = bu.toString();
                }
            }
            if ( accessToken.equals( "" ) != true )
            {
                // yahoo�v���~�A���f�[�^�擾
                url = new URL( "https://userinfo.yahooapis.jp/Oauth/V1/getUserAttribute" );
                param = "access_token=" + URLEncoder.encode( accessToken, "utf-8" );
                urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setInstanceFollowRedirects( false );
                urlConn.setRequestMethod( "POST" );
                urlConn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded;" );
                urlConn.setRequestProperty( "Content-Length", param.length() + "" );
                urlConn.setRequestProperty( "Authorization", "Bearer " + URLEncoder.encode( accessToken, "utf-8" ) );
                urlConn.setDoOutput( true );

                out = new OutputStreamWriter( urlConn.getOutputStream() );
                out.write( param );
                out.flush();
                out.close();

                input = new InputStreamReader( urlConn.getInputStream(), "UTF-8" );
                reader = new BufferedReader( input );

                while( (line = reader.readLine()) != null )
                {
                    bu.append( line );
                }
                value = bu.toString();
                reader.close();
                input.close();

                // Yp�̎擾���s��
                int YpPos = value.indexOf( "<Yp>" );
                if ( YpPos > 0 )
                {
                    bu = new StringBuilder();
                    // �X�^�[�g�ʒu��<Yp>�����Z
                    for( int i = YpPos + 4 ; i < value.length() ; i++ )
                    {
                        if ( value.charAt( i ) == '<' )
                        {
                            break;
                        }
                        else
                        {
                            bu.append( value.charAt( i ) );
                        }
                    }
                    ypValue = bu.toString();
                }
            }
            if ( ypValue.equalsIgnoreCase( "true" ) == true )
            {
                Cookie addCookie;
                Cookie addCookie2;
                addCookie = new Cookie( "hhyhappy", "secret" );
                addCookie.setPath( "/" );
                // 7����ɐ؂��Cookie
                addCookie.setMaxAge( 86400 * 7 );
                addCookie.setDomain( ".happyhotel.jp" );
                response.addCookie( addCookie );
                addCookie2 = new Cookie( "hhyhistory", "pm" );
                addCookie2.setPath( "/" );
                addCookie2.setDomain( ".happyhotel.jp" );
                // 31���Ő؂��Y�v���~�A������Cookie
                addCookie2.setMaxAge( 86400 * 31 );
                response.addCookie( addCookie2 );
                requestDispatcher = request.getRequestDispatcher( "index_yp.jsp" );
                requestDispatcher.forward( request, response );
            }
            else if ( ypValue.equalsIgnoreCase( "false" ) == true )
            {
                // ���t�[�v���~�A���o�^�y�[�W�֑J��
                request.setAttribute( "yahooRegist", "true" );
                requestDispatcher = request.getRequestDispatcher( "index_yp.jsp" );
                requestDispatcher.forward( request, response );
            }
            else
            {
                /* err = "yahoo�Ƃ̐ڑ��ŃG���[���������܂����B"; */
                err = "2015�N3���������ďI�����܂����B";
                request.setAttribute( "errMsg", err );
                requestDispatcher = request.getRequestDispatcher( "index_yp.jsp" );
                requestDispatcher.forward( request, response );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionYahooPremium execute]Exception:" + e.toString() );
            try
            {
                if ( urlConn.getErrorStream() != null )
                {
                    reader = new BufferedReader( new InputStreamReader( urlConn.getErrorStream() ) );
                    bu = new StringBuilder();
                    while( (line = reader.readLine()) != null )
                    {
                        bu.append( line );
                    }
                    /* err = "yahoo�Ƃ̐ڑ��ŃG���[���������܂����B"; */
                    err = "2015�N3���������ďI�����܂����B";
                    Logging.error( "[ActionYahooPremium execute]Exception:" + bu.toString() );
                    request.setAttribute( "errMsg", err );
                    requestDispatcher = request.getRequestDispatcher( "index_yp.jsp" );
                    requestDispatcher.forward( request, response );
                }
            }
            catch ( Exception ex )
            {
            }
        }
        finally
        {
            if ( urlConn != null )
            {
                urlConn.disconnect();
            }
        }

    }
}
