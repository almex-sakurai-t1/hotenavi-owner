package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * �z�e���ݒ�t�@�C��(ap_hotel_setting)�擾�N���X
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/12/10
 */
public class DataApHotelSetting implements Serializable
{
    /**
	 *
	 */
    private static final long  serialVersionUID = -249773005077432282L;
    public static final String TABLE            = "ap_hotel_setting";
    private int                id;                                     // ID
    private int                customFlag;                             // �ڋq�Ή��t���O
    private int                autoCustomId;                           // �����̔ԗp�ڋqID
    private int                terminalFlag;                           // �^�[�~�i���[���L��
    private int                autoCouponFlag;                         // �����K�p�N�[�|���L��
    private int                nonrefundableFlag;                      // �ԋ��s�t���O�@1:�ԋ��s�A2:�N���W�b�g�̂ݕԋ��s��
    private int                startDate;                              //
    private int                endDate;                                //

    /**
     * �f�[�^�����������܂��B
     */
    public DataApHotelSetting()
    {
        this.id = 0;
        this.customFlag = 0;
        this.autoCustomId = 0;
        this.terminalFlag = 0;
        this.autoCouponFlag = 0;
        this.nonrefundableFlag = 0;
        this.startDate = 0;
        this.endDate = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getCustomFlag()
    {
        return customFlag;
    }

    public int getAutoCustomId()
    {
        return autoCustomId;
    }

    public int getTerminalFlag()
    {
        return terminalFlag;
    }

    public int getAutoCouponFlag()
    {
        return autoCouponFlag;
    }

    public int getNonrefundableFlag()
    {
        return nonrefundableFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCustomFlag(int customFlag)
    {
        this.customFlag = customFlag;
    }

    public void setAutoCustomId(int autoCustomId)
    {
        this.autoCustomId = autoCustomId;
    }

    public void setTerminalFlag(int terminalFlag)
    {
        this.terminalFlag = terminalFlag;
    }

    public void setAutoCouponFlag(int autoCouponFlag)
    {
        this.autoCouponFlag = autoCouponFlag;
    }

    public void setNonrefundableFlag(int nonrefundableFlag)
    {
        this.nonrefundableFlag = nonrefundableFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void endStartDate(int endDate)
    {
        this.endDate = endDate;
    }

    /****
     * �z�e���ݒ�t�@�C��(ap_hotel_setting)�擾
     * 
     * @param id �n�s�z�e�z�e��ID
     * @return
     */
    public boolean getData(int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_setting WHERE id = ? ";
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
                    this.id = result.getInt( "id" );
                    this.customFlag = result.getInt( "custom_flag" );
                    this.autoCustomId = result.getInt( "auto_custom_id" );
                    this.terminalFlag = result.getInt( "terminal_flag" );
                    this.autoCouponFlag = result.getInt( "auto_coupon_flag" );
                    this.nonrefundableFlag = result.getInt( "nonrefundable_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �n�s�z�e�^�b�`�A�v���Ώۃz�e�����f
     * 
     * @param id �n�s�z�e�z�e��ID
     * @return
     */
    public boolean getIsTouchApi(int id)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_setting WHERE id = ? AND start_date != 0 AND start_date <= ? AND end_date >= ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.getIsTouchApi] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �n�s�z�e�^�b�`�A�v���Ώۃz�e�����f
     * 
     * @param id �n�s�z�e�z�e��ID
     * @return
     */
    public boolean getIsCardless(Connection connection, int id)
    {
        boolean ret = false;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT setting.id FROM ap_hotel_setting setting";
        query += " INNER JOIN hh_hotel_newhappie happie ON setting.id=happie.id AND happie.date_start <= ? AND happie.date_start > 0 ";
        query += " WHERE setting.id = ? AND setting.start_date > 0 AND setting.start_date <= ? AND setting.end_date >= ? AND setting.custom_flag=1";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, id );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.getIsCardless] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /****
     * �n�s�z�e�^�b�`�A�v���Ώۃz�e�����f
     * 
     * @param id �n�s�z�e�z�e��ID
     * @return
     */
    public boolean getIsCardless(int id)
    {
        boolean ret = false;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = getIsCardless( connection, id );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.getIsCardless] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �z�e���ݒ�t�@�C��(ap_hotel_setting)�ݒ�
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.customFlag = result.getInt( "custom_flag" );
                this.autoCustomId = result.getInt( "auto_custom_id" );
                this.terminalFlag = result.getInt( "terminal_flag" );
                this.autoCouponFlag = result.getInt( "auto_coupon_flag" );
                this.nonrefundableFlag = result.getInt( "nonrefundable_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �z�e���ݒ�t�@�C��(ap_hotel_setting)�}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
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

        query = "INSERT ap_hotel_setting SET ";
        query += " id=?";
        query += ", custom_flag=?";
        query += ", auto_custom_id=?";
        query += ", terminal_flag=?";
        query += ", auto_coupon_flag=?";
        query += ", nonrefundable_flag=?";
        query += ", start_date=?";
        query += ", end_date=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.customFlag );
            prestate.setInt( i++, this.autoCustomId );
            prestate.setInt( i++, this.terminalFlag );
            prestate.setInt( i++, this.autoCouponFlag );
            prestate.setInt( i++, this.nonrefundableFlag );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.insertData] Exception=" + e.toString() );
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
     * �z�e���ݒ�t�@�C��(ap_hotel_setting)�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �n�s�z�e�z�e��ID
     * @return
     */
    public boolean updateData(int id)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_hotel_setting SET ";
        query += " custom_flag=?";
        query += ", auto_custom_id=?";
        query += ", terminal_flag=?";
        query += ", auto_coupon_flag=?";
        query += ", nonrefundable_flag=?";
        query += ", start_date=?";
        query += ", end_date=?";
        query += " WHERE id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.customFlag );
            prestate.setInt( i++, this.autoCustomId );
            prestate.setInt( i++, this.terminalFlag );
            prestate.setInt( i++, this.autoCouponFlag );
            prestate.setInt( i++, this.nonrefundableFlag );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.updateData] Exception=" + e.toString() );
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
     * �ڋq�ԍ��̔�
     * 
     * @param id �n�s�z�e�z�e��ID
     * @return
     */

    public int getMaxCustomId(int id)
    {
        int i = 1;
        int customId = 0;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ret = false;

        query = "SELECT auto_custom_id FROM ap_hotel_setting  ";
        query += " WHERE id=?";
        try
        {
            connection = DBConnection.getConnection();
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate = connection.prepareStatement( query );
            prestate.setInt( i, id );
            result = prestate.executeQuery();
            // �X�V�Ώۂ̒l���Z�b�g����
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    customId = result.getInt( "auto_custom_id" );
                    customId++;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelSetting.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(customId);
    }
}
