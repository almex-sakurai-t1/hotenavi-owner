package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApContentsConfig;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApHotelCustom;
import jp.happyhotel.data.DataApHotelSetting;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.dto.DtoApCommon;
import jp.happyhotel.dto.DtoApHotelCustomerData;
import jp.happyhotel.dto.DtoApInquiryForm;
import jp.happyhotel.dto.DtoApMemberCardReg;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.others.HapiTouchErrorMessage;
import jp.happyhotel.touch.MemberRegist;

/**
 * ハピホテアプリチェックインクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2014/08/26
 * 
 */

public class ActionHtRegistCustom extends BaseAction
{
    final int                      TIMEOUT     = 3000;
    final int                      PORT_NO     = 7023;
    final int                      RESULT_OK   = 1;
    final int                      RESULT_NG   = 2;
    final int                      RETRY_COUNT = 3;
    private RequestDispatcher      requestDispatcher;
    private DtoApCommon            apCommon;
    private DtoApMemberCardReg     apMemberCardReg;
    private DtoApInquiryForm       apInquiry;
    private DtoApHotelCustomerData apHotelCustomerData;
    private DataApContentsConfig   config      = new DataApContentsConfig();
    private final int              TIME        = 1;
    private final int              DATE        = 2;

    /**
     * ハピホテタッチ
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        // XML出力
        int i = 0;
        boolean ret = false;
        boolean retMemberOperation = false;
        boolean boolHotelCustom = false;// ホテル顧客
        boolean boolCardRegFlag = false;// カード登録
        boolean boolHotenaviContract = false;// ホテナビ契約
        boolean boolDuplicationRegFlag = false;// カード重複登録
        boolean boolResult = false;// カード登録結果
        boolean boolConnect = false;// 接続結果
        String paramId;
        String paramSeq;
        String paramGoodsCode;
        String paramGoodsPrice;
        String userId = "";
        String hotenaviIp = "";
        String contents = "";
        int roomNo = 0;
        int errorCode = 0;
        HotelCi hc = null;
        DataHotelBasic dhb = null;
        DataApHotelCustom dahc = null;
        DataApHotelSetting dahs = null;
        DataHotelRoomMore dhrm = null;
        MemberRegist mr = new MemberRegist();

        // 自動採番のため、ここだけintを使う
        int customId = 0;
        String securityCode = "";
        String hotelUserId = "";
        String hotelPassword = "";
        String errorMsg = "";
        try
        {

            // 前ページからの引継ぎ
            paramId = request.getParameter( "id" );
            paramSeq = request.getParameter( "seq" );
            paramGoodsCode = request.getParameter( "goodsCode" );
            paramGoodsPrice = request.getParameter( "goodsPrice" );
            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }
            if ( paramSeq == null || paramSeq.equals( "" ) != false || CheckString.numCheck( paramSeq ) == false )
            {
                paramSeq = "0";
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
            if ( Integer.parseInt( paramId ) > 0 && Integer.parseInt( paramSeq ) > 0 )
            {
                ret = dhb.getData( Integer.parseInt( paramId ) );
                boolHotelCustom = dahs.getData( Integer.parseInt( paramId ) );

                hc = new HotelCi();
                hc.getData( Integer.parseInt( paramId ), Integer.parseInt( paramSeq ) );
                // データが取得できて、ユーザIDがあれば新規会員登録を行う。
                if ( hc.getHotelCi() != null && hc.getHotelCi().getUserId().equals( "" ) == false )
                {
                    dahc = new DataApHotelCustom();

                    // ホテルのフロントIPを取得
                    hotenaviIp = HotelIp.getHotenaviIp( Integer.parseInt( paramId ) );
                    userId = hc.getHotelCi().getUserId();
                    dhrm.getData( Integer.parseInt( paramId ), hc.getHotelCi().getRoomNo() );
                    // 既にメンバー登録ずみかどうかをチェック
                    ret = dahc.getData( Integer.parseInt( paramId ), userId );
                    // データがない、または削除済みユーザの場合
                    if ( ret == false || dahc.getDelFlag() == 1 )
                    {
                        customId = dahs.getMaxCustomId( Integer.parseInt( paramId ) );
                        // メンバー未登録の場合にホストへ新規登録顧客番号を通知する。
                        if ( customId > 0 )
                        {
                            // ホストへ3回リトライさせる。
                            for( i = 0 ; i < RETRY_COUNT ; i++ )
                            {
                                /** (1044)メンバー新規登録 **/
                                mr.setMemberId( Integer.toString( customId ) );
                                mr.sendToHost( hotenaviIp, TIMEOUT, PORT_NO, paramId );
                                if ( mr.getResult() == RESULT_OK )
                                {
                                    boolConnect = true;
                                    boolCardRegFlag = true;
                                    securityCode = mr.getSecurityCode();
                                    Logging.info( "securityCode:" + securityCode );
                                    break;
                                }
                                // 繰り返す場合は、顧客番号を振りなおす。
                                customId++;
                            }

                            // メンバーの登録結果がOKだったら登録
                            if ( boolConnect != false )
                            {
                                if ( boolCardRegFlag != false )
                                {
                                    // ap_hotel_customとap_hotel_settingに書き込み
                                    Logging.info( "HH_customId:" + customId + ", host_customId:" + mr.getMemberId() );

                                    dahc.setId( Integer.parseInt( paramId ) );
                                    dahc.setUserId( userId );
                                    // ホストから返ってきたメンバーIDをセット
                                    dahc.setCustomId( mr.getMemberId() );
                                    dahc.setSecurityCode( securityCode );
                                    dahc.setHotelUserId( hotelUserId );
                                    dahc.setHotelPassword( hotelPassword );
                                    dahc.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                    dahc.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                    dahc.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                    dahc.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                    dahc.setRegistStatus( 0 );
                                    dahc.setDelFlag( 0 );
                                    dahc.setAutoFlag( 1 ); // 自動発行
                                    if ( ret == false )
                                    {
                                        boolCardRegFlag = dahc.insertData();
                                    }
                                    else
                                    {
                                        boolCardRegFlag = dahc.updateData( Integer.parseInt( paramId ), userId );
                                    }
                                    if ( boolCardRegFlag == false )
                                    {
                                        errorCode = HapiTouchErrorMessage.ERR_30404;
                                    }
                                }
                                else
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_30402;
                                }
                            }
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_30405;
                            }
                            // ap_hotel_settingは必ず更新する。
                            dahs.setId( Integer.parseInt( paramId ) );
                            dahs.setAutoCustomId( customId );
                            boolResult = dahs.updateData( Integer.parseInt( paramId ) );
                        }
                    }
                    else
                    {
                        customId = Integer.parseInt( dahc.getCustomId() );
                        if ( dahc.getRegistStatus() == 0 )
                        {
                            boolCardRegFlag = true;
                        }
                        else
                        {
                            boolCardRegFlag = false;
                            errorCode = HapiTouchErrorMessage.ERR_30403;
                            errorMsg = "既にメンバー登録済みです。";
                        }
                    }
                }
            }
            // メンバーIDの採番がうまくいったら
            if ( boolCardRegFlag != false )
            {
                // if( config.getData( contentsId, contentsSub, id, contentsSeq ))
                if ( config.getDataCommon( "MemberCard.jsp", 0, Integer.parseInt( paramId ), 0 ) )
                {
                    contents = config.getContents();
                }

                apCommon = new DtoApCommon();
                apMemberCardReg = new DtoApMemberCardReg();
                apHotelCustomerData = new DtoApHotelCustomerData();

                // 共通設定
                apCommon.setId( Integer.parseInt( paramId ) );
                apCommon.setHotelName( dhb.getName() );
                apCommon.setRoomNo( dhrm.getRoomNameHost() );
                apCommon.setSeq( Integer.parseInt( paramSeq ) );

                // ホテル顧客
                apMemberCardReg.setApCommon( apCommon );
                apMemberCardReg.setCustomId( Integer.toString( customId ) );
                apMemberCardReg.setHotenaviContract( boolHotenaviContract );
                apMemberCardReg.setResult( boolCardRegFlag );
                apMemberCardReg.setGoodsCode( Integer.parseInt( paramGoodsCode ) );
                apMemberCardReg.setGoodsPrice( Integer.parseInt( paramGoodsPrice ) );

                // ホテル顧客データ
                apHotelCustomerData.setApCommon( apCommon );
                apHotelCustomerData.setCustomId( Integer.toString( customId ) );
                apHotelCustomerData.setContents( contents );

                request.setAttribute( "DtoApCommon", apCommon );
                request.setAttribute( "DtoApMemberCardReg", apMemberCardReg );
                request.setAttribute( "DtoApHotelCustomerData", apHotelCustomerData );

                // requestDispatcher = request.getRequestDispatcher( "MemberCardRegForm.jsp" );
                requestDispatcher = request.getRequestDispatcher( "MemberCard.jsp" );
                requestDispatcher.forward( request, response );
            }
            // 失敗したら問合せ画面へ
            else
            {
                if ( errorCode == 0 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_30406;
                }

                apCommon = new DtoApCommon();
                // 共通設定
                apCommon.setId( Integer.parseInt( paramId ) );
                apCommon.setHotelName( dhb.getName() );
                apCommon.setRoomNo( dhrm.getRoomNameHost() );
                apCommon.setSeq( Integer.parseInt( paramSeq ) );
                apCommon.setErrorCode( errorCode );
                apCommon.setErrorMessage( errorMsg );

                // エラー内容を登録
                DataApErrorHistory daeh = new DataApErrorHistory();
                daeh.setErrorCode( errorCode );
                daeh.setErrorSub( 0 );
                daeh.setHotelName( dhb.getName() );
                daeh.setRoomName( dhrm.getRoomNameHost() );
                daeh.setId( Integer.parseInt( paramId ) );
                daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                daeh.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                daeh.setTouchSeq( hc.getHotelCi().getSeq() );
                daeh.setUserId( userId );
                daeh.insertData();

                request.setAttribute( "DtoApCommon", apCommon );
                requestDispatcher = request.getRequestDispatcher( "common.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "HtRegistCustom:" + exception );
        }
        finally
        {
        }
    }
}
