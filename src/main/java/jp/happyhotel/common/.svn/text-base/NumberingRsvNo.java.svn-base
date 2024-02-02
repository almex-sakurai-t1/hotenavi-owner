package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 予約番号取得クラス
 * ホテル毎の最新の予約番号を返す
 * 
 * @author H.Takanami
 * @version 1.00 2011/01/18
 * @see
 */
public class NumberingRsvNo implements Serializable
{

    private static final long   serialVersionUID = -872593932481322974L;
    private static final String INIT_PREFIX      = "A";
    private static final int    INIT_SEQ         = 1;

    /***
     * 予約番号取得
     * 
     * @param hotelID ホテルID
     * @return 予約番号
     **/
    public static String getRsvNo(int hotelID)
    {
        String query = "";
        Connection conn = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;
        String curPrefixChar = "";
        String newPrefixChar = "";
        int curSeqNum = 0;
        int newSeqNum = 0;
        String newRsvNo = null;
        String[] RsvNoAndSeq = new String[2];

        try
        {
            conn = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();

            // 現在の接頭語取得
            curPrefixChar = getCurPrefixChar( hotelID, conn, prestate );

            // 現在の連番取得
            curSeqNum = getCurSeqNum( hotelID, conn, prestate );

            if ( curSeqNum == 0 )
            {
                // 新規追加
                ret = insRsvNo( hotelID, conn, prestate );
                newPrefixChar = INIT_PREFIX;
                newSeqNum = INIT_SEQ;

            }
            else
            {
                // 更新
                // 次の接頭語取得
                newPrefixChar = getNextPrefixChar( curPrefixChar, curSeqNum );

                // 次の連番取得
                newSeqNum = getNextSeqNum( curSeqNum );
            }

            // 新しい予約番号取得
            if ( newPrefixChar.trim().length() != 0 )
            {
                RsvNoAndSeq = getCheckDigitRsvNo( newPrefixChar, hotelID, newSeqNum, conn );
                newRsvNo = RsvNoAndSeq[0];
                newSeqNum = Integer.parseInt( RsvNoAndSeq[1] );
                ret = updRsvNo( hotelID, conn, prestate, newPrefixChar, newSeqNum );
            }

            if ( ret )
            {
                query = "COMMIT ";
                prestate = conn.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = conn.prepareStatement( query );
                result = prestate.executeQuery();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.getRsvNo() ] " + e.getMessage() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return(newRsvNo);
    }

    /***
     * チェックディジット予約番号取得
     * 
     * @param prefixchar 冠文字
     * @param hotelid ホテルID
     * @param seqnum シーケンス番号
     * 
     * @return 予約番号
     * 
     **/
    private static String[] getCheckDigitRsvNo(String prefixchar, int hotelid, int seqnum, Connection conn)
    {
        String ret = null;
        String hoterstr = "";
        int even = 0;
        int even2 = 0;
        int odd = 0;
        int odd2 = 0;
        String strOdd = "";
        String seqstr = "";
        int count = 0;
        String[] RsvNoAndSeq = new String[2];

        try
        {
            // ホテルIDのチェックディジット
            hoterstr = String.valueOf( hotelid );
            while( true )
            {
                count = 1;
                for( int i = hoterstr.length() - 1 ; i >= 0 ; i-- )
                {
                    if ( (count) % 2 == 0 )
                    {
                        // 偶数
                        even += Integer.parseInt( "" + hoterstr.charAt( i ) );
                    }
                    else
                    {
                        // 奇数
                        odd += Integer.parseInt( "" + hoterstr.charAt( i ) );
                    }
                    count++;
                }
                seqstr = String.valueOf( seqnum );
                count = 1;
                for( int i = seqstr.length() - 1 ; i >= 0 ; i-- )
                {
                    if ( (count) % 2 == 0 )
                    {
                        // 偶数
                        even2 += Integer.parseInt( "" + seqstr.charAt( i ) );
                    }
                    else
                    {
                        // 奇数
                        odd2 += Integer.parseInt( "" + seqstr.charAt( i ) );
                    }
                    count++;
                }
                // odd * odd2は0になった場合に、2桁目に行かない場合があるため、0埋めを行う。
                strOdd = String.format( "%1$02d", odd * odd2 );

                // 掛け合わせた偶数桁の1の位を予約番号の1の位に、掛け合わせた奇数桁の1の位を予約番号の10の位にする
                ret = prefixchar + hotelid + "-" + String.format( "%1$04d", seqnum ) + strOdd.charAt( 1 ) + String.valueOf( even * even2 ).charAt( 0 );
                boolean isRsvExsit = isRsvNoExsit( ret, conn );
                if ( isRsvExsit )
                {
                    seqnum++;
                    continue;
                }
                else
                {
                    break;
                }
            }
            RsvNoAndSeq[0] = ret;
            RsvNoAndSeq[1] = String.valueOf( seqnum );
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.checkDigitRsvNo() ] " + e.getMessage() );
        }
        finally
        {
        }

        return(RsvNoAndSeq);
    }

