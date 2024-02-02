package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * �ݔ��f�[�^�擾�N���X
 * 
 * @author HCL Technologies LTD
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataSearchMasterEquip_M2 implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 6118606304925320191L;

    /** �ݔ�ID **/
    private int               equipId;
    /** �ݔ����� **/
    private String            name;
    /** �ݔ����̃J�i **/
    private String            nameKana;
    /**  **/
    private String            equipClassNameKana;
    /** ���͏� **/
    private int               sortInput;
    /** �t���O�U **/
    private int               inputFlag6;

    /**
     * �f�[�^�����������܂��B
     */
    public DataSearchMasterEquip_M2()
    {
        equipId = 0;
        name = "";
    }

    public int getEquipId()
    {
        return equipId;
    }

    public String getName()
    {
        return name;
    }

    public int getInputFlag6()
    {
        return inputFlag6;
    }

    public void setEquipId(int equipId)
    {
        this.equipId = equipId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setInputFlag6(int inputFlag6)
    {
        this.inputFlag6 = inputFlag6;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public int getSortInput()
    {
        return sortInput;
    }

    public void setSortInput(int sortInput)
    {
        this.sortInput = sortInput;
    }

    /**
     * �n���f�[�^�ݒ�
     * 
     * @param result �n���f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.equipId = result.getInt( "equip_id" );
                this.name = result.getString( "name" );
                this.inputFlag6 = result.getInt( "input_flag6" );
                this.sortInput = result.getInt( "sort_input" );
                this.nameKana = result.getString( "name_kana" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterEquip.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    public String getEquipClassNameKana()
    {
        return equipClassNameKana;
    }

    public void setEquipClassNameKana(String equipClassNameKana)
    {
        this.equipClassNameKana = equipClassNameKana;
    }

}
