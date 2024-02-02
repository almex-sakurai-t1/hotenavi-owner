package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.ClipString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;

/**
 * �����o�[�V�K�o�^
 * 
 * @author S.Tashiro
 * @version 1.00 2014/09/18
 * @see ���N�G�X�g�F1044�d��<br>
 *      ���X�|���X�F1045�d��
 */
public class MemberRegist implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5730588352898876522L;
    final int                 HEADER_LENGTH    = 32;
    final int                 COMMAND_LENGTH   = 4;
    final String              COMMAND          = "1044";
    final String              REPLY_COMMAND    = "1045";
    final int                 HOTENAVI_PORT_NO = 7023;
    final int                 HOTENAVI_TIMEOUT = 3000;
    String                    header;
    // ���M�d��
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

    // ��M�d��
    int                       result;
    String                    securityCode;

    public MemberRegist()
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

        // ���o�f�[�^�̊J�n�ʒu
        nIndex = HEADER_LENGTH + COMMAND_LENGTH;

        // �z�X�g���f�[�^���M
        tcpclient = new TcpClientEx();
        // �w���ip�A�h���X�ɐڑ�
        ret = tcpclient.connectServiceByAddr( frontIp, timeOut, portNo );
        if ( ret != false )
        {
            try
            {
                sendData = COMMAND;
                sendData += tcpclient.leftFitFormat( this.memberId, 9 );
                sendData += tcpclient.leftFitFormat( this.reserve, 140 );

                header = tcpclient.getPacketHeader( hotelId, sendData.getBytes( "Windows-31J" ).length );

                sendData = header + sendData;
                int roop = 0;
                while( true )
                {
                    // �d�����M
                    tcpclient.send( sendData );
                    // ��M�ҋ@
                    recvData = tcpclient.recv();
                    roop++;
                    if ( recvData.indexOf( "exception" ) >= 0 )
                    {
                        Logging.error( "�d����MException " + recvData );
                    }
                    else
                    {
                        charData = new char[recvData.length()];
                        charData = recvData.toCharArray();

                        // �R�}���h�擾
                        data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                        // �����d���R�}���h��1045�Ȃ琳��������
                        if ( data.compareTo( REPLY_COMMAND ) == 0 )
                        {
                            // �Ԃ��Ă��������Z�b�g
                            this.memberId = clip.clipWord( charData, nIndex, 9 );
                            nIndex = clip.getNextIndex();

                            this.securityCode = clip.clipWord( charData, nIndex, 5 );
                            nIndex = clip.getNextIndex();

                            this.result = Integer.parseInt( clip.clipWord( charData, nIndex, 2 ) );
                            nIndex = clip.getNextIndex();

                            this.reserve = clip.clipWord( charData, nIndex, 10 );
                            nIndex = clip.getNextIndex();

                            // �����Ԃ��Đ����Ƃ���
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
                Logging.error( "[MemberRegist.sendToHost()]Exception:" + e.toString() );
            }
            finally
            {
                tcpclient.disconnectService();
            }
        }

        return ret;

    }

    /***
     * SC�����o�[�����񗈓X�^�b�`�����Ƃ��̌ڋq�o�^����
     * 
     * @param accountID �A�J�E���gID
     * @param hotelId �z�e��ID
     * @param userId ���[�U�[ID
     * @return ����
     */
    public boolean makeScHotenaviMember(int accountID, int hotelId, String userId)
    {
        boolean rtn = false;
        DataApHotelCustom dahc = new DataApHotelCustom();
        DataApHotelSetting dahs = new DataApHotelSetting();
        boolean isCustom = false; // �n�s�z�e���̃����o�[�o�^���m�F����
        dahc = new DataApHotelCustom();
        if ( !dahc.getValidData( hotelId, userId ) )
        {
            GroupHotelCustom hotelCustom = new GroupHotelCustom();
            isCustom = hotelCustom.getMutltiCustomData( hotelId, userId );
        }
        else
        {
            isCustom = true;
        }
        String hotenaviIp = HotelIp.getHotenaviIp( hotelId );

        boolean isGoodPrice = false; // �����o�[�Y�J�[�h�ۋ����ǂ���������ׂ�

        MemberCardChargeConfirm mccc = new MemberCardChargeConfirm();
        // (1052)�����o�[�J�[�h���ۋ����ǂ������擾����
        if ( mccc.sendToHost( hotenaviIp, HOTENAVI_TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( hotelId ) ) )
        {
            if ( mccc.getGoodsPrice() != 0 )
                isGoodPrice = true;
        }

        if ( !isCustom && !isGoodPrice )
        {
            boolean isMember = false;
            boolean isMemberCustom = false;
            dahc = new DataApHotelCustom();
            if ( dahc.getScCustomData( hotelId, userId ) )
            {
                isMemberCustom = true; // ap_hotel_custom�ɂ͓o�^����Ă��Ȃ����Ar_member_custom�ɂ͓o�^����Ă���
            }
            else
            // SC���ɓo�^����Ă��Ȃ��̂ŁA�����o�[�ڋq�o�^���s��
            {
                isMemberCustom = false;
                if ( dahc.getScMemberData( userId ) )
                {
                    MemberRegist mr = new MemberRegist();
                    int customId = dahs.getMaxCustomId( hotelId );
                    // �����o�[���o�^�̏ꍇ�Ƀz�X�g�֐V�K�o�^�ڋq�ԍ���ʒm����B
                    // �z�X�g��3�񃊃g���C������B
                    for( int i = 0 ; i < 3 ; i++ )
                    {
                        /** (1044)�����o�[�V�K�o�^ **/
                        mr.setMemberId( Integer.toString( customId ) );
                        mr.sendToHost( hotenaviIp, HOTENAVI_TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( hotelId ) );
                        if ( mr.getResult() == 1 )
                        {
                            isMember = true; // r_member �ɂ͓o�^����Ă��āA�z�X�g�ɓo�^�ł����̂œo�^OK�ɂȂ���
                            break;
                        }
                        // �J��Ԃ��ꍇ�́A�ڋq�ԍ���U��Ȃ����B
                        customId++;
                    }
                    if ( isMember )
                    {
                        dahc.setCustomId( Integer.toString( customId ) );
                        dahc.setId( hotelId );
                        dahc.setUserId( userId );
                        dahc.setSecurityCode( mr.getSecurityCode() );
                        dahc.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dahc.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dahc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dahc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dahc.setRegistStatus( 1 );
                        dahc.setAutoFlag( 1 );
                    }
                }
            }
            if ( isMemberCustom || isMember )
            {
                /** (1046)�z�e�i�r �����o�[���o�^ **/
                MemberOverwrite mo = new MemberOverwrite();
                mo.setMemberId( dahc.getCustomId() );
                mo.setBirthYear1( dahc.getBirthday1() / 10000 );
                mo.setBirthMonth1( dahc.getBirthday1() / 100 % 100 );
                mo.setBirthDate1( dahc.getBirthday1() % 100 );
                mo.setUserId( dahc.getHotelUserId() );
                mo.setPassword( dahc.getHotelPassword() );
                mo.setHandleName( dahc.getNickname() ); // �v����
                mo.setName( dahc.getName() );// �v����
                mo.setNameKana( dahc.getNameKana() );// �v����
                mo.setSex( dahc.getSex() );
                mo.setAddr1( dahc.getAddress1() );
                mo.setAddr2( dahc.getAddress2() );
                mo.setTel1( dahc.getTel1() );// �v����
                mo.setTel2( "" );
                mo.setMailAddr( dahc.getMailAddress() );// �v����
                mo.setMailMagFlag( 0 ); // m_member �� �����}�K�z�M�t���O���K�v�˕ʃ`�P�b�g�ɂ�
                mo.setMemorialYear1( dahc.getMemorial1() / 10000 );
                mo.setMemorialMonth1( dahc.getMemorial1() / 100 % 100 );
                mo.setMemorialDate1( dahc.getMemorial1() % 100 );
                mo.setBirthYear2( dahc.getBirthday2() / 10000 );
                mo.setBirthMonth2( dahc.getBirthday2() / 100 % 100 );
                mo.setBirthDate2( dahc.getBirthday2() % 100 );
                mo.setMemorialYear2( dahc.getMemorial2() / 10000 );
                mo.setMemorialMonth2( dahc.getMemorial2() / 100 % 100 );
                mo.setMemorialDate2( dahc.getMemorial2() % 100 );

                if ( mo.sendToHost( hotenaviIp, HOTENAVI_TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( hotelId ) ) )
                {
                    dahc.setSecurityCode( mo.getSecurityCode() );
                    // �z�e���ւ̌ڋq�o�^�����������̂ŁAap_hotel_custom �ɓo�^
                    if ( dahc.insertData() )
                        rtn = true;
                }
            }
            if ( !isMemberCustom ) // r_member_custom ���o�^����Ă��Ȃ��̂œo�^
            {
                MemberSync msync = new MemberSync();
                msync.syncDataSc( accountID, hotelId, userId );
            }
        }
        return rtn;
    }
}
