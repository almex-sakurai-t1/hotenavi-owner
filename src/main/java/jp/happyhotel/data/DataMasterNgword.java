/*
 * @(#)DataMasterNgword.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 NGワードマスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * NGワードマスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/23
 * @version 1.1 2007/11/29
 */
public class DataMasterNgword implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 4309989917413718839L;

    /**
     * 管理番号
     */
    private int               seq;
    /**
     * NGワード
     */
    private String            ngWord;
    /**
     * ON/OFFフラグ
     */
    private int               onoffFlag;
    /**
     * 登録担当者
     */
    private String            personCode;
    /**
     * 登録日付
     */
    private int               appendDate;
    /**
     * 登録時刻
     */
    private int               appendTime;

    /**
     * データを初期化します。
     */
    public DataMasterNgword()
    {
        seq = 0;
        ngWord = "";
        onoffFlag = 0;
        personCode = "";
        appendDate = 0;
        appendTime = 0;
    }

    public int getAppendDate()
    {
        return appendDate;
    }

    public int getAppendTime()
    {
        return appendTime;
    }

    public String getNgWord()
    {
        return ngWord;
    }

    public int getOnoffFlag()
    {
        return onoffFlag;
    }

    public String getPersonCode()
    {
        return personCode;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setAppendDate(int appendDate)
    {
        this.appendDate = appendDate;
    }

    public void setAppendTime(int appendTime)
    {
        this.appendTime = appendTime;
    }

    public void setNgWord(String ngWord)
    {
        this.ngWord = ngWord;
    }

    public void setOnoffFlag(int onoffFlag)
    {
        this.onoffFlag = onoffFlag;
    }

    public void setPersonCode(String personCode)
    {
        this.personCode = personCode;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    /**
     * NGワードマスタ取得
     * 
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_master_ngword WHERE seq = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.seq = result.getInt( "seq" );
                    this.ngWord = result.getString( "ng_word" );
                    this.onoffFlag = result.getInt( "onoff_flag" );
                    this.personCode = result.getString( "person_code" );
                    this.appendDate = result.getInt( "append_date" );
                    this.appendTime = result.getInt( "append_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterNgword.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * NGワードマスタ設定
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
                this.seq = result.getInt( "seq" );
                this.ngWord = result.getString( "ng_word" );
                this.onoffFlag = result.getInt( "onoff_flag" );
                this.personCode = result.getString( "person_code" );
                this.appendDate = result.getInt( "append_date" );
                this.appendTime = result.getInt( "append_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterNgword.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
