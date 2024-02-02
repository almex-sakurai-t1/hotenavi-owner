package com.hotenavi2.kitchen;

/**
 * オーダー分類名称をソートします
 */
@SuppressWarnings("rawtypes")
public class OrderClassComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        String strA = ((OrderInfo)a).OrderClassName;
        String strB = ((OrderInfo)b).OrderClassName;
        return(strA.compareTo( strB ));
    }
}
