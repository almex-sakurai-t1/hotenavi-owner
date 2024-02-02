package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Feb 10 14:27:17 JST 2014
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataHotelUniquePv implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2414136006679849963L;
    private int               id;
    private int               collectDate;
    private int               totalUuPv;
    private int               prevDayRatio;
    private int               rank;
    private int               prevRank;

    public DataHotelUniquePv()
    {
        this.id = 0;
        this.collectDate = 0;
        this.totalUuPv = 0;
        this.prevDayRatio = 0;
        this.rank = 0;
        this.prevRank = 0;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getCollectDate()
    {
        return this.collectDate;
    }

    public void setCollectDate(int collectDate)
    {
        this.collectDate = collectDate;
    }

    public int getTotalUuPv()
    {
        return this.totalUuPv;
    }

    public void setTotalUuPv(int totalUuPv)
    {
        this.totalUuPv = totalUuPv;
    }

    public int getPrevDayRatio()
    {
        return this.prevDayRatio;
    }

    public void setPrevDayRatio(int prevDayRatio)
    {
        this.prevDayRatio = prevDayRatio;
    }

    public int getRank()
    {
        return rank;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public int getPrevRank()
    {
        return prevRank;
    }

    public void setPrevRank(int prevRank)
    {
        this.prevRank = prevRank;
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

        query = "SELECT * FROM hh_hotel_unique_pv WHERE id = ? AND collect_date = ?";
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
                    this.prevDayRatio = result.getInt( "prev_day_ratio" );
                    this.totalUuPv = result.getInt( "total_uu_pv" );
                    this.rank = result.getInt( "rank" );
                    this.prevRank = result.getInt( "prev_rank" );
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
                this.prevDayRatio = result.getInt( "prev_day_ratio" );
                this.totalUuPv = result.getInt( "total_uu_pv" );
                this.rank = result.getInt( "rank" );
                this.prevRank = result.getInt( "prev_rank" );
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

        query = "INSERT hh_hotel_unique_pv SET ";
        query = query + " id = ?,";
        query = query + " collect_date = ?,";
        query = query + " total_uu_pv = ?,";
        query = query + " prev_day_ratio = ?,";
        query = query + " rank= ?,";
        query = query + " prev_rank= ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.collectDate );
            prestate.setInt( 3, this.totalUuPv );
            prestate.setInt( 4, this.prevDayRatio );
            prestate.setInt( 5, this.rank );
            prestate.setInt( 6, this.prevRank );

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

        query = "UPDATE hh_hotel_unique_pv SET ";
        query = query + " prev_day_ratio = ?,";
        query = query + " total_uu_pv = ?,";
        query = query + " rank = ?,";
        query = query + " prev_rank = ?";
        query = query + " WHERE id = ?";
        query = query + " AND collect_date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.prevDayRatio );
            prestate.setInt( 2, this.totalUuPv );
            prestate.setInt( 3, this.rank );
            prestate.setInt( 4, this.prevRank );
            prestate.setInt( 5, id );
            prestate.setInt( 6, collectDate );

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

    /***
     * �z�e���\�[�g�擾
     * 
     * @param con �R�l�N�V����
     * @param id �z�e��ID
     * @param collectDate �W�v��
     * @return
     */
    public boolean getData(Connection con, int id, int collectDate)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_unique_pv WHERE id = ? AND collect_date = ?";

        try
        {
            prestate = con.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, collectDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.collectDate = result.getInt( "collect_date" );
                    this.prevDayRatio = result.getInt( "prev_day_ratio" );
                    this.totalUuPv = result.getInt( "total_uu_pv" );
                    this.rank = result.getInt( "rank" );
                    this.prevRank = result.getInt( "prev_rank" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUniquePv.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /***
     * �}�X�^�[�f�[�^�}��
     * 
     * @param con �R�l�N�V����
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return
     */
    public boolean insertData(Connection con)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_hotel_unique_pv SET ";
        query = query + " id = ?,";
        query = query + " collect_date = ?,";
        query = query + " total_uu_pv = ?,";
        query = query + " prev_day_ratio = ?,";
        query = query + " rank = ?,";
        query = query + " prev_rank=?";

        try
        {
            prestate = con.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.collectDate );
            prestate.setInt( 3, this.totalUuPv );
            prestate.setInt( 4, this.prevDayRatio );
            prestate.setInt( 5, this.rank );
            prestate.setInt( 6, this.prevRank );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUniquePv.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * �}�X�^�[�f�[�^�X�V
     * 
     * @param con �R�l�N�V����
     * @param id �z�e��ID
     * @param collectDate �W�v��
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(Connection con, int id, int collectDate)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_unique_pv SET ";
        query = query + " prev_day_ratio = ?,";
        query = query + " total_uu_pv = ?,";
        query = query + " rank = ?,";
        query = query + " prev_rank = ?";
        query = query + " WHERE id = ?";
        query = query + " AND collect_date = ?";

        try
        {
            prestate = con.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, this.prevDayRatio );
            prestate.setInt( 2, this.totalUuPv );
            prestate.setInt( 3, this.rank );
            prestate.setInt( 4, this.prevRank );
            prestate.setInt( 5, id );
            prestate.setInt( 6, collectDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelUniquePv.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

}
