package com.hotenavi2.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * 
 * 
 * @author S.Sakurai
 * @version 1.00 2023/10/10
 */
public class MailAddressEncrypt implements Serializable
{
    private final static String CONF_PATH        = "/etc/hotenavi/mailAddressEncrypt.conf";
    private final static String KEY_KEY          = "key";
    private final static String IVBYTES_KEY      = "ivBytes";
    // �L�[
    private static byte[]       key              = null;
    // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
    private static byte[]       ivBytes          = null;

    private static final long   serialVersionUID = -3924291538252750667L;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            // �v���p�e�B�t�@�C����ǂݍ���
            prop.load( propfile );

            String confKey = prop.getProperty( KEY_KEY );
            String confIbytes = prop.getProperty( IVBYTES_KEY );
            key = confKey.getBytes();
            ivBytes = confIbytes.getBytes();
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "mailAddressEncrypt Static Block Error=" + e.toString() );
        }
    }

    /**
     * @param:���͒l
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )�ɂ��Í���������
     * @throws Exception
     */

    public static String encrypt(String inputStr) //
    {
        // �Í���

        String encrypt = "";
        try
        {
            encrypt = EncodeData.encodeString( key, ivBytes, inputStr );
        }
        catch ( Exception e )
        {
            Logging.error( "[MailAddressEncrypt.encrypt] Exception:" + e.toString() );
        }
        return(encrypt);
    }

    public static String decrypt(String encrypt) //
    {
        // ����
        String decrypt = "";
        try
        {
            decrypt = EncodeData.decodeString( key, ivBytes, encrypt );
        }
        catch ( Exception e )
        {
            Logging.error( "[MailAddressEncrypt.decrypt] Exception:" + e.toString() );
        }
        return(decrypt);
    }

    /**
     * ���[���A�h���X�̈Í������ꊇ�ōs��
     * 
     * @param cContractId �_���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public static boolean convertData(String hotel_id)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "SELECT hotel_id,address FROM hotenavi.mag_address";
        query = query + " WHERE address LIKE '%@%'";
        if ( hotel_id != null )
        {
            query = query + " AND hotel_id = ?";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( hotel_id != null )
            {
                prestate.setString( 1, hotel_id );
            }
            result = prestate.executeQuery();
            while( result.next() )
            {
                query = "UPDATE hotenavi.mag_address SET address = ?  WHERE hotel_id = ? AND address = ?";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, encrypt( result.getString( "address" ) ) );
                prestate.setString( 2, result.getString( "hotel_id" ) );
                prestate.setString( 3, result.getString( "address" ) );
                prestate.executeUpdate();
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[MailAddressEncrypt.convertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
