package jp.happyhotel.data;

import java.util.List;

/**
 * ���u�C���W���p���p�z�e��
 */
public class DataApiLvjHotel extends DataApiSuccess
{
    /** �S���� */
    private int                   number_of_records;
    /** �v�����z�� */
    private List<DataApiHogeLvjHotel> hotels;

    public int getNumber_of_records()
    {
        return number_of_records;
    }

    public void setNumber_of_records(int number_of_records)
    {
        this.number_of_records = number_of_records;
    }

    public List<DataApiHogeLvjHotel> gethotels()
    {
        return hotels;
    }

    public void setHotels(List<DataApiHogeLvjHotel> l_Basic)
    {
        this.hotels = l_Basic;
    }
}
