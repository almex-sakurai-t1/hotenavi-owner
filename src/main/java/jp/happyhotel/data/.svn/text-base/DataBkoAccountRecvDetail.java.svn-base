/*
 * @(#)DataBkoAccountRecvDetail.java 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 売掛明細データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 売掛明細データ取得クラス
 * 
 * @author J.Horie
 * @version 1.00 2011/04/18
 */
public class DataBkoAccountRecvDetail implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -7574159004804209831L;

    private int               accrecvSlipNo;
    private int               slipDetailNo;
    private int               slipKind;
    private int               accountTitleCd;
    private String            accountTitleName;
    private int               amount;
    private int               point;
    private int               id;
    private String            reserveNo;
    private String            userId;
    private int               seq;
    private int               closingKind;

    /**
     * データを初期化します。
     */
    public DataBkoAccountRecvDetail()
    {
        accrecvSlipNo = 0;
        slipDetailNo = 0;
        slipKind = 0;
        accountTitleCd = 0;
        accountTitleName = "";
        amount = 0;
        point = 0;
        id = 0;
        reserveNo = "";
        userId = "";
        seq = 0;
        closingKind = 0;
    }

    /**
     * 売掛明細データ取得
     * 
     * @param accrecvSlipNo 売掛伝票No
     * @param slipDetailNo 伝票明細No
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int accrecvSlipNo, int slipDetailNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_account_recv_detail WHERE accrecv_slip_no = ? AND slip_detail_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            prestate.setInt( 2, slipDetailNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                    this.slipDetailNo = result.getInt( "slip_detail_no" );
                    this.slipKind = result.getInt( "slip_kind" );
                    this.accountTitleCd = result.getInt( "account_title_cd" );
                    this.accountTitleName = result.getString( "account_title_name" );
                    this.amount = result.getInt( "amount" );
                    this.point = result.getInt( "point" );
                    this.id = result.getInt( "id" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.closingKind = result.getInt( "closing_kind" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecvDetail.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 明細番号取得
     * 
     * @param accrecvSlipNo 売掛伝票No
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getSlipDetailNo(int accrecvSlipNo)
    {
        String query;
        slipDetailNo = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_account_recv_detail WHERE accrecv_slip_no = ? ORDER BY slip_detail_no DESC LIMIT 0,1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    slipDetailNo = result.getInt( "slip_detail_no" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecvDetail.getSlipDetailNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(slipDetailNo);
    }

    /**
     * 売掛明細データ削除
     * 
     * @param accrecvSlipNo 売掛伝票No
     * @return
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public void deleteData(int accrecvSlipNo)
    {
        String query;
        slipDetailNo = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_bko_account_recv_detail WHERE accrecv_slip_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecvDetail.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 売掛明細データ取得
     * 
     * @param accrecvSlipNo 売掛伝票No
     * @param titleCd タイトルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByTitleCd(int accrecvSlipNo, int accountTitleCd)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_account_recv_detail WHERE accrecv_slip_no = ? AND account_title_cd = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            prestate.setInt( 2, accountTitleCd );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                    this.slipDetailNo = result.getInt( "slip_detail_no" );
                    this.slipKind = result.getInt( "slip_kind" );
                    this.accountTitleCd = result.getInt( "account_title_cd" );
                    this.accountTitleName = result.getString( "account_title_name" );
                    this.amount = result.getInt( "amount" );
                    this.point = result.getInt( "point" );
                    this.id = result.getInt( "id" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.closingKind = result.getInt( "closing_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecvDetail.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 売掛データ取得
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
                this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                this.slipDetailNo = result.getInt( "slip_detail_no" );
                this.slipKind = result.getInt( "slip_kind" );
                this.accountTitleCd = result.getInt( "account_title_cd" );
                this.accountTitleName = result.getString( "account_title_name" );
                this.amount = result.getInt( "amount" );
                this.point = result.getInt( "point" );
                this.id = result.getInt( "id" );
                this.reserveNo = result.getString( "reserve_no" );
                this.userId = result.getString( "user_id" );
                this.seq = result.getInt( "seq" );
                this.closingKind = result.getInt( "closing_kind" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecvDetail.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 売掛明細データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_bko_account_recv_detail SET ";
        query = query + " slip_kind = ?,";
        query = query + " account_title_cd = ?,";
        query = query + " account_title_name = ?,";
        query = query + " amount = ?,";
        query = query + " point = ?,";
        query = query + " id = ?,";
        query = query + " reserve_no = ?,";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " closing_kind = ?,";
        query = query + " accrecv_slip_no = ?,"; // TODO 自動に
        query = query + " slip_detail_no = ?"; // TODO 自動に

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.slipKind );
            prestate.setInt( 2, this.accountTitleCd );
            prestate.setString( 3, this.accountTitleName );
            prestate.setInt( 4, this.amount );
            prestate.setInt( 5, this.point );
            prestate.setInt( 6, this.id );
            prestate.setString( 7, this.reserveNo );
            prestate.setString( 8, this.userId );
            prestate.setInt( 9, this.seq );
            prestate.setInt( 10, this.closingKind );
            prestate.setInt( 11, this.accrecvSlipNo );
            prestate.setInt( 12, this.slipDetailNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecvDetail.insertData] Exception=" + e.toString() );
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
     * 売掛明細データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_bko_account_recv_detail SET";
        query = query + " slip_kind = ?,";
        query = query + " account_title_cd = ?,";
        query = query + " account_title_name = ?,";
        query = query + " amount = ?,";
        query = query + " point = ?,";
        query = query + " id = ?,";
        query = query + " reserve_no = ?,";
        query = query + " user_id = ?,";
        query = query + " seq = ?,";
        query = query + " closing_kind = ?,";
        query = query + " accrecv_slip_no = ?,"; // TODO 自動に
        query = query + " slip_detail_no = ?"; // TODO 自動に

        query = query + " WHERE accrecv_slip_no = ?";
        query = query + " AND slip_detail_no = ?";

        // Logging.error("■updateDataQuery = " + query);

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.slipKind );
            prestate.setInt( 2, this.accountTitleCd );
            prestate.setString( 3, this.accountTitleName );
            prestate.setInt( 4, this.amount );
            prestate.setInt( 5, this.point );
            prestate.setInt( 6, this.id );
            prestate.setString( 7, this.reserveNo );
            prestate.setString( 8, this.userId );
            prestate.setInt( 9, this.seq );
            prestate.setInt( 10, this.closingKind );
            prestate.setInt( 11, this.accrecvSlipNo );
            prestate.setInt( 12, this.slipDetailNo );
            prestate.setInt( 13, this.accrecvSlipNo );
            prestate.setInt( 14, this.slipDetailNo );

            // Logging.error("■paaara = " + this.accrecvSlipNo + ":" + this.slipDetailNo);

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecv.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * accrecvSlipNoを取得します。
     * 
     * @return accrecvSlipNo
     */
    public int getAccrecvSlipNo()
    {
        return accrecvSlipNo;
    }

    /**
     * accrecvSlipNoを設定します。
     * 
     * @param accrecvSlipNo accrecvSlipNo
     */
    public void setAccrecvSlipNo(int accrecvSlipNo)
    {
        this.accrecvSlipNo = accrecvSlipNo;
    }

    /**
     * slipDetailNoを取得します。
     * 
     * @return slipDetailNo
     */
    public int getSlipDetailNo()
    {
        return slipDetailNo;
    }

    /**
     * slipDetailNoを設定します。
     * 
     * @param slipDetailNo slipDetailNo
     */
    public void setSlipDetailNo(int slipDetailNo)
    {
        this.slipDetailNo = slipDetailNo;
    }

    /**
     * slipKindを取得します。
     * 
     * @return slipKind
     */
    public int getSlipKind()
    {
        return slipKind;
    }

    /**
     * slipKindを設定します。
     * 
     * @param slipKind slipKind
     */
    public void setSlipKind(int slipKind)
    {
        this.slipKind = slipKind;
    }

    /**
     * accountTitleCdを取得します。
     * 
     * @return accountTitleCd
     */
    public int getAccountTitleCd()
    {
        return accountTitleCd;
    }

    /**
     * accountTitleCdを設定します。
     * 
     * @param accountTitleCd accountTitleCd
     */
    public void setAccountTitleCd(int accountTitleCd)
    {
        this.accountTitleCd = accountTitleCd;
    }

    /**
     * accountTitleNameを取得します。
     * 
     * @return accountTitleName
     */
    public String getAccountTitleName()
    {
        return accountTitleName;
    }

    /**
     * accountTitleNameを設定します。
     * 
     * @param accountTitleName accountTitleName
     */
    public void setAccountTitleName(String accountTitleName)
    {
        this.accountTitleName = accountTitleName;
    }

    /**
     * amountを取得します。
     * 
     * @return amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * amountを設定します。
     * 
     * @param amount amount
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * pointを取得します。
     * 
     * @return point
     */
    public int getPoint()
    {
        return point;
    }

    /**
     * pointを設定します。
     * 
     * @param point point
     */
    public void setPoint(int point)
    {
        this.point = point;
    }

    /**
     * idを取得します。
     * 
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * idを設定します。
     * 
     * @param id id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * reserveNoを取得します。
     * 
     * @return reserveNo
     */
    public String getReserveNo()
    {
        return reserveNo;
    }

    /**
     * reserveNoを設定します。
     * 
     * @param reserveNo reserveNo
     */
    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    /**
     * userIdを取得します。
     * 
     * @return userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * userIdを設定します。
     * 
     * @param userId userId
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * seqを取得します。
     * 
     * @return seq
     */
    public int getSeq()
    {
        return seq;
    }

    /**
     * seqを設定します。
     * 
     * @param seq seq
     */
    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    /**
     * closingKindを取得します。
     * 
     * @return closingKind
     */
    public int getClosingKind()
    {
        return closingKind;
    }

    /**
     * closingKindを設定します。
     * 
     * @param closingKind closingKind
     */
    public void setClosingKind(int closingKind)
    {
        this.closingKind = closingKind;
    }
}
