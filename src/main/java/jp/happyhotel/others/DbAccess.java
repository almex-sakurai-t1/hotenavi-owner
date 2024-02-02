package jp.happyhotel.others;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

public class DbAccess
{
    private String                     driver;
    private String                     url;
    private String                     user;
    private String                     password;
    private String                     jdbcds;
    private Properties                 prop;
    private int                        result;
    private ResultSet                  resultset;
    private DataSource                 ds;
    private Connection                 connect;
    private java.sql.PreparedStatement state;

    /**
     * �f�[�^�x�[�X�ւ̃A�N�Z�X���ݒ�
     *
     */
    public DbAccess(boolean demoflag)
    {

        try
        {
            String dbconnectPath = "/etc/happyhotel/dbconnect.conf";
            if ( demoflag )
            {
                dbconnectPath = "/etc/happyhotel/demo_dbconnect.conf";
            }
            File file = new File( dbconnectPath );
            FileInputStream propfile = new FileInputStream( file );
            prop = new Properties();
            // �����ȩ̀�ق��緰�ƒl��ؽĂ�ǂݍ��݂܂�
            prop.load( propfile );
            // "jdbc.driver"�ɐݒ肳��Ă���l���擾���܂�
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"�ɐݒ肳��Ă���l���擾���܂�
            url = prop.getProperty( "jdbc.url" );
            // "jdbc.user"�ɐݒ肳��Ă���l���擾���܂�
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"�ɐݒ肳��Ă���l���擾���܂�
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"�ɐݒ肳��Ă���l���擾���܂�
            jdbcds = prop.getProperty( "jdbc.datasource" );

            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }
    }

    /**
     * �f�[�^�x�[�X�ւ̃A�N�Z�X���ݒ�
     *
     * @param driver_param JDBC�h���C�o��
     * @param url_param �ڑ���
     * @param user_param ���[�U��
     * @param password_param �p�X���[�h
     */
    public DbAccess(String driver_param, String url_param, String user_param, String password_param)
    {
        driver = driver_param;
        url = url_param;
        user = user_param;
        password = password_param;
    }

    /**
     * �f�[�^�x�[�X�ւ̃A�N�Z�X���ݒ�
     *
     * @param filename �ݒ�t�@�C����
     */
    public DbAccess(String filename)
    {
        try
        {
            FileInputStream propfile = new FileInputStream( filename );

            prop = new Properties();
            // �����ȩ̀�ق��緰�ƒl��ؽĂ�ǂݍ��݂܂�
            prop.load( propfile );
            // "jdbc.driver"�ɐݒ肳��Ă���l���擾���܂�
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"�ɐݒ肳��Ă���l���擾���܂�
            url = prop.getProperty( "jdbc.url" );
            // "jdbc.user"�ɐݒ肳��Ă���l���擾���܂�
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"�ɐݒ肳��Ă���l���擾���܂�
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"�ɐݒ肳��Ă���l���擾���܂�
            jdbcds = prop.getProperty( "jdbc.datasource" );

            propfile.close();
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * SQL���Z�b�g����(SELECT/INSERT/UPDATE)
     *
     * @param query SQL��
     * @return ��������(True���Z�b�g����,False���Z�b�g���s)
     */
    public boolean setQuery(String query)
    {
        boolean ret = false;

        try
        {
        	Class.forName( driver );
            // DB�֐ڑ�����
            connect = DriverManager.getConnection( url, user, password );

            // �X�e�[�g�����g�̍쐬
            state = connect.prepareStatement( query );

            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * �p�����[�^�Z�b�g����
     *
     * @param no �p�����[�^�Z�b�g�ԍ�
     * @param value �Z�b�g�l(String�`��)
     */
    public void setParameter(int no, String value)
    {
        try
        {
            state.setString( no, value );
        }
        catch ( Exception e )
        {

        }

        return;
    }

    /**
     * �p�����[�^�Z�b�g����
     *
     * @param no �p�����[�^�Z�b�g�ԍ�
     * @param value �Z�b�g�l(Int�`��)
     */
    public void setParameter(int no, int value)
    {
        try
        {
            state.setInt( no, value );
        }
        catch ( Exception e )
        {

        }
        return;
    }

    /**
     * SQL�����s����(SELECT)
     *
     * @return ��������(���s:null)
     */
    public ResultSet execQuery()
    {
        try
        {
            // SQL�̎��s
            resultset = state.executeQuery();
        }
        catch ( Exception e )
        {
            resultset = null;
        }

        return(resultset);
    }

    /**
     * SQL�����s����(INSERT,UPDATE,DELETE)
     *
     * @return ��������(���s:-1)
     */
    public int execUpdate()
    {
        try
        {
            // SQL�̎��s
            result = state.executeUpdate();
        }
        catch ( Exception e )
        {
            result = -1;
        }

        return(result);
    }

    /**
     * �f�[�^�x�[�X�I������
     *
     */
    public void close()
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
            }
            if ( state != null )
            {
                state.close();
            }
            if ( connect != null )
            {
                connect.close();
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
            try
            {
                if ( resultset != null )
                {
                    resultset.close();
                }
                if ( state != null )
                {
                    state.close();
                }
                if ( connect != null )
                {
                    connect.close();
                }
            }
            catch ( Exception e )
            {
            }
        }
    }
}
