package com.hotenavi2.kitchen;

/**
 * ���i���̂��\�[�g���܂�
 */
@SuppressWarnings("rawtypes")
public class GoodsComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        String strA = ((OrderInfo)a).GoodsName;
        String strB = ((OrderInfo)b).GoodsName;
        return(strA.compareTo( strB ));
    }
}
