package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;

/**
 * プラン画像選択ビジネスロジック
 */
public class LogicOwnerRsvPlanImage implements Serializable
{
    private static final long     serialVersionUID = 4479081893163688415L;

    private FormOwnerRsvPlanImage frm;

    /* フォームオブジェクト */
    public FormOwnerRsvPlanImage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlanImage frm)
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
        ArrayList<String> imgOriginalList = new ArrayList<String>();

        query = query + "SELECT release_file_name,original_file_name FROM hh_rsv_image ";
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
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanImage.getFile] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }
}
