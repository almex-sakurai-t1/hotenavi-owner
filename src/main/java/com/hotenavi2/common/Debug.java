/*
 * @(#)Debug.java  2.00 2004/06/14
 *
 * Copyright (C) ALMEX Inc. 2004
 *
 * HEXÉ_ÉìÉvèoóÕ
 */

package com.hotenavi2.common;

import java.io.*;
import java.lang.*;

public class Debug implements Serializable
{
    public static String hex(String str)
    {
        StringBuffer sb = new StringBuffer();
        char[] buf=str.toCharArray();

        for (int i=0; i<buf.length; i++)
        {
            sb.append(Integer.toString(buf[i], 16)+" ");
        }

        return new String(sb);
    }
}

