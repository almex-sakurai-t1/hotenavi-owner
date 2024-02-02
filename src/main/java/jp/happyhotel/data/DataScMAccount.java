/*
 * @(#)DataMAccount.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 �A�J�E���g�Ǘ����f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �A�J�E���g�Ǘ����f�[�^�擾�N���X
 * 
 * @author Mitsuhashi-k1
 * @version 1.00 2019/06/25
 */
public class DataScMAccount implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    /**
     * �_���ID�B
     */
    private int               accountId;

    /**
     * �v���W�F�N�gID(��project.project_id)�B
     */
    private int               projectId;

    /**
     * �A�J�E���g���B
     */
    private String            name;

    /**
     * �A�J�E���g��(�p��)�B
     */
    private String            nameEn;

    /**
     * ����o�^�����M�����[���A�h���X�B
     */
    private String            fromMailaddress;

    /**
     * �o�^���t�iYYYYMMDD)�B
     */
    private int               registDate;

    /**
     * �o�^�����iHHMMSS)�B
     */
    private int               registTime;

    /**
     * �ύX���t�iYYYYMMDD)�B
     */
    private int               updateDate;

    /**
     * �ύX�����iHHMMSS)�B
     */
    private int               updateTime;

    /**
     * 1:�폜�B
     */
    private int               delFlag;

    /**
     * �J�X�^��URL�X�L�[���B
     */
    private String            customUrlScheme;

    /**
     * �f�[�^�����������܂��B
     */
    public DataScMAccount()
    {
        accountId = 0;
        projectId = 0;
        name = "";
        nameEn = "";
        fromMailaddress = "";
        registDate = 0;
        registTime = 0;
        updateDate = 0;
        updateTime = 0;
        delFlag = 0;
        customUrlScheme = "";

    }

    public int getAccountId()
    {
        return accountId;
    }

    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public void setMemberId(int projectId)
    {
        this.projectId = projectId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getFromMailaddress()
    {
        return fromMailaddress;
    }

    public void setFromMailaddress(String fromMailaddress)
    {
        this.fromMailaddress = fromMailaddress;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public int getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getCustomUrlScheme()
    {
        return customUrlScheme;
    }

    public void setCustomUrlScheme(String customUrlScheme)
    {
        this.customUrlScheme = customUrlScheme;
    }

    /**
     * �A�J�E���g�Ǘ����f�[�^�擾
     * 
     * @param memberNo �����o�[�ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int accountId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM sc.m_account WHERE account_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accountId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.accountId = result.getInt( "account_id" );
                    this.projectId = result.getInt( "project_id" );
                    this.name = result.getString( "name" );
                    this.nameEn = result.getString( "name_en" );
                    this.fromMailaddress = result.getString( "from_mailaddress" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.customUrlScheme = result.getString( "custom_url_scheme" );

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMAccount.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �A�J�E���g�Ǘ����f�[�^�ݒ�
     * 
     * @param result �A�J�E���g�Ǘ����f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.accountId = result.getInt( "account_id" );
                this.projectId = result.getInt( "project_id" );
                this.name = result.getString( "name" );
                this.nameEn = result.getString( "name_en" );
                this.fromMailaddress = result.getString( "from_mailaddress" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.customUrlScheme = result.getString( "custom_url_scheme" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMAccount.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �A�J�E���g�Ǘ����f�[�^�ǉ�
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

        query = "INSERT sc.m_account SET ";
        query = query + "account_id       =   ?,";
        query = query + "project_id       =   ?,";
        query = query + "name             =   ?,";
        query = query + "name_en           =   ?,";
        query = query + "from_mailaddress =   ?,";
        query = query + "regist_date     =   ?,";
        query = query + "regist_time     =   ?,";
        query = query + "update_date     =   ?,";
        query = query + "update_time     =   ?,";
        query = query + "del_flag        =   ?,";
        query = query + "custom_url_scheme =   ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            int i = 1;
            prestate.setInt( i++, this.accountId );
            prestate.setInt( i++, this.projectId );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameEn );
            prestate.setString( i++, this.fromMailaddress );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.customUrlScheme );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMAccount.insertData] Exception=" + e.toString() );
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
     * �A�J�E���g�Ǘ����f�[�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param accountId �A�J�E���gID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(int accountId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE sc.m_account SET ";
        query = query + "project_id       =   ?,";
        query = query + "name             =   ?,";
        query = query + "name_en        =   ?,";
        query = query + "from_mailaddress          =   ?,";
        query = query + "regist_date     =   ?,";
        query = query + "regist_time     =   ?,";
        query = query + "update_date     =   ?,";
        query = query + "update_time     =   ?,";
        query = query + "del_flag        =   ?,";
        query = query + "custom_url_scheme  =   ?";
        query = query + " WHERE account_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            int i = 1;

            prestate.setInt( i++, this.projectId );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameEn );
            prestate.setString( i++, this.fromMailaddress );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.delFlag );
            prestate.setString( i++, this.customUrlScheme );
            prestate.setInt( i++, this.accountId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMAccount.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /****
     * �A�J�E���g�Ǘ��f�[�^���݃`�F�b�N
     * (�폜�t���O�͍l������)
     * 
     * @param accountId �_���ID
     * @return ���݂���Ƃ��Ftrue ���݂��Ȃ��Ƃ�:false
     */
    public boolean existsData(int accountId)
    {

        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT count(*) as cnt FROM sc.m_account WHERE account_id = ? AND del_flag = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accountId );
            prestate.setInt( 2, 0 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "cnt" ) > 0 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMAcount.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �_���ID�擾
     * 
     * @param apiKey
     * @return�@���݂���Ƃ��Ftrue ���݂��Ȃ��Ƃ�:false
     */
    public boolean getAccountID(String apiKey)
    {

        boolean ret = false;

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            StringBuilder query = new StringBuilder();
            query.append( "SELECT sc_ma.account_id" );
            query.append( " FROM sc.m_account sc_ma" );
            query.append( " INNER JOIN happyhotel_developers.project hd_pj" );
            query.append( " ON sc_ma.project_id = hd_pj.project_id" );
            query.append( " WHERE hd_pj.secret_key = ?" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            prestate.setString( 1, apiKey );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                this.accountId = result.getInt( "account_id" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMAcount.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;

    }
}
