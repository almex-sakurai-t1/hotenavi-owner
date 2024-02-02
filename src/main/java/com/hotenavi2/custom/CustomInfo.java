/*
 * @(#)CustomInfo.java 2.00 2004/03/31
 * Copyright (C) ALMEX Inc. 2004
 * �ڋq���֘A�ʐMAP�N���X
 */

package com.hotenavi2.custom;

import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DbAccess;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.StringFormat;
import com.hotenavi2.common.TcpClient;

/**
 * AMFWEB�T�[�r�X�Ƃ̌ڋq���֘A�d���ҏW�E����M���s���B
 * 
 * @author S.Shiiya
 * @version 2.00 2004/03/31
 */
public class CustomInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID           = -9152132824736895499L;

    // ------------------------------------------------------------------------------
    // �萔��`
    // ------------------------------------------------------------------------------
    /** �����ő吔 **/
    public static final int   CUSTOMINFO_ROOMMAX         = 128;
    /** �L�O���ő吔 **/
    public static final int   CUSTOMINFO_MEMORIALMAX     = 8;
    /** ���p�����ő吔 **/
    public static final int   CUSTOMINFO_USEHISTORYMAX   = 10;
    /** �|�C���g�����ő吔 **/
    public static final int   CUSTOMINFO_POINTHISTORYMAX = 10;
    /** �O�ド���L���O�ő吔 **/
    public static final int   CUSTOMINFO_RANKINGORDERMAX = 3;
    /** �����L���O�ő吔 **/
    public static final int   CUSTOMINFO_RANKINGMAX      = 5;
    /** �X�܍ő吔 **/
    public static final int   CUSTOMINFO_TENPOMAX        = 10;
    /** ���b�Z�[�W�ő吔 **/
    public static final int   CUSTOMINFO_MESSAGEMAX      = 5;
    /** �ʐM�̔����׍ő吔 **/
    public static final int   CUSTOMINFO_ORDERMAX        = 10;
    /** �C�x���g�������ő吔 **/
    public static final int   CUSTOMINFO_EVENTMAX        = 10;

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
    /** (����)�j�b�N�l�[�� **/
    public String             NickName;
    /** (����)���[�UID **/
    public String             UserId;
    /** (����)�p�X���[�h **/
    public String             Password;

    /** (�����o�[���)���O **/
    public String             InfoName;
    /** (�����o�[���)�t���K�i **/
    public String             InfoKana;
    /** (�����o�[���)����(1:�j��,2:����) **/
    public int                InfoSex;
    /** (�����o�[���)�Z���P **/
    public String             InfoAddress1;
    /** (�����o�[���)�Z���Q **/
    public String             InfoAddress2;
    /** (�����o�[���)�d�b�ԍ��P **/
    public String             InfoTel1;
    /** (�����o�[���)�d�b�ԍ��Q **/
    public String             InfoTel2;
    /** (�����o�[���)�����o�[�����N�R�[�h **/
    public int                InfoRankCode;
    /** (�����o�[���)�����o�[�����N���� **/
    public String             InfoRankName;
    /** (�����o�[���)���p�� **/
    public int                InfoUseCount;
    /** (�����o�[���)���p���z **/
    public int                InfoUseTotal;
    /** (�����o�[���)�|�C���g�P **/
    public int                InfoPoint;
    /** (�����o�[���)�|�C���g�Q **/
    public int                InfoPoint2;
    /** (�����o�[���)�ŏI���X�� **/
    public int                InfoLastDay;
    /** (�����o�[���)�a�����P(MMDD) **/
    public int                InfoBirthday1;
    /** (�����o�[���)�a�����Q(MMDD) **/
    public int                InfoBirthday2;
    /** (�����o�[���)�a�����P�L���t���O(0:�L��,1:����) **/
    public int                InfoBirthday1Flag;
    /** (�����o�[���)�a�����Q�L���t���O(0:�L��,1:����) **/
    public int                InfoBirthday2Flag;
    /** (�����o�[���)�L�O���P(MMDD) **/
    public int                InfoMemorial1;
    /** (�����o�[���)�L�O���Q(MMDD) **/
    public int                InfoMemorial2;
    /** (�����o�[���)�L�O���P�L���t���O(0:�L��,1:����) **/
    public int                InfoMemorialFlag1;
    /** (�����o�[���)�L�O���Q�L���t���O(0:�L��,1:����) **/
    public int                InfoMemorialFlag2;
    /** (�����o�[���)�{�������� **/
    public int                InfoDiscount;
    /** (�����o�[���)���[���A�h���X **/
    public String             InfoMailAddress;
    /** (�����o�[���)���[���}�K�W���z�M(1:�z�M����,2:�z�M���Ȃ�) **/
    public int                InfoMailMag;
    /** (�����o�[���)���[���z�M���(0:OK,1:�z�M�s�\) **/
    public int                InfoMailStatus;
    /** (�����o�[���)�Ԕԁi�n��j **/
    public String             InfoCarArea;
    /** (�����o�[���)�Ԕԁi��ʁj **/
    public String             InfoCarKana;
    /** (�����o�[���)�Ԕԁi�Ԏ�j **/
    public String             InfoCarType;
    /** (�����o�[���)�Ԕ� **/
    public String             InfoCarNo;
    /** (�����o�[���)�i�i�����L��(0:�Ȃ�,1:����) **/
    public int                InfoGiftChange;
    /** (�����o�[���)�ڋq���ی�敪(0:�ی�Ȃ�,1:�ی삠��,2:�\�����Ȃ�) **/
    public int                InfoProtect;
    /** (�����o�[���)�Ïؔԍ��ύX���敪(0:�ύX�֎~,1:�ύX����) **/
    public int                InfoSecretChange;
    /** (�����o�[���)�Ïؔԍ����� **/
    public int                InfoSecretDigit;
    /** (�����o�[���)�����p�� **/
    public int                InfoUseCountAll;
    /** (�����o�[���)�����p���z **/
    public int                InfoUseTotalAll;

    /** (���p����)�y�[�W�J�n���t **/
    public String             UsePageDate;
    /** (���p����)�y�[�W�J�n���� **/
    public String             UsePageTime;
    /** (���p����)�y�[�W�g���f�[�^ **/
    public String             UsePageData;
    /** (���p����)���y�[�W�J�n���t **/
    public String             UseNextDate;
    /** (���p����)���y�[�W�J�n���� **/
    public String             UseNextTime;
    /** (���p����)���y�[�W�g���f�[�^ **/
    public String             UseNextData;
    /** (���p����)�O�y�[�W�J�n���t **/
    public String             UsePrevDate;
    /** (���p����)�O�y�[�W�J�n���� **/
    public String             UsePrevTime;
    /** (���p����)�O�y�[�W�g���f�[�^ **/
    public String             UsePrevData;
    /** (���p����)�L�����p�� **/
    public int                UseCount;
    /** (���p����)���t(MAX:10) **/
    public int                UseDate[];
    /** (���p����)��������(MAX:10) **/
    public String             UseRoom[];
    /** (���p����)���p���z(MAX:10) **/
    public int                UseTotal[];
    /** (���p����)�l���|�C���g(MAX:10) **/
    public int                UsePoint[];
    /** (���p����)�X�܃R�[�h(MAX:10) **/
    public int                UseStoreCode[];
    /** (���p����)�X�ܖ���(MAX:10) **/
    public String             UseStoreName[];

    /** (�|�C���g����)�y�[�W�J�n���t **/
    public String             PointPageDate;
    /** (�|�C���g����)�y�[�W�J�n���� **/
    public String             PointPageTime;
    /** (�|�C���g����)���y�[�W�J�n���t **/
    public String             PointNextDate;
    /** (�|�C���g����)���y�[�W�J�n���� **/
    public String             PointNextTime;
    /** (�|�C���g����)�O�y�[�W�J�n���t **/
    public String             PointPrevDate;
    /** (�|�C���g����)�O�y�[�W�J�n���� **/
    public String             PointPrevTime;
    /** (�|�C���g����)�|�C���g���(1:�|�C���g�P,2:�|�C���g�Q) **/
    public int                PointKind;
    /** (�|�C���g����)�L���|�C���g�� **/
    public int                Point;
    /** (�|�C���g����)���t(MAX:10) **/
    public int                PointDate[];
    /** (�|�C���g����)�|�C���g(MAX:10) **/
    public int                PointCount[];
    /** (�|�C���g����)�|�C���g����(MAX:10) **/
    public String             PointName[];
    /** (�|�C���g����)�L������(YYYYMMDD)(MAX:10) **/
    public int                PointLimit[];
    /** (�|�C���g����)�X�܃R�[�h(MAX:10) **/
    public int                PointStoreCode[];
    /** (�|�C���g����)�X�ܖ���(MAX:10) **/
    public String             PointStoreName[];

    /** (���[���}�K�W��)���[���A�h���X **/
    public String             MailmagAddress;
    /** (���[���}�K�W��)�������(1:�o�^,2:����) **/
    public int                MailmagKind;

    /** (���[���}�K�W��)���[���A�h���X **/
    public String             MailReplaceAddress;
    /** (���[���}�K�W���폜)���[���A�h���X **/
    public String             MailDeleteAddress;

    /** (�S���B��)������ **/
    public int                AllRoomCount;
    /** (�S���B��)�����R�[�h(MAX:128) **/
    public int                AllRoomCode[];
    /** (�S���B��)��������(MAX:128) **/
    public String             AllRoomName[];
    /** (�S���B��)�������p��(MAX:128) **/
    public int                AllRoomUse[];
    /** (�S���B��)�ŏI���p��(YYYYMMDD)(MAX:128) **/
    public int                AllRoomLast[];
    /** (�S���B��)�\���ʒuX(MAX:128) **/
    public int                AllRoomDispX[];
    /** (�S���B��)�\���ʒuY(MAX:128) **/
    public int                AllRoomDispY[];
    /** (�S���B��)�\���ʒuZ(MAX:128) **/
    public int                AllRoomDispZ[];

    /** (�I�[�i�[�Y���[��)������ **/
    public int                OwnerRoomCount;
    /** (�I�[�i�[�Y���[��)�����R�[�h(MAX:128) **/
    public int                OwnerRoomCode[];
    /** (�I�[�i�[�Y���[��)��������(MAX:128) **/
    public String             OwnerRoomName[];
    /** (�I�[�i�[�Y���[��)�c���(MAX:128) **/
    public int                OwnerRoomUse[];
    /** (�I�[�i�[�Y���[��)�L������(YYYYMMDD ���B����:0)(MAX:128) **/
    public int                OwnerRoomLimit[];
    /** (�I�[�i�[�Y���[��)�\���ʒuX(MAX:128) **/
    public int                OwnerRoomDispX[];
    /** (�I�[�i�[�Y���[��)�\���ʒuX(MAX:128) **/
    public int                OwnerRoomDispY[];
    /** (�I�[�i�[�Y���[��)�\���ʒuX(MAX:128) **/
    public int                OwnerRoomDispZ[];

    /** (�����L���O)�擾�敪(1:����,2:�O��) **/
    public int                RankingKind;
    /** (�����L���O)�擾�J�n�����L���O **/
    public int                RankingGetStart;
    /** (�����L���O)�ŏI�X�V���t(YYYYMMDD) **/
    public int                RankingUpdate;
    /** (�����L���O)�ŏI�X�V����(HHMM) **/
    public int                RankingUptime;
    /** (�����L���O)�J�Ê��ԊJ�n(YYYYMMDD) **/
    public int                RankingStart;
    /** (�����L���O)�J�Ê��ԏI��(YYYYMMDD) **/
    public int                RankingEnd;
    /** (�����L���O)���y�[�W�擾�J�n�����L���O **/
    public int                RankingGetNext;
    /** (�����L���O)�O�ド���L���O(����)(�z��0:����,1:��O,2:���) **/
    public int                RankingOrderNo[];
    /** (�����L���O)�O�ド���L���O(�|�C���g)(�z��0:����,1:��O,2:���) **/
    public int                RankingOrderPoint[];
    /** (�����L���O)�O�ド���L���O(���p��)(�z��0:����,1:��O,2:���) **/
    public int                RankingOrderCount[];
    /** (�����L���O)�O�ド���L���O(���p���z)(�z��0:����,1:��O,2:���) **/
    public int                RankingOrderTotal[];
    /** (�����L���O)�����L���O(����)(5���Œ�) **/
    public int                RankingNo[];
    /** (�����L���O)�����L���O(�|�C���g)(5���Œ�) **/
    public int                RankingPoint[];
    /** (�����L���O)�����L���O(���p��)(5���Œ�) **/
    public int                RankingCount[];
    /** (�����L���O)�����L���O(���p���z)(5���Œ�) **/
    public int                RankingTotal[];

    /** (�S�X�S���B��)�X�ܐ� **/
    public int                AllAllTenpoCount;
    /** (�S�X�S���B��)�X�ܔԍ�(MAX:10) **/
    public int                AllAllTenpoNo[];
    /** (�S�X�S���B��)�X�ܖ���(MAX:10) **/
    public String             AllAllTenpoName[];
    /** (�S�X�S���B��)�X�ܖ���(MAX:10) **/
    public int                AllAllRoomCount[];
    /** (�S�X�S���B��)�����R�[�h(MAX:10)(MAX:128) **/
    public int                AllAllRoomCode[][];
    /** (�S�X�S���B��)��������(MAX:10)(MAX:128) **/
    public String             AllAllRoomName[][];
    /** (�S�X�S���B��)���p��(MAX:10)(MAX:128) **/
    public int                AllAllRoomUse[][];
    /** (�S�X�S���B��)�ŏI���p��(MAX:10)(MAX:128) **/
    public int                AllAllRoomLast[][];

    /** (���b�Z�[�W)���ʃ��b�Z�[�W(5���Œ�) **/
    public String             MessageCommon[];
    /** (���b�Z�[�W)�����o�[���b�Z�[�W(5���Œ�) **/
    public String             MessageAllMember[];
    /** (���b�Z�[�W)�����o�[�ʃ��b�Z�[�W(5���Œ�) **/
    public String             MessageMember[];

    /** (�ʐM�̔�)��� **/
    public int                OrderCount;
    /** (�ʐM�̔�)��t���t(YYYYMMDD) **/
    public int                OrderReceiptDate[];
    /** (�ʐM�̔�)��t����(HHMM) **/
    public int                OrderReceiptTime[];
    /** (�ʐM�̔�)���ח\����t(YYYYMMDD) **/
    public int                OrderArrivePlanDate[];
    /** (�ʐM�̔�)���ח\�莞��(HHMM) **/
    public int                OrderArrivePlanTime[];
    /** (�ʐM�̔�)���ד��t(YYYYMMDD) **/
    public int                OrderArriveDate[];
    /** (�ʐM�̔�)���׎���(HHMM) **/
    public int                OrderArriveTime[];
    /** (�ʐM�̔�)��n���t(YYYYMMDD) **/
    public int                OrderDeliveryDate[];
    /** (�ʐM�̔�)��n����(HHMM) **/
    public int                OrderDeliveryTime[];
    /** (�ʐM�̔�)��ԋ敪(1:��t,2:���ב�,3:���׍�,4:��n��,5:��n��,6:��t���,7:���׎��) **/
    public int                OrderOrderMode[];
    /** (�ʐM�̔�)��n�敪(1:����,2:����,3:�ގ���,4:���񗈓X��) **/
    public int                OrderDeliveryMode[];
    /** (�ʐM�̔�)���i�� **/
    public String             OrderGoodsName[];
    /** (�ʐM�̔�)���� **/
    public int                OrderGoodsCount[];

    /** (�Ïؔԍ�)�Ïؔԍ� **/
    public String             SecretCode;
    /** (�Ïؔԍ�)�Ïؔԍ��擾���� **/
    public int                SecretResult;
    /** (�Ïؔԍ�)���Ïؔԍ� **/
    public String             SecretOldCode;
    /** (�Ïؔԍ�)�ڋq���ی�敪 **/
    public int                SecretType;

    /** (���[�UID�ύX)�ύX�テ�[�UID **/
    public String             ChangeUserId;
    /** (���[�UID�ύX)�ύX��p�X���[�h **/
    public String             ChangePassword;
    /** (���[�UID�ύX)���[�UID�ύX���� **/
    public int                ChangeResult;

    /** (���b�Z�[�W)���ʃ��b�Z�[�W�P **/
    public String             MessageCommon1;
    /** (���b�Z�[�W)���ʃ��b�Z�[�W�Q **/
    public String             MessageCommon2;
    /** (���b�Z�[�W)���ʃ��b�Z�[�W�R **/
    public String             MessageCommon3;
    /** (���b�Z�[�W)���ʃ��b�Z�[�W�S **/
    public String             MessageCommon4;
    /** (���b�Z�[�W)���ʃ��b�Z�[�W�T **/
    public String             MessageCommon5;
    /** (���b�Z�[�W)�����o�[���b�Z�[�W�P **/
    public String             MessageMember1;
    /** (���b�Z�[�W)�����o�[���b�Z�[�W�Q **/
    public String             MessageMember2;
    /** (���b�Z�[�W)�����o�[���b�Z�[�W�R **/
    public String             MessageMember3;
    /** (���b�Z�[�W)�����o�[���b�Z�[�W�S **/
    public String             MessageMember4;
    /** (���b�Z�[�W)�����o�[���b�Z�[�W�T **/
    public String             MessageMember5;
    /** (���b�Z�[�W)�ʃ��b�Z�[�W�P **/
    public String             MessageOne1;
    /** (���b�Z�[�W)�ʃ��b�Z�[�W�Q **/
    public String             MessageOne2;
    /** (���b�Z�[�W)�ʃ��b�Z�[�W�R **/
    public String             MessageOne3;
    /** (���b�Z�[�W)�ʃ��b�Z�[�W�S **/
    public String             MessageOne4;
    /** (���b�Z�[�W)�ʃ��b�Z�[�W�T **/
    public String             MessageOne5;

    /** (�����r���S)������ **/
    public int                BingoRoomCount;
    /** (�����r���S)�����R�[�h(MAX:128) **/
    public int                BingoRoomCode[];
    /** (�����r���S)��������(MAX:128) **/
    public String             BingoRoomName[];
    /** (�����r���S)�������p��(MAX:128) **/
    public int                BingoRoomUse[];
    /** (�����r���S)�ŏI���p��(YYYYMMDD)(MAX:128) **/
    public int                BingoRoomLast[];
    /** (�����r���S)�\���ʒuX(MAX:128) **/
    public int                BingoRoomDispX[];
    /** (�����r���S)�\���ʒuY(MAX:128) **/
    public int                BingoRoomDispY[];
    /** (�����r���S)�\���ʒuZ(MAX:128) **/
    public int                BingoRoomDispZ[];
    /** (�����r���S)�B���敪(MAX:128) **/
    public int                BingoRoomFlag[];

    /** (�I�[�i�[�Y�Z���N�V����)������ **/
    public int                SelectionRoomCount;
    /** (�I�[�i�[�Y�Z���N�V����)�����R�[�h(MAX:128) **/
    public int                SelectionRoomCode[];
    /** (�I�[�i�[�Y�Z���N�V����)��������(MAX:128) **/
    public String             SelectionRoomName[];

    /** (�C�x���g���)�C�x���g�敪 **/
    public String             EventKind;
    /** (�C�x���g���)�\���I���敪 **/
    public int                EventDispKind;
    /** (�C�x���g���)���b�Z�[�W�\���敪 **/
    public int                EventMessageKind[];
    /** (�C�x���g���)�\�����b�Z�[�W **/
    public String             EventMessage[];
    /** (�C�x���g���)�����C�x���g���� **/
    public String             EventRoomEventName;
    /** (�C�x���g���)������ **/
    public int                EventRoomCount;
    /** (�C�x���g���)�����R�[�h **/
    public int                EventRoomCode[];
    /** (�C�x���g���)�������� **/
    public String             EventRoomName[];

    /** (�������i�i�ꗗ)�������i�i�� **/
    public int                GiftCount;
    /** (�������i�i�ꗗ)�������t **/
    public int                GiftDate[];
    /** (�������i�i�ꗗ)�������� **/
    public int                GiftTime[];
    /** (�������i�i�ꗗ)�L������ **/
    public int                GiftExpireDate[];
    /** (�������i�i�ꗗ)�i�i���� **/
    public String             GiftName[];

    /** (QR�����o�[)�a�����i�N�j **/
    public String             BirthdayYear;
    /** (QR�����o�[)�L�O���i�N�j **/
    public String             AnniversaryYear;
    /** (�����o�[���ύX�^QR�����o�[)�L�O���i���j **/
    public String             Anniversary1;
    /** (�����o�[���ύX�^QR�����o�[)�L�O���i���j **/
    public String             Anniversary2;
    /** (�����o�[���ύX�^QR�����o�[)�L�O���Q�i�N�j **/
    public String             Anniversary2Year;
    /** (�����o�[���ύX�^QR�����o�[)�L�O���Q�i���j **/
    public String             Anniversary2_1;
    /** (�����o�[���ύX�^QR�����o�[)�L�O���Q�i���j **/
    public String             Anniversary2_2;
    /** (�����o�[���ύX�^QR�����o�[)�a�����Q�i�N�j **/
    public String             Birthday2Year;
    /** (�����o�[���ύX�^QR�����o�[)�a�����Q�i���j **/
    public String             Birthday2_1;
    /** (�����o�[���ύX�^QR�����o�[)�a�����Q�i���j **/
    public String             Birthday2_2;

    // �ȉ��^�b�`�[���Ŏg�p����f�[�^
    /** (�����o�[��t���)�����R�[�h **/
    public int                TouchRoomNo;
    /** (�����o�[��t���)�a����(�N�j **/
    public int                TouchBirthYear1;
    /** (�����o�[��t���)�a����(���j **/
    public int                TouchBirthMonth1;
    /** (�����o�[��t���)�a����(���j **/
    public int                TouchBirthDate1;
    /** (�����o�[��t���)���O **/
    public String             TouchName;
    /** (�����o�[��t���)���O **/
    public String             TouchNameKana;
    /** (�����o�[��t���)���� **/
    public int                TouchSex;
    /** (�����o�[��t���)�Z��1 **/
    public String             TouchAddr1;
    /** (�����o�[��t���)�Z��2 **/
    public String             TouchAddr2;
    /** (�����o�[��t���)�d�b1 **/
    public String             TouchTel1;
    /** (�����o�[��t���)�d�b2 **/
    public String             TouchTel2;
    /** (�����o�[��t���)�����A�h **/
    public String             TouchMailAddr;
    /** (�����o�[��t���)�����}�K **/
    public int                TouchMailMag;
    /** (�����o�[��t���)�L�O�N1(�N�j **/
    public int                TouchMemorialYear1;
    /** (�����o�[��t���)�L�O��1(���j **/
    public int                TouchMemorialMonth1;
    /** (�����o�[��t���)�L�O��1(���j **/
    public int                TouchMemorialDate1;
    /** (�����o�[��t���)�a����2(�N�j **/
    public int                TouchBirthYear2;
    /** (�����o�[��t���)�a����2(���j **/
    public int                TouchBirthMonth2;
    /** (�����o�[��t���)�a����2(���j **/
    public int                TouchBirthDate2;
    /** (�����o�[��t���)�L�O�N2 **/
    public int                TouchMemorialYear2;
    /** (�����o�[��t���)�L�O��2 **/
    public int                TouchMemorialMonth2;
    /** (�����o�[��t���)�L�O��2 **/
    public int                TouchMemorialDate2;
    /** (�����o�[��t���)�Z�L�����e�B�R�[�h **/
    public String             TouchSecurityCode;
    /** (�����o�[��t���)�n�s�z�e�`�F�b�N�C���R�[�h **/
    public int                TouchSeq;
    /** (�����o�[��t���)�n�s�z�e�`�F�b�N�C���R�[�h **/
    public String             TouchRoomName;
    /** (�����o�[��t���)�n�s�z�e�`�F�b�N�C���R�[�h **/
    public int                StartDate;
    /** (�����o�[��t���)�n�s�z�e�`�F�b�N�C���R�[�h **/
    public int                EndDate;
    /** (�����o�[��t���)�^�b�`�f�[�^�� **/
    public int                TouchData;
    /** (�����o�[��t���)�W�v�� **/
    public int[]              CollectDate;
    /** (�����o�[��t���)�S�`�F�b�N�C���� **/
    public int[]              AllCheckIn;
    /** (�����o�[��t���)�n�s�z�e�����o�[�`�F�b�N�C���� **/
    public int[]              MemberCheckIn;
    /** (�����o�[��t���)�S�����o�[�� **/
    public int[]              AllMember;
    /** �i�����o�[�J�[�h�ۋ�) ���i�R�[�h **/
    public int                GoodsCode;
    /** �i�����o�[�J�[�h�ۋ� ���i���z **/
    public int                GoodsPrice;
    /** �i�����o�[�`�F�b�N�C�����їv�� **/
    public String             TermId;

    /** �f�o�b�O���O **/
    public LogLib             log;

    /**
     * �ڋq���f�[�^�̏��������s���܂��B
     * 
     */
    public CustomInfo()
    {
        Result = 0;
        HotelId = "";
        CustomId = "";
        Birthday1 = "";
        Birthday2 = "";
        NickName = "";
        UserId = "";
        Password = "";

        InfoName = "";
        InfoKana = "";
        InfoSex = 0;
        InfoAddress1 = "";
        InfoAddress2 = "";
        InfoTel1 = "";
        InfoTel2 = "";
        InfoRankCode = 0;
        InfoRankName = "";
        InfoUseCount = 0;
        InfoUseTotal = 0;
        InfoPoint = 0;
        InfoPoint2 = 0;
        InfoLastDay = 0;
        InfoBirthday1 = 0;
        InfoBirthday2 = 0;
        InfoBirthday1Flag = 0;
        InfoBirthday2Flag = 0;
        InfoMemorial1 = 0;
        InfoMemorial2 = 0;
        InfoMemorialFlag1 = 0;
        InfoMemorialFlag2 = 0;
        InfoDiscount = 0;
        InfoMailAddress = "";
        InfoMailMag = 0;
        InfoMailStatus = 0;
        InfoCarArea = "";
        InfoCarKana = "";
        InfoCarType = "";
        InfoCarNo = "";
        InfoGiftChange = 0;
        InfoProtect = 0;
        InfoSecretChange = 0;
        InfoSecretDigit = 0;
        InfoUseCountAll = 0;
        InfoUseTotalAll = 0;

        UsePageDate = "";
        UsePageTime = "";
        UsePageData = "";
        UseNextDate = "";
        UseNextTime = "";
        UseNextData = "";
        UsePrevDate = "";
        UsePrevTime = "";
        UsePrevData = "";
        UseCount = 0;
        UseDate = new int[CUSTOMINFO_USEHISTORYMAX];
        UseRoom = new String[CUSTOMINFO_USEHISTORYMAX];
        UseTotal = new int[CUSTOMINFO_USEHISTORYMAX];
        UsePoint = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreCode = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreName = new String[CUSTOMINFO_USEHISTORYMAX];

        PointPageDate = "";
        PointPageTime = "";
        PointNextDate = "";
        PointNextTime = "";
        PointPrevDate = "";
        PointPrevTime = "";
        PointKind = 0;
        Point = 0;
        PointDate = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointCount = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointName = new String[CUSTOMINFO_POINTHISTORYMAX];
        PointLimit = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreCode = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreName = new String[CUSTOMINFO_POINTHISTORYMAX];

        MailmagAddress = "";
        MailmagKind = 0;

        MailReplaceAddress = "";
        MailDeleteAddress = "";

        AllRoomCount = 0;
        AllRoomCode = new int[CUSTOMINFO_ROOMMAX];
        AllRoomName = new String[CUSTOMINFO_ROOMMAX];
        AllRoomUse = new int[CUSTOMINFO_ROOMMAX];
        AllRoomLast = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        OwnerRoomCount = 0;
        OwnerRoomCode = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomName = new String[CUSTOMINFO_ROOMMAX];
        OwnerRoomUse = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomLimit = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        RankingKind = 0;
        RankingGetStart = 0;
        RankingUpdate = 0;
        RankingUptime = 0;
        RankingStart = 0;
        RankingEnd = 0;
        RankingGetNext = 0;
        RankingOrderNo = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderPoint = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderCount = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderTotal = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingNo = new int[CUSTOMINFO_RANKINGMAX];
        RankingPoint = new int[CUSTOMINFO_RANKINGMAX];
        RankingCount = new int[CUSTOMINFO_RANKINGMAX];
        RankingTotal = new int[CUSTOMINFO_RANKINGMAX];

        AllAllTenpoCount = 0;
        AllAllTenpoNo = new int[CUSTOMINFO_TENPOMAX];
        AllAllTenpoName = new String[CUSTOMINFO_TENPOMAX];
        AllAllRoomCount = new int[CUSTOMINFO_TENPOMAX];
        AllAllRoomCode = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomName = new String[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomUse = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomLast = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];

        MessageCommon = new String[CUSTOMINFO_MESSAGEMAX];
        MessageAllMember = new String[CUSTOMINFO_MESSAGEMAX];
        MessageMember = new String[CUSTOMINFO_MESSAGEMAX];

        OrderCount = 0;
        OrderReceiptDate = new int[CUSTOMINFO_ORDERMAX];
        OrderReceiptTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveTime = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryDate = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryTime = new int[CUSTOMINFO_ORDERMAX];
        OrderOrderMode = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryMode = new int[CUSTOMINFO_ORDERMAX];
        OrderGoodsName = new String[CUSTOMINFO_ORDERMAX];
        OrderGoodsCount = new int[CUSTOMINFO_ORDERMAX];

        SecretCode = "";
        SecretResult = 0;
        SecretOldCode = "";
        SecretType = 0;

        ChangeUserId = "";
        ChangePassword = "";
        ChangeResult = 0;

        MessageCommon1 = "";
        MessageCommon2 = "";
        MessageCommon3 = "";
        MessageCommon4 = "";
        MessageCommon5 = "";
        MessageMember1 = "";
        MessageMember2 = "";
        MessageMember3 = "";
        MessageMember4 = "";
        MessageMember5 = "";
        MessageOne1 = "";
        MessageOne2 = "";
        MessageOne3 = "";
        MessageOne4 = "";
        MessageOne5 = "";

        BingoRoomCount = 0;
        BingoRoomCode = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomName = new String[CUSTOMINFO_ROOMMAX];
        BingoRoomUse = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomLast = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispZ = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomFlag = new int[CUSTOMINFO_ROOMMAX];

        SelectionRoomCount = 0;
        SelectionRoomCode = new int[CUSTOMINFO_ROOMMAX];
        SelectionRoomName = new String[CUSTOMINFO_ROOMMAX];

        EventKind = "00";
        EventDispKind = 0;
        EventMessageKind = new int[CUSTOMINFO_EVENTMAX];
        EventMessage = new String[CUSTOMINFO_EVENTMAX];
        EventRoomEventName = "";
        EventRoomCount = 0;
        EventRoomCode = new int[CUSTOMINFO_ROOMMAX];
        EventRoomName = new String[CUSTOMINFO_ROOMMAX];

        GiftCount = 0;
        GiftDate = new int[0];
        GiftTime = new int[0];
        GiftExpireDate = new int[0];
        GiftName = new String[0];

        BirthdayYear = "";
        AnniversaryYear = "0000";
        Anniversary1 = "00";
        Anniversary2 = "00";
        Anniversary2Year = "0000";
        Anniversary2_1 = "00";
        Anniversary2_2 = "00";
        Birthday2Year = "0000";
        Birthday2_1 = "00";
        Birthday2_2 = "00";

        // �ȉ��^�b�`�[���Ŏg�p����f�[�^
        TouchRoomNo = 0;
        TouchBirthYear1 = 0;
        TouchBirthMonth1 = 0;
        TouchBirthDate1 = 0;
        TouchName = "";
        TouchNameKana = "";
        TouchSex = 0;
        TouchAddr1 = "";
        TouchAddr2 = "";
        TouchTel1 = "";
        TouchTel2 = "";
        TouchMailAddr = "";
        TouchMailMag = 0;
        TouchMemorialYear1 = 0;
        TouchMemorialMonth1 = 0;
        TouchMemorialDate1 = 0;
        TouchBirthYear2 = 0;
        TouchBirthMonth2 = 0;
        TouchBirthDate2 = 0;
        TouchMemorialYear2 = 0;
        TouchMemorialMonth2 = 0;
        TouchMemorialDate2 = 0;
        TouchSecurityCode = "";
        TouchSeq = 0;
        TouchRoomName = "";
        StartDate = 0;
        EndDate = 0;
        TouchData = 0;

        log = new LogLib();
        TermId = "ceritfiedid";
    }

    // ------------------------------------------------------------------------------
    //
    // �d������
    //
    // ------------------------------------------------------------------------------
    /**
     * �d�����M����(1000)
     * �����o�[���O�C��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1000()
    {
        return(sendPacket1000Sub( 0, "" ));
    }

    /**
     * �d�����M����(1000)
     * �����o�[���O�C��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1000(int kind, String value)
    {
        return(sendPacket1000Sub( kind, value ));
    }

    /**
     * �d�����M����(1000)
     * �����o�[���O�C��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1000Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        InfoRankCode = 0;
        InfoRankName = "";
        InfoProtect = 0;
        InfoSecretChange = 0;
        InfoSecretDigit = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1000";

                // �����o�[ID
                if ( CustomId.equals( "FFFFFF" ) )
                {
                    CustomId = "    ";
                    strSend = strSend + format.leftFitFormat( CustomId, 9 );
                    CustomId = "FFFFFF";
                }
                // else if ( !UserId.equals( "    " ) && UserId.length() >= 6 )
                // {
                // strSend = strSend + format.leftFitFormat( "    ", 9 );
                // }
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1001" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ���O�C�����̂ݖ߂�l���擾����
                            strData = new String( cRecv, 36, 9 );
                            CustomId = strData.trim();
                            strData = new String( cRecv, 45, 2 );
                            Birthday1 = strData.trim();
                            strData = new String( cRecv, 47, 2 );
                            Birthday2 = strData.trim();
                            strData = new String( cRecv, 49, 32 );
                            UserId = strData.trim();
                            strData = new String( cRecv, 81, 8 );
                            Password = strData.trim();

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �i�i�����L��
                            strData = new String( cRecv, 111, 2 );
                            InfoGiftChange = Integer.valueOf( strData ).intValue();

                            // �����o�[�����N�R�[�h
                            strData = new String( cRecv, 113, 3 );
                            InfoRankCode = Integer.valueOf( strData ).intValue();

                            // �����o�[�����N����
                            strData = new String( cRecv, 116, 40 );
                            InfoRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ڋq���ی�敪
                            strData = new String( cRecv, 156, 2 );
                            InfoProtect = Integer.valueOf( strData ).intValue();

                            // �Ïؔԍ��ύX���敪
                            strData = new String( cRecv, 158, 2 );
                            InfoSecretChange = Integer.valueOf( strData ).intValue();

                            // �Ïؔԍ�����
                            strData = new String( cRecv, 160, 2 );
                            InfoSecretDigit = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1000:" + e.toString() );
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
     * �d�����M����(1002)
     * �����o�[���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1002()
    {
        return(sendPacket1002Sub( 0, "" ));
    }

    /**
     * �d�����M����(1002)
     * �����o�[���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1002(int kind, String value)
    {
        return(sendPacket1002Sub( kind, value ));
    }

    /**
     * �d�����M����(1002)
     * �����o�[���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1002Sub(int kind, String value)
    {
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        InfoName = "";
        InfoKana = "";
        InfoSex = 0;
        InfoAddress1 = "";
        InfoAddress2 = "";
        InfoTel1 = "";
        InfoTel2 = "";
        InfoRankName = "";
        InfoUseCount = 0;
        InfoUseTotal = 0;
        InfoPoint = 0;
        InfoPoint2 = 0;
        InfoLastDay = 0;
        InfoBirthday1 = 0;
        InfoBirthday2 = 0;
        InfoBirthday1Flag = 0;
        InfoBirthday2Flag = 0;
        InfoMemorial1 = 0;
        InfoMemorial2 = 0;
        InfoMemorialFlag1 = 0;
        InfoMemorialFlag2 = 0;
        InfoDiscount = 0;
        InfoMailAddress = "";
        InfoMailMag = 0;
        InfoMailStatus = 0;
        InfoCarArea = "";
        InfoCarKana = "";
        InfoCarType = "";
        InfoCarNo = "";
        InfoGiftChange = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1002";

                // �����o�[ID
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        // �R�}���h�擾
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1003" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ����
                            strData = new String( cRecv, 111, 2 );
                            InfoSex = Integer.valueOf( strData ).intValue();

                            // �����o�[�����N���擾
                            strData = new String( cRecv, 113, 40 );
                            InfoRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���p��
                            strData = new String( cRecv, 153, 9 );
                            InfoUseCount = Integer.valueOf( strData ).intValue();

                            // �|�C���g
                            strData = new String( cRecv, 162, 9 );
                            InfoPoint = Integer.valueOf( strData ).intValue();

                            // �|�C���g�Q
                            strData = new String( cRecv, 171, 9 );
                            InfoPoint2 = Integer.valueOf( strData ).intValue();

                            // �a�����P
                            strData = new String( cRecv, 180, 4 );
                            InfoBirthday1 = Integer.valueOf( strData ).intValue();

                            // �a�����Q
                            strData = new String( cRecv, 184, 4 );
                            InfoBirthday2 = Integer.valueOf( strData ).intValue();

                            // �a�����P�L���t���O
                            strData = new String( cRecv, 188, 2 );
                            InfoBirthday1Flag = Integer.valueOf( strData ).intValue();

                            // �a�����Q�L���t���O
                            strData = new String( cRecv, 190, 2 );
                            InfoBirthday2Flag = Integer.valueOf( strData ).intValue();

                            // �L�O���P
                            strData = new String( cRecv, 192, 4 );
                            InfoMemorial1 = Integer.valueOf( strData ).intValue();

                            // �L�O���Q
                            strData = new String( cRecv, 196, 4 );
                            InfoMemorial2 = Integer.valueOf( strData ).intValue();

                            // �L�O���P�L���t���O
                            strData = new String( cRecv, 200, 2 );
                            InfoMemorialFlag1 = Integer.valueOf( strData ).intValue();

                            // �L�O���Q�L���t���O
                            strData = new String( cRecv, 202, 2 );
                            InfoMemorialFlag2 = Integer.valueOf( strData ).intValue();

                            // �{��������
                            strData = new String( cRecv, 204, 3 );
                            InfoDiscount = Integer.valueOf( strData ).intValue();

                            // ���[���A�h���X
                            strData = new String( cRecv, 207, 63 );
                            InfoMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���}�K�W���z�M
                            strData = new String( cRecv, 270, 2 );
                            InfoMailMag = Integer.valueOf( strData ).intValue();

                            // �Ԕԁi�n��j
                            strData = new String( cRecv, 272, 8 );
                            InfoCarArea = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Ԕԁi��ʁj
                            strData = new String( cRecv, 280, 2 );
                            InfoCarKana = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Ԕԁi�Ԏ�j
                            strData = new String( cRecv, 282, 3 );
                            InfoCarType = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Ԕ�
                            strData = new String( cRecv, 285, 4 );
                            InfoCarNo = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���z�M���
                            strData = new String( cRecv, 289, 2 );
                            InfoMailStatus = Integer.valueOf( strData ).intValue();

                            // ���O
                            strData = new String( cRecv, 291, 40 );
                            InfoName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �t���K�i
                            strData = new String( cRecv, 331, 20 );
                            InfoKana = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Z���P
                            strData = new String( cRecv, 351, 40 );
                            InfoAddress1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Z���Q
                            strData = new String( cRecv, 391, 40 );
                            InfoAddress2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �d�b�ԍ��P
                            strData = new String( cRecv, 431, 15 );
                            InfoTel1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �d�b�ԍ��Q
                            strData = new String( cRecv, 446, 15 );
                            InfoTel2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���p���z
                            strData = new String( cRecv, 461, 9 );
                            InfoUseTotal = Integer.valueOf( strData ).intValue();

                            // �ŏI���X��
                            strData = new String( cRecv, 470, 8 );
                            InfoLastDay = Integer.valueOf( strData ).intValue();

                            try
                            {
                                // �����p��
                                strData = new String( cRecv, 478, 9 );
                                InfoUseCountAll = Integer.valueOf( strData ).intValue();
                            }
                            catch ( Exception e )
                            {
                                InfoUseCountAll = 0;
                            }

                            try
                            {
                                // �����p���z
                                strData = new String( cRecv, 487, 9 );
                                InfoUseTotalAll = Integer.valueOf( strData ).intValue();
                            }
                            catch ( Exception e )
                            {
                                InfoUseTotalAll = 0;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1002:" + e.toString() );
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
     * �d�����M����(1004)
     * ���p�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1004()
    {
        return(sendPacket1004Sub( 0, "" ));
    }

    /**
     * �d�����M����(1004)
     * ���p�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1004(int kind, String value)
    {
        return(sendPacket1004Sub( kind, value ));
    }

    /**
     * �d�����M����(1004)
     * ���p�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1004Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        UseNextDate = "";
        UseNextTime = "";
        UseNextData = "";
        UsePrevDate = "";
        UsePrevTime = "";
        UsePrevData = "";
        UseCount = 0;
        UseDate = new int[CUSTOMINFO_USEHISTORYMAX];
        UseRoom = new String[CUSTOMINFO_USEHISTORYMAX];
        UseTotal = new int[CUSTOMINFO_USEHISTORYMAX];
        UsePoint = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreCode = new int[CUSTOMINFO_USEHISTORYMAX];
        UseStoreName = new String[CUSTOMINFO_USEHISTORYMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1004";
                // �����o�[ID
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
                // �a�����i�y�[�W�J�n���t�j
                strSend = strSend + format.leftFitFormat( UsePageDate, 8 );
                // �a�����i�y�[�W�J�n���ԁj
                strSend = strSend + format.leftFitFormat( UsePageTime, 8 );
                // �a�����i�y�[�W�g���f�[�^�j
                strSend = strSend + format.leftFitFormat( UsePageData, 8 );
                // �\��
                strSend = strSend + "          ";

                // �d���̃w�b�_�擾
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
                        if ( strData.compareTo( "1005" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���y�[�W�J�n���t
                            strData = new String( cRecv, 111, 8 );
                            UseNextDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���y�[�W�J�n����
                            strData = new String( cRecv, 119, 8 );
                            UseNextTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���y�[�W�g���f�[�^
                            strData = new String( cRecv, 127, 8 );
                            UseNextData = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �O�y�[�W�J�n���t
                            strData = new String( cRecv, 135, 8 );
                            UsePrevDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �O�y�[�W�J�n����
                            strData = new String( cRecv, 143, 8 );
                            UsePrevTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �O�y�[�W�g���f�[�^
                            strData = new String( cRecv, 151, 8 );
                            UsePrevData = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���p��
                            strData = new String( cRecv, 159, 9 );
                            UseCount = Integer.valueOf( strData ).intValue();

                            // ���p����
                            for( i = 0 ; i < CUSTOMINFO_USEHISTORYMAX ; i++ )
                            {
                                // ���t
                                strData = new String( cRecv, 168 + (i * 78), 8 );
                                if ( strData != null )
                                {
                                    UseDate[i] = Integer.valueOf( strData ).intValue();
                                }

                                // ��������
                                strData = new String( cRecv, 176 + (i * 78), 8 );
                                if ( strData != null )
                                {
                                    UseRoom[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // ���p���z
                                strData = new String( cRecv, 184 + (i * 78), 9 );
                                if ( strData != null )
                                {
                                    UseTotal[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �l���|�C���g
                                strData = new String( cRecv, 193 + (i * 78), 9 );
                                if ( strData != null )
                                {
                                    UsePoint[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �X�܃R�[�h
                                strData = new String( cRecv, 202 + (i * 78), 4 );
                                if ( strData != null )
                                {
                                    UseStoreCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �X�ܖ���
                                strData = new String( cRecv, 206 + (i * 78), 30 );
                                if ( strData != null )
                                {
                                    UseStoreName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1004:" + e.toString() );
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
     * �d�����M����(1006)
     * �|�C���g�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1006()
    {
        return(sendPacket1006Sub( 0, "" ));
    }

    /**
     * �d�����M����(1006)
     * �|�C���g�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1006(int kind, String value)
    {
        return(sendPacket1006Sub( kind, value ));
    }

    /**
     * �d�����M����(1006)
     * �|�C���g�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1006Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        String strSign;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        PointNextDate = "";
        PointNextTime = "";
        PointPrevDate = "";
        PointPrevTime = "";
        Point = 0;
        PointDate = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointCount = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointName = new String[CUSTOMINFO_POINTHISTORYMAX];
        PointLimit = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreCode = new int[CUSTOMINFO_POINTHISTORYMAX];
        PointStoreName = new String[CUSTOMINFO_POINTHISTORYMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1006";
                // �����o�[ID
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
                // �y�[�W�J�n���t
                strSend = strSend + format.leftFitFormat( PointPageDate, 8 );
                // �y�[�W�J�n����
                strSend = strSend + format.leftFitFormat( PointPageTime, 8 );
                // �|�C���g���
                nf = new DecimalFormat( "00" );
                strData = nf.format( PointKind );
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1007" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���y�[�W�J�n���t
                            strData = new String( cRecv, 111, 8 );
                            PointNextDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���y�[�W�J�n����
                            strData = new String( cRecv, 119, 8 );
                            PointNextTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �O�y�[�W�J�n���t
                            strData = new String( cRecv, 127, 8 );
                            PointPrevDate = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �O�y�[�W�J�n����
                            strData = new String( cRecv, 135, 8 );
                            PointPrevTime = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �|�C���g���
                            strData = new String( cRecv, 143, 2 );
                            PointKind = Integer.valueOf( strData ).intValue();

                            // �L���|�C���g��
                            strData = new String( cRecv, 145, 9 );
                            Point = Integer.valueOf( strData ).intValue();

                            // �|�C���g����
                            for( i = 0 ; i < CUSTOMINFO_POINTHISTORYMAX ; i++ )
                            {
                                // ���t
                                strData = new String( cRecv, 154 + (i * 128), 8 );
                                if ( strData != null )
                                {
                                    PointDate[i] = Integer.valueOf( strData ).intValue();
                                }

                                // ����
                                strSign = new String( cRecv, 162 + (i * 128), 1 );
                                // �|�C���g
                                strData = new String( cRecv, 163 + (i * 128), 8 );
                                if ( strData != null )
                                {
                                    PointCount[i] = Integer.parseInt( strData );
                                }
                                if ( strSign.compareTo( "-" ) == 0 )
                                {
                                    PointCount[i] *= -1;
                                }

                                // �|�C���g����
                                strData = new String( cRecv, 171 + (i * 128), 40 );
                                PointName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                                // �|�C���g�L������
                                strData = new String( cRecv, 211 + (i * 128), 8 );
                                if ( strData != null )
                                {
                                    PointLimit[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �X�܃R�[�h
                                strData = new String( cRecv, 219 + (i * 128), 4 );
                                if ( strData != null )
                                {
                                    PointStoreCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �X�ܖ���
                                strData = new String( cRecv, 223 + (i * 128), 30 );
                                if ( strData != null )
                                {
                                    PointStoreName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1006:" + e.toString() );
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
     * �d�����M����(1008)
     * �S���B�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1008()
    {
        return(sendPacket1008Sub( 0, "" ));
    }

    /**
     * �d�����M����(1008)
     * �S���B�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1008(int kind, String value)
    {
        return(sendPacket1008Sub( kind, value ));
    }

    /**
     * �d�����M����(1008)
     * �S���B�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1008Sub(int kind, String value)
    {
        int i;
        int count;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        AllRoomCount = 0;
        AllRoomCode = new int[CUSTOMINFO_ROOMMAX];
        AllRoomName = new String[CUSTOMINFO_ROOMMAX];
        AllRoomUse = new int[CUSTOMINFO_ROOMMAX];
        AllRoomLast = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        AllRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1008";
                // �����o�[ID
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1009" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ������
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            AllRoomCount = count;

                            // �������p��
                            for( i = 0 ; i < count ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 114 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    AllRoomCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // ��������
                                strData = new String( cRecv, 117 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    AllRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // ���p��
                                strData = new String( cRecv, 125 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    AllRoomUse[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �ŏI���p��
                                strData = new String( cRecv, 128 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    AllRoomLast[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �\���ʒuX
                                strData = new String( cRecv, 136 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    AllRoomDispX[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �\���ʒuY
                                strData = new String( cRecv, 138 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    AllRoomDispY[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �\���ʒuZ
                                strData = new String( cRecv, 140 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    AllRoomDispZ[i] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1008:" + e.toString() );
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
     * �d�����M����(1010)
     * �I�[�i�[�Y���[�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1010()
    {
        return(sendPacket1010Sub( 0, "" ));
    }

    /**
     * �d�����M����(1010)
     * �I�[�i�[�Y���[�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1010(int kind, String value)
    {
        return(sendPacket1010Sub( kind, value ));
    }

    /**
     * �d�����M����(1010)
     * �I�[�i�[�Y���[�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1010Sub(int kind, String value)
    {
        int i;
        int count;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        OwnerRoomCount = 0;
        OwnerRoomCode = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomName = new String[CUSTOMINFO_ROOMMAX];
        OwnerRoomUse = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomLimit = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        OwnerRoomDispZ = new int[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1010";
                // �����o�[ID
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1011" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ������
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            OwnerRoomCount = count;

                            // �������p��
                            for( i = 0 ; i < count ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 114 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    OwnerRoomCode[i] = Integer.valueOf( strData ).intValue();
                                }

                                // ��������
                                strData = new String( cRecv, 117 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    OwnerRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // ���p��
                                strData = new String( cRecv, 125 + (i * 32), 3 );
                                if ( strData != null )
                                {
                                    OwnerRoomUse[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �L������
                                strData = new String( cRecv, 128 + (i * 32), 8 );
                                if ( strData != null )
                                {
                                    OwnerRoomLimit[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �\���ʒuX
                                strData = new String( cRecv, 136 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    OwnerRoomDispX[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �\���ʒuY
                                strData = new String( cRecv, 138 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    OwnerRoomDispY[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �\���ʒuZ
                                strData = new String( cRecv, 140 + (i * 32), 2 );
                                if ( strData != null )
                                {
                                    OwnerRoomDispZ[i] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1010:" + e.toString() );
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
     * �d�����M����(1012)
     * �����L���O���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1012()
    {
        return(sendPacket1012Sub( 0, "" ));
    }

    /**
     * �d�����M����(1012)
     * �����L���O���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1012(int kind, String value)
    {
        return(sendPacket1012Sub( kind, value ));
    }

    /**
     * �d�����M����(1012)
     * �����L���O���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1012Sub(int kind, String value)
    {
        int i;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        RankingUpdate = 0;
        RankingUptime = 0;
        RankingStart = 0;
        RankingEnd = 0;
        RankingGetNext = 0;
        RankingOrderNo = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderPoint = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderCount = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingOrderTotal = new int[CUSTOMINFO_RANKINGORDERMAX];
        RankingNo = new int[CUSTOMINFO_RANKINGMAX];
        RankingPoint = new int[CUSTOMINFO_RANKINGMAX];
        RankingCount = new int[CUSTOMINFO_RANKINGMAX];
        RankingTotal = new int[CUSTOMINFO_RANKINGMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1012";
                // �����o�[ID
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
                // �擾�敪
                nf = new DecimalFormat( "00" );
                strData = nf.format( RankingKind );
                strSend = strSend + strData;
                // �擾�J�n�����L���O
                nf = new DecimalFormat( "000000" );
                strData = nf.format( RankingGetStart );
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1013" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            Result = Integer.valueOf( strData ).intValue();

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ŏI�X�V���t
                            strData = new String( cRecv, 111, 8 );
                            RankingUpdate = Integer.valueOf( strData ).intValue();

                            // �ŏI�X�V����
                            strData = new String( cRecv, 119, 4 );
                            RankingUptime = Integer.valueOf( strData ).intValue();

                            // �J�Ê��ԁi�J�n�j
                            strData = new String( cRecv, 123, 8 );
                            RankingStart = Integer.valueOf( strData ).intValue();

                            // �J�Ê��ԁi�I���j
                            strData = new String( cRecv, 131, 8 );
                            RankingEnd = Integer.valueOf( strData ).intValue();

                            // �擾�敪
                            strData = new String( cRecv, 139, 2 );
                            RankingKind = Integer.valueOf( strData ).intValue();

                            // ���Ŏ擾�J�n�����L���O
                            strData = new String( cRecv, 141, 6 );
                            RankingGetNext = Integer.valueOf( strData ).intValue();

                            // �O�ド���L���O���
                            for( i = 0 ; i < CUSTOMINFO_RANKINGORDERMAX ; i++ )
                            {
                                // �O�ド���L���O����
                                strData = new String( cRecv, 147 + (i * 33), 6 );
                                RankingOrderNo[i] = Integer.valueOf( strData ).intValue();

                                // �O�ド���L���O�|�C���g��
                                strData = new String( cRecv, 153 + (i * 33), 9 );
                                RankingOrderPoint[i] = Integer.valueOf( strData ).intValue();

                                // �O�ド���L���O���p��
                                strData = new String( cRecv, 162 + (i * 33), 9 );
                                RankingOrderCount[i] = Integer.valueOf( strData ).intValue();

                                // �O�ド���L���O���p���z
                                strData = new String( cRecv, 171 + (i * 33), 9 );
                                RankingOrderTotal[i] = Integer.valueOf( strData ).intValue();
                            }

                            // �����L���O���
                            for( i = 0 ; i < CUSTOMINFO_RANKINGMAX ; i++ )
                            {
                                // �����L���O����
                                strData = new String( cRecv, 246 + (i * 33), 6 );
                                RankingNo[i] = Integer.valueOf( strData ).intValue();

                                // �����L���O�|�C���g��
                                strData = new String( cRecv, 252 + (i * 33), 9 );
                                RankingPoint[i] = Integer.valueOf( strData ).intValue();

                                // �����L���O���p��
                                strData = new String( cRecv, 261 + (i * 33), 9 );
                                RankingCount[i] = Integer.valueOf( strData ).intValue();

                                // �����L���O���p���z
                                strData = new String( cRecv, 270 + (i * 33), 9 );
                                RankingTotal[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1012:" + e.toString() );
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
     * �d�����M����(1014)
     * �����o�[���ύX
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1014()
    {
        return(sendPacket1014Sub( 0, "" ));
    }

    /**
     * �d�����M����(1014)
     * �����o�[���ύX
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1014(int kind, String value)
    {
        return(sendPacket1014Sub( kind, value ));
    }

    /**
     * �d�����M����(1014)
     * �����o�[���ύX
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1014Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        InfoMailAddress = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1014";
                // �����o�[ID
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
                // �j�b�N�l�[��
                strData = format.leftFitFormat( NickName, 20 );
                strSend = strSend + strData;
                // ���O
                strData = format.leftFitFormat( InfoName, 40 );
                strSend = strSend + strData;
                // �t���K�i
                strData = format.leftFitFormat( InfoKana, 20 );
                strSend = strSend + strData;
                // ����
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoSex );
                strSend = strSend + strData;
                // �Z���P
                strData = format.leftFitFormat( InfoAddress1, 40 );
                strSend = strSend + strData;
                // �Z���Q
                strData = format.leftFitFormat( InfoAddress2, 40 );
                strSend = strSend + strData;
                // �d�b�ԍ��P
                strData = format.leftFitFormat( InfoTel1, 15 );
                strSend = strSend + strData;
                // �d�b�ԍ��Q
                strData = format.leftFitFormat( InfoTel2, 15 );
                strSend = strSend + strData;
                // ���[���A�h���X
                strData = format.leftFitFormat( InfoMailAddress, 63 );
                strSend = strSend + strData;
                // ���[���}�K�W��
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoMailMag );
                strSend = strSend + strData;
                // �Ԕԁi�n��j
                strData = format.leftFitFormat( InfoCarArea, 8 );
                strSend = strSend + strData;
                // �Ԕԁi��ʁj
                strData = format.leftFitFormat( InfoCarKana, 2 );
                strSend = strSend + strData;
                // �Ԕԁi�Ԏ�j
                strData = format.leftFitFormat( InfoCarType, 3 );
                strSend = strSend + strData;
                // �Ԕ�
                strData = format.leftFitFormat( InfoCarNo, 4 );
                strSend = strSend + strData;

                try
                {
                    // �L�O���i�N�j
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( AnniversaryYear ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // �L�O���i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �L�O���i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �a�����Q�i�N�j
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Birthday2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // �a�����Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �a�����Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �L�O���Q�i�N�j
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Anniversary2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // �L�O���Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �L�O���Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }

                // �\��
                for( i = 0 ; i < 124 ; i++ )
                {
                    strSend = strSend + " ";
                }

                try
                {
                    // �d���w�b�_�̎擾
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );
                }
                catch ( Exception e )
                {
                    strHeader = "";
                }

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
                        if ( strData.compareTo( "1015" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 111, 63 );
                            InfoMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1014:" + e.toString() );
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
     * �d�����M����(1016)
     * ���[���}�K�W���o�^�E����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1016()
    {
        return(sendPacket1016Sub( 0, "" ));
    }

    /**
     * �d�����M����(1016)
     * ���[���}�K�W���o�^�E����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1016(int kind, String value)
    {
        return(sendPacket1016Sub( kind, value ));
    }

    /**
     * �d�����M����(1016)
     * ���[���}�K�W���o�^�E����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1016Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1016";
                // �����o�[ID
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
                // ���[���A�h���X
                strData = format.leftFitFormat( MailmagAddress, 63 );
                strSend = strSend + strData;
                // �����敪
                nf = new DecimalFormat( "00" );
                strData = nf.format( MailmagKind );
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1017" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 111, 63 );
                            MailmagAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1016:" + e.toString() );
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
     * �d�����M����(1018)
     * ���[���A�h���X�ύX
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1018()
    {
        return(sendPacket1018Sub( 0, "" ));
    }

    /**
     * �d�����M����(1018)
     * ���[���A�h���X�ύX
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1018(int kind, String value)
    {
        return(sendPacket1018Sub( kind, value ));
    }

    /**
     * �d�����M����(1018)
     * ���[���A�h���X�ύX
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1018Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1018";
                // �����o�[ID
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
                // ���[���A�h���X
                strData = format.leftFitFormat( MailReplaceAddress, 63 );
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1019" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 111, 63 );
                            MailReplaceAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1019:" + e.toString() );
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
     * �d�����M����(1020)
     * �S�X�S���B�����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1020()
    {
        return(sendPacket1020Sub( 0, "" ));
    }

    /**
     * �d�����M����(1020)
     * �S�X�S���B�����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1020(int kind, String value)
    {
        return(sendPacket1020Sub( kind, value ));
    }

    /**
     * �d�����M����(1020)
     * �S�X�S���B�����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1020Sub(int kind, String value)
    {
        int i;
        int j;
        int count;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        AllAllTenpoCount = 0;
        AllAllTenpoNo = new int[CUSTOMINFO_TENPOMAX];
        AllAllTenpoName = new String[CUSTOMINFO_TENPOMAX];
        AllAllRoomCount = new int[CUSTOMINFO_TENPOMAX];
        AllAllRoomCode = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomName = new String[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomUse = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];
        AllAllRoomLast = new int[CUSTOMINFO_TENPOMAX][CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1020";
                // �����o�[ID
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

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1021" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �X�ܐ�
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            AllAllTenpoCount = count;

                            // �B����
                            for( i = 0 ; i < count ; i++ )
                            {
                                // �X�ܔԍ�
                                strData = new String( cRecv, 114 + (i * 2862), 3 );
                                if ( strData != null )
                                {
                                    AllAllTenpoNo[i] = Integer.valueOf( strData ).intValue();
                                }

                                // �X�ܖ���
                                strData = new String( cRecv, 117 + (i * 2862), 40 );
                                if ( strData != null )
                                {
                                    AllAllTenpoName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }

                                // �������i�P�Q�W�Œ�j
                                strData = new String( cRecv, 157 + (i * 2862), 3 );
                                if ( strData != null )
                                {
                                    AllAllRoomCount[i] = Integer.valueOf( strData ).intValue();
                                }

                                for( j = 0 ; j < CUSTOMINFO_ROOMMAX ; j++ )
                                {

                                    // �����R�[�h
                                    strData = new String( cRecv, 160 + (i * 2862) + (j * 22), 3 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomCode[i][j] = Integer.valueOf( strData ).intValue();
                                    }

                                    // ��������
                                    strData = new String( cRecv, 163 + (i * 2862) + (j * 22), 8 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomName[i][j] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                    }

                                    // ���p��
                                    strData = new String( cRecv, 171 + (i * 2862) + (j * 22), 3 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomUse[i][j] = Integer.valueOf( strData ).intValue();
                                    }

                                    // �ŏI���p��
                                    strData = new String( cRecv, 174 + (i * 2862) + (j * 22), 8 );
                                    if ( strData != null )
                                    {
                                        AllAllRoomLast[i][j] = Integer.valueOf( strData ).intValue();
                                    }

                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1020:" + e.toString() );
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
     * �d�����M����(1022)
     * �ʔ̔̔����
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1022()
    {
        return(sendPacket1022Sub( 0, "" ));
    }

    /**
     * �d�����M����(1022)
     * �ʔ̔̔����
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1022(int kind, String value)
    {
        return(sendPacket1022Sub( kind, value ));
    }

    /**
     * �d�����M����(1022)
     * �ʔ̔̔����
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1022Sub(int kind, String value)
    {
        int i;
        int result;
        int count;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        OrderCount = 0;
        OrderReceiptDate = new int[CUSTOMINFO_ORDERMAX];
        OrderReceiptTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArrivePlanTime = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveDate = new int[CUSTOMINFO_ORDERMAX];
        OrderArriveTime = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryDate = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryTime = new int[CUSTOMINFO_ORDERMAX];
        OrderOrderMode = new int[CUSTOMINFO_ORDERMAX];
        OrderDeliveryMode = new int[CUSTOMINFO_ORDERMAX];
        OrderGoodsName = new String[CUSTOMINFO_ORDERMAX];
        OrderGoodsCount = new int[CUSTOMINFO_ORDERMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1022";
                // �����o�[ID
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

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1023" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���
                            strData = new String( cRecv, 111, 3 );
                            count = Integer.valueOf( strData ).intValue();
                            OrderCount = count;

                            // �ʔ̃f�[�^
                            for( i = 0 ; i < count ; i++ )
                            {
                                // ��t���t
                                strData = new String( cRecv, 114 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderReceiptDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ��t����
                                strData = new String( cRecv, 122 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderReceiptTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ���ח\����t
                                strData = new String( cRecv, 126 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderArrivePlanDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ���ח\�莞��
                                strData = new String( cRecv, 134 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderArrivePlanTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ���ד��t
                                strData = new String( cRecv, 138 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderArriveDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ���׎���
                                strData = new String( cRecv, 146 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderArriveTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ��n���t
                                strData = new String( cRecv, 150 + (i * 96), 8 );
                                if ( strData != null )
                                {
                                    OrderDeliveryDate[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ��n����
                                strData = new String( cRecv, 158 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderDeliveryTime[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ��ԋ敪
                                strData = new String( cRecv, 162 + (i * 96), 2 );
                                if ( strData != null )
                                {
                                    OrderOrderMode[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ��n�敪
                                strData = new String( cRecv, 164 + (i * 96), 2 );
                                if ( strData != null )
                                {
                                    OrderDeliveryMode[i] = Integer.valueOf( strData ).intValue();
                                }
                                // ���i��
                                strData = new String( cRecv, 166 + (i * 96), 40 );
                                if ( strData != null )
                                {
                                    OrderGoodsName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                                }
                                // ����
                                strData = new String( cRecv, 206 + (i * 96), 4 );
                                if ( strData != null )
                                {
                                    OrderGoodsCount[i] = Integer.valueOf( strData ).intValue();
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1022:" + e.toString() );
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
     * �d�����M����(1024)
     * �Ïؔԍ��擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1024()
    {
        return(sendPacket1024Sub( 0, "" ));
    }

    /**
     * �d�����M����(1024)
     * �Ïؔԍ��擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1024(int kind, String value)
    {
        return(sendPacket1024Sub( kind, value ));
    }

    /**
     * �d�����M����(1024)
     * �Ïؔԍ��擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1024Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        SecretResult = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1024";
                // �����o�[ID
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
                // �Ïؔԍ�
                strData = format.leftFitFormat( SecretCode, 8 );
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1025" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Ïؔԍ����ʎ擾
                            strData = new String( cRecv, 111, 2 );
                            SecretResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1024:" + e.toString() );
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
     * �d�����M����(1026)
     * �Ïؔԍ��ݒ�
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1026()
    {
        return(sendPacket1026Sub( 0, "" ));
    }

    /**
     * �d�����M����(1026)
     * �Ïؔԍ��ݒ�
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1026(int kind, String value)
    {
        return(sendPacket1026Sub( kind, value ));
    }

    /**
     * �d�����M����(1026)
     * �Ïؔԍ��ݒ�
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1026Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        SecretResult = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1026";
                // �����o�[ID
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
                // ���Ïؔԍ�
                strData = format.leftFitFormat( SecretOldCode, 8 );
                strSend = strSend + strData;
                // �V�Ïؔԍ�
                strData = format.leftFitFormat( SecretCode, 8 );
                strSend = strSend + strData;
                // �ڋq���ی�敪
                nf = new DecimalFormat( "00" );
                strData = nf.format( SecretType );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1027" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Ïؔԍ����ʎ擾
                            strData = new String( cRecv, 111, 2 );
                            SecretResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1026:" + e.toString() );
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
     * �d�����M����(1028)
     * ���[�UID�ύX
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1028()
    {
        return(sendPacket1028Sub( 0, "" ));
    }

    /**
     * �d�����M����(1028)
     * ���[�UID�ύX
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1028(int kind, String value)
    {
        return(sendPacket1028Sub( kind, value ));
    }

    /**
     * �d�����M����(1028)
     * ���[�UID�ύX
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1028Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        ChangeResult = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1028";
                // �����o�[ID
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
                // �ύX�テ�[�UID
                strData = format.leftFitFormat( ChangeUserId, 32 );
                strSend = strSend + strData;
                // �ύX��p�X���[�h
                strData = format.leftFitFormat( ChangePassword, 8 );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1029" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[�UID�ύX���ʎ擾
                            strData = new String( cRecv, 111, 2 );
                            ChangeResult = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1028:" + e.toString() );
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
     * �d�����M����(1030)
     * �����r���S�B�����擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1030()
    {
        return(sendPacket1030Sub( 0, "" ));
    }

    /**
     * �d�����M����(1030)
     * �����r���S�B�����擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1030(int kind, String value)
    {
        return(sendPacket1030Sub( kind, value ));
    }

    /**
     * �d�����M����(1030)
     * �����r���S�B�����擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1030Sub(int kind, String value)
    {
        boolean blnRet;
        int i;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        BingoRoomCount = 0;
        BingoRoomCode = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomName = new String[CUSTOMINFO_ROOMMAX];
        BingoRoomUse = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomLast = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispX = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispY = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomDispZ = new int[CUSTOMINFO_ROOMMAX];
        BingoRoomFlag = new int[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1030";
                // �����o�[ID
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

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1031" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ������
                            strData = new String( cRecv, 111, 3 );
                            BingoRoomCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < BingoRoomCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 114 + (i * 32), 3 );
                                BingoRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 117 + (i * 32), 8 );
                                BingoRoomName[i] = strData.trim();

                                // ���p��
                                strData = new String( cRecv, 125 + (i * 32), 3 );
                                BingoRoomUse[i] = Integer.valueOf( strData ).intValue();

                                // �ŏI���p��
                                strData = new String( cRecv, 128 + (i * 32), 8 );
                                BingoRoomLast[i] = Integer.valueOf( strData ).intValue();

                                // �\���ʒuX
                                strData = new String( cRecv, 136 + (i * 32), 2 );
                                BingoRoomDispX[i] = Integer.valueOf( strData ).intValue();

                                // �\���ʒuY
                                strData = new String( cRecv, 138 + (i * 32), 2 );
                                BingoRoomDispY[i] = Integer.valueOf( strData ).intValue();

                                // �\���ʒuZ
                                strData = new String( cRecv, 140 + (i * 32), 2 );
                                BingoRoomDispZ[i] = Integer.valueOf( strData ).intValue();

                                // �B���敪
                                strData = new String( cRecv, 142 + (i * 32), 2 );
                                BingoRoomFlag[i] = Integer.valueOf( strData ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1030:" + e.toString() );
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
     * �d�����M����(1032)
     * �I�[�i�[�Y�Z���N�V�������擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1032()
    {
        return(sendPacket1032Sub( 0, "" ));
    }

    /**
     * �d�����M����(1032)
     * �I�[�i�[�Y�Z���N�V�������擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1032(int kind, String value)
    {
        return(sendPacket1032Sub( kind, value ));
    }

    /**
     * �d�����M����(1032)
     * �I�[�i�[�Y�Z���N�V�������擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1032Sub(int kind, String value)
    {
        boolean blnRet;
        int i;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        SelectionRoomCount = 0;
        SelectionRoomCode = new int[CUSTOMINFO_ROOMMAX];
        SelectionRoomName = new String[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1032";
                // �����o�[ID
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

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1033" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ������
                            strData = new String( cRecv, 111, 3 );
                            SelectionRoomCount = Integer.valueOf( strData ).intValue();

                            for( i = 0 ; i < SelectionRoomCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 124 + (i * 20), 3 );
                                SelectionRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 127 + (i * 20), 8 );
                                SelectionRoomName[i] = strData.trim();

                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1032:" + e.toString() );
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
     * �d�����M����(1034)
     * ���[���A�h���X�폜
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1034()
    {
        return(sendPacket1034Sub( 0, "" ));
    }

    /**
     * �d�����M����(1034)
     * ���[���A�h���X�폜
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1034(int kind, String value)
    {
        return(sendPacket1034Sub( kind, value ));
    }

    /**
     * �d�����M����(1034)
     * ���[���A�h���X�폜
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1034Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1034";
                // �����o�[ID
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
                // ���[���A�h���X
                strData = format.leftFitFormat( MailDeleteAddress, 63 );
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1035" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 111, 63 );
                            MailDeleteAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1035:" + e.toString() );
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
     * �d�����M����(1036)
     * �������i�i�ꗗ�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1036()
    {
        return(sendPacket1036Sub( 0, "" ));
    }

    /**
     * �d�����M����(1036)
     * �������i�i�ꗗ���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1036(int kind, String value)
    {
        return(sendPacket1036Sub( kind, value ));
    }

    /**
     * �d�����M����(1032)
     * �������i�i�ꗗ���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1036Sub(int kind, String value)
    {
        boolean blnRet;
        int i;
        int result;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        GiftCount = 0;
        GiftDate = new int[0];
        GiftTime = new int[0];
        GiftExpireDate = new int[0];
        GiftName = new String[0];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1036";
                // �����o�[ID
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

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1037" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �i�i������
                            strData = new String( cRecv, 111, 3 );
                            GiftCount = Integer.valueOf( strData ).intValue();

                            GiftDate = new int[GiftCount];
                            GiftTime = new int[GiftCount];
                            GiftExpireDate = new int[GiftCount];
                            GiftName = new String[GiftCount];

                            for( i = 0 ; i < GiftCount ; i++ )
                            {
                                // �������t
                                strData = new String( cRecv, 178 + (i * 128), 8 );
                                GiftDate[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 186 + (i * 128), 4 );
                                GiftTime[i] = Integer.valueOf( strData ).intValue();

                                // �L������
                                strData = new String( cRecv, 190 + (i * 128), 8 );
                                GiftExpireDate[i] = Integer.valueOf( strData ).intValue();

                                // �i�i����
                                strData = new String( cRecv, 198 + (i * 128), 40 );
                                GiftName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1036:" + e.toString() );
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
     * �d�����M����(1038)
     * QR�����o�[�f�[�^�m�F�v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1038()
    {
        return(sendPacket1038Sub( 0, "" ));
    }

    /**
     * �d�����M����(1038)
     * QR�����o�[�f�[�^�m�F�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1038(int kind, String value)
    {
        return(sendPacket1038Sub( kind, value ));
    }

    /**
     * �d�����M����(1038)
     * QR�����o�[�f�[�^�m�F�v��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1038Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        InfoRankCode = 0;
        InfoRankName = "";
        InfoProtect = 0;
        InfoSecretChange = 0;
        InfoSecretDigit = 0;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1038";
                // �����o�[ID
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

                try
                {
                    // �a�����i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �a�����i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
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

                    // ��M�ҋ@
                    strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        // �R�}���h�擾
                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1039" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ���O�C�����̂ݖ߂�l���擾����
                            strData = new String( cRecv, 36, 9 );
                            CustomId = strData.trim();
                            strData = new String( cRecv, 45, 2 );
                            Birthday1 = strData.trim();
                            strData = new String( cRecv, 47, 2 );
                            Birthday2 = strData.trim();
                            strData = new String( cRecv, 49, 32 );
                            UserId = strData.trim();
                            strData = new String( cRecv, 81, 8 );
                            Password = strData.trim();

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �i�i�����L��
                            strData = new String( cRecv, 111, 2 );
                            InfoGiftChange = Integer.valueOf( strData ).intValue();

                            // �����o�[�����N�R�[�h
                            strData = new String( cRecv, 113, 3 );
                            InfoRankCode = Integer.valueOf( strData ).intValue();

                            // �����o�[�����N����
                            strData = new String( cRecv, 116, 40 );
                            InfoRankName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ڋq���ی�敪
                            strData = new String( cRecv, 156, 2 );
                            InfoProtect = Integer.valueOf( strData ).intValue();

                            // �Ïؔԍ��ύX���敪
                            strData = new String( cRecv, 158, 2 );
                            InfoSecretChange = Integer.valueOf( strData ).intValue();

                            // �Ïؔԍ�����
                            strData = new String( cRecv, 160, 2 );
                            InfoSecretDigit = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1038:" + e.toString() );
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
     * �d�����M����(1040)
     * QR�����o�[�f�[�^�o�^
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1040()
    {
        return(sendPacket1040Sub( 0, "" ));
    }

    /**
     * �d�����M����(1040)
     * QR�����o�[�f�[�^�o�^
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1040(int kind, String value)
    {
        return(sendPacket1040Sub( kind, value ));
    }

    /**
     * �d�����M����(1040)
     * QR�����o�[�f�[�^�o�^
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1040Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

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
                strSend = "1040";
                // �����o�[ID
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
                // �a�����i�N�j
                nf = new DecimalFormat( "0000" );
                strData = nf.format( Integer.valueOf( BirthdayYear ).intValue() );
                strSend = strSend + strData;
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
                // �j�b�N�l�[��
                strData = format.leftFitFormat( NickName, 20 );
                strSend = strSend + strData;
                // ���O
                strData = format.leftFitFormat( InfoName, 40 );
                strSend = strSend + strData;
                // �t���K�i
                strData = format.leftFitFormat( InfoKana, 20 );
                strSend = strSend + strData;
                // ����
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoSex );
                strSend = strSend + strData;
                // �Z���P
                strData = format.leftFitFormat( InfoAddress1, 40 );
                strSend = strSend + strData;
                // �Z���Q
                strData = format.leftFitFormat( InfoAddress2, 40 );
                strSend = strSend + strData;
                // �d�b�ԍ��P
                strData = format.leftFitFormat( InfoTel1, 15 );
                strSend = strSend + strData;
                // �d�b�ԍ��Q
                strData = format.leftFitFormat( InfoTel2, 15 );
                strSend = strSend + strData;
                // ���[���A�h���X
                strData = format.leftFitFormat( InfoMailAddress, 63 );
                strSend = strSend + strData;
                // ���[���}�K�W��
                nf = new DecimalFormat( "00" );
                strData = nf.format( InfoMailMag );
                strSend = strSend + strData;
                // �Ԕԁi�n��j
                strData = format.leftFitFormat( InfoCarArea, 8 );
                strSend = strSend + strData;
                // �Ԕԁi��ʁj
                strData = format.leftFitFormat( InfoCarKana, 2 );
                strSend = strSend + strData;
                // �Ԕԁi�Ԏ�j
                strData = format.leftFitFormat( InfoCarType, 3 );
                strSend = strSend + strData;
                // �Ԕ�
                strData = format.leftFitFormat( InfoCarNo, 4 );
                strSend = strSend + strData;

                try
                {
                    // �L�O���i�N�j
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( AnniversaryYear ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // �L�O���i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �L�O���i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �a�����Q�i�N�j
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Birthday2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // �a�����Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �a�����Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Birthday2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �L�O���Q�i�N�j
                    nf = new DecimalFormat( "0000" );
                    strData = nf.format( Integer.valueOf( Anniversary2Year ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "0000";
                }
                try
                {
                    // �L�O���Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_1 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                try
                {
                    // �L�O���Q�i���j
                    nf = new DecimalFormat( "00" );
                    strData = nf.format( Integer.valueOf( Anniversary2_2 ).intValue() );
                    strSend = strSend + strData;
                }
                catch ( Exception e )
                {
                    strSend = strSend + "00";
                }
                // �\��
                for( i = 0 ; i < 120 ; i++ )
                {
                    strSend = strSend + " ";
                }

                try
                {
                    // �d���w�b�_�̎擾
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );
                }
                catch ( Exception e )
                {
                    strHeader = "";
                }

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
                        if ( strData.compareTo( "1041" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���[���A�h���X
                            strData = new String( cRecv, 111, 63 );
                            InfoMailAddress = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1040:" + e.toString() );
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
     * �d�����M����(0400)
     * ���b�Z�[�W���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0400()
    {
        return(sendPacket0400Sub( 0, "" ));
    }

    /**
     * �d�����M����(0400)
     * ���b�Z�[�W���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0400(int kind, String value)
    {
        return(sendPacket0400Sub( kind, value ));
    }

    /**
     * �d�����M����(0400)
     * ���b�Z�[�W���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0400Sub(int kind, String value)
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

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        MessageCommon1 = "";
        MessageCommon2 = "";
        MessageCommon3 = "";
        MessageCommon4 = "";
        MessageCommon5 = "";
        MessageMember1 = "";
        MessageMember2 = "";
        MessageMember3 = "";
        MessageMember4 = "";
        MessageMember5 = "";
        MessageOne1 = "";
        MessageOne2 = "";
        MessageOne3 = "";
        MessageOne4 = "";
        MessageOne5 = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0400";
                // �����o�[ID
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

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "0401" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���ʃ��b�Z�[�W�P
                            strData = new String( cRecv, 111, 80 );
                            MessageCommon1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���ʃ��b�Z�[�W�Q
                            strData = new String( cRecv, 191, 80 );
                            MessageCommon2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���ʃ��b�Z�[�W�R
                            strData = new String( cRecv, 271, 80 );
                            MessageCommon3 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���ʃ��b�Z�[�W�S
                            strData = new String( cRecv, 351, 80 );
                            MessageCommon4 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ���ʃ��b�Z�[�W�T
                            strData = new String( cRecv, 431, 80 );
                            MessageCommon5 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����o�[���b�Z�[�W�P
                            strData = new String( cRecv, 511, 80 );
                            MessageMember1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����o�[���b�Z�[�W�Q
                            strData = new String( cRecv, 591, 80 );
                            MessageMember2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����o�[���b�Z�[�W�R
                            strData = new String( cRecv, 671, 80 );
                            MessageMember3 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����o�[���b�Z�[�W�S
                            strData = new String( cRecv, 751, 80 );
                            MessageMember4 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����o�[���b�Z�[�W�T
                            strData = new String( cRecv, 831, 80 );
                            MessageMember5 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ʃ��b�Z�[�W�P
                            strData = new String( cRecv, 911, 80 );
                            MessageOne1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ʃ��b�Z�[�W�Q
                            strData = new String( cRecv, 991, 80 );
                            MessageOne2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ʃ��b�Z�[�W�R
                            strData = new String( cRecv, 1071, 80 );
                            MessageOne3 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ʃ��b�Z�[�W�S
                            strData = new String( cRecv, 1151, 80 );
                            MessageOne4 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �ʃ��b�Z�[�W�T
                            strData = new String( cRecv, 1231, 80 );
                            MessageOne5 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0400:" + e.toString() );
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
     * �d�����M����(0402)
     * �C�x���g���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0402()
    {
        return(sendPacket0402Sub( 0, "" ));
    }

    /**
     * �d�����M����(0402)
     * �C�x���g���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket0402(int kind, String value)
    {
        return(sendPacket0402Sub( kind, value ));
    }

    /**
     * �d�����M����(0402)
     * �C�x���g���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket0402Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        EventDispKind = 0;
        EventMessageKind = new int[CUSTOMINFO_EVENTMAX];
        EventMessage = new String[CUSTOMINFO_EVENTMAX];
        EventRoomEventName = "";
        EventRoomCount = 0;
        EventRoomCode = new int[CUSTOMINFO_ROOMMAX];
        EventRoomName = new String[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "0402";
                // �����o�[ID
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
                // �C�x���g�敪
                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( EventKind ).intValue() );
                strSend = strSend + strData;
                // �\��
                strSend = strSend + "          ";

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "0403" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 89, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // �j�b�N�l�[���擾
                            strData = new String( cRecv, 91, 20 );
                            NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �\���I���敪
                            strData = new String( cRecv, 175, 1 );
                            EventDispKind = Integer.valueOf( strData ).intValue();

                            // �C�x���g�������
                            for( i = 0 ; i < CUSTOMINFO_EVENTMAX ; i++ )
                            {
                                // �\���敪
                                strData = new String( cRecv, 176 + (i * 41), 1 );
                                EventMessageKind[i] = Integer.valueOf( strData ).intValue();

                                // �\�����b�Z�[�W
                                strData = new String( cRecv, 177 + (i * 41), 40 );
                                EventMessage[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }

                            // �����C�x���g����
                            strData = new String( cRecv, 586, 40 );
                            EventRoomEventName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // ������
                            strData = new String( cRecv, 626, 3 );
                            EventRoomCount = Integer.valueOf( strData ).intValue();

                            // �C�x���g�������
                            for( i = 0 ; i < EventRoomCount ; i++ )
                            {
                                // �����R�[�h
                                strData = new String( cRecv, 629 + (i * 11), 3 );
                                EventRoomCode[i] = Integer.valueOf( strData ).intValue();

                                // ��������
                                strData = new String( cRecv, 632 + (i * 11), 8 );
                                EventRoomName[i] = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket0402:" + e.toString() );
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
     * �d�����M����(1042)
     * �����o�[��t���擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1042()
    {
        return(sendPacket1042Sub( 0, "" ));
    }

    /**
     * �d�����M����(1042)
     * �����o�[��t���擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1042(int kind, String value)
    {
        return(sendPacket1042Sub( kind, value ));
    }

    /**
     * �d�����M����(1042)
     * �C�x���g���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1042Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        EventDispKind = 0;
        EventMessageKind = new int[CUSTOMINFO_EVENTMAX];
        EventMessage = new String[CUSTOMINFO_EVENTMAX];
        EventRoomEventName = "";
        EventRoomCount = 0;
        EventRoomCode = new int[CUSTOMINFO_ROOMMAX];
        EventRoomName = new String[CUSTOMINFO_ROOMMAX];

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1042";

                // ��������
                strSend += format.leftFitFormat( TouchRoomName, 8 );

                // �\��
                strSend += format.leftFitFormat( "", 120 );

                try
                {
                    // �d���w�b�_�擾
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );
                    // �d���̌���
                    strSend = strHeader + strSend;

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
                        if ( strData.compareTo( "1043" ) == 0 )
                        {
                            // �������ʎ擾
                            strData = new String( cRecv, 36, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ��������
                            strData = new String( cRecv, 38, 8 );
                            TouchRoomName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �����o�[ID
                            strData = new String( cRecv, 46, 9 );
                            CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Z�L�����e�B�R�[�h
                            strData = new String( cRecv, 55, 5 );
                            TouchSecurityCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �a�����i���j
                            strData = new String( cRecv, 60, 2 );
                            Birthday1 = strData;

                            // �a�����i���j
                            strData = new String( cRecv, 62, 4 );
                            Birthday2 = strData;
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1042:" + e.toString() );
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
     * �d�����M����(1044)
     * �V�K�����o�[�o�^�ʒm�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1044()
    {
        return(sendPacket1044Sub( 0, "" ));
    }

    /**
     * �d�����M����(1044)
     * �V�K�����o�[�o�^�ʒm�擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1044(int kind, String value)
    {
        return(sendPacket1044Sub( kind, value ));
    }

    /**
     * �d�����M����(1044)
     * �C�x���g���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1044Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1044";
                // �����o�[ID
                strSend += format.leftFitFormat( CustomId, 9 );
                // �\��
                strSend += format.leftFitFormat( "", 140 );

                // �d���w�b�_�擾
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
                        //
                        if ( strData.compareTo( "1045" ) == 0 )
                        {

                            // �����o�[ID
                            strData = new String( cRecv, 36, 9 );
                            CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Z�L�����e�B�R�[�h
                            strData = new String( cRecv, 45, 5 );
                            TouchSecurityCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������ʎ擾
                            strData = new String( cRecv, 50, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;
                        }
                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1044:" + e.toString() );
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
     * �d�����M����(1046)
     * �����o�[�o�^�ʒm�擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1046()
    {
        return(sendPacket1046Sub( 0, "" ));
    }

    /**
     * �d�����M����(1046)
     * �����o�[�o�^�ʒm�擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1046(int kind, String value)
    {
        return(sendPacket1046Sub( kind, value ));
    }

    /**
     * �d�����M����(1046)
     * �C�x���g���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1046Sub(int kind, String value)
    {
        int i;
        int result;
        boolean blnRet;
        boolean blnCommand;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;

        blnCommand = false;

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1046";
                // �����o�[ID
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
                // �a�����i�N�j
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchBirthYear1 );
                // �a�����i���j
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchBirthMonth1 );
                // �a�����i���j
                strSend += nf.format( TouchBirthDate1 );

                // ���[�UID
                strSend += format.leftFitFormat( UserId, 32 );

                // �p�X���[�h
                strSend += format.leftFitFormat( Password, 8 );

                // �j�b�N�l�[��
                strSend += format.leftFitFormat( NickName, 20 );

                // ���O
                strSend += format.leftFitFormat( TouchName, 40 );

                // �t���K�i
                strSend += format.leftFitFormat( TouchNameKana, 20 );

                // ����
                strSend += nf.format( TouchSex );

                // �Z��1
                strSend += format.leftFitFormat( TouchAddr1, 40 );

                // �Z��2
                strSend += format.leftFitFormat( TouchAddr2, 40 );

                // �d�b�ԍ�1
                strSend += format.leftFitFormat( TouchTel1, 15 );

                // �d�b�ԍ�2
                strSend += format.leftFitFormat( TouchTel2, 15 );

                // �����A�h
                strSend += format.leftFitFormat( TouchMailAddr, 63 );

                // �����}�K
                strSend += nf.format( TouchMailMag );

                // �L�O���i�N�j
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchMemorialYear1 );

                // �L�O���i���j
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchMemorialMonth1 );
                // �L�O���i���j
                strSend += nf.format( TouchMemorialDate1 );

                // �a����2�i�N�j
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchBirthYear2 );

                // �a����2�i���j
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchBirthMonth2 );
                // �a����2�i���j
                strSend += nf.format( TouchBirthDate2 );

                // �L�O��2�i�N�j
                nf = new DecimalFormat( "0000" );
                strSend += nf.format( TouchMemorialYear2 );
                // �L�O��2�i���j
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchMemorialMonth2 );
                // �L�O��2�i���j
                nf = new DecimalFormat( "00" );
                strSend += nf.format( TouchMemorialDate2 );
                // �\��
                strSend += format.leftFitFormat( "", 140 );

                try
                {
                    strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );

                    // �d���̌���
                    strSend = strHeader + strSend;

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
                        //
                        if ( strData.compareTo( "1047" ) == 0 )
                        {
                            blnCommand = true;

                            // �����o�[ID
                            strData = new String( cRecv, 36, 9 );
                            CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �Z�L�����e�B�R�[�h
                            strData = new String( cRecv, 45, 5 );
                            TouchSecurityCode = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            // �������ʎ擾
                            strData = new String( cRecv, 50, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;
                        }
                    }

                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1044:" + e.toString() );
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
     * �d�����M����(1048)
     * �����o�[�`�F�b�N�C�����ю擾
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1048()
    {
        return(sendPacket1048Sub( 0, "" ));
    }

    /**
     * �d�����M����(1048)
     * �����o�[�`�F�b�N�C�����ю擾
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1048(int kind, String value)
    {
        return(sendPacket1048Sub( kind, value ));
    }

    /**
     * �d�����M����(1048)
     * �C�x���g���擾
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1048Sub(int kind, String value)
    {
        int result;
        boolean blnRet;
        String strSend;
        String strRecv;
        String strHeader;
        String strData;
        char cRecv[];
        TcpClient tcpClient;
        StringFormat format;
        NumberFormat nf;

        // �f�[�^�̃N���A
        Result = 0;
        NickName = "";
        TermId = "ceritfiedid";
        Password = "6268";

        if ( HotelId != null )
        {
            // �z�X�g�ڑ�����
            tcpClient = new TcpClient();
            blnRet = connect( tcpClient, kind, value );

            if ( blnRet != false )
            {
                format = new StringFormat();

                // �R�}���h
                strSend = "1048";

                // ���[�UID
                strSend += format.leftFitFormat( TermId, 11 );

                // �p�X���[�h
                strSend += format.leftFitFormat( Password, 4 );

                // �n�s�z�e�`�F�b�N�C���R�[�h
                nf = new DecimalFormat( "00000000" );
                // �J�n���t
                strSend += nf.format( StartDate );
                // �I�����t
                strSend += nf.format( EndDate );

                // �\��
                strSend += format.leftFitFormat( "", 120 );

                // �d���w�b�_�擾
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
                        if ( strData.compareTo( "1049" ) == 0 )
                        {
                            // ���[�U�[ID
                            strData = new String( cRecv, 36, 11 );
                            TermId = strData;

                            // �p�X���[�h
                            strData = new String( cRecv, 47, 4 );
                            Password = strData;

                            // �������ʎ擾
                            strData = new String( cRecv, 51, 2 );
                            result = Integer.valueOf( strData ).intValue();
                            Result = result;

                            // ���уf�[�^��
                            strData = new String( cRecv, 53, 3 );
                            TouchData = Integer.valueOf( strData ).intValue();

                            CollectDate = new int[TouchData];
                            AllCheckIn = new int[TouchData];
                            MemberCheckIn = new int[TouchData];
                            AllMember = new int[TouchData];
                            for( int i = 0 ; i < TouchData ; i++ )
                            {
                                // �W�v��
                                CollectDate[i] = Integer.valueOf( new String( cRecv, 56 + i * 23, 8 ) ).intValue();

                                // �S�`�F�b�N�C����
                                AllCheckIn[i] = Integer.valueOf( new String( cRecv, 64 + i * 23, 5 ) ).intValue();

                                // web�����o�[�`�F�b�N�C����
                                MemberCheckIn[i] = Integer.valueOf( new String( cRecv, 69 + i * 23, 5 ) ).intValue();

                                // �S�����o�[�`�F�b�N�C����
                                AllMember[i] = Integer.valueOf( new String( cRecv, 74 + i * 23, 5 ) ).intValue();
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    log.error( "sendPacket1048:" + e.toString() );
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
     * �d�����M����(1050)
     * �o�^�O�����o�[���v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1050()
    {
        return(sendPacket1050Sub( 0, "" ));
    }

    /**
     * �d�����M����(1050)
     * �o�^�O�����o�[���v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1050(int kind, String value)
    {
        return(sendPacket1050Sub( kind, value ));
    }

    /**
     * �d�����M����(1050)
     * �o�^�O�����o�[���v��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1050Sub(int kind, String value)
    {
        this.Result = 0;
        this.TouchBirthYear1 = 0;
        this.TouchBirthMonth1 = 0;
        this.TouchBirthDate1 = 0;
        this.NickName = "";
        this.TouchName = "";
        this.TouchNameKana = "";
        this.TouchSex = 0;
        this.TouchAddr1 = "";
        this.TouchAddr2 = "";
        this.TouchTel1 = "";
        this.TouchTel2 = "";
        this.TouchMailAddr = "";
        this.TouchMailMag = 0;
        this.TouchMemorialYear1 = 0;
        this.TouchMemorialMonth1 = 0;
        this.TouchMemorialDate1 = 0;
        this.TouchBirthYear2 = 0;
        this.TouchBirthMonth2 = 0;
        this.TouchBirthDate2 = 0;
        this.TouchMemorialYear2 = 0;
        this.TouchMemorialMonth2 = 0;
        this.TouchMemorialDate2 = 0;

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );
            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1050";

                // �����o�[ID
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

                NumberFormat nf = new DecimalFormat( "00" );
                String strData = nf.format( Integer.valueOf( this.Birthday1 ).intValue() );
                strSend = strSend + strData;

                nf = new DecimalFormat( "00" );
                strData = nf.format( Integer.valueOf( this.Birthday2 ).intValue() );
                strSend = strSend + strData;

                strSend = strSend + format.leftFitFormat( this.UserId, 32 );

                strSend = strSend + format.leftFitFormat( this.Password, 8 );

                strSend = strSend + "          ";

                String strHeader = tcpClient.getPacketHeader( this.HotelId, strSend.length() );

                strSend = strHeader + strSend;
                try
                {
                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1051" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 2 );
                            this.Result = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 38, 9 );
                            this.CustomId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 47, 4 );
                            this.TouchBirthYear1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 51, 2 );
                            this.TouchBirthMonth1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 53, 2 );
                            this.TouchBirthDate1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 55, 32 );
                            this.UserId = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 87, 8 );
                            this.Password = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 95, 20 );
                            this.NickName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 115, 40 );
                            this.TouchName = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 155, 20 );
                            this.TouchNameKana = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 175, 2 );
                            this.TouchSex = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 177, 40 );
                            this.TouchAddr1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 217, 40 );
                            this.TouchAddr2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 257, 15 );
                            this.TouchTel1 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 272, 15 );
                            this.TouchTel2 = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 287, 63 );
                            this.TouchMailAddr = new String( strData.trim().getBytes( "8859_1" ), "Windows-31J" );

                            strData = new String( cRecv, 350, 2 );
                            this.TouchMailMag = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 352, 4 );
                            this.TouchMemorialYear1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 356, 2 );
                            this.TouchMemorialMonth1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 358, 2 );
                            this.TouchMemorialDate1 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 360, 4 );
                            this.TouchBirthYear2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 364, 2 );
                            this.TouchBirthMonth2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 366, 2 );
                            this.TouchBirthDate2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 368, 4 );
                            this.TouchMemorialYear2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 372, 2 );
                            this.TouchMemorialMonth2 = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 374, 2 );
                            this.TouchMemorialDate2 = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1050:" + e.toString() );
                    return false;
                }
                tcpClient.disconnectService();

                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * �d�����M����(1052)
     * �����o�[�Y�J�[�h�ۋ��m�F�v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1052()
    {
        return sendPacket1052Sub( 0, "" );
    }

    /**
     * �d�����M����(1052)
     * �����o�[�Y�J�[�h�ۋ��m�F�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1052(int kind, String value)
    {
        return sendPacket1052Sub( kind, value );
    }

    /**
     * �d�����M����(1052)
     * �����o�[�Y�J�[�h�ۋ��m�F�v��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1052Sub(int kind, String value)
    {
        this.Result = 0;
        this.NickName = "";

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );

            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1052";

                strSend = strSend + format.leftFitFormat( this.TermId, 11 );

                strSend = strSend + format.leftFitFormat( this.Password, 4 );

                strSend = strSend + format.leftFitFormat( "", 10 );

                String strHeader = tcpClient.getPacketHeader( this.HotelId, strSend.length() );

                strSend = strHeader + strSend;
                try
                {
                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        String strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1053" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 11 );
                            this.TermId = strData;

                            strData = new String( cRecv, 47, 4 );
                            this.Password = strData;

                            strData = new String( cRecv, 51, 9 );
                            this.GoodsCode = Integer.valueOf( strData ).intValue();

                            strData = new String( cRecv, 60, 9 );
                            this.GoodsPrice = Integer.valueOf( strData ).intValue();
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1052:" + e.toString() );
                    return false;
                }
                String strRecv;
                tcpClient.disconnectService();

                return true;
            }

            return false;
        }

        return false;
    }

    /**
     * �d�����M����(1054)
     * �����o�[�Y�J�[�h�ۋ��ʒm�v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1054()
    {
        return sendPacket1054Sub( 0, "" );
    }

    /**
     * �d�����M����(1054)
     * �����o�[�Y�J�[�h�ۋ��ʒm�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1054(int kind, String value)
    {
        return sendPacket1054Sub( kind, value );
    }

    /**
     * �d�����M����(1054)
     * �����o�[�Y�J�[�h�ۋ��ʒm�v��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1054Sub(int kind, String value)
    {
        this.Result = 0;

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );

            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1054";

                strSend = strSend + format.leftFitFormat( this.TermId, 11 );

                strSend = strSend + format.leftFitFormat( this.Password, 4 );

                NumberFormat nf = new DecimalFormat( "000000000" );
                strSend = strSend + nf.format( Integer.valueOf( this.GoodsCode ).intValue() );

                nf = new DecimalFormat( "000000000" );
                strSend = strSend + nf.format( Integer.valueOf( this.GoodsPrice ).intValue() );

                strSend = strSend + format.leftFitFormat( this.TouchRoomName, 8 );

                strSend = strSend + format.leftFitFormat( "", 10 );

                String strHeader = tcpClient.getPacketHeader( this.HotelId, strSend.length() );

                strSend = strHeader + strSend;
                try
                {
                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        String strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1055" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 11 );
                            this.TermId = strData;

                            strData = new String( cRecv, 47, 4 );
                            this.Password = strData;

                            strData = new String( cRecv, 51, 2 );
                            int result = Integer.valueOf( strData ).intValue();
                            this.Result = result;
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1054:" + e.toString() );
                    return false;
                }
                String strRecv;
                tcpClient.disconnectService();

                return true;
            }

            return false;
        }

        return false;
    }

    /**
     * �d�����M����(1056)
     * �����o�[�o�^�m�F�v��
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1056()
    {
        return sendPacket1056Sub( 0, "" );
    }

    /**
     * �d�����M����(1056)
     * �����o�[�o�^�m�F�v��
     * �ڑ����@�w��(1:HotelId�w��,2:IP�A�h���X�w��)
     * 
     * @param kind �ڑ����@(1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean sendPacket1056(int kind, String value)
    {
        return sendPacket1056Sub( kind, value );
    }

    /**
     * �d�����M����(1056)
     * �����o�[�o�^�m�F�v��
     * 
     * @param kind �ڑ����@(0:�w��Ȃ�,1:HotelId�w��,2:IP�A�h���X�w��)
     * @param value �w��ڑ����@
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean sendPacket1056Sub(int kind, String value)
    {
        this.Result = 0;

        if ( this.HotelId != null )
        {
            TcpClient tcpClient = new TcpClient();
            boolean blnRet = connect( tcpClient, kind, value );

            if ( blnRet )
            {
                StringFormat format = new StringFormat();

                String strSend = "1056";

                strSend = strSend + format.leftFitFormat( this.TermId, 11 );

                strSend = strSend + format.leftFitFormat( this.Password, 4 );

                strSend = strSend + format.leftFitFormat( this.TouchRoomName, 8 );

                strSend = strSend + format.leftFitFormat( "", 10 );

                try
                {
                    String strHeader = tcpClient.getPacketHeader( HotelId, strSend.getBytes( "Windows-31J" ).length );

                    strSend = strHeader + strSend;

                    tcpClient.send( strSend );

                    String strRecv = tcpClient.recv();
                    if ( strRecv != null )
                    {
                        char[] cRecv = new char[strRecv.length()];
                        cRecv = strRecv.toCharArray();

                        String strData = new String( cRecv, 32, 4 );
                        if ( strData.compareTo( "1057" ) == 0 )
                        {
                            strData = new String( cRecv, 36, 11 );
                            this.TermId = strData;

                            strData = new String( cRecv, 47, 4 );
                            this.Password = strData;

                            strData = new String( cRecv, 51, 2 );
                            int result = Integer.valueOf( strData ).intValue();
                            this.Result = result;
                        }
                    }
                }
                catch ( Exception e )
                {
                    this.log.error( "sendPacket1056:" + e.toString() );
                    return false;
                }
                String strRecv;
                tcpClient.disconnectService();

                return true;
            }

            return false;
        }

        return false;
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
