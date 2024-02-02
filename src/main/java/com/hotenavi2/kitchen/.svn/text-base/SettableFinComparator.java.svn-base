package com.hotenavi2.kitchen;

/**
 * 配膳完了時刻をソートします
 */
@SuppressWarnings("rawtypes")
public class SettableFinComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        String strA = ((OrderInfo)a).SettableFinDatetime;
        String strB = ((OrderInfo)b).SettableFinDatetime;
        return(strA.compareTo( strB ));
    }
}
