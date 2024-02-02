package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �O���T�[�r�X��`�f�[�^����p�N���X<br>
 * <br>
 * �O���T�[�r�X�iFacebook�AGoogleMap�ALINE@�Aetc...�j�̒�`�f�[�^����p�̃N���X�ł��B<br>
 * 
 * @author koshiba-y1
 */
public class DataHhMasterUrl implements Serializable
{
    /** �O���T�[�r�X�̎�ނ��������ʎq */
    private int    data_type;
    /** �T�[�r�X�� */
    private String name;
    /** �{�^���v�fHTML�e�L�X�g */
    private String button_image_elem;
    /** �{�^���v�fHTML�e�L�X�g�i�X�}�z�T�C�g�p�j */
    private String button_image_elem_phone;
    /** URL�̃o���f�[�V�����`�F�b�N�pJS */
    private String val_check_script;
    /** ���ƕ��̂ݕҏW�\�ȃR���e���c��\���t���O */
    private int    imedia_only;

    /**
     * �R���X�g���N�^
     */
    public DataHhMasterUrl()
    {
        // �C���X�^���X�ϐ�������
        this.data_type = 0;
        this.name = "";
        this.button_image_elem = "";
        this.button_image_elem_phone = "";
        this.val_check_script = "";
        this.imedia_only = 0;
    }

    /**
     * this.data_type�̃Q�b�^�[
     * 
     * @return this.data_type
     */
    public int getDataType()
    {
        return this.data_type;
    }

    /**
     * this.name�̃Q�b�^�[
     * 
     * @return this.name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * this.button_image_elem�̃Q�b�^�[
     * 
     * @return this.button_image_elem
     */
    public String getButtonImageElem()
    {
        return this.button_image_elem;
    }

    /**
     * this.button_image_elem_phone�̃Q�b�^�[
     * 
     * @return button_image_elem_phone
     */
    public String getButtonImageElemPhone()
    {
        return this.button_image_elem_phone;
    }

    /**
     * this.val_check_script�̃Q�b�^�[
     * 
     * @return this.val_check_script
     */
    public String getValCheckScript()
    {
        return this.val_check_script;
    }

    /**
     * this.imedia_only�̃Q�b�^�[
     * 
     * @return this.imedia_only
     */
    public int getImediaOnly()
    {
        return this.imedia_only;
    }

    /**
     * this.data_type�̃Z�b�^�[
     * 
     * @param data_type �O���T�[�r�X�̎�ނ��������ʎq
     */
    public void setDataType(int data_type)
    {
        this.data_type = data_type;
    }

    /**
     * this.name�̃Z�b�^�[
     * 
     * @param name �T�[�r�X��
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * this.button_image_elem�̃Z�b�^�[
     * 
     * @param button_image_elem �{�^���v�fHTML�e�L�X�g
     */
    public void setButtonImageElem(String button_image_elem)
    {
        this.button_image_elem = button_image_elem;
    }

    /**
     * this.button_image_elem_phone�̃Z�b�^�[
     * 
     * @param button_image_elem_phone �{�^���v�fHTML�e�L�X�g
     */
    public void setButtonImageElemPhone(String button_image_elem_phone)
    {
        this.button_image_elem_phone = button_image_elem_phone;
    }

    /**
     * this.val_check_script�̃Z�b�^�[
     * 
     * @param val_check_script URL�̃o���f�[�V�����`�F�b�N�pJS
     */
    public void setValCheckScript(String val_check_script)
    {
        this.val_check_script = val_check_script;
    }

    /**
     * this.imedia_only�̃Z�b�^�[
     * 
     * @param imedia_only ���ƕ��̂ݕҏW�\�ȃR���e���c��\���t���O
     */
    public void setImediaOnly(int imedia_only)
    {
        this.imedia_only = imedia_only;
    }

    /**
     * �S�ẴC���X�^���X�ϐ��ւ̃Z�b�^�[
     * 
     * @param result �f�[�^���܂ލs��������ResultSet
     * @return �����̌���
     * @throws Exception
     */
    public boolean setData(ResultSet result) throws Exception
    {
        if ( result == null )
        {
            return false;
        }

        try
        {
            this.data_type = result.getInt( "data_type" );
            this.name = result.getString( "name" );
            this.button_image_elem = result.getString( "button_image_elem" );
            this.button_image_elem_phone = result.getString( "button_image_elem_phone" );
            this.val_check_script = result.getString( "val_check_script" );
            this.imedia_only = result.getInt( "imedia_only" );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrl.setData] Exception=" + e.toString() );
            throw e;
        }

        return true;
    }

    /**
     * �f�[�^�擾<br>
     * <br>
     * hh_master_url����f�[�^���擾���A�C���X�^���X�ϐ��ɃZ�b�g���܂��B<br>
     * �f�[�^���擾�ł����ꍇ��true���A�擾�ł��Ȃ������ꍇ��false��Ԃ��܂��B<br>
     * BD�ւ̃A�N�Z�X���ŃG���[�����������ꍇ�͗�O�𓊂��܂��B<br>
     * 
     * @param data_type �O���T�[�r�X�̎�ނ��������ʎq
     * @return �����̌���
     * @exception Exception
     */
    public boolean selectData(int data_type) throws Exception
    {
        // DB�A�N�Z�X�֘A
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( "SELECT * FROM hh_master_url WHERE data_type = " + data_type );
            result = prestate.executeQuery();

            if ( result == null )
            {
                return false;
            }

            if ( result.next() == false )
            {
                return false;
            }

            this.data_type = result.getInt( "data_type" );
            this.name = result.getString( "name" );
            this.button_image_elem = result.getString( "button_image_elem" );
            this.button_image_elem_phone = result.getString( "button_image_elem_phone" );
            this.val_check_script = result.getString( "val_check_script" );
            this.imedia_only = result.getInt( "imedia_only" );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrl.selectData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /**
     * hh_master_url�ɓo�^����Ă���O���T�[�r�X���̎擾
     * 
     * @return hh_master_url�ɓo�^����Ă���O���T�[�r�X�̐�
     * @throws Exception
     */
    public int selectServicesCount() throws Exception
    {
        // DB�A�N�Z�X�֘A
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( "SELECT COUNT(data_type) AS num FROM hh_master_url" );
            result = prestate.executeQuery();

            if ( result == null )
            {
                return 0;
            }

            if ( result.next() == false )
            {
                return 0;
            }

            return result.getInt( "num" );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrl.selectServicesCount] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }
}
