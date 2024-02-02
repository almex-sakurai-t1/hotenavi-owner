package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * 設備データ取得クラス
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

    /** 設備ID **/
    private int               equipId;
    /** 設備名称 **/
    private String            name;
    /** 設備名称カナ **/
    private String            nameKana;
    /**  **/
    private String            equipClassNameKana;
    /** 入力順 **/
    private int               sortInput;
    /** フラグ６ **/
    private int               inputFlag6;

    /**
     * データを初期化します。
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
     * 地方データ設定
     * 
     * @param result 地方データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
