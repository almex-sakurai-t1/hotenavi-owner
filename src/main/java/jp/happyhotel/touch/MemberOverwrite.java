﻿package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;

/**
 * メンバー登録
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see リクエスト：1046電文<br>
 *      レスポンス：1047電文
 */
public class MemberOverwrite implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3852165715873047806L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1046";
    final String              REPLY_COMMAND    = "1047";
    String                    header;
    // 送信電文
    String                    memberId;
    int                       birthYear1;
    int                       birthMonth1;
    int                       birthDate1;
    String                    userId;
    String                    password;
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

    // 受信電文
    int                       result;
    String                    securityCode;

    public MemberOverwrite()
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
        this.securityCode = "";
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

    public int getResult()
    {
        return result;
    }

    public String getSecurityCode()
    {
        return securityCode;
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

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
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
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.rightFitZeroFormat( this.memberId, 9 );
                // 誕生日1
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthYear1 ), 4 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthMonth1 ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthDate1 ), 2 );
                // ユーザID
                sendData += tcpclient.leftFitFormat( this.userId, 32 );
                // パスワード
                sendData += tcpclient.leftFitFormat( this.password, 8 );
                // ニックネーム
                sendData += tcpclient.leftFitFormat( this.handleName, 20 );
                // 名前
                sendData += tcpclient.leftFitFormat( this.name, 40 );
                // 名前カナ
                sendData += tcpclient.leftFitFormat( this.nameKana, 20 );
                // 性別
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.sex ), 2 );
                // 住所1
                sendData += tcpclient.leftFitFormat( this.addr1, 40 );
                // 住所2
                sendData += tcpclient.leftFitFormat( this.addr2, 40 );
                // 電話1
                sendData += tcpclient.leftFitFormat( this.tel1, 15 );
                // 電話2
                sendData += tcpclient.leftFitFormat( this.tel2, 15 );
                // メールアドレス
                sendData += tcpclient.leftFitFormat( this.mailAddr, 63 );
                // メルマガフラグ
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.mailMagFlag ), 2 );
                // 記念日1
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.memorialYear1 ), 4 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.memorialMonth1 ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.memorialDate1 ), 2 );
                // 誕生日2
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthYear2 ), 4 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthMonth2 ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.birthDate2 ), 2 );
                // 記念日2
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.memorialYear2 ), 4 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.memorialMonth2 ), 2 );
                sendData += tcpclient.rightFitZeroFormat( Integer.toString( this.memorialDate2 ), 2 );
                sendData += tcpclient.leftFitFormat( this.reserve, 140 );
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
                        Logging.error( "電文受信Exception " + recvData, "memberOverWrite" );
                    }
                    else
                    {
                        charData = new char[recvData.length()];
                        charData = recvData.toCharArray();

                        // コマンド取得
                        data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                        // 応答電文コマンドが1047なら正しい応答
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // 返ってきた情報をセット
                            this.memberId = clip.clipWord( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            this.securityCode = clip.clipWord( charData, nIndex, 5 );
                            nIndex = clip.getNextIndex();

                            this.result = Integer.parseInt( clip.clipWord( charData, nIndex, 2 ) );
                            nIndex = clip.getNextIndex();

                            this.reserve = clip.clipWord( charData, nIndex, 10 );
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
                Logging.error( "[MemberOverwrite.sendToHost()]Exception:" + e.toString(), "memberOverWrite" );
                ret = false;
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }
        return ret;
    }
}
