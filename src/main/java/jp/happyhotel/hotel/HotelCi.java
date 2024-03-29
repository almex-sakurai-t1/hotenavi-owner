/*
 * @(#)HotelCheclIn.java 1.00 2011/05/19 Copyright (C) ALMEX Inc. 2011 ホテルチェックイン情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApTouchCi;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.data.DataUserMyHotel;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserDataIndex;

/**
 * ホテルチェックイン情報クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class HotelCi implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7860767167031174837L;
    static final double       DEFAULT_RATE     = 1;
    private int               hotelCiCount;
    private int               lastUpdate;
    private DataHotelCi       hotelCi;
    private DataHotelCi[]     hotelCiMulti;
    private int               errorMsgNum;
    private int               errorMsg;

    /**
     * データを初期化します。
     */
    public HotelCi()
    {
        hotelCiCount = 0;
        hotelCi = null;
    }

    public DataHotelCi getHotelCi()
    {
        return hotelCi;
    }

    public DataHotelCi[] getHotelCiMulti()
    {
        return hotelCiMulti;
    }

    public int gethotelCiCount()
    {
        return hotelCiCount;
    }

    public void sethotelCi(DataHotelCi hotelCi)
    {
        this.hotelCi = hotelCi;
    }

    public void sethotelCiCount(int hotelCiCount)
    {
        this.hotelCiCount = hotelCiCount;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getErrorMsgNum()
    {
        return errorMsgNum;
    }

    public void setErrorMsgNum(int errorMsgNum)
    {
        this.errorMsgNum = errorMsgNum;
    }

    public int getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(int errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    /***
     * ホテルチェックイン情報取得
     * 
     * @param id
     * @param seq
     * @return
     */
    public boolean getData(int id, int seq)
    {
        boolean ret = false;
        DataHotelCi dci;
        dci = new DataHotelCi();

        ret = dci.getData( id, seq );
        this.hotelCi = dci;

        dci = null;
        return(ret);
    }

    /**
     * ホテルチェックイン情報取得
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCheckInBeforeData(int id, String userId)
    {

        return getCheckInBeforeData( id, userId, "" );
    }

    public boolean getCheckInBeforeData(int id, String userId, String rsvNo)
    {

        return getCheckInBeforeData( id, userId, rsvNo, "" );
    }

    public boolean getCheckInBeforeData(int id, String userId, String rsvNo, String roomNo)
    {
        boolean ret = false;
        String beforeDay;
        String beforeTime;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 24時間前の日付と時刻を取得
        beforeDay = DateEdit.elapsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );
        beforeTime = DateEdit.elapsedTime( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );

        query = " SELECT * FROM hh_hotel_ci m WHERE id = ?";
        query += " AND user_id = ? AND user_id<>'ota'"; // "ota"はすべて通す
        query += " AND (( ci_date = ? AND ci_time >= ? ) OR ci_date = ?) "; // 24時間以内のチェックインデータ
        query += " AND ci_status=0";
        if ( !rsvNo.equals( "" ) )
        {
            query += " AND rsv_no <> ?"; // 違う予約番号の来店済データがあったらNG
        }
        else if ( !roomNo.equals( "" ) )
        {
            query += " AND rsv_no <> ? AND room_no = ? "; // 同じ部屋で違う予約番号だったらNG
        }

        query += " AND";
        query += " NOT EXISTS ("; // 同じseqで最大値のもののみを対象とする
        query += "   SELECT 1";
        query += "   FROM hh_hotel_ci AS s";
        query += "   WHERE m.id = s.id";
        query += "   AND m.seq = s.seq";
        query += "   AND m.sub_seq < s.sub_seq";
        query += "   )";
        query += "   ORDER BY seq DESC";
        query += " LIMIT 0,1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, userId );
            prestate.setInt( 3, Integer.parseInt( beforeDay ) );
            prestate.setInt( 4, Integer.parseInt( beforeTime ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            if ( !rsvNo.equals( "" ) )
            {
                prestate.setString( 6, rsvNo );
            }
            else if ( !roomNo.equals( "" ) )
            {
                prestate.setString( 6, rsvNo );
                prestate.setString( 7, roomNo );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    hotelCiCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelCi = new DataHotelCi();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelCi.setData( result );

                    // 取得したデータのci_statusが1であれば、24時間以内のデータをなしとみなす
                    if ( this.hotelCi.getCiStatus() == 0 || (this.hotelCi.getCiStatus() == 4 && this.hotelCi.getExtUserFlag() == 1) )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.getCheckInBeforeData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルチェックイン情報取得
     * 
     * @param id ホテルID
     * @param roomNo 部屋番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataFromRoom(int id, String roomName)
    {
        boolean ret = false;
        String beforeDay;
        String beforeTime;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 24時間前の日付と時刻を取得
        beforeDay = DateEdit.elapsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );
        beforeTime = DateEdit.elapsedTime( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );

        query = " SELECT * FROM hh_hotel_ci m WHERE id = ?";
        query += " AND room_no = ?";
        query += " AND (( ci_date = ? AND ci_time >= ? ) OR ci_date = ?) ";
        // query += " AND ci_status IN(0,1,2,3) ";
        query += " AND";
        query += " NOT EXISTS ("; // 同じseqで最大値のもののみを対象とする
        query += "   SELECT 1";
        query += "   FROM hh_hotel_ci AS s";
        query += "   WHERE m.id = s.id";
        query += "   AND m.seq = s.seq";
        query += "   AND m.sub_seq < s.sub_seq";
        query += "   )";
        query += "   ORDER BY seq DESC";
        query += " LIMIT 0,1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, roomName );
            prestate.setInt( 3, Integer.parseInt( beforeDay ) );
            prestate.setInt( 4, Integer.parseInt( beforeTime ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    hotelCiCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelCi = new DataHotelCi();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelCi.setData( result );
                    // 取得したデータのci_statusが1であれば、24時間以内のデータをなしとみなす
                    if ( this.hotelCi.getCiStatus() == 0 || (this.hotelCi.getCiStatus() == 4 && this.hotelCi.getExtUserFlag() == 1) )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.getDataFromRoom()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * チェックイン部屋での最新のチェックインコードを取得するため
     * 
     * @param id ホテルID
     * @param seq チェックインコード
     * @param roomNo 管理番号
     * @param userId ユーザId
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getCheckInCode(int id, int seq, String roomNo, String userId)
    {
        boolean ret = false;
        String beforeDay;
        String beforeTime;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 24時間前の日付と時刻を取得
        beforeDay = DateEdit.elapsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );
        beforeTime = DateEdit.elapsedTime( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );

        query = " SELECT * FROM hh_hotel_ci m WHERE id = ?";
        query += " AND seq >= ?";
        query += " AND room_no = ?";
        query += " AND user_id = ?";
        query += " AND (( ci_date = ? AND ci_time >= ? ) OR ci_date = ?) ";
        // query += " AND ci_status IN(0,1,2,3) ";
        query += " AND";
        query += " NOT EXISTS ("; // 同じseqで最大値のもののみを対象とする
        query += "   SELECT 1";
        query += "   FROM hh_hotel_ci AS s";
        query += "   WHERE m.id = s.id";
        query += "   AND m.seq = s.seq";
        query += "   AND m.sub_seq < s.sub_seq";
        query += "   )";
        query += "   ORDER BY seq DESC";
        query += " LIMIT 0,1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            prestate.setString( 3, roomNo );
            prestate.setString( 4, userId );
            prestate.setInt( 5, Integer.parseInt( beforeDay ) );
            prestate.setInt( 6, Integer.parseInt( beforeTime ) );
            prestate.setInt( 7, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    hotelCiCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelCi = new DataHotelCi();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // ホテルPV情報の取得
                    this.hotelCi.setData( result );
                    if ( this.hotelCi.getCiStatus() <= 1 || (this.hotelCi.getCiStatus() == 4 && this.hotelCi.getExtUserFlag() == 1) )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.getCheckInBeforeData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * タッチユーザの状態取得（最後のタッチデータのみチェック）
     * 
     * @param userId
     * @return
     */
    public DataHotelCi touchState(String userId)
    {

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataHotelCi dhc = null;
        String beforeDay = "";
        String beforeTime = "";

        // 24時間前の日付と時刻を取得
        beforeDay = DateEdit.elapsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );
        beforeTime = DateEdit.elapsedTime( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );

        Long checkTime = Long.valueOf( beforeDay ) * 1000000 + Long.valueOf( beforeTime );

        // 24時間以内のデータを探す。
        query = " SELECT * FROM hh_hotel_ci ";
        query += " WHERE user_id = ?";
        query += " AND ci_date * 1000000 + ci_time >= ? ";
        query += " ORDER BY ci_date DESC, ci_time DESC, seq DESC, sub_seq DESC";
        query += " LIMIT 0,1";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setLong( 2, checkTime );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    dhc = new DataHotelCi();
                    dhc.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCi.touchState]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return dhc;
    }

    /**
     * チェックインデータ作成
     * 
     * @param id ホテルID
     * @param userId ユーザID
     * @param idm フェリカID
     * @param usertSeq ユーザ管理番号
     * @param visitSeq 来店回数
     * @param visitPoint 来店ハピー
     * @param hotenaviId ホテナビID
     * @param employeeCode 従業員コード
     * @param amountRate 付与倍率
     * @param userType ユーザタイプ
     * @return
     */
    public int insertData(int id, String userId, String idm, int userSeq, int visitSeq, int visitPoint,
            String hotenaviId, int employeeCode, double amountRate, int userType, String roomNo, boolean isReplaceUserId, int ciSeq)
    {
        DataHotelCi dhc;
        dhc = new DataHotelCi();
        boolean ret;
        int maxSeq = 0;
        UserBasicInfo ubi = new UserBasicInfo();
        try
        {
            dhc.setId( id );
            if ( isReplaceUserId )// ユーザーIDを書き換える場合は、チェックインコードを変えずにsub_seq を変更する。
            {
                dhc.setSeq( ciSeq );
                if ( dhc.getData( id, ciSeq ) != false )
                {
                    dhc.setSubSeq( dhc.getSubSeq() + 1 );
                }
            }
            else
            {
                dhc.setSeq( 0 ); // AutoIncrement
            }
            dhc.setCiDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhc.setCiTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dhc.setCiStatus( 0 );
            dhc.setUserId( userId );
            dhc.setIdm( idm );
            dhc.setUserSeq( userSeq );
            dhc.setVisitSeq( visitSeq );
            dhc.setVisitPoint( visitPoint );
            dhc.setVisitDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhc.setVisitTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dhc.setVisitHotenaviId( hotenaviId );
            if ( !roomNo.equals( "" ) )
            {
                dhc.setRoomNo( roomNo );
            }
            dhc.setVisitEmployeeCode( employeeCode );
            dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if ( amountRate > 0 )
            {
                dhc.setAmountRate( amountRate );
            }
            else
            {
                dhc.setAmountRate( DEFAULT_RATE );
            }
            dhc.setUserType( userType );
            if ( ubi.isLvjUser( userId ) )
            {
                dhc.setExtUserFlag( 1 );
                dhc.setCiStatus( 4 );
            }
            ret = dhc.insertData();
            if ( ret != false )
            {
                maxSeq = dhc.getSeq();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.insertData()] Exception=" + e.toString() );
        }
        return(maxSeq);
    }

    /***
     * チェックインステータス取得
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @return
     */
    public int getCiStatus(int id, int seq, int subSeq)
    {
        boolean ret = false;
        int status = 0;
        DataHotelCi dhc;
        dhc = new DataHotelCi();

        ret = dhc.getData( id, seq, subSeq );
        if ( ret != false )
        {
            status = dhc.getCiStatus();
        }
        dhc = null;

        return(status);
    }

    /***
     * マイナス履歴追加処理
     * 
     * @param dhc ホテルチェックインデータ
     * @param employeeCode 従業員コード
     * @return
     */
    public boolean setMinusHistory(DataHotelCi dhc, int employeeCode)
    {
        boolean ret = false;
        try
        {
            dhc.setSlipNo( dhc.getSlipNo() );
            dhc.setAddPoint( dhc.getAddPoint() * -1 );
            dhc.setAmount( dhc.getAmount() * -1 );
            dhc.setAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhc.setAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dhc.setAddHotenaviId( dhc.getAddHotenaviId() );
            dhc.setAddEmployeeCode( employeeCode );
            dhc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dhc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

            dhc.setSubSeq( dhc.getSubSeq() + 1 );
            ret = dhc.insertData();
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCi.setMinusHistory()] Exception:" + e.toString() );
            ret = false;
        }
        finally
        {
        }
        return(ret);
    }

    /***
     * チェックインデータ削除機能
     * 
     * @param id ホテルID
     * @return 処理結果(true：成功、fale：失敗
     */
    public boolean deleteCiData(int id)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        // query = " DELETE FROM hh_hotel_ci WHERE id = ?";
        // 物理削除から非表示のステータスに変更する
        query = " UPDATE hh_hotel_ci SET ci_status = 3, last_update = 0, last_uptime = 0  WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            if ( prestate.executeUpdate() >= 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.getCheckInBeforeData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /***
     * チェックインデータ取得機能
     * 
     * @param id ホテルID
     * @param startDate 開始日
     * @param startTime 開始時刻
     * @param endDate 終了日
     * @param endTime 終了時刻
     * @return 処理結果(true：成功、fale：失敗
     */
    public boolean getData(int id, int startDate, int startTime, int endDate, int endTime)
    {
        boolean ret = false;
        int i;
        int count;
        String query;
        String query2;
        String query3;
        String query4;
        String query5;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        int[] nSeq = null;
        int[] nSubSeq = null;

        i = 0;
        count = 0;
        if ( startDate > endDate )
        {
            return(false);
        }

        // seq毎の枝番のMAXを取得するクエリ
        query = " SELECT seq, MAX(sub_seq) AS subSeq FROM hh_hotel_ci WHERE id = ?";
        query += " AND last_update * 1000000 + last_uptime >= ? * 1000000 + ?";
        query += " AND last_update * 1000000 + last_uptime <= ? * 1000000 + ?";
        query += " GROUP BY id, seq";

        // 上記のクエリで取得した枝番をseq毎にセットしてデータを取得
        query2 = "SELECT * FROM hh_hotel_ci WHERE id = ";
        query3 = " AND seq = ";
        query4 = " AND sub_seq = ";
        query5 = " UNION ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, startTime );
            prestate.setInt( 4, endDate );
            prestate.setInt( 5, endTime );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                }

                if ( count > 0 )
                {
                    nSeq = new int[count];
                    nSubSeq = new int[count];

                    i = 0;
                    query = "";
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        nSeq[i] = result.getInt( "seq" );
                        nSubSeq[i] = result.getInt( "subSeq" );
                        if ( i > 0 )
                        {
                            query += query5;
                        }
                        query += query2 + id;
                        query += query3 + result.getInt( "seq" );
                        query += query4 + result.getInt( "subSeq" );
                        i++;
                    }
                }
            }
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );

            if ( count > 0 )
            {
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        hotelCiCount = result.getRow();
                    }
                    // クラスの配列を用意し、初期化する。
                    this.hotelCiMulti = new DataHotelCi[hotelCiCount];

                    result.beforeFirst();
                    i = 0;
                    while( result.next() != false )
                    {
                        this.hotelCiMulti[i] = new DataHotelCi();
                        this.hotelCiMulti[i].setData( result );
                        i++;
                    }
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.getCheckInBeforeData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 最新日付時刻取得
     * 
     * @param id
     * @return 最新のチェックイン日付時刻（YYYYMMDDHHMMSS）
     */
    public long latestData(int id)
    {
        String query;
        long nTimeStamp;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        nTimeStamp = 0;
        query = " SELECT MAX(last_update * 1000000 +  last_uptime ) AS latestData";
        query += "  FROM hh_hotel_ci WHERE id = ? GROUP BY id";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    nTimeStamp = result.getLong( "latestData" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCi.latestData]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return nTimeStamp;
    }

    /**
     * 来店回数取得
     * 
     * @param id
     * @param userId
     * @return
     */
    public int getMaxVisitSeq(int id, String userId)
    {
        String query;
        int maxVisit;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        maxVisit = 0;
        query = " SELECT MAX(visit_seq ) AS MaxVisit";
        query += "  FROM hh_hotel_ci WHERE id = ? AND user_id = ? GROUP BY id, user_id";

        try
        {

            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, userId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxVisit = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCi.getMaxVisitSeq]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return maxVisit;
    }

    /****
     * チェックインデータ作成
     * 
     * @param userId
     * @param hotelId
     * @return
     */
    public HotelCi registCiData(String userId, String idm, int hotelId, String roomNo, boolean isReplaceUserId, int ciCode)
    {
        // ポイント区分
        final int AMOUNT_POINT = 1000005;
        final int PREMIUM_USER = 0; // 有料会員
        final int FREE_USER = 1; // 無料会員
        final int NO_MEMBER = 99;

        int nUserSeq = 0;
        int nVisitSeq = 0;
        int userType = 0;
        int multiple = 0;
        double amountRate = 0;
        UserBasicInfo ubi = new UserBasicInfo();
        UserDataIndex udi = new UserDataIndex();
        DataMasterPoint dmp = new DataMasterPoint();
        HotelBasicInfo hbi = new HotelBasicInfo();
        HotelHappie hh = new HotelHappie();
        HotelCi hc = new HotelCi();

        // ホテル情報取得
        hbi.getHotelBasicInfo( hotelId );

        // ハピー情報をセット
        hh.getData( hotelId );

        // 付与マイルの情報を取得
        dmp.getData( AMOUNT_POINT );

        // 倍率をセット
        multiple = hh.getHotelHappie().getUsePointMultiple();

        // データがない場合は通常通り1とする
        if ( multiple <= 0 )
        {
            multiple = 1;
        }

        if ( userId.equals( "" ) == false )
        {
            // ユーザの管理番号を取得（ホテル別）
            if ( udi.getDataUserIndex( userId, hotelId ) != false )
            {
                // ユーザの管理番号を取得
                nUserSeq = udi.getUserDataIndexInfo().getUserSeq();
            }
            // ユーザ情報の取得
            ubi.getUserBasic( userId );

            // 来店管理番号を取得（後から変更を考えると、チェックインデータを新しく作られた時には必ず来店回数を+1する）
            nVisitSeq = hc.getMaxVisitSeq( hotelId, userId );
            // かならず来店回数を1増やす
            nVisitSeq++;

            // 付与率の計算
            if ( ubi.getUserInfo().getRegistStatusPay() > 0 )
            {
                amountRate = multiple * dmp.getAddPoint();
                userType = PREMIUM_USER;
            }
            else
            {
                amountRate = dmp.getFreeMultiple() * multiple * dmp.getAddPoint();
                userType = FREE_USER;
            }
        }
        else
        {
            userType = NO_MEMBER;
        }

        // チェックインデータを作成（先にhh_hotel_ciをAUTO INCREMENTで作成する。ただし、ユーザーIDを書き換えるときは、sub_seqを追加するので変わらない）
        ciCode = hc.insertData( hotelId, userId, idm, nUserSeq, nVisitSeq, 0, hbi.getHotelInfo().getHotenaviId(), 0, amountRate, userType, roomNo, isReplaceUserId, ciCode );

        if ( ciCode > 0 )
        {
            this.registMyHotel( userId, hotelId );
        }
        else
        {
        }

        return hc;
    }

    /****
     * チェックインデータ作成
     * 
     * @param userId
     * @param hotelId
     * @return
     */
    public HotelCi registCiData(String userId, int hotelId)
    {
        // ポイント区分
        final int AMOUNT_POINT = 1000005;
        final int PREMIUM_USER = 0; // 有料会員
        final int FREE_USER = 1; // 無料会員

        int ciCode = 0;
        int nUserSeq = 0;
        int nVisitSeq = 0;
        int userType = 0;
        int multiple = 0;
        double amountRate = 0;
        UserBasicInfo ubi = new UserBasicInfo();
        UserDataIndex udi = new UserDataIndex();
        DataMasterPoint dmp = new DataMasterPoint();
        DataUserFelica duf = new DataUserFelica();
        HotelBasicInfo hbi = new HotelBasicInfo();
        HotelCi hc = new HotelCi();
        HotelHappie hh = new HotelHappie();

        // ユーザの管理番号を取得（ホテル別）
        if ( udi.getDataUserIndex( userId, hotelId ) != false )
        {
            // ユーザの管理番号を取得
            nUserSeq = udi.getUserDataIndexInfo().getUserSeq();
        }
        // ユーザ情報の取得
        ubi.getUserBasic( userId );

        // 来店管理番号を取得（後から変更を考えると、チェックインデータを新しく作られた時には必ず来店回数を+1する）
        nVisitSeq = hc.getMaxVisitSeq( hotelId, userId );
        // かならず来店回数を1増やす
        nVisitSeq++;

        hbi.getHotelBasicInfo( hotelId );

        // ハピー情報をセット
        hh.getData( hotelId );

        // 付与マイルの情報を取得
        dmp.getData( AMOUNT_POINT );
        duf.getData( userId );

        // 倍率をセット
        multiple = hh.getHotelHappie().getUsePointMultiple();
        // データがない場合は通常通り1とする
        if ( multiple <= 0 )
        {
            multiple = 1;
        }

        // 付与率の計算
        if ( ubi.getUserInfo().getRegistStatusPay() > 0 )
        {
            amountRate = multiple * dmp.getAddPoint();
            userType = PREMIUM_USER;
        }
        else
        {
            amountRate = dmp.getFreeMultiple() * multiple * dmp.getAddPoint();
            userType = FREE_USER;
        }

        // チェックインデータを作成
        ciCode = hc.insertData( hotelId, userId, duf.getIdm(), nUserSeq, nVisitSeq, 0, hbi.getHotelInfo().getHotenaviId(), 0, amountRate, userType, "", false, 0 );
        hc.getData( hotelId, ciCode );
        return hc;
    }

    /***
     * ホテルチェックインデータ書き込み処理
     * 
     * @param dhc タッチ履歴データ
     * @return
     */
    public boolean registTouchCi(DataHotelCi dhc, boolean retUse)
    {
        boolean ret = false;
        DataApTouchCi datc = new DataApTouchCi();
        datc.getData( dhc.getId(), dhc.getSeq() );
        datc.setId( dhc.getId() );
        datc.setSeq( dhc.getSeq() );
        datc.setCiDate( dhc.getCiDate() );
        datc.setCiTime( dhc.getCiTime() );
        datc.setCiStatus( dhc.getCiStatus() );
        datc.setUserId( dhc.getUserId() );
        datc.setIdm( dhc.getIdm() );
        datc.setUserSeq( dhc.getUserSeq() );
        datc.setVisitSeq( dhc.getVisitSeq() );
        datc.setVisitPoint( dhc.getVisitPoint() );
        datc.setVisitDate( dhc.getVisitDate() );
        datc.setVisitTime( dhc.getVisitTime() );
        datc.setVisitHotenaviId( dhc.getVisitHotenaviId() );
        datc.setRoomNo( dhc.getRoomNo() );

        datc.setUsePoint( dhc.getUsePoint() );
        datc.setUseDate( dhc.getUseDate() );
        datc.setUseTime( dhc.getUseTime() );
        datc.setUseHotenaviId( dhc.getUseHotenaviId() );
        datc.setUseEmployeeCode( dhc.getUseEmployeeCode() );
        if ( dhc.getSlipNo() != 0 )
        {
            datc.setSlipNo( dhc.getSlipNo() );
        }
        datc.setAmount( dhc.getAmount() );
        datc.setAddPoint( dhc.getAddPoint() );
        datc.setAddDate( dhc.getAddDate() );
        datc.setAddTime( dhc.getAddTime() );
        datc.setAddHotenaviId( dhc.getAddHotenaviId() );
        datc.setAddEmployeeCode( dhc.getAddEmployeeCode() );

        datc.setLastUpdate( dhc.getLastUpdate() );
        datc.setLastUptime( dhc.getLastUptime() );
        datc.setAmountRate( dhc.getAmountRate() );
        datc.setRsvNo( dhc.getRsvNo() );
        datc.setAlluseFlag( dhc.getAllUseFlag() );
        datc.setAllusePoint( dhc.getAllUsePoint() );
        datc.setFixFlag( dhc.getFixFlag() );
        datc.setUserType( dhc.getUserType() );

        // マイル使用のリクエスト及び、仮マイル使用であれば仮マイルを使用済みに変更する
        if ( retUse != false && datc.getUseTempFlag() == 1 )
        {
            datc.setUseTempFlag( 2 );
        }
        datc.setExtUserFlag( dhc.getExtUserFlag() );

        datc.updateData( dhc.getId(), dhc.getSeq() );

        return ret;
    }

    /**
     * 来店済み判断
     * 
     * @param id ホテルID
     * @param rsvNo 予約番号
     * @return 処理結果(TRUE:来店済み,FALSE:未来店)
     */
    public boolean isRaiten(int id, String rsvNo)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = " SELECT * FROM hh_hotel_ci m WHERE m.id = ?";
        query += " AND m.rsv_no = ?";
        query += " AND m.ci_status IN (0,1)";
        query += " AND NOT EXISTS (";
        query += "   SELECT 1";
        query += "   FROM hh_hotel_ci AS s";
        query += "   WHERE m.id = s.id";
        query += "   AND m.seq = s.seq";
        query += "   AND m.sub_seq < s.sub_seq";
        query += "   )";
        query += " LIMIT 0,1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsvNo );

            result = prestate.executeQuery();
            if ( result != null )
            {
                ret = result.next();
                if ( ret )
                {
                    this.hotelCi.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[hotelCi.isRaiten()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * マイホテル登録処理
     * 
     * @param userId
     * @param hotelId
     */
    public void registMyHotel(String userId, int hotelId)
    {
        boolean ret = false;
        // マイホテル登録処理
        DataUserMyHotel dumh = new DataUserMyHotel();
        ret = dumh.getData( userId, hotelId );

        dumh.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dumh.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        if ( ret == false )
        {
            dumh.setUserId( userId );
            dumh.setHotelId( hotelId );
            dumh.insertData();
        }
        else
        {
            dumh.setDelFlag( 0 );
            dumh.updateData( userId, hotelId );
        }
    }

}
