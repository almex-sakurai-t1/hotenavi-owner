package com.hotenavi2.kitchen;

/**
 * �z�V�����������\�[�g���܂�
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
