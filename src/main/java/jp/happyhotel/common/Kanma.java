/*
 * @(#)Kanma.java 2.00 2004/03/18
 * Copyright (C) ALMEX Inc. 2004
 * カンマ編集クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * カンマ編集クラス。
 * 
 * 与えられた数値をカンマ編集します。
 * 
 * @author S.Tashiro
 * @version 2.00 2010/12/15
 */
public class Kanma implements Serializable
{
    /**
     * カンマ編集
     * 
     * @param num 数値
     * @return カンマ編集後文字列
     */
    public static String get(int num)
    {
        DecimalFormat df;

        df = new DecimalFormat( "###,###,###" );
        return(df.format( num ));
    }
}
