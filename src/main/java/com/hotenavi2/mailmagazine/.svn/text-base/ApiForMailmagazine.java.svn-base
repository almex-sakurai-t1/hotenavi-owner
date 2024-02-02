package com.hotenavi2.mailmagazine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.MailSystemConf;
import com.hotenavi2.data.DataMagError;
import com.hotenavi2.tls.TLSSocketFactory;

/**
 * HTTP通信にはJava標準の「HttpUrlConnection」
 */
public class ApiForMailmagazine
{

    private int    response_code;
    private String error_message;

    public ApiForMailmagazine()
    {
        this.error_message = "";
        this.response_code = 0;
    }

    public int getResponseCode()
    {
        return this.response_code;
    }

    public String getErrorMessage()
    {
        return this.error_message;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.error_message = errorMessage;
    }

    String execute(String hotelId, String method, String function, String json) throws IOException
    {
        return execute( hotelId, method, function, json, null );
    }

    String execute(String hotelId, String method, String function, String json, String apiKey) throws IOException
    {
        LogLib log = new LogLib();
        if ( apiKey == null )
        {
            apiKey = MailSystemConf.getApiKey();
        }
        if ( method.equals( "PATCH" ) )
        {
            setRequestMethod();
        }
        String response_data = "";
        HttpsURLConnection urlConnection = null;
        try
        {
            // 1.接続するための設定をする
            // URL に対して openConnection メソッドを呼び出すし、接続オブジェクトを生成
            URL url = new URL( MailSystemConf.getApiEndpoint() + function );

            // TLS1.2対応 (暗号化ライブラリ:bouncycastle を使用）
            HttpsURLConnection.setDefaultSSLSocketFactory( new TLSSocketFactory() );

            urlConnection = (HttpsURLConnection)url.openConnection();

            // 1.接続するための設定をする
            // HttpsURLConnectionの各種設定
            // HTTPのメソッドをPOSTに設定
            urlConnection.setRequestMethod( method );
            // if ( method.equals( "PATCH" ) )
            // {
            // setRequestMethod();
            // setRequestMethod( urlConnection, method );
            // urlConnection.setRequestProperty( "X-HTTP-Method", "PATCH" ); // Microsoft
            // urlConnection.setRequestProperty( "X-HTTP-Method-Override", "PATCH" ); // Google/GData
            // urlConnection.setRequestProperty( "X-Method-Override", "PATCH" ); // IBM
            // }
            // リクエストボディへの書き込みを許可
            urlConnection.setDoInput( true );

            // POST,PUT のときは、レスポンスボディの取得を許可
            urlConnection.setDoOutput( method.equals( "POST" ) || method.equals( "PUT" ) || method.equals( "PATCH" ) || method.equals( "DELETE" ) );

            // リクエスト形式をJsonに指定
            urlConnection.setRequestProperty( "Content-Type", "application/json; charset=utf-8" );

            urlConnection.setRequestProperty( "X-MAIL-API-KEY", apiKey );

            // 2.接続を確立する
            urlConnection.connect();

            if ( !method.equals( "GET" ) )
            {
                // 3.リクエストボディに書き込みを行う
                // HttpURLConnectionからOutputStreamを取得し、json文字列を書き込む
                PrintStream ps = new PrintStream( urlConnection.getOutputStream() );
                ps.print( json );
                ps.close();
            }
            response_code = urlConnection.getResponseCode();
            if ( urlConnection.getInputStream() != null )
            {
                response_data = convertToString( urlConnection.getInputStream() );
            }
            // if ( response_code < 200 || response_code > 205 )
            {
                magError( hotelId, method, function, json, response_code, response_data + "\r\n" + apiKey );
            }
        }
        catch ( FileNotFoundException e )
        {
            if ( urlConnection.getErrorStream() != null )
            {
                response_data = convertToString( urlConnection.getErrorStream() );
            }
            log.error( "ApiForMailmagazine execeute FileNotFoundException Error=" + e.toString() );
            setErrorMessage( "ApiForMailmagazine execeute FileNotFoundException Error=" + e.toString() );
            magError( hotelId, method, function, json, response_code, e.toString() + "\r\n" + response_data + "\r\n" + apiKey );
            return response_data;
        }
        catch ( IOException e )
        {
            if ( urlConnection.getErrorStream() != null )
            {
                response_data = convertToString( urlConnection.getErrorStream() );
            }
            log.error( "ApiForMailmagazine execeute IOException Error=" + e.toString() );
            setErrorMessage( "ApiForMailmagazine execeute IOException Error=" + e.toString() );
            magError( hotelId, method, function, json, response_code, e.toString() + "\r\n" + response_data + "\r\n" + apiKey );
            return response_data;
        }
        finally
        {
            if ( urlConnection != null )
            {
                // コネクションを閉じる。
                urlConnection.disconnect();
            }
        }
        // 結果は呼び出し元に返しておく
        return response_data;
    }

    private static void setRequestMethod(final HttpsURLConnection connection, final String method)
    {
        // Nasty workaround for ancient HttpURLConnection only supporting few methods
        final Class<?> httpsURLConnectionClass = connection.getClass();
        try
        {// from ww w . j a v a2s . c om
            Field methodField;
            HttpsURLConnection delegate;
            final Field delegateField = httpsURLConnectionClass.getDeclaredField( "delegate" );
            delegateField.setAccessible( true );
            delegate = (HttpsURLConnection)delegateField.get( connection );
            methodField = delegate.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField( "method" );

            methodField.setAccessible( true );
            methodField.set( delegate, method );
        }
        catch ( NoSuchFieldException e )
        {
        }
        catch ( IllegalAccessException e )
        {
        }
    }

    private static void setRequestMethod()
    {
        try
        {
            Field methodsField = HttpsURLConnection.class.getDeclaredField( "methods" );
            methodsField.setAccessible( true );
            // get the methods field modifiers
            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            // bypass the "private" modifier
            modifiersField.setAccessible( true );

            // remove the "final" modifier
            modifiersField.setInt( methodsField, methodsField.getModifiers() & ~Modifier.FINAL );

            /* valid HTTP methods */
            String[] methods = {
                    "GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
            };
            // set the new methods - including patch
            methodsField.set( null, methods );

        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( IllegalArgumentException e )
        {
            e.printStackTrace();
        }
        catch ( IllegalAccessException e )
        {
            e.printStackTrace();
        }
        catch ( NoSuchFieldException e )
        {
            e.printStackTrace();
        }
    }

    public String convertToString(InputStream stream) throws IOException
    {
        LogLib log = new LogLib();
        StringBuffer sb = new StringBuffer();
        String line = "";
        try
        {
            BufferedReader br = new BufferedReader( new InputStreamReader( stream, "UTF-8" ) );
            while( (line = br.readLine()) != null )
            {
                sb.append( line );
            }
            stream.close();
            br.close();
        }
        catch ( Exception e )
        {
            log.error( "ApiForMailmagazine convertToString Error=" + e.toString() );
        }
        return sb.toString();
    }

    private void magError(String hotelId, String method, String function, String json, int responseCode, String errorMessage) throws IOException
    {
        DataMagError dataMagError = new DataMagError();
        dataMagError.setHotelId( hotelId );
        dataMagError.setMethod( method );
        dataMagError.setFunction( function );
        dataMagError.setJson( json );
        dataMagError.setResponseCode( response_code );
        dataMagError.setMessage( errorMessage );
        dataMagError.insertData();
    }

}
