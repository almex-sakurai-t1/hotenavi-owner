package jp.happyhotel.owner;

public class FormOwnerRsvImageUpLoad
{
    public static final int MAX_POST_SIZE = 1024 * 500; // ���M����f�[�^�̍ő�T�C�Y(�o�C�g)
 
    private int selHotelId = 0;
 
    private String message = "";

    
    public int getSelHotelId()
    {
        return selHotelId;
    }

    public void setSelHotelId(int selHotelId)
    {
        this.selHotelId = selHotelId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
