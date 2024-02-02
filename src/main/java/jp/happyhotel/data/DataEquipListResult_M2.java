package jp.happyhotel.data;

import java.io.Serializable;

public class DataEquipListResult_M2 implements Serializable
{

    //
    private static final long          serialVersionUID = -8232208801753894669L;

    private String                     errorMessage     = "";
    private int[]                      amenityId;
    private String                     equipClassNameKana;
    private DataSearchMasterEquip_M2[] dataEquipListDto = new DataSearchMasterEquip_M2[0];

    public DataSearchMasterEquip_M2[] getDataEquipListDto()
    {
        return dataEquipListDto;
    }

    public void setDataEquipListDto(DataSearchMasterEquip_M2[] dataEquipListDto)
    {
        this.dataEquipListDto = dataEquipListDto;
    }

    public int[] getAmenityId()
    {
        return amenityId;
    }

    public void setAmenityId(int[] amenityId)
    {
        this.amenityId = amenityId;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
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
