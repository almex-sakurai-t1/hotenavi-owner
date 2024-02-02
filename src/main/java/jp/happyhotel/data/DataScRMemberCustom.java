package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �����o�[�ʌڋq�o�^���(r_member_custom)�擾�N���X
 * 
 * @author K.Mitsuhashi
 * @version 1.00 2019/11/22
 */
public class DataScRMemberCustom implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -3582553810189358856L;
    public static final String TABLE            = "sc.r_member_custom";
    private int                memberNo;                                // �����o�[NO
    private int                id;                                      // �n�s�z�e�z�e��ID
    private String             securityCode;                            // �Z�L�����e�B�R�[�h�i�z�X�g�o�^�j
    private String             customId;                                // �����o�[ID�i�z�X�g�o�^�j
    private String             hotelUserId;                             // ���[�U�[ID�i�z�X�g�o�^�j
    private String             hotelPassword;                           // �p�X���[�h�i�z�X�g�o�^�j
    private int                birthday1;                               // �a����1�i�z�X�g�o�^�j
    private int                birthday2;                               // �a����2�i�z�X�g�o�^�j
    private int                memorial1;                               // �L�O��1�i�z�X�g�o�^�j
    private int                memorial2;                               // �L�O��2�i�z�X�g�o�^�j
    private String             nickname;                                // �j�b�N�l�[���i�z�X�g�o�^�p�j
    private int                sex;                                     // ����
    private String             name;                                    // ���O
    private String             nameKana;                                // ���O�i�J�i�j
    private String             address1;                                // �Z��1
    private String             address2;                                // �Z��2
    private String             tel1;                                    // �d�b1
    private int                registDate;                              // �o�^���t(YYYYMMDD)
    private int                registTime;                              // �o�^����(HHMMSS)
    private int                lastUpdate;                              // �ŏI�X�V���t(YYYYMMDD)
    private int                lastUptime;                              // �ŏI�X�V����(HHMMSS)
    private int                registStatus;                            // 0:�z�X�g����OK�̂��߉��o�^�A1:�����o���܂œo�^����
    private int                delFlag;                                 // 1:�폜
    private int                autoFlag;                                // 1:�������s

    /**
     * �f�[�^�����������܂��B
     */
    public DataScRMemberCustom()
    {
        this.memberNo = 0;
        this.id = 0;
        this.securityCode = "";
        this.customId = "";
        this.hotelUserId = "";
        this.hotelPassword = "";
        this.birthday1 = 0;
        this.birthday2 = 0;
        this.memorial1 = 0;
        this.memorial2 = 0;
        this.nickname = "";
        this.sex = 0;
        this.name = "";
        this.nameKana = "";
        this.address1 = "";
        this.address2 = "";
        this.tel1 = "";
        this.registDate = 0;
        this.registTime = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.registStatus = 0;
        this.delFlag = 0;
        this.autoFlag = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getMemberNo()
    {
        return memberNo;
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

    public int getSex()
    {
        return sex;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public String getTel1()
    {
        return tel1;
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

    public int getRegistStatus()
    {
        return registStatus;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getAutoFlag()
    {
        return autoFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMemberNo(int memberNo)
    {
        this.memberNo = memberNo;
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

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
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

    public int setRegistStatus()
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

    /****
     * �����o�[�ʌڋq�o�^���(r_member_custom)�擾
     * 
     * @param memberNo �����o�[NO
     * @param id �n�s�z�e�z�e��ID
     * @see �o�^�����f�[�^�̂�
     * @return
     */
    public boolean getData(int memberNo, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM sc.r_member_custom WHERE member_no = ? AND id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, memberNo );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.memberNo = result.getInt( "member_no" );
                    this.id = result.getInt( "id" );
                    this.securityCode = result.getString( "security_code" );
                    this.customId = result.getString( "custom_id" );
                    this.hotelUserId = result.getString( "hotel_user_id" );
                    this.hotelPassword = result.getString( "hotel_password" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorial1 = result.getInt( "memorial1" );
                    this.memorial2 = result.getInt( "memorial2" );
                    this.nickname = result.getString( "nickname" );
                    this.sex = result.getInt( "sex" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.tel1 = result.getString( "tel1" );
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
            Logging.error( "[DataScRMemberCustom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �����o�[�ʌڋq�o�^���(sc.r_member_custom)�ݒ�
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
                this.memberNo = result.getInt( "member_no" );
                this.id = result.getInt( "id" );
                this.securityCode = result.getString( "security_code" );
                this.customId = result.getString( "custom_id" );
                this.hotelUserId = result.getString( "hotel_user_id" );
                this.hotelPassword = result.getString( "hotel_password" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.nickname = result.getString( "nickname" );
                this.sex = result.getInt( "sex" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
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
            Logging.error( "[DataScRMemberCustom.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �����o�[�ʌڋq�o�^���(sc.r_member_custom)�}��
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

        query = "INSERT sc.r_member_custom SET ";
        query += " member_no=?";
        query += ", id=?";
        query += ", security_code=?";
        query += ", custom_id=?";
        query += ", hotel_user_id=?";
        query += ", hotel_password=?";
        query += ", birthday1=?";
        query += ", birthday2=?";
        query += ", memorial1=?";
        query += ", memorial2=?";
        query += ", nickname=?";
        query += ", sex=?";
        query += ", name=?";
        query += ", name_kana=?";
        query += ", address1=?";
        query += ", address2=?";
        query += ", tel1=?";
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
            prestate.setInt( i++, this.memberNo );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.securityCode );
            prestate.setString( i++, this.customId );
            prestate.setString( i++, this.hotelUserId );
            prestate.setString( i++, this.hotelPassword );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.sex );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.tel1 );
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
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRMemberCustom.insertData] Exception=" + e.toString() );
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
     * �����o�[�ʌڋq�o�^���(sc.r_member_custom)�X�V
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
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE sc.r_member_custom SET ";
        query += " security_code=?";
        query += ", custom_id=?";
        query += ", hotel_user_id=?";
        query += ", hotel_password=?";
        query += ", birthday1=?";
        query += ", birthday2=?";
        query += ", memorial1=?";
        query += ", memorial2=?";
        query += ", nickname=?";
        query += ", sex=?";
        query += ", name=?";
        query += ", name_kana=?";
        query += ", address1=?";
        query += ", address2=?";
        query += ", tel1=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", regist_status=?";
        query += ", del_flag=?";
        query += ", auto_flag=?";
        query += " WHERE member_no=? AND id=?";

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
            prestate.setInt( i++, this.sex );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.registStatus );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.autoFlag );
            prestate.setInt( i++, this.memberNo );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRMemberCustom.updateData] Exception=" + e.toString() );
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
