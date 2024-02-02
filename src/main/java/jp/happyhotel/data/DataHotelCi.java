package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Thu May 19 18:16:07 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * Hh_hotel_ciVo.
 * 
 * @author tashiro-s1
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2011/05/19 tashiro-s1 Generated.
 */
public class DataHotelCi implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -3263225031220088671L;
    private int               id;
    private int               seq;
    private int               subSeq;
    private int               ciDate;
    private int               ciTime;
    private int               ciStatus;
    private String            userId;
    private String            idm;
    private int               userSeq;
    private int               visitSeq;
    private int               visitPoint;
    private int               visitDate;
    private int               visitTime;
    private String            visitHotenaviId;
    private int               visitEmployeeCode;
    private String            roomNo;
    private int               usePoint;
    private int               useDate;
    private int               useTime;
    private String            useHotenaviId;
    private int               useEmployeeCode;
    private int               slipNo;
    private int               amount;
    private int               addPoint;
    private int               addTime;
    private int               addDate;
    private String            addHotenaviId;
    private int               addEmployeeCode;
    private int               lastUpdate;
    private int               lastUptime;
    private double            amountRate;
    private String            rsvNo;
    private int               allUseFlag;
    private int               allUsePoint;
    private int               fixFlag;
    private int               userType;
    private String            customId;                                // ホスト側メンバーID
    private int               useTempFlag;                             // 1:仮使用（非連動物件用）
    private int               extUserFlag;                             // 予約ユーザーの区別(0:ハピホテユーザー1:ハピホテユーザーではない)

    public DataHotelCi()
    {
        id = 0;
        seq = 0;
        subSeq = 0;
        ciDate = 0;
        ciTime = 0;
        ciStatus = 0;
        userId = "";
        idm = "";
        userSeq = 0;
        visitSeq = 0;
        visitPoint = 0;
        visitDate = 0;
        visitTime = 0;
        visitHotenaviId = "";
        visitEmployeeCode = 0;
        roomNo = "";
        usePoint = 0;
        useDate = 0;
        useTime = 0;
        useHotenaviId = "";
        useEmployeeCode = 0;
        slipNo = 0;
        amount = 0;
        addPoint = 0;
        addTime = 0;
        addDate = 0;
        addHotenaviId = "";
        addEmployeeCode = 0;
        lastUpdate = 0;
        lastUptime = 0;
        amountRate = 0;
        rsvNo = "";
        allUseFlag = 0;
        allUsePoint = 0;
        fixFlag = 0;
        userType = 0;
        customId = "";
        useTempFlag = 0;
        extUserFlag = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getSubSeq()
    {
        return subSeq;
    }

    public int getCiDate()
    {
        return ciDate;
    }

    public int getCiTime()
    {
        return ciTime;
    }

    public int getCiStatus()
    {
        return ciStatus;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getIdm()
    {
        return idm;
    }

    public int getUserSeq()
    {
        return userSeq;
    }

    public int getVisitSeq()
    {
        return visitSeq;
    }

    public int getVisitPoint()
    {
        return visitPoint;
    }

    public int getVisitDate()
    {
        return visitDate;
    }

    public int getVisitTime()
    {
        return visitTime;
    }

    public String getVisitHotenaviId()
    {
        return visitHotenaviId;
    }

    public int getVisitEmployeeCode()
    {
        return visitEmployeeCode;
    }

    public String getRoomNo()
    {
        return roomNo;
    }

    public int getUsePoint()
    {
        return usePoint;
    }

    public int getUseDate()
    {
        return useDate;
    }

    public int getUseTime()
    {
        return useTime;
    }

    public String getUseHotenaviId()
    {
        return useHotenaviId;
    }

    public int getUseEmployeeCode()
    {
        return useEmployeeCode;
    }

    public int getSlipNo()
    {
        return slipNo;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getAddPoint()
    {
        return addPoint;
    }

    public int getAddTime()
    {
        return addTime;
    }

    public int getAddDate()
    {
        return addDate;
    }

    public String getAddHotenaviId()
    {
        return addHotenaviId;
    }

    public int getAddEmployeeCode()
    {
        return addEmployeeCode;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public double getAmountRate()
    {
        return amountRate;
    }

    public String getRsvNo()
    {
        return rsvNo;
    }

    public int getAllUseFlag()
    {
        return allUseFlag;
    }

    public int getAllUsePoint()
    {
        return allUsePoint;
    }

    public int getFixFlag()
    {
        return fixFlag;
    }

    public int getUserType()
    {
        return userType;
    }

    public String getCustomId()
    {
        return customId;
    }

    public int getUseTempFlag()
    {
        return useTempFlag;
    }

    public int getExtUserFlag()
    {
        return extUserFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setSubSeq(int subSeq)
    {
        this.subSeq = subSeq;
    }

    public void setCiDate(int ciDate)
    {
        this.ciDate = ciDate;
    }

    public void setCiTime(int ciTime)
    {
        this.ciTime = ciTime;
    }

    public void setCiStatus(int ciStatus)
    {
        this.ciStatus = ciStatus;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setIdm(String idm)
    {
        this.idm = idm;
    }

    public void setUserSeq(int userSeq)
    {
        this.userSeq = userSeq;
    }

    public void setVisitSeq(int visitSeq)
    {
        this.visitSeq = visitSeq;
    }

    public void setVisitPoint(int visitPoint)
    {
        this.visitPoint = visitPoint;
    }

    public void setVisitDate(int visitDate)
    {
        this.visitDate = visitDate;
    }

    public void setVisitTime(int visitTime)
    {
        this.visitTime = visitTime;
    }

    public void setVisitHotenaviId(String visitHotenaviId)
    {
        this.visitHotenaviId = visitHotenaviId;
    }

    public void setVisitEmployeeCode(int visitEmployeeCode)
    {
        this.visitEmployeeCode = visitEmployeeCode;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setUsePoint(int usePoint)
    {
        this.usePoint = usePoint;
    }

    public void setUseDate(int useDate)
    {
        this.useDate = useDate;
    }

    public void setUseTime(int useTime)
    {
        this.useTime = useTime;
    }

    public void setUseHotenaviId(String useHotenaviId)
    {
        this.useHotenaviId = useHotenaviId;
    }

    public void setUseEmployeeCode(int useEmployeeCode)
    {
        this.useEmployeeCode = useEmployeeCode;
    }

    public void setSlipNo(int slipNo)
    {
        this.slipNo = slipNo;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setAddPoint(int addPoint)
    {
        this.addPoint = addPoint;
    }

    public void setAddTime(int addTime)
    {
        this.addTime = addTime;
    }

    public void setAddDate(int addDate)
    {
        this.addDate = addDate;
    }

    public void setAddHotenaviId(String addHotenaviId)
    {
        this.addHotenaviId = addHotenaviId;
    }

    public void setAddEmployeeCode(int addEmployeeCode)
    {
        this.addEmployeeCode = addEmployeeCode;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setAmountRate(Double amountRate)
    {
        this.amountRate = amountRate;
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = rsvNo;
    }

    public void setAllUseFlag(int allUseFlag)
    {
        this.allUseFlag = allUseFlag;
    }

    public void setAllUsePoint(int allUsePoint)
    {
        this.allUsePoint = allUsePoint;
    }

    public void setFixFlag(int fixFlag)
    {
        this.fixFlag = fixFlag;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setUseTempFlag(int useTempFlag)
    {
        this.useTempFlag = useTempFlag;
    }

    public void setExtUserFlag(int extUserFlag)
    {
        this.extUserFlag = extUserFlag;
    }

    /**
     * ホテルチェックインデータ取得
     * 
     * @param licenceKey ライセンスキー
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int seq, int subSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_ci WHERE id=? AND seq=? AND sub_seq=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            prestate.setInt( 3, subSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCi.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルチェックインデータ取得
     * 
     * @param id ホテルID
     *            @
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_ci WHERE id=? AND seq=? ORDER BY seq DESC, sub_seq DESC";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCi.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルチェックインデータ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.subSeq = result.getInt( "sub_seq" );
                this.ciDate = result.getInt( "ci_date" );
                this.ciTime = result.getInt( "ci_time" );
                this.ciStatus = result.getInt( "ci_status" );
                this.userId = result.getString( "user_id" );
                this.idm = result.getString( "idm" );
                this.userSeq = result.getInt( "user_seq" );
                this.visitSeq = result.getInt( "visit_seq" );
                this.visitPoint = result.getInt( "visit_point" );
                this.visitDate = result.getInt( "visit_date" );
                this.visitTime = result.getInt( "visit_time" );
                this.visitHotenaviId = result.getString( "visit_hotenavi_id" );
                this.visitEmployeeCode = result.getInt( "visit_employee_code" );
                this.roomNo = result.getString( "room_no" );
                this.usePoint = result.getInt( "use_point" );
                this.useDate = result.getInt( "use_date" );
                this.useTime = result.getInt( "use_time" );
                this.useHotenaviId = result.getString( "use_hotenavi_id" );
                this.useEmployeeCode = result.getInt( "use_employee_code" );
                this.slipNo = result.getInt( "slip_no" );
                this.amount = result.getInt( "amount" );
                this.addPoint = result.getInt( "add_point" );
                this.addDate = result.getInt( "use_date" );
                this.addTime = result.getInt( "use_time" );
                this.addHotenaviId = result.getString( "use_hotenavi_id" );
                this.addEmployeeCode = result.getInt( "use_employee_code" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.amountRate = result.getDouble( "amount_rate" );
                this.rsvNo = result.getString( "rsv_no" );
                this.allUseFlag = result.getInt( "alluse_flag" );
                this.allUsePoint = result.getInt( "alluse_point" );
                this.fixFlag = result.getInt( "fix_flag" );
                this.userType = result.getInt( "user_type" );
                this.customId = result.getString( "custom_id" );
                this.useTempFlag = result.getInt( "use_temp_flag" );
                this.extUserFlag = result.getInt( "ext_user_flag" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCi.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ホテルチェックインデータ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        boolean retConf = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataHotelHapiTouchConfig dhhtc;

        ret = false;

        query = "INSERT hh_hotel_ci SET ";
        query += " id= ?,";
        query += " seq =?,";
        query += " sub_seq =?,";
        query += " ci_date = ?,";
        query += " ci_time  = ?,";
        query += " ci_status = ?,";
        query += " user_id = ?,";
        query += " idm = ?,";
        query += " user_seq = ?,";
        query += " visit_seq = ?,";
        query += " visit_point = ?,";
        query += " visit_date = ?,";
        query += " visit_time = ?,";
        query += " visit_hotenavi_id = ?,";
        query += " visit_employee_code = ?,";
        query += " room_no = ?,";
        query += " use_point = ?,";
        query += " use_date = ?,";
        query += " use_time = ?,";
        query += " use_hotenavi_id = ?,";
        query += " use_employee_code = ?,";
        query += " slip_no = ?,";
        query += " amount = ?,";
        query += " add_point = ?,";
        query += " add_date = ?,";
        query += " add_time = ?,";
        query += " add_hotenavi_id = ?,";
        query += " add_employee_code = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " amount_rate = ?,";
        query += " rsv_no = ?,";
        query += " alluse_flag = ?,";
        query += " alluse_point = ?,";
        query += " fix_flag = ?,";
        query += " user_type = ?,";
        query += " custom_id = ?,";
        query += " use_temp_flag = ?,";
        query += " ext_user_flag = ?";

        try
        {

            // 実行するクエリーをログに出力
            // Logging.info( "[DataHotelCi.insertData] query:" + query );
            // クエリーにセットする値をログに出力
            Logging.info( "[DataHotelCi.insertData] :"
                    + " id=" + id + ", seq=" + seq + ", sub_seq=" + subSeq + ", roomNo=" + this.roomNo
                    + ", point=" + this.usePoint + ", slipNo=" + this.slipNo + ", amount=" + this.amount );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.seq );
            prestate.setInt( 3, this.subSeq );
            prestate.setInt( 4, this.ciDate );
            prestate.setInt( 5, this.ciTime );
            prestate.setInt( 6, this.ciStatus );
            prestate.setString( 7, this.userId );
            prestate.setString( 8, this.idm );
            prestate.setInt( 9, this.userSeq );
            prestate.setInt( 10, this.visitSeq );
            prestate.setInt( 11, this.visitPoint );
            prestate.setInt( 12, this.visitDate );
            prestate.setInt( 13, this.visitTime );
            prestate.setString( 14, this.visitHotenaviId );
            prestate.setInt( 15, this.visitEmployeeCode );
            prestate.setString( 16, this.roomNo );
            prestate.setInt( 17, this.usePoint );
            prestate.setInt( 18, this.useDate );
            prestate.setInt( 19, this.useTime );
            prestate.setString( 20, this.useHotenaviId );
            prestate.setInt( 21, this.useEmployeeCode );
            prestate.setInt( 22, this.slipNo );
            prestate.setInt( 23, this.amount );
            prestate.setInt( 24, this.addPoint );
            prestate.setInt( 25, this.addDate );
            prestate.setInt( 26, this.addTime );
            prestate.setString( 27, this.addHotenaviId );
            prestate.setInt( 28, this.addEmployeeCode );
            prestate.setInt( 29, this.lastUpdate );
            prestate.setInt( 30, this.lastUptime );
            prestate.setDouble( 31, this.amountRate );
            prestate.setString( 32, this.rsvNo );
            prestate.setInt( 33, this.allUseFlag );
            prestate.setInt( 34, this.allUsePoint );
            prestate.setInt( 35, this.fixFlag );
            prestate.setInt( 36, this.userType );
            prestate.setString( 37, this.customId );
            prestate.setInt( 38, this.useTempFlag );
            prestate.setInt( 39, this.extUserFlag );

            result = prestate.executeUpdate();

            // 実行結果の行数をログに出力
            Logging.info( "[DataHotelCi.insertData] updateResult:" + result + " rows" );

            if ( result > 0 )
            {
                if ( this.seq == 0 )
                {
                    // AUTO_INCREMENTでインサートした場合、書込んだseqをセットする
                    ret = GetInsertedData( connection );
                }
                else
                {
                    ret = true;
                }
                // チェックインデータ更新日付・時刻をハピホテタッチ設定データに登録
                dhhtc = new DataHotelHapiTouchConfig();
                retConf = dhhtc.getData( this.id );

                dhhtc.setId( this.id );
                dhhtc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dhhtc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dhhtc.setCiUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dhhtc.setCiUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( retConf != false )
                {
                    retConf = dhhtc.updateData( id );
                }
                else
                {
                    retConf = dhhtc.insertData();
                }
                // 登録処理、更新処理に失敗したらログに残す
                if ( retConf == false )
                {
                    Logging.error( "[DataHotelHapiTouchConfig] registFailed:hh_hotel_ci.id=" + this.id + ", seq=" + this.seq +
                            ", sub_seq=" + this.subSeq );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCi.insertData] Exception=" + e.toString() );
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
     * ホテルチェックインデータ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param licencekey ライセンスキー
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq, int subSeq)
    {
        int result;
        boolean ret;
        boolean retConf = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataHotelHapiTouchConfig dhhtc;

        ret = false;

        query = "UPDATE hh_hotel_ci SET ";
        query += " ci_date = ?,";
        query += " ci_time  = ?,";
        query += " ci_status = ?,";
        query += " user_id = ?,";
        query += " idm = ?,";
        query += " user_seq = ?,";
        query += " visit_seq = ?,";
        query += " visit_point = ?,";
        query += " visit_date = ?,";
        query += " visit_time = ?,";
        query += " visit_hotenavi_id = ?,";
        query += " visit_employee_code = ?,";
        query += " room_no = ?,";
        query += " use_point = ?,";
        query += " use_date = ?,";
        query += " use_time = ?,";
        query += " use_hotenavi_id = ?,";
        query += " use_employee_code = ?,";
        query += " slip_no = ?,";
        query += " amount = ?,";
        query += " add_point = ?,";
        query += " add_date = ?,";
        query += " add_time = ?,";
        query += " add_hotenavi_id = ?,";
        query += " add_employee_code = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " amount_rate = ?,";
        query += " rsv_no = ?,";
        query += " alluse_flag = ?,";
        query += " alluse_point = ?,";
        query += " fix_flag = ?,";
        query += " user_type = ?,";
        query += " custom_id = ?,";
        query += " use_temp_flag = ?,";
        query += " ext_user_flag = ?";
        query += " WHERE id= ? AND seq =? AND sub_seq=?";

        try
        {
            // 実行するクエリーをログに出力
            // Logging.info( "[DataHotelCi.updateData] query:" + query );
            // クエリーにセットする値をログに出力
            Logging.info( "[DataHotelCi.updateData] :"
                    + " id=" + id + ", seq=" + seq + ", sub_seq=" + subSeq + ", roomNo=" + this.roomNo
                    + ", point=" + this.usePoint + ", slipNo=" + this.slipNo + ", amount=" + this.amount );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.ciDate );
            prestate.setInt( 2, this.ciTime );
            prestate.setInt( 3, this.ciStatus );
            prestate.setString( 4, this.userId );
            prestate.setString( 5, this.idm );
            prestate.setInt( 6, this.userSeq );
            prestate.setInt( 7, this.visitSeq );
            prestate.setInt( 8, this.visitPoint );
            prestate.setInt( 9, this.visitDate );
            prestate.setInt( 10, this.visitTime );
            prestate.setString( 11, this.visitHotenaviId );
            prestate.setInt( 12, this.visitEmployeeCode );
            prestate.setString( 13, this.roomNo );
            prestate.setInt( 14, this.usePoint );
            prestate.setInt( 15, this.useDate );
            prestate.setInt( 16, this.useTime );
            prestate.setString( 17, this.useHotenaviId );
            prestate.setInt( 18, this.useEmployeeCode );
            prestate.setInt( 19, this.slipNo );
            prestate.setInt( 20, this.amount );
            prestate.setInt( 21, this.addPoint );
            prestate.setInt( 22, this.addDate );
            prestate.setInt( 23, this.addTime );
            prestate.setString( 24, this.addHotenaviId );
            prestate.setInt( 25, this.addEmployeeCode );
            prestate.setInt( 26, this.lastUpdate );
            prestate.setInt( 27, this.lastUptime );
            prestate.setDouble( 28, this.amountRate );
            prestate.setString( 29, this.rsvNo );
            prestate.setInt( 30, this.allUseFlag );
            prestate.setInt( 31, this.allUsePoint );
            prestate.setInt( 32, this.fixFlag );
            prestate.setInt( 33, this.userType );
            prestate.setString( 34, this.customId );
            prestate.setInt( 35, this.useTempFlag );
            prestate.setInt( 36, this.extUserFlag );
            prestate.setInt( 37, id );
            prestate.setInt( 38, seq );
            prestate.setInt( 39, subSeq );

            result = prestate.executeUpdate();

            // 実行結果の行数をログに出力
            Logging.info( "[DataHotelCi.updateData] updateResult:" + result + " rows" );

            if ( result > 0 )
            {
                ret = true;

                // チェックインデータ更新日付・時刻をハピホテタッチ設定データに登録
                dhhtc = new DataHotelHapiTouchConfig();
                retConf = dhhtc.getData( this.id );

                dhhtc.setId( this.id );
                dhhtc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dhhtc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dhhtc.setCiUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dhhtc.setCiUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( retConf != false )
                {
                    retConf = dhhtc.updateData( id );
                }
                else
                {
                    retConf = dhhtc.insertData();
                }
                // 登録処理、更新処理に失敗したらログに残す
                if ( retConf == false )
                {
                    Logging.error( "[DataHotelHapiTouchConfig] registFailed:hh_hotel_ci.id=" + this.id + ", seq=" + this.seq +
                            ", sub_seq=" + this.subSeq );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCi.updateData] Exception=" + e.toString() );
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
     * ホテルチェックインデータ取得
     * 
     * @param licenceKey ライセンスキー
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getMaxSeq(int id, String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int maxSeq = 0;

        query = "SELECT MAX(seq) FROM hh_hotel_ci WHERE id=? AND user_id=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxSeq = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCi.getMaxSeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(maxSeq);
    }

    /**
     * 最後にAUTO INCREMENTで追加したseqをセット
     * 
     * @param conn Connection
     * @return boolean
     */
    public boolean GetInsertedData(Connection conn)
    {
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;
        boolean ret = false;

        query = "SELECT @LAST_INSERT_ID AS seq";

        try
        {
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.seq = result.getInt( "seq" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[DataHotelCi.GetInsertedData] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);
    }

}
