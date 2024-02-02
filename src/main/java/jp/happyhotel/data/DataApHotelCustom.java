package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * �z�e���ڋq�o�^���(ap_hotel_custom)�擾�N���X
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/9/11
 */
public class DataApHotelCustom implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -3582553810189358856L;
    public static final String TABLE            = "ap_hotel_custom";
    private int                id;                                      // �n�s�z�e�z�e��ID
    private String             userId;                                  // ���[�U�[ID
    private String             securityCode;                            // �Z�L�����e�B�R�[�h�i�z�X�g�o�^�j
    private String             customId;                                // �����o�[ID�i�z�X�g�o�^�j
    private String             hotelUserId;                             // ���[�U�[ID�i�z�X�g�o�^�j
    private String             hotelPassword;                           // �p�X���[�h�i�z�X�g�o�^�j
    private int                birthday1;                               // �a����1�i�z�X�g�o�^�j
    private int                birthday2;                               // �a����2�i�z�X�g�o�^�j
    private int                memorial1;                               // �L�O��1�i�z�X�g�o�^�j
    private int                memorial2;                               // �L�O��2�i�z�X�g�o�^�j
    private String             nickname;                                // �j�b�N�l�[���i�z�X�g�o�^�p�j
    private int                registDate;                              // �o�^���t(YYYYMMDD)
    private int                registTime;                              // �o�^����(HHMMSS)
    private int                lastUpdate;                              // �ŏI�X�V���t(YYYYMMDD)
    private int                lastUptime;                              // �ŏI�X�V����(HHMMSS)
    private int                registStatus;                            // 0:�z�X�g����OK�̂��߉��o�^�A1:�����o���܂œo�^����
    private int                delFlag;                                 // 1:�폜
    private int                autoFlag;                                // 1:�������s
    private String             name;                                    // ���O�iSC�̂݁j
    private String             nameKana;                                // ���O�J�i�iSC�̂݁j
    private String             tel1;                                    // �d�b�ԍ��iSC�̂݁j
    private String             mailAddress;                             // ���[���A�h���X�iSC�̂݁j
    private int                sex;                                     // ���ʁiSC�̂݁j
    private String             address1;                                // �Z��1:�s���{���iSC�̂݁j
    private String             address2;                                // �Z��2:�s�撬���iSC�̂݁j

    /**
     * �f�[�^�����������܂��B
     */
    public DataApHotelCustom()
    {
        this.id = 0;
        this.userId = "";
        this.securityCode = "";
        this.customId = "";
        this.hotelUserId = "";
        this.hotelPassword = "";
        this.birthday1 = 0;
        this.birthday2 = 0;
        this.memorial1 = 0;
        this.memorial2 = 0;
        this.nickname = "";
        this.registDate = 0;
        this.registTime = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.registStatus = 0;
        this.delFlag = 0;
        this.autoFlag = 0;
        this.tel1 = "";
        this.name = "";
        this.nameKana = "";
        this.sex = 0;
        this.mailAddress = "";
        this.address1 = "";
        this.address2 = "";
    }

    public int getId()
    {
        return id;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public String getCustomId()
    {
        return customId;
    }

    public String getHotelUserId()
    {
        return hotelUserId;
    }

    public String getHotelPassword()
    {
        return hotelPassword;
    }

    public int getBirthday1()
    {
        return birthday1;
    }

    public int getBirthday2()
    {
        return birthday2;
    }

    public int getMemorial1()
    {
        return memorial1;
    }

    public int getMemorial2()
    {
        return memorial2;
    }

    public String getNickname()
    {
        return nickname;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getAutoFlag()
    {
        return autoFlag;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getTel1()
    {
        return tel1;
    }

    public int getSex()
    {
        return sex;
    }

    public String getMailAddress()
    {
        return mailAddress;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setHotelUserId(String hotelUserId)
    {
        this.hotelUserId = hotelUserId;
    }

    public void setHotelPassword(String hotelPassword)
    {
        this.hotelPassword = hotelPassword;
    }

    public void setBirthday1(int birthday1)
    {
        this.birthday1 = birthday1;
    }

    public void setBirthday2(int birthday2)
    {
        this.birthday2 = birthday2;
    }

    public void setMemorial1(int memorial1)
    {
        this.memorial1 = memorial1;
    }

    public void setMemorial2(int memorial2)
    {
        this.memorial2 = memorial2;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getRegistStatus()
    {
        return registStatus;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setAutoFlag(int autoFlag)
    {
        this.autoFlag = autoFlag;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setMailAddress(String mailAddress)
    {
        this.mailAddress = mailAddress;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    /****
     * �z�e���ڋq�o�^���(ap_hotel_custom)�擾
     * 
     * @param id �n�s�z�e�z�e��ID
     * @param userId ���[�U�[ID
     * @see �o�^�����f�[�^�̂�
     * @return
     */
    public boolean getData(int id, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_custom WHERE id = ? AND user_id = ? ";
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
                    this.id = result.getInt( "id" );
                    this.userId = result.getString( "user_id" );
                    this.securityCode = result.getString( "security_code" );
                    this.customId = result.getString( "custom_id" );
                    this.hotelUserId = result.getString( "hotel_user_id" );
                    this.hotelPassword = result.getString( "hotel_password" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorial1 = result.getInt( "memorial1" );
                    this.memorial2 = result.getInt( "memorial2" );
                    this.nickname = result.getString( "nickname" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.autoFlag = result.getInt( "auto_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �z�e���ڋq�o�^���(ap_hotel_custom)�擾
     * 
     * @param id �n�s�z�e�z�e��ID
     * @param userId ���[�U�[ID
     * @see �o�^�����f�[�^�̂�
     * @return
     */
    public boolean getValidData(int id, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_custom WHERE id = ? AND user_id = ? AND regist_status = 1 AND del_flag = 0";
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
                    this.id = result.getInt( "id" );
                    this.userId = result.getString( "user_id" );
                    this.securityCode = result.getString( "security_code" );
                    this.customId = result.getString( "custom_id" );
                    this.hotelUserId = result.getString( "hotel_user_id" );
                    this.hotelPassword = result.getString( "hotel_password" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorial1 = result.getInt( "memorial1" );
                    this.memorial2 = result.getInt( "memorial2" );
                    this.nickname = result.getString( "nickname" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.autoFlag = result.getInt( "auto_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �z�e���ڋq�o�^���(ap_hotel_custom)�擾
     * 
     * @param id �n�s�z�e�z�e��ID
     * @param customId �z�e���ڋqID
     * @see �o�^�����f�[�^�̂�
     * @return
     */
    public boolean getData(int id, int customId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT COUNT(custom_id) FROM ap_hotel_custom WHERE id = ? AND custom_id = ? AND  del_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, customId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.userId = result.getString( "user_id" );
                    this.securityCode = result.getString( "security_code" );
                    this.customId = result.getString( "custom_id" );
                    this.hotelUserId = result.getString( "hotel_user_id" );
                    this.hotelPassword = result.getString( "hotel_password" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorial1 = result.getInt( "memorial1" );
                    this.memorial2 = result.getInt( "memorial2" );
                    this.nickname = result.getString( "nickname" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.autoFlag = result.getInt( "auto_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �z�e���ڋq�o�^���(ap_hotel_custom)�擾
     * 
     * @param id �n�s�z�e�z�e��ID
     * @param customId �z�e���ڋqID
     * @see �o�^�����f�[�^�̂�
     * @return
     */
    public boolean getDataCustom(int id, String customId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_custom WHERE id = ? AND custom_id = ?  AND regist_status = 1 AND  del_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, customId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.userId = result.getString( "user_id" );
                    this.securityCode = result.getString( "security_code" );
                    this.customId = result.getString( "custom_id" );
                    this.hotelUserId = result.getString( "hotel_user_id" );
                    this.hotelPassword = result.getString( "hotel_password" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorial1 = result.getInt( "memorial1" );
                    this.memorial2 = result.getInt( "memorial2" );
                    this.nickname = result.getString( "nickname" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.autoFlag = result.getInt( "auto_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �z�e���ڋq�o�^���(ap_hotel_custom)�擾
     * 
     * @param id �n�s�z�e�z�e��ID
     * @param customId �z�e���ڋqID
     * @see �o�^�����f�[�^�̂�
     * @return
     */
    public boolean deleteCustom(int id, String customId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        DataApHotelCustomHistory dhch = null;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_hotel_custom SET ";
        query += " del_flag=?";
        query += " WHERE id=? AND custom_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, 1 );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.customId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                dhch = new DataApHotelCustomHistory();
                dhch.setId( this.id );
                dhch.setUserId( this.userId );
                dhch.setSecurityCode( this.securityCode );
                dhch.setCustomId( this.customId );
                dhch.setHotelUserId( this.hotelUserId );
                dhch.setHotelPassword( this.hotelPassword );
                dhch.setBirthday1( this.birthday1 );
                dhch.setBirthday2( this.birthday2 );
                dhch.setMemorial1( this.memorial1 );
                dhch.setMemorial2( this.memorial2 );
                dhch.setNickname( this.nickname );
                dhch.setRegistDate( this.registDate );
                dhch.setRegistTime( this.registTime );
                dhch.setLastUpdate( this.lastUpdate );
                dhch.setLastUptime( this.lastUptime );
                dhch.setRegistStatus( this.registStatus );
                dhch.setDelFlag( this.delFlag );
                dhch.setAutoFlag( this.autoFlag );
                dhch.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.deleteCustom] Exception=" + e.toString() );
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
     * 
     * @param id
     * @param customId
     * @return
     */
    public boolean isReusableCustomId(int id, String customId)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT COUNT(custom_id) FROM ap_hotel_custom WHERE id = ? AND custom_id = ? AND  del_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, customId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( 1 ) > 0 )
                    {
                        ret = false;
                    }
                    else
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.isReusableCustomId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public int customerCount(int id, String customId)
    {
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(custom_id) FROM ap_hotel_custom WHERE id = ? AND custom_id = ?"
                + " AND regist_status = 1 AND del_flag=0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, customId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.customerCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(count);

    }

    // �J�[�h���X��������̎擾
    public int customerCount(int id)
    {
        int count = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(custom_id) FROM ap_hotel_custom WHERE id = ?"
                + " AND regist_status = 1 AND del_flag=0";
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
                    count = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.customerCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(count);

    }

    /***
     * ���ԓ��V�K�J�[�h���X�����
     * 
     * @param id �z�e��ID
     * @param startDate �J�n���t
     * @param endDate �I�����t
     * @return �������ʁi�J�E���g���j
     */
    public int newCustomerCount(int id, int startDate, int endDate)
    {
        int count = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT";
        query += " count(ahc.user_id) AS COUNT";
        query += " FROM ap_hotel_custom ahc";
        query += " INNER JOIN hh_rsv_reserve_basic hrrb ON hrrb.id = ahc.id";
        query += " AND (ahc.regist_date * 1000000 + ahc.regist_time <= @n * 1000000 + hrrb.deadline_time)";
        query += " AND (ahc.regist_date * 1000000 + ahc.regist_time > @t * 1000000 + hrrb.deadline_time)";
        query += " WHERE ahc.id = ? ";
        query += " AND ahc.regist_status = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, DateEdit.addDay( endDate, 1 ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCostom.newCustomerCount()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /***
     * ���ԓ��ڍs�J�[�h�����
     * 
     * @param id �z�e��ID
     * @param startDate �J�n���t
     * @param endDate �I�����t
     * @return �������ʁitrue�F�����Afale�F���s�j
     */
    public int changeCustomerCount(int id, int startDate, int endDate)
    {
        int count = 0;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT";
        query += " count(ahc.user_id) AS COUNT";
        query += " FROM ap_hotel_custom ahc";
        query += " INNER JOIN hh_rsv_reserve_basic hrrb ON hrrb.id = ahc.id";
        query += " AND (ahc.regist_date * 1000000 + ahc.regist_time <= @n * 1000000 + hrrb.deadline_time)";
        query += " AND (ahc.regist_date * 1000000 + ahc.regist_time > @t * 1000000 + hrrb.deadline_time)";
        query += " WHERE ahc.id = ? ";
        query += " AND ahc.regist_status = 1";
        query += " AND ahc.auto_flag = 0";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, DateEdit.addDay( endDate, 1 ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCostom.changeCustomerCount()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    public boolean changeNewCustomId(int id, String customId, String newCustomId)
    {
        boolean ret = false;

        // �f�[�^�擾
        ret = this.getDataCustom( id, customId );
        if ( ret != false )
        {
            this.setCustomId( newCustomId );
            this.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            this.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            // �X�V
            ret = this.updateData( id, customId );
        }
        return(ret);
    }

    /**
     * �z�e���ڋq�o�^���(ap_hotel_custom)�ݒ�
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
                this.userId = result.getString( "user_id" );
                this.securityCode = result.getString( "security_code" );
                this.customId = result.getString( "custom_id" );
                this.hotelUserId = result.getString( "hotel_user_id" );
                this.hotelPassword = result.getString( "hotel_password" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.nickname = result.getString( "nickname" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.registStatus = result.getInt( "regist_status" );
                this.delFlag = result.getInt( "del_flag" );
                this.autoFlag = result.getInt( "auto_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �z�e���ڋq�o�^���(ap_hotel_custom)�}��
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
        DataApHotelCustomHistory dhch = null;
        ret = false;

        query = "INSERT ap_hotel_custom SET ";
        query += " id=?";
        query += ", user_id=?";
        query += ", security_code=?";
        query += ", custom_id=?";
        query += ", hotel_user_id=?";
        query += ", hotel_password=?";
        query += ", birthday1=?";
        query += ", birthday2=?";
        query += ", memorial1=?";
        query += ", memorial2=?";
        query += ", nickname=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", regist_status=?";
        query += ", del_flag=?";
        query += ", auto_flag=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.securityCode );
            prestate.setString( i++, this.customId );
            prestate.setString( i++, this.hotelUserId );
            prestate.setString( i++, this.hotelPassword );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.registStatus );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.autoFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                dhch = new DataApHotelCustomHistory();
                dhch.setId( this.id );
                dhch.setUserId( this.userId );
                dhch.setSecurityCode( this.securityCode );
                dhch.setCustomId( this.customId );
                dhch.setHotelUserId( this.hotelUserId );
                dhch.setHotelPassword( this.hotelPassword );
                dhch.setBirthday1( this.birthday1 );
                dhch.setBirthday2( this.birthday2 );
                dhch.setMemorial1( this.memorial1 );
                dhch.setMemorial2( this.memorial2 );
                dhch.setNickname( this.nickname );
                dhch.setRegistDate( this.registDate );
                dhch.setRegistTime( this.registTime );
                dhch.setLastUpdate( this.lastUpdate );
                dhch.setLastUptime( this.lastUptime );
                dhch.setRegistStatus( this.registStatus );
                dhch.setDelFlag( this.delFlag );
                dhch.setAutoFlag( this.autoFlag );
                dhch.insertData();

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.insertData] Exception=" + e.toString() );
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
     * �z�e���ڋq�o�^���(ap_hotel_custom)�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �n�s�z�e�z�e��ID
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean updateData(int id, String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        DataApHotelCustomHistory dhch = null;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_hotel_custom SET ";
        query += " security_code=?";
        query += ", custom_id=?";
        query += ", hotel_user_id=?";
        query += ", hotel_password=?";
        query += ", birthday1=?";
        query += ", birthday2=?";
        query += ", memorial1=?";
        query += ", memorial2=?";
        query += ", nickname=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", regist_status=?";
        query += ", del_flag=?";
        query += ", auto_flag=?";
        query += " WHERE id=? AND user_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.securityCode );
            prestate.setString( i++, this.customId );
            prestate.setString( i++, this.hotelUserId );
            prestate.setString( i++, this.hotelPassword );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.registStatus );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.autoFlag );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.userId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                dhch = new DataApHotelCustomHistory();
                dhch.setId( this.id );
                dhch.setUserId( this.userId );
                dhch.setSecurityCode( this.securityCode );
                dhch.setCustomId( this.customId );
                dhch.setHotelUserId( this.hotelUserId );
                dhch.setHotelPassword( this.hotelPassword );
                dhch.setBirthday1( this.birthday1 );
                dhch.setBirthday2( this.birthday2 );
                dhch.setMemorial1( this.memorial1 );
                dhch.setMemorial2( this.memorial2 );
                dhch.setNickname( this.nickname );
                dhch.setRegistDate( this.registDate );
                dhch.setRegistTime( this.registTime );
                dhch.setLastUpdate( this.lastUpdate );
                dhch.setLastUptime( this.lastUptime );
                dhch.setRegistStatus( this.registStatus );
                dhch.setDelFlag( this.delFlag );
                dhch.setAutoFlag( this.autoFlag );
                dhch.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.updateData] Exception=" + e.toString() );
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
     * �X�e�C�R���V�F���W���p�O���[�v�z�e���̌ڋq�����擾
     * 
     * @param id
     * @param userId
     * @return
     */
    public boolean getScCustomData(int id, String userId)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;

        try
        {
            // �P�X�܂�r_member_custom �𒲍�
            query = "SELECT member.member_id AS mail_address,custom.* FROM sc.r_member_custom custom";
            query = " INNER JOIN sc.r_user_member user ON custom.member_no = user.member_no AND user.user_id = ? AND user.del_flag = 0";
            query = " INNER JOIN sc.m_member member ON custom.member_no = member.member_no AND member.del_flag = 0";
            query = " WHERE custom.id = ? AND custom.regist_status = 1 AND custom.del_flag = 0";
            query = " LIMIT 0,1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    setScData( result, userId );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
            if ( !ret )// �O���[�v�X�܂�r_member_custom �𒲍�

            {
                query = "SELECT member.member_id AS mail_address,custom.* FROM sc.r_member_custom custom";
                query = " INNER JOIN sc.r_user_member user ON custom.member_no = user.member_no AND user.user_id = ? AND user.del_flag = 0";
                query = " INNER JOIN sc.m_member member ON custom.member_no = member.member_no AND member.del_flag = 0";
                query = " INNER JOIN hotenavi.ap_group_hotel chain ON chain.id = custom.id AND chain.multi_id = ( SELECT multi_id FROM hotenavi.ap_group_hotel WHERE id = ? AND del_flag=0 )";
                query = " WHERE custom.regist_status = 1 AND custom.del_flag = 0";
                query = " LIMIT 0,1";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );
                prestate.setInt( 2, id );
                result = prestate.executeQuery();

                if ( result != null )
                {

                    if ( result.next() != false )
                    {
                        setScData( result, userId );
                    }

                    if ( result.last() != false )
                    {
                        count = result.getRow();
                    }
                }
                if ( count > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCustom.getCustomData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * �z�e���ڋq�o�^���(ap_hotel_custom)�ݒ� //sc.r_member_custom���
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @param userId ���[�U�[ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setScData(ResultSet result, String userId)
    {
        try
        {
            if ( result != null )
            {
                this.mailAddress = result.getString( "mail_address" );
                this.id = result.getInt( "id" );
                this.userId = userId;
                this.securityCode = result.getString( "security_code" );
                this.customId = result.getString( "custom_id" );
                this.hotelUserId = result.getString( "hotel_user_id" );
                this.hotelPassword = result.getString( "hotel_password" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.nickname = result.getString( "nickname" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kara" );
                this.sex = result.getInt( "sex" );
                this.tel1 = result.getString( "tel1" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.registStatus = result.getInt( "regist_status" );
                this.delFlag = result.getInt( "del_flag" );
                this.autoFlag = result.getInt( "auto_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.setScData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �X�e�C�R���V�F���W���pm_member�̌ڋq�����擾
     * 
     * @param id
     * @param userId
     * @return
     */
    public boolean getScMemberData(String userId)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;

        try
        {
            // �P�X�܂�r_member_custom �𒲍�
            query = "SELECT member.* FROM sc.m_member member";
            query = " INNER JOIN sc.r_user_member user ON member.member_no = user.member_no AND user.user_id = ? AND user.del_flag = 0";
            query = " WHERE member.del_flag = 0";
            query = " LIMIT 0,1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    setScData( result );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelCustom.getCustomData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * �z�e���ڋq�o�^���(ap_hotel_custom)�ݒ� //sc.r_member_custom���
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setScData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.mailAddress = result.getString( "member_id" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.nickname = result.getString( "nickname" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kara" );
                this.sex = result.getInt( "sex" );
                this.tel1 = result.getString( "tel1" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelCustom.setScData] Exception=" + e.toString() );
        }
        return(true);
    }
}