    /**
     * 現在の接頭語取得
     * 
     * @param hotelId ホテルID
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @return 現在登録されている接頭語
     */
    private static String getCurPrefixChar(int hotelID, Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        ResultSet result = null;
        String curPrefixChar = "";

        query = query + "SELECT prefix_char FROM hh_rsv_reserve_number ";
        query = query + "WHERE id = ? ";
        query = query + "FOR UPDATE";

        try
        {
            // conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            result = prestate.executeQuery();

            while( result.next() )
            {
                curPrefixChar = result.getString( "prefix_char" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.getCurPrefixChar] Exception=" + e.toString() );
            throw new Exception( "[NumberingRsvNo.getCurPrefixChar] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(curPrefixChar);
    }

    /**
     * 現在の連番取得
     * 
     * @param hotelId ホテルID
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @return 現在登録されている連番
     */
    private static int getCurSeqNum(int hotelID, Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int curSeqNum = 0;

        query = query + "SELECT seq_num FROM hh_rsv_reserve_number ";
        query = query + "WHERE id = ? ";

        try
        {
            // conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            result = prestate.executeQuery();

            while( result.next() )
            {
                curSeqNum = result.getInt( "seq_num" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.getCurSeqNum] Exception=" + e.toString() );
            throw new Exception( "[NumberingRsvNo.getCurSeqNum] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(curSeqNum);
    }

    /**
     * 予約番号新規追加
     * 
     * @param hotelId ホテルID
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @return true:処理成功、false:処理失敗
     */
    private static boolean insRsvNo(int hotelID, Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        int result = 0;
        boolean ret = false;

        query = query + "INSERT hh_rsv_reserve_number SET ";
        query = query + "id = ?, ";
        query = query + "prefix_char = ?, ";
        query = query + "seq_num = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setString( 2, "A" );
            prestate.setInt( 3, 1 );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.insRsvNo] Exception=" + e.toString() );
            throw new Exception( "[NumberingRsvNo.insRsvNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * 予約番号更新
     * 
     * @param hotelId ホテルID
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newPrefixChar 新しい接頭語
     * @param newSeqNum 新しい連番
     * @return true:正常、false:失敗
     */
    private static boolean updRsvNo(int hotelID, Connection conn, PreparedStatement prestate, String newPrefixChar, int newSeqNum) throws Exception
    {
        String query = "";
        int result = 0;
        boolean ret = false;

        query = query + "UPDATE hh_rsv_reserve_number SET ";
        query = query + "prefix_char = ?, ";
        query = query + "seq_num = ? ";
        query = query + "WHERE id = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, newPrefixChar );
            prestate.setInt( 2, newSeqNum );
            prestate.setInt( 3, hotelID );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.updRsvNo] Exception=" + e.toString() );
            throw new Exception( "[NumberingRsvNo.updRsvNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * 次の接頭語を取得する
     * 
     * @param prefixChar 現在の接頭語
     * @param seqNum 現在の連番
     * @return 接頭語
     */
    private static String getNextPrefixChar(String prefixChar, int seqNum)
    {
        String nextPrefixChar = "";

        if ( seqNum < 9999 )
        {
            nextPrefixChar = prefixChar;
            return(nextPrefixChar);
        }

        // 次の接頭語を取得
        if ( prefixChar.equals( "A" ) )
        {
            nextPrefixChar = "B";

        }
        else if ( prefixChar.equals( "B" ) )
        {
            nextPrefixChar = "C";

        }
        else if ( prefixChar.equals( "C" ) )
        {
            nextPrefixChar = "D";

        }
        else if ( prefixChar.equals( "D" ) )
        {
            nextPrefixChar = "E";

        }
        else if ( prefixChar.equals( "E" ) )
        {
            nextPrefixChar = "F";

        }
        else if ( prefixChar.equals( "F" ) )
        {
            nextPrefixChar = "G";

        }
        else if ( prefixChar.equals( "G" ) )
        {
            nextPrefixChar = "H";

        }
        else if ( prefixChar.equals( "H" ) )
        {
            nextPrefixChar = "I";

        }
        else if ( prefixChar.equals( "I" ) )
        {
            nextPrefixChar = "J";

        }
        else if ( prefixChar.equals( "J" ) )
        {
            nextPrefixChar = "K";

        }
        else if ( prefixChar.equals( "K" ) )
        {
            nextPrefixChar = "L";

        }
        else if ( prefixChar.equals( "L" ) )
        {
            nextPrefixChar = "M";

        }
        else if ( prefixChar.equals( "M" ) )
        {
            nextPrefixChar = "N";

        }
        else if ( prefixChar.equals( "N" ) )
        {
            nextPrefixChar = "O";

        }
        else if ( prefixChar.equals( "O" ) )
        {
            nextPrefixChar = "P";

        }
        else if ( prefixChar.equals( "P" ) )
        {
            nextPrefixChar = "Q";

        }
        else if ( prefixChar.equals( "Q" ) )
        {
            nextPrefixChar = "R";

        }
        else if ( prefixChar.equals( "R" ) )
        {
            nextPrefixChar = "S";

        }
        else if ( prefixChar.equals( "S" ) )
        {
            nextPrefixChar = "T";

        }
        else if ( prefixChar.equals( "T" ) )
        {
            nextPrefixChar = "U";

        }
        else if ( prefixChar.equals( "U" ) )
        {
            nextPrefixChar = "V";

        }
        else if ( prefixChar.equals( "V" ) )
        {
            nextPrefixChar = "W";

        }
        else if ( prefixChar.equals( "W" ) )
        {
            nextPrefixChar = "X";

        }
        else if ( prefixChar.equals( "X" ) )
        {
            nextPrefixChar = "Y";

        }
        else if ( prefixChar.equals( "Y" ) )
        {
            nextPrefixChar = "Z";
        }

        return(nextPrefixChar);
    }

    /**
     * 次の連番を取得する
     * 
     * @param seqNum 現在の連番
     * @return 次の連番
     */
    private static int getNextSeqNum(int seqNum)
    {
        int nextSeqNum = 0;

        if ( seqNum < 9999 )
        {
            nextSeqNum = seqNum + 1;
            return(nextSeqNum);
        }

        // 次の連番は「1」
        nextSeqNum = 1;
        return(nextSeqNum);
    }

    /**
     * 予約番号チェック
     * 
     * @param RsvNo 予約番号
     * @param conn Connectionオブジェクト
     * @return true:存在する、false:存在しない
     */
    private static boolean isRsvNoExsit(String RsvNo, Connection conn)
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = "SELECT 1 FROM newRsvDB.hh_rsv_reserve ";
        query += " WHERE reserve_no=?";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, RsvNo );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[NumberingRsvNo.isRsvNoExsit] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( result );
        }

        return(ret);
    }
}
