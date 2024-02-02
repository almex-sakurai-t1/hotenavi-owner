/*
 * @(#)DataHotelPv.java 1.00 2008/01/17 Copyright (C) ALMEX Inc. 2007 �z�e���y�[�W�r���[�f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * �z�e���y�[�W�r���[�f�[�^�擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2008/01/17
 */
public class DataHotelPv implements Serializable
{
    private static final long serialVersionUID = 5707400289111157022L;

    private int               id;
    private int               collectDate;
    private int               totalPv;
    private int               pcPv;
    private int               docomoPv;
    private int               auPv;
    private int               softbankPv;
    private int               prevDayRatio;
    private int               totalUuPv;
    private int               pcUuPv;
    private int               docomoUuPv;
    private int               auUuPv;
    private int               softbankUuPv;

    /**
     * �f�[�^�����������܂��B
     */
    public DataHotelPv()
    {
        id = 0;
        collectDate = 0;
        totalPv = 0;
        pcPv = 0;
        docomoPv = 0;
        auPv = 0;
        softbankPv = 0;
        prevDayRatio = 0;
        totalUuPv = 0;
        pcUuPv = 0;
        docomoUuPv = 0;
        auUuPv = 0;
        softbankUuPv = 0;
    }

    public int getAuPv()
    {
        return auPv;
    }

    public int getAuUuPv()
    {
        return auUuPv;
    }

    public int getCollectDate()
    {
        return collectDate;
    }

    public int getDocomoPv()
    {
        return docomoPv;
    }

    public int getDocomoUuPv()
    {
        return docomoUuPv;
    }

    public int getId()
    {
        return id;
    }

    public int getPcPv()
    {
        return pcPv;
    }

    public int getPcUuPv()
    {
        return pcUuPv;
    }

    public int getPrevDayRatio()
    {
        return prevDayRatio;
    }

    public int getSoftbankPv()
    {
        return softbankPv;
    }

    public int getSoftbankUuPv()
    {
        return softbankUuPv;
    }

    public int getTotalPv()
    {
        return totalPv;
    }

    public int getTotalUuPv()
    {
        return totalUuPv;
    }

    public void setAuPv(int auPv)
    {
        this.auPv = auPv;
    }

    public void setAuUuPv(int auUuPv)
    {
        this.auUuPv = auUuPv;
    }

    public void setCollectDate(int collectDate)
    {
        this.collectDate = collectDate;
    }

    public void setDocomoPv(int docomoPv)
    {
        this.docomoPv = docomoPv;
    }

