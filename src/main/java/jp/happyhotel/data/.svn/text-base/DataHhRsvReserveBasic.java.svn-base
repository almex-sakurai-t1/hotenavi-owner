package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 取得クラス
 * 
 * @author Keion.Park
 */
public class DataHhRsvReserveBasic implements Serializable
{
    public static final String TABLE = "hh_rsv_reserve_basic";
    private int                id;                            // ホテルID
    private String             reservePr;                     // 予約用PR
    private int                parking;                       // 駐車場利用区分: 1：入力有（台数有）、2：入力有（台数無）、3：入力無
    private int                salesFlag;                     // 販売フラグ
    private int                menOkFlag;                     // 男性多数許可フラグ
    private int                paymentKind;                   // 事前予約決済方法区分
    private int                localPaymentKind;              // 事前予約現地支払区分
    private int                paymentKind_today;             // 当日予約決済方法区分
    private int                localPaymentKind_today;        // 当日予約現地支払区分
    private int                localizeEnglish;               // 多言語対応（英）
    private int                localizeChinese;               // 多言語対応（中）
    private int                localizeKorean;                // 多言語対応（韓）
    private int                noLocalize;                    // 多言語対応しない
    private int                cancelAccrueDays;              // キャンセル料発生日数
    private int                cancelAccrueDaysRate;          // キャンセル料発生日割合
    private int                cancelAccrueTime;              // キャンセル料発生時間
    private int                cancelAccrueTimeRate;          // キャンセル料発生時間割合
    private int                cancelCoRate;                  // キャンセルチェックイン予定以降割合
    private String             cancelPolicy;                  // キャンセル規定
    private String             hotelId;                       // オーナーホテルID
    private int                userId;                        // ユーザID
    private String             tel;                           // 音声通知用電話番号
    private int                lastUpdate;                    // 最終更新日
    private int                lastUptime;                    // 最終更新時刻
    private int                rsvDateDue;                    // 予約掲載開始予定
    private String             rsvDateDueText;                // 予約掲載開始予定（テキスト)
    private int                rsvDateStart;                  // 予約掲載開始
    private int                nextAttentionDate;             // 次回表示日
    private int                nextAttentionTime;             // 次回表示時刻
    private int                paymentKindForeign;            // 外国人向け事前予約決済方法区分(1:事前決済させない 2:クレジットカード決済)
    private int                localPaymentKindForeign;       // 外国人向け事前予約現地支払区分(1:現地払いをさせない 2:電話番号認証させる 3:電話番号認証させない)
    private int                foreignLocalizeEnglish;        // 外国人向け多言語対応（英）
    private int                foreignLocalizeChinese;        // 外国人向け多言語対応（中）
    private int                foreignLocalizeKorean;         // 外国人向け多言語対応（韓）
    private int                foreignLocalizeTaiwan;         // 外国人向け多言語対応（台湾）
    private int                foreignNoLocalize;             // 外国人向け多言語対応しない
    private int                salesFlagForeign;              // 外国人向け販売フラグ
    private int                rsvfDateDue;                   // 外国人向け予約掲載開始予定
    private String             rsvfDateDueText;               // 外国人向け予約掲載開始予定（テキスト)
    private int                rsvfDateStart;                 // 外国人向け予約掲載開始
    private int                interval;                      // 予約猶予時間
    private String             restPrecautionDefault;         // 休憩予約プラン注意事項初期値
    private String             stayPrecautionDefault;         // 宿泊予約プラン注意事項初期値
    private int                salesFlagOta;                  // 販売フラグ(OTA)
    private int                rsvotaDateDue;                 // 予約掲載開始予定(OTA)
    private String             rsvotaDateDueText;             // 予約掲載開始予定（テキスト)(OTA)
    private int                rsvotafDateStart;              // 予約掲載開始(OTA)
    private int                citimeHideFlag;                // チェックイン可能時間の設定 0:表示 1:非表示
    private int                autoSalesFlag;                 // 当日限定プランの自動販売フラグ(0:自動販売しない 1:自動販売する)
    private int                autoSalesRoomCount;            // 自動販売開始空部屋数
    private int                hotelFeeOta;                   // OTA手数料

