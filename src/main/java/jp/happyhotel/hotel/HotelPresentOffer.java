package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelPresentOffer;
import jp.happyhotel.data.DataHotelPresentOfferSub;
import jp.happyhotel.data.DataMasterLocal;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataUserPresentEntry;
import jp.happyhotel.search.SearchEngineBasic;

public class HotelPresentOffer implements Serializable
{
    /**
     *
     */
    private static final long          serialVersionUID     = -3957984258161172180L;
    private int                        hotelCount;
    private int                        hotelAllCount;
    private int                        localCount;
    private int[]                      prefCount;
    private DataMasterLocal            dmLocal;
    private DataMasterPref[]           dmPref;
    private DataHotelBasic[]           m_hotelBasic;
    private DataHotelPresentOffer[]    hotelPresentOffer;
    private DataHotelPresentOfferSub[] formSub;
    private int[]                      entryCount;

    private int                        userCount;
    private DataUserPresentEntry[]     userPresentEntry;
    private String[]                   handleNameList;

    private int                        lotteryUserCount;
    private String[]                   lotteryUserList;

    final int                          FORM_ENTRY           = 0;
    final int                          FORM_DROW            = 1;
    final int                          FORM_DROW_FIX        = 2;
    final int                          FORM_MAKE_ELECT_MAIL = 3;
    final int                          FORM_SEND_ELECT_MAIL = 4;
    final int                          FORM_DRAW_LOSE_FIX   = 5;
    final int                          FORM_MAKE_LOSE_MAIL  = 6;
    final int                          FORM_SEND_LOSE_MAIL  = 7;
    final int                          FORM_STATUS_FIX      = 9;

    public HotelPresentOffer()
    {
        hotelCount = 0;
        hotelAllCount = 0;
        localCount = 0;
        lotteryUserCount = 0;
        userCount = 0;
    }

    /** ホテル件数取得（1ページの件数） **/
    public int getHotelCount()
    {
        return hotelCount;
    }

    /** ホテル件数取得 **/
    public int getHotelAllCount()
    {
        return hotelAllCount;
    }

