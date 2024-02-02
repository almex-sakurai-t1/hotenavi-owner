package jp.happyhotel.dto;

import java.io.Serializable;

/**
 * タッチ共通データ
 * 
 * @author Shingo Tashiro
 * @version 1.00 2014/8/13
 */
public class DtoApCommon implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2265892507114267138L;

    private boolean           htCheckIn;                              // チェックイン結果
    private boolean           connected;                              // ホスト連動状態
    private boolean           customer;                               // 顧客
    private int               id;                                     // ホテルID
    private String            hotelName;                              // ホテル名
    private int               seq;                                    // ハピホテチェックインコード
    private String            roomNo;                                 // 部屋番号
    private int               errorCode;                              // エラーコード
    private String            errorMessage;                           // エラーメッセージ
    private boolean           useableMile;                            // マイル使用可否
    private String            unavailableMessage;                     // マイル使用不可メッセージ

    /**
     * データを初期化します。
     */
    public DtoApCommon()
    {
        this.htCheckIn = false;
        this.connected = false;
        this.customer = false;
        this.id = 0;
        this.hotelName = "";
        this.seq = 0;
        this.roomNo = "";
        this.errorCode = 0;
        this.errorMessage = "";
        this.useableMile = true;
        this.unavailableMessage = "";
    }

    public boolean isHtCheckIn()
    {
        return htCheckIn;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public boolean isCustomer()
    {
        return customer;
    }

    public int getId()
    {
        return id;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getRoomNo()
    {
        return roomNo;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public boolean isUseableMile()
    {
        return useableMile;
    }

    public String getUnavailableMessage()
    {
        return unavailableMessage;
    }

    public void setHtCheckIn(boolean htCheckIn)
    {
        this.htCheckIn = htCheckIn;
    }

    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    public void setCustomer(boolean customer)
    {
        this.customer = customer;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public void setUseableMile(boolean useableMile)
    {
        this.useableMile = useableMile;
    }

    public void setUnavailableMessage(String unavailableMessage)
    {
        this.unavailableMessage = unavailableMessage;
    }

}
