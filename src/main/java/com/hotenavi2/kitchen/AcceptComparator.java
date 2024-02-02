package com.hotenavi2.kitchen;

/**
 * 受付日時をソートします
 */
@SuppressWarnings("rawtypes")
public class AcceptComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        String strA = ((OrderInfo)a).AcceptDatetime;
        String strB = ((OrderInfo)b).AcceptDatetime;
        return(strA.compareTo( strB ));
    }
}
