package com.hotenavi2.common;

import java.io.Serializable;

public class ConvertHexToInt implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -1109809504367402466L;

    /**
     * 16�i��1������int�ɕϊ�
     * 
     * @param hex 16�i��������
     * @return int 10�i��
     */
    public static int HexToInt(String hex)
    {
        int value = 0;
        char hexDigit[] = hex.toCharArray();

        value = (Character.digit( hexDigit[0], 16 ));
        return(value);
    }
}