    /**
     * データを初期化します。
     */
    public DataHhRsvReserveBasic()
    {
        this.id = 0;
        this.reservePr = "";
        this.parking = 0;
        this.salesFlag = 0;
        this.menOkFlag = 0;
        this.paymentKind = 0;
        this.localPaymentKind = 0;
        this.paymentKind_today = 0;
        this.localPaymentKind_today = 0;
        this.localizeEnglish = 0;
        this.localizeChinese = 0;
        this.localizeKorean = 0;
        this.noLocalize = 0;
        this.cancelAccrueDays = 0;
        this.cancelAccrueDaysRate = 0;
        this.cancelAccrueTime = 0;
        this.cancelAccrueTimeRate = 0;
        this.cancelCoRate = 0;
        this.setCancelPolicy( "" );
        this.hotelId = "";
        this.userId = 0;
        this.tel = "";
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.rsvDateDue = 0;
        this.rsvDateDueText = "";
        this.rsvDateStart = 0;
        this.nextAttentionDate = 0;
        this.nextAttentionTime = 0;
        this.paymentKindForeign = 0;
        this.localPaymentKindForeign = 0;
        this.foreignLocalizeEnglish = 0;
        this.foreignLocalizeChinese = 0;
        this.foreignLocalizeKorean = 0;
        this.foreignLocalizeTaiwan = 0;
        this.foreignNoLocalize = 0;
        this.salesFlagForeign = 0;
        this.rsvfDateDue = 0;
        this.rsvfDateDueText = "";
        this.rsvfDateStart = 0;
        this.interval = 0;
        this.restPrecautionDefault = "";
        this.stayPrecautionDefault = "";
        this.salesFlagOta = 0;
        this.salesFlagOta = 0;
        this.rsvotaDateDue = 0;
        this.rsvotaDateDueText = "";
        this.rsvotafDateStart = 0;
        this.citimeHideFlag = 0;
        this.autoSalesFlag = 0;
        this.autoSalesRoomCount = 0;
        this.hotelFeeOta = 0;
    }

    public int getAutoSalesFlag()
    {
        return autoSalesFlag;
    }

    public int getCitimeHideFlag()
    {
        return citimeHideFlag;
    }

    public int getId()
    {
        return id;
    }

    public String getReservePr()
    {
        return reservePr;
    }

    public int getParking()
    {
        return parking;
    }

    public int getSalesFlag()
    {
        return salesFlag;
    }

    public int getMenOkFlag()
    {
        return menOkFlag;
    }

    public int getPaymentKind()
    {
        return paymentKind;
    }

    public int getLocalPaymentKind()
    {
        return localPaymentKind;
    }

    public int getPaymentKind_today()
    {
        return paymentKind_today;
    }

    public int getLocalPaymentKind_today()
    {
        return localPaymentKind_today;
    }

    public int getLocalizeEnglish()
    {
        return localizeEnglish;
    }

    public int getLocalizeChinese()
    {
        return localizeChinese;
    }

    public int getLocalizeKorean()
    {
        return localizeKorean;
    }

    public int getNoLocalize()
    {
        return noLocalize;
    }

    public int getCancelAccrueDays()
    {
        return cancelAccrueDays;
    }

    public int getCancelAccrueDaysRate()
    {
        return cancelAccrueDaysRate;
    }

    public int getCancelAccrueTime()
    {
        return cancelAccrueTime;
    }

    public int getCancelAccrueTimeRate()
    {
        return cancelAccrueTimeRate;
    }

