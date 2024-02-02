/*
 * @(#)CustomInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * �ڋq���֘A�ʐMAP�N���X
 */

package com.hotenavi2.discount;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEB�T�[�r�X�Ƃ̃N�[�|�����֘A�d���ҏW�E����M���s���B
 * 
 * @author S.Tashiro
 * @version 2.00 2014/09/22
 */
public class DiscountInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID      = -9152132824736895499L;

    // ------------------------------------------------------------------------------
    // �萔��`
    // ------------------------------------------------------------------------------
    /** �������ő吔 **/
    public static final int   DISCOUNT_INFO_MAX     = 400;

    /** �����v�����ő吔 **/
    public static final int   DISCOUNT_INFO_PLANMAX = 20;
    /** �������[�h�ő吔 **/
    public static final int   DISCOUNT_INFO_MODEMAX = 20;

    // ------------------------------------------------------------------------------
    // �f�[�^�̈��`
    // ------------------------------------------------------------------------------
    /** (����)�������� **/
    public int                Result;
    /** (����)�z�e��ID **/
    public String             HotelId;

    /** (����)�����R�[�h **/
    public int                DiscountCode;
    /** (����)�������� **/
    public String             DiscountName;

    /** (����)�t�я��1�R�[�h **/
    public int[]              SupplementaryCode1;
    /** (����)�t�я��1���� **/
    public String[]           SupplementaryName1;
    /** (����)�t�я��2�R�[�h **/
    public int[]              SupplementaryCode2;
    /** (����)�t�я��2���� **/
    public String[]           SupplementaryName2;

    /** (�������擾)�ݒ�f�[�^�� **/
    public int                DataLength;
    /** (�������擾)�������z **/
    public int[]              Discount;
    /** (�������擾)������ **/
    public int[]              DiscountRate;

    /** (�t�ъ������擾)�����f�[�^ **/
    public int                DiscountData;
    /** (�t�ъ������擾)�����R�[�h **/
    public int[]              DiscountCodeList;
    /** (�t�ъ������擾)�����f���� **/
    public String[]           DiscountNameList;
    /** (�t�ъ������擾)�t�я��1�f�[�^�� **/
    public int                SupplementaryData1;
    /** (�t�ъ������擾)�t�я��2�f�[�^�� **/
    public int                SupplementaryData2;

    /** (�������ю擾)�J�n�� **/
    public int                StartDate;
    /** (�������ю擾)�I���� **/
    public int                EndDate;

    /** (�������ю擾)�������уf�[�^�� **/
    public int                DiscountResultData;
    /** (�������ю擾)���t **/
    public int[]              CollectDate;
    /** (�������ю擾)�������� **/
    public int[]              DiscountAll;
    /** (�������ю擾)�������� **/
    public int[]              DiscountReal;
    /** (�������ю擾)�������z **/
    public int[]              DiscountTotal;
    /** (�������ю擾)�������z **/
    public int[]              DiscountResult;

    /** �f�o�b�O���O **/
    public LogLib             log;

    /**
     * �ڋq���f�[�^�̏��������s���܂��B
     * 
     */
    public DiscountInfo()
    {
        Result = 0;
        HotelId = "";
        DiscountCode = 0;
        DiscountName = "";
        DataLength = 0;
        DiscountData = 0;
        SupplementaryData1 = 0;
        SupplementaryData2 = 0;

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // �d������
    //
    // ------------------------------------------------------------------------------
    /**
     * �d�����M����(0500)
     * �������擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0500()
    {
        return(sendPacket0500Sub( 0, "" ));
    }

    /**
     * �d�����M����(0500)
     * �������擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0500(int kind, String value)
    {
        return(sendPacket0500Sub( kind, value ));
    }

    /**
     * �������擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0500Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        // �f�[�^�̃N���A
        Result = 0;
        SupplementaryCode1 = new int[DISCOUNT_INFO_MAX];
        SupplementaryCode2 = new int[DISCOUNT_INFO_MAX];
        Discount = new int[DISCOUNT_INFO_MAX];
        DiscountRate = new int[DISCOUNT_INFO_MAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0500";
                // �����R�[�h
                nf = new DecimalFormat( "0000" );
                strData = Integer.toString( DiscountCode );

                strSend = strSend + format.leftFitFormat( strData, 4 );
                strSend += format.leftFitFormat( "", 10 );

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // �d���̌���
                strSend = strHeader + strSend;

                try
                {
                    // �d�����M
                    tcpClient.send( strSend );

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0501" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            strData = new String( cRecv, 38, 4 );
                            DiscountCode = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 42, 20 );
                            DiscountName = strData.trim();

                            strData = new String( cRecv, 62, 2 );
                            count = Integer.valueOf( strData ).intValue();
                            DataLength = count;

                            for( i = 0 ; i < count ; i++ )
                            {
                                // �t�я��1�R�[�h
                                strData = new String( cRecv, 65 + (i * 12), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode1[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �t�я��2�R�[�h
                                strData = new String( cRecv, 67 + (i * 12), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode2[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �������z
                                strData = new String( cRecv, 69 + (i * 12), 5 );
                                if ( strData != null )
                                {
                                    Discount[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �������z
                                strData = new String( cRecv, 74 + (i * 12), 5 );
                                if ( strData != null )
                                {
                                    DiscountRate[i] = Integer.valueOf( strData ).intValue();
                                }

                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0500:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    /**
     * �d�����M����(0502)
     * �������o�^�v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0502()
    {
        return(sendPacket0502Sub( 0, "" ));
    }

    /**
     * �d�����M����(0502)
     * �������o�^�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0502(int kind, String value)
    {
        return(sendPacket0502Sub( kind, value ));
    }

    /**
     * �������o�^�v��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0502Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        // �f�[�^�̃N���A
        Result = 0;
        SupplementaryCode1 = new int[DISCOUNT_INFO_MAX];
        SupplementaryCode2 = new int[DISCOUNT_INFO_MAX];
        Discount = new int[DISCOUNT_INFO_MAX];
        DiscountRate = new int[DISCOUNT_INFO_MAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0502";
                // �����R�[�h
                nf = new DecimalFormat( "0000" );
                // �����R�[�h
                strData = Integer.toString( DiscountCode );
                strSend += format.leftFitFormat( strData, 4 );
                // ��������
                strSend += format.leftFitFormat( DiscountName, 20 );

                // �ݒ�f�[�^��
                nf = new DecimalFormat( "000" );
                strSend += nf.format( Integer.valueOf( DataLength ).intValue() );

                for( i = 0 ; i < DataLength ; i++ )
                {
                    nf = new DecimalFormat( "00" );
                    // �t�я��1���Z�b�g
                    strSend += nf.format( SupplementaryCode1[i] );

                    // �t�я��2���Z�b�g
                    strSend += nf.format( SupplementaryCode2[i] );

                    // �������z���Z�b�g
                    nf = new DecimalFormat( "00000" );
                    strSend += nf.format( Discount[i] );

                    // ���������Z�b�g
                    nf = new DecimalFormat( "000" );
                    strSend += nf.format( DiscountRate );

                }

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // �d���̌���
                strSend = strHeader + strSend;

                try
                {
                    // �d�����M
                    tcpClient.send( strSend );

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0503" ) == 0 )
                        {

                            // �����R�[�h
                            strData = new String( cRecv, 36, 4 );
                            DiscountCode = Integer.valueOf( strData ).intValue();

                            // �������ʎ擾
                            strData = new String( cRecv, 40, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0502:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    /**
     * �d�����M����(0504)
     * �t�ъ������擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0504()
    {
        return(sendPacket0504Sub( 0, "" ));
    }

    /**
     * �d�����M����(0504)
     * �t�ъ������擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0504(int kind, String value)
    {
        return(sendPacket0504Sub( kind, value ));
    }

    /**
     * �t�ъ������擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0504Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        int addIndex = 0;

        // �f�[�^�̃N���A
        Result = 0;
        DiscountCodeList = new int[DISCOUNT_INFO_MAX];
        DiscountNameList = new String[DISCOUNT_INFO_MAX];
        SupplementaryCode1 = new int[DISCOUNT_INFO_PLANMAX];
        SupplementaryName1 = new String[DISCOUNT_INFO_PLANMAX];
        SupplementaryCode2 = new int[DISCOUNT_INFO_PLANMAX];
        SupplementaryName2 = new String[DISCOUNT_INFO_PLANMAX];
        Discount = new int[DISCOUNT_INFO_MAX];
        DiscountRate = new int[DISCOUNT_INFO_MAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0504";
                strSend += format.leftFitFormat( "", 10 );

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // �d���̌���
                strSend = strHeader + strSend;

                try
                {
                    // �d�����M
                    tcpClient.send( strSend );

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0505" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            strData = new String( cRecv, 38, 4 );
                            DiscountData = Integer.valueOf( strData ).intValue();

                            // ������
                            DiscountCodeList = new int[DiscountData];
                            DiscountNameList = new String[DiscountData];

                            for( i = 0 ; i < DiscountData ; i++ )
                            {
                                strData = new String( cRecv, 40 + i * 24, 4 );
                                DiscountCodeList[i] = Integer.valueOf( strData ).intValue();

                                strData = new String( cRecv, 44 + i * 24, 20 );
                                DiscountNameList[i] = strData.trim();

                                // �J��Ԃ����������擾�ʒu�����炷�B
                                addIndex += 24;
                            }

                            // �t�я��1�f�[�^��
                            strData = new String( cRecv, 64 + addIndex, 2 );
                            count = Integer.valueOf( strData ).intValue();
                            SupplementaryData1 = count;

                            // ������
                            SupplementaryCode1 = new int[SupplementaryData1];
                            SupplementaryName1 = new String[SupplementaryData1];
                            for( i = 0 ; i < SupplementaryData1 ; i++ )
                            {
                                // �t�я��1�R�[�h
                                strData = new String( cRecv, 66 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode1[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �t�я��1����
                                strData = new String( cRecv, 70 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryName1[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                                // �J��Ԃ����������擾�ʒu�����炷�B
                                addIndex += 24;
                            }

                            // �t�я��2�f�[�^��
                            strData = new String( cRecv, 90 + addIndex, 2 );
                            count = Integer.valueOf( strData ).intValue();
                            SupplementaryData2 = count;

                            // ������
                            SupplementaryCode2 = new int[SupplementaryData2];
                            SupplementaryName2 = new String[SupplementaryData2];
                            for( i = 0 ; i < SupplementaryData2 ; i++ )
                            {
                                // �t�я��2�R�[�h
                                strData = new String( cRecv, 92 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryCode2[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �t�я��2����
                                strData = new String( cRecv, 96 + (i * 52), 2 );
                                if ( strData != null )
                                {
                                    SupplementaryName2[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                            }

                        }

                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0504:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    /**
     * �d�����M����(0506)
     * �������ю擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0506()
    {
        return(sendPacket0504Sub( 0, "" ));
    }

    /**
     * �d�����M����(0506)
     * �������ю擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0506(int kind, String value)
    {
        return(sendPacket0506Sub( kind, value ));
    }

    /**
     * �t�ъ������擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0506Sub(int kind, String value)
    {
        boolean blnRet;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;
        int count = 0;
        int i = 0;
        int addIndex = 0;

        // �f�[�^�̃N���A
        Result = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0506";
                nf = new DecimalFormat( "00000000" );
                strSend += nf.format( StartDate );
                strSend += nf.format( EndDate );

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
                // �d���̌���
                strSend = strHeader + strSend;

                try
                {
                    // �d�����M
                    tcpClient.send( strSend );

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "0507" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            strData = new String( cRecv, 38, 3 );
                            DiscountResultData = Integer.valueOf( strData ).intValue();

                            // ������
                            CollectDate = new int[DiscountResultData];
                            DiscountAll = new int[DiscountResultData];
                            DiscountReal = new int[DiscountResultData];
                            DiscountTotal = new int[DiscountResultData];
                            DiscountResult = new int[DiscountResultData];

                            for( i = 0 ; i < DiscountData ; i++ )
                            {
                                // ���t
                                strData = new String( cRecv, 41 + i * 40, 8 );
                                CollectDate[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 49 + i * 40, 8 );
                                DiscountAll[i] = Integer.valueOf( strData ).intValue();

                                // Web������
                                strData = new String( cRecv, 57 + i * 40, 8 );
                                DiscountReal[i] = Integer.valueOf( strData ).intValue();

                                // �������z
                                strData = new String( cRecv, 65 + i * 40, 8 );
                                DiscountTotal[i] = Integer.valueOf( strData ).intValue();

                                // Web�����z
                                strData = new String( cRecv, 65 + i * 40, 8 );
                                DiscountResult[i] = Integer.valueOf( strData ).intValue();
                            }
                        }

                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0504:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }

            return(false);
        }

        return(false);
    }

    // ------------------------------------------------------------------------------
    // ���������֐�
    // ------------------------------------------------------------------------------
    /**
     * �z�X�g�ڑ�����
     * 
     * @param tcpclient TCP�ڑ��N���C�A���g���ʎq
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean connect(TcpClient tcpclient, int kind, String value)
    {
        boolean ret;
        String query;
        ResultSet result;
        DbAccess db;

        switch( kind )
        {
        // �Z�b�g���ꂽ�z�e��ID�Őڑ�
            case 0:

                try
                {
                    db = new DbAccess();
                    query = "SELECT * FROM hotel WHERE hotel_id='" + HotelId + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                    }
                    else
                    {
                        ret = tcpclient.connectService( HotelId );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = tcpclient.connectService( HotelId );
                    log.error( "connect(0):" + e.toString() );
                }
                break;

            // �w�肳�ꂽ�z�e��ID�Őڑ�
            case 1:

                try
                {
                    db = new DbAccess();
                    query = "SELECT * FROM hotel WHERE hotel_id='" + HotelId + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                    }
                    else
                    {
                        ret = tcpclient.connectService( value );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = tcpclient.connectService( value );
                    log.error( "connect(1):" + e.toString() );
                }

                break;

            // IP�A�h���X�Őڑ�
            case 2:

                ret = tcpclient.connectServiceByAddr( value );
                break;

            // �Z�b�g���ꂽ�z�e��ID�Őڑ�
            default:

                ret = tcpclient.connectService( HotelId );
                break;
        }

        return(ret);
    }
}
