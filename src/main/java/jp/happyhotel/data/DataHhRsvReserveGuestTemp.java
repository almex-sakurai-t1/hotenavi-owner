package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 仮予約取得クラス
 * 
 * @author mitsuhashi-k1
 * @version 1.00 2019/3/22
 */
public class DataHhRsvReserveGuestTemp implements Serializable
{
    public static final String TABLE = "hh_rsv_reserve_guest_temp";
    /** ホテルID */
    private int                id;
    /** 仮予約番号 */
    private long               reserveTempNo;
    /** ホテル情報 */
    private String             hotelData;
    /** 予約情報 */
    private String             reserveData;
    /** 予約フォーム（お客様情報入力その1）情報 */
    private String             page1Data;
    /** 予約フォーム（お客様情報入力その2）情報 */
    private String             page2Data;
    /**
     * 氏名（姓漢字）。
     */
    private String             nameLast;

    /**
     * 氏名（名漢字）。
     */
    private String             nameFirst;

    /**
     * 氏名（姓カナ）。
     */
    private String             nameLastKana;

    /**
     * 氏名（名カナ）。
     */
    private String             nameFirstKana;

    /**
     * 郵便番号。
     */
    private String             zipCd3;

    /**
     * 郵便番号。
     */
    private String             zipCd4;

    /**
     * 都道府県コード。
     */
    private int                prefCode;

    /**
     * 市区町村コード: 全国地方公共団体コード。
     */
    private int                jisCode;

    /**
     * 住所３: 丁目番地、アパート名等。
     */
    private String             address3;

    /**
     * 電話番号１。
     */
    private String             tel1;

    /**
     * リマインダーフラグ: 0：前日にお知らせメールを送信しない、1：前日にお知らせメールを送信する。
     */
    private int                reminderFlag;

    /**
     * メールアドレス（追加分）: メールリマインダー用のアドレス（新規ゲストのときはメールアドレス）。
     */
    private String             mailAddr;

    /**
     * 到着予定時刻: HHMMSS。
     */
    private int                estTimeArrival;

    /**
     * 大人人数（男）。
     */
    private int                numMan;

    /**
     * 大人人数（女）。
     */
    private int                numWoman;

    /**
     * 子供人数。
     */
    private int                numChild;

    /**
     * 駐車場利用区分: 1：利用あり、2：利用なし、3：駐車場なし。
     */
    private int                parking;

    /**
     * 駐車場台数。
     */
    private int                parkingCount;

    /**
     * うちハイルーフ台数。
     */
    private int                highroofCount;

    /**
     * 要望事項。
     */
    private String             demands;

    /**
     * 必須備考。
     */
    private String             remarks;

    /**
     * 必須選択事項。
     */
    private String             customerOption;

    /**
     * 有料オプション。
     */
    private String             additionalOption;
    /**
     * 必須選択事項 ユーザー入力値。
     */
    private String             customerOptionValues;

    /**
     * 有料オプション ユーザー入力値。
     */
    private String             additionalOptionValues;

    /**
     * ランクID。
     */
    private int                rankId;
    /**
     * 部屋番号。
     */
    private int                seq;
    /**
     * ユーザーID自動発行フラグ。
     */
    private int                issueUserIdFlag;

    /** 最終更新日 */
    private int                lastUpdate;
    /** 最終更新時刻 */
    private int                lastUptime;

    /**
     * データを初期化します。
     */
    public DataHhRsvReserveGuestTemp()
    {
        this.id = 0;
        this.reserveTempNo = 0L;
        this.hotelData = "";
        this.reserveData = "";
        this.page1Data = "";
        this.page2Data = "";
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.nameLast = "";
        this.nameFirst = "";
        this.nameLastKana = "";
        this.nameFirstKana = "";
        this.zipCd3 = "";
        this.zipCd4 = "";
        this.prefCode = 0;
        this.jisCode = 0;
        this.address3 = "";
        this.tel1 = "";
        this.reminderFlag = 0;
        this.mailAddr = "";
        this.estTimeArrival = 0;
        this.numMan = 0;
        this.numWoman = 0;
        this.numChild = 0;
        this.parking = 0;
        this.parkingCount = 0;
        this.highroofCount = 0;
        this.demands = "";
        this.remarks = "";
        this.customerOption = "";
        this.additionalOption = "";
        this.customerOptionValues = "";
        this.additionalOptionValues = "";
        this.rankId = 0;
        this.seq = 0;
        this.issueUserIdFlag = 0;
    }

