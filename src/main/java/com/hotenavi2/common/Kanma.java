/*
 * @(#)Kanma.java  2.00 2004/03/18
 *
 * Copyright (C) ALMEX Inc. 2004
 *
 * �J���}�ҏW�N���X
 */

package com.hotenavi2.common;

import java.io.*;
import java.text.*;

/**
 * �J���}�ҏW�N���X�B
 *
 * �^����ꂽ���l���J���}�ҏW���܂��B
 *
 * @autho   S.Shiiya
 * @version 2.00 2004/03/18
 */
public class Kanma implements Serializable
{
    /**
     *  �J���}�ҏW
     *
     *  @param num ���l
     *  @return �J���}�ҏW�㕶����
     */
    public static String get( int num )
    {
        DecimalFormat    df;

        df = new DecimalFormat("###,###,###");
        return( df.format(num) );
    }
}

