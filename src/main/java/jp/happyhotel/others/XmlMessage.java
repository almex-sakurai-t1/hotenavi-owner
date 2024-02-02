package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSystemMessage;

/**
 * XMLメニューデータ
 * メニューを取得する
 * 
 * @author S.Tashiro
 * @version 1.00 2012/10/15
 */
public class XmlMessage implements Serializable
{
    private int                 msgCount;
    private DataSystemMessage[] msgData;

    /**
     * データを初期化します。
     */
    public XmlMessage()
    {
        msgCount = 0;
    }

    /** ユーザ基本情報件数取得 **/
    public int getCount()
    {
        return(msgCount);
    }

    /** ユーザ基本情報取得 **/
    public DataSystemMessage[] getMessageDataInfo()
    {
        return(msgData);
    }

    /***
     * メッセージ取得
     * 
     * @param kind
     * @param dispCount
     * @return
     */
    public boolean getMessage(int kind, int dispCount)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int today = Integer.parseInt( DateEdit.getDate( 2 ) );

        if ( kind < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_system_message";
        query += " WHERE kind = ?";
        query += " AND start_date <= ? ";
        query += " AND end_date >= ? ";
        query += " AND del_flag = 0 ";
        query += " ORDER BY seq DESC";
        query += " Limit 0, ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, today );
            prestate.setInt( 3, today );
            prestate.setInt( 4, dispCount );
            ret = getMessageSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[XmlMessage.getMenu()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);

    }

    /**
     * メッセージのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMessageSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    msgCount = result.getRow();
                }
                this.msgData = new DataSystemMessage[this.msgCount];

                for( i = 0 ; i < msgCount ; i++ )
                {
                    msgData[i] = new DataSystemMessage();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.msgData[count].setData( result );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[XmlMessage.getMenuSub()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( msgCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

}
