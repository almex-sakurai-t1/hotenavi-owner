/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * �n�s�^�b�`����N���X
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataHotelAuth;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.owner.FormOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvList;
import jp.happyhotel.owner.LogicOwnerRsvRoomChange;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.touch.RsvList;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

import org.apache.commons.lang.StringUtils;

/**
 * �n�s�^�b�`>
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class HapiTouchRsv
{
    // �|�C���g�敪
    private static final int    POINT_KIND_YOYAKU     = 24;                       // �\��
    private static final int    AM12                  = 240000;                   // 0��00��
    private static final int    TOMORROW              = 1;
    private static final String RESULT_OK             = "OK";
    private static final String RESULT_NG             = "NG";
    private static final String RESULT_DENY           = "DENY";
    private static final String CONTENT_TYPE          = "text/xml; charset=UTF-8";
    private static final String ENCODE                = "UTF-8";
    private static final int    RSV_KIND              = 5;                        // ���X�҂��Ɨ��X������
    private static final int    CI_STATUS_NOT_DISPLAY = 3;                        // �^�b�`PC�ւ̔�\��

    private int                 errCode               = 0;

    /***
     * �G���[���b�Z�[�W�o�͏���
     * 
     * @param root ���[�g�m�[�h�l�[��
     * @param message �G���[���b�Z�[�W
     * @param response ���X�|���X
     */
    public void errorData(String root, String message, HttpServletResponse response)
    {
        GenerateXmlHapiTouchHotelInfo gxTouch;
        ServletOutputStream stream = null;

        try
        {
            stream = response.getOutputStream();

            gxTouch = new GenerateXmlHapiTouchHotelInfo();

            // xml�o�̓N���X�Ƀm�[�h���Z�b�g

            gxTouch.setResult( RESULT_DENY );
            gxTouch.setMessage( message );

            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * �\��̈ꗗ���擾����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvData(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic
        ServletOutputStream stream = null;
        Boolean ret = false;
        GenerateXmlHapiTouchRsvData gxRsvData;
        GenerateXmlHapiTouchRsvDataSub gxRsvDataSub;
        DataRsvReserve drr;
        DataUserFelica duf;

        String paramStartDate;
        String paramEndDate;
        String paramRsvNo;
        String paramKind;
        int nRsvDate = 0;
        int nEstArrivalTime = 0;
        int felicaFlag = 0;
        paramKind = request.getParameter( "kind" );

        drr = new DataRsvReserve();
        dsp0 = new FormOwnerRsvList();
        duf = new DataUserFelica();
        lgLPC = new LogicOwnerRsvList();
        gxRsvData = new GenerateXmlHapiTouchRsvData();

        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );
        paramRsvNo = request.getParameter( "rsvNo" );

        // �J�n���̃`�F�b�N
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }
        // �I�����̃`�F�b�N
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }
        //
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        //
        if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
        {
            paramKind = "0";
        }

        if ( Integer.parseInt( paramKind ) <= 0 )
        {
            paramKind = "5";
        }

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();

            // �z�e��ID�擾
            dsp0.setSelHotelID( hotelId );
            dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // �V�X�e�����t
            dsp0.setDateTo( DateEdit.getDate( 1 ) ); // �V�X�e�����t

            lgLPC.setDateFrom( paramStartDate );
            lgLPC.setDateTo( paramEndDate );
            lgLPC.setRsvNo( paramRsvNo );
            lgLPC.setHotelId( hotelId );
            ret = lgLPC.getData( dsp0, Integer.parseInt( paramKind ), 0, "ALL" );
            if ( ret != false )
            {
                int i = 0;
                for( i = 0 ; i < lgLPC.getIdList().size() ; i++ )
                {
                    gxRsvDataSub = new GenerateXmlHapiTouchRsvDataSub();
                    gxRsvDataSub.setHotelId( hotelId );
                    gxRsvDataSub.setRsvDate( lgLPC.getRsvDateList().get( i ) );
                    gxRsvDataSub.setRsvNo( lgLPC.getReserveNoList().get( i ) );
                    gxRsvDataSub.setPlanName( ReplaceString.replaceApiFull( lgLPC.getPlanNmList().get( i ) ) );
                    gxRsvDataSub.setArrivalTime( lgLPC.getEstTimeArrivalList().get( i ) );
                    gxRsvDataSub.setRoomNo( lgLPC.getSeqList().get( i ) );
                    gxRsvDataSub.setRoomName( this.getRoomName( hotelId, lgLPC.getSeqList().get( i ) ) );
                    gxRsvDataSub.setOption( lgLPC.getOptionList().get( i ) );
                    gxRsvDataSub.setUserId( lgLPC.getUserIdList().get( i ) );
                    gxRsvDataSub.setUserName( lgLPC.getUserNmList().get( i ) );

                    // �\��L�����Z����������
                    if ( lgLPC.getStatusValList().get( i ) == 3 && lgLPC.getNoShowList().get( i ) == 1 )
                    {
                        gxRsvDataSub.setStatus( "\\" + lgLPC.getStatusList().get( i ) );
                    }
                    else
                    {
                        gxRsvDataSub.setStatus( lgLPC.getStatusList().get( i ) );

                    }
                    gxRsvDataSub.setDispStatus( lgLPC.getDspList().get( i ) );
                    gxRsvDataSub.setStatusValue( lgLPC.getStatusValList().get( i ) );
                    gxRsvDataSub.setCharegeFlag( lgLPC.getNoShowList().get( i ) );

                    // ���[�U�̕R�t���󋵂��m�F
                    if ( duf != null )
                    {
                        duf = null;
                        duf = new DataUserFelica();
                    }
                    if ( duf.getData( lgLPC.getUserIdList().get( i ) ) != false )
                    {
                        if ( duf.getIdm().equals( "" ) == false && duf.getDelFlag() == 0 )
                        {
                            felicaFlag = 1;
                        }
                        else
                        {
                            felicaFlag = 0;
                        }
                    }
                    else
                    {
                        felicaFlag = 0;
                    }

                    gxRsvDataSub.setFelicaFlag( felicaFlag );

                    nRsvDate = lgLPC.getRsvDateValList().get( i );
                    nEstArrivalTime = lgLPC.getEstTimeArrivalValList().get( i );

                    // ������24�����߂����痂���̓��t�����ɕϊ�����
                    if ( nEstArrivalTime >= AM12 )
                    {
                        nEstArrivalTime -= AM12;
                        nRsvDate = DateEdit.addDay( nRsvDate, TOMORROW );
                    }

                    gxRsvDataSub.setRsvDateValue( lgLPC.getRsvDateValList().get( i ) );
                    gxRsvDataSub.setArrivalDateValue( nRsvDate );
                    gxRsvDataSub.setArrivalTimeValue( nEstArrivalTime );

                    // ���[�U���y�ѓd�b�ԍ����擾����
                    ret = false;
                    // �O�̃f�[�^���c���Ă����珉����
                    if ( drr != null )
                    {
                        drr = null;
                        drr = new DataRsvReserve();
                    }

                    // �V�\�� newRsvDB �ǉ����ځi2015.08.21�j
                    gxRsvDataSub.setPaidFlag( lgLPC.getPaymentList().get( i ) );
                    gxRsvDataSub.setTotalPrice( lgLPC.getChargeTotalList().get( i ) );

                    ret = drr.getData( hotelId, lgLPC.getReserveNoList().get( i ) );
                    if ( ret != false )
                    {
                        gxRsvDataSub.setTel( drr.getTel1() );
                    }
                    gxRsvData.addData( gxRsvDataSub );
                }
            }
            else
            {
                gxRsvData.setErrorCode( 0 );

            }

            // XML�̏o��
            String xmlOut = gxRsvData.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            response.setContentLength( xmlOut.getBytes().length );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * �\��̈ꗗ���擾����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvList(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret = true;
        boolean retUpd = true;
        RsvList rl = new RsvList();
        final int sendKind = 1; // �f�[�^�擾�v��
        int rsvCount = 0;
        int errorCount = 0;
        String paramLicenceKey = "";
        String paramVersion = "";

        paramLicenceKey = request.getParameter( "key" );
        paramVersion = request.getParameter( "version" );

        if ( paramVersion == null || !CheckString.numCheck( paramVersion ) )
        {
            paramVersion = "0";
        }
        DataHotelAuth dha;
        dha = new DataHotelAuth();

        retUpd = dha.getData( paramLicenceKey );
        if ( retUpd != false )
        {
            if ( dha.getRsvAddDate() == 0 )
            {
                dha.setRsvAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dha.setRsvAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            }
            dha.setRsvLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dha.setRsvLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dha.setRsvVersion( Integer.parseInt( paramVersion ) );

            dha.updateData( paramLicenceKey );
        }

        try
        {
            if ( rl.getData( hotelId, request, response ) != false )
            {
                ret = rl.sendToHost( hotelId, sendKind, "" );
                rsvCount = rl.getRsvListData().length;
                errorCount = rsvCount - rl.getSendCount();
                // rsvCount = rl.getSendCount();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        if ( ret )
        {
            GenerateXmlRsvList gxRsvList;
            ServletOutputStream stream = null;

            gxRsvList = new GenerateXmlRsvList();

            // ���X�|���X���Z�b�g
            try
            {
                // hh_hotel_master.pms_flag��1���Z�b�g
                DataHotelMaster hm = new DataHotelMaster();
                if ( hm.getData( hotelId ) )
                {
                    if ( hm.getPmsFlag() != 1 )
                    {
                        hm.setPmsFlag( 1 );
                        hm.updateData( hotelId );
                    }
                }

                stream = response.getOutputStream();
                // if ( ret != false )
                if ( errorCount == 0 )
                {
                    gxRsvList.setResult( RESULT_OK );
                    gxRsvList.setIdentifyNo( rl.getIdentifyNo() );
                    gxRsvList.setRsvCount( rsvCount );
                    gxRsvList.setErrorCount( errorCount );
                }
                else
                {
                    gxRsvList.setResult( RESULT_NG );
                    gxRsvList.setIdentifyNo( rl.getIdentifyNo() );
                    gxRsvList.setRsvCount( rsvCount );
                    gxRsvList.setErrorCount( errorCount );
                    gxRsvList.setErrorCode( 0 );
                }

                // XML�̏o��
                String xmlOut = gxRsvList.createXml();
                ServletOutputStream out = null;

                Logging.info( xmlOut );
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
            }
            finally
            {
                if ( stream != null )
                {
                    try
                    {
                        stream.close();
                    }
                    catch ( IOException e )
                    {
                        Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
                    }
                }
            }
        }
        else
        // ���M���Ȃ�����
        {
            GenerateXmlRsvList gxRsvList;
            ServletOutputStream stream = null;

            gxRsvList = new GenerateXmlRsvList();

            // ���X�|���X���Z�b�g
            try
            {
                stream = response.getOutputStream();
                gxRsvList.setResult( RESULT_NG );
                if ( request.getParameter( "IdentifyNo" ) == null )
                {
                    gxRsvList.setIdentifyNo( 0 );
                }
                else
                {
                    gxRsvList.setIdentifyNo( Integer.parseInt( request.getParameter( "IdentifyNo" ) ) );
                }
                gxRsvList.setRsvCount( 0 );
                gxRsvList.setErrorCount( rsvCount );

                // XML�̏o��
                String xmlOut = gxRsvList.createXml();
                ServletOutputStream out = null;

                Logging.info( xmlOut );
                out = response.getOutputStream();
                response.setContentType( CONTENT_TYPE );
                out.write( xmlOut.getBytes( ENCODE ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
            }
            finally
            {
                if ( stream != null )
                {
                    try
                    {
                        stream.close();
                    }
                    catch ( IOException e )
                    {
                        Logging.error( "[ActionHapiTouch getRsvList]Exception:" + e.toString() );
                    }
                }
            }
        }
    }

    /****
     * �\��ڍ׏����擾����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvDataDetail(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchRsvDataDetail gxRsvDetail;
        GenerateXmlHapiTouchRsvDataDetailOptionPrice gxRsvDetailOptionPrice;
        GenerateXmlHapiTouchRsvDataDetailImperativeOption gxRsvDetailInperativeOption;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        int nRsvDate = 0;
        int nEstArrivalTime = 0;

        gxRsvDetail = new GenerateXmlHapiTouchRsvDataDetail();

        paramRsvNo = request.getParameter( "rsvNo" );
        // �I�����̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // �t�H�[���ɃZ�b�g
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // �\��f�[�^���o
                logic.setFrm( frm );
                // ���уf�[�^�擾
                logic.getData( 2 );

                frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                if ( frm.getSeq() > 0 )
                {

                    // �\��o�^�������[�U�[�̃��[���A�h���X�擾
                    UserBasicInfo ubi = new UserBasicInfo();
                    ubi.getUserBasic( frm.getUserId() );
                    frm.setLoginUserId( frm.getUserId() );
                    if ( ubi.getUserInfo().getMailAddr().equals( "" ) == false )
                    {
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );
                    }
                    else if ( ubi.getUserInfo().getMailAddrMobile().equals( "" ) == false )
                    {
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddrMobile() );
                        frm.setMail( ubi.getUserInfo().getMailAddrMobile() );
                    }
                    frm.setLoginUserTel( ubi.getUserInfo().getTel1() );

                    // �\��f�[�^���Z�b�g
                    gxRsvDetail.setStatusValue( frm.getStatus() );

                    if ( frm.getStatus() == 1 )
                    {
                        gxRsvDetail.setStatus( "���X�҂�" );
                    }
                    else if ( frm.getStatus() == 2 )
                    {
                        gxRsvDetail.setStatus( "���X" );
                    }
                    else if ( frm.getStatus() == 3 )
                    {
                        if ( frm.getNoShow() == 1 )
                        {
                            gxRsvDetail.setStatus( "\\�L�����Z��" );
                        }
                        else
                        {
                            gxRsvDetail.setStatus( "�L�����Z��" );
                        }
                    }
                    gxRsvDetail.setCancelKind( frm.getCancelKind() );
                    gxRsvDetail.setRsvNo( frm.getRsvNo() );
                    gxRsvDetail.setHotelId( frm.getSelHotelId() );
                    gxRsvDetail.setHotelName( frm.getHotelNm() );
                    gxRsvDetail.setHotelAddr( frm.getHotelAddr() );
                    gxRsvDetail.setHotelTel( frm.getHotelTel() );
                    gxRsvDetail.setPlanName( ReplaceString.replaceApiFull( frm.getPlanNm() ) );
                    gxRsvDetail.setPlanPr( frm.getPlanPr() );
                    gxRsvDetail.setRsvDate( frm.getRsvDateView() );
                    gxRsvDetail.setRsvDateValue( frm.getRsvDate() );
                    gxRsvDetail.setRoomNo( frm.getSeq() );
                    gxRsvDetail.setRoomName( this.getRoomName( hotelId, frm.getSeq() ) );
                    Logging.info( frm.getRsvNo() + "-" + frm.getRsvSubNo(), "HapiTouchRsv.rsvDataDetail setRankName" );
                    gxRsvDetail.setRankName( this.getRankName( hotelId, frm.getRsvNo() ) );
                    gxRsvDetail.setArrivalTime( frm.getEstTimeArrivalView() );

                    nRsvDate = frm.getRsvDate();
                    nEstArrivalTime = frm.getEstTimeArrival();

                    // ������24�����߂����痂���̓��t�����ɕϊ�����
                    if ( nEstArrivalTime >= AM12 )
                    {
                        nEstArrivalTime -= AM12;
                        nRsvDate = DateEdit.addDay( nRsvDate, TOMORROW );
                    }
                    // �\����t�A�����������Z�b�g

                    gxRsvDetail.setArrivalDateValue( nRsvDate );
                    gxRsvDetail.setArrivalTimeValue( nEstArrivalTime );

                    // ����
                    gxRsvDetail.setRoomPrice( frm.getBasicTotalView() );
                    gxRsvDetail.setAdultNum( frm.getAdultNumView() );
                    gxRsvDetail.setChildNum( frm.getChildNumView() );

                    // �ʏ�I�v�V��������
                    int i = 0;
                    for( i = 0 ; i < frm.getOptInpMaxQuantityList().size() ; i++ )
                    {
                        gxRsvDetailOptionPrice = new GenerateXmlHapiTouchRsvDataDetailOptionPrice();
                        gxRsvDetailOptionPrice.setOptionName( frm.getOptNmList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionUnitPrice( frm.getOptUnitPriceViewList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionMaxQuantity( frm.getOptInpMaxQuantityList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionTotal( frm.getOptChargeTotalList().get( i ) );
                        gxRsvDetailOptionPrice.setOptionRemarks( frm.getOptRemarksList().get( i ) );

                        gxRsvDetail.setOptionPrice( gxRsvDetailOptionPrice );
                    }
                    // �J��Ԃ�

                    gxRsvDetail.setTotalPrice( frm.getChargeTotalView() );

                    boolean isNumber = false;
                    if ( frm.getOptNmImpList().size() > 1 )
                    {
                        if ( frm.getOptNumberList().get( frm.getOptNmImpList().size() - 1 ) > 1 )
                        {
                            isNumber = true;
                        }
                    }
                    int beforeNumber = -1;
                    // �K�{�I�v�V�������擾
                    if ( frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
                    {
                        for( i = 0 ; i < frm.getOptNmImpList().size() ; i++ )
                        {
                            gxRsvDetailInperativeOption = new GenerateXmlHapiTouchRsvDataDetailImperativeOption();

                            String optName = "";
                            if ( beforeNumber != frm.getOptNumberList().get( i ) && isNumber )
                            {
                                optName = "�y" + frm.getOptNumberList().get( i ) + "�l�ځz\n";
                            }
                            optName += frm.getOptNmImpList().get( i );
                            gxRsvDetailInperativeOption.setImperativeOptionName( optName );

                            String optSubName = frm.getOptSubNmImpList().get( i );

                            if ( frm.getOptQuantityImpList().get( i ) > 1 )
                            {
                                optSubName += "�~" + frm.getOptQuantityImpList().get( i );
                            }
                            gxRsvDetailInperativeOption.setImperativeOptionSubName( optSubName );

                            gxRsvDetail.setImperativeOption( gxRsvDetailInperativeOption );

                            beforeNumber = frm.getOptNumberList().get( i );
                        }
                    }
                    else
                    {
                        // lvj�\��ŘA���̂Ƃ��A���t���Ƃ̗������o��
                        if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                        {
                            String[][] eachAmount = OwnerRsvCommon.getRsvEachAmountArr( frm.getSelHotelId(), frm.getRsvNo() );
                            int len = eachAmount.length;
                            for( int index = 0 ; index < len ; index++ )
                            {
                                gxRsvDetailInperativeOption = new GenerateXmlHapiTouchRsvDataDetailImperativeOption();
                                gxRsvDetailInperativeOption.setImperativeOptionName( eachAmount[index][0] );
                                gxRsvDetailInperativeOption.setImperativeOptionSubName( eachAmount[index][1] );
                                gxRsvDetail.setImperativeOption( gxRsvDetailInperativeOption );
                            }
                        }
                    }

                    // �`�F�b�N�C���J�n���Ԃ��Z�b�g
                    gxRsvDetail.setCiTimeFrom( frm.getCiTime() );
                    gxRsvDetail.setCiTimeFromValue( frm.getCiTimeFrom() );
                    // �`�F�b�N�C���I�����Ԃ��Z�b�g
                    gxRsvDetail.setCiTimeTo( frm.getCiTimeToView() );
                    gxRsvDetail.setCiTimeToValue( frm.getCiTimeTo() );
                    // �`�F�b�N�A�E�g���Ԃ��Z�b�g
                    gxRsvDetail.setCoTime( frm.getCoTimeView() );
                    gxRsvDetail.setCar( frm.getParkingUsed() );
                    gxRsvDetail.setUserId( frm.getUserId() );
                    gxRsvDetail.setMailAddr( frm.getMail() );
                    gxRsvDetail.setMailReminder( frm.getReminderView() );
                    gxRsvDetail.setUserName( frm.getName() );
                    gxRsvDetail.setUserNameKana( "" );
                    gxRsvDetail.setZipCode( frm.getZip() );
                    gxRsvDetail.setAddress( frm.getAddress() );
                    gxRsvDetail.setTel( frm.getTel() );
                    gxRsvDetail.setDemands( frm.getDemandsView() );
                    if ( !frm.getRemarksView().equals( "" ) )
                    {
                        gxRsvDetail.setRemarks( frm.getQuestion() + "\n�񓚁u" + frm.getRemarksView() + "�v" );
                    }
                    gxRsvDetail.setCancelPolicy( frm.getCahcelPolicy() );
                    gxRsvDetail.setPaidCredit( frm.getPayCredit() );
                    gxRsvDetail.setPaidMile( frm.getUsedMile() );

                    if ( frm.getExtFlag() != ReserveCommon.EXT_HAPIHOTE ) // �O���\��̏ꍇ�́Aci_status==3���ǂݍ��ݑΏۂƂ���
                    {
                        gxRsvDetail.setCiCode( this.getCheckInCode( hotelId, frm.getRsvNo(), "0,1,3,4" ) );
                    }
                    else
                    {
                        gxRsvDetail.setCiCode( this.getCheckInCode( hotelId, frm.getRsvNo() ) );
                    }
                }
                else
                {
                    // �\��ԍ����Ȃ�
                    gxRsvDetail.setErrorCode( HapiTouchErrorMessage.ERR_20202 );
                }
            }
            else
            {
                // �\��ԍ��G���[
                gxRsvDetail.setErrorCode( HapiTouchErrorMessage.ERR_20201 );
            }
            // XML�̏o��
            String xmlOut = gxRsvDetail.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvDataDetail xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvDataDetail]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvDataDetail]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * �\�񕔉��ԍ����Z�b�g����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void getRsvRoom(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchGetRsvRoom gxRsvRoom;
        GenerateXmlHapiTouchGetRsvRoomNo gxRsvRoomNo;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;

        gxRsvRoom = new GenerateXmlHapiTouchGetRsvRoom();

        paramRsvNo = request.getParameter( "rsvNo" );
        // �I�����̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // �t�H�[���ɃZ�b�g
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // �\��f�[�^���o
                logic.setFrm( frm );
                // ���уf�[�^�擾
                logic.getData( 2 );
                // �\�񖢊��蓖�ĕ����݂̂��擾
                getSeqList( frm );

                frm.setMode( ReserveCommon.MODE_RAITEN );
                frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                if ( frm.getSeq() > 0 )
                {
                    for( int i = 0 ; i < frm.getSeqList().size() ; i++ )
                    {
                        gxRsvRoomNo = new GenerateXmlHapiTouchGetRsvRoomNo();
                        gxRsvRoomNo.setRoom( frm.getSeqList().get( i ) );
                        gxRsvRoomNo.setRoomName( this.getRoomName( hotelId, frm.getSeqList().get( i ) ) );
                        gxRsvRoom.setData( gxRsvRoomNo );
                    }
                    gxRsvRoom.setReserveRoom( frm.getSeq() );
                }
                else
                {
                    // �\��ԍ����Ȃ�
                    gxRsvRoom.setErrorCode( HapiTouchErrorMessage.ERR_20302 );
                }
            }
            else
            {
                // �p�����[�^�G���[
                gxRsvRoom.setErrorCode( HapiTouchErrorMessage.ERR_20301 );
            }

            // XML�̏o��
            String xmlOut = gxRsvRoom.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvRoom xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getRsvRoom]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getRsvRoom]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * �\�񗈓X�m�F���s��
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvFix(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        Boolean ret = false;
        Boolean fixResult = false;
        GenerateXmlHapiTouchRsvFix gxRsvFix;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        String paramRoomNo;
        String paramRoomNoEx;

        String roomHost = "";
        int roomSeq = 0;

        String paramVerifyCi;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        Boolean updateCi = true;
        boolean retVerify = false;
        String userId = "";

        // �`�F�b�N�C���f�[�^�쐬�̂��߂̕ϐ�
        HotelCi hc = new HotelCi();
        UserPointPay upp = new UserPointPay();
        int ciCode = 0;
        DataRsvReserve dhrr = new DataRsvReserve();

        gxRsvFix = new GenerateXmlHapiTouchRsvFix();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramRoomNo = request.getParameter( "roomNo" ); // �z�X�g�ʐM�������� hh_hotel_room_more.room_name_host ����
        paramRoomNoEx = request.getParameter( "roomNoEx" ); // hh_hotel_room_more.seq ����
        paramVerifyCi = request.getParameter( "verifyCi" );

        // �I�����̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramRoomNo == null) || (paramRoomNo.equals( "" ) != false) )
        {
            paramRoomNo = "";
        }
        else
        {
            roomHost = paramRoomNo;
        }
        if ( (paramRoomNoEx == null) || (paramRoomNoEx.equals( "" ) != false) || CheckString.numCheck( paramRoomNoEx ) == false )
        {
            paramRoomNoEx = "0";
        }
        else
        {
            roomSeq = Integer.parseInt( paramRoomNoEx );
        }

        if ( (paramVerifyCi == null) || (paramVerifyCi.equals( "" ) != false) || CheckString.numCheck( paramVerifyCi ) == false )
        {
            paramVerifyCi = "0";
        }

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // �t�H�[���ɃZ�b�g
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // �\��f�[�^���o
                logic.setFrm( frm );
                // ���уf�[�^�擾
                logic.getData( 2 );

                frm.setMode( ReserveCommon.MODE_RAITEN );
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                frm.setRoomNo( frm.getSeq() );
                frm.setPlanType( logic.getFrm().getPlanType() );

                ret = hc.getCheckInBeforeData( hotelId, frm.getUserId(), paramRsvNo, roomHost );

                Logging.info( "rsvFix Start :paramVerifyCi:" + paramVerifyCi );
                Logging.info( "rsvFix Start :ret:" + ret );

                // �����[�U��24���Ԉȓ��`�F�b�N�C���f�[�^������ꍇ�͏������Ȃ�
                if ( ret == false )
                {

                    if ( paramVerifyCi.equals( "0" ) == false )
                    {
                        if ( hc.getData( hotelId, Integer.parseInt( paramVerifyCi ) ) != false )
                        {
                            if ( frm.getUserId().equals( hc.getHotelCi().getUserId() ) != false )
                            {
                                retVerify = true;
                            }
                            else
                            {
                                retVerify = false;
                            }

                        }
                        else
                        {
                            retVerify = true;
                        }
                    }
                    else
                    {
                        retVerify = true;
                    }

                    if ( frm.getSeq() > 0 && retVerify != false )
                    {
                        // �\��o�^�������[�U�[�̃��[���A�h���X�擾
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        if ( ubi.getUserInfo().getMailAddr().equals( "" ) == false )
                        {
                            frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                            frm.setMail( ubi.getUserInfo().getMailAddr() );
                        }
                        else
                        {
                            frm.setLoginUserMail( ubi.getUserInfo().getMailAddrMobile() );
                            frm.setMail( ubi.getUserInfo().getMailAddrMobile() );
                        }
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );

                        // ���N�G�X�g�Ŏ擾���������ԍ����Z�b�g
                        if ( roomSeq == 0 )
                        {
                            frm.setSeq( getSeq( hotelId, roomHost ) );
                        }
                        else
                        {
                            frm.setSeq( roomSeq );
                        }
                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;

                        // �z�e���̒񋟋敪�擾
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );

                        // �����X�m�F
                        int statusOld = frm.getStatus();
                        frm = htRsvSub.execRaiten( frm, frm.getStatus() );

                        // �G���[���莞
                        if ( frm.getErrMsg().trim().length() != 0 && statusOld != ReserveCommon.RSV_STATUS_ZUMI ) // ���X�ς݂̓G���[�Ƃ��Ȃ�
                        {
                            errCode = HapiTouchErrorMessage.ERR_20403;
                            Logging.error( frm.getErrMsg() );
                        }
                        else
                        {
                            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                            frm.setMode( "" );
                            fixResult = true;
                        }

                        if ( fixResult != false )
                        {
                            // ���m��̃f�[�^��24���Ԉȓ��ɂ�������o�^���Ȃ��B
                            ret = hc.getCheckInBeforeData( hotelId, frm.getUserId(), paramRsvNo );

                            if ( ret == false )
                            {
                                if ( hc.isRaiten( hotelId, paramRsvNo ) ) // �����\��ԍ��ŗ��X�ς������ꍇ�͂��Ƃ��Ƃ̃f�[�^�𖳌��ɂ���
                                {
                                    hc.getHotelCi().setCiStatus( CI_STATUS_NOT_DISPLAY );
                                    hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                    hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                    hc.getHotelCi().updateData( hotelId, hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                                }

                                // �`�F�b�N�C���f�[�^�o�^
                                hc = hc.registCiData( frm.getUserId(), hotelId );
                                ciCode = hc.getHotelCi().getSeq();
                                if ( ciCode > 0 )
                                {
                                    ret = true;
                                }
                                else
                                {
                                    ret = false;
                                }
                            }
                            else
                            // ���m��̃f�[�^��24���Ԉȓ��ɂ�����
                            {
                                ciCode = hc.getHotelCi().getSeq();
                                ret = false;
                            }
                            if ( errCode == 0 )
                            {
                                // �n�s�z�e����ȊO���\�񗈓X�����ꍇ�́A�`�F�b�N�C���f�[�^�𖳌������Ƃ���
                                if ( ubi.isLvjUser( frm.getUserId() ) )
                                {
                                    hc.getHotelCi().setCiStatus( 4 );
                                    hc.getHotelCi().setExtUserFlag( 1 );
                                }
                                else
                                {
                                    hc.getHotelCi().setExtUserFlag( 0 );
                                }
                                // ���������o�^�ς݂ł���Ύ}�Ԓǉ��ő}���A�o�^����Ă��Ȃ���΍X�V
                                if ( hc.getHotelCi().getRoomNo().compareTo( "" ) != 0 )
                                {
                                    updateCi = false;
                                }
                                else
                                {
                                    updateCi = true;
                                }
                                if ( roomHost.equals( "" ) )
                                {
                                    hc.getHotelCi().setRoomNo( this.getRoomName( hotelId, frm.getSeq() ) );
                                }
                                else
                                {
                                    hc.getHotelCi().setRoomNo( roomHost );
                                }
                                // �\��ԍ����Z�b�g
                                hc.getHotelCi().setRsvNo( paramRsvNo );

                                // �\����͎��g�p�}�C�����Z�b�g
                                if ( dhrr.getData( hotelId, paramRsvNo ) != false )
                                {
                                    hc.getHotelCi().setUsePoint( dhrr.getUsedMile() );
                                    if ( dhrr.getUsedMile() != 0 )
                                    {
                                        hc.getHotelCi().setUseDate( dhrr.getAcceptDate() );
                                        hc.getHotelCi().setUseTime( dhrr.getAcceptTime() );
                                    }
                                }

                                // �\��̗��p�������Z�b�g���Ă���
                                // hc.getHotelCi().setAmount( frm.getChargeTotal() + frm.getOptionChargeTotal() );
                                // �`�F�b�N�C���f�[�^���X�V�܂��̓C���T�[�g
                                if ( updateCi != false )
                                {
                                    // �X�V
                                    ret = hc.getHotelCi().updateData( hotelId, hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
                                }
                                else
                                {
                                    // false�Ȃ�Ύ}�ԍ���ǉ�����K�v������
                                    if ( ret == false )
                                    {
                                        hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                                    }
                                    ret = hc.getHotelCi().insertData();
                                }
                            }
                            if ( errCode == 0 )
                            {
                                gxRsvFix.setResult( RESULT_OK );
                                gxRsvFix.setRsvRoomNo( roomHost );
                                gxRsvFix.setStatus( this.getRsvStatus( frm.getStatus() ) );
                                gxRsvFix.setStatusValue( frm.getStatus() );
                                if ( ret != false )
                                {
                                    gxRsvFix.setCiResult( RESULT_OK );
                                    // �����ɗ\��}�C���o�^������ǉ�
                                    // �n�s�z�e�\��̂Ƃ�����
                                    if ( frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
                                    {
                                        if ( statusOld != ReserveCommon.RSV_STATUS_ZUMI )
                                        {
                                            errCode = htRsvSub.setRsvPoint( hc );
                                        }
                                    }
                                }
                                else
                                {
                                    gxRsvFix.setCiResult( RESULT_NG );
                                }
                                gxRsvFix.setPoint( upp.getNowPoint( frm.getUserId(), false ) );
                                gxRsvFix.setCiCode( ciCode );
                                gxRsvFix.setCiDate( hc.getHotelCi().getCiDate() );
                                gxRsvFix.setCiTime( hc.getHotelCi().getCiTime() );
                                gxRsvFix.setUserId( Integer.toString( hc.getHotelCi().getUserSeq() ) );
                                gxRsvFix.setAmountRate( hc.getHotelCi().getAmountRate() );
                                gxRsvFix.setRoomNo( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setRoomName( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setUsePoint( hc.getHotelCi().getUsePoint() );
                                gxRsvFix.setAmount( hc.getHotelCi().getAmount() );
                                gxRsvFix.setSlipNo( hc.getHotelCi().getSlipNo() );
                                gxRsvFix.setRsvNo( frm.getRsvNo() );
                                gxRsvFix.setUserType( hc.getHotelCi().getUserType() );
                                gxRsvFix.setPaidCredit( frm.getPayCredit() );
                                gxRsvFix.setPaidMile( frm.getUsedMile() );
                                gxRsvFix.setErrorCode( errCode );
                                if ( !hc.getHotelCi().getRsvNo().equals( "" ) )
                                {
                                    if ( hc.getHotelCi().getExtUserFlag() == 1 )
                                    {
                                        if ( DataRsvReserve.getExtFlag( hotelId, hc.getHotelCi().getRsvNo() ) == 1 )
                                        {
                                            gxRsvFix.setUserId( "LIJ" );
                                            // gxRsvFix.setUserType( 2 );
                                        }
                                    }
                                }
                            }
                            else
                            {
                                gxRsvFix.setResult( "NG" );
                                gxRsvFix.setStatus( "���X" );
                                gxRsvFix.setStatusValue( 2 );
                                gxRsvFix.setCiResult( RESULT_NG );
                                gxRsvFix.setCiCode( ciCode );
                                gxRsvFix.setCiDate( hc.getHotelCi().getCiDate() );
                                gxRsvFix.setCiTime( hc.getHotelCi().getCiTime() );
                                gxRsvFix.setUserId( Integer.toString( hc.getHotelCi().getUserSeq() ) );
                                gxRsvFix.setAmountRate( hc.getHotelCi().getAmountRate() );
                                gxRsvFix.setRoomNo( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setRoomName( hc.getHotelCi().getRoomNo() );
                                gxRsvFix.setUsePoint( hc.getHotelCi().getUsePoint() );
                                gxRsvFix.setAmount( hc.getHotelCi().getAmount() );
                                gxRsvFix.setSlipNo( hc.getHotelCi().getSlipNo() );
                                gxRsvFix.setRsvNo( frm.getRsvNo() );
                                gxRsvFix.setUserType( hc.getHotelCi().getUserType() );
                                gxRsvFix.setPaidCredit( frm.getPayCredit() );
                                gxRsvFix.setPaidMile( frm.getUsedMile() );
                                gxRsvFix.setErrorCode( errCode );
                            }
                        }
                        else
                        {
                            // ���ʂ��Z�b�g
                            gxRsvFix.setResult( "NG" );
                            gxRsvFix.setPoint( upp.getNowPoint( frm.getUserId(), false ) );
                            gxRsvFix.setStatus( this.getRsvStatus( frm.getStatus() ) );
                            gxRsvFix.setStatusValue( frm.getStatus() );
                            gxRsvFix.setErrorCode( errCode );
                            gxRsvFix.setRsvNo( paramRsvNo );
                            gxRsvFix.setRoomNo( roomHost );
                        }

                        userId = frm.getUserId();
                    }
                    else
                    {
                        if ( retVerify == false )
                        {
                            errCode = HapiTouchErrorMessage.ERR_20400;
                        }
                        else
                        {
                            // �\��ԍ����Ȃ�
                            errCode = HapiTouchErrorMessage.ERR_20402;
                        }

                        gxRsvFix.setResult( "NG" );
                        gxRsvFix.setStatus( "" );
                        gxRsvFix.setStatusValue( 0 );
                        gxRsvFix.setRsvNo( paramRsvNo );
                        gxRsvFix.setRoomNo( roomHost );
                        // �\��ԍ����Ȃ�
                        gxRsvFix.setErrorCode( errCode );
                    }
                }
                else
                {
                    errCode = HapiTouchErrorMessage.ERR_20400;
                    gxRsvFix.setResult( "NG" );
                    gxRsvFix.setStatus( "" );
                    gxRsvFix.setStatusValue( 0 );
                    gxRsvFix.setRsvNo( paramRsvNo );
                    gxRsvFix.setRoomNo( roomHost );
                    gxRsvFix.setErrorCode( errCode );
                }
            }
            else
            {
                // �\��ԍ��G���[
                gxRsvFix.setErrorCode( -1 );
            }

            StringBuffer requestUrl = request.getRequestURL();
            String requestUrlStr = requestUrl.toString();
            int errorCode = 0;
            if ( HotelIp.getFrontIp( hotelId ) != "" && requestUrlStr.indexOf( "happyhotel.jp" ) > 0 )
            {
                errorCode = HapiTouchErrorMessage.ERR_20407;
            }
            if ( errorCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\��f�[�^�̍ŐV��Ԃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( hotelId, 0, 0, 0, paramRsvNo, 0 ) != false )
                    {
                        rl.sendToHost( hotelId );
                    }
                }
            }

            if ( errorCode != 0 )
            // �^�b�`PC����I�t���C���̓d���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                Logging.info( "rsvFix Error:" + errorCode, "rsvFix Error" );
                DataHotelBasic dhb;
                dhb = new DataHotelBasic();
                String hotenaviId = "";
                String hotelName = "";
                // �z�e�i�rID���擾����
                if ( dhb.getData( hotelId ) != false )
                {
                    hotenaviId = dhb.getHotenaviId();
                    hotelName = dhb.getName();
                }
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( hotelName );
                daeh.setHotenaviId( hotenaviId );
                daeh.setRoomName( roomHost );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( ciCode );
                daeh.setUserId( userId );
                daeh.setReserveNo( paramRsvNo );
                daeh.insertData();
                errorCode = 0;
            }
            // if ( errorCode == 0 )
            // {
            // // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\�񂪂��������Ƃ��z�X�g�ɓ`����
            // if ( !HotelIp.getFrontIp( hotelId ).equals( "" ) )
            // {
            // int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            // // 5���ȑO�̏ꍇ�͑O���Ƃ���
            // if ( 50000 > nowTime )
            // {
            // nowTime = nowTime + 240000;
            // nowDate = DateEdit.addDay( nowDate, -1 );
            // }
            // if ( nRsvDate == nowDate// �\����t�������̏ꍇ
            // || (nRsvDate == DateEdit.addDay( nowDate, 1 ) && nEstTimeArrival < nowTime) /* �\����t�������ŗ��X�\�莞����24���Ԉȓ��̏ꍇ */)
            // {
            // RsvList rl = new RsvList();
            // if ( rl.getData( hotelId, 0, 0, 0, paramRsvNo ) != false )
            // {
            // rl.sendToHost( hotelId );
            // }
            // }
            // }
            // }
            //
            // XML�̏o��
            String xmlOut = gxRsvFix.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvFix xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvFix]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvFix]Exception:" + e.toString() );
                }
            }

        }
    }

    /****
     * �\��L�����Z�����s��
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlHapiTouchRsvCancel gxRsvCancel;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        String paramNoShow;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();

        // �`�F�b�N�C���f�[�^�쐬�̂��߂̕ϐ�

        gxRsvCancel = new GenerateXmlHapiTouchRsvCancel();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramNoShow = request.getParameter( "noShow" );
        // �I�����̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramNoShow == null) || (paramNoShow.equals( "" ) != false) || CheckString.numCheck( paramNoShow ) == false )
        {
            paramNoShow = "";
        }

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // �t�H�[���ɃZ�b�g
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // �\��f�[�^���o
                logic.setFrm( frm );
                // ���уf�[�^�擾
                logic.getData( 2 );

                if ( frm.getExtFlag() != ReserveCommon.EXT_OTA )
                {
                    frm.setMode( ReserveCommon.MODE_CANCEL );
                    frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                    frm.setErrMsg( "" );
                    frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                    if ( frm.getSeq() > 0 )
                    {
                        // �\��o�^�������[�U�[�̃��[���A�h���X�擾
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );
                        // ���N�G�X�g�Ŏ擾�����m�[�V���E�t���O���Z�b�g
                        frm.setNoShow( Integer.parseInt( paramNoShow ) );
                        // �L�����Z���t���O�𗧂Ă�
                        frm.setCancelCheck( 1 );

                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;

                        // �z�e���̒񋟋敪�擾
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );
                        frm.setPlanType( logic.getFrm().getPlanType() );

                        // ���L�����Z��
                        frm = htRsvSub.execCancel( frm, frm.getStatus() );

                        if ( frm.getErrMsg().trim().length() != 0 )
                        {
                            errCode = HapiTouchErrorMessage.ERR_20503;
                            gxRsvCancel.setStatus( this.getRsvStatus( frm.getStatus() ) );
                            gxRsvCancel.setStatusValue( frm.getStatus() );
                        }
                        else
                        {
                            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                            frm.setMode( "" );
                            gxRsvCancel.setResult( RESULT_OK );
                            gxRsvCancel.setStatus( this.getRsvStatus( frm.getStatus() ) );
                            gxRsvCancel.setStatusValue( frm.getStatus() );
                            gxRsvCancel.setRsvNo( frm.getRsvNo() );
                        }
                    }
                    else
                    {
                        // �\�񕔉��������Ȃ�
                        errCode = HapiTouchErrorMessage.ERR_20502;
                        gxRsvCancel.setStatusValue( -99 );
                    }

                }
                else
                {
                    // OTA����̗\��
                    errCode = HapiTouchErrorMessage.ERR_20507;
                    gxRsvCancel.setStatusValue( -99 );
                }
            }
            else
            {
                // �\��ԍ��G���[
                errCode = HapiTouchErrorMessage.ERR_20501;
                gxRsvCancel.setStatusValue( -99 );
            }

            if ( errCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\�񂪂��������Ƃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5���ȑO�̏ꍇ�͑O���Ƃ���
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getRsvDate() == nowDate// �\����t�������̏ꍇ
                            || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* �\����t�������ŗ��X�\�莞����24���Ԉȓ��̏ꍇ */)
                    {
                        RsvList rl = new RsvList();
                        if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                        {
                            rl.sendToHost( hotelId );
                        }
                    }
                }
            }
            else
            {
                gxRsvCancel.setResult( RESULT_NG );
                gxRsvCancel.setRsvNo( paramRsvNo );
                gxRsvCancel.setErrorCode( errCode );
            }

            // XML�̏o��
            String xmlOut = gxRsvCancel.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvCancel xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvCancel]Exception:" + e.toString() );
                }
            }

        }
    }

    /***
     * �\��X�e�[�^�X�����擾
     * 
     * @param status
     * @return ���X�|���X
     */
    public String getRsvStatus(int status)
    {

        String statusWord = "";
        if ( status == ReserveCommon.RSV_STATUS_UKETUKE )
        {
            statusWord = "���X�҂�";
        }
        else if ( status == ReserveCommon.RSV_STATUS_ZUMI )
        {
            statusWord = "���X";
        }
        else if ( status == ReserveCommon.RSV_STATUS_CANCEL )
        {
            statusWord = "�L�����Z��";
        }
        return statusWord;
    }

    /****
     * ���������ύX���s��
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g���e
     * @param response ���X�|���X
     */
    public void rsvModifyArrvialTime(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        Boolean ret = false;

        GenerateXmlHapiTouchRsvModifyArrivalTime gxRsvModifyArrivalTime;
        String paramRsvNo;
        String paramArrivalTime;
        DataRsvReserve drr = new DataRsvReserve();
        int nEstTimeArrival = 0;
        int nEstTimeArrivalBefore = 0;
        int nRsvDate = 0;

        gxRsvModifyArrivalTime = new GenerateXmlHapiTouchRsvModifyArrivalTime();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramArrivalTime = request.getParameter( "arrivalTime" );
        // �I�����̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramArrivalTime == null) || (paramArrivalTime.equals( "" ) != false) || CheckString.numCheck( paramArrivalTime ) == false )
        {
            paramArrivalTime = "0";
        }

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();
            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                ret = drr.getData( hotelId, paramRsvNo );
                if ( ret != false )
                {
                    if ( drr.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE )
                    {
                        nEstTimeArrivalBefore = drr.getEstTimeArrival();
                        // �\��ԍ����Z�b�g���A�b�v�f�[�g
                        drr.setEstTimeArrival( Integer.parseInt( paramArrivalTime ) );
                        ret = drr.updateEstTimeArrivalData( hotelId, paramRsvNo, ReserveCommon.SCHEMA_NEWRSV );
                        if ( ret != false )
                        {
                            gxRsvModifyArrivalTime.setResult( "OK" );
                            drr = null;
                            drr = new DataRsvReserve();
                            drr.getData( hotelId, paramRsvNo );
                            nEstTimeArrival = drr.getEstTimeArrival();
                            nRsvDate = drr.getReserveDate();
                            // ������24�����߂����痂���̓��t�����ɕϊ�����
                            if ( nEstTimeArrival >= AM12 )
                            {
                                nEstTimeArrival -= AM12;
                                nRsvDate = DateEdit.addDay( drr.getReserveDate(), TOMORROW );
                            }

                            gxRsvModifyArrivalTime.setArraivalDateValue( nRsvDate );
                            gxRsvModifyArrivalTime.setArraivalTime( ReserveCommon.getArrivalTimeView( drr.getEstTimeArrival() ) );
                            gxRsvModifyArrivalTime.setArraivalTimeValue( nEstTimeArrival );
                            gxRsvModifyArrivalTime.setRsvDateValue( drr.getReserveDate() );
                            /* 1016�d���p�Ɍ��ɖ߂��Ă��� */
                            nEstTimeArrival = drr.getEstTimeArrival();
                            nRsvDate = drr.getReserveDate();
                        }
                        else
                        {
                            // �\��f�[�^�X�V���s
                            errCode = HapiTouchErrorMessage.ERR_20604;
                            gxRsvModifyArrivalTime.setResult( "NG" );
                            gxRsvModifyArrivalTime.setErrorCode( errCode );
                        }
                    }
                    else
                    {
                        // �\��f�[�^�Ȃ�
                        errCode = HapiTouchErrorMessage.ERR_20602;
                        gxRsvModifyArrivalTime.setResult( "NG" );
                        gxRsvModifyArrivalTime.setErrorCode( errCode );
                    }
                }
                else
                {
                    // �\��f�[�^�Ȃ�
                    errCode = HapiTouchErrorMessage.ERR_20602;
                    gxRsvModifyArrivalTime.setResult( "NG" );
                    gxRsvModifyArrivalTime.setErrorCode( errCode );
                }
            }
            else
            {
                // �\��ԍ��G���[
                errCode = HapiTouchErrorMessage.ERR_20601;
                gxRsvModifyArrivalTime.setResult( "NG" );
                gxRsvModifyArrivalTime.setErrorCode( errCode );
            }
            if ( errCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\�񂪂��������Ƃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5���ȑO�̏ꍇ�͑O���Ƃ���
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( nRsvDate == nowDate// �\����t�������̏ꍇ
                            || (nRsvDate == DateEdit.addDay( nowDate, 1 ) && nEstTimeArrivalBefore < nowTime /* �\����t�������ŕύX�O�̗��X�\�莞����24���Ԉȓ��̏ꍇ */)
                            || (nRsvDate == DateEdit.addDay( nowDate, 1 ) && nEstTimeArrival < nowTime) /* �\����t�������ŗ��X�\�莞����24���Ԉȓ��̏ꍇ */)
                    {
                        RsvList rv = new RsvList();
                        if ( rv.getData( hotelId, 0, 0, 0, paramRsvNo, 0 ) != false )
                        {
                            rv.sendToHost( hotelId );
                        }
                    }
                }
            }
            // XML�̏o��
            String xmlOut = gxRsvModifyArrivalTime.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvModifyArrvialTime xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvModifyArrvialTime]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvModifyArrvialTime]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * �\��L�����Z�����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g���e
     * @param response ���X�|���X
     */
    public void rsvUndoCancel(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        boolean undoResult = false;
        GenerateXmlHapiTouchRsvUndoCancel gxRsvUndoCancel;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();

        gxRsvUndoCancel = new GenerateXmlHapiTouchRsvUndoCancel();

        paramRsvNo = request.getParameter( "rsvNo" );

        // �\��ԍ��̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        try
        {
            stream = response.getOutputStream();

            if ( paramRsvNo.compareTo( "" ) != 0 )
            {
                // �t�H�[���ɃZ�b�g
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // �\��f�[�^���o
                logic.setFrm( frm );
                // ���уf�[�^�擾
                logic.getData( 2 );

                frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                // �����ԍ����o�^����Ă���A�L�����Z���̃X�e�[�^�X�̃f�[�^�̂ݑΏۂƂ���
                if ( frm.getSeq() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_CANCEL )
                {
                    // �\��o�^�������[�U�[�̃��[���A�h���X�擾
                    UserBasicInfo ubi = new UserBasicInfo();
                    ubi.getUserBasic( frm.getUserId() );
                    frm.setLoginUserId( frm.getUserId() );
                    frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                    frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                    frm.setMail( ubi.getUserInfo().getMailAddr() );
                    frm.setPlanType( logic.getFrm().getPlanType() );

                    DataRsvPlan dataPlan = new DataRsvPlan();
                    int offerKind;

                    // �z�e���̒񋟋敪�擾
                    dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                    offerKind = dataPlan.getOfferKind();
                    frm.setOfferKind( offerKind );

                    // ���L�����Z��
                    frm = htRsvSub.execUndoCancel( frm, frm.getStatus() );

                    // �G���[���莞
                    if ( frm.getErrMsg().trim().length() != 0 )
                    {
                        errCode = HapiTouchErrorMessage.ERR_20704;
                    }
                    else
                    {
                        undoResult = true;
                    }
                }
                else
                {
                    if ( frm.getSeq() <= 0 )
                    {
                        // �f�[�^�Ȃ�
                        errCode = HapiTouchErrorMessage.ERR_20702;
                    }
                    else
                    {
                        // �X�e�[�^�X���L�����Z���ȊO
                        errCode = HapiTouchErrorMessage.ERR_20703;
                    }
                }
            }
            // �p�����[�^�ُ�
            else
            {
                errCode = HapiTouchErrorMessage.ERR_20701;
                gxRsvUndoCancel.setResult( "NG" );
            }

            if ( undoResult != false )
            {
                gxRsvUndoCancel.setResult( "OK" );
            }
            else
            {
                gxRsvUndoCancel.setResult( "NG" );
            }

            gxRsvUndoCancel.setStatus( frm.getStatus() );
            gxRsvUndoCancel.setErrorCode( errCode );
            if ( errCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\�񂪂��������Ƃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5���ȑO�̏ꍇ�͑O���Ƃ���
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getRsvDate() == nowDate// �\����t�������̏ꍇ
                            || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* �\����t�������ŗ��X�\�莞����24���Ԉȓ��̏ꍇ */)
                    {
                        RsvList rl = new RsvList();
                        if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                        {
                            rl.sendToHost( hotelId );
                        }
                    }
                }
            }

            // XML�̏o��
            String xmlOut = gxRsvUndoCancel.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvUndoCancel xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvUndoCancel]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvUndoCancel]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * �\�񗈓X�m�F������s��
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvUndoFix(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        boolean ret = false;
        boolean undoResult = false;
        GenerateXmlHapiTouchRsvUndoFix gxRsvUndoFix;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        String paramRsvNo;
        String paramSeq;
        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        HotelCi hc = new HotelCi();
        HapiTouchBko bko = new HapiTouchBko();

        gxRsvUndoFix = new GenerateXmlHapiTouchRsvUndoFix();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramSeq = request.getParameter( "seq" );

        // �\��ԍ��̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        if ( (paramSeq == null) || (paramSeq.equals( "" ) != false) || (CheckString.numCheck( paramSeq ) == false) )
        {
            paramSeq = "0";
        }

        // �\��f�[�^��ǂ�
        int extFlag = 0;
        DataRsvReserve drr = new DataRsvReserve();
        if ( drr.getData( hotelId, paramRsvNo ) )
        {
            extFlag = drr.getExtFlag();
        }

        /*
         * HtCheckIn �ɂ��`�F�b�N�C�������ꍇ�́A�n�s�z�e�^�b�`PC�̗\��ڍׂł̓`�F�b�N�C���R�[�h���킩��Ȃ�
         * �̂ŗ\��No���`�F�b�N�C���R�[�h���擾����B
         */
        paramSeq = Integer.toString( getCheckInCode( hotelId, paramRsvNo ) );

        try
        {
            stream = response.getOutputStream();
            if ( (paramRsvNo.compareTo( "" ) != 0) && (Integer.parseInt( paramSeq ) > 0) )
            {
                // �t�H�[���ɃZ�b�g
                frm.setSelHotelId( hotelId );
                frm.setRsvNo( paramRsvNo );

                // �\��f�[�^���o
                logic.setFrm( frm );
                // ���уf�[�^�擾
                logic.getData( 2 );

                frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                frm.setErrMsg( "" );
                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );

                // �z�e���`�F�b�N�C���f�[�^���擾����
                ret = hc.getData( hotelId, Integer.parseInt( paramSeq ) );

                if ( ret != false )
                {
                    hc = htRsvSub.visitCancel( hotelId, hc, 1 );
                    if ( hc.getErrorMsg() > 0 )
                    {
                        errCode = hc.getErrorMsg();
                        ret = false;
                    }
                    else
                    {
                        /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                        hc.registTouchCi( hc.getHotelCi(), false );
                        /** �n�s�z�e�^�b�`�[���p�ɒǉ� **/
                    }
                }
                else
                {
                    ret = false;
                    errCode = HapiTouchErrorMessage.ERR_20905;
                }

                if ( ret != false )
                {
                    // �����ԍ����o�^����Ă���A���X�̃X�e�[�^�X�̃f�[�^�ŁA�`�F�b�N�C���f�[�^���쐬����Ă���ꍇ
                    if ( frm.getSeq() > 0 && frm.getStatus() == ReserveCommon.RSV_STATUS_ZUMI )
                    {
                        // �\��o�^�������[�U�[�̃��[���A�h���X�擾
                        UserBasicInfo ubi = new UserBasicInfo();
                        ubi.getUserBasic( frm.getUserId() );
                        frm.setLoginUserId( frm.getUserId() );
                        frm.setLoginUserMail( ubi.getUserInfo().getMailAddr() );
                        frm.setLoginUserTel( ubi.getUserInfo().getTel1() );
                        frm.setMail( ubi.getUserInfo().getMailAddr() );

                        DataRsvPlan dataPlan = new DataRsvPlan();
                        int offerKind;

                        // �z�e���̒񋟋敪�擾
                        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );
                        offerKind = dataPlan.getOfferKind();
                        frm.setOfferKind( offerKind );
                        frm.setPlanType( logic.getFrm().getPlanType() );

                        // �����X�̃L�����Z��
                        frm = htRsvSub.execUndoFix( frm, frm.getStatus() );

                        // �G���[���莞
                        if ( frm.getErrMsg().trim().length() != 0 )
                        {
                            errCode = HapiTouchErrorMessage.ERR_20904;
                        }
                        else
                        {
                            if ( hc.getHotelCi().getCiStatus() <= 2 && extFlag == 0 )
                            {
                                errCode = hc.getErrorMsg();
                                UserPointPayTemp uppt = new UserPointPayTemp();
                                UserPointPay upp = new UserPointPay();

                                undoResult = bko.cancelBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_YOYAKU, hc.getHotelCi() );
                                if ( undoResult == false )
                                {
                                    // �X�e�[�^�X���L�����Z���ȊO
                                    errCode = HapiTouchErrorMessage.ERR_20912;

                                }

                                // �n�s�z�e�\��̏ꍇ�͗\��}�C����������
                                undoResult = uppt.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                if ( undoResult == false )
                                {
                                    errCode = HapiTouchErrorMessage.ERR_20913;
                                }
                                // uppt�̃}�C�������������܂���������Aupp��������
                                if ( undoResult != false )
                                {
                                    undoResult = upp.cancelRsvPoint( hc.getHotelCi(), POINT_KIND_YOYAKU, 0 );
                                }
                            }
                            else
                            {
                                undoResult = true;
                            }
                        }
                    }
                    else
                    {
                        if ( frm.getSeq() <= 0 )
                        {
                            // �f�[�^�Ȃ�
                            errCode = HapiTouchErrorMessage.ERR_20902;
                        }
                        else
                        {
                            // �X�e�[�^�X���L�����Z���ȊO
                            errCode = HapiTouchErrorMessage.ERR_20911;
                        }
                    }
                }
                else
                {
                    undoResult = false;
                }
            }
            else
            {
                errCode = HapiTouchErrorMessage.ERR_20901;
                gxRsvUndoFix.setResult( "NG" );
            }

            if ( undoResult != false )
            {
                gxRsvUndoFix.setResult( "OK" );
            }
            else
            {
                gxRsvUndoFix.setResult( "NG" );
            }

            gxRsvUndoFix.setStatus( frm.getStatus() );
            gxRsvUndoFix.setErrorCode( errCode );

            if ( errCode == 0 )
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\��f�[�^�̍ŐV��Ԃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    RsvList rl = new RsvList();
                    if ( rl.getData( hotelId, 0, 0, 0, paramRsvNo, 0 ) != false )
                    {
                        rl.sendToHost( hotelId );
                    }
                }
            }

            // XML�̏o��
            String xmlOut = gxRsvUndoFix.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvUndoFix xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvUndoFix]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvUndoFix]Exception:" + e.toString() );
                }
            }
        }
    }

    /**
     * �\��ݒ�擾
     * 
     * @param hotelId �z�e��ID
     * @param response ���X�|���X
     **/
    public void getRsvConfig(int hotelId, HttpServletResponse response)
    {

        GenerateXmlRsvGetConfig gxTouch;
        ServletOutputStream stream = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        int manageDate = nowTime <= ReserveCommon.RSV_DEADLINE_TIME ? DateEdit.addDay( nowDate, -1 ) : nowDate;

        gxTouch = new GenerateXmlRsvGetConfig();

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();
            gxTouch.setManageDate( Integer.toString( manageDate ) );
            gxTouch.setDeadlineTime( Integer.toString( ReserveCommon.RSV_DEADLINE_TIME ) );

            // XML�̏o��
            String xmlOut = gxTouch.createXml();
            ServletOutputStream out = null;

            Logging.info( xmlOut );
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch getRsvConfig]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch getRsvConfig]Exception:" + e.toString() );
                }
            }
        }
    }

    /*****
     * �\�񕔉��ύX (modifyCi,corrctCi)
     * 
     * @param hotelId
     * @param paramRsvNo
     * @param paramRoomNo
     */

    public static void rsvRoomChange(int hotelId, String paramRsvNo, String paramRoomNo)
    {
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        LogicOwnerRsvRoomChange logicRoom = new LogicOwnerRsvRoomChange();
        String paramSeq = paramRoomNo;
        boolean errorFlag = false;
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        try
        {
            if ( (paramSeq != null) && (paramSeq.equals( "" ) == false) )
            {
                paramSeq = Integer.toString( getSeq( hotelId, paramSeq ) ); // �z�X�g�ʐM�p�������̂��n�s�z�e�̕����ԍ��ɕϊ�����
                // "0"�ŕԂ��Ă����ꍇ�́Ahh_hotel_room_more �̓o�^�ُ�Ƃ������ƂȂ̂ŏ��������Ȃ��B

                if ( paramSeq.equals( "0" ) == false )
                {
                    if ( checkChangeRoom( hotelId, paramRsvNo, paramSeq ) == false )
                    {
                        errorFlag = true; // �ύX��̕����������ς�
                    }
                    if ( (paramRsvNo.compareTo( "" ) != 0) && (Integer.parseInt( paramSeq ) > 0) )
                    {
                        // �t�H�[���ɃZ�b�g
                        frm.setSelHotelId( hotelId );
                        frm.setRsvNo( paramRsvNo );

                        // �\��f�[�^���o
                        logic.setFrm( frm );
                        // ���уf�[�^�擾
                        logic.getData( 2 );

                        // �A���̍ۂ́A���̏h�������`�F�b�N
                        if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                        {
                            if ( checkChangeRoomConsecutive( hotelId, frm.getRsvNoMain(), paramSeq ) == false )
                            {
                                errorFlag = true;// �ύX��̕����������ς�
                            }
                        }

                        frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                        frm.setErrMsg( "" );
                        frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                        frm.setMode( ReserveCommon.MODE_UPD );
                        frm.setOrgRsvSeq( frm.getSeq() );
                        if ( errorFlag == false )
                        {
                            frm.setSeq( Integer.parseInt( paramSeq ) );
                            frm.setRoomHold( 0 );
                        }
                        else
                        // �����ς�
                        {
                            frm.setRoomHold( Integer.parseInt( paramSeq ) ); // �ύX��̕����������ς݂̏ꍇ�͊m�ە����݂̂ɃZ�b�g����B
                        }
                        frm.setOrgRsvDate( frm.getRsvDate() );
                        frm.setPlanType( logic.getFrm().getPlanType() );

                        // ���X�҂�or���X�ς݂̃X�e�[�^�X�̂ݗL��
                        if ( frm.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE || frm.getStatus() == ReserveCommon.RSV_STATUS_ZUMI )
                        {
                            logicRoom.setFrm( frm );
                            logicRoom.updReserve();
                        }
                    }
                    // �d���̑��M����
                    // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\�񂪂��������Ƃ��z�X�g�ɓ`����
                    if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                    {
                        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                        // 5���ȑO�̏ꍇ�͑O���Ƃ���
                        if ( 50000 > nowTime )
                        {
                            nowTime = nowTime + 240000;
                            nowDate = DateEdit.addDay( nowDate, -1 );
                        }
                        if ( frm.getRsvDate() == nowDate// �\����t�������̏ꍇ
                                || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* �\����t�������ŗ��X�\�莞����24���Ԉȓ��̏ꍇ */)
                        {
                            RsvList rl = new RsvList();
                            // �v�m�F
                            if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                            {
                                rl.sendToHost( hotelId );
                            }
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
        }
    }

    /*****
     * �����ύX
     * 
     * @param hotelId
     * @param request
     * @param response
     */
    public void rsvRoomChange(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        GenerateXmlHapiTouchRsvRoomChange gxRsvChangeRoom;
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();
        LogicOwnerRsvRoomChange logicRoom = new LogicOwnerRsvRoomChange();
        String paramRsvNo = "";
        String paramSeq = "";
        String paramRoomNo = "";
        String paramCode = "";
        boolean isSyncReserve = ReserveCommon.isSyncReserve( hotelId ); // �\��A�������̔��f

        gxRsvChangeRoom = new GenerateXmlHapiTouchRsvRoomChange();

        paramRsvNo = request.getParameter( "rsvNo" );
        paramRoomNo = request.getParameter( "roomNo" );
        paramCode = request.getParameter( "code" );

        if ( paramCode != null && !paramCode.equals( "" ) )
        {
            isSyncReserve = false; // �^�b�`PC����̃A�N�Z�X�Ȃ̂ŁA�\�񊄓��ςݕ����ύX��NG
        }

        // �\��ԍ��̃`�F�b�N
        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }
        paramRsvNo = ReserveCommon.AdjustRsvNo( hotelId, paramRsvNo );

        try
        {
            if ( (paramRoomNo != null) && (paramRoomNo.equals( "" ) == false) )
            {
                paramSeq = Integer.toString( getSeq( hotelId, paramRoomNo ) );// �z�X�g�ʐM�p�������̂��n�s�z�e�̕����ԍ��ɕϊ�����
                // �^�b�`PC����̕������̂́Ahh_hotel_room_more.seq�����ł��邱�Ƃ�����
                // "0"�ŕԂ��Ă����ꍇ�́Ahh_hotel_room_more �̓o�^�ُ�Ƃ������ƂȂ̂Ńp�����[�^�G���[�Ƃ������ƁB

                if ( paramSeq.equals( "0" ) == false )
                {
                    if ( checkChangeRoom( hotelId, paramRsvNo, paramSeq ) == false )
                    {
                        errCode = HapiTouchErrorMessage.ERR_20807; // �ύX��̕����������ς�
                    }
                    if ( errCode == 0 || isSyncReserve ) // �\��A�������͕ύX��̕����������ς݂ł�OK�Ƃ���B
                    {
                        if ( (paramRsvNo.compareTo( "" ) != 0) && (Integer.parseInt( paramSeq ) > 0) )
                        {
                            // �t�H�[���ɃZ�b�g
                            frm.setSelHotelId( hotelId );
                            frm.setRsvNo( paramRsvNo );

                            // �\��f�[�^���o
                            logic.setFrm( frm );
                            // ���уf�[�^�擾
                            logic.getData( 2 );

                            // �A���̍ۂ́A���̏h�������`�F�b�N
                            if ( StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                            {
                                if ( checkChangeRoomConsecutive( hotelId, frm.getRsvNoMain(), paramSeq ) == false )
                                {
                                    errCode = HapiTouchErrorMessage.ERR_20807;// �ύX��̕����������ς�
                                }
                            }

                            if ( errCode == 0 || isSyncReserve )// �\��A�������͕ύX��̕����������ς݂ł�OK�Ƃ���B
                            {
                                frm.setMode( "" ); // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
                                frm.setErrMsg( "" );
                                frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
                                frm.setMode( ReserveCommon.MODE_UPD );
                                frm.setOrgRsvSeq( frm.getSeq() );
                                if ( errCode == 0 )
                                {
                                    frm.setSeq( Integer.parseInt( paramSeq ) );
                                    frm.setRoomHold( 0 );
                                }
                                else
                                {
                                    frm.setRoomHold( Integer.parseInt( paramSeq ) ); // �ύX��̕����������ς݂̏ꍇ�͊m�ە����݂̂ɃZ�b�g����B
                                }
                                frm.setOrgRsvDate( frm.getRsvDate() );
                                frm.setPlanType( logic.getFrm().getPlanType() );

                                // ���X�҂��̃X�e�[�^�X�̂ݗL��
                                if ( frm.getStatus() == ReserveCommon.RSV_STATUS_UKETUKE )
                                {
                                    //
                                    logicRoom.setFrm( frm );
                                    logicRoom.updReserve();

                                    // �G���[������������OK�Ƃ��ă��X�|���X���Z�b�g
                                    if ( frm.getErrMsg().trim().length() == 0 )
                                    {
                                        gxRsvChangeRoom.setRoomNo( paramSeq );
                                        gxRsvChangeRoom.setRoomName( this.getRoomName( hotelId, Integer.parseInt( paramSeq ) ) );
                                        gxRsvChangeRoom.setResult( "OK" );
                                        gxRsvChangeRoom.setStatusValue( frm.getStatus() );
                                        if ( frm.getStatus() == 1 )
                                        {
                                            gxRsvChangeRoom.setStatus( "���X�҂�" );
                                        }
                                        else if ( frm.getStatus() == 2 )
                                        {
                                            gxRsvChangeRoom.setStatus( "���X" );
                                        }
                                        else if ( frm.getStatus() == 3 )
                                        {
                                            if ( frm.getNoShow() == 1 )
                                            {
                                                gxRsvChangeRoom.setStatus( "\\�L�����Z��" );
                                            }
                                            else
                                            {
                                                gxRsvChangeRoom.setStatus( "�L�����Z��" );
                                            }
                                        }
                                    }
                                    else
                                    {
                                        errCode = HapiTouchErrorMessage.ERR_20806;
                                        Logging.info( "[HapiTouchRsv.rsvRoomChange()]:" + frm.getErrMsg() );
                                    }
                                }
                                else
                                {
                                    if ( frm.getSeq() <= 0 )
                                    {
                                        // �f�[�^�Ȃ�
                                        errCode = HapiTouchErrorMessage.ERR_20802;
                                    }
                                    else
                                    {
                                        // �X�e�[�^�X���L�����Z���ȊO
                                        errCode = HapiTouchErrorMessage.ERR_20803;
                                    }
                                }
                            }
                        }
                        else
                        {
                            errCode = HapiTouchErrorMessage.ERR_20801;
                        }
                    }
                }
                else
                {
                    errCode = HapiTouchErrorMessage.ERR_20801;
                }
            }
            else
            {
                errCode = HapiTouchErrorMessage.ERR_20801;
            }
            if ( errCode > 0 && !(isSyncReserve && errCode == HapiTouchErrorMessage.ERR_20807) )
            {
                gxRsvChangeRoom.setErrorCode( errCode );
                gxRsvChangeRoom.setResult( "NG" );
            }
            // �d���̑��M����
            if ( errCode == 0 || (isSyncReserve && errCode == HapiTouchErrorMessage.ERR_20807) ) // �ύX��̕��������̗\�񊄓��ς݂ł��m�ە������ύX�ɂȂ����̂Ńz�X�g�ɒm�点�܂��B
            {
                // IP�A�h���X���o�^����Ă���ꍇ�́A�z�X�g�A�������B�\�񂪂��������Ƃ��z�X�g�ɓ`����
                if ( !HotelIp.getFrontIp( hotelId, 1 ).equals( "" ) )
                {
                    int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                    int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                    // 5���ȑO�̏ꍇ�͑O���Ƃ���
                    if ( 50000 > nowTime )
                    {
                        nowTime = nowTime + 240000;
                        nowDate = DateEdit.addDay( nowDate, -1 );
                    }
                    if ( frm.getRsvDate() == nowDate// �\����t�������̏ꍇ
                            || (frm.getRsvDate() == DateEdit.addDay( nowDate, 1 ) && frm.getEstTimeArrival() < nowTime) /* �\����t�������ŗ��X�\�莞����24���Ԉȓ��̏ꍇ */)
                    {
                        RsvList rl = new RsvList();
                        // �v�m�F
                        if ( rl.getData( hotelId, frm.getRsvDate(), frm.getRsvDate(), 0, paramRsvNo, 1 ) != false )
                        {
                            rl.sendToHost( hotelId );
                        }
                    }
                }
            }
            if ( errCode != 0 )
            // �G���[���������Ă����ꍇ�ɂ̓G���[�����ɏ�������
            {
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( frm.getHotelNm() );
                daeh.setRoomName( paramRoomNo );
                daeh.setId( hotelId );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( frm.getCiSeq() );
                daeh.setUserId( frm.getUserId() );
                daeh.insertData();
            }

            // XML�̏o��
            String xmlOut = gxRsvChangeRoom.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.roomCange xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * �������̎擾
     * 
     * @param id
     * @param seq
     * @return
     */
    private String getRoomName(int id, int seq)
    {
        String roomName = "";
        boolean ret = false;
        DataHotelRoomMore dhrm;
        dhrm = new DataHotelRoomMore();

        ret = dhrm.getData( id, seq );
        if ( ret != false )
        {
            roomName = dhrm.getRoomNameHost();
        }

        return roomName;
    }

    /**
     * �����N���̎擾
     * 
     * @param id
     * @param seq
     * @return
     */
    private String getRankName(int id, String rsvNo)
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rankName = "";
        String query;
        try
        {

            query = "SELECT IFNULL(CASE pln.room_select_kind WHEN 3 THEN '�w�薳��' WHEN 2 THEN '�����w��' ELSE rank.rank_name END, '') AS rank_name";
            query += " FROM newRsvDB.hh_rsv_reserve rsv INNER JOIN newRsvDB.hh_rsv_plan pln";
            query += " ON rsv.id=pln.id AND rsv.plan_id=pln.plan_id AND rsv.plan_sub_id=pln.plan_sub_id";
            query += " LEFT JOIN hh_hotel_room_more rm ON rm.id = rsv.id AND rm.seq = rsv.seq";
            query += " LEFT JOIN hh_hotel_roomrank rank ON rank.id = rm.id AND rank.room_rank = rm.room_rank";
            query += " WHERE rsv.id=? AND rsv.reserve_no=?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsvNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    rankName = result.getString( "rank_name" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getRankName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return rankName;

    }

    /**
     * �z�e���`�F�b�N�C���f�[�^�擾
     * 
     * @param �\��No
     * @return ��������(�`�F�b�N�C���R�[�h)
     */
    private int getCheckInCode(int id, String rsv_no)
    {
        String status = "0,1,4";
        return getCheckInCode( id, rsv_no, status );
    }

    private int getCheckInCode(int id, String rsv_no, String status)
    {
        int ciCode = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT seq FROM hh_hotel_ci AS m " +
                " WHERE id = ? " +
                " AND rsv_no = ?" +
                " AND ci_status IN (" + status + ") " +
                " AND " +
                " NOT EXISTS (  " + // ����seq�ōő�l�̂��݂̂̂�ΏۂƂ���
                "  SELECT 1 " +
                " FROM hh_hotel_ci AS s " +
                "  WHERE m.id = s.id " +
                "  AND m.seq = s.seq " +
                "   AND m.sub_seq < s.sub_seq " +
                "  ) " +
                "  ORDER BY seq DESC " +
                "  LIMIT 0,1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, rsv_no );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ciCode = result.getInt( "seq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getCheckInCode] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ciCode);
    }

    /**
     * �����ԍ��擾
     * 
     * @param id
     * @param seq
     * @return
     */
    private static int getSeq(int id, String roomName)
    {
        int seq = 0;
        boolean ret = false;
        DataHotelRoomMore dhrm;
        dhrm = new DataHotelRoomMore();

        ret = dhrm.getData( id, roomName );
        if ( ret != false )
        {
            seq = dhrm.getSeq();
        }

        if ( seq == 0 && CheckString.numCheck( roomName ) )
        {
            seq = Integer.parseInt( roomName );
        }
        return seq;
    }

    /****
     * �\��̈ꗗ���擾����
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param response ���X�|���X
     */
    public void rsvDailyCount(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        FormOwnerRsvList dsp0; // form
        LogicOwnerRsvList lgLPC; // logic
        ServletOutputStream stream = null;
        Boolean ret = false;
        GenerateXmlHapiTouchRsvDailyCount gxRsvDaily;
        GenerateXmlHapiTouchRsvDailyCountSub gxRsvDailySub;

        String paramStartDate;
        String paramEndDate;

        dsp0 = new FormOwnerRsvList();
        lgLPC = new LogicOwnerRsvList();
        gxRsvDaily = new GenerateXmlHapiTouchRsvDailyCount();

        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );

        // �J�n���̃`�F�b�N
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }
        // �I�����̃`�F�b�N
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }

        // ���X�|���X���Z�b�g
        try
        {
            stream = response.getOutputStream();

            // �z�e��ID�擾
            dsp0.setSelHotelID( hotelId );
            dsp0.setDateFrom( DateEdit.getDate( 1 ) ); // �V�X�e�����t
            dsp0.setDateTo( DateEdit.getDate( 1 ) ); // �V�X�e�����t

            lgLPC.setDateFrom( paramStartDate );
            lgLPC.setDateTo( paramEndDate );
            lgLPC.setHotelId( hotelId );
            ret = lgLPC.getDailyCount( dsp0, RSV_KIND );
            if ( ret != false )
            {
                int i = 0;
                for( i = 0 ; i < lgLPC.getRsvDateValList().size() ; i++ )
                {
                    gxRsvDailySub = new GenerateXmlHapiTouchRsvDailyCountSub();
                    gxRsvDailySub.setRsvDateValue( lgLPC.getRsvDateValList().get( i ) );
                    gxRsvDailySub.setRsvCount( lgLPC.getRsvCountList().get( i ) );
                    gxRsvDaily.addData( gxRsvDailySub );
                }
            }
            else
            {
                gxRsvDaily.setErrorCode( 0 );
            }

            // XML�̏o��
            String xmlOut = gxRsvDaily.createXml();
            Logging.info( xmlOut, "HapiTouchRsv.rsvDailyCount xmlOut" );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch rsvData]Exception:" + e.toString() );
                }
            }

        }
    }

    /**
     * �\�񖢊��蓖�ĕ������擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
     */
    private void getSeqList(FormReserveSheetPC frm)
    {
        String seqTbl = "";
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        PreparedStatement prestateR = null;
        ResultSet result = null;
        ResultSet resultR = null;

        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> seqIds = new ArrayList<Integer>();

        try
        {
            // ���łɗ\�񊄂蓖�čς݂̕����ԍ����擾����B
            query = "SELECT seq FROM newRsvDB.hh_rsv_room_remainder" +
                    " WHERE id = ? AND cal_date = ? AND " +
                    " ( stay_status <> 1 " +
                    "   OR rest_status <> 1 " +
                    "   OR (stay_reserve_no <> '' AND stay_reserve_no <> ?) " +
                    "   OR (rest_reserve_no <> '' AND rest_reserve_no <> ?) " +
                    "   OR (stay_reserve_temp_no <> 0 AND stay_reserve_limit_day * 1000000 + stay_reserve_limit_time > ?) " +
                    "   OR (rest_reserve_temp_no <> 0 AND rest_reserve_limit_day * 1000000 + rest_reserve_limit_time > ?) " +
                    " ) ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getRsvDate() );
            prestate.setString( 3, frm.getRsvNo() );
            prestate.setString( 4, frm.getRsvNo() );
            prestate.setString( 5, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
            prestate.setString( 6, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
            result = prestate.executeQuery();

            while( result.next() )
            {
                seqIds.add( result.getInt( "seq" ) );
            }
            result.close();
            prestate.clearParameters();

            // �A���̏ꍇ�́A�A�����S�Ăɂ��Ď��s����
            if ( (frm.getRsvNoMain() != null) && (frm.getRsvNoMain().equals( "" ) == false) )
            {
                String que = "SELECT * FROM  newRsvDB.hh_rsv_reserve" +
                        " WHERE id = ? AND reserve_no_main = ? " +
                        " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";

                prestateR = connection.prepareStatement( que );
                prestateR.setInt( 1, frm.getSelHotelId() );
                prestateR.setString( 2, frm.getRsvNoMain() );
                resultR = prestateR.executeQuery();
                while( resultR.next() != false )
                {
                    prestate.setInt( 1, resultR.getInt( "id" ) );
                    prestate.setInt( 2, resultR.getInt( "reserve_date" ) );
                    prestate.setString( 3, resultR.getString( "reserve_no" ) );
                    prestate.setString( 4, resultR.getString( "reserve_no" ) );
                    prestate.setString( 5, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
                    prestate.setString( 6, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) );
                    result = prestate.executeQuery();

                    while( result.next() )
                    {
                        seqIds.add( result.getInt( "seq" ) );
                    }
                    result.close();
                    prestate.clearParameters();
                }

            }
            prestate.close();

            // �d���폜
            ArrayList<Integer> seqIdsTmp = new ArrayList<Integer>();
            for( int id : seqIds )
            {
                if ( !seqIdsTmp.contains( id ) )
                {
                    seqIdsTmp.add( id );
                }
            }
            // WHERE��쐬
            for( int i = 0 ; i < seqIdsTmp.size() ; i++ )
            {
                seqTbl += " AND seq NOT IN (" + String.valueOf( seqIdsTmp.get( i ) ) + ")";
            }

            // ���p�ς݂̕����ԍ��ȊO�̕������擾����B
            query = "SELECT seq FROM hh_hotel_room_more WHERE id = ? AND disp_flag = 1 AND seq <>0";
            if ( !seqTbl.equals( "" ) )
            {
                query += seqTbl;
            }
            query = query + " ORDER BY seq";

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );

            result = prestate.executeQuery();
            while( result.next() )
            {
                seqList.add( result.getInt( "seq" ) );
            }
            frm.setSeqList( seqList );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.getSeqList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( resultR );
            DBConnection.releaseResources( prestateR );
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �����ύX�̍ہA�ύX�敔�����\�񊄂蓖�čς݂��ǂ������`�F�b�N����
     * 
     * @param hotelId �z�e��ID
     * @param rsvNo �\��No
     * @param rsvDate �\���
     * @param paramSeq �ύX�敔��
     * @return �Ȃ�
     */
    private static boolean checkChangeRoom(int hotelId, String RsvNo, String paramSeq)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            // ���łɗ\�񊄂蓖�čς݂̕����ԍ����擾����B
            query = "SELECT stay_status,stay_reserve_no,stay_reserve_temp_no,stay_reserve_limit_day,stay_reserve_limit_time" +
                    ",rest_status,rest_reserve_no,rest_reserve_temp_no,rest_reserve_limit_day,rest_reserve_limit_time,cal_date FROM newRsvDB.hh_rsv_room_remainder" +
                    " WHERE id = ? AND cal_date = (SELECT reserve_date FROM newRsvDB.hh_rsv_reserve WHERE id = ? AND reserve_no = ?) AND seq= ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, hotelId );
            prestate.setString( 3, RsvNo );
            prestate.setString( 4, paramSeq );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                // ���l�̗\��NO���͂����Ă���Ζ��Ȃ�
                if ( result.getString( "stay_reserve_no" ).equals( RsvNo ) || result.getString( "rest_reserve_no" ).equals( RsvNo ) )
                {
                    ret = true;
                }
                // �h���A�x�e�A�ǂ��炩�ɗ\��ԍ��������Ă����fale
                else if ( !result.getString( "stay_reserve_no" ).equals( "" ) || !result.getString( "rest_reserve_no" ).equals( "" ) )
                {
                    // Logging.info( "[HapiTouchRsv.checkChangeRoom] stay_reserve_no=" + result.getString( "stay_reserve_no" ) );
                    ret = false;
                }
                // �������́A�ǂ��炩���X�e�[�^�X1�ȊO��������false
                else if ( result.getInt( "stay_status" ) != 1 || result.getInt( "rest_status" ) != 1 )
                {
                    // Logging.info( "[HapiTouchRsv.checkChangeRoom] stay_status=" + result.getInt( "stay_status" ) );
                    ret = false;
                }
                // �������́A�h���̉��\��̊����O��������false
                else if ( result.getInt( "stay_reserve_temp_no" ) != 0
                        && (double)result.getInt( "stay_reserve_limit_day" ) * 1000000 + (double)result.getInt( "stay_reserve_limit_time" ) > Double.parseDouble( DateEdit.getDate( 2 ) ) * 1000000 + Double.parseDouble( DateEdit.getTime( 1 ) ) )
                {
                    // Logging.info( "[HapiTouchRsv.checkChangeRoom] stay_reserve_limit_day=" + (double)result.getInt( "stay_reserve_limit_day" ) * 1000000 + (double)result.getInt( "stay_reserve_limit_time" ) );

                    ret = false;
                }
                // �������͋x�e�̉��\��̊����O��������false
                else if ( result.getInt( "rest_reserve_temp_no" ) != 0
                        && (double)result.getInt( "rest_reserve_limit_day" ) * 1000000 + (double)result.getInt( "rest_reserve_limit_time" ) > Double.parseDouble( DateEdit.getDate( 2 ) ) * 1000000 + Double.parseDouble( DateEdit.getTime( 1 ) ) )
                {
                    ret = false;
                }
                else
                {
                    ret = true;
                }

                /*
                 * if ( ret )
                 * {
                 * TimeCommon timeCommon = new TimeCommon();
                 * ret = timeCommon.isRsvChangeRoom( hotelId, result.getInt( "cal_date" ), RsvNo, Integer.parseInt( paramSeq ) );
                 * }
                 */
            }
            else
            {
                // Logging.info( "[HapiTouchRsv.checkChangeRoom] query=" + query );
                ret = false;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsv.checkChangeRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * �����ύX�̍ہA�ύX�敔�����\�񊄂蓖�čς݂��ǂ������`�F�b�N����(�A���p)
     * 
     * @param hotelId �z�e��ID
     * @param rsvNo �\��No
     * @param rsvDate �\���
     * @param paramSeq �ύX�敔��
     * @return �Ȃ�
     */
    private static boolean checkChangeRoomConsecutive(int hotelId, String RsvNoMain, String paramSeq)
    {

        boolean chkChange = true;
        Connection connection = null;
        PreparedStatement prestateR = null;
        String que = "SELECT * FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
        ResultSet resultR = null;
        try
        {
            connection = DBConnection.getConnection();
            prestateR = connection.prepareStatement( que );
            prestateR.setInt( 1, hotelId );
            prestateR.setString( 2, RsvNoMain );
            resultR = prestateR.executeQuery();

            if ( resultR != null )
            {
                while( resultR.next() != false )
                {
                    // �`�F�b�N
                    if ( checkChangeRoom( resultR.getInt( "id" ), resultR.getString( "reserve_no" ), paramSeq ) == false )
                    {
                        chkChange = false;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.execRaiten] Exception=" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( resultR, prestateR, connection );
        }
        return(chkChange);
    }
}
