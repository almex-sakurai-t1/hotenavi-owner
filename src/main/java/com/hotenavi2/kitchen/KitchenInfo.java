package com.hotenavi2.kitchen;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClientCommon;

/**
 * �L�b�`���T�[�r�X�Ƃ̃L�b�`���[�����֘A�d���ҏW�E����M���s��<br>
 * 
 * �����d�� sendPacket0000(int kind, String value)<br>
 * �����擾�d�� sendPacket0006(int kind, String value, int slipNo, int getCount)<br>
 * �`�[���̃\�[�g SortOrderInfo(byte orderInfoType, byte type)<br>
 * 
 * @author Y.Tanabe
 * @version 2.00 2011/06/30
 */
@SuppressWarnings("serial")
public class KitchenInfo implements Serializable
{
    // ------------------------------------------------------------------------------
    // �萔��`
    // ------------------------------------------------------------------------------
    /** �L�b�`���T�[�r�X�ڑ��|�[�g�ԍ� **/
    public static final int     KITCHENSERVICE_PORTNO  = 6984;
    /** �z�V�����ő匏�� **/
    public static final int     SETTABLE_MAXCOUNT      = 10;
    /** �I�[�_�[�����ő匏�� **/
    public static final int     ORDER_HISTORY_MAXCOUNT = 10;
    /** �����N�����I�[�_�[�ő匏�� **/
    public static final int     INITIAL_ORDER_MAXCOUNT = 128;
    /** �I�[�_�[�ő匏�� **/
    public static final int     ORDER_MAXCOUNT         = 32;

    /** �\�[�g��ʓ`�[�ԍ� **/
    public static final byte    SORTYPE_SLIP           = 0;
    /** �\�[�g��ʎ�t���� **/
    public static final byte    SORTTYPE_ACCEPT        = 1;
    /** �\�[�g��ʔz�V�w����� **/
    public static final byte    SORTTYPE_SETTABLE      = 2;
    /** �\�[�g��ʕ������� **/
    public static final byte    SORTTYPE_ROOM          = 3;
    /** �\�[�g��ʃI�[�_�[���ޖ��� **/
    public static final byte    SORTTYPE_ORDERCLASS    = 4;
    /** �\�[�g��ʏ��i���� **/
    public static final byte    SORTTYPE_GOODS         = 5;
    /** �\�[�g��ʏ��i���� **/
    public static final byte    SORTTYPE_COUNT         = 6;
    /** �\�[�g��ʔz�V�������� **/
    public static final byte    SORTTYPE_SETTABLEFIN   = 7;
    /** �\�[�g��ʏ��i�R�[�h **/
    public static final byte    SORTTYPE_GOODSCODE     = 8;

    /** �����d�����ʃI�[�_�[��� **/
    public static final byte    ORDERINFOTYPE_INITIAL  = 1;
    /** �����I�[�_�[��� **/
    public static final byte    ORDERINFOTYPE_HISTORY  = 2;
    // ------------------------------------------------------------------------------
    // �f�[�^�̈��`
    // ------------------------------------------------------------------------------
    /** (����)�������� **/
    public int                  Result;
    /** (����)�z�e��ID **/
    public String               HotelId;

