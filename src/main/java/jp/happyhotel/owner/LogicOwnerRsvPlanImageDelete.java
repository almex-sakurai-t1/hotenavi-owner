package jp.happyhotel.owner;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.data.DataRsvImage;

/**
 * プラン画像選択ビジネスロジック
 */
public class LogicOwnerRsvPlanImageDelete implements Serializable
{
    private static final long           serialVersionUID = 4479081893163688415L;

    private FormOwnerRsvPlanImageDelete frm;

    /* フォームオブジェクト */
    public FormOwnerRsvPlanImageDelete getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlanImageDelete frm)
    {
        this.frm = frm;
    }

    /**
     * プラン画像情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getFile() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        ArrayList<String> imgFileList = new ArrayList<String>();
        ArrayList<String> imgIdList = new ArrayList<String>();
        ArrayList<String> imgOriginalList = new ArrayList<String>();

        query = query + "SELECT release_file_name, image_id,original_file_name FROM hh_rsv_image ";
        query = query + "WHERE id = ? AND status = 3 ";
        query = query + "ORDER BY release_file_name";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                imgFileList.add( result.getString( "release_file_name" ) );
                imgOriginalList.add( result.getString( "original_file_name" ) );
                imgIdList.add( result.getString( "image_id" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "選択可能なプラン画像" ) );
                return;
            }

            // Formに値をセット
            frm.setImgFileNmList( imgFileList );
            frm.setImgOriginalNmList( imgOriginalList );
            frm.setImgIdList( imgIdList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanImageDelete.getFile] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プラン画像削除処理
     * 
     * @param inworkPath 画像パス
     * @param deleteImagePath 表示用の画像パス
     * @param hotelId ホテルID
     * @param image_id 画像ID
     * @param userId ユーザID
     * @param ownerHotelId オーナーサイトホテルID
     * @return なし
     */
    public boolean deletePlanImage(String inworkPath, String deleteImagePath, int hotelId, String image_id, int userId, String ownerHotelId)
    {
        Connection connection = null;
        String sql = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        boolean ret = false;

        try
        {
            DataRsvImage drImage = new DataRsvImage();
            drImage.setId( hotelId );
            drImage.setImage_id( Integer.parseInt( image_id ) );
            drImage.setStatus( 9 );
            drImage.setUser_id( userId );
            drImage.setOwnerHotelId( ownerHotelId );

            connection = DBConnection.getConnection();
            // DB上のステータスを削除に変更
            boolean sts = drImage.updateStatus( connection );
            // 実際にファイルを削除
            if ( sts == true )
            {
                ret = true;
                sql = "SELECT upload_file_name, release_file_name FROM hh_rsv_image" +
                        " WHERE id = ? AND image_id = ?";
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( sql );
                prestate.setInt( 1, hotelId );
                prestate.setInt( 2, Integer.parseInt( image_id ) );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    while( result.next() )
                    {
                        String uploadFile = result.getString( "upload_file_name" );
                        String releaseFile = result.getString( "release_file_name" );
                        try
                        {
                            File objFile = new File( inworkPath + hotelId + "/" + uploadFile );
                            objFile.delete();
                        }
                        catch ( Exception e )
                        {
                        }

                        if ( releaseFile.length() > 0 )
                        {
                            try
                            {
                                File objFile1 = new File( deleteImagePath + hotelId + "/" + releaseFile );
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
            else
            {
                frm.setErrMsg( Message.getMessage( "erro.30002", "選択したプラン画像の削除" ) );
                return(ret);
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanImageDelete.deletePlanImage] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
