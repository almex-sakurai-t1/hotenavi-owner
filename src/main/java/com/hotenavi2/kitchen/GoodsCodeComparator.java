package com.hotenavi2.kitchen;

/**
 * ���i�R�[�h���\�[�g���܂�
 */
@SuppressWarnings("rawtypes")
public class GoodsCodeComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        return(((OrderInfo)a).GoodsCode - ((OrderInfo)b).GoodsCode);
    }
}
