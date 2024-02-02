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
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.FileUploader;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataRsvImage;
import jp.happyhotel.owner.FormOwnerRsvImageList.FormOwnerRsvImageListData;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * �摜�ꗗ��� business Logic
 */
public class LogicOwnerRsvImageList implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5492022205679739875L;

    /**
     * �ꗗ�f�[�^�̎擾
     */
    public boolean getData(String loginHotelId, int userId, int hotelId, int statusKind, FormOwnerRsvImageList formDt, int start, int maxCount) throws Exception
    {
        boolean isResult = true;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";
        String queryWhere = "";
        int seq = 0;
        String convDate = "";
        String weekName = "";
        String dispTime;

        try
        {
            query = "SELECT SQL_CALC_FOUND_ROWS g.id, g.image_id, g.original_file_name, g.upload_file_name," +
                    " g.release_file_name, g.upload_date, g.upload_time, g.message, g.status," +
                    " h.name AS hotelName ,  ifnull(u.name, '') as userName" +
                    " FROM hh_rsv_image g " +
                    " INNER JOIN hh_hotel_basic h ON h.id = g.id" +
                    " LEFT JOIN owner_user u ON u.hotelid = g.upload_hotelid AND u.userid = g.upload_user_id";

            if ( hotelId != 0 )
            {
                queryWhere = " WHERE g.id = ?";
            }

            if ( statusKind != 0 )
            {
                if ( queryWhere.length() == 0 )
                {
                    queryWhere = " WHERE g.status = ?";
                }
                else
                {
                    queryWhere += " AND g.status = ?";
                }
            }

            queryWhere += " ORDER BY g.upload_date DESC, g.upload_time DESC LIMIT " + (start - 1) + ", " + maxCount;

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query + queryWhere );
            if ( hotelId != 0 )
            {
                seq++;
                prestate.setInt( seq, hotelId );
            }
            if ( statusKind != 0 )
            {
                seq++;
                prestate.setInt( seq, statusKind );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {

                // ���o
                while( result.next() )
                {
                    convDate = String.valueOf( result.getInt( "upload_date" ) );
                    weekName = DateEdit.getWeekName( result.getInt( "upload_date" ) );

                    FormOwnerRsvImageListData imageDt = formDt.new FormOwnerRsvImageListData();

                    imageDt.setHotelId( result.getInt( "id" ) );
                    imageDt.setImageId( result.getInt( "image_id" ) );
                    imageDt.setMessage( ConvertCharacterSet.convDb2Form( result.getString( "message" ).toString() ) );
                    imageDt.setFileName( ConvertCharacterSet.convDb2Form( result.getString( "original_file_name" ).toString() ) );
                    imageDt.setUpFileName( ConvertCharacterSet.convDb2Form( result.getString( "upload_file_name" ).toString() ) );
                    imageDt.setReleaseFile( ConvertCharacterSet.convDb2Form( result.getString( "g.release_file_name" ).toString() ) );
                    imageDt.setHotelName( ConvertCharacterSet.convDb2Form( result.getString( "hotelName" ).toString() ) );
                    imageDt.setUserName( ConvertCharacterSet.convDb2Form( result.getString( "userName" ).toString() ) );
                    imageDt.setStatus( result.getInt( "status" ) );

                    imageDt.setDispDate( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 ) + "/" +
                            convDate.substring( 6, 8 ) + "(" + weekName + ")" );
                    dispTime = ConvertTime.convTimeStr( result.getInt( "upload_time" ), 3 );
                    imageDt.setDispTime( dispTime );

                    formDt.setImgList( imageDt );
                }

                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( result );

                // ���ہi���~�b�g�Ȃ��̏����j�̌����𓾂�
                query = "SELECT FOUND_ROWS() AS count";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                while( result.next() != false )
                {
                    int resultCount = result.getInt( "count" );
                    formDt.setRecordCount( resultCount );
                    break;
                }

                // �A�����b�N�X�Ј����̎擾
                int imedia_user = OwnerRsvCommon.getImediaFlag( connection, loginHotelId, userId );
                formDt.setImedia_user( imedia_user );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvImageList.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /*
     * ���F�����A�폜����
     */
    public boolean setStatus(int newStatus, HttpServletRequest request, FileUploader fupl, int userId, String ownerHotelId, boolean multicontentFlag) throws Exception
    {
        boolean isResult = false;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String sql;
        String releasePath = "";
        String errMessage = "";
        DataRsvImage drImage = null;
        String[] id = null;
        String[] imageId = null;
        String[] status = null;

        try
        {
            if ( multicontentFlag )
            {
                id = fupl.getParameterValues( "id" );
                imageId = fupl.getParameterValues( "imageId" );
                status = fupl.getParameterValues( "status" );
            }
            else
            {
                id = request.getParameterValues( "id" );
                imageId = request.getParameterValues( "imageId" );
                status = request.getParameterValues( "status" );
            }

            byte[] imageData = null;

            connection = DBConnection.getConnection();
            // �ݒ�擾
            Properties config = new Properties();
            FileInputStream propfile = null;
            // �摜�̊i�[�f�B���N�g�����Z�b�g
            propfile = new FileInputStream( "/etc/happyhotel/planimage.conf" );
            config = new Properties();
            config.load( propfile );

            String imagePath = config.getProperty( "inwork.image.path" );
            String imageRelease = config.getProperty( "release.image.path" );
            propfile.close();

            for( int i = 0 ; i < status.length ; i++ )
            {
                int j = Integer.parseInt( status[i] );

                if ( newStatus == 2 )
                {
                    // ���F����
                    drImage = new DataRsvImage();
                    // �o�^����Ă���t�@�C����DB�����擾
                    drImage.getData( connection, Integer.parseInt( id[j] ), Integer.parseInt( imageId[j] ) );
                    if ( drImage.getId() > 0 && drImage.getImage_id() > 0 )
                    {
                        if ( multicontentFlag )
                        {
                            imageData = fupl.getIndexOfInputData( "uploadfile", j );
                            if ( imageData == null || imageData.length <= 0 )
                            {
                                isResult = false;
                                errMessage = Message.getMessage( "erro.30013", drImage.getUpload_file_name() );
                                break;
                            }
                            else
                            {
                                // �t�@�C���̃A�b�v���[�h
                                releasePath = imageRelease + id[j];
                                // ������΃f�B���N�g�����쐬����
                                File objDir = new File( releasePath );
                                if ( objDir.exists() == false )
                                {
                                    if ( objDir.mkdir() == false )
                                    {
                                        isResult = false;
                                        errMessage = Message.getMessage( "erro.30003", "�f�B���N�g��" );
                                        break;
                                    }
                                }
                                if ( errMessage.length() == 0 )
                                {
                                    StringBuilder release_newname = new StringBuilder();
                                    // yyyyMMddHHmmssSSS�̂Ƃ���܂ł𔲐����ĕ�����擾
                                    for( int k = 0 ; k < drImage.getUpload_file_name().length() ; k++ )
                                    {
                                        if ( drImage.getUpload_file_name().charAt( k ) == '_' )
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            release_newname.append( drImage.getUpload_file_name().charAt( k ) );
                                        }
                                    }
                                    // �t�@�C���R�s�[����
                                    FileOutputStream fos = new FileOutputStream( releasePath + "/" + release_newname.toString() + ".jpg" );
                                    fos.write( imageData );
                                    fos.close();

                                    // �摜���ǂ������`�F�b�N
                                    int width = 0; // ��
                                    int height = 0; // ����
                                    InputStream is = null;
                                    try
                                    {
                                        is = new FileInputStream( releasePath + "/" + release_newname + ".jpg" );
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

                                    if ( width == 0 || height == 0 )
                                    {
                                        errMessage = Message.getMessage( "warn.30016" );
                                        // �t�@�C���̍폜
                                        File objFile = new File( releasePath + "/" + release_newname + ".jpg" );
                                        objFile.delete();
                                        isResult = false;
                                        break;
                                    }
                                    else
                                    {

                                        // FTP UPLOAD
                                        // Logging.info( "FTP_RELEASE_RESULT:" + this.ftp( "172.25.2.101", "hhadmin", "49G7RPQR", "/happyhotel/common/images/plan/release/" + id[j] + "/", release_newname + ".jpg", id[j] ) );
                                        // FTP UPLOAD

                                        // ���������Ȃ̂�newStatus�𐳎��ɂ���DB�X�V���s��
                                        drImage.setRelease_file_name( release_newname + ".jpg" );
                                        drImage.setRelease_file_size( (int)fupl.getIndexOfFileSize( "uploadfile", j ) );
                                        drImage.setRelease_height( height );
                                        drImage.setRelease_width( width );
                                        drImage.setStatus( 3 );
                                        drImage.setUser_id( userId );
                                        drImage.setOwnerHotelId( ownerHotelId );
                                        if ( drImage.updateStatusAll( connection ) )
                                        {
                                            errMessage = Message.getMessage( "info.00004", "���F" );
                                            isResult = true;
                                        }
                                        else
                                        {
                                            errMessage = Message.getMessage( "erro.00002", "���F��DB�X�V" );
                                            isResult = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        errMessage = Message.getMessage( "erro.30001", "���F���t�@�C��" );
                        isResult = false;
                        break;
                    }
                }
                else
                {
                    // �폜����
                    drImage = new DataRsvImage();
                    drImage.setId( Integer.parseInt( id[j] ) );
                    drImage.setImage_id( Integer.parseInt( imageId[j] ) );
                    drImage.setStatus( newStatus );
                    drImage.setUser_id( userId );
                    drImage.setOwnerHotelId( ownerHotelId );

                    boolean sts = drImage.updateStatus( connection );
                    if ( sts )
                    {
                        isResult = true;
                    }
                    if ( sts && newStatus == 9 )
                    {
                        // �t�@�C���̍폜
                        sql = "SELECT upload_file_name, release_file_name FROM hh_rsv_image" +
                                " WHERE id = ? AND image_id = ?";
                        connection = DBConnection.getConnection();
                        prestate = connection.prepareStatement( sql );
                        prestate.setInt( 1, Integer.parseInt( id[j] ) );
                        prestate.setInt( 2, Integer.parseInt( imageId[j] ) );
                        result = prestate.executeQuery();

                        if ( result != null )
                        {
                            while( result.next() )
                            {
                                String uploadFile = result.getString( "upload_file_name" );
                                String releaseFile = result.getString( "release_file_name" );
                                try
                                {
                                    File objFile = new File( imagePath + id[j] + "/" + uploadFile );
                                    objFile.delete();
                                }
                                catch ( Exception e )
                                {
                                }

                                if ( releaseFile.length() > 0 )
                                {
                                    try
                                    {
                                        File objFile1 = new File( imageRelease + id[j] + "/" + releaseFile );
                                        objFile1.delete();
                                    }
                                    catch ( Exception e )
                                    {
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // ���ڏ��F�̂Ƃ��ɃG���[�ɂȂ�̂�
            if ( result != null )
            {
                request.setAttribute( "errMsgDetail", errMessage );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvImageList.setStatus] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;

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
    private int ftp(String host, String user, String password, String path, String filename, String id)
    {

        int returnInt = 99;
        FTPClient fp = new FTPClient();
        FileInputStream is = null;
        final String FTP_PATH = "/common/images/plan/release/";
        try
        {

            Logging.info( "FTP LOADED" );

            fp.connect( host );
            if ( !FTPReply.isPositiveCompletion( fp.getReplyCode() ) )
            { // �R�l�N�g�ł������H
                returnInt = 1;
            }
            if ( fp.login( user, password ) == false )
            { // ���O�C���ł������H
                returnInt = 2;
            }
            fp.enterLocalPassiveMode();

            Logging.info( path + filename );

            // �t�@�C�����M
            is = new FileInputStream( path + filename );// �N���C�A���g��
            System.out.println( path + filename );

            fp.makeDirectory( path );
            if ( fp.storeFile( FTP_PATH + id + "/" + filename, is ) )// �T�[�o�[��
            {
                Logging.info( "FTP UPLOAD RELEASE OK:" + path + filename );
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
                // TODO �����������ꂽ catch �u���b�N
                Logging.info( "[FTP.ftp()] disconnect IOException:" + e.toString() );
            }
        }
        return returnInt;
    }
}
