/*
 * @(#)DataMasterPoint.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ポイント管理マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ポイント管理マスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/29
 */
public class DataMasterPoint implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 8405624667439787934L;

    private int               code;
    private String            name;
    private String            nameKana;
    private String            nameMobile;
    private int               kind;
    private int               addPoint;
    private int               period;
    private int               limitFlag;
    private int               adjustmentFlag;
    private int               payFlag;
    private int               range;
    private int               shiftDay;
    private double            freeMultiple;

    /**
     * データを初期化します。
     */
    public DataMasterPoint()
    {
        code = 0;
        name = "";
        nameKana = "";
        nameMobile = "";
        kind = 0;
        addPoint = 0;
        period = 0;
        limitFlag = 0;
        adjustmentFlag = 0;
        payFlag = 0;
        range = 0;
        shiftDay = 0;
        freeMultiple = 0.0;
    }

    public int getAddPoint()
    {
        return addPoint;
    }

    public int getAdjustmentFlag()
    {
        return adjustmentFlag;
    }

    public int getCode()
    {
        return code;
    }

    public int getKind()
    {
        return kind;
    }

    public int getLimitFlag()
    {
        return limitFlag;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getNameMobile()
    {
        return nameMobile;
    }

    public int getPayFlag()
    {
        return payFlag;
    }

    public int getPeriod()
    {
        return period;
    }

    public int getRange()
    {
        return range;
    }

    public int getShiftDay()
    {
        return shiftDay;
    }

    public double getFreeMultiple()
    {
        return freeMultiple;
    }

    public void setAddPoint(int addPoint)
    {
        this.addPoint = addPoint;
    }

    public void setAdjustmentFlag(int adjustmentFlag)
    {
        this.adjustmentFlag = adjustmentFlag;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setLimitFlag(int limitFlag)
    {
        this.limitFlag = limitFlag;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setNameMobile(String nameMobile)
    {
        this.nameMobile = nameMobile;
    }

    public void setPayFlag(int payFlag)
    {
        this.payFlag = payFlag;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public void setShiftDay(int shiftDay)
    {
        this.shiftDay = shiftDay;
    }

    public void setFreeMultiple(double freeMultiple)
    {
        this.freeMultiple = freeMultiple;
    }

    /**
     * ポイント管理マスタデータ取得
     * 
     * @param code ポイントコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int code)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE code = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, code );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.code = result.getInt( "code" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.nameMobile = result.getString( "name_mobile" );
                    this.kind = result.getInt( "kind" );
                    this.addPoint = result.getInt( "add_point" );
                    this.period = result.getInt( "period" );
                    this.limitFlag = result.getInt( "limit_flag" );
                    this.adjustmentFlag = result.getInt( "adjustment_flag" );
                    this.payFlag = result.getInt( "pay_flag" );
                    this.range = result.getInt( "available_range" );
                    this.shiftDay = result.getInt( "shift_day" );
                    this.freeMultiple = result.getDouble( "free_multiple" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPoint.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ポイント管理マスタデータ設定
     * 
     * @param result 都道府県データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.code = result.getInt( "code" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.nameMobile = result.getString( "name_mobile" );
                this.kind = result.getInt( "kind" );
                this.addPoint = result.getInt( "add_point" );
                this.period = result.getInt( "period" );
                this.limitFlag = result.getInt( "limit_flag" );
                this.adjustmentFlag = result.getInt( "adjustment_flag" );
                this.payFlag = result.getInt( "pay_flag" );
                this.range = result.getInt( "available_range" );
                this.shiftDay = result.getInt( "shift_day" );
                this.freeMultiple = result.getDouble( "free_multiple" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterPoint.getData] Exception=" + e.toString() );
        }
        return(true);
    }
}
