package jp.happyhotel.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * �v���p�e�B�t�@�C���ǂݍ��݃N���X<br>
 * <br>
 * �v���p�e�B�t�@�C����ǂݍ��ނ��߂̃��[�e�B���e�B�N���X�ł��B<br>
 * �v���p�e�B�t�@�C������l���擾����@�\��񋟂��܂��B<br>
 */
public class PropertyReader
{
    /**
     * �R���X�g���N�^
     */
    private PropertyReader()
    {
    }

    /**
     * ������擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������擾���܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @return �L�[�ɑΉ�����l
     * @throws NullPointerException �w�肵���L�[��t�@�C������null�̏ꍇ�B
     * @throws MissingResourceException �w�肵���L�[��t�@�C����������Ȃ��ꍇ�B
     * @throws ClassCastException �w�肵���L�[�Ō��������I�u�W�F�N�g��������ł͂Ȃ��ꍇ�B
     */
    public static String readStr(String key, String fileName)
    {
        ResourceBundle rb = ResourceBundle.getBundle( fileName );

        return rb.getString( key );
    }

    /**
     * ������擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������擾���܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param directory �v���p�e�B�t�@�C�������݂���f�B���N�g��
     * @return �L�[�ɑΉ�����l
     * @throws NullPointerException �w�肵���L�[��t�@�C������null�̏ꍇ�B
     * @throws MissingResourceException �w�肵���L�[��t�@�C����������Ȃ��ꍇ�B
     * @throws ClassCastException �w�肵���L�[�Ō��������I�u�W�F�N�g��������ł͂Ȃ��ꍇ�B
     */
    public static String readStr(String key, String fileName, URL directory)
    {
        URLClassLoader urlLoader = new URLClassLoader( new URL[]{ directory } );
        ResourceBundle rb = ResourceBundle.getBundle( fileName, Locale.getDefault(), urlLoader );

        return rb.getString( key );
    }

    /**
     * ������擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������擾���܂��B<br>
     * �l���ǂݍ��߂Ȃ������ꍇ�A�w�肵���f�t�H���g�l��Ԃ��܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param defaultValue �l���ǂݍ��߂Ȃ������ꍇ�̃f�t�H���g�l
     * @return �L�[�ɑΉ�����l�i�ǂݍ��߂Ȃ������ꍇ�͎w�肵���f�t�H���g�l�j
     */
    public static String readStr(String key, String fileName, String defaultValue)
    {
        try
        {
            return readStr( key, fileName );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * ������擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������擾���܂��B<br>
     * �l���ǂݍ��߂Ȃ������ꍇ�A�w�肵���f�t�H���g�l��Ԃ��܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param directory �v���p�e�B�t�@�C�������݂���f�B���N�g��
     * @param defaultValue �l���ǂݍ��߂Ȃ������ꍇ�̃f�t�H���g�l
     * @return �L�[�ɑΉ�����l�i�ǂݍ��߂Ȃ������ꍇ�͎w�肵���f�t�H���g�l�j
     */
    public static String readStr(String key, String fileName, URL directory, String defaultValue)
    {
        try
        {
            return readStr( key, fileName, directory );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * �����l�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����琮���l���擾���܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @return �L�[�ɑΉ�����l
     * @throws NullPointerException �w�肵���L�[��t�@�C������null�̏ꍇ�B
     * @throws MissingResourceException �w�肵���L�[��t�@�C����������Ȃ��ꍇ�B
     * @throws ClassCastException �w�肵���L�[�Ō��������I�u�W�F�N�g��������ł͂Ȃ��ꍇ�B
     * @throws NumberFormatException �w�肵���L�[�Ō��������I�u�W�F�N�g�𐮐��l�Ƀp�[�X�ł��Ȃ������ꍇ�B
     */
    public static int readInt(String key, String fileName)
    {
        return Integer.parseInt( readStr( key, fileName ) );
    }

    /**
     * �����l�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����琮���l���擾���܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param directory �v���p�e�B�t�@�C�������݂���f�B���N�g��
     * @return �L�[�ɑΉ�����l
     * @throws NullPointerException �w�肵���L�[��t�@�C������null�̏ꍇ�B
     * @throws MissingResourceException �w�肵���L�[��t�@�C����������Ȃ��ꍇ�B
     * @throws ClassCastException �w�肵���L�[�Ō��������I�u�W�F�N�g��������ł͂Ȃ��ꍇ�B
     * @throws NumberFormatException �w�肵���L�[�Ō��������I�u�W�F�N�g�𐮐��l�Ƀp�[�X�ł��Ȃ������ꍇ�B
     */
    public static int readInt(String key, String fileName, URL directory)
    {
        return Integer.parseInt( readStr( key, fileName, directory ) );
    }

    /**
     * �����l�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����琮���l���擾���܂��B<br>
     * �l���ǂݍ��߂Ȃ������ꍇ�A�w�肵���f�t�H���g�l��Ԃ��܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param defaultValue �l���ǂݍ��߂Ȃ������ꍇ�̃f�t�H���g�l
     * @return �L�[�ɑΉ�����l�i�ǂݍ��߂Ȃ������ꍇ�͎w�肵���f�t�H���g�l�j
     */
    public static int readInt(String key, String fileName, int defaultValue)
    {
        try
        {
            return readInt( key, fileName );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * �����l�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����琮���l���擾���܂��B<br>
     * �l���ǂݍ��߂Ȃ������ꍇ�A�w�肵���f�t�H���g�l��Ԃ��܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param directory �v���p�e�B�t�@�C�������݂���f�B���N�g��
     * @param defaultValue �l���ǂݍ��߂Ȃ������ꍇ�̃f�t�H���g�l
     * @return �L�[�ɑΉ�����l�i�ǂݍ��߂Ȃ������ꍇ�͎w�肵���f�t�H���g�l�j
     */
    public static int readInt(String key, String fileName, URL directory, int defaultValue)
    {
        try
        {
            return readInt( key, fileName, directory );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    /**
     * ������̃��X�g�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������i�[�������X�g���擾���܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @return �L�[�ɑΉ�����l���i�[�������X�g
     * @throws NullPointerException �w�肵���L�[��t�@�C������null�̏ꍇ�B
     * @throws MissingResourceException �w�肵���L�[��t�@�C����������Ȃ��ꍇ�B
     * @throws ClassCastException �w�肵���L�[�Ō��������I�u�W�F�N�g��������ł͂Ȃ��ꍇ�B
     */
    public static List<String> readStrs(String key, String fileName)
    {
        return Arrays.asList( readStr( key, fileName ).split( ", *" ) );
    }

    /**
     * ������̃��X�g�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������i�[�������X�g���擾���܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param directory �v���p�e�B�t�@�C�������݂���f�B���N�g��
     * @return �L�[�ɑΉ�����l���i�[�������X�g
     * @throws NullPointerException �w�肵���L�[��t�@�C������null�̏ꍇ�B
     * @throws MissingResourceException �w�肵���L�[��t�@�C����������Ȃ��ꍇ�B
     * @throws ClassCastException �w�肵���L�[�Ō��������I�u�W�F�N�g��������ł͂Ȃ��ꍇ�B
     */
    public static List<String> readStrs(String key, String fileName, URL directory)
    {
        return Arrays.asList( readStr( key, fileName, directory ).split( ", *" ) );
    }

    /**
     * ������̃��X�g�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������i�[�������X�g���擾���܂��B<br>
     * �l���ǂݍ��߂Ȃ������ꍇ�A�w�肵���f�t�H���g�l��Ԃ��܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param defaultValues �l���ǂݍ��߂Ȃ������ꍇ�̃f�t�H���g�l���i�[�������X�g
     * @return �L�[�ɑΉ�����l���i�[�������X�g�i�ǂݍ��߂Ȃ������ꍇ�͎w�肵���f�t�H���g�l���i�[�������X�g�j
     */
    public static List<String> readStrs(String key, String fileName, List<String> defaultValues)
    {
        try
        {
            return readStrs( key, fileName );
        }
        catch ( Exception e )
        {
            return defaultValues;
        }
    }

    /**
     * ������̃��X�g�擾<br>
     * <br>
     * �v���p�e�B�t�@�C�����當������i�[�������X�g���擾���܂��B<br>
     * �l���ǂݍ��߂Ȃ������ꍇ�A�w�肵���f�t�H���g�l��Ԃ��܂��B<br>
     *
     * @param key �L�[
     * @param fileName �t�@�C�����i�g���q�Ȃ��j
     * @param directory �v���p�e�B�t�@�C�������݂���f�B���N�g��
     * @param defaultValues �l���ǂݍ��߂Ȃ������ꍇ�̃f�t�H���g�l���i�[�������X�g
     * @return �L�[�ɑΉ�����l���i�[�������X�g�i�ǂݍ��߂Ȃ������ꍇ�͎w�肵���f�t�H���g�l���i�[�������X�g�j
     */
    public static List<String> readStrs(String key, String fileName, URL directory, List<String> defaultValues)
    {
        try
        {
            return readStrs( key, fileName, directory );
        }
        catch ( Exception e )
        {
            return defaultValues;
        }
    }
}
