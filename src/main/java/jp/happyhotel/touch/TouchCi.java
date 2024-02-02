package jp.happyhotel.touch;

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
import jp.happyhotel.data.DataUserMyHotel;
import jp.happyhotel.hotel.HotelBasicInfo;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.hotel.HotelHappie;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserDataIndex;

/**
 * ホテルチェックイン情報クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class TouchCi implements Serializable
{
    static final double     DEFAULT_RATE = 1;
    private int             touchCiCount;
    private int             lastUpdate;
    private DataApTouchCi   touchCi;
    private DataApTouchCi[] touchCiMulti;
    private int             errorMsgNum;
    private int             errorMsg;

    public int getTouchCiCount()
    {
        return touchCiCount;
    }

    public void setTouchCiCount(int touchCiCount)
    {
        this.touchCiCount = touchCiCount;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public DataApTouchCi getTouchCi()
    {
        return touchCi;
    }

    public void setTouchCi(DataApTouchCi touchCi)
    {
        this.touchCi = touchCi;
    }

    public DataApTouchCi[] getTouchCiMulti()
    {
        return touchCiMulti;
    }

    public void setTouchCiMulti(DataApTouchCi[] touchCiMulti)
    {
        this.touchCiMulti = touchCiMulti;
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
        final int TIME = 3;
        final int ONE_DAY = 24;
        boolean ret = false;
        DataApTouchCi datc;
        datc = new DataApTouchCi();

        ret = datc.getData( id, seq );
        // 24時間を超えていたら新しく作成させるため、falseとする。
        if ( DateEdit.isValidDate( datc.getVisitDate(), datc.getVisitTime(), TIME, ONE_DAY ) != false )
        {
            if ( datc.getCiStatus() == 0 )
            {
                this.touchCi = datc;
            }
            else
            {
                ret = false;
            }
        }
        else
        {
            ret = false;
        }
        this.touchCi = datc;
        datc = null;
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
        boolean ret = false;
        int i;
        int count;
        String beforeDay;
        String beforeTime;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataApTouchCi datc;

        // 24時間前の日付と時刻を取得
        beforeDay = DateEdit.elapsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );
        beforeTime = DateEdit.elapsedTime( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );

        query = " SELECT * FROM ap_touch_ci WHERE id = ?";
        query += " AND user_id = ?";
        query += " AND ( ci_date = ? AND ci_time >= ? ) ";
        query += " UNION  SELECT * FROM ap_touch_ci WHERE id = ?";
        query += " AND user_id = ?";
        query += " AND ci_date = ? ORDER BY seq DESC";
        query += " LIMIT 0,1";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, userId );
            prestate.setInt( 3, Integer.parseInt( beforeDay ) );
            prestate.setInt( 4, Integer.parseInt( beforeTime ) );
            prestate.setInt( 5, id );
            prestate.setString( 6, userId );
            prestate.setInt( 7, Integer.parseInt( DateEdit.getDate( 2 ) ) );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                datc = new DataApTouchCi();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // ホテルPV情報の取得
                    datc.setData( result );
                    this.touchCi = datc;

                    // 取得したデータのci_statusが1であれば、24時間以内のデータをなしとみなす
                    if ( datc.getCiStatus() == 0 || (datc.getCiStatus() == 4 && datc.getExtUserFlag() == 1) )
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
            Logging.error( "[TouchCi.getCheckInBeforeData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
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
    public int insertData(int id, int seq, String userId, String idm, int userSeq, int visitSeq, int visitPoint,
            String hotenaviId, int employeeCode, double amountRate, int userType, String roomNo, boolean isReplaceUserId)
    {
        DataApTouchCi datc;
        datc = new DataApTouchCi();
        boolean ret;
        int maxSeq = 0;
        UserBasicInfo ubi = new UserBasicInfo();
        Connection connection = null;

        try
        {
            datc.setId( id );
            datc.setSeq( seq );
            datc.setCiDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            datc.setCiTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            datc.setCiStatus( 0 );
            datc.setUserId( userId );
            datc.setIdm( idm );
            datc.setUserSeq( userSeq );
            datc.setVisitSeq( visitSeq );
            datc.setVisitPoint( visitPoint );
            datc.setVisitDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            datc.setVisitTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            datc.setVisitHotenaviId( hotenaviId );
            datc.setVisitEmployeeCode( employeeCode );
            datc.setRoomNo( roomNo );
            datc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            datc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if ( amountRate > 0 )
            {
                datc.setAmountRate( amountRate );
            }
            else
            {
                datc.setAmountRate( DEFAULT_RATE );
            }
            datc.setUserType( userType );
            if ( ubi.isLvjUser( userId ) )
            {
                datc.setExtUserFlag( 1 );
                datc.setCiStatus( 4 );
            }
            if ( isReplaceUserId )
            {
                ret = datc.updateData( id, seq );
            }
            else
            {
                ret = datc.insertData();
            }
            if ( ret != false )
            {
                maxSeq = datc.getSeq();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchCi.insertData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
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

        // query = " DELETE FROM ap_touch_ci WHERE id = ?";
        // 物理削除から非表示のステータスに変更する
        query = " UPDATE ap_touch_ci SET ci_status = 3, last_update = 0, last_uptime = 0  WHERE id = ?";

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
            Logging.error( "[TouchCi.getCheckInBeforeData()] Exception=" + e.toString() );
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
        query = " SELECT seq, MAX(sub_seq) AS subSeq FROM ap_touch_ci WHERE id = ?";
        query += " AND last_update * 1000000 + last_uptime >= ? * 1000000 + ?";
        query += " AND last_update * 1000000 + last_uptime <= ? * 1000000 + ?";
        query += " GROUP BY id, seq";

        // 上記のクエリで取得した枝番をseq毎にセットしてデータを取得
        query2 = "SELECT * FROM ap_touch_ci WHERE id = ";
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
                        touchCiCount = result.getRow();
                    }
                    // クラスの配列を用意し、初期化する。
                    this.touchCiMulti = new DataApTouchCi[touchCiCount];

                    result.beforeFirst();
                    i = 0;
                    while( result.next() != false )
                    {
                        this.touchCiMulti[i] = new DataApTouchCi();
                        this.touchCiMulti[i].setData( result );
                        i++;
                    }
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[TouchCi.getCheckInBeforeData()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * チェックインデータ取得
     * 
     * @param id ホテルID
     * @param targetDate 対象日付
     * @return 処理結果（true：成功、fale：失敗）
     */
    public boolean getCheckInCount(int id, int targetDate)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int nextDate = DateEdit.addDay( targetDate, 1 );

        query = "SELECT";
        query += " count(atc.seq) AS COUNT";
        // query += ",CASE WHEN atc.ci_time > hrrb.deadline_time THEN atc.ci_date ";
        // query += "     WHEN atc.ci_time <= hrrb.deadline_time THEN DATE_FORMAT(DATE_SUB(STR_TO_DATE(atc.ci_date,'%Y%m%d'),INTERVAL 1 DAY),'%Y%m%d')";
        // query += "    END AS CiDate";
        query += " FROM ap_touch_ci atc";
        query += " INNER JOIN hh_rsv_reserve_basic hrrb ON hrrb.id = atc.id";
        query += " WHERE atc.id = ? ";
        query += " AND atc.ci_status = 1";
        query += " AND atc.ci_date * 1000000 + atc.ci_time > ? * 1000000 + hrrb.deadline_time";
        query += " AND atc.ci_date * 1000000 + atc.ci_time <= ?  * 1000000 + hrrb.deadline_time";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, nextDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.next() != false )
                {
                    touchCiCount = result.getInt( 1 );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchCi.getCheckInCount()] Exception=" + e.toString() );
            return(ret);
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
        query += "  FROM ap_touch_ci WHERE id = ? GROUP BY id";

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
            Logging.error( "[TouchCi.latestData]Exception:" + e.toString() );
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
        query += "  FROM ap_touch_ci WHERE id = ? AND user_id = ? GROUP BY id, user_id";
        query += " UNION SELECT MAX(visit_seq ) AS MaxVisit";
        query += "  FROM hh_hotel_ci WHERE id = ? AND user_id = ? GROUP BY id, user_id";
        query += " ORDER BY MaxVisit DESC";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, userId );
            prestate.setInt( 3, id );
            prestate.setString( 4, userId );

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
            Logging.error( "[TouchCi.getMaxVisitSeq]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return maxVisit;
    }

    /**
     * 来店回数取得
     * 
     * @param id
     * @param userId
     * @return
     */
    public int getMaxSeq(int id)
    {
        String query;
        int maxVisit;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        maxVisit = 0;
        query = " SELECT MAX( seq ) AS MaxVisit";
        query += "  FROM ap_touch_ci WHERE id = ?  GROUP BY id";
        query += "  UNION SELECT MAX( seq ) AS MaxVisit";
        query += "  FROM hh_hotel_ci WHERE id = ?  GROUP BY id ";
        query += " ORDER BY MaxVisit DESC";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, id );

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
            Logging.error( "[TouchCi.getMaxVisitSeq]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        maxVisit++;
        return maxVisit;
    }

    /****
     * チェックインデータ作成
     * 
     * @param userId
     * @param hotelId
     * @return
     */
    public TouchCi registCiData(String userId, String idm, int hotelId, String roomNo, boolean isReplaceUserId, int ciCode)
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
        TouchCi tc = new TouchCi();
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
            nVisitSeq = tc.getMaxVisitSeq( hotelId, userId );
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
        tc.insertData( hotelId, ciCode, userId, idm, nUserSeq, nVisitSeq, 0, hbi.getHotelInfo().getHotenaviId(), 0, amountRate, userType, roomNo, isReplaceUserId );
        tc.getData( hotelId, ciCode );// ap_touch_ciはAUTO INCREMENTで作成していないのでTouchCiには作成したデータがセットされていない。チェックインコードで取得し直す。

        if ( ciCode > 0 )
        {
            this.registMyHotel( userId, hotelId );
        }
        else
        {
        }

        return tc;
    }

    /***
     * ホテルチェックインデータ書き込み処理
     * 
     * @param tc タッチ履歴データ
     * @return
     */
    public boolean registHotelCi(DataApTouchCi tc)
    {
        boolean ret = false;
        DataHotelCi dhc = new DataHotelCi();
        boolean insMode = false;
        ret = dhc.getData( tc.getId(), tc.getSeq() );
        if ( ret == true && tc.getCiStatus() == 0 && dhc.getCiStatus() == 2 ) // チェックイン取り消しの状態でチェックイン
        {
            insMode = true;
        }
        dhc.setId( tc.getId() );
        dhc.setSeq( tc.getSeq() );
        dhc.setCiDate( tc.getCiDate() );
        dhc.setCiTime( tc.getCiTime() );
        dhc.setCiStatus( tc.getCiStatus() );
        dhc.setUserId( tc.getUserId() );
        dhc.setIdm( tc.getIdm() );
        dhc.setUserSeq( tc.getUserSeq() );
        dhc.setVisitSeq( tc.getVisitSeq() );
        dhc.setVisitPoint( tc.getVisitPoint() );
        dhc.setVisitDate( tc.getVisitDate() );
        dhc.setVisitTime( tc.getVisitTime() );
        dhc.setVisitHotenaviId( tc.getVisitHotenaviId() );
        dhc.setRoomNo( tc.getRoomNo() );

        dhc.setUsePoint( tc.getUsePoint() );
        dhc.setUseDate( tc.getUseDate() );
        dhc.setUseTime( tc.getUseTime() );
        dhc.setUseHotenaviId( tc.getUseHotenaviId() );
        dhc.setUseEmployeeCode( tc.getUseEmployeeCode() );
        dhc.setSlipNo( tc.getSlipNo() );
        dhc.setAmount( tc.getAmount() );
        dhc.setAddPoint( tc.getAddPoint() );
        dhc.setAddDate( tc.getAddDate() );
        dhc.setAddTime( tc.getAddTime() );
        dhc.setAddHotenaviId( tc.getAddHotenaviId() );
        dhc.setAddEmployeeCode( tc.getAddEmployeeCode() );

        dhc.setLastUpdate( tc.getLastUpdate() );
        dhc.setLastUptime( tc.getLastUptime() );
        dhc.setAmountRate( tc.getAmountRate() );
        dhc.setRsvNo( tc.getRsvNo() );
        dhc.setAllUseFlag( tc.getAlluseFlag() );
        dhc.setAllUsePoint( tc.getAllusePoint() );
        dhc.setFixFlag( tc.getFixFlag() );
        dhc.setUserType( tc.getUserType() );
        dhc.setCustomId( tc.getCustomId() );
        dhc.setUseTempFlag( tc.getUseTempFlag() );
        dhc.setExtUserFlag( tc.getExtUserFlag() );
        Logging.info( "TouchCi.registHotelCi ret:" + ret + ",dhc.setRsvNo=" + dhc.getRsvNo() + ",dhc.getSubSeq():" + dhc.getSubSeq() );

        if ( ret != false )
        {
            if ( insMode )// 取り消しなのでレコードを追加する
            {
                dhc.setSubSeq( dhc.getSubSeq() + 1 );
                ret = dhc.insertData();
            }
            else
            {
                ret = dhc.updateData( dhc.getId(), dhc.getSeq(), dhc.getSubSeq() );
            }
        }
        else
        {
            ret = dhc.insertData();
        }

        return ret;
    }

    /***
     * タッチチェックインデータ書き込み処理
     * 
     * @param tc タッチ履歴データ
     * @return
     */
    public static boolean registTouchCi(DataHotelCi hc)
    {
        boolean ret = false;
        DataApTouchCi tc = new DataApTouchCi();

        ret = tc.getData( hc.getId(), hc.getSeq() );
        tc.setId( hc.getId() );
        tc.setSeq( hc.getSeq() );
        tc.setCiDate( hc.getCiDate() );
        tc.setCiTime( hc.getCiTime() );
        tc.setCiStatus( hc.getCiStatus() );
        tc.setUserId( hc.getUserId() );
        tc.setIdm( hc.getIdm() );
        tc.setUserSeq( hc.getUserSeq() );
        tc.setVisitSeq( hc.getVisitSeq() );
        tc.setVisitPoint( hc.getVisitPoint() );
        tc.setVisitDate( hc.getVisitDate() );
        tc.setVisitTime( hc.getVisitTime() );
        tc.setVisitHotenaviId( hc.getVisitHotenaviId() );
        tc.setRoomNo( hc.getRoomNo() );

        tc.setUsePoint( hc.getUsePoint() );
        tc.setUseDate( hc.getUseDate() );
        tc.setUseTime( hc.getUseTime() );
        tc.setUseHotenaviId( hc.getUseHotenaviId() );
        tc.setUseEmployeeCode( hc.getUseEmployeeCode() );
        tc.setSlipNo( hc.getSlipNo() );
        tc.setAmount( hc.getAmount() );
        tc.setAddPoint( hc.getAddPoint() );
        tc.setAddDate( hc.getAddDate() );
        tc.setAddTime( hc.getAddTime() );
        tc.setAddHotenaviId( hc.getAddHotenaviId() );
        tc.setAddEmployeeCode( hc.getAddEmployeeCode() );

        tc.setLastUpdate( hc.getLastUpdate() );
        tc.setLastUptime( hc.getLastUptime() );
        tc.setAmountRate( hc.getAmountRate() );
        tc.setRsvNo( hc.getRsvNo() );
        tc.setFixFlag( hc.getFixFlag() );
        tc.setUserType( hc.getUserType() );
        tc.setExtUserFlag( hc.getExtUserFlag() );
        if ( ret != false )
        {
            ret = tc.updateData( tc.getId(), tc.getSeq() );
        }
        else
        {
            ret = tc.insertData();
        }

        return ret;
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

    /**
     * 重複チェック
     * 
     * @param userId
     * @return
     */
    public boolean checkDuplicate(String userId)
    {
        boolean ret = false;
        String query;
        int maxVisit;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        maxVisit = 0;
        query = " SELECT MAX( seq ) AS MaxVisit";
        query += "  FROM ap_touch_ci WHERE id = ?  GROUP BY id";
        query += "  UNION SELECT MAX( seq ) AS MaxVisit";
        query += "  FROM hh_hotel_ci WHERE id = ?  GROUP BY id ";
        query += " ORDER BY MaxVisit DESC";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );

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
            Logging.error( "[TouchCi.getMaxVisitSeq]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /***
     * タッチユーザの状態取得（最後のタッチデータのみチェック）
     * 
     * @param userId
     * @return
     */
    public DataApTouchCi touchState(String userId)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataApTouchCi datc = null;
        String beforeDay = "";
        String beforeTime = "";

        // 24時間前の日付と時刻を取得
        beforeDay = DateEdit.elapsedDate( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );
        beforeTime = DateEdit.elapsedTime( Integer.parseInt( DateEdit.getDate( 2 ) ), Integer.parseInt( DateEdit.getTime( 1 ) ), 3, -24 );

        Long checkTime = Long.valueOf( beforeDay ) * 1000000 + Long.valueOf( beforeTime );

        // 24時間以内のデータを探す。
        query = " SELECT * FROM ap_touch_ci ";
        query += " WHERE user_id = ?";
        query += " AND ci_date * 1000000 + ci_time >= ? ";
        query += " ORDER BY ci_date DESC, ci_time DESC, seq DESC";
        query += " LIMIT 0,1";

        Logging.info( query );
        Logging.info( "" + checkTime );
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
                    datc = new DataApTouchCi();
                    datc.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchCi.touchState]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return datc;
    }

    /***
     * タッチデータからホテルチェックインデータ取得
     * 
     * @param tc
     * @return
     */
    public HotelCi getHotelCi(TouchCi tc)
    {
        HotelCi hc = new HotelCi();

        hc.getData( tc.getTouchCi().getId(), tc.getTouchCi().getSeq() );

        return hc;
    }

}
