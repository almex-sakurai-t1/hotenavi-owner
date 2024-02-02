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
    // キー
    private static byte[]       key              = null;
    // 暗号ベクター（Initialization Vector：初期化ベクトル）
    private static byte[]       ivBytes          = null;

    private static final long   serialVersionUID = -3924291538252750667L;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            // プロパティファイルを読み込む
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
     * @param:入力値
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )による暗号化文字列
     * @throws Exception
     */

    public static String encrypt(String inputStr) //
    {
        // 暗号化

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
        // 復号
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
     * メールアドレスの暗号化を一括で行う
     * 
     * @param cContractId 契約先コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
