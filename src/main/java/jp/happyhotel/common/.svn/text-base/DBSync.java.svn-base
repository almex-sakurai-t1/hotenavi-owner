package jp.happyhotel.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.Pubsub.Projects.Topics;
import com.google.api.services.pubsub.PubsubScopes;
import com.google.api.services.pubsub.model.PublishRequest;
import com.google.api.services.pubsub.model.PublishResponse;
import com.google.api.services.pubsub.model.PubsubMessage;

/**
 * GCPのPub/Subにて、データを同期するクラス
 * 
 * @author Keion.Park
 */
public class DBSync
{
    private static final HttpTransport TRANSPORT;
    private static final JsonFactory   JSON_FACTORY     = JacksonFactory.getDefaultInstance();
    private static final String        APPLICATION_NAME = "happyhotel";
    private static final String        TOPIC            = "rdb_sync";

    private static String              PROJECT_ID;
    private static String              PROJECT_ID_STG;
    private static String              JSON_PATH;
    private static String              JSON_PATH_STG;
    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/GCP_project_id.conf" );
            // プロパティファイルを読み込む
            prop.load( propfile );

            PROJECT_ID_STG = prop.getProperty( "project_id_stg" );
            PROJECT_ID = prop.getProperty( "project_id" );
            JSON_PATH_STG = prop.getProperty( "json_path_stg" );
            JSON_PATH = prop.getProperty( "json_path" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "DBSync Static Block Error=" + e.toString() );
        }
        try
        {
            // Logging.info( "TRANSPORT START!" );
            TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }
        catch ( GeneralSecurityException e )
        {
            throw new IllegalStateException( e );
        }
        catch ( IOException e )
        {
            throw new IllegalStateException( e );
        }
        // Logging.info( "TRANSPORT END!" );
    }

    public static final void publish(String message) throws FileNotFoundException, IOException
    {
        publish( message, false );
    }

    /**
     * GCPデータ同期
     * 
     * @param message SQL
     * @param isStg true:GCPステージング環境特定 false:ハピホテ環境ごとに更新
     * @return
     */
    public static final void publish(String message, boolean isStg) throws FileNotFoundException, IOException
    {
        String projectId = "";
        String jsonPath = "";

        // ステージング環境
        if ( isStg )
        {
            projectId = PROJECT_ID_STG;
            jsonPath = JSON_PATH_STG;
        }
        else
        {
            projectId = PROJECT_ID;
            jsonPath = JSON_PATH;
        }
        publish( message, projectId, jsonPath );
    }

    /**
     * GCPのPub/Sub、環境ごとに更新する
     * 
     * @param message SQL
     * @param env 環境
     * @return 処理結果(true:正常,false:異常)
     * @throws IOException
     */
    public static final void publish(String message, String projectId, String jsonPath) throws IOException
    {
        Logging.info( "publish message is: '" + message + "'" );
        String topicId = "projects/" + projectId + "/topics/" + TOPIC;

        // リクエスト内容のオブジェクト作成。APIと同じ形式で作成する
        PubsubMessage pubsubMessage = new PubsubMessage();
        pubsubMessage.setData( Base64.encodeBase64String( message.getBytes( "UTF-8" ) ) );
        PublishRequest content = new PublishRequest();
        content.setMessages( Arrays.asList( pubsubMessage ) );
        // 認証。キーファイルから
        Credential credential = GoogleCredential.fromStream( new FileInputStream( jsonPath ) )
                .createScoped( Collections.singleton( PubsubScopes.PUBSUB ) );
        // Pub/Sub用サービスオブジェクト作成。これがREST APIのリフレクターとして動く
        Pubsub pubsub = new Pubsub.Builder( TRANSPORT, JSON_FACTORY, credential ).setApplicationName( APPLICATION_NAME )
                .build();
        Topics topics = pubsub.projects().topics();
        PublishResponse response = topics.publish( topicId, content ).execute();
        // 結果のロギング。IDが取得できていれば、正しくpublishできているということになる
        for( String id : response.getMessageIds() )
        {
            Logging.info( "published message id: " + id );
        }

    }

    public static void publish(PreparedStatement prestate, boolean needSchema) throws IOException
    {
        publish( prestate, needSchema, false );
    }

    /**
     * almex-happyhotel-booster-devのPub/Sub内にある、rdb_sync topicにSQL文をpublishする
     * Service AccountからJson key fileをdownloadし、環境変数GOOGLE_CREDENTIALS
     * にそのパスを記述してから起動すること 本番のオンプレでは、tomcatの起動スクリプトに環境設定の命令を埋め込むと良い
     * 
     * @throws IOException
     */
    public static void publish(PreparedStatement prestate, boolean needSchema, boolean isStg) throws IOException
    {
        try
        {
            // sql文取得
            String sql = prestate.toString().split( ":", 2 )[1];
            sql = getSQL( sql, needSchema );
            if ( sql != null )
            {
                sql = sql.replace( "INSERT", "REPLACE" );
                publish( sql, isStg );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "DBSync Exception=" + getStackTraceStr( e ) );
        }
    }

    public static String getStackTraceStr(Throwable throwable) throws IOException
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter( stringWriter );
        throwable.printStackTrace( printWriter );

        String stackTrace = stringWriter.toString();

        stringWriter.flush();
        stringWriter.close();
        printWriter.flush();
        printWriter.close();

        return stackTrace;
    }

    public static String getSQL(String sql, boolean needSchema)
    {
        if ( sql != null && needSchema )
        {
            if ( sql.indexOf( "INSERT INTO" ) != -1 )
            {
                sql = sql.replace( "INSERT INTO ", "INSERT INTO hotenavi." );
            }
            else
            {
                sql = sql.replace( "INSERT ", "INSERT hotenavi." );
            }
            sql = sql.replace( "UPDATE ", "UPDATE hotenavi." );
            sql = sql.replace( "DELETE FROM ", "DELETE FROM hotenavi." );
        }
        return sql;
    }

    public static String updateToReplaceSQL(String sql)
    {
        if ( sql != null )
        {
            sql = sql.replace( "UPDATE", "REPLACE" );
            sql = sql.replace( "WHERE", "," );
            sql = sql.replace( "AND", "," );
        }
        return sql;
    }
}
