package jp.happyhotel.data;

import java.sql.ResultSet;

import jp.happyhotel.common.Logging;

/**
 * @author Sakurai
 */
public class DataHotelMailmagazine
{
    private String hotel_id;
    private String name;

    /**
     * �f�[�^�����������܂��B
     */
    public DataHotelMailmagazine()
    {
        hotel_id = "";
        name = "";
    }

    public String getHotelId()
    {
        return hotel_id;
    }

    public String getName()
    {
        return name;
    }

    public void setHotelId(String hotel_id)
    {
        this.hotel_id = hotel_id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * �z�e���M�������[�ݒ�
     * 
     * @param result �n���f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotel_id = result.getString( "hotel_id" );
                this.name = result.getString( "name" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMailmagazine.setData] Exception=" + e.toString() );
        }

        return(true);
    }

}
