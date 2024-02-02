package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApMemberCardReg;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.touch.MemberCardCharge;
import jp.happyhotel.touch.MemberCheckIn;
import jp.happyhotel.touch.MemberInfo;
import jp.happyhotel.touch.MemberOverwrite;
import jp.happyhotel.touch.MemberRegist;
import jp.happyhotel.touch.MemberSync;
import jp.happyhotel.touch.TouchCi;

/**
 * �n�s�z�e�A�v���`�F�b�N�C���N���X
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtOverwriteCustom extends BaseAction
{
    final int                      HAPIHOTE_TIMEOUT = 10000;
    final int                      HOTENAVI_TIMEOUT = 3000;
    final int                      PORT_NO          = 7023;
    final int                      HOTENAVI_PORT_NO = 7023;
    final int                      HAPIHOTE_PORT_NO = 7046;
    final int                      RESULT_OK        = 1;
    final int                      RESULT_NG        = 2;
    final int                      RETRY_COUNT      = 3;
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApMemberCardReg     apMemberCardReg;
    private DtoApHotelCustomerData apHotelCustomerData;

    /**
     * �n�s�z�e�^�b�`
     * 
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // XML�o��
        int i = 0;
        boolean ret = false;
        boolean retMemberOperation = false;
        boolean boolHotelCustom = false;// �z�e���ڋq
        boolean boolCardRegFlag = false;// �J�[�h�o�^
        boolean boolHotenaviContract = false;// �z�e�i�r�_��
        boolean boolDuplicationRegFlag = false;// �J�[�h�d���o�^
        boolean boolResult = false;// �J�[�h�o�^����

        String hotelName = "";
        String hotenaviId = "";
        String termId = "";
        String termNo = "";
        String roomName = "";

        String paramId;
        String paramSeq;
        String userId = "";
        String hapihoteIp = "";
        String hotenaviIp = "";
        String roomNo = "";

        int birthday1 = 0;
        int birthday2 = 0;
        int memorial1 = 0;
        int memorial2 = 0;
        int errorCode = 0;
        int point = 0;
        int point2 = 0;

        HotelCi hc = null;
        TouchCi tc = new TouchCi();
        DataHotelBasic dhb = null;
        DataHotelRoomMore dhrm = null;
        DataApHotelCustom dahc = null;
        DataApHotelSetting dahs = null;
        DataMasterPref dmp = new DataMasterPref();
        DataMasterCity dmc = new DataMasterCity();
        MemberRegist mr = new MemberRegist();

        String paramCustomId = "";
        String securityCode = "";
        String hotelPassword = "";
        try
        {
            Logging.info( "ActionHtOverwriteCustom start" );

            String hotelUserId = request.getParameter( "userId" );
            // TODO�@�p�X���[�h
            String pass = request.getParameter( "password" );

            String name = request.getParameter( "name" );
            String nameKana = request.getParameter( "furigana" );
            String handleName = request.getParameter( "handleName" );
            String sex = request.getParameter( "sex" );
            String birthday1Year = request.getParameter( "birthday1_year" );
            String birthday1Month = request.getParameter( "birthday1_month" );
            String birthday1Day = request.getParameter( "birthday1_day" );
            String birthday2Year = request.getParameter( "birthday2_year" );
            String birthday2Month = request.getParameter( "birthday2_month" );
            String birthday2Day = request.getParameter( "birthday2_day" );
            String memorialday1Year = request.getParameter( "memorialday1_year" );
            String memorialday1Month = request.getParameter( "memorialday1_month" );
            String memorialday1Day = request.getParameter( "memorialday1_day" );
            String memorialday2Year = request.getParameter( "memorialday2_year" );
            String memorialday2Month = request.getParameter( "memorialday2_month" );
            String memorialday2Day = request.getParameter( "memorialday2_day" );
            String prefCode1 = request.getParameter( "pref_code1" );
            String prefCode2 = request.getParameter( "pref_code2" );
            String jisCode1 = request.getParameter( "jiscode1" );
            String jisCode2 = request.getParameter( "jiscode2" );
            String tel1 = request.getParameter( "telNo1" );
            String tel2 = request.getParameter( "telNo2" );
            String address1 = request.getParameter( "address1" );
            String address2 = request.getParameter( "address2" );
            String mailAddr = request.getParameter( "mailAddress" );
            String mailAddrConfirm = request.getParameter( "mailAddressConfirm" );
            String mailMagazine = request.getParameter( "mailMagazine" );
            String paramGoodsCode = request.getParameter( "goodsCode" );
            String paramGoodsPrice = request.getParameter( "goodsPrice" );

            if ( hotelUserId == null || hotelUserId.equals( "" ) != false )
            {
                hotelUserId = "";
            }
            if ( pass == null || pass.equals( "" ) != false )
            {
                pass = "";
            }
            if ( name == null || name.equals( "" ) != false )
            {
                name = "";
            }
            if ( nameKana == null || nameKana.equals( "" ) != false )
            {
                nameKana = "";
            }
            if ( handleName == null || handleName.equals( "" ) != false )
            {
                handleName = "";
            }
            if ( sex == null || sex.equals( "" ) != false || CheckString.numCheck( sex ) == false )
            {
                sex = "0";
            }
            if ( birthday1Year == null || birthday1Year.equals( "" ) != false || CheckString.numCheck( birthday1Year ) == false )
            {
                birthday1Year = "0";
            }
            if ( birthday1Month == null || birthday1Month.equals( "" ) != false || CheckString.numCheck( birthday1Month ) == false )
            {
                birthday1Month = "0";
            }
            if ( birthday1Day == null || birthday1Day.equals( "" ) != false || CheckString.numCheck( birthday1Day ) == false )
            {
                birthday1Day = "0";
            }
            if ( birthday2Year == null || birthday2Year.equals( "" ) != false || CheckString.numCheck( birthday2Year ) == false )
            {
                birthday2Year = "0";
            }
            if ( birthday2Month == null || birthday2Month.equals( "" ) != false || CheckString.numCheck( birthday2Month ) == false )
            {
                birthday2Month = "0";
            }
            if ( birthday2Day == null || birthday2Day.equals( "" ) != false || CheckString.numCheck( birthday2Day ) == false )
            {
                birthday2Day = "0";
            }
            if ( memorialday1Year == null || memorialday1Year.equals( "" ) != false || CheckString.numCheck( memorialday1Year ) == false )
            {
                memorialday1Year = "0";
            }
            if ( memorialday1Month == null || memorialday1Month.equals( "" ) != false || CheckString.numCheck( memorialday1Month ) == false )
            {
                memorialday1Month = "0";
            }
            if ( memorialday1Day == null || memorialday1Day.equals( "" ) != false || CheckString.numCheck( memorialday1Day ) == false )
            {
                memorialday1Day = "0";
            }
            if ( memorialday2Year == null || memorialday2Year.equals( "" ) != false || CheckString.numCheck( memorialday2Year ) == false )
            {
                memorialday2Year = "0";
            }
            if ( memorialday2Month == null || memorialday2Month.equals( "" ) != false || CheckString.numCheck( memorialday2Month ) == false )
            {
                memorialday2Month = "0";
            }
            if ( memorialday2Day == null || memorialday2Day.equals( "" ) != false || CheckString.numCheck( memorialday2Day ) == false )
            {
                memorialday2Day = "0";
            }
            if ( prefCode1 == null || prefCode1.equals( "" ) != false || CheckString.numCheck( prefCode1 ) == false )
            {
                prefCode1 = "0";
            }
            if ( prefCode2 == null || prefCode2.equals( "" ) != false || CheckString.numCheck( prefCode2 ) == false )
            {
                prefCode2 = "0";
            }
            if ( jisCode1 == null || jisCode1.equals( "" ) != false || CheckString.numCheck( jisCode1 ) == false )
            {
                jisCode1 = "0";
            }
            if ( jisCode2 == null || jisCode2.equals( "" ) != false || CheckString.numCheck( jisCode2 ) == false )
            {
                jisCode2 = "0";
            }
            if ( tel1 == null || tel1.equals( "" ) != false || CheckString.numCheck( tel1 ) == false )
            {
                tel1 = "";
            }
            if ( tel2 == null || tel2.equals( "" ) != false || CheckString.numCheck( tel2 ) == false )
            {
                tel2 = "";
            }
            if ( address1 == null || address1.equals( "" ) != false )
            {
                address1 = "";
            }
            if ( address2 == null || address2.equals( "" ) != false )
            {
                address2 = "";
            }
            if ( mailAddr == null || mailAddr.equals( "" ) != false )
            {
                mailAddr = "";
            }
            if ( mailAddrConfirm == null || mailAddrConfirm.equals( "" ) != false )
            {
                mailAddrConfirm = "";
            }
            if ( mailMagazine == null || mailMagazine.equals( "" ) != false || CheckString.numCheck( mailMagazine ) == false )
            {
                mailMagazine = "0";
            }
            // �a����
            birthday1 = Integer.parseInt( birthday1Year ) * 10000 + Integer.parseInt( birthday1Month ) * 100 + Integer.parseInt( birthday1Day );
            birthday2 = Integer.parseInt( birthday2Year ) * 10000 + Integer.parseInt( birthday2Month ) * 100 + Integer.parseInt( birthday2Day );
            memorial1 = Integer.parseInt( memorialday1Year ) * 10000 + Integer.parseInt( memorialday1Month ) * 100 + Integer.parseInt( memorialday1Day );
            memorial2 = Integer.parseInt( memorialday2Year ) * 10000 + Integer.parseInt( memorialday2Month ) * 100 + Integer.parseInt( memorialday2Day );

            // �O�y�[�W����̈��p��
            paramId = request.getParameter( "h_id" );
            paramSeq = request.getParameter( "h_seq" );
            paramCustomId = request.getParameter( "h_customId" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
            }
            if ( paramCustomId == null || paramCustomId.equals( "" ) != false || CheckString.numCheck( paramCustomId ) == false )
            {
                paramCustomId = "0";
            }
            if ( paramGoodsCode == null || paramGoodsCode.equals( "" ) != false || CheckString.numCheck( paramGoodsCode ) == false )
            {
                paramGoodsCode = "0";
            }
            if ( paramGoodsPrice == null || paramGoodsPrice.equals( "" ) != false || CheckString.numCheck( paramGoodsPrice ) == false )
            {
                paramGoodsPrice = "0";
            }

            dhb = new DataHotelBasic();
            dahs = new DataApHotelSetting();
            dhrm = new DataHotelRoomMore();

            // �Z��1��DB����擾
            // dmp.getData( Integer.parseInt( prefCode1 ) );
            // dmc.getData( Integer.parseInt( jisCode1 ) );
            // address1 = dmp.getName() + dmc.getName();

            // �Z��2��DB����擾
            // if ( Integer.parseInt( prefCode2 ) > 0 && Integer.parseInt( jisCode2 ) > 0 )
            // {
            // dmp.getData( Integer.parseInt( prefCode2 ) );
            // dmc.getData( Integer.parseInt( jisCode2 ) );
            // address2 = dmp.getName() + dmc.getName();
            // }

            // ���̓f�[�^���Z�b�g
            /** (1046)�z�e�i�r�@�����o�[���o�^ **/
            MemberOverwrite mo = new MemberOverwrite();
            mo.setMemberId( paramCustomId );
            mo.setBirthYear1( Integer.parseInt( birthday1Year ) );
            mo.setBirthMonth1( Integer.parseInt( birthday1Month ) );
            mo.setBirthDate1( Integer.parseInt( birthday1Day ) );
            mo.setUserId( hotelUserId );
            mo.setPassword( pass );
            mo.setHandleName( handleName );
            mo.setName( name );
            mo.setNameKana( nameKana );
            mo.setSex( Integer.parseInt( sex ) );
            mo.setAddr1( address1 );
            mo.setAddr2( address2 );
            mo.setTel1( tel1 );
            mo.setTel2( tel2 );
            mo.setMailAddr( mailAddrConfirm );
            mo.setMailMagFlag( Integer.parseInt( mailMagazine ) );
            mo.setMemorialYear1( Integer.parseInt( memorialday1Year ) );
            mo.setMemorialMonth1( Integer.parseInt( memorialday1Month ) );
            mo.setMemorialDate1( Integer.parseInt( memorialday1Day ) );
            mo.setBirthYear2( Integer.parseInt( birthday2Year ) );
            mo.setBirthMonth2( Integer.parseInt( birthday2Month ) );
            mo.setBirthDate2( Integer.parseInt( birthday2Day ) );
            mo.setMemorialYear2( Integer.parseInt( memorialday2Year ) );
            mo.setMemorialMonth2( Integer.parseInt( memorialday2Month ) );
            mo.setMemorialDate2( Integer.parseInt( memorialday2Day ) );

            if ( Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramSeq ) > 0 )
            {

                if ( dhb.getData( Integer.parseInt( paramId ) ) != false )
                {
                    hotelName = dhb.getName();
                    hotenaviId = dhb.getHotenaviId();
                }
                boolHotelCustom = dahs.getData( Integer.parseInt( paramId ) );

                hc = new HotelCi();
                hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                // �f�[�^���擾�ł��āA���[�UID������ΐV�K����o�^���s���B
                if ( hc.getHotelCi() != null )
                {

                    dahc = new DataApHotelCustom();
                    // �������擾
                    dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );
                    roomName = dhrm.getRoomNameHost();
                    // �z�e���̃t�����gIP���擾
                    hapihoteIp = HotelIp.getFrontIp( Integer.parseInt( paramId ) );
                    hotenaviIp = HotelIp.getHotenaviIp( Integer.parseInt( paramId ) );
                    roomNo = hc.getHotelCi().getRoomNo();
                    userId = hc.getHotelCi().getUserId();
                    ret = mo.sendToHost( hotenaviIp, HOTENAVI_TIMEOUT, HOTENAVI_PORT_NO, paramId );
                    if ( mo.getResult() == RESULT_OK )
                    {
                        securityCode = mo.getSecurityCode();
                        // �z�X�g����OK���Ԃ��Ă����̂ŁAap_hotel_custom�Ƀf�[�^��o�^
                        // ���Ƀ����o�[�o�^���݂��ǂ������`�F�b�N
                        ret = dahc.getData( Integer.parseInt( paramId ), userId );

                        // ���ɓo�^�ς݂̏ꍇ�ɂ́A�a�����̔N�́A�o�^�ς݂̕����g��
                        if ( ret != false )
                        {
                            birthday1 = birthday1 % 10000 + (dahc.getBirthday1() / 10000) * 10000;
                            birthday2 = birthday2 % 10000 + (dahc.getBirthday2() / 10000) * 10000;
                        }
                        dahc.setUserId( userId );
                        dahc.setId( Integer.parseInt( paramId ) );
                        dahc.setHotelUserId( hotelUserId );
                        dahc.setHotelPassword( hotelPassword );
                        // �z�X�g����Ԃ��Ă��������o�[ID���g���悤�ɕύX
                        dahc.setCustomId( mo.getMemberId() );
                        dahc.setSecurityCode( securityCode );
                        dahc.setBirthday1( birthday1 );
                        dahc.setBirthday2( birthday2 );
                        dahc.setMemorial1( memorial1 );
                        dahc.setMemorial2( memorial2 );
                        dahc.setNickname( handleName );
                        dahc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dahc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dahc.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dahc.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dahc.setRegistStatus( RESULT_OK );
                        dahc.setDelFlag( 0 );
                        if ( ret != false )
                        {
                            ret = dahc.updateData( Integer.parseInt( paramId ), userId );
                        }
                        else
                        {
                            ret = dahc.insertData();
                        }

                        // �����o�[�ʌڋq�o�^���(sc.r_member_custom)�f�[�^�쐬
                        int accountId = UserAgent.getAccountId( request );
                        MemberSync msync = new MemberSync();
                        msync.syncDataSc( accountId, Integer.parseInt( paramId ), userId );

                        // �}�C�z�e���o�^
                        tc.registMyHotel( hotelUserId, Integer.parseInt( paramId ) );

                        /** �i1010�j�n�s�z�e_�����o�[�`�F�b�N�C���d�� **/
                        MemberCheckIn mci = new MemberCheckIn();
                        MemberInfo mi = new MemberInfo();

                        mci.setSeq( hc.getHotelCi().getSeq() );
                        mci.setRoomName( dhrm.getRoomNameHost() );
                        mci.setCustomId( paramCustomId );
                        mci.setSecurityCode( securityCode );
                        mci.sendToHost( hapihoteIp, HAPIHOTE_TIMEOUT, HAPIHOTE_PORT_NO, paramId );

                        if ( mci.getResult() == RESULT_OK )
                        {
                            // OK��������ap_touch_ci��custom_id��ǉ�
                            if ( tc.getData( hc.getHotelCi().getId(), hc.getHotelCi().getSeq() ) != false )
                            {
                                tc.getTouchCi().setCustomId( paramCustomId );
                                tc.getTouchCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                tc.getTouchCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                tc.getTouchCi().updateData( hc.getHotelCi().getId(), hc.getHotelCi().getSeq() );
                            }
                            hc.getHotelCi().setCustomId( paramCustomId );
                            hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            hc.getHotelCi().updateData( hc.getHotelCi().getId(), hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );

                            mi.setMemberId( paramCustomId );
                            mi.setBirthMonth( dahc.getBirthday1() / 100 % 100 );
                            mi.setBirthDay( dahc.getBirthday1() % 100 );
                            mi.sendToHost( hotenaviIp, HOTENAVI_TIMEOUT, HOTENAVI_PORT_NO, paramId );

                            if ( mi.getResult() == RESULT_OK )
                            {
                                apHotelCustomerData = mi.getMemberInfo();
                            }
                        }
                        else
                        {
                            errorCode = mci.getErrorCode();
                            if ( errorCode == 0 )
                            {
                                errorCode = HapiTouchErrorMessage.ERR_30401;
                            }
                        }
                    }
                    else
                    {
                        errorCode = HapiTouchErrorMessage.ERR_30601;
                    }
                }
            }
            Logging.info( "ActionHtOverWriteCustom.MemberCardCharge() paramGoodsCode:" + paramGoodsCode + ",paramGoodsPrice:" + paramGoodsPrice + ",paramId:" + paramId + ",paramSeq:" + paramSeq, "ActionHtOverWriteCustom.MemberCardCharge()" );
            if ( errorCode == 0 )
            {
                if ( Integer.parseInt( paramGoodsCode ) != 0 && Integer.parseInt( paramGoodsPrice ) != 0 )
                {

                    /** (1054)�z�e�i�r�@�����o�[�Y�J�[�h�ۋ��ʒm�v�� **/
                    MemberCardCharge mcc = new MemberCardCharge();
                    mcc.setGoodsCode( Integer.parseInt( paramGoodsCode ) );
                    mcc.setGoodsPrice( Integer.parseInt( paramGoodsPrice ) );
                    mcc.setRoomName( roomName );
                    boolean priceRet = false;
                    if ( Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramSeq ) > 0 )
                    {
                        priceRet = mcc.sendToHost( hotenaviIp, HOTENAVI_TIMEOUT, HOTENAVI_PORT_NO, paramId );
                    }
                    Logging.info( "ActionHtOverWriteCustom.MemberCardCharge() ret:" + priceRet, "ActionHtOverWriteCustom.MemberCardCharge()" );
                }
            }
            Logging.info( "ActionHtOverWriteCustom ret:" + ret + ",errorCode:" + errorCode, "ActionHtOverWriteCustom" );

            if ( errorCode != 0 )
            {
                // �G���[���e��o�^
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomNo );
                daeh.setId( Integer.parseInt( paramId ) );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( Integer.parseInt( paramSeq ) );
                daeh.setUserId( userId );
                daeh.insertData();
            }

            apCommon = new DtoApCommon();
            apMemberCardReg = new DtoApMemberCardReg();
            if ( apHotelCustomerData == null )
            {
                apHotelCustomerData = new DtoApHotelCustomerData();
                apHotelCustomerData.setCustomId( paramCustomId );
            }

            // ���ʐݒ�
            apCommon.setId( Integer.parseInt( paramId ) );
            apCommon.setHotelName( hotelName );
            apCommon.setRoomNo( roomNo );
            apCommon.setSeq( Integer.parseInt( paramSeq ) );

            // �z�e���ڋq
            apMemberCardReg.setApCommon( apCommon );
            apMemberCardReg.setHotenaviContract( boolHotelCustom );
            apMemberCardReg.setResult( ret );
            apMemberCardReg.setDuplicationRegFlag( boolDuplicationRegFlag );
            apMemberCardReg.setCustomId( paramCustomId );
            apMemberCardReg.setErrorCode( errorCode );

            apHotelCustomerData.setApCommon( apCommon );

            request.setAttribute( "DtoApCommon", apCommon );
            request.setAttribute( "DtoApMemberCardReg", apMemberCardReg );
            request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );

            requestDispatcher = request.getRequestDispatcher( "MemberCardRegResult.jsp" );
            requestDispatcher.forward( request, response );

        }
        catch ( Exception exception )
        {
            Logging.error( "ActionHtOverWriteCustom:" + exception );
        }
        finally
        {
        }
    }
}
