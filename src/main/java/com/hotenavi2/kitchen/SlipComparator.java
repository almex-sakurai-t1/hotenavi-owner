package com.hotenavi2.kitchen;

/**
 * 伝票番号をソートします
 */
@SuppressWarnings("rawtypes")
public class SlipComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        return(((OrderInfo)a).SlipNo - ((OrderInfo)b).SlipNo);
    }
}
