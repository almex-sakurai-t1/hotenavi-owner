package jp.happyhotel.owner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.FileUploader;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataRsvImage;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * 画像アップロード画面 business Logic
 */
public class LogicOwnerRsvImageUpLoad implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID  = 3242328608998465050L;

    private static final String PLAN_IMAGE_CONF   = "/etc/happyhotel/planimage.conf";
    private static final String IN_WORK_IMAGE_URL = "inwork.image.url";
    private static final String IMAGE_KEY         = "inwork.image.path";
    private static final String MAIL_TO           = "mail.to";
    private static final String MAIL_TITLE        = "mail.title";
    private static final String IMAGE_HTML        = "uploadfile";                    // HMTLのアップロードファイル名

    private String              errMsg            = "";
    private String              message           = "";
    private String              upFileName        = "";
    private String              cmd               = "";
    private FileUploader        fupl;

    /**
     * 初期化
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public boolean init(HttpServletRequest request) throws Exception
    {
        boolean isResult = true;

        fupl = new FileUploader();
        try
        {
            if ( fupl.setData( request ) == false )
            {
                isResult = false;
                errMsg = Message.getMessage( "erro.30002", "画像アップロード" );
                return isResult;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvImageUpLoad.init] Exception=" + e.toString() );
            throw e;
        }

        return isResult;
    }

    /*
     * 項目値の取得
     */
    public String getParam(String param)
    {
        String data;

        data = fupl.getParameter( param );
        if ( data == null )
        {
            data = "";
        }
        return data;
    }

    /**
     * 入力チェック
     * 
     * @return
     * @throws Exception
     */
    public boolean check() throws Exception
    {
        boolean isResult = true;

        try
        {
            // アップロードかどうかチェック
            cmd = fupl.getParameter( "UPLOAD" );
            if ( cmd.length() == 0 )
            {
                return true;
            }

            upFileName = fupl.getFileName( IMAGE_HTML );
            if ( upFileName == null || upFileName.length() == 0 )
            {
                isResult = false;
                errMsg = Message.getMessage( "warn.00040", "ファイル" );
            }
            else
            {
                if ( upFileName.toLowerCase().matches( ".*jpg.*" ) == false && upFileName.toLowerCase().matches( ".*jpeg.*" ) == false &&
                        upFileName.toLowerCase().matches( ".*gif.*" ) == false && upFileName.toLowerCase().matches( ".*png.*" ) == false )
                {
                    isResult = false;
                    errMsg = Message.getMessage( "warn.00040", "画像ファイル" );
                }
            }

            if ( isResult == true )
            {
                long fileSize = fupl.getFileSize( IMAGE_HTML );
                if ( fileSize > FormOwnerRsvImageUpLoad.MAX_POST_SIZE )
                {
                    errMsg = Message.getMessage( "warn.30015" );
                }
                if ( fileSize == 0 )
                {
                    errMsg = Message.getMessage( "warn.30014" );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvImageUpLoad.check] Exception=" + e.toString() );
            throw e;
        }
        return isResult;

    }

    /**
     * アップロード処理
     * 
     * @param ownerHotelId
     * @param loginHotelId
     * @param hotelId
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean upload(String ownerHotelId, String loginHoteId, int hotelId, int userId) throws Exception
    {
        boolean isResult = true;
        Connection connection = null;
        Properties config = new Properties();
        FileInputStream propfile = null;
        LogicOwnerRsvImageList logic = null;

        try
        {
            // configから、イメージディレクトリを得る
            propfile = new FileInputStream( PLAN_IMAGE_CONF );
            config = new Properties();
            config.load( propfile );

            String imagePath = config.getProperty( IMAGE_KEY );
            String imageUrl = config.getProperty( IN_WORK_IMAGE_URL );
            String mailTo = config.getProperty( MAIL_TO );
            String mailTitle = config.getProperty( MAIL_TITLE );
            mailTitle = new String( mailTitle.getBytes( "ISO-8859-1" ), "Windows-31J" );

            propfile.close();

            // ディレクトリの存在チェック
            imagePath = imagePath + hotelId;
            imageUrl += hotelId;

            // 無ければディレクトリを作成する

            File objDir = new File( imagePath );
            if ( objDir.exists() == false )
            {
                if ( objDir.mkdir() == false )
                {
                    isResult = false;
                    errMsg = Message.getMessage( "erro.30003", "ディレクトリ" );
                }
            }
            if ( errMsg.length() == 0 )
            {
                //
                Date date1 = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat( "yyyyMMddHHmmssSSS" );
                String ymdhms = sdf1.format( date1 );
                String original_file = fupl.getFileName( IMAGE_HTML );
                ymdhms = ymdhms + "_" + original_file;

                FileOutputStream fos = new FileOutputStream( imagePath + "/" + ymdhms );
                fos.write( fupl.getInputData( IMAGE_HTML ) );
                fos.close();

                // 画像かどうかをチェック
                int width = 0; // 幅
                int height = 0; // 高さ
                InputStream is = null;
                try
                {
                    is = new FileInputStream( imagePath + "/" + ymdhms );
                    BufferedImage img = ImageIO.read( is );
                    if ( img != null )
                    {
                        width = img.getWidth();
                        height = img.getHeight();
                    }

                }
                catch ( IOException e )
                {
                }
                finally
                {
                    if ( is != null )
                        try
                        {
                            is.close();
                        }
                        catch ( IOException e )
                        {
                        }
                }

                // FTPで画像をアップロードする
                // Logging.info( "FTP_TEST" );
                // Logging.info( "FTP_RESULT:" + this.ftp( "172.25.2.101", "hhadmin", "49G7RPQR", "/happyhotel/common/images/plan/inwork/" + hotelId + "/", ymdhms, hotelId ) );
                // FTPで画像をアップロードする

                if ( width == 0 || height == 0 )
                {
                    errMsg = Message.getMessage( "warn.30016" );
                    // ファイルの削除
                    File objFile = new File( imagePath + "/" + ymdhms );
                    objFile.delete();
                    isResult = false;
                    return isResult;
                }

                // DBにレコードを保存する
                connection = DBConnection.getConnection();

                DataRsvImage drImage = new DataRsvImage();
                drImage.setId( hotelId );
                drImage.setOriginal_file_name( ConvertCharacterSet.convForm2Db( original_file ) );
                drImage.setUpload_file_name( ConvertCharacterSet.convForm2Db( ymdhms ) );
                drImage.setOriginal_file_size( (int)fupl.getFileSize( IMAGE_HTML ) );
                drImage.setOriginal_width( width );
                drImage.setOriginal_height( height );
                drImage.setUpload_hotelid( loginHoteId );
                drImage.setUpload_user_id( userId );
                drImage.setUser_id( userId );
                drImage.setMessage( "" );
                drImage.setOwnerHotelId( ownerHotelId );

                drImage.insertData( connection );

                drImage.getData( hotelId, ConvertCharacterSet.convForm2Db( ymdhms ) );

                // 承認処理
                logic = new LogicOwnerRsvImageList();
                fupl.setParameter( "imageId", String.valueOf( drImage.getImage_id() ) );
                if ( logic.setStatus( 2, null, fupl, userId, ownerHotelId, true ) == false )
                {
                    isResult = false;
                    errMsg += Message.getMessage( "erro.30002", "アップロード" );
                }

                // メール送信処理
                int imedia_user = OwnerRsvCommon.getImediaFlag( connection, loginHoteId, userId );

                // Logging.info("LogicOwnerRsvImageUpLoad: hotelid=" + loginHoteId + ", userId = " + userId + ", imedia_user= " + imedia_user);

                if ( imedia_user != 1 ) // アルメックス社員はメールを送信しない
                {
                    if ( sendUploadMail( connection, drImage, imageUrl, loginHoteId, mailTo, mailTitle ) == false )
                    {
                        errMsg = Message.getMessage( "erro.30002", "メール送信" );
                    }
                }
                DBConnection.releaseResources( connection );

                errMsg += Message.getMessage( "info.00004", "アップロード" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvImageUpLoad.upload] Exception=" + e.toString() );
            throw e;

        }

        return isResult;
    }

    /**
     * メールの送信
     * 
     * @return
     */
    private boolean sendUploadMail(Connection connection, DataRsvImage drImage, String imagePath, String loginHotelId, String mailTo, String mailTitle)
    {
        boolean isResult = true;
        PreparedStatement prestate = null;
        ResultSet result = null;

        int id = drImage.getId();
        int userId = drImage.getUpload_user_id();

        // 現在日時
        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
        String date_format = sdf1.format( date1 );

        try
        {
            // 投稿ホテル名の取得
            String hotelname = "";
            String query = "SELECT * FROM hh_hotel_basic WHERE id = ?";

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                hotelname = result.getString( "name" );
            }
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );

            // 投稿担当者名の取得
            String username = "";
            String mailaddr_user = "";
            query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, loginHotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            if ( result.next() != false )
            {
                username = result.getString( "name" );
                mailaddr_user = result.getString( "mailaddr_pc" );
            }
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );

            if ( mailaddr_user.compareTo( "" ) == 0 )
            {
                mailaddr_user = mailTo;
            }

            String title_mail = mailTitle + "(" + loginHotelId + ")";
            String text = date_format + "\r\n";
            text = text + "［グループ／ホテル名］" + "\r\n";
            text = text + hotelname + "(" + id + ")" + "\r\n";
            text = text + "［担当者名］" + "\r\n";
            text = text + username + "(" + userId + ")" + "\r\n";
            text = text + "=====================" + "\r\n";
            text = text + "［元画像名］" + drImage.getOriginal_file_name() + "\r\n";
            text = text + "［画 像 名］" + "https://happyhotel.jp" + imagePath + "/" + drImage.getUpload_file_name() + "\r\n";
            // text = text + "［画像説明］" + (String)params.get("subject") + "\r\n";
            text = text + "［大 き さ］" + drImage.getOriginal_width() + " × " + drImage.getOriginal_height() + "\r\n";

            if ( drImage.getOriginal_file_size() > 1000 )
            {
                text = text + "［サ イ ズ］" + (drImage.getOriginal_file_size() / 1024) + " KB\r\n";
            }
            else
            {
                text = text + "［サ イ ズ］" + drImage.getOriginal_file_size() + " バイト\r\n";
            }
            text = text + "=====================" + "\r\n";
            text = text + "［連絡事項］" + "\r\n" + drImage.getMessage() + "\r\n";

            // メール送信を行う
            SendMail.send( mailaddr_user, mailTo, title_mail, text );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvImageUpLoad.sendUploadMail] Exception=" + e.toString() );
            isResult = false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return isResult;
    }

    /**
     * getter
     */
    public String getErrMsg()
    {
        return errMsg;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUpFileName()
    {
        return upFileName;
    }

    public String getCmd()
    {
        return cmd;
    }

    /***
     * 
     * @param host
     * @param user
     * @param password
     * @param path
     * @param filename
     * @return
     */
    private int ftp(String host, String user, String password, String path, String filename, int id)
    {

        int returnInt = 99;
        FTPClient fp = new FTPClient();
        FileInputStream is = null;
        final String FTP_PATH = "/common/images/plan/inwork/";
        try
        {

            Logging.info( "FTP LOADED" );

            fp.connect( host );
            if ( !FTPReply.isPositiveCompletion( fp.getReplyCode() ) )
            { // コネクトできたか？
                returnInt = 1;
            }
            if ( fp.login( user, password ) == false )
            { // ログインできたか？
                returnInt = 2;
            }
            fp.enterLocalPassiveMode();

            Logging.info( "FTP:" + path + filename );
            Logging.info( "FTP:" + FTP_PATH + id + "/" + filename );

            // ファイル送信
            is = new FileInputStream( path + filename );// クライアント側

            fp.makeDirectory( path );
            if ( fp.storeFile( FTP_PATH + filename, is ) )// サーバー側
            {
                Logging.info( "FTP UPLOAD INWORK OK:" + path + filename );
                returnInt = 0;
            }
            else
            {
                returnInt = 3;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[ActionYWalletDiffUser.ftp()] Exception:" + e.toString() );
        }
        finally
        {
            try
            {
                fp.disconnect();
            }
            catch ( IOException e )
            {
                // TODO 自動生成された catch ブロック
                Logging.info( "[FTP.ftp()] disconnect IOException:" + e.toString() );
            }
        }
        return returnInt;
    }
}