    /** (�~�[�[���N����������)���ݓ��t(YYYYMMDD) **/
    public int                  InitialNowDate;
    /** (�~�[�[���N����������)���ݗj��(0:sunday�`6:Saturday) **/
    public int                  InitialNowDayofweek;
    /** (�~�[�[���N����������)���ݎ���(HHMM) **/
    public int                  InitialNowTime;
    /** (�~�[�[���N����������)���f�[�^�t���O **/
    public int                  InitialNextDataFlag;
    /** (�~�[�[���N����������)�I�[�_�[���� **/
    public int                  InitialOrderCount;
    /** (�~�[�[���N����������)�`�[�ԍ� **/
    public int                  InitialSlipNo[];
    /** (�~�[�[���N����������)��t���t(YYYYMMDD) **/
    public int                  InitialAcceptDate[];
    /** (�~�[�[���N����������)��t����(HHMM) **/
    public int                  InitialAcceptTime[];
    /** (�~�[�[���N����������)�z�V���t(YYYYMMDD) **/
    public int                  InitialSettableDate[];
    /** (�~�[�[���N����������)�z�V����(HHMM) **/
    public int                  InitialSettableTime[];
    /** (�~�[�[���N����������)�����R�[�h **/
    public int                  InitialRoomCode[];
    /** (�~�[�[���N����������)�������� **/
    public String               InitialRoomName[];
    /** (�~�[�[���N����������)�I�[�_�[���ރR�[�h **/
    public int                  InitialOrderClassCode[];
    /** (�~�[�[���N����������)�I�[�_�[���ޖ��� **/
    public String               InitialOrderClassName[];
    /** (�~�[�[���N����������)���i�R�[�h **/
    public int                  InitialGoodsCode[];
    /** (�~�[�[���N����������)���i���� **/
    public String               InitialGoodsName[];
    /** (�~�[�[���N����������)���� **/
    public int                  InitialGoodsCount[];

    /** (�z�V����)���ݓ��t(YYYYMMDD) **/
    public int                  HistoryNowDate;
    /** (�z�V����)���ݗj��(0:sunday�`6:Saturday) **/
    public int                  HistoryNowDayofweek;
    /** (�z�V����)���ݎ���(HHMM) **/
    public int                  HistoryNowTime;
    /** (�z�V����)���y�[�W�`�[�ԍ� 0:���y�[�W�Ȃ� **/
    public int                  HistoryNextPageSlipNo;
    /** (�z�V����)�I�[�_�[���� **/
    public int                  HistoryOrderCount;
    /** (�z�V����)�`�[�ԍ� **/
    public int                  HistorySlipNo[];
    /** (�z�V����)��t���t **/
    public int                  HistoryAcceptDate[];
    /** (�z�V����)��t���� **/
    public int                  HistoryAcceptTime[];
    /** (�z�V����)�z�V�w����t **/
    public int                  HistorySettableDate[];
    /** (�z�V����)�z�V�w�莞�� **/
    public int                  HistorySettableTime[];
    /** (�z�V����)�����R�[�h **/
    public int                  HistoryRoomCode[];
    /** (�z�V����)�������� **/
    public String               HistoryRoomName[];
    /** (�z�V����)�I�[�_�[���ރR�[�h **/
    public int                  HistoryOrderClassCode[];
    /** (�z�V����)�I�[�_�[���ޖ��� **/
    public String               HistoryOrderClassName[];
    /** (�z�V����)���i�R�[�h **/
    public int                  HistoryGoodsCode[];
    /** (�z�V����)���i���� **/
    public String               HistoryGoodsName[];
    /** (�z�V����)���� **/
    public int                  HistoryGoodsCount[];
    /** (�z�V����)�z�V�������t **/
    public int                  HistorySettableFinDate[];
    /** (�z�V����)�z�V�������� **/
    public int                  HistorySettableFinTime[];

    /** (�~�[�[���N����������)�I�[�_�[��� **/
    public ArrayList<OrderInfo> InitialOrderList;
    /** (�z�V����)�I�[�_�[��� **/
    public ArrayList<OrderInfo> HistoryOrderList;

    /** ���O�o�̓��C�u���� **/
    private LogLib              log;