    public void setDocomoUuPv(int docomoUuPv)
    {
        this.docomoUuPv = docomoUuPv;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPcPv(int pcPv)
    {
        this.pcPv = pcPv;
    }

    public void setPcUuPv(int pcUuPv)
    {
        this.pcUuPv = pcUuPv;
    }

    public void setPrevDayRatio(int prevDayRatio)
    {
        this.prevDayRatio = prevDayRatio;
    }

    public void setSoftbankPv(int softbankPv)
    {
        this.softbankPv = softbankPv;
    }

    public void setSoftbankUuPv(int softbankUuPv)
    {
        this.softbankUuPv = softbankUuPv;
    }

    public void setTotalPv(int totalPv)
    {
        this.totalPv = totalPv;
    }

    public void setTotalUuPv(int totalUuPv)
    {
        this.totalUuPv = totalUuPv;
    }

    /**
     * �z�e���y�[�W�r���[�f�[�^�擾
     * 
     * @param id �z�e���R�[�h
     * @param collectDate �W�v�Ώۓ��t
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int id, int collectDate)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_pv WHERE id = ? AND collect_date = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, collectDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.collectDate = result.getInt( "collect_date" );
                    this.totalPv = result.getInt( "total_pv" );
                    this.pcPv = result.getInt( "pc_pv" );
                    this.docomoPv = result.getInt( "docomo_pv" );
                    this.auPv = result.getInt( "au_pv" );
                    this.softbankPv = result.getInt( "softbank_pv" );
                    this.prevDayRatio = result.getInt( "prev_day_ratio" );
                    this.totalUuPv = result.getInt( "total_uu_pv" );
                    this.pcUuPv = result.getInt( "pc_uu_pv" );
                    this.docomoUuPv = result.getInt( "docomo_uu_pv" );
                    this.auUuPv = result.getInt( "au_uu_pv" );
                    this.softbankUuPv = result.getInt( "softbank_uu_pv" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPv.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �z�e���y�[�W�r���[�f�[�^�ݒ�
     * 
     * @param result �z�e���y�[�W�r���[�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.collectDate = result.getInt( "collect_date" );
                this.totalPv = result.getInt( "total_pv" );
                this.pcPv = result.getInt( "pc_pv" );
                this.docomoPv = result.getInt( "docomo_pv" );
                this.auPv = result.getInt( "au_pv" );
                this.softbankPv = result.getInt( "softbank_pv" );
                this.prevDayRatio = result.getInt( "prev_day_ratio" );
                this.totalUuPv = result.getInt( "total_uu_pv" );
                this.pcUuPv = result.getInt( "pc_uu_pv" );
                this.docomoUuPv = result.getInt( "docomo_uu_pv" );
                this.auUuPv = result.getInt( "au_uu_pv" );
                this.softbankUuPv = result.getInt( "softbank_uu_pv" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPv.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * �z�e���������f�[�^�ǉ�
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

        query = "INSERT hh_hotel_pv SET ";
        query = query + " id = ?,";
        query = query + " collect_date = ?,";
        query = query + " total_pv = ?,";
        query = query + " pc_pv = ?,";
        query = query + " docomo_pv = ?,";
        query = query + " au_pv = ?,";
        query = query + " softbank_pv = ?,";
        query = query + " prev_day_ratio = ?,";
        query = query + " total_uu_pv = ?,";
        query = query + " pc_uu_pv = ?,";
        query = query + " docomo_uu_pv = ?,";
        query = query + " au_uu_pv = ?,";
        query = query + " softbank_uu_pv = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.collectDate );
            prestate.setInt( 3, this.totalPv );
            prestate.setInt( 4, this.pcPv );
            prestate.setInt( 5, this.docomoPv );
            prestate.setInt( 6, this.auPv );
            prestate.setInt( 7, this.softbankPv );
            prestate.setInt( 8, this.prevDayRatio );
            prestate.setInt( 9, this.totalUuPv );
            prestate.setInt( 10, this.pcUuPv );
            prestate.setInt( 11, this.docomoUuPv );
            prestate.setInt( 12, this.auUuPv );
            prestate.setInt( 13, this.softbankUuPv );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelpv.insertData] Exception=" + e.toString() );
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
     * �z�e���������f�[�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �z�e��ID
     * @param collectDate �W�v�Ώۓ��t
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(int id, int collectDate)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_pv SET ";
        query = query + " total_pv = ?,";
        query = query + " pc_pv = ?,";
        query = query + " docomo_pv = ?,";
        query = query + " au_pv = ?,";
        query = query + " softbank_pv = ?,";
        query = query + " prev_day_ratio = ?,";
        query = query + " total_pv = ?,";
        query = query + " pc_pv = ?,";
        query = query + " docomo_pv = ?,";
        query = query + " au_pv = ?,";
        query = query + " softbank_pv = ?";
        query = query + " WHERE id = ?";
        query = query + " AND collect_date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.totalPv );
            prestate.setInt( 2, this.pcPv );
            prestate.setInt( 3, this.docomoPv );
            prestate.setInt( 4, this.auPv );
            prestate.setInt( 5, this.softbankPv );
            prestate.setInt( 6, this.prevDayRatio );
            prestate.setInt( 7, this.totalUuPv );
            prestate.setInt( 8, this.pcUuPv );
            prestate.setInt( 9, this.docomoUuPv );
            prestate.setInt( 10, this.auUuPv );
            prestate.setInt( 11, this.softbankUuPv );
            prestate.setInt( 12, id );
            prestate.setInt( 13, collectDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPv.updateData] Exception=" + e.toString() );
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
