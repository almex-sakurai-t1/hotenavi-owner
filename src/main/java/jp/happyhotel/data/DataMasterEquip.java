/*
 * @(#)DataMasterEquip.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �ݔ��f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �ݔ��f�[�^�擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterEquip implements Serializable
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
    /** �I�����P�i����j���� **/
    private String            branchName1;
    /** �I�����X�i�Ȃ��j���� **/
    private String            branchName2;
    /** ���ނP **/
    private int               class1;
    /** ���ނQ **/
    private int               class2;
    /** ���͏� **/
    private int               sortInput;
    /** �\���� **/
    private int               sortDisplay;
    /** ���͕��@ **/
    private int               method;
    /** �t���O�P **/
    private int               inputFlag1;
    /** �t���O�Q **/
    private int               inputFlag2;
    /** �t���O�R **/
    private int               inputFlag3;
    /** �t���O�S **/
    private int               inputFlag4;
    /** �t���O�T **/
    private int               inputFlag5;
    /** �t���O�U **/
    private int               inputFlag6;
    /** �t���O�V **/
    private int               inputFlag7;
    /** �t���O�W **/
    private int               inputFlag8;
    /** �t���O�X **/
    private int               inputFlag9;
    /** �t���O�P�O **/
    private int               inputFlag10;
    /** �A�C�R���ԍ� **/
    private int               iconNo;
    /** ����������P **/
    private String            findStr1;
    /** ����������Q **/
    private String            findStr2;
    /** ����������R **/
    private String            findStr3;
    /** ����������S **/
    private String            findStr4;
    /** ����������T **/
    private String            findStr5;
    /** ����������U **/
    private String            findStr6;
    /** ����������V **/
    private String            findStr7;
    /** ����������W **/
    private String            findStr8;
    /** ����������X **/
    private String            findStr9;
    /** ����������P�O **/
    private String            findStr10;
    /** ���� **/
    private String            memo;
    /** �ǉ������Ώېݔ�ID **/
    private int               addId;

    /**
     * �f�[�^�����������܂��B
     */
    public DataMasterEquip()
    {
        equipId = 0;
        name = "";
        nameKana = "";
        branchName1 = "";
        branchName2 = "";
        class1 = 0;
        class2 = 0;
        sortInput = 0;
        sortDisplay = 0;
        method = 0;
        inputFlag1 = 0;
        inputFlag2 = 0;
        inputFlag3 = 0;
        inputFlag4 = 0;
        inputFlag5 = 0;
        inputFlag6 = 0;
        inputFlag7 = 0;
        inputFlag8 = 0;
        inputFlag9 = 0;
        inputFlag10 = 0;
        iconNo = 0;
        findStr1 = "";
        findStr2 = "";
        findStr3 = "";
        findStr4 = "";
        findStr5 = "";
        findStr6 = "";
        findStr7 = "";
        findStr8 = "";
        findStr9 = "";
        findStr10 = "";
        memo = "";
        addId = 0;
    }

    public String getBranchName1()
    {
        return branchName1;
    }

    public String getBranchName2()
    {
        return branchName2;
    }

    public int getClass1()
    {
        return class1;
    }

    public int getClass2()
    {
        return class2;
    }

    public int getEquipId()
    {
        return equipId;
    }

    public String getFindStr1()
    {
        return findStr1;
    }

    public String getFindStr10()
    {
        return findStr10;
    }

    public String getFindStr2()
    {
        return findStr2;
    }

    public String getFindStr3()
    {
        return findStr3;
    }

    public String getFindStr4()
    {
        return findStr4;
    }

    public String getFindStr5()
    {
        return findStr5;
    }

    public String getFindStr6()
    {
        return findStr6;
    }

    public String getFindStr7()
    {
        return findStr7;
    }

    public String getFindStr8()
    {
        return findStr8;
    }

    public String getFindStr9()
    {
        return findStr9;
    }

    public int getIconNo()
    {
        return iconNo;
    }

    public int getInputFlag1()
    {
        return inputFlag1;
    }

    public int getInputFlag10()
    {
        return inputFlag10;
    }

    public int getInputFlag2()
    {
        return inputFlag2;
    }

    public int getInputFlag3()
    {
        return inputFlag3;
    }

    public int getInputFlag4()
    {
        return inputFlag4;
    }

    public int getInputFlag5()
    {
        return inputFlag5;
    }

    public int getInputFlag6()
    {
        return inputFlag6;
    }

    public int getInputFlag7()
    {
        return inputFlag7;
    }

    public int getInputFlag8()
    {
        return inputFlag8;
    }

    public int getInputFlag9()
    {
        return inputFlag9;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getAddId()
    {
        return addId;
    }

    public int getMethod()
    {
        return method;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public int getSortDisplay()
    {
        return sortDisplay;
    }

    public int getSortInput()
    {
        return sortInput;
    }

    public void setBranchName1(String branchName1)
    {
        this.branchName1 = branchName1;
    }

    public void setBranchName2(String branchName2)
    {
        this.branchName2 = branchName2;
    }

    public void setClass1(int class1)
    {
        this.class1 = class1;
    }

    public void setClass2(int class2)
    {
        this.class2 = class2;
    }

    public void setEquipId(int equipId)
    {
        this.equipId = equipId;
    }

    public void setFindStr1(String findStr1)
    {
        this.findStr1 = findStr1;
    }

    public void setFindStr10(String findStr10)
    {
        this.findStr10 = findStr10;
    }

    public void setFindStr2(String findStr2)
    {
        this.findStr2 = findStr2;
    }

    public void setFindStr3(String findStr3)
    {
        this.findStr3 = findStr3;
    }

    public void setFindStr4(String findStr4)
    {
        this.findStr4 = findStr4;
    }

    public void setFindStr5(String findStr5)
    {
        this.findStr5 = findStr5;
    }

    public void setFindStr6(String findStr6)
    {
        this.findStr6 = findStr6;
    }

    public void setFindStr7(String findStr7)
    {
        this.findStr7 = findStr7;
    }

    public void setFindStr8(String findStr8)
    {
        this.findStr8 = findStr8;
    }

    public void setFindStr9(String findStr9)
    {
        this.findStr9 = findStr9;
    }

    public void setIconNo(int iconNo)
    {
        this.iconNo = iconNo;
    }

    public void setInputFlag1(int inputFlag1)
    {
        this.inputFlag1 = inputFlag1;
    }

    public void setInputFlag10(int inputFlag10)
    {
        this.inputFlag10 = inputFlag10;
    }

    public void setInputFlag2(int inputFlag2)
    {
        this.inputFlag2 = inputFlag2;
    }

    public void setInputFlag3(int inputFlag3)
    {
        this.inputFlag3 = inputFlag3;
    }

    public void setInputFlag4(int inputFlag4)
    {
        this.inputFlag4 = inputFlag4;
    }

    public void setInputFlag5(int inputFlag5)
    {
        this.inputFlag5 = inputFlag5;
    }

    public void setInputFlag6(int inputFlag6)
    {
        this.inputFlag6 = inputFlag6;
    }

    public void setInputFlag7(int inputFlag7)
    {
        this.inputFlag7 = inputFlag7;
    }

    public void setInputFlag8(int inputFlag8)
    {
        this.inputFlag8 = inputFlag8;
    }

    public void setInputFlag9(int inputFlag9)
    {
        this.inputFlag9 = inputFlag9;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setAddId(int addId)
    {
        this.addId = addId;
    }

    public void setMethod(int method)
    {
        this.method = method;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setSortDisplay(int sortDisplay)
    {
        this.sortDisplay = sortDisplay;
    }

    public void setSortInput(int sortInput)
    {
        this.sortInput = sortInput;
    }

    /**
     * �ݔ��f�[�^�擾
     * 
     * @param equipId �ݔ�ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int equipId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_equip WHERE equip_id = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, equipId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.equipId = result.getInt( "equip_id" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.branchName1 = result.getString( "branch_name1" );
                    this.branchName2 = result.getString( "branch_name2" );
                    this.class1 = result.getInt( "class1" );
                    this.class2 = result.getInt( "class2" );
                    this.sortInput = result.getInt( "sort_input" );
                    this.sortDisplay = result.getInt( "sort_display" );
                    this.method = result.getInt( "method" );
                    this.inputFlag1 = result.getInt( "input_flag1" );
                    this.inputFlag2 = result.getInt( "input_flag2" );
                    this.inputFlag3 = result.getInt( "input_flag3" );
                    this.inputFlag4 = result.getInt( "input_flag4" );
                    this.inputFlag5 = result.getInt( "input_flag5" );
                    this.inputFlag6 = result.getInt( "input_flag6" );
                    this.inputFlag7 = result.getInt( "input_flag7" );
                    this.inputFlag8 = result.getInt( "input_flag8" );
                    this.inputFlag9 = result.getInt( "input_flag9" );
                    this.inputFlag10 = result.getInt( "input_flag10" );
                    this.iconNo = result.getInt( "icon_no" );
                    this.findStr1 = result.getString( "find_str1" );
                    this.findStr2 = result.getString( "find_str2" );
                    this.findStr3 = result.getString( "find_str3" );
                    this.findStr4 = result.getString( "find_str4" );
                    this.findStr5 = result.getString( "find_str5" );
                    this.findStr6 = result.getString( "find_str6" );
                    this.findStr7 = result.getString( "find_str7" );
                    this.findStr8 = result.getString( "find_str8" );
                    this.findStr9 = result.getString( "find_str9" );
                    this.findStr10 = result.getString( "find_str10" );
                    this.memo = result.getString( "memo" );
                    this.addId = result.getInt( "add_id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterEquip.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
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
                this.nameKana = result.getString( "name_kana" );
                this.branchName1 = result.getString( "branch_name1" );
                this.branchName2 = result.getString( "branch_name2" );
                this.class1 = result.getInt( "class1" );
                this.class2 = result.getInt( "class2" );
                this.sortInput = result.getInt( "sort_input" );
                this.sortDisplay = result.getInt( "sort_display" );
                this.method = result.getInt( "method" );
                this.inputFlag1 = result.getInt( "input_flag1" );
                this.inputFlag2 = result.getInt( "input_flag2" );
                this.inputFlag3 = result.getInt( "input_flag3" );
                this.inputFlag4 = result.getInt( "input_flag4" );
                this.inputFlag5 = result.getInt( "input_flag5" );
                this.inputFlag6 = result.getInt( "input_flag6" );
                this.inputFlag7 = result.getInt( "input_flag7" );
                this.inputFlag8 = result.getInt( "input_flag8" );
                this.inputFlag9 = result.getInt( "input_flag9" );
                this.inputFlag10 = result.getInt( "input_flag10" );
                this.iconNo = result.getInt( "icon_no" );
                this.findStr1 = result.getString( "find_str1" );
                this.findStr2 = result.getString( "find_str2" );
                this.findStr3 = result.getString( "find_str3" );
                this.findStr4 = result.getString( "find_str4" );
                this.findStr5 = result.getString( "find_str5" );
                this.findStr6 = result.getString( "find_str6" );
                this.findStr7 = result.getString( "find_str7" );
                this.findStr8 = result.getString( "find_str8" );
                this.findStr9 = result.getString( "find_str9" );
                this.findStr10 = result.getString( "find_str10" );
                this.memo = result.getString( "memo" );
                this.addId = result.getInt( "add_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterEquip.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
