package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.dto.DtoApHotelCustomerData;

/**
 * 前登録メンバー情報
 * 
 * @author S.Tashiro
 * @version 1.00 2015/03/14
 * @see リクエスト：1044電文<br>
 *      レスポンス：1045電文
 */
public class MemberRegistExInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5730588352898876522L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1050";
    final String              REPLY_COMMAND    = "1051";
    String                    header;
    // 送信電文
    String                    memberId;
    int                       birthYear1;
    int                       birthMonth1;
    int                       birthDate1;
    String                    userId;
    String                    password;

    // 受信電文
    int                       result;
    String                    handleName;
    String                    name;
    String                    nameKana;
    int                       sex;
    String                    addr1;
    String                    addr2;
    String                    tel1;
    String                    tel2;
    String                    mailAddr;
    int                       mailMagFlag;
    int                       memorialYear1;
    int                       memorialMonth1;
    int                       memorialDate1;
    int                       birthYear2;
    int                       birthMonth2;
    int                       birthDate2;
    int                       memorialYear2;
    int                       memorialMonth2;
    int                       memorialDate2;
    String                    reserve;

    // DTOホテル顧客データ
    boolean                   boolMemberCheckIn;
    DtoApHotelCustomerData    apHotelCustomerData;

    public MemberRegistExInfo()
    {
        this.header = "";
        this.memberId = "";
        this.birthYear1 = 0;
        this.birthMonth1 = 0;
        this.birthDate1 = 0;
        this.userId = "";
        this.password = "";
        this.handleName = "";
        this.name = "";
        this.nameKana = "";
        this.sex = 0;
        this.addr1 = "";
        this.addr2 = "";
        this.tel1 = "";
        this.tel2 = "";
        this.mailAddr = "";
        this.mailMagFlag = 0;
        this.memorialYear1 = 0;
        this.memorialMonth1 = 0;
        this.memorialDate1 = 0;
        this.birthYear2 = 0;
        this.birthMonth2 = 0;
        this.birthDate2 = 0;
        this.memorialYear2 = 0;
        this.memorialMonth2 = 0;
        this.memorialDate2 = 0;
        this.reserve = "";
        this.result = 0;
    }

    public String getHeader()
    {
        return header;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public int getBirthYear1()
    {
        return birthYear1;
    }

    public int getBirthMonth1()
    {
        return birthMonth1;
    }

    public int getBirthDate1()
    {
        return birthDate1;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getPassword()
    {
        return password;
    }

    public int getResult()
    {
        return result;
    }

    public String getHandleName()
    {
        return handleName;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public int getSex()
    {
        return sex;
    }

    public String getAddr1()
    {
        return addr1;
    }

    public String getAddr2()
    {
        return addr2;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public int getMailMagFlag()
    {
        return mailMagFlag;
    }

    public int getMemorialYear1()
    {
        return memorialYear1;
    }

    public int getMemorialMonth1()
    {
        return memorialMonth1;
    }

    public int getMemorialDate1()
    {
        return memorialDate1;
    }

    public int getBirthYear2()
    {
        return birthYear2;
    }

    public int getBirthMonth2()
    {
        return birthMonth2;
    }

    public int getBirthDate2()
    {
        return birthDate2;
    }

    public int getMemorialYear2()
    {
        return memorialYear2;
    }

    public int getMemorialMonth2()
    {
        return memorialMonth2;
    }

    public int getMemorialDate2()
    {
        return memorialDate2;
    }

    public String getReserve()
    {
        return reserve;
    }

    public boolean isBoolMemberCheckIn()
    {
        return boolMemberCheckIn;
    }

    public DtoApHotelCustomerData getApHotelCustomerData()
    {
        return apHotelCustomerData;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public void setBirthYear1(int birthYear1)
    {
        this.birthYear1 = birthYear1;
    }

    public void setBirthMonth1(int birthMonth1)
    {
        this.birthMonth1 = birthMonth1;
    }

    public void setBirthDate1(int birthDate1)
    {
        this.birthDate1 = birthDate1;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setAddr1(String addr1)
    {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2)
    {
        this.addr2 = addr2;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMailMagFlag(int mailMagFlag)
    {
        this.mailMagFlag = mailMagFlag;
    }

    public void setMemorialYear1(int memorialYear1)
    {
        this.memorialYear1 = memorialYear1;
    }

    public void setMemorialMonth1(int memorialMonth1)
    {
        this.memorialMonth1 = memorialMonth1;
    }

    public void setMemorialDate1(int memorialDate1)
    {
        this.memorialDate1 = memorialDate1;
    }

    public void setBirthYear2(int birthYear2)
    {
        this.birthYear2 = birthYear2;
    }

    public void setBirthMonth2(int birthMonth2)
    {
        this.birthMonth2 = birthMonth2;
    }

    public void setBirthDate2(int birthDate2)
    {
        this.birthDate2 = birthDate2;
    }

    public void setMemorialYear2(int memorialYear2)
    {
        this.memorialYear2 = memorialYear2;
    }

    public void setMemorialMonth2(int memorialMonth2)
    {
        this.memorialMonth2 = memorialMonth2;
    }

    public void setMemorialDate2(int memorialDate2)
    {
        this.memorialDate2 = memorialDate2;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public void setBoolMemberCheckIn(boolean boolMemberCheckIn)
    {
        this.boolMemberCheckIn = boolMemberCheckIn;
    }

    public void setApHotelCustomerData(DtoApHotelCustomerData apHotelCustomerData)
    {
        this.apHotelCustomerData = apHotelCustomerData;
    }

    /***
     * 電文送信
     * 
     * @param frontIp
     * @param timeOut
     * @param portNo
     * @param hotelId
     * @return
     */
    public boolean sendToHost(String frontIp, int timeOut, int portNo, String hotelId)
    {
        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        boolean ret = false;
        int nIndex = 0;
        ClipString clip = new ClipString();

        // 抜出データの開始位置
        nIndex = HEADER_LENGTH + COMMAND_LENGTH;

        // ホスト側データ送信
        tcpclient = new TcpClientEx();
        // 指定のipアドレスに接続
        ret = tcpclient.connectServiceByAddr( frontIp, timeOut, portNo );
        if ( ret != false )
        {
            ret = false;
            try
            {

                sendData = COMMAND;
                sendData += tcpclient.leftFitFormat( this.memberId, 9 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthMonth1 ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthDate1 ), 2 );
                sendData += tcpclient.leftFitFormat( this.userId, 32 );
                sendData += tcpclient.leftFitFormat( this.password, 8 );
                sendData += tcpclient.leftFitFormat( this.reserve, 10 );
                header = tcpclient.getPacketHeader( hotelId, sendData.getBytes( "Windows-31J" ).length );

                sendData = header + sendData;
                int roop = 0;
                while( true )
                {
                    // 電文送信
                    tcpclient.send( sendData );

                    // 受信待機
                    recvData = tcpclient.recv();

                    roop++;
                    if ( recvData.indexOf( "exception" ) >= 0 )
                    {
                        Logging.error( "電文受信Exception " + recvData );
                    }
                    else
                    {
                        charData = new char[recvData.length()];
                        charData = recvData.toCharArray();

                        // コマンド取得
                        data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                        // 応答電文コマンドが1003なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {

                            // 返ってきた情報をセット
                            // 結果
                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // メンバID
                            this.memberId = clip.clipWord( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // 誕生日1（年）
                            this.birthYear1 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            // 誕生日1（月）
                            this.birthMonth1 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 誕生日1（日）
                            this.birthDate1 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // ユーザID
                            this.userId = clip.clipWord( charData, nIndex, 32 );
                            nIndex = clip.getNextIndex();

                            // パス
                            this.password = clip.clipWord( charData, nIndex, 8 );
                            nIndex = clip.getNextIndex();

                            // ハンドルネーム
                            this.handleName = clip.clipWord( charData, nIndex, 20 );
                            nIndex = clip.getNextIndex();

                            // 名前
                            this.name = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // フリガナ
                            // 名前
                            this.nameKana = clip.clipWord( charData, nIndex, 20 );
                            nIndex = clip.getNextIndex();

                            // 性別
                            this.sex = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 住所1
                            this.addr1 = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // 住所2
                            this.addr2 = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // 電話1
                            this.tel1 = clip.clipWord( charData, nIndex, 15 );
                            nIndex = clip.getNextIndex();

                            // 電話2
                            this.tel2 = clip.clipWord( charData, nIndex, 15 );
                            nIndex = clip.getNextIndex();

                            // メールアドレス
                            this.mailAddr = clip.clipWord( charData, nIndex, 63 );
                            nIndex = clip.getNextIndex();

                            // メルマガ
                            this.mailMagFlag = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 記念日1（年）
                            this.memorialYear1 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();
                            // 記念日1（月）
                            this.memorialMonth1 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
                            // 記念日1（日）
                            this.memorialDate1 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 誕生日2（年）
                            this.birthYear2 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            // 誕生日2（月）
                            this.birthMonth2 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 誕生日2（日）
                            this.birthDate2 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 記念日2（年）
                            this.memorialYear2 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();
                            // 記念日1（月）
                            this.memorialMonth2 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();
                            // 記念日1（日）
                            this.memorialDate2 = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 正常を返して成功とする
                            ret = true;
                        }
                    }
                    if ( roop >= retryCount )
                    {
                        break;
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[MemberInfo.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;
    }

    /***
     * メンバー情報をDtoクラスにセット
     * 
     * @return
     */
    public DtoApHotelCustomerData getMemberInfo()
    {
        DtoApHotelCustomerData dacd = new DtoApHotelCustomerData();
        // ユーザID
        dacd.setUserId( this.userId );
        // メンバーID
        dacd.setCustomId( this.memberId );
        // パス
        dacd.setPassword( this.password );
        // 名前
        dacd.setName( this.name );
        // 名前カナ
        dacd.setFurigana( this.nameKana );
        // ハンドルネーム
        dacd.setHandleName( this.handleName );
        // 性別
        dacd.setSex( this.sex );
        // メールアドレス
        dacd.setMailAddr( this.mailAddr );
        // メルマガ
        dacd.setMailmag( this.mailMagFlag );
        // 電話1
        dacd.setTel1( this.tel1 );
        // 電話2
        dacd.setTel2( this.tel2 );

        // 誕生日1
        dacd.setBirthday1Year( this.birthYear1 );
        dacd.setBirthday1Month( this.birthMonth1 );
        dacd.setBirthday1Day( this.birthDate1 );

        // 誕生日2
        dacd.setBirthday2Year( this.birthYear2 );
        dacd.setBirthday2Month( this.birthMonth2 );
        dacd.setBirthday2Day( this.birthDate2 );

        // 記念日1
        dacd.setMemorial1Year( this.memorialYear1 );
        dacd.setMemorial1Month( this.memorialMonth1 );
        dacd.setMemorial1Day( this.memorialDate1 );

        // 記念日2
        dacd.setMemorial2Year( this.memorialYear2 );
        dacd.setMemorial2Month( this.memorialMonth2 );
        dacd.setMemorial2Day( this.memorialDate2 );

        // 住所
        dacd.setAddr1( this.addr1 );
        // 住所
        dacd.setAddr2( this.addr2 );

        return dacd;
    }

}
