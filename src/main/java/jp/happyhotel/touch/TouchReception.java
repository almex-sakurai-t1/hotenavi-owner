package jp.happyhotel.touch;

import java.util.ArrayList;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCouponUnit;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class TouchReception
{
    final int                          TIMEOUT              = 5000;
    final int                          HOTENAVI_PORT_NO     = 7023;
    final int                          RESULT_OK            = 1;
    final int                          RESULT_NG            = 2;
    final int                          COUPON_KIND_AUTO     = 0;
    final int                          COUPON_KIND_MANUAL   = 1;
    final int                          COUPON_NO_USE        = 0;
    final int                          COUPON_USED1         = 1;
    final int                          COUPON_USED10        = 10;
    final int                          COUPON_USED100       = 100;
    final int                          HAPIHOTE_PORT_NO     = 7046;
    final int                          DEPOSIT_NO           = 1;
    final int                          DEPOSIT_CASH         = 2;
    final int                          DEPOSIT_CREDIT       = 3;
    final int                          NONREFUNDABLE        = 1;   // �ԋ��s��
    final int                          CREDIT_NONREFUNDABLE = 2;   // �N���W�b�g���Z�̕ԋ��s��
    boolean                            boolUseableMile;
    boolean                            boolMemberInfo;
    boolean                            boolMemberCheckIn;
    boolean                            boolMemberAccept;
    boolean                            boolCouponAvailable;
    int                                point;
    int                                point2;
    int                                errorCode;
    String                             unavailableMessage;
    String                             customId;
    String                             securityCode;
    String                             customRank;
    DtoApHotelCustomerData             apHotelCustomerData;
    private ArrayList<DtoApCouponUnit> apCouponUnitList;
    boolean                            boolMemberCardIssued;
    int                                goodsCode;
    int                                goodsPrice;

    public TouchReception()
    {
        this.boolUseableMile = true;
        this.boolMemberInfo = false;
        this.boolMemberCheckIn = false;
        this.boolMemberAccept = false;
        this.boolCouponAvailable = false;
        this.point = 0;
        this.point2 = 0;
        this.errorCode = 0;
        this.unavailableMessage = "";
        this.customId = "";
        this.securityCode = "";
        this.customRank = "";
        this.apHotelCustomerData = null;
        this.apCouponUnitList = null;
        this.boolMemberCardIssued = false;
        this.goodsCode = 0;
        this.goodsPrice = 0;
    }

    public boolean isBoolUseableMile()
    {
        return boolUseableMile;
    }

    public boolean isBoolMemberInfo()
    {
        return boolMemberInfo;
    }

    public boolean isBoolMemberCheckIn()
    {
        return boolMemberCheckIn;
    }

    public boolean isBoolMemberAccept()
    {
        return boolMemberAccept;
    }

    public boolean isBoolCouponAvailable()
    {
        return boolCouponAvailable;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPoint2()
    {
        return point2;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getUnavailableMessage()
    {
        return unavailableMessage;
    }

    public String getCustomId()
    {
        return customId;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public String getCustomRank()
    {
        return customRank;
    }

    public DtoApHotelCustomerData getApHotelCustomerData()
    {
        return apHotelCustomerData;
    }

    public ArrayList<DtoApCouponUnit> getApCouponUnitList()
    {
        return apCouponUnitList;
    }

    public boolean isBoolMemberCardIssued()
    {
        return boolMemberCardIssued;
    }

    public int getGoodsCode()
    {
        return goodsCode;
    }

    public int getGoodsPrice()
    {
        return goodsPrice;
    }

    public void setBoolUseableMile(boolean boolUseableMile)
    {
        this.boolUseableMile = boolUseableMile;
    }

    public void setBoolMemberInfo(boolean boolMemberInfo)
    {
        this.boolMemberInfo = boolMemberInfo;
    }

    public void setBoolMemberCheckIn(boolean boolMemberCheckIn)
    {
        this.boolMemberCheckIn = boolMemberCheckIn;
    }

    public void setBoolMemberAccept(boolean boolMemberAccept)
    {
        this.boolMemberAccept = boolMemberAccept;
    }

    public void setBoolCouponAvailable(boolean boolCouponAvailable)
    {
        this.boolCouponAvailable = boolCouponAvailable;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPoint2(int point2)
    {
        this.point2 = point2;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setUnavailableMessage(String unavailableMessage)
    {
        this.unavailableMessage = unavailableMessage;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public void setCustomRank(String customRank)
    {
        this.customRank = customRank;
    }

    public void setApHotelCustomerData(DtoApHotelCustomerData apHotelCustomerData)
    {
        this.apHotelCustomerData = apHotelCustomerData;
    }

    public void setApCouponUnitList(ArrayList<DtoApCouponUnit> apCouponUnitList)
    {
        this.apCouponUnitList = apCouponUnitList;
    }

    public void setBoolMemberCardIssued(boolean boolMemberCardIssued)
    {
        this.boolMemberCardIssued = boolMemberCardIssued;
    }

    public void setGoodsCode(int goodsCode)
    {
        this.goodsCode = goodsCode;
    }

    public void setGoodsPrice(int goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void getTouchReception(int id, int seq, String userId, HotelCi hc, String roomName, boolean boolHotelCustom, int nonrefundableFlag)
    {
        String hapihoteIp = "";
        String hotenaviIp = "";
        DepositInfo di;
        TouchCi tc = new TouchCi();
        MemberCheckIn mci;
        MemberAcceptInfo memberAcceptInfo;
        DataApHotelCustom dahc = new DataApHotelCustom();
        TouchUserCoupon tuc = new TouchUserCoupon();
        GroupHotelCustom hotelCustom = new GroupHotelCustom();
        MemberCardIssuedCheck mcic = new MemberCardIssuedCheck();
        MemberCardChargeConfirm mccc = new MemberCardChargeConfirm();
        int birthday = 0;

        hapihoteIp = HotelIp.getFrontIp( id );
        hotenaviIp = HotelIp.getHotenaviIp( id );

        // �}�C���g�p�ς݂̏ꍇ
        if ( hc.getHotelCi().getUsePoint() > 0 )
        {
            boolUseableMile = false;
            unavailableMessage = hc.getHotelCi().getUsePoint() + "�}�C����t�ς݂ł��B";
        }
        // �O����擾
        else if ( hapihoteIp.equals( "" ) == false )
        {
            di = new DepositInfo();
            di.getDepositInfo( id, hc.getHotelCi().getSeq(), roomName, hotenaviIp, nonrefundableFlag );

            boolUseableMile = di.isBoolUseableMile();
            unavailableMessage = di.getUnavailableMessage();
        }

        // �ڋq��������
        if ( boolHotelCustom != false && hapihoteIp.equals( "" ) == false )
        {
            // �����o�[DB���烁���o�[�����擾
            boolMemberInfo = dahc.getValidData( id, userId );
            if ( boolMemberInfo == false )
            {
                boolMemberInfo = hotelCustom.getMutltiCustomData( id, userId );
                // �O���[�v�X�܂���f�[�^���擾
                if ( boolMemberInfo != false )
                {
                    dahc = hotelCustom.getCustom();
                }
            }

            if ( boolMemberInfo != false )
            {
                customId = dahc.getCustomId();
                securityCode = dahc.getSecurityCode();

                // 2015.03.18 �a����1��0�̏ꍇ�͒a����2���Z�b�g���� tashiro
                if ( dahc.getBirthday1() > 0 )
                {
                    birthday = dahc.getBirthday1();
                }
                else
                {
                    birthday = dahc.getBirthday2();
                }

                /** �i1010�j�n�s�z�e_�����o�[�`�F�b�N�C���d�� **/
                mci = new MemberCheckIn();
                mci.getMemberCheckIn( id, hc.getHotelCi().getSeq(), roomName, hapihoteIp, customId, securityCode, birthday );

                if ( mci.getResult() == RESULT_OK )
                {
                    boolMemberCheckIn = mci.isBoolMemberCheckIn();
                    // OK��������ap_touch_ci��custom_id��ǉ�
                    if ( tc.getData( hc.getHotelCi().getId(), hc.getHotelCi().getSeq() ) != false )
                    {
                        tc.getTouchCi().setCustomId( customId );
                        tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        tc.getTouchCi().updateData( hc.getHotelCi().getId(), hc.getHotelCi().getSeq() );
                    }

                    hc.getHotelCi().setCustomId( customId );
                    hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    hc.getHotelCi().updateData( hc.getHotelCi().getId(), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );

                    apHotelCustomerData = mci.getApHotelCustomerData();

                    // �K�v�ɉ����ĕ\������
                    customRank = mci.getRank();
                    point = mci.getPoint();
                    point2 = mci.getPoint2();
                }
                else
                {
                    errorCode = mci.getErrorCode();
                    // 2015.03.21 �z�X�g����̃G���[���Ȃ�������A�����o�[�`�F�b�N�C�����s�̃G���[�R�[�h���Z�b�g tashiro
                    if ( errorCode == 0 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30401;
                    }
                }
            }
            else
            {
                memberAcceptInfo = new MemberAcceptInfo();
                /** �i1042�j�z�e�i�r_�����o�[��t���擾�d�� **/
                memberAcceptInfo.getMemberAcceptInfo( id, roomName, hotenaviIp );
                boolMemberAccept = memberAcceptInfo.isBoolMemberAccept();
                customId = memberAcceptInfo.getCustomId();
                Logging.info( "customId:" + customId + ",boolMemberAccept:" + boolMemberAccept );

                if ( memberAcceptInfo.getSecurityCode().equals( "XX999" ) || memberAcceptInfo.getSecurityCode().equals( "AL123" ) )
                {
                    if ( errorCode == 0 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30409;
                        Logging.info( "getTouchReception errCode:" + errorCode );
                    }
                }
            }

            // (1056)�����o�[�J�[�h�𔭍s���Ă��邩���擾����
            mcic.setRoomName( roomName );
            if ( mcic.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) ) )
            {
                setBoolMemberCardIssued( mcic.getResult() == 1 ); // 1:���s�ς݁�true
            }
            else
            {
                if ( errorCode == 0 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30407;
                    Logging.info( "getTouchReception errCode:" + errorCode );
                }
            }

            // (1052)�����o�[�J�[�h���ۋ����ǂ������擾����
            if ( mccc.sendToHost( hotenaviIp, TIMEOUT, HOTENAVI_PORT_NO, Integer.toString( id ) ) )
            {
                setGoodsCode( mccc.getGoodsCode() );
                setGoodsPrice( mccc.getGoodsPrice() );
            }
            else
            {
                if ( errorCode == 0 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30408;
                    Logging.info( "getTouchReception errCode:" + errorCode );
                }
            }
        }
        // �N�[�|�����p�\�m�F
        boolCouponAvailable = tuc.getCouponData( userId, id );
        // �N�[�|�����X�g�̎擾
        apCouponUnitList = this.getCouponList( tuc );

    }

    public void getMemberCheckIn(HotelCi hc)
    {
        String hapihoteIp = "";
        String hotenaviIp = "";
        MemberCheckIn mci;
        int id = hc.getHotelCi().getId();
        String userId = hc.getHotelCi().getUserId();
        int birthday = 0;
        String roomName = "";
        DataApHotelCustom dahc = new DataApHotelCustom();
        DataHotelRoomMore dhrm = new DataHotelRoomMore();

        if ( dahc.getValidData( id, userId ) != false )
        {
            customId = dahc.getCustomId();
            securityCode = dahc.getSecurityCode();
        }
        if ( dhrm.getData( id, hc.getHotelCi().getRoomNo() ) != false )
        {
            roomName = dhrm.getRoomNameHost();
        }

        hapihoteIp = HotelIp.getFrontIp( id );
        hotenaviIp = HotelIp.getHotenaviIp( id );

        /** �i1010�j�n�s�z�e_�����o�[�`�F�b�N�C���d�� **/
        mci = new MemberCheckIn();
        mci.getMemberCheckIn( id, hc.getHotelCi().getSeq(), roomName, hapihoteIp, customId, securityCode, birthday );
        if ( mci.getResult() == RESULT_OK )
        {
            customRank = mci.getRank();
            point = mci.getPoint();
            point2 = mci.getPoint2();
        }
        else
        {
            errorCode = mci.getErrorCode();
        }

    }

    /****
     * �N�[�|�����X�g�̍쐬
     * 
     * @param tuc
     * @return
     */
    public ArrayList<DtoApCouponUnit> getCouponList(TouchUserCoupon tuc)
    {
        ArrayList<DtoApCouponUnit> couponUnitList = new ArrayList<DtoApCouponUnit>();
        // �����K�p�N�[�|�����������ꍇ
        if ( tuc.isAutoUserCoupon() != false )
        {
            for( int i = 0 ; i < tuc.getUserAutoCoupon().length ; i++ )
            {
                DtoApCouponUnit apCouponUnit = new DtoApCouponUnit();
                apCouponUnit.setCouponKind( COUPON_KIND_AUTO );
                apCouponUnit.setCouponNo( tuc.getUserAutoCoupon()[i].getCouponSeq() );
                apCouponUnit.setUsedFlag( COUPON_NO_USE );
                apCouponUnit.setCouponName( tuc.getCoupon()[i].getDispText() );
                couponUnitList.add( apCouponUnit );
            }
        }
        // �\�����N�[�|�����������ꍇ
        if ( tuc.isUserCoupon() != false )
        {
            if ( tuc.getHotelCoupon().getBenefitText1().equals( "" ) == false )
            {
                DtoApCouponUnit apCouponUnit = new DtoApCouponUnit();
                apCouponUnit.setCouponKind( COUPON_KIND_MANUAL );
                apCouponUnit.setCouponNo( tuc.getUserCoupon().getCouponNo() );
                apCouponUnit.setUsedFlag( COUPON_USED1 );
                apCouponUnit.setCouponName( tuc.getHotelCoupon().getBenefitText1() );
                couponUnitList.add( apCouponUnit );
            }
            if ( tuc.getHotelCoupon().getBenefitText2().equals( "" ) == false )
            {
                DtoApCouponUnit apCouponUnit = new DtoApCouponUnit();
                apCouponUnit.setCouponKind( COUPON_KIND_MANUAL );
                apCouponUnit.setCouponNo( tuc.getUserCoupon().getCouponNo() );
                apCouponUnit.setUsedFlag( COUPON_USED10 );
                apCouponUnit.setCouponName( tuc.getHotelCoupon().getBenefitText2() );
                couponUnitList.add( apCouponUnit );
            }
            if ( tuc.getHotelCoupon().getBenefitText3().equals( "" ) == false )
            {
                DtoApCouponUnit apCouponUnit = new DtoApCouponUnit();
                apCouponUnit.setCouponKind( COUPON_KIND_MANUAL );
                apCouponUnit.setCouponNo( tuc.getUserCoupon().getCouponNo() );
                apCouponUnit.setUsedFlag( COUPON_USED100 );
                apCouponUnit.setCouponName( tuc.getHotelCoupon().getBenefitText3() );
                couponUnitList.add( apCouponUnit );
            }
        }
        return couponUnitList;
    }

}
