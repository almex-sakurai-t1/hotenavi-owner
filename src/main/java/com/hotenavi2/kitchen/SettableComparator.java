package com.hotenavi2.kitchen;

/**
 * �z�V�w�莞�����\�[�g���܂�
 */
@SuppressWarnings("rawtypes")
public class SettableComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        String strA = ((OrderInfo)a).SettableDatetime;
        String strB = ((OrderInfo)b).SettableDatetime;
        return(strA.compareTo( strB ));
    }
}
