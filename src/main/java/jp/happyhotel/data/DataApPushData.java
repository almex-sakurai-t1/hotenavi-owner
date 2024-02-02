package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �v�b�V���z�M�f�[�^(ap_push_data)�擾�N���X
 *
 * @author Takeshi.Sakurai
 * @version 1.00 2014/9/11
 */
public class DataApPushData implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 1393028357603346918L;
    public static final String TABLE            = "ap_push_data";
    private int                pushSeq;                                // �v�b�V���z�M�A��
    private int                campaignId;                             // �L�����y�[��ID
    private int                status;                                 // 0:���o�^�C1:�z�M��], 2:�z�M���F, 3:�z�M 10:�z�M�p��
    private int                delFlag;                                // 1:�폜
    private int                desiredDate;                            // PUSH��]��(YYYYMMDD)
    private int                desiredTime;                            // PUSH��]����(HHMMSS)
    private int                desiredCount;                           // PUSH��]����
    private int                pushDate;                               // PUSH�z�M��(YYYYMMDD)
    private int                pushTime;                               // PUSH�z�M����(HHMMSS)
    private int                pushCount;                              // PUSH����
    private int                conditionSex;                           //���ʏ���(9:�Ȃ�,1:�j,2:���j
    private int                conditionAgeFrom;                       //�N��ȏ�����i0:�Ȃ��j
    private int                conditionAgeTo;                         //�N��ȉ������i99:�Ȃ��j
    private int                conditionBirthday;                      //YYYYMMDD�i���ꂼ���0�͏����Ȃ��j
    private String             conditionArea;                          //�Z�܂������i,��؂�j
    private String             conditionMyarea;                        //�}�C�G���A�����i,��؂�j
    private String             conditionMyhotel;                       //�}�C�z�e�������i,��؂�j
    private String             conditionUserId;                        //���[�U�[ID�w��i,��؂�j
    private String             ownerHotelId;                           // �X�V�_��z�e��ID
    private int                ownerUserId;                            // �X�V�I�[�i�[���[�U�[ID
    private int                registDate;                             // �o�^���t(YYYYMMDD)
    private int                registTime;                             // �o�^����(HHMMSS)
    private int                companyId;                              // ���F���ID:2
    private String             staffId;                                // ���F�Ј�ID
    private int                approvalDate;                           // ���F���t(YYYYMMDD)
    private int                approvalTime;                           // ���F����(HHMMSS)
    private int                apliKind;                              // 1:�n�s�z�e�A�v��,10:�\��A�v��

    /**
     * �f�[�^�����������܂��B
     */
    public DataApPushData()
    {
        this.pushSeq = 0;
        this.campaignId = 0;
        this.status = 0;
        this.delFlag = 0;
        this.desiredDate = 0;
        this.desiredTime = 0;
        this.desiredCount = 0;
        this.pushDate = 0;
        this.pushTime = 0;
        this.pushCount = 0;
        this.conditionSex = 0;
        this.conditionAgeFrom = 0;
        this.conditionAgeTo = 0;
        this.conditionBirthday = 0;
        this.conditionArea = "";
        this.conditionMyarea = "";
        this.conditionMyhotel = "";
        this.conditionUserId = "";
        this.ownerHotelId = "";
        this.ownerUserId = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.companyId = 0;
        this.staffId = "";
        this.approvalDate = 0;
        this.approvalTime = 0;
        this.apliKind = 0;
    }

    public int getPushSeq()
    {
        return pushSeq;
    }

    public int getCampaignId()
    {
        return campaignId;
    }

    public int getStatus()
    {
        return status;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getDesiredDate()
    {
        return desiredDate;
    }

    public int getDesiredTime()
    {
        return desiredTime;
    }

    public int getDesiredCount()
    {
        return desiredCount;
    }

    public int getPushDate()
    {
        return pushDate;
    }

    public int getPushTime()
    {
        return pushTime;
    }

    public int getPushCount()
    {
        return pushCount;
    }

    public int getConditionSex()
    {
        return conditionSex;
    }

    public int geConditionAgeFrom()
    {
        return conditionAgeFrom;
    }

    public int getConditionAgeTo()
    {
        return conditionAgeTo;
    }

    public int getConditionBirthday()
    {
        return conditionBirthday;
    }

    public String geConditionArea()
    {
        return conditionArea;
    }

    public String getConditionMyarea()
    {
        return conditionMyarea;
    }

    public String getConditionMyhotel()
    {
        return conditionMyhotel;
    }

    public String getConditionUserId()
    {
        return conditionUserId;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public String getStaffId()
    {
        return staffId;
    }

    public int getApprovalDate()
    {
        return approvalDate;
    }

    public int getApprovalTime()
    {
        return approvalTime;
    }

    public int getApliKind()
    {
        return apliKind;
    }

    public void setPushSeq(int pushSeq)
    {
        this.pushSeq = pushSeq;
    }

    public void setCampaignId(int campaignId)
    {
        this.campaignId = campaignId;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setDesiredDate(int desiredDate)
    {
        this.desiredDate = desiredDate;
    }

    public void setDesiredTime(int desiredTime)
    {
        this.desiredTime = desiredTime;
    }

    public void setDesiredCount(int desiredCount)
    {
        this.desiredCount = desiredCount;
    }

    public void setPushDate(int pushDate)
    {
        this.pushDate = pushDate;
    }

    public void setPushTime(int pushTime)
    {
        this.pushTime = pushTime;
    }

    public void setPushCount(int pushCount)
    {
        this.pushCount = pushCount;
    }

    public void setConditionSex(int conditionSex)
    {
        this.conditionSex = conditionSex;
    }

    public void setConditionAgeFrom(int conditionAgeFrom)
    {
        this.conditionAgeFrom = conditionAgeFrom;
    }

    public void setConditionAgeTo(int conditionAgeTo)
    {
        this.conditionAgeTo = conditionAgeTo;
    }

    public void setConditionBirthday(int conditionBirthday)
    {
        this.conditionBirthday = conditionBirthday;
    }

    public void setConditionArea(String conditionArea)
    {
        this.conditionArea = conditionArea;
    }

    public void setConditionMyareat(String conditionMyarea)
    {
        this.conditionMyarea = conditionMyarea;
    }

    public void setConditionMyhotel(String conditionMyhotel)
    {
        this.conditionMyhotel = conditionMyhotel;
    }

    public void setConditionUserId(String conditionUserId)
    {
        this.conditionUserId = conditionUserId;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public void setStaffId(String staffId)
    {
        this.staffId = staffId;
    }

    public void setApprovalDate(int approvalDate)
    {
        this.approvalDate = approvalDate;
    }

    public void setApprovalTime(int approvalTime)
    {
        this.approvalTime = approvalTime;
    }

    public void setApliKind(int apliKind)
    {
        this.apliKind = apliKind;
    }

    /****
     * �v�b�V���z�M�f�[�^(ap_push_data)�擾
     *
     * @param pushSeq �v�b�V���z�M�A��
     * @return
     */
    public boolean getData(int pushSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_data WHERE push_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, pushSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.pushSeq = result.getInt( "push_seq" );
                    this.campaignId = result.getInt( "campaign_id" );
                    this.status = result.getInt( "status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.desiredDate = result.getInt( "desired_date" );
                    this.desiredTime = result.getInt( "desired_time" );
                    this.desiredCount = result.getInt( "desired_count" );
                    this.pushDate = result.getInt( "push_date" );
                    this.pushTime = result.getInt( "push_time" );
                    this.pushCount = result.getInt( "push_count" );
                    this.conditionSex  = result.getInt( "condition_sex" );
                    this.conditionAgeFrom  = result.getInt( "condition_age_from" );
                    this.conditionAgeTo  = result.getInt( "condition_age_to" );
                    this.conditionBirthday  = result.getInt( "condition_birthday" );
                    this.conditionArea = result.getString( "condition_area" );
                    this.conditionMyarea = result.getString( "condition_myarea" );
                    this.conditionMyhotel = result.getString( "condition_myhotel" );
                    this.conditionUserId = result.getString( "condition_user_id" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.companyId = result.getInt( "company_id" );
                    this.staffId = result.getString( "staff_id" );
                    this.approvalDate = result.getInt( "approval_date" );
                    this.approvalTime = result.getInt( "approval_time" );
                    this.apliKind = result.getInt( "apli_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.getData] Exception=" + e.toString() ,"DataApPushData.getData");
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �v�b�V���z�M�f�[�^(ap_push_data)�擾
     *
     * @param connection �R�l�N�V����
     * @param pushSeq �v�b�V���z�M�A��
     * @return
     */
    public boolean getData(Connection connection, int pushSeq )
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_data WHERE push_seq = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, pushSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.pushSeq = result.getInt( "push_seq" );
                    this.campaignId = result.getInt( "campaign_id" );
                    this.status = result.getInt( "status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.desiredDate = result.getInt( "desired_date" );
                    this.desiredTime = result.getInt( "desired_time" );
                    this.desiredCount = result.getInt( "desired_count" );
                    this.pushDate = result.getInt( "push_date" );
                    this.pushTime = result.getInt( "push_time" );
                    this.pushCount = result.getInt( "push_count" );
                    this.conditionSex  = result.getInt( "condition_sex" );
                    this.conditionAgeFrom  = result.getInt( "condition_age_from" );
                    this.conditionAgeTo  = result.getInt( "condition_age_to" );
                    this.conditionBirthday  = result.getInt( "condition_birthday" );
                    this.conditionArea = result.getString( "condition_area" );
                    this.conditionMyarea = result.getString( "condition_myarea" );
                    this.conditionMyhotel = result.getString( "condition_myhotel" );
                    this.conditionUserId = result.getString( "condition_user_id" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.companyId = result.getInt( "company_id" );
                    this.staffId = result.getString( "staff_id" );
                    this.approvalDate = result.getInt( "approval_date" );
                    this.approvalTime = result.getInt( "approval_time" );
                    this.apliKind = result.getInt( "apli_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.getData] Exception=" + e.toString() ,"DataApPushData.getData");
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �v�b�V���z�M�f�[�^(ap_push_data)�ݒ�
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
                this.pushSeq = result.getInt( "push_seq" );
                this.campaignId = result.getInt( "campaign_id" );
                this.status = result.getInt( "status" );
                this.delFlag = result.getInt( "del_flag" );
                this.desiredDate = result.getInt( "desired_date" );
                this.desiredTime = result.getInt( "desired_time" );
                this.desiredCount = result.getInt( "desired_count" );
                this.pushDate = result.getInt( "push_date" );
                this.pushTime = result.getInt( "push_time" );
                this.pushCount = result.getInt( "push_count" );
                this.conditionSex  = result.getInt( "condition_sex" );
                this.conditionAgeFrom  = result.getInt( "condition_age_from" );
                this.conditionAgeTo  = result.getInt( "condition_age_to" );
                this.conditionBirthday  = result.getInt( "condition_birthday" );
                this.conditionArea = result.getString( "condition_area" );
                this.conditionMyarea = result.getString( "condition_myarea" );
                this.conditionMyhotel = result.getString( "condition_myhotel" );
                this.conditionUserId = result.getString( "condition_user_id" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.companyId = result.getInt( "company_id" );
                this.staffId = result.getString( "staff_id" );
                this.approvalDate = result.getInt( "approval_date" );
                this.approvalTime = result.getInt( "approval_time" );
                this.apliKind = result.getInt( "apli_kind" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �v�b�V���z�M�f�[�^(ap_push_data)�}��
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

        query = "INSERT ap_push_data SET ";
        query += " push_seq=?";
        query += ", campaign_id=?";
        query += ", status=?";
        query += ", del_flag=?";
        query += ", desired_date=?";
        query += ", desired_time=?";
        query += ", push_date=?";
        query += ", push_time=?";

        query += ", push_count=?";
        query += ", condition_sex=?";
        query += ", condition_age_from=?";
        query += ", condition_age_to=?";
        query += ", condition_birthday=?";
        query += ", condition_area=?";
        query += ", condition_myarea=?";
        query += ", condition_myhotel=?";
        query += ", condition_user_id=?";


        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += ", apli_kind=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.pushSeq );
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.desiredDate );
            prestate.setInt( i++, this.desiredTime );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );

            prestate.setInt( i++, this.conditionSex );
            prestate.setInt( i++, this.conditionAgeFrom );
            prestate.setInt( i++, this.conditionAgeTo );
            prestate.setInt( i++, this.conditionBirthday );
            prestate.setString( i++, this.conditionArea );
            prestate.setString( i++, this.conditionMyarea );
            prestate.setString( i++, this.conditionMyhotel );
            prestate.setString( i++, this.conditionUserId );

            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, this.apliKind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.insertData] Exception=" + e.toString() );
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
     * �v�b�V���z�M�f�[�^(ap_push_data)�X�V
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param pushSeq �v�b�V���z�M�A��
     * @return
     */
    public boolean updateData(int pushSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_data SET ";
        query += " campaign_id=?";
        query += ", status=?";
        query += ", del_flag=?";
        query += ", desired_date=?";
        query += ", desired_time=?";
        query += ", push_date=?";
        query += ", push_time=?";
        query += ", push_count=?";
        query += ", condition_sex=?";
        query += ", condition_age_from=?";
        query += ", condition_age_to=?";
        query += ", condition_birthday=?";
        query += ", condition_area=?";
        query += ", condition_myarea=?";
        query += ", condition_myhotel=?";
        query += ", condition_user_id=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += ", apli_kind=?";
        query += " WHERE push_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.desiredDate );
            prestate.setInt( i++, this.desiredTime );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.pushCount );
            prestate.setInt( i++, this.conditionSex );
            prestate.setInt( i++, this.conditionAgeFrom );
            prestate.setInt( i++, this.conditionAgeTo );
            prestate.setInt( i++, this.conditionBirthday );
            prestate.setString( i++, this.conditionArea );
            prestate.setString( i++, this.conditionMyarea );
            prestate.setString( i++, this.conditionMyhotel );
            prestate.setString( i++, this.conditionUserId );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, this.apliKind );
            prestate.setInt( i++, this.pushSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.updateData] Exception=" + e.toString(),"DataApPushData" );
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
     * �v�b�V���z�M�f�[�^(ap_push_data)�X�V
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param connection �R�l�N�V����
     * @param pushSeq �v�b�V���z�M�A��
     * @return
     */
    public boolean updateData(Connection connection, int pushSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_data SET ";
        query += " campaign_id=?";
        query += ", status=?";
        query += ", del_flag=?";
        query += ", desired_date=?";
        query += ", desired_time=?";
        query += ", push_date=?";
        query += ", push_time=?";
        query += ", push_count=?";
        query += ", condition_sex=?";
        query += ", condition_age_from=?";
        query += ", condition_age_to=?";
        query += ", condition_birthday=?";
        query += ", condition_area=?";
        query += ", condition_myarea=?";
        query += ", condition_myhotel=?";
        query += ", condition_user_id=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += ", approval_date=?";
        query += ", approval_time=?";
        query += ", apli_kind=?";
        query += " WHERE push_seq=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.desiredDate );
            prestate.setInt( i++, this.desiredTime );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.pushCount );
            prestate.setInt( i++, this.conditionSex );
            prestate.setInt( i++, this.conditionAgeFrom );
            prestate.setInt( i++, this.conditionAgeTo );
            prestate.setInt( i++, this.conditionBirthday );
            prestate.setString( i++, this.conditionArea );
            prestate.setString( i++, this.conditionMyarea );
            prestate.setString( i++, this.conditionMyhotel );
            prestate.setString( i++, this.conditionUserId );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.approvalDate );
            prestate.setInt( i++, this.approvalTime );
            prestate.setInt( i++, this.apliKind );
            prestate.setInt( i++, this.pushSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushData.updateData] Exception=" + e.toString(),"DataApPushData" );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

}
