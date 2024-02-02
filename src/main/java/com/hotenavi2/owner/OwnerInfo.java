/*
 * @(#)OwnerInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * �I�[�i�[�T�C�g�֘A�ʐMAP�N���X
 */

package com.hotenavi2.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.hotenavi2.common.DBConnection;
import com.hotenavi2.common.DateEdit;
import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.Logging;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEB�T�[�r�X�Ƃ̃I�[�i�[�T�C�g�֘A�d���ҏW�E����M���s���B
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/31
 */
public class OwnerInfo implements Serializable
{

    /**
     *
     */
    private static final long  serialVersionUID              = -3827290595500590205L;

    // ------------------------------------------------------------------------------
    // �萔��`
    // ------------------------------------------------------------------------------
    /** �����ő吔 **/
    public static final int    OWNERINFO_ROOMMAX             = 128;
    /** �����ݔ��ő吔 **/
    public static final int    OWNERINFO_EQUIPMAX            = 20;
    /** ���׏��ő吔 **/
    public static final int    OWNERINFO_DETAILMAX           = 100;
    /** ���ԏ��ő吔 **/
    public static final int    OWNERINFO_TIMEHOURMAX         = 24;
    /** �����X�e�[�^�X���ő吔 **/
    public static final int    OWNERINFO_ROOMSTATUSMAX       = 50;
    /** �X�܍ő吔 **/
    public static final int    OWNERINFO_TENPOMAX            = 20;
    /** ������z�f�[�^��ʍő吔 **/
    public static final int    OWNERINFO_TEXSALESMAX         = 4;
    /** ���ɏ��f�[�^��ʍő吔 **/
    public static final int    OWNERINFO_TEXSAFEMAX          = 4;
    /** ����ő吔 **/
    public static final int    OWNERINFO_TEXSAFETYPEMAX      = 16;
    /** ����ڕW���א� **/
    public static final int    OWNERINFO_SALESTAGETMAX       = 99;
    /** �h�A�J��񖾍א� **/
    public static final int    OWNERINFO_DOORDETAILMAX       = 40;
    /** ���t�ő吔 **/
    public static final int    OWNERINFO_DAYMAX              = 31;
    /** �������[�h�ő吔 **/
    public static final int    OWNERINFO_CHAGEMODEMAX        = 20;
    /** �L�O���ő吔 **/
    public static final int    OWNERINFO_MEMORIALMAX         = 8;
    /** �����o�[�C�x���g�ő吔 **/
    public static final int    OWNERINFO_MEMBEREVENTMAX      = 10;
    /** �����o�[�����N�ő吔 **/
    public static final int    OWNERINFO_CUSTOMRANKMAX       = 20;
    /** ���Z�@���O���ő吔 **/
    public static final int    OWNERINFO_TEXLOGMAX           = 50;
    /** ����ڍ׉ϖ��׍ő吔 **/
    public static final int    OWNERINFO_SALESDETAILMAX      = 10;
    /** �i�t�����g���Z�@�j������z�f�[�^��ʍő吔 **/
    public static final int    OWNERINFO_FRONTTEXSALESMAX    = 8;
    /** �i�t�����g���Z�@�j���ɏ��f�[�^��ʍő吔 **/
    public static final int    OWNERINFO_FRONTTEXSAFEMAX     = 10;
    /** �i�t�����g���Z�@�j����ő吔 **/
    public static final int    OWNERINFO_FRONTTEXSAFETYPEMAX = 16;
    /** �i�X�e�[�^�X�ڍבJ�ځj�^�C���e�[�u���ő吔 **/
    public static final int    OWNERINFO_TIMETABLEMAX        = 144;
    /** �i�ϖ��̔���ڍׁj�ϖ��׍ő吔 **/
    public static final int    OWNERINFO_MANUALSALESMAX      = 99;

    private final String       YYYY_MM                       = "yyyyMM";

    private final String       YYYY_MM_DD                    = "yyyyMMdd";

    /** �z�X�g�̃o�[�W�����ɌW��锻�f�Ɏg�p **/
    public static final String SYSTEM_KIND_NEO               = "N";
    public static final int    SYSTEM_VER1_MIN               = 1;
    public static final int    SYSTEM_VER2_NEO_TO_SIRIUS     = 180;
    public static final String SYSTEM_CUSTOMER_KIND_SIMPLE   = "1";
    public static final String SYSTEM_CUSTOMER_KIND_ALMEX    = "2";

    // ------------------------------------------------------------------------------
    // �f�[�^�̈��`
    // ------------------------------------------------------------------------------
    /** (����)�������� **/
    public int                 Result;
    /** (����)�z�e��ID **/
    public String              HotelId;
    /** (����)���[�UID(ceritfiedid�Œ�) **/
    public String              TermId;
    /** (����)�p�X���[�h(6268�Œ�) **/
    public String              Password;
    /** (����)�v��� **/
    public int                 Addupdate;
    /** (����)�S���Җ�(""�Œ�) **/
    public String              Name;
    /** (����)�擾�����R�[�h(0:�S����) **/
    public int                 RoomCode;
    /** (����)�]�ƈ��� **/
    public String              EmployeeName;
    /** (����)�������[�h���� **/
    public String              ModeName;

    /** (�I�[�i�[���O�C������)�V�X�e����� **/
    public String              SystemKind;
    /** (�I�[�i�[���O�C������)�V�X�e���o�[�W����1 **/
    public int                 SystemVer1;
    /** (�I�[�i�[���O�C������)�V�X�e���o�[�W����2 **/
    public int                 SystemVer2;
    /** (�I�[�i�[���O�C������)�V�X�e���o�[�W����3 **/
    public int                 SystemVer3;
    /** (�I�[�i�[���O�C������)�V�X�e���o�[�W����4 **/
    public int                 SystemVer4;
    /** (�I�[�i�[���O�C������)�ڋq�V�X�e����� **/
    public String              SystemCustomerKind;

    /** (DB����)���O�C�����[�UID **/
    public String              DbLoginUser;
    /** (DB����)���O�C�����[�U�� **/
    public String              DbUserName;
    /** (DB����)�p�X���[�h **/
    public String              DbPassword;
    /** (DB����)���[�U���x�� **/
    public int                 DbUserLevel;
    /** (DB����)���[�UID **/
    public int                 DbUserId;

    /** (������)�擾�J�n���t **/
    public int                 SalesGetStartDate;
    /** (������)�擾�I�����t **/
    public int                 SalesGetEndDate;
    /** (������)���Z�@�������� **/
    public int                 SalesTex;
    /** (������)���Z�@�N���W�b�g���� **/
    public int                 SalesTexCredit;
    /** (������)�t�����g���� **/
    public int                 SalesFront;
    /** (������)�t�����g�N���W�b�g���� **/
    public int                 SalesFrontCredit;
    /** (������)���㍇�v **/
    public int                 SalesTotal;
    /** (������)�x�e�g�� **/
    public int                 SalesRestCount;
    /** (������)�h���g�� **/
    public int                 SalesStayCount;
    /** (������)���v�g�� **/
    public int                 SalesTotalCount;
    /** (������)���ݓ����� **/
    public int                 SalesNowCheckin;
    /** (������)�x�e��]�� **/
    public int                 SalesRestRate;
    /** (������)�h����]�� **/
    public int                 SalesStayRate;
    /** (������)���v��]�� **/
    public int                 SalesTotalRate;
    /** (������)�x�e�q�P�� **/
    public int                 SalesRestPrice;
    /** (������)�h���q�P�� **/
    public int                 SalesStayPrice;
    /** (������)���v�q�P�� **/
    public int                 SalesTotalPrice;
    /** (������)�r�W�^�[�q�P�� **/
    public int                 SalesVisitorPrice;
    /** (������)�����o�[�q�P�� **/
    public int                 SalesMemberPrice;
    /** (������)�����P�� **/
    public int                 SalesRoomPrice;
    /** (������)�݌v�����P�� **/
    public int                 SalesRoomTotalPrice;
    /** (������)�x�e���� **/
    public int                 SalesRestTotal;
    /** (������)�h������ **/
    public int                 SalesStayTotal;
    /** (������)���O���� **/
    public int                 SalesOtherTotal;
    /** (������)�x�e�O��ϑg�� **/
    public int                 SalesPayRestCount;
    /** (������)�h���O��ϑg�� **/
    public int                 SalesPayStayCount;
    /** (������)��g�|�C���g **/
    public int                 SalesPointTotal;

    /** (����ڍ�)�擾�J�n���t **/
    public int                 SalesDetailGetStartDate;
    /** (����ڍ�)�擾�I�����t **/
    public int                 SalesDetailGetEndDate;
    /** (����ڍ�)�x�e **/
    public int                 SalesDetailRest;
    /** (����ڍ�)�h�� **/
    public int                 SalesDetailStay;
    /** (����ڍ�)�x�e�O���� **/
    public int                 SalesDetailRestBeforeOver;
    /** (����ڍ�)�x�e�㉄�� **/
    public int                 SalesDetailRestAfterOver;
    /** (����ڍ�)�h���O���� **/
    public int                 SalesDetailStayBeforeOver;
    /** (����ڍ�)�h���㉄�� **/
    public int                 SalesDetailStayAfterOver;
    /** (����ڍ�)���H **/
    public int                 SalesDetailMeat;
    /** (����ڍ�)�o�O **/
    public int                 SalesDetailDelivery;
    /** (����ڍ�)�R���r�j **/
    public int                 SalesDetailConveni;
    /** (����ڍ�)�①�� **/
    public int                 SalesDetailRef;
    /** (����ڍ�)�}���`���f�B�A **/
    public int                 SalesDetailMulti;
    /** (����ڍ�)�̔����i **/
    public int                 SalesDetailSales;
    /** (����ڍ�)�����^�����i **/
    public int                 SalesDetailRental;
    /** (����ڍ�)�^�o�R **/
    public int                 SalesDetailCigarette;
    /** (����ڍ�)�d�b **/
    public int                 SalesDetailTel;
    /** (����ڍ�)���̑� **/
    public int                 SalesDetailEtc;
    /** (����ڍ�)������Ŋz **/
    public int                 SalesDetailStaxIn;
    /** (����ڍ�)�\���敪���X **/
    public int                 SalesDetailFiller[];
    /** (����ڍ�)���� **/
    public int                 SalesDetailDiscount;
    /** (����ڍ�)���� **/
    public int                 SalesDetailExtra;
    /** (����ڍ�)�����o�[���� **/
    public int                 SalesDetailMember;
    /** (����ڍ�)��d������ **/
    public int                 SalesDetailService;
    /** (����ڍ�)����Ŕ��� **/
    public int                 SalesDetailStax;
    /** (����ڍ�)���������� **/
    public int                 SalesDetailAdjust;
    /** (����ڍ�)�����v **/
    public int                 SalesDetailTotal;
    /** (����ڍ�)�ŗ�1���0:�K�p����(�P��0.1%,��10%��100) **/
    public int                 SalesDetailTaxRate1;
    /** (����ڍ�)�ŗ�1�ېőΏۋ��z **/
    public int                 SalesDetailTaxableAmount1;
    /** (����ڍ�)�ŗ�2���0:�K�p����(�P��0.1%,��10%��100) **/
    public int                 SalesDetailTaxRate2;
    /** (����ڍ�)�ŗ�2�ېőΏۋ��z **/
    public int                 SalesDetailTaxableAmount2;

    /** (IN/OUT�g��)�擾�J�n���t **/
    public int                 InOutGetStartDate;
    /** (IN/OUT�g��)�擾�I�����t **/
    public int                 InOutGetEndDate;
    /** (IN/OUT�g��)����(24���Œ�) **/
    public int                 InOutTime[];
    /** (IN/OUT�g��)IN�g��(24���Œ�) **/
    public int                 InOutIn[];
    /** (IN/OUT�g��)OUT�g��(24���Œ�) **/
    public int                 InOutOut[];

    /** (�������)�󖞉^�p���[�h(1:�蓮,2:����) **/
    public int                 StatusEmptyFullMode;
    /** (�������)�󖞏��(1:��,2:����) **/
    public int                 StatusEmptyFullState;
    /** (�������)�E�F�C�e�B���O�� **/
    public int                 StatusWaiting;
    /** (�������)�X�e�[�^�X����(MAX:50) **/
    public String              StatusName[];
    /** (�������)������(MAX:50) **/
    public int                 StatusCount[];

    /** (�����ڍ�)������ **/
    public int                 StatusDetailCount;
    /** (�����ڍ�)�����R�[�h(MAX:128) **/
    public int                 StatusDetailRoomCode[];
    /** (�����ڍ�)��������(MAX:128) **/
    public String              StatusDetailRoomName[];
    /** (�����ڍ�)�o�ߎ���(MAX:128) **/
    public int                 StatusDetailElapseTime[];
    /** (�����ڍ�)�X�e�[�^�X����(MAX:128) **/
    public String              StatusDetailStatusName[];
    /** (�����ڍ�)�����X�e�[�^�X�F(MAX:128) **/
    public String              StatusDetailColor[];
    /** (�����ڍ�)�����X�e�[�^�X�����F(MAX:128) **/
    public String              StatusDetailForeColor[];
    /** (�����ڍ�)�\���ʒuX(MAX:128) **/
    public int                 StatusDetailX[];
    /** (�����ڍ�)�\���ʒuY(MAX:128) **/
    public int                 StatusDetailY[];
    /** (�����ڍ�)�\���ʒuZ(MAX:128) **/
    public int                 StatusDetailZ[];
    /** (�����ڍ�)�t���A�ԍ�(MAX:128) **/
    public int                 StatusDetailFloor[];
    /** (�����ڍ�)�\�莺���K�p�敪(MAX:128) **/
    public int                 StatusDetailUserChargeMode[];

    /** (���P��IN/OUT�g��)�擾���t **/
    public int                 AddupInOutGetDate;
    /** (���P��IN/OUT�g��)����IN **/
    public int                 AddupInOutAfterIn;
    /** (���P��IN/OUT�g��)���OIN **/
    public int                 AddupInOutBeforeIn;
    /** (���P��IN/OUT�g��)��OUT **/
    public int                 AddupInOutAllOut;
    /** (���P��IN/OUT�g��)���OOUT **/
    public int                 AddupInOutBeforeOut;

    /** (������)������ **/
    public int                 StateRoomCount;
    /** (������)�����R�[�h(MAX:128) **/
    public int                 StateRoomCode[];
    /** (������)�������t(MAX:128) **/
    public int                 StateInDate[];
    /** (������)��������(MAX:128) **/
    public int                 StateInTime[];
    /** (������)�ގ����t(MAX:128) **/
    public int                 StateOutDate[];
    /** (������)�ގ�����(MAX:128) **/
    public int                 StateOutTime[];
    /** (������)���p�l��(MAX:128) **/
    public int                 StatePerson[];
    /** (������)�h�A���(MAX:128)(0:��,1:�J) **/
    public int                 StateDoor[];
    /** (������)�①�ɗ��p���(MAX:128)(0:���g�p,1:�g�p) **/
    public int                 StateRefUse[];
    /** (������)�R���r�j���p���(MAX:128)(0:���g�p,1:�g�p) **/
    public int                 StateConveniUse[];
    /** (������)���b�L�[���[��(MAX:128)(1:���b�L�[���[��) **/
    public int                 StateLucky[];
    /** (������)�����o�[ID(MAX:128) **/
    public String              StateCustomId[];
    /** (������)�j�b�N�l�[��(MAX:128) **/
    public String              StateNickName[];
    /** (������)�����o�[�����N��(MAX:128) **/
    public String              StateCustomRankName[];
    /** (������)�����o�[�C�x���g(MAX:128)(1:�C�x���g����) **/
    public int                 StateCustomEvent[];
    /** (������)�x�����(MAX:128)(1:�x������) **/
    public int                 StateCustomWarning[];
    /** (������)�A�����(MAX:128)(1:�A������) **/
    public int                 StateCustomContact[];
    /** (������)�i�i���(MAX:128)(1:�i�i����) **/
    public int                 StateCustomPresent[];
    /** (������)�\�񎞊�(MAX:128) **/
    public int                 StateReserveTime[];
    /** (������)���ԏ�ԍ�(MAX:128) **/
    public int                 StateParkingNo[];
    /** (������)���[�UID(MAX:128) **/
    public String              StateCustomUserId[];
    /** (������)�n�s�z�e�^�b�`(MAX:128) **/
    public int                 StateHapihoteTouch[];
    /** (������)�n�s�z�e�}�C���g�p��(MAX:128) **/
    public int                 StateHapihoteMile[];

    /** (�Ǘ��@��)������ **/
    public int                 EquipRoomCount;
    /** (�Ǘ��@��)�����R�[�h(MAX:128) **/
    public int                 EquipRoomCode[];
    /** (�Ǘ��@��)����敪(MAX:128)(20�Œ�) **/
    public int                 EquipActMode[][];
    /** (�Ǘ��@��)�󋵌x��(MAX:128)(20�Œ�)(0:����,1:�ُ�) **/
    public int                 EquipStatusAlarm[][];
    /** (�Ǘ��@��)�󋵃f�[�^(MAX:128)(20�Œ�) **/
    public int                 EquipStatusData[][];
    /** (�Ǘ��@��)����f�[�^(MAX:128)(20�Œ�) **/
    public int                 EquipActData[][];

    /** (���l����)������ **/
    public int                 LinenRoomCount;
    /** (���l����)�����R�[�h(MAX:128) **/
    public int                 LinenRoomCode[];
    /** (���l����)�①�ɗ��p���(MAX:128)(0:���g�p,1:�g�p) **/
    public int                 LinenRefUse[];
    /** (���l����)�R���r�j���p���(MAX:128)(0:���g�p,1:�g�p) **/
    public int                 LinenConveniUse[];
    /** (���l����)��Ɣ�(MAX:128) **/
    public String              LinenGroup[];

    /** (�����o�[��)������ **/
    public int                 MemberRoomCount;
    /** (�����o�[��)�����R�[�h(MAX:128) **/
    public int                 MemberRoomCode[];
    /** (�����o�[��)�����o�[ID(MAX:128) **/
    public String              MemberCustomId[];
    /** (�����o�[��)�j�b�N�l�[��(MAX:128) **/
    public String              MemberNickName[];
    /** (�����o�[��)����(MAX:128) **/
    public String              MemberName[];
    /** (�����o�[��)�����o�[�����N��(MAX:128) **/
    public String              MemberRankName[];
    /** (�����o�[��)�����p��(MAX:128) **/
    public int                 MemberCount[];
    /** (�����o�[��)�|�C���g(MAX:128) **/
    public int                 MemberPoint[];
    /** (�����o�[��)�|�C���g�Q(MAX:128) **/
    public int                 MemberPoint2[];
    /** (�����o�[��)�a�����P(MAX:128) **/
    public int                 MemberBirthday1[];
    /** (�����o�[��)�a�����Q(MAX:128) **/
    public int                 MemberBirthday2[];
    /** (�����o�[��)�L�O���P(MAX:128) **/
    public int                 MemberMemorial1[];
    /** (�����o�[��)�L�O���Q(MAX:128) **/
    public int                 MemberMemorial2[];
    /** (�����o�[��)�o�^��(MAX:128) **/
    public int                 MemberEntryDate[];
    /** (�����o�[��)���������L���O(MAX:128) **/
    public int                 MemberNowRanking[];
    /** (�����o�[��)�O�������L���O(MAX:128) **/
    public int                 MemberOldRanking[];
    /** (�����o�[��)�����L���O�����p��(MAX:128) **/
    public int                 MemberRankingCount[];
    /** (�����o�[��)�W�v���ԓ����p��(MAX:128) **/
    public int                 MemberAddupCount[];
    /** (�����o�[��)�����L���O�����p���z(MAX:128) **/
    public int                 MemberRankingTotal[];
    /** (�����o�[��)�W�v���ԓ����p���z(MAX:128) **/
    public int                 MemberAddupTotal[];
    /** (�����o�[��)�J�z���p���z(MAX:128) **/
    public int                 MemberSurplus[];
    /** (�����o�[��)�����o�[�C�x���g(MAX:128)(1:�C�x���g����) **/
    public int                 MemberEvent[];
    /** (�����o�[��)�x�����(MAX:128)(1:�x������) **/
    public int                 MemberWarning[];
    /** (�����o�[��)�A�����(MAX:128)(1:�A������) **/
    public int                 MemberContact[];
    /** (�����o�[��)�i�i���(MAX:128)(1:�i�i����) **/
    public int                 MemberPresent[];
    /** (�����o�[��)�C�x���g���(MAX:128)(10�Œ�) **/
    public int                 MemberEventInfo[][];
    /** (�����o�[��)�A�������P(MAX:128) **/
    public String              MemberContact1[];
    /** (�����o�[��)�A�������Q(MAX:128) **/
    public String              MemberContact2[];
    /** (�����o�[��)�x�������P(MAX:128) **/
    public String              MemberWarning1[];
    /** (�����o�[��)�x�������Q(MAX:128) **/
    public String              MemberWarning2[];
    /** (�����o�[��)���[�UID(MAX:128) **/
    public String              MemberUserId[];

    /** (�Ԕԏ�)������ **/
    public int                 CarRoomCount;
    /** (�Ԕԏ�)�����R�[�h(MAX:128) **/
    public int                 CarRoomCode[];
    /** (�Ԕԏ�)�Ԕ�(�n��)(MAX:128) **/
    public String              CarArea[];
    /** (�Ԕԏ�)�Ԕ�(���)(MAX:128) **/
    public String              CarKind[];
    /** (�Ԕԏ�)�Ԕ�(�Ԏ�)(MAX:128) **/
    public String              CarType[];
    /** (�Ԕԏ�)�Ԕ�(�Ԕ�)(MAX:128) **/
    public String              CarNo[];
    /** (�Ԕԏ�)���ԏ�ԍ�(MAX:128) **/
    public int                 CarParkingNo[];

    /** (���Z�@��)������ **/
    public int                 TexRoomCount;
    /** (���Z�@��)�����R�[�h(MAX:128) **/
    public int                 TexRoomCode[];
    /** (���Z�@��)���Z�@���[�h(MAX:128) **/
    public int                 TexMode[];
    /** (���Z�@��)��[���(MAX:128) **/
    public int                 TexSupplyStat[];
    /** (���Z�@��)�Z�L�����e�B���(MAX:128) **/
    public int                 TexSecurityStat[];
    /** (���Z�@��)�����(MAX:128) **/
    public int                 TexDoorStat[];
    /** (���Z�@��)������(MAX:128) **/
    public int                 TexLineStat[];
    /** (���Z�@��)�G���[���(MAX:128) **/
    public int                 TexErrorStat[];
    /** (���Z�@��)���Z���(MAX:128) **/
    public int                 TexPayStat[];
    /** (���Z�@��)�ޑK���(MAX:128) **/
    public int                 TexChargeStat[];
    /** (���Z�@��)�G���[�R�[�h(MAX:128) **/
    public String              TexErrorCode[];
    /** (���Z�@��)�G���[���e(MAX:128) **/
    public String              TexErrorMsg[];
    /** (���Z�@��)������z�f�[�^�i���z�j(MAX:128)(0:����,1:�N���W�b�g,2:�\���P,3:�\���Q) **/
    public int                 TexSalesTotal[][];
    /** (���Z�@��)������z�f�[�^�i�񐔁j(MAX:128)(0:����,1:�N���W�b�g,2:�\���P,3:�\���Q) **/
    public int                 TexSalesCount[][];
    /** (���Z�@��)���ɏ��f�[�^�i�����j(MAX:128)(0:�g�[�^������,1:�g�[�^���o��,2:�g�[�^�����o������,3:�c����) **/
    public int                 TexSafeCount[][][];
    /** (���Z�@��)���ɏ��f�[�^�i���v���z�j(MAX:128)(0:�g�[�^������,1:�g�[�^���o��,2:�g�[�^�����o������,3:�c����) **/
    public int                 TexSafeTotal[][];
    /** (���Z�@��)���ɖ�����(MAX:128)(16����) **/
    public int                 TexSafeStat[][];
    /** (���Z�@��)����������[���z(MAX:128) **/
    public int                 TexSupplyTotal[];
    /** (���Z�@��)���������[���t(MAX:128) **/
    public int                 TexSupplyDate[];
    /** (���Z�@��)����������[����(MAX:128) **/
    public int                 TexSupplyTime[];
    /** (���Z�@��)�]���(MAX:128) **/
    public int                 TexSurplus[];
    /** (���Z�@��)�J�[�h���(MAX:128) **/
    public int                 TexCardStat[];

    /** (�}���`���f�B�A��)������ **/
    public int                 MultiRoomCount;
    /** (�}���`���f�B�A��)�����R�[�h(MAX:128) **/
    public int                 MultiRoomCode[];
    /** (�}���`���f�B�A��)������(MAX:128) **/
    public int                 MultiLineStat[];
    /** (�}���`���f�B�A��)�G���[���(MAX:128) **/
    public int                 MultiErrorStat[];
    /** (�}���`���f�B�A��)�d�����(MAX:128) **/
    public int                 MultiPowerStat[];
    /** (�}���`���f�B�A��)�G���[�R�[�h(MAX:128) **/
    public String              MultiErrorCode[];
    /** (�}���`���f�B�A��)�G���[���e(MAX:128) **/
    public String              MultiErrorMsg[];
    /** (�}���`���f�B�A��)�����`�����l���ԍ�(MAX:128) **/
    public int                 MultiChannelNo[];
    /** (�}���`���f�B�A��)�����`�����l������(MAX:128) **/
    public String              MultiChannelName[];

    /** (�����ڍׁE���p����)�����R�[�h **/
    public int                 DetailUseRoomCode;
    /** (�����ڍׁE���p����)���p���א� **/
    public int                 DetailUseCount;
    /** (�����ڍׁE���p����)���p���ז�(MAX:100) **/
    public String              DetailUseGoodsName[];
    /** (�����ڍׁE���p����)����(MAX:100) **/
    public int                 DetailUseGoodsCount[];
    /** (�����ڍׁE���p����)���K�P��(MAX:100) **/
    public int                 DetailUseGoodsRegularPrice[];
    /** (�����ڍׁE���p����)�P��(MAX:100) **/
    public int                 DetailUseGoodsPrice[];
    /** (�����ڍׁE���p����)������(MAX:100) **/
    public int                 DetailUseGoodsDiscount[];

    /** (�����ڍׁE�x������)�����R�[�h **/
    public int                 DetailPayRoomCode;
    /** (�����ڍׁE�x������)���p���v **/
    public int                 DetailPayTotal;
    /** (�����ڍׁE�x������)�������z **/
    public int                 DetailPayClaim;
    /** (�����ڍׁE�x������)�x�������א� **/
    public int                 DetailPayCount;
    /** (�����ڍׁE�x������)���p���ז�(MAX:100) **/
    public String              DetailPayName[];
    /** (�����ڍׁE�x������)����(MAX:100) **/
    public int                 DetailPayAmount[];
    /** (�����ڍׁE�x������)���z(MAX:100) **/
    public int                 DetailPayMoney[];

    /** (�����ڍׁE���i����)�����R�[�h **/
    public int                 DetailGoodsRoomCode;
    /** (�����ڍׁE���i����)���i���א� **/
    public int                 DetailGoodsCount;
    /** (�����ڍׁE���i����)���p���ז�(MAX:100) **/
    public String              DetailGoodsName[];
    /** (�����ڍׁE���i����)����(MAX:100) **/
    public int                 DetailGoodsAmount[];
    /** (�����ڍׁE���i����)�P��(MAX:100) **/
    public int                 DetailGoodsPrice[];
    /** (�����ڍׁE���i����)�①�Ƀt���O(MAX:100)(1:�①��(�R���r�j),2:����,3:�蓮) **/
    public int                 DetailGoodsRef[];

    /** (����ڕW)�v��N�� **/
    public int                 TargetMonth;
    /** (����ڕW)�݌v�g�� **/
    public int                 TargetCount;
    /** (����ڕW)�݌v����z **/
    public int                 TargetTotal;
    /** (����ڕW)�������[�h�� **/
    public int                 TargetModeCount;
    /** (����ڕW)�������[�h(MAX:99) **/
    public int                 TargetModeCode[];
    /** (����ڕW)�������[�h����(MAX:99) **/
    public String              TargetModeName[];
    /** (����ڕW)�x�e�g��(MAX:99) **/
    public int                 TargetModeRestCount[];
    /** (����ڕW)�h���g��(MAX:99) **/
    public int                 TargetModeStayCount[];
    /** (����ڕW)�x�e������z(MAX:99) **/
    public int                 TargetModeRestTotal[];
    /** (����ڕW)�h��������z(MAX:99) **/
    public int                 TargetModeStayTotal[];

    /** (�������)�v��N�� **/
    public int                 ResultMonth;
    /** (�������)�݌v�g�� **/
    public int                 ResultCount;
    /** (�������)�݌v����z **/
    public int                 ResultTotal;
    /** (�������)�������[�h�� **/
    public int                 ResultModeCount;
    /** (�������)�������[�h(MAX:99) **/
    public int                 ResultModeCode[];
    /** (�������)�������[�h����(MAX:99) **/
    public String              ResultModeName[];
    /** (�������)�x�e�g��(MAX:99) **/
    public int                 ResultModeRestCount[];
    /** (�������)�h���g��(MAX:99) **/
    public int                 ResultModeStayCount[];
    /** (�������)�x�e������z(MAX:99) **/
    public int                 ResultModeRestTotal[];
    /** (�������)�h��������z(MAX:99) **/
    public int                 ResultModeStayTotal[];

    /** (����C�x���g)���y�[�W�擾���t **/
    public int                 EventGetNextDate;
    /** (����C�x���g)���y�[�W�擾���� **/
    public int                 EventGetNextTime;
    /** (����C�x���g)���y�[�W�����⏕ **/
    public int                 EventGetNextTimeSub;
    /** (����C�x���g)���y�[�W�����⏕ **/
    public int                 EventGetNextRoomCode;
    /** (����C�x���g)���y�[�W�C�x���g�R�[�h **/
    public int                 EventGetNextEventCode;
    /** (����C�x���g)�O�y�[�W�擾���t **/
    public int                 EventGetPrevDate;
    /** (����C�x���g)�O�y�[�W�擾���� **/
    public int                 EventGetPrevTime;
    /** (����C�x���g)�O�y�[�W�����⏕ **/
    public int                 EventGetPrevTimeSub;
    /** (����C�x���g)�O�y�[�W�����⏕ **/
    public int                 EventGetPrevRoomCode;
    /** (����C�x���g)�O�y�[�W�C�x���g�R�[�h **/
    public int                 EventGetPrevEventCode;
    /** (����C�x���g)�擾���� **/
    public int                 EventCount;
    /** (����C�x���g)���t(40���Œ�) **/
    public int                 EventDate[];
    /** (����C�x���g)����(40���Œ�) **/
    public int                 EventTime[];
    /** (����C�x���g)�����⏕(40���Œ�) **/
    public int                 EventTimeSub[];
    /** (����C�x���g)�����R�[�h(40���Œ�) **/
    public int                 EventRoomCode[];
    /** (����C�x���g)��������(40���Œ�) **/
    public String              EventRoomName[];
    /** (����C�x���g)�n���ԍ�(40���Œ�) **/
    public int                 EventLineNo[];
    /** (����C�x���g)�[��ID(40���Œ�) **/
    public int                 EventTermId[];
    /** (����C�x���g)�]�ƈ��R�[�h(40���Œ�) **/
    public int                 EventEmployeeCode[];
    /** (����C�x���g)�C�x���g�R�[�h(40���Œ�) **/
    public int                 EventEventCode[];
    /** (����C�x���g)�V�X�e���G���[�R�[�h(40���Œ�) **/
    public int                 EventSystemErrCode[];
    /** (����C�x���g)�t�я��(40���Œ�)(6�Œ�) **/
    public int                 EventNumData[][];
    /** (����C�x���g)�t�ѕ������(40���Œ�) **/
    public String              EventStrData[];

    /** (�I�[�g�J�����_�[)�擾���t **/
    public int                 CalGetDate;
    /** (�I�[�g�J�����_�[)�������[�h�� **/
    public int                 CalModeCount;
    /** (�I�[�g�J�����_�[)�������[�h�ԍ�(20���Œ�) **/
    public int                 CalModeCode[];
    /** (�I�[�g�J�����_�[)�������[�h����(20���Œ�) **/
    public String              CalModeName[];
    /** (�I�[�g�J�����_�[)���t(31���Œ�) **/
    public int                 CalDayDate[];
    /** (�I�[�g�J�����_�[)�������[�h(31���Œ�) **/
    public int                 CalDayMode[];
    /** (�I�[�g�J�����_�[)�������[�h����(31���Œ�) **/
    public String              CalDayModeName[];
    /** (�I�[�g�J�����_�[)�j�����(31���Œ�)(1:���`7:�y) **/
    public int                 CalDayWeekKind[];
    /** (�I�[�g�J�����_�[)�x�����(31���Œ�)(1:����,2:�x��(�j�Փ�),3:�x�O��) **/
    public int                 CalDayHolidayKind[];
    /** (�I�[�g�J�����_�[)���L�����P(31���Œ�) **/
    public String              CalDayMemo1[];
    /** (�I�[�g�J�����_�[)���L�����Q(31���Œ�) **/
    public String              CalDayMemo2[];

    /** (�����󋵗���)�擾���t **/
    public int                 RoomHistoryDate;
    /** (�����󋵗���)�������� **/
    public int                 RoomHistoryRoomCount;
    /** (�����󋵗���)�擾���� **/
    public int                 RoomHistoryCount;
    /** (�����󋵗���)����(24���Œ�) **/
    public int                 RoomHistoryTime[];
    /** (�����󋵗���)�󎺐�(24���Œ�) **/
    public int                 RoomHistoryEmpty[];
    /** (�����󋵗���)�ݎ���(24���Œ�) **/
    public int                 RoomHistoryExist[];
    /** (�����󋵗���)������(24���Œ�) **/
    public int                 RoomHistoryClean[];
    /** (�����󋵗���)���~��(24���Œ�) **/
    public int                 RoomHistoryStop[];

    /** (�����o�[�����N)�����o�[�����N�� **/
    public int                 CustomRankCount;
    /** (�����o�[�����N)�����o�[�����N�R�[�h **/
    public int                 CustomRankCode[];
    /** (�����o�[�����N)�����o�[�����N���� **/
    public String              CustomRankName[];

    /** (���Z�@���O)���O���x�� **/
    public int                 TexlogGetLogLevel;
    /** (���Z�@���O)���y�[�W�������t **/
    public int                 TexlogNextDate;
    /** (���Z�@���O)���y�[�W�������� **/
    public int                 TexlogNextTime;
    /** (���Z�@���O)���y�[�W�����R�[�h **/
    public int                 TexlogNextRoomCode;
    /** (���Z�@���O)���y�[�W�n���ԍ� **/
    public int                 TexlogNextLineNo;
    /** (���Z�@���O)���y�[�W�[��ID **/
    public int                 TexlogNextTermId;
    /** (���Z�@���O)���y�[�W�������t **/
    public int                 TexlogPrevDate;
    /** (���Z�@���O)���y�[�W�������� **/
    public int                 TexlogPrevTime;
    /** (���Z�@���O)���y�[�W�����R�[�h **/
    public int                 TexlogPrevRoomCode;
    /** (���Z�@���O)���y�[�W�n���ԍ� **/
    public int                 TexlogPrevLineNo;
    /** (���Z�@���O)���y�[�W�[��ID **/
    public int                 TexlogPrevTermId;
    /** (���Z�@���O)���O���� **/
    public int                 TexlogCount;
    /** (���Z�@���O)�������t **/
    public int                 TexlogDate[];
    /** (���Z�@���O)�������� **/
    public int                 TexlogTime[];
    /** (���Z�@���O)�����R�[�h **/
    public int                 TexlogRoomCode[];
    /** (���Z�@���O)�������� **/
    public String              TexlogRoomName[];
    /** (���Z�@���O)�n���ԍ� **/
    public int                 TexlogLineNo[];
    /** (���Z�@���O)�[��ID **/
    public int                 TexlogTermId[];
    /** (���Z�@���O)�������z **/
    public int                 TexlogClaimed[];
    /** (���Z�@���O)�]��� **/
    public int                 TexlogSurplus[];
    /** (���Z�@���O)���ɖ��� **/
    public int                 TexlogSafeCount[][][];
    /** (���Z�@���O)���ɍ��v���z **/
    public int                 TexlogSafeTotal[][];
    /** (���Z�@���O)���O���x�� **/
    public int                 TexlogLogLevel[];
    /** (���Z�@���O)���O���e **/
    public String              TexlogLogContent[];
    /** (���Z�@���O)���O�ڍ� **/
    public String              TexlogLogDetail[];
    /** (���Z�@���O)������ **/
    public int                 TexlogStatTrade[];
    /** (���Z�@���O)���o����� **/
    public int                 TexlogStatInOut[];
    /** (���Z�@���O)�Z�L�����e�B��� **/
    public int                 TexlogStatSecurity[];

    /** (����ڍ�)�擾�J�n���t **/
    public int                 AscSalesDetailGetStartDate;
    /** (����ڍ�)�擾�I�����t **/
    public int                 AscSalesDetailGetEndDate;
    /** (����ڍ�)�h�� **/
    public int                 AscSalesDetailStay;
    /** (����ڍ�)�h���O���� **/
    public int                 AscSalesDetailStayBeforeOver;
    /** (����ڍ�)�h���㉄�� **/
    public int                 AscSalesDetailStayAfterOver;
    /** (����ڍ�)�x�e **/
    public int                 AscSalesDetailRest;
    /** (����ڍ�)�x�e�㉄�� **/
    public int                 AscSalesDetailRestOver;
    /** (����ڍ�)�d�b **/
    public int                 AscSalesDetailTel;
    /** (����ڍ�)���֋� **/
    public int                 AscSalesDetailAdvance;
    /** (����ڍ�)���v **/
    public int                 AscSalesDetailSubTotal;
    /** (����ڍ�)��d������ **/
    public int                 AscSalesDetailService;
    /** (����ڍ�)�ŋ��i�O�Łj **/
    public int                 AscSalesDetailTaxOut;
    /** (����ڍ�)�ŋ��i���Łj **/
    public int                 AscSalesDetailTaxIn;
    /** (����ڍ�)���� **/
    public int                 AscSalesDetailDiscount;
    /** (����ڍ�)���� **/
    public int                 AscSalesDetailExtra;
    /** (����ڍ�)�����o�[���� **/
    public int                 AscSalesDetailMember;
    /** (����ڍ�)���������� **/
    public int                 AscSalesDetailAdjust;
    /** (����ڍ�)�\���敪��6 **/
    public int                 AscSalesDetailFiller[];
    /** (����ڍ�)�����v **/
    public int                 AscSalesDetailTotal;

    /** (����ڍ�)�ϖ��ז��א� **/
    public int                 AscSalesDetailCount;
    /** (����ڍ�)�ϖ��ז��ז��́��P�O **/
    public String              AscSalesDetailName[];
    /** (����ڍ�)�ϖ��׋��z���P�O **/
    public int                 AscSalesDetailAmount[];

    /** (�t�����g���Z�@��)�[���R�[�h(IN) **/
    public int                 FrontTexTermCodeIn;
    /** (�t�����g���Z�@��)�[���� **/
    public int                 FrontTexTermCount;
    /** (�t�����g���Z�@��)�[���R�[�h(MAX:128) **/
    public int                 FrontTexTermCode[];
    /** (�t�����g���Z�@��)�[������(MAX:128) **/
    public String              FrontTexTermName[];
    /** (�t�����g���Z�@��)�戵���(MAX:128) **/
    public int                 FrontTexServiceStat[];
    /** (�t�����g���Z�@��)�L�[SW���(MAX:128) **/
    public int                 FrontTexKeySwStat[];
    /** (�t�����g���Z�@��)�Z�L�����e�B���(MAX:128) **/
    public int                 FrontTexSecurityStat[];
    /** (�t�����g���Z�@��)�����(MAX:128) **/
    public int                 FrontTexDoorStat[];
    /** (�t�����g���Z�@��)������(MAX:128) **/
    public int                 FrontTexLineStat[];
    /** (�t�����g���Z�@��)�G���[���(MAX:128) **/
    public int                 FrontTexErrorStat[];
    /** (�t�����g���Z�@��)�G���[�R�[�h(MAX:128) **/
    public String              FrontTexErrorCode[];
    /** (�t�����g���Z�@��)�G���[���e(MAX:128) **/
    public String              FrontTexErrorMsg[];
    /** (�t�����g���Z�@��)������z�f�[�^�i���z�j(MAX:128)(0:����,1:�N���W�b�g,2:�\���P,3:�\���Q) **/
    public int                 FrontTexSalesTotal[][];
    /** (�t�����g���Z�@��)������z�f�[�^�i�񐔁j(MAX:128)(0:����,1:�N���W�b�g,2:�\���P,3:�\���Q) **/
    public int                 FrontTexSalesCount[][];
    /** (�t�����g���Z�@��)���ɏ��f�[�^�i�����j(MAX:128)(0:�g�[�^������,1:�g�[�^���o��,2:�g�[�^�����o������,3:�c����) **/
    public int                 FrontTexSafeCount[][][];
    /** (�t�����g���Z�@��)���ɏ��f�[�^�i���v���z�j(MAX:128)(0:�g�[�^������,1:�g�[�^���o��,2:�g�[�^�����o������,3:�c����) **/
    public int                 FrontTexSafeTotal[][];
    /** (�t�����g���Z�@��)���ɖ�����(MAX:128)(16����) **/
    public int                 FrontTexSafeStat[][];

    /** (�����X�e�[�^�X�ڍבJ��)�����R�[�h **/
    public int                 TimeChartRoomCodeOne;
    /** (�����X�e�[�^�X�ڍבJ��)�^�C���`���[�g����� **/
    public int                 TimeChartStartTime;
    /** (�����X�e�[�^�X�ڍבJ��)�X�e�[�^�X���́i52�Œ�j **/
    public String              TimeChartStatusName[];
    /** (�����X�e�[�^�X�ڍבJ��)�X�e�[�^�X�F�i52�Œ�j **/
    public String              TimeChartStatusColor[];
    /** (�����X�e�[�^�X�ڍבJ��)�X�e�[�^�X�����F�i52�Œ�j **/
    public String              TimeChartStatusForeColor[];
    /** (�����X�e�[�^�X�ڍבJ��)������ **/
    public int                 TimeChartRoomCount;
    /** (�����X�e�[�^�X�ڍבJ��)�����R�[�h�iMAX128�j **/
    public int                 TimeChartRoomCode[];
    /** (�����X�e�[�^�X�ڍבJ��)�������́iMAX128�j **/
    public String              TimeChartRoomName[];
    /** (�����X�e�[�^�X�ڍבJ��)�t���A�ԍ��iMAX128�j **/
    public int                 TimeChartRoomFloor[];
    /** (�����X�e�[�^�X�ڍבJ��)�����X�e�[�^�X�iMAX128�j�i144�Œ�j **/
    public int                 TimeChartRoomStatus[][];

    /** (�ϖ��̔��㖾��)�擾�J�n���t **/
    public int                 ManualSalesDetailGetStartDate;
    /** (�ϖ��̔��㖾��)�擾�I�����t **/
    public int                 ManualSalesDetailGetEndDate;
    /** (�ϖ��̔��㖾��)�ŋ��i���Łj **/
    public int                 ManualSalesDetailTaxIn;
    /** (�ϖ��̔��㖾��)�����v **/
    public int                 ManualSalesDetailTotal;
    /** (�ϖ��̔��㖾��)�ϖ��ז��א� **/
    public int                 ManualSalesDetailCount;
    /** (�ϖ��̔��㖾��)�ϖ��ז��ז��� **/
    public String              ManualSalesDetailName[];
    /** (�ϖ��̔��㖾��)�ϖ��׋��z **/
    public int                 ManualSalesDetailAmount[];

    /** ���O�o�̓N���X **/
    private LogLib             log;

    /**
     * �I�[�i�[�T�C�g�֘A���f�[�^�̏��������s���܂�
     * 
     */
    public OwnerInfo()
    {
        // ���ʍ���
        Result = 0;
        HotelId = "";
        TermId = "ceritfiedid";
        Password = "6268";
        Addupdate = 0;
        Name = "";
        RoomCode = 0;
        EmployeeName = "";
        ModeName = "";

        // DB���ʍ���
        DbLoginUser = "";
        DbUserName = "";
        DbPassword = "";
        DbUserLevel = 0;
        DbUserId = 0;

        // �I�[�i�[���O�C������
        SystemKind = "";
        SystemVer1 = 0;
        SystemVer2 = 0;
        SystemVer3 = 0;
        SystemVer4 = 0;
        SystemCustomerKind = "";

        // ������
        SalesGetStartDate = 0;
        SalesGetEndDate = 0;
        SalesTex = 0;
        SalesTexCredit = 0;
        SalesFront = 0;
        SalesFrontCredit = 0;
        SalesTotal = 0;
        SalesRestCount = 0;
        SalesStayCount = 0;
        SalesTotalCount = 0;
        SalesNowCheckin = 0;
        SalesRestRate = 0;
        SalesStayRate = 0;
        SalesTotalRate = 0;
        SalesRestPrice = 0;
        SalesStayPrice = 0;
        SalesTotalPrice = 0;
        SalesVisitorPrice = 0;
        SalesMemberPrice = 0;
        SalesRoomPrice = 0;
        SalesRoomTotalPrice = 0;
        SalesRestTotal = 0;
        SalesStayTotal = 0;
        SalesOtherTotal = 0;
        SalesPayRestCount = 0;
        SalesPayStayCount = 0;
        SalesPointTotal = 0;

        // ����ڍ�
        SalesDetailGetStartDate = 0;
        SalesDetailGetEndDate = 0;
        SalesDetailRest = 0;
        SalesDetailStay = 0;
        SalesDetailRestBeforeOver = 0;
        SalesDetailRestAfterOver = 0;
        SalesDetailStayBeforeOver = 0;
        SalesDetailStayAfterOver = 0;
        SalesDetailMeat = 0;
        SalesDetailDelivery = 0;
        SalesDetailConveni = 0;
        SalesDetailRef = 0;
        SalesDetailMulti = 0;
        SalesDetailSales = 0;
        SalesDetailRental = 0;
        SalesDetailCigarette = 0;
        SalesDetailTel = 0;
        SalesDetailEtc = 0;
        SalesDetailStaxIn = 0;
        SalesDetailFiller = new int[9];
        SalesDetailDiscount = 0;
        SalesDetailExtra = 0;
        SalesDetailMember = 0;
        SalesDetailService = 0;
        SalesDetailStax = 0;
        SalesDetailAdjust = 0;
        SalesDetailTotal = 0;
        SalesDetailTaxRate1 = 0;
        SalesDetailTaxableAmount1 = 0;
        SalesDetailTaxRate2 = 0;
        SalesDetailTaxableAmount2 = 0;

        // IN/OUT�g��
        InOutGetStartDate = 0;
        InOutGetEndDate = 0;
        InOutTime = new int[OWNERINFO_TIMEHOURMAX];
        InOutIn = new int[OWNERINFO_TIMEHOURMAX];
        InOutOut = new int[OWNERINFO_TIMEHOURMAX];

        // �������
        StatusEmptyFullMode = 0;
        StatusEmptyFullState = 0;
        StatusWaiting = 0;
        StatusName = new String[OWNERINFO_ROOMSTATUSMAX];
        StatusCount = new int[OWNERINFO_ROOMSTATUSMAX];

        // �����ڍ�
        StatusDetailCount = 0;
        StatusDetailRoomCode = new int[OWNERINFO_ROOMMAX];
        StatusDetailRoomName = new String[OWNERINFO_ROOMMAX];
        StatusDetailElapseTime = new int[OWNERINFO_ROOMMAX];
        StatusDetailStatusName = new String[OWNERINFO_ROOMMAX];
        StatusDetailColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailForeColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailX = new int[OWNERINFO_ROOMMAX];
        StatusDetailY = new int[OWNERINFO_ROOMMAX];
        StatusDetailZ = new int[OWNERINFO_ROOMMAX];
        StatusDetailFloor = new int[OWNERINFO_ROOMMAX];

        // ���P��IN/OUT�g��
        AddupInOutGetDate = 0;
        AddupInOutAfterIn = 0;
        AddupInOutBeforeIn = 0;
        AddupInOutAllOut = 0;
        AddupInOutBeforeOut = 0;

        // ������
        StateRoomCount = 0;
        StateRoomCode = new int[OWNERINFO_ROOMMAX];
        StateInDate = new int[OWNERINFO_ROOMMAX];
        StateInTime = new int[OWNERINFO_ROOMMAX];
        StateOutDate = new int[OWNERINFO_ROOMMAX];
        StateOutTime = new int[OWNERINFO_ROOMMAX];
        StatePerson = new int[OWNERINFO_ROOMMAX];
        StateDoor = new int[OWNERINFO_ROOMMAX];
        StateRefUse = new int[OWNERINFO_ROOMMAX];
        StateConveniUse = new int[OWNERINFO_ROOMMAX];
        StateLucky = new int[OWNERINFO_ROOMMAX];
        StateCustomId = new String[OWNERINFO_ROOMMAX];
        StateNickName = new String[OWNERINFO_ROOMMAX];
        StateCustomRankName = new String[OWNERINFO_ROOMMAX];
        StateCustomEvent = new int[OWNERINFO_ROOMMAX];
        StateCustomWarning = new int[OWNERINFO_ROOMMAX];
        StateCustomContact = new int[OWNERINFO_ROOMMAX];
        StateCustomPresent = new int[OWNERINFO_ROOMMAX];
        StateReserveTime = new int[OWNERINFO_ROOMMAX];
        StateParkingNo = new int[OWNERINFO_ROOMMAX];
        StateCustomUserId = new String[OWNERINFO_ROOMMAX];
        StateHapihoteTouch = new int[OWNERINFO_ROOMMAX];
        StateHapihoteMile = new int[OWNERINFO_ROOMMAX];

        // �Ǘ��@��
        EquipRoomCount = 0;
        EquipRoomCode = new int[OWNERINFO_ROOMMAX];
        EquipActMode = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusAlarm = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipActData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];

        // ���l����
        LinenRoomCount = 0;
        LinenRoomCode = new int[OWNERINFO_ROOMMAX];
        LinenRefUse = new int[OWNERINFO_ROOMMAX];
        LinenConveniUse = new int[OWNERINFO_ROOMMAX];
        LinenGroup = new String[OWNERINFO_ROOMMAX];

        // �����o�[��
        MemberRoomCount = 0;
        MemberRoomCode = new int[OWNERINFO_ROOMMAX];
        MemberCustomId = new String[OWNERINFO_ROOMMAX];
        MemberNickName = new String[OWNERINFO_ROOMMAX];
        MemberName = new String[OWNERINFO_ROOMMAX];
        MemberRankName = new String[OWNERINFO_ROOMMAX];
        MemberCount = new int[OWNERINFO_ROOMMAX];
        MemberPoint = new int[OWNERINFO_ROOMMAX];
        MemberPoint2 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday1 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday2 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial1 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial2 = new int[OWNERINFO_ROOMMAX];
        MemberEntryDate = new int[OWNERINFO_ROOMMAX];
        MemberNowRanking = new int[OWNERINFO_ROOMMAX];
        MemberOldRanking = new int[OWNERINFO_ROOMMAX];
        MemberRankingCount = new int[OWNERINFO_ROOMMAX];
        MemberAddupCount = new int[OWNERINFO_ROOMMAX];
        MemberRankingTotal = new int[OWNERINFO_ROOMMAX];
        MemberAddupTotal = new int[OWNERINFO_ROOMMAX];
        MemberSurplus = new int[OWNERINFO_ROOMMAX];
        MemberEvent = new int[OWNERINFO_ROOMMAX];
        MemberWarning = new int[OWNERINFO_ROOMMAX];
        MemberContact = new int[OWNERINFO_ROOMMAX];
        MemberPresent = new int[OWNERINFO_ROOMMAX];
        MemberEventInfo = new int[OWNERINFO_ROOMMAX][OWNERINFO_MEMBEREVENTMAX];
        MemberContact1 = new String[OWNERINFO_ROOMMAX];
        MemberContact2 = new String[OWNERINFO_ROOMMAX];
        MemberWarning1 = new String[OWNERINFO_ROOMMAX];
        MemberWarning2 = new String[OWNERINFO_ROOMMAX];
        MemberUserId = new String[OWNERINFO_ROOMMAX];

        // �Ԕԏ�
        CarRoomCount = 0;
        CarRoomCode = new int[OWNERINFO_ROOMMAX];
        CarArea = new String[OWNERINFO_ROOMMAX];
        CarKind = new String[OWNERINFO_ROOMMAX];
        CarType = new String[OWNERINFO_ROOMMAX];
        CarNo = new String[OWNERINFO_ROOMMAX];
        CarParkingNo = new int[OWNERINFO_ROOMMAX];

        // ���Z�@��
        TexRoomCount = 0;
        TexRoomCode = new int[OWNERINFO_ROOMMAX];
        TexMode = new int[OWNERINFO_ROOMMAX];
        TexSupplyStat = new int[OWNERINFO_ROOMMAX];
        TexSecurityStat = new int[OWNERINFO_ROOMMAX];
        TexDoorStat = new int[OWNERINFO_ROOMMAX];
        TexLineStat = new int[OWNERINFO_ROOMMAX];
        TexErrorStat = new int[OWNERINFO_ROOMMAX];
        TexPayStat = new int[OWNERINFO_ROOMMAX];
        TexChargeStat = new int[OWNERINFO_ROOMMAX];
        TexErrorCode = new String[OWNERINFO_ROOMMAX];
        TexErrorMsg = new String[OWNERINFO_ROOMMAX];
        TexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX];
        TexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSupplyTotal = new int[OWNERINFO_ROOMMAX];
        TexSupplyDate = new int[OWNERINFO_ROOMMAX];
        TexSupplyTime = new int[OWNERINFO_ROOMMAX];
        TexSurplus = new int[OWNERINFO_ROOMMAX];
        TexCardStat = new int[OWNERINFO_ROOMMAX];

        // �}���`���f�B�A��
        MultiRoomCount = 0;
        MultiRoomCode = new int[OWNERINFO_ROOMMAX];
        MultiLineStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorStat = new int[OWNERINFO_ROOMMAX];
        MultiPowerStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorCode = new String[OWNERINFO_ROOMMAX];
        MultiErrorMsg = new String[OWNERINFO_ROOMMAX];
        MultiChannelNo = new int[OWNERINFO_ROOMMAX];
        MultiChannelName = new String[OWNERINFO_ROOMMAX];

        // �����ڍׁE���p����
        DetailUseRoomCode = 0;
        DetailUseCount = 0;
        DetailUseGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailUseGoodsCount = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsRegularPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsDiscount = new int[OWNERINFO_DETAILMAX];

        // �����ڍׁE�x������
        DetailPayRoomCode = 0;
        DetailPayTotal = 0;
        DetailPayClaim = 0;
        DetailPayCount = 0;
        DetailPayName = new String[OWNERINFO_DETAILMAX];
        DetailPayAmount = new int[OWNERINFO_DETAILMAX];
        DetailPayMoney = new int[OWNERINFO_DETAILMAX];

        // �����ڍׁE���i����
        DetailGoodsRoomCode = 0;
        DetailGoodsCount = 0;
        DetailGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailGoodsAmount = new int[OWNERINFO_DETAILMAX];
        DetailGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailGoodsRef = new int[OWNERINFO_DETAILMAX];

        // ����ڕW
        TargetMonth = 0;
        TargetCount = 0;
        TargetTotal = 0;
        TargetModeCount = 0;
        TargetModeCode = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeName = new String[OWNERINFO_SALESTAGETMAX];
        TargetModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        // �������
        ResultMonth = 0;
        ResultCount = 0;
        ResultTotal = 0;
        ResultModeCount = 0;
        ResultModeCode = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeName = new String[OWNERINFO_SALESTAGETMAX];
        ResultModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        ResultModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        // ����C�x���g
        EventGetNextDate = 0;
        EventGetNextTime = 0;
        EventGetNextTimeSub = 0;
        EventGetNextRoomCode = 0;
        EventGetNextEventCode = 0;
        EventGetPrevDate = 0;
        EventGetPrevTime = 0;
        EventGetPrevTimeSub = 0;
        EventGetPrevRoomCode = 0;
        EventGetPrevEventCode = 0;
        EventCount = 0;
        EventDate = new int[OWNERINFO_DOORDETAILMAX];
        EventTime = new int[OWNERINFO_DOORDETAILMAX];
        EventTimeSub = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomCode = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomName = new String[OWNERINFO_DOORDETAILMAX];
        EventLineNo = new int[OWNERINFO_DOORDETAILMAX];
        EventTermId = new int[OWNERINFO_DOORDETAILMAX];
        EventEmployeeCode = new int[OWNERINFO_DOORDETAILMAX];
        EventEventCode = new int[OWNERINFO_DOORDETAILMAX];
        EventSystemErrCode = new int[OWNERINFO_DOORDETAILMAX];
        EventNumData = new int[OWNERINFO_DOORDETAILMAX][6];
        EventStrData = new String[OWNERINFO_DOORDETAILMAX];

        // �I�[�g�J�����_�[
        CalGetDate = 0;
        CalModeCount = 0;
        CalModeCode = new int[OWNERINFO_CHAGEMODEMAX];
        CalModeName = new String[OWNERINFO_CHAGEMODEMAX];
        CalDayDate = new int[OWNERINFO_DAYMAX];
        CalDayMode = new int[OWNERINFO_DAYMAX];
        CalDayModeName = new String[OWNERINFO_DAYMAX];
        CalDayWeekKind = new int[OWNERINFO_DAYMAX];
        CalDayHolidayKind = new int[OWNERINFO_DAYMAX];
        CalDayMemo1 = new String[OWNERINFO_DAYMAX];
        CalDayMemo2 = new String[OWNERINFO_DAYMAX];

        // �����󋵗���
        RoomHistoryDate = 0;
        RoomHistoryRoomCount = 0;
        RoomHistoryCount = 0;
        RoomHistoryTime = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryEmpty = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryExist = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryClean = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryStop = new int[OWNERINFO_TIMEHOURMAX];

        // �����o�[�����N
        CustomRankCount = 0;
        CustomRankCode = new int[OWNERINFO_CUSTOMRANKMAX];
        CustomRankName = new String[OWNERINFO_CUSTOMRANKMAX];

        // ���Z�@���O
        TexlogGetLogLevel = 0;
        TexlogNextDate = 0;
        TexlogNextTime = 0;
        TexlogNextRoomCode = 0;
        TexlogNextLineNo = 0;
        TexlogNextTermId = 0;
        TexlogPrevDate = 0;
        TexlogPrevTime = 0;
        TexlogPrevRoomCode = 0;
        TexlogPrevLineNo = 0;
        TexlogPrevTermId = 0;
        TexlogCount = 0;
        TexlogDate = new int[OWNERINFO_TEXLOGMAX];
        TexlogTime = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomCode = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomName = new String[OWNERINFO_TEXLOGMAX];
        TexlogLineNo = new int[OWNERINFO_TEXLOGMAX];
        TexlogTermId = new int[OWNERINFO_TEXLOGMAX];
        TexlogClaimed = new int[OWNERINFO_TEXLOGMAX];
        TexlogSurplus = new int[OWNERINFO_TEXLOGMAX];
        TexlogSafeCount = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexlogSafeTotal = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX];
        TexlogLogLevel = new int[OWNERINFO_TEXLOGMAX];
        TexlogLogContent = new String[OWNERINFO_TEXLOGMAX];
        TexlogLogDetail = new String[OWNERINFO_TEXLOGMAX];
        TexlogStatTrade = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatInOut = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatSecurity = new int[OWNERINFO_TEXLOGMAX];

        AscSalesDetailGetStartDate = 0;
        AscSalesDetailGetEndDate = 0;
        AscSalesDetailStay = 0;
        AscSalesDetailStayBeforeOver = 0;
        AscSalesDetailStayAfterOver = 0;
        AscSalesDetailRest = 0;
        AscSalesDetailRestOver = 0;
        AscSalesDetailTel = 0;
        AscSalesDetailAdvance = 0;
        AscSalesDetailSubTotal = 0;
        AscSalesDetailService = 0;
        AscSalesDetailTaxOut = 0;
        AscSalesDetailTaxIn = 0;
        AscSalesDetailDiscount = 0;
        AscSalesDetailExtra = 0;
        AscSalesDetailMember = 0;
        AscSalesDetailAdjust = 0;
        AscSalesDetailFiller = new int[6];
        AscSalesDetailTotal = 0;

        AscSalesDetailCount = 0;
        AscSalesDetailName = new String[OWNERINFO_SALESDETAILMAX];
        AscSalesDetailAmount = new int[OWNERINFO_SALESDETAILMAX];

        FrontTexTermCodeIn = 999;
        FrontTexTermCount = 0;
        FrontTexTermCode = new int[OWNERINFO_ROOMMAX];
        FrontTexTermName = new String[OWNERINFO_ROOMMAX];
        FrontTexServiceStat = new int[OWNERINFO_ROOMMAX];
        FrontTexKeySwStat = new int[OWNERINFO_ROOMMAX];
        FrontTexSecurityStat = new int[OWNERINFO_ROOMMAX];
        FrontTexDoorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexLineStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorCode = new String[OWNERINFO_ROOMMAX];
        FrontTexErrorMsg = new String[OWNERINFO_ROOMMAX];
        FrontTexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];
        FrontTexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX];
        FrontTexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];

        TimeChartRoomCodeOne = 0;
        TimeChartStartTime = 0;
        TimeChartStatusName = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusForeColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartRoomCount = 0;
        TimeChartRoomCode = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomName = new String[OWNERINFO_ROOMMAX];
        TimeChartRoomFloor = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomStatus = new int[OWNERINFO_ROOMMAX][OWNERINFO_TIMETABLEMAX];

        ManualSalesDetailGetStartDate = 0;
        ManualSalesDetailGetEndDate = 0;
        ManualSalesDetailTaxIn = 0;
        ManualSalesDetailTotal = 0;
        ManualSalesDetailCount = 0;
        ManualSalesDetailName = new String[OWNERINFO_MANUALSALESMAX];
        ManualSalesDetailAmount = new int[OWNERINFO_MANUALSALESMAX];

        log = new LogLib();
    }

    // ------------------------------------------------------------------------------
    //
    // �d������
    //
    // ------------------------------------------------------------------------------
    /**
     * �d�����M����(0100)
     * �I�[�i�[�T�C�g���O�C��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0100()
    {
        return(sendPacket0100Sub( 0, "" ));
    }

    /**
     * �d�����M����(0100)
     * �I�[�i�[�T�C�g���O�C��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0100(int kind, String value)
    {
        return(sendPacket0100Sub( kind, value ));
    }

    /**
     * �d�����M����(0100)
     * �I�[�i�[�T�C�g���O�C��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0100Sub(int kind, String value)
    {
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;

        // �f�[�^�̃N���A
        Result = 0;
        Name = "";
        Addupdate = 0;
        SystemKind = "";
        SystemVer1 = 0;
        SystemVer2 = 0;
        SystemVer3 = 0;
        SystemVer4 = 0;
        SystemCustomerKind = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0100";
                // �[��ID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0101" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �S���Җ�
                            data = new String( cdata, 53, 20 );
                            Name = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �v����t
                            data = new String( cdata, 73, 8 );
                            Addupdate = Integer.valueOf( data ).intValue();

                            // �V�X�e�����
                            data = new String( cdata, 81, 1 );
                            SystemKind = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �V�X�e���o�[�W����1
                            data = new String( cdata, 82, 5 );
                            SystemVer1 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // �V�X�e���o�[�W����2
                            data = new String( cdata, 87, 5 );
                            SystemVer2 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // �V�X�e���o�[�W����3
                            data = new String( cdata, 92, 5 );
                            SystemVer3 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // �V�X�e���o�[�W����4
                            data = new String( cdata, 97, 5 );
                            SystemVer4 = Integer.valueOf( data.replace( " ", "0" ) ).intValue();

                            // �ڋq�V�X�e�����
                            data = new String( cdata, 102, 1 );
                            SystemCustomerKind = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0100:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();

                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0102)
     * ������擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0102()
    {
        return(sendPacket0102Sub( 0, "", 0 ));
    }

    /**
     * �d�����M����(0102)
     * ������擾
     * 
     * @param updateFlag �X�V���@(0:�K��DB�X�V����A1:DB�ɓo�^���Ȃ���΍X�V����j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0102(int updateFlag)
    {
        return(sendPacket0102Sub( 0, "", updateFlag ));
    }

    /**
     * �d�����M����(0102)
     * ������擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0102(int kind, String value)
    {
        return(sendPacket0102Sub( kind, value, 0 ));
    }

    /**
     * �d�����M����(0102)
     * ������擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @param updateFlag �X�V���@(0:�K��DB�X�V����A1:DB�ɓo�^���Ȃ���΍X�V����j
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0102(int kind, String value, int updateFlag)
    {
        return(sendPacket0102Sub( kind, value, updateFlag ));
    }

    /**
     * �d�����M����(0102)
     * ������擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @param updateFlag �X�V���@(0:�K��DB�X�V����A1:DB�ɓo�^���Ȃ���΍X�V����j
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0102Sub(int kind, String value, int updateFlag)
    {

        // �ُ���t�����邩�ǂ������`�F�b�N
        if ( !dateCheck( SalesGetStartDate, SalesGetEndDate, Addupdate ) )
        {
            return(false);
        }

        boolean connectFlag = true;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;
        String element_data[] = new String[25];
        int element_value[] = new int[25];
        DateEdit date = new DateEdit();

        // �f�[�^�̃N���A
        Result = 0;
        SalesTex = 0;
        SalesTexCredit = 0;
        SalesFront = 0;
        SalesFrontCredit = 0;
        SalesRestTotal = 0;
        SalesStayTotal = 0;
        SalesOtherTotal = 0;
        SalesTotal = 0;
        SalesRestCount = 0;
        SalesStayCount = 0;
        SalesTotalCount = 0;
        SalesNowCheckin = 0;
        SalesRestRate = 0;
        SalesStayRate = 0;
        SalesTotalRate = 0;
        SalesRestPrice = 0;
        SalesStayPrice = 0;
        SalesTotalPrice = 0;
        SalesVisitorPrice = 0;
        SalesMemberPrice = 0;
        SalesRoomPrice = 0;
        SalesRoomTotalPrice = 0;
        SalesPayRestCount = 0;
        SalesPayStayCount = 0;
        SalesPointTotal = 0;
        element_data[0] = "SalesTex";
        element_data[1] = "SalesTexCredit";
        element_data[2] = "SalesFront";
        element_data[3] = "SalesFrontCredit";
        element_data[4] = "SalesRestTotal";
        element_data[5] = "SalesStayTotal";
        element_data[6] = "SalesOtherTotal";
        element_data[7] = "SalesTotal";
        element_data[8] = "SalesRestCount";
        element_data[9] = "SalesStayCount";
        element_data[10] = "SalesTotalCount";
        element_data[11] = "SalesNowCheckin";
        element_data[12] = "SalesRestRate";
        element_data[13] = "SalesStayRate";
        element_data[14] = "SalesTotalRate";
        element_data[15] = "SalesRestPrice";
        element_data[16] = "SalesStayPrice";
        element_data[17] = "SalesTotalPrice";
        element_data[18] = "SalesVisitorPrice";
        element_data[19] = "SalesMemberPrice";
        element_data[20] = "SalesRoomPrice";
        element_data[21] = "SalesRoomTotalPrice";
        element_data[22] = "SalesPayRestCount";
        element_data[23] = "SalesPayStayCount";
        element_data[24] = "SalesPointTotal";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String paramHotelId = (kind == 1 ? value : HotelId);

        if ( updateFlag == 1 )
        {

            // hotel_sales����f�[�^�ǂݍ���
            String query = "SELECT (SELECT value FROM hotel_sales WHERE hotel_id = ? AND manage_date=? AND field_name=? AND sub = 0)";
            for( int i = 1 ; i < element_data.length ; i++ )
            {
                query += ",(SELECT value FROM hotel_sales WHERE hotel_id = ? AND manage_date=? AND field_name=? AND sub = 0)";
            }
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                for( int i = 0 ; i < element_data.length ; i++ )
                {
                    prestate.setString( i * 3 + 1, paramHotelId );
                    prestate.setInt( i * 3 + 2, SalesGetStartDate );
                    prestate.setString( i * 3 + 3, element_data[i] );
                }
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        SalesTex = result.getString( 1 ) == null ? 0 : result.getInt( 1 );
                        SalesTexCredit = result.getString( 2 ) == null ? 0 : result.getInt( 2 );
                        SalesFront = result.getString( 3 ) == null ? 0 : result.getInt( 3 );
                        SalesFrontCredit = result.getString( 4 ) == null ? 0 : result.getInt( 4 );
                        SalesRestTotal = result.getString( 5 ) == null ? 0 : result.getInt( 5 );
                        SalesStayTotal = result.getString( 6 ) == null ? 0 : result.getInt( 6 );
                        SalesOtherTotal = result.getString( 7 ) == null ? 0 : result.getInt( 7 );
                        SalesTotal = result.getString( 8 ) == null ? 0 : result.getInt( 8 );
                        SalesRestCount = result.getString( 9 ) == null ? 0 : result.getInt( 9 );
                        SalesStayCount = result.getString( 10 ) == null ? 0 : result.getInt( 10 );
                        SalesTotalCount = result.getString( 11 ) == null ? 0 : result.getInt( 11 );
                        SalesNowCheckin = result.getString( 12 ) == null ? 0 : result.getInt( 12 );
                        SalesRestRate = result.getString( 13 ) == null ? 0 : result.getInt( 13 );
                        SalesStayRate = result.getString( 14 ) == null ? 0 : result.getInt( 14 );
                        SalesTotalRate = result.getString( 15 ) == null ? 0 : result.getInt( 15 );
                        SalesRestPrice = result.getString( 16 ) == null ? 0 : result.getInt( 16 );
                        SalesStayPrice = result.getString( 17 ) == null ? 0 : result.getInt( 17 );
                        SalesTotalPrice = result.getString( 18 ) == null ? 0 : result.getInt( 18 );
                        SalesVisitorPrice = result.getString( 19 ) == null ? 0 : result.getInt( 19 );
                        SalesMemberPrice = result.getString( 20 ) == null ? 0 : result.getInt( 20 );
                        SalesRoomPrice = result.getString( 21 ) == null ? 0 : result.getInt( 21 );
                        SalesRoomTotalPrice = result.getString( 22 ) == null ? 0 : result.getInt( 22 );
                        SalesPayRestCount = result.getString( 23 ) == null ? 0 : result.getInt( 23 );
                        SalesPayStayCount = result.getString( 24 ) == null ? 0 : result.getInt( 24 );
                        SalesPointTotal = result.getString( 25 ) == null ? 0 : result.getInt( 25 );
                        if ( SalesTotal != 0 )
                            connectFlag = false;
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[ownerInfo.sendPacket0102() SELECT hotel_sales ERROR] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        if ( connectFlag )
        {
            if ( paramHotelId != null )
            {
                if ( SalesGetStartDate <= 0 )
                {
                    Result = 1;
                    return(true);
                }

                // �z�X�g�ڑ�����
                tcpclient = new TcpClient();
                ret = connect( tcpclient, kind, value );

                if ( ret != false )
                {
                    format = new StringFormat();

                    // �R�}���h
                    senddata = "0102";
                    // ���[�UID
                    senddata = senddata + format.leftFitFormat( TermId, 11 );
                    // �p�X���[�h
                    senddata = senddata + format.leftFitFormat( Password, 4 );
                    // �擾�J�n���t
                    nf = new DecimalFormat( "00000000" );
                    senddata = senddata + nf.format( SalesGetStartDate );
                    // �擾�I�����t
                    nf = new DecimalFormat( "00000000" );
                    senddata = senddata + nf.format( SalesGetEndDate );
                    // �\��
                    senddata = senddata + "          ";

                    // �d���w�b�_�̎擾
                    if ( kind == 1 )
                    {
                        header = tcpclient.getPacketHeader( value, senddata.length() );
                    }
                    else
                    {
                        header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                    }
                    // �d���̌���
                    senddata = header + senddata;

                    try
                    {
                        // �d�����M
                        tcpclient.send( senddata );

                        // ��M�ҋ@
                        recvdata = tcpclient.recv();
                        if ( recvdata != null )
                        {
                            cdata = new char[recvdata.length()];
                            cdata = recvdata.toCharArray();

                            // �R�}���h�擾
                            data = new String( cdata, 32, 4 );
                            if ( data.compareTo( "0103" ) == 0 )
                            {
                                // ��������
                                data = new String( cdata, 51, 2 );
                                Result = Integer.valueOf( data ).intValue();

                                // �J�n���t
                                data = new String( cdata, 53, 8 );
                                SalesGetStartDate = Integer.valueOf( data ).intValue();

                                // �I�����t
                                data = new String( cdata, 61, 8 );
                                SalesGetEndDate = Integer.valueOf( data ).intValue();

                                // ���Z�@��������
                                data = new String( cdata, 69, 9 );
                                SalesTex = Integer.valueOf( data ).intValue();

                                // ���Z�@�N���W�b�g����
                                data = new String( cdata, 78, 9 );
                                SalesTexCredit = Integer.valueOf( data ).intValue();

                                // �t�����g��������
                                data = new String( cdata, 87, 9 );
                                SalesFront = Integer.valueOf( data ).intValue();

                                // �t�����g�N���W�b�g����
                                data = new String( cdata, 96, 9 );
                                SalesFrontCredit = Integer.valueOf( data ).intValue();

                                // ���㍇�v
                                data = new String( cdata, 105, 9 );
                                SalesTotal = Integer.valueOf( data ).intValue();

                                // �x�e�g��
                                data = new String( cdata, 114, 4 );
                                SalesRestCount = Integer.valueOf( data ).intValue();

                                // �h���g��
                                data = new String( cdata, 118, 4 );
                                SalesStayCount = Integer.valueOf( data ).intValue();

                                // ���v�g��
                                data = new String( cdata, 122, 5 );
                                SalesTotalCount = Integer.valueOf( data ).intValue();

                                // ���ݓ�����
                                data = new String( cdata, 127, 3 );
                                SalesNowCheckin = Integer.valueOf( data ).intValue();

                                // �x�e��]��
                                data = new String( cdata, 130, 5 );
                                SalesRestRate = Integer.valueOf( data ).intValue();

                                // �h����]��
                                data = new String( cdata, 135, 5 );
                                SalesStayRate = Integer.valueOf( data ).intValue();

                                // ���v��]��
                                data = new String( cdata, 140, 5 );
                                SalesTotalRate = Integer.valueOf( data ).intValue();

                                // �x�e�q�P��
                                data = new String( cdata, 145, 9 );
                                SalesRestPrice = Integer.valueOf( data ).intValue();

                                // �h���q�P��
                                data = new String( cdata, 154, 9 );
                                SalesStayPrice = Integer.valueOf( data ).intValue();

                                // ���v�q�P��
                                data = new String( cdata, 163, 9 );
                                SalesTotalPrice = Integer.valueOf( data ).intValue();

                                // �r�W�^�[�q�P��
                                data = new String( cdata, 172, 9 );
                                SalesVisitorPrice = Integer.valueOf( data ).intValue();

                                // �����o�[�q�P��
                                data = new String( cdata, 181, 9 );
                                SalesMemberPrice = Integer.valueOf( data ).intValue();

                                // �����P��
                                data = new String( cdata, 190, 9 );
                                SalesRoomPrice = Integer.valueOf( data ).intValue();

                                try
                                {
                                    // �݌v�����P��
                                    data = new String( cdata, 199, 9 );
                                    SalesRoomTotalPrice = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesRoomTotalPrice = 0;
                                }
                                try
                                {
                                    // �x�e����
                                    data = new String( cdata, 208, 9 );
                                    SalesRestTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesRestTotal = 0;
                                }
                                try
                                {
                                    // �h������
                                    data = new String( cdata, 217, 9 );
                                    SalesStayTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesStayTotal = 0;
                                }
                                try
                                {
                                    // ���O����
                                    data = new String( cdata, 226, 9 );
                                    SalesOtherTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesOtherTotal = 0;
                                }
                                try
                                {
                                    // �x�e�O��ϑg��
                                    data = new String( cdata, 235, 4 );
                                    SalesPayRestCount = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesPayRestCount = 0;
                                }
                                try
                                {
                                    // �h���O��ϑg��
                                    data = new String( cdata, 239, 4 );
                                    SalesPayStayCount = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesPayStayCount = 0;
                                }
                                try
                                {
                                    // ��g�|�C���g
                                    data = new String( cdata, 243, 9 );
                                    SalesPointTotal = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    SalesPointTotal = 0;
                                }
                                // DB�������� �����������͏������܂Ȃ��B
                                if ( (SalesGetEndDate == 0 || SalesGetStartDate == SalesGetEndDate)
                                        && Integer.parseInt( date.getDate( 2 ) ) > date.addDay( SalesGetStartDate, 1 ) && Addupdate > SalesGetStartDate
                                        && !(SalesGetStartDate % 100 == 0 && (SalesGetStartDate / 100 >= Addupdate / 100 || SalesGetStartDate / 100 >= Integer.parseInt( date.getDate( 2 ) ))) )
                                {
                                    element_value[0] = SalesTex;
                                    element_value[1] = SalesTexCredit;
                                    element_value[2] = SalesFront;
                                    element_value[3] = SalesFrontCredit;
                                    element_value[4] = SalesRestTotal;
                                    element_value[5] = SalesStayTotal;
                                    element_value[6] = SalesOtherTotal;
                                    element_value[7] = SalesTotal;
                                    element_value[8] = SalesRestCount;
                                    element_value[9] = SalesStayCount;
                                    element_value[10] = SalesTotalCount;
                                    element_value[11] = SalesNowCheckin;
                                    element_value[12] = SalesRestRate;
                                    element_value[13] = SalesStayRate;
                                    element_value[14] = SalesTotalRate;
                                    element_value[15] = SalesRestPrice;
                                    element_value[16] = SalesStayPrice;
                                    element_value[17] = SalesTotalPrice;
                                    element_value[18] = SalesVisitorPrice;
                                    element_value[19] = SalesMemberPrice;
                                    element_value[20] = SalesRoomPrice;
                                    element_value[21] = SalesRoomTotalPrice;
                                    element_value[22] = SalesPayRestCount;
                                    element_value[23] = SalesPayStayCount;
                                    element_value[24] = SalesPointTotal;
                                    String query = "DELETE FROM `hotel_sales` WHERE `hotel_id` = ? AND `manage_date` = ?";
                                    try
                                    {
                                        connection = DBConnection.getConnection();
                                        prestate = connection.prepareStatement( query );
                                        prestate.setString( 1, paramHotelId );
                                        prestate.setInt( 2, SalesGetStartDate );
                                        prestate.executeUpdate();
                                    }
                                    catch ( Exception e )
                                    {
                                        Logging.error( "[ownerInfo.sendPacket0102() DELETE hotel_sales ERROR] Exception=" + e.toString() );
                                    }
                                    finally
                                    {
                                        DBConnection.releaseResources( prestate );
                                    }

                                    query = "INSERT INTO `hotel_sales` (`hotel_id`, `manage_date`, `field_name`, `sub`, `value`, `text`, `last_update`, `last_uptime`) VALUES";
                                    query += " ( ?, ?, ?, 0, ?, '', ?, ?)";
                                    for( int i = 1 ; i < element_data.length ; i++ )
                                    {
                                        query += ",( ?, ?, ?, 0, ?, '', ?, ?)";
                                    }
                                    try
                                    {
                                        prestate = connection.prepareStatement( query );
                                        for( int i = 0 ; i < element_data.length ; i++ )
                                        {
                                            prestate.setString( i * 6 + 1, paramHotelId );
                                            prestate.setInt( i * 6 + 2, SalesGetStartDate );
                                            prestate.setString( i * 6 + 3, element_data[i] );
                                            prestate.setInt( i * 6 + 4, element_value[i] );
                                            prestate.setInt( i * 6 + 5, Integer.parseInt( date.getDate( 2 ) ) );
                                            prestate.setInt( i * 6 + 6, Integer.parseInt( date.getTime( 1 ) ) );
                                        }
                                        prestate.executeUpdate();
                                    }
                                    catch ( Exception e )
                                    {
                                        Logging.error( "[ownerInfo.sendPacket0102() INSERT hotel_sales ERROR] Exception=" + e.toString() );
                                    }
                                    finally
                                    {
                                        DBConnection.releaseResources( prestate );
                                        DBConnection.releaseResources( connection );
                                    }
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        log.error( "sendPacket0102:" + e.toString() );
                        return(false);
                    }

                    tcpclient.disconnectService();

                    Logging.info( "[ownerInfo.sendPacket0102() Addupdate=" + Addupdate + ",SalesGetStartDate=" + SalesGetStartDate + ",SalesGetEndDate=" + SalesGetEndDate + ",HotelId=" + HotelId + ",value=" + value + ",SalesTotal" + SalesTotal );

                    return(true);
                }
            }
            return(false);
        }
        return(true);
    }

    /**
     * �d�����M����(0104)
     * ����ڍ׏��擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0104()
    {
        return(sendPacket0104Sub( 0, "" ));
    }

    /**
     * �d�����M����(0104)
     * ����ڍ׏��擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0104(int kind, String value)
    {
        return(sendPacket0104Sub( kind, value ));
    }

    /**
     * �d�����M����(0104)
     * ����ڍ׏��擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0104Sub(int kind, String value)
    {

        if ( !dateCheck( SalesDetailGetStartDate, SalesDetailGetEndDate ) )
        {
            return(false);
        }

        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        SalesDetailRest = 0;
        SalesDetailStay = 0;
        SalesDetailRestBeforeOver = 0;
        SalesDetailRestAfterOver = 0;
        SalesDetailStayBeforeOver = 0;
        SalesDetailStayAfterOver = 0;
        SalesDetailMeat = 0;
        SalesDetailDelivery = 0;
        SalesDetailConveni = 0;
        SalesDetailRef = 0;
        SalesDetailMulti = 0;
        SalesDetailSales = 0;
        SalesDetailRental = 0;
        SalesDetailCigarette = 0;
        SalesDetailTel = 0;
        SalesDetailEtc = 0;
        SalesDetailStaxIn = 0;
        SalesDetailFiller = new int[9];
        SalesDetailDiscount = 0;
        SalesDetailExtra = 0;
        SalesDetailMember = 0;
        SalesDetailService = 0;
        SalesDetailStax = 0;
        SalesDetailAdjust = 0;
        SalesDetailTotal = 0;
        SalesDetailTaxRate1 = 0;
        SalesDetailTaxableAmount1 = 0;
        SalesDetailTaxRate2 = 0;
        SalesDetailTaxableAmount2 = 0;

        if ( HotelId != null )
        {
            if ( SalesDetailGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0104";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾�J�n���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( SalesDetailGetStartDate );
                // �擾�I�����t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( SalesDetailGetEndDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0105" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �J�n���t
                            data = new String( cdata, 53, 8 );
                            SalesDetailGetStartDate = Integer.valueOf( data ).intValue();

                            // �I�����t
                            data = new String( cdata, 61, 8 );
                            SalesDetailGetEndDate = Integer.valueOf( data ).intValue();

                            // �x�e
                            data = new String( cdata, 69, 9 );
                            SalesDetailRest = Integer.valueOf( data ).intValue();

                            // �h��
                            data = new String( cdata, 78, 9 );
                            SalesDetailStay = Integer.valueOf( data ).intValue();

                            // �x�e�O����
                            data = new String( cdata, 87, 9 );
                            SalesDetailRestBeforeOver = Integer.valueOf( data ).intValue();

                            // �x�e�㉄��
                            data = new String( cdata, 96, 9 );
                            SalesDetailRestAfterOver = Integer.valueOf( data ).intValue();

                            // �h���O����
                            data = new String( cdata, 105, 9 );
                            SalesDetailStayBeforeOver = Integer.valueOf( data ).intValue();

                            // �h���㉄��
                            data = new String( cdata, 114, 9 );
                            SalesDetailStayAfterOver = Integer.valueOf( data ).intValue();

                            // ���H����
                            data = new String( cdata, 123, 9 );
                            SalesDetailMeat = Integer.valueOf( data ).intValue();

                            // �o�O����
                            data = new String( cdata, 132, 9 );
                            SalesDetailDelivery = Integer.valueOf( data ).intValue();

                            // �R���r�j����
                            data = new String( cdata, 141, 9 );
                            SalesDetailConveni = Integer.valueOf( data ).intValue();

                            // �①�ɔ���
                            data = new String( cdata, 150, 9 );
                            SalesDetailRef = Integer.valueOf( data ).intValue();

                            // �}���`���f�B�A����
                            data = new String( cdata, 159, 9 );
                            SalesDetailMulti = Integer.valueOf( data ).intValue();

                            // �̔����i����
                            data = new String( cdata, 168, 9 );
                            SalesDetailSales = Integer.valueOf( data ).intValue();

                            // �����^������
                            data = new String( cdata, 177, 9 );
                            SalesDetailRental = Integer.valueOf( data ).intValue();

                            // ���΂�����
                            data = new String( cdata, 186, 9 );
                            SalesDetailCigarette = Integer.valueOf( data ).intValue();

                            // �d�b����
                            data = new String( cdata, 195, 9 );
                            SalesDetailTel = Integer.valueOf( data ).intValue();

                            // ���̑�����
                            data = new String( cdata, 204, 9 );
                            SalesDetailEtc = Integer.valueOf( data ).intValue();

                            // ������Ŋz
                            data = new String( cdata, 213, 9 );
                            SalesDetailStaxIn = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 222, 9 );
                            SalesDetailFiller[0] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 231, 9 );
                            SalesDetailFiller[1] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 240, 9 );
                            SalesDetailFiller[2] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 249, 9 );
                            SalesDetailFiller[3] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 258, 9 );
                            SalesDetailFiller[4] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 267, 9 );
                            SalesDetailFiller[5] = Integer.valueOf( data ).intValue();

                            // ��������
                            data = new String( cdata, 276, 9 );
                            SalesDetailDiscount = Integer.valueOf( data ).intValue();

                            // ��������
                            data = new String( cdata, 285, 9 );
                            SalesDetailExtra = Integer.valueOf( data ).intValue();

                            // �����o�[��������
                            data = new String( cdata, 294, 9 );
                            SalesDetailMember = Integer.valueOf( data ).intValue();

                            // ��d������
                            data = new String( cdata, 303, 9 );
                            SalesDetailService = Integer.valueOf( data ).intValue();

                            // ����Ŕ���
                            data = new String( cdata, 312, 9 );
                            SalesDetailStax = Integer.valueOf( data ).intValue();

                            // ����������
                            data = new String( cdata, 321, 9 );
                            SalesDetailAdjust = Integer.valueOf( data ).intValue();

                            // �����v
                            data = new String( cdata, 330, 9 );
                            SalesDetailTotal = Integer.valueOf( data ).intValue();

                            // �ŗ�1 ��� 0:�K�p����(�P��0.1%,��10%��100)
                            data = new String( cdata, 339, 4 );
                            SalesDetailTaxRate1 = Integer.valueOf( data ).intValue();

                            // �ŗ�1�ېőΏۋ��z
                            data = new String( cdata, 343, 9 );
                            SalesDetailTaxableAmount1 = Integer.valueOf( data ).intValue();

                            // �ŗ�2 ��� 0:�K�p����(�P��0.1%,��8%��80)
                            data = new String( cdata, 352, 4 );
                            SalesDetailTaxRate2 = Integer.valueOf( data ).intValue();

                            // �ŗ�2�ېőΏۋ��z
                            data = new String( cdata, 356, 9 );
                            SalesDetailTaxableAmount2 = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0104:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0106)
     * IN/OUT�g�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0106()
    {
        return(sendPacket0106Sub( 0, "" ));
    }

    /**
     * �d�����M����(0106)
     * IN/OUT�g�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0106(int kind, String value)
    {
        return(sendPacket0106Sub( kind, value ));
    }

    /**
     * �d�����M����(0106)
     * IN/OUT�g�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0106Sub(int kind, String value)
    {

        if ( !dateCheck( InOutGetStartDate, InOutGetEndDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        InOutTime = new int[OWNERINFO_TIMEHOURMAX];
        InOutIn = new int[OWNERINFO_TIMEHOURMAX];
        InOutOut = new int[OWNERINFO_TIMEHOURMAX];

        if ( HotelId != null )
        {
            if ( InOutGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0106";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾�J�n���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( InOutGetStartDate );
                // �擾�I�����t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( InOutGetEndDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0107" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �J�n���t
                            data = new String( cdata, 53, 8 );
                            InOutGetStartDate = Integer.valueOf( data ).intValue();

                            // �I�����t
                            data = new String( cdata, 61, 8 );
                            InOutGetEndDate = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_TIMEHOURMAX ; i++ )
                            {
                                // ����
                                data = new String( cdata, 69 + (i * 12), 4 );
                                InOutTime[i] = Integer.valueOf( data ).intValue();

                                // �h�m�g��
                                data = new String( cdata, 73 + (i * 12), 4 );
                                InOutIn[i] = Integer.valueOf( data ).intValue();

                                // �n�t�s�g��
                                data = new String( cdata, 77 + (i * 12), 4 );
                                InOutOut[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0106:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0108)
     * �����X�e�[�^�X���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0108()
    {
        return(sendPacket0108Sub( 0, "" ));
    }

    /**
     * �d�����M����(0108)
     * �����X�e�[�^�X���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0108(int kind, String value)
    {
        return(sendPacket0108Sub( kind, value ));
    }

    /**
     * �d�����M����(0108)
     * �����X�e�[�^�X���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0108Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;

        // �f�[�^�̃N���A
        Result = 0;
        StatusEmptyFullMode = 0;
        StatusEmptyFullState = 0;
        StatusWaiting = 0;
        StatusName = new String[OWNERINFO_ROOMSTATUSMAX];
        StatusCount = new int[OWNERINFO_ROOMSTATUSMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0108";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0109" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �󖞉^�p���[�h
                            data = new String( cdata, 53, 1 );
                            StatusEmptyFullMode = Integer.valueOf( data ).intValue();

                            // �󖞏��
                            data = new String( cdata, 54, 1 );
                            StatusEmptyFullState = Integer.valueOf( data ).intValue();

                            // �E�F�C�e�B���O��
                            data = new String( cdata, 55, 3 );
                            StatusWaiting = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_ROOMSTATUSMAX ; i++ )
                            {
                                // �X�e�[�^�X��
                                data = new String( cdata, 127 + (i * 15), 12 );
                                StatusName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ������
                                data = new String( cdata, 139 + (i * 15), 3 );
                                StatusCount[i] = Integer.valueOf( data ).intValue();
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0108:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0110)
     * �����X�e�[�^�X�ڍ׏��擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0110()
    {
        return(sendPacket0110Sub( 0, "" ));
    }

    /**
     * �d�����M����(0110)
     * �����X�e�[�^�X�ڍ׏��擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0110(int kind, String value)
    {
        return(sendPacket0110Sub( kind, value ));
    }

    /**
     * �d�����M����(0110)
     * �����X�e�[�^�X�ڍ׏��擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0110Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        StatusDetailCount = 0;
        StatusDetailRoomCode = new int[OWNERINFO_ROOMMAX];
        StatusDetailRoomName = new String[OWNERINFO_ROOMMAX];
        StatusDetailElapseTime = new int[OWNERINFO_ROOMMAX];
        StatusDetailStatusName = new String[OWNERINFO_ROOMMAX];
        StatusDetailColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailForeColor = new String[OWNERINFO_ROOMMAX];
        StatusDetailX = new int[OWNERINFO_ROOMMAX];
        StatusDetailY = new int[OWNERINFO_ROOMMAX];
        StatusDetailZ = new int[OWNERINFO_ROOMMAX];
        StatusDetailFloor = new int[OWNERINFO_ROOMMAX];
        StatusDetailUserChargeMode = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0110";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0111" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            StatusDetailCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < StatusDetailCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 128), 3 );
                                StatusDetailRoomCode[i] = Integer.valueOf( data ).intValue();

                                // ��������
                                data = new String( cdata, 59 + (i * 128), 8 );
                                StatusDetailRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �o�ߎ���
                                data = new String( cdata, 67 + (i * 128), 6 );
                                StatusDetailElapseTime[i] = Integer.valueOf( data ).intValue();

                                // �X�e�[�^�X��
                                data = new String( cdata, 73 + (i * 128), 12 );
                                StatusDetailStatusName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �X�e�[�^�X�F
                                data = new String( cdata, 85 + (i * 128), 6 );
                                StatusDetailColor[i] = data.trim();

                                // �X�e�[�^�X�����F
                                data = new String( cdata, 91 + (i * 128), 6 );
                                StatusDetailForeColor[i] = data.trim();

                                // �\���ʒuX
                                data = new String( cdata, 97 + (i * 128), 2 );
                                StatusDetailX[i] = Integer.valueOf( data ).intValue();

                                // �\���ʒuY
                                data = new String( cdata, 99 + (i * 128), 2 );
                                StatusDetailY[i] = Integer.valueOf( data ).intValue();

                                // �\���ʒuZ
                                data = new String( cdata, 101 + (i * 128), 2 );
                                StatusDetailZ[i] = Integer.valueOf( data ).intValue();

                                // �t���A�ԍ�
                                data = new String( cdata, 103 + (i * 128), 2 );
                                StatusDetailFloor[i] = Integer.valueOf( data ).intValue();

                                try
                                {
                                    // �\�莺���K�p�敪
                                    data = new String( cdata, 105 + (i * 128), 2 );
                                    StatusDetailUserChargeMode[i] = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    StatusDetailUserChargeMode[i] = 0;
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0110:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0112)
     * ���P��IN/OUT�g�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0112()
    {
        return(sendPacket0112Sub( 0, "" ));
    }

    /**
     * �d�����M����(0112)
     * ���P��IN/OUT�g�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0112(int kind, String value)
    {
        return(sendPacket0112Sub( kind, value ));
    }

    /**
     * �d�����M����(0112)
     * ���P��IN/OUT�g�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0112Sub(int kind, String value)
    {

        if ( !dateCheck( AddupInOutGetDate ) )
        {
            return(false);
        }

        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        AddupInOutAfterIn = 0;
        AddupInOutBeforeIn = 0;
        AddupInOutAllOut = 0;
        AddupInOutBeforeOut = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0112";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( AddupInOutGetDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0113" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ���t
                            data = new String( cdata, 53, 8 );
                            AddupInOutGetDate = Integer.valueOf( data ).intValue();

                            // ����h�m��
                            data = new String( cdata, 61, 4 );
                            AddupInOutAfterIn = Integer.valueOf( data ).intValue();

                            // ���O�h�m��
                            data = new String( cdata, 65, 4 );
                            AddupInOutBeforeIn = Integer.valueOf( data ).intValue();

                            // ���n�t�s�g��
                            data = new String( cdata, 69, 4 );
                            AddupInOutAllOut = Integer.valueOf( data ).intValue();

                            // ���O�n�t�s��
                            data = new String( cdata, 73, 4 );
                            AddupInOutBeforeOut = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0112:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0114)
     * �������p��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0114()
    {
        return(sendPacket0114Sub( 0, "" ));
    }

    /**
     * �d�����M����(0114)
     * �������p��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0114(int kind, String value)
    {
        return(sendPacket0114Sub( kind, value ));
    }

    /**
     * �d�����M����(0114)
     * �������p��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0114Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        EmployeeName = "";
        ModeName = "";
        StateRoomCount = 0;
        StateRoomCode = new int[OWNERINFO_ROOMMAX];
        StateInDate = new int[OWNERINFO_ROOMMAX];
        StateInTime = new int[OWNERINFO_ROOMMAX];
        StateOutDate = new int[OWNERINFO_ROOMMAX];
        StateOutTime = new int[OWNERINFO_ROOMMAX];
        StatePerson = new int[OWNERINFO_ROOMMAX];
        StateDoor = new int[OWNERINFO_ROOMMAX];
        StateRefUse = new int[OWNERINFO_ROOMMAX];
        StateConveniUse = new int[OWNERINFO_ROOMMAX];
        StateLucky = new int[OWNERINFO_ROOMMAX];
        StateCustomId = new String[OWNERINFO_ROOMMAX];
        StateNickName = new String[OWNERINFO_ROOMMAX];
        StateCustomRankName = new String[OWNERINFO_ROOMMAX];
        StateCustomEvent = new int[OWNERINFO_ROOMMAX];
        StateCustomWarning = new int[OWNERINFO_ROOMMAX];
        StateCustomContact = new int[OWNERINFO_ROOMMAX];
        StateCustomPresent = new int[OWNERINFO_ROOMMAX];
        StateReserveTime = new int[OWNERINFO_ROOMMAX];
        StateParkingNo = new int[OWNERINFO_ROOMMAX];
        StateCustomUserId = new String[OWNERINFO_ROOMMAX];
        StateHapihoteTouch = new int[OWNERINFO_ROOMMAX];
        StateHapihoteMile = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0114";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0115" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �]�ƈ���
                            data = new String( cdata, 53, 20 );
                            EmployeeName = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������[�h����
                            data = new String( cdata, 73, 20 );
                            ModeName = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ������
                            data = new String( cdata, 93, 3 );
                            StateRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < StateRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 96 + (i * 256), 3 );
                                StateRoomCode[i] = Integer.valueOf( data ).intValue();

                                // �������t
                                data = new String( cdata, 99 + (i * 256), 8 );
                                StateInDate[i] = Integer.valueOf( data ).intValue();

                                // ��������
                                data = new String( cdata, 107 + (i * 256), 4 );
                                StateInTime[i] = Integer.valueOf( data ).intValue();

                                // �ގ����t
                                data = new String( cdata, 111 + (i * 256), 8 );
                                StateOutDate[i] = Integer.valueOf( data ).intValue();

                                // �ގ�����
                                data = new String( cdata, 119 + (i * 256), 4 );
                                StateOutTime[i] = Integer.valueOf( data ).intValue();

                                // ���p�l��
                                data = new String( cdata, 123 + (i * 256), 2 );
                                StatePerson[i] = Integer.valueOf( data ).intValue();

                                // �h�A���
                                data = new String( cdata, 125 + (i * 256), 1 );
                                StateDoor[i] = Integer.valueOf( data ).intValue();

                                // �①�ɗ��p���
                                data = new String( cdata, 126 + (i * 256), 1 );
                                StateRefUse[i] = Integer.valueOf( data ).intValue();

                                // �R���r�j���p���
                                data = new String( cdata, 127 + (i * 256), 1 );
                                StateConveniUse[i] = Integer.valueOf( data ).intValue();

                                // ���b�L�[���[��
                                data = new String( cdata, 128 + (i * 256), 1 );
                                StateLucky[i] = Integer.valueOf( data ).intValue();

                                // �����o�[�h�c
                                data = new String( cdata, 129 + (i * 256), 9 );
                                StateCustomId[i] = data.trim();

                                // �j�b�N�l�[��
                                data = new String( cdata, 138 + (i * 256), 20 );
                                StateNickName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����o�[�����N��
                                data = new String( cdata, 158 + (i * 256), 40 );
                                StateCustomRankName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����o�[�C�x���g
                                data = new String( cdata, 198 + (i * 256), 1 );
                                StateCustomEvent[i] = Integer.valueOf( data ).intValue();

                                // �x�����
                                data = new String( cdata, 199 + (i * 256), 1 );
                                StateCustomWarning[i] = Integer.valueOf( data ).intValue();

                                // �A�����
                                data = new String( cdata, 200 + (i * 256), 1 );
                                StateCustomContact[i] = Integer.valueOf( data ).intValue();

                                // �i�i���
                                data = new String( cdata, 201 + (i * 256), 1 );
                                StateCustomPresent[i] = Integer.valueOf( data ).intValue();

                                // �\�񎞊�
                                data = new String( cdata, 202 + (i * 256), 4 );
                                StateReserveTime[i] = Integer.valueOf( data ).intValue();

                                // ���ԏ�ԍ�
                                data = new String( cdata, 206 + (i * 256), 3 );
                                StateParkingNo[i] = Integer.valueOf( data ).intValue();

                                // ���[�UID
                                data = new String( cdata, 209 + (i * 256), 32 );
                                StateCustomUserId[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                try
                                {
                                    // �n�s�z�e�^�b�`
                                    data = new String( cdata, 241 + (i * 256), 1 );
                                    StateHapihoteTouch[i] = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    StateHapihoteTouch[i] = 0;
                                }
                                try
                                {
                                    // �n�s�z�e�}�C���g�p��
                                    data = new String( cdata, 242 + (i * 256), 9 );
                                    StateHapihoteMile[i] = Integer.valueOf( data ).intValue();
                                }
                                catch ( Exception e )
                                {
                                    StateHapihoteMile[i] = 0;
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0114:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0116)
     * �Ǘ��@��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0116()
    {
        return(sendPacket0116Sub( 0, "" ));
    }

    /**
     * �d�����M����(0116)
     * �Ǘ��@��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0116(int kind, String value)
    {
        return(sendPacket0116Sub( kind, value ));
    }

    /**
     * �d�����M����(0116)
     * �Ǘ��@��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0116Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        EquipRoomCount = 0;
        EquipRoomCode = new int[OWNERINFO_ROOMMAX];
        EquipActMode = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusAlarm = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipStatusData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];
        EquipActData = new int[OWNERINFO_ROOMMAX][OWNERINFO_EQUIPMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0116";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0117" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            EquipRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < EquipRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 256), 3 );
                                EquipRoomCode[i] = Integer.valueOf( data ).intValue();

                                // �����ݔ���
                                for( j = 0 ; j < OWNERINFO_EQUIPMAX ; j++ )
                                {
                                    // ����敪
                                    data = new String( cdata, 59 + (i * 256) + (j * 9), 2 );
                                    EquipActMode[i][j] = Integer.valueOf( data ).intValue();

                                    // �󋵌x��
                                    data = new String( cdata, 61 + (i * 256) + (j * 9), 1 );
                                    EquipStatusAlarm[i][j] = Integer.valueOf( data ).intValue();

                                    // �󋵃f�[�^
                                    data = new String( cdata, 62 + (i * 256) + (j * 9), 2 );
                                    EquipStatusData[i][j] = Integer.valueOf( data ).intValue();

                                    // ����f�[�^
                                    data = new String( cdata, 64 + (i * 256) + (j * 9), 4 );
                                    EquipActData[i][j] = Integer.valueOf( data ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0116:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0118)
     * ���l����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0118()
    {
        return(sendPacket0118Sub( 0, "" ));
    }

    /**
     * �d�����M����(0118)
     * ���l����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0118(int kind, String value)
    {
        return(sendPacket0118Sub( kind, value ));
    }

    /**
     * �d�����M����(0118)
     * ���l����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0118Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        LinenRoomCount = 0;
        LinenRoomCode = new int[OWNERINFO_ROOMMAX];
        LinenRefUse = new int[OWNERINFO_ROOMMAX];
        LinenConveniUse = new int[OWNERINFO_ROOMMAX];
        LinenGroup = new String[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0118";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0119" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            LinenRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < LinenRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 64), 3 );
                                LinenRoomCode[i] = Integer.valueOf( data ).intValue();

                                // �①�ɗ��p���
                                data = new String( cdata, 59 + (i * 64), 1 );
                                LinenRefUse[i] = Integer.valueOf( data ).intValue();

                                // �R���r�j���p���
                                data = new String( cdata, 60 + (i * 64), 1 );
                                LinenConveniUse[i] = Integer.valueOf( data ).intValue();

                                // ��Ɣ�
                                data = new String( cdata, 61 + (i * 64), 8 );
                                LinenGroup[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0118:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0120)
     * �����o�[��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0120()
    {
        return(sendPacket0120Sub( 0, "" ));
    }

    /**
     * �d�����M����(0120)
     * �����o�[��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0120(int kind, String value)
    {
        return(sendPacket0120Sub( kind, value ));
    }

    /**
     * �d�����M����(0120)
     * �����o�[��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0120Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        MemberRoomCount = 0;
        MemberRoomCode = new int[OWNERINFO_ROOMMAX];
        MemberCustomId = new String[OWNERINFO_ROOMMAX];
        MemberNickName = new String[OWNERINFO_ROOMMAX];
        MemberName = new String[OWNERINFO_ROOMMAX];
        MemberRankName = new String[OWNERINFO_ROOMMAX];
        MemberCount = new int[OWNERINFO_ROOMMAX];
        MemberPoint = new int[OWNERINFO_ROOMMAX];
        MemberPoint2 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday1 = new int[OWNERINFO_ROOMMAX];
        MemberBirthday2 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial1 = new int[OWNERINFO_ROOMMAX];
        MemberMemorial2 = new int[OWNERINFO_ROOMMAX];
        MemberEntryDate = new int[OWNERINFO_ROOMMAX];
        MemberNowRanking = new int[OWNERINFO_ROOMMAX];
        MemberOldRanking = new int[OWNERINFO_ROOMMAX];
        MemberRankingCount = new int[OWNERINFO_ROOMMAX];
        MemberAddupCount = new int[OWNERINFO_ROOMMAX];
        MemberRankingTotal = new int[OWNERINFO_ROOMMAX];
        MemberAddupTotal = new int[OWNERINFO_ROOMMAX];
        MemberSurplus = new int[OWNERINFO_ROOMMAX];
        MemberEvent = new int[OWNERINFO_ROOMMAX];
        MemberWarning = new int[OWNERINFO_ROOMMAX];
        MemberContact = new int[OWNERINFO_ROOMMAX];
        MemberPresent = new int[OWNERINFO_ROOMMAX];
        MemberEventInfo = new int[OWNERINFO_ROOMMAX][OWNERINFO_MEMBEREVENTMAX];
        MemberContact1 = new String[OWNERINFO_ROOMMAX];
        MemberContact2 = new String[OWNERINFO_ROOMMAX];
        MemberWarning1 = new String[OWNERINFO_ROOMMAX];
        MemberWarning2 = new String[OWNERINFO_ROOMMAX];
        MemberUserId = new String[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0120";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0121" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            MemberRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < MemberRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 512), 3 );
                                MemberRoomCode[i] = Integer.valueOf( data ).intValue();

                                // �����o�[�h�c
                                data = new String( cdata, 59 + (i * 512), 9 );
                                MemberCustomId[i] = data.trim();

                                // �j�b�N�l�[��
                                data = new String( cdata, 68 + (i * 512), 20 );
                                MemberNickName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ����
                                data = new String( cdata, 88 + (i * 512), 40 );
                                MemberName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����o�[�����N��
                                data = new String( cdata, 128 + (i * 512), 40 );
                                MemberRankName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����p��
                                data = new String( cdata, 168 + (i * 512), 9 );
                                MemberCount[i] = Integer.valueOf( data ).intValue();

                                // �����o�[�|�C���g
                                data = new String( cdata, 177 + (i * 512), 9 );
                                MemberPoint[i] = Integer.valueOf( data ).intValue();

                                // �����o�[�|�C���g�Q
                                data = new String( cdata, 186 + (i * 512), 9 );
                                MemberPoint2[i] = Integer.valueOf( data ).intValue();

                                // �a�����P
                                data = new String( cdata, 195 + (i * 512), 8 );
                                MemberBirthday1[i] = Integer.valueOf( data ).intValue();

                                // �a�����Q
                                data = new String( cdata, 203 + (i * 512), 8 );
                                MemberBirthday2[i] = Integer.valueOf( data ).intValue();

                                // �L�O���P
                                data = new String( cdata, 211 + (i * 512), 8 );
                                MemberMemorial1[i] = Integer.valueOf( data ).intValue();

                                // �L�O���Q
                                data = new String( cdata, 219 + (i * 512), 8 );
                                MemberMemorial2[i] = Integer.valueOf( data ).intValue();

                                // �o�^��
                                data = new String( cdata, 227 + (i * 512), 8 );
                                MemberEntryDate[i] = Integer.valueOf( data ).intValue();

                                // ���������L���O
                                data = new String( cdata, 235 + (i * 512), 6 );
                                MemberNowRanking[i] = Integer.valueOf( data ).intValue();

                                // �O�������L���O
                                data = new String( cdata, 241 + (i * 512), 6 );
                                MemberOldRanking[i] = Integer.valueOf( data ).intValue();

                                // �����L���O�����p��
                                data = new String( cdata, 247 + (i * 512), 6 );
                                MemberRankingCount[i] = Integer.valueOf( data ).intValue();

                                // �W�v���ԓ����p��
                                data = new String( cdata, 253 + (i * 512), 6 );
                                MemberAddupCount[i] = Integer.valueOf( data ).intValue();

                                // �����L���O�����p���z
                                data = new String( cdata, 259 + (i * 512), 9 );
                                MemberRankingTotal[i] = Integer.valueOf( data ).intValue();

                                // �W�v���ԓ����p���z
                                data = new String( cdata, 268 + (i * 512), 9 );
                                MemberAddupTotal[i] = Integer.valueOf( data ).intValue();

                                // �J�z���p���z
                                data = new String( cdata, 277 + (i * 512), 6 );
                                MemberSurplus[i] = Integer.valueOf( data ).intValue();

                                // �����o�[�C�x���g
                                data = new String( cdata, 283 + (i * 512), 1 );
                                MemberEvent[i] = Integer.valueOf( data ).intValue();

                                // �x�����
                                data = new String( cdata, 284 + (i * 512), 1 );
                                MemberWarning[i] = Integer.valueOf( data ).intValue();

                                // �A�����
                                data = new String( cdata, 285 + (i * 512), 1 );
                                MemberContact[i] = Integer.valueOf( data ).intValue();

                                // �i�i���
                                data = new String( cdata, 286 + (i * 512), 1 );
                                MemberPresent[i] = Integer.valueOf( data ).intValue();

                                // �C�x���g���
                                for( j = 0 ; j < OWNERINFO_MEMBEREVENTMAX ; j++ )
                                {
                                    data = new String( cdata, 287 + (i * 512) + j, 1 );
                                    MemberEventInfo[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // �A�������P
                                data = new String( cdata, 297 + (i * 512), 40 );
                                MemberContact1[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �A�������Q
                                data = new String( cdata, 337 + (i * 512), 40 );
                                MemberContact2[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �x�������P
                                data = new String( cdata, 377 + (i * 512), 40 );
                                MemberWarning1[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �x�������Q
                                data = new String( cdata, 417 + (i * 512), 40 );
                                MemberWarning2[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���[�UID
                                data = new String( cdata, 457 + (i * 512), 40 );
                                MemberUserId[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0120:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0122)
     * �Ԕԏ�
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0122()
    {
        return(sendPacket0122Sub( 0, "" ));
    }

    /**
     * �d�����M����(0122)
     * �Ԕԏ�
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0122(int kind, String value)
    {
        return(sendPacket0122Sub( kind, value ));
    }

    /**
     * �d�����M����(0122)
     * �Ԕԏ�
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0122Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        CarRoomCount = 0;
        CarRoomCode = new int[OWNERINFO_ROOMMAX];
        CarArea = new String[OWNERINFO_ROOMMAX];
        CarKind = new String[OWNERINFO_ROOMMAX];
        CarType = new String[OWNERINFO_ROOMMAX];
        CarNo = new String[OWNERINFO_ROOMMAX];
        CarParkingNo = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0122";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0123" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            CarRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < CarRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 64), 3 );
                                CarRoomCode[i] = Integer.valueOf( data ).intValue();

                                // �Ԕԁi�n��j
                                data = new String( cdata, 59 + (i * 64), 8 );
                                CarArea[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �Ԕԁi��ʁj
                                data = new String( cdata, 67 + (i * 64), 2 );
                                CarKind[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �Ԕԁi�Ԏ�j
                                data = new String( cdata, 69 + (i * 64), 3 );
                                CarType[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �Ԕ�
                                data = new String( cdata, 72 + (i * 64), 4 );
                                CarNo[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���ԏ�ԍ�
                                data = new String( cdata, 76 + (i * 64), 3 );
                                CarParkingNo[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0112:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0124)
     * ���Z�@��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0124()
    {
        return(sendPacket0124Sub( 0, "" ));
    }

    /**
     * �d�����M����(0124)
     * ���Z�@��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0124(int kind, String value)
    {
        return(sendPacket0124Sub( kind, value ));
    }

    /**
     * �d�����M����(0124)
     * ���Z�@��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0124Sub(int kind, String value)
    {
        int i;
        int j;
        int k;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        TexRoomCount = 0;
        TexRoomCode = new int[OWNERINFO_ROOMMAX];
        TexMode = new int[OWNERINFO_ROOMMAX];
        TexSupplyStat = new int[OWNERINFO_ROOMMAX];
        TexSecurityStat = new int[OWNERINFO_ROOMMAX];
        TexDoorStat = new int[OWNERINFO_ROOMMAX];
        TexLineStat = new int[OWNERINFO_ROOMMAX];
        TexErrorStat = new int[OWNERINFO_ROOMMAX];
        TexPayStat = new int[OWNERINFO_ROOMMAX];
        TexChargeStat = new int[OWNERINFO_ROOMMAX];
        TexErrorCode = new String[OWNERINFO_ROOMMAX];
        TexErrorMsg = new String[OWNERINFO_ROOMMAX];
        TexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSALESMAX];
        TexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFEMAX];
        TexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexSupplyTotal = new int[OWNERINFO_ROOMMAX];
        TexSupplyDate = new int[OWNERINFO_ROOMMAX];
        TexSupplyTime = new int[OWNERINFO_ROOMMAX];
        TexSurplus = new int[OWNERINFO_ROOMMAX];
        TexCardStat = new int[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0124";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0125" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            TexRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TexRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 700), 3 );
                                TexRoomCode[i] = Integer.valueOf( data ).intValue();

                                // ���Z�@���[�h
                                data = new String( cdata, 59 + (i * 700), 1 );
                                TexMode[i] = Integer.valueOf( data ).intValue();

                                // ��[���
                                data = new String( cdata, 60 + (i * 700), 1 );
                                TexSupplyStat[i] = Integer.valueOf( data ).intValue();

                                // �Z�L�����e�B���
                                data = new String( cdata, 61 + (i * 700), 1 );
                                TexSecurityStat[i] = Integer.valueOf( data ).intValue();

                                // �����
                                data = new String( cdata, 62 + (i * 700), 1 );
                                TexDoorStat[i] = Integer.valueOf( data ).intValue();

                                // ������
                                data = new String( cdata, 63 + (i * 700), 1 );
                                TexLineStat[i] = Integer.valueOf( data ).intValue();

                                // �G���[���
                                data = new String( cdata, 64 + (i * 700), 1 );
                                TexErrorStat[i] = Integer.valueOf( data ).intValue();

                                // ���Z���
                                data = new String( cdata, 65 + (i * 700), 1 );
                                TexPayStat[i] = Integer.valueOf( data ).intValue();

                                // �ޑK���
                                data = new String( cdata, 66 + (i * 700), 3 );
                                TexChargeStat[i] = Integer.valueOf( data ).intValue();

                                // �G���[�R�[�h
                                data = new String( cdata, 69 + (i * 700), 4 );
                                TexErrorCode[i] = data.trim();

                                // �G���[���e
                                data = new String( cdata, 73 + (i * 700), 32 );
                                TexErrorMsg[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ������z�f�[�^
                                for( j = 0 ; j < OWNERINFO_TEXSALESMAX ; j++ )
                                {
                                    // ������z
                                    data = new String( cdata, 105 + (i * 700) + (j * 18), 9 );
                                    TexSalesTotal[i][j] = Integer.valueOf( data ).intValue();
                                    // �����
                                    data = new String( cdata, 114 + (i * 700) + (j * 18), 9 );
                                    TexSalesCount[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ���ɏ��f�[�^
                                for( j = 0 ; j < OWNERINFO_TEXSAFEMAX ; j++ )
                                {
                                    for( k = 0 ; k < OWNERINFO_TEXSAFETYPEMAX ; k++ )
                                    {
                                        // ����
                                        data = new String( cdata, 177 + (i * 700) + (j * 105) + (k * 6), 6 );
                                        TexSafeCount[i][j][k] = Integer.valueOf( data ).intValue();
                                    }

                                    // ���v���z
                                    data = new String( cdata, 273 + (i * 700) + (j * 105), 9 );
                                    TexSafeTotal[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ���ɖ�����
                                for( j = 0 ; j < OWNERINFO_TEXSAFETYPEMAX ; j++ )
                                {
                                    // ���v���z
                                    data = new String( cdata, 597 + (i * 700) + (j * 1), 1 );
                                    TexSafeStat[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ���Ȃ���[���z
                                data = new String( cdata, 613 + (i * 700), 9 );
                                TexSupplyTotal[i] = Integer.valueOf( data ).intValue();

                                // �������[���t
                                data = new String( cdata, 622 + (i * 700), 8 );
                                TexSupplyDate[i] = Integer.valueOf( data ).intValue();

                                // �������[����
                                data = new String( cdata, 630 + (i * 700), 8 );
                                TexSupplyTime[i] = Integer.valueOf( data ).intValue();

                                // �]���
                                data = new String( cdata, 638 + (i * 700), 9 );
                                TexSurplus[i] = Integer.valueOf( data ).intValue();

                                // �J�[�h���
                                data = new String( cdata, 647 + (i * 700), 1 );
                                TexCardStat[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0124:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0126)
     * �}���`���f�B�A��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0126()
    {
        return(sendPacket0126Sub( 0, "" ));
    }

    /**
     * �d�����M����(0126)
     * �}���`���f�B�A��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0126(int kind, String value)
    {
        return(sendPacket0126Sub( kind, value ));
    }

    /**
     * �d�����M����(0126)
     * �}���`���f�B�A��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0126Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        MultiRoomCount = 0;
        MultiRoomCode = new int[OWNERINFO_ROOMMAX];
        MultiLineStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorStat = new int[OWNERINFO_ROOMMAX];
        MultiPowerStat = new int[OWNERINFO_ROOMMAX];
        MultiErrorCode = new String[OWNERINFO_ROOMMAX];
        MultiErrorMsg = new String[OWNERINFO_ROOMMAX];
        MultiChannelNo = new int[OWNERINFO_ROOMMAX];
        MultiChannelName = new String[OWNERINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0126";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0127" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ������
                            data = new String( cdata, 53, 3 );
                            MultiRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < MultiRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 56 + (i * 128), 3 );
                                MultiRoomCode[i] = Integer.valueOf( data ).intValue();

                                // ������
                                data = new String( cdata, 59 + (i * 128), 1 );
                                MultiLineStat[i] = Integer.valueOf( data ).intValue();

                                // �G���[���
                                data = new String( cdata, 60 + (i * 128), 1 );
                                MultiErrorStat[i] = Integer.valueOf( data ).intValue();

                                // �d�����
                                data = new String( cdata, 61 + (i * 128), 1 );
                                MultiPowerStat[i] = Integer.valueOf( data ).intValue();

                                // �G���[�R�[�h
                                data = new String( cdata, 62 + (i * 128), 4 );
                                MultiErrorCode[i] = data.trim();

                                // �G���[���e
                                data = new String( cdata, 66 + (i * 128), 32 );
                                MultiErrorMsg[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �`�����l���ԍ�
                                data = new String( cdata, 98 + (i * 128), 3 );
                                MultiChannelNo[i] = Integer.valueOf( data ).intValue();

                                // �`�����l������
                                data = new String( cdata, 101 + (i * 128), 40 );
                                MultiChannelName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0126:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0128)
     * �����ڍׁi���p���ׁj
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0128()
    {
        return(sendPacket0128Sub( 0, "" ));
    }

    /**
     * �d�����M����(0128)
     * �����ڍׁi���p���ׁj
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0128(int kind, String value)
    {
        return(sendPacket0128Sub( kind, value ));
    }

    /**
     * �d�����M����(0128)
     * �����ڍׁi���p���ׁj
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0128Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        DetailUseRoomCode = 0;
        DetailUseCount = 0;
        DetailUseGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailUseGoodsCount = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsRegularPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailUseGoodsDiscount = new int[OWNERINFO_DETAILMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0128";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0129" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �����R�[�h
                            data = new String( cdata, 53, 3 );
                            DetailUseRoomCode = Integer.valueOf( data ).intValue();

                            // ���p���א�
                            data = new String( cdata, 56, 3 );
                            DetailUseCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < DetailUseCount ; i++ )
                            {
                                // ���p���ז�
                                data = new String( cdata, 59 + (i * 128), 40 );
                                DetailUseGoodsName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ����
                                data = new String( cdata, 99 + (i * 128), 3 );
                                DetailUseGoodsCount[i] = Integer.valueOf( data ).intValue();

                                // ���K�P��
                                data = new String( cdata, 102 + (i * 128), 9 );
                                DetailUseGoodsRegularPrice[i] = Integer.valueOf( data ).intValue();

                                // �P��
                                data = new String( cdata, 111 + (i * 128), 9 );
                                DetailUseGoodsPrice[i] = Integer.valueOf( data ).intValue();

                                // ������
                                data = new String( cdata, 120 + (i * 128), 3 );
                                DetailUseGoodsDiscount[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0128:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0130)
     * �����ڍׁi�x�����󋵁j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0130()
    {
        return(sendPacket0130Sub( 0, "" ));
    }

    /**
     * �d�����M����(0130)
     * �����ڍׁi�x�����󋵁j
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0130(int kind, String value)
    {
        return(sendPacket0130Sub( kind, value ));
    }

    /**
     * �d�����M����(0130)
     * �����ڍׁi�x�����󋵁j
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0130Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        DetailPayRoomCode = 0;
        DetailPayTotal = 0;
        DetailPayClaim = 0;
        DetailPayCount = 0;
        DetailPayName = new String[OWNERINFO_DETAILMAX];
        DetailPayAmount = new int[OWNERINFO_DETAILMAX];
        DetailPayMoney = new int[OWNERINFO_DETAILMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0130";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0131" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �����R�[�h
                            data = new String( cdata, 53, 3 );
                            DetailPayRoomCode = Integer.valueOf( data ).intValue();

                            // ���p���v
                            data = new String( cdata, 56, 9 );
                            DetailPayTotal = Integer.valueOf( data ).intValue();

                            // �������z
                            data = new String( cdata, 65, 9 );
                            DetailPayClaim = Integer.valueOf( data ).intValue();

                            // �x�������א�
                            data = new String( cdata, 74, 3 );
                            DetailPayCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < DetailPayCount ; i++ )
                            {
                                // ���p���ז�
                                data = new String( cdata, 77 + (i * 128), 40 );
                                DetailPayName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ����
                                data = new String( cdata, 117 + (i * 128), 3 );
                                DetailPayAmount[i] = Integer.valueOf( data ).intValue();

                                // ���z
                                data = new String( cdata, 120 + (i * 128), 9 );
                                DetailPayMoney[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0130:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0132)
     * �����ڍׁi���i���ׁj
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0132()
    {
        return(sendPacket0132Sub( 0, "" ));
    }

    /**
     * �d�����M����(0132)
     * �����ڍׁi���i���ׁj
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0132(int kind, String value)
    {
        return(sendPacket0132Sub( kind, value ));
    }

    /**
     * �d�����M����(0132)
     * �����ڍׁi���i���ׁj
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0132Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        DetailGoodsRoomCode = 0;
        DetailGoodsCount = 0;
        DetailGoodsName = new String[OWNERINFO_DETAILMAX];
        DetailGoodsAmount = new int[OWNERINFO_DETAILMAX];
        DetailGoodsPrice = new int[OWNERINFO_DETAILMAX];
        DetailGoodsRef = new int[OWNERINFO_DETAILMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0132";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( RoomCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0133" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �����R�[�h
                            data = new String( cdata, 53, 3 );
                            DetailGoodsRoomCode = Integer.valueOf( data ).intValue();

                            // ���i���א�
                            data = new String( cdata, 56, 3 );
                            DetailGoodsCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < DetailGoodsCount ; i++ )
                            {
                                // ���p���ז�
                                data = new String( cdata, 59 + (i * 128), 40 );
                                DetailGoodsName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ����
                                data = new String( cdata, 99 + (i * 128), 3 );
                                DetailGoodsAmount[i] = Integer.valueOf( data ).intValue();

                                // �P��
                                data = new String( cdata, 102 + (i * 128), 9 );
                                DetailGoodsPrice[i] = Integer.valueOf( data ).intValue();

                                // �①�Ƀt���O
                                data = new String( cdata, 111 + (i * 128), 1 );
                                DetailGoodsRef[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0132:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0134)
     * ����ڕW���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0134()
    {
        return(sendPacket0134Sub( 0, "" ));
    }

    /**
     * �d�����M����(0134)
     * ����ڕW���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0134(int kind, String value)
    {
        return(sendPacket0134Sub( kind, value ));
    }

    /**
     * �d�����M����(0134)
     * ����ڕW���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0134Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        TargetCount = 0;
        TargetTotal = 0;
        TargetModeCount = 0;
        TargetModeCode = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeName = new String[OWNERINFO_SALESTAGETMAX];
        TargetModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0134";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �v��N��
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( TargetMonth );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0135" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �v��N��
                            data = new String( cdata, 53, 6 );
                            TargetMonth = Integer.valueOf( data ).intValue();

                            // �݌v�g��
                            data = new String( cdata, 59, 9 );
                            TargetCount = Integer.valueOf( data ).intValue();

                            // �݌v����z
                            data = new String( cdata, 68, 9 );
                            TargetTotal = Integer.valueOf( data ).intValue();

                            // �������[�h��
                            data = new String( cdata, 77, 2 );
                            TargetModeCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TargetModeCount ; i++ )
                            {
                                // �������[�h
                                data = new String( cdata, 79 + (i * 54), 4 );
                                TargetModeCode[i] = Integer.valueOf( data ).intValue();

                                // �������[�h����
                                data = new String( cdata, 83 + (i * 54), 20 );
                                TargetModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �x�e�g��
                                data = new String( cdata, 103 + (i * 54), 6 );
                                TargetModeRestCount[i] = Integer.valueOf( data ).intValue();

                                // �h���g��
                                data = new String( cdata, 109 + (i * 54), 6 );
                                TargetModeStayCount[i] = Integer.valueOf( data ).intValue();

                                // �x�e����
                                data = new String( cdata, 115 + (i * 54), 9 );
                                TargetModeRestTotal[i] = Integer.valueOf( data ).intValue();

                                // �h������
                                data = new String( cdata, 124 + (i * 54), 9 );
                                TargetModeStayTotal[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0134:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0136)
     * ����ڕW�ݒ�
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0136()
    {
        return(sendPacket0136Sub( 0, "" ));
    }

    /**
     * �d�����M����(0136)
     * ����ڕW�ݒ�
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0136(int kind, String value)
    {
        return(sendPacket0136Sub( kind, value ));
    }

    /**
     * �d�����M����(0136)
     * ����ڕW�ݒ�
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0136Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        Result = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0136";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �v��N��
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( ResultMonth );
                // �݌v�g��
                nf = new DecimalFormat( "000000000" );
                senddata = senddata + nf.format( ResultCount );
                // �݌v����z
                nf = new DecimalFormat( "000000000" );
                senddata = senddata + nf.format( ResultTotal );
                // �������[�h��
                nf = new DecimalFormat( "00" );
                senddata = senddata + nf.format( ResultModeCount );

                for( i = 0 ; i < TargetModeCount ; i++ )
                {
                    // �������[�h
                    nf = new DecimalFormat( "0000" );
                    senddata = senddata + nf.format( ResultModeCode[i] );
                    // �x�e�g��
                    nf = new DecimalFormat( "000000" );
                    senddata = senddata + nf.format( ResultModeRestCount[i] );
                    // �h���g��
                    nf = new DecimalFormat( "000000" );
                    senddata = senddata + nf.format( ResultModeStayCount[i] );
                    // �x�e������z
                    nf = new DecimalFormat( "000000000" );
                    senddata = senddata + nf.format( ResultModeRestTotal[i] );
                    // �h��������z
                    nf = new DecimalFormat( "000000000" );
                    senddata = senddata + nf.format( ResultModeStayTotal[i] );
                }

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0137" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �v��N��
                            data = new String( cdata, 53, 6 );
                            ResultMonth = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0136:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0138)
     * ������я��擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0138()
    {
        return(sendPacket0138Sub( 0, "" ));
    }

    /**
     * �d�����M����(0138)
     * ������я��擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0138(int kind, String value)
    {
        return(sendPacket0138Sub( kind, value ));
    }

    /**
     * �d�����M����(0138)
     * ������я��擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0138Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        TargetCount = 0;
        TargetTotal = 0;
        TargetModeCount = 0;
        TargetModeCode = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeName = new String[OWNERINFO_SALESTAGETMAX];
        TargetModeRestCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayCount = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeRestTotal = new int[OWNERINFO_SALESTAGETMAX];
        TargetModeStayTotal = new int[OWNERINFO_SALESTAGETMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0138";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �v��N��
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( TargetMonth );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0139" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �v��N��
                            data = new String( cdata, 53, 6 );
                            TargetMonth = Integer.valueOf( data ).intValue();

                            // �݌v�g��
                            data = new String( cdata, 59, 9 );
                            TargetCount = Integer.valueOf( data ).intValue();

                            // �݌v����z
                            data = new String( cdata, 68, 9 );
                            TargetTotal = Integer.valueOf( data ).intValue();

                            // �������[�h��
                            data = new String( cdata, 77, 2 );
                            TargetModeCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TargetModeCount ; i++ )
                            {
                                // �������[�h
                                data = new String( cdata, 79 + (i * 54), 4 );
                                TargetModeCode[i] = Integer.valueOf( data ).intValue();

                                // �������[�h����
                                data = new String( cdata, 83 + (i * 54), 20 );
                                TargetModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �x�e�g��
                                data = new String( cdata, 103 + (i * 54), 6 );
                                TargetModeRestCount[i] = Integer.valueOf( data ).intValue();

                                // �h���g��
                                data = new String( cdata, 109 + (i * 54), 6 );
                                TargetModeStayCount[i] = Integer.valueOf( data ).intValue();

                                // �x�e����
                                data = new String( cdata, 115 + (i * 54), 9 );
                                TargetModeRestTotal[i] = Integer.valueOf( data ).intValue();

                                // �h������
                                data = new String( cdata, 124 + (i * 54), 9 );
                                TargetModeStayTotal[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0138:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0140)
     * ����C�x���g���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0140()
    {
        return(sendPacket0140Sub( 0, "" ));
    }

    /**
     * �d�����M����(0140)
     * ����C�x���g���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0140(int kind, String value)
    {
        return(sendPacket0140Sub( kind, value ));
    }

    /**
     * �d�����M����(0140)
     * ����C�x���g���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0140Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        EventCount = 0;
        EventDate = new int[OWNERINFO_DOORDETAILMAX];
        EventTime = new int[OWNERINFO_DOORDETAILMAX];
        EventTimeSub = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomCode = new int[OWNERINFO_DOORDETAILMAX];
        EventRoomName = new String[OWNERINFO_DOORDETAILMAX];
        EventLineNo = new int[OWNERINFO_DOORDETAILMAX];
        EventTermId = new int[OWNERINFO_DOORDETAILMAX];
        EventEmployeeCode = new int[OWNERINFO_DOORDETAILMAX];
        EventEventCode = new int[OWNERINFO_DOORDETAILMAX];
        EventSystemErrCode = new int[OWNERINFO_DOORDETAILMAX];
        EventNumData = new int[OWNERINFO_DOORDETAILMAX][6];
        EventStrData = new String[OWNERINFO_DOORDETAILMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0140";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextDate );
                // �擾����
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextTime );
                // �����⏕
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextTimeSub );
                // �����R�[�h
                nf = new DecimalFormat( "0000" );
                senddata = senddata + nf.format( EventGetNextRoomCode );
                // �C�x���g�R�[�h
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( EventGetNextEventCode );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0147" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ���y�[�W�擾���t
                            data = new String( cdata, 53, 8 );
                            EventGetNextDate = Integer.valueOf( data ).intValue();

                            // ���y�[�W�擾����
                            data = new String( cdata, 61, 8 );
                            EventGetNextTime = Integer.valueOf( data ).intValue();

                            // ���y�[�W�擾���ԕ⏕
                            data = new String( cdata, 69, 8 );
                            EventGetNextTimeSub = Integer.valueOf( data ).intValue();

                            // ���y�[�W�����R�[�h
                            data = new String( cdata, 77, 4 );
                            EventGetNextRoomCode = Integer.valueOf( data ).intValue();

                            // ���y�[�W�C�x���g�R�[�h
                            data = new String( cdata, 81, 8 );
                            EventGetNextEventCode = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�擾���t
                            data = new String( cdata, 89, 8 );
                            EventGetPrevDate = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�擾����
                            data = new String( cdata, 97, 8 );
                            EventGetPrevTime = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�擾���ԕ⏕
                            data = new String( cdata, 105, 8 );
                            EventGetPrevTimeSub = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�����R�[�h
                            data = new String( cdata, 113, 4 );
                            EventGetPrevRoomCode = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�C�x���g�R�[�h
                            data = new String( cdata, 117, 8 );
                            EventGetPrevEventCode = Integer.valueOf( data ).intValue();

                            // �擾����
                            data = new String( cdata, 125, 3 );
                            EventCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < EventCount ; i++ )
                            {
                                // ���t
                                data = new String( cdata, 128 + (i * 256), 8 );
                                EventDate[i] = Integer.valueOf( data ).intValue();

                                // ����
                                data = new String( cdata, 136 + (i * 256), 8 );
                                EventTime[i] = Integer.valueOf( data ).intValue();

                                // �����⏕
                                data = new String( cdata, 144 + (i * 256), 8 );
                                EventTimeSub[i] = Integer.valueOf( data ).intValue();

                                // �����R�[�h
                                data = new String( cdata, 152 + (i * 256), 4 );
                                EventRoomCode[i] = Integer.valueOf( data ).intValue();

                                // ��������
                                data = new String( cdata, 156 + (i * 256), 8 );
                                EventRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �n���ԍ�
                                data = new String( cdata, 164 + (i * 256), 4 );
                                EventLineNo[i] = Integer.valueOf( data ).intValue();

                                // �[��ID
                                data = new String( cdata, 168 + (i * 256), 4 );
                                EventTermId[i] = Integer.valueOf( data ).intValue();

                                // �]�ƈ��R�[�h
                                data = new String( cdata, 172 + (i * 256), 4 );
                                EventEmployeeCode[i] = Integer.valueOf( data ).intValue();

                                // �C�x���g�R�[�h
                                data = new String( cdata, 176 + (i * 256), 8 );
                                EventEventCode[i] = Integer.valueOf( data ).intValue();

                                // �V�X�e���G���[�R�[�h
                                data = new String( cdata, 184 + (i * 256), 9 );
                                EventSystemErrCode[i] = Integer.valueOf( data ).intValue();

                                for( j = 0 ; j < 6 ; j++ )
                                {
                                    // �t�я��
                                    data = new String( cdata, 193 + (i * 256) + (j * 9), 9 );
                                    EventNumData[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // �t�ѕ������
                                data = new String( cdata, 247 + (i * 256), 32 );
                                EventStrData[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0140:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0142)
     * �I�[�g�J�����_�[���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0142()
    {
        return(sendPacket0142Sub( 0, "" ));
    }

    /**
     * �d�����M����(0142)
     * �I�[�g�J�����_�[���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0142(int kind, String value)
    {
        return(sendPacket0142Sub( kind, value ));
    }

    /**
     * �d�����M����(0142)
     * �I�[�g�J�����_�[���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0142Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        CalModeCount = 0;
        CalModeCode = new int[OWNERINFO_CHAGEMODEMAX];
        CalModeName = new String[OWNERINFO_CHAGEMODEMAX];
        CalDayDate = new int[OWNERINFO_DAYMAX];
        CalDayMode = new int[OWNERINFO_DAYMAX];
        CalDayModeName = new String[OWNERINFO_DAYMAX];
        CalDayWeekKind = new int[OWNERINFO_DAYMAX];
        CalDayHolidayKind = new int[OWNERINFO_DAYMAX];
        CalDayMemo1 = new String[OWNERINFO_DAYMAX];
        CalDayMemo2 = new String[OWNERINFO_DAYMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0142";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾�N��
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( CalGetDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0143" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �擾�N��
                            data = new String( cdata, 53, 6 );
                            CalGetDate = Integer.valueOf( data ).intValue();

                            // �������[�h��
                            data = new String( cdata, 59, 2 );
                            CalModeCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_CHAGEMODEMAX ; i++ )
                            {
                                // �������[�h�ԍ�
                                data = new String( cdata, 61 + (i * 24), 4 );
                                CalModeCode[i] = Integer.valueOf( data ).intValue();

                                // �������[�h����
                                data = new String( cdata, 65 + (i * 24), 20 );
                                CalModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }

                            for( i = 0 ; i < OWNERINFO_DAYMAX ; i++ )
                            {
                                // ���t
                                data = new String( cdata, 541 + (i * 108), 2 );
                                CalDayDate[i] = Integer.valueOf( data ).intValue();

                                // �������[�h
                                data = new String( cdata, 543 + (i * 108), 4 );
                                CalDayMode[i] = Integer.valueOf( data ).intValue();

                                // �������[�h����
                                data = new String( cdata, 547 + (i * 108), 20 );
                                CalDayModeName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �j�����
                                data = new String( cdata, 567 + (i * 108), 1 );
                                CalDayWeekKind[i] = Integer.valueOf( data ).intValue();

                                // �x�����
                                data = new String( cdata, 568 + (i * 108), 1 );
                                CalDayHolidayKind[i] = Integer.valueOf( data ).intValue();

                                // ���L�����P
                                data = new String( cdata, 569 + (i * 108), 40 );
                                CalDayMemo1[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���L�����Q
                                data = new String( cdata, 609 + (i * 108), 40 );
                                CalDayMemo2[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0142:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0144)
     * �I�[�g�J�����_�[�ݒ�
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0144()
    {
        return(sendPacket0144Sub( 0, "" ));
    }

    /**
     * �d�����M����(0144)
     * �I�[�g�J�����_�[�ݒ�
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0144(int kind, String value)
    {
        return(sendPacket0144Sub( kind, value ));
    }

    /**
     * �d�����M����(0144)
     * �I�[�g�J�����_�[�ݒ�
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0144Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        Result = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0144";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �ݒ�N��
                nf = new DecimalFormat( "000000" );
                senddata = senddata + nf.format( CalGetDate );

                for( i = 0 ; i < OWNERINFO_DAYMAX ; i++ )
                {
                    // ���t
                    nf = new DecimalFormat( "00" );
                    senddata = senddata + nf.format( CalDayDate[i] );
                    // �������[�h
                    nf = new DecimalFormat( "0000" );
                    senddata = senddata + nf.format( CalDayMode[i] );
                    // �j�����
                    nf = new DecimalFormat( "0" );
                    senddata = senddata + nf.format( CalDayWeekKind[i] );
                    // �x�����
                    nf = new DecimalFormat( "0" );
                    senddata = senddata + nf.format( CalDayHolidayKind[i] );
                    // ���L�����P
                    data = format.leftFitFormat( CalDayMemo1[i], 40 );
                    senddata = senddata + data;
                    // ���L�����Q
                    data = format.leftFitFormat( CalDayMemo2[i], 40 );
                    senddata = senddata + data;
                }
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0145" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �擾�N��
                            data = new String( cdata, 53, 6 );
                            CalGetDate = Integer.valueOf( data ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0144:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0146)
     * �����󋵗����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0146()
    {
        return(sendPacket0146Sub( 0, "" ));
    }

    /**
     * �d�����M����(0146)
     * �����󋵗����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0146(int kind, String value)
    {
        return(sendPacket0146Sub( kind, value ));
    }

    /**
     * �d�����M����(0146)
     * �����󋵗����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0146Sub(int kind, String value)
    {

        if ( !dateCheck( RoomHistoryDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        RoomHistoryRoomCount = 0;
        RoomHistoryCount = 0;
        RoomHistoryTime = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryEmpty = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryExist = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryClean = new int[OWNERINFO_TIMEHOURMAX];
        RoomHistoryStop = new int[OWNERINFO_TIMEHOURMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0146";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( RoomHistoryDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0147" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ��������
                            data = new String( cdata, 53, 3 );
                            RoomHistoryRoomCount = Integer.valueOf( data ).intValue();

                            // �擾����
                            data = new String( cdata, 56, 2 );
                            RoomHistoryCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < OWNERINFO_TIMEHOURMAX ; i++ )
                            {
                                // ����
                                data = new String( cdata, 58 + (i * 16), 4 );
                                RoomHistoryTime[i] = Integer.valueOf( data ).intValue();

                                // �󎺐�
                                data = new String( cdata, 62 + (i * 16), 3 );
                                RoomHistoryEmpty[i] = Integer.valueOf( data ).intValue();

                                // �ݎ���
                                data = new String( cdata, 65 + (i * 16), 3 );
                                RoomHistoryExist[i] = Integer.valueOf( data ).intValue();

                                // ������
                                data = new String( cdata, 68 + (i * 16), 3 );
                                RoomHistoryClean[i] = Integer.valueOf( data ).intValue();

                                // ���~��
                                data = new String( cdata, 71 + (i * 16), 3 );
                                RoomHistoryStop[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0146:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0148)
     * �����o�[�����N�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0148()
    {
        return(sendPacket0148Sub( 0, "" ));
    }

    /**
     * �d�����M����(0148)
     * �����o�[�����N�擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0148(int kind, String value)
    {
        return(sendPacket0148Sub( kind, value ));
    }

    /**
     * �d�����M����(0148)
     * �����o�[�����N�擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0148Sub(int kind, String value)
    {
        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;

        // �f�[�^�̃N���A
        Result = 0;
        CustomRankCount = 0;
        CustomRankCode = new int[OWNERINFO_CUSTOMRANKMAX];
        CustomRankName = new String[OWNERINFO_CUSTOMRANKMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0148";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0149" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �����o�[�����N��
                            data = new String( cdata, 53, 2 );
                            CustomRankCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < CustomRankCount ; i++ )
                            {
                                // �����o�[�����N�R�[�h
                                data = new String( cdata, 55 + (i * 64), 3 );
                                CustomRankCode[i] = Integer.valueOf( data ).intValue();

                                // �����o�[�����N����
                                data = new String( cdata, 58 + (i * 64), 40 );
                                CustomRankName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0148:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0152)
     * ���Z�@���O�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0152()
    {
        return(sendPacket0152Sub( 0, "" ));
    }

    /**
     * �d�����M����(0152)
     * ���Z�@���O�擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0152(int kind, String value)
    {
        return(sendPacket0152Sub( kind, value ));
    }

    /**
     * �d�����M����(0152)
     * ���Z�@���O�擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0152Sub(int kind, String value)
    {

        if ( !dateCheck( TexlogNextDate ) )
        {
            return(false);
        }

        int i;
        int j;
        int k;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        TexlogCount = 0;
        TexlogDate = new int[OWNERINFO_TEXLOGMAX];
        TexlogTime = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomCode = new int[OWNERINFO_TEXLOGMAX];
        TexlogRoomName = new String[OWNERINFO_TEXLOGMAX];
        TexlogLineNo = new int[OWNERINFO_TEXLOGMAX];
        TexlogTermId = new int[OWNERINFO_TEXLOGMAX];
        TexlogClaimed = new int[OWNERINFO_TEXLOGMAX];
        TexlogSurplus = new int[OWNERINFO_TEXLOGMAX];
        TexlogSafeCount = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX][OWNERINFO_TEXSAFETYPEMAX];
        TexlogSafeTotal = new int[OWNERINFO_TEXLOGMAX][OWNERINFO_TEXSAFEMAX];
        TexlogLogLevel = new int[OWNERINFO_TEXLOGMAX];
        TexlogLogContent = new String[OWNERINFO_TEXLOGMAX];
        TexlogLogDetail = new String[OWNERINFO_TEXLOGMAX];
        TexlogStatTrade = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatInOut = new int[OWNERINFO_TEXLOGMAX];
        TexlogStatSecurity = new int[OWNERINFO_TEXLOGMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0152";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( TexlogNextDate );
                // �擾����
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( TexlogNextTime );
                // �����R�[�h
                nf = new DecimalFormat( "0000" );
                senddata = senddata + nf.format( TexlogNextRoomCode );
                // �n���ԍ�
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( TexlogNextLineNo );
                // �[��ID
                nf = new DecimalFormat( "0000" );
                senddata = senddata + nf.format( TexlogNextTermId );
                // ���O���x��
                nf = new DecimalFormat( "00" );
                senddata = senddata + nf.format( TexlogGetLogLevel );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0153" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // ���y�[�W���t
                            data = new String( cdata, 53, 8 );
                            TexlogNextDate = Integer.valueOf( data ).intValue();

                            // ���y�[�W����
                            data = new String( cdata, 61, 8 );
                            TexlogNextTime = Integer.valueOf( data ).intValue();

                            // ���y�[�W�����R�[�h
                            data = new String( cdata, 69, 4 );
                            TexlogNextRoomCode = Integer.valueOf( data ).intValue();

                            // ���y�[�W�n���ԍ�
                            data = new String( cdata, 73, 3 );
                            TexlogNextLineNo = Integer.valueOf( data ).intValue();

                            // ���y�[�W�[��ID
                            data = new String( cdata, 76, 4 );
                            TexlogNextTermId = Integer.valueOf( data ).intValue();

                            // �O�y�[�W���t
                            data = new String( cdata, 80, 8 );
                            TexlogPrevDate = Integer.valueOf( data ).intValue();

                            // �O�y�[�W����
                            data = new String( cdata, 88, 8 );
                            TexlogPrevTime = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�����R�[�h
                            data = new String( cdata, 96, 4 );
                            TexlogPrevRoomCode = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�n���ԍ�
                            data = new String( cdata, 100, 3 );
                            TexlogPrevLineNo = Integer.valueOf( data ).intValue();

                            // �O�y�[�W�[��ID
                            data = new String( cdata, 103, 4 );
                            TexlogPrevTermId = Integer.valueOf( data ).intValue();

                            // �擾����
                            data = new String( cdata, 107, 3 );
                            TexlogCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TexlogCount ; i++ )
                            {
                                // ���t
                                data = new String( cdata, 110 + (i * 700), 8 );
                                TexlogDate[i] = Integer.valueOf( data ).intValue();

                                // ����
                                data = new String( cdata, 118 + (i * 700), 8 );
                                TexlogTime[i] = Integer.valueOf( data ).intValue();

                                // �����R�[�h
                                data = new String( cdata, 126 + (i * 700), 4 );
                                TexlogRoomCode[i] = Integer.valueOf( data ).intValue();

                                // ��������
                                data = new String( cdata, 130 + (i * 700), 8 );
                                TexlogRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �n���ԍ�
                                data = new String( cdata, 138 + (i * 700), 3 );
                                TexlogLineNo[i] = Integer.valueOf( data ).intValue();

                                // �[��ID
                                data = new String( cdata, 141 + (i * 700), 4 );
                                TexlogTermId[i] = Integer.valueOf( data ).intValue();

                                // �������z
                                data = new String( cdata, 145 + (i * 700), 9 );
                                TexlogClaimed[i] = Integer.valueOf( data ).intValue();

                                // �]���
                                data = new String( cdata, 154 + (i * 700), 9 );
                                TexlogSurplus[i] = Integer.valueOf( data ).intValue();

                                for( j = 0 ; j < 4 ; j++ )
                                {
                                    for( k = 0 ; k < 16 ; k++ )
                                    {
                                        // ���ɏ�񖇐�
                                        data = new String( cdata, 163 + (i * 700) + (j * 105) + (k * 6), 6 );
                                        TexlogSafeCount[i][j][k] = Integer.valueOf( data ).intValue();
                                    }

                                    // ���ɏ�񍇌v���z
                                    data = new String( cdata, 259 + (i * 700) + (j * 105), 9 );
                                    TexlogSafeTotal[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ���O���x��
                                data = new String( cdata, 583 + (i * 700), 2 );
                                TexlogLogLevel[i] = Integer.valueOf( data ).intValue();

                                // ���O���e
                                data = new String( cdata, 585 + (i * 700), 40 );
                                TexlogLogContent[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���O�ڍ�
                                data = new String( cdata, 625 + (i * 700), 80 );
                                TexlogLogDetail[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ������
                                data = new String( cdata, 705 + (i * 700), 2 );
                                TexlogStatTrade[i] = Integer.valueOf( data ).intValue();

                                // ���o�����
                                data = new String( cdata, 707 + (i * 700), 1 );
                                TexlogStatInOut[i] = Integer.valueOf( data ).intValue();

                                // �Z�L�����e�B���
                                data = new String( cdata, 708 + (i * 700), 1 );
                                TexlogStatSecurity[i] = Integer.valueOf( data ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0152:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0154)
     * �V�V������ڍ׏��擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0154()
    {
        return(sendPacket0154Sub( 0, "" ));
    }

    /**
     * �d�����M����(0154)
     * �V�V������ڍ׏��擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0154(int kind, String value)
    {
        return(sendPacket0154Sub( kind, value ));
    }

    /**
     * �d�����M����(0154)
     * �V�V������ڍ׏��擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0154Sub(int kind, String value)
    {

        if ( !dateCheck( AscSalesDetailGetStartDate, AscSalesDetailGetEndDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        AscSalesDetailStay = 0;
        AscSalesDetailStayBeforeOver = 0;
        AscSalesDetailStayAfterOver = 0;
        AscSalesDetailRest = 0;
        AscSalesDetailRestOver = 0;
        AscSalesDetailTel = 0;
        AscSalesDetailAdvance = 0;
        AscSalesDetailSubTotal = 0;
        AscSalesDetailService = 0;
        AscSalesDetailTaxOut = 0;
        AscSalesDetailTaxIn = 0;
        AscSalesDetailDiscount = 0;
        AscSalesDetailExtra = 0;
        AscSalesDetailMember = 0;
        AscSalesDetailAdjust = 0;
        AscSalesDetailFiller = new int[6];
        AscSalesDetailTotal = 0;

        AscSalesDetailCount = 0;
        AscSalesDetailName = new String[OWNERINFO_SALESDETAILMAX];
        AscSalesDetailAmount = new int[OWNERINFO_SALESDETAILMAX];

        if ( HotelId != null )
        {
            if ( AscSalesDetailGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0154";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾�J�n���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( AscSalesDetailGetStartDate );
                // �擾�I�����t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( AscSalesDetailGetEndDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0155" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �J�n���t
                            data = new String( cdata, 53, 8 );
                            AscSalesDetailGetStartDate = Integer.valueOf( data ).intValue();

                            // �I�����t
                            data = new String( cdata, 61, 8 );
                            AscSalesDetailGetEndDate = Integer.valueOf( data ).intValue();

                            // �h��
                            data = new String( cdata, 69, 9 );
                            AscSalesDetailStay = Integer.valueOf( data ).intValue();

                            // �h���O����
                            data = new String( cdata, 78, 9 );
                            AscSalesDetailStayBeforeOver = Integer.valueOf( data ).intValue();

                            // �h���㉄��
                            data = new String( cdata, 87, 9 );
                            AscSalesDetailStayAfterOver = Integer.valueOf( data ).intValue();

                            // �x�e
                            data = new String( cdata, 96, 9 );
                            AscSalesDetailRest = Integer.valueOf( data ).intValue();

                            // �x�e����
                            data = new String( cdata, 105, 9 );
                            AscSalesDetailRestOver = Integer.valueOf( data ).intValue();

                            // �d�b����
                            data = new String( cdata, 114, 9 );
                            AscSalesDetailTel = Integer.valueOf( data ).intValue();

                            // ���֋�����
                            data = new String( cdata, 123, 9 );
                            AscSalesDetailAdvance = Integer.valueOf( data ).intValue();

                            // ���v
                            data = new String( cdata, 132, 9 );
                            AscSalesDetailSubTotal = Integer.valueOf( data ).intValue();

                            // ��d������
                            data = new String( cdata, 141, 9 );
                            AscSalesDetailService = Integer.valueOf( data ).intValue();

                            // �ŋ��i�O�Łj����
                            data = new String( cdata, 150, 9 );
                            AscSalesDetailTaxOut = Integer.valueOf( data ).intValue();

                            // �ŋ��i���Łj����
                            data = new String( cdata, 159, 9 );
                            AscSalesDetailTaxIn = Integer.valueOf( data ).intValue();

                            // ��������
                            data = new String( cdata, 168, 9 );
                            AscSalesDetailExtra = Integer.valueOf( data ).intValue();

                            // ��������
                            data = new String( cdata, 177, 9 );
                            AscSalesDetailDiscount = Integer.valueOf( data ).intValue();

                            // �����o�[��������
                            data = new String( cdata, 186, 9 );
                            AscSalesDetailMember = Integer.valueOf( data ).intValue();

                            // ����������
                            data = new String( cdata, 195, 9 );
                            AscSalesDetailAdjust = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 204, 9 );
                            AscSalesDetailFiller[0] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 213, 9 );
                            AscSalesDetailFiller[1] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 222, 9 );
                            AscSalesDetailFiller[2] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 231, 9 );
                            AscSalesDetailFiller[3] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 240, 9 );
                            AscSalesDetailFiller[4] = Integer.valueOf( data ).intValue();

                            // �\������
                            data = new String( cdata, 249, 9 );
                            AscSalesDetailFiller[5] = Integer.valueOf( data ).intValue();

                            // �����v
                            data = new String( cdata, 258, 9 );
                            AscSalesDetailTotal = Integer.valueOf( data ).intValue();

                            data = new String( cdata, 267, 2 );
                            AscSalesDetailCount = Integer.valueOf( data ).intValue();
                            for( i = 0 ; i < OWNERINFO_SALESDETAILMAX ; i++ )
                            {
                                // ���ז���
                                data = new String( cdata, 269 + (i * 64), 40 );
                                AscSalesDetailName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �����v
                                data = new String( cdata, 309 + (i * 64), 9 );
                                AscSalesDetailAmount[i] = Integer.valueOf( data ).intValue();
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0154:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0156)
     * �t�����g���Z�@��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0156()
    {
        return(sendPacket0156Sub( 0, "" ));
    }

    /**
     * �d�����M����(0156)
     * �t�����g���Z�@��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0156(int kind, String value)
    {
        return(sendPacket0156Sub( kind, value ));
    }

    /**
     * �d�����M����(0156)
     * �t�����g���Z�@��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0156Sub(int kind, String value)
    {
        int i;
        int j;
        int k;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        FrontTexTermCount = 0;
        FrontTexTermCode = new int[OWNERINFO_ROOMMAX];
        FrontTexTermName = new String[OWNERINFO_ROOMMAX];
        FrontTexServiceStat = new int[OWNERINFO_ROOMMAX];
        FrontTexKeySwStat = new int[OWNERINFO_ROOMMAX];
        FrontTexSecurityStat = new int[OWNERINFO_ROOMMAX];
        FrontTexDoorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexLineStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorStat = new int[OWNERINFO_ROOMMAX];
        FrontTexErrorCode = new String[OWNERINFO_ROOMMAX];
        FrontTexErrorMsg = new String[OWNERINFO_ROOMMAX];
        FrontTexSalesTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSalesCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSALESMAX];
        FrontTexSafeCount = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];
        FrontTexSafeTotal = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFEMAX];
        FrontTexSafeStat = new int[OWNERINFO_ROOMMAX][OWNERINFO_FRONTTEXSAFETYPEMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0156";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �[���ԍ�
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( FrontTexTermCodeIn );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0157" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �[����
                            data = new String( cdata, 53, 3 );
                            FrontTexTermCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < FrontTexTermCount ; i++ )
                            {
                                // �[���ԍ�
                                data = new String( cdata, 56 + (i * 1400), 3 );
                                FrontTexTermCode[i] = Integer.valueOf( data ).intValue();

                                // �[������
                                data = new String( cdata, 59 + (i * 1400), 40 );
                                FrontTexTermName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �戵���
                                data = new String( cdata, 99 + (i * 1400), 1 );
                                FrontTexServiceStat[i] = Integer.valueOf( data ).intValue();

                                // �L�[SW���
                                data = new String( cdata, 100 + (i * 1400), 1 );
                                FrontTexKeySwStat[i] = Integer.valueOf( data ).intValue();

                                // �Z�L�����e�B���
                                data = new String( cdata, 101 + (i * 1400), 1 );
                                FrontTexSecurityStat[i] = Integer.valueOf( data ).intValue();

                                // �����
                                data = new String( cdata, 102 + (i * 1400), 1 );
                                FrontTexDoorStat[i] = Integer.valueOf( data ).intValue();

                                // ������
                                data = new String( cdata, 103 + (i * 1400), 1 );
                                FrontTexLineStat[i] = Integer.valueOf( data ).intValue();

                                // �G���[���
                                data = new String( cdata, 104 + (i * 1400), 1 );
                                FrontTexErrorStat[i] = Integer.valueOf( data ).intValue();

                                // �G���[�R�[�h
                                data = new String( cdata, 111 + (i * 1400), 4 );
                                FrontTexErrorCode[i] = data.trim();

                                // �G���[���e
                                data = new String( cdata, 115 + (i * 1400), 32 );
                                FrontTexErrorMsg[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ������z�f�[�^
                                for( j = 0 ; j < OWNERINFO_FRONTTEXSALESMAX ; j++ )
                                {
                                    // ������z
                                    data = new String( cdata, 147 + (i * 1400) + (j * 18), 9 );
                                    FrontTexSalesTotal[i][j] = Integer.valueOf( data ).intValue();
                                    // �����
                                    data = new String( cdata, 156 + (i * 1400) + (j * 18), 9 );
                                    FrontTexSalesCount[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ���ɏ��f�[�^
                                for( j = 0 ; j < OWNERINFO_FRONTTEXSAFEMAX ; j++ )
                                {
                                    for( k = 0 ; k < OWNERINFO_FRONTTEXSAFETYPEMAX ; k++ )
                                    {
                                        // ����
                                        data = new String( cdata, 291 + (i * 1400) + (j * 105) + (k * 6), 6 );
                                        FrontTexSafeCount[i][j][k] = Integer.valueOf( data ).intValue();
                                    }

                                    // ���v���z
                                    data = new String( cdata, 387 + (i * 1400) + (j * 105), 9 );
                                    FrontTexSafeTotal[i][j] = Integer.valueOf( data ).intValue();
                                }

                                // ���ɖ�����
                                for( j = 0 ; j < OWNERINFO_FRONTTEXSAFETYPEMAX ; j++ )
                                {
                                    // ���v���z
                                    data = new String( cdata, 1341 + (i * 1400) + (j * 1), 1 );
                                    FrontTexSafeStat[i][j] = Integer.valueOf( data ).intValue();
                                }

                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0156:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0158)
     * �����X�e�[�^�X�ڍבJ��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0158()
    {
        return(sendPacket0158Sub( 0, "" ));
    }

    /**
     * �d�����M����(0158)
     * �����X�e�[�^�X�ڍבJ��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0158(int kind, String value)
    {
        return(sendPacket0158Sub( kind, value ));
    }

    /**
     * �d�����M����(0158)
     * �����X�e�[�^�X�ڍבJ��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0158Sub(int kind, String value)
    {
        int i;
        int j;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        TimeChartStartTime = 0;
        TimeChartStatusName = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartStatusForeColor = new String[OWNERINFO_ROOMSTATUSMAX + 2];
        TimeChartRoomCount = 0;
        TimeChartRoomCode = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomName = new String[OWNERINFO_ROOMMAX];
        TimeChartRoomFloor = new int[OWNERINFO_ROOMMAX];
        TimeChartRoomStatus = new int[OWNERINFO_ROOMMAX][OWNERINFO_TIMETABLEMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0158";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �����R�[�h
                nf = new DecimalFormat( "000" );
                senddata = senddata + nf.format( TimeChartRoomCodeOne );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0159" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �^�C���`���[�g�����
                            data = new String( cdata, 53, 4 );
                            TimeChartStartTime = Integer.valueOf( data ).intValue();

                            // �����X�e�[�^�X���
                            for( i = 0 ; i < OWNERINFO_ROOMSTATUSMAX + 2 ; i++ )
                            {
                                // �X�e�[�^�X����
                                data = new String( cdata, 57 + (i * 24), 12 );
                                TimeChartStatusName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // �X�e�[�^�X�F
                                data = new String( cdata, 69 + (i * 24), 6 );
                                TimeChartStatusColor[i] = data.trim();
                                // �X�e�[�^�X�����F
                                data = new String( cdata, 75 + (i * 24), 6 );
                                TimeChartStatusForeColor[i] = data.trim();
                            }

                            // ������
                            data = new String( cdata, 1305, 3 );
                            TimeChartRoomCount = Integer.valueOf( data ).intValue();

                            for( i = 0 ; i < TimeChartRoomCount ; i++ )
                            {
                                // �����R�[�h
                                data = new String( cdata, 1308 + (i * 311), 3 );
                                TimeChartRoomCode[i] = Integer.valueOf( data ).intValue();
                                // ��������
                                data = new String( cdata, 1311 + (i * 311), 8 );
                                TimeChartRoomName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );
                                // �t���A�ԍ�
                                data = new String( cdata, 1319 + (i * 311), 2 );
                                TimeChartRoomFloor[i] = Integer.valueOf( data ).intValue();

                                for( j = 0 ; j < OWNERINFO_TIMETABLEMAX ; j++ )
                                {
                                    // �����X�e�[�^�X
                                    data = new String( cdata, 1331 + (i * 311) + (j * 2), 2 );
                                    TimeChartRoomStatus[i][j] = Integer.valueOf( data ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0158:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * �d�����M����(0160)
     * �ϖ��̔���ڍ׏��擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0160()
    {
        return(sendPacket0160Sub( 0, "" ));
    }

    /**
     * �d�����M����(0160)
     * �ϖ��̔���ڍ׏��擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0160(int kind, String value)
    {
        return(sendPacket0160Sub( kind, value ));
    }

    /**
     * �d�����M����(0160)
     * �ϖ��̔���ڍ׏��擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0160Sub(int kind, String value)
    {

        if ( !dateCheck( ManualSalesDetailGetStartDate, ManualSalesDetailGetEndDate ) )
        {
            return(false);
        }

        int i;
        boolean ret;
        String senddata;
        String header;
        String recvdata;
        String data;
        char cdata[];
        TcpClient tcpclient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        ManualSalesDetailTaxIn = 0;
        ManualSalesDetailTotal = 0;

        ManualSalesDetailCount = 0;
        ManualSalesDetailName = new String[OWNERINFO_MANUALSALESMAX];
        ManualSalesDetailAmount = new int[OWNERINFO_MANUALSALESMAX];

        if ( HotelId != null )
        {
            if ( ManualSalesDetailGetStartDate <= 0 )
            {
                Result = 1;
                return(true);
            }

            // �z�X�g�ڑ�����
            tcpclient = new TcpClient();
            ret = connect( tcpclient, kind, value );

            if ( ret != false )
            {
                format = new StringFormat();

                // �R�}���h
                senddata = "0160";
                // ���[�UID
                senddata = senddata + format.leftFitFormat( TermId, 11 );
                // �p�X���[�h
                senddata = senddata + format.leftFitFormat( Password, 4 );
                // �擾�J�n���t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( ManualSalesDetailGetStartDate );
                // �擾�I�����t
                nf = new DecimalFormat( "00000000" );
                senddata = senddata + nf.format( ManualSalesDetailGetEndDate );
                // �\��
                senddata = senddata + "          ";

                // �d���w�b�_�̎擾
                if ( kind == 1 )
                {
                    header = tcpclient.getPacketHeader( value, senddata.length() );
                }
                else
                {
                    header = tcpclient.getPacketHeader( HotelId, senddata.length() );
                }
                // �d���̌���
                senddata = header + senddata;

                try
                {
                    // �d�����M
                    tcpclient.send( senddata );

                    // ��M�ҋ@
                    recvdata = tcpclient.recv();
                    if ( recvdata != null )
                    {
                        cdata = new char[recvdata.length()];
                        cdata = recvdata.toCharArray();

                        // �R�}���h�擾
                        data = new String( cdata, 32, 4 );
                        if ( data.compareTo( "0161" ) == 0 )
                        {
                            // ��������
                            data = new String( cdata, 51, 2 );
                            Result = Integer.valueOf( data ).intValue();

                            // �J�n���t
                            data = new String( cdata, 53, 8 );
                            ManualSalesDetailGetStartDate = Integer.valueOf( data ).intValue();

                            // �I�����t
                            data = new String( cdata, 61, 8 );
                            ManualSalesDetailGetEndDate = Integer.valueOf( data ).intValue();

                            // �ŋ��i���Łj
                            data = new String( cdata, 114, 9 );
                            ManualSalesDetailTaxIn = Integer.valueOf( data ).intValue();

                            // �����v
                            data = new String( cdata, 123, 9 );
                            ManualSalesDetailTotal = Integer.valueOf( data ).intValue();

                            data = new String( cdata, 132, 2 );
                            ManualSalesDetailCount = Integer.valueOf( data ).intValue();
                            for( i = 0 ; i < ManualSalesDetailCount ; i++ )
                            {
                                // ���ז���
                                data = new String( cdata, 134 + (i * 64), 40 );
                                ManualSalesDetailName[i] = new String( data.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // ���v
                                data = new String( cdata, 174 + (i * 64), 9 );
                                ManualSalesDetailAmount[i] = Integer.valueOf( data ).intValue();
                            }

                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0160:" + e.toString() );
                    return(false);
                }

                tcpclient.disconnectService();
                return(true);
            }
        }

        return(false);
    }

    /**
     * հ�ޏ��擾
     * 
     * @param hotelid ���ID
     * @param loginid ۸޲�ID
     * @param loginpasswd �߽ܰ��
     * @param useragent հ�ް���ު��
     * @param remoteip �Ӱ�IP���ڽ
     * @param loginkind ���O�C�����(1:PC,2:�g��)
     * @return ��������(1:����A2:հ�ޖ��Ȃ�,3:�߽ܰ�ވႢ,4:���̑�)
     */
    public int getUserDbInfo(String hotelid, String loginid, String loginpasswd, String useragent, String remoteip, int loginkind)
    {
//        System.out.println( "hotelid:"+hotelid+",loginid:"+loginid+",loginpasswd:"+loginpasswd);

        int ret;
        int userid;
        String query;
        String logdetail;
        ResultSet result;
        DbAccess db;
        DbAccess db_update;
        DateEdit date;

        ret = 1;
        logdetail = "���O�C������";
        userid = 0;

        // �p�����^�`�F�b�N
        if ( hotelid == null )
        {
            hotelid = "";
        }
        if ( loginid == null )
        {
            loginid = "";
        }
        if ( loginpasswd == null )
        {
            loginpasswd = "";
        }

        for( ; ; )
        {
            try
            {
                // �f�[�^�x�[�X�ւ̐ڑ�
                db = new DbAccess();

                if ( loginkind == 1 )
                {
                    query = "SELECT * FROM owner_user WHERE hotelid='" + hotelid + "' AND loginid='" + loginid + "'";
                }
                else
                {
                    query = "SELECT * FROM owner_user WHERE hotelid='" + hotelid + "' AND machineid='" + loginid + "'";
                }
                // SQL���̎��s
                result = db.execQuery( query );
                if ( result.next() != false )
                {
                    // ���[�UID�̎擾
                    userid = result.getInt( "userid" );
                    DbUserId = userid;

                    // �擾���ʂ̕]��
                    // �߽ܰ������
                    if ( loginkind == 1 )
                    {
                        if ( loginpasswd.compareTo( result.getString( "passwd_pc" ) ) != 0 )
                        {
                            ret = 3;
                            logdetail = "�p�X���[�h���Ⴂ�܂�";
                        }
                    }
                    else
                    {
                        if ( loginpasswd.compareTo( result.getString( "passwd_mobile" ) ) != 0 )
                        {
                            ret = 3;
                            logdetail = "�p�X���[�h���Ⴂ�܂�";
                        }
                    }
                    if ( ret != 3 )
                    { // ۸޲�ID�̎擾
                      // �߽ܰ�ނ̎擾
                        if ( loginkind == 1 )
                        {
                            DbLoginUser = result.getString( "loginid" );
                            DbPassword = result.getString( "passwd_pc" );
                        }
                        else
                        {
                            DbLoginUser = result.getString( "machineid" );
                            DbPassword = result.getString( "passwd_mobile" );
                        }
                        // հ�ް���̎擾
                        DbUserName = result.getString( "name" );
                        // ���[�U���x���̎擾
                        DbUserLevel = result.getInt( "level" );
                    }
                }
                else
                {
                    ret = 2;
                    logdetail = "���[�UID������܂���";
                }
                db.close();

            }
            catch ( Exception e )
            {
                ret = 4;
                logdetail = e.toString();
                log.error( "getUserDbInfo:" + e.toString() );
            }

            break;
        }

        // �z�e��ID�E���O�C��ID���Ȃ��ꍇ�̓G���[�Ƃ���
        if ( hotelid.compareTo( "" ) == 0 ||
                loginid.compareTo( "" ) == 0 ||
                loginpasswd.compareTo( "" ) == 0 )
        {
            ret = 2;
        }

        try
        {
            db_update = new DbAccess();
            date = new DateEdit();

            // ���O�C�������̏�������
            query = "INSERT INTO owner_user_log VALUES (";
            query = query + "'" + hotelid + "',";
            query = query + "0,";
            query = query + "'" + date.getDate( 0 ) + "',";
            query = query + "'" + date.getTime( 0 ) + "',";
            query = query + userid + ",";
            query = query + "'" + loginid + "',";
            query = query + "'" + loginpasswd + "',";
            query = query + ret + ",";
            query = query + "'" + logdetail + "',";
            query = query + "'" + useragent + "',";
            query = query + "'" + remoteip + "'";
            query = query + ")";

            // SQL�N�G���[�̎��s
            db_update.execUpdate( query );
            db_update.close();
        }
        catch ( Exception e )
        {
            log.error( "getUserDbInfo:" + e.toString() );
        }

        return(ret);
    }

    /**
     * �Z�L�����e�B���擾
     * 
     * @param db DbAccess�n���h��
     * @param hotelid ���ID
     * @param userid ���[�UID
     * @return ��������
     */
    public ResultSet getUserSecurity(DbAccess db, String hotelid, int userid)
    {
        String query;

        // �Z�L�����e�B���擾
        query = "SELECT * FROM owner_user_security WHERE hotelid='" + hotelid + "'";
        query = query + " AND userid=" + userid;

        return(db.execQuery( query ));
    }

    /**
     * ��ُ��̎擾
     * 
     * @param db DbAccess�n���h��
     * @param hotelid ���ID
     * @param userid ���[�UID
     * @return ��������
     */
    public ResultSet getHotelInfo(DbAccess db, String hotelid)
    {
        String query;

        // ��ُ��̎擾
        query = "SELECT * FROM hotel WHERE hotel_id='" + hotelid + "'";

        return(db.execQuery( query ));
    }

    /**
     * �Ǘ��\�z�e���̎擾
     * 
     * @param db DbAccess�n���h��
     * @param hotelid ���ID
     * @param userid ���[�UID
     * @return ��������
     */
    public ResultSet getManageHotel(DbAccess db, String hotelid, int userid)
    {
        String query;

        // �Ǘ��\�z�e���̎擾
        query = "SELECT hotel.*,owner_user_hotel.accept_hotelid FROM hotel,owner_user_hotel";
        query = query + " WHERE hotelid='" + hotelid + "'";
        query = query + " AND userid = " + userid;
        query = query + " AND owner_user_hotel.accept_hotelid = hotel.hotel_id";

        return(db.execQuery( query ));
    }

    /**
     * �ŏI���O�C�������̎擾
     * �i2���擾�F1����=���݃��O�C��,2����=�O�񃍃O�C���j
     * 
     * @param db DbAccess�n���h��
     * @param hotelid ���ID
     * @param userid ���[�UID
     * @return ��������
     */
    public ResultSet getLoginHistory(DbAccess db, String hotelid, int userid)
    {
        String query;

        // �ŏI���O�C�������̎擾�i2���擾�F1����=���݃��O�C��,2����=�O�񃍃O�C���j
        query = "SELECT * FROM owner_user_log";
        query = query + " WHERE hotelid = '" + hotelid + "'";
        query = query + " AND userid = " + userid;
        query = query + " AND log_level = 1";
        query = query + " ORDER BY logid DESC LIMIT 2";

        return(db.execQuery( query ));
    }

    /**
     * �z�e��ID�L�����m�F����
     * 
     * @param session_hotel �Z�b�V�����i�[�z�e��ID
     * @param hotelid ���ID
     * @param userid ���[�UID
     * @return ��������(1:�L��,2:����)
     */
    public int checkHotelId(String session_hotel, String hotelid, int userid)
    {
        int ret;
        int count;
        DbAccess dbmanage;
        ResultSet resultmanage;

        if ( session_hotel.compareTo( hotelid ) == 0 )
        {
            return(1);
        }

        ret = 2;
        count = 0;

        try
        {
            dbmanage = new DbAccess();
            resultmanage = this.getManageHotel( dbmanage, session_hotel, userid );
            if ( resultmanage != null )
            {
                while( resultmanage.next() != false )
                {
                    if ( resultmanage.getString( "hotel_id" ).compareTo( hotelid ) == 0 )
                    {
                        count++;
                        break;
                    }
                }
            }
            else
            {
            }

            dbmanage.close();
        }
        catch ( Exception e )
        {
        }

        if ( count > 0 )
        {
            ret = 1;
        }

        return(ret);
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
                        if ( result.getString( "front_ip" ).equals( "255.255.255.255" ) )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                        }
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
                    query = "SELECT * FROM hotel WHERE hotel_id='" + value + "'";
                    result = db.execQuery( query );
                    if ( result.next() != false )
                    {
                        if ( result.getString( "front_ip" ).equals( "255.255.255.255" ) )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = tcpclient.connectServiceByAddr( result.getString( "front_ip" ) );
                        }
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
                if ( value.equals( "255.255.255.255" ) )
                {
                    ret = true;
                }
                else
                {
                    ret = tcpclient.connectServiceByAddr( value );
                }
                break;

            // �Z�b�g���ꂽ�z�e��ID�Őڑ�
            default:

                ret = tcpclient.connectService( value );
                break;
        }

        return(ret);
    }

    /**
     * ���t�`�F�b�N
     * 
     * @param intDates
     * @return
     */
    private boolean dateCheck(int... intDates)
    {
        return dateCheck( this.YYYY_MM_DD, intDates );
    }

    /**
     * ���t�`�F�b�N
     * 
     * @param pattern
     * @param intDates
     * @return
     */
    private boolean dateCheck(String pattern, int... intDates)
    {
        boolean returnFlg = false;

        SimpleDateFormat sdf;

        String tmpStr;
        int tmpDate = 0;

        boolean yearMonthCheck = false;

        try
        {
            if ( intDates != null )
            {
                for( int intDate : intDates )
                {
                    if ( intDate > 0 )
                    {
                        tmpStr = Integer.toString( intDate );

                        // intDate�̌�����6��菬�����A�܂���8���傫���ꍇ�A�ُ���t�Ƃ݂Ȃ�
                        if ( tmpStr.length() < 6 || tmpStr.length() > 8 )
                        {
                            Logging.error( "[ownerInfo.dateCheck()] ���� �F �u" + tmpStr + "�v�ُ͈���t�ł��邽�߁A�d�����M�𒆒f���܂��B " );
                            return(false);
                        }

                        // intDate�̖�����00�ł��邩�ǂ����`�F�b�N
                        yearMonthCheck =
                                (tmpStr.length() == 8 && (intDate % 100 == 0)) // YYYYMM00�̏ꍇ
                                        || tmpStr.length() == 6; // YYYYMM�̏ꍇ

                        if ( yearMonthCheck )
                        {
                            pattern = this.YYYY_MM;
                        }

                        if ( this.YYYY_MM.equals( pattern ) ) // �N���`�F�b�N
                        {

                            intDate = Integer.parseInt( tmpStr.substring( 0, 4 ) + tmpStr.substring( 4, 6 ) );
                        }

                        tmpDate = intDate;
                        sdf = new SimpleDateFormat( pattern );
                        sdf.setLenient( false );
                        sdf.parse( Integer.toString( intDate ) );

                    }
                }
                returnFlg = true;
            }
        }
        catch ( ParseException e )
        {
            Logging.error( "[ownerInfo.dateCheck()] ���� �F �u" + tmpDate + "�v�ُ͈���t�ł��邽�߁A�d�����M�𒆒f���܂��B Exception = " + e.toString() );
        }

        return returnFlg;
    }

}
// OwnerInfo Class End

