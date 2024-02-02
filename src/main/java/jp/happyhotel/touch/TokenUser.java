package jp.happyhotel.touch;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApPushDataList;
import jp.happyhotel.data.DataApTokenUser;
import jp.happyhotel.data.DataApUserPushConfig;

/**
 * ホテルチェックイン情報クラス
 *
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class TokenUser implements Serializable
{
    final int         TYPE = 2;
    final int         ANDROID = 2;
    final int         PUSH    = 1;
    final String      configFilePath = "//etc//happyhotel//push.conf";

    DataApTokenUser[] tokenList;
    int               tokenCount;
    int               maxPushSeq;

    public TokenUser()
    {
        this.tokenList = null;
        this.tokenCount = 0;
    }

    public DataApTokenUser[] getTokenList()
    {
        return tokenList;
    }

    public void setTokenList(DataApTokenUser[] tokenList)
    {
        this.tokenList = tokenList;
    }

    public int getTokenCount()
    {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount)
    {
        this.tokenCount = tokenCount;
    }

    /***
     * トークンリストを取得
     *
     * @param osType（1:iPhone、2:Android）
     * @param campaignFlag
     * @return
     */
    public boolean getTokenUserList(int osType, int campaignFlag)
    {

        boolean ret = false;
        int i;
        int count;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;

        // 24時間前の日付と時刻を取得

        query = "SELECT * FROM ap_token_user ATU";
        query += " INNER JOIN ap_user_push_config AUPC ON ATU.user_id = AUPC.user_id";
        // 通知フラグが通知OKの場合のみ
        query += " AND AUPC.push_flag = 1";

        // キャンペーン情報を送る場合にのみ使用
        if ( campaignFlag == 1 )
        {
            query += " AND AUPC.campaign_flag = 1";
        }

        // osTypeを指定する場合に使用
        if ( osType > 0 )
        {
            query += " WHERE os_type = ?";
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( osType > 0 )
            {
                prestate.setInt( 1, osType );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.tokenCount = result.getRow();
                }
                result.beforeFirst();

                // クラスの配列を用意し、初期化する。
                this.tokenList = new DataApTokenUser[this.tokenCount];
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.tokenList[count] = new DataApTokenUser();
                    this.tokenList[count].setData( result );
                    count++;
                }
                // データが1件以上あればtrue
                if ( count > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TokenUser.getTokenUser()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * チェックアウト用PUSH通知
     *
     * @param userId
     * @param id
     * @return
     */
    public boolean sendCheckOutMessage(String userId, int id)
    {
        boolean ret = false;
        int hotel_id = 0;
        int hotel_seq = 0;
        String msg = "";
        String url = "";
        String deviceToken = "";
        DataApTokenUser datu = new DataApTokenUser();
        DataApUserPushConfig daupc = new DataApUserPushConfig();
        PushHotel ph = new PushHotel();
        Push2Android p2a = new Push2Android();
        Push2Iphone p2i = new Push2Iphone();
        GooglePushMapper gpm = new GooglePushMapper();

        try
        {
            // URL情報を取得
            FileInputStream propfile = null;
            Properties config;
            String      Urlhotel="";
            try
            {
                propfile = new FileInputStream( configFilePath );
                config = new Properties();
                config.load( propfile );
                Urlhotel = String.valueOf( config.getProperty( "urlhotel" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[TokenUser]confファイルからURL情報を取得に失敗しました Exception:" + e.toString() );
                return false;
            }

            if( ph.getData( id ))
            {
                //Logging.info( "ph.getData( id ) ret = " + ret ,"TokenUser sendCheckOutMessage" );
                ret = daupc.getData( userId );
                ret = datu.getDataLastLoginByUserId( userId );

                msg = ph.getPushHotel().getDetail();
                hotel_seq = ph.getPushHotel().getSeq();
                hotel_id = ph.getPushHotel().getId();

                if ( ret != false && daupc.getPushFlag() == PUSH && daupc.getCoFlag() == PUSH )
                {
                    //プッシュ配信データリスト(ap_push_data_list)作成
                    ret = this.insertPushDataList( TYPE,id, 0, userId, datu.getToken() ,hotel_id, hotel_seq);
                    if(ret)
                    {
                        //作成したプッシュ配信データリスト(ap_push_data_list)のpush_seq取得
                        ret = this.getPushSeq( TYPE, id, userId, datu.getToken() ,hotel_id, hotel_seq);
                        if(ret)
                        {
                            //jspを開いたときにap_push_data_listのconfirmdate等を更新するため、キー項目を送る
                            String param = "";
                            param += "?type=" + TYPE;
                            param += "&id=" + id;
                            param += "&push_seq=" + maxPushSeq;
                            param += "&token=" + datu.getToken();
                            param += "&hotel_id=" + hotel_id;
                            param += "&hotel_seq=" + hotel_seq;
                            url = Urlhotel + param;

                            //Logging.info( "TokenUser.sendCheckOutMessage:user_id=" + userId + ", token=" + datu.getToken() +" osType=" +datu.getOsType() + " msg="+msg);

                            if ( datu.getOsType() == ANDROID )
                            {
                                // p2a.push( regIdList, paramMessage, paramUrl );
                                gpm.addRegId( datu.getToken() );
                                gpm.createData( msg, url );
                                if( p2a.push( gpm ))
                                {
                                    //ap_push_data_list更新
                                    if(!this.updatePushDataList(TYPE, id, maxPushSeq, userId, datu.getToken()))
                                    {
                                        Logging.error( "[sendCheckoutMessage()] ap_push_data_list更新失敗","TokenUser" );
                                        throw new Exception();
                                    }
                                }
                            }
                            else
                            {

                                if( p2i.push( datu.getToken(), msg ,url))
                                {
                                  //ap_push_data_list更新
                                    if(!this.updatePushDataList(TYPE, id, maxPushSeq, userId, datu.getToken()))
                                    {
                                        Logging.error( "[sendCheckoutMessage()] ap_push_data_list更新失敗","TokenUser" );
                                        throw new Exception();
                                    }
                                }
                            }
                        }
                     }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[sendCheckoutMessage()] Exception=" + e.toString(),"TokenUser" );
            return(false);
        }
        return ret;
    }

    /****
     * PUSH通知データ登録
     *
     * @param id
     * @param pushSeq
     * @param userId
     * @param token
     * @return
     */
    public boolean insertPushDataList(int type, int id, int pushSeq, String userId, String token, int hotelId, int hotelSeq)
    {
        boolean ret = false;
        DataApPushDataList dapdl = new DataApPushDataList();
        dapdl.setType( type );
        dapdl.setId( id );
        dapdl.setPushSeq( pushSeq );
        dapdl.setUserId( userId );
        dapdl.setToken( token );
        dapdl.setStatus( 0 );
        dapdl.setSendDate( 0 );
        dapdl.setSendTime( 0 );
        dapdl.setHotelId( hotelId );
        dapdl.setHotelSeq( hotelSeq );
        ret = dapdl.insertData();

        return ret;
    }

    /****
     * PUSH通知データpush_seq取得
     *
     * @param id
     * @param pushSeq
     * @param userId
     * @param token
     * @param hotelId
     * @param hotelSeq
     * @return
     */
    public boolean getPushSeq(int type, int id, String userId, String token, int hotelId, int hotelSeq)
    {
        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;

        // プッシュ配信データリスト(ap_push_data_list)からpush_seqを取得する

        query = "SELECT max(push_seq) as MAXPushSeq FROM ap_push_data_list";
        query += " WHERE type=? AND id= ? AND user_id=? AND token=? AND hotel_id=? AND hotel_seq=?";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, id );
            prestate.setString( 3, userId );
            prestate.setString( 4, token );
            prestate.setInt( 5, hotelId );
            prestate.setInt( 6, hotelSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.maxPushSeq = result.getInt( "MAXPushSeq" );

                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[TokenUser.getPushSeq()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * PUSH通知データ更新
     *
     * @param id
     * @param pushSeq
     * @param userId
     * @param token
     * @return
     */
    public boolean updatePushDataList(int type, int id, int pushSeq, String userId, String token)
    {
        boolean ret = false;
        DataApPushDataList dapdl = new DataApPushDataList();
        if(dapdl.getData( type, id, pushSeq, userId, token ))
        {
            dapdl.setStatus( 1 );//送信済み
            dapdl.setSendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dapdl.setSendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if(dapdl.updateData( type, id, pushSeq, userId, token ))
            {
               ret=true;
            }
        }
        else
        {
            ret=false;
        }

        return ret;
    }
}
