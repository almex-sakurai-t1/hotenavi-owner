package com.hotenavi2.kitchen;

/**
 * 部屋名称をソートします
 */
@SuppressWarnings("rawtypes")
public class RoomComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        String strA = ((OrderInfo)a).RoomName;
        String strB = ((OrderInfo)b).RoomName;
        return(strA.compareTo( strB ));
    }
}