    public int getCancelCoRate()
    {
        return cancelCoRate;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getTel()
    {
        return tel;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getRsvDateDue()
    {
        return rsvDateDue;
    }

    public String getRsvDateDueText()
    {
        return rsvDateDueText;
    }

    public int getRsvDateStart()
    {
        return rsvDateStart;
    }

    public int getNextAttentionDate()
    {
        return nextAttentionDate;
    }

    public int getNextAttentionTime()
    {
        return nextAttentionTime;
    }

    public int getPaymentKindForeign()
    {
        return paymentKindForeign;
    }

    public int getLocalPaymentKindForeign()
    {
        return localPaymentKindForeign;
    }

    public int getForeignLocalizeEnglish()
    {
        return foreignLocalizeEnglish;
    }

    public int getForeignLocalizeChinese()
    {
        return foreignLocalizeChinese;
    }

    public int getForeignLocalizeKorean()
    {
        return foreignLocalizeKorean;
    }

    public int getForeignLocalizeTaiwan()
    {
        return foreignLocalizeTaiwan;
    }

    public int getForeignNoLocalize()
    {
        return foreignNoLocalize;
    }

    public int getSalesFlagForeign()
    {
        return salesFlagForeign;
    }

    public int getRsvfDateDue()
    {
        return rsvfDateDue;
    }

    public String getRsvfDateDueText()
    {
        return rsvfDateDueText;
    }

    public int getRsvfDateStart()
    {
        return rsvfDateStart;
    }

    public int getInterval()
    {
        return interval;
    }

    public int getAutoSalesRoomCount()
    {
        return autoSalesRoomCount;
    }

    public int getHotelFeeOta()
    {
        return hotelFeeOta;
    }

    public void setHotelFeeOta(int hotelFeeOta)
    {
        this.hotelFeeOta = hotelFeeOta;
    }

    public void setAutoSalesRoomCount(int autoSalesRoomCount)
    {
        this.autoSalesRoomCount = autoSalesRoomCount;
    }

    public void setAutoSalesFlag(int autoSalesFlag)
    {
        this.autoSalesFlag = autoSalesFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setReservePr(String reservePr)
    {
        this.reservePr = reservePr;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
    }

    public void setMenOkFlag(int menOkFlag)
    {
        this.menOkFlag = menOkFlag;
    }

    public void setPaymentKind(int paymentKind)
    {
        this.paymentKind = paymentKind;
    }

    public void setLocalPaymentKind(int localPaymentKind)
    {
        this.localPaymentKind = localPaymentKind;
    }

    public void setPaymentKind_today(int paymentKind_today)
    {
        this.paymentKind_today = paymentKind_today;
    }

    public void setLocalPaymentKind_today(int localPaymentKind_today)
    {
        this.localPaymentKind_today = localPaymentKind_today;
    }

    public void setLocalizeEnglish(int localizeEnglish)
    {
        this.localizeEnglish = localizeEnglish;
    }

    public void setLocalizeChinese(int localizeChinese)
    {
        this.localizeChinese = localizeChinese;
    }

    public void setLocalizeKorean(int localizeKorean)
    {
        this.localizeKorean = localizeKorean;
    }

    public void setNoLocalize(int noLocalize)
    {
        this.noLocalize = noLocalize;
    }

    public void setCancelAccrueDays(int cancelAccrueDays)
    {
        this.cancelAccrueDays = cancelAccrueDays;
    }

    public void setCancelAccrueDaysRate(int cancelAccrueDaysRate)
    {
        this.cancelAccrueDaysRate = cancelAccrueDaysRate;
    }

    public void setCancelAccrueTime(int cancelAccrueTime)
    {
        this.cancelAccrueTime = cancelAccrueTime;
    }

    public void setCancelAccrueTimeRate(int cancelAccrueTimeRate)
    {
        this.cancelAccrueTimeRate = cancelAccrueTimeRate;
    }

    public void setCancelCoRate(int cancelCoRate)
    {
        this.cancelCoRate = cancelCoRate;
    }

    public String getCancelPolicy()
    {
        return cancelPolicy;
    }

    public void setCancelPolicy(String cancelPolicy)
    {
        this.cancelPolicy = cancelPolicy;
    }

    public void setCitimeHideFlag(int citimeHideFlag)
    {
        this.citimeHideFlag = citimeHideFlag;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setRsvDateDue(int rsvDateDue)
    {
        this.rsvDateDue = rsvDateDue;
    }

    public void setRsvDateDueText(String rsvDateDueText)
    {
        this.rsvDateDueText = rsvDateDueText;
    }

    public void setRsvDateStart(int rsvDateStart)
    {
        this.rsvDateStart = rsvDateStart;
    }

    public void setNextAttentionDate(int nextAttentionDate)
    {
        this.nextAttentionDate = nextAttentionDate;
    }

    public void setNextAttentionTime(int nextAttentionTime)
    {
        this.nextAttentionTime = nextAttentionTime;
    }

    public void setPaymentKindForeign(int paymentKindForeign)
    {
        this.paymentKindForeign = paymentKindForeign;
    }

    public void setLocalPaymentKindForeign(int localPaymentKindForeign)
    {
        this.localPaymentKindForeign = localPaymentKindForeign;
    }

    public void setForeignLocalizeEnglish(int foreignLocalizeEnglish)
    {
        this.foreignLocalizeEnglish = foreignLocalizeEnglish;
    }

    public void setForeignLocalizeChinese(int foreignLocalizeChinese)
    {
        this.foreignLocalizeChinese = foreignLocalizeChinese;
    }

    public void setForeignLocalizeKorean(int foreignLocalizeKorean)
    {
        this.foreignLocalizeKorean = foreignLocalizeKorean;
    }

    public void setForeignLocalizeTaiwan(int foreignLocalizeTaiwan)
    {
        this.foreignLocalizeTaiwan = foreignLocalizeTaiwan;
    }

    public void setForeignNoLocalize(int foreignNoLocalize)
    {
        this.foreignNoLocalize = foreignNoLocalize;
    }

    public void setSalesFlagForeign(int salesFlagForeign)
    {
        this.salesFlagForeign = salesFlagForeign;
    }

    public void setRsvfDateDue(int rsvfDateDue)
    {
        this.rsvfDateDue = rsvfDateDue;
    }

    public void setRsvfDateDueText(String rsvfDateDueText)
    {
        this.rsvfDateDueText = rsvfDateDueText;
    }

    public void setRsvfDateStart(int rsvfDateStart)
    {
        this.rsvfDateStart = rsvfDateStart;
    }

    public void setInterval(int interval)
    {
        this.interval = interval;
    }

    public String getRestPrecautionDefault()
    {
        return restPrecautionDefault;
    }

    public void setRestPrecautionDefault(String restPrecautionDefault)
    {
        this.restPrecautionDefault = restPrecautionDefault;
    }

    public String getStayPrecautionDefault()
    {
        return stayPrecautionDefault;
    }

    public void setStayPrecautionDefault(String stayPrecautionDefault)
    {
        this.stayPrecautionDefault = stayPrecautionDefault;
    }

    public int getSalesFlagOta()
    {
        return salesFlagOta;
    }

    public void setSalesFlagOta(int salesFlagOta)
    {
        this.salesFlagOta = salesFlagOta;
    }

    public int getRsvotaDateDue()
    {
        return rsvotaDateDue;
    }

    public void setRsvotaDateDue(int rsvotaDateDue)
    {
        this.rsvotaDateDue = rsvotaDateDue;
    }

    public String getRsvotaDateDueText()
    {
        return rsvotaDateDueText;
    }

    public void setRsvotaDateDueText(String rsvotaDateDueText)
    {
        this.rsvotaDateDueText = rsvotaDateDueText;
    }

    public int getRsvotaDateStart()
    {
        return rsvotafDateStart;
    }

    public void setRsvotafDateStart(int rsvotafDateStart)
    {
        this.rsvotafDateStart = rsvotafDateStart;
    }

    /**
     * 取得
     * 
     * @param id ホテルID
     * @return
     */
    public boolean getData(int id)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return this.getData( connection, id );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 取得
     * 
     * @param id ホテルID
     * @return
     */
    public boolean getData(Connection connection, int id)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "SELECT * FROM newRsvDB.hh_rsv_reserve_basic WHERE id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return this.setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, null );
        }
    }

    /**
     * 設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.reservePr = result.getString( "reserve_pr" );
                this.parking = result.getInt( "parking" );
                this.salesFlag = result.getInt( "sales_flag" );
                this.menOkFlag = result.getInt( "men_ok_flag" );
                this.paymentKind = result.getInt( "payment_kind" );
                this.localPaymentKind = result.getInt( "local_payment_kind" );
                this.paymentKind_today = result.getInt( "payment_kind_today" );
                this.localPaymentKind_today = result.getInt( "local_payment_kind_today" );
                this.localizeEnglish = result.getInt( "localize_english" );
                this.localizeChinese = result.getInt( "localize_chinese" );
                this.localizeKorean = result.getInt( "localize_korean" );
                this.noLocalize = result.getInt( "no_localize" );
                this.cancelAccrueDays = result.getInt( "cancel_accrue_days" );
                this.cancelAccrueDaysRate = result.getInt( "cancel_accrue_days_rate" );
                this.cancelAccrueTime = result.getInt( "cancel_accrue_time" );
                this.cancelAccrueTimeRate = result.getInt( "cancel_accrue_time_rate" );
                this.cancelCoRate = result.getInt( "cancel_co_rate" );
                this.setCancelPolicy( result.getString( "cancel_policy" ) );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.tel = result.getString( "tel" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.rsvDateDue = result.getInt( "rsv_date_due" );
                this.rsvDateDueText = result.getString( "rsv_date_due_text" );
                this.rsvDateStart = result.getInt( "rsv_date_start" );
                this.nextAttentionDate = result.getInt( "next_attention_date" );
                this.nextAttentionTime = result.getInt( "next_attention_time" );
                this.paymentKindForeign = result.getInt( "payment_kind_foreign" );
                this.localPaymentKindForeign = result.getInt( "local_payment_kind_foreign" );
                this.foreignLocalizeEnglish = result.getInt( "foreign_localize_english" );
                this.foreignLocalizeChinese = result.getInt( "foreign_localize_chinese" );
                this.foreignLocalizeKorean = result.getInt( "foreign_localize_korean" );
                this.foreignLocalizeTaiwan = result.getInt( "foreign_localize_taiwan" );
                this.foreignNoLocalize = result.getInt( "foreign_no_localize" );
                this.salesFlagForeign = result.getInt( "sales_flag_foreign" );
                this.rsvfDateDue = result.getInt( "rsvf_date_due" );
                this.rsvfDateDueText = result.getString( "rsvf_date_due_text" );
                this.rsvfDateStart = result.getInt( "rsvf_date_start" );
                this.interval = result.getInt( "interval" );
                this.restPrecautionDefault = result.getString( "rest_precaution_default" );
                this.stayPrecautionDefault = result.getString( "stay_precaution_default" );
                this.salesFlagOta = result.getInt( "sales_flag_ota" );
                this.rsvotaDateDue = result.getInt( "rsvota_date_due" );
                this.rsvotaDateDueText = result.getString( "rsvota_date_due_text" );
                this.rsvotafDateStart = result.getInt( "rsvota_date_start" );
                this.citimeHideFlag = result.getInt( "citime_hide_flag" );
                this.autoSalesFlag = result.getInt( "auto_sales_flag" );
                this.autoSalesRoomCount = result.getInt( "auto_sales_room_count" );
                this.hotelFeeOta = result.getInt( "hotel_fee_ota" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */
    public boolean updateData()
    {
        boolean ret = false;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = updateData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 更新(複数テーブル用)
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */

    public boolean updateData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_reserve_basic SET ";
        query += " reserve_pr=?";
        query += ", parking=?";
        query += ", sales_flag=?";
        query += ", men_ok_flag=?";
        query += ", payment_kind=?";
        query += ", local_payment_kind=?";
        query += ", payment_kind_today=?";
        query += ", local_payment_kind_today=?";
        query += ", localize_english=?";
        query += ", localize_chinese=?";
        query += ", localize_korean=?";
        query += ", no_localize=?";
        query += ", cancel_accrue_days=?";
        query += ", cancel_accrue_days_rate=?";
        query += ", cancel_accrue_time=?";
        query += ", cancel_accrue_time_rate=?";
        query += ", cancel_co_rate=?";
        query += ", cancel_policy=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", tel=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", rsv_date_due=?";
        query += ", rsv_date_due_text=?";
        query += ", rsv_date_start=?";
        query += ", next_attention_date=?";
        query += ", next_attention_time=?";
        query += ", payment_kind_foreign=?";
        query += ", local_payment_kind_foreign=?";
        query += ", foreign_localize_english=?";
        query += ", foreign_localize_chinese=?";
        query += ", foreign_localize_korean=?";
        query += ", foreign_localize_taiwan=?";
        query += ", foreign_no_localize=?";
        query += ", sales_flag_foreign=?";
        query += ", rsvf_date_due=?";
        query += ", rsvf_date_due_text=?";
        query += ", rsvf_date_start=?";
        query += ", `interval`=?";
        query += ", rest_precaution_default=?";
        query += ", stay_precaution_default=?";
        query += ", sales_flag_ota=?";
        query += ", rsvota_date_due=?";
        query += ", rsvota_date_due_text=?";
        query += ", rsvota_date_start=?";
        query += ", citime_hide_flag=?";
        query += ", auto_sales_flag=?";
        query += ", auto_sales_room_count=?";
        query += ", hotel_fee_almex=?";
        query += ", hotel_fee_ota=?";
        query += " WHERE id=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.reservePr );
            prestate.setInt( i++, this.parking );
            prestate.setInt( i++, this.salesFlag );
            prestate.setInt( i++, this.menOkFlag );
            prestate.setInt( i++, this.paymentKind );
            prestate.setInt( i++, this.localPaymentKind );
            prestate.setInt( i++, this.paymentKind_today );
            prestate.setInt( i++, this.localPaymentKind_today );
            prestate.setInt( i++, this.localizeEnglish );
            prestate.setInt( i++, this.localizeChinese );
            prestate.setInt( i++, this.localizeKorean );
            prestate.setInt( i++, this.noLocalize );
            prestate.setInt( i++, this.cancelAccrueDays );
            prestate.setInt( i++, this.cancelAccrueDaysRate );
            prestate.setInt( i++, this.cancelAccrueTime );
            prestate.setInt( i++, this.cancelAccrueTimeRate );
            prestate.setInt( i++, this.cancelCoRate );
            prestate.setString( i++, this.cancelPolicy );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setString( i++, this.tel );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.rsvDateDue );
            prestate.setString( i++, this.rsvDateDueText );
            prestate.setInt( i++, this.rsvDateStart );
            prestate.setInt( i++, this.nextAttentionDate );
            prestate.setInt( i++, this.nextAttentionTime );
            prestate.setInt( i++, this.paymentKindForeign );
            prestate.setInt( i++, this.localPaymentKindForeign );
            prestate.setInt( i++, this.foreignLocalizeEnglish );
            prestate.setInt( i++, this.foreignLocalizeChinese );
            prestate.setInt( i++, this.foreignLocalizeKorean );
            prestate.setInt( i++, this.foreignLocalizeTaiwan );
            prestate.setInt( i++, this.foreignNoLocalize );
            prestate.setInt( i++, this.salesFlagForeign );
            prestate.setInt( i++, this.rsvfDateDue );
            prestate.setString( i++, this.rsvfDateDueText );
            prestate.setInt( i++, this.rsvfDateStart );
            prestate.setInt( i++, this.interval );
            prestate.setString( i++, this.restPrecautionDefault );
            prestate.setString( i++, this.stayPrecautionDefault );
            prestate.setInt( i++, this.salesFlagOta );
            prestate.setInt( i++, this.rsvotaDateDue );
            prestate.setString( i++, this.rsvotaDateDueText );
            prestate.setInt( i++, this.rsvotafDateStart );
            prestate.setInt( i++, this.citimeHideFlag );
            prestate.setInt( i++, this.autoSalesFlag );
            prestate.setInt( i++, this.autoSalesRoomCount );
            prestate.setInt( i++, this.hotelFeeOta );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param Connection connection
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_reserve_basic SET ";
        query += " id=?";
        query += ", reserve_pr=?";
        query += ", parking=?";
        query += ", sales_flag=?";
        query += ", men_ok_flag=?";
        query += ", payment_kind=?";
        query += ", local_payment_kind=?";
        query += ", payment_kind_today=?";
        query += ", local_payment_kind_today=?";
        query += ", localize_english=?";
        query += ", localize_chinese=?";
        query += ", localize_korean=?";
        query += ", no_localize=?";
        query += ", cancel_accrue_days=?";
        query += ", cancel_accrue_days_rate=?";
        query += ", cancel_accrue_time=?";
        query += ", cancel_accrue_time_rate=?";
        query += ", cancel_co_rate=?";
        query += ", cancel_policy=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", tel=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", rsv_date_due=?";
        query += ", rsv_date_due_text=?";
        query += ", rsv_date_start=?";
        query += ", payment_kind_foreign=?";
        query += ", local_payment_kind_foreign=?";
        query += ", foreign_localize_english=?";
        query += ", foreign_localize_chinese=?";
        query += ", foreign_localize_korean=?";
        query += ", foreign_localize_taiwan=?";
        query += ", foreign_no_localize=?";
        query += ", sales_flag_foreign=?";
        query += ", rsvf_date_due=?";
        query += ", rsvf_date_due_text=?";
        query += ", rsvf_date_start=?";
        query += ", `interval`=?";
        query += ", rest_precaution_default=?";
        query += ", stay_precaution_default=?";
        query += ", sales_flag_ota=?";
        query += ", rsvota_date_due=?";
        query += ", rsvota_date_due_text=?";
        query += ", rsvota_date_start=?";
        query += ", citime_hide_flag=?";
        query += ", auto_sales_flag=?";
        query += ", auto_sales_room_count=?";
        query += ", hotel_fee_almex=?";
        query += ", hotel_fee_ota=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.reservePr );
            prestate.setInt( i++, this.parking );
            prestate.setInt( i++, this.salesFlag );
            prestate.setInt( i++, this.menOkFlag );
            prestate.setInt( i++, this.paymentKind );
            prestate.setInt( i++, this.localPaymentKind );
            prestate.setInt( i++, this.paymentKind_today );
            prestate.setInt( i++, this.localPaymentKind_today );
            prestate.setInt( i++, this.localizeEnglish );
            prestate.setInt( i++, this.localizeChinese );
            prestate.setInt( i++, this.localizeKorean );
            prestate.setInt( i++, this.noLocalize );
            prestate.setInt( i++, this.cancelAccrueDays );
            prestate.setInt( i++, this.cancelAccrueDaysRate );
            prestate.setInt( i++, this.cancelAccrueTime );
            prestate.setInt( i++, this.cancelAccrueTimeRate );
            prestate.setInt( i++, this.cancelCoRate );
            prestate.setString( i++, this.cancelPolicy );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setString( i++, this.tel );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.rsvDateDue );
            prestate.setString( i++, this.rsvDateDueText );
            prestate.setInt( i++, this.rsvDateStart );
            prestate.setInt( i++, this.paymentKindForeign );
            prestate.setInt( i++, this.localPaymentKindForeign );
            prestate.setInt( i++, this.foreignLocalizeEnglish );
            prestate.setInt( i++, this.foreignLocalizeChinese );
            prestate.setInt( i++, this.foreignLocalizeKorean );
            prestate.setInt( i++, this.foreignLocalizeTaiwan );
            prestate.setInt( i++, this.foreignNoLocalize );
            prestate.setInt( i++, this.salesFlagForeign );
            prestate.setInt( i++, this.rsvfDateDue );
            prestate.setString( i++, this.rsvfDateDueText );
            prestate.setInt( i++, this.rsvfDateStart );
            prestate.setInt( i++, this.interval );
            prestate.setString( i++, this.restPrecautionDefault );
            prestate.setString( i++, this.stayPrecautionDefault );
            prestate.setInt( i++, this.salesFlagOta );
            prestate.setInt( i++, this.rsvotaDateDue );
            prestate.setString( i++, this.rsvotaDateDueText );
            prestate.setInt( i++, this.rsvotafDateStart );
            prestate.setInt( i++, this.citimeHideFlag );
            prestate.setInt( i++, this.autoSalesFlag );
            prestate.setInt( i++, this.autoSalesRoomCount );
            prestate.setInt( i++, this.hotelFeeOta );

            result = prestate.executeUpdate();
            // Logging.info( "[でばっぐ]insert:" + ret + "ホテルID:" + this.id );
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveBasic.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
