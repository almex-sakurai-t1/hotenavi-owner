package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.dto.DtoApHotelCustomerData;

/**
 * メンバー情報取得通知クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see リクエスト：1002電文<br>
 *      レスポンス：1003電文
 */
public class MemberInfo implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 5385130148620657030L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1002";
    final String              REPLY_COMMAND    = "1003";
    String                    header;
    // 送信電文
    String                    memberId;
    int                       birthMonth;
    int                       birthDay;
    String                    userId;
    String                    pass;
    String                    reserve;
    boolean                   boolMemberCheckIn;
    DtoApHotelCustomerData    apHotelCustomerData;
    String                    customRank;

    // 受信電文
    int                       result;
    String                    handleName;
    int                       sex;
    String                    rank;
    int                       usage;
    int                       point;
    int                       point2;
    int                       birthday1;
    int                       birthday2;
    int                       birthday1flag;
    int                       birthday2flag;
    int                       memorial1;
    int                       memorial2;
    int                       memorial1flag;
    int                       memorial2flag;
    int                       discountRate;
    String                    mailAddr;
    int                       mailMag;
    String                    carNoLocal;
    String                    carNoClass;
    String                    carNoType;
    String                    carNo;
    int                       mailStatus;
    String                    name;
    String                    nameKana;
    String                    addr1;
    String                    addr2;
    String                    tel1;
    String                    tel2;
    int                       amount;
    int                       lastCome;
    int                       usageAll;
    int                       amountTotal;

    public MemberInfo()
    {
        this.header = "";
        this.memberId = "";
        this.birthMonth = 0;
        this.birthDay = 0;
        this.userId = "";
        this.pass = "";
        this.reserve = "";
        this.result = 0;
        this.handleName = "";
        this.sex = 0;
        this.rank = "";
        this.usage = 0;
        this.point = 0;
        this.point2 = 0;
        this.birthday1 = 0;
        this.birthday2 = 0;
        this.birthday1flag = 0;
        this.birthday2flag = 0;
        this.memorial1 = 0;
        this.memorial2 = 0;
        this.memorial1flag = 0;
        this.memorial2flag = 0;
        this.discountRate = 0;
        this.mailAddr = "";
        this.mailMag = 0;
        this.carNoLocal = "";
        this.carNoClass = "";
        this.carNoType = "";
        this.carNo = "";
        this.mailStatus = 0;
        this.name = "";
        this.nameKana = "";
        this.addr1 = "";
        this.addr2 = "";
        this.tel1 = "";
        this.tel2 = "";
        this.amount = 0;
        this.lastCome = 0;
        this.usageAll = 0;
        this.amountTotal = 0;
        this.boolMemberCheckIn = false;
        this.apHotelCustomerData = null;
        this.customRank = "";
    }

    public String getHeader()
    {
        return header;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public int getBirthMonth()
    {
        return birthMonth;
    }

    public int getBirthDay()
    {
        return birthDay;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getPass()
    {
        return pass;
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

    public String getCustomRank()
    {
        return customRank;
    }

    public int getResult()
    {
        return result;
    }

    public String getHandleName()
    {
        return handleName;
    }

    public int getSex()
    {
        return sex;
    }

    public String getRank()
    {
        return rank;
    }

    public int getUsage()
    {
        return usage;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPoint2()
    {
        return point2;
    }

    public int getBirthday1()
    {
        return birthday1;
    }

    public int getBirthday2()
    {
        return birthday2;
    }

    public int getBirthday1flag()
    {
        return birthday1flag;
    }

    public int getBirthday2flag()
    {
        return birthday2flag;
    }

    public int getMemorial1()
    {
        return memorial1;
    }

    public int getMemorial2()
    {
        return memorial2;
    }

    public int getMemorial1flag()
    {
        return memorial1flag;
    }

    public int getMemorial2flag()
    {
        return memorial2flag;
    }

    public int getDiscountRate()
    {
        return discountRate;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public int getMailMag()
    {
        return mailMag;
    }

    public String getCarNoLocal()
    {
        return carNoLocal;
    }

    public String getCarNoClass()
    {
        return carNoClass;
    }

    public String getCarNoType()
    {
        return carNoType;
    }

    public String getCarNo()
    {
        return carNo;
    }

    public int getMailStatus()
    {
        return mailStatus;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
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

    public int getAmount()
    {
        return amount;
    }

    public int getLastCome()
    {
        return lastCome;
    }

    public int getUsageAll()
    {
        return usageAll;
    }

    public int getAmountTotal()
    {
        return amountTotal;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public void setBirthMonth(int birthMonth)
    {
        this.birthMonth = birthMonth;
    }

    public void setBirthDay(int birthDay)
    {
        this.birthDay = birthDay;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
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

    public void setCustomRank(String customRank)
    {
        this.customRank = customRank;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public void setUsage(int usage)
    {
        this.usage = usage;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPoint2(int point2)
    {
        this.point2 = point2;
    }

    public void setBirthday1(int birthday1)
    {
        this.birthday1 = birthday1;
    }

    public void setBirthday2(int birthday2)
    {
        this.birthday2 = birthday2;
    }

    public void setBirthday1flag(int birthday1flag)
    {
        this.birthday1flag = birthday1flag;
    }

    public void setBirthday2flag(int birthday2flag)
    {
        this.birthday2flag = birthday2flag;
    }

    public void setMemorial1(int memorial1)
    {
        this.memorial1 = memorial1;
    }

    public void setMemorial2(int memorial2)
    {
        this.memorial2 = memorial2;
    }

    public void setMemorial1flag(int memorial1flag)
    {
        this.memorial1flag = memorial1flag;
    }

    public void setMemorial2flag(int memorial2flag)
    {
        this.memorial2flag = memorial2flag;
    }

    public void setDiscountRate(int discountRate)
    {
        this.discountRate = discountRate;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMailMag(int mailMag)
    {
        this.mailMag = mailMag;
    }

    public void setCarNoLocal(String carNoLocal)
    {
        this.carNoLocal = carNoLocal;
    }

    public void setCarNoClass(String carNoClass)
    {
        this.carNoClass = carNoClass;
    }

    public void setCarNoType(String carNoType)
    {
        this.carNoType = carNoType;
    }

    public void setCarNo(String carNo)
    {
        this.carNo = carNo;
    }

    public void setMailStatus(int mailStatus)
    {
        this.mailStatus = mailStatus;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
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

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setLastCome(int lastCome)
    {
        this.lastCome = lastCome;
    }

    public void setUsageAll(int usageAll)
    {
        this.usageAll = usageAll;
    }

    public void setAmountTotal(int amountTotal)
    {
        this.amountTotal = amountTotal;
    }

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
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthMonth ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthDay ), 2 );
                sendData += tcpclient.leftFitFormat( this.userId, 32 );
                sendData += tcpclient.leftFitFormat( this.pass, 8 );
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
                            // メンバID
                            this.memberId = clip.clipWord( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // 誕生月
                            this.birthMonth = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 誕生日
                            this.birthDay = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // ユーザID
                            this.userId = clip.clipWord( charData, nIndex, 32 );
                            nIndex = clip.getNextIndex();

                            // パス
                            this.pass = clip.clipWord( charData, nIndex, 8 );
                            nIndex = clip.getNextIndex();

                            // 結果
                            this.result = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // ハンドルネーム
                            this.handleName = clip.clipWord( charData, nIndex, 20 );
                            nIndex = clip.getNextIndex();

                            // 性別
                            this.sex = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // ランク
                            this.rank = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // 利用回数
                            this.usage = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // ポイント
                            this.point = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // ポイント2
                            this.point2 = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // 誕生日1
                            this.birthday1 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            // 誕生日2
                            this.birthday2 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            // 誕生日1フラグ
                            this.birthday1flag = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 誕生日2フラグ
                            this.birthday2flag = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 記念日1
                            this.memorial1 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            // 記念日2
                            this.memorial2 = clip.clipNum( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            // 記念日1フラグ
                            this.memorial1flag = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 記念日2フラグ
                            this.memorial2flag = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 割引率
                            this.discountRate = clip.clipNum( charData, nIndex, 3 );
                            nIndex = clip.getNextIndex();

                            // メールアドレス
                            this.mailAddr = clip.clipWord( charData, nIndex, 63 );
                            nIndex = clip.getNextIndex();

                            // メルマガ
                            this.mailMag = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 車番地域
                            this.carNoLocal = clip.clipWord( charData, nIndex, 8 );
                            nIndex = clip.getNextIndex();

                            // 車番種別
                            this.carNoClass = clip.clipWord( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 車番車種
                            this.carNoType = clip.clipWord( charData, nIndex, 3 );
                            nIndex = clip.getNextIndex();

                            // 車番車種
                            this.carNo = clip.clipWord( charData, nIndex, 4 );
                            nIndex = clip.getNextIndex();

                            this.mailStatus = clip.clipNum( charData, nIndex, 2 );
                            nIndex = clip.getNextIndex();

                            // 名前
                            this.name = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // 名前
                            this.nameKana = clip.clipWord( charData, nIndex, 20 );
                            nIndex = clip.getNextIndex();

                            // 住所
                            this.addr1 = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // 住所
                            this.addr2 = clip.clipWord( charData, nIndex, 40 );
                            nIndex = clip.getNextIndex();

                            // 電話1
                            this.tel1 = clip.clipWord( charData, nIndex, 15 );
                            nIndex = clip.getNextIndex();

                            // 電話2
                            this.tel2 = clip.clipWord( charData, nIndex, 15 );
                            nIndex = clip.getNextIndex();

                            // 利用金額
                            this.amount = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // 最終来店日
                            this.lastCome = clip.clipNum( charData, nIndex, 8 );
                            nIndex = clip.getNextIndex();

                            // 総利用回数
                            this.usageAll = clip.clipNum( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            // 総利用金額
                            this.amountTotal = clip.clipNum( charData, nIndex, 9 );
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

    public DtoApHotelCustomerData getMemberInfo()
    {
        DtoApHotelCustomerData dacd = new DtoApHotelCustomerData();
        dacd.setUserId( this.userId );
        // メンバーID
        dacd.setCustomId( this.memberId );
        // パス
        dacd.setPassword( this.pass );
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
        dacd.setMailmag( this.mailMag );
        // 電話1
        dacd.setTel1( this.tel1 );
        // 電話2
        dacd.setTel2( this.tel2 );

        dacd.setBirthday1Year( this.birthday1 / 10000 );
        dacd.setBirthday1Month( this.birthday1 / 100 % 100 );
        dacd.setBirthday1Day( this.birthday1 % 100 );

        dacd.setBirthday2Year( this.birthday2 / 10000 );
        dacd.setBirthday2Month( this.birthday2 / 100 % 100 );
        dacd.setBirthday2Day( this.birthday2 % 100 );

        dacd.setMemorial1Year( this.memorial1 / 10000 );
        dacd.setMemorial1Month( this.memorial1 / 100 % 100 );
        dacd.setMemorial1Day( this.memorial1 % 100 );

        dacd.setMemorial2Year( this.memorial2 / 10000 );
        dacd.setMemorial2Month( this.memorial2 / 100 % 100 );
        dacd.setMemorial2Day( this.memorial2 % 100 );

        // 1002電文で追加する
        // 誕生月
        dacd.setBirthMonth( this.birthMonth );
        // 誕生日
        dacd.setBirthDay( this.birthDay );
        // ランク
        dacd.setRank( this.rank );
        // 利用回数
        dacd.setUsage( this.usage );
        // ポイント
        dacd.setPoint( this.point );
        // ポイント2
        dacd.setPoint2( this.point2 );
        // 誕生日1
        dacd.setBirthDay1( this.birthday1 );
        // 誕生日2
        dacd.setBirthDay2( this.birthday2 );
        // 誕生日1フラグ
        dacd.setBirthDay1flag( this.birthday1flag );
        // 誕生日2フラグ
        dacd.setBirthDay2flag( this.birthday2flag );
        // 記念日1
        dacd.setMemorial1( this.memorial1 );
        // 記念日2
        dacd.setMemorial2( this.memorial2 );
        // 記念日1フラグ
        dacd.setMemorial1flag( this.memorial1flag );
        // 記念日2フラグ
        dacd.setMemorial2flag( this.memorial2flag );
        // 割引率
        dacd.setDiscountRate( this.discountRate );
        // 車番地域
        dacd.setCarNoLocal( this.carNoLocal );
        // 車番クラス
        dacd.setCarNoClass( this.carNoClass );
        // 車番
        dacd.setCarNoType( this.carNoType );
        // 車番
        dacd.setCarNo( this.carNo );
        // メールステータス
        dacd.setMailStatus( this.mailStatus );
        // 住所
        dacd.setAddr1( this.addr1 );
        // 住所
        dacd.setAddr2( this.addr2 );
        // 利用金額
        dacd.setAmount( this.amount );
        // 最終来店日
        dacd.setLastCome( this.lastCome );
        // 総利用回数
        dacd.setUsageAll( this.usageAll );
        // 総利用金額
        dacd.setAmountTotal( this.amountTotal );

        return dacd;
    }

    /**
     * メンバー情報取得
     * 
     * @param id
     * @param customId
     * @param birthDay
     * @param frontIp
     */
    public void getMemberInfo(int id, String customId, int birthDay, String frontIp)
    {
        final int TIMEOUT = 10000;
        final int HOTENAVI_PORT_NO = 7023;
        final int RESULT_OK = 1;
        final int RESULT_NG = 2;

        this.setMemberId( customId );
        this.setBirthMonth( birthDay / 100 % 100 );
        this.setBirthDay( birthDay % 100 );
        this.sendToHost( frontIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) );
        if ( this.getResult() == RESULT_OK )
        {
            apHotelCustomerData = this.getMemberInfo();
        }

        customRank = this.getRank();
        point = this.getPoint();
        point2 = this.getPoint2();

        boolMemberCheckIn = true;

    }

}
