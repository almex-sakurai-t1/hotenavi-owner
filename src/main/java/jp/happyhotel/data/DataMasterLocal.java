/*
 * @(#)DataMasterLocal.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �n���f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �n���f�[�^�擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterLocal implements Serializable
{
    private static final long serialVersionUID = 6118606304925320191L;

    /** �n��ID **/
    private int               localId;
    /** �n������ **/
    private String            name;
    /** �n�����̃J�i **/
    private String            nameKana;
    /** �\������ **/
    private int               dispIndex;
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

    /**
     * �f�[�^�����������܂��B
     */
    public DataMasterLocal()
    {
        localId = 0;
        name = "";
        nameKana = "";
        dispIndex = 0;
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
    }

    public int getDispIndex()
    {
        return dispIndex;
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

    public int getLocalId()
    {
        return localId;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
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

    public void setLocalId(int localId)
    {
        this.localId = localId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    /**
     * �n���f�[�^�擾
     * 
     * @param localId �n���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int localId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_local WHERE local_id = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, localId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.localId = result.getInt( "local_id" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.dispIndex = result.getInt( "disp_index" );
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
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterLocal.getData] Exception=" + e.toString() );
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
                this.localId = result.getInt( "local_id" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.dispIndex = result.getInt( "disp_index" );
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
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterLocal.setData] Exception=" + e.toString() );
        }
        return(true);
    }
}
