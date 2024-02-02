package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML�n�s�^�b�`�\�񕔉��ύX
 */
public class GenerateXmlHapiTouchRsvRoomChange extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_RSV_ROOM_CHANGE = "RsvRoomChange";
    private static final String TAG_RESULT          = "Result";
    private static final String TAG_STATUS          = "Status";
    private static final String TAG_STATUS_VALUE    = "StatusValue";
    private static final String TAG_ROOM_NO         = "RoomNo";
    private static final String TAG_ROOM_NAME       = "RoomName";
    private static final String TAG_ERROR_CODE      = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                               // ���ʗp�^�O
    private XmlTag              status;                               // �X�e�[�^�X�p�^�O
    private XmlTag              statusValue;                          // �X�e�[�^�X�p�^�O
    private XmlTag              rsvNo;                                // �\��ԍ��p�^�O
    private XmlTag              roomName;                             // �\�񕔉�
    private XmlTag              errorCode;                            // �G���[�R�[�h�p�^�O

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
