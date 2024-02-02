package com.hotenavi2.kitchen;

/**
 * 商品数量をソートします
 */
@SuppressWarnings("rawtypes")
public class CountComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        return(((OrderInfo)a).GoodsCount - ((OrderInfo)b).GoodsCount);
    }
}
