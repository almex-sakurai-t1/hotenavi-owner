/**
 *
 */
package jp.happyhotel.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * ���b�Z�[�W�擾�N���X
 * 
 * @author J.Sato
 * @version 1.00 2010/12/09
 */
public class Message implements Serializable
{
    /**
     *
     */
    private static final long   serialVersionUID = -7247565049652207806L;

    private static final String MESSAGE_FILE     = "/etc/happyhotel/messages_jp.properties";
    private static final String MESSAGE_WIN_FILE = "C:\\etc\\happyhotel\\messages_jp.properties";

    /**
     * ���b�Z�[�W�t�@�C������A�w�肵�����b�Z�[�WID�̃��b�Z�[�W���擾����
     * 
     * @param messageID ���b�Z�[�WID
     * @return �擾�������b�Z�[�W�i���b�Z�[�WID�����o�^�̏ꍇ�́ANull��Ԃ��j
     * 
     */
    public static String getMessage(String messageID)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Properties�I�u�W�F�N�g�𐶐�
            Properties props = new Properties();

            // OS�̎�ނŃC���X�g�[���t�@�C���̐ݒu�p�X��ύX����
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // �t�@�C����ǂݍ���
            props.load( new FileInputStream( fileURL ) );

            // ���b�Z�[�W�t�@�C������擾
            messageStr = props.getProperty( messageID );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID );
        }

        return messageStr;
    }

    /**
     * ���b�Z�[�W�t�@�C������A�w�肵�����b�Z�[�WID�̃��b�Z�[�W���擾����
     * 
     * @param messageID ���b�Z�[�WID
     * @param repString �u������
     * @return �擾�������b�Z�[�W�i���b�Z�[�WID�����o�^�̏ꍇ�́ANull��Ԃ��j
     * 
     */
    public static String getMessage(String messageID, String repString)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Properties�I�u�W�F�N�g�𐶐�
            Properties props = new Properties();

            // OS�̎�ނŃC���X�g�[���t�@�C���̐ݒu�p�X��ύX����
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // �t�@�C����ǂݍ���
            props.load( new FileInputStream( fileURL ) );

            // ���b�Z�[�W�t�@�C������擾
            messageStr = MessageFormat.format( props.getProperty( messageID ), repString );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID + ", repString:" + repString );
        }

        return messageStr;
    }

    /**
     * ���b�Z�[�W�t�@�C������A�w�肵�����b�Z�[�WID�̃��b�Z�[�W���擾����
     * 
     * @param messageID ���b�Z�[�WID
     * @param repString1 �u�������P
     * @param repString2 �u�������Q
     * @return �擾�������b�Z�[�W�i���b�Z�[�WID�����o�^�̏ꍇ�́ANull��Ԃ��j
     * 
     */
    public static String getMessage(String messageID, String repString1, String repString2)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Properties�I�u�W�F�N�g�𐶐�
            Properties props = new Properties();

            // OS�̎�ނŃC���X�g�[���t�@�C���̐ݒu�p�X��ύX����
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // �t�@�C����ǂݍ���
            props.load( new FileInputStream( fileURL ) );

            // ���b�Z�[�W�t�@�C������擾
            messageStr = MessageFormat.format( props.getProperty( messageID ), repString1, repString2 );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID + ", repString1:" + repString1 + ", repString2:" + repString2 );
        }

        return messageStr;
    }

    /**
     * ���b�Z�[�W�t�@�C������A�w�肵�����b�Z�[�WID�̃��b�Z�[�W���擾����
     * 
     * @param messageID ���b�Z�[�WID
     * @param repString1 �u�������P
     * @param repString2 �u�������Q
     * @param repString3 �u�������R
     * @return �擾�������b�Z�[�W�i���b�Z�[�WID�����o�^�̏ꍇ�́ANull��Ԃ��j
     * 
     */
    public static String getMessage(String messageID, String repString1, String repString2, String repString3)
    {
        String messageStr = null;
        String fileURL = null;

        try
        {
            // Properties�I�u�W�F�N�g�𐶐�
            Properties props = new Properties();

            // OS�̎�ނŃC���X�g�[���t�@�C���̐ݒu�p�X��ύX����
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
            {
                fileURL = MESSAGE_WIN_FILE;
            }
            else
            {
                fileURL = MESSAGE_FILE;
            }

            // �t�@�C����ǂݍ���
            props.load( new FileInputStream( fileURL ) );

            // ���b�Z�[�W�t�@�C������擾
            messageStr = MessageFormat.format( props.getProperty( messageID ), repString1, repString2, repString3 );

        }
        catch ( Exception e )
        {
            Logging.error( "[Message.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID + ", repString1:" + repString1 + ", repString2:" + repString2 + ", repString3:" + repString3 );
        }

        return messageStr;
    }

}
