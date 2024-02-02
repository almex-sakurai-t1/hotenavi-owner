/*
 * @(#)RoomInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * �������֘A�ʐMAP�N���X
 */

package com.hotenavi2.room;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEB�T�[�r�X�Ƃ̕������֘A�d���ҏW�E����M���s���B
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/31
 */
public class RoomInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID       = 2027771029278186727L;

    // ------------------------------------------------------------------------------
    // �萔��`
    // ------------------------------------------------------------------------------
    /** �����ő吔 **/
    public static final int   ROOMINFO_ROOMMAX       = 128;
    /** ���������N�ő吔 **/
    public static final int   ROOMINFO_ROOMRANKMAX   = 99;
    /** ���������N�ő吔 **/
    public static final int   ROOMINFO_CHARGERANKMAX = 99;

    // ------------------------------------------------------------------------------
    // �f�[�^�̈��`
    // ------------------------------------------------------------------------------
    /** (����)�������� **/
    public int                Result;
    /** (����)�z�e��ID **/
    public String             HotelId;
    /** (����)�ڋq�ԍ� **/
    public String             CustomId;
    /** (����)�a�����i���j **/
    public String             Birthday1;
    /** (����)�a�����i���j **/
    public String             Birthday2;
    /** (����)���[�UID **/
    public String             UserId;
    /** (����)�p�X���[�h **/
    public String             Password;
    /** (����)�j�b�N�l�[�� **/
    public String             NickName;

    /** (�󎺏��)�󎺐� **/
    public int                RoomEmpty;
    /** (�󎺏��)�������� **/
    public int                RoomClean;
    /** (�󎺏��)�󎺕����R�[�h�ꗗ(MAX128��) **/
    public int                RoomCodeList[];
    /** (�󎺏��)�󎺕������̈ꗗ(MAX128��) **/
    public String             RoomList[];
    /** (�󎺏��)�󎺕��������N�R�[�h�ꗗ(MAX128��) **/
    public int                RoomRankCodeList[];
    /** (�󎺏��)�󎺕��������N���̈ꗗ(MAX128��) **/
    public String             RoomRankList[];
    /** (�󎺏��)�����������R�[�h�ꗗ(MAX128��) **/
    public int                RoomCodeCleanList[];
    /** (�󎺏��)�������������̈ꗗ(MAX128��) **/
    public String             RoomCleanList[];
    /** (�󎺏��)���������������N�R�[�h�ꗗ(MAX128��) **/
    public int                RoomRankCodeCleanList[];
    /** (�󎺏��)���������������N���̈ꗗ(MAX128��) **/
    public String             RoomRankCleanList[];

    /** (�\��)�\��\������ **/
    public int                ReserveListCount;
    /** (�\��)�\��\�����R�[�h�ꗗ(MAX128��) **/
    public int                ReserveListCode[];
    /** (�\��)�\��\�������̈ꗗ(MAX128��) **/
    public String             ReserveList[];
    /** (�\��)�\�񕔉��R�[�h **/
    public int                ReserveRoomCode;
    /** (�\��)�\�񕔉����� **/
    public String             ReserveRoomName;
    /** (�\��)�\��ԍ� **/
    public int                ReserveNo;
    /** (�\��)�\�񎞊� **/
    public int                ReserveLimitTime;
    /** (�\��)�\��\�����ݕ����R�[�h **/
    public int                ReserveReqRoomCode;
    /** (�\��)���[���A�h���X **/
    public String             ReserveMailAddress;
    /** (�\��)�\�񏈗����� **/
    public int                ReserveResult;
    /** (�\��)���������N�� **/
    public int                ReserveRoomRankCount;
    /** (�\��)���������N�R�[�h(MAX99) **/
    public int                ReserveRoomRankCode[];
    /** (�\��)���������N����(MAX99) **/
    public String             ReserveRoomRankName[];
    /** (�����V�~�����[�V����)�`�F�b�N�C�����t **/
    public int                SimulateCheckinDate;
    /** (�����V�~�����[�V����)�`�F�b�N�C������ **/
    public int                SimulateCheckinTime;
    /** (�����V�~�����[�V����)�`�F�b�N�A�E�g���t **/
    public int                SimulateCheckoutDate;
    /** (�����V�~�����[�V����)�`�F�b�N�A�E�g���� **/
    public int                SimulateCheckoutTime;
    /** (�����V�~�����[�V����)�\�Z **/
    public int                SimulateBudget;
    /** (�����V�~�����[�V����)�����^�C�v **/
    public int                SimulateRoomType;
    /** (�����V�~�����[�V����)���p���z **/
    public int                SimulateUseTotal;
    /** (�����V�~�����[�V����)�؍ݎ��� **/
    public int                SimulateStayTime;
    /** (�����V�~�����[�V����)�������א� **/
    public int                SimulatePlanCount;
    /** (�����V�~�����[�V����)�v��������(MAX99) **/
    public String             SimulatePlanName[];
    /** (�����V�~�����[�V����)�v�������z(MAX99) **/
    public int                SimulatePlanTotal[];
    /** (�����V�~�����[�V����)���������N�� **/
    public int                SimulateRoomRankCount;
    /** (�����V�~�����[�V����)���������N�R�[�h(MAX99) **/
    public int                SimulateRoomRankCode[];
    /** (�����V�~�����[�V����)���������N����(MAX99) **/
    public String             SimulateRoomRankName[];
    /** (�����V�~�����[�V����)���Ԓ��߃t���O **/
    public int                SimulateTimeOver;
    /** (�{�����������N)���������N **/
    public int                TodayChargeRank;
    /** (�{�����������N)���������N���� **/
    public String             TodayChargeRankName;

    /** (���O�\��\����)�`�F�b�N�C�����t **/
    public int                BeforeAcceptInDate;
    /** (���O�\��\����)�`�F�b�N�C������ **/
    public int                BeforeAcceptInTime;
    /** (���O�\��\����)�`�F�b�N�A�E�g���t **/
    public int                BeforeAcceptOutDate;
    /** (���O�\��\����)�`�F�b�N�A�E�g���� **/
    public int                BeforeAcceptOutTime;

    /** (���O�\��\�����ꗗ)�`�F�b�N�C�����t **/
    public int                BeforeListInDate;
    /** (���O�\��\�����ꗗ)�`�F�b�N�C������ **/
    public int                BeforeListInTime;
    /** (���O�\��\�����ꗗ)�`�F�b�N�A�E�g���t **/
    public int                BeforeListOutDate;
    /** (���O�\��\�����ꗗ)�`�F�b�N�A�E�g���� **/
    public int                BeforeListOutTime;
    /** (���O�\��\�����ꗗ)�����^�C�v�R�[�h **/
    public int                BeforeListRoomType;
    /** (���O�\��\�����ꗗ)�\��\������ **/
    public int                BeforeListRoomCount;
    /** (���O�\��\�����ꗗ)�����R�[�h **/
    public int                BeforeListRoomCode[];
    /** (���O�\��\�����ꗗ)�������� **/
    public String             BeforeListRoomName[];
    /** (���O�\��\�����ꗗ)�����^�C�v�R�[�h **/
    public int                BeforeListTypeCode[];
    /** (���O�\��\�����ꗗ)�����^�C�v���� **/
    public String             BeforeListTypeName[];
    /** (���O�\��\�����ꗗ)�`�F�b�N�C���\���t **/
    public int                BeforeListInPossibleDate[];
    /** (���O�\��\�����ꗗ)�`�F�b�N�C���\���� **/
    public int                BeforeListInPossibleTime[];
    /** (���O�\��\�����ꗗ)�`�F�b�N�A�E�g�\���t **/
    public int                BeforeListOutPossibleDate[];
    /** (���O�\��\�����ꗗ)�`�F�b�N�A�E�g�\���� **/
    public int                BeforeListOutPossibleTime[];
    /** (���O�\��\�����ꗗ)���v���z **/
    public int                BeforeListTotal[];
    /** (���O�\��\�����ꗗ)�������א� **/
    public int                BeforeListDetailCount[];
    /** (���O�\��\�����ꗗ)�������� **/
    public String             BeforeListDetailName[][];
    /** (���O�\��\�����ꗗ)���z **/
    public int                BeforeListDetailPrice[][];

    /** (���O�\�񏳔F)�`�F�b�N�C�����t **/
    public int                BeforeAuthInDate;
    /** (���O�\�񏳔F)�`�F�b�N�C������ **/
    public int                BeforeAuthInTime;
    /** (���O�\�񏳔F)�`�F�b�N�A�E�g���t **/
    public int                BeforeAuthOutDate;
    /** (���O�\�񏳔F)�`�F�b�N�A�E�g���� **/
    public int                BeforeAuthOutTime;
    /** (���O�\�񏳔F)�����R�[�h **/
    public int                BeforeAuthRoomCode;
    /** (���O�\�񏳔F)�������� **/
    public String             BeforeAuthRoomName;
    /** (���O�\�񏳔F)�����^�C�v�R�[�h **/
    public int                BeforeAuthTypeCode;
    /** (���O�\�񏳔F)�����^�C�v���� **/
    public String             BeforeAuthTypeName;
    /** (���O�\�񏳔F)���v���z **/
    public int                BeforeAuthTotal;
    /** (���O�\�񏳔F)�J�[�h�ԍ� **/
    public String             BeforeAuthCardNo;
    /** (���O�\�񏳔F)�L������ **/
    public int                BeforeAuthExpire;
    /** (���O�\�񏳔F)���O�i���j **/
    public String             BeforeAuthName1;
    /** (���O�\�񏳔F)���O�i���j **/
    public String             BeforeAuthName2;
    /** (���O�\�񏳔F)�Z�L�����e�B�R�[�h **/
    public String             BeforeAuthSecurity;
    /** (���O�\�񏳔F)����\���t **/
    public int                BeforeAuthCancelDate;
    /** (���O�\�񏳔F)����\���� **/
    public int                BeforeAuthCancelTime;
    /** (���O�\�񏳔F)�\��ԍ� **/
    public int                BeforeAuthReserveNo;
    /** (���O�\�񏳔F)���F�ԍ� **/
    public String             BeforeAuthApprove;
    /** (���O�\�񏳔F)�N���W�b�g�G���[�R�[�h **/
    public String             BeforeAuthErrorCode;

    /** (���O�\��ꗗ)�J�[�h�ԍ� **/
    public String             BeforeReserveCardNo;
    /** (���O�\��ꗗ)�\��ԍ� **/
    public int                BeforeReserveNo;
    /** (���O�\��ꗗ)�\�� **/
    public int                BeforeReserveCount;
    /** (���O�\��ꗗ)�����R�[�h **/
    public int                BeforeReserveRoomCode[];
    /** (���O�\��ꗗ)�������� **/
    public String             BeforeReserveRoomName[];
    /** (���O�\��ꗗ)�����^�C�v�R�[�h **/
    public int                BeforeReserveTypeCode[];
    /** (���O�\��ꗗ)�����^�C�v���� **/
    public String             BeforeReserveTypeName[];
    /** (���O�\��ꗗ)�`�F�b�N�C���\���t **/
    public int                BeforeReserveInPossibleDate[];
    /** (���O�\��ꗗ)�`�F�b�N�C���\���� **/
    public int                BeforeReserveInPossibleTime[];
    /** (���O�\��ꗗ)�`�F�b�N�A�E�g�\���t **/
    public int                BeforeReserveOutPossibleDate[];
    /** (���O�\��ꗗ)�`�F�b�N�A�E�g�\���� **/
    public int                BeforeReserveOutPossibleTime[];
    /** (���O�\��ꗗ)����\���t **/
    public int                BeforeReserveCancelDate[];
    /** (���O�\��ꗗ)����\���� **/
    public int                BeforeReserveCancelTime[];
    /** (���O�\��ꗗ)�\����t **/
    public int                BeforeReserveDate[];
    /** (���O�\��ꗗ)�\�񎞍� **/
    public int                BeforeReserveTime[];
    /** (���O�\��ꗗ)���v���z **/
    public int                BeforeReserveTotal[];
    /** (���O�\��ꗗ)�\��ԍ� **/
    public int                BeforeReserveReserveNo[];
    /** (���O�\��ꗗ)����敪(1:�����,2:����s��) **/
    public int                BeforeReserveCancel[];
    /** (���O�\��ꗗ)�������א� **/
    public int                BeforeReserveDetailCount[];
    /** (���O�\��ꗗ)�������� **/
    public String             BeforeReserveDetailName[][];
    /** (���O�\��ꗗ)���z **/
    public int                BeforeReserveDetailPrice[][];

    /** (���O�\����)�J�[�h�ԍ� **/
    public String             BeforeCancelCardNo;
    /** (���O�\����)�\��ԍ� **/
    public int                BeforeCancelReserveNo;

    /** (���ԏ���)���ԏ��ʐ� **/
    public int                ParkingKindCount;
    /** (���ԏ���)��ʃR�[�h(MAX:128) **/
    public int                ParkingKindCode[];
    /** (���ԏ���)��ʖ���(MAX:128) **/
    public String             ParkingKindName[];
    /** (���ԏ���)�󂫐�(MAX:128) **/
    public int                ParkingSpaceCount[];
    /** (���ԏ�ꗗ���)���ԏ��ʐ� **/
    public int                ParkingListCount;
    /** (���ԏ�ꗗ���)���ԏ�ԍ�(MAX:128) **/
    public String             ParkingListNo[];
    /** (���ԏ�ꗗ���)��ʃR�[�h(MAX:128) **/
    public int                ParkingListKindCode[];
    /** (���ԏ�ꗗ���)��ʖ���(MAX:128) **/
    public String             ParkingListKindName[];
    /** (���ԏ�ꗗ���)�X�e�[�^�X(MAX:128) **/
    public int                ParkingListStatus[];
    /** (���ԏ�ꗗ���)�����R�[�h(MAX:128) **/
    public int                ParkingListRoomCode[];
    /** (���ԏ�ꗗ���)��������(MAX:128) **/
    public String             ParkingListRoomName[];
    /** (���ԏ�ꗗ���)�Ԕ�(MAX:128) **/
    public String             ParkingListCarNo[];

    /** (�������ꗗ)������ **/
    public int                InfoRoomListTotalRooms;
    /** (�������ꗗ)�����R�[�h(MAX:128) **/
    public int                InfoRoomListRoomCode[];
    /** (�������ꗗ)��������(MAX:128) **/
    public String             InfoRoomListRoomName[];
    /** (���������ꗗ)�����X�e�[�^�X(MAX:128) **/
    public int                InfoRoomListRoomStatus[];

    /** (�O����擾)�`�F�b�N�C���R�[�h **/
    public int                Seq;
    /** (�O����擾)�����R�[�h **/
    public int                RoomCode;
    /** (�O����擾)�O����z **/
    public int                Deposit;
    /** (�O����擾)�������z **/
    public int                Charge;

    /** ���O�o�̓��C�u���� **/
    private LogLib            log;

    /**
     * �����֘A���f�[�^�̏��������s���܂��B
     * 
     */
    public RoomInfo()
    {
        HotelId = "";
        CustomId = "0";
        Birthday1 = "0";
        Birthday2 = "0";
        UserId = "";
        Password = "";
        NickName = "";
        RoomEmpty = 0;
        RoomClean = 0;
        RoomCodeList = new int[ROOMINFO_ROOMMAX];
        RoomList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeList = new int[ROOMINFO_ROOMMAX];
        RoomRankList = new String[ROOMINFO_ROOMMAX];
        RoomCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomCleanList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomRankCleanList = new String[ROOMINFO_ROOMMAX];

        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;
        ReserveReqRoomCode = 0;
        ReserveMailAddress = "";
        ReserveResult = 0;
        ReserveRoomRankCount = 0;
        ReserveRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        ReserveRoomRankName = new String[ROOMINFO_ROOMRANKMAX];
        SimulateCheckinDate = 0;
        SimulateCheckinTime = 0;
        SimulateCheckoutDate = 0;
        SimulateCheckoutTime = 0;
        SimulateBudget = 0;
        SimulateRoomType = 0;
        SimulateUseTotal = 0;
        SimulateStayTime = 0;
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateRoomRankCount = 0;
        SimulateRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        SimulateRoomRankName = new String[ROOMINFO_ROOMRANKMAX];
        SimulateTimeOver = 0;
        TodayChargeRank = 0;
        TodayChargeRankName = "";

        BeforeAcceptInDate = 0;
        BeforeAcceptInTime = 0;
        BeforeAcceptOutDate = 0;
        BeforeAcceptOutTime = 0;

        BeforeListInDate = 0;
        BeforeListInTime = 0;
        BeforeListOutDate = 0;
        BeforeListOutTime = 0;
        BeforeListRoomType = 0;
        BeforeListRoomCount = 0;
        BeforeListRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeListRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeListTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeListTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeListInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListTotal = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeListDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        BeforeAuthInDate = 0;
        BeforeAuthInTime = 0;
        BeforeAuthOutDate = 0;
        BeforeAuthOutTime = 0;
        BeforeAuthRoomCode = 0;
        BeforeAuthRoomName = "";
        BeforeAuthTypeCode = 0;
        BeforeAuthTypeName = "";
        BeforeAuthTotal = 0;
        BeforeAuthCardNo = "";
        BeforeAuthExpire = 0;
        BeforeAuthName1 = "";
        BeforeAuthName2 = "";
        BeforeAuthSecurity = "";
        BeforeAuthCancelDate = 0;
        BeforeAuthCancelTime = 0;
        BeforeAuthReserveNo = 0;
        BeforeAuthApprove = "";
        BeforeAuthErrorCode = "";

        BeforeReserveCardNo = "";
        BeforeReserveNo = 0;
        BeforeReserveCount = 0;
        BeforeReserveRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTotal = new int[ROOMINFO_ROOMMAX];
        BeforeReserveReserveNo = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancel = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeReserveDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        BeforeCancelCardNo = "";
        BeforeCancelReserveNo = 0;

        ParkingKindCount = 0;
        ParkingKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingKindName = new String[ROOMINFO_ROOMMAX];
        ParkingSpaceCount = new int[ROOMINFO_ROOMMAX];
        ParkingListCount = 0;
        ParkingListNo = new String[ROOMINFO_ROOMMAX];
        ParkingListKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingListKindName = new String[ROOMINFO_ROOMMAX];
        ParkingListStatus = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomCode = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomName = new String[ROOMINFO_ROOMMAX];
        ParkingListCarNo = new String[ROOMINFO_ROOMMAX];

        InfoRoomListTotalRooms = 0;
        InfoRoomListRoomCode = new int[ROOMINFO_ROOMMAX];
        InfoRoomListRoomName = new String[ROOMINFO_ROOMMAX];
        InfoRoomListRoomStatus = new int[ROOMINFO_ROOMMAX];

        Seq = 0;
        RoomCode = 0;
        Deposit = 0;
        Charge = 0;

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // �d���������\�b�h
    //
    // ------------------------------------------------------------------------------
    /**
     * �d�����M����(0000)
     * �����V�~�����[�V�����i�\�Z�j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0000()
    {
        return(sendPacket0000Sub( 0, "" ));
    }

    /**
     * �d�����M����(0000)
     * �����V�~�����[�V�����i�\�Z�j
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
     * �����V�~�����[�V�����i�\�Z�j
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0000Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0000";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �\�Z
                nf = new DecimalFormat( "00000" );
                strData = nf.format( SimulateBudget );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 138, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 140 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 160 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                            if ( SimulateCheckinDate == SimulateCheckoutDate && SimulateCheckinTime == SimulateCheckoutTime && SimulateUseTotal == 0 && SimulatePlanCount == 0 )
                            {
                                /*
                                 * NEO�̏ꍇ�A�\�Z�ɂ��V�~�����[�V�����Ɏ��s�����ꍇ�A�`�F�b�N�A�E�g�����iSimulateCheckoutDate,SimulateCheckoutTime�j�Ƀ`�F�b�N�C�������Ɠ����l�������Ă���B
                                 * ���̏ꍇ�A�\�Z�ɂ��V�~�����[�V�����Ɏ��s�����Ƃ݂Ȃ��A�`�F�b�N�A�E�g������0���Z�b�g����
                                 */

                                SimulateCheckoutDate = 0;
                                SimulateCheckoutTime = 0;
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0000:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0002)
     * �����V�~�����[�V�����i�؍ݎ��ԁj
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0002()
    {
        return(sendPacket0002Sub( 0, "" ));
    }

    /**
     * �d�����M����(0002)
     * �����V�~�����[�V�����i�؍ݎ��ԁj
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0002(int kind, String value)
    {
        return(sendPacket0002Sub( kind, value ));
    }

    /**
     * �d�����M����(0002)
     * �����V�~�����[�V�����i�؍ݎ��ԁj
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0002Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0002";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �؍ݎ���
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateStayTime );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0003" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 138, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 140 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 160 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0002:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0004)
     * �����V�~�����[�V�����i���Ԏw��j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0004()
    {
        return(sendPacket0004Sub( 0, "" ));
    }

    /**
     * �d�����M����(0004)
     * �����V�~�����[�V�����i���Ԏw��j
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0004(int kind, String value)
    {
        return(sendPacket0004Sub( kind, value ));
    }

    /**
     * �d�����M����(0004)
     * �����V�~�����[�V�����i���Ԏw��j
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0004Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0004";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g���t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckoutDate );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g����
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckoutTime );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0005" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 138, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 140 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 160 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0004:" + e.toString() );
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
     * ���������N�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0006()
    {
        return(sendPacket0006Sub( 0, "" ));
    }

    /**
     * �d�����M����(0006)
     * ���������N�擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0006(int kind, String value)
    {
        return(sendPacket0006Sub( kind, value ));
    }

    /**
     * �d�����M����(0006)
     * ���������N�擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0006Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulateRoomRankCount = 0;
        SimulateRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        SimulateRoomRankName = new String[ROOMINFO_ROOMRANKMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0006";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���������N���א�
                            strData = new String( cRecv, 109, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulateRoomRankCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ���������N�R�[�h
                                strData = new String( cRecv, 111 + (i * 44), 4 );
                                SimulateRoomRankCode[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 115 + (i * 44), 40 );
                                SimulateRoomRankName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0006:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0008)
     * �g�������V�~�����[�V�����i�\�Z�j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0008()
    {
        return(sendPacket0008Sub( 0, "" ));
    }

    /**
     * �d�����M����(0008)
     * �����V�~�����[�V�����i�\�Z�j
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0008(int kind, String value)
    {
        return(sendPacket0008Sub( kind, value ));
    }

    /**
     * �d�����M����(0008)
     * �����V�~�����[�V�����i�\�Z�j
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0008Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0008";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �\�Z
                nf = new DecimalFormat( "00000" );
                strData = nf.format( SimulateBudget );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0009" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 5 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // ���Ԓ��߃t���O
                            strData = new String( cRecv, 138, 1 );
                            SimulateTimeOver = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 139, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 141 + (i * 25), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 161 + (i * 25), 5 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                            if ( SimulateCheckinDate == SimulateCheckoutDate && SimulateCheckinTime == SimulateCheckoutTime && SimulateUseTotal == 0 && SimulatePlanCount == 0 )
                            {
                                /*
                                 * NEO�̏ꍇ�A�\�Z�ɂ��V�~�����[�V�����Ɏ��s�����ꍇ�A�`�F�b�N�A�E�g�����iSimulateCheckoutDate,SimulateCheckoutTime�j�Ƀ`�F�b�N�C�������Ɠ����l�������Ă���B
                                 * ���̏ꍇ�A�\�Z�ɂ��V�~�����[�V�����Ɏ��s�����Ƃ݂Ȃ��A�`�F�b�N�A�E�g������0���Z�b�g����
                                 */

                                SimulateCheckoutDate = 0;
                                SimulateCheckoutTime = 0;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0008:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0010)
     * �\��\���[���ꗗ�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0010()
    {
        return(sendPacket0010Sub( 0, "" ));
    }

    /**
     * �d�����M����(0010)
     * �\��\���[���ꗗ�擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0010(int kind, String value)
    {
        return(sendPacket0010Sub( kind, value ));
    }

    /**
     * �d�����M����(0010)
     * �\��\���[���ꗗ�擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0010Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        RoomEmpty = 0;
        RoomClean = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0010";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0011" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �󎺕�����
                            strData = new String( cRecv, 109, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 112, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // �\��\������
                            strData = new String( cRecv, 115, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 118 + (i * 11), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 121 + (i * 11), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0010:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0012)
     * �\��\�����݊m�F
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0012()
    {
        return(sendPacket0012Sub( 0, "" ));
    }

    /**
     * �d�����M����(0012)
     * �\��\�����݊m�F
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0012(int kind, String value)
    {
        return(sendPacket0012Sub( kind, value ));
    }

    /**
     * �d�����M����(0012)
     * �\��\�����݊m�F
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0012Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0012";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0013" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����R�[�h
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 183, 2 );
                            ReserveResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0012:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0014)
     * �\��\������
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0014()
    {
        return(sendPacket0014Sub( 0, "" ));
    }

    /**
     * �d�����M����(0014)
     * �\��\������
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0014(int kind, String value)
    {
        return(sendPacket0014Sub( kind, value ));
    }

    /**
     * �d�����M����(0014)
     * �\��\������
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0014Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0014";
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // ���[���A�h���X
                strData = format.leftFitFormat( ReserveMailAddress, 63 );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0015" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\�񕔉��R�[�h
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // �\�񕔉�����
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\��ԍ�
                            strData = new String( cRecv, 183, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // �\��L������
                            strData = new String( cRecv, 191, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0014:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0016)
     * �\��m�F
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0016()
    {
        return(sendPacket0016Sub( 0, "" ));
    }

    /**
     * �d�����M����(0016)
     * �\��m�F
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0016(int kind, String value)
    {
        return(sendPacket0016Sub( kind, value ));
    }

    /**
     * �d�����M����(0016)
     * �\��m�F
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0016Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0016";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0017" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\�񕔉��R�[�h
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // �\�񕔉�����
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\��ԍ�
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // �\��L������
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0016:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0018)
     * �\��ύX
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0018()
    {
        return(sendPacket0018Sub( 0, "" ));
    }

    /**
     * �d�����M����(0018)
     * �\��ύX
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0018(int kind, String value)
    {
        return(sendPacket0018Sub( kind, value ));
    }

    /**
     * �d�����M����(0018)
     * �\��ύX
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0018Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0018";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0019" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����R�[�h
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\��ԍ�
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // �\��L������
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();

                            // �󎺕�����
                            strData = new String( cRecv, 132, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 135, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // �\��\������
                            strData = new String( cRecv, 138, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 141 + (i * 11), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 144 + (i * 11), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0018:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0020)
     * �\��ύX�m�F
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0020()
    {
        return(sendPacket0020Sub( 0, "" ));
    }

    /**
     * �d�����M����(0020)
     * �\��ύX�m�F
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0020(int kind, String value)
    {
        return(sendPacket0020Sub( kind, value ));
    }

    /**
     * �d�����M����(0020)
     * �\��ύX�m�F
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0020Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0020";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0021" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����R�[�h
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 183, 2 );
                            ReserveResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0020:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0022)
     * �\�����m�F
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0022()
    {
        return(sendPacket0022Sub( 0, "" ));
    }

    /**
     * �d�����M����(0022)
     * �\�����m�F
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0022(int kind, String value)
    {
        return(sendPacket0022Sub( kind, value ));
    }

    /**
     * �d�����M����(0022)
     * �\�����m�F
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0022Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0022";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0023" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\�񕔉��R�[�h
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // �\�񕔉�����
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\��ԍ�
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // �\��L������
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0022:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0024)
     * �\����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0024()
    {
        return(sendPacket0024Sub( 0, "" ));
    }

    /**
     * �d�����M����(0024)
     * �\����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0024(int kind, String value)
    {
        return(sendPacket0024Sub( kind, value ));
    }

    /**
     * �d�����M����(0024)
     * �\����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0024Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveMailAddress = "";
        ReserveResult = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0024";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                strData = nf.format( ReserveReqRoomCode );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0025" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 109, 63 );
                            ReserveMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����R�[�h
                            strData = new String( cRecv, 172, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 175, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 183, 2 );
                            ReserveResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0024:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0026)
     * �����N�ʗ\��\���[���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0026()
    {
        return(sendPacket0026Sub( 0, "" ));
    }

    /**
     * �d�����M����(0026)
     * �����N�ʗ\��\���[���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0026(int kind, String value)
    {
        return(sendPacket0026Sub( kind, value ));
    }

    /**
     * �d�����M����(0026)
     * �����N�ʗ\��\���[���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0026Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        RoomEmpty = 0;
        RoomClean = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];
        ReserveRoomRankCount = 0;
        ReserveRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        ReserveRoomRankName = new String[ROOMINFO_ROOMRANKMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0026";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0027" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �󎺕�����
                            strData = new String( cRecv, 109, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 112, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // �\��\������
                            strData = new String( cRecv, 115, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ���������N�R�[�h
                                strData = new String( cRecv, 118 + (i * 53), 2 );
                                ReserveRoomRankCode[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 120 + (i * 53), 40 );
                                ReserveRoomRankName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����R�[�h
                                strData = new String( cRecv, 160 + (i * 53), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 163 + (i * 53), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0026:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0028)
     * �����N�ʗ\��ύX
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0028()
    {
        return(sendPacket0028Sub( 0, "" ));
    }

    /**
     * �d�����M����(0028)
     * �����N�ʗ\��ύX
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0028(int kind, String value)
    {
        return(sendPacket0028Sub( kind, value ));
    }

    /**
     * �d�����M����(0028)
     * �����N�ʗ\��ύX
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0028Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        RoomEmpty = 0;
        RoomClean = 0;
        ReserveListCount = 0;
        ReserveListCode = new int[ROOMINFO_ROOMMAX];
        ReserveList = new String[ROOMINFO_ROOMMAX];
        ReserveRoomRankCount = 0;
        ReserveRoomRankCode = new int[ROOMINFO_ROOMRANKMAX];
        ReserveRoomRankName = new String[ROOMINFO_ROOMRANKMAX];
        ReserveRoomCode = 0;
        ReserveRoomName = "";
        ReserveNo = 0;
        ReserveLimitTime = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0028";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0029" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����R�[�h
                            strData = new String( cRecv, 109, 3 );
                            ReserveRoomCode = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 112, 8 );
                            ReserveRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\��ԍ�
                            strData = new String( cRecv, 120, 8 );
                            ReserveNo = Integer.valueOf( strData ).intValue();

                            // �\��L������
                            strData = new String( cRecv, 128, 4 );
                            ReserveLimitTime = Integer.valueOf( strData ).intValue();

                            // �󎺕�����
                            strData = new String( cRecv, 132, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 135, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();

                            // �\��\������
                            strData = new String( cRecv, 138, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ReserveListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ���������N�R�[�h
                                strData = new String( cRecv, 141 + (i * 53), 2 );
                                ReserveRoomRankCode[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 143 + (i * 53), 40 );
                                ReserveRoomRankName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����R�[�h
                                strData = new String( cRecv, 183 + (i * 53), 3 );
                                ReserveListCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 186 + (i * 53), 8 );
                                ReserveList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0028:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0030)
     * ���O�\��\����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0030()
    {
        return(sendPacket0030Sub( 0, "" ));
    }

    /**
     * �d�����M����(0030)
     * ���O�\��\����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0030(int kind, String value)
    {
        return(sendPacket0030Sub( kind, value ));
    }

    /**
     * �d�����M����(0030)
     * ���O�\��\����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0030Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        Result = 0;
        BeforeAcceptInDate = 0;
        BeforeAcceptInTime = 0;
        BeforeAcceptOutDate = 0;
        BeforeAcceptOutTime = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0030";

                for( i = 0 ; i < 10 ; i++ )
                {
                    // �\��
                    strSend = strSend + " ";
                }

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0031" ) == 0 )
                        {
                            // ��������
                            strData = new String( cRecv, 36, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�C���\���t
                            strData = new String( cRecv, 38, 8 );
                            BeforeAcceptInDate = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�C���\����
                            strData = new String( cRecv, 46, 4 );
                            BeforeAcceptInTime = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�A�E�g�\���t
                            strData = new String( cRecv, 50, 8 );
                            BeforeAcceptOutDate = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�A�E�g�\����
                            strData = new String( cRecv, 58, 4 );
                            BeforeAcceptOutTime = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0030:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0032)
     * ���O�\��\�����ꗗ
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0032()
    {
        return(sendPacket0032Sub( 0, "" ));
    }

    /**
     * �d�����M����(0032)
     * ���O�\��\�����ꗗ
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0032(int kind, String value)
    {
        return(sendPacket0032Sub( kind, value ));
    }

    /**
     * �d�����M����(0032)
     * ���O�\��\�����ꗗ
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0032Sub(int kind, String value)
    {
        int i;
        int j;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        BeforeListRoomCount = 0;
        BeforeListRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeListRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeListTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeListTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeListInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeListOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeListTotal = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeListDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeListDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0032";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeListInDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeListInTime );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g���t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeListOutDate );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g����
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeListOutTime );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeListRoomType );
                strSend = strSend + strData;

                for( i = 0 ; i < 82 ; i++ )
                {
                    // �\��
                    strSend = strSend + " ";
                }

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0033" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �\��\������
                            strData = new String( cRecv, 111, 3 );
                            BeforeListRoomCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < BeforeListRoomCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 114 + (i * 400), 3 );
                                BeforeListRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 117 + (i * 400), 8 );
                                BeforeListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���������N�R�[�h
                                strData = new String( cRecv, 125 + (i * 400), 2 );
                                BeforeListTypeCode[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 129 + (i * 400), 40 );
                                BeforeListTypeName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �`�F�b�N�C���\���t
                                strData = new String( cRecv, 169 + (i * 400), 8 );
                                BeforeListInPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // �`�F�b�N�C���\����
                                strData = new String( cRecv, 177 + (i * 400), 4 );
                                BeforeListInPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // �`�F�b�N�A�E�g�\���t
                                strData = new String( cRecv, 181 + (i * 400), 8 );
                                BeforeListOutPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // �`�F�b�N�A�E�g�\����
                                strData = new String( cRecv, 189 + (i * 400), 4 );
                                BeforeListOutPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // ���v���z
                                strData = new String( cRecv, 193 + (i * 400), 9 );
                                BeforeListTotal[i] = Integer.valueOf( strData ).intValue();

                                // �\��20BYTE

                                // �������א�
                                strData = new String( cRecv, 222 + (i * 400), 2 );
                                BeforeListDetailCount[i] = Integer.valueOf( strData ).intValue();

                                for( j = 0 ; j < 10 ; j++ )
                                {
                                    // ��������
                                    strData = new String( cRecv, 224 + (i * 400) + (j * 29), 20 );
                                    BeforeListDetailName[i][j] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                    // �������א�
                                    strData = new String( cRecv, 244 + (i * 400) + (j * 29), 9 );
                                    BeforeListDetailPrice[i][j] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0032:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0034)
     * ���O�\�񏳔F
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0034()
    {
        return(sendPacket0034Sub( 0, "" ));
    }

    /**
     * �d�����M����(0034)
     * ���O�\�񏳔F
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0034(int kind, String value)
    {
        return(sendPacket0034Sub( kind, value ));
    }

    /**
     * �d�����M����(0034)
     * ���O�\�񏳔F
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0034Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        BeforeAuthCancelDate = 0;
        BeforeAuthCancelTime = 0;
        BeforeAuthReserveNo = 0;
        BeforeAuthApprove = "";
        BeforeAuthErrorCode = "";

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0034";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeAuthInDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeAuthInTime );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g���t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( BeforeAuthOutDate );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g����
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeAuthOutTime );
                strSend = strSend + strData;
                // ���������R�[�h
                nf = new DecimalFormat( "000" );
                strData = nf.format( BeforeAuthRoomCode );
                strSend = strSend + strData;
                // ���v���z
                nf = new DecimalFormat( "000000000" );
                strData = nf.format( BeforeAuthTotal );
                strSend = strSend + strData;
                // �J�[�h�ԍ�
                strSend = strSend + format.leftFitFormat( BeforeAuthCardNo, 19 );
                // �L������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( BeforeAuthExpire );
                strSend = strSend + strData;
                // ���O�i���j
                strSend = strSend + format.leftFitFormat( BeforeAuthName1, 16 );
                // ���O�i���j
                strSend = strSend + format.leftFitFormat( BeforeAuthName2, 16 );
                // �Z�L�����e�B�R�[�h
                strSend = strSend + format.leftFitFormat( BeforeAuthSecurity, 4 );

                for( i = 0 ; i < 15 ; i++ )
                {
                    // �\��
                    strSend = strSend + " ";
                }

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0035" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �����R�[�h
                            strData = new String( cRecv, 111, 3 );
                            BeforeAuthRoomCode = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 114, 8 );
                            BeforeAuthRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���������N�R�[�h
                            strData = new String( cRecv, 122, 4 );
                            BeforeAuthTypeCode = Integer.valueOf( strData ).intValue();

                            // ���������N����
                            strData = new String( cRecv, 126, 40 );
                            BeforeAuthTypeName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �`�F�b�N�C�����t
                            strData = new String( cRecv, 166, 8 );
                            BeforeAuthInDate = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�C������
                            strData = new String( cRecv, 174, 4 );
                            BeforeAuthInTime = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�A�E�g���t
                            strData = new String( cRecv, 178, 8 );
                            BeforeAuthOutDate = Integer.valueOf( strData ).intValue();

                            // �`�F�b�N�A�E�g����
                            strData = new String( cRecv, 186, 4 );
                            BeforeAuthOutTime = Integer.valueOf( strData ).intValue();

                            // ����\���t
                            strData = new String( cRecv, 190, 8 );
                            BeforeAuthCancelDate = Integer.valueOf( strData ).intValue();

                            // ����\����
                            strData = new String( cRecv, 198, 4 );
                            BeforeAuthCancelTime = Integer.valueOf( strData ).intValue();

                            // ���v���z
                            strData = new String( cRecv, 202, 9 );
                            BeforeAuthTotal = Integer.valueOf( strData ).intValue();

                            // �\��ԍ�
                            strData = new String( cRecv, 211, 9 );
                            BeforeAuthReserveNo = Integer.valueOf( strData ).intValue();

                            // �J�[�h�ԍ�
                            strData = new String( cRecv, 220, 19 );
                            BeforeAuthCardNo = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���F�ԍ�
                            strData = new String( cRecv, 239, 7 );
                            BeforeAuthApprove = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �G���[�R�[�h
                            strData = new String( cRecv, 246, 3 );
                            BeforeAuthErrorCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0034:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0036)
     * ���O�\��ꗗ
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0036()
    {
        return(sendPacket0036Sub( 0, "" ));
    }

    /**
     * �d�����M����(0036)
     * ���O�\��ꗗ
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0036(int kind, String value)
    {
        return(sendPacket0036Sub( kind, value ));
    }

    /**
     * �d�����M����(0036)
     * ���O�\��ꗗ
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0036Sub(int kind, String value)
    {
        int i;
        int j;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        BeforeReserveCount = 0;
        BeforeReserveRoomCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveRoomName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveTypeCode = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTypeName = new String[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveInPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveOutPossibleTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancelTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDate = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTime = new int[ROOMINFO_ROOMMAX];
        BeforeReserveTotal = new int[ROOMINFO_ROOMMAX];
        BeforeReserveReserveNo = new int[ROOMINFO_ROOMMAX];
        BeforeReserveCancel = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailCount = new int[ROOMINFO_ROOMMAX];
        BeforeReserveDetailName = new String[ROOMINFO_ROOMMAX][10];
        BeforeReserveDetailPrice = new int[ROOMINFO_ROOMMAX][10];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0036";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �J�[�h�ԍ�
                strSend = strSend + format.leftFitFormat( BeforeReserveCardNo, 19 );
                // �\��ԍ�
                nf = new DecimalFormat( "000000000" );
                strData = nf.format( BeforeReserveNo );
                strSend = strSend + strData;

                for( i = 0 ; i < 82 ; i++ )
                {
                    // �\��
                    strSend = strSend + " ";
                }

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0037" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �\��
                            strData = new String( cRecv, 111, 3 );
                            BeforeReserveCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < BeforeReserveCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 114 + (i * 500), 3 );
                                BeforeReserveRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 117 + (i * 500), 8 );
                                BeforeReserveRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���������N�R�[�h
                                strData = new String( cRecv, 125 + (i * 500), 4 );
                                BeforeReserveTypeCode[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 129 + (i * 500), 40 );
                                BeforeReserveTypeName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �`�F�b�N�C�����t
                                strData = new String( cRecv, 169 + (i * 500), 8 );
                                BeforeReserveInPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // �`�F�b�N�C������
                                strData = new String( cRecv, 177 + (i * 500), 4 );
                                BeforeReserveInPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // �`�F�b�N�A�E�g���t
                                strData = new String( cRecv, 181 + (i * 500), 8 );
                                BeforeReserveOutPossibleDate[i] = Integer.valueOf( strData ).intValue();

                                // �`�F�b�N�A�E�g����
                                strData = new String( cRecv, 189 + (i * 500), 4 );
                                BeforeReserveOutPossibleTime[i] = Integer.valueOf( strData ).intValue();

                                // ����\���t
                                strData = new String( cRecv, 193 + (i * 500), 8 );
                                BeforeReserveCancelDate[i] = Integer.valueOf( strData ).intValue();

                                // ����\����
                                strData = new String( cRecv, 201 + (i * 500), 4 );
                                BeforeReserveCancelTime[i] = Integer.valueOf( strData ).intValue();

                                // �\����t
                                strData = new String( cRecv, 205 + (i * 500), 8 );
                                BeforeReserveDate[i] = Integer.valueOf( strData ).intValue();

                                // �\�񎞍�
                                strData = new String( cRecv, 213 + (i * 500), 4 );
                                BeforeReserveTime[i] = Integer.valueOf( strData ).intValue();

                                // ���v���z
                                strData = new String( cRecv, 217 + (i * 500), 9 );
                                BeforeReserveTotal[i] = Integer.valueOf( strData ).intValue();

                                // �\��ԍ�
                                strData = new String( cRecv, 226 + (i * 500), 9 );
                                BeforeReserveReserveNo[i] = Integer.valueOf( strData ).intValue();

                                // ����敪
                                strData = new String( cRecv, 235 + (i * 500), 1 );
                                BeforeReserveCancel[i] = Integer.valueOf( strData ).intValue();

                                // �\��86BYTE

                                // �������א�
                                strData = new String( cRecv, 322 + (i * 500), 2 );
                                BeforeReserveDetailCount[i] = Integer.valueOf( strData ).intValue();

                                for( j = 0 ; j < 10 ; j++ )
                                {
                                    // ��������
                                    strData = new String( cRecv, 324 + (i * 500) + (j * 29), 20 );
                                    BeforeReserveDetailName[i][j] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                    // �������א�
                                    strData = new String( cRecv, 344 + (i * 500) + (j * 29), 9 );
                                    BeforeReserveDetailPrice[i][j] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0036:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0036)
     * ���O�\����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0038()
    {
        return(sendPacket0038Sub( 0, "" ));
    }

    /**
     * �d�����M����(0038)
     * ���O�\����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0038(int kind, String value)
    {
        return(sendPacket0038Sub( kind, value ));
    }

    /**
     * �d�����M����(0038)
     * ���O�\����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0038Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0038";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �J�[�h�ԍ�
                strSend = strSend + format.leftFitFormat( BeforeCancelCardNo, 19 );
                // �\��ԍ�
                nf = new DecimalFormat( "000000000" );
                strData = nf.format( BeforeCancelReserveNo );
                strSend = strSend + strData;

                for( i = 0 ; i < 82 ; i++ )
                {
                    // �\��
                    strSend = strSend + " ";
                }

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0039" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ��������
                            strData = new String( cRecv, 109, 2 );
                            Result = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0038:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0040)
     * 6�������V�~�����[�V�����i�\�Z�j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0040()
    {
        return(sendPacket0040Sub( 0, "" ));
    }

    /**
     * �d�����M����(0040)
     * 6�������V�~�����[�V�����i�\�Z�j
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0040(int kind, String value)
    {
        return(sendPacket0040Sub( kind, value ));
    }

    /**
     * �d�����M����(0040)
     * 6�������V�~�����[�V�����i�\�Z�j
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0040Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0040";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �\�Z
                nf = new DecimalFormat( "000000" );
                strData = nf.format( SimulateBudget );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0041" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 6 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // ���Ԓ��߃t���O
                            strData = new String( cRecv, 139, 1 );
                            SimulateTimeOver = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 140, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 142 + (i * 26), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 162 + (i * 26), 6 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                            if ( SimulateCheckinDate == SimulateCheckoutDate && SimulateCheckinTime == SimulateCheckoutTime && SimulateUseTotal == 0 && SimulatePlanCount == 0 )
                            {
                                /*
                                 * NEO�̏ꍇ�A�\�Z�ɂ��V�~�����[�V�����Ɏ��s�����ꍇ�A�`�F�b�N�A�E�g�����iSimulateCheckoutDate,SimulateCheckoutTime�j�Ƀ`�F�b�N�C�������Ɠ����l�������Ă���B
                                 * ���̏ꍇ�A�\�Z�ɂ��V�~�����[�V�����Ɏ��s�����Ƃ݂Ȃ��A�`�F�b�N�A�E�g������0���Z�b�g����
                                 */

                                SimulateCheckoutDate = 0;
                                SimulateCheckoutTime = 0;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0040:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0042)
     * 6�������V�~�����[�V�����i�؍ݎ��ԁj
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0042()
    {
        return(sendPacket0042Sub( 0, "" ));
    }

    /**
     * �d�����M����(0042)
     * 6�������V�~�����[�V�����i�؍ݎ��ԁj
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0042(int kind, String value)
    {
        return(sendPacket0042Sub( kind, value ));
    }

    /**
     * �d�����M����(0042)
     * 6�������V�~�����[�V�����i�؍ݎ��ԁj
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0042Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0042";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �؍ݎ���
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateStayTime );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0043" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 6 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 139, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 141 + (i * 26), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 161 + (i * 26), 6 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0042:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0044)
     * 6�������V�~�����[�V�����i���Ԏw��j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0044()
    {
        return(sendPacket0044Sub( 0, "" ));
    }

    /**
     * �d�����M����(0044)
     * 6�������V�~�����[�V�����i���Ԏw��j
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0044(int kind, String value)
    {
        return(sendPacket0044Sub( kind, value ));
    }

    /**
     * �d�����M����(0044)
     * 6�������V�~�����[�V�����i���Ԏw��j
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0044Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        SimulatePlanCount = 0;
        SimulatePlanName = new String[ROOMINFO_CHARGERANKMAX];
        SimulatePlanTotal = new int[ROOMINFO_CHARGERANKMAX];
        SimulateTimeOver = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0044";
                // �ڋq�ԍ�
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                else if ( !UserId.trim().equals( "" ) )
                {
                    strSend = strSend + format.leftFitFormat( "    ", 9 );
                }
                else
                {
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                }
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                strSend = strSend + strData;
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                strSend = strSend + strData;
                // ���[�UID
                strSend = strSend + format.leftFitFormat( UserId, 32 );
                // �p�X���[�h
                strSend = strSend + format.leftFitFormat( Password, 8 );
                // �`�F�b�N�C�����t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckinDate );
                strSend = strSend + strData;
                // �`�F�b�N�C������
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckinTime );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g���t
                nf = new DecimalFormat( "00000000" );
                strData = nf.format( SimulateCheckoutDate );
                strSend = strSend + strData;
                // �`�F�b�N�A�E�g����
                nf = new DecimalFormat( "0000" );
                strData = nf.format( SimulateCheckoutTime );
                strSend = strSend + strData;
                // �����^�C�v
                nf = new DecimalFormat( "00" );
                strData = nf.format( SimulateRoomType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0045" ) == 0 )
                        {
                            // �j�b�N�l�[��
                            strData = new String( cRecv, 89, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������t
                            strData = new String( cRecv, 109, 8 );
                            SimulateCheckinDate = Integer.valueOf( strData ).intValue();

                            // ��������
                            strData = new String( cRecv, 117, 4 );
                            SimulateCheckinTime = Integer.valueOf( strData ).intValue();

                            // �ގ����t
                            strData = new String( cRecv, 121, 8 );
                            SimulateCheckoutDate = Integer.valueOf( strData ).intValue();

                            // �ގ�����
                            strData = new String( cRecv, 129, 4 );
                            SimulateCheckoutTime = Integer.valueOf( strData ).intValue();

                            // ���p���z
                            strData = new String( cRecv, 133, 6 );
                            SimulateUseTotal = Integer.valueOf( strData ).intValue();

                            // �������א�
                            strData = new String( cRecv, 139, 2 );
                            nCount = Integer.valueOf( strData ).intValue();
                            SimulatePlanCount = nCount;

                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �v��������
                                strData = new String( cRecv, 141 + (i * 26), 20 );
                                SimulatePlanName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �v�������z
                                strData = new String( cRecv, 161 + (i * 26), 6 );
                                SimulatePlanTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0044:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0200)
     * �󎺏��̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0200()
    {
        return(sendPacket0200Sub( 0, "" ));
    }

    /**
     * �d�����M����(0200)
     * �󎺏��̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0200(int kind, String value)
    {
        return(sendPacket0200Sub( kind, value ));
    }

    /**
     * �d�����M����(0200)
     * �󎺏��̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0200Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv = null;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        RoomEmpty = 0;
        RoomClean = 0;

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0200";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0201" ) == 0 )
                        {
                            // �󎺐�
                            strData = new String( cRecv, 36, 3 );
                            RoomEmpty = Integer.valueOf( strData ).intValue();

                            // ������
                            strData = new String( cRecv, 39, 3 );
                            RoomClean = Integer.valueOf( strData ).intValue();
                        }
                        else
                        {
                            RoomEmpty = 0;
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0200:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                if ( strRecv != null )
                {
                    return(true);
                }
                else
                {
                    return(false);
                }
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0202)
     * �󎺈ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0202()
    {
        return(sendPacket0202Sub( 0, "", "" ));
    }

    /**
     * �d�����M����Keep�Ώە����擾(0202)
     * �󎺈ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0202ForKeep()
    {
        return(sendPacket0202Sub( 0, "", "K" ));
    }

    /**
     * �d�����M����(0202)
     * �󎺈ꗗ���̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0202(int kind, String value)
    {
        return(sendPacket0202Sub( kind, value, "" ));
    }

    /**
     * �d�����M����(0202)
     * �󎺈ꗗ���̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0202Sub(int kind, String value, String requestKind)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv = null;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        RoomEmpty = 0;
        RoomClean = 0;
        RoomCodeList = new int[ROOMINFO_ROOMMAX];
        RoomList = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0202";
                // �\��
                strSend = strSend + requestKind + "         ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0203" ) == 0 )
                        {
                            // ��������
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomEmpty = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 39 + (i * 11), 3 );
                                RoomCodeList[i] = Integer.valueOf( strData ).intValue();
                                // ��������
                                strData = new String( cRecv, 42 + (i * 11), 8 );
                                RoomList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomEmpty = 0;
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0202:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                if ( strRecv != null )
                {
                    return(true);
                }
                else
                {
                    return(false);
                }
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0204)
     * ���������N�ʋ󎺈ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0204()
    {
        return(sendPacket0204Sub( 0, "" ));
    }

    /**
     * �d�����M����(0204)
     * ���������N�ʋ󎺈ꗗ���̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0204(int kind, String value)
    {
        return(sendPacket0204Sub( kind, value ));
    }

    /**
     * �d�����M����(0204)
     * ���������N�ʋ󎺈ꗗ���̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0204Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        RoomEmpty = 0;
        RoomClean = 0;
        RoomCodeList = new int[ROOMINFO_ROOMMAX];
        RoomList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeList = new int[ROOMINFO_ROOMMAX];
        RoomRankList = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0204";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0205" ) == 0 )
                        {
                            // ��������
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomEmpty = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ���������N�R�[�h
                                strData = new String( cRecv, 39 + (i * 53), 2 );
                                RoomRankCodeList[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 41 + (i * 53), 40 );
                                RoomRankList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����R�[�h
                                strData = new String( cRecv, 81 + (i * 53), 3 );
                                RoomCodeList[i] = Integer.valueOf( strData ).intValue();
                                // ��������
                                strData = new String( cRecv, 84 + (i * 53), 8 );
                                RoomList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomEmpty = 0;
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0204:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                if ( strRecv != null )
                {
                    return(true);
                }
                else
                {
                    return(false);
                }
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0206)
     * �������ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0206()
    {
        return(sendPacket0206Sub( 0, "" ));
    }

    /**
     * �d�����M����(0206)
     * �������ꗗ���̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0206(int kind, String value)
    {
        return(sendPacket0206Sub( kind, value ));
    }

    /**
     * �d�����M����(0206)
     * �󎺈ꗗ���̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0206Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        RoomClean = 0;
        RoomCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomCleanList = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0206";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0207" ) == 0 )
                        {
                            // ��������
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomClean = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 39 + (i * 11), 3 );
                                RoomCodeCleanList[i] = Integer.valueOf( strData ).intValue();
                                // ��������
                                strData = new String( cRecv, 42 + (i * 11), 8 );
                                RoomCleanList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0206:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0208)
     * ���������N�ʏ������ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0208()
    {
        return(sendPacket0208Sub( 0, "" ));
    }

    /**
     * �d�����M����(0208)
     * ���������N�ʏ������ꗗ���̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0208(int kind, String value)
    {
        return(sendPacket0208Sub( kind, value ));
    }

    /**
     * �d�����M����(0208)
     * ���������N�ʏ������ꗗ���̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0208Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        RoomClean = 0;
        RoomCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomCleanList = new String[ROOMINFO_ROOMMAX];
        RoomRankCodeCleanList = new int[ROOMINFO_ROOMMAX];
        RoomRankCleanList = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0208";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0209" ) == 0 )
                        {
                            // ��������
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            RoomClean = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ���������N�R�[�h
                                strData = new String( cRecv, 39 + (i * 53), 2 );
                                RoomRankCodeCleanList[i] = Integer.valueOf( strData ).intValue();

                                // ���������N����
                                strData = new String( cRecv, 41 + (i * 53), 40 );
                                RoomRankCleanList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����R�[�h
                                strData = new String( cRecv, 81 + (i * 53), 3 );
                                RoomCodeCleanList[i] = Integer.valueOf( strData ).intValue();
                                // ��������
                                strData = new String( cRecv, 84 + (i * 53), 8 );
                                RoomCleanList[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            RoomClean = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0208:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0210)
     * ���ԏ�󂫏��̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0210()
    {
        return(sendPacket0210Sub( 0, "" ));
    }

    /**
     * �d�����M����(0210)
     * ���ԏ�󂫏��̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0210(int kind, String value)
    {
        return(sendPacket0210Sub( kind, value ));
    }

    /**
     * �d�����M����(0210)
     * ���ԏ�󂫏��̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0210Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        ParkingKindCount = 0;
        ParkingKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingKindName = new String[ROOMINFO_ROOMMAX];
        ParkingSpaceCount = new int[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0210";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0211" ) == 0 )
                        {
                            // ���ԏ��ʐ�
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ParkingKindCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ��ʃR�[�h
                                strData = new String( cRecv, 39 + (i * 53), 2 );
                                ParkingKindCode[i] = Integer.valueOf( strData ).intValue();
                                // ��ʖ���
                                strData = new String( cRecv, 41 + (i * 53), 40 );
                                ParkingKindName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // �󂫐�
                                strData = new String( cRecv, 81 + (i * 53), 3 );
                                ParkingSpaceCount[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                        else
                        {
                            ParkingKindCount = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0210:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0212)
     * ���ԏ���ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0212()
    {
        return(sendPacket0212Sub( 0, "" ));
    }

    /**
     * �d�����M����(0212)
     * ���ԏ���ꗗ���̎擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0212(int kind, String value)
    {
        return(sendPacket0212Sub( kind, value ));
    }

    /**
     * �d�����M����(0212)
     * ���ԏ���ꗗ���̎擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0212Sub(int kind, String value)
    {
        int i;
        int nCount;
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        ParkingListCount = 0;
        ParkingListNo = new String[ROOMINFO_ROOMMAX];
        ParkingListKindCode = new int[ROOMINFO_ROOMMAX];
        ParkingListKindName = new String[ROOMINFO_ROOMMAX];
        ParkingListStatus = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomCode = new int[ROOMINFO_ROOMMAX];
        ParkingListRoomName = new String[ROOMINFO_ROOMMAX];
        ParkingListCarNo = new String[ROOMINFO_ROOMMAX];

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0212";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0213" ) == 0 )
                        {
                            // ���ԏꐔ
                            strData = new String( cRecv, 36, 3 );
                            nCount = Integer.valueOf( strData ).intValue();
                            ParkingListCount = nCount;
                            for( i = 0 ; i < nCount ; i++ )
                            {
                                // ���ԏ�ԍ�
                                strData = new String( cRecv, 39 + (i * 128), 8 );
                                ParkingListNo[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ��ʃR�[�h
                                strData = new String( cRecv, 47 + (i * 128), 2 );
                                ParkingListKindCode[i] = Integer.valueOf( strData ).intValue();

                                // ��ʖ���
                                strData = new String( cRecv, 49 + (i * 128), 40 );
                                ParkingListKindName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �X�e�[�^�X
                                strData = new String( cRecv, 89 + (i * 128), 1 );
                                ParkingListStatus[i] = Integer.valueOf( strData ).intValue();

                                // �����R�[�h
                                strData = new String( cRecv, 90 + (i * 128), 3 );
                                ParkingListRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 93 + (i * 128), 8 );
                                ParkingListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �Ԕ�
                                strData = new String( cRecv, 101 + (i * 128), 40 );
                                ParkingListCarNo[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                        else
                        {
                            ParkingListCount = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0212:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0214)
     * �������ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0214()
    {
        return(sendPacket0214Sub( 0, "" ));
    }

    /**
     * �@�d�����M����(0214)
     * �������ꗗ�擾�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    public boolean sendPacket0214(int kind, String value)
    {
        return(sendPacket0214Sub( kind, value ));
    }

    /**
     * �@�d�����M����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */

    private boolean sendPacket0214Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        int i = 0;
        int loop = 0;

        // �z�e��ID�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0214";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0215" ) == 0 )
                        {
                            // ������
                            strData = new String( cRecv, 36, 3 );
                            InfoRoomListTotalRooms = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < InfoRoomListTotalRooms ; i++ )
                            {
                                loop = 13 * i;
                                // �����R�[�h
                                strData = new String( cRecv, 39 + loop, 3 );
                                InfoRoomListRoomCode[i] = Integer.valueOf( strData ).intValue();
                                // ��������
                                strData = new String( cRecv, 42 + loop, 8 );
                                InfoRoomListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // �����X�e�[�^�X
                                strData = new String( cRecv, 50 + loop, 2 );
                                InfoRoomListRoomStatus[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                        else
                        {
                            InfoRoomListRoomCode[i] = 0;
                            InfoRoomListRoomName[i] = "";
                            InfoRoomListRoomStatus[i] = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0214:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0216)
     * �O����擾�v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0216()
    {
        return(sendPacket0216Sub( 0, "" ));
    }

    /**
     * �d�����M����(0216)
     * �O����擾�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0216(int kind, String value)
    {
        return(sendPacket0216Sub( kind, value ));
    }

    /**
     * �@�d�����M����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */

    private boolean sendPacket0216Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        int i = 0;
        int loop = 0;
        NumberFormat nf;

        // �z�e��ID�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0216";
                // �`�F�b�N�C���R�[�h
                nf = new DecimalFormat( "00000000" );
                strSend += nf.format( Seq );

                // �����R�[�h
                nf = new DecimalFormat( "000" );
                strSend += nf.format( RoomCode );

                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0217" ) == 0 )
                        {
                            // ����
                            strData = new String( cRecv, 36, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �O���
                            strData = new String( cRecv, 39, 6 );
                            Deposit = Integer.valueOf( strData ).intValue();

                            // �������z
                            strData = new String( cRecv, 45, 6 );
                            Charge = Integer.valueOf( strData ).intValue();

                        }
                        else
                        {
                            Result = 99;
                            Deposit = 0;
                            Charge = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0216:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0218)
     * �z�X�g�ʐM�p�������ꗗ���̎擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0218()
    {
        return(sendPacket0218Sub( 0, "" ));
    }

    /**
     * �@�d�����M����(0218)
     * �z�X�g�ʐM�p�������ꗗ�擾�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    public boolean sendPacket0218(int kind, String value)
    {
        return(sendPacket0218Sub( kind, value ));
    }

    /**
     * �@�d�����M����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */

    private boolean sendPacket0218Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        int i = 0;
        int loop = 0;

        // �z�e��ID�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value, 3000 );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0218";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );
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
                        if ( strData.compareTo( "0219" ) == 0 )
                        {
                            // ������
                            strData = new String( cRecv, 36, 3 );
                            InfoRoomListTotalRooms = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < InfoRoomListTotalRooms ; i++ )
                            {
                                loop = 13 * i;
                                // �����R�[�h
                                strData = new String( cRecv, 39 + loop, 3 );
                                InfoRoomListRoomCode[i] = Integer.valueOf( strData ).intValue();
                                // ��������
                                strData = new String( cRecv, 42 + loop, 8 );
                                InfoRoomListRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // �����X�e�[�^�X
                                strData = new String( cRecv, 50 + loop, 2 );
                                InfoRoomListRoomStatus[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                        else
                        {
                            InfoRoomListRoomCode[i] = 0;
                            InfoRoomListRoomName[i] = "";
                            InfoRoomListRoomStatus[i] = 0;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0218:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0300)
     * �{���������[�h�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0300()
    {
        return(sendPacket0300Sub( 0, "" ));
    }

    /**
     * �d�����M����(0300)
     * �����󋵗����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0300(int kind, String value)
    {
        return(sendPacket0300Sub( kind, value ));
    }

    /**
     * �d�����M����(0300)
     * �{���������[�h�擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������
     */
    private boolean sendPacket0300Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strHeader;
        String strRecv;
        String strData;
        char cRecv[];
        TcpClient tcpClient;

        // �f�[�^�̃N���A
        TodayChargeRank = 0;
        TodayChargeRankName = "";

        // �z�e���h�c�̃`�F�b�N
        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                // �R�}���h
                strSend = "0300";
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�̎擾
                strHeader = tcpClient.getPacketHeader( HotelId, strSend.length() );

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
                        if ( strData.compareTo( "0301" ) == 0 )
                        {
                            // ���������N�R�[�h
                            strData = new String( cRecv, 36, 3 );
                            TodayChargeRank = Integer.valueOf( strData ).intValue();

                            // ���������N����
                            strData = new String( cRecv, 39, 40 );
                            TodayChargeRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0300:" + e.toString() );
                    return(false);
                }

                tcpClient.disconnectService();

                return(true);
            }
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
     * @param value �^�C���A�E�g�l
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

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
        return connect( tcpclient, kind, value, null );
    }

    private boolean connect(TcpClient tcpclient, int kind, String value, Integer timeOut)
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
                        ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ), timeOut );
                    }
                    else
                    {
                        ret = tcpclient.connectService( HotelId, timeOut );
                    }

                    db.close();
                }
                catch ( Exception e )
                {
                    ret = tcpclient.connectService( HotelId, timeOut );
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