    /** ホテル情報取得 **/
    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelBasic);
    }

    /** ホテル情報取得 **/
    public DataHotelPresentOffer[] getHotel()
    {
        return(hotelPresentOffer);
    }

    /** 地方件数取得 **/
    public int getLocalCount()
    {
        return localCount;
    }

    /** 都道府県件数取得 **/
    public int[] getPrefCount()
    {
        return prefCount;
    }

    /** 地方マスタ取得 **/
    public DataMasterLocal getMasterLocal()
    {
        return dmLocal;
    }

    /** 都道府県マスタ取得 **/
    public DataMasterPref[] getMasterPref()
    {
        return dmPref;
    }

    /** 応募件数 **/
    public int[] getEntryCount()
    {
        return entryCount;
    }

    /** フォームステータス **/
    public DataHotelPresentOfferSub[] getFormSub()
    {
        return formSub;
    }

    public int getUserCount()
    {
        return userCount;
    }

    public DataUserPresentEntry[] getUser()
    {
        return userPresentEntry;
    }

    public String[] getHandleNameList()
    {
        return handleNameList;
    }

    /**
     * プレゼントホテル件数取得（都道府県IDから）
     * 
     * @param prefId 都道府県ID
     * @return ホテル件数
     */
    public int getHotelCountByPref(int prefId)
    {
        int i = 0;
        int count;
        int today = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        today = Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( prefId <= 0 )
        {
            return(-1);
        }
        query = "SELECT COUNT(A.id) FROM hh_hotel_basic A";
        query += " INNER JOIN hh_master_pref B ON A.pref_id = B.pref_id";
        query += " INNER JOIN hh_hotel_present_offer C ON A.id = C.id AND C.del_flag=0";
        query += "   AND C.start_date <= ? AND C.end_date >= ?";
        query += " WHERE A.rank > 1 ";
        query += " AND A.kind <= 7";
        query += " AND B.pref_id = ?";
        query += " GROUP BY A.id";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( prefId > 0 )
            {
                prestate.setInt( ++i, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            else
            {
                count = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCountByPref] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * プレゼントホテル件数取得（地方IDから）
     * 
     * @param localId 地方ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelCountByLocal(int localId)
    {
        int i;
        int j;
        SearchEngineBasic seb;
        seb = new SearchEngineBasic();

        if ( localId < 0 )
            return(false);

        this.localCount = 0;
        seb.getLocalList( localId, 0 );
        try
        {
            if ( seb.getMasterLocalCount() > 0 )
            {
                // 地方IDの数だけループさせる
                for( i = 0 ; i < seb.getMasterLocalCount() ; i++ )
                {
                    // 地方IDに含まれる都道府県を取得する
                    seb.getPrefListByLocal( seb.getMasterLocal()[i].getLocalId(), 0 );
                    if ( seb.getMasterPrefCount() > 0 )
                    {
                        // 全国の件数取得の場合はカウントだけ取得する
                        if ( localId == 0 )
                        {
                            for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                            {
                                this.localCount = this.localCount + getHotelCountByPref( seb.getMasterPref()[j].getPrefId() );
                            }
                        }
                        // 地方ごとに件数取得する場合は、地方、都道府県の件数とデータを取得
                        else
                        {
                            // 地方のデータをセット
                            this.dmLocal = new DataMasterLocal();
                            this.dmLocal = seb.getMasterLocal()[i];
                            // 都道府県の配列を用意
                            this.dmPref = new DataMasterPref[seb.getMasterPrefCount()];
                            this.prefCount = new int[seb.getMasterPrefCount()];
                            for( j = 0 ; j < seb.getMasterPrefCount() ; j++ )
                            {
                                this.localCount = localCount + getHotelCountByPref( seb.getMasterPref()[j].getPrefId() );

                                this.dmPref[j] = new DataMasterPref();
                                this.dmPref[j] = seb.getMasterPref()[j];
                                this.prefCount[j] = getHotelCountByPref( seb.getMasterPref()[j].getPrefId() );
                            }
                        }
                    }
                    else
                    {
                        localCount = 0;
                        Logging.error( "[getHotelCountByLocal] pref=0" );
                    }
                }
            }
            else
            {
                localCount = 0;
                Logging.error( "[getHotelCountByLocal] local=0" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelCountByLocal] Exception=" + e.toString() );
            return false;
        }
        return true;
    }

    /**
     * プレゼントホテル情報取得（都道府県ごとに）
     * 
     * @param prefId 都道府県ID
     * @param order ソートフラグ(0:disp_pos順、1:地域順、2:有効期限の昇順、3:ホテル名の昇順)
     * @param countNum 取得件数（0：全件 ※pageNum無視
     * @param pageNum ページ番号（0～)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotel(int prefId, int order, int countNum, int pageNum)
    {
        int i = 0;
        int today = 0;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        today = Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT A.* FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( prefId > 0 )
            {
                query += " AND B.pref_id = ?";
            }
            query += " GROUP BY A.id";

            // disp_pos順
            if ( order == 0 )
            {
                query += " ORDER BY A.start_date DESC";
            }
            // 地域順
            if ( order == 1 )
            {
                query += " ORDER BY B.pref_id, A.start_date DESC";
            }
            // 有効期限の終了日付の昇順
            else if ( order == 2 )
            {
                query += " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name, B.rank DESC, A.start_date DESC";

            }
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( prefId > 0 )
            {
                prestate.setInt( ++i, prefId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                hotelPresentOffer = new DataHotelPresentOffer[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();

                    this.m_hotelBasic[count].getData( result.getInt( "A.id" ) );
                    this.hotelPresentOffer[count].setData( result );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        if ( order < 0 || prefId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT COUNT(A.id) FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( prefId > 0 )
            {
                query += " AND B.pref_id = ?";
            }
            query += " GROUP BY A.id";

            // disp_pos順
            if ( order == 0 )
            {
                query += " ORDER BY A.start_date DESC";
            }
            // 地域順
            if ( order == 1 )
            {
                query += " ORDER BY B.pref_id, A.start_date DESC";
            }
            // 有効期限の終了日付の昇順
            else if ( order == 2 )
            {
                query += " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name, B.rank DESC, A.start_date DESC";

            }
        }

        try
        {
            i = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( prefId > 0 )
            {
                prestate.setInt( ++i, prefId );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * プレゼントホテル情報取得（地方ごとに）
     * 
     * @param localId 地方ID
     * @param order ソートフラグ(0:disp_pos順、1:地域順、2:有効期限の昇順、3:ホテル名の昇順)
     * @param countNum 取得件数（0：全件 ※pageNum無視
     * @param pageNum ページ番号（0～)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelByLocal(int localId, int order, int countNum, int pageNum)
    {
        int i = 0;
        int today;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "";
        today = Integer.parseInt( DateEdit.getDate( 2 ) );
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT A.* FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " INNER JOIN hh_master_pref C ON B.pref_id = C.pref_id ";

            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( localId > 0 )
            {
                query = query + " AND C.local_id = ?";
            }
            query = query + " GROUP BY A.id";
            // disp_pos順
            if ( order == 0 )
            {
                query = query + " ORDER BY A.start_date DESC";
            }
            // 地域順
            if ( order == 1 )
            {
                query = query + " ORDER BY B.pref_id, A.start_date DESC";
            }
            // 有効期限の終了日付の昇順
            else if ( order == 2 )
            {
                query = query + " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name_kana, B.rank DESC,";
                query += " A.start_date DESC";
            }
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );

            if ( localId > 0 )
            {
                prestate.setInt( ++i, localId );

            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                hotelPresentOffer = new DataHotelPresentOffer[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();

                    this.m_hotelBasic[count].getData( result.getInt( "A.id" ) );
                    this.hotelPresentOffer[count].setData( result );
                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelByLocal] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        if ( order < 0 || localId < 0 )
        {
            return(false);
        }
        else if ( order >= 0 && order <= 3 )
        {
            query = "SELECT COUNT(A.id) FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " INNER JOIN hh_master_pref C ON B.pref_id = C.pref_id ";

            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            if ( localId > 0 )
            {
                query = query + " AND C.local_id = ?";
            }
            query = query + " GROUP BY A.id";
            // disp_pos順
            if ( order == 0 )
            {
                query = query + " ORDER BY A.start_date DESC";
            }
            // 地域順
            if ( order == 1 )
            {
                query = query + " ORDER BY B.pref_id, A.start_date DESC";
            }
            // 有効期限の終了日付の昇順
            else if ( order == 2 )
            {
                query = query + " ORDER BY A.end_date, B.pref_id, A.start_date DESC";
            }
            else if ( order == 3 )
            {
                query += " ORDER BY B.name_kana, B.rank DESC,";
                query += " A.start_date DESC";
            }
        }
        try
        {
            i = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, today );
            prestate.setInt( ++i, today );
            if ( localId > 0 )
            {
                prestate.setInt( ++i, localId );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得to
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelByLocal] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * プレゼントホテル情報取得（最新のもの）
     * 
     * @param newSpan 新着表示する期間（○日前から）
     * @param countNum 取得件数（0：全件 ※pageNum無視
     * @param pageNum ページ番号（0～)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelLatest(int newSpan, int countNum, int pageNum)
    {
        int i = 0;
        int count;
        int startDate;
        int endDate;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        startDate = 0;
        endDate = 0;
        query = "";
        if ( newSpan < 0 )
        {
            return(false);
        }
        else
        {
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -newSpan );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );

            query = "SELECT A.* FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date >= ?";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            query = query + " GROUP BY A.id";
            query = query + " ORDER BY A.start_date DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, startDate );
            prestate.setInt( ++i, endDate );
            prestate.setInt( ++i, endDate );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getSortOfferHotel] count=" + result.getRow() );

                }
                m_hotelBasic = new DataHotelBasic[hotelCount];
                hotelPresentOffer = new DataHotelPresentOffer[hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_hotelBasic[count] = new DataHotelBasic();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();

                    this.m_hotelBasic[count].getData( result.getInt( "hotel_id" ) );
                    this.hotelPresentOffer[count].setData( result );

                    count++;
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelLatest] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテル総件数の取得
        if ( newSpan < 0 )
        {
            return(false);
        }
        else
        {
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -newSpan );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            query = "SELECT A.form_id, A.id FROM hh_hotel_present_offer A";
            query += " INNER JOIN hh_hotel_basic B ON A.id = B.id ";
            query += "  AND B.rank > 1";
            query += "  AND B.kind <= 7";
            query += " WHERE A.del_flag=0";
            query += "  AND A.start_date >= ?";
            query += "  AND A.start_date <= ?";
            query += "  AND A.end_date >= ?";

            query = query + " GROUP BY A.id";
            query = query + " ORDER BY A.start_date DESC";
        }
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, startDate );
            prestate.setInt( ++i, endDate );
            prestate.setInt( ++i, endDate );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelLatest] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * ホテルの応募一覧を取得
     * 
     * @param id
     * @param countNum
     * @param pageNum
     * @return
     */
    public boolean getHotel(int id, int countNum, int pageNum)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT * FROM hh_hotel_present_offer ";
        query += " WHERE del_flag=0";
        query += "  AND id = ?";

        query = query + " ORDER BY start_date DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                    Logging.error( "[getHotel] count=" + result.getRow() );

                }
                hotelPresentOffer = new DataHotelPresentOffer[this.hotelCount];

                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();
                    this.hotelPresentOffer[count].setData( result );
                    count++;
                }

                this.entryCount = new int[this.hotelCount];
                this.formSub = new DataHotelPresentOfferSub[this.hotelCount];

                for( i = 0 ; i < this.hotelCount ; i++ )
                {
                    this.entryCount[i] = this.getEntryCount( connection, this.hotelPresentOffer[i].getFormId(), this.hotelPresentOffer[i].getId() );
                    this.formSub[i] = new DataHotelPresentOfferSub();
                    this.getFormStatus( this.hotelPresentOffer[i], this.formSub[i] );
                }
            }
            else
            {
                this.hotelCount = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        query = "SELECT * FROM hh_hotel_present_offer ";
        query += " WHERE del_flag=0";
        query += "  AND id = ?";

        query = query + " ORDER BY start_date DESC";

        try
        {
            i = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.hotelAllCount = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( (hotelCount > 0) && (hotelAllCount > 0) )
            ret = true;
        else
            ret = false;

        return(ret);
    }

    /**
     * ホテルの応募一覧を取得
     * 
     * @param id
     * @param countNum
     * @param pageNum
     * @return
     */
    public int getEntryCount(Connection con, int formId, int id)
    {

        int i = 0;
        int count;
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT COUNT(user_id) FROM hh_user_present_entry ";
        query += " WHERE form_id = ?";
        query += "  AND id = ?";
        query += "  AND status_flag < 9";

        count = 0;
        try
        {

            prestate = con.prepareStatement( query );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotel] Exception=" + e.toString() );
            return(0);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        return(count);
    }

    /**
     * ホテルの応募フォームのステータスを判断
     * 
     * @param dhpo DataHotelPresentOffer
     * @return
     */
    public void getFormStatus(DataHotelPresentOffer dhpo, DataHotelPresentOfferSub dhpoSub)
    {

        String status = "";
        String value = "";
        String url = "";

        // 応募期間内
        if ( dhpo.getStartDate() <= Integer.parseInt( DateEdit.getDate( 2 ) ) &&
                dhpo.getEndDate() >= Integer.parseInt( DateEdit.getDate( 2 ) ) )
        {
            if ( dhpo.getStatusFlag() == FORM_ENTRY )
            {
                status = "<font color='blue'>受付中</font>";
            }
            else
            {
                status = "ステータス異常";
            }
            value = "応募期間中";
        }
        else
        {
            switch( dhpo.getStatusFlag() )
            {
            // 応募中
                case FORM_ENTRY:
                    if ( dhpo.getStartDate() >= Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        status = "応募待";
                        value = "";

                    }
                    if ( dhpo.getEndDate() <= Integer.parseInt( DateEdit.getDate( 2 ) ) )
                    {
                        status = "<font color='red'>未処理</font>";
                        value = "抽選";
                        url = "owner_stay_draw_list.jsp";
                    }
                    break;
                case FORM_DROW:
                    status = "当選処理済（未確定）";
                    value = "当選メール作成";
                    url = "owner_stay_draw_confirm.jsp";
                    break;

                case FORM_DROW_FIX:
                    status = "当選処理済（確定）";
                    value = "当選メール作成";
                    url = "owner_stay_make_mail.jsp";
                    break;

                case FORM_MAKE_ELECT_MAIL:
                    status = "当選メール作成済";
                    value = "メール送信";
                    url = "owner_stay_send_mail.jsp";
                    break;

                case FORM_SEND_ELECT_MAIL:
                    status = "当選メール送信済";
                    value = "シリアル番号";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_DRAW_LOSE_FIX:
                    status = "はずれ処理済";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_MAKE_LOSE_MAIL:
                    status = "はずれメール作成済";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_SEND_LOSE_MAIL:
                    status = "はずれメール送信済";
                    url = "owner_stay_serial.jsp";
                    break;

                case FORM_STATUS_FIX:
                    status = "シリアル番号";
                    url = "owner_stay_serial.jsp";
                    break;
                default:
                    status = "ステータス異常";
                    break;
            }
        }
        dhpoSub.setFormId( dhpo.getFormId() );
        dhpoSub.setId( dhpo.getId() );
        dhpoSub.setFormUrl( url );
        dhpoSub.setFormValue( value );
        dhpoSub.setFormStatus( status );
    }

    /**
     * 応募ユーザ一覧データ
     * 
     * @param formId フォームID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getEntryUser(int formId, int id)
    {
        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = " SELECT A.form_id, A.id, B.status_flag, A.title, A.elect_no, C.user_id, C.handle_name, B.address1, B.address2, B.address3 ";
        query += " FROM hh_hotel_present_offer A";
        query += " INNER JOIN hh_user_present_entry B ON A.form_id = B.form_id AND A.id = B.id AND B.status_flag < 9";
        query += " INNER JOIN hh_user_basic C ON B.user_id = C.user_id AND C.regist_status = 9 AND C.del_flag = 0";
        query += "  WHERE A.form_id = ? AND A.id = ? AND A.status_flag < 9";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.userCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userPresentEntry = new DataUserPresentEntry[this.userCount];
                this.hotelPresentOffer = new DataHotelPresentOffer[this.userCount];
                this.handleNameList = new String[this.userCount];

                // レコードセットを先頭に戻す。
                result.beforeFirst();

                while( result.next() != false )
                {
                    this.userPresentEntry[count] = new DataUserPresentEntry();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();
                    this.handleNameList[count] = new String();

                    // ユーザ情報をセット
                    this.userPresentEntry[count].setFormId( result.getInt( "form_id" ) );
                    this.userPresentEntry[count].setId( result.getInt( "id" ) );
                    this.userPresentEntry[count].setStatusFlag( result.getInt( "status_flag" ) );
                    this.userPresentEntry[count].setUserId( result.getString( "user_id" ) );
                    this.userPresentEntry[count].setAddress1( result.getString( "address1" ) );
                    this.userPresentEntry[count].setAddress2( result.getString( "address2" ) );
                    this.userPresentEntry[count].setAddress3( result.getString( "address3" ) );

                    // this.userPresentEntry[count].setData( result );
                    // ホテル情報をセット
                    this.hotelPresentOffer[count].setFormId( result.getInt( "form_id" ) );
                    this.hotelPresentOffer[count].setId( result.getInt( "id" ) );
                    this.hotelPresentOffer[count].setTitle( result.getString( "title" ) );
                    this.hotelPresentOffer[count].setElectNo( result.getInt( "elect_no" ) );
                    // this.hotelPresentOffer[count].setData( result );
                    // ハンドルネームをセット
                    this.handleNameList[count] = result.getString( "handle_name" );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPresentOffer.getEntryUser()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }

    /**
     * ランダム抽選データ取得
     * 
     * @param collectDate 対象日付(0:最新PV)
     * @param getCount 取得件数(0:1000件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean lotteryPresent(int formId, int id, int electNo, String[] userIdList)
    {
        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String electUser = "";
        int electCount = 0;

        // 先に当選者を抽選する
        query = " SELECT A.form_id, A.id, B.status_flag, A.title, A.elect_no, C.user_id, C.handle_name, B.address1, B.address2, B.address3, B.freeword ";
        query += " FROM hh_hotel_present_offer A";
        query += " INNER JOIN hh_user_present_entry B ON A.form_id = B.form_id AND A.id = B.id AND B.status_flag BETWEEN 1 AND 2";
        query += " INNER JOIN hh_user_basic C ON B.user_id = C.user_id AND C.regist_status = 9 AND C.del_flag = 0";
        query += " WHERE A.form_id = ? AND A.id = ? AND A.status_flag < 9";
        // 残りの当選者数を応募者から取得(ランダムを有効にするため)
        query += " UNION ( SELECT A.form_id, A.id, B.status_flag, A.title, A.elect_no, C.user_id, C.handle_name, B.address1, B.address2, B.address3, B.freeword ";
        query += " FROM hh_hotel_present_offer A";
        query += " INNER JOIN hh_user_present_entry B ON A.form_id = B.form_id AND A.id = B.id AND B.status_flag = 0";
        query += " INNER JOIN hh_user_basic C ON B.user_id = C.user_id AND C.regist_status = 9 AND C.del_flag = 0";
        query += "  WHERE A.form_id = ? AND A.id = ? AND A.status_flag < 9";
        if ( userIdList != null )
        {
            query += " AND C.user_id IN (";
            for( i = 0 ; i < userIdList.length ; i++ )
            {
                if ( i > 0 )
                {
                    query += ",";
                }
                query += "'" + userIdList[i] + "'";
            }
            query += ") ";
        }
        query += " ORDER BY RAND()";
        if ( electNo > 0 )
        {
            // 当選していないユーザをランダム取得するためのlimit句
            query += " LIMIT 0, ? ";
        }
        query += " )";
        if ( electNo > 0 )
        {
            // 全体の当選者数を絞り込むためのlimit句
            query += " LIMIT 0, ? ";
        }

        count = 0;

        i = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );
            prestate.setInt( ++i, formId );
            prestate.setInt( ++i, id );
            if ( electNo > 0 )
            {
                prestate.setInt( ++i, electNo );
                prestate.setInt( ++i, electNo );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.userCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userPresentEntry = new DataUserPresentEntry[this.userCount];
                this.hotelPresentOffer = new DataHotelPresentOffer[this.userCount];
                this.handleNameList = new String[this.userCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.userPresentEntry[count] = new DataUserPresentEntry();
                    this.hotelPresentOffer[count] = new DataHotelPresentOffer();
                    this.handleNameList[count] = new String();

                    // ユーザ情報をセット
                    this.userPresentEntry[count].setFormId( result.getInt( "form_id" ) );
                    this.userPresentEntry[count].setId( result.getInt( "id" ) );
                    this.userPresentEntry[count].setStatusFlag( result.getInt( "status_flag" ) );
                    this.userPresentEntry[count].setUserId( result.getString( "user_id" ) );
                    this.userPresentEntry[count].setAddress1( result.getString( "address1" ) );
                    this.userPresentEntry[count].setAddress2( result.getString( "address2" ) );
                    this.userPresentEntry[count].setAddress3( result.getString( "address3" ) );
                    this.userPresentEntry[count].setFreeword( result.getString( "freeword" ) );

                    // this.userPresentEntry[count].setData( result );
                    // ホテル情報をセット
                    this.hotelPresentOffer[count].setFormId( result.getInt( "form_id" ) );
                    this.hotelPresentOffer[count].setId( result.getInt( "id" ) );
                    this.hotelPresentOffer[count].setTitle( result.getString( "title" ) );
                    this.hotelPresentOffer[count].setElectNo( result.getInt( "elect_no" ) );
                    // this.hotelPresentOffer[count].setData( result );
                    // ハンドルネームをセット
                    this.handleNameList[count] = result.getString( "handle_name" );

                    // 更新処理を行うユーザIDをまとめておく
                    if ( result.getInt( "status_flag" ) == 0 )
                    {
                        if ( electCount > 0 )
                        {
                            electUser += ",";
                        }
                        electUser += "'" + result.getString( "user_id" ) + "'";
                        electCount++;
                    }

                    count++;
                }

                // 当選していない人を仮当選させておく。
                query = " UPDATE hh_user_present_entry SET status_flag = 1";
                query += " WHERE form_id = ? AND id = ?";
                query += " AND user_id IN (" + electUser + ")";

                i = 0;
                prestate = connection.prepareStatement( query );
                prestate.setInt( ++i, formId );
                prestate.setInt( ++i, id );
                if ( prestate.executeUpdate() <= 0 )
                {
                    Logging.info( query );
                    Logging.info( "[HotelPresentOffer.lotteryPresent()] electUser Update Error" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelPresentOffer.lotteryPresent()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(true);
    }
}
