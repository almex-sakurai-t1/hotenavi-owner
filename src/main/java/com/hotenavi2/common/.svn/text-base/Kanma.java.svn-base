/*
 * @(#)Kanma.java  2.00 2004/03/18
 *
 * Copyright (C) ALMEX Inc. 2004
 *
 * カンマ編集クラス
 */

package com.hotenavi2.common;

import java.io.*;
import java.text.*;

/**
 * カンマ編集クラス。
 *
 * 与えられた数値をカンマ編集します。
 *
 * @autho   S.Shiiya
 * @version 2.00 2004/03/18
 */
public class Kanma implements Serializable
{
    /**
     *  カンマ編集
     *
     *  @param num 数値
     *  @return カンマ編集後文字列
     */
    public static String get( int num )
    {
        DecimalFormat    df;

        df = new DecimalFormat("###,###,###");
        return( df.format(num) );
    }
}

