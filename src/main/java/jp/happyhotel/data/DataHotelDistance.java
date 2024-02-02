/*
 * @(#)DataHotelDistance.java
 * 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007
 * �z�e���������f�[�^�擾�N���X
 */

package jp.happyhotel.data;

/**
 * �z�e�������A�ܓx�o�x�擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/22
 */

public class DataHotelDistance implements Comparable<Object>
{
    private int    hotelId;
    private int    distance;
    private String hotelLat;
    private String hotelLon;

    /**
     * �f�[�^�����������܂��B
     */
    public DataHotelDistance()
    {
        hotelId = 0;
        distance = 0;
    }

    /**
     * @return ����
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * @return �z�e���̈ܓx
     */
    public String getHotelLat()
    {
        return hotelLat;
    }

    /**
     * @return �z�e���̌o�x
     */
    public String getHotelLon()
    {
        return hotelLon;
    }

    /**
     * @return �z�e��ID
     */
    public int getId()
    {
        return hotelId;
    }

    /**
     * @param �������Z�b�g
     */
    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    /**
     * @param �z�e���̈ܓx���Z�b�g
     */
    public void setHotelLat(String hotelLat)
    {
        this.hotelLat = hotelLat;
    }

    /**
     * @param �z�e���̌o�x���Z�b�g
     */
    public void setHotelLon(String hotelLon)
    {
        this.hotelLon = hotelLon;
    }

    /**
     * @param �z�e��ID���Z�b�g
     */
    public void setId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    /**
     * �I�u�W�F�N�g��r����
     * 
     * @param obj �I�u�W�F�N�g
     * @return ��������(-1:������,0:������,1:�傫��)
     * @see DataHotelDistance�N���X��distance���r����
     * @see java.util.Arrays.sort()��p���邱�ƂŃ\�[�g�\
     */
    public int compareTo(Object obj)
    {
        double distance1 = this.distance;
        double distance2 = ((DataHotelDistance)obj).distance;

        if ( distance1 == distance2 )
        {
            return 0;
        }
        else if ( distance1 > distance2 )
        {
            return 1;
        }
        else
        {
            return -1;
        }

    }
}