    public int getId()
    {
        return id;
    }

    public long getReserveTempNo()
    {
        return reserveTempNo;
    }

    public String getHotelData()
    {
        return hotelData;
    }

    public String getReserveData()
    {
        return reserveData;
    }

    public String getPage1Data()
    {
        return page1Data;
    }

    public String getPage2Data()
    {
        return page2Data;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setReserveTempNo(long reserveTempNo)
    {
        this.reserveTempNo = reserveTempNo;
    }

    public void setHotelData(String hotelData)
    {
        this.hotelData = hotelData;
    }

    public void setReserveData(String reserveData)
    {
        this.reserveData = reserveData;
    }

    public void setPage1Data(String page1Data)
    {
        this.page1Data = page1Data;
    }

    public void setPage2Data(String page2Data)
    {
        this.page2Data = page2Data;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public String getNameLast()
    {
        return nameLast;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

    public String getNameFirst()
    {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }

    public String getNameLastKana()
    {
        return nameLastKana;
    }

    public void setNameLastKana(String nameLastKana)
    {
        this.nameLastKana = nameLastKana;
    }

    public String getNameFirstKana()
    {
        return nameFirstKana;
    }

    public void setNameFirstKana(String nameFirstKana)
    {
        this.nameFirstKana = nameFirstKana;
    }

    public String getZipCd3()
    {
        return zipCd3;
    }

    public void setZipCd3(String zipCd3)
    {
        this.zipCd3 = zipCd3;
    }

    public String getZipCd4()
    {
        return zipCd4;
    }

    public void setZipCd4(String zipCd4)
    {
        this.zipCd4 = zipCd4;
    }

    public int getPrefCode()
    {
        return prefCode;
    }

    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public String getTel1()
    {
        return tel1;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public int getReminderFlag()
    {
        return reminderFlag;
    }

    public void setReminderFlag(int reminderFlag)
    {
        this.reminderFlag = reminderFlag;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public int getEstTimeArrival()
    {
        return estTimeArrival;
    }

    public void setEstTimeArrival(int estTimeArrival)
    {
        this.estTimeArrival = estTimeArrival;
    }

    public int getNumMan()
    {
        return numMan;
    }

    public void setNumMan(int numMan)
    {
        this.numMan = numMan;
    }

    public int getNumWoman()
    {
        return numWoman;
    }

    public void setNumWoman(int numWoman)
    {
        this.numWoman = numWoman;
    }

    public int getNumChild()
    {
        return numChild;
    }

    public void setNumChild(int numChild)
    {
        this.numChild = numChild;
    }

    public int getParking()
    {
        return parking;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public int getParkingCount()
    {
        return parkingCount;
    }

    public void setParkingCount(int parkingCount)
    {
        this.parkingCount = parkingCount;
    }

    public int getHighroofCount()
    {
        return highroofCount;
    }

    public void setHighroofCount(int highroofCount)
    {
        this.highroofCount = highroofCount;
    }

    public String getDemands()
    {
        return demands;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getCustomerOption()
    {
        return customerOption;
    }

    public void setCustomerOption(String customerOption)
    {
        this.customerOption = customerOption;
    }

    public String getAdditionalOption()
    {
        return additionalOption;
    }

    public void setAdditionalOption(String additionalOption)
    {
        this.additionalOption = additionalOption;
    }

    public String getCustomerOptionValues()
    {
        return customerOptionValues;
    }

    public void setCustomerOptionValues(String customerOptionValues)
    {
        this.customerOptionValues = customerOptionValues;
    }

    public String getAdditionalOptionValues()
    {
        return additionalOptionValues;
    }

    public void setAdditionalOptionValues(String additionalOptionValues)
    {
        this.additionalOptionValues = additionalOptionValues;
    }

    public int getRankId()
    {
        return rankId;
    }

    public void setRankId(int rankId)
    {
        this.rankId = rankId;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getIssueUserIdFlag()
    {
        return issueUserIdFlag;
    }

    public void setIssueUserIdFlag(int issueUserIdFlag)
    {
        this.issueUserIdFlag = issueUserIdFlag;
    }

    /**
     * データ取得
     * 
     * @param id ホテルID
     * @param reserveTempNo 仮予約番号
     * @return
     */
    public boolean getData(int id, long reserveTempNo)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return getData( connection, id, reserveTempNo );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveGuestTemp.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * データ取得
     * 
     * @param connection
     * @param id
     * @param reserveTempNo
     * @return
     */
    public boolean getData(Connection connection, int id, long reserveTempNo)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "SELECT * FROM newRsvDB.hh_rsv_reserve_guest_temp WHERE reserve_temp_no = ? ";
        query += " AND id = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setLong( 1, reserveTempNo );
            prestate.setInt( 2, id );

            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveGuestTemp.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * データ設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            this.id = result.getInt( "id" );
            this.reserveTempNo = result.getInt( "reserve_temp_no" );
            this.hotelData = result.getString( "hotel_data" );
            this.reserveData = result.getString( "reserve_data" );
            this.page1Data = result.getString( "page1_data" );
            this.page2Data = result.getString( "page2_data" );
            this.lastUpdate = result.getInt( "last_update" );
            this.lastUptime = result.getInt( "last_uptime" );
            this.nameLast = result.getString( "name_last" );
            this.nameFirst = result.getString( "name_first" );
            this.nameLastKana = result.getString( "name_last_kana" );
            this.nameFirstKana = result.getString( "name_first_kana" );
            this.zipCd3 = result.getString( "zip_cd3" );
            this.zipCd4 = result.getString( "zip_cd4" );
            this.prefCode = result.getInt( "pref_code" );
            this.jisCode = result.getInt( "jis_code" );
            this.address3 = result.getString( "address3" );
            this.tel1 = result.getString( "tel1" );
            this.reminderFlag = result.getInt( "reminder_flag" );
            this.mailAddr = result.getString( "mail_addr" );
            this.estTimeArrival = result.getInt( "est_time_arrival" );
            this.numMan = result.getInt( "num_man" );
            this.numWoman = result.getInt( "num_woman" );
            this.numChild = result.getInt( "num_child" );
            this.parking = result.getInt( "parking" );
            this.parkingCount = result.getInt( "parking_count" );
            this.highroofCount = result.getInt( "highroof_count" );
            this.demands = result.getString( "demands" );
            this.remarks = result.getString( "remarks" );
            this.customerOption = result.getString( "customer_option" );
            this.additionalOption = result.getString( "additional_option" );
            this.customerOptionValues = result.getString( "customer_option_values" );
            this.additionalOptionValues = result.getString( "additional_option_values" );
            this.rankId = result.getInt( "rank_id" );
            this.seq = result.getInt( "seq" );
            this.issueUserIdFlag = result.getInt( "issue_userId_flag" );

            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveGuestTemp.setData] Exception=" + e.toString() );
            return false;
        }
    }

    /**
     * データ挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_reserve_guest_temp SET ";
        query += " id=?";
        query += ", reserve_temp_no=?";
        query += ", hotel_data=?";
        query += ", reserve_data=?";
        query += ", page1_data=?";
        query += ", page2_data=?";
        query += ", name_last=?";
        query += ", name_first=?";
        query += ", name_last_kana=?";
        query += ", name_first_kana=?";
        query += ", zip_cd3=?";
        query += ", zip_cd4=?";
        query += ", pref_code=?";
        query += ", jis_code=?";
        query += ", address3=?";
        query += ", tel1=?";
        query += ", reminder_flag=?";
        query += ", mail_addr=?";
        query += ", est_time_arrival=?";
        query += ", num_man=?";
        query += ", num_woman=?";
        query += ", num_child=?";
        query += ", parking=?";
        query += ", parking_count=?";
        query += ", highroof_count=?";
        query += ", demands=?";
        query += ", remarks=?";
        query += ", customer_option=?";
        query += ", additional_option=?";
        query += ", customer_option_values=?";
        query += ", additional_option_values=?";
        query += ", rank_id=?";
        query += ", seq=?";
        query += ", issue_userId_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query, java.sql.Statement.RETURN_GENERATED_KEYS );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setLong( i++, this.reserveTempNo );
            prestate.setString( i++, this.hotelData );
            prestate.setString( i++, this.reserveData );
            prestate.setString( i++, this.page1Data );
            prestate.setString( i++, this.page2Data );
            prestate.setString( i++, this.nameLast );
            prestate.setString( i++, this.nameFirst );
            prestate.setString( i++, this.nameLastKana );
            prestate.setString( i++, this.nameFirstKana );
            prestate.setString( i++, this.zipCd3 );
            prestate.setString( i++, this.zipCd4 );
            prestate.setInt( i++, this.prefCode );
            prestate.setInt( i++, this.jisCode );
            prestate.setString( i++, this.address3 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.reminderFlag );
            prestate.setString( i++, this.mailAddr );
            prestate.setInt( i++, this.estTimeArrival );
            prestate.setInt( i++, this.numMan );
            prestate.setInt( i++, this.numWoman );
            prestate.setInt( i++, this.numChild );
            prestate.setInt( i++, this.parking );
            prestate.setInt( i++, this.parkingCount );
            prestate.setInt( i++, this.highroofCount );
            prestate.setString( i++, this.demands );
            prestate.setString( i++, this.remarks );
            prestate.setString( i++, this.customerOption );
            prestate.setString( i++, this.additionalOption );
            prestate.setString( i++, this.customerOptionValues );
            prestate.setString( i++, this.additionalOptionValues );
            prestate.setInt( i++, this.rankId );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.issueUserIdFlag );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveGuestTemp.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "UPDATE newRsvDB.hh_rsv_reserve_guest_temp SET ";
        query += "  hotel_data=?";
        query += ", reserve_data=?";
        query += ", page1_data=?";
        query += ", page2_data=?";
        query += ", name_last=?";
        query += ", name_first=?";
        query += ", name_last_kana=?";
        query += ", name_first_kana=?";
        query += ", zip_cd3=?";
        query += ", zip_cd4=?";
        query += ", pref_code=?";
        query += ", jis_code=?";
        query += ", address3=?";
        query += ", tel1=?";
        query += ", reminder_flag=?";
        query += ", mail_addr=?";
        query += ", est_time_arrival=?";
        query += ", num_man=?";
        query += ", num_woman=?";
        query += ", num_child=?";
        query += ", parking=?";
        query += ", parking_count=?";
        query += ", highroof_count=?";
        query += ", demands=?";
        query += ", remarks=?";
        query += ", customer_option=?";
        query += ", additional_option=?";
        query += ", customer_option_values=?";
        query += ", additional_option_values=?";
        query += ", rank_id=?";
        query += ", seq=?";
        query += ", issue_userId_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";

        query += " WHERE id=? AND reserve_temp_no=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.hotelData );
            prestate.setString( i++, this.reserveData );
            prestate.setString( i++, this.page1Data );
            prestate.setString( i++, this.page2Data );
            prestate.setString( i++, this.nameLast );
            prestate.setString( i++, this.nameFirst );
            prestate.setString( i++, this.nameLastKana );
            prestate.setString( i++, this.nameFirstKana );
            prestate.setString( i++, this.zipCd3 );
            prestate.setString( i++, this.zipCd4 );
            prestate.setInt( i++, this.prefCode );
            prestate.setInt( i++, this.jisCode );
            prestate.setString( i++, this.address3 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.reminderFlag );
            prestate.setString( i++, this.mailAddr );
            prestate.setInt( i++, this.estTimeArrival );
            prestate.setInt( i++, this.numMan );
            prestate.setInt( i++, this.numWoman );
            prestate.setInt( i++, this.numChild );
            prestate.setInt( i++, this.parking );
            prestate.setInt( i++, this.parkingCount );
            prestate.setInt( i++, this.highroofCount );
            prestate.setString( i++, this.demands );
            prestate.setString( i++, this.remarks );
            prestate.setString( i++, this.customerOption );
            prestate.setString( i++, this.additionalOption );
            prestate.setString( i++, this.customerOptionValues );
            prestate.setString( i++, this.additionalOptionValues );
            prestate.setInt( i++, this.rankId );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.issueUserIdFlag );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.id );
            prestate.setLong( i++, this.reserveTempNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveGuestTemp.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
