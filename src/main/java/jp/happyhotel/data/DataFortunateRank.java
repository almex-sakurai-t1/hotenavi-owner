/*
 * @(#)DataFortunateRank.java 1.00 2008/12/02 Copyright (C) ALMEX Inc. 2008 �肢�֘A�f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * �肢�֘A�f�[�^�擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2008/12/02
 */
public class DataFortunateRank implements Serializable
{
    private static final long serialVersionUID = 8932757064302704155L;

    private int               dispDate;
    private int               dispRank;
    private int               seq;
    private int               itemSeq;
    private int               hotelSeq;

    /**
     * �f�[�^�����������܂��B
     */
    public DataFortunateRank()
    {
        dispDate = 0;
        dispRank = 0;
        seq = 0;
        itemSeq = 0;
        hotelSeq = 0;
    }

    public int getDispDate()
    {
        return dispDate;
    }

    public int getDispRank()
    {
        return dispRank;
    }

    public int getHotelSeq()
    {
        return hotelSeq;
    }

    public int getItemSeq()
    {
        return itemSeq;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setDispDate(int dispDate)
    {
        this.dispDate = dispDate;
    }

    public void setDispRank(int dispRank)
    {
        this.dispRank = dispRank;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    /**
     * �肢�֘A�f�[�^�擾
     * 
     * @param disp_date ���t
     * @param disp_rank �����L���O����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int dispDate, int dispRank)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_fortunate_rank WHERE disp_date = ? AND disp_rank = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dispDate );
            prestate.setInt( 2, dispRank );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.dispDate = result.getInt( "disp_date" );
                    this.dispRank = result.getInt( "disp_rank" );
                    this.seq = result.getInt( "seq" );
                    this.itemSeq = result.getInt( "item_seq" );
                    this.hotelSeq = result.getInt( "hotelSeq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateRank.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �肢�֘A�f�[�^�ݒ�
     * 
     * @param result �肢�֘A�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.dispDate = result.getInt( "disp_date" );
                this.dispRank = result.getInt( "disp_rank" );
                this.seq = result.getInt( "seq" );
                this.itemSeq = result.getInt( "item_seq" );
                this.hotelSeq = result.getInt( "hotel_seq" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateRank.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * �肢�֘A�f�[�^�ݒ�
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

        query = "INSERT hh_fortunate_rank SET";
        query = query + " disp_date = ?,";
        query = query + " disp_rank = ?,";
        query = query + " seq = ?,";
        query = query + " item_seq = ?,";
        query = query + " hotel_seq = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.dispDate );
            prestate.setInt( 2, this.dispRank );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.itemSeq );
            prestate.setInt( 5, this.hotelSeq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateRank.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �肢�֘A�f�[�^�ݒ�
     * 
     * @param kind ���
     * @param seq �Ǘ��ԍ�
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(int dispDate, int dispRank)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_fortunate_rank SET";
        query = query + " seq = ?,";
        query = query + " item_seq = ?,";
        query = query + " hotel_seq = ?";
        query = query + " WHERE disp_date = ?";
        query = query + " AND disp_rank = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.seq );
            prestate.setInt( 2, this.itemSeq );
            prestate.setInt( 3, this.hotelSeq );
            prestate.setInt( 4, dispDate );
            prestate.setInt( 5, dispRank );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataFortunateRank.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