    /**
     * �L�b�`���֘A���f�[�^�̏��������s���܂��B
     * 
     */
    public KitchenInfo()
    {
        HotelId = "";

        InitialNowDate = 0;
        InitialNowDayofweek = 0;
        InitialNowTime = 0;
        InitialNextDataFlag = 0;
        InitialOrderCount = 0;
        InitialSlipNo = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCount = new int[INITIAL_ORDER_MAXCOUNT];

        InitialOrderList = new ArrayList<OrderInfo>();

        HistoryNowDate = 0;
        HistoryNowDayofweek = 0;
        HistoryNowTime = 0;
        HistoryNextPageSlipNo = 0;
        HistoryOrderCount = 0;
        HistorySlipNo = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCount = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinTime = new int[ORDER_HISTORY_MAXCOUNT];

        HistoryOrderList = new ArrayList<OrderInfo>();

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // �d���������\�b�h
    //
    // ------------------------------------------------------------------------------
    /**
     * �d�����M����(0000)
     * �~�[�[���N�����������d��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0000()
    {
        return(sendPacket0000Sub( TcpClientCommon.TCPCONNECTTYPE_COMMONHOTELID, "" ));
    }

    /**
     * �d�����M����(0000)
     * �~�[�[���N�����������d��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0000(int kind, String value)
    {
        return(sendPacket0000Sub( kind, value ));
    }

    /**
     * �d�����M����(0000)
     * �~�[�[���N�����������d��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0000Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClientCommon tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        InitialNowDate = 0;
        InitialNowDayofweek = 0;
        InitialNowTime = 0;
        InitialNextDataFlag = 0;
        InitialOrderCount = 0;
        InitialSlipNo = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialAcceptTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableDate = new int[INITIAL_ORDER_MAXCOUNT];
        InitialSettableTime = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialRoomName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialOrderClassName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCode = new int[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsName = new String[INITIAL_ORDER_MAXCOUNT];
        InitialGoodsCount = new int[INITIAL_ORDER_MAXCOUNT];

        InitialOrderList = new ArrayList<OrderInfo>();

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClientCommon( KITCHENSERVICE_PORTNO );
            blnRet = tcpClient.connect( kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0000";

                // �d���w�b�_�̎擾(�[��ID�͎g��Ȃ��̂�0���Z�b�g)
                strHeader = tcpClient.getPacketHeader( TcpClientCommon.CONNECTAPTYPE_KITCHEN, "0", strSend.length() );
                // �d���̌���
                strSend = strHeader + strSend;

                try
                {
                    // �d�����M
                    tcpClient.send( strSend );

                    // �d�����M���M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0001" ) == 0 )
                        {
                            // ���ݓ��t
                            strData = new String( cRecv, 36, 8 );
                            InitialNowDate = Integer.valueOf( strData ).intValue();

                            // ���ݗj��
                            strData = new String( cRecv, 44, 2 );
                            InitialNowDayofweek = Integer.valueOf( strData ).intValue();

                            // ���ݎ���
                            strData = new String( cRecv, 46, 4 );
                            InitialNowTime = Integer.valueOf( strData ).intValue();

                            // ���f�[�^�t���O
                            strData = new String( cRecv, 50, 1 );
                            InitialNextDataFlag = Integer.valueOf( strData ).intValue();

                            // �I�[�_�[����
                            strData = new String( cRecv, 51, 3 );
                            InitialOrderCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < InitialOrderCount ; i++ )
                            {
                                OrderInfo item = new OrderInfo();
                                // �`�[�ԍ�
                                strData = new String( cRecv, 54 + 134 * i, 8 );
                                InitialSlipNo[i] = Integer.valueOf( strData ).intValue();
                                item.SlipNo = InitialSlipNo[i];
                                // ��t���t
                                strData = new String( cRecv, 62 + 134 * i, 8 );
                                InitialAcceptDate[i] = Integer.valueOf( strData ).intValue();
                                // ��t����
                                strData = new String( cRecv, 70 + 134 * i, 4 );
                                InitialAcceptTime[i] = Integer.valueOf( strData ).intValue();
                                item.AcceptDatetime = String.format( "%08d", InitialAcceptDate[i] ) + String.format( "%04d", InitialAcceptTime[i] );
                                // �z�V�w����t
                                strData = new String( cRecv, 74 + 134 * i, 8 );
                                InitialSettableDate[i] = Integer.valueOf( strData ).intValue();
                                // �z�V�w�莞��
                                strData = new String( cRecv, 82 + 134 * i, 4 );
                                InitialSettableTime[i] = Integer.valueOf( strData ).intValue();
                                item.SettableDatetime = String.format( "%08d", InitialSettableDate[i] ) + String.format( "%04d", InitialSettableTime[i] );
                                // �����R�[�h
                                strData = new String( cRecv, 86 + 134 * i, 3 );
                                InitialRoomCode[i] = Integer.valueOf( strData ).intValue();
                                item.RoomCode = InitialRoomCode[i];
                                // ��������
                                strData = new String( cRecv, 89 + 134 * i, 8 );
                                InitialRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                item.RoomName = InitialRoomName[i];
                                // �I�[�_�[���ރR�[�h
                                strData = new String( cRecv, 97 + 134 * i, 4 );
                                InitialOrderClassCode[i] = Integer.valueOf( strData ).intValue();
                                item.OrderClassCode = InitialOrderClassCode[i];
                                // �I�[�_�[���ޖ���
                                strData = new String( cRecv, 101 + 134 * i, 40 );
                                InitialOrderClassName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                item.OrderClassName = InitialOrderClassName[i];
                                // ���i�R�[�h
                                strData = new String( cRecv, 141 + 134 * i, 4 );
                                InitialGoodsCode[i] = Integer.valueOf( strData ).intValue();
                                item.GoodsCode = InitialGoodsCode[i];
                                // ���i����
                                strData = new String( cRecv, 145 + 134 * i, 40 );
                                InitialGoodsName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                item.GoodsName = InitialGoodsName[i];
                                // ����
                                strData = new String( cRecv, 185 + 134 * i, 3 );
                                InitialGoodsCount[i] = Integer.valueOf( strData ).intValue();
                                item.GoodsCount = InitialGoodsCount[i];
                                InitialOrderList.add( item );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendKitchenPacket0000:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0006)
     * �z�V����d��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0006()
    {
        return(sendPacket0006Sub( TcpClientCommon.TCPCONNECTTYPE_COMMONHOTELID, "", 0, 10 ));
    }

    /**
     * �d�����M����(0006)
     * �z�V����d��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @param slipNo �`�[�ԍ�
     * @param getCount �擾�v������
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0006(int kind, String value, int slipNo, int getCount)
    {
        return(sendPacket0006Sub( kind, value, slipNo, getCount ));
    }

    /**
     * �d�����M����(0006)
     * �z�V����d��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @param slipNo �`�[�ԍ�
     * @param getCount �擾�v������
     * @return ��������
     */
    private boolean sendPacket0006Sub(int kind, String value, int slipNo, int getCount)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClientCommon tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        HistoryNowDate = 0;
        HistoryNowDayofweek = 0;
        HistoryNowTime = 0;
        HistoryNextPageSlipNo = 0;
        HistoryOrderCount = 0;
        HistorySlipNo = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryAcceptTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableTime = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryRoomName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryOrderClassName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCode = new int[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsName = new String[ORDER_HISTORY_MAXCOUNT];
        HistoryGoodsCount = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinDate = new int[ORDER_HISTORY_MAXCOUNT];
        HistorySettableFinTime = new int[ORDER_HISTORY_MAXCOUNT];

        HistoryOrderList = new ArrayList<OrderInfo>();

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClientCommon( KITCHENSERVICE_PORTNO );
            blnRet = tcpClient.connect( kind, value );

            if ( blnRet != false )
            {
                int roopCount = 0;
                while( true )
                {

                    format = new StringFormat();

                    // �R�}���h
                    strSend = "0006";
                    // �`�[�ԍ�
                    nf = new DecimalFormat( "00000000" );
                    strData = nf.format( Integer.valueOf( slipNo ).intValue() );
                    strSend = strSend + strData;

                    // �d���w�b�_�̎擾(�[��ID�͎g��Ȃ��̂�0���Z�b�g)
                    strHeader = tcpClient.getPacketHeader( TcpClientCommon.CONNECTAPTYPE_KITCHEN, "0", strSend.length() );
                    // �d���̌���
                    strSend = strHeader + strSend;

                    try
                    {
                        // �d�����M
                        tcpClient.send( strSend );

                        // �d�����M���M�ҋ@
                        strRecv = tcpClient.recv();
                        if ( strRecv != null )
                        {
                            cRecv = new char[strRecv.length()];
                            cRecv = strRecv.toCharArray();

                            // �R�}���h�擾
                            strData = new String( cRecv, 32, 4 );
                            if ( strData.compareTo( "0007" ) == 0 )
                            {
                                // ���ݓ��t
                                strData = new String( cRecv, 36, 8 );
                                HistoryNowDate = Integer.valueOf( strData ).intValue();

                                // ���ݗj��
                                strData = new String( cRecv, 44, 2 );
                                HistoryNowDayofweek = Integer.valueOf( strData ).intValue();

                                // ���ݎ���
                                strData = new String( cRecv, 46, 4 );
                                HistoryNowTime = Integer.valueOf( strData ).intValue();

                                // ���y�[�W�`�[�ԍ�
                                strData = new String( cRecv, 50, 8 );
                                HistoryNextPageSlipNo = Integer.valueOf( strData ).intValue();

                                // �I�[�_�[����
                                strData = new String( cRecv, 58, 3 );
                                HistoryOrderCount = Integer.valueOf( strData ).intValue();

                                for( i = 0 ; i < HistoryOrderCount ; i++ )
                                {
                                    // �v�������𒴂����珈���I��
                                    if ( roopCount * ORDER_HISTORY_MAXCOUNT + i >= getCount )
                                    {
                                        break;
                                    }
                                    OrderInfo item = new OrderInfo();
                                    // �`�[�ԍ�
                                    strData = new String( cRecv, 61 + 146 * i, 8 );
                                    HistorySlipNo[i] = Integer.valueOf( strData ).intValue();
                                    item.SlipNo = HistorySlipNo[i];
                                    // ��t���t
                                    strData = new String( cRecv, 69 + 146 * i, 8 );
                                    HistoryAcceptDate[i] = Integer.valueOf( strData ).intValue();
                                    // ��t����
                                    strData = new String( cRecv, 77 + 146 * i, 4 );
                                    HistoryAcceptTime[i] = Integer.valueOf( strData ).intValue();
                                    item.AcceptDatetime = String.format( "%08d", HistoryAcceptDate[i] ) + String.format( "%04d", HistoryAcceptTime[i] );
                                    // �z�V�w����t
                                    strData = new String( cRecv, 81 + 146 * i, 8 );
                                    HistorySettableDate[i] = Integer.valueOf( strData ).intValue();
                                    // �z�V�w�莞��
                                    strData = new String( cRecv, 89 + 146 * i, 4 );
                                    HistorySettableTime[i] = Integer.valueOf( strData ).intValue();
                                    item.SettableDatetime = String.format( "%08d", HistorySettableDate[i] ) + String.format( "%04d", HistorySettableTime[i] );
                                    // �����R�[�h
                                    strData = new String( cRecv, 93 + 146 * i, 3 );
                                    HistoryRoomCode[i] = Integer.valueOf( strData ).intValue();
                                    item.RoomCode = HistoryRoomCode[i];
                                    // ��������
                                    strData = new String( cRecv, 96 + 146 * i, 8 );
                                    HistoryRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    item.RoomName = HistoryRoomName[i];
                                    // �I�[�_�[���ރR�[�h
                                    strData = new String( cRecv, 104 + 146 * i, 4 );
                                    HistoryOrderClassCode[i] = Integer.valueOf( strData ).intValue();
                                    item.OrderClassCode = HistoryOrderClassCode[i];
                                    // �I�[�_�[���ޖ���
                                    strData = new String( cRecv, 108 + 146 * i, 40 );
                                    HistoryOrderClassName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    item.OrderClassName = HistoryOrderClassName[i];
                                    // ���i�R�[�h
                                    strData = new String( cRecv, 148 + 146 * i, 4 );
                                    HistoryGoodsCode[i] = Integer.valueOf( strData ).intValue();
                                    item.GoodsCode = HistoryGoodsCode[i];
                                    // ���i����
                                    strData = new String( cRecv, 152 + 146 * i, 40 );
                                    HistoryGoodsName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    item.GoodsName = HistoryGoodsName[i];
                                    // ����
                                    strData = new String( cRecv, 192 + 146 * i, 3 );
                                    HistoryGoodsCount[i] = Integer.valueOf( strData ).intValue();
                                    item.GoodsCount = HistoryGoodsCount[i];
                                    // �z�V�������t
                                    strData = new String( cRecv, 195 + 146 * i, 8 );
                                    HistorySettableFinDate[i] = Integer.valueOf( strData ).intValue();
                                    // �z�V��������
                                    strData = new String( cRecv, 203 + 146 * i, 4 );
                                    HistorySettableFinTime[i] = Integer.valueOf( strData ).intValue();
                                    item.SettableFinDatetime = String.format( "%08d", HistorySettableFinDate[i] ) + String.format( "%04d", HistorySettableFinTime[i] );

                                    HistoryOrderList.add( item );

                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        log.error( "sendKitchenPacket0006:" + e.toString() );
                        return(false);
                    }
                    // ���y�[�W�Ȃ� ���͎擾�v�������𒴂�����I��
                    if ( HistoryNextPageSlipNo <= 0 || roopCount * ORDER_HISTORY_MAXCOUNT + HistoryOrderCount >= getCount || roopCount > 1000 )
                    {
                        break;
                    }
                    slipNo = HistoryNextPageSlipNo;
                    roopCount++;
                }
                tcpClient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �I�[�_�[���\�[�g����
     * 
     * @param orderInfoType �I�[�_�[����ʁ@ORDERINFOTYPE_XXXX�Œ萔��p�ӂ��Ă��܂�
     * @param type �\�[�g��ʁ@SORTTYPE_XXXX�Œ萔��p�ӂ��Ă��܂�
     * 
     */
    @SuppressWarnings("unchecked")
    public void SortOrderInfo(byte orderInfoType, byte type)
    {
        ArrayList<OrderInfo> list = null;

        try
        {
            switch( orderInfoType )
            {
                case ORDERINFOTYPE_INITIAL:
                    list = InitialOrderList;
                    break;
                case ORDERINFOTYPE_HISTORY:
                    list = HistoryOrderList;
                    break;
                default:
                    break;
            }

            if ( list != null )
            {
                switch( type )
                {
                    case SORTYPE_SLIP:
                        Collections.sort( list, new SlipComparator() );
                        break;
                    case SORTTYPE_ACCEPT:
                        Collections.sort( list, new AcceptComparator() );
                        break;
                    case SORTTYPE_SETTABLE:
                        Collections.sort( list, new SettableComparator() );
                        break;
                    case SORTTYPE_ROOM:
                        Collections.sort( list, new RoomComparator() );
                        break;
                    case SORTTYPE_ORDERCLASS:
                        Collections.sort( list, new OrderClassComparator() );
                        break;
                    case SORTTYPE_GOODS:
                        Collections.sort( list, new GoodsComparator() );
                        break;
                    case SORTTYPE_COUNT:
                        Collections.sort( list, new CountComparator() );
                        break;
                    case SORTTYPE_SETTABLEFIN:
                        Collections.sort( list, new SettableFinComparator() );
                        break;
                    case SORTTYPE_GOODSCODE:
                        Collections.sort( list, new GoodsCodeComparator() );
                        break;
                    default:
                        break;
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "SortOrderInfo:" + e.toString() );
        }

    }

}
