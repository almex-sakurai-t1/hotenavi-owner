/*
 * @(#)CheckString.java 1.00 2007/07/19 Copyright (C) ALMEX Inc. 2007 文字列チェック汎用クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <p>
 * 文字列のチェックを行うメソッド群です。
 * </p>
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/19
 */
public class CheckNgWord implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -8273317193908794385L;

    /**
     * NGワードチェック処理
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:異常,false:正常)
     */
    public static boolean ngWordCheck(String orgData)
    {
        int count;
        boolean ret;
        String query;
        String ngWord;
        DbAccess db;
        PreparedStatement prestate;
        ResultSet result;

        query = "SELECT * FROM hh_master_ngword WHERE onoff_flag = 0";

        db = new DbAccess();
        ret = false;
        count = 0;

        try
        {
            prestate = db.createPrepared( query );
            if ( prestate != null )
            {
                result = db.execQuery( prestate );
                if ( result != null )
                {
                    while( result.next() != false )
                    {
                        ngWord = result.getString( "ng_word" );
                        if ( orgData.indexOf( ngWord ) != -1 )
                        {
                            count++;
                        }
                    }
                }
                result.close();
            }
            prestate.close();
            if ( count > 0 )
                ret = true;
            else
                ret = false;
        }
        catch ( Exception e )
        {
        }
        finally
        {
            db.close();
        }
        return(ret);
    }

    /**
     * NGワードチェック処理
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果("":正常,それ以外:NGワードあり)
     */
    public static String outPutNgWord(String orgData)
    {
        int count;
        String query;
        String ngWord;
        DbAccess db;
        PreparedStatement prestate;
        ResultSet result;
        String outputWord;

        query = "SELECT * FROM hh_master_ngword WHERE onoff_flag = 0";

        db = new DbAccess();
        count = 0;
        outputWord = "";

        try
        {
            prestate = db.createPrepared( query );
            if ( prestate != null )
            {
                result = db.execQuery( prestate );
                if ( result != null )
                {
                    while( result.next() != false )
                    {
                        ngWord = result.getString( "ng_word" );
                        if ( orgData.indexOf( ngWord ) != -1 )
                        {
                            if ( count != 0 )
                            {
                                outputWord = outputWord + ", ";
                            }
                            outputWord = outputWord + result.getString( "ng_word" );
                            count++;
                        }
                    }
                }
                result.close();
            }
            prestate.close();
        }
        catch ( Exception e )
        {
        }
        finally
        {
            db.close();
        }
        return(outputWord);
    }

}
