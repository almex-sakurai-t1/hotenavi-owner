package com.hotenavi2.kitchen;

/**
 * �`�[�ԍ����\�[�g���܂�
 */
@SuppressWarnings("rawtypes")
public class SlipComparator implements java.util.Comparator
{
    public int compare(Object a, Object b)
    {
        return(((OrderInfo)a).SlipNo - ((OrderInfo)b).SlipNo);
    }
}
