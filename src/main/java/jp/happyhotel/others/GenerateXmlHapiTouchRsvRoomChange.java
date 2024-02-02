package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約部屋変更
 */
public class GenerateXmlHapiTouchRsvRoomChange extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_ROOM_CHANGE = "RsvRoomChange";
    private static final String TAG_RESULT          = "Result";
    private static final String TAG_STATUS          = "Status";
    private static final String TAG_STATUS_VALUE    = "StatusValue";
    private static final String TAG_ROOM_NO         = "RoomNo";
    private static final String TAG_ROOM_NAME       = "RoomName";
    private static final String TAG_ERROR_CODE      = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                               // 結果用タグ
    private XmlTag              status;                               // ステータス用タグ
    private XmlTag              statusValue;                          // ステータス用タグ
    private XmlTag              rsvNo;                                // 予約番号用タグ
    private XmlTag              roomName;                             // 予約部屋
    private XmlTag              errorCode;                            // エラーコード用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_ROOM_CHANGE );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, status );
        XmlTag.setParent( root, statusValue );
        XmlTag.setParent( root, rsvNo );
        XmlTag.setParent( root, roomName );
        XmlTag.setParent( root, errorCode );

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setStatus(String status)
    {
        this.status = XmlTag.createXmlTag( TAG_STATUS, status );
    }

    public void setStatusValue(int status)
    {
        this.statusValue = XmlTag.createXmlTag( TAG_STATUS_VALUE, status );
    }

    public void setRoomNo(String roomNo)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomNo );
    }

    public void setRoomName(String roomName)
    {
        this.roomName = XmlTag.createXmlTag( TAG_ROOM_NAME, roomName );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

}
