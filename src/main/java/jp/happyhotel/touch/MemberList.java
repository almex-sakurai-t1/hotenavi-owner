package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.dto.DtoMemberList;
import jp.happyhotel.dto.DtoMemberListData;
import jp.happyhotel.others.HapiTouchErrorMessage;

/**
 * ハピホテタッチカードレスメンバー一覧情報処理クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2018/04/25
 * @see リクエスト：1018電文<br>
 *      レスポンス：1019電文
 */
public class MemberList extends DtoMemberList implements Serializable
{

    private static final long serialVersionUID = -8773588860845751555L;
    private static final int  TIMEOUT          = 10000;
    private static final int  HAPIHOTE_PORT_NO = 7046;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1018";
    final String              REPLY_COMMAND    = "1019";
    String                    header;

    public boolean getData(int hotelId, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        boolean ret = false;
        String paramKind;
        String paramStartDate = "";
        String paramEndDate = "";
        String paramUserId = "0";
        String paramMax = "0";
        String paramIdentifyNo = "0";

        paramKind = request.getParameter( "kind" );
        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );
        paramUserId = request.getParameter( "userId" );
        paramMax = request.getParameter( "max" );
        paramIdentifyNo = request.getParameter( "IdentifyNo" );

        // 開始日のチェック
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }

        // 終了日のチェック
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "99999999";
        }

        if ( (paramUserId == null) || (paramUserId.equals( "" ) != false) )
        {
            paramUserId = "0";
        }

        if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
        {
            paramKind = "0";
        }

        if ( (paramMax == null) || (paramMax.equals( "" ) != false) || (CheckString.numCheck( paramMax ) == false) )
        {
            paramMax = "0";
        }

        if ( (paramIdentifyNo == null) || (paramIdentifyNo.equals( "" ) != false) || (CheckString.numCheck( paramIdentifyNo ) == false) )
        {
            paramIdentifyNo = "0";
        }
        setIdentifyNo( Integer.parseInt( paramIdentifyNo ) );

        ret = getData( hotelId, Integer.parseInt( paramKind ), Integer.parseInt( paramStartDate ), Integer.parseInt( paramEndDate ), Integer.parseInt( paramUserId ), Integer.parseInt( paramMax ), paramIdentifyNo );
        return ret;
    }

    /*
     * all_flag : 0 ･･･1件のみ， 1･･･すべて
     */

    public boolean getData(int hotelId, int kind, int startDate, int endDate, int userSeq, int max, String identifyNo) throws Exception
    {
        Connection connection = null;
        connection = DBConnection.getConnection();
        boolean ret = false;
        try
        {
            ret = getData( connection, hotelId, kind, startDate, endDate, userSeq, max, identifyNo );
        }
        catch ( Exception e )
        {
            Logging.error( "[MemberList.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    public boolean getData(Connection connection, int hotelId, int kind, int startDate, int endDate, int userSeq, int max, String identifyNo) throws Exception
    {
        boolean ret = false;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = -1;

        try
        {
            query = "SELECT";
            query += " c.*,d.user_seq";
            query += " FROM ap_hotel_custom c";
            query += " INNER JOIN hh_user_data_index d ON c.id = d.id AND c.user_id= d.user_id";
            query += " WHERE c.id = ? ";
            query += "   AND c.del_flag = 0";
            query += "   AND c.regist_status = 1";
            if ( kind != 0 )
                query += "   AND c.auto_flag = ?";
            if ( userSeq != 0 )
                query += "   AND d.user_seq = ?";
            query += "   AND c.regist_date BETWEEN  ? AND ?";
            query += "   ORDER BY c.custom_id";
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setInt( i++, hotelId );
            if ( kind != 0 )
                prestate.setInt( i++, kind - 1 );
            if ( userSeq != 0 )
                prestate.setInt( i++, userSeq );
            prestate.setInt( i++, startDate );
            prestate.setInt( i++, endDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                ret = true;
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                    // クラスの配列を用意し、初期化する。
                    dtoMemberListData = new DtoMemberListData[count];
                }
                if ( count > 0 )
                {
                    result.beforeFirst();
                    count = -1;
                    while( result.next() != false )
                    {
                        count++;
                        if ( count >= max && max != 0 )
                        {
                            break;
                        }
                        dtoMemberListData[count] = new DtoMemberListData();
                        dtoMemberListData[count].setUserId( result.getInt( "d.user_seq" ) );
                        dtoMemberListData[count].setRegistDate( result.getInt( "c.regist_date" ) );
                        dtoMemberListData[count].setMemberId( result.getString( "c.custom_id" ) );
                        dtoMemberListData[count].setKind( result.getInt( "c.auto_flag" ) + 1 );
                        dtoMemberListData[count].setReserve( "" );
                    }
                    setMemberCount( max == 0 ? count + 1 : count );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[MemberList.getData] Exception=" + e.toString() );
            setErrorCode( HapiTouchErrorMessage.ERR_30812 );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }

    /****
     * 1018電文送信
     * 
     * @param hotelId ハピホテホテルID
     * @return
     */

    public boolean sendToHost(int hotelId)
    {

        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        int memberCount = 0;
        boolean ret = false;

        // ホスト側データ送信
        tcpclient = new TcpClientEx();
        // 指定のipアドレスに接続
        ret = tcpclient.connectServiceByAddr( HotelIp.getFrontIp( hotelId ), TIMEOUT, HAPIHOTE_PORT_NO );

        if ( ret != false )
        {
            for( int i = 0 ; i < dtoMemberListData.length ; i = i + 100 )
            {
                if ( i >= getMemberCount() )
                    break;
                try
                {
                    sendData = COMMAND;
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( getIdentifyNo() ), 8 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( i / 100 + 1 ), 8 );
                    int sendCount = getMemberCount() - i >= 100 ? 100 : getMemberCount() - i;

                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( sendCount ), 3 );
                    for( int j = 0 ; j < 100 ; j++ )
                    {
                        if ( j < sendCount )
                        {
                            sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoMemberListData[i + j].getUserId() ), 8 );
                            sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoMemberListData[i + j].getRegistDate() ), 8 );
                            sendData += tcpclient.leftFitFormat( dtoMemberListData[i + j].getMemberId(), 9 );
                            sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoMemberListData[i + j].getKind() ), 2 );
                            sendData += tcpclient.leftFitFormat( dtoMemberListData[i + j].getReserve(), 5 );
                        }
                        else
                        {
                            sendData += tcpclient.leftFitFormat( getReserve(), 32 );
                        }
                    }
                    sendData += tcpclient.leftFitFormat( getReserve(), 44 );
                    setHeader( tcpclient.getPacketHapihoteHeader( Integer.toString( hotelId ), sendData.getBytes( "Windows-31J" ).length ) );
                    sendData = getHeader() + sendData;
                    int roop = 0;

                    while( true )
                    {
                        // 電文送信
                        tcpclient.send( sendData );

                        // 受信待機
                        recvData = tcpclient.recv();

                        roop++;
                        if ( recvData.indexOf( "exception" ) >= 0 )
                        {
                            Logging.error( "電文受信Exception " + recvData );
                        }
                        else
                        {
                            charData = new char[recvData.length()];
                            charData = recvData.toCharArray();

                            // コマンド取得
                            data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                            // 応答電文コマンドが1019なら正しい応答
                            if ( data.compareTo( REPLY_COMMAND ) == 0 )
                            {
                                // 返ってきた情報をセット
                                setResult( Integer.valueOf( new String( charData, 36, 2 ) ).intValue() );
                                setErrorCode( Integer.valueOf( new String( charData, 38, 4 ) ).intValue() );
                                // 正常を返して成功とする
                                ret = true;
                                memberCount = memberCount + sendCount;
                            }
                        }
                        if ( roop >= retryCount )
                        {
                            break;
                        }
                    }
                }
                catch ( SocketTimeoutException e )
                {
                    Logging.error( "[MemberList.sendToHost()]Exception:" + e.toString() );
                    // 送信エラーのときは、タイムアウト
                    setErrorCode( HapiTouchErrorMessage.ERR_30813 );
                    ret = false;
                }
                catch ( Exception e )
                {
                    Logging.error( "[MemberList.sendToHost()]Exception:" + e.toString() );
                    setErrorCode( HapiTouchErrorMessage.ERR_30814 );
                    ret = false;
                }
                finally
                {
                }
            }
        }
        else
        {
            setErrorCode( HapiTouchErrorMessage.ERR_30818 );
        }
        setMemberCount( memberCount );
        tcpclient.disconnectService();
        return ret;

    }
}
