/*
 * @(#)CheckString.java 1.00 2007/07/19 Copyright (C) ALMEX Inc. 2007 文字列チェック汎用クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * <p>
 * AMFWEBサービス
 * </p>
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/19
 */
public class ClipString implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -5943851177137253405L;
    private int               nextIndex;                               // 抜出後の開始位置

    public int getNextIndex()
    {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex)
    {
        this.nextIndex = nextIndex;
    }

    public ClipString()
    {
        this.nextIndex = 0;
    }

    /**
     * 文字列抜出（文字列）
     * 
     * @param charData
     * @param index
     * @param length
     * @return 文字列
     */
    public String clipWord(char[] charData, int index, int length)
    {
        String returnWord;
        returnWord = new String( charData, index, length );
        try
        {
            returnWord = new String( returnWord.trim().getBytes( "8859_1" ), "Windows-31J" );
            Logging.info( returnWord );

            this.nextIndex = index + length;
        }
        catch ( Exception e )
        {
            Logging.info( "[ClipString.clipWord()]Exception:" + e.toString() );
        }

        return returnWord;
    }

    /**
     * 文字列抜出(数字)
     * 
     * @param charData
     * @param index
     * @param length
     * @return 数字
     */
    public int clipNum(char[] charData, int index, int length)
    {
        int returnValue;

        returnValue = Integer.valueOf( new String( charData, index, length ) ).intValue();
        Logging.info( Integer.toString( returnValue ) );

        this.nextIndex = index + length;

        return returnValue;
    }
}
