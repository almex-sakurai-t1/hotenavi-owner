package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataUserHistory;
import jp.happyhotel.hotel.HotelCoupon;
import jp.happyhotel.hotel.HotelDetail;
import jp.happyhotel.others.GenerateXmlDetail;
import jp.happyhotel.others.GenerateXmlDetailHotel;
import jp.happyhotel.others.GenerateXmlDetailHotelCouponDetail;
import jp.happyhotel.others.GenerateXmlDetailHotelEquipsKind;
import jp.happyhotel.others.GenerateXmlDetailHotelGalleryCategory;
import jp.happyhotel.others.GenerateXmlDetailHotelGalleryCategoryDetail;
import jp.happyhotel.others.GenerateXmlDetailHotelPricesKind;
import jp.happyhotel.others.GenerateXmlDetailHotelRoomDetail;
import jp.happyhotel.others.GenerateXmlDetailHotelSite;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;
import jp.happyhotel.user.UserPoint;

/**
 * ホテル詳細クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.1 2011/10/19
 * 
 */

public class ActionApiHotelDetail extends BaseAction
{
    private final static String PREMIUM_INFO_URL = Url.getUrl() + "/phone/others/info_premium_app.jsp";
    private final static String PREMIUM_APP_URL  = "premium";

    /**
     * ホテル詳細情報（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int IPHONE = 0;
        final int ANDROID = 1;
        int errorCode = 0;
        String paramId;
        String paramMethod = null;
        DataUserHistory duh;
        UserLoginInfo uli;
        HotelDetail hotelDetail;
        UserPoint up;
        int appKind = IPHONE;
        String paramSponsorCd;
        SponsorData_M2 sd = new SponsorData_M2();

        // XML出力
        boolean ret = false;
        GenerateXmlHeader header = new GenerateXmlHeader();
        hotelDetail = new HotelDetail();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramId = request.getParameter( "hotel_id" );
            paramSponsorCd = request.getParameter( "sponsor" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramId == null || paramId.equals( "" ) != false || CheckString.numCheck( paramId ) == false )
            {
                paramId = "0";
            }

            if ( UserAgent.getUserAgentTypeString( request ).equals( "ada" ) != false )
            {
                appKind = ANDROID;
            }

            if ( paramSponsorCd != null && paramSponsorCd.equals( "" ) == false )
            {
                sd.setClickCountForSmart( Integer.parseInt( paramSponsorCd ) );
            }

            ret = hotelDetail.getData( Integer.parseInt( paramId ), uli, appKind );
            if ( ret != false )
            {
                if ( hotelDetail.getRank() > 1 )
                {
                    // hh_user_historyに履歴を追加する
                    if ( (uli.isMemberFlag() != false) && (uli.getUserInfo().getRegistStatus() == 9) )
                    {

                        // ユーザー履歴に追加
                        duh = new DataUserHistory();
                        duh.setUserId( uli.getUserInfo().getUserId() );
                        duh.setId( Integer.parseInt( paramId ) );
                        duh.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        duh.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        duh.setDispIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
                        duh.setDispUserAgent( UserAgent.getUserAgentTypeString( request ) );
                        duh.insertData();

                        // ホテル詳細閲覧ポイントを付与
                        up = new UserPoint();
                        up.setPointHotelDetail( uli.getUserInfo().getUserId(), Integer.parseInt( paramId ) );
                        up = null;
                    }
                    else
                    {
                        // 非ユーザー分履歴に追加（非ユーザーはキャリアごとに書き込み）
                        duh = new DataUserHistory();
                        duh.setUserId( UserAgent.getUserAgentTypeString( request ) );
                        duh.setId( Integer.parseInt( paramId ) );
                        duh.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        duh.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        duh.setDispIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
                        duh.setDispUserAgent( UserAgent.getUserAgentTypeString( request ) );
                        duh.insertData();
                    }
                }

                GenerateXmlDetailHotel hotel;
                hotel = new GenerateXmlDetailHotel();

                // 全ホテル共通
                hotel.setName( hotelDetail.getName() );
                hotel.setId( hotelDetail.getId() );
                hotel.setRank( hotelDetail.getRank() );
                hotel.setAddress( hotelDetail.getAddress() );
                hotel.setTel( hotelDetail.getTel() );

                // ライト以上
                if ( hotelDetail.getRank() >= 1 )
                {
                    // 18歳未満
                    hotel.setOver18( hotelDetail.getOver18() );
                    // PR
                    hotel.setPr( hotelDetail.getPr() );
                    // MAP CODE
                    hotel.setMapcode( hotelDetail.getMapcode() );
                    // アクセス
                    hotel.setAccess( hotelDetail.getAccess() );

                    if ( uli.isPaymemberFlag() != false )
                    {
                        if ( hotelDetail.getEmptyStatus() == 1 || hotelDetail.getEmptyStatus() == 2 )
                        {
                            // iPhoneは最新情報をセットしないと空室情報が表示されない
                            if ( hotelDetail.getRank() == 1 )
                            {
                                hotel.setMessage( "" );
                            }

                            // 空室情報
                            hotel.setEmpty( hotelDetail.getEmptyStatus() );
                            hotel.setEmptyStatus( hotelDetail.getEmptyStatus() );
                            hotel.setEmptyRoom( Integer.toString( hotelDetail.getEmptyRoomCount() ) );
                            hotel.setEmptyUpDate( hotelDetail.getEmptyUpdate() );

                            String time = "";
                            int nTime = 0;

                            nTime = hotelDetail.getEmptyUptime();
                            time = String.format( "%1$02d", nTime / 10000 ) + String.format( "%1$02d", nTime % 10000 / 100 );
                            hotel.setEmptyUpTime( time );
                        }
                    }
                    else
                    {
                        if ( hotelDetail.getEmptyStatus() == 1 || hotelDetail.getEmptyStatus() == 2 )
                        {
                            // 2015.03.20 プレミアムの誘導リンクを表示 tashiro
                            // if ( UserAgent.getUserAgentTypeString( request ).equals( "ada" ) != false )
                            // {
                            // 空室情報
                            hotel.setEmpty( hotelDetail.getEmptyStatus() );
                            hotel.setEmptyNoMemberText( hotelDetail.getNomemberText() );

                            // iPhone時にプレミアム紹介URLをセット
                            if ( UserAgent.getUserAgentTypeString( request ).equals( "ipa" ) != false )
                            {
                                hotel.setEmptyNoMemberUrl( PREMIUM_APP_URL );
                            }
                            else
                            {
                                hotel.setEmptyNoMemberUrl( PREMIUM_INFO_URL );
                            }

                            // }
                        }
                    }

                    // 全部屋数
                    hotel.setRoomAllCount( hotelDetail.getRoomAllCount() );

                    // 地図関連
                    hotel.setMap( "" );
                    hotel.setMapLat( hotelDetail.getLat() );
                    hotel.setMapLon( hotelDetail.getLon() );

                    // 料金追加
                    if ( hotelDetail.getHotelPriceName() != null )
                    {
                        if ( hotelDetail.getHotelPriceName().size() > 0 )
                        {
                            hotel.setHotelPrices( "" );
                            for( int i = 0 ; i < hotelDetail.getHotelPriceName().size() ; i++ )
                            {
                                GenerateXmlDetailHotelPricesKind priceKind = new GenerateXmlDetailHotelPricesKind();
                                // セットするメッセージを初期化
                                String priceMessage = "";
                                for( int j = 0 ; j < hotelDetail.getHotelPriceMessage().get( i ).size() ; j++ )
                                {
                                    priceMessage += hotelDetail.getHotelPriceMessage().get( i ).get( j );
                                }
                                priceKind.setName( hotelDetail.getHotelPriceName().get( i ) );
                                priceKind.setMessage( priceMessage );
                                if ( hotelDetail.getHotelPriceRemarks().get( i ) != null )
                                {
                                    priceKind.setRemarks( hotelDetail.getHotelPriceRemarks().get( i ) );
                                }
                                hotel.addPricesKind( priceKind );
                            }
                        }
                    }

                    // メンバー特典
                    hotel.setPrivileges( hotelDetail.getPrivilege() );
                    // ルームサービス
                    hotel.setRoomservice( hotelDetail.getRoomService() );
                    // その他サービス
                    hotel.setOtherservice( hotelDetail.getOtherService() );
                    // クレジット
                    hotel.setCredit( hotelDetail.getCredit() );
                    // 途中外出
                    hotel.setHalfway( hotelDetail.getHalfway() );
                    // 利用人数
                    hotel.setHeadcount( hotelDetail.getHeadCount() );
                    // 予約
                    hotel.setReserve( hotelDetail.getReserve() );
                    try
                    {
                        // 設備
                        if ( hotelDetail.getEquipName() != null )
                        {
                            if ( hotelDetail.getEquipName().size() > 0 )
                            {
                                hotel.setEquips( "" );
                                for( int i = 0 ; i < hotelDetail.getEquipName().size() ; i++ )
                                {
                                    GenerateXmlDetailHotelEquipsKind equip = new GenerateXmlDetailHotelEquipsKind();
                                    String equipMessage = "";

                                    try
                                    {
                                        for( int j = 0 ; j < hotelDetail.getEquipMessage().get( i ).size() ; j++ )
                                        {
                                            equipMessage += hotelDetail.getEquipMessage().get( i ).get( j );
                                            if ( j + 1 < hotelDetail.getEquipMessage().get( i ).size() )
                                            {
                                                equipMessage += ", ";
                                            }
                                        }
                                    }
                                    catch ( Exception e )
                                    {
                                        Logging.info( "[ActionApiHotelDetail overLight9:]id:" + paramId + "," + e.toString() );
                                        Logging.info( "[ActionApiHotelDetail overLight9:]NameSize:" + hotelDetail.getEquipName().size() );
                                        Logging.info( "[ActionApiHotelDetail overLight9:]MessageSize:" + hotelDetail.getEquipMessage().size() );
                                        Logging.info( "[ActionApiHotelDetail overLight9:]RemarksSize:" + hotelDetail.getEquipRemarks().size() );
                                    }

                                    equip.setName( hotelDetail.getEquipName().get( i ) );
                                    equip.setMessage( equipMessage + hotelDetail.getEquipRemarks().get( i ) );
                                    hotel.addEquipsKind( equip );
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.info( "[ActionApiHotelDetail overLight10:]id:" + paramId + "," + e.toString() );
                    }

                }
                // スタンダード以上
                if ( hotelDetail.getRank() >= 2 )
                {
                    // メッセージ
                    String message = hotelDetail.getMessage();
                    message = message.replaceAll( "</br>", "" );
                    hotel.setMessage( message );
                    // ホテル外観画像
                    hotel.setImage( hotelDetail.getImage() );
                    // クチコミ
                    if ( hotelDetail.getKuchikomiAvg().equals( "" ) == false )
                    {
                        hotel.setKuchikomiAvg( hotelDetail.getKuchikomiAvg() );
                    }
                    // 部屋
                    hotel.setRoom( "" );

                    int count = 0;
                    // 部屋詳細
                    for( int i = 0 ; i < hotelDetail.getRoomCount() ; i++ )
                    {
                        GenerateXmlDetailHotelRoomDetail roomDetail = new GenerateXmlDetailHotelRoomDetail();
                        if ( (hotelDetail.getRoomName()[i] != null) && (hotelDetail.getRoomImage()[i] != null) && (hotelDetail.getRoomText()[i] != null) )
                        {
                            count++;
                            roomDetail.setName( hotelDetail.getRoomName()[i] );
                            roomDetail.setImage( hotelDetail.getRoomImage()[i] );
                            roomDetail.setText( hotelDetail.getRoomText()[i] );
                            roomDetail.setTextUrl( hotelDetail.getRoomTextUrl()[i] );
                            hotel.addRoomDateil( roomDetail );
                        }
                    }

                    // 有料会員以外で部屋データが１件以上あった場合
                    if ( hotelDetail.getRoomCount() > 0 && uli.isPaymemberFlag() == false )
                    {

                        GenerateXmlDetailHotelRoomDetail roomDetail = new GenerateXmlDetailHotelRoomDetail();
                        roomDetail.setName( "プレミアムコース紹介" );
                        roomDetail.setImage( "/common/images/app_info_premium_room.jpg" );
                        roomDetail.setText( "プレミアムコースはホテルの空室情報や全室画像を閲覧できるサービスです。詳しくはホテル詳細内にある「ハピホテプレミアムコースのご案内」をご覧ください" );

                        // iPhone時にプレミアム紹介URLをセット
                        if ( UserAgent.getUserAgentTypeString( request ).equals( "ipa" ) != false )
                        {
                            roomDetail.setTextUrl( PREMIUM_APP_URL );
                        }
                        else
                        {
                            roomDetail.setTextUrl( PREMIUM_INFO_URL );
                        }
                        hotel.addRoomDateil( roomDetail );

                    }
                    hotel.setRoomCount( count );

                    // ギャラリー
                    if ( hotelDetail.getHotelGallery() != null && hotelDetail.getGalleryName().size() > 0 )
                    {
                        // 中のデータがなかったらタグをセットしない
                        if ( hotelDetail.getGalleryName().get( 0 ).size() > 0 )
                        {
                            hotel.setGallery( "" );
                            for( int i = 0 ; i < hotelDetail.getGalleryCount().size() ; i++ )
                            {
                                GenerateXmlDetailHotelGalleryCategory category = new GenerateXmlDetailHotelGalleryCategory();
                                category.setName( hotelDetail.getHotelGallery().get( i ).getCategoryName() );
                                category.setCount( hotelDetail.getGalleryCount().get( i ) );
                                // detailタグを追加
                                for( int j = 0 ; j < hotelDetail.getGalleryName().get( i ).size() ; j++ )
                                {
                                    GenerateXmlDetailHotelGalleryCategoryDetail categoryDetail = new GenerateXmlDetailHotelGalleryCategoryDetail();
                                    categoryDetail.setName( hotelDetail.getGalleryName().get( i ).get( j ) );
                                    categoryDetail.setImage( hotelDetail.getGalleryImage().get( i ).get( j ) );
                                    categoryDetail.setText( hotelDetail.getGalleryText().get( i ).get( j ) );
                                    categoryDetail.setTextUrl( hotelDetail.getGalleryTextUrl().get( i ).get( j ) );
                                    category.addDetail( categoryDetail );
                                }
                                hotel.addGalleryCategor( category );
                            }
                        }
                    }
                    // 駐車場
                    hotel.setParking( hotelDetail.getParking() );

                    // URL追加
                    if ( hotelDetail.getUrl() != null )
                    {
                        for( int i = 0 ; i < hotelDetail.getUrl().size() ; i++ )
                        {
                            GenerateXmlDetailHotelSite site = new GenerateXmlDetailHotelSite();
                            site.setUrl( hotelDetail.getUrl().get( i ) );
                            site.setImage( hotelDetail.getUrlText().get( i ) );
                            hotel.addSite( site );
                        }
                    }

                    // クーポン
                    if ( uli.isMemberFlag() != false )
                    {
                        if ( hotelDetail.getCouponText() != null )
                        {
                            if ( hotelDetail.getCouponText().size() > 0 )
                            {
                                hotel.setCoupon( "" );
                                hotel.setCouponImage( hotelDetail.getCouponImage() );
                                for( int i = 0 ; i < hotelDetail.getCouponText().size() ; i++ )
                                {
                                    GenerateXmlDetailHotelCouponDetail coupon = new GenerateXmlDetailHotelCouponDetail();
                                    coupon.setText( hotelDetail.getCouponText().get( i ) );
                                    if ( hotelDetail.getCouponCondition().get( i ) != null )
                                    {
                                        coupon.setCondition( hotelDetail.getCouponCondition().get( i ) );
                                    }
                                    hotel.addCouponDetail( coupon );
                                }
                                hotel.setCouponNo( hotelDetail.getCouponNo() );
                                hotel.setCouponCommonCondition( hotelDetail.getCouponCommonCondition() );
                                hotel.setCouponPeriod( hotelDetail.getCouponPeriod() );
                                hotel.setCouponIssuance( hotelDetail.getCouponIssuance() );
                                hotel.setCouponMobileCondition( hotelDetail.getCouponMobileCondition() );
                            }
                        }
                    }
                    else
                    {
                        // 非会員のクーポンを確認する
                        HotelCoupon coupon;
                        coupon = new HotelCoupon();
                        // クーポンを取得する
                        ret = coupon.getMobileCouponData( Integer.parseInt( paramId ), 0 );
                        if ( ret != false && coupon.getHotelCoupon().getDispMobile() == 1 )
                        {
                            // siteにクーポンのURLを追加する
                            GenerateXmlDetailHotelSite site = new GenerateXmlDetailHotelSite();
                            site.setUrl( Url.getUrl() + "/phone/search/coupon_distinction.jsp?hotel_id=" + hotelDetail.getId() );
                            site.setImage( "クーポン発行" );
                            hotel.addSite( site );
                        }
                    }
                    // プレミアム会員登録
                    // アンドロイドのみ見せることになった場合
                    if ( UserAgent.getUserAgentTypeString( request ).equals( "ada" ) != false )
                    {
                        if ( uli.isPaymemberFlag() == false )
                        {
                            // siteにクーポンのURLを追加する
                            GenerateXmlDetailHotelSite site = new GenerateXmlDetailHotelSite();
                            site.setUrl( PREMIUM_INFO_URL );
                            site.setImage( "プレミアムコースのご案内" );
                            hotel.addSite( site );

                        }
                    }
                    hotel.setCustomId( hotelDetail.getCustomId() );

                }
                // ハピー加盟店以上
                if ( hotelDetail.getRank() >= 3 )
                {
                    // 加盟店状況をセット
                    hotel.setMemberImg( hotelDetail.getMemberImg() );
                    hotel.setMemberUrl( hotelDetail.getMemberUrl() );
                    hotel.setMemberText( hotelDetail.getMemberText() );

                    // ホテル予約フラグ
                    if ( hotelDetail.getRsvFlag() == 1 )
                    {
                        hotel.setRsvFlag( hotelDetail.getRsvFlag() );
                    }
                    else
                    {
                        hotel.setRsvFlag( 0 );
                    }
                    // ハピーPR
                    hotel.setHappiePr( hotelDetail.getHappiePr() );
                }

                // 詳細ヘッダーの情報をセット
                GenerateXmlDetail detail = new GenerateXmlDetail();
                detail.setError( "" );
                detail.setErrorCode( errorCode );
                detail.addHotel( hotel );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( "ホテル詳細" );
                header.setCount( 1 );
                // ホテル詳細を追加
                header.setDetail( detail );
            }
            else
            {
                // エラーを出力
                GenerateXmlDetail detail = new GenerateXmlDetail();
                detail.setError( Constants.ERROR_MSG_API6 );
                detail.setErrorCode( Constants.ERROR_CODE_API6 );

                // 検索結果ヘッダ作成
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setName( "ホテル詳細" );
                header.setCount( 0 );
                // ホテル詳細を追加
                header.setDetail( detail );
            }

            // 出力をヘッダーから
            String xmlOut = header.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiHotelDetail ]Exception:" + exception.toString() );

            // エラーを出力
            GenerateXmlDetail detail = new GenerateXmlDetail();
            detail.setError( Constants.ERROR_MSG_API10 );
            detail.setErrorCode( Constants.ERROR_CODE_API10 );

            // 検索結果ヘッダ作成
            header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            header.setMethod( paramMethod );
            header.setName( "ホテル詳細" );
            header.setCount( 0 );
            // ホテル詳細を追加
            header.setDetail( detail );

            String xmlOut = header.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiHotelDetail response]Exception:" + e.toString() );
            }
        }
        finally
        {
        }
    }
}
