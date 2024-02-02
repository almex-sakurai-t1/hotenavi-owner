/*
 * @(#)DataBkoAccountRecvDetail.java 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 ���|���׃f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ���|���׃f�[�^�擾�N���X
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
     * �f�[�^�����������܂��B
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
     * ���|���׃f�[�^�擾
     * 
     * @param accrecvSlipNo ���|�`�[No
     * @param slipDetailNo �`�[����No
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * ���הԍ��擾
     * 
     * @param accrecvSlipNo ���|�`�[No
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * ���|���׃f�[�^�폜
     * 
     * @param accrecvSlipNo ���|�`�[No
     * @return
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * ���|���׃f�[�^�擾
     * 
     * @param accrecvSlipNo ���|�`�[No
     * @param titleCd �^�C�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * ���|�f�[�^�擾
     * 
     * @param result ���[�U��{�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * ���|���׃f�[�^�ǉ�
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
        query = query + " accrecv_slip_no = ?,"; // TODO ������
        query = query + " slip_detail_no = ?"; // TODO ������

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
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
     * ���|���׃f�[�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
        query = query + " accrecv_slip_no = ?,"; // TODO ������
        query = query + " slip_detail_no = ?"; // TODO ������

        query = query + " WHERE accrecv_slip_no = ?";
        query = query + " AND slip_detail_no = ?";

        // Logging.error("��updateDataQuery = " + query);

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

            // Logging.error("��paaara = " + this.accrecvSlipNo + ":" + this.slipDetailNo);

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
     * accrecvSlipNo���擾���܂��B
     * 
     * @return accrecvSlipNo
     */
    public int getAccrecvSlipNo()
    {
        return accrecvSlipNo;
    }

    /**
     * accrecvSlipNo��ݒ肵�܂��B
     * 
     * @param accrecvSlipNo accrecvSlipNo
     */
    public void setAccrecvSlipNo(int accrecvSlipNo)
    {
        this.accrecvSlipNo = accrecvSlipNo;
    }

    /**
     * slipDetailNo���擾���܂��B
     * 
     * @return slipDetailNo
     */
    public int getSlipDetailNo()
    {
        return slipDetailNo;
    }

    /**
     * slipDetailNo��ݒ肵�܂��B
     * 
     * @param slipDetailNo slipDetailNo
     */
    public void setSlipDetailNo(int slipDetailNo)
    {
        this.slipDetailNo = slipDetailNo;
    }

    /**
     * slipKind���擾���܂��B
     * 
     * @return slipKind
     */
    public int getSlipKind()
    {
        return slipKind;
    }

    /**
     * slipKind��ݒ肵�܂��B
     * 
     * @param slipKind slipKind
     */
    public void setSlipKind(int slipKind)
    {
        this.slipKind = slipKind;
    }

    /**
     * accountTitleCd���擾���܂��B
     * 
     * @return accountTitleCd
     */
    public int getAccountTitleCd()
    {
        return accountTitleCd;
    }

    /**
     * accountTitleCd��ݒ肵�܂��B
     * 
     * @param accountTitleCd accountTitleCd
     */
    public void setAccountTitleCd(int accountTitleCd)
    {
        this.accountTitleCd = accountTitleCd;
    }

    /**
     * accountTitleName���擾���܂��B
     * 
     * @return accountTitleName
     */
    public String getAccountTitleName()
    {
        return accountTitleName;
    }

    /**
     * accountTitleName��ݒ肵�܂��B
     * 
     * @param accountTitleName accountTitleName
     */
    public void setAccountTitleName(String accountTitleName)
    {
        this.accountTitleName = accountTitleName;
    }

    /**
     * amount���擾���܂��B
     * 
     * @return amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * amount��ݒ肵�܂��B
     * 
     * @param amount amount
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * point���擾���܂��B
     * 
     * @return point
     */
    public int getPoint()
    {
        return point;
    }

    /**
     * point��ݒ肵�܂��B
     * 
     * @param point point
     */
    public void setPoint(int point)
    {
        this.point = point;
    }

    /**
     * id���擾���܂��B
     * 
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * id��ݒ肵�܂��B
     * 
     * @param id id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * reserveNo���擾���܂��B
     * 
     * @return reserveNo
     */
    public String getReserveNo()
    {
        return reserveNo;
    }

    /**
     * reserveNo��ݒ肵�܂��B
     * 
     * @param reserveNo reserveNo
     */
    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    /**
     * userId���擾���܂��B
     * 
     * @return userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * userId��ݒ肵�܂��B
     * 
     * @param userId userId
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * seq���擾���܂��B
     * 
     * @return seq
     */
    public int getSeq()
    {
        return seq;
    }

    /**
     * seq��ݒ肵�܂��B
     * 
     * @param seq seq
     */
    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    /**
     * closingKind���擾���܂��B
     * 
     * @return closingKind
     */
    public int getClosingKind()
    {
        return closingKind;
    }

    /**
     * closingKind��ݒ肵�܂��B
     * 
     * @param closingKind closingKind
     */
    public void setClosingKind(int closingKind)
    {
        this.closingKind = closingKind;
    }
}
